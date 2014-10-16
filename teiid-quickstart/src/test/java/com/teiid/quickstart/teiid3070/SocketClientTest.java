package com.teiid.quickstart.teiid3070;

import java.util.Properties;

import org.teiid.net.socket.SocketServerConnectionFactory;

public class SocketClientTest {
	
	public static void main(String[] args) throws Exception {
		
		SocketServerConnectionFactory factory = new SocketServerConnectionFactory();
		factory.setDisablePing(true);
		
		factory.initialize(new Properties());
		
		System.out.println(factory);
		
	}

}
