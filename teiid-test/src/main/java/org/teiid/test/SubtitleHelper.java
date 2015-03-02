package org.teiid.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubtitleHelper {
	
	static List<String> list = new ArrayList<String>();

	public static void main(String[] args) {

		File file = new File("txt/5 - 1 - 4.1. Key-value stores: Why Key-Value_NOSQL? (00:15:36).txt");
	    if (!file.exists()) {
	      System.out.println(file + " does not exist.");
	      return;
	    }
	    if (!(file.isFile() && file.canRead())) {
	      System.out.println(file.getName() + " cannot be read from.");
	      return;
	    }
	    try {
	      FileInputStream fis = new FileInputStream(file);
	      char current;
	      StringBuffer sb = new StringBuffer();
	      while (fis.available() > 0) {
	        current = (char) fis.read();
	        if(current == '.'){
	        	sb.append(current);
	        	sb.append("\n");
	        	list.add(sb.toString());
	        	sb = new StringBuffer();
	        } else if(current != '\n'){
	        	sb.append(current);
	        } else if(current == '\n'){
	        	sb.append(' ');
	        }
	      }
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    
	    for(String line : list) {
	    	System.out.println(line);
	    }
	}

}
