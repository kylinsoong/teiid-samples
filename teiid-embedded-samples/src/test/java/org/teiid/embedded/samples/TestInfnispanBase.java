package org.teiid.embedded.samples;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.teiid.embedded.samples.infinispan.model.LineItem;
import org.teiid.embedded.samples.infinispan.model.Order;
import org.teiid.embedded.samples.infinispan.model.Product;

public class TestInfnispanBase extends TestBase {

	public static final int NUM_ORDERS = 10;
	public static final int NUM_PRODUCTS = 3;
	
	protected static Map<String, Order> loadCache() {
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
}
