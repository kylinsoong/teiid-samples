package com.jboss.teiid.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jboss.teiid.client.util.JDBCUtil;

/*
 * 
 * java -cp target/teiid-jdbc-client-1.0-SNAPSHOT.jar:target/dependency/mysql-connector-java-5.1.30.jar -Xms5096m -Xmx5096m -XX:MaxPermSize=512m -verbose:gc -Xloggc:gc.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps com.jboss.teiid.mysql.DeserializationCaculation 100000000
 * 
 * */
public class DeserializationCaculation {
	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String JDBC_URL = "jdbc:mysql://localhost:3306/customer";
	static final String JDBC_USER = "jdv_user";
	static final String JDBC_PASS = "jdv_pass";
	
	static final String COL_A = "abcdefghabcdefgh";
	static final String COL_B = "abcdefghigklmnopqrstabcdefghigklmnopqrst";
	static final String COL_C = "1234567890123456789012345678901234567890";
	
	static final String INSERT_SQL = "insert into PERFTEST values(?, ?, ?, ?)";
	
	static Integer[] array = new Integer[] {1000, 5000, 10000, 30000, 50000, 70000, 90000, 100000, 300000, 500000, 700000, 900000, 1000000, 3000000, 5000000, 7000000, 9000000, 10000000, 20000000, 30000000, 40000000, 50000000, 60000000, 70000000, 80000000, 90000000, 100000000, 200000000, 300000000, 400000000, 500000000, 600000000, 700000000, 800000000, 900000000, 1000000000};

	public static void main(String[] args) throws Exception {
		
//		init(10000 * 1000);
		
		if(args.length > 0) {
			int size = Integer.parseInt(args[0]);
			System.out.println("Deserialize " + size + " bytes spend time: " + caculation(size) + " ms\n");
		} else {
			caculation();
		}

	}



	private static void caculation() throws Exception {
		
		for(Integer size : array) {
			System.out.println("Deserialize " + size + " bytes spend time: " + caculation(size) + " ms\n");
		}
	}



	private static long caculation(int size) throws Exception {
		
		int rows = size / 100;
		String query = "SELECT * FROM PERFTEST WHERE id < " + rows;
		System.out.println("Query SQL: " + query);
		
		
		
		return caculation(query);
		
//		long sum = 0;
//		for(int i = 0 ; i < 10 ; i ++) {
//			Thread.currentThread().sleep(1000 * 20);
//			sum += caculation(query);
//		}
//
//		return sum/10;
	}



	private static long caculation(String query) throws Exception {

		Connection conn = JDBCUtil.getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		Statement stmt = conn .createStatement();
		ResultSet rs = null;
		long time = 0;
		
		try {
			rs = stmt.executeQuery(query);
			Thread.currentThread().sleep(1000 * 5);
			System.out.println("Start count: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(System.currentTimeMillis())));
			long start = System.currentTimeMillis();
			while(rs.next()) {
				rs.getInt(1);
				rs.getString(2);
				rs.getString(3);
				rs.getString(4);
			}
			time = System.currentTimeMillis() - start;
			System.out.println("End count: " +  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(System.currentTimeMillis())));
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
