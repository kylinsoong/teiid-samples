package org.teiid.test.bufferservice;

import java.io.IOException;

import org.teiid.common.buffer.FileStore;
import org.teiid.common.buffer.impl.MemoryStorageManager;
import org.teiid.common.buffer.impl.SplittableStorageManager;

public class SplittableStorageManagerTest {

	public static void main(String[] args) throws IOException {

		testCreatesSpillFiles();
		
		testTruncate();
	}

	static void testTruncate() throws IOException {
		
		MemoryStorageManager msm = new MemoryStorageManager();
        SplittableStorageManager ssm = new SplittableStorageManager(msm);
        ssm.setMaxFileSizeDirect(2048);
        
        String tsID = "0";
        
        FileStore store = ssm.createFileStore(tsID);
        System.out.println(store);
        System.out.println(store.getLength());
        
        FileStorageManagerTest.writeBytes(store);
        
        System.out.println(msm.getCreated());
        
        FileStorageManagerTest.writeBytes(store);
        
        System.out.println(msm.getCreated());
	}

	static void testCreatesSpillFiles() throws IOException {

		MemoryStorageManager msm = new MemoryStorageManager();
        SplittableStorageManager ssm = new SplittableStorageManager(msm);
        ssm.setMaxFileSizeDirect(2048);
        
        String tsID = "0";
        
        FileStore store = ssm.createFileStore(tsID);
        System.out.println(store);
        
        FileStorageManagerTest.writeBytes(store);
        
        System.out.println(msm.getCreated());
        
        store.setLength(10000);
        
        System.out.println(msm.getCreated());
        
        store.setLength(100);
        
        System.out.println(msm.getCreated());
        
        store.remove();
        
        System.out.println(msm.getRemoved());
        
        
	}

}
