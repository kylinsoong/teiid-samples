package com.jboss.teiid.client;

import java.sql.Connection;

import com.jboss.teiid.client.util.JDBCUtil;

/**
 * 
 * @author ksoong
 * 
 */
public class XMLDataSourceClient {

	static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
	static final String JDBC_URL = "jdbc:teiid:Books_VDB@mm://localhost:31000;version=1";
	static final String JDBC_USER = "user";
	static final String JDBC_PASS = "user";

	static final String SQL_QUERY = "SELECT * FROM ViewModel.Books";
	
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
