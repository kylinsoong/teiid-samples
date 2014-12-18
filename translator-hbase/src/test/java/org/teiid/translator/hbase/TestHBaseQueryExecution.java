package org.teiid.translator.hbase;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.resource.ResourceException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.teiid.deployers.VirtualDatabaseException;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository.ConnectorManagerException;
import org.teiid.resource.adapter.hbase.HBaseConnection;
import org.teiid.resource.adapter.hbase.HBaseManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.TranslatorException;

public class TestHBaseQueryExecution {
	
	static {
	      String pattern = "%d %-5p [%c] (%t) %m%n";
	      PatternLayout layout = new PatternLayout(pattern);
	      ConsoleAppender consoleAppender = new ConsoleAppender(layout);
	      Logger.getRootLogger().setLevel(Level.WARN);
	      Logger.getRootLogger().addAppender(consoleAppender);  
		}
	
	static Connection conn = null;
	
	@BeforeClass
	public static void init() throws TranslatorException, ResourceException, VirtualDatabaseException, ConnectorManagerException, FileNotFoundException, IOException, SQLException {
		
		EmbeddedServer server = new EmbeddedServer();
		
		HBaseExecutionFactory executionFactory = new HBaseExecutionFactory();
		executionFactory.start();
		server.addTranslator("translator-hbase", executionFactory);
		
		HBaseManagedConnectionFactory managedconnectionFactory = new HBaseManagedConnectionFactory();
		managedconnectionFactory.setZkQuorum("localhost:2181");
		server.addConnectionFactory("java:/hbaseDS", managedconnectionFactory.createConnectionFactory());
		
		server.start(new EmbeddedConfiguration());
		server.deployVDB(new FileInputStream(new File("src/test/resources/hbase-vdb.xml")));
		conn = server.getDriver().connect("jdbc:teiid:hbasevdb", null);
	}
	
	@Test
	public void testSelect() throws Exception {
		JDBCUtil.executeQuery(conn, "SELECT * FROM Customer");
		JDBCUtil.executeQuery(conn, "SELECT city, amount FROM Customer");
		JDBCUtil.executeQuery(conn, "SELECT DISTINCT city FROM Customer");
		JDBCUtil.executeQuery(conn, "SELECT city, amount FROM Customer WHERE PK='105'");
		JDBCUtil.executeQuery(conn, "SELECT city, amount FROM Customer WHERE PK='105' OR name='John White'");
		JDBCUtil.executeQuery(conn, "SELECT city, amount FROM Customer WHERE PK='105' AND name='John White'");
	}
	
	@Test
	public void testSelectOrderBy() throws Exception {
		JDBCUtil.executeQuery(conn, "SELECT * FROM Customer ORDER BY PK");
		JDBCUtil.executeQuery(conn, "SELECT * FROM Customer ORDER BY PK ASC");
		JDBCUtil.executeQuery(conn, "SELECT * FROM Customer ORDER BY PK DESC");
		JDBCUtil.executeQuery(conn, "SELECT * FROM Customer ORDER BY name, city DESC");
	}
	
	@Test
	public void testSelectGroupBy() throws Exception {
		JDBCUtil.executeQuery(conn, "SELECT COUNT(PK) FROM Customer WHERE name='John White'");
		JDBCUtil.executeQuery(conn, "SELECT name, COUNT(PK) FROM Customer GROUP BY name");
		JDBCUtil.executeQuery(conn, "SELECT name, COUNT(PK) FROM Customer GROUP BY name HAVING COUNT(PK) > 1");
		JDBCUtil.executeQuery(conn, "SELECT name, city, COUNT(PK) FROM Customer GROUP BY name, city");
		JDBCUtil.executeQuery(conn, "SELECT name, city, COUNT(PK) FROM Customer GROUP BY name, city HAVING COUNT(PK) > 1");
	}
	
	@Test
	public void testSelectLimit() throws Exception {
		JDBCUtil.executeQuery(conn, "SELECT * FROM Customer LIMIT 3");
		JDBCUtil.executeQuery(conn, "SELECT * FROM Customer ORDER BY PK DESC LIMIT 3");
	}
	
	@Test
	public void testConnection() throws ResourceException {
		HBaseManagedConnectionFactory connectionFactory = new HBaseManagedConnectionFactory();
		connectionFactory.setZkQuorum("localhost:2181");
		HBaseConnection connection = connectionFactory.createConnectionFactory().getConnection();
		assertNotNull(connection);
		JDBCUtil.close(connection.getConnection());;
	}
	
	@AfterClass 
	public static void tearDown() throws SQLException{
		if(null != conn){
			conn.close();
		}
	}
	
	
	
	public static void main(String[] args) throws Exception {
		
		EmbeddedServer server = new EmbeddedServer();
		
		HBaseExecutionFactory executionFactory = new HBaseExecutionFactory();
		executionFactory.start();
		server.addTranslator("translator-hbase", executionFactory);
		
		HBaseManagedConnectionFactory managedconnectionFactory = new HBaseManagedConnectionFactory();
		managedconnectionFactory.setZkQuorum("localhost:2181");
		server.addConnectionFactory("java:/hbaseDS", managedconnectionFactory.createConnectionFactory());
		
		server.start(new EmbeddedConfiguration());
		server.deployVDB(new FileInputStream(new File("src/test/resources/hbase-vdb.xml")));
		Connection conn = server.getDriver().connect("jdbc:teiid:hbasevdb", null);
		
//		JDBCUtil.executeQuery(conn, "SELECT * FROM Customer");
		
		JDBCUtil.executeUpdate(conn, "INSERT INTO Customer VALUES('106', 'Beijing', 'Kylin Soong', '$4000', 'Crystal Orange')");
		
		JDBCUtil.close(conn);
		
	}
	

}
