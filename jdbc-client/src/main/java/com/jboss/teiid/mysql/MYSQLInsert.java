package com.jboss.teiid.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.jboss.teiid.client.util.JDBCUtil;

public class MYSQLInsert {

	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String JDBC_URL = "jdbc:mysql://localhost:3306/customer";
	static final String JDBC_USER = "jdv_user";
	static final String JDBC_PASS = "jdv_pass";
	
	static final String COL_A = "abcdefghabcd";
	static final String COL_B = "abcdefghigklmnopqrstabcdefghigklmnopqrst";
	static final String COL_C = "1234567890123456789012345678901234567890";
	
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
			} 
		}
		
		JDBCUtil.close(conn);
		JDBCUtil.close(pstmt);
		
		System.out.println("Insert " + count + " rows spend " + (System.currentTimeMillis() - start) + " ms");
	}
	
	public static void main(String[] args) throws Exception {

		init(10000 * 1200);
	}

}
