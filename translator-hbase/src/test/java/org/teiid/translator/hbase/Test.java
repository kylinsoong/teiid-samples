package org.teiid.translator.hbase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.resource.ResourceException;
import javax.transaction.TransactionManager;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.BeforeClass;
import org.mockito.Mockito;
import org.teiid.client.DQP;
import org.teiid.core.util.SimpleMock;
import org.teiid.deployers.VDBLifeCycleListener;
import org.teiid.deployers.VirtualDatabaseException;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository.ConnectorManagerException;
import org.teiid.jdbc.EmbeddedProfile;
import org.teiid.jdbc.TeiidDriver;
import org.teiid.net.CommunicationException;
import org.teiid.net.ConnectionException;
import org.teiid.net.ServerConnection;
import org.teiid.resource.adapter.hbase.HBaseManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.HBaseConnection;
import org.teiid.translator.TranslatorException;
import org.teiid.transport.ClientServiceRegistry;
import org.teiid.transport.ClientServiceRegistryImpl;
import org.teiid.transport.LocalServerConnection;

public class Test {
	
	static {
	      String pattern = "%d %-5p [%c] (%t) %m%n";
	      PatternLayout layout = new PatternLayout(pattern);
	      ConsoleAppender consoleAppender = new ConsoleAppender(layout);
	      Logger.getRootLogger().setLevel(Level.WARN);
	      Logger.getRootLogger().addAppender(consoleAppender);  
	      
	      System.setProperty("java.util.logging.config.file", "src/test/resources/logging.properties");
		}
	
	static class MyEmbeddedServer extends EmbeddedServer {
		public ClientServiceRegistryImpl getServices() {
			return services ;
		}
	}
	
	static MyEmbeddedServer server;
	
	@BeforeClass
	public static void init() throws TranslatorException, ResourceException, VirtualDatabaseException, ConnectorManagerException, FileNotFoundException, IOException, SQLException {
		
		if(null != server) {
			return;
		}
		
		server = new MyEmbeddedServer();
		
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

	public static void main(String[] args) throws SQLException, VirtualDatabaseException, TranslatorException, ConnectorManagerException, FileNotFoundException, ResourceException, IOException, CommunicationException, ConnectionException {
		
		test();
		
//		reproduce1();
		
//		reproduce2();
	}

	static void test() throws SQLException, VirtualDatabaseException, TranslatorException, ConnectorManagerException, FileNotFoundException, ResourceException, IOException {

		init();

		Driver driver = server.getDriver();
		
		Connection conn = driver.connect("jdbc:teiid:hbasevdb", null);		
		
		TestHBaseUtil.executeBatchedUpdateDataType(conn, "INSERT INTO TypesTest VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		
//		Statement stmt = conn.createStatement();
//		
//		ResultSet rs = stmt.executeQuery("SELECT * FROM Customer ORDER BY PK");
//		
//		ResultSetMetaData metadata = rs.getMetaData();
//		int columns = metadata.getColumnCount();
//		for (int row = 1; rs.next(); row++) {
//			System.out.print(row + ": ");
//			for (int i = 0; i < columns; i++) {
//				if (i > 0) {
//					System.out.print(", ");
//				}
//				System.out.print(rs.getObject(i + 1));
//			}
//			System.out.println();
//		}
		
				
//		TestHBaseUtil.close(rs, stmt);
		TestHBaseUtil.close(conn);
	}

	/*
	 * How Dynamic proxy created 'org.teiid.dqp.internal.process.DQPCore'
	 */
	static void reproduce1() throws VirtualDatabaseException, TranslatorException, ConnectorManagerException, FileNotFoundException, ResourceException, IOException, SQLException, CommunicationException, ConnectionException {
		init();
		
		Properties info = new Properties();
		info.put("ApplicationName", "JDBC");
		info.put("VirtualDatabaseName", "hbasevdb");
		
		ServerConnection conn = new LocalServerConnection(info, true){

			@Override
			protected ClientServiceRegistry getClientServiceRegistry(String transport) {
				return server.getServices();
			}
			
		};
		
		DQP dqp = conn.getService(DQP.class);
		
		System.out.println(dqp);
	}

	/*
	 * Test Proxy
	 */
	static void reproduce2() {
		DQP dqp = getService(DQP.class);
		System.out.println(dqp);
	}
	
	public static <T> T getService(final Class<T> iface) {
		Object obj = Proxy.newProxyInstance(Test.class.getClassLoader(), new Class[] {iface}, new InvocationHandler(){

			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				System.out.println(proxy.getClass());
				System.out.println(method);
				System.out.println(args);
				return null;
			}});
		
		System.out.println(obj);
		return iface.cast(obj);
	}

	static void reproduce3() {

	}

	static void reproduce4() {

	}

	static void reproduce5() {

	}

}
