package org.teiid.test.bufferservice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.teiid.common.buffer.FileStore;
import org.teiid.common.buffer.FileStore.FileStoreOutputStream;
import org.teiid.common.buffer.FileStoreInputStreamFactory;
import org.teiid.common.buffer.LobManager;
import org.teiid.common.buffer.LobManager.ReferenceMode;
import org.teiid.common.buffer.impl.BufferFrontedFileStoreCache;
import org.teiid.common.buffer.impl.BufferManagerImpl;
import org.teiid.common.buffer.impl.MemoryStorageManager;
import org.teiid.common.buffer.impl.SplittableStorageManager;
import org.teiid.core.TeiidComponentException;
import org.teiid.core.types.BlobImpl;
import org.teiid.core.types.BlobType;
import org.teiid.core.types.ClobImpl;
import org.teiid.core.types.ClobType;
import org.teiid.core.types.DataTypeManager;
import org.teiid.core.types.InputStreamFactory;
import org.teiid.core.types.Streamable;
import org.teiid.core.util.ReaderInputStream;

public class LobManagerTest {

	public static void main(String[] args) throws TeiidComponentException, IOException {
		
		testLobPeristence();
	}

	static void testLobPeristence() throws TeiidComponentException, IOException {
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
		
		FileStore fs = bm.createFileStore("temp");
		
		ClobType clob = new ClobType(new ClobImpl(new InputStreamFactory() {
			@Override
			public InputStream getInputStream() throws IOException {
				return new ReaderInputStream(new StringReader("Clob contents One"),  Charset.forName(Streamable.ENCODING)); 
			}
			
		}, -1));
		
		BlobType blob = new BlobType(new BlobImpl(new InputStreamFactory() {
			@Override
			public InputStream getInputStream() throws IOException {
				return new ReaderInputStream(new StringReader("Blob contents Two"),  Charset.forName(Streamable.ENCODING));
			}
			
		}));
		
		BlobType blobEmpty = new BlobType(new BlobImpl(new InputStreamFactory() {
			@Override
			public InputStream getInputStream() throws IOException {
				return new ByteArrayInputStream(new byte[0]);
			}
			
		}));
		
		FileStore fs1 = bm.createFileStore("blob");
		FileStoreInputStreamFactory fsisf = new FileStoreInputStreamFactory(fs1, Streamable.ENCODING);
		FileStoreOutputStream fsos = fsisf.getOuputStream();
		byte[] b = new byte[DataTypeManager.MAX_LOB_MEMORY_BYTES + 1];
		fsos.write(b);
		fsos.close();
		BlobType blob1 = new BlobType(new BlobImpl(fsisf));	
		
		LobManager lobManager = new LobManager(new int[] {0, 1, 2, 3}, fs);
		lobManager.setMaxMemoryBytes(4);
		List<?> tuple = Arrays.asList(clob, blob, blob1, blobEmpty);
		lobManager.updateReferences(tuple, ReferenceMode.CREATE);
		
		System.out.println(blob1.getReferenceStreamId());
	}

}
