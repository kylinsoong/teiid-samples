package com.teiid.quickstart;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.resource.cci.ConnectionFactory;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.teiid.resource.adapter.file.FileManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.runtime.EmbeddedServer.ConnectionFactoryProvider;
import org.teiid.translator.file.FileExecutionFactory;
import org.teiid.transport.SocketConfiguration;
import org.teiid.transport.WireProtocol;

import com.teiid.quickstart.util.JDBCUtil;


public class TestTeiidServerJDBC {
	
	static EmbeddedServer server;
	private static Connection conn;

	@BeforeClass
	public static void setUp() throws Exception {
			
		server = new EmbeddedServer();
		
		FileExecutionFactory executionFactory = new FileExecutionFactory();
		server.addTranslator("file", executionFactory);
		
		FileManagedConnectionFactory fileManagedconnectionFactory = new FileManagedConnectionFactory();
		fileManagedconnectionFactory.setParentDirectory("src/file");
		ConnectionFactory connectionFactory = fileManagedconnectionFactory.createConnectionFactory();
		ConnectionFactoryProvider<ConnectionFactory> connectionFactoryProvider = new EmbeddedServer.SimpleConnectionFactoryProvider<ConnectionFactory>(connectionFactory);
		server.addConnectionFactoryProvider("java:/marketdata-file", connectionFactoryProvider);
		
		SocketConfiguration s = new SocketConfiguration();
		InetSocketAddress addr = new InetSocketAddress("localhost", 31000);
		s.setBindAddress(addr.getHostName());
		s.setPortNumber(addr.getPort());
		s.setProtocol(WireProtocol.teiid);
		EmbeddedConfiguration config = new EmbeddedConfiguration();
		config.addTransport(s);
		server.start(config);
		
		server.deployVDB(new FileInputStream(new File("src/vdb/marketdata-vdb.xml")));
		
		conn = JDBCUtil.getDriverConnection("org.teiid.jdbc.TeiidDriver", "jdbc:teiid:Marketdata@mm://localhost:31000;version=1", "", "");
	}
	
	@Test
	public void testConnection(){
		assertNotNull(conn);
	}
	
	@Test
	public void testSchema() {
		assertNotNull(server.getSchemaDdl("Marketdata", "Stocks"));
		assertNotNull(server.getSchemaDdl("Marketdata", "SYS"));
		assertNotNull(server.getSchemaDdl("Marketdata", "SYSADMIN"));
	}
	
	@Test
	public void testQuery() throws SQLException {
		
		String query = "SELECT * FROM Marketdata";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		ResultSetMetaData metadata = rs.getMetaData();
		assertEquals("symbol", metadata.getColumnName(1));
		assertEquals("price", metadata.getColumnName(2));
		assertNotNull(rs.next());
		JDBCUtil.close(rs, stmt);
		
		query = "SELECT * FROM SYMBOLS";
		stmt = conn.createStatement();
		rs = stmt.executeQuery(query);
		metadata = rs.getMetaData();
		assertEquals(1, metadata.getColumnCount());
		assertNotNull(rs.next());
		JDBCUtil.close(rs, stmt);
		
		query = "SELECT * FROM HelloWorld";
		stmt = conn.createStatement();
		rs = stmt.executeQuery(query);
		assertNotNull(rs.next());
		assertEquals("HELLO WORLD", rs.getString(1));
		JDBCUtil.close(rs, stmt);
	}
	
	/**
	 * 1. view can be created explicitly via the metadata supplied to Teiid at deploy time
	 * 2. Teiid DO NOT support create view via JDBC
	 * 3. Teiid support alter view via JDBC
	 */
	@Test
	public void testDDLAlterView() throws Exception {
	
		assertEquals(10, JDBCUtil.countResults(conn, "SELECT * FROM SYMBOLS"));
		JDBCUtil.executeUpdate(conn, "ALTER VIEW SYMBOLS AS SELECT Marketdata.symbol FROM Marketdata WHERE Marketdata.symbol = 'RHT'");
		assertEquals(1, JDBCUtil.countResults(conn, "SELECT * FROM SYMBOLS"));
		JDBCUtil.executeUpdate(conn, "ALTER VIEW SYMBOLS AS SELECT Marketdata.symbol FROM Marketdata");
		
		assertEquals("HELLO WORLD", JDBCUtil.query(conn, "SELECT * FROM HelloWorld"));
		JDBCUtil.executeUpdate(conn, "ALTER VIEW HelloWorld as SELECT 'ABCDEFGH'");
		assertEquals("ABCDEFGH", JDBCUtil.query(conn, "SELECT * FROM HelloWorld"));
		JDBCUtil.executeUpdate(conn, "ALTER VIEW HelloWorld as SELECT 'HELLO WORLD'");
	}
	
	
	/**
     *  Local Temporary Tables only support created in runtime via JDBC
	 * 
     */
	@Test
	public void testDDLLocalTempTables() throws Exception {
	
		assertTrue(JDBCUtil.executeUpdate(conn, "CREATE LOCAL TEMPORARY TABLE TEMP (id SERIAL NOT NULL, name string, PRIMARY KEY (id))"));
		for(int i = 0 ; i < 3 ; i ++) {
			JDBCUtil.executeUpdate(conn, "INSERT INTO TEMP (name) VALUES ('test-name-" + i + "')");
		}
		assertTrue(JDBCUtil.executeUpdate(conn, "DELETE FROM TEMP WHERE id = 2"));
		assertTrue(JDBCUtil.executeUpdate(conn, "UPDATE TEMP SET name = 'Kylin Soong' WHERE id = 3"));
		assertEquals("Kylin Soong", JDBCUtil.query(conn, "SELECT name FROM TEMP WHERE id = 3"));
		assertTrue(JDBCUtil.executeUpdate(conn, "DROP TABLE TEMP"));
	}
	
	/**
	 * 
	 * Global Temporary Tables only support create via the metadata supplied to Teiid at deploy time, cannot be created at runtime.
	 */
	@Test
	public void testDDLGlobalTempTables() throws Exception {
		assertNotNull("todo");
	}
	


	@AfterClass
	public static void tearDown() throws Exception {
		conn.close();
		server.stop();
	}

}
