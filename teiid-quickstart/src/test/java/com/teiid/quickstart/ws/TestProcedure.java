package com.teiid.quickstart.ws;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;

import com.teiid.quickstart.util.JDBCUtil;

public class TestProcedure {
	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String JDBC_URL = "jdbc:mysql://localhost:3306/customer";
	static final String JDBC_USER = "jdv_user";
	static final String JDBC_PASS = "jdv_pass";

	public static void main(String[] args) throws Exception {
		
		Connection conn = JDBCUtil.getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		
		CallableStatement cStmt = conn.prepareCall("{call employee_hos(?, ?)}");
		cStmt.setString(1, "BPM");
		cStmt.registerOutParameter(2, Types.INTEGER);
		
		boolean hadResults = cStmt.execute();
		while (hadResults){
			ResultSet rs = cStmt.getResultSet();
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
			hadResults = cStmt.getMoreResults();
		}
		
		int outputValue = cStmt.getInt(2);
		System.out.println(outputValue);
		
		JDBCUtil.close(cStmt);
		JDBCUtil.close(conn);
	}

}
