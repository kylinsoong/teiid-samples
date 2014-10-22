package com.jboss.teiid;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.Context;

import org.teiid.client.plan.PlanNode;
import org.teiid.jdbc.TeiidStatement;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.ExecutionFactory;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.jdbc.mysql.MySQL5ExecutionFactory;

import com.jboss.teiid.client.util.JDBCUtil;

import bitronix.tm.resource.jdbc.PoolingDataSource;

/*
 * CREATE TABLE SERIALTEST(id BIGINT, col_a CHAR(12), col_b CHAR(40), col_c CHAR(40));
 * 
 * SELECT sum(table_rows) from information_schema.TABLES WHERE table_name = 'SERIALTEST';
 * SELECT sum(data_length) from information_schema.TABLES WHERE table_name = 'SERIALTEST';
 * 
 * mvn clean install dependency:copy-dependencies
 * 
 * java -cp target/teiid-jdbc-client-1.0-SNAPSHOT.jar:target/dependency/* -Xms5096m -Xmx5096m -XX:MaxPermSize=512m -verbose:gc -Xloggc:gc.logXX:+PrintGCDetails -XX:+PrintGCDateStamps com.jboss.teiid.SerializationCaculation plan -s 1000000000
 * 
 * 
 */
public class SerializationCaculation {

	static {
		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "bitronix.tm.jndi.BitronixInitialContextFactory");
	}
	
	static PoolingDataSource pds = null;
	static EmbeddedServer server = null;
	static Connection conn = null;
	
	public SerializationCaculation() throws Exception {
		init();
	}

	public void init() throws Exception {
		setupDataSource();
		init("translator-mysql", new MySQL5ExecutionFactory());
		server.start(new EmbeddedConfiguration());
		server.deployVDB(new FileInputStream(new File("mysql-vdb.xml")));
		conn = server.getDriver().connect("jdbc:teiid:MysqlVDB", null);
	}
	
	private void init(String name, ExecutionFactory<?, ?> factory) throws TranslatorException {
		server = new EmbeddedServer();
		factory.start();
		factory.setSupportsDirectQueryProcedure(true);
		server.addTranslator(name, factory);
	}

	private void setupDataSource() {
		if (null != pds)
			return;
		
		pds = new PoolingDataSource();
		pds.setUniqueName("java:/accounts-ds");
		pds.setClassName("bitronix.tm.resource.jdbc.lrc.LrcXADataSource");
		pds.setMaxPoolSize(5);
		pds.setAllowLocalTransactions(true);
		pds.getDriverProperties().put("user", "jdv_user");
		pds.getDriverProperties().put("password", "jdv_pass");
		pds.getDriverProperties().put("url", "jdbc:mysql://localhost:3306/customer");
		pds.getDriverProperties().put("driverClassName", "com.mysql.jdbc.Driver");
		pds.init();
	}
	
	static Integer[] array = new Integer[] {1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};
	
	public void query() throws Exception {
		
		for(long size : array){
			long rows = size / 100;
			String query = "SELECT * FROM SERIALTESTVIEW WHERE id < " + rows;
			Connection conn = getConn();
			
			System.out.println("Serialize " + size + " bytes spend time: " + caculation(query, conn) + " ms\n");
		}
		
	}
	
	private Connection getConn() throws SQLException {
		return server.getDriver().connect("jdbc:teiid:MysqlVDB", null);
	}
	
	private static boolean showPlan = false; 

	private static long caculation(String query, Connection conn) throws Exception {

		Statement stmt = conn .createStatement();
		if(showPlan){
			stmt.execute("set showplan on");
		}
		ResultSet rs = null;
		long time = 0;
		
		try {
			rs = stmt.executeQuery(query);
			printQueryPlan(stmt, query);
			Thread.currentThread().sleep(1000 * 20);
			System.out.println("Start count: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(System.currentTimeMillis())));
			long start = System.currentTimeMillis();
			while(rs.next()) {
				rs.getLong(1);
				rs.getString(2);
				rs.getString(3);
				rs.getString(4);
			}
			time = System.currentTimeMillis() - start;
			System.out.println("End count: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(System.currentTimeMillis())));
		} catch (Exception e) {
			throw e;
		} finally {
			JDBCUtil.close(rs, stmt);
			JDBCUtil.close(conn);
		}
		return time;
	}
	
	private static void printQueryPlan(Statement stmt, String sql) throws SQLException {
		System.out.println("Query Plans for execute " + sql);
		TeiidStatement tstatement = stmt.unwrap(TeiidStatement.class);
		PlanNode queryPlan = tstatement.getPlanDescription();
		System.out.println(queryPlan);
	}

	public static void main(String[] args) throws Exception {
		SerializationCaculation caculation = new SerializationCaculation();

		if(args.length > 0) {
			for(int i = 0; i < args.length; i++) {
				
				if(args[i].equals("-s") || args[i].equals("-size")){
					int size = Integer.parseInt(args[++i]);
					long rows = size / 100;
					String query = "SELECT * FROM SERIALTESTVIEW WHERE id < " + rows;
					System.out.println("Deserialize " + size + " bytes spend time: " + caculation(query, conn) + " ms\n");
				}
				
				if(args[i].equals("plan")){
					showPlan = true;
				}
			}
		} else {
			caculation.query();
		}	
	}
}
