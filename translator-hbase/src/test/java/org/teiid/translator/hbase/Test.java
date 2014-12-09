package org.teiid.translator.hbase;

import org.teiid.cdk.api.TranslationUtility;
import org.teiid.language.Command;

public class Test {

	public static void main(String[] args) {
		
		TranslationUtility util = new TranslationUtility(TestHBaseUtil.queryMetadataInterface());

		Command command = util.parseCommand("SELECT * FROM Customer");
		
		System.out.println(command);
	}

}
