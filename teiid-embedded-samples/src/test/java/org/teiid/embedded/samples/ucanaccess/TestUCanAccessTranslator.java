package org.teiid.embedded.samples.ucanaccess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.naming.Context;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.teiid.core.types.BlobType;
import org.teiid.deployers.VirtualDatabaseException;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository.ConnectorManagerException;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.jdbc.ucanaccess.UCanAccessExecutionFactory;

import bitronix.tm.resource.jdbc.PoolingDataSource;

@Ignore("need add ucanaccess driver to classpath")
public class TestUCanAccessTranslator {
	
	public static void main(String[] args) throws VirtualDatabaseException, TranslatorException, ConnectorManagerException, FileNotFoundException, IOException, SQLException {
		init();
	}
	
	static Connection conn = null;
	
	@BeforeClass
	public static void init() throws TranslatorException, VirtualDatabaseException, ConnectorManagerException, FileNotFoundException, IOException, SQLException {
		
		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "bitronix.tm.jndi.BitronixInitialContextFactory");
		
		EmbeddedServer server = new EmbeddedServer();
		
		UCanAccessExecutionFactory executionFactory = new UCanAccessExecutionFactory();
		executionFactory.start();
        server.addTranslator("translator-ucanaccess", executionFactory);
        
        setupTestDataSource();
        
        EmbeddedConfiguration config = new EmbeddedConfiguration();
//        config.setTransactionManager(SimpleMock.createSimpleMock(TransactionManager.class));
        server.start(config);
        server.deployVDB(new FileInputStream(new File("src/test/resources/ucanaccess/vdb.xml")));
        conn = server.getDriver().connect("jdbc:teiid:UCanAccessVDB", null);
	}
	
	@Test
	public void testInsert() throws SQLException {
		executeUpdate(conn, "INSERT INTO T21 VALUES(100, 't')");
		executeQuery(conn, "SELECT * FROM T21");
		executeUpdate(conn, "DELETE FROM T21 WHERE ID = 100");
	}
	
	@Test
	public void testMetadata() throws SQLException {
		
		Set<String> nameSet = new HashSet<String>();
		DatabaseMetaData databaseMetaData = conn.getMetaData();
		
		ResultSet result = databaseMetaData.getTables("UCanAccessVDB", "TestData", null, null );
		while(result.next()) {
		    String tableName = result.getString(3);
		    nameSet.add(tableName);
		}
		
		result = databaseMetaData.getTables("UCanAccessVDB", "TestUCanAccess", null, null );
		while(result.next()) {
		    String tableName = result.getString(3);
		    nameSet.add(tableName);
		}
		
		close(result, null);
		
		assertEquals(6, nameSet.size());
		assertTrue(nameSet.contains("EmpDataView"));
		assertTrue(nameSet.contains("EMPDATA"));
		assertTrue(nameSet.contains("EMPDATA_TEST"));
		assertTrue(nameSet.contains("T20"));
		assertTrue(nameSet.contains("T21"));
		assertTrue(nameSet.contains("T21View"));
		
//		assertTrue(nameSet.contains(""));
		
	}
	
	@Test
	public void testSelect() throws SQLException {
		executeQuery(conn, "SELECT * FROM EMPDATA");
		executeQuery(conn, "SELECT * FROM EMPDATA_TEST");
		executeQuery(conn, "SELECT * FROM EmpDataView");
		
		executeQuery(conn, "SELECT * FROM DATATYPE_TEST");
	}
	
	@Test
	public void testFunctions() throws SQLException {
		executeQuery(conn, "SELECT ASCII('A') FROM T20");
		executeQuery(conn, "SELECT CURDATE() FROM T20");
		executeQuery(conn, "SELECT CURTIME() FROM T20");

	}
	
	@Test
	public void testAccessLike() throws SQLException {
		executeQuery(conn, "SELECT * FROM T21 ORDER BY ID DESC");
		executeQuery(conn, "SELECT * FROM T21 WHERE DESCR = 'aba' ORDER BY ID DESC");
		executeQuery(conn, "select * from T21 WHERE DESCR like 'a*a' ORDER BY ID DESC");
		executeQuery(conn, "SELECT * FROM T21 WHERE DESCR like 'a*a' AND '1'='1' ORDER BY ID DESC");
		executeQuery(conn, "SELECT * FROM T21 WHERE DESCR like 'a%a' ORDER BY ID DESC");
		executeQuery(conn, "SELECT * FROM T21 WHERE DESCR like 'a%a' AND '1'='1' ORDER BY ID DESC");

		executeQuery(conn, "SELECT * FROM T21View ORDER BY ID DESC");
		executeQuery(conn, "SELECT * FROM T21View WHERE DESCR = 'aba' ORDER BY ID DESC");
		executeQuery(conn, "select * from T21View WHERE DESCR like 'a*a' ORDER BY ID DESC");
		executeQuery(conn, "SELECT * FROM T21View WHERE DESCR like 'a*a' AND '1'='1' ORDER BY ID DESC");
		executeQuery(conn, "SELECT * FROM T21View WHERE DESCR like 'a%a' ORDER BY ID DESC");
		executeQuery(conn, "SELECT * FROM T21View WHERE DESCR like 'a%a' AND '1'='1' ORDER BY ID DESC");
		
	}
	
	@Test
	public void testAggregateFunctions() throws SQLException {

		executeQuery(conn, "SELECT * FROM T20");
		executeQuery(conn, "SELECT COUNT(*) FROM T20");
		executeQuery(conn, "SELECT COUNT(ID) FROM T20");
		executeQuery(conn, "SELECT SUM(ID) FROM T20");
		executeQuery(conn, "SELECT AVG(ID) FROM T20");
		executeQuery(conn, "SELECT MIN(ID) FROM T20");
		executeQuery(conn, "SELECT MAX(ID) FROM T20");
		
	}
	
	/**
	 * 1. UCanAccess supported datatypes 
	 * 		YESNO, BYTE, INTEGER, LONG, SINGLE, DOUBLE, NUMERIC, CURRENCY, COUNTER, TEXT, OLE, MEMO, GUID,DATETIME
	 * 
	 * 2. Table 'DATATYPE_TEST' in ODBCTesting.accdb
	 * 		CREATE TABLE DATATYPE_TEST (id LONG, c_yesno YESNO, c_byte BYTE, c_integer INTEGER, c_long LONG, c_single SINGLE, 
	 * 										c_double DOUBLE, c_numeric numeric(24,5), c_currency CURRENCY, c_counter COUNTER, 
	 * 										c_txt TEXT, c_ole OLE, c_memo MEMO, c_guid GUID, c_datatime DATETIME)
	 * @throws SQLException 
	 * 
	 */
	@Test
	public void testDataType() throws SQLException{
		
		executeQuery(conn, "SELECT * FROM DATATYPE_TEST");
		
		long v_long = 10001;
		boolean v_boolean = true;
		byte v_byte = 127;
		int v_int = 5;
		double v_double = 5.6666;
		BigDecimal v_bigdecimal = new BigDecimal(4.5555);
		String v_string = "sting column";
		Blob v_blob = BlobType.createBlob("".getBytes());
		Timestamp v_timestramp = new Timestamp(new java.util.Date().getTime());
		
		PreparedStatement ps = conn.prepareStatement("INSERT INTO DATATYPE_TEST (id, c_yesno, c_byte, c_integer, c_long, c_single, c_double, c_numeric, c_currency, c_counter, c_txt, c_ole, c_memo, c_guid, c_datatime) VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		ps.setLong(1, v_long);
		ps.setBoolean(2, v_boolean);
		ps.setByte(3, v_byte);
		ps.setInt(4, v_int);
		ps.setLong(5, v_long);
		ps.setDouble(6, v_double);
		ps.setDouble(7, v_double);
		ps.setBigDecimal(8, v_bigdecimal);
		ps.setBigDecimal(9, v_bigdecimal);
		ps.setInt(10, v_int);
		ps.setString(11, v_string);
		ps.setBlob(12, v_blob);
		ps.setString(13, v_string);
		ps.setString(14, v_string);
		ps.setTimestamp(15, v_timestramp);
		ps.addBatch();
		ps.executeBatch();
		
		executeQuery(conn, "SELECT * FROM DATATYPE_TEST");
		
		executeUpdate(conn, "UPDATE DATATYPE_TEST SET c_txt = 'updated test' WHERE id = 10001");
		
		executeUpdate(conn, "DELETE FROM DATATYPE_TEST WHERE id = 10001");
		
		executeQuery(conn, "SELECT * FROM DATATYPE_TEST");
	}
	
	private static void setupTestDataSource() {
		
		PoolingDataSource pds = new PoolingDataSource();
		pds.setUniqueName("java:/UCanAccessDS");
        pds.setClassName("bitronix.tm.resource.jdbc.lrc.LrcXADataSource");
        pds.setMaxPoolSize(5);
        pds.setAllowLocalTransactions(true);
        pds.getDriverProperties().put("user", "");
        pds.getDriverProperties().put("password", "");
        pds.getDriverProperties().put("url", "jdbc:ucanaccess://src/test/resources/ucanaccess/ODBCTesting.accdb");
        pds.getDriverProperties().put("driverClassName", "net.ucanaccess.jdbc.UcanaccessDriver");
        pds.init();
		
	}
	
	static void executeQuery(Connection conn, String sql) throws SQLException {
		
		System.out.println("Query: " + sql);
                
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData metadata = rs.getMetaData();
            int columns = metadata.getColumnCount();
            for (int row = 1; rs.next(); row++) {
                System.out.print(row + ": ");
                for (int i = 0; i < columns; i++) {
                    if (i > 0) {
                        System.out.print(", ");
                    }
                    System.out.print(rs.getObject(i + 1));
                }
                System.out.println();
            }
        }  finally {
            close(rs, stmt);
        }
        
        System.out.println();
        
    }
	
	static boolean executeUpdate(Connection conn, String sql) throws SQLException {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		}  finally {
			close(null, stmt);
		}
		return true;
	}
	
	static void close(ResultSet rs, Statement stmt) {

        if (null != rs) {
            try {
                rs.close();
                rs = null;
            } catch (SQLException e) {
            }
        }
        
        if(null != stmt) {
            try {
                stmt.close();
                stmt = null;
            } catch (SQLException e) {
            }
        }
    }

	@AfterClass
	public static void destory() throws SQLException{
		if(null != conn){
            conn.close();
        }
	}

}
