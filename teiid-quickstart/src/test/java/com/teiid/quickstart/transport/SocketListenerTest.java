package com.teiid.quickstart.transport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;

import javax.resource.ResourceException;

import org.teiid.deployers.VirtualDatabaseException;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository.ConnectorManagerException;
import org.teiid.net.CommunicationException;
import org.teiid.net.ConnectionException;
import org.teiid.net.HostInfo;
import org.teiid.net.TeiidURL;
import org.teiid.net.socket.SocketServerConnection;
import org.teiid.net.socket.SocketServerConnectionFactory;
import org.teiid.net.socket.UrlServerDiscovery;
import org.teiid.resource.adapter.file.FileManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.file.FileExecutionFactory;
import org.teiid.transport.SSLConfiguration;
import org.teiid.transport.SocketConfiguration;
import org.teiid.transport.SocketListener;
import org.teiid.transport.WireProtocol;

public class SocketListenerTest {
	
	private InetSocketAddress addr = new InetSocketAddress("localhost", 31000);
	SocketListener listener;
//	SSLConfiguration config = new SSLConfiguration();
	private SocketServerConnectionFactory sscf;
	
	public void test() throws CommunicationException, ConnectionException, VirtualDatabaseException, TranslatorException, ConnectorManagerException, FileNotFoundException, ResourceException, IOException {
//		startServer();
		Properties p = new Properties();
		TeiidURL teiidUrl = new TeiidURL(addr.getHostName(), addr.getPort(), false);
		teiidUrl.getHostInfo().add(new HostInfo(addr.getHostName(), addr.getPort()));
		String url = teiidUrl.getAppServerURL();
		p.setProperty(TeiidURL.CONNECTION.SERVER_URL, url); 
		p.setProperty(TeiidURL.CONNECTION.DISCOVERY_STRATEGY, UrlServerDiscovery.class.getName());
		p.setProperty(TeiidURL.CONNECTION.AUTO_FAILOVER, Boolean.TRUE.toString());
		if (sscf == null) {
			sscf = new SocketServerConnectionFactory();
			sscf.initialize(new Properties());
		}
		SocketServerConnection conn = sscf.getConnection(p);
		
		System.out.println(conn.isOpen(1000));
	}

	private void startServer() throws TranslatorException, ResourceException, VirtualDatabaseException, ConnectorManagerException, FileNotFoundException, IOException {

		EmbeddedServer server = new EmbeddedServer();
		
		FileExecutionFactory executionFactory = new FileExecutionFactory();
		executionFactory.start();
		server.addTranslator("file", executionFactory);
		
		FileManagedConnectionFactory managedconnectionFactory = new FileManagedConnectionFactory();
		managedconnectionFactory.setParentDirectory("src/file");
		server.addConnectionFactory("java:/marketdata-file", managedconnectionFactory.createConnectionFactory());
		
		SocketConfiguration s = new SocketConfiguration();
		InetSocketAddress addr = new InetSocketAddress("localhost", 31000);
		s.setBindAddress(addr.getHostName());
		s.setPortNumber(addr.getPort());
		s.setProtocol(WireProtocol.teiid);
		EmbeddedConfiguration config = new EmbeddedConfiguration();
		config.addTransport(s);
		server.start(config);
		
		server.deployVDB(new FileInputStream(new File("src/vdb/files-vdb.xml")));
	}

	public static void main(String[] args) throws CommunicationException, ConnectionException, VirtualDatabaseException, TranslatorException, ConnectorManagerException, FileNotFoundException, ResourceException, IOException {
		new SocketListenerTest().test();
	}

}
