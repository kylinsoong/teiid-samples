package com.teiid.quickstart.log;

import java.util.logging.Logger;

public class MyLoggerGlobalTestClass {
	
	static {
		System.setProperty("java.util.logging.config.file", "/home/kylin/tmp/logging.properties");
	}

	public static void main(String[] args) {
		Logger logger = Logger.getLogger("java.util.logging.TEST");
		logger.finest("This is finest test message");
		logger.finer("This is finer test message");
		logger.fine("This is fine test message");
		logger.config("This is config test message");
		logger.info("This is info test message");
		logger.warning("This is warning test message");
		logger.severe("This is severe test message");
	}

}
