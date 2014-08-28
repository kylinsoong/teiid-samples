package com.teiid.quickstart.excel;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;

import org.junit.AfterClass;
import org.junit.Test;
import org.teiid.resource.adapter.file.FileManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.excel.ExcelExecutionFactory;

import com.teiid.quickstart.util.JDBCUtil;

public class TestExcelDataSource {
	
	static EmbeddedServer server = null;
	private static Connection conn = null;
	
	protected void init() throws Exception {
		
		server = new EmbeddedServer();
		
		ExcelExecutionFactory executionFactory = new ExcelExecutionFactory();
		executionFactory.start();
		server.addTranslator("excel", executionFactory);
		
		FileManagedConnectionFactory managedconnectionFactory = new FileManagedConnectionFactory();
		managedconnectionFactory.setParentDirectory("src/file");
		server.addConnectionFactory("java:/excel-file", managedconnectionFactory.createConnectionFactory());
		
		server.start(new EmbeddedConfiguration());
		
		server.deployVDB(new FileInputStream(new File("src/vdb/excel-vdb.xml")));
		
		conn = server.getDriver().connect("jdbc:teiid:ExcelVDB", null);	
	}
	
	@Test
	public void testQuery() throws Exception {
		init();
		assertNotNull(conn);
		assertTrue(JDBCUtil.countResults(conn, "SELECT * FROM Sheet1") > 0);
		assertTrue(JDBCUtil.countResults(conn, "SELECT * FROM PersonalHoldings") > 0);
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
