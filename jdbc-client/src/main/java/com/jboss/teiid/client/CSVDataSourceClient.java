package com.jboss.teiid.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class CSVDataSourceClient {
	
	private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
	private static final String JDBC_URL = "jdbc:teiid:MarketData_VDB@mm://localhost:31000;version=1";
	private static final String JDBC_USER = "user";
	private static final String JDBC_PASS = "user";

	private static final String SQL_QUERY = "SELECT * FROM ViewModel.MarketData";
	
	private Connection getDriverConnection() throws Exception {
		Class.forName(JDBC_DRIVER);
		return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS); 
	}


	protected void executeQuery(Connection conn, String sql) throws Exception {
		
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
					System.out.print(rs.getString(i + 1));
				}
				System.out.println();
			}
		} catch (Exception e) {
			throw e ;
		} finally {
			if (null != rs) {
				rs.close();
			}
			if(null != stmt) {
				stmt.close();
			}
		}
		
	}

	public static void main(String[] args) throws Exception {

		CSVDataSourceClient client = new CSVDataSourceClient();
		
		Connection conn = client.getDriverConnection();
		
		try {
			client.executeQuery(conn, SQL_QUERY);
		} catch (Exception e) {
			throw e;
		} finally {
			conn.close();
		}
	}

}
