package org.teiid.translator.hbase.phoenix;

import org.apache.phoenix.schema.PColumnImpl;
import org.apache.phoenix.schema.PDataType;
import org.apache.phoenix.schema.PName;
import org.apache.phoenix.schema.SortOrder;

public class PColumnTeiidImpl extends PColumnImpl {
	
	public PColumnTeiidImpl (PName name,PName familyName,PDataType dataType){
		super(name, familyName, dataType, 0, 0, false, 0, SortOrder.getDefault(), 0, null, false);
	}

}
