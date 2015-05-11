package org.teiid.test.bufferservice;

import org.teiid.common.buffer.BufferManager;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.services.BufferServiceImpl;

public class BufferserviceTest {

	public static void main(String[] args) throws Exception {
		
		int processorBatchSize = BufferManager.DEFAULT_PROCESSOR_BATCH_SIZE;
		
		int maxProcessingBytes = 1 << 21;
		
		int maxReserveKb = BufferManager.DEFAULT_RESERVE_BUFFER_KB;
		int maxProcessingKb = BufferManager.DEFAULT_MAX_PROCESSING_KB;
		boolean inlineLobs = true;

		BufferServiceImpl bufferService = new BufferServiceImpl();
		
		EmbeddedConfiguration config = new EmbeddedConfiguration();
		config.setUseDisk(true);
		config.setBufferDirectory("/home/kylin/tmp");
		
		bufferService.setUseDisk(config.isUseDisk());
		bufferService.setDiskDirectory(config.getBufferDirectory());
		bufferService.start();
		
		BufferManager bufferManager = bufferService.getBufferManager();
		
		
		System.out.println(bufferManager);
	}


}
