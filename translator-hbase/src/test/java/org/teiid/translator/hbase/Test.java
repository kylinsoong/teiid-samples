package org.teiid.translator.hbase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;

import javax.resource.ResourceException;
import javax.transaction.TransactionManager;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.BeforeClass;
import org.mockito.Mockito;
import org.teiid.core.util.SimpleMock;
import org.teiid.deployers.VirtualDatabaseException;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository.ConnectorManagerException;
import org.teiid.jdbc.TeiidDriver;
import org.teiid.resource.adapter.hbase.HBaseManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.HBaseConnection;
import org.teiid.translator.TranslatorException;

public class Test {
	
	static {
	      String pattern = "%d %-5p [%c] (%t) %m%n";
	      PatternLayout layout = new PatternLayout(pattern);
	      ConsoleAppender consoleAppender = new ConsoleAppender(layout);
	      Logger.getRootLogger().setLevel(Level.WARN);
	      Logger.getRootLogger().addAppender(consoleAppender);  
	      
	      System.setProperty("java.util.logging.config.file", "src/test/resources/logging.properties");
		}
	
	static EmbeddedServer server;
	
	@BeforeClass
	public static void init() throws TranslatorException, ResourceException, VirtualDatabaseException, ConnectorManagerException, FileNotFoundException, IOException, SQLException {
		
		server = new EmbeddedServer();
		
		HBaseExecutionFactory executionFactory = new HBaseExecutionFactory();
		executionFactory.start();
		server.addTranslator("translator-hbase", executionFactory);
		
		HBaseManagedConnectionFactory managedconnectionFactory = new HBaseManagedConnectionFactory();
		managedconnectionFactory.setZkQuorum("localhost:2181");
		server.addConnectionFactory("java:/hbaseDS", managedconnectionFactory.createConnectionFactory());
		
		EmbeddedConfiguration config = new EmbeddedConfiguration();
//		config.setTransactionManager(SimpleMock.createSimpleMock(TransactionManager.class));
		server.start(config);
		server.deployVDB(new FileInputStream(new File("src/test/resources/hbase-vdb.xml")));
	}

	public static void main(String[] args) throws SQLException, VirtualDatabaseException, TranslatorException, ConnectorManagerException, FileNotFoundException, ResourceException, IOException {
		
		test();
		
		reproduce1();
		
		
	}

	private static void test() throws SQLException, VirtualDatabaseException, TranslatorException, ConnectorManagerException, FileNotFoundException, ResourceException, IOException {

		init();

		Driver driver = server.getDriver();
		
		Connection conn = driver.connect("jdbc:teiid:hbasevdb", null);		
		
		System.out.println(conn);
				
		TestHBaseUtil.close(conn);
	}

	static void reproduce1() {

	}

	static void reproduce2() {

	}

	static void reproduce3() {

	}

	static void reproduce4() {

	}

	static void reproduce5() {

	}

}
