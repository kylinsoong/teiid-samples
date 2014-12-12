package org.teiid.translator.hbase.phoenix;

import org.apache.phoenix.schema.PColumnImpl;
import org.apache.phoenix.schema.PDataType;
import org.apache.phoenix.schema.PName;

public class PColumnTeiidImpl extends PColumnImpl {
	
	public PColumnTeiidImpl (PName name,PName familyName,PDataType dataType){
		super(name, familyName, dataType, 0, 0, false, 0, null, 0, null, false);
	}

}
