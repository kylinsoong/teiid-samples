package com.teiid.quickstart.mongodb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.resource.ResourceException;

import org.teiid.deployers.VirtualDatabaseException;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository.ConnectorManagerException;
import org.teiid.resource.adapter.mongodb.MongoDBManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.mongodb.MongoDBExecutionFactory;

import com.teiid.quickstart.util.JDBCUtil;

public class TestMongoDataSource {
	
	private static final String SERVERLIST = "10.66.218.46:27017" ;
	private static final String DBNAME = "mydb" ;
	
	static EmbeddedServer server = null;
	private static Connection conn = null;
	
	public void init() throws Exception {
		
		server = new EmbeddedServer();
		
		MongoDBExecutionFactory executionFactory = new MongoDBExecutionFactory();
		executionFactory.start();
		server.addTranslator("translator-mongodb", executionFactory);
		
		MongoDBManagedConnectionFactory managedconnectionFactory = new MongoDBManagedConnectionFactory();
		managedconnectionFactory.setRemoteServerList(SERVERLIST);
		managedconnectionFactory.setDatabase(DBNAME);
		server.addConnectionFactory("java:/mongoDS", managedconnectionFactory.createConnectionFactory());
		
		server.start(new EmbeddedConfiguration());
		server.deployVDB(new FileInputStream(new File("src/vdb/mongodb-vdb.xml")));
		conn = server.getDriver().connect("jdbc:teiid:nothwind", null);
		
		JDBCUtil.executeUpdate(conn, "DELETE FROM Customer");
		
		JDBCUtil.executeUpdate(conn, "INSERT INTO Customer(customer_id, FirstName, LastName) VALUES (1, 'Kylin', 'Soong')");
		JDBCUtil.executeUpdate(conn, "INSERT INTO Customer(customer_id, FirstName, LastName) VALUES (2, 'Kylin', 'Soong')");
		JDBCUtil.executeUpdate(conn, "INSERT INTO Customer(customer_id, FirstName, LastName) VALUES (3, 'Kylin', 'Soong')");
		
		JDBCUtil.executeQuery(conn, "SELECT * FROM Customer");
		
		
		JDBCUtil.close(conn);
	}

	public static void main(String[] args) throws Exception {
		
		TestMongoDataSource test = new TestMongoDataSource();
		test.init();
	}

}
