package org.teiid.test.bufferservice;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import org.teiid.common.buffer.CacheEntry;
import org.teiid.common.buffer.FileStore;
import org.teiid.common.buffer.Serializer;
import org.teiid.common.buffer.StorageManager;
import org.teiid.common.buffer.impl.BufferFrontedFileStoreCache;
import org.teiid.common.buffer.impl.MemoryStorageManager;
import org.teiid.common.buffer.impl.OutOfDiskException;
import org.teiid.common.buffer.impl.SplittableStorageManager;
import org.teiid.core.TeiidComponentException;

public class BufferFrontedFileStoreCacheTest {
	
	private static BufferFrontedFileStoreCache cache;

	public static void main(String[] args) throws TeiidComponentException {
		
		testAddGetMultiBlock();
	}

	static void testAddGetMultiBlock() throws TeiidComponentException {

		cache = createLayeredCache(1 << 26, 1 << 26, true, true);
		
		CacheEntry entry = new CacheEntry(2l);
		Serializer<Integer> s = new SimpleSerializer();
		cache.createCacheGroup(s.getId());
		Integer cacheObject = Integer.valueOf(2);
		entry.setObject(cacheObject);
		cache.addToCacheGroup(s.getId(), entry.getId());
		cache.add(entry, s);
		
//		entry = 
//		
//		System.out.println(cache);
	}
	
	private static BufferFrontedFileStoreCache createLayeredCache(int bufferSpace, int objectSize, boolean memStorage, boolean allocate) throws TeiidComponentException {
		BufferFrontedFileStoreCache fsc = new BufferFrontedFileStoreCache();
		fsc.setMemoryBufferSpace(bufferSpace);
		fsc.setMaxStorageObjectSize(objectSize);
		fsc.setDirect(false);
		if (memStorage) {
			SplittableStorageManager ssm = new SplittableStorageManager(new MemoryStorageManager());
			ssm.setMaxFileSizeDirect(MemoryStorageManager.MAX_FILE_SIZE);
			fsc.setStorageManager(ssm);
		} else {
			StorageManager sm = new StorageManager() {
				
				@Override
				public void initialize() throws TeiidComponentException {
					
				}
				
				@Override
				public FileStore createFileStore(String name) {
					return new FileStore() {
						
						@Override
						public void setLength(long length) throws IOException {
							throw new OutOfDiskException(null);
						}
						
						@Override
						protected void removeDirect() {
							
						}
						
						@Override
						protected int readWrite(long fileOffset, byte[] b, int offSet, int length,
								boolean write) throws IOException {
							return 0;
						}
						
						@Override
						public long getLength() {
							return 0;
						}
					};
				}
			};
			fsc.setStorageManager(sm);
		}
		fsc.initialize();
		return fsc;
	}

	
	private final static class SimpleSerializer implements Serializer<Integer> {
		@Override
		public Integer deserialize(ObjectInput ois)
				throws IOException, ClassNotFoundException {
			Integer result = ois.readInt();
			for (int i = 0; i < result; i++) {
				System.out.println(ois.readInt());
			}
			return result;
		}

		@Override
		public Long getId() {
			return 1l;
		}

		@Override
		public void serialize(Integer obj, ObjectOutput oos)
				throws IOException {
			oos.writeInt(obj);
			for (int i = 0; i < obj; i++) {
				oos.writeInt(i);
			}
		}

		@Override
		public boolean useSoftCache() {
			return false;
		}
	}

}
