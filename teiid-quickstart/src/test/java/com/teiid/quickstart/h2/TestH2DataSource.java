package com.teiid.quickstart.h2;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;

import org.h2.tools.RunScript;
import org.h2.tools.Server;
import org.junit.Test;
import org.teiid.client.plan.PlanNode;
import org.teiid.deployers.VirtualDatabaseException;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository.ConnectorManagerException;
import org.teiid.jdbc.TeiidStatement;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

import com.teiid.quickstart.util.JDBCUtil;

import bitronix.tm.resource.jdbc.PoolingDataSource;

public class TestH2DataSource {
	
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
		
		server.deployVDB(new FileInputStream(new File("src/vdb/h2-vdb.xml")));
		
		conn = server.getDriver().connect("jdbc:teiid:H2VDB", null);	
		
		assertNotNull(conn);
		
		JDBCUtil.executeQuery(conn, "SELECT * FROM PRODUCT");
		JDBCUtil.executeQuery(conn, "SELECT * FROM ACCOUNT");
		JDBCUtil.executeQuery(conn, "SELECT * FROM CUSTOMER");
		JDBCUtil.executeQuery(conn, "SELECT * FROM PROUDCTVIEW");
		JDBCUtil.executeQuery(conn, "SELECT * FROM CUSTOMERVIEW");
		
	}

	private void initTestData() throws SQLException, FileNotFoundException {
		RunScript.execute(pds.getConnection(), new FileReader("src/file/customer-schema-h2.sql"));
	}
	
	@Test
	public void testQueryPlans() throws Exception {
		init();
		Statement stmt = conn.createStatement();
		stmt.execute("set showplan on");
		ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT");
		TeiidStatement tstatement = stmt.unwrap(TeiidStatement.class);
		PlanNode queryPlan = tstatement.getPlanDescription();
		System.out.println(queryPlan);
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

	private void startServer() throws SQLException {
		h2Server = Server.createTcpServer().start();
	}

	public static void main(String[] args) throws Exception {
		new TestH2DataSource().init();
		h2Server.stop();
	}

}
