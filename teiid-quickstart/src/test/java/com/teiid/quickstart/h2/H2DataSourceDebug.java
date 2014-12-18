package com.teiid.quickstart.h2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;

import org.h2.tools.RunScript;
import org.h2.tools.Server;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

import bitronix.tm.resource.jdbc.PoolingDataSource;

import com.teiid.quickstart.util.JDBCUtil;

public class H2DataSourceDebug {
	
	static {
		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "bitronix.tm.jndi.BitronixInitialContextFactory");
	}
	
	static EmbeddedServer server = null;
	static PoolingDataSource pds = null ;
	static Connection conn = null;
	static Server h2Server = null;
	
	public void init() throws Exception {
		
		startServer();
		setupDataSource();
		initTestData();
		
		server = new EmbeddedServer();
		
		H2ExecutionFactory executionFactory = new H2ExecutionFactory() ;
		executionFactory.setSupportsDirectQueryProcedure(true);
		executionFactory.start();
		server.addTranslator("translator-h2", executionFactory);
		
		server.start(new EmbeddedConfiguration());
		
		server.deployVDB(new FileInputStream(new File("src/vdb/h2-debug-vdb.xml")));
		
		conn = server.getDriver().connect("jdbc:teiid:H2VDB", null);	
				
		JDBCUtil.executeQuery(conn, "SELECT company_name, COUNT(product_id) FROM PROUDCTVIEW GROUP BY company_name");
		
		JDBCUtil.close(conn);
		
	}
	
	private void startServer() throws SQLException {
		h2Server = Server.createTcpServer().start();
	}
	
	private void setupDataSource() {
		if (null != pds)
			return;
		
		pds = new PoolingDataSource();
		pds.setUniqueName("java:/accounts-ds");
		pds.setClassName("bitronix.tm.resource.jdbc.lrc.LrcXADataSource");
		pds.setMaxPoolSize(5);
		pds.setAllowLocalTransactions(true);
		pds.getDriverProperties().put("user", "sa");
		pds.getDriverProperties().put("password", "sa");
		pds.getDriverProperties().put("url", "jdbc:h2:mem://localhost/~/test");
		pds.getDriverProperties().put("driverClassName", "org.h2.Driver");
		pds.init();
	}
	
	private void initTestData() throws SQLException, FileNotFoundException {
		RunScript.execute(pds.getConnection(), new FileReader("src/file/customer-schema-h2.sql"));
	}

	public static void main(String[] args) throws Exception {
		new H2DataSourceDebug().init();
		h2Server.stop();
	}

}
