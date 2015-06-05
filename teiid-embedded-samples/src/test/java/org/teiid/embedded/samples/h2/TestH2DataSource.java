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

import javax.sql.DataSource;

import org.h2.tools.RunScript;
import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.teiid.adminapi.Transaction;
import org.teiid.embedded.samples.EmbeddedHelper;
import org.teiid.embedded.samples.TestBase;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

public class TestH2DataSource extends TestBase {
	
	static Server h2Server = null;
	
	@BeforeClass
	public static void init() throws Exception {
		startServer();
		
		DataSource ds = EmbeddedHelper.newDataSource("org.h2.Driver", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "sa");
		
		initTestData(ds);
		
		init("translator-h2", new H2ExecutionFactory());
		
		server.addConnectionFactory("java:/accounts-ds", ds);
		
		start(false);
		
		server.deployVDB(new FileInputStream(new File("vdb/h2-vdb.xml")));
		conn = server.getDriver().connect("jdbc:teiid:H2VDB", null);
	}
	
	private static void initTestData(DataSource ds) throws FileNotFoundException, SQLException {
		RunScript.execute(ds.getConnection(), new FileReader("metadata/customer-schema-h2.sql"));
	}

	private static void startServer() throws SQLException {
		h2Server = Server.createTcpServer().start();
	}
	
	@Test
	public void testConnection(){
		assertNotNull(conn);
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
