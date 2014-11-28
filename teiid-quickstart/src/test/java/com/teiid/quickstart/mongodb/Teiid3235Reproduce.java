package com.teiid.quickstart.mongodb;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;


import org.teiid.resource.adapter.mongodb.MongoDBManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.mongodb.MongoDBExecutionFactory;

import com.teiid.quickstart.util.JDBCUtil;

public class Teiid3235Reproduce {
	
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
		server.deployVDB(new FileInputStream(new File("src/vdb/mongo-vdb-3235.xml")));
		conn = server.getDriver().connect("jdbc:teiid:mongodb", null);
		
		JDBCUtil.executeQuery(conn, "SELECT IntKey FROM BQT1.SmallA");
		JDBCUtil.executeQuery(conn, "SELECT IntKey FROM BQT1.SmallA GROUP BY IntKey");
//		JDBCUtil.executeQuery(conn, "SELECT IntKey, IntNum FROM BQT1.SmallA GROUP BY IntKey, IntNum");
//		JDBCUtil.executeQuery(conn, "SELECT intKey, SUM(IntNum) FROM BQT1.SmallA GROUP BY intKey");
		
		JDBCUtil.close(conn);
		
//		System.out.println(conn);
	}

}
