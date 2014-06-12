package org.teiid.jboss.test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.teiid.core.BundleUtil;
import org.teiid.core.TeiidRuntimeException;

public class StartupExceptionSimulation {
		
	private String hostName;
	
	public String getHostName() {
	    resolveHostName();
	    return this.hostName;
	  }

	private void resolveHostName() {
		try {
			if (this.hostName == null)
				this.hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			throw new TeiidRuntimeException(Event.TEIID40065, "Failed to resolve the bind address");
		}
	}

	public static void main(String[] args) throws UnknownHostException {
		
		System.out.println(new StartupExceptionSimulation().getHostName());
		
		
	}
	
	public static enum Event implements BundleUtil.Event {
		TEIID40065
	}

}
