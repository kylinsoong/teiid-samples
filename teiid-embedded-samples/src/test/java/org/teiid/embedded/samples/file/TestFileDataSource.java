package org.teiid.embedded.samples.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;

import org.junit.BeforeClass;
import org.junit.Test;
import org.teiid.embedded.samples.TestBase;
import org.teiid.embedded.samples.util.JDBCUtil;
import org.teiid.resource.adapter.file.FileManagedConnectionFactory;
import org.teiid.translator.file.FileExecutionFactory;

public class TestFileDataSource extends TestBase {
	
	@BeforeClass
	public static void init() throws Exception {
		
		init("file", new FileExecutionFactory());
		
		FileManagedConnectionFactory managedconnectionFactory = new FileManagedConnectionFactory();
		managedconnectionFactory.setParentDirectory("metadata");
		server.addConnectionFactory("java:/marketdata-file", managedconnectionFactory.createConnectionFactory());
		
		start(false);
		
		server.deployVDB(new FileInputStream(new File("vdb/files-vdb.xml")));
		
		conn = server.getDriver().connect("jdbc:teiid:FilesVDB", null);
	}
	
	
	@Test
	public void testQuery() throws Exception {
		assertNotNull(conn);
		assertEquals(10, JDBCUtil.countResults(conn, "SELECT * FROM Marketdata"));
		assertEquals(10, JDBCUtil.countResults(conn, "SELECT * FROM SYMBOLS"));
		assertEquals(2, JDBCUtil.countResults(conn, "SELECT * FROM Books"));
	}
	
}
