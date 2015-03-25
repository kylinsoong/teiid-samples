package org.teiid.test.admin;

import org.teiid.adminapi.Admin;
import org.teiid.adminapi.AdminException;
import org.teiid.adminapi.AdminFactory;
import org.teiid.adminapi.AdminObject;
import org.teiid.adminapi.DomainAware;
import org.teiid.adminapi.Request;
import org.teiid.adminapi.Session;
import org.teiid.adminapi.Transaction;
import org.teiid.adminapi.Translator;
import org.teiid.adminapi.VDB;
import org.teiid.adminapi.WorkerPoolStatistics;

public class AdminAPITest {
	
	private static final String HOST = "127.0.0.1";
    private static final int PORT = 9999;
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";

	public static void main(String[] args) throws Exception, AdminException {
		
		Admin admin = AdminFactory.getInstance().createAdmin(HOST, PORT, JDBC_USER, JDBC_PASS.toCharArray());
		
		for(VDB vdb : admin.getVDBs()){
			System.out.println(vdb);
			vdbView(vdb);
			System.out.println("\n");
		}
		
		for(Translator translator : admin.getTranslators()) {
			System.out.println(translator);
			translatorView(translator);
			System.out.println("\n");
		}
		
		for(WorkerPoolStatistics pool : admin.getWorkerPoolStats()){
			System.out.println(pool);
			poolView(pool);
			System.out.println("\n");
		}
		
		for(String type : admin.getCacheTypes()){
			System.out.println(type);
			System.out.println("\n");
		}
		
		for(Session session : admin.getSessions()) {
			System.out.println(session);
		}
		
		for(Request request : admin.getRequests()) {
			System.out.println(request);
		}
		
		for(Transaction transaction : admin.getTransactions()) {
			System.out.println(transaction);
		}
		
		admin.close();

	}

	private static void poolView(WorkerPoolStatistics pool) {

		domainAwareView(pool);
		
		adminObjectView(pool);
		
		poolSelfView(pool);
	}

	private static void poolSelfView(WorkerPoolStatistics pool) {
		System.out.println(pool.getActiveThreads());
		System.out.println(pool.getHighestActiveThreads());
		System.out.println(pool.getQueueName());
		System.out.println(pool.getMaxThreads());
		System.out.println(pool.getQueued());
		System.out.println(pool.getTotalCompleted());
		System.out.println(pool.getTotalSubmitted());
		System.out.println(pool.getHighestQueued());
	}

	private static void translatorView(Translator translator) {

		domainAwareView(translator);
		
		adminObjectView(translator);
		
		translatorSelfView(translator);
	}

	private static void translatorSelfView(Translator translator) {
		System.out.println(translator.getType());
		System.out.println(translator.getDescription());
	}

	private static void vdbView(VDB vdb) {
		
		domainAwareView(vdb);
		
		adminObjectView(vdb);
		
		vdbSelfView(vdb);
	}

	/*
	 * org.teiid.adminapi.VDB
	 */
	private static void vdbSelfView(VDB vdb) {
		System.out.println(vdb.getModels());
		System.out.println(vdb.getStatus());
		System.out.println(vdb.getConnectionType());
		System.out.println(vdb.getVersion());
		System.out.println(vdb.getDescription());
		System.out.println(vdb.getValidityErrors());
		System.out.println(vdb.isValid());
		System.out.println(vdb.getDataPolicies());
		System.out.println(vdb.getOverrideTranslators());
		System.out.println(vdb.getVDBImports());
		System.out.println(vdb.getEntries());
	}

	/*
	 * org.teiid.adminapi.AdminObject
	 */
	private static void adminObjectView(AdminObject vdb) {
		System.out.println(vdb.getName());
		System.out.println(vdb.getProperties());
	}

	/*
	 * org.teiid.adminapi.DomainAware
	 */
	private static void domainAwareView(DomainAware vdb) {
		System.out.println(vdb.getServerGroup());
		System.out.println( vdb.getServerName());
		System.out.println(vdb.getHostName());
	}

}
