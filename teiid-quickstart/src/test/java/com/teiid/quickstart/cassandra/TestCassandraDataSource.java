package com.teiid.quickstart.cassandra;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;

import javax.resource.ResourceException;

import org.teiid.resource.adapter.cassandra.CassandraManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.cassandra.CassandraConnection;
import org.teiid.translator.cassandra.CassandraExecutionFactory;

import com.teiid.quickstart.util.JDBCUtil;

public class TestCassandraDataSource {
	
	private static final String ADDRESS = "10.66.218.46";
	private static final String KEYSPACE = "demo";
	
	static EmbeddedServer server = null;
	private static Connection conn = null;
	
	public void testTranalator() throws TranslatorException, ResourceException {
		
		CassandraExecutionFactory executionFactory = new CassandraExecutionFactory();
		executionFactory.start();
		
		CassandraManagedConnectionFactory managedconnectionFactory = new CassandraManagedConnectionFactory();
		managedconnectionFactory.setAddress(ADDRESS);
		managedconnectionFactory.setKeyspace(KEYSPACE);
		
		CassandraConnection connection = executionFactory.getConnection(managedconnectionFactory.createConnectionFactory(), null);
	}
	
	protected void init() throws Exception { 
		
		server = new EmbeddedServer();
		
		CassandraExecutionFactory executionFactory = new CassandraExecutionFactory();
		executionFactory.start();
		server.addTranslator("translator-cassandra", executionFactory);
		
		CassandraManagedConnectionFactory managedconnectionFactory = new CassandraManagedConnectionFactory();
		managedconnectionFactory.setAddress(ADDRESS);
		managedconnectionFactory.setKeyspace(KEYSPACE);
		server.addConnectionFactory("java:/demoCassandra", managedconnectionFactory.createConnectionFactory());
		
		server.start(new EmbeddedConfiguration());
		server.deployVDB(new FileInputStream(new File("src/vdb/cassandra-vdb.xml")));
		conn = server.getDriver().connect("jdbc:teiid:users", null);
		JDBCUtil.executeQuery(conn, "SELECT * FROM UsersView");
	}

	public static void main(String[] args) throws Exception {
		new TestCassandraDataSource().init();
	}

}
