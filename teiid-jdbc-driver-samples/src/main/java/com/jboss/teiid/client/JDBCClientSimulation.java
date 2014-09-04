package com.jboss.teiid.client;

import java.sql.SQLException;
import java.util.Properties;

import org.teiid.jdbc.ConnectionImpl;
import org.teiid.net.CommunicationException;
import org.teiid.net.ConnectionException;
import org.teiid.net.ServerConnection;
import org.teiid.net.socket.SocketServerConnectionFactory;

public class JDBCClientSimulation {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, CommunicationException, ConnectionException {
		
		
        /*
         * 1. create SocketServerConnectionFactory, init OioObjectChannelFactory
         */
		SocketServerConnectionFactory factory = SocketServerConnectionFactory.getInstance();
		
		/*
		 * 2. create ServerConnection
		 */
		Properties prop = new Properties();
		prop.setProperty("ApplicationName", "JDBC");
		prop.setProperty("version", "1");
		prop.setProperty("serverURL", "mm://localhost:31000");
		prop.setProperty("user", "user");
		prop.setProperty("password", "user");
		prop.setProperty("VirtualDatabaseVersion", "1");
		prop.setProperty("VirtualDatabaseName", "Marketdata");
		
		ServerConnection serverConn = factory.getConnection(prop);
		
		/*
		 * 3. create ConnectionImpl
		 */
		String url = "jdbc:teiid:Marketdata@mm://localhost:31000;version=1";
		prop.put("clientIpAddress", "127.0.0.1");
		prop.put("clientHostName", "localhost");
		
		ConnectionImpl conn = new ConnectionImpl(serverConn, prop, url);		
		
	}

}
