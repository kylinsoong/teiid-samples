package com.jboss.teiid.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.jboss.teiid.client.util.JDBCUtil;

public class DeserializationCaculation {
	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String JDBC_URL = "jdbc:mysql://localhost:3306/customer";
	static final String JDBC_USER = "jdv_user";
	static final String JDBC_PASS = "jdv_pass";
	
	static final String COL_A = "abcdefghabcdefgh";
	static final String COL_B = "abcdefghigklmnopqrstabcdefghigklmnopqrst";
	static final String COL_C = "1234567890123456789012345678901234567890";
	
	static final String INSERT_SQL = "insert into PERFTEST values(?, ?, ?, ?)";
	
	static Integer[] array = new Integer[] {1000, 5000, 10000, 50000, 100000, 500000, 1000000, 5000000, 10000000, 50000000, 100000000, 500000000, 1000000000};

	public static void main(String[] args) throws Exception {
		
//		init(10000 * 10000);
		
		
		caculation();
	}



	private static void caculation() throws Exception {
		
		for(Integer size : array) {
			System.out.println("Deserialize " + " bytes spend time: " + caculation(size) + " ms\n");
		}
	}



	private static long caculation(int size) throws Exception {
		
		int rows = size / 100;
		String query = "SELECT * FROM PERFTEST WHERE id < " + rows;
		System.out.println("Query SQL: " + query);
		
		long sum = 0;
		for(int i = 0 ; i < 10 ; i ++) {
			sum += caculation(query);
		}

		return sum/10;
	}



	private static long caculation(String query) throws Exception {

		Connection conn = JDBCUtil.getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		Statement stmt = conn .createStatement();
		ResultSet rs = null;
		long time = 0;
		
		try {
			rs = stmt.executeQuery(query);
			long start = System.currentTimeMillis();
			while(rs.next()) {
				rs.getInt(1);
				rs.getString(2);
				rs.getString(3);
				rs.getString(4);
			}
			time = System.currentTimeMillis() - start;
		} catch (Exception e) {
			throw e;
		} finally {
			JDBCUtil.close(rs, stmt);
			JDBCUtil.close(conn);
		}
		return time;
	}



	private static void init(int count) throws Exception {

		Connection conn = JDBCUtil.getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		conn.setAutoCommit(false);
		
		long start = System.currentTimeMillis();
		
		PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL);
		
		for(int i = 0 ; i < count ; i ++) {
			
			pstmt.setInt(1, i);
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

}
