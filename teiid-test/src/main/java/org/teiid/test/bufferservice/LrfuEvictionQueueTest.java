package org.teiid.test.bufferservice;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.teiid.common.buffer.CacheEntry;
import org.teiid.common.buffer.CacheKey;
import org.teiid.common.buffer.impl.LrfuEvictionQueue;

public class LrfuEvictionQueueTest {

	public static void main(String[] args) {
		
		testPrecision();
		
		testKeyCompare();
		
		testUsage();
	}

	static void testUsage() {

		LrfuEvictionQueue<CacheEntry> queue = new LrfuEvictionQueue<CacheEntry>(new AtomicLong());
		queue.add(new CacheEntry(1000L));
		queue.touch(new CacheEntry(1000L));
		queue.remove(new CacheEntry(1000L));
		
		System.out.println(queue.getSize());
		System.out.println(queue.getEvictionQueue());
		
	}

	static void testKeyCompare() {
		CacheKey key0 = new CacheKey(-5600000000000000000l, 0l, 0l);
		CacheKey key1 = new CacheKey(3831662765844904176l, 0l, 0l);
		CacheKey key2 = new CacheKey(0l, 0l, 0l);
		
		System.out.println(key2.compareTo(key0));
		System.out.println(key2.compareTo(key1));
		
		List<CacheKey> list = Arrays.asList(key2, key1, key0);
		System.out.println(list);
		Collections.sort(list);
		System.out.println(list);
	}

	static void testPrecision() {
		
		LrfuEvictionQueue<CacheEntry> queue = new LrfuEvictionQueue<CacheEntry>(new AtomicLong());
		
		for (long i = Integer.MAX_VALUE; i < 10l + Integer.MAX_VALUE; i++) {
			queue.add(new CacheEntry(i));
		}
		
		System.out.println(queue.getSize());
		System.out.println(queue.getEvictionQueue());
		
		long size = Integer.MAX_VALUE + 1;
		queue.touch(new CacheEntry(size));
		
		System.out.println(queue.getSize());
		System.out.println(queue.getEvictionQueue());
	}
}
