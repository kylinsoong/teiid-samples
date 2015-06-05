package org.teiid.embedded.samples.mongodb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.teiid.embedded.samples.TestBase;
import org.teiid.embedded.samples.util.JDBCUtil;
import org.teiid.resource.adapter.mongodb.MongoDBManagedConnectionFactory;
import org.teiid.translator.mongodb.MongoDBExecutionFactory;

@Ignore("Need Mongo Running")
public class TestMongoDBDataSource extends TestBase {
	
	private static final String SERVERLIST = "10.66.218.46:27017" ;
	private static final String DBNAME = "mydb" ;

	@BeforeClass
	public static void init() throws Exception {

		init("translator-mongodb", new MongoDBExecutionFactory());
		
		MongoDBManagedConnectionFactory managedconnectionFactory = new MongoDBManagedConnectionFactory();
		managedconnectionFactory.setRemoteServerList(SERVERLIST);
		managedconnectionFactory.setDatabase(DBNAME);
		server.addConnectionFactory("java:/mongoDS", managedconnectionFactory.createConnectionFactory());
		
		start(false);
		server.deployVDB(new FileInputStream(new File("vdb/mongodb-vdb.xml")));
		conn = server.getDriver().connect("jdbc:teiid:nothwind", null);
	}
	
	@Test
	public void testQuery() throws Exception {
		assertNotNull(conn);
		
		JDBCUtil.executeUpdate(conn, "DELETE FROM Customer");
		
		JDBCUtil.executeUpdate(conn, "INSERT INTO Customer(customer_id, FirstName, LastName) VALUES (1, 'Kylin', 'Soong')");
		JDBCUtil.executeUpdate(conn, "INSERT INTO Customer(customer_id, FirstName, LastName) VALUES (2, 'Kylin', 'Soong')");
		JDBCUtil.executeUpdate(conn, "INSERT INTO Customer(customer_id, FirstName, LastName) VALUES (3, 'Kylin', 'Soong')");
		
		assertEquals(3, JDBCUtil.countResults(conn, "SELECT * FROM Customer"));
	}
}
