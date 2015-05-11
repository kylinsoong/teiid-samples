package org.teiid.test;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;

import javax.naming.Context;

import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.ExecutionFactory;
import org.teiid.translator.jdbc.mysql.MySQL5ExecutionFactory;

import bitronix.tm.resource.jdbc.PoolingDataSource;

public class MysqDataSourceExample{
	
	 static {
         System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "bitronix.tm.jndi.BitronixInitialContextFactory");
	 }
	
	EmbeddedServer server = null;
	
	Connection conn = null;

	public void execute() throws Exception {
		
		server = new EmbeddedServer();
		
		ExecutionFactory<?, ?> factory = new MySQL5ExecutionFactory();
		factory.start();
		factory.setSupportsDirectQueryProcedure(true);
		server.addTranslator("translator-mysql", factory);
		
		setupDataSource();
		
		EmbeddedConfiguration config = new EmbeddedConfiguration();
		server.start(config);
		
		server.deployVDB(new FileInputStream(new File("src/main/resources/mysql-vdb.xml")));
		
		conn = server.getDriver().connect("jdbc:teiid:MysqlVDB", null);	
		
		JDBCUtil.executeQuery(conn, "SELECT AVG(TIMESTAMPDIFF(SQL_TSI_SECOND, tf.start_time, tf.end_time)) FROM time_function_test tf GROUP BY id");
		
		JDBCUtil.close(conn);
	}
	
	static PoolingDataSource pds = null;
	
	protected static void setupDataSource() {
		
		if (null != pds)
			return;
		
		pds = new PoolingDataSource();
		pds.setUniqueName("java:/accounts-ds");
		pds.setClassName("bitronix.tm.resource.jdbc.lrc.LrcXADataSource");
		pds.setMaxPoolSize(5);
		pds.setAllowLocalTransactions(true);
		pds.getDriverProperties().put("user", "jdv_user");
		pds.getDriverProperties().put("password", "jdv_pass");
		pds.getDriverProperties().put("url", "jdbc:mysql://localhost:3306/test");
		pds.getDriverProperties().put("driverClassName", "com.mysql.jdbc.Driver");
		pds.init();
	}
	
	
	
	public static void main(String[] args) throws Exception {
		new MysqDataSourceExample().execute();
	}


}
