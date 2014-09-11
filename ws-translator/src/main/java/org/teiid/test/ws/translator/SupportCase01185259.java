package org.teiid.test.ws.translator;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;

import org.teiid.resource.adapter.ws.WSManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.ws.WSExecutionFactory;

public class SupportCase01185259 {
	
	static final String ENDPOINT = "http://localhost:8080/CustomerRESTWebSvc/MyRESTApplication/customerList";
	
	static EmbeddedServer server = null;
	static Connection conn = null;

	public static void main(String[] args) throws Exception {

		init();
		
		JDBCUtil.executeQuery(conn, "SELECT * FROM CustomersView");
		
		tearDown();
	}

	private static void init() throws Exception {
		server = new EmbeddedServer();
		
		WSExecutionFactory factory = new WSExecutionFactory();
		factory.start();
		server.addTranslator("translator-rest", factory);
		
		WSManagedConnectionFactory managedconnectionFactory = new WSManagedConnectionFactory();
		managedconnectionFactory.setEndPoint(ENDPOINT);
		server.addConnectionFactory("java:/CustomerRESTWebSvcSource", managedconnectionFactory.createConnectionFactory());
	
		server.start(new EmbeddedConfiguration());
		server.deployVDB(new FileInputStream(new File("vdb/restwebservice-vdb.xml")));
		conn = server.getDriver().connect("jdbc:teiid:restwebservice", null);
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
