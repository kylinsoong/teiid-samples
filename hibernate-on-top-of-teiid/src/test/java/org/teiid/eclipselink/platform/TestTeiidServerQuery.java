package org.teiid.eclipselink.platform;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.teiid.adminapi.Model;
import org.teiid.adminapi.impl.ModelMetaData;
import org.teiid.adminapi.impl.SourceMappingMetadata;
import org.teiid.jdbc.FakeServer;
import org.teiid.translator.loopback.LoopbackExecutionFactory;

public class TestTeiidServerQuery {
	
	static FakeServer server;
	private static Connection conn;

	@BeforeClass
	public static void setUp() throws Exception {
		server = new FakeServer(true);
		
		LoopbackExecutionFactory loopy = new LoopbackExecutionFactory();
    	loopy.setRowCount(10);
    	loopy.start();
    	server.addTranslator("l", loopy);
    	
    	String DDL = "CREATE FOREIGN TABLE G1 (e1 string, e2 integer);";
    	ModelMetaData model = new ModelMetaData();
    	model.setName("TEMP");
    	model.setModelType(Model.Type.PHYSICAL);
    	model.setSchemaSourceType("DDL");
    	model.setSchemaText(DDL);
    	SourceMappingMetadata sm = new SourceMappingMetadata();
    	sm.setName("loopy");
    	sm.setTranslatorName("l");
    	model.addSourceMapping(sm);
    	
		server.deployVDB("test", model);
		conn = server.createConnection("jdbc:teiid:test"); 
	}
	
	@Test
	public void testConnection(){
		assertNotNull(conn);
	}
	
	@Test
	public void testQuery() throws Exception {
		JDBCUtil.executeQuery(conn, "SELECT * FROM G1");
	}

	@AfterClass
	public static void tearDown() throws Exception {
		conn.close();
		server.stop();
	}

}
