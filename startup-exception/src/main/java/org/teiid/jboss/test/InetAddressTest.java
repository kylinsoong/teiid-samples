package org.teiid.jboss.test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressTest {

	public static void main(String[] args) throws UnknownHostException {

		InetAddress address = InetAddress.getByName("www.baidu.com");
		System.out.println(address.getHostAddress());
		System.out.println(address.getHostName());
		
		address = InetAddress.getLocalHost();
		System.out.println(address.getHostAddress());
		System.out.println(address.getHostName());
		
	}

}
