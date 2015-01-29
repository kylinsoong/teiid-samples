package org.teiid.test.client;

import java.sql.Connection;

import org.teiid.test.JDBCUtil;

public class HBaseClient {
	
	private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:hbasevdb@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";
    
//    JDBCUtil.executeQuery(conn, "");

	public static void main(String[] args) throws Exception {
		
		Connection conn = JDBCUtil.getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);

		JDBCUtil.executeQuery(conn, "SELECT * FROM Customer");
		
		JDBCUtil.executeQuery(conn, "SELECT city, amount FROM Customer");
		
		JDBCUtil.executeQuery(conn, "SELECT DISTINCT city FROM Customer");
		
		JDBCUtil.executeQuery(conn, "SELECT city, amount FROM Customer WHERE PK='105'");
		
		JDBCUtil.executeQuery(conn, "SELECT * FROM Customer WHERE PK BETWEEN '105' AND '108'");
		
		JDBCUtil.executeQuery(conn, "SELECT * FROM Customer WHERE PK='105' AND name='John White'");
		
		JDBCUtil.executeQuery(conn, "SELECT * FROM Customer ORDER BY PK");
		
		JDBCUtil.executeQuery(conn, "SELECT * FROM Customer ORDER BY name, city DESC");
		
		JDBCUtil.executeQuery(conn, "SELECT name, city, COUNT(PK) FROM Customer GROUP BY name, city");
		
		JDBCUtil.executeQuery(conn, "SELECT name, city, COUNT(PK) FROM Customer GROUP BY name, city HAVING COUNT(PK) > 1");
		
		JDBCUtil.close(conn);
	}

}
