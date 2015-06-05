package org.teiid.embedded.samples.ws;

import java.io.File;
import java.io.FileInputStream;

import org.teiid.embedded.samples.ExampleBase;
import org.teiid.resource.adapter.ws.WSManagedConnectionFactory;
import org.teiid.translator.ws.WSExecutionFactory;

/**
 * 'CountryInfoServiceExample' depend on
 * 		http://www.oorsprong.org/websamples.countryinfo/CountryInfoService.wso?WSDL
 * 
 * @author kylin
 *
 */
public class CountryInfoServiceExample extends ExampleBase {

	@Override
	public void execute() throws Exception {
		
		init("translator-ws", new WSExecutionFactory());
		
		WSManagedConnectionFactory managedconnectionFactory = new WSManagedConnectionFactory();
		server.addConnectionFactory("java:/SOAPWebServiceSource", managedconnectionFactory.createConnectionFactory());
		
		start(false);
		server.deployVDB(new FileInputStream(new File("vdb/SOAPService-vdb.xml")));
		conn = server.getDriver().connect("jdbc:teiid:SOAPServiceVDB", null);
		
		System.out.println(conn);
	}

	public static void main(String[] args) throws Exception {
		new CountryInfoServiceExample().execute();
	}

}
