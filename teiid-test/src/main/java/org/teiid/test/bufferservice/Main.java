package org.teiid.test.bufferservice;

public class Main {

	public static void main(String[] args) throws Exception {
		
		TupleBufferTest.main(args);
		
		TupleBatchTest.main(args);
		
		STreeTest.main(args);
		
		LrfuEvictionQueueTest.main(args);
		
		SplittableStorageManagerTest.main(args);
		
		EncryptedStorageManagerTest.main(args);
		
		ConcurrentBitSetTest.main(args);
		
		BlockByteBufferTest.main(args);
	}

}
