package com.teiid.quickstart.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

public class EchoClient {
	
	private SocketChannel socketChannel = null ;
	
	private ByteBuffer sendBuffer = ByteBuffer.allocate(32 * 1024); 
	
	private ByteBuffer receiveBuffer = ByteBuffer.allocate(32 * 1024);
	
	private Charset charset = Charset.forName("UTF-8");
	
	private Selector selector;  
    
    private boolean isSend = false; 
    
    public EchoClient() throws IOException {
    	socketChannel = SocketChannel.open();
    	InetSocketAddress isa = new InetSocketAddress(EchoServer.HOST, EchoServer.PORT); 
    	socketChannel.connect(isa); 
    	socketChannel.configureBlocking(false);
    	System.out.println("Client: connection establish[" + isa.getHostName() + ":" + isa.getPort() + "]");
    	selector = Selector.open();  
    }
    
    public void receiveFromUser() {  
        try {  
            BufferedReader localReader = new BufferedReader(new InputStreamReader(System.in));  
            String msg = null;  
            while ((msg = localReader.readLine()) != null) {  
            	System.out.println("Read From System Console: " + msg);  
                isSend = true;  
                System.out.println("Command line thread set isSend: " + isSend);  
                synchronized (sendBuffer) {  
                    sendBuffer.put(encode(msg + "\r\n"));  
                }  
                if (msg.equals("bye")){  
                	System.out.println("Client Exit");  
                    break;  
                }  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    } 
    
    public void talk() throws Exception {  
    	System.out.println("SocketChannel register [" + SelectionKey.OP_READ  + "] and [" + SelectionKey.OP_WRITE + "] to selector");  
        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);  
        while (selector.select() > 0) {  
        	for(Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it.hasNext();) {
        		SelectionKey key = null;
        		try {
					key = it.next();
					it.remove();
					if (key.isReadable()) {  
					    receive(key);  
					}  
					if (key.isWritable()) {  
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
		if(!isSend) {  
            return;  
        } 
		SocketChannel socketChannel = (SocketChannel) key.channel(); 
		synchronized (sendBuffer) {  
            sendBuffer.flip();   
            socketChannel.write(sendBuffer);  
            sendBuffer.compact();  
        }
		isSend = false; 
		System.out.println("Send method set isSend: " + isSend);
	}

	private void receive(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();  
        socketChannel.read(receiveBuffer);  
        receiveBuffer.flip();  
        String receiveData = decode(receiveBuffer);  
  
        if (receiveData.indexOf("\n") == -1){  
            return;  
        } 
        
        String outputData = receiveData.substring(0, receiveData.indexOf("\n") + 1);  
        System.out.println("Recieve Data: " + outputData);  
        if (outputData.equals("echo:bye\r\n")) {  
            key.cancel();  
            socketChannel.close();  
            System.out.println("Exit");  
            selector.close();  
            System.exit(0);  
        }  
  
        ByteBuffer temp = encode(outputData);  
        receiveBuffer.position(temp.limit());  
        receiveBuffer.compact();  
	}

	private String decode(ByteBuffer buffer) {
		CharBuffer charBuffer = charset.decode(buffer);  
        return charBuffer.toString(); 
	}

	private ByteBuffer encode(String str) {
		return charset.encode(str); 
	}

	public static void main(String[] args) throws Exception {
		final EchoClient client = new EchoClient();  
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				client.receiveFromUser(); 
			}}).start();
		
        client.talk();  
	}

}
