package org.teiid.test.file.mongodb;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;

import org.teiid.resource.adapter.mongodb.MongoDBManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.JDBCUtil;
import org.teiid.translator.mongodb.MongoDBExecutionFactory;

public class Teiid3235Reproduce {
	
	static {
		System.setProperty("java.util.logging.config.file", "/home/kylin/tmp/logging.properties");
	}
	
	private static final String SERVERLIST = "10.66.218.46:27017" ;
	private static final String DBNAME = "mydb" ;
	
	static EmbeddedServer server = null;
	private static Connection conn = null;

	public static void main(String[] args) throws Exception {

		server = new EmbeddedServer();
		
		MongoDBExecutionFactory executionFactory = new MongoDBExecutionFactory();
		executionFactory.start();
		server.addTranslator("mongodb", executionFactory);
		
		MongoDBManagedConnectionFactory managedconnectionFactory = new MongoDBManagedConnectionFactory();
		managedconnectionFactory.setRemoteServerList(SERVERLIST);
		managedconnectionFactory.setDatabase(DBNAME);
		server.addConnectionFactory("java:/mongoDS", managedconnectionFactory.createConnectionFactory());
		
		server.start(new EmbeddedConfiguration());
		server.deployVDB(new FileInputStream(new File("vdb/mongo-vdb-3235.xml")));
		conn = server.getDriver().connect("jdbc:teiid:mongodb", null);
		
		JDBCUtil.executeQuery(conn, "SELECT * FROM BQT1.SmallA");
//		
		JDBCUtil.executeUpdate(conn, "INSERT INTO BQT1.SmallA VALUES(4, 4, '4', '4') ");
		
		JDBCUtil.close(conn);
		
//		System.out.println(conn);
	}

}
