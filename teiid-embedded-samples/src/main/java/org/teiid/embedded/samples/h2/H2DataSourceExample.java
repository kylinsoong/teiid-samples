package org.teiid.embedded.samples.h2;

import static org.teiid.embedded.samples.util.JDBCUtil.executeQuery;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;

import javax.naming.Context;

import org.h2.tools.RunScript;
import org.h2.tools.Server;
import org.teiid.embedded.samples.ExampleBase;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

import bitronix.tm.resource.jdbc.PoolingDataSource;

public class H2DataSourceExample extends ExampleBase {
	
	static {
		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "bitronix.tm.jndi.BitronixInitialContextFactory");
	}
	
	static Server h2Server = null;
	static PoolingDataSource pds = null ;
	

	@Override
	public void execute() throws Exception {
		startServer();
		setupDataSource();
		initTestData();
		
		init("translator-h2", new H2ExecutionFactory());
		start(false);
		server.deployVDB(new FileInputStream(new File("vdb/h2-vdb.xml")));
		conn = server.getDriver().connect("jdbc:teiid:H2VDB", null);
		
		executeDDL();
		
		tearDown();
	}
	
	private void executeDDL() throws Exception {
		executeQuery(conn, "SELECT * FROM PRODUCT");
		executeQuery(conn, "SELECT COUNT(*) FROM PRODUCT");
		executeQuery(conn, "SELECT COUNT(ID) FROM PRODUCT");
		executeQuery(conn, "SELECT SUM(ID) FROM PRODUCT");
		executeQuery(conn, "SELECT AVG(ID) FROM PRODUCT");
		executeQuery(conn, "SELECT MIN(ID) FROM PRODUCT");
		executeQuery(conn, "SELECT MAX(ID) FROM PRODUCT");
	}

	static void startServer() throws SQLException {
		h2Server = Server.createTcpServer().start();
	}
	
	static void setupDataSource() {
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
	
	static void initTestData() throws FileNotFoundException, SQLException {
		RunScript.execute(pds.getConnection(), new FileReader("metadata/customer-schema-h2.sql"));
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		h2Server.stop();
	}

	public static void main(String[] args) throws Exception {
		new H2DataSourceExample().execute();
	}

}
