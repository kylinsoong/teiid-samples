package com.jboss.teiid.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author bmincey
 * 
 */
public class XMLDataSourceClient {

	private static final String JDBC_URL = "jdbc:teiid:XMLDataSource_VDB@mm://localhost:31000;version=1";

	private static final String SQL = "SELECT * FROM ViewModel.new_table";

	/**
	 * 
	 */
	public XMLDataSourceClient() throws Exception {
		this.execute(getDriverConnection(), SQL);
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private Connection getDriverConnection() throws Exception {
		Class.forName("org.teiid.jdbc.TeiidDriver");

		return DriverManager.getConnection(XMLDataSourceClient.JDBC_URL, // URL
				"user", // username
				"user"); // password
	}

	/**
	 * 
	 * @param connection
	 * @param sql
	 * @throws Exception
	 */
	private void execute(Connection connection, String sql) throws Exception {
		try {
			Statement statement = connection.createStatement();

			ResultSet results = statement.executeQuery(sql);

			ResultSetMetaData metadata = results.getMetaData();

			int columns = metadata.getColumnCount();

			for (int row = 1; results.next(); row++) {
				System.out.print(row + ": ");
				for (int i = 0; i < columns; i++) {
					if (i > 0) {
						System.out.print(",");
					}
					System.out.print(results.getString(i + 1));
				}
				System.out.println();
			}

			results.close();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}
	
	/**
	 * 
	 */
	public static void main(String[] args) throws Exception {
		new XMLDataSourceClient();
	}

}
