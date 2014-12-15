package org.teiid.translator.hbase;

import org.junit.Test;
import org.teiid.cdk.api.TranslationUtility;
import org.teiid.cdk.unittest.FakeTranslationFactory;
import org.teiid.language.Command;
import org.teiid.translator.TranslatorException;

public class TestSQLConversionVisitor {
	
	@Test
	public void testSelect() throws TranslatorException {
		String sql = "SELECT * FROM Customer";
		Command command = translationUtility.parseCommand(sql);
		
		HBaseExecutionFactory ef = new HBaseExecutionFactory();
		ef.start();

	}
	
	private static TranslationUtility translationUtility = new TranslationUtility(TestHBaseUtil.queryMetadataInterface());
	
	private void helpTest(String sql, String expected) throws Exception {
		
	}

}
