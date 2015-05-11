package org.teiid.test.bufferservice;

import java.util.Arrays;
import java.util.List;

import org.teiid.common.buffer.STree;
import org.teiid.common.buffer.STree.InsertMode;
import org.teiid.common.buffer.impl.BufferFrontedFileStoreCache;
import org.teiid.common.buffer.impl.BufferManagerImpl;
import org.teiid.core.TeiidComponentException;
import org.teiid.query.sql.symbol.ElementSymbol;

public class BufferManagerTest {

	public static void main(String[] args) throws TeiidComponentException {

		BufferManagerImpl bm = BufferManagerFactory.createBufferManager();
		bm.setProcessorBatchSize(32);
		bm.setMaxReserveKB(0);//force all to disk
		BufferFrontedFileStoreCache fsc =(BufferFrontedFileStoreCache)bm.getCache();
		fsc.setMaxStorageObjectSize(1 << 19);
		fsc.setMemoryBufferSpace(1 << 19);
		fsc.initialize();
		bm.initialize();
		
		ElementSymbol e1 = new ElementSymbol("x");
		e1.setType(String.class);
		List<ElementSymbol> elements = Arrays.asList(e1);
		STree map = bm.createSTree(elements, "1", 1);
		
		int size = 1000;
		
		for (int i = 0; i < size; i++) {
			map.insert(Arrays.asList(new String(new byte[1000])), InsertMode.ORDERED, size);
		}
		
		System.out.println(map.getRowCount());
		
		for (int i = 0; i < size; i++) {
			map.remove(Arrays.asList(new String(new byte[1000])));
		}
	}

}
