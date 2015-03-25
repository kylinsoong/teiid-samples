package com.jboss.teiid.client;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.teiid.client.plan.PlanNode;
import org.teiid.jdbc.TeiidStatement;

import com.jboss.teiid.client.util.JDBCUtil;

public class PortfolioCient {
	
	private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
	private static final String JDBC_URL = "jdbc:teiid:Portfolio@mm://localhost:31000;version=1";
	private static final String JDBC_USER = "teiidUser";
	private static final String JDBC_PASS = "password1!";
	
	static String sql_sheet1 = "SELECT * FROM Sheet1";
	static String sql_StockPrices = "SELECT * FROM StockPrices";
	static String sql_PRODUCT = "SELECT * FROM PRODUCT";
	static String sql_Stock = "SELECT * FROM Stock";
	
	

	public static void main(String[] args) throws Exception {
		
		Connection conn = JDBCUtil.getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		
		subPlanStatistic(sql_sheet1, conn);
		subPlanStatistic(sql_StockPrices, conn);
		subPlanStatistic(sql_PRODUCT, conn);
		subPlanStatistic(sql_Stock, conn);
		
		JDBCUtil.close(conn);
	}
	
	static void subPlanStatistic(String sql, Connection conn) throws SQLException {
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.createStatement();
			stmt.execute("set showplan DEBUG");
			rs = stmt.executeQuery(sql);
			while(rs.next()){
//				System.out.println(rs.getLong(1));
			}
			TeiidStatement tstatement = stmt.unwrap(TeiidStatement.class);
            PlanNode queryPlan = tstatement.getPlanDescription();
            System.out.println(queryPlan.toXml());
		} finally {
			if(rs != null) {
				rs.close();
			}
			if(stmt != null) {
				stmt.close();
			}
		}
		
		System.out.println("\n\n");
		
	}

}
