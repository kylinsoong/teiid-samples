package com.jboss.teiid.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.jboss.teiid.client.util.JDBCUtil;

public class MYSQLInsert {

	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String JDBC_URL = "jdbc:mysql://localhost:3306/test";
	static final String JDBC_USER = "test_user";
	static final String JDBC_PASS = "test_pass";
	
	static final String COL_A = "12341234";
	static final String COL_B = "123412341234";
	static final String COL_C = "123412341234";
	
	static final String INSERT_SQL = "insert into SERIALTEST values(?, ?, ?, ?)";
	
	private static void init(int count) throws Exception {

		Connection conn = JDBCUtil.getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		conn.setAutoCommit(false);
		
		long start = System.currentTimeMillis();
		
		PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL);
		
		for(int i = 0 ; i < count ; i ++) {
			
			pstmt.setLong(1, i);
			pstmt.setString(2, COL_A);
			pstmt.setString(3, COL_B);
			pstmt.setString(4, COL_C);
			pstmt.addBatch();
			if((i + 1) % 1000 == 0){
				pstmt.executeBatch();
				conn.commit();
			} 
		}
		
		JDBCUtil.close(conn);
		JDBCUtil.close(pstmt);
		
		
		
		System.out.println("Insert " + count + " rows spend " + (System.currentTimeMillis() - start) + " ms");
	}
	
	public static void main(String[] args) throws Exception {

		init(100000);
	}

}
