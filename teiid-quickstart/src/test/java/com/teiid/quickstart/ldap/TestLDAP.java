package com.teiid.quickstart.ldap;

import static org.junit.Assert.assertNotNull;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jndi.ldap.LdapCtx;

public class TestLDAP {
	
	static InitialLdapContext cxt = null;
	
	@BeforeClass
	public static void init() throws NamingException {
		Hashtable<String,String> env = new Hashtable<String,String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://10.66.218.46:389");
		env.put(Context.SECURITY_PRINCIPAL, "cn=Manager,dc=example,dc=com");
		env.put(Context.SECURITY_CREDENTIALS, "redhat");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		cxt = new InitialLdapContext(env, null);
	}
	
	@Test
	public void testInit(){
		assertNotNull(cxt);
	}
	
	@Test
	public void testGetAttr() throws NamingException {
		Attributes attrs = cxt.getAttributes("uid=hr1,ou=HR,dc=example,dc=com");
		System.out.println(attrs);
	}

	@Test
	public void testLookup() throws NamingException {
		
		LdapContext context = (LdapContext) cxt.lookup("ou=HR,dc=example,dc=com");
		
		SearchControls controls = new SearchControls();
		controls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
		controls.setTimeLimit(6);
		controls.setCountLimit(5);
		controls.setReturningAttributes(new String[]{"uid", "cn", "mail", "sn"});
		
		String filter = "(objectClass=*)";
		NamingEnumeration<SearchResult> en = context.search("", filter, controls);
		while(en.hasMoreElements()){
			SearchResult result = en.nextElement();
			Attributes attrs = result.getAttributes();
			System.out.print(attrs.get("uid").get() + ", ");
			System.out.print(attrs.get("cn").get() + ", ");
			System.out.print(attrs.get("mail").get() + ", ");
			System.out.println(attrs.get("sn").get());
		}
		
	}
}
