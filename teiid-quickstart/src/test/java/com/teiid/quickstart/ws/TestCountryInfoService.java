package com.teiid.quickstart.ws;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.util.Properties;

import javax.resource.ResourceException;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stax.StAXSource;

import org.junit.Test;
import org.teiid.adminapi.impl.ModelMetaData;
import org.teiid.adminapi.impl.SourceMappingMetadata;
import org.teiid.core.types.SQLXMLImpl;
import org.teiid.deployers.VirtualDatabaseException;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository.ConnectorManagerException;
import org.teiid.resource.adapter.ws.WSManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.ws.WSExecutionFactory;
import org.teiid.util.StAXSQLXML;

import com.teiid.quickstart.util.JDBCUtil;


public class TestCountryInfoService {
	
	static final String WSDL = "http://www.oorsprong.org/websamples.countryinfo/CountryInfoService.wso?WSDL" ;
	static final String ENDPORTNAME = "CountryInfoServiceSoap" ;
	static final String NAMESPACEURI = "http://www.oorsprong.org/websamples.countryinfo";
	static final String SERVICE = "CountryInfoService";
	
	static EmbeddedServer server = null;
	static Connection conn = null;
	
	@Test
	public void testInvokeProcedure() throws ResourceException, TranslatorException, VirtualDatabaseException, ConnectorManagerException, SQLException, XMLStreamException {
		
		server = new EmbeddedServer();
		
		WSExecutionFactory executionFactory = new WSExecutionFactory();
		executionFactory.start();
		server.addTranslator("translator-ws", executionFactory);
		
		WSManagedConnectionFactory managedconnectionFactory = new WSManagedConnectionFactory();
		server.addConnectionFactory("java:/CountryInfoService", managedconnectionFactory.createConnectionFactory());
		
		server.start(new EmbeddedConfiguration());
		
		ModelMetaData model = new ModelMetaData();
		model.setName("CountryInfoService");
		SourceMappingMetadata source = new SourceMappingMetadata();
		source.setName("webservice");
		source.setTranslatorName("translator-ws");
		source.setConnectionJndiName("java:/CountryInfoService");
		model.addSourceMapping(source);
		
		server.deployVDB("CountryInfoServiceVDB", model);
		
		conn = server.getDriver().connect("jdbc:teiid:CountryInfoServiceVDB", null);
		
		assertNotNull(conn);
		
		CallableStatement cStmt = conn.prepareCall("{call invoke(?, ?, ?, ?, ?)}");
		cStmt.setString(1, "SOAP11");
		cStmt.setString(2, "");
		cStmt.setObject(3, getSQLXML());
		cStmt.setString(4, WSDL);
		cStmt.setBoolean(5, Boolean.TRUE);
		
		cStmt.execute();
		
		StAXSQLXML xml = (StAXSQLXML) cStmt.getObject(1);
		
		System.out.println(xml);
		
		String result = getResult(xml.getSource(StAXSource.class).getXMLStreamReader());
		
		assertEquals("Beijing", result);
		
		JDBCUtil.close(cStmt);
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
	
	String xmlRequest = "<tns:CapitalCity xmlns:tns=\"http://www.oorsprong.org/websamples.countryinfo\"><sCountryISOCode>CNA</sCountryISOCode></tns:CapitalCity>";
	
	private SQLXML getSQLXML() {
		return new SQLXMLImpl(xmlRequest);
	}
	
	@Test
	public void testSQLXML () throws SQLException {
		assertEquals(xmlRequest, getSQLXML().getString());
	}

	@Test
	public void testCapitalCity_request() throws Exception {
		server = new EmbeddedServer();
		
		WSExecutionFactory executionFactory = new WSExecutionFactory();
		executionFactory.start();
		server.addTranslator("CountryInfoService_ws", executionFactory);
		
		WSManagedConnectionFactory managedconnectionFactory = new WSManagedConnectionFactory();
		managedconnectionFactory.setEndPointName(ENDPORTNAME);
		managedconnectionFactory.setNamespaceUri(NAMESPACEURI);
		managedconnectionFactory.setServiceName(SERVICE);
		managedconnectionFactory.setWsdl(WSDL);
		server.addConnectionFactory("CountryInfoService", managedconnectionFactory.createConnectionFactory());
		
		server.start(new EmbeddedConfiguration());
		
		server.deployVDBZip(new File("src/vdb/WebServiceVDB.vdb").toURI().toURL());
		
		conn = server.getDriver().connect("jdbc:teiid:WebServiceVDB", new Properties());
				
		CallableStatement cStmt = conn.prepareCall("{call CapitalCity_request(?)}");
		cStmt.setString(1, "CNA");
		
		boolean hasResult = cStmt.execute();
		
		if(hasResult) {
			ResultSet rs = cStmt.getResultSet();
			assertEquals(1, rs.getMetaData().getColumnCount());
			assertEquals("xml_out", rs.getMetaData().getColumnName(1));
			assertEquals("xml", rs.getMetaData().getColumnTypeName(1));
			rs.next();
			SQLXML xml = (SQLXML) rs.getObject(1);
			assertEquals(xmlRequest, xml.getString());
			JDBCUtil.close(rs, cStmt);
		}
	}	
	
	@Test
	public void testCapitalCity_response() throws Exception {
		server = new EmbeddedServer();
		
		WSExecutionFactory executionFactory = new WSExecutionFactory();
		executionFactory.start();
		server.addTranslator("CountryInfoService_ws", executionFactory);
		
		WSManagedConnectionFactory managedconnectionFactory = new WSManagedConnectionFactory();
		managedconnectionFactory.setEndPointName(ENDPORTNAME);
		managedconnectionFactory.setNamespaceUri(NAMESPACEURI);
		managedconnectionFactory.setServiceName(SERVICE);
		managedconnectionFactory.setWsdl(WSDL);
		server.addConnectionFactory("CountryInfoService", managedconnectionFactory.createConnectionFactory());
		
		server.start(new EmbeddedConfiguration());
		
		server.deployVDBZip(new File("src/vdb/WebServiceVDB.vdb").toURI().toURL());
		
		conn = server.getDriver().connect("jdbc:teiid:WebServiceVDB", new Properties());
		
		CallableStatement cStmt = conn.prepareCall("{call CapitalCity_response(?)}");
		cStmt.setObject(1, getSQLXML());
		
		boolean hasResult = cStmt.execute();
		
		if(hasResult) {
			ResultSet rs = cStmt.getResultSet();
			assertEquals(1, rs.getMetaData().getColumnCount());
			assertEquals("CapitalCityResult", rs.getMetaData().getColumnName(1));
			assertEquals("string", rs.getMetaData().getColumnTypeName(1));
			JDBCUtil.close(rs, cStmt);
		}
	}
	
	@Test
	public void testCapitalCity() throws Exception {
		
		server = new EmbeddedServer();
		
		WSExecutionFactory executionFactory = new WSExecutionFactory();
		executionFactory.start();
		server.addTranslator("CountryInfoService_ws", executionFactory);
		
		WSManagedConnectionFactory managedconnectionFactory = new WSManagedConnectionFactory();
		managedconnectionFactory.setEndPointName(ENDPORTNAME);
		managedconnectionFactory.setEndPoint(WSDL);
		managedconnectionFactory.setNamespaceUri(NAMESPACEURI);
		managedconnectionFactory.setServiceName(SERVICE);
		managedconnectionFactory.setWsdl(WSDL);
		server.addConnectionFactory("CountryInfoService", managedconnectionFactory.createConnectionFactory());
		
		server.start(new EmbeddedConfiguration());
		
		server.deployVDBZip(new File("src/vdb/WebServiceVDB.vdb").toURI().toURL());
		
		conn = server.getDriver().connect("jdbc:teiid:WebServiceVDB", new Properties());
		
		CallableStatement cStmt = conn.prepareCall("{call CapitalCity(?)}");
		cStmt.setString(1, "CNA");
		
		boolean hasResult = cStmt.execute();
		
		if(hasResult) {
			ResultSet rs = cStmt.getResultSet();
			assertEquals(1, rs.getMetaData().getColumnCount());
			assertEquals("CapitalCityResult", rs.getMetaData().getColumnName(1));
			assertEquals("string", rs.getMetaData().getColumnTypeName(1));
			rs.next();
			assertEquals("Beijing", rs.getString(1));
			JDBCUtil.close(rs, cStmt);
		}
	}

	public static void main(String[] args) throws Exception {
		
//		new TestCountryInfoService().testCapitalCity_request();
		
//		new TestCountryInfoService().testCapitalCity_response();
		
		new TestCountryInfoService().testCapitalCity();
		
//		new TestCountryInfoService().testInvokeProcedure();
	}

}
