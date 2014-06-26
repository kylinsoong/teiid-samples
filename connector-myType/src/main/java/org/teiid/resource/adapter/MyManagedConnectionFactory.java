package org.teiid.resource.adapter;

import javax.resource.ResourceException;

import org.teiid.resource.spi.BasicManagedConnectionFactory;

public class MyManagedConnectionFactory extends BasicManagedConnectionFactory {
	
	private static final long serialVersionUID = 1738537818200424511L;

	public MyConnectionFactory createConnectionFactory() throws ResourceException {
		return new MyConnectionFactory();
	}
	

	String userName;

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String name) {
		this.userName = name;
	}

	Integer count;

	public Integer getCount() {
		return this.count;
	}

	public void setCount(Integer value) {
		this.count = value;
	}

}
