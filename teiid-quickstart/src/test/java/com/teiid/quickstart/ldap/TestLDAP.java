package com.teiid.quickstart.ldap;

import static org.junit.Assert.assertNotNull;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jndi.ldap.LdapCtx;

public class TestLDAP {
	
	static InitialLdapContext initCtx = null;
	
	@BeforeClass
	public static void init() throws NamingException {
		Hashtable<String,String> env = new Hashtable<String,String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://10.66.218.46:389");
		env.put(Context.SECURITY_PRINCIPAL, "cn=Manager,dc=example,dc=com");
		env.put(Context.SECURITY_CREDENTIALS, "redhat");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		initCtx = new InitialLdapContext(env, null);
	}
	
	@Test
	public void testInit(){
		assertNotNull(initCtx);
	}

	@Test
	public void testLookup() throws NamingException {
		com.sun.jndi.ldap.LdapCtx ctx = (LdapCtx) initCtx.lookup("uid=kylin,ou=People,dc=example,dc=com");
		System.out.println(ctx);
	}
}
