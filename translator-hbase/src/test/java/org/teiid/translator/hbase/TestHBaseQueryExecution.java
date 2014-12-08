package org.teiid.translator.hbase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.resource.ResourceException;

import org.junit.Test;
import org.teiid.cdk.api.TranslationUtility;
import org.teiid.cdk.unittest.FakeTranslationFactory;
import org.teiid.deployers.VirtualDatabaseException;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository.ConnectorManagerException;
import org.teiid.language.Command;
import org.teiid.resource.adapter.hbase.HBaseConnection;
import org.teiid.resource.adapter.hbase.HBaseManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.TranslatorException;

public class TestHBaseQueryExecution {
	
	@Test
	public void testConnection() throws ResourceException {
		HBaseManagedConnectionFactory connectionFactory = new HBaseManagedConnectionFactory();
		connectionFactory.setZkQuorum("localhost:2181");
		HBaseConnection connection = connectionFactory.createConnectionFactory().getConnection();
		
		TranslationUtility util = FakeTranslationFactory.getInstance().getExampleTranslationUtility();
		Command command = util.parseCommand("SELECT * FROM Customer");
		
		
		HBaseExecutionFactory executionFactory = new HBaseExecutionFactory();
//		HBaseQueryExecution execution = executionFactory.createResultSetExecution(command, null, null, connection);
		
		System.out.println(command);
	}
	
	static Connection conn = null;
	
	public static void main(String[] args) throws Exception {
		
		EmbeddedServer server = new EmbeddedServer();
		
		HBaseExecutionFactory executionFactory = new HBaseExecutionFactory();
		executionFactory.start();
		server.addTranslator("translator-hbase", executionFactory);
		
		HBaseManagedConnectionFactory managedconnectionFactory = new HBaseManagedConnectionFactory();
		managedconnectionFactory.setZkQuorum("localhost:2181");
		server.addConnectionFactory("java:/hbaseDS", managedconnectionFactory.createConnectionFactory());
		
		server.start(new EmbeddedConfiguration());
		server.deployVDB(new FileInputStream(new File("src/test/resources/hbase-vdb.xml")));
		Connection conn = server.getDriver().connect("jdbc:teiid:hbasevdb", null);
		
		JDBCUtil.executeQuery(conn, "select * from Customer");
		
		JDBCUtil.close(conn);
		
	}

}
