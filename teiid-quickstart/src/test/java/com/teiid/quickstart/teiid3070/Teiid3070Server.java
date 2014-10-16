package com.teiid.quickstart.teiid3070;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.Properties;

import javax.resource.cci.ConnectionFactory;

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
import org.teiid.runtime.EmbeddedServer.ConnectionFactoryProvider;
import org.teiid.translator.file.FileExecutionFactory;
import org.teiid.transport.SocketConfiguration;
import org.teiid.transport.SocketListener;
import org.teiid.transport.WireProtocol;


public class Teiid3070Server {
	
	private MyEmbeddedServer server;
	
	private InetSocketAddress addr = new InetSocketAddress(0);
	
	SocketListener listener;
	SocketListener listener1;
	
	private SocketServerConnectionFactory sscf;
	
	public Teiid3070Server() throws Exception {
		server = new MyEmbeddedServer();
		
		FileExecutionFactory executionFactory = new FileExecutionFactory();
		server.addTranslator("file", executionFactory);
		
		FileManagedConnectionFactory fileManagedconnectionFactory = new FileManagedConnectionFactory();
		fileManagedconnectionFactory.setParentDirectory("src/file");
		ConnectionFactory connectionFactory = fileManagedconnectionFactory.createConnectionFactory();
		ConnectionFactoryProvider<ConnectionFactory> connectionFactoryProvider = new EmbeddedServer.SimpleConnectionFactoryProvider<ConnectionFactory>(connectionFactory);
		server.addConnectionFactoryProvider("java:/marketdata-file", connectionFactoryProvider);
		
		SocketConfiguration s1 = new SocketConfiguration();
		s1.setBindAddress("127.0.0.1");
		s1.setPortNumber(21000);
		s1.setProtocol(WireProtocol.teiid);
		
		SocketConfiguration s2 = new SocketConfiguration();
		s2.setBindAddress("127.0.0.1");
		s2.setPortNumber(31000);
		s2.setProtocol(WireProtocol.teiid);
		
		EmbeddedConfiguration config = new EmbeddedConfiguration();
		config.addTransport(s1);
		config.addTransport(s2);
		server.start(config);
		
		server.deployVDB(new FileInputStream(new File("src/vdb/marketdata-vdb.xml")));
	}
	
	public void start() throws Exception {
		
		initListener();
		
		SocketServerConnection conn = establishConnection();
		
		System.out.println(conn.isOpen(1000));
				
		listener.stop();
		
		Thread.currentThread().sleep(1000);
		
		System.out.println(conn.isOpen(1000));
		
		listener1.stop();
		
		Thread.currentThread().sleep(1000);
		
		System.out.println(conn.isOpen(1000));
		
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//		while(true){
//			String input = br.readLine();
//			if("show".equals(input)) {
//				for(SocketListener listener : server.getListeners()) {
//					System.out.println(listener);
//				}
//			} else if("stop".equals(input)) {
//				server.getListeners().get(0).stop();
//				System.out.println("Stoped");
//			}  else if("exit".equals(input)){
//				System.exit(0);
//			}
//			
//		}
	}

	private SocketServerConnection establishConnection() throws CommunicationException, ConnectionException {
//		listener1.stop();
		Properties p = new Properties();
		TeiidURL teiidUrl = new TeiidURL("127.0.0.1", 21000, false);
		teiidUrl.getHostInfo().add(new HostInfo("127.0.0.1", 31000));
		String url = teiidUrl.getAppServerURL();
		p.setProperty(TeiidURL.CONNECTION.SERVER_URL, url); 
		p.setProperty(TeiidURL.CONNECTION.DISCOVERY_STRATEGY, UrlServerDiscovery.class.getName());
		p.setProperty(TeiidURL.CONNECTION.AUTO_FAILOVER, Boolean.TRUE.toString());
		p.setProperty("ApplicationName", "JDBC");
		p.setProperty("version", "1");
//		p.setProperty("serverURL", "mm://localhost:31000");
		p.setProperty("user", "user");
		p.setProperty("password", "user");
		p.setProperty("VirtualDatabaseVersion", "1");
		p.setProperty("VirtualDatabaseName", "Marketdata");
		if (sscf == null) {
			sscf = new SocketServerConnectionFactory();
			sscf.initialize(new Properties());
		}
		return sscf.getConnection(p);
	}

	private void initListener() {
		listener = server.getListeners().get(0);
		listener1 = server.getListeners().get(1);
	}

	public static void main(String[] args) throws Exception {
		new Teiid3070Server().start();
	}

}
