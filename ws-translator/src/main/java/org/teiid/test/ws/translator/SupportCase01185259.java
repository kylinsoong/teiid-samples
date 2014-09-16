package org.teiid.test.ws.translator;

import java.io.File;
import java.io.FileInputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLXML;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stax.StAXSource;

import org.teiid.adminapi.impl.ModelMetaData;
import org.teiid.adminapi.impl.SourceMappingMetadata;
import org.teiid.core.types.SQLXMLImpl;
import org.teiid.resource.adapter.ws.WSManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.ws.WSExecutionFactory;
import org.teiid.util.StAXSQLXML;

public class SupportCase01185259 {
	
	static final String WSDL = "http://www.oorsprong.org/websamples.countryinfo/CountryInfoService.wso?WSDL" ;
	static final String ENDPORTNAME = "CountryInfoServiceSoap" ;
	static final String NAMESPACEURI = "http://www.oorsprong.org/websamples.countryinfo";
	static final String SERVICE = "CountryInfoService";
	
	static EmbeddedServer server = null;
	static Connection conn = null;

	public static void main(String[] args) throws Exception {

		test();
		
		tearDown();
	}

	protected static void test() throws Exception {
		server = new EmbeddedServer();
		
		WSExecutionFactory executionFactory = new WSExecutionFactory();
		executionFactory.start();
		server.addTranslator("translator-ws", executionFactory);
		
		WSManagedConnectionFactory managedconnectionFactory = new WSManagedConnectionFactory();
		server.addConnectionFactory("java:/CaseWebSvcSource", managedconnectionFactory.createConnectionFactory());
		
		server.start(new EmbeddedConfiguration());
		
		server.deployVDB(new FileInputStream(new File("vdb/webservice-vdb.xml")));
		
		conn = server.getDriver().connect("jdbc:teiid:CountryInfoServiceVDB", null);
		
		
		CallableStatement cStmt = conn.prepareCall("{call invoke(?, ?, ?, ?)}");
		cStmt.setString(1, "SOAP11");
		cStmt.setString(2, "");
		cStmt.setObject(3, getSQLXML());
		cStmt.setString(4, WSDL);
//		cStmt.setBoolean(5, Boolean.TRUE);
		
		boolean hadResults = cStmt.execute();
		
		SQLXML xml = (SQLXML) cStmt.getObject(1);
				
//		String result = getResult(xml.getSource(StAXSource.class).getXMLStreamReader());
		
		System.out.println(xml.getString());
		
	}
	
	private static String getResult(XMLStreamReader reader) throws XMLStreamException {
		String result = "";
		while (true) {
			if (reader.getEventType() == XMLStreamConstants.END_DOCUMENT) {
				break;
			}
			if(reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
				String cursor = reader.getLocalName();
				if(cursor.equals("CapitalCityResult")){
					reader.next();
					result = reader.getText();
					break;
				}
			}
			
			reader.next();
		}
		return result;
	}
	
	static String xmlRequest = "<tns:CapitalCity xmlns:tns=\"http://www.oorsprong.org/websamples.countryinfo\"><sCountryISOCode>CNA</sCountryISOCode></tns:CapitalCity>";
	
	private static SQLXML getSQLXML() {
		return new SQLXMLImpl(xmlRequest);
	}
	
	private static void tearDown() throws Exception {
		if(null != conn) {
			conn.close();
			conn = null;
		}
		if(null != server) {
			server.stop();
			server = null;
		}
	}

}
