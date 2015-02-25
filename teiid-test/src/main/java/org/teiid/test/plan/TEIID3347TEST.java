package org.teiid.test.plan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;

import org.h2.tools.RunScript;
import org.h2.tools.Server;
import org.teiid.client.plan.PlanNode;
import org.teiid.jdbc.TeiidStatement;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.JDBCUtil;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

import bitronix.tm.resource.jdbc.PoolingDataSource;

public class TEIID3347TEST {
	
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
		
		server.deployVDB(new FileInputStream(new File("vdb/h2-plan-vdb.xml")));
		
		conn = server.getDriver().connect("jdbc:teiid:H2VDB", null);	
								
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

		TEIID3347TEST test = new TEIID3347TEST();
		
		test.init();
		test.subPlanStatistic();
		
		test.destory();
	}

	private void destory() {
		
		JDBCUtil.close(conn);
		
		h2Server.stop();
	}

	private void subPlanStatistic() throws SQLException {

		String sql = "SELECT * FROM PROUDCTVIEW";
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.createStatement();
			stmt.execute("set showplan on");
			rs = stmt.executeQuery(sql);
			TeiidStatement tstatement = stmt.unwrap(TeiidStatement.class);
            PlanNode queryPlan = tstatement.getPlanDescription();
            System.out.println(queryPlan);
		} finally {
			if(rs != null) {
				rs.close();
			}
			if(stmt != null) {
				stmt.close();
			}
		}
		
		
	}

}
