package org.teiid.runtime.embedded;

import java.net.URL;

/**
 * A Simple Fungal Container used to bootstrap jndi naming server, TransactionManager, outbound resource adapter.
 * 
 * @author kylin
 *
 */
public interface EmbeddedBootstrap {
	
	public void startup() throws Throwable;
	
	public void shutdown() throws Throwable;
	
	public void deploy(URL url) throws Throwable;
	
	
	static class Factory {
		static EmbeddedBootstrap create() {
			return new EmbeddedBootstrapImpl();
		}
	}

}
