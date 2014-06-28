package org.teiid.jboss.test;

import java.sql.Connection;

import com.jboss.teiid.client.util.JDBCUtil;


public class PermissionIssueReproduce {

	static final String JDBC_DRIVER = "org.postgresql.Driver";
	static final String JDBC_URL = "jdbc:postgresql://localhost:5432/products";
	static final String JDBC_USER = "pg_user";
	static final String JDBC_PASS = "pg_pass";

	public static void main(String[] args) throws Exception {
		
		Connection conn = JDBCUtil.getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);

		System.out.println(conn);
		
		conn.close();
	}

}
