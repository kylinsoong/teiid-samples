package org.teiid.resource.adapter;

import javax.resource.ResourceException;

import org.teiid.resource.spi.BasicConnection;

/*
 * https://docs.jboss.org/author/display/teiid87final/Implementing+the+Teiid+Framework
 */
public class MyConnection extends BasicConnection {
	
	public void doSomeOperation() {
		
	}

	public void close() throws ResourceException {

	}

	public boolean isAlive() {
		return super.isAlive();
	}

	public void cleanUp() {
		super.cleanUp();
	}

}
