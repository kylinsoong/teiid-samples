package com.teiid.quickstart.sanbox;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.teiid.core.util.NamedThreadFactory;
import org.teiid.transport.SocketConfiguration;
import org.teiid.transport.WireProtocol;


public class Test {

	public static void main(String[] args) {
		
		SocketConfiguration s = new SocketConfiguration();
		InetSocketAddress addr = new InetSocketAddress("localhost", 31000);
		s.setBindAddress(addr.getHostName());
		s.setPortNumber(addr.getPort());
		s.setProtocol(WireProtocol.teiid);
		
		ExecutorService nettyPool = Executors.newCachedThreadPool(new NamedThreadFactory("NIO"));
		ChannelFactory factory = new NioServerSocketChannelFactory(nettyPool, nettyPool, 4);
		ServerBootstrap bootstrap = new ServerBootstrap(factory);
		bootstrap.setOption("keepAlive", Boolean.TRUE);
		Channel serverChanel = bootstrap.bind(addr);
		System.out.println(serverChanel);
	}
}
