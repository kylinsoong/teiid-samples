package org.teiid.test.cache;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.teiid.adminapi.Model.Type;
import org.teiid.adminapi.impl.ModelMetaData;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.runtime.EmbeddedServer.ConnectionFactoryProvider;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.jdbc.mysql.MySQL5ExecutionFactory;

public class EmbeddedServerWithMaterializedView {

	private String teiidModel = "teiidModel";
	private String globalVDB = "globalVDB";
	private String mysql5 = "mysql5";
	
	public String getUrl_MYSQL(String ip, Integer port, String catalog) {
		return "jdbc:mysql://" + ip + ":" + port + "/" + catalog + "?autoReconnect=true";
	}
	
	protected DataSource createMysqlDataSource(String ip, Integer port, String catalog, String user, String pw) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl(getUrl_MYSQL(ip, port, catalog));
		dataSource.setUsername(user);
		dataSource.setPassword(pw);
		dataSource.setValidationQuery("select now()");
		dataSource.setRemoveAbandoned(true);
		dataSource.setRemoveAbandonedTimeout(60);
		//
		return dataSource;
	}
	
	private String getDS(String catalog) {
		return catalog + "-ds";
	}
	
	private ModelMetaData createModelMetaData(String serviceName, String translator) {
		ModelMetaData modelMetaData = new ModelMetaData();
		modelMetaData.setName(teiidModel);
		modelMetaData.addSourceMapping(serviceName, translator, getDS(serviceName));
		return modelMetaData;
	}
	
	public ModelMetaData deploy_MYSQL5(EmbeddedServer ebdServer, String ip, Integer port, String catalog, String user, String pw) throws Exception {
		DataSource mysqlDataSource = createMysqlDataSource(ip, port, catalog, user, pw);
		ConnectionFactoryProvider<DataSource> mysqlCfp = new EmbeddedServer.SimpleConnectionFactoryProvider<DataSource>(mysqlDataSource);
		ebdServer.addConnectionFactoryProvider(getDS(catalog), mysqlCfp);
		ModelMetaData mysqlModelMetaData = createModelMetaData(catalog, mysql5);
		return mysqlModelMetaData;
	}
	
	public EmbeddedServer initEmbeddedServer() throws TranslatorException {
		EmbeddedServer ebdServer = new EmbeddedServer();
		// init Translator
		MySQL5ExecutionFactory executionFactory = new MySQL5ExecutionFactory();
		executionFactory.start();
		ebdServer.addTranslator(mysql5, executionFactory);
		// start the Server
		EmbeddedConfiguration embeddedConfiguration = new EmbeddedConfiguration();
		embeddedConfiguration.setUseDisk(true);
		ebdServer.start(embeddedConfiguration);
		//
		return ebdServer;
	}
	
	private ModelMetaData createVirtualModel() {
		ModelMetaData mmd1 = new ModelMetaData();
		mmd1.setName("virt");
		mmd1.setModelType(Type.VIRTUAL);
		mmd1.setSchemaSourceType("ddl");
		//
		//
		mmd1.setSchemaText("create view \"myView\" (  testName varchar(45) ,  testTitle varchar(45) ) OPTIONS " + //
				" (MATERIALIZED 'TRUE' ,  " + //
				" UPDATABLE 'TRUE' , " + //
				" MATERIALIZED_TABLE '" + teiidModel + ".teiidtest.testView',  " + //
				"\"teiid_rel:MATVIEW_STATUS_TABLE\" '" + teiidModel + ".teiidtest.status'  , " + //
				"\"teiid_rel:MATERIALIZED_STAGE_TABLE\" '" + teiidModel + ".teiidtest.myViewTable_staging'," + //
				"\"teiid_rel:ALLOW_MATVIEW_MANAGEMENT\" 'true' ,  " + //
				"\"teiid_rel:MATVIEW_BEFORE_LOAD_SCRIPT\" 'execute pg.native(''truncate table mat_actor_staging'');', " + //
				"\"teiid_rel:MATVIEW_AFTER_LOAD_SCRIPT\" 'execute pg.native(''ALTER TABLE myViewTable RENAME TO myViewTable_temp;ALTER TABLE myViewTable_staging RENAME TO myViewTable;ALTER TABLE myViewTable_temp RENAME TO myViewTable_staging;'')', " + //
				"\"teiid_rel:MATVIEW_SHARE_SCOPE\" 'NONE' ,  " + //
				"\"teiid_rel:MATVIEW_TTL\" '20' ,  " + //
				"\"teiid_rel:MATVIEW_ONERROR_ACTION\" 'THROW_EXCEPTION' " + //
				")  as  SELECT  testName,  testTitle FROM  testtable ");
		return mmd1;
	}
	
	public void run() throws Exception {
		
		EmbeddedServer ebdServer = initEmbeddedServer();
		ModelMetaData metaData = deploy_MYSQL5(ebdServer, "localhost", 3306, "customer", "jdv_user", "jdv_pass");
		ebdServer.deployVDB(globalVDB, metaData, createVirtualModel());
		//
		Driver driver = ebdServer.getDriver();
		Connection conn = driver.connect("jdbc:teiid:" + globalVDB, null);
		//
		String cmd1 = "SELECT * from testtable   ";
		executeQuery(conn, cmd1);
		
		//
		String cmd = "SELECT * from testView ";
		executeQuery(conn, cmd);
 		//
	}
	
	private static void executeQuery(Connection conn, String cmd) {
		System.out.println("-------------------------------------------------------------------------------------------");
		try {
			Statement stmt = conn.createStatement();
			stmt.executeQuery(cmd);
			ResultSet resultSet = stmt.getResultSet();
			ResultSetMetaData meta = resultSet.getMetaData();
			while (resultSet.next()) {
				String row = "";
				for (int j = 1; j <= meta.getColumnCount(); j++) {
					Object fieldValue = resultSet.getObject(j);
					row += " [" + meta.getColumnLabel(j) + " = ";
					row += fieldValue == null ? "null] " : fieldValue.toString() + "] ";
				}
				System.out.println(row);
			}
			stmt.close();
		} catch (Exception e) {
			
			System.err.println(cmd + "\n\t  ERROR: ." + e.getMessage());
		}
		System.out.println("-------------------------------------------------------------------------------------------");
	}
	
	public static void main(String[] args) throws Exception {
		
		EmbeddedServerWithMaterializedView test = new EmbeddedServerWithMaterializedView();
		test.run();
		
	}
	

}
