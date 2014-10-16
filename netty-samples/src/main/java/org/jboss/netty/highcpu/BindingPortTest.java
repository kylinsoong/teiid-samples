package org.jboss.netty.highcpu;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
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

public class BindingPortTest {
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws InterruptedException, IOException {

		ExecutorService nettyPool = Executors.newCachedThreadPool();
		ChannelFactory factory = new NioServerSocketChannelFactory(nettyPool, nettyPool, 1);
		ServerBootstrap bootstrap = new ServerBootstrap(factory);
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new SimpleChannelHandler());
			}});
		bootstrap.setOption("keepAlive", Boolean.TRUE);
		Channel serverChanel = bootstrap.bind(new InetSocketAddress("localhost", 31000));
		
		Socket socket = new Socket("localhost", 31000);
		
		AccessibleBufferedInputStream in = new AccessibleBufferedInputStream(socket.getInputStream(), STREAM_BUFFER_SIZE);
		DataInput dis = new DataInputStream(in);
		
//		pressEnterToStart();
		
		serverChanel.close();
		nettyPool.shutdownNow();
		
		System.out.println("Listener Stoped");
		
		System.out.println(dis.readInt());
		
		Thread.currentThread().sleep(Long.MAX_VALUE);
	}

	private static void pressEnterToStart() throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while(true){
			String input = br.readLine();
			if("y".equals(input)) {
				break;
			} else {
				System.out.println("Input 'y' to continue");
			}
			
		}
	}
	
	private final static int STREAM_BUFFER_SIZE = 1<<15;
	
	private static class AccessibleBufferedInputStream extends BufferedInputStream {
		
		public AccessibleBufferedInputStream(InputStream in, int size) {
			super(in, size);
		}

		public byte[] getBuffer() {
			return this.buf;
		}
		
		public int getCount() {
			return this.count;
		}
		
		public int getPosition() {
			return this.pos;
		}
		
		public void setPosition(int pos) {
			this.pos = pos;
		}

	}

	

}
