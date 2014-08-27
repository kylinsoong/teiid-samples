package com.teiid.quickstart.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class EchoServer {
	
	public final static int PORT = 8000;
	public final static String HOST = "localhost";
	
	private Selector selector = null; 

	private ServerSocketChannel serverSocketChannel = null; 
	
	private Charset charset = Charset.forName("UTF-8");
	
	public EchoServer() throws Exception {
		selector = Selector.open() ;
		serverSocketChannel = ServerSocketChannel.open() ;
		serverSocketChannel.socket().setReuseAddress(true);
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.socket().bind(new InetSocketAddress(HOST, PORT));
		System.out.println("Server is start[" + HOST + ":" + PORT + "]");
	}
	
	public void service() throws Exception {
		
		System.out.println("ServerSocketChannel register [" + SelectionKey.OP_ACCEPT + "] to selector");
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT ); 
		
		while (selector.select() > 0) {
			for(Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it.hasNext();){
				SelectionKey key = null;
				
				try {
					key = it.next();
					it.remove();
					if (key.isAcceptable()) {
						System.out.println("Selection Key isAcceptable: " + key.isAcceptable());
						ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
						SocketChannel socketChannel = ssc.accept();
						System.out.println("Recieved Client Connection:" + socketChannel.socket().getInetAddress() + ":" + socketChannel.socket().getPort());
						socketChannel.configureBlocking(false);
						ByteBuffer buffer = ByteBuffer.allocate(1024);  
						socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, buffer);
						System.out.println("SocketChannel register [" + SelectionKey.OP_READ  + "] and [" + SelectionKey.OP_WRITE + "] to selector");
					}
					
					if (key.isReadable()) {
						System.out.println("Selection Key isReadable");
						receive(key);
					}
					
					if (key.isWritable()) {
//						System.out.println("Selection Key isWritable");
						send(key);
					}
				} catch (Exception e) {
					 if (key != null) {  
                         key.cancel();  
                         key.channel().close();  
                     }  
					 throw e;
				}
			}
			
			
		}
	}

	private void send(SelectionKey key) throws IOException {
		ByteBuffer buffer = (ByteBuffer) key.attachment();
		SocketChannel socketChannel = (SocketChannel) key.channel(); 
		buffer.flip();  
		String data = decode(buffer); 
		if (data.indexOf("\r\n") == -1){  
            return;  
        }  
		String outputData = data.substring(0, data.indexOf("\n") + 1);
		System.out.println("outputData: " + outputData);
		ByteBuffer outputBuffer = encode("echo:" + outputData); 
		
		while (outputBuffer.hasRemaining()){  
            socketChannel.write(outputBuffer);  
        }
		
		ByteBuffer temp = encode(outputData);
		buffer.position(temp.limit()); 
		buffer.compact();
		
		if (outputData.equals("bye\r\n")) {
			key.cancel();
			socketChannel.close(); 
			System.out.println("Close Client Connection");
		}
	}

	private ByteBuffer encode(String str) {
		return charset.encode(str); 
	}

	private String decode(ByteBuffer buffer) {
		CharBuffer charBuffer = charset.decode(buffer);  
        return charBuffer.toString();  
	}

	private void receive(SelectionKey key) throws IOException {
		System.out.println("receive");
		ByteBuffer buffer = (ByteBuffer) key.attachment();  
		SocketChannel socketChannel = (SocketChannel) key.channel(); 
		ByteBuffer readBuff = ByteBuffer.allocate(32 * 1024);
		socketChannel.read(readBuff); 
		readBuff.flip();  
		buffer.limit(buffer.capacity()); 
		buffer.put(readBuff); 
		System.out.println("Recieved data: " + decode(buffer));
	}

	public static void main(String[] args) throws Exception {

		new EchoServer().service();
	}

}
