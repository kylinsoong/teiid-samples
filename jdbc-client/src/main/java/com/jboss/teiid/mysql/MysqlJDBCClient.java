package com.jboss.teiid.mysql;

import java.sql.Connection;

import com.jboss.teiid.client.util.JDBCUtil;

public class MysqlJDBCClient {
	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String JDBC_URL = "jdbc:mysql://localhost:3306/brokerinfo";
	static final String JDBC_USER = "jdv_user";
	static final String JDBC_PASS = "jdv_pass";
	
	static final String SQL_BROKER_UPDATE = "UPDATE Broker SET LastName = 'Steve' WHERE Id = 'B1236'";
	static final String SQL_BROKERINFO_QUERY_BROKER = "SELECT * FROM Broker";
	static final String SQL_BROKERINFO_QUERY_CUSTOMER = "SELECT * FROM Customer";
	static final String SQL_BROKERINFO_QUERY_ALL = "SELECT Broker.Id, Broker.LastName, Broker.FirstName, Customer.ID FROM Broker INNER JOIN Customer ON Broker.Id=Customer.BrokerId";

	public static void main(String[] args) throws Exception {
		
		Connection conn = JDBCUtil.getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		
		try {
			JDBCUtil.executeUpdate(conn, SQL_BROKER_UPDATE);
			JDBCUtil.executeQuery(conn, SQL_BROKERINFO_QUERY_BROKER);
			JDBCUtil.executeQuery(conn, SQL_BROKERINFO_QUERY_CUSTOMER);
			JDBCUtil.executeQuery(conn, SQL_BROKERINFO_QUERY_ALL);
		} catch (Exception e) {
			throw e ;
		} finally {
			JDBCUtil.close(conn);
		}

	}

}
