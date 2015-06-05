package org.teiid.embedded.samples.mysql;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;

import javax.naming.Context;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.teiid.embedded.samples.TestBase;
import org.teiid.embedded.samples.util.JDBCUtil;
import org.teiid.translator.jdbc.mysql.MySQL5ExecutionFactory;

import bitronix.tm.resource.jdbc.PoolingDataSource;

@Ignore()
public class TestMysqDataSource extends TestBase {
	
	static {
		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "bitronix.tm.jndi.BitronixInitialContextFactory");
	}
	
	static PoolingDataSource pds = null ;
	
	@BeforeClass
	public static void init() throws Exception {
		
		setupDataSource();
		
		init("translator-mysql", new MySQL5ExecutionFactory());
		
		start(false);
		
		server.deployVDB(new FileInputStream(new File("vdb/mysql-vdb.xml")));
		
		conn = server.getDriver().connect("jdbc:teiid:MysqlVDB", null);	
	}

	protected static void setupDataSource() {
		
		if (null != pds)
			return;
		
		pds = new PoolingDataSource();
		pds.setUniqueName("java:/accounts-ds");
		pds.setClassName("bitronix.tm.resource.jdbc.lrc.LrcXADataSource");
		pds.setMaxPoolSize(5);
		pds.setAllowLocalTransactions(true);
		pds.getDriverProperties().put("user", "jdv_user");
		pds.getDriverProperties().put("password", "jdv_pass");
		pds.getDriverProperties().put("url", "jdbc:mysql://localhost:3306/customer");
		pds.getDriverProperties().put("driverClassName", "com.mysql.jdbc.Driver");
		pds.init();
	}
	
	@Test
	public void testQuery() throws Exception {
		assertTrue(JDBCUtil.countResults(conn, "SELECT * FROM PROUDCTVIEW") > 0);
		assertTrue(JDBCUtil.countResults(conn, "SELECT * FROM CUSTOMERVIEW") > 0);
		assertTrue(JDBCUtil.countResults(conn, "SELECT * FROM PRODUCT") > 0);
		assertTrue(JDBCUtil.countResults(conn, "SELECT * FROM ACCOUNT") > 0);
		assertTrue(JDBCUtil.countResults(conn, "SELECT * FROM CUSTOMER") > 0);
	}

}
