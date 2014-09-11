package com.teiid.quickstart.ws;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;

import org.junit.Test;
import org.teiid.resource.adapter.ws.WSManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.ws.WSExecutionFactory;

import com.teiid.quickstart.util.JDBCUtil;

public class TestRESTWebServiceDataSource {
	
	static EmbeddedServer server = null;
	static Connection conn = null;
	
	

	public static void main(String[] args) throws Exception {

		server = new EmbeddedServer();
		
		WSExecutionFactory executionFactory = new WSExecutionFactory();
		executionFactory.start();
		server.addTranslator("translator-rest", executionFactory);
		
		WSManagedConnectionFactory managedconnectionFactory = new WSManagedConnectionFactory();
		server.addConnectionFactory("java:/CustomerRESTWebSvcSource", managedconnectionFactory.createConnectionFactory());
		
		
		server.start(new EmbeddedConfiguration());
		server.deployVDB(new FileInputStream(new File("src/vdb/restwebservice-vdb.xml")));
		conn = server.getDriver().connect("jdbc:teiid:restwebservice", null);
		
		JDBCUtil.executeQuery(conn, "SELECT * FROM CustomersView");
		
		JDBCUtil.close(conn);
		
		server.stop();
	}

}
