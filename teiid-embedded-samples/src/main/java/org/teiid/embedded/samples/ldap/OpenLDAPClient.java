package org.teiid.embedded.samples.ldap;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class OpenLDAPClient {

	public static void main(String[] args) throws NamingException {

		Hashtable<String,String> env = new Hashtable<String,String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://10.66.218.46:389");
		env.put(Context.SECURITY_PRINCIPAL, "cn=Manager,dc=example,dc=com");
		env.put(Context.SECURITY_CREDENTIALS, "redhat");
		DirContext ctx = new InitialLdapContext(env, null);
		
		LdapContext context = (LdapContext) ctx.lookup("ou=HR,dc=example,dc=com");
		
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
