package org.teiid.test.client;

import static org.teiid.test.JDBCUtil.close;
import static org.teiid.test.JDBCUtil.executeQuery;
import static org.teiid.test.JDBCUtil.executeUpdate;
import static org.teiid.test.JDBCUtil.getDriverConnection;

import java.sql.Connection;

public class MatVDBClient {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:MatVDB@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";

    @SuppressWarnings("static-access")
    public static void main(String[] args) throws Exception {
        
        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        query(conn);
        
        executeUpdate(conn, "INSERT INTO PRODUCT (ID,SYMBOL,COMPANY_NAME) VALUES(2000,'RHT','Red Hat Inc')");
        Thread.currentThread().sleep(30000);
        query(conn);
        
        executeUpdate(conn, "DELETE FROM PRODUCT  WHERE ID = 2000");
        Thread.currentThread().sleep(30000);
        query(conn);
        
        close(conn);
    }

    private static void query(Connection conn) throws Exception {

        executeQuery(conn, "select * from MatView");
        
        executeQuery(conn, "select * from h2_test_mat");
        
        executeQuery(conn, "select * from mat_test_staging");
        
        executeQuery(conn, "select * from status");
    }

}
