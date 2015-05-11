package org.teiid.runtime.embedded;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.resource.ResourceException;
import javax.sql.DataSource;

import org.jboss.jca.adapters.jdbc.local.LocalManagedConnectionFactory;
import org.jboss.jca.core.api.connectionmanager.pool.PoolConfiguration;
import org.jboss.jca.core.connectionmanager.notx.NoTxConnectionManagerImpl;
import org.jboss.jca.core.connectionmanager.pool.strategy.OnePool;

public class IronjacmarDataSource {
	
	public static void main(String[] args) throws ResourceException, SQLException {
		
		String driverClass = "org.h2.Driver";
		String connURL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
		String user = "sa";
		String password = "sa";

		
		DataSource ds = newDataSource(driverClass, connURL, user, password);
		
		System.out.println(ds);
		
		Connection conn = ds.getConnection();
		System.out.println(conn.getAutoCommit());
		System.out.println(conn.getClass());
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT CURRENT_DATE()");
		while(rs.next()) {
			System.out.println(rs.getObject(1));
		}
				
	}
	
	public static DataSource newDataSource(String driverClass, String connURL, String user, String password) throws ResourceException{

		LocalManagedConnectionFactory mcf = new LocalManagedConnectionFactory();
		
		mcf.setDriverClass(driverClass);
		mcf.setConnectionURL(connURL);
		mcf.setUserName(user);
		mcf.setPassword(password);
		
		NoTxConnectionManagerImpl cm = new NoTxConnectionManagerImpl();
		OnePool pool = new OnePool(mcf, new PoolConfiguration(), false);
		pool.setConnectionListenerFactory(cm);
		cm.setPool(pool);
		
		return (DataSource) mcf.createConnectionFactory(cm);
	}

}
