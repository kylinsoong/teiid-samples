package org.jboss.netty.example.echo;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class EchoClient {
	
	private final String host;
	private final int port;
	private final int firstMessageSize;

	public EchoClient(String host, int port, int firstMessageSize) {
		this.host = host;
		this.port = port;
		this.firstMessageSize = firstMessageSize;
	}
	
	public void run() {
		// Configure the client.
		ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		// Set up the pipeline factory.
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new EchoClientHandler(firstMessageSize));
			}
		});
		// Start the connection attempt.
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(host,port));
		// Wait until the connection is closed or the connection attempt fails.
		future.getChannel().getCloseFuture().awaitUninterruptibly();
		// Shut down thread pools to exit.
		bootstrap.releaseExternalResources();
	}

	public static void main(String[] args) {

		final String host = "localhost";
		final int port = 8080;
		final int firstMessageSize = 256;

		new EchoClient(host, port, firstMessageSize).run();
	}

}
