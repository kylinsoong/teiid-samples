package org.teiid.test.file;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;

import org.teiid.resource.adapter.file.FileManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.JDBCUtil;
import org.teiid.translator.file.FileExecutionFactory;


public class SimpleExample {

	public static void main(String[] args) throws Exception {
		EmbeddedServer server = new EmbeddedServer();
		
		FileExecutionFactory executionFactory = new FileExecutionFactory();
		executionFactory.start();
		server.addTranslator("file", executionFactory);
		
		FileManagedConnectionFactory managedconnectionFactory = new FileManagedConnectionFactory();
		managedconnectionFactory.setParentDirectory("src/file");
		server.addConnectionFactory("java:/marketdata-file", managedconnectionFactory.createConnectionFactory());
	
		EmbeddedConfiguration config = new EmbeddedConfiguration();
		server.start(config);
		server.deployVDB(new FileInputStream(new File("vdb/files-simple-vdb.xml")));
		
		Connection conn = server.getDriver().connect("jdbc:teiid:FilesVDB", null);
		
//		JDBCUtil.executeQuery(conn, "EXEC FilesVDB.getTextFiles('marketdata.csv')");
		JDBCUtil.executeQuery(conn, "SELECT * FROM Marketdata");
		JDBCUtil.close(conn);
	}

}
