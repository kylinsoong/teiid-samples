package com.teiid.quickstart.mysql;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.jdbc.mysql.MySQL5ExecutionFactory;

import com.teiid.quickstart.util.JDBCUtil;

import bitronix.tm.resource.jdbc.PoolingDataSource;

public class TestMysqTranslatorConnector {
	
	static {
		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "bitronix.tm.jndi.BitronixInitialContextFactory");
	}
	
	static EmbeddedServer server = null;
	static PoolingDataSource pds = null ;
	static Connection conn = null;
	
	@BeforeClass
	public static void init() throws Exception {
		
		setupDataSource();
		
		server = new EmbeddedServer();
		
		MySQL5ExecutionFactory executionFactory = new MySQL5ExecutionFactory();
		executionFactory.setSupportsDirectQueryProcedure(true);
		executionFactory.start();
		server.addTranslator("translator-mysql", executionFactory);
		
		server.start(new EmbeddedConfiguration());
		
		server.deployVDB(new FileInputStream(new File("src/vdb/mysql-vdb.xml")));
		
		conn = server.getDriver().connect("jdbc:teiid:MysqlVDB", null);	
	}

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
		pds.getDriverProperties().put("url", "jdbc:mysql://localhost:3306/customer");
		pds.getDriverProperties().put("driverClassName", "com.mysql.jdbc.Driver");
		pds.init();
	}
	
	@Test
	public void testSetup() throws Exception {
		assertNotNull(conn);
		InitialContext ic = new InitialContext();
		assertNotNull(ic.lookup("java:/accounts-ds"));
	}
	
	@Test
	public void testQuery() throws Exception {
		assertTrue(JDBCUtil.countResults(conn, "SELECT * FROM CUSTOMERVIEW") > 0);
		assertTrue(JDBCUtil.countResults(conn, "SELECT * FROM PRODUCT") > 0);
		assertTrue(JDBCUtil.countResults(conn, "SELECT * FROM ACCOUNT") > 0);
		assertTrue(JDBCUtil.countResults(conn, "SELECT * FROM CUSTOMER") > 0);
		assertTrue(JDBCUtil.countResults(conn, "SELECT * FROM CUSTOMER") > 0);
	}


	@AfterClass
	public static void tearDown() throws Exception {
		if(null != conn) {
			conn.close();
			conn = null;
		}
		if(null != server) {
			server.stop();
			server = null;
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		init();
		JDBCUtil.executeQuery(conn, "SELECT * FROM PRODUCT");
		JDBCUtil.executeQuery(conn, "SELECT * FROM ACCOUNT");
		JDBCUtil.executeQuery(conn, "SELECT * FROM CUSTOMER");
		JDBCUtil.executeQuery(conn, "SELECT * FROM PROUDCTVIEW");
		JDBCUtil.executeQuery(conn, "SELECT * FROM CUSTOMERVIEW");
	}
}
