package org.teiid.test.bufferservice;

import org.teiid.common.buffer.impl.ConcurrentBitSet;

public class ConcurrentBitSetTest {

	public static void main(String[] args) {
		
		testBitsSet();
		
		testSegmentUse();
		
		testCompactBitSet();
		
		testCompactHighest();
		
		testCompactHighestEmpty();
		
	}

	static void testCompactHighestEmpty() {

		ConcurrentBitSet bst = new ConcurrentBitSet(1 << 19, 1);
		bst.setCompact(true);
		bst.getAndSetNextClearBit();
		bst.clear(0);
		
		System.out.println(bst.getAndSetNextClearBit(0));
	}

	static void testCompactHighest() {

		ConcurrentBitSet bst = new ConcurrentBitSet(1 << 19, 1);
		bst.setCompact(true);
		for (int i = 0; i < bst.getTotalBits(); i++) {
			System.out.println(bst.getAndSetNextClearBit());
		}
		
		System.out.println();
		System.out.println(bst.getTotalBits()-1 + " - " + bst.getHighestBitSet(0));
		System.out.println(bst.getTotalBits()-1 + " - " + bst.getHighestBitSet(1));
		
		for (int i = bst.getTotalBits()-20; i < bst.getTotalBits(); i++) {
			bst.clear(i);
		}
		
		System.out.println(bst.getTotalBits()-21 + " - " + bst.compactHighestBitSet(0));
		
		for (int i = bst.getTotalBits()-20; i < bst.getTotalBits(); i++) {
			bst.getAndSetNextClearBit();
		}
		
		System.out.println(bst.getAndSetNextClearBit());
		
		for (int i = 20; i < bst.getTotalBits(); i++) {
			bst.clear(i);
		}
		
		System.out.println(bst.getTotalBits()-1 + " - " + bst.getHighestBitSet(0));
		
		System.out.println( bst.compactHighestBitSet(0));
	}

	static void testCompactBitSet() {

		ConcurrentBitSet bst = new ConcurrentBitSet(100000, 1);
		bst.setCompact(true);
		
		for (int i = 0; i < 100000; i++) {
			System.out.println(bst.getAndSetNextClearBit());
		}
		
		bst.clear(50);
		bst.clear(500);
		bst.clear(5000);
		
		System.out.println(bst.getAndSetNextClearBit());
	}

	static void testSegmentUse() {
		
		ConcurrentBitSet bst = new ConcurrentBitSet(50001, 4);
		System.out.println(bst.getAndSetNextClearBit(0));
		System.out.println(bst.getAndSetNextClearBit(0));
		System.out.println(bst.getAndSetNextClearBit(4));
	}

	static void testBitsSet() {
		
		ConcurrentBitSet bst = new ConcurrentBitSet(50001, 4);
		System.out.println(bst);
		System.out.println(bst.getAndSetNextClearBit());
		System.out.println(bst.getAndSetNextClearBit());
		System.out.println(bst.getAndSetNextClearBit());
		System.out.println(bst.getAndSetNextClearBit());
		System.out.println(bst.getAndSetNextClearBit());
		System.out.println(bst.getBitsSet());
		
		bst.clear(12501);
		
		System.out.println(bst.getBitsSet());
	}

}
