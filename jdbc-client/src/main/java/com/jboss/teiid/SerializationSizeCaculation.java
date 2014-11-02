package com.jboss.teiid;


import java.sql.ResultSet;
import java.sql.Statement;


import org.teiid.core.types.DataTypeManager;

import com.jboss.teiid.client.util.JDBCUtil;

public class SerializationSizeCaculation extends SerializationCaculation {

	public SerializationSizeCaculation() throws Exception {
		super();
	}
	
	public void countSize() throws Exception {
		Statement stmt = conn .createStatement();
		String query = "SELECT * FROM SERIALTESTVIEW WHERE id < 10";
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				Object obj = rs.getLong(1);
				System.out.print(obj.getClass() + " -> " + new MySizeUtility(null).getSize(obj, DataTypeManager.determineDataTypeClass(obj), true, false));
				obj = rs.getString(2);
				System.out.print(", " + obj.getClass() + " -> " + new MySizeUtility(null).getSize(obj, DataTypeManager.determineDataTypeClass(obj), true, false));
				obj = rs.getString(3);
				System.out.print(", " + obj.getClass() + " -> " + new MySizeUtility(null).getSize(obj, DataTypeManager.determineDataTypeClass(obj), true, false));
				rs.getString(4);
				System.out.println(", " + obj.getClass() + " -> " + new MySizeUtility(null).getSize(obj, DataTypeManager.determineDataTypeClass(obj), true, false));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			JDBCUtil.close(rs, stmt);
			JDBCUtil.close(conn);
		}
	}

	public static void main(String[] args) throws Exception {
		
		new SerializationSizeCaculation().countSize();
		
		Object c = '1';
		System.out.println(c.getClass() + " -> " + helpTestGetSize(c));
		
		byte[] buf = new byte[1];
		System.out.println(buf.getClass() + " -> " + helpTestGetSize(buf));
		
		String str = "123412341234";
		System.out.println(str.getClass() + " -> " + helpTestGetSize(str));
		
		Byte x = new Byte((byte) 127) ;
		System.out.println(x + " -> " + helpTestGetSize(x));
		
		Integer i = new Integer(2147483647);
		System.out.println(i + " -> " + helpTestGetSize(i));
		
		Long l = new Long(9223372036854775807L);
		System.out.println(l + " -> " + helpTestGetSize(l));
		
		String cloa = "abcdefghabcd";
		System.out.println(cloa + " -> " + helpTestGetSize(cloa));
		
		String clob = "abcdefghigklmnopqrstabcdefghigklmnopqrst";
		System.out.println(clob + " -> " + helpTestGetSize(clob));
		
		String cloc = "1234567890123456789012345678901234567890";
		System.out.println(cloc + " -> " + helpTestGetSize(cloc));
		
		System.out.println(helpTestGetSize(""));
		System.out.println(helpTestGetSize("1"));
		System.out.println(helpTestGetSize("12"));
		System.out.println(helpTestGetSize("123"));
		System.out.println(helpTestGetSize("1234"));
		
		System.out.println(helpTestGetSize("12341"));
		System.out.println(helpTestGetSize("123412"));
		System.out.println(helpTestGetSize("1234123"));
		System.out.println(helpTestGetSize("12341234"));
		
		System.out.println(helpTestGetSize("123412341"));
		System.out.println(helpTestGetSize("1234123412"));
		System.out.println(helpTestGetSize("12341234123"));
		System.out.println(helpTestGetSize("123412341234"));
		
		System.out.println(helpTestGetSize("1234123412341"));
		System.out.println(helpTestGetSize("12341234123412"));
		System.out.println(helpTestGetSize("123412341234123"));
		System.out.println(helpTestGetSize("1234123412341234"));
		
	}
	
	 public static long helpTestGetSize(Object obj) {  
		return new MySizeUtility(null).getSize(obj, DataTypeManager.determineDataTypeClass(obj), true, false);
	}

}
