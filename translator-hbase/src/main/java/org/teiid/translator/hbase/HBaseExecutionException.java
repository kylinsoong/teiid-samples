package org.teiid.translator.hbase;

import java.sql.SQLException;

import org.teiid.core.BundleUtil;
import org.teiid.language.Command;
import org.teiid.translator.TranslatorException;

public class HBaseExecutionException extends TranslatorException {

	private static final long serialVersionUID = 2440281475514847142L;
	
	public HBaseExecutionException(BundleUtil.Event event, SQLException error, String command) {
		super(error, event.toString() + ":" + HBasePlugin.Util.gs(HBasePlugin.Event.TEIID27010, command));
		setCode(String.valueOf(error.getErrorCode()));
	}
	
	public HBaseExecutionException(BundleUtil.Event event, SQLException error, Command command) {
		super(error, event.toString() + ":" + HBasePlugin.Util.gs(HBasePlugin.Event.TEIID27010, command));
		setCode(String.valueOf(error.getErrorCode()));
	}
	
	public HBaseExecutionException(BundleUtil.Event event, SQLException error, BundleUtil.Event code, String command) {
		super(error, event.toString() + ":" + HBasePlugin.Util.gs(code, command));
		setCode(String.valueOf(error.getErrorCode()));
	}
	
	public HBaseExecutionException(BundleUtil.Event event, SQLException error, BundleUtil.Event code, Command command) {
		super(error, event.toString() + ":" + HBasePlugin.Util.gs(code, command));
		setCode(String.valueOf(error.getErrorCode()));
	}

}
