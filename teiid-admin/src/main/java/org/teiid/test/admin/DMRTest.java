package org.teiid.test.admin;

import org.jboss.as.cli.operation.OperationFormatException;
import org.jboss.as.cli.operation.impl.DefaultOperationRequestBuilder;
import org.jboss.dmr.ModelNode;
import org.teiid.adminapi.AdminComponentException;
import org.teiid.adminapi.AdminException;
import org.teiid.adminapi.AdminPlugin;

public class DMRTest {
	
	static boolean domainMode = false;
	static String profileName = "ha";

	public static void main(String[] args) throws AdminException {

		final ModelNode request = buildRequest("teiid", "terminate-session", "session", "yJAH02692S2I", "execution-id", String.valueOf(0));
		
		System.out.println(request);
	}
	
	static ModelNode buildRequest(String subsystem, String operationName, String... params) throws AdminException {
		DefaultOperationRequestBuilder builder = new DefaultOperationRequestBuilder();
        final ModelNode request;
        try {
        	if (subsystem != null) {
	        	addProfileNode(builder);
	            builder.addNode("subsystem", subsystem); //$NON-NLS-1$ 
        	}
            builder.setOperationName(operationName);
            request = builder.buildRequest();
            if (params != null && params.length % 2 == 0) {
            	for (int i = 0; i < params.length; i+=2) {
            		builder.addProperty(params[i], params[i+1]);
            	}
            }
        } catch (OperationFormatException e) {
        	throw new AdminComponentException(AdminPlugin.Event.TEIID70010, e, "Failed to build operation"); //$NON-NLS-1$
        }
		return request;
	}
	
	static void addProfileNode(DefaultOperationRequestBuilder builder) throws AdminException {
		if (domainMode) {
			String profile = getProfileName();
			if (profile != null) {
				builder.addNode("profile",profile);
			}
		}
	}
	
	static String getProfileName() throws AdminException {
		if (!domainMode) {
			return null;
		}
		return profileName;
	}

}
