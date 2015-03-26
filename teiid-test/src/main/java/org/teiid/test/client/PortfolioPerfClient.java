package org.teiid.test.client;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PortfolioPerfClient {
	
	static AtomicInteger ID = new AtomicInteger(1);
	
	static AtomicLong connCount = new AtomicLong(0);
	static AtomicLong reqCount = new AtomicLong(0);

	public static void main(String[] args) {
		
		int num = Runtime.getRuntime().availableProcessors() * 2;
		
		while(num > 0) {

			new Thread(new Runnable(){
				
				@Override
				public void run() {
					
					Thread.currentThread().setName("client-thread-" + ID.getAndIncrement());

					for(int i = 0 ; i < 10000 ; i ++) {
						try {
							PortfolioClient.test();
							connCount.addAndGet(1);
							reqCount.addAndGet(9);
						} catch (Exception e) {
							Thread.currentThread().interrupt();
						}
					}
					
					System.out.println(Thread.currentThread().getName() + " exit. Connection Count: " + connCount.get() + ", Request Count: " + reqCount.get());
					
				}}).start();
			
			num -- ;
		}
		
	}

}
