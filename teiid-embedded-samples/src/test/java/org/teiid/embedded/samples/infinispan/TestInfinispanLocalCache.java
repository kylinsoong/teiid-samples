package org.teiid.embedded.samples.infinispan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;

import javax.resource.ResourceException;

import org.infinispan.Cache;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.teiid.embedded.samples.TestInfnispanBase;
import org.teiid.embedded.samples.infinispan.model.Order;
import org.teiid.embedded.samples.util.JDBCUtil;
import org.teiid.resource.adapter.infinispan.InfinispanManagedConnectionFactory;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.object.ObjectConnection;
import org.teiid.translator.object.ObjectExecutionFactory;

@Ignore()
public class TestInfinispanLocalCache extends TestInfnispanBase {
	
	static final String TEST_CACHE_NAME = "local-quickstart-cache";


	@BeforeClass
	public static void init() throws Exception {
		
		init("infinispan-local", new ObjectExecutionFactory());
		
		InfinispanManagedConnectionFactory managedConnectionFactory = new InfinispanManagedConnectionFactory();
		managedConnectionFactory.setConfigurationFileNameForLocalCache("src/test/resources/infinispan-config-local.xml");
		managedConnectionFactory.setCacheTypeMap(TEST_CACHE_NAME + ":" + Order.class.getName());
		server.addConnectionFactory("java:/infinispanTest", managedConnectionFactory.createConnectionFactory());
		
		start(false);
		
		loadCaches(managedConnectionFactory);
		
		server.deployVDB(new FileInputStream(new File("vdb/infinispancache-vdb.xml")));
		
		conn = server.getDriver().connect("jdbc:teiid:orders", null);
	}
	
	@Test
	public void testQuery() throws Exception {
		assertNotNull(conn);
		assertEquals(10, JDBCUtil.countResults(conn, "select * from OrdersView"));
		assertEquals(7, JDBCUtil.countResults(conn, "select * from OrdersView where OrderNum > 3"));
	}
	
	protected static void loadCaches(InfinispanManagedConnectionFactory managedConnectionFactory) throws TranslatorException, ResourceException {
		
		ObjectConnection conn = (ObjectConnection) managedConnectionFactory.createConnectionFactory().getConnection();
		Cache<String, Order> cache = conn.getCacheContainer().getCache(TEST_CACHE_NAME);
		cache.putAll(loadCache());		
	}
	
	
	
}
