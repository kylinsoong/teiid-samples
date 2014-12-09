package org.teiid.translator.hbase;

import java.util.ResourceBundle;

import org.teiid.core.BundleUtil;

public class HBasePlugin {

	public static final String PLUGIN_ID = "org.teiid.translator.hbase" ; //$NON-NLS-1$
	public static final BundleUtil Util = new BundleUtil(PLUGIN_ID,PLUGIN_ID + ".i18n", ResourceBundle.getBundle(PLUGIN_ID + ".i18n")); //$NON-NLS-1$ //$NON-NLS-2$
}
