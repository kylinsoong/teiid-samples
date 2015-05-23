package org.teiid.test.bufferservice;

import java.io.File;
import java.io.IOException;

import org.teiid.common.buffer.BufferManager;
import org.teiid.common.buffer.StorageManager;
import org.teiid.common.buffer.impl.BufferFrontedFileStoreCache;
import org.teiid.common.buffer.impl.BufferManagerImpl;
import org.teiid.common.buffer.impl.EncryptedStorageManager;
import org.teiid.common.buffer.impl.FileStorageManager;
import org.teiid.common.buffer.impl.MemoryStorageManager;
import org.teiid.common.buffer.impl.SplittableStorageManager;
import org.teiid.core.TeiidComponentException;
import org.teiid.core.util.FileUtils;
public class BufferManagerTest {

	public static void main(String[] args) throws Exception {
		
//		testFloating();
		
		testMain();
		
//		testConfig();
	}
	
	@SuppressWarnings("unused")
	static void testConfig() {
		
		int processorBatchSize ;
		int maxReserveKb ;
		int maxProcessingKb ;
		boolean inlineLobs = true;
		int maxOpenFiles ;
		
		long maxBufferSpace ;
		long maxFileSize ;
		boolean encryptFiles = false;
		int maxStorageObjectSize ;
		boolean memoryBufferOffHeap = false;
		long memoryBufferSpace ;
	}

	static void testFloating() {
		System.out.println(FileStorageManager.DEFAULT_MAX_BUFFERSPACE);
		System.out.println(50L * 1<<30);
		System.out.println(50L * (1<<30) / (1<<20));
		System.out.println(FileStorageManager.DEFAULT_MAX_BUFFERSPACE>>20);
		System.out.println(1024 >> 3);
		System.out.println(FileStorageManager.DEFAULT_MAX_BUFFERSPACE / (1<<20));
		
		System.out.println(13 / (1<<3));
		System.out.println(13 >> 3);
		
		System.out.println();
		System.out.println(1<<1);
		System.out.println(1<<10 * 1<<10);
		System.out.println(MB);
		System.out.println(FileStorageManager.DEFAULT_MAX_BUFFERSPACE>>20 * MB);
		System.out.println();
		
		System.out.println(2<<1);
		System.out.println(2<<4);
		System.out.println(3<<4);
		System.out.println(10<<4);
		System.out.println();
		
		System.out.println(1<<0);
		System.out.println(1<<10);
		System.out.println(1<<20);
		System.out.println(1<<30);
		System.out.println();
		
		System.out.println(1073741824>>10);
		System.out.println(1048576>>10);
		System.out.println(1024>>10);
		System.out.println(FileStorageManager.DEFAULT_MAX_BUFFERSPACE>>20 * MB);
		System.out.println(50L * 1024 * MB);
		System.out.println(50L * 1<<30);
		System.out.println(50L<<30);
	}

	static FileStorageManager fsm;
	static BufferFrontedFileStoreCache fsc;
	static final long MB = 1<<20;
	
	static int workingMaxReserveKb;

	static void testMain() throws TeiidComponentException, IOException {
		
		boolean useDisk = true;
		File bufferDir = new File("/home/kylin/tmp/buffer");
		
		int processorBatchSize = BufferManager.DEFAULT_PROCESSOR_BATCH_SIZE;
		int maxReserveKb = BufferManager.DEFAULT_RESERVE_BUFFER_KB;
		int maxProcessingKb = BufferManager.DEFAULT_MAX_PROCESSING_KB;
		boolean inlineLobs = true;
		int maxOpenFiles = FileStorageManager.DEFAULT_MAX_OPEN_FILES;
		long maxBufferSpace = FileStorageManager.DEFAULT_MAX_BUFFERSPACE>>20;
		long maxFileSize = SplittableStorageManager.DEFAULT_MAX_FILESIZE;
		boolean encryptFiles = false;
		int maxStorageObjectSize = BufferFrontedFileStoreCache.DEFAuLT_MAX_OBJECT_SIZE;
		boolean memoryBufferOffHeap = false;
		long memoryBufferSpace = -1;
		
		
		BufferManagerImpl bufferMgr = new BufferManagerImpl();
		bufferMgr.setProcessorBatchSize(processorBatchSize);
		bufferMgr.setMaxReserveKB(maxReserveKb);
		bufferMgr.setMaxProcessingKB(maxProcessingKb);
		bufferMgr.setInlineLobs(inlineLobs);
		bufferMgr.initialize();
		
		if(useDisk) {
			System.out.println("Starting BufferManager using " + bufferDir);
			if (!bufferDir.exists()) {
    			bufferDir.mkdirs();
    		}
			
			FileUtils.removeChildrenRecursively(bufferDir);
			
			fsm = new FileStorageManager();
			fsm.setStorageDirectory(bufferDir.getCanonicalPath());
			fsm.setMaxOpenFiles(maxOpenFiles);
			fsm.setMaxBufferSpace(maxBufferSpace * MB);//50GB
			SplittableStorageManager ssm = new SplittableStorageManager(fsm);
			ssm.setMaxFileSize(maxFileSize);
			StorageManager sm = ssm;
			if (encryptFiles){
				sm = new EncryptedStorageManager(ssm);
			}
			fsc = new BufferFrontedFileStoreCache();
			fsc.setBufferManager(bufferMgr);
			fsc.setMaxStorageObjectSize(maxStorageObjectSize);
            fsc.setDirect(memoryBufferOffHeap);
            int batchOverheadKB = (int)(memoryBufferSpace<0?(bufferMgr.getMaxReserveKB()<<8):memoryBufferSpace)>>20;
            bufferMgr.setMaxReserveKB(Math.max(0, bufferMgr.getMaxReserveKB() - batchOverheadKB));
            if (memoryBufferSpace < 0){
            	fsc.setMemoryBufferSpace(((long)bufferMgr.getMaxReserveKB()) << 8);
            } else {
            	fsc.setMemoryBufferSpace(memoryBufferSpace << 20);
            }
            if (!memoryBufferOffHeap && maxReserveKb < 0) {
            	bufferMgr.setMaxReserveKB(bufferMgr.getMaxReserveKB() - (int)Math.min(bufferMgr.getMaxReserveKB(), (fsc.getMemoryBufferSpace()>>10)));
            }
            fsc.setStorageManager(sm);
            fsc.initialize();
            bufferMgr.setCache(fsc);
            workingMaxReserveKb = bufferMgr.getMaxReserveKB();
		} else {
			bufferMgr.setCache(new MemoryStorageManager());
		}
		
		System.out.println("DONE");
	}

}
