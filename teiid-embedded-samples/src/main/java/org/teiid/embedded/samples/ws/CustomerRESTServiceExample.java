package org.teiid.embedded.samples.ws;

import java.io.File;
import java.io.FileInputStream;

import org.teiid.embedded.samples.ExampleBase;
import org.teiid.embedded.samples.util.JDBCUtil;
import org.teiid.resource.adapter.ws.WSManagedConnectionFactory;
import org.teiid.translator.ws.WSExecutionFactory;

/**
 * More details about 'CustomerRESTService' please refer to
 * 		https://github.com/kylinsoong/jaxrs/tree/master/customer
 * 		http://ksoong.org/jaxrs-customers/
 * 
 * @author kylin
 *
 */
public class CustomerRESTServiceExample extends ExampleBase {

	@Override
	public void execute() throws Exception {

		init("translator-rest", new WSExecutionFactory());
		
		WSManagedConnectionFactory managedconnectionFactory = new WSManagedConnectionFactory();
		server.addConnectionFactory("java:/CustomerRESTWebSvcSource", managedconnectionFactory.createConnectionFactory());
		
		start(false);
		server.deployVDB(new FileInputStream(new File("vdb/restwebservice-vdb.xml")));
		conn = server.getDriver().connect("jdbc:teiid:restwebservice", null);
		
		JDBCUtil.executeQuery(conn, "SELECT * FROM CustomersView");
		
		tearDown();
	}

	public static void main(String[] args) throws Exception {
		new CustomerRESTServiceExample().execute();
	}

}
