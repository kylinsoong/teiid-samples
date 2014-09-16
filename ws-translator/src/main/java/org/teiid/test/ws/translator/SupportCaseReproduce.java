package org.teiid.test.ws.translator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLXML;

import org.teiid.core.types.SQLXMLImpl;

public class SupportCaseReproduce {
	
	private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
	private static final String JDBC_URL = "jdbc:teiid:CountryInfoServiceVDB@mm://localhost:31000;version=1";
	private static final String JDBC_USER = "user";
	private static final String JDBC_PASS = "user";

	public static void main(String[] args) throws Exception {

		Connection conn = JDBCUtil.getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		
		PreparedStatement stmt = conn.prepareStatement("EXEC invoke(?,?,?,?)");
		stmt.setString(1, "SOAP11");
		stmt.setString(2, "");
		stmt.setObject(3, getSQLXML());
		stmt.setString(4, "http://www.oorsprong.org/websamples.countryinfo/CountryInfoService.wso?WSDL");
		
		
		stmt.execute();
		
	}
	
	static String xmlRequest = "<tns:CapitalCity xmlns:tns=\"http://www.oorsprong.org/websamples.countryinfo\"><sCountryISOCode>CNA</sCountryISOCode></tns:CapitalCity>";
	
	private static SQLXML getSQLXML() {
		return new SQLXMLImpl(xmlRequest);
	}

}
