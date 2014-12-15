package org.teiid.translator.hbase.phoenix;

import org.apache.phoenix.hbase.index.util.ImmutableBytesPtr;
import org.apache.phoenix.schema.PName;

public class PNameTeiidImpl implements PName {

	private String name;

	public static PNameTeiidImpl makePName(String name) {
		return new PNameTeiidImpl("\"" +name + "\"");
	}
	
	public static PNameTeiidImpl makePNameWithoutQuote(String name) {
		return new PNameTeiidImpl(name);
	}

	public PNameTeiidImpl(String name) {
		this.name = name;
	}

	@Override
	public String getString() {
		return name;
	}

	@Override
	public byte[] getBytes() {
		return name.getBytes();
	}

	@Override
	public ImmutableBytesPtr getBytesPtr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getEstimatedSize() {
		// TODO Auto-generated method stub
		return 0;
	}

}
