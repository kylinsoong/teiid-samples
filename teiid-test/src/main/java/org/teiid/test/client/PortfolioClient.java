package org.teiid.test.client;

import java.sql.Connection;

import org.teiid.test.JDBCUtil;

public class PortfolioClient {
	
	private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:Portfolio@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";

	public static void main(String[] args) throws Exception {
		test();
	}

	public static void test() throws Exception {
		
		Connection conn = JDBCUtil.getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		
		JDBCUtil.executeQueryPerf(conn, "SELECT * FROM Sheet1");
		
		JDBCUtil.executeQueryPerf(conn, "SELECT * FROM StockPrices");
//		
		JDBCUtil.executeQueryPerf(conn, "SELECT * FROM Stock");
//		
		JDBCUtil.executeQueryPerf(conn, "SELECT * FROM PersonalHoldings");
//		
//		JDBCUtil.executeQueryPerf(conn, "SELECT * FROM stockPricesMatView");
//		
//		JDBCUtil.executeQueryPerf(conn, "SELECT * FROM product");
//		
//		JDBCUtil.executeQueryPerf(conn, "select stock.* from (call MarketData.getTextFiles('*.txt')) f, TEXTTABLE(f.file COLUMNS symbol string, price bigdecimal HEADER) stock");
//		
//		JDBCUtil.executeQueryPerf(conn, "select product.symbol, stock.price, company_name from product, (call MarketData.getTextFiles('*.txt')) f, TEXTTABLE(f.file COLUMNS symbol string, price bigdecimal HEADER) stock where product.symbol=stock.symbol");
//		
//		JDBCUtil.executeQueryPerf(conn, "select x.* FROM (call native('select Shares_Count, MONTHNAME(Purchase_Date) from Holdings')) w, ARRAYTABLE(w.tuple COLUMNS \"Shares_Count\" integer, \"MonthPurchased\" string ) AS x");
		
		
		
		JDBCUtil.close(conn);
		
	}

}
