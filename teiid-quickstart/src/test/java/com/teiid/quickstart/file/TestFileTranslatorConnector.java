package com.teiid.quickstart.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.util.Arrays;
import java.util.HashMap;

import javax.resource.cci.ConnectionFactory;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.teiid.language.Argument;
import org.teiid.language.Call;
import org.teiid.language.LanguageFactory;
import org.teiid.language.Literal;
import org.teiid.language.Argument.Direction;
import org.teiid.resource.adapter.file.FileConnectionImpl;
import org.teiid.resource.adapter.file.FileManagedConnectionFactory;
import org.teiid.resource.spi.BasicConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.runtime.EmbeddedServer.ConnectionFactoryProvider;
import org.teiid.translator.FileConnection;
import org.teiid.translator.ProcedureExecution;
import org.teiid.translator.TypeFacility;
import org.teiid.translator.file.FileExecutionFactory;
import org.teiid.transport.SocketConfiguration;
import org.teiid.transport.WireProtocol;

import com.teiid.quickstart.util.JDBCUtil;

public class TestFileTranslatorConnector {
	
	@Test
	public void testCall() {
		
		Literal literal = new Literal("marketdata.csv", TypeFacility.RUNTIME_TYPES.STRING);
		Argument argument = new Argument(Direction.IN, literal, TypeFacility.RUNTIME_TYPES.STRING, null);
		Call call = LanguageFactory.INSTANCE.createCall("getTextFiles", Arrays.asList(argument), null);
		assertEquals("getTextFiles", call.getProcedureName());
		assertNotNull(call.getArguments());
	}
	
	
	@Test
	public void testGetTextFiles() throws Exception {
		
		FileConnection conn = Mockito.mock(FileConnection.class);
		Mockito.stub(conn.getFile("marketdata.csv")).toReturn(new File("src/file/marketdata.csv"));
		assertTrue(conn.getFile("marketdata.csv").exists());
		
		FileExecutionFactory fileExecutionFactory = new FileExecutionFactory();
		Call call = fileExecutionFactory.getLanguageFactory().createCall("getTextFiles", Arrays.asList(new Argument(Direction.IN, new Literal("marketdata.csv", TypeFacility.RUNTIME_TYPES.STRING), TypeFacility.RUNTIME_TYPES.STRING, null)), null);
		ProcedureExecution procedureExecution = fileExecutionFactory.createProcedureExecution(call, null, null, conn);
		procedureExecution.execute();
		assertNotNull(procedureExecution.next());
	}
	
	@Test
	public void testFileExecutionFactory() throws Exception {
		
		FileConnection conn = new FileConnectionImpl("src/file", new HashMap<String, String> (), true);
		assertTrue(conn.getFile("marketdata.csv").exists());
		Literal literal = new Literal("marketdata.csv", TypeFacility.RUNTIME_TYPES.STRING);
		Argument argument = new Argument(Direction.IN, literal, TypeFacility.RUNTIME_TYPES.STRING, null);
		Call call = LanguageFactory.INSTANCE.createCall("getTextFiles", Arrays.asList(argument), null);
		FileExecutionFactory fileExecutionFactory = new FileExecutionFactory();
		ProcedureExecution pe = fileExecutionFactory.createProcedureExecution(call, null, null, conn);
		pe.execute();
		assertNotNull(pe.next());
	}
	
	@Test
	public void testFileMapping() throws Exception {
		FileManagedConnectionFactory fmcf = new FileManagedConnectionFactory();
		fmcf.setParentDirectory("src/file");
		fmcf.setFileMapping("x=marketdata.csv,y=noexist.csv");
		BasicConnectionFactory<FileConnectionImpl> bcf = fmcf.createConnectionFactory();
		FileConnection fc = (FileConnection) bcf.getConnection();
		File f = fc.getFile("x");
		assertEquals("src/file/marketdata.csv", f.getPath());
		assertTrue(f.exists());
		f = fc.getFile("y");
		assertEquals("src/file/noexist.csv", f.getPath());
		assertFalse(f.exists());
	}
	
	@Test
	public void testParentPaths() throws Exception {
		FileManagedConnectionFactory fmcf = new FileManagedConnectionFactory();
		fmcf.setParentDirectory("src/file");
		fmcf.setAllowParentPaths(true);
		BasicConnectionFactory<FileConnectionImpl> bcf = fmcf.createConnectionFactory();
		FileConnection fc = bcf.getConnection();
		File f = fc.getFile(".." + File.separator + "file");
		assertTrue(f.exists());
		assertTrue(f.isDirectory());
	}
	
	static EmbeddedServer server = null;
	private static Connection conn = null;

	@BeforeClass
	public static void setUp() throws Exception {
			
	}
	
	protected void init() throws Exception {
		
		server = new EmbeddedServer();
		
		FileExecutionFactory executionFactory = new FileExecutionFactory();
		server.addTranslator("file", executionFactory);
		
		FileManagedConnectionFactory fileManagedconnectionFactory = new FileManagedConnectionFactory();
		fileManagedconnectionFactory.setParentDirectory("src/file");
		ConnectionFactory connectionFactory = fileManagedconnectionFactory.createConnectionFactory();
		ConnectionFactoryProvider<ConnectionFactory> connectionFactoryProvider = new EmbeddedServer.SimpleConnectionFactoryProvider<ConnectionFactory>(connectionFactory);
		server.addConnectionFactoryProvider("java:/marketdata-file", connectionFactoryProvider);
		
		SocketConfiguration s = new SocketConfiguration();
		InetSocketAddress addr = new InetSocketAddress("localhost", 31000);
		s.setBindAddress(addr.getHostName());
		s.setPortNumber(addr.getPort());
		s.setProtocol(WireProtocol.teiid);
		EmbeddedConfiguration config = new EmbeddedConfiguration();
		config.addTransport(s);
		server.start(config);
		
		server.deployVDB(new FileInputStream(new File("src/vdb/marketdata-vdb.xml")));
		
		conn = JDBCUtil.getDriverConnection("org.teiid.jdbc.TeiidDriver", "jdbc:teiid:Marketdata@mm://localhost:31000;version=1", "", "");
	}
	
	
	@Test
	public void testQuery() throws Exception {
		init();
		assertNotNull(conn);
		String query = "SELECT * FROM Marketdata";
		assertEquals(10, JDBCUtil.countResults(conn, query));
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
		
		TestFileTranslatorConnector test = new TestFileTranslatorConnector();
		test.init();
		JDBCUtil.executeQuery(conn, "SELECT * FROM Marketdata");
	}
	
	

}
