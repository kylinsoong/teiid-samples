package org.teiid.embedded.samples.h2;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.teiid.embedded.samples.util.JDBCUtil.executeQuery;
import static org.teiid.embedded.samples.util.JDBCUtil.executeUpdate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.List;

import javax.naming.Context;

import org.h2.tools.RunScript;
import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.teiid.adminapi.Transaction;
import org.teiid.embedded.samples.TestBase;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

import bitronix.tm.resource.jdbc.PoolingDataSource;

public class TestH2BTMDataSource extends TestBase {
	
	static {
		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "bitronix.tm.jndi.BitronixInitialContextFactory");
	}
	
	static Server h2Server = null;
	static PoolingDataSource pds = null ;
	
	@BeforeClass
	public static void init() throws Exception {
		startServer();
		setupDataSource();
		initTestData();
		
		init("translator-h2", new H2ExecutionFactory());
		start(false);
		server.deployVDB(new FileInputStream(new File("vdb/h2-vdb.xml")));
		conn = server.getDriver().connect("jdbc:teiid:H2VDB", null);
	}

	private static void startServer() throws SQLException {
		h2Server = Server.createTcpServer().start();
	}

	private static void setupDataSource() {
		if (null != pds)
			return;
		
		pds = new PoolingDataSource();
		pds.setUniqueName("java:/accounts-ds");
		pds.setClassName("bitronix.tm.resource.jdbc.lrc.LrcXADataSource");
		pds.setMaxPoolSize(5);
		pds.setAllowLocalTransactions(true);
		pds.getDriverProperties().put("user", "sa");
		pds.getDriverProperties().put("password", "sa");
		pds.getDriverProperties().put("url", "jdbc:h2:mem://localhost/~/test");
		pds.getDriverProperties().put("driverClassName", "org.h2.Driver");
		pds.init();
	}

	private static void initTestData() throws FileNotFoundException, SQLException {
		RunScript.execute(pds.getConnection(), new FileReader("metadata/customer-schema-h2.sql"));
	}
	
	@Test
	public void testQuery() throws Exception {
		executeQuery(conn, "SELECT * FROM PROUDCTVIEW");
		executeQuery(conn, "SELECT * FROM CUSTOMERVIEW");
		executeQuery(conn, "SELECT * FROM PRODUCT");
		executeQuery(conn, "SELECT * FROM ACCOUNT");
		executeQuery(conn, "SELECT * FROM CUSTOMER");
	}
	
	@Test
	public void testTransaction() throws Exception {
		conn.setAutoCommit(false);
		executeUpdate(conn, "DELETE FROM PRODUCT");
		Thread.sleep(2000);
		@SuppressWarnings("unchecked")
		List<Transaction> list = (List<Transaction>) server.getAdmin().getTransactions();
		Transaction tx = list.get(0);
		assertNotNull(tx.getAssociatedSession());
		assertNotNull(tx.getId());
		assertTrue((System.currentTimeMillis() - tx.getCreatedTime()) > 2000);
		conn.rollback();
	}
	
	@AfterClass
	public static void tearDown() throws Exception {
		h2Server.stop();
	}

}
