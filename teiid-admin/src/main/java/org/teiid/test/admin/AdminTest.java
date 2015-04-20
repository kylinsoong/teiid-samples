package org.teiid.test.admin;

import java.util.EnumSet;
import java.util.List;
import java.util.Properties;

import org.teiid.adminapi.Admin;
import org.teiid.adminapi.Admin.SchemaObjectType;
import org.teiid.adminapi.Admin.TranlatorPropertyType;
import org.teiid.adminapi.AdminException;
import org.teiid.adminapi.AdminFactory;
import org.teiid.adminapi.CacheStatistics;
import org.teiid.adminapi.DataPolicy;
import org.teiid.adminapi.EngineStatistics;
import org.teiid.adminapi.Model;
import org.teiid.adminapi.PropertyDefinition;
import org.teiid.adminapi.Request;
import org.teiid.adminapi.Session;
import org.teiid.adminapi.Transaction;
import org.teiid.adminapi.VDB;
import org.teiid.adminapi.WorkerPoolStatistics;
import org.teiid.adminapi.VDB.ConnectionType;
import org.teiid.adminapi.impl.DataPolicyMetadata;
import org.teiid.adminapi.impl.VDBMetaData;

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
			
//			testGetTranslatorPropertyDefinitions();
			
//			testGetTransactions();
			
//			testClearCache();
			
//			testGetCacheStats();
			
//			testGetEngineStats();
			
//			testTerminateSession();
			
			testCancelRequest();
			
//			testDataRoleMapping();
			
//			testDataSources();
			
//			testGetSchema();
			
//			testRestart();
			
//			admin.markDataSourceAvailable("test");
			
//			System.out.println(admin.getDataSourceTemplateNames());
			
		} finally {
			admin.close();
		}
	}
	
	static void testRestart() {
		admin.restart();
	}

	static void testGetSchema() throws AdminException {
		EnumSet<SchemaObjectType> allowedTypes = EnumSet.of(Admin.SchemaObjectType.TABLES);
		String schema = admin.getSchema("AdminAPITestVDB", 1, "TestModel",  allowedTypes, "helloworld");
		System.out.println(schema);
	}

	static void testDataSources() throws AdminException {

//		admin.createDataSource("deployment", "templete", new Properties());
		
		
	}

	static void testDataRoleMapping() throws AdminException {
		admin.addDataRoleMapping("AdminAPITestVDB", 1, "TestDataRole", "test-role-name");
		
		VDB vdb = admin.getVDB("AdminAPITestVDB", 1);
		for(DataPolicy policy : vdb.getDataPolicies()) {
			System.out.println(policy.getMappedRoleNames());
			System.out.println(policy.getName());
			System.out.println(policy.getDescription());
			System.out.println(policy.getPermissions());
		}
		
		admin.removeDataRoleMapping("AdminAPITestVDB", 1, "TestDataRole", "test-role-name");
		
		System.out.println(getPolicy(admin.getVDB("AdminAPITestVDB", 1), "TestDataRole").isAnyAuthenticated());
		admin.setAnyAuthenticatedForDataRole("AdminAPITestVDB", 1, "TestDataRole", false);
		System.out.println(getPolicy(admin.getVDB("AdminAPITestVDB", 1), "TestDataRole").isAnyAuthenticated());
		admin.setAnyAuthenticatedForDataRole("AdminAPITestVDB", 1, "TestDataRole", true);
	}
	
	static DataPolicyMetadata getPolicy(VDB vdb, String policyName) {
		VDBMetaData vdbMetaData = (VDBMetaData) vdb;
		return vdbMetaData.getDataPolicyMap().get(policyName);
	}	

	static void testCancelRequest() throws AdminException {
		
		List<Session> sessions = (List<Session>) admin.getSessions();
		String id = sessions.get(0).getSessionId();
		List<Request> requests = (List<Request>) admin.getRequestsForSession(id);
		long executionId = requests.get(0).getExecutionId();
		
		
		System.out.println(id + " " + executionId);
		
		admin.cancelRequest(id,executionId);
	}

	static void testTerminateSession() throws AdminException {
		admin.terminateSession("aaa");
	}

	static void testGetEngineStats() throws AdminException {
		for(EngineStatistics stat : admin.getEngineStats()) {
			System.out.println(stat);
		}
	}

	@SuppressWarnings("unchecked")
	static void testGetCacheStats() throws AdminException {
		List<CacheStatistics> list = (List<CacheStatistics>) admin.getCacheStats("PREPARED_PLAN_CACHE");
		System.out.println(list.get(0).getName());
		System.out.println(list.get(0).getHitRatio());
		System.out.println(list.get(0).getTotalEntries());
		System.out.println(list.get(0).getRequestCount());
		list = (List<CacheStatistics>) admin.getCacheStats("QUERY_SERVICE_RESULT_SET_CACHE");
		System.out.println(list.get(0).getName());
		System.out.println(list.get(0).getHitRatio());
		System.out.println(list.get(0).getTotalEntries());
		System.out.println(list.get(0).getRequestCount());
	}

	static void testClearCache() throws AdminException{
		admin.clearCache("PREPARED_PLAN_CACHE");
		admin.clearCache("QUERY_SERVICE_RESULT_SET_CACHE");
		admin.clearCache("PREPARED_PLAN_CACHE", "AdminAPITestVDB", 1);
		admin.clearCache("QUERY_SERVICE_RESULT_SET_CACHE", "AdminAPITestVDB", 1);
	}

	static void testGetTransactions() throws AdminException {

		for(Transaction transaction : admin.getTransactions()) {
			System.out.println(transaction);
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
