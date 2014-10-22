package com.teiid.quickstart.supportcase;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;

import org.teiid.jdbc.ConnectionImpl;
import org.teiid.net.socket.SocketServerConnection;
import org.teiid.net.socket.SocketServerInstance;

public class Case01242969Client {

	public static void main(String[] args) throws Exception {

		Class.forName("org.teiid.jdbc.TeiidDriver");
		Connection conn = DriverManager.getConnection("jdbc:teiid:Marketdata@mm://10.66.192.103:31000;version=1", "user", "user");
		ConnectionImpl teiidconn = (ConnectionImpl) conn;
		SocketServerConnection socketServerConnection = (SocketServerConnection) teiidconn.getServerConnection();
		SocketServerInstance instance = socketServerConnection.selectServerInstance(false);
		String ip  = instance.getLocalAddress().getHostAddress();
		System.out.println(ip);
		
	}

	protected static void tcpConnection() throws UnknownHostException, IOException {
		Socket socket = new Socket("10.66.192.103", 31000);
		System.out.println(socket.getInetAddress());
		System.out.println(socket.getLocalAddress().getHostAddress());
		System.out.println(socket.getLocalSocketAddress());
		socket.close();
	}

}
