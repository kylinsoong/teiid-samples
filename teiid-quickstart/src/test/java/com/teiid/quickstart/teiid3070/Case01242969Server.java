package com.teiid.quickstart.teiid3070;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.sql.Connection;

import javax.resource.cci.ConnectionFactory;

import org.teiid.resource.adapter.file.FileManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.runtime.EmbeddedServer.ConnectionFactoryProvider;
import org.teiid.translator.file.FileExecutionFactory;
import org.teiid.transport.SocketConfiguration;
import org.teiid.transport.WireProtocol;

public class Case01242969Server {
	
	static EmbeddedServer server;
	static Connection conn;

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws Exception {
		
		server = new EmbeddedServer();
		
		FileExecutionFactory executionFactory = new FileExecutionFactory();
		server.addTranslator("file", executionFactory);
		
		FileManagedConnectionFactory fileManagedconnectionFactory = new FileManagedConnectionFactory();
		fileManagedconnectionFactory.setParentDirectory("src/file");
		ConnectionFactory connectionFactory = fileManagedconnectionFactory.createConnectionFactory();
		ConnectionFactoryProvider<ConnectionFactory> connectionFactoryProvider = new EmbeddedServer.SimpleConnectionFactoryProvider<ConnectionFactory>(connectionFactory);
		server.addConnectionFactoryProvider("java:/marketdata-file", connectionFactoryProvider);
		
		SocketConfiguration s = new SocketConfiguration();
		InetSocketAddress addr = new InetSocketAddress("localhost", 31000);
		s.setBindAddress(addr.getHostName());
		s.setPortNumber(addr.getPort());
		s.setProtocol(WireProtocol.teiid);
		EmbeddedConfiguration config = new EmbeddedConfiguration();
		config.addTransport(s);
		server.start(config);
		
		server.deployVDB(new FileInputStream(new File("src/vdb/marketdata-vdb.xml")));
		
		Thread.currentThread().sleep(Long.MAX_VALUE);
	}

}
