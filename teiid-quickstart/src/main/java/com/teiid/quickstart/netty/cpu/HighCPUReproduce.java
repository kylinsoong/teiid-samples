package com.teiid.quickstart.netty.cpu;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class HighCPUReproduce {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService nettyPool = Executors.newCachedThreadPool();
		ChannelFactory factory = new NioServerSocketChannelFactory(nettyPool, nettyPool, 1);
		ServerBootstrap bootstrap = new ServerBootstrap(factory);
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new SimpleChannelHandler());
			}});
		bootstrap.setOption("keepAlive", Boolean.TRUE);
		Channel serverChanel = bootstrap.bind(new InetSocketAddress(0));
		
		serverChanel.close();
		nettyPool.shutdownNow();
		
		Thread.currentThread().sleep(Long.MAX_VALUE);
	}

}
