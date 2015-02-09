package org.teiid.test.client;

import java.sql.Connection;

import org.teiid.test.JDBCUtil;

public class HBaseCustomerClient {
	
	private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:ExampleVDB@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";

    public static void main(String[] args) throws Exception {
    	
    	Connection conn = JDBCUtil.getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
    	
    	JDBCUtil.executeQuery(conn, "SELECT * FROM Customer");
    	
    	JDBCUtil.close(conn);
    }
}
