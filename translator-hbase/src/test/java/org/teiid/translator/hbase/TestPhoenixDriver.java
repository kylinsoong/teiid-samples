package org.teiid.translator.hbase;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class TestPhoenixDriver {

	@Test
	public void testHBaseTableMapping() throws Exception {
		Connection conn = PhoenixUtils.getPhoenixConnection("127.0.0.1:2181");
		String tname = "Customer";
		String rname = "ROW_ID";
		Map<String, List<String>> map = new HashMap<String, List<String>> ();
		List<String> clist = new ArrayList<String>();
		clist.add("city");
		clist.add("name");
		map.put("customer", clist);
		List<String> slist = new ArrayList<String>();
		slist.add("amount");
		slist.add("product");
		map.put("sales", slist);
		PhoenixUtils.hbaseTableMapping(conn, tname, rname, map);
	}
}
