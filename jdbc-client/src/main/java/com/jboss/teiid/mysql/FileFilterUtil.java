package com.jboss.teiid.mysql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileFilterUtil {

	public static void main(String[] args) throws IOException {
		File file = new File("time_log");
		FileInputStream fis = new FileInputStream(file);
		 
		//Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	 
		String line = null;
		while ((line = br.readLine()) != null) {
			if(line.startsWith("Deserialize")){
				System.out.println(line);
			}
		}
	 
		br.close();
	}

}
