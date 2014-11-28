package com.teiid.quickstart.log;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

import java.util.logging.Logger;


public class MyLoggerTestClass {
	
//	static {
//		Logger root =  LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME);
//		ConsoleHandler handler = new ConsoleHandler();
//		handler.setLevel(Level.FINEST);
//		handler.setFormatter(new MyFormatter());
//		root.addHandler(handler);
//	}

	public static void main(String[] args) {
		Logger logger = Logger.getLogger("java.util.logging.TEST");
		logger.setLevel(Level.FINEST);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.FINEST); 
		handler.setFormatter(new MyFormatter());
		logger.setUseParentHandlers(false);
		logger.addHandler(handler);
				
		logger.finest("This is finest test message");
		logger.finer("This is finer test message");
		logger.fine("This is fine test message");
		logger.config("This is config test message");
		logger.info("This is info test message");
		logger.warning("This is warning test message");
		logger.severe("This is severe test message");
		
	}

}
