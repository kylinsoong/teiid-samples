package org.teiid.test.admin;

import java.util.List;

import org.teiid.adminapi.Admin;
import org.teiid.adminapi.Admin.TranlatorPropertyType;
import org.teiid.adminapi.AdminException;
import org.teiid.adminapi.AdminFactory;
import org.teiid.adminapi.Model;
import org.teiid.adminapi.PropertyDefinition;
import org.teiid.adminapi.Request;
import org.teiid.adminapi.Session;
import org.teiid.adminapi.Translator;
import org.teiid.adminapi.VDB;
import org.teiid.adminapi.WorkerPoolStatistics;
import org.teiid.adminapi.VDB.ConnectionType;
import org.teiid.adminapi.impl.VDBTranslatorMetaData;

public class AdminTest {
	
	private static final String HOST = "127.0.0.1";
    private static final int PORT = 9999;
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";
    
    static Admin admin;

	public static void main(String[] args) throws AdminException {

		try {
			admin = AdminFactory.getInstance().createAdmin(HOST, PORT, JDBC_USER, JDBC_PASS.toCharArray());
			
//			testGetVDBs();
			
//			testGetVDB();
			
//			testAddRemoveSource();
			
//			testChangeVDBConnectionType();
			
//			testDeployUndeploy();
			
//			testGetTranslators();
			
//			testGetWorkerPoolStats();
			
//			testGetCacheTypes();
			
//			testGetSessions();
			
//			testGetRequests();
			
//			testGetRequest();
			
//			testGetTemplatePropertyDefinitions();
			
			testGetTranslatorPropertyDefinitions();
		} finally {
			admin.close();
		}
	}

	@SuppressWarnings("unchecked")
	static void testGetTranslatorPropertyDefinitions() throws AdminException {

		List<PropertyDefinition> list = (List<PropertyDefinition>) admin.getTranslatorPropertyDefinitions("file", TranlatorPropertyType.OVERRIDE);
		
		for(PropertyDefinition pd : list){
			System.out.println(pd.getName() + ", " + pd.getDefaultValue() + ", " + pd.getDescription());
		}
	}

	static void testGetTemplatePropertyDefinitions() throws AdminException {
		Object obj = admin.getTemplatePropertyDefinitions("filead");
		System.out.println(obj);
	}

	@SuppressWarnings("unchecked")
	static void testGetRequest() throws AdminException {
		List<Session> sessions = (List<Session>) admin.getSessions();
		String id = sessions.get(0).getSessionId();
		List<Request> requests = (List<Request>) admin.getRequestsForSession(id);
		Request request = requests.get(0);
		System.out.println(request);
	}

	static void testGetRequests() throws AdminException {
		for(Request request : admin.getRequests()) {
			System.out.println(request);
		}
	}

	static void testGetSessions() throws AdminException {

		for(Session session : admin.getSessions()) {
			System.out.println(session);
		}
	}

	static void testGetCacheTypes() throws AdminException {
		for(String type : admin.getCacheTypes()){
			System.out.println(type);
		}
	}

	static void testGetWorkerPoolStats() throws AdminException {
		for(WorkerPoolStatistics pool : admin.getWorkerPoolStats()){
			System.out.println(pool);
			System.out.println(pool.getActiveThreads());
			System.out.println(pool.getHighestActiveThreads());
			System.out.println(pool.getHighestQueued());
			System.out.println(pool.getMaxThreads());
			System.out.println(pool.getName());
			System.out.println(pool.getQueueName());
			System.out.println(pool.getQueued());
			System.out.println(pool.getTotalCompleted());
			System.out.println(pool.getTotalSubmitted());
			System.out.println(pool.getServerName());
		}
	}

	static void testGetTranslators() throws AdminException {
		
//		VDBTranslatorMetaData metadata = TranslatorUtil.buildTranslatorMetadata(ef, moduleName);

//		for(Translator translator : admin.getTranslators()){
//			System.out.println(translator.getName() + ", " + translator.getType() + ", " + translator.getDescription());
//		}
	}

	static void testDeployUndeploy() throws AdminException {
//		admin.undeploy("adminapi-test-vdb.xml");
//		admin.deploy("adminapi-test-vdb.xml", "");
	}

	static void testChangeVDBConnectionType() throws AdminException {
		VDB vdb = admin.getVDB("AdminAPITestVDB", 1);	
		ConnectionType previous = vdb.getConnectionType();
		admin.changeVDBConnectionType("AdminAPITestVDB", 1, ConnectionType.ANY);
		System.out.println(admin.getVDB("AdminAPITestVDB", 1).getConnectionType());
		admin.changeVDBConnectionType("AdminAPITestVDB", 1, previous);
	}

	static void testGetVDB() throws AdminException {
		VDB vdb = admin.getVDB("AdminAPITestVDB", 1);	
		System.out.println(vdb);	
	}

	static void testGetVDBs() throws AdminException {
		for(VDB vdb : admin.getVDBs()){
			System.out.println(vdb);
		}
	}

	static void testAddRemoveSource() throws AdminException {
		admin.addSource("AdminAPITestVDB", 1, "TestModel", "text-connector-test", "file", "java:/test-file");	
		
		String expect = null;
		for(VDB vdb : admin.getVDBs()){
			for(Model model : vdb.getModels()){
				if(model.getName().equals("TestModel")) {
					for(String name : model.getSourceNames()) {
						expect = name;
					}
				}
			}
		}
		
		System.out.println(expect);
		
		admin.removeSource("AdminAPITestVDB", 1, "TestModel", "text-connector-test");
	}

}
