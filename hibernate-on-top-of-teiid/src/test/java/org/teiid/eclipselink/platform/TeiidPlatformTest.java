package org.teiid.eclipselink.platform;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TeiidPlatformTest {
	
	private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
	private static final String JDBC_URL = "jdbc:teiid:ModeShape@mm://localhost:31000;version=1";
	private static final String JDBC_USER = "user";
	private static final String JDBC_PASS = "user";
	
	static Connection conn = null;
	
	@BeforeClass 
	public static void init() {
		try {	
			conn = JDBCUtil.getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		}  catch (Exception e) {
			throw new RuntimeException("Can not init Teiid Server JDBC connection");
		}
	}
	
	@Test
	public void testPing() throws Exception {
		String sql = "SELECT 1";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		assertEquals(1, rs.getMetaData().getColumnCount());
		rs.next();
		assertEquals("1", rs.getString(1));
		JDBCUtil.close(rs, stmt);
	}
	
	@AfterClass 
	public static void destory() {
		JDBCUtil.close(conn);
	}

}
