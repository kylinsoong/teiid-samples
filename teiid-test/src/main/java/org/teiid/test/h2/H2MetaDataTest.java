package org.teiid.test.h2;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.h2.tools.RunScript;
import org.h2.tools.Server;
import org.teiid.test.JDBCUtil;

public class H2MetaDataTest {
	
	static Connection conn = null;
	static Server h2Server = null;
	
	static void startServer() throws Exception {
		h2Server = Server.createTcpServer().start();
		conn = JDBCUtil.getDriverConnection("org.h2.Driver", "jdbc:h2:mem://localhost/~/test", "sa", "sa");
	}
	
	static void initTestData() throws SQLException, FileNotFoundException {
		RunScript.execute(conn, new FileReader("src/file/customer-schema-h2.sql"));
	}
	
	static String quoteString;
	
	@SuppressWarnings("unused")
	static void testMetaData() throws SQLException {
		
		DatabaseMetaData metadata = conn.getMetaData();
		
		quoteString = metadata.getIdentifierQuoteString();
		
		ResultSet rs = metadata.getTypeInfo();
		printColumnNameType(rs);
		if (rs != null) {
			while (rs.next()) {
				String TYPE_NAME = rs.getString(1);
				int DATA_TYPE = rs.getInt(2);
				int PRECISION = rs.getInt(3);
				boolean UNSIGNED_ATTRIBUTE = rs.getBoolean(10);
//				System.out.println(TYPE_NAME + ", " + DATA_TYPE + ", " + PRECISION + ", " + UNSIGNED_ATTRIBUTE + "");
			}
		}
		
		rs = metadata.getTables("TEST", "PUBLIC", null, null);
		printColumnNameType(rs);
		if (rs != null) {
			while(rs.next()) {
				String TABLE_CATALOG = rs.getString(1);
				String TABLE_SCHEMA = rs.getString(2);
				String TABLE_NAME = rs.getString(3);
				String TABLE_TYPE = rs.getString(4);
				String REMARKS = rs.getString(5);
				String TYPE_NAME = rs.getString(6);
				String SQL = rs.getString(11);
				System.out.println(TABLE_CATALOG + ", " + TABLE_SCHEMA + ", " + TABLE_NAME + ", " + TABLE_TYPE + ", " + REMARKS + ", " + TYPE_NAME + ", " + rs.getString(7) + ", " + rs.getString(8) + ", " + rs.getString(9) + ", " + rs.getString(10));
			}
		}
		
		
	}
	
	private static void printColumnNameType(ResultSet rs) throws SQLException {

		ResultSetMetaData metadata  = rs.getMetaData();
		int columns = metadata.getColumnCount();
		for(int i = 1 ; i <= columns ; i ++) {
			System.out.print(i + ": " + metadata.getColumnName(i) + "/" + metadata.getColumnTypeName(i) + "    ");
		}
		
		System.out.println();
	}

	static void stopServer() {
		h2Server.stop();
	}

	public static void main(String[] args) throws Exception {
		
		startServer();
		
		initTestData();
		
		testMetaData();
		
		stopServer();

	}

}
