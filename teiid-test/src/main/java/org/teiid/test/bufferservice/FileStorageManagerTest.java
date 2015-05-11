package org.teiid.test.bufferservice;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import org.teiid.common.buffer.FileStore;
import org.teiid.common.buffer.FileStore.FileStoreOutputStream;
import org.teiid.common.buffer.impl.FileStorageManager;
import org.teiid.common.buffer.impl.SplittableStorageManager;
import org.teiid.core.TeiidComponentException;

public class FileStorageManagerTest {

	public static void main(String[] args) throws TeiidComponentException, IOException {
		
		getStorageManager(20, null);
		
		testInitialRead();
		
		testWrite();
		
		testPositionalWrite();
		
		testMaxSpace();
		
		testMaxSpaceSplit();
		
		testSetLength();
		
		testFlush();
		
		testGrowth();
		
		testClose();
		
		testWritingMultipleFiles();
	}

	static void testWritingMultipleFiles() throws TeiidComponentException, IOException {
		
		FileStorageManager sm = getStorageManager(null, "/home/kylin/tmp/buffer/t10");
		FileStore store = sm.createFileStore("0");
		String contentOrig = new String("some file content this will stored in same tmp file with another");
		OutputStream out = store.createOutputStream();
		out.write(contentOrig.getBytes(), 0, contentOrig.getBytes().length);
        out.close();
        
        out = store.createOutputStream();
        long start = store.getLength();
        byte[] bytesOrig = new byte[2048];
        r.nextBytes(bytesOrig);
        out.write(bytesOrig, 0, 2048);
        
        byte[] readContent = new byte[2048];
        InputStream in = store.createInputStream(0, contentOrig.getBytes().length);        
    	int c = in.read(readContent, 0, 3000);
    	System.out.println(c);
       	c = in.read(readContent, 0, 3000);
       	in.close();
		
       	in = store.createInputStream(start, 2048);
        c = in.read(readContent, 0, 3000);
       	c = in.read(readContent, 0, 3000);
       	in.close();   
	}

	static void testClose() throws TeiidComponentException, IOException {

		FileStorageManager sm = getStorageManager(null, "/home/kylin/tmp/buffer/t9");
		FileStore store = sm.createFileStore("0");
		FileStoreOutputStream fsos = store.createOutputStream(2);
		fsos.write(new byte[100000]);
    	fsos.close();
	}

	static void testGrowth() throws TeiidComponentException, IOException {
		
		FileStorageManager sm = getStorageManager(null, "/home/kylin/tmp/buffer/t8");
		FileStore store = sm.createFileStore("0");
		FileStoreOutputStream fsos = store.createOutputStream(1<<15);
		System.out.println(fsos.getBuffer().length);
		fsos.write(1);
		fsos.write(new byte[1<<14]);
    	fsos.flush();
    	System.out.println(fsos.getCount());
    	System.out.println(fsos.getBuffer().length);
	}

	static void testFlush() throws TeiidComponentException, IOException {

		FileStorageManager sm = getStorageManager(null, "/home/kylin/tmp/buffer/t7");
		FileStore store = sm.createFileStore("0");
		FileStoreOutputStream fsos = store.createOutputStream(2);
		fsos.write(new byte[3]);
    	fsos.write(1);
    	fsos.flush();
    	System.out.println(fsos.getCount());
	}

	static void testSetLength() throws TeiidComponentException, IOException {
		
		FileStorageManager sm = getStorageManager(null, "/home/kylin/tmp/buffer/t6");
		String tsID = "0";
		FileStore store = sm.createFileStore(tsID);
		
		store.setLength(1000);
		System.out.println(sm.getUsedBufferSpace());
		
		store.setLength(200);
		System.out.println(sm.getUsedBufferSpace());
		
		store.setLength(1000);
		System.out.println(sm.getUsedBufferSpace());
	}

	static void testMaxSpaceSplit() throws TeiidComponentException {

		FileStorageManager sm = getStorageManager(null, "/home/kylin/tmp/buffer/t5");
		sm.setMaxBufferSpace(1);
		String tsID = "0"; 
		
		SplittableStorageManager ssm = new SplittableStorageManager(sm);
        FileStore store = ssm.createFileStore(tsID);
        try {
			writeBytes(store);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.out.println(sm.getUsedBufferSpace());
		}
	}

	static void testMaxSpace() throws TeiidComponentException {
		
		FileStorageManager sm = getStorageManager(null, "/home/kylin/tmp/buffer/t4");
		sm.setMaxBufferSpace(1);
		String tsID = "0"; 
		FileStore store = sm.createFileStore(tsID);
		try {
			writeBytes(store);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.out.println(sm.getUsedBufferSpace());
		}
	}

	static void testPositionalWrite() throws TeiidComponentException, IOException {
		
		FileStorageManager sm = getStorageManager(null, "/home/kylin/tmp/buffer/t3");
		String tsID = "0";  
		FileStore store = sm.createFileStore(tsID);
		byte[] expectedBytes = writeBytes(store, 2048);
		System.out.println(sm.getUsedBufferSpace());
		writeBytes(store, 4096);
		System.out.println(sm.getUsedBufferSpace());
		
		byte[] bytesRead = new byte[2048];        
        store.readFully(2048, bytesRead, 0, bytesRead.length);
        
        System.out.println(expectedBytes == bytesRead);
        
        store.remove();
        
        System.out.println(sm.getUsedBufferSpace());	
	}
	

	static Random r = new Random();
	
	static void testWrite() throws TeiidComponentException, IOException {
		FileStorageManager sm = getStorageManager(30, "/home/kylin/tmp/buffer/t2");
		String tsID = "0";  
		FileStore store = sm.createFileStore(tsID);
		writeBytes(store);
		System.out.println(sm.getUsedBufferSpace());
		store.remove();
		System.out.println(sm.getUsedBufferSpace());
	}
	
	static void writeBytes(FileStore store) throws IOException {
		writeBytes(store, store.getLength());
	}

	static byte[] writeBytes(FileStore store, long start)
			throws IOException {
		byte[] bytes = new byte[2048];
        r.nextBytes(bytes);
        store.write(start, bytes, 0, bytes.length);
        byte[] bytesRead = new byte[2048];        
        store.readFully(start, bytesRead, 0, bytesRead.length);
        return bytes;
	}

	static void testInitialRead() throws TeiidComponentException, IOException {
		
		FileStorageManager sm = getStorageManager(30, "/home/kylin/tmp/buffer/t1");
		String tsID = "0";  
		FileStore store = sm.createFileStore(tsID);
		byte[] buf = new byte[1];
		store.read(0, buf, 0, 1);
		System.out.println(new String(buf));
		System.out.println(store);
	}

	static FileStorageManager getStorageManager(Integer openFiles, String dir) throws TeiidComponentException {
		
		FileStorageManager sm = new FileStorageManager();
		sm.setStorageDirectory(dir == null ? "/home/kylin/tmp/buffer" : dir);
		if (openFiles != null) {
        	sm.setMaxOpenFiles(openFiles);
        }
		sm.initialize();
		return sm;
	}

}
