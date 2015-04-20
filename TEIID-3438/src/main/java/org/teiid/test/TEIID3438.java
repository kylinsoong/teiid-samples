package org.teiid.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.h2.tools.RunScript;
import org.h2.tools.Server;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;

import bitronix.tm.resource.jdbc.PoolingDataSource;

public class TEIID3438 {
	
	static EmbeddedServer server = null;
	static Server h2Server = null;
	static Connection conn = null;

	public static void main(String[] args) throws Exception {

		try {
			init();
			
			server = new EmbeddedServer();
			
			TestH2ExecutionFactory executionFactory = new TestH2ExecutionFactory() ;
			executionFactory.setSupportsDirectQueryProcedure(true);
			executionFactory.start();
			server.addTranslator("translator-test", executionFactory);
			
			server.start(new EmbeddedConfiguration());

			server.deployVDB(new FileInputStream(new File("src/main/resources/vdb.xml")));

			conn = server.getDriver().connect("jdbc:teiid:VDB", null);
			
			JDBCUtil.executeQuery(conn, "SELECT * FROM TEIID");
		} finally {
			destory();
		}
		
		
	}


	private static void init() throws FileNotFoundException, SQLException, NamingException {
		
		h2Server = Server.createTcpServer().start();
		
		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "bitronix.tm.jndi.BitronixInitialContextFactory");
		
		PoolingDataSource pds = new PoolingDataSource();
		pds.setUniqueName("java:/accounts-ds");
		pds.setClassName("bitronix.tm.resource.jdbc.lrc.LrcXADataSource");
		pds.setMaxPoolSize(5);
		pds.setAllowLocalTransactions(true);
		pds.getDriverProperties().put("user", "sa");
		pds.getDriverProperties().put("password", "sa");
		pds.getDriverProperties().put("url", "jdbc:h2:mem://localhost/~/test");
		pds.getDriverProperties().put("driverClassName", "org.h2.Driver");
		pds.init();
		
		RunScript.execute(pds.getConnection(), new FileReader("src/main/resources/h2.sql"));
		
		Context ctx = new InitialContext();
		
		DataSource ds = (DataSource) ctx.lookup("java:/accounts-ds");
		Connection conn = ds.getConnection();
		
		PreparedStatement ps = conn.prepareStatement("INSERT INTO TEIID (ID, ITEM) VALUES(?, ?)");
		ps.setInt(1, 1);
		ps.setBlob(2, conn.createBlob());
		ps.addBatch();
        ps.executeBatch();
        
        JDBCUtil.close(null, ps);
        
        JDBCUtil.close(conn);
	}
	
	private static void destory() {
		if(null != h2Server) {
			h2Server.stop();
		}
	}
	

}
