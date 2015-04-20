package org.teiid.test.client;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PortfolioPerfClient {
	
	static AtomicInteger ID = new AtomicInteger(1);
	
	static AtomicLong connCount = new AtomicLong(0);
	static AtomicLong reqCount = new AtomicLong(0);

	public static void main(String[] args) {
		// 16790 16653 16652
		
		// 20100
		
		int num = Runtime.getRuntime().availableProcessors() * 2;
		final long start = System.currentTimeMillis();
		
		while(num > 0) {

			new Thread(new Runnable(){
				
				@Override
				public void run() {
					
					Thread.currentThread().setName("client-thread-" + ID.getAndIncrement());

					for(int i = 0 ; i < 500 ; i ++) {
						try {
							PortfolioClient.test();
							connCount.addAndGet(1);
							reqCount.addAndGet(4);
						} catch (Exception e) {
							Thread.currentThread().interrupt();
						}
					}
					
					System.out.println(Thread.currentThread().getName() + " exit. Connection Count: " + connCount.get() + ", Request Count: " + reqCount.get() + ", commands insert: " + reqCount.get() * 20 + ", Time consume: " + (System.currentTimeMillis() - start) / 1000);
					
				}}).start();
			
			num -- ;
		}
		
	}

}
