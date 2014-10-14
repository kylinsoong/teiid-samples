package com.teiid.quickstart.teiid3070;

import java.util.Properties;

import org.teiid.net.CommunicationException;
import org.teiid.net.ConnectionException;
import org.teiid.net.socket.SocketServerConnection;
import org.teiid.net.socket.SocketServerConnectionFactory;

public class Teiid3070Client {

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
		
		SocketServerConnection serverConn = factory.getConnection(prop);
		
		System.out.println(serverConn.isOpen(1000));
		
	}

}
