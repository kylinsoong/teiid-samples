package com.teiid.quickstart.excel;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;

import org.junit.AfterClass;
import org.junit.Test;
import org.teiid.language.Command;
import org.teiid.resource.adapter.file.FileManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.excel.ExcelExecutionFactory;

import com.teiid.quickstart.FakeTranslationFactory;
import com.teiid.quickstart.TranslationUtility;
import com.teiid.quickstart.util.JDBCUtil;

public class TestExcelDataSource {
	
	static EmbeddedServer server = null;
	private static Connection conn = null;
	
	@Test
	public void testExecution() {
		
		String input = "SELECT Sheet1.ROW_ID, Sheet1.ACCOUNT_ID, Sheet1.PRODUCT_TYPE, Sheet1.PRODUCT_VALUE FROM Sheet1";
		TranslationUtility util = FakeTranslationFactory.getInstance().getExampleTranslationUtility();
        Command command = util.parseCommand(input);
        System.out.println(command);
        Class<?>[] expectedColumnTypesClasses;
	}
	
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
	
	public static void main(String[] args) throws Exception {
		TestExcelDataSource test = new TestExcelDataSource();
		test.init();
		JDBCUtil.executeQuery(conn, "SELECT * FROM Sheet1");
		JDBCUtil.executeQuery(conn, "SELECT * FROM PersonalHoldings");
		JDBCUtil.close(conn);
	}

}
