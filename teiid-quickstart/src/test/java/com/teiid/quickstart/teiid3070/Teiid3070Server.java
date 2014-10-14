package com.teiid.quickstart.teiid3070;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

import javax.resource.ResourceException;
import javax.resource.cci.ConnectionFactory;

import org.teiid.resource.adapter.file.FileManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.runtime.EmbeddedServer.ConnectionFactoryProvider;
import org.teiid.translator.file.FileExecutionFactory;
import org.teiid.transport.SocketConfiguration;
import org.teiid.transport.SocketListener;
import org.teiid.transport.WireProtocol;


public class Teiid3070Server {
	
	MyEmbeddedServer server;
	
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
		InetSocketAddress addr = new InetSocketAddress("localhost", 31000);
		s1.setBindAddress(addr.getHostName());
		s1.setPortNumber(addr.getPort());
		s1.setProtocol(WireProtocol.teiid);
		
//		SocketConfiguration s2 = new SocketConfiguration();
//		InetSocketAddress addr2 = new InetSocketAddress("localhost", 41000);
//		s2.setBindAddress(addr.getHostName());
//		s2.setPortNumber(addr.getPort());
//		s2.setProtocol(WireProtocol.teiid);
		
		EmbeddedConfiguration config = new EmbeddedConfiguration();
		config.addTransport(s1);
//		config.addTransport(s2);
		server.start(config);
		
		server.deployVDB(new FileInputStream(new File("src/vdb/marketdata-vdb.xml")));
	}
	
	public void start() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while(true){
			String input = br.readLine();
			if("show".equals(input)) {
				for(SocketListener listener : server.getListeners()) {
					System.out.println(listener);
				}
			} else if("stop".equals(input)) {
				server.getListeners().get(0).stop();
			}  else if("exit".equals(input)){
				System.exit(0);
			}
			
		}
	}

	public static void main(String[] args) throws Exception {
		new Teiid3070Server().start();
	}

}
