package org.teiid.translator.hbase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class PhoenixUtils {
	
	public static void hbaseTableMapping(Connection conn, String tname, String rname, Map<String, List<String>> quaMap) throws SQLException{
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE IF NOT EXISTS").append(" ").append("\"" + tname + "\"");
		sb.append(" (").append(rname).append(" ").append("VARCHAR PRIMARY KEY,");
		for(String family : quaMap.keySet()) {
			for(String qualifier: quaMap.get(family)) {
				sb.append(" ");
				sb.append("\"").append(family).append("\"");
				sb.append(".");
				sb.append("\"").append(qualifier).append("\"");
				sb.append(" ");
				sb.append("VARCHAR,");
			}
		}
		String ddl = sb.toString();
		ddl = ddl.substring(0, ddl.length() - 1) + ")";
		if(executeUpdate(conn, ddl)){
			System.out.println("Mapping HBase Table " + tname + ": " + ddl);
		}
	}
	
	public static boolean executeUpdate(Connection conn, String sql) throws SQLException {
		
		
		Statement stmt = null;
		
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			throw e;
		} finally {
			close(stmt);
		}
		
		return true ;
	}
	
	public static void close(Statement stmt) {

		if(null != stmt) {
			try {
				stmt.close();
				stmt = null;
			} catch (SQLException e) {
				
			}
		}
	}
	
	public static Connection getPhoenixConnection(String zkQuorum) throws Exception {
		Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
		String connectionURL = "jdbc:phoenix:" + zkQuorum;
		Connection conn = DriverManager.getConnection(connectionURL);
		conn.setAutoCommit(true);
		return conn;
	}

}
