package org.teiid.test.bufferservice;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialException;

import org.teiid.common.buffer.BufferManager;
import org.teiid.common.buffer.BufferManager.TupleSourceType;
import org.teiid.common.buffer.TupleBatch;
import org.teiid.common.buffer.TupleBuffer;
import org.teiid.common.buffer.TupleBuffer.TupleBufferTupleSource;
import org.teiid.common.buffer.impl.BufferFrontedFileStoreCache;
import org.teiid.common.buffer.impl.BufferManagerImpl;
import org.teiid.common.buffer.impl.MemoryStorageManager;
import org.teiid.common.buffer.impl.SplittableStorageManager;
import org.teiid.core.TeiidComponentException;
import org.teiid.core.TeiidProcessingException;
import org.teiid.core.types.ClobType;
import org.teiid.core.types.DataTypeManager;
import org.teiid.query.sql.symbol.ElementSymbol;

public class TupleBufferTest {

	public static void main(String[] args) throws Exception {
		
		testInit();
		
		testForwardOnly();
		
		testReverseIteration();
		
		testTruncate();
		
		testTruncatePartial();
		
		testLobHandling();
		
	}
	
	static void testLobHandling() throws SerialException, SQLException, TeiidComponentException {
		ElementSymbol x = new ElementSymbol("x");
		x.setType(DataTypeManager.DefaultDataClasses.CLOB);
		List<ElementSymbol> schema = Arrays.asList(x);
		
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
		TupleBuffer tb = bm.createTupleBuffer(schema, "x", TupleSourceType.PROCESSOR);
		tb.setInlineLobs(false);
		
		ClobType c = new ClobType(new SerialClob(new char[0]));
		TupleBatch batch = new TupleBatch(1, new List[] {Arrays.asList(c)});
		tb.addTupleBatch(batch, false);
		System.out.println(tb.getLobReference(c.getReferenceStreamId()));
	}

	static void testTruncatePartial() throws TeiidComponentException, TeiidProcessingException {
		
		ElementSymbol x = new ElementSymbol("x");
		x.setType(DataTypeManager.DefaultDataClasses.INTEGER);
		List<ElementSymbol> schema = Arrays.asList(x);
		
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
		TupleBuffer tb = bm.createTupleBuffer(schema, "x", TupleSourceType.PROCESSOR);
		tb.setBatchSize(64);
		for (int i = 0; i < 65; i++) {
			tb.addTuple(Arrays.asList(1));
		}
		
		TupleBufferTupleSource tupleSource = tb.createIndexedTupleSource();
		tupleSource.setReverse(true);
		
		while(tupleSource.hasNext()) {
			int index = tupleSource.getCurrentIndex();
			List<?> source = tupleSource.nextTuple();
			System.out.println(index + ": " + source);
		}
		
		TupleBatch batch = tb.getBatch(1);
		System.out.println(batch);
		System.out.println(batch.getTuples());
		System.out.println(batch.getEndRow());
		System.out.println(batch.getTerminationFlag());
		System.out.println();
		
		System.out.println(tb.getManagedRowCount());
		System.out.println(tb.getRowCount());
		
		tb.truncateTo(3);
		
		System.out.println(tb.getManagedRowCount());
		System.out.println(tb.getRowCount());
		System.out.println();
		
		batch = tb.getBatch(3);
		System.out.println(batch);
		System.out.println(batch.getTuples());
		System.out.println(batch.getEndRow());
		System.out.println(batch.getTerminationFlag());
		
		tupleSource = tb.createIndexedTupleSource();
		tupleSource.setReverse(true);
		
		while(tupleSource.hasNext()) {
			int index = tupleSource.getCurrentIndex();
			List<?> source = tupleSource.nextTuple();
			System.out.println(index + ": " + source);
		}
		
	}

	static void testTruncate() throws TeiidComponentException, TeiidProcessingException {
		ElementSymbol x = new ElementSymbol("x");
		x.setType(DataTypeManager.DefaultDataClasses.INTEGER);
		List<ElementSymbol> schema = Arrays.asList(x);
		
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
		TupleBuffer tb = bm.createTupleBuffer(schema, "x", TupleSourceType.PROCESSOR);
		tb.setBatchSize(2);
		for (int i = 0; i < 5; i++) {
			tb.addTuple(Arrays.asList(1));
		}
		
		TupleBufferTupleSource tupleSource = tb.createIndexedTupleSource();
		tupleSource.setReverse(true);
		
		while(tupleSource.hasNext()) {
			int index = tupleSource.getCurrentIndex();
			List<?> source = tupleSource.nextTuple();
			System.out.println(index + ": " + source);
		}
		
		TupleBatch batch = tb.getBatch(1);
		System.out.println(batch);
		System.out.println(batch.getTuples());
		System.out.println(batch.getEndRow());
		System.out.println(batch.getTerminationFlag());
		System.out.println();
		
		tb.close();
		
		System.out.println(tb.getManagedRowCount());
		
		tb.truncateTo(3);
		
		System.out.println(tb.getManagedRowCount());
		System.out.println(tb.getRowCount());
		
		batch = tb.getBatch(3);
		System.out.println(batch);
		System.out.println(batch.getTerminationFlag());
		
		tb.truncateTo(2);
		
		System.out.println(tb.getManagedRowCount());
		System.out.println(tb.getRowCount());
		
		batch = tb.getBatch(3);
		System.out.println(batch);
		System.out.println(batch.getTerminationFlag());
		
		tupleSource = tb.createIndexedTupleSource();
		tupleSource.setReverse(true);
		
		while(tupleSource.hasNext()) {
			int index = tupleSource.getCurrentIndex();
			List<?> source = tupleSource.nextTuple();
			System.out.println(index + ": " + source);
		}
	}

	static void testReverseIteration() throws TeiidComponentException, TeiidProcessingException {
		
		ElementSymbol x = new ElementSymbol("x");
		x.setType(DataTypeManager.DefaultDataClasses.INTEGER);
		List<ElementSymbol> schema = Arrays.asList(x);
		
		BufferManager bm = new BufferManagerImpl();
		TupleBuffer tb = bm.createTupleBuffer(schema, "x", TupleSourceType.PROCESSOR);
		tb.addTuple(Arrays.asList(1, 2, 3));
		tb.addTuple(Arrays.asList(2, 3, 4));
		TupleBufferTupleSource tupleSource = tb.createIndexedTupleSource();
		tupleSource.setReverse(true);
		
		while(tupleSource.hasNext()) {
			int index = tupleSource.getCurrentIndex();
			List<?> source = tupleSource.nextTuple();
			System.out.println(index + ": " + source);
		}
		
		tupleSource.closeSource();

		
	}

	static void testInit() throws TeiidComponentException {
		ElementSymbol x = new ElementSymbol("x");
		x.setType(DataTypeManager.DefaultDataClasses.INTEGER);
		List<ElementSymbol> schema = Arrays.asList(x);
			
		TupleBuffer tupleBuffer = new TupleBuffer(null, "1", schema, null,100);
		tupleBuffer.setInlineLobs(false);
		tupleBuffer.setForwardOnly(true);
		tupleBuffer.addTuple(Arrays.asList(1, 2, 3));
		tupleBuffer.addTuple(Arrays.asList(1, 2, 3));
		
		System.out.println(tupleBuffer.getBatch(1).getTuples());
		
	}

	static void testForwardOnly() throws TeiidComponentException {
		ElementSymbol x = new ElementSymbol("x");
		x.setType(DataTypeManager.DefaultDataClasses.INTEGER);
		List<ElementSymbol> schema = Arrays.asList(x);
		
		BufferManagerImpl bm = new BufferManagerImpl();
		
		TupleBuffer tb = bm.createTupleBuffer(schema, "x", TupleSourceType.PROCESSOR);
		tb.setForwardOnly(true);
		tb.addTuple(Arrays.asList(1, 2, 3));
		
		TupleBatch batch = tb.getBatch(1);
		
		System.out.println(batch.getTerminationFlag());
		System.out.println(batch.getBeginRow());
		System.out.println(batch.getTuples());
		
		tb.addTuple(Arrays.asList(1, 2, 3, 4, 5, 6));
		
		batch = tb.getBatch(2);
		
		System.out.println(batch.getTerminationFlag());
		System.out.println(batch.getBeginRow());
		System.out.println(batch.getTuples());
		
		tb.addTuple(Arrays.asList(1, 2, 3));
		
		batch = tb.getBatch(3);
		
		System.out.println(batch.getTerminationFlag());
		System.out.println(batch.getBeginRow());
		System.out.println(batch.getTuples());
	} 

}
