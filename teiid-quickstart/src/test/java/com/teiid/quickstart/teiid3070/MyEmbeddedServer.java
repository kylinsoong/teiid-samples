package com.teiid.quickstart.teiid3070;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import org.teiid.net.socket.ObjectChannel;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.transport.ChannelListener;
import org.teiid.transport.SocketClientInstance;
import org.teiid.transport.SocketConfiguration;
import org.teiid.transport.SocketListener;


public class MyEmbeddedServer extends EmbeddedServer {

	public ArrayList<SocketListener> getListeners() {
		return transports;
	}
	
	public void startListener(SocketConfiguration socketConfig){
		InetSocketAddress address = new InetSocketAddress(socketConfig.getHostAddress(), socketConfig.getPortNumber());
		new SocketListener(address, socketConfig, services, bufferService.getBufferManager()) {};
	}
}
