package org.teiid.test.bufferservice;

import org.teiid.common.buffer.impl.BlockByteBuffer;

public class BlockByteBufferTest {

	public static void main(String[] args) {

		BlockByteBuffer bbb = new BlockByteBuffer(4, 100, 4, false);
		bbb.getByteBuffer(1);
	}

}
