package org.teiid.test.bufferservice;

import java.io.IOException;
import java.util.Arrays;

import org.teiid.common.buffer.FileStore;
import org.teiid.common.buffer.impl.EncryptedStorageManager;
import org.teiid.common.buffer.impl.MemoryStorageManager;
import org.teiid.core.TeiidComponentException;

public class EncryptedStorageManagerTest {

	public static void main(String[] args) throws TeiidComponentException, IOException {

        testSetLength();
        
        testReadWrite();
        
        testInvalidRead();
     
	}

	static void testInvalidRead() throws TeiidComponentException, IOException {
		MemoryStorageManager msm = new MemoryStorageManager();
        EncryptedStorageManager ssm = new EncryptedStorageManager(msm);
        ssm.initialize();
        String tsID = "0";
        FileStore store = ssm.createFileStore(tsID);
        try {
			store.read(1, new byte[1], 0, 1);
		} catch (Exception e) {
			 System.out.println(e.getMessage());
		}
	}

	static void testReadWrite() throws TeiidComponentException, IOException {
		
		MemoryStorageManager msm = new MemoryStorageManager();
        EncryptedStorageManager ssm = new EncryptedStorageManager(msm);
        ssm.initialize();
        String tsID = "0";
        FileStore store = ssm.createFileStore(tsID);
        
        for (int i = 0; i < 500; i++) {
        	byte[] b = new byte[i];
        	Arrays.fill(b, (byte)i);
        	store.write(b, 0, i);
        	store.readFully(store.getLength()-b.length, b, 0, b.length);
        	for (int j = 0; j < b.length; j++) {
        		System.out.println(b[j]);
        	}
        }
        
        int start = 0;
        for (int i = 0; i < 500; i++) {
        	byte[] b = new byte[i];
        	store.readFully(start, b, 0, b.length);
        	for (int j = 0; j < b.length; j++) {
        		System.out.println(b[j]);
        	}
        	start += i;
        }
        
        store.readFully(0, new byte[(int) store.getLength()], 0, (int) store.getLength());
        store.write(16, new byte[100], 0, 100);
        store.write((int)store.getLength() - 100, new byte[99], 0, 99);
	}

	static void testSetLength() throws TeiidComponentException, IOException {

		MemoryStorageManager msm = new MemoryStorageManager();
        EncryptedStorageManager ssm = new EncryptedStorageManager(msm);
        ssm.initialize();
        String tsID = "0"; 
        
        FileStore store = ssm.createFileStore(tsID);
        
        System.out.println(store);
        System.out.println(store.getLength());
        
        FileStorageManagerTest.writeBytes(store);
        
        System.out.println(msm.getCreated());
        System.out.println(store.getLength());
        
        store.readFully(0, new byte[100], 0, 100);
	}

}
