package com.teiid.quickstart.client;

import java.sql.Connection;

import com.teiid.quickstart.util.JDBCUtil;


public class FilesVDBClient {
	
	private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
	private static final String JDBC_URL = "jdbc:teiid:FilesVDB@mm://localhost:31000;version=1";
	private static final String JDBC_USER = "user";
	private static final String JDBC_PASS = "user";

	private static final String SQL_QUERY = "SELECT * FROM Marketdata";


	public static void main(String[] args) throws Exception {

		Connection conn = JDBCUtil.getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		
		try {
			JDBCUtil.executeQuery(conn, SQL_QUERY);
		} catch (Exception e) {
			throw e;
		} finally {
			JDBCUtil.close(conn);
		}
	}

}
