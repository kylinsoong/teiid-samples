package org.teiid.drools.client;

import java.sql.Connection;

public class DroolsClient {
	
	private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
	private static final String JDBC_URL = "jdbc:teiid:DroolsVDB@mm://localhost:31000;version=1";
	private static final String JDBC_USER = "teiidUser";
	private static final String JDBC_PASS = "password1!";
	
	static Connection conn;

	public static void main(String[] args) throws Exception {
		
		conn = JDBCUtil.getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		
//		JDBCUtil.executeQuery(conn, "SELECT COUNT(1), sayHello('Teiid'), celsiusToFahrenheit(1.23) FROM FOO");
				
		JDBCUtil.executeQuery(conn, "SELECT performRuleOnData('org.jboss.teiid.drools.Message', 'Hello World', 0) FROM FOO");
		
		JDBCUtil.close(conn);

	}
}
