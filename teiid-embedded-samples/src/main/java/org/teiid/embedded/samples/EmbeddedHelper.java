package org.teiid.embedded.samples;

import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.resource.ResourceException;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;

import org.jboss.jca.adapters.jdbc.local.LocalManagedConnectionFactory;
import org.jboss.jca.core.api.connectionmanager.pool.PoolConfiguration;
import org.jboss.jca.core.connectionmanager.notx.NoTxConnectionManagerImpl;
import org.jboss.jca.core.connectionmanager.pool.strategy.OnePool;

import com.arjuna.ats.arjuna.common.ObjectStoreEnvironmentBean;
import com.arjuna.ats.arjuna.common.arjPropertyManager;
import com.arjuna.common.internal.util.propertyservice.BeanPopulator;

/**
 * The <code>EmbeddedHelper</code> is a Helper class for running Teiid Embedded. 
 * 
 * The 'getTransactionManager()' method return a Narayana JTA TransactionManager which used to set to EmbeddedConfiguration:
 * 
 * <pre>
 *  EmbeddedServer server = new EmbeddedServer();
 *  ...
 *  EmbeddedConfiguration config = new EmbeddedConfiguration();
 *  config.setTransactionManager(EmbeddedHelper.getTransactionManager());
 *  server.start(config);
 * </pre>
 * 
 * The 'newDataSource' method return a ironjacamar WrapperDataSource which used to set EmbeddedServer's named connection factory:
 * 
 * <pre>
 *  EmbeddedServer server = new EmbeddedServer();
 *  ...
 *  DataSource ds = EmbeddedHelper.newDataSource("${driver}", "${url}", "${user}", "${password}");
 *  server.addConnectionFactory("name", ds);
 *  ...
 * </pre>
 * 
 *
 */
public class EmbeddedHelper {
	
	public static TransactionManager getTransactionManager() throws Exception {
		
		arjPropertyManager.getCoreEnvironmentBean().setNodeIdentifier("1");
		arjPropertyManager.getCoreEnvironmentBean().setSocketProcessIdPort(0);
		arjPropertyManager.getCoreEnvironmentBean().setSocketProcessIdMaxPorts(10);
		
		arjPropertyManager.getCoordinatorEnvironmentBean().setEnableStatistics(false);
		arjPropertyManager.getCoordinatorEnvironmentBean().setDefaultTimeout(300);
		arjPropertyManager.getCoordinatorEnvironmentBean().setTransactionStatusManagerEnable(false);
		arjPropertyManager.getCoordinatorEnvironmentBean().setTxReaperTimeout(120000);
		
		String storeDir = getStoreDir();
		
		arjPropertyManager.getObjectStoreEnvironmentBean().setObjectStoreDir(storeDir);
		BeanPopulator.getNamedInstance(ObjectStoreEnvironmentBean.class, "communicationStore").setObjectStoreDir(storeDir); //$NON-NLS-1$
		
		return com.arjuna.ats.jta.TransactionManager.transactionManager();
	}

	
	private static String getStoreDir() {
		String defDir = getSystemProperty("user.home") + File.separator + ".teiid/embedded/data"; //$NON-NLS-1$ //$NON-NLS-2$
		return getSystemProperty("teiid.embedded.txStoreDir", defDir);
	}


	private static String getSystemProperty(final String name, final String value) {
		return AccessController.doPrivileged(new PrivilegedAction<String>(){

			@Override
			public String run() {
				return System.getProperty(name, value);
			}});
	}
	
	private static String getSystemProperty(final String name) {
		return AccessController.doPrivileged(new PrivilegedAction<String>(){

			@Override
			public String run() {
				return System.getProperty(name);
			}});
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
