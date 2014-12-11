package org.teiid.supportcase;

import java.sql.Connection;

public class SupportCase01301617 {
	
	private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
	private static final String JDBC_URL = "jdbc:teiid:FilesVDB@mm://localhost:31000;version=1";
	private static final String JDBC_USER = "user";
	private static final String JDBC_PASS = "user";
	
	static Connection conn;

	public static void main(String[] args) throws Exception {
		
		conn = JDBCUtil.getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		
		JDBCUtil.executeQuery(conn, "SELECT COUNT(1), sayHello('Teiid'), celsiusToFahrenheit(1.23) FROM Marketdata");
		
		JDBCUtil.close(conn);

	}
}
