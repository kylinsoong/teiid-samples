package org.teiid.translator.hbase;

import java.util.ResourceBundle;

import org.teiid.core.BundleUtil;

public class HBasePlugin {

	public static final String PLUGIN_ID = "org.teiid.translator.hbase" ; 
	
	private static final String BUNDLE_NAME = PLUGIN_ID + ".i18n";
	
	public static final BundleUtil Util = new BundleUtil(PLUGIN_ID, BUNDLE_NAME, ResourceBundle.getBundle(BUNDLE_NAME));
	
	public static enum Event implements BundleUtil.Event {
		TEIID27001,
		TEIID27002,
		TEIID27003,
		TEIID27004,
		TEIID27005,
		TEIID27006,
		TEIID27007,
		TEIID27008,
		TEIID27009,
		TEIID27010,
	}
}