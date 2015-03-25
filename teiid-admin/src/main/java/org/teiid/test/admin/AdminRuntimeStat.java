package org.teiid.test.admin;

import org.jboss.dmr.ModelNode;
import org.teiid.adminapi.Admin;
import org.teiid.adminapi.AdminException;
import org.teiid.adminapi.AdminFactory;
import org.teiid.adminapi.Request;
import org.teiid.adminapi.Session;
import org.teiid.adminapi.impl.RequestMetadata;
import org.teiid.adminapi.impl.SessionMetadata;
import org.teiid.adminapi.impl.VDBMetadataMapper;

public class AdminRuntimeStat {
	
	private static final String HOST = "127.0.0.1";
    private static final int PORT = 9999;
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";

	public static void main(String[] args) throws AdminException {
		
		Admin admin = AdminFactory.getInstance().createAdmin(HOST, PORT, JDBC_USER, JDBC_PASS.toCharArray());
		
		for(Session session : admin.getSessions()) {
			ModelNode node = VDBMetadataMapper.SessionMetadataMapper.INSTANCE.wrap((SessionMetadata)session, new ModelNode());
//			System.out.println(node);
			System.out.println(node.toJSONString(false));
		}
		
		System.out.println("\n");
		
		for(Request request : admin.getRequests()) {
			ModelNode node = VDBMetadataMapper.RequestMetadataMapper.INSTANCE.wrap((RequestMetadata)request, new ModelNode());
//			System.out.println(node);
			System.out.println(node.toJSONString(false));
		}
		
		admin.close();

	}

}
