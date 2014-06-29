package org.teiid.jboss.test;

import java.sql.Connection;

import com.jboss.teiid.client.util.JDBCUtil;



public class PermissionIssueReproduce {

	static final String JDBC_DRIVER = "org.postgresql.Driver";
	static final String JDBC_URL = "jdbc:postgresql://localhost:5432/products";
	static final String JDBC_USER = "postgres";
	static final String JDBC_PASS = "redhat";
	
	static final String SQL_QUERY = "SELECT * FROM productdata";

	public static void main(String[] args) throws Exception {
		
		Connection conn = JDBCUtil.getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);

		try {
			JDBCUtil.executeQuery(conn, SQL_QUERY);
		} catch (Exception e) {
			throw e ;
		} finally {
			conn.close();
		}

	}

}
