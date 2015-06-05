package org.teiid.embedded.samples.ws;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.teiid.embedded.samples.TestBase;
import org.teiid.embedded.samples.util.JDBCUtil;
import org.teiid.resource.adapter.ws.WSManagedConnectionFactory;
import org.teiid.translator.ws.WSExecutionFactory;

@Ignore("Need RestService Running")
public class TestRESTWebServiceDataSource extends TestBase {
		
	@BeforeClass
	public static void init() throws Exception {
		
		init("translator-rest", new WSExecutionFactory());
		
		WSManagedConnectionFactory managedconnectionFactory = new WSManagedConnectionFactory();
		server.addConnectionFactory("java:/CustomerRESTWebSvcSource", managedconnectionFactory.createConnectionFactory());
		
		start(false);
		server.deployVDB(new FileInputStream(new File("vdb/restwebservice-vdb.xml")));
		conn = server.getDriver().connect("jdbc:teiid:restwebservice", null);
	}
	
	@Test
	public void testQuery() throws Exception {
		assertNotNull(conn);
		assertEquals(122, JDBCUtil.countResults(conn, "SELECT * FROM CustomersView"));
	}

}
