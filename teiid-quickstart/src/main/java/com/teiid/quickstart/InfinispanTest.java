package com.teiid.quickstart;

import java.io.IOException;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;

public class InfinispanTest {

	public static void main(String[] args) throws IOException {
		
		new DefaultCacheManager();
		new DefaultCacheManager();
	}

}
