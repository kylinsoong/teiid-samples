package org.teiid.translator.hbase;

import org.teiid.metadata.Column;
import org.teiid.metadata.ExtensionMetadataProperty;
import org.teiid.metadata.MetadataFactory;
import org.teiid.metadata.Table;
import org.teiid.resource.adapter.hbase.HBaseConnection;
import org.teiid.translator.MetadataProcessor;
import org.teiid.translator.TranslatorException;

public class HBaseMetadataProcessor implements MetadataProcessor<HBaseConnection> {
	
	@ExtensionMetadataProperty(applicable=Table.class, datatype=String.class, display="HBase Table Name", description="HBase Table Name", required=true)
	public static final String TABLE = MetadataFactory.HBASE_URI + "TABLE";

    @ExtensionMetadataProperty(applicable=Column.class, datatype=String.class, display="Column Family and Qualifier", description="Column Family and Column Qualifier, seperated by a dot, for eample, 'customer.city' means cell's family is 'customer', qualifier is 'city'", required=true)    
	public static final String CELL = MetadataFactory.HBASE_URI + "CELL";


	@Override
	public void process(MetadataFactory metadataFactory, HBaseConnection connection) throws TranslatorException {
		
	}

}
