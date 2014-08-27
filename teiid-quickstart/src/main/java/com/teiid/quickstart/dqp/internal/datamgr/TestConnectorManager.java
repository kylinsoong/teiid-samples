package com.teiid.quickstart.dqp.internal.datamgr;

import org.teiid.dqp.internal.datamgr.ConnectorManager;
import org.teiid.dqp.message.AtomicRequestMessage;
import org.teiid.translator.ExecutionFactory;

public class TestConnectorManager {
	
	private AtomicRequestMessage request;
    private ConnectorManager csm;
    
    static ConnectorManager getConnectorManager() throws Exception {
		final FakeConnector c = new FakeConnector();
		ConnectorManager cm = new ConnectorManager("FakeConnector","FakeConnector") { //$NON-NLS-1$ //$NON-NLS-2$
			public ExecutionFactory<Object, Object> getExecutionFactory() {
				return c;
			}
			public Object getConnectionFactory(){
				return c;
			}
		};
		cm.start();
		return cm;
	}
    
    public TestConnectorManager() throws Exception {
    	request = TestConnectorWorkItem.createNewAtomicRequestMessage(1, 1);
    	csm = getConnectorManager();
    	csm.registerRequest(request);
    }
    
    public void testCreateAndAddRequestState() {
    	
    }

	public static void main(String[] args) {
		
	}

}
