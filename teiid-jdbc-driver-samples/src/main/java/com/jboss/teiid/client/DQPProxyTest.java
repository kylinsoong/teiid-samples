package com.jboss.teiid.client;

import java.util.Properties;

import org.teiid.client.DQP;
import org.teiid.net.CommunicationException;
import org.teiid.net.ConnectionException;
import org.teiid.net.ServerConnection;
import org.teiid.net.socket.SocketServerConnectionFactory;

public class DQPProxyTest {

	public static void main(String[] args) throws CommunicationException, ConnectionException {

		SocketServerConnectionFactory factory = SocketServerConnectionFactory.getInstance();
		Properties prop = new Properties();
		prop.setProperty("ApplicationName", "JDBC");
		prop.setProperty("version", "1");
		prop.setProperty("serverURL", "mm://localhost:31000");
		prop.setProperty("user", "user");
		prop.setProperty("password", "user");
		prop.setProperty("VirtualDatabaseVersion", "1");
		prop.setProperty("VirtualDatabaseName", "Marketdata");
		
		ServerConnection serverConn = factory.getConnection(prop);
		
		DQP dqp = serverConn.getService(DQP.class);
		System.out.println(dqp.getClass());
		
		System.out.println("\nConstructors:");
		for(Object obj : dqp.getClass().getConstructors()) {
			System.out.println(obj);
		}
		
		System.out.println("\nDeclaredFields:");
		for(Object obj : dqp.getClass().getDeclaredFields()) {
			System.out.println(obj);
		}
		
		System.out.println("\nDeclaredConstructors:");
		for(Object obj : dqp.getClass().getDeclaredConstructors()) {
			System.out.println(obj);
		}
		
		System.out.println("\nDeclaredMethods:");
		for(Object obj : dqp.getClass().getDeclaredMethods()) {
			System.out.println(obj);
		}
		
		System.out.println("\nInterfaces:");
		for(Object obj : dqp.getClass().getInterfaces()) {
			System.out.println(obj);
		}
		
	}

}
