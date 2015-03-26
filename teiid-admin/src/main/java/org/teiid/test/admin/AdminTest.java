package org.teiid.test.admin;

import org.teiid.adminapi.Admin;
import org.teiid.adminapi.AdminException;
import org.teiid.adminapi.AdminFactory;
import org.teiid.adminapi.Model;
import org.teiid.adminapi.VDB;

public class AdminTest {
	
	private static final String HOST = "127.0.0.1";
    private static final int PORT = 9999;
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";
    
    static Admin admin;

	public static void main(String[] args) throws AdminException {

		admin = AdminFactory.getInstance().createAdmin(HOST, PORT, JDBC_USER, JDBC_PASS.toCharArray());
		
		addSource();
		
		removeSource();
		
		
		admin.close();
	}

	static void removeSource() throws AdminException {
		admin.removeSource("Portfolio", 1, "MarketData", "text-connector-test");
	}

	static void addSource() throws AdminException {
		admin.addSource("Portfolio", 1, "MarketData", "text-connector-test", "file", "java:/marketdata-file");
		for(VDB vdb : admin.getVDBs()){
			for(Model model : vdb.getModels()){
				for(String name : model.getSourceNames()) {
					System.out.println(name);
				}
			}
		}
	}

}
