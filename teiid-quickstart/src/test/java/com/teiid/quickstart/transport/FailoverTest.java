package com.teiid.quickstart.transport;

import static org.mockito.Mockito.mock;

import java.net.InetSocketAddress;
import java.util.Properties;

import org.teiid.client.security.ILogon;
import org.teiid.client.security.InvalidSessionException;
import org.teiid.client.security.LogonException;
import org.teiid.client.security.LogonResult;
import org.teiid.client.security.SessionToken;
import org.teiid.client.util.ResultsFuture;
import org.teiid.common.buffer.BufferManagerFactory;
import org.teiid.core.TeiidComponentException;
import org.teiid.dqp.service.SessionService;
import org.teiid.net.socket.SocketServerConnectionFactory;
import org.teiid.transport.ClientServiceRegistryImpl;
import org.teiid.transport.LogonImpl;
import org.teiid.transport.SSLConfiguration;
import org.teiid.transport.SocketListener;


public class FailoverTest {
	
	SocketListener listener;
	SocketListener listener1;
	
	private SocketServerConnectionFactory sscf;
	private InetSocketAddress addr = new InetSocketAddress(0);
	private int logonAttempts;
	
	
	
	public void testFailover() throws Exception {
		
		SSLConfiguration config = new SSLConfiguration();
		Properties p = new Properties();
		
	}

	public static void main(String[] args) throws Exception {
		new FailoverTest().testFailover();
	}

}
