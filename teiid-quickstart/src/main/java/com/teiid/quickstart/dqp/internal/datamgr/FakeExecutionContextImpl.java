package com.teiid.quickstart.dqp.internal.datamgr;

import java.util.concurrent.atomic.AtomicInteger;

import org.teiid.dqp.internal.datamgr.ExecutionContextImpl;
import org.teiid.translator.ExecutionContext;

public class FakeExecutionContextImpl extends ExecutionContextImpl {
	
	private final static AtomicInteger COUNT = new AtomicInteger(0);
	
	public FakeExecutionContextImpl() {
		this(COUNT.getAndIncrement());
	}
	
	public FakeExecutionContextImpl(int unique) {
		super("VDB" + unique, 
				unique, 
				"ExecutionPayload" + unique,         
				"ConnectionID" + unique,
				"ConnectorID" + unique, 
				unique,
				"PartID" + unique, 
				"ExecCount" + unique); 
	}

	public FakeExecutionContextImpl(ExecutionContext c) {
		super(c.getVdbName(), c.getVdbVersion(), c.getCommandPayload(), c
				.getConnectionId(), c.getConnectorIdentifier(), Long.valueOf(c
				.getRequestId()), c.getPartIdentifier(), c
				.getExecutionCountIdentifier());
	}

	

}
