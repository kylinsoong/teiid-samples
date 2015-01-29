package org.teiid.test.client;

import java.sql.Connection;

import org.teiid.test.JDBCUtil;

public class PortfolioClient {
	
	private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:Portfolio@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";

	public static void main(String[] args) throws Exception {
		
		for(int i = 0 ; i < 100 ; i ++) {
			try {
				test();
			} catch (Exception e) {
				
			}
		}

	}

	static void test() throws Exception {
		
		Connection conn = JDBCUtil.getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		
		JDBCUtil.executeQuery(conn, "SELECT * FROM Sheet1");
		
		JDBCUtil.executeQuery(conn, "SELECT * FROM StockPrices");
		
		JDBCUtil.executeQuery(conn, "SELECT * FROM Stock");
		
		JDBCUtil.executeQuery(conn, "SELECT * FROM PersonalHoldings");
		
		JDBCUtil.executeQuery(conn, "SELECT * FROM stockPricesMatView");
		
		JDBCUtil.executeQuery(conn, "SELECT * FROM product");
		
		JDBCUtil.executeQuery(conn, "select stock.* from (call MarketData.getTextFiles('*.txt')) f, TEXTTABLE(f.file COLUMNS symbol string, price bigdecimal HEADER) stock");
		
		JDBCUtil.executeQuery(conn, "select product.symbol, stock.price, company_name from product, (call MarketData.getTextFiles('*.txt')) f, TEXTTABLE(f.file COLUMNS symbol string, price bigdecimal HEADER) stock where product.symbol=stock.symbol");
		
		JDBCUtil.executeQuery(conn, "select x.* FROM (call native('select Shares_Count, MONTHNAME(Purchase_Date) from Holdings')) w, ARRAYTABLE(w.tuple COLUMNS \"Shares_Count\" integer, \"MonthPurchased\" string ) AS x");
		
		JDBCUtil.close(conn);
		
	}

}
