package com.teiid.quickstart.ws;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Properties;

import javax.resource.ResourceException;
import javax.xml.transform.stax.StAXSource;
import javax.xml.ws.Dispatch;

import org.junit.Test;
import org.mockito.Mockito;
import org.teiid.cdk.CommandBuilder;
import org.teiid.dqp.internal.datamgr.RuntimeMetadataImpl;
import org.teiid.language.Call;
import org.teiid.metadata.MetadataFactory;
import org.teiid.metadata.Procedure;
import org.teiid.query.metadata.SystemMetadata;
import org.teiid.query.metadata.TransformationMetadata;
import org.teiid.query.unittest.RealMetadataFactory;
import org.teiid.resource.adapter.ws.WSManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.ExecutionContext;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.WSConnection;
import org.teiid.translator.ws.WSExecutionFactory;
import org.teiid.translator.ws.WSWSDLProcedureExecution;

public class TestWebServiceDataSource {
	
	@Test
	public void testExecution() throws ResourceException, TranslatorException {
		
		server = new EmbeddedServer();
		
		WSExecutionFactory executionFactory = new WSExecutionFactory();
		executionFactory.start();
		server.addTranslator("translator-ws", executionFactory);
		
		WSManagedConnectionFactory managedconnectionFactory = new WSManagedConnectionFactory();
		managedconnectionFactory.setEndPointName(PORT);
		managedconnectionFactory.setNamespaceUri(NAMESPACEURI);
		managedconnectionFactory.setServiceName(SERVICE);
		managedconnectionFactory.setWsdl(WSDL);
		server.addConnectionFactory("java:/CustomerWebSvcSource", managedconnectionFactory.createConnectionFactory());
		
		WSConnection connection = executionFactory.getConnection(managedconnectionFactory.createConnectionFactory(), null);
		
		Properties props = new Properties();
		MetadataFactory metadataFactory = new MetadataFactory("testvdb", 1, "x", SystemMetadata.getInstance().getRuntimeTypeMap(), props, null);
		executionFactory.getMetadata(metadataFactory, connection);
		
		for(Iterator<String> iterator = metadataFactory.getSchema().getProcedures().keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next() ;
			System.out.println(key + " -> " + metadataFactory.getSchema().getProcedures().get(key));
		}
		
		Procedure procedure = metadataFactory.getSchema().getProcedures().get("GetAllStateInfo");
		
		TransformationMetadata tm = RealMetadataFactory.createTransformationMetadata(metadataFactory.asMetadataStore(), "testvdb");
		RuntimeMetadataImpl rm = new RuntimeMetadataImpl(tm);
		
		CommandBuilder cb = new CommandBuilder(tm);
		
		Call call = (Call)cb.getCommand("call GetAllStateInfo()");
		
		WSWSDLProcedureExecution wpe = new WSWSDLProcedureExecution(call, rm, Mockito.mock(ExecutionContext.class), executionFactory, connection);
//		wpe.execute();
//		wpe.getOutputParameterValues();
	
	}
	
	@Test
	public void testWebserviceInvoke() throws TranslatorException, ResourceException, IOException {
		
		server = new EmbeddedServer();
		
		WSExecutionFactory executionFactory = new WSExecutionFactory();
		executionFactory.start();
		server.addTranslator("translator-ws", executionFactory);
		
		WSManagedConnectionFactory managedconnectionFactory = new WSManagedConnectionFactory();
		managedconnectionFactory.setEndPointName(PORT);
		managedconnectionFactory.setNamespaceUri(NAMESPACEURI);
		managedconnectionFactory.setServiceName(SERVICE);
		managedconnectionFactory.setWsdl(WSDL);
		server.addConnectionFactory("java:/CustomerWebSvcSource", managedconnectionFactory.createConnectionFactory());
		
		WSConnection connection = executionFactory.getConnection(managedconnectionFactory.createConnectionFactory(), null);
		Dispatch<StAXSource> dispatch = connection.createDispatch(StAXSource.class, executionFactory.getDefaultServiceMode());
	}
	
	static final String WSDL = "http://localhost:8080/StateService/stateService/StateServiceImpl?WSDL" ;
	static final String PORT = "StateServiceImplPort" ;
	static final String NAMESPACEURI = "http://www.teiid.org/stateService/";
	static final String SERVICE = "stateService";
	
	static EmbeddedServer server = null;
	static Connection conn = null;
	
	public void init() throws Exception {
		
		server = new EmbeddedServer();
		
		WSExecutionFactory executionFactory = new WSExecutionFactory();
		executionFactory.start();
		server.addTranslator("translator-ws", executionFactory);
		
		WSManagedConnectionFactory managedconnectionFactory = new WSManagedConnectionFactory();
		managedconnectionFactory.setEndPointName(PORT);
		managedconnectionFactory.setNamespaceUri(NAMESPACEURI);
		managedconnectionFactory.setServiceName(SERVICE);
		managedconnectionFactory.setWsdl(WSDL);
		server.addConnectionFactory("java:/CustomerWebSvcSource", managedconnectionFactory.createConnectionFactory());
		
//		WSConnection connection = executionFactory.getConnection(managedconnectionFactory.createConnectionFactory(), null);
		
		server.start(new EmbeddedConfiguration());
		server.deployVDB(new FileInputStream(new File("src/vdb/webservice-vdb.xml")));
		conn = server.getDriver().connect("jdbc:teiid:webservice", null);
		
		System.out.println(conn);
	}
	
	
	public static void main(String[] args) throws Exception {
		new TestWebServiceDataSource().testExecution();
	}

}
