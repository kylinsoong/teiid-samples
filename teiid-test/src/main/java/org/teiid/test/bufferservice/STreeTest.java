package org.teiid.test.bufferservice;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.teiid.common.buffer.STree;
import org.teiid.common.buffer.STree.InsertMode;
import org.teiid.common.buffer.TupleBrowser;
import org.teiid.common.buffer.impl.BufferFrontedFileStoreCache;
import org.teiid.common.buffer.impl.BufferManagerImpl;
import org.teiid.common.buffer.impl.MemoryStorageManager;
import org.teiid.common.buffer.impl.SplittableStorageManager;
import org.teiid.core.TeiidComponentException;
import org.teiid.core.TeiidProcessingException;
import org.teiid.core.types.DataTypeManager;
import org.teiid.query.processor.CollectionTupleSource;
import org.teiid.query.sql.symbol.ElementSymbol;

public class STreeTest {

	public static void main(String[] args) throws Exception {
		
		testRemoveAll();
		
		testUnOrderedInsert();
		
		testOrderedInsert();
		
		testStorageWrites();
		
		testSearch();
	}

	static void testSearch() throws TeiidComponentException, TeiidProcessingException {
		ElementSymbol x = new ElementSymbol("x");
		x.setType(DataTypeManager.DefaultDataClasses.INTEGER);
		
		ElementSymbol y = new ElementSymbol("y");
		y.setType(DataTypeManager.DefaultDataClasses.INTEGER);
		
		List<ElementSymbol> elements = Arrays.asList(x, y);
		
		BufferManagerImpl bm = new BufferManagerImpl();
		
		MemoryStorageManager storageManager = new MemoryStorageManager();
		SplittableStorageManager ssm = new SplittableStorageManager(storageManager);
		ssm.setMaxFileSizeDirect(MemoryStorageManager.MAX_FILE_SIZE);
		
		BufferFrontedFileStoreCache fsc = new BufferFrontedFileStoreCache();
		fsc.setBufferManager(bm);
		fsc.setDirect(false);
		fsc.setMaxStorageObjectSize(1<<20);
		fsc.setMemoryBufferSpace(1<<21);
		fsc.setStorageManager(ssm);
		fsc.initialize();
		
		bm.setCache(fsc);
		bm.initialize();
		
		STree stree = bm.createSTree(elements, "1", 1);
		
		int size = 1<<16;
		
		for (int i = size; i > 0; i--) {
			stree.insert(Arrays.asList(i, i), InsertMode.NEW, -1);
		}
		
		System.out.println(stree);
		System.out.println(stree.getHeight());
		System.out.println(stree.getRowCount());
		System.out.println(stree.getKeyLength());
		System.out.println(stree.isPreferMemory());
		System.out.println();
		
		stree.compact();
		
		for (int i = 0; i < size; i++) {
			TupleBrowser tb = new TupleBrowser(stree, new CollectionTupleSource(Collections.singletonList(Arrays.asList(i)).iterator()), true);
			System.out.println(tb.nextTuple());
		}
	}

	static void testStorageWrites() throws TeiidComponentException {
		
		ElementSymbol x = new ElementSymbol("x");
		x.setType(String.class);
		List<ElementSymbol> elements = Arrays.asList(x);
		
		BufferManagerImpl bm = new BufferManagerImpl();
		MemoryStorageManager storageManager = new MemoryStorageManager();
		SplittableStorageManager ssm = new SplittableStorageManager(storageManager);
		ssm.setMaxFileSizeDirect(MemoryStorageManager.MAX_FILE_SIZE);
		
		BufferFrontedFileStoreCache fsc = new BufferFrontedFileStoreCache();
		fsc.setBufferManager(bm);
		fsc.setDirect(false);
		fsc.setMaxStorageObjectSize(1<<19);
		fsc.setMemoryBufferSpace(1<<19);
		fsc.setStorageManager(ssm);
		fsc.initialize();
		
		bm.setCache(fsc);
		bm.initialize();
		
		STree stree = bm.createSTree(elements, "1", 1);
		
		int size = 1000;
		
		for (int i = 0; i < size; i++) {
			stree.insert(Arrays.asList(new String(new byte[1000])), InsertMode.ORDERED, size);
		}
		
		System.out.println(stree);
		System.out.println(stree.getHeight());
		System.out.println(stree.getRowCount());
		System.out.println(stree.getKeyLength());
		System.out.println(stree.isPreferMemory());
		System.out.println();
		
		for (int i = 0; i < size; i++) {
			List<?> list = stree.remove(Arrays.asList(new String(new byte[1000])));
			System.out.println(list);
		}
		
	}

	static void testOrderedInsert() throws TeiidComponentException {

		ElementSymbol x = new ElementSymbol("x");
		x.setType(DataTypeManager.DefaultDataClasses.INTEGER);
		List<ElementSymbol> elements = Arrays.asList(x);
		
		BufferManagerImpl bm = new BufferManagerImpl();
		bm.setProcessorBatchSize(4);
		
		MemoryStorageManager storageManager = new MemoryStorageManager();
		SplittableStorageManager ssm = new SplittableStorageManager(storageManager);
		ssm.setMaxFileSizeDirect(MemoryStorageManager.MAX_FILE_SIZE);
		
		BufferFrontedFileStoreCache fsc = new BufferFrontedFileStoreCache();
		fsc.setBufferManager(bm);
		fsc.setDirect(false);
		fsc.setMaxStorageObjectSize(1<<20);
		fsc.setMemoryBufferSpace(1<<21);
		fsc.setStorageManager(ssm);
		fsc.initialize();
		
		bm.setCache(fsc);
		bm.initialize();
		
		STree stree = bm.createSTree(elements, "1", 1);
		
		int size = (1<<16) + (1<<4) + 1;
				
		for (int i = 0; i < size; i++) {
			stree.insert(Arrays.asList(i), InsertMode.ORDERED, size);
		}
		
		System.out.println(stree);
		System.out.println(stree.getHeight());
		System.out.println(stree.getRowCount());
		System.out.println(stree.getKeyLength());
		System.out.println(stree.isPreferMemory());
		System.out.println();
		
		for (int i = 0; i < size; i++){
			List<?> list = stree.remove(Arrays.asList(i));
			System.out.println(list);
		}
	}

	static void testUnOrderedInsert() throws TeiidComponentException {
		
		ElementSymbol x = new ElementSymbol("x");
		x.setType(DataTypeManager.DefaultDataClasses.INTEGER);
		List<ElementSymbol> elements = Arrays.asList(x);
		
		BufferManagerImpl bm = new BufferManagerImpl();
		bm.setProcessorBatchSize(16);
		
		MemoryStorageManager storageManager = new MemoryStorageManager();
		SplittableStorageManager ssm = new SplittableStorageManager(storageManager);
		ssm.setMaxFileSizeDirect(MemoryStorageManager.MAX_FILE_SIZE);
		
		BufferFrontedFileStoreCache fsc = new BufferFrontedFileStoreCache();
		fsc.setBufferManager(bm);
		fsc.setDirect(false);
		fsc.setMaxStorageObjectSize(1<<20);
		fsc.setMemoryBufferSpace(1<<21);
		fsc.setStorageManager(ssm);
		fsc.initialize();
		
		bm.setCache(fsc);
		bm.initialize();
		
		STree stree = bm.createSTree(elements, "1", 1);
		
		int size = (1<<16) + (1<<4) + 1;
		
		int logSize = stree.getExpectedHeight(size);
		
		for (int i = 0; i < size; i++) {
			stree.insert(Arrays.asList(i), InsertMode.NEW, logSize);
		}
		
		System.out.println(stree);
		System.out.println(stree.getHeight());
		System.out.println(stree.getRowCount());
		System.out.println(stree.getKeyLength());
		System.out.println(stree.isPreferMemory());
		System.out.println();
		
		
	}

	@SuppressWarnings("unchecked")
	static void testRemoveAll() throws TeiidComponentException {
		
		ElementSymbol x = new ElementSymbol("x");
		x.setType(DataTypeManager.DefaultDataClasses.INTEGER);
		
		ElementSymbol y = new ElementSymbol("x");
		y.setType(DataTypeManager.DefaultDataClasses.STRING);
		
		List<ElementSymbol> elements = Arrays.asList(x, y);
		
		BufferManagerImpl bm = new BufferManagerImpl();
		
		MemoryStorageManager storageManager = new MemoryStorageManager();
		SplittableStorageManager ssm = new SplittableStorageManager(storageManager);
		ssm.setMaxFileSizeDirect(MemoryStorageManager.MAX_FILE_SIZE);
		
		BufferFrontedFileStoreCache fsc = new BufferFrontedFileStoreCache();
		fsc.setBufferManager(bm);
		fsc.setDirect(false);
		fsc.setMaxStorageObjectSize(1<<20);
		fsc.setMemoryBufferSpace(1<<21);
		fsc.setStorageManager(ssm);
		fsc.initialize();
		
		bm.setCache(fsc);
		bm.initialize();
		
		STree stree = bm.createSTree(elements, "1", 1);
		
		for (int i = 20000; i > 0; i--) {
			stree.insert(Arrays.asList(i, String.valueOf(i)), InsertMode.NEW, -1);
		}
		
		System.out.println(stree);
		System.out.println(stree.getHeight());
		System.out.println(stree.getRowCount());
		System.out.println(stree.getKeyLength());
		System.out.println(stree.isPreferMemory());
		System.out.println();
		
		for (int i = 20000; i > 0; i--) {
			List<?> list = stree.remove(Arrays.asList(i));
			System.out.println(list);
		}
		
		System.out.println();
		
		System.out.println(stree.getRowCount());
	}

}
