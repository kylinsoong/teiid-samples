package org.teiid.jboss.test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressTest {

	public static void main(String[] args) throws UnknownHostException {
		
		InetAddress address = InetAddress.getByName("www.redhat.com");
		System.out.println(address);
		System.out.println(address.getHostName());
		System.out.println(address.getHostAddress());
		
	    address = InetAddress.getLocalHost();
	    System.out.println(address);
		System.out.println(address.getHostName());
		System.out.println(address.getHostAddress());
	}

}
