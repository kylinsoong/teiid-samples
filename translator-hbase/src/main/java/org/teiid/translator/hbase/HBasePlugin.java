package org.teiid.translator.hbase;

import java.util.ResourceBundle;

import org.teiid.core.BundleUtil;

public class HBasePlugin {

	public static final String PLUGIN_ID = "org.teiid.translator.hbase" ; 
	
	private static final String BUNDLE_NAME = PLUGIN_ID + ".i18n";
	
	public static final BundleUtil Util = new BundleUtil(PLUGIN_ID, BUNDLE_NAME, ResourceBundle.getBundle(BUNDLE_NAME));
	
	public static enum Event implements BundleUtil.Event {
		
		// Phoenix HBase Table Mapping
		TEIID27001,
		
		//HBaseQueryExecution
		TEIID27002,
		
		// HBaseUpdateExecution
		TEIID27003,
		
		TEIID27004,
		TEIID27005,
		TEIID27006,
		TEIID27007,
		TEIID27008,
		TEIID27009,
		TEIID27010,
		
		TEIID27011,
		TEIID27012,
		TEIID27013,
		TEIID27014,
		TEIID27015,
		TEIID27016,
		TEIID27017,
		TEIID27018,
		TEIID27019,
		TEIID27020,
	}
}