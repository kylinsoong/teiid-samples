package org.teiid.embedded.samples.ldap;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.teiid.embedded.samples.TestBase;
import org.teiid.embedded.samples.util.JDBCUtil;
import org.teiid.resource.adapter.ldap.LDAPManagedConnectionFactory;
import org.teiid.translator.ldap.LDAPExecutionFactory;

@Ignore("Need Ldap Running")
public class TestLDAPDataSource extends TestBase{
	
	@BeforeClass
	public static void init() throws Exception {

		init("translator-ldap", new LDAPExecutionFactory());
		
		LDAPManagedConnectionFactory managedconnectionFactory = new LDAPManagedConnectionFactory();
		managedconnectionFactory.setLdapUrl("ldap://10.66.218.46:389");
		managedconnectionFactory.setLdapAdminUserDN("cn=Manager,dc=example,dc=com");
		managedconnectionFactory.setLdapAdminUserPassword("redhat");
		server.addConnectionFactory("java:/ldapDS", managedconnectionFactory.createConnectionFactory());
		
		start(false);
		server.deployVDB(new FileInputStream(new File("vdb/ldap-vdb.xml")));
		conn = server.getDriver().connect("jdbc:teiid:ldapVDB", null);
	}
	
	@Test
	public void testQuery() throws Exception {
		assertNotNull(conn);
		assertTrue(JDBCUtil.countResults(conn, "SELECT * FROM HR_Group") > 0);
	}

}
