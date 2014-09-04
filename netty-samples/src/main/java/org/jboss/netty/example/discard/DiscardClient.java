package org.jboss.netty.example.discard;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class DiscardClient {
	
	private final String host;
	private final int port;
	private final int firstMessageSize;

	public DiscardClient(String host, int port, int firstMessageSize) {
		this.host = host;
		this.port = port;
		this.firstMessageSize = firstMessageSize;
	}
	
	public void run() {
		ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		bootstrap.setPipelineFactory(new ChannelPipelineFactory(){

			@Override
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new DiscardClientHandler(firstMessageSize));
			}});
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
		future.getChannel().getCloseFuture().awaitUninterruptibly();
		bootstrap.releaseExternalResources();
	}

	public static void main(String[] args) {
		final String host = "localhost";
		final int port = 8080;
		final int firstMessageSize = 256;
		new DiscardClient(host, port, firstMessageSize).run();
	}

}
