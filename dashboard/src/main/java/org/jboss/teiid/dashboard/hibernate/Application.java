package org.jboss.teiid.dashboard.hibernate;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import bitronix.tm.resource.jdbc.PoolingDataSource;

public class Application {

	public static Set<File> getJarFiles() {
		
		File[] files = new File(HibernateInitializerTest.DIR_LIB).listFiles();
		
		Set<File> set = new HashSet<File>();
		for(File file : files) {
			set.add(file);
		}
		
		return set;
	}
	
	public static Object invokeMethod(Object o, String methodName, Object[] params) {
        Method methods[] = o.getClass().getMethods();
        for (int i = 0; i < methods.length; ++i) {
            if (methodName.equals(methods[i].getName())) {
                try {
                    methods[i].setAccessible(true);
                    return methods[i].invoke(o, params);
                }
                catch (IllegalAccessException ex) {
                    return null;
                }
                catch (InvocationTargetException ite) {
                    return null;
                }
            }
        }
        return null;
    }
	
	public static void setupDataSource() {

        PoolingDataSource pds = new PoolingDataSource();
        pds.setUniqueName("java:comp/env/jdbc/dashbuilder");
        pds.setClassName("bitronix.tm.resource.jdbc.lrc.LrcXADataSource");
        pds.setMaxPoolSize(5);
        pds.setAllowLocalTransactions(true);
        pds.getDriverProperties().put("user", "test_user");
        pds.getDriverProperties().put("password", "test_pass");
        pds.getDriverProperties().put("url", "jdbc:mysql://localhost:3306/dashbuilder");
        pds.getDriverProperties().put("driverClassName", "com.mysql.jdbc.Driver");
        pds.init();
	}

	
	public static void main(String[] args) {
		System.out.println(Application.getJarFiles());
	}

}
