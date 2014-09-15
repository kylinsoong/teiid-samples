package com.teiid.quickstart.ws;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.teiid.resource.adapter.ws.WSManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.ws.WSExecutionFactory;


public class TestCountryInfoService {
	
	static final String WSDL = "http://www.oorsprong.org/websamples.countryinfo/CountryInfoService.wso?WSDL" ;
	static final String PORT = "CountryInfoServiceSoap" ;
	static final String NAMESPACEURI = "http://www.oorsprong.org/websamples.countryinfo";
	static final String SERVICE = "CountryInfoService";
	
	static EmbeddedServer server = null;
	static Connection conn = null;
	
	public void testCountryInfoService() throws Exception {
		server = new EmbeddedServer();
		
		WSExecutionFactory executionFactory = new WSExecutionFactory();
		executionFactory.start();
		server.addTranslator("CountryInfoService_ws", executionFactory);
		
		WSManagedConnectionFactory managedconnectionFactory = new WSManagedConnectionFactory();
//		managedconnectionFactory.setEndPointName(PORT);
		managedconnectionFactory.setEndPoint(PORT);
		managedconnectionFactory.setNamespaceUri(NAMESPACEURI);
		managedconnectionFactory.setServiceName(SERVICE);
		managedconnectionFactory.setWsdl(WSDL);
		server.addConnectionFactory("CountryInfoService", managedconnectionFactory.createConnectionFactory());
		
		server.start(new EmbeddedConfiguration());
		
		server.deployVDBZip(new File("src/vdb/WebServiceVDB.vdb").toURI().toURL());
		
		conn = server.getDriver().connect("jdbc:teiid:WebServiceVDB", new Properties());
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("EXEC CapitalCity('CNA')");
		System.out.println(rs.getMetaData().getColumnName(1) + " " + rs.getMetaData().getColumnTypeName(1));
		while(rs.next()) {
			System.out.println(rs.getObject(1));
		}
	}	

	public static void main(String[] args) throws Exception {
		new TestCountryInfoService().testCountryInfoService();
	}

}
