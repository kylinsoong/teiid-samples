package org.teiid.runtime.embedded;

import java.security.AccessController;
import java.security.PrivilegedAction;

class SecurityActions {
	
	private SecurityActions() {
		
	}
	
	static ClassLoader getClassLoader(final Class<?> c) {
		
		if (System.getSecurityManager() == null){
			return c.getClassLoader();
		}
		
		return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>(){

			@Override
			public ClassLoader run() {
				return c.getClassLoader();
		}});
	}
	
	static String getSystemProperty(final String name) {
		
		return (String) AccessController.doPrivileged(new PrivilegedAction<Object>(){

			@Override
			public Object run() {
				return System.getProperty(name);
		}});
	}
	
	static String getSystemProperty(final String name, final String value) {
		
		return (String) AccessController.doPrivileged(new PrivilegedAction<Object>(){

			@Override
			public Object run() {
				return System.getProperty(name, value);
			}});
	}
	
	static void setSystemProperty(final String name, final String value) {
		
		AccessController.doPrivileged(new PrivilegedAction<Object>(){

			@Override
			public Object run() {
				System.setProperty(name, value);
				return null;
			}

		});
	}

}
