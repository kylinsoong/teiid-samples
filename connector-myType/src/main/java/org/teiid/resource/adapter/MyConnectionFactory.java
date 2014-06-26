package org.teiid.resource.adapter;

import javax.resource.ResourceException;

import org.teiid.resource.spi.BasicConnectionFactory;

public class MyConnectionFactory extends BasicConnectionFactory<MyConnection> {

	private static final long serialVersionUID = -3305690822536439692L;

	public MyConnection getConnection() throws ResourceException {
		return new MyConnection();
	}

}
