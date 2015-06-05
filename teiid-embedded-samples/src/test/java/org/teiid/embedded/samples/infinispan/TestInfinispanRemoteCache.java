package org.teiid.embedded.samples.infinispan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.teiid.embedded.samples.TestInfnispanBase;
import org.teiid.embedded.samples.infinispan.model.Order;
import org.teiid.embedded.samples.util.JDBCUtil;
import org.teiid.resource.adapter.infinispan.InfinispanManagedConnectionFactory;
import org.teiid.translator.object.ObjectExecutionFactory;

@Ignore()
public class TestInfinispanRemoteCache extends TestInfnispanBase {
	
private static final String PROPERTIES_FILE = "jdg.properties";
	
	private static final String TEST_CACHE_NAME = getProperty().getProperty("jdg.cache.name");
	private static final String REMOTE_SERVE_LIST = getProperty().getProperty("infinispan.client.hotrod.server_list");
    
    private static Properties props = null;
    
    private static Properties getProperty() {
        if (props == null) {
            props = new Properties();
            try {
                props.load(TestInfinispanRemoteCache.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE));
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }
        return props;
    }
	
	@BeforeClass
	public static void init() throws Exception {
		
		loadCaches();
		
		init("infinispan-remote", new ObjectExecutionFactory());
		
		InfinispanManagedConnectionFactory managedConnectionFactory = new InfinispanManagedConnectionFactory();
		managedConnectionFactory.setRemoteServerList(REMOTE_SERVE_LIST);
		managedConnectionFactory.setCacheTypeMap(TEST_CACHE_NAME + ":" + Order.class.getName());
		server.addConnectionFactory("java:/infinispanRemote", managedConnectionFactory.createConnectionFactory());
		
		start(false);
		
		server.deployVDB(new FileInputStream(new File("vdb/remotecache-vdb.xml")));
		
		conn = server.getDriver().connect("jdbc:teiid:remote_orders", null);
	}
	
	protected static void loadCaches() {
		RemoteCacheManager cacheManager = new RemoteCacheManager(getProperty());
		cacheManager.getCache(TEST_CACHE_NAME).putAll(loadCache());
	}

	@Test
	public void testQuery() throws Exception {
		assertNotNull(conn);
		assertEquals(10, JDBCUtil.countResults(conn, "select * from OrdersView"));
		assertEquals(7, JDBCUtil.countResults(conn, "select * from OrdersView where OrderNum > 3"));
	}

}
