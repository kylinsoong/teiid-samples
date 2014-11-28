package com.teiid.quickstart.log;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.teiid.logging.LogConstants;

public class JavaLogTest {
	
	static Logger log = Logger.getLogger(LogConstants.CTX_RUNTIME);
	
	public static void main(String[] args) {
		
		log.setLevel(Level.FINEST);
		
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.FINEST);
        log.addHandler(handler);
		
		log.log(Level.SEVERE, "This is message");
		log.log(Level.WARNING, "This is message");
		log.log(Level.INFO, "This is message");
		log.log(Level.CONFIG, "This is message");
		log.log(Level.FINE, "This is message");
		log.log(Level.FINER, "This is message");
		log.log(Level.FINEST, "This is message");
	}

}
