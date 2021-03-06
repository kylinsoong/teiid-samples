/*
 * JBoss, Home of Professional Open Source.
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 */
package org.teiid.translator.hbase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.resource.ResourceException;
import javax.transaction.TransactionManager;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.teiid.core.util.SimpleMock;
import org.teiid.deployers.VirtualDatabaseException;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository.ConnectorManagerException;
import org.teiid.resource.adapter.hbase.HBaseManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.HBaseConnection;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.TypeFacility;

//@Ignore
public class TestHBaseExecution {
	
	static {
	      String pattern = "%d %-5p [%c] (%t) %m%n";
	      PatternLayout layout = new PatternLayout(pattern);
	      ConsoleAppender consoleAppender = new ConsoleAppender(layout);
	      Logger.getRootLogger().setLevel(Level.WARN);
	      Logger.getRootLogger().addAppender(consoleAppender);  
	      
	      System.setProperty("java.util.logging.config.file", "src/test/resources/logging.properties");
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
		
		EmbeddedConfiguration config = new EmbeddedConfiguration();
		config.setTransactionManager(SimpleMock.createSimpleMock(TransactionManager.class));
		server.start(config);
		server.deployVDB(new FileInputStream(new File("src/test/resources/hbase-vdb.xml")));
		conn = server.getDriver().connect("jdbc:teiid:hbasevdb", null);
	}
	
	@Test
	public void testInsert() throws Exception {
		TestHBaseUtil.executeUpdate(conn, "INSERT INTO Customer VALUES('108', 'Beijing', 'Kylin Soong', '$8000.00', 'Crystal Orange')");
		TestHBaseUtil.executeUpdate(conn, "INSERT INTO Customer(PK, city, name) VALUES ('109', 'Beijing', 'Kylin Soong')");
	}
	
	@Test
	public void testBatchedInsert() throws SQLException {
		TestHBaseUtil.executeBatchedUpdate(conn, "INSERT INTO Customer VALUES (?, ?, ?, ?, ?)", 2);
		TestHBaseUtil.executeBatchedUpdate(conn, "INSERT INTO Customer(PK, city, name, amount, product) VALUES (?, ?, ?, ?, ?)", 2);
	}
	
	@Test
	public void testConditionAndOr() throws Exception {
		TestHBaseUtil.executeQuery(conn, "SELECT * FROM Customer WHERE PK='105' OR name='John White'");
		TestHBaseUtil.executeQuery(conn, "SELECT * FROM Customer WHERE PK='105' AND name='John White'");
		TestHBaseUtil.executeQuery(conn, "SELECT * FROM Customer WHERE PK='105' AND (name='John White' OR name='Kylin Soong')");
	}
	
	/**
	 * = 	Equal
	 * > 	Greater than
	 * < 	Less than
	 * >= 	Greater than or equal
	 * <= 	Less than or equal
	 * BETWEEN 	Between an inclusive range
	 * LIKE 	Search for a pattern
	 * IN 	To specify multiple possible values for a column
	 */
	@Test
	public void testConditionComparison() throws Exception {
		TestHBaseUtil.executeQuery(conn, "SELECT * FROM Customer WHERE PK = '108'");
		TestHBaseUtil.executeQuery(conn, "SELECT * FROM Customer WHERE PK > '108'");
		TestHBaseUtil.executeQuery(conn, "SELECT * FROM Customer WHERE PK < '108'");
		TestHBaseUtil.executeQuery(conn, "SELECT * FROM Customer WHERE PK >= '108'");
		TestHBaseUtil.executeQuery(conn, "SELECT * FROM Customer WHERE PK <= '108'");
		TestHBaseUtil.executeQuery(conn, "SELECT * FROM Customer WHERE PK BETWEEN '105' AND '108'");
		TestHBaseUtil.executeQuery(conn, "SELECT * FROM Customer WHERE PK LIKE '10%'");
		TestHBaseUtil.executeQuery(conn, "SELECT * FROM Customer WHERE PK IN ('105', '106')");
	}
	
	@Test
	public void testSelect() throws Exception {
		TestHBaseUtil.executeQuery(conn, "SELECT * FROM Customer");
		TestHBaseUtil.executeQuery(conn, "SELECT city, amount FROM Customer");
		TestHBaseUtil.executeQuery(conn, "SELECT DISTINCT city FROM Customer");
		TestHBaseUtil.executeQuery(conn, "SELECT city, amount FROM Customer WHERE PK='105'");
	}
	
	@Test
	public void testSelectOrderBy() throws Exception {
		TestHBaseUtil.executeQuery(conn, "SELECT * FROM Customer ORDER BY PK");
		TestHBaseUtil.executeQuery(conn, "SELECT * FROM Customer ORDER BY PK ASC");
		TestHBaseUtil.executeQuery(conn, "SELECT * FROM Customer ORDER BY PK DESC");
		TestHBaseUtil.executeQuery(conn, "SELECT * FROM Customer ORDER BY name, city DESC");
	}
	
	@Test
	public void testSelectGroupBy() throws Exception {
		TestHBaseUtil.executeQuery(conn, "SELECT COUNT(PK) FROM Customer WHERE name='John White'");
		TestHBaseUtil.executeQuery(conn, "SELECT name, COUNT(PK) FROM Customer GROUP BY name");
		TestHBaseUtil.executeQuery(conn, "SELECT name, COUNT(PK) FROM Customer GROUP BY name HAVING COUNT(PK) > 1");
		TestHBaseUtil.executeQuery(conn, "SELECT name, city, COUNT(PK) FROM Customer GROUP BY name, city");
		TestHBaseUtil.executeQuery(conn, "SELECT name, city, COUNT(PK) FROM Customer GROUP BY name, city HAVING COUNT(PK) > 1");
	}
	
	@Test
	public void testSelectLimit() throws Exception {
		TestHBaseUtil.executeQuery(conn, "SELECT * FROM Customer LIMIT 3");
		TestHBaseUtil.executeQuery(conn, "SELECT * FROM Customer ORDER BY PK DESC LIMIT 3");
	}
	
	/*
	 * Teiid: https://docs.jboss.org/author/display/TEIID/Supported+Types
	 * 
	 * Phoenix: http://phoenix.apache.org/language/datatypes.html
	 */
	@Test
	public void testDataTypes() throws Exception{
		TestHBaseUtil.executeQuery(conn, "SELECT * FROM TypesTest");
		TestHBaseUtil.executeBatchedUpdateDataType(conn, "INSERT INTO TypesTest VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM TypesTest WHERE PK = '10001'");
			if(rs.next()) {
				assertEquals(rs.getObject(1).getClass(), TypeFacility.RUNTIME_TYPES.STRING);
				assertEquals(rs.getObject(2).getClass(), TypeFacility.RUNTIME_TYPES.STRING);
				assertEquals(rs.getObject(3).getClass(), byte[].class);
				assertEquals(rs.getObject(4).getClass(), TypeFacility.RUNTIME_TYPES.CHAR);
				assertEquals(rs.getObject(5).getClass(), TypeFacility.RUNTIME_TYPES.BOOLEAN);
				assertEquals(rs.getObject(6).getClass(), TypeFacility.RUNTIME_TYPES.BYTE);
				assertEquals(rs.getObject(7).getClass(), TypeFacility.RUNTIME_TYPES.BYTE);
				assertEquals(rs.getObject(8).getClass(), TypeFacility.RUNTIME_TYPES.SHORT);
				assertEquals(rs.getObject(9).getClass(), TypeFacility.RUNTIME_TYPES.SHORT);
				assertEquals(rs.getObject(10).getClass(), TypeFacility.RUNTIME_TYPES.INTEGER);
				assertEquals(rs.getObject(11).getClass(), TypeFacility.RUNTIME_TYPES.INTEGER);
				assertEquals(rs.getObject(12).getClass(), TypeFacility.RUNTIME_TYPES.LONG);
				assertEquals(rs.getObject(13).getClass(), TypeFacility.RUNTIME_TYPES.LONG);
				assertEquals(rs.getObject(14).getClass(), TypeFacility.RUNTIME_TYPES.FLOAT);
				assertEquals(rs.getObject(15).getClass(), TypeFacility.RUNTIME_TYPES.FLOAT);
				assertEquals(rs.getObject(16).getClass(), TypeFacility.RUNTIME_TYPES.DOUBLE);
				assertEquals(rs.getObject(17).getClass(), TypeFacility.RUNTIME_TYPES.BIG_DECIMAL);
				assertEquals(rs.getObject(18).getClass(), TypeFacility.RUNTIME_TYPES.BIG_DECIMAL);
				assertEquals(rs.getObject(19).getClass(), TypeFacility.RUNTIME_TYPES.DATE);
				assertEquals(rs.getObject(20).getClass(), TypeFacility.RUNTIME_TYPES.TIME);
				assertEquals(rs.getObject(21).getClass(), TypeFacility.RUNTIME_TYPES.TIMESTAMP);
			}			
		} catch (Exception e) {
			throw e ;
		} finally {
			TestHBaseUtil.close(rs, stmt);
		}
	}
	
	@Test
	public void testFunctions() throws Exception {
		TestHBaseUtil.executeQuery(conn, "SELECT COUNT(PK) AS totalCount FROM Customer WHERE name = 'Kylin Soong'");
	}
	
	@Test
	public void testProcedures() throws Exception {
		TestHBaseUtil.executeCallable(conn, "call extractData('103')");
	}
	
	@Test
	public void testConnection() throws ResourceException, SQLException {
		HBaseManagedConnectionFactory connectionFactory = new HBaseManagedConnectionFactory();
		connectionFactory.setZkQuorum("localhost:2181");
		HBaseConnection connection = connectionFactory.createConnectionFactory().getConnection();
		assertNotNull(connection);
		DatabaseMetaData dbmd = connection.getConnection().getMetaData();
		assertEquals(false, dbmd.supportsGetGeneratedKeys());
		TestHBaseUtil.close(connection.getConnection());;
	}
	
	@AfterClass 
	public static void tearDown() throws SQLException{
		if(null != conn){
			conn.close();
		}
	}
	
	public static void main(String[] args) throws Exception {
		init();
		
//		TestHBaseUtil.executeQuery(conn, "SELECT * FROM Customer");
		
		TestHBaseUtil.executeQuery(conn, "SELECT * FROM Customer ORDER BY PK DESC");
		
		tearDown();
	}
	
}
