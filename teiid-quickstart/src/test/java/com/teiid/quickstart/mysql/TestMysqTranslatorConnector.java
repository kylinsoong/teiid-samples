package com.teiid.quickstart.mysql;

import java.sql.Connection;

import javax.sql.DataSource;

import org.junit.AfterClass;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.runtime.EmbeddedServer.ConnectionFactoryProvider;
import org.teiid.translator.jdbc.mysql.MySQL5ExecutionFactory;

public class TestMysqTranslatorConnector {
	
	static EmbeddedServer server = null;
	private static Connection conn = null;
	
	protected void init() throws Exception {
		
		server = new EmbeddedServer();
		
		MySQL5ExecutionFactory executionFactory = new MySQL5ExecutionFactory();
		executionFactory.setSupportsDirectQueryProcedure(true);
		server.addTranslator("translator-mysql", executionFactory);
		
		ConnectionFactoryProvider<DataSource> provider = new EmbeddedServer.SimpleConnectionFactoryProvider<DataSource>(null);
		server.addConnectionFactoryProvider("java:/accounts-ds", provider);
		
		EmbeddedConfiguration config = new EmbeddedConfiguration();
		
		
		server.start(config);
		
		
	}

	
	@AfterClass
	public static void tearDown() throws Exception {
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
