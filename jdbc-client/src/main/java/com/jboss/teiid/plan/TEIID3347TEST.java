package com.jboss.teiid.plan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;

import org.teiid.client.plan.PlanNode;
import org.teiid.jdbc.TeiidStatement;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;
import org.teiid.translator.jdbc.mysql.MySQL5ExecutionFactory;

import bitronix.tm.resource.jdbc.PoolingDataSource;

public class TEIID3347TEST {
	
	static {
		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "bitronix.tm.jndi.BitronixInitialContextFactory");
	}
	
	static EmbeddedServer server = null;
	static PoolingDataSource pds = null ;
	static Connection conn = null;
	
	public void init() throws Exception {

		setupDataSource();
		
		server = new EmbeddedServer();
		
		MySQL5ExecutionFactory executionFactory = new MySQL5ExecutionFactory() ;
		executionFactory.setSupportsDirectQueryProcedure(true);
		executionFactory.start();
		server.addTranslator("translator-mysql", executionFactory);
		
		server.start(new EmbeddedConfiguration());
		
		server.deployVDB(new FileInputStream(new File("mysql-vdb.xml")));
		
		conn = conn = server.getDriver().connect("jdbc:teiid:MysqlVDB", null);	
								
	}

	private void setupDataSource() {
		if (null != pds)
			return;
		
		pds = new PoolingDataSource();
		pds.setUniqueName("java:/accounts-ds");
		pds.setClassName("bitronix.tm.resource.jdbc.lrc.LrcXADataSource");
		pds.setMaxPoolSize(5);
		pds.setAllowLocalTransactions(true);
		pds.getDriverProperties().put("user", "test_user");
		pds.getDriverProperties().put("password", "test_pass");
		pds.getDriverProperties().put("url", "jdbc:mysql://localhost:3306/test");
		pds.getDriverProperties().put("driverClassName", "com.mysql.jdbc.Driver");
		pds.init();
	}
	
	

	public static void main(String[] args) throws Exception {

		TEIID3347TEST test = new TEIID3347TEST();
		
		test.init();
		test.subPlanStatistic();
	}

	private void subPlanStatistic() throws SQLException {

		String sql = "SELECT * FROM SERIALTEST";
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.createStatement();
			stmt.execute("set showplan on");
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				System.out.println(rs.getLong(1));
			}
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
