package org.teiid.runtime.embedded;

import javax.naming.Context;
import javax.naming.InitialContext;

public class Test {

	public static void main(String[] args) throws Throwable {

		EmbeddedBootstrap bootstrap = EmbeddedBootstrap.Factory.create();
		bootstrap.startup();
		
		Context ctx = new InitialContext();
		
		System.out.println(ctx);
		
		bootstrap.shutdown();
		
		System.out.println("Exit");
		
	}

}
