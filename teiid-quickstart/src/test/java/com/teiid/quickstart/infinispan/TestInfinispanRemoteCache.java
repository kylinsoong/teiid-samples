package com.teiid.quickstart.infinispan;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.teiid.resource.adapter.infinispan.InfinispanManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.object.ObjectExecutionFactory;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.junit.Test;

import com.teiid.quickstart.infinispan.model.LineItem;
import com.teiid.quickstart.infinispan.model.Order;
import com.teiid.quickstart.infinispan.model.Product;
import com.teiid.quickstart.util.JDBCUtil;


public class TestInfinispanRemoteCache {

    private static final String PROPERTIES_FILE = "jdg.properties";
	
	private static final String TEST_CACHE_NAME = getProperty().getProperty("jdg.cache.name");
	private static final String REMOTE_SERVE_LIST = getProperty().getProperty("infinispan.client.hotrod.server_list");
    
    private static Properties props = null;
    
    public static Properties getProperty() {
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

	static EmbeddedServer server = null;
	private static Connection conn = null;
	
	@Test
	public void testLoadCache() {
		RemoteCacheManager cacheManager = new RemoteCacheManager(getProperty());
		RemoteCache<String, Object> cache = cacheManager.getCache(TEST_CACHE_NAME);
		cache.putAll(loadCache());
		assertEquals(10, cache.size());
	}
	
	protected void init() throws Exception {
		
		loadCaches();

		server = new EmbeddedServer();
		
		ObjectExecutionFactory executionFactory = new ObjectExecutionFactory() ;
		executionFactory.start();
		server.addTranslator("infinispan-remote", executionFactory);
		
		
		InfinispanManagedConnectionFactory managedConnectionFactory = new InfinispanManagedConnectionFactory();
		managedConnectionFactory.setRemoteServerList(REMOTE_SERVE_LIST);
		managedConnectionFactory.setCacheTypeMap(TEST_CACHE_NAME + ":" + Order.class.getName() + ";id");
		server.addConnectionFactory("java:/infinispanRemote", managedConnectionFactory.createConnectionFactory());
		
		EmbeddedConfiguration config = new EmbeddedConfiguration();
		server.start(config);
		
		
		server.deployVDB(new FileInputStream(new File("src/vdb/remotecache-vdb.xml")));
		
		conn = server.getDriver().connect("jdbc:teiid:remote_orders", null);
	}
	
	@Test
	public void testQuery() throws Exception {
		init();
		JDBCUtil.executeQuery(conn, "select * from OrdersView");
		JDBCUtil.executeQuery(conn, "select * from OrdersView where OrderNum > 3");
	}
	
	
	private void loadCaches() {
		RemoteCacheManager cacheManager = new RemoteCacheManager(getProperty());
		cacheManager.getCache(TEST_CACHE_NAME).putAll(loadCache());
	}

	public static final int NUM_ORDERS = 10;
	public static final int NUM_PRODUCTS = 3;
	
	protected Map<String, Order> loadCache() {
		
		Map<String, Order> incache = new HashMap<String, Order>();
		List<Product> products = new ArrayList<Product>(NUM_PRODUCTS);
		products.add(new Product(1, "Shirt", 54.99)); 
		products.add(new Product(2, "Pants", 89.00)); 
		products.add(new Product(3, "Socks", 1.29)); 
		int lineitems = 1;
		for (int i = 1; i <= NUM_ORDERS; i++) {
			List<LineItem> items = new ArrayList<LineItem>();
			for (int j = 0, p = 0, q = 1; j < lineitems; j++) {
				LineItem item = new LineItem(j + 1, products.get(p), q);
				items.add(item);
				++p;
				++q;
			}
			Order order = new Order(i, new Date(), "Person " + i, items); 
			incache.put(String.valueOf(i), order);
			++lineitems;
			if (lineitems > NUM_PRODUCTS) {
				lineitems = 1;
			}
		}
		return incache;
	}
	
	public static void main(String[] args) throws Exception { 
		
		TestInfinispanRemoteCache test = new TestInfinispanRemoteCache();
		test.testQuery();
	}
}
