package com.teiid.quickstart.log;

import org.teiid.logging.LogConstants;
import org.teiid.logging.LogManager;
import org.teiid.logging.MessageLevel;

public class LogManagerTest {

	public static void main(String[] args) {
		LogManager.logCritical(LogConstants.CTX_RUNTIME, "This is Critical message");
		LogManager.logError(LogConstants.CTX_RUNTIME, "This is Error message");
		LogManager.logWarning(LogConstants.CTX_RUNTIME, "This is Warning message");
		LogManager.logInfo(LogConstants.CTX_RUNTIME, "This is Info message");
		LogManager.logDetail(LogConstants.CTX_RUNTIME, "This is Detail message");
		LogManager.logTrace(LogConstants.CTX_RUNTIME, "This is Trace message");
		
		LogManager.log(MessageLevel.CRITICAL, LogConstants.CTX_RUNTIME, "This is Critical message");
		LogManager.log(MessageLevel.ERROR, LogConstants.CTX_RUNTIME, "This is Error message");
		LogManager.log(MessageLevel.WARNING, LogConstants.CTX_RUNTIME, "This is Warning message");
		LogManager.log(MessageLevel.INFO, LogConstants.CTX_RUNTIME, "This is Info message");
		LogManager.log(MessageLevel.DETAIL, LogConstants.CTX_RUNTIME, "This is Detail message");
		LogManager.log(MessageLevel.TRACE, LogConstants.CTX_RUNTIME, "This is Trace message");
		
	}
}
