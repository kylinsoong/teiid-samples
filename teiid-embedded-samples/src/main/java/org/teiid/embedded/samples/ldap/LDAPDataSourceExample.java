package org.teiid.embedded.samples.ldap;

import java.io.File;
import java.io.FileInputStream;

import org.teiid.embedded.samples.ExampleBase;
import org.teiid.embedded.samples.util.JDBCUtil;
import org.teiid.resource.adapter.ldap.LDAPManagedConnectionFactory;
import org.teiid.translator.ldap.LDAPExecutionFactory;

public class LDAPDataSourceExample extends ExampleBase{

	public static void main(String[] args) throws Exception {
		new LDAPDataSourceExample().execute();
	}

	@Override
	public void execute() throws Exception {

		init("translator-ldap", new LDAPExecutionFactory());
		
		LDAPManagedConnectionFactory managedconnectionFactory = new LDAPManagedConnectionFactory();
		managedconnectionFactory.setLdapUrl("ldap://10.66.218.46:389");
		managedconnectionFactory.setLdapAdminUserDN("cn=Manager,dc=example,dc=com");
		managedconnectionFactory.setLdapAdminUserPassword("redhat");
		server.addConnectionFactory("java:/ldapDS", managedconnectionFactory.createConnectionFactory());
		
		start(false);
		server.deployVDB(new FileInputStream(new File("vdb/ldap-vdb.xml")));
		conn = server.getDriver().connect("jdbc:teiid:ldapVDB", null);
		
		JDBCUtil.executeQuery(conn, "SELECT * FROM HR_Group");
		
		JDBCUtil.close(conn);
	}

}
