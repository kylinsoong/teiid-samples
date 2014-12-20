package org.teiid.translator.hbase;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.phoenix.schema.PColumn;
import org.apache.phoenix.schema.PDataType;
import org.apache.phoenix.schema.PTable;
import org.junit.Test;
import org.teiid.translator.hbase.phoenix.PColumnTeiidImpl;
import org.teiid.translator.hbase.phoenix.PNameTeiidImpl;
import org.teiid.translator.hbase.phoenix.PTableTeiidImpl;
import org.teiid.translator.hbase.phoenix.PhoenixUtils;

public class TestPhoenixDriver {
	
	@Test
	public void testHBaseTableMapping() {
		
		String expect = "CREATE TABLE IF NOT EXISTS \"Customer\" (\"Row_Id\" VARCHAR PRIMARY KEY, \"customer\".\"name\" VARCHAR, \"customer\".\"city\" VARCHAR, \"sales\".\"product\" VARCHAR, \"sales\".\"amount\" VARCHAR)";
		List<PColumn> columns = new ArrayList<PColumn>();
		PColumn pk = new PColumnTeiidImpl(PNameTeiidImpl.makePName("Row_Id"), null, PDataType.VARCHAR);
		PColumn name = new PColumnTeiidImpl(PNameTeiidImpl.makePName("name"), PNameTeiidImpl.makePName("customer"), PDataType.VARCHAR);
		PColumn city = new PColumnTeiidImpl(PNameTeiidImpl.makePName("city"), PNameTeiidImpl.makePName("customer"), PDataType.VARCHAR);
		PColumn product = new PColumnTeiidImpl(PNameTeiidImpl.makePName("product"), PNameTeiidImpl.makePName("sales"), PDataType.VARCHAR);
		PColumn amount = new PColumnTeiidImpl(PNameTeiidImpl.makePName("amount"), PNameTeiidImpl.makePName("sales"), PDataType.VARCHAR);
		columns.add(pk);
		columns.add(name);
		columns.add(city);
		columns.add(product);
		columns.add(amount);
		
		PTable ptable = PTableTeiidImpl.makeTable(PNameTeiidImpl.makePName("Customer"), columns);

		assertEquals(expect, PhoenixUtils.hbaseTableMappingDDL(ptable));
	
	}
	
	@Test
	public void testDataTypes() {
		assertEquals("VARCHAR", PDataType.VARCHAR.getSqlTypeName());
		assertEquals("VARBINARY", PDataType.VARBINARY.getSqlTypeName());
		assertEquals("BOOLEAN", PDataType.BOOLEAN.getSqlTypeName());
		assertEquals("TINYINT", PDataType.TINYINT.getSqlTypeName());
		assertEquals("SMALLINT", PDataType.SMALLINT.getSqlTypeName());
		assertEquals("INTEGER", PDataType.INTEGER.getSqlTypeName());
		assertEquals("BIGINT", PDataType.LONG.getSqlTypeName());
		assertEquals("FLOAT", PDataType.FLOAT.getSqlTypeName());
		assertEquals("DOUBLE", PDataType.DOUBLE.getSqlTypeName());
		assertEquals("DECIMAL", PDataType.DECIMAL.getSqlTypeName());
		assertEquals("DATE", PDataType.DATE.getSqlTypeName());
		assertEquals("TIME", PDataType.TIME.getSqlTypeName());
		assertEquals("TIMESTAMP", PDataType.TIMESTAMP.getSqlTypeName());
	}
}
