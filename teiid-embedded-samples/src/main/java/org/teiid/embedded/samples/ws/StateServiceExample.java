package org.teiid.embedded.samples.ws;

import java.io.File;
import java.io.FileInputStream;
import java.sql.CallableStatement;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stax.StAXSource;

import org.teiid.core.types.SQLXMLImpl;
import org.teiid.embedded.samples.ExampleBase;
import org.teiid.embedded.samples.util.JDBCUtil;
import org.teiid.resource.adapter.ws.WSManagedConnectionFactory;
import org.teiid.translator.ws.WSExecutionFactory;
import org.teiid.util.StAXSQLXML;

/**
 * 'StateServiceExample' depend on StateService, more details refer to
 * 		1. https://github.com/kylinsoong/jaxws/tree/master/stateService
 * 		2. http://ksoong.org/jaxws-stateservice/
 * 
 * @author kylin
 *
 */
public class StateServiceExample extends ExampleBase{
	
	static final String GET_ALL = "<GetAllStateInfo xmlns=\"http://www.teiid.org/stateService/\"/>";
	static final String GET_ONE = "<GetStateInfo xmlns=\"http://www.teiid.org/stateService/\"><stateCode xmlns=\"\">CA</stateCode></GetStateInfo>";

	@Override
	public void execute() throws Exception {
		
		init("translator-ws", new WSExecutionFactory());
		
		WSManagedConnectionFactory managedconnectionFactory = new WSManagedConnectionFactory();
		server.addConnectionFactory("java:/StateServiceWebSvcSource", managedconnectionFactory.createConnectionFactory());
		
		start(false);
		server.deployVDB(new FileInputStream(new File("vdb/webservice-vdb.xml")));
		conn = server.getDriver().connect("jdbc:teiid:StateServiceVDB", null);
		
		CallableStatement cStmt = conn.prepareCall("{call invoke(?, ?, ?, ?, ?)}");
		cStmt.setString(1, "SOAP11");
		cStmt.setString(2, "");
		cStmt.setObject(3, getSQLXML(GET_ALL));
		cStmt.setString(4, "http://localhost:8080/StateService/stateService/StateServiceImpl?WSDL");
		cStmt.setBoolean(5, Boolean.TRUE);
		cStmt.execute();
		StAXSQLXML xml = (StAXSQLXML) cStmt.getObject(1);
		List<String> namelist = getResult(xml.getSource(StAXSource.class).getXMLStreamReader());
		System.out.println(namelist);
		
		cStmt = conn.prepareCall("{call invoke(?, ?, ?, ?, ?)}");
		cStmt.setString(1, "SOAP11");
		cStmt.setString(2, "");
		cStmt.setObject(3, getSQLXML(GET_ONE));
		cStmt.setString(4, "http://localhost:8080/StateService/stateService/StateServiceImpl?WSDL");
		cStmt.setBoolean(5, Boolean.TRUE);
		cStmt.execute();
		xml = (StAXSQLXML) cStmt.getObject(1);
		namelist = getResult(xml.getSource(StAXSource.class).getXMLStreamReader());
		System.out.println(namelist);
		
		JDBCUtil.close(null, cStmt);
		
		tearDown();
		
	}
	
	private List<String> getResult(XMLStreamReader reader) throws XMLStreamException {
		List<String> stateNames = new ArrayList<String>();
		while (true) {
			if (reader.getEventType() == XMLStreamConstants.END_DOCUMENT) {
				break;
			}		
			if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
				String cursor = reader.getLocalName();
				if (cursor.equals("Name")) {
					reader.next();
					String value = reader.getText();
					stateNames.add(value);
				}
//				if (cursor.equals("Abbreviation")) {
//					reader.next();
//					String value = reader.getText();
//					System.out.print(value + " ");
//				}
//				if (cursor.equals("Capital")) {
//					reader.next();
//					String value = reader.getText();
//					System.out.print(value + " ");
//				}
//				if (cursor.equals("YearOfStatehood")) {
//					reader.next();
//					String value = reader.getText();
//					System.out.println(value + " ");
//				}
			}
			reader.next();
		}
		return stateNames;
	}

	private Object getSQLXML(String request) {
		return new SQLXMLImpl(request);
	}
	
	public static void main(String[] args) throws Exception {
		new StateServiceExample().execute();
	}

}
