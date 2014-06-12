package org.teiid.jboss.test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class StartupExceptionReproduce {

	public static void main(String[] args) throws UnknownHostException {

		InetAddress.getLocalHost();
	}

}
