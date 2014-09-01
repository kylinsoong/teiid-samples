package com.jboss.teiid.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBCSimpleClient {
	
	public static void main(String[] args) throws Exception {
		
		Class.forName("org.teiid.jdbc.TeiidDriver");
		Connection conn = DriverManager.getConnection("jdbc:teiid:Marketdata@mm://localhost:31000;version=1", "user", "user");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM Marketdata");
		int columns = rs.getMetaData().getColumnCount();
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
		
		rs.close();
		stmt.close();
		conn.close();
	}

}
