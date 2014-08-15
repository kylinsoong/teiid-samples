package com.teiid.quickstart;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.teiid.adminapi.Model.Type;
import org.teiid.adminapi.impl.ModelMetaData;
import org.teiid.jdbc.TeiidDriver;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.runtime.EmbeddedServer.ConnectionFactoryProvider;

import com.teiid.quickstart.util.JDBCUtil;

public class EmbeddedServerTest {
	
	static EmbeddedServer embeddedServer;

	public static void main(String[] args) throws Exception {
		
		embeddedServer = new EmbeddedServer();
		
//		testDeploy();
		
//		testDeployZip();

//		testDeployDesignerZip();

//		testXMLDeploy();
		
		testASTQueries();

	}

	private static void testASTQueries() {
		embeddedServer.start(new EmbeddedConfiguration());
		
	}

	protected static void testXMLDeploy() throws Exception {
		embeddedServer.start(new EmbeddedConfiguration());
		embeddedServer.deployVDB(new FileInputStream(new File("vdb/sample-vdb.xml")));	
		
		Connection conn = embeddedServer.getDriver().connect("jdbc:teiid:test", null);
        JDBCUtil.executeQuery(conn, "select * from helloworld");  
        print(embeddedServer.getSchemaDdl("test", "test"));
        JDBCUtil.close(conn);	
	}

	private static void print(Object str) {
		System.out.println();
		System.out.println(str);
		System.out.println();	
	}

	protected static void testDeployDesignerZip() throws Exception {
		
		embeddedServer.start(new EmbeddedConfiguration());
		embeddedServer.deployVDBZip(new File("vdb/matviews.vdb").toURI().toURL());		
		Connection conn = embeddedServer.getDriver().connect("jdbc:teiid:matviews", null);
		
		JDBCUtil.executeQuery(conn, "select * from tables where schemaname='test'");
		print(embeddedServer.getSchemaDdl("matviews", "test"));
		JDBCUtil.close(conn);
	}

	protected static void testDeployZip() throws Exception {
	
		embeddedServer.start(new EmbeddedConfiguration());
		
		File f = new File("some.vdb");
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));
        out.putNextEntry(new ZipEntry("v1.ddl")); 
        out.write("CREATE VIEW helloworld as SELECT 'HELLO WORLD';".getBytes("UTF-8"));
        out.putNextEntry(new ZipEntry("META-INF/vdb.xml"));
        out.write("<vdb name=\"test\" version=\"1\"><model name=\"test\" type=\"VIRTUAL\"><metadata type=\"DDL-FILE\">/v1.ddl</metadata></model></vdb>".getBytes("UTF-8"));
        out.close();
        
        embeddedServer.deployVDBZip(f.toURI().toURL());
        Connection conn = embeddedServer.getDriver().connect("jdbc:teiid:test", null);
        
        JDBCUtil.executeQuery(conn, "select * from helloworld");
        print(embeddedServer.getSchemaDdl("test", "test"));
        JDBCUtil.close(conn);
        
	}

	protected static void testDeploy() throws Exception {
		
		EmbeddedConfiguration config = new EmbeddedConfiguration();
		config.setUseDisk(false);
		embeddedServer.start(config);
		
		MyTranslator translator = new MyTranslator();
		embeddedServer.addTranslator("translator", translator);
		
		final AtomicInteger counter = new AtomicInteger();
		ConnectionFactoryProvider<AtomicInteger> connectionFactoryProvider = new EmbeddedServer.SimpleConnectionFactoryProvider<AtomicInteger>(counter);
	
		embeddedServer.addConnectionFactoryProvider("connectionFactoryProvider", connectionFactoryProvider);
		
		ModelMetaData mmd = new ModelMetaData();
		mmd.setName("my-schema");
		mmd.addSourceMapping("x", "translator", "connectionFactoryProvider");
		
		ModelMetaData mmd1 = new ModelMetaData();
		mmd1.setName("virt");
		mmd1.setModelType(Type.VIRTUAL);
		mmd1.setSchemaSourceType("ddl");
		mmd1.setSchemaText("create view \"my-view\" OPTIONS (UPDATABLE 'true') as select * from \"my-table\"");
		
		embeddedServer.deployVDB("test", mmd, mmd1);
		
		TeiidDriver driver = embeddedServer.getDriver();
		Connection c = driver.connect("jdbc:teiid:test", null);
		JDBCUtil.executeQuery(c, "select * from tables");
		Statement s = c.createStatement();
		ResultSet rs = s.executeQuery("select * from \"my-view\"");
		
		print("\nGet the effective ddl text for the my-schema \n");
		print(embeddedServer.getSchemaDdl("test", "my-schema"));
		print("\nGet the effective ddl text for the virt \n");
		print(embeddedServer.getSchemaDdl("test", "virt"));
		
		print(rs.getMetaData().getColumnLabel(1));
		
		s.execute("update \"my-view\" set \"my-column\" = 'a'");
		print(s.getUpdateCount());
		
		embeddedServer.deployVDB("empty");
		c = embeddedServer.getDriver().connect("jdbc:teiid:empty", new Properties());
		JDBCUtil.executeQuery(c, "select * from tables");
		print("\nGet the effective ddl text for the SYSADMIN ");
		print(embeddedServer.getSchemaDdl("empty", "SYS"));
		print("\nGet the effective ddl text for the SYSADMIN ");
		print(embeddedServer.getSchemaDdl("empty", "SYSADMIN"));
		
		JDBCUtil.close(c);
	}

}
