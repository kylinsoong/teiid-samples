package com.teiid.quickstart.teiid3070;

import java.util.ArrayList;

import org.teiid.runtime.EmbeddedServer;
import org.teiid.transport.SocketListener;


public class MyEmbeddedServer extends EmbeddedServer {

	public ArrayList<SocketListener> getListeners() {
		return transports;
	}
}
