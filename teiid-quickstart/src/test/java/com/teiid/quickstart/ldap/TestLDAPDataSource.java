package com.teiid.quickstart.ldap;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.ldap.InitialLdapContext;
import javax.resource.ResourceException;

import org.junit.Test;
import org.teiid.language.Command;
import org.teiid.resource.adapter.ldap.LDAPConnectionImpl;
import org.teiid.resource.adapter.ldap.LDAPManagedConnectionFactory;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.ldap.LDAPDirectSearchQueryExecution;
import org.teiid.translator.ldap.LDAPExecutionFactory;
import org.teiid.translator.ldap.LDAPSearchDetails;

import com.teiid.quickstart.FakeTranslationFactory;
import com.teiid.quickstart.TranslationUtility;

public class TestLDAPDataSource {
	
	static EmbeddedServer server = null;
	static Connection conn = null;
	
	@Test
	public void testConnection() throws ResourceException, TranslatorException{

		LDAPManagedConnectionFactory factory = new LDAPManagedConnectionFactory();
		factory.setLdapUrl("ldap://10.66.218.46:389");
		factory.setLdapAdminUserDN("cn=Manager,dc=example,dc=com");
		factory.setLdapAdminUserPassword("redhat");
		
		LDAPConnectionImpl connection = factory.createConnectionFactory().getConnection();
				
		assertNotNull(connection);
	}
	
	@Test
	public void testSearch() throws TranslatorException, ResourceException {
		
		LDAPExecutionFactory translator = new LDAPExecutionFactory();
		translator.setSupportsDirectQueryProcedure(true);
		translator.start();
		
		LDAPManagedConnectionFactory factory = new LDAPManagedConnectionFactory();
		factory.setLdapUrl("ldap://10.66.218.46:389");
		factory.setLdapAdminUserDN("cn=Manager,dc=example,dc=com");
		factory.setLdapAdminUserPassword("redhat");
		LDAPConnectionImpl connection = factory.createConnectionFactory().getConnection();
		
		String input = "exec native('search;context-name=corporate;filter=(objectClass=*);count-limit=5;timeout=6;search-scope=ONELEVEL_SCOPE;attributes=uid,cn')"; 

        TranslationUtility util = FakeTranslationFactory.getInstance().getExampleTranslationUtility();
        Command command = util.parseCommand(input);
        
        LDAPDirectSearchQueryExecution execution = (LDAPDirectSearchQueryExecution) translator.createExecution(command, null, null, connection);
        execution.execute();
//        LDAPSearchDetails details = execution.g
        
        System.out.println(execution);
	}
	
	
	@Test
	public void testLdapSearch() throws NamingException{
		
		Hashtable<String,String> env = new Hashtable<String,String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://10.66.218.46:389");
		env.put(Context.SECURITY_PRINCIPAL, "cn=Manager,dc=example,dc=com");
		env.put(Context.SECURITY_CREDENTIALS, "redhat");
		DirContext ctx = new InitialLdapContext(env, null);

		Attributes attrs = ctx.getAttributes("ou=People,dc=example,dc=com");
		Enumeration en = attrs.getAll();
		while(en.hasMoreElements()){
			Attribute attr = (Attribute) en.nextElement();
			printAttribute(attr);
		}
	}
	
	private void printAttribute(Attribute attr) throws NamingException {
		System.out.println(attr);
		Enumeration en = attr.getAll();
		while(en.hasMoreElements()){
//			Attribute subattr = (Attribute) en.nextElement();
//			printAttribute(subattr);
			System.out.println(en.nextElement());
		}
	}


	@Test
	public void testJNDIbasic() throws NamingException {
		Hashtable<String,String> env = new Hashtable<String,String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://10.66.218.46:389");
		env.put(Context.SECURITY_PRINCIPAL, "cn=Manager,dc=example,dc=com");
		env.put(Context.SECURITY_CREDENTIALS, "redhat");
		DirContext ctx = new InitialLdapContext(env, null);

		Attributes attrs = ctx.getAttributes("uid=kylin,ou=People,dc=example,dc=com");
		
		System.out.println(attrs.get("sn"));
		
	}
	
	@Test
	public void testInitialization() throws Exception {
		
		LDAPManagedConnectionFactory config = new LDAPManagedConnectionFactory();
		config.setLdapUrl("ldap://10.66.218.46:389");
		config.setLdapAdminUserDN("cn=Manager,dc=example,dc=com");
		config.setLdapAdminUserPassword("redhat");
		config.setLdapContextFactory("com.sun.jndi.ldap.LdapCtxFactory");
		
        
		LDAPConnectionImpl conn = new LDAPConnectionImpl(config);
		
	}
	
	public void test() throws TranslatorException, ResourceException {
		server = new EmbeddedServer();
		
		LDAPExecutionFactory executionFactory = new LDAPExecutionFactory();
		executionFactory.start();
		server.addTranslator("translator-ldap", executionFactory);
		
		LDAPManagedConnectionFactory managedconnectionFactory = new LDAPManagedConnectionFactory();
		managedconnectionFactory.setLdapUrl("ldap://10.66.218.46:389");
//		managedconnectionFactory.s
		server.addConnectionFactory("java:/ldapDS", managedconnectionFactory.createConnectionFactory());
	}

	public static void main(String[] args) throws Exception {
		new TestLDAPDataSource().testInitialization();
	}

}
