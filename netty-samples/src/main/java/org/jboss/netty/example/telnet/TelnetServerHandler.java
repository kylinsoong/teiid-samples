package org.jboss.netty.example.telnet;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class TelnetServerHandler extends SimpleChannelUpstreamHandler {

	private static final Logger logger = Logger.getLogger(TelnetServerHandler.class.getName());
	
	private final AtomicLong transferredBytes = new AtomicLong();

	public long getTransferredBytes() {
		return transferredBytes.get();
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		
		ChannelBuffer buf = (ChannelBuffer) e.getMessage();
		byte[] dest = new byte[buf.readableBytes()];
		buf.readBytes(dest);
		System.out.println("Received message: "+ new String(dest));
		
		// Send back the received message to the remote peer.
//		transferredBytes.addAndGet(((ChannelBuffer) e.getMessage()).readableBytes());
//		e.getChannel().write(e.getMessage());
//		System.out.println("Server: " + e.getMessage());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		// Close the connection when an exception is raised.
		logger.log(Level.WARNING, "Unexpected exception from downstream.", e.getCause());
		e.getChannel().close();
	}
}
