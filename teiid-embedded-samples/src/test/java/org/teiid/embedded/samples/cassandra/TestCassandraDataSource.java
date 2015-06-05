package org.teiid.embedded.samples.cassandra;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.teiid.embedded.samples.TestBase;
import org.teiid.embedded.samples.util.JDBCUtil;
import org.teiid.resource.adapter.cassandra.CassandraManagedConnectionFactory;
import org.teiid.translator.cassandra.CassandraExecutionFactory;

@Ignore("Need Cassandra Server running")
public class TestCassandraDataSource extends TestBase {
	
	// Cassandra Contact Point Address
	private static final String ADDRESS = "10.66.218.46";
	
	// Cassandra keyspace
	private static final String KEYSPACE = "demo";
	
	@BeforeClass
	public static void init() throws Exception {
		
		init("translator-cassandra", new CassandraExecutionFactory());
		
		CassandraManagedConnectionFactory managedconnectionFactory = new CassandraManagedConnectionFactory();
		managedconnectionFactory.setAddress(ADDRESS);
		managedconnectionFactory.setKeyspace(KEYSPACE);
		server.addConnectionFactory("java:/demoCassandra", managedconnectionFactory.createConnectionFactory());
		
		start(false);
		
		server.deployVDB(new FileInputStream(new File("vdb/cassandra-vdb.xml")));
		
		conn = server.getDriver().connect("jdbc:teiid:users", null);
	}
	
	@Test
	public void testQuery() throws Exception {
		assertNotNull(conn);
		assertTrue(JDBCUtil.countResults(conn, "SELECT * FROM UsersView") > 0);
	}

}
