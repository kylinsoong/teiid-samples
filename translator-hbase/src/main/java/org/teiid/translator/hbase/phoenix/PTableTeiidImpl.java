package org.teiid.translator.hbase.phoenix;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.phoenix.hbase.index.util.KeyValueBuilder;
import org.apache.phoenix.index.IndexMaintainer;
import org.apache.phoenix.schema.AmbiguousColumnException;
import org.apache.phoenix.schema.ColumnFamilyNotFoundException;
import org.apache.phoenix.schema.ColumnNotFoundException;
import org.apache.phoenix.schema.PColumn;
import org.apache.phoenix.schema.PColumnFamily;
import org.apache.phoenix.schema.PIndexState;
import org.apache.phoenix.schema.PName;
import org.apache.phoenix.schema.PRow;
import org.apache.phoenix.schema.PTable;
import org.apache.phoenix.schema.PTableKey;
import org.apache.phoenix.schema.PTableType;
import org.apache.phoenix.schema.RowKeySchema;
import org.apache.phoenix.schema.stats.PTableStats;

public class PTableTeiidImpl implements PTable {
	
	private PName tableName;
	private List<PColumn> columns;
	private List<PColumn> pkColumns;
	
	private PTableTeiidImpl(PName tableName, List<PColumn> columns) {
		this.tableName = tableName;
		this.columns = columns;
		pkColumns = new ArrayList<PColumn>();
		for(int i = 0 ; i < columns.size() ; i ++) {
			PName familyName = columns.get(i).getFamilyName();
			if(familyName == null) {
				pkColumns.add(columns.get(i));
			}
		}
	}
	
	public static PTableTeiidImpl makeTable(PName tableName, List<PColumn> columns) {
		return new PTableTeiidImpl(tableName, columns);
	}

	@Override
	public long getTimeStamp() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getSequenceNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public PName getName() {
		String name = tableName.getString();
		name = name.substring(1, name.length() -1);
		return PNameTeiidImpl.makePNameWithoutQuote(name.toLowerCase());
	}

	@Override
	public PName getSchemaName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PName getTableName() {
		return tableName;
	}

	@Override
	public PName getTenantId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PTableType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PName getPKName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PColumn> getPKColumns() {
		return pkColumns;
	}

	@Override
	public List<PColumn> getColumns() {
		return columns;
	}

	@Override
	public List<PColumnFamily> getColumnFamilies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PColumnFamily getColumnFamily(byte[] family)
			throws ColumnFamilyNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PColumnFamily getColumnFamily(String family)
			throws ColumnFamilyNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PColumn getColumn(String name) throws ColumnNotFoundException,
			AmbiguousColumnException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PColumn getPKColumn(String name) throws ColumnNotFoundException {
		return null;
	}

	@Override
	public PRow newRow(KeyValueBuilder builder, long ts,
			ImmutableBytesWritable key, byte[]... values) {
		return null;
	}

	@Override
	public PRow newRow(KeyValueBuilder builder, ImmutableBytesWritable key,
			byte[]... values) {
		return null;
	}

	@Override
	public int newKey(ImmutableBytesWritable key, byte[][] values) {
		return 0;
	}

	@Override
	public RowKeySchema getRowKeySchema() {
		return null;
	}

	@Override
	public Integer getBucketNum() {
		return null;
	}

	@Override
	public List<PTable> getIndexes() {
		return null;
	}

	@Override
	public PIndexState getIndexState() {
		return null;
	}

	@Override
	public PName getParentName() {
		return null;
	}

	@Override
	public PName getParentTableName() {
		return null;
	}

	@Override
	public PName getParentSchemaName() {
		return null;
	}

	@Override
	public List<PName> getPhysicalNames() {
		return null;
	}

	@Override
	public PName getPhysicalName() {
		return null;
	}

	@Override
	public boolean isImmutableRows() {
		return false;
	}

	@Override
	public void getIndexMaintainers(ImmutableBytesWritable ptr) {

	}

	@Override
	public IndexMaintainer getIndexMaintainer(PTable dataTable) {
		return null;
	}

	@Override
	public PName getDefaultFamilyName() {
		return null;
	}

	@Override
	public boolean isWALDisabled() {
		return false;
	}

	@Override
	public boolean isMultiTenant() {
		return false;
	}

	@Override
	public ViewType getViewType() {
		return null;
	}

	@Override
	public String getViewStatement() {
		return null;
	}

	@Override
	public Short getViewIndexId() {
		return null;
	}

	@Override
	public PTableKey getKey() {
		return null;
	}

	@Override
	public int getEstimatedSize() {
		return 0;
	}

	@Override
	public IndexType getIndexType() {
		return null;
	}

	@Override
	public PTableStats getTableStats() {
		return null;
	}

}
