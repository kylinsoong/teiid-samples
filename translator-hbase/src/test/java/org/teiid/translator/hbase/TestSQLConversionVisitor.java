package org.teiid.translator.hbase;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.teiid.cdk.api.TranslationUtility;
import org.teiid.language.Command;
import org.teiid.translator.TranslatorException;

public class TestSQLConversionVisitor {
	
	@Test
	public void testSelect() throws TranslatorException {
		String sql = "SELECT * FROM Customer";
		String expected = "SELECT customer.\"ROW_ID\", customer.\"city\", customer.\"name\", customer.\"amount\", customer.\"product\" FROM \"Customer\" AS customer";
		helpTest(sql, expected);
		
		sql = "SELECT city, amount FROM Customer";
		expected = "SELECT customer.\"city\", customer.\"amount\" FROM \"Customer\" AS customer";
		helpTest(sql, expected);
		
		sql = "SELECT DISTINCT city FROM Customer";
		expected = "SELECT DISTINCT customer.\"city\" FROM \"Customer\" AS customer";
		helpTest(sql, expected);
		
		sql = "SELECT city, amount FROM Customer WHERE PK='104'";
		expected = "SELECT customer.\"city\", customer.\"amount\" FROM \"Customer\" AS customer WHERE customer.\"ROW_ID\" = '104'";
		helpTest(sql, expected);
	}
	
	@Test
	public void testSelectOrderBy() throws TranslatorException {
		String sql = "SELECT * FROM Customer ORDER BY PK";
		String expected = "SELECT customer.\"ROW_ID\", customer.\"city\", customer.\"name\", customer.\"amount\", customer.\"product\" FROM \"Customer\" AS customer ORDER BY customer.\"ROW_ID\"";
		helpTest(sql, expected);
		
		sql = "SELECT * FROM Customer ORDER BY PK ASC";
		expected = "SELECT customer.\"ROW_ID\", customer.\"city\", customer.\"name\", customer.\"amount\", customer.\"product\" FROM \"Customer\" AS customer ORDER BY customer.\"ROW_ID\"";
		helpTest(sql, expected);
		
		sql = "SELECT * FROM Customer ORDER BY PK DESC";
		expected = "SELECT customer.\"ROW_ID\", customer.\"city\", customer.\"name\", customer.\"amount\", customer.\"product\" FROM \"Customer\" AS customer ORDER BY customer.\"ROW_ID\" DESC";
		helpTest(sql, expected);
		
		sql = "SELECT * FROM Customer ORDER BY name, city DESC";
		expected = "SELECT customer.\"ROW_ID\", customer.\"city\", customer.\"name\", customer.\"amount\", customer.\"product\" FROM \"Customer\" AS customer ORDER BY customer.\"name\", customer.\"city\" DESC";
		helpTest(sql, expected);
	}
	
	private static TranslationUtility translationUtility = new TranslationUtility(TestHBaseUtil.queryMetadataInterface());
	
	private void helpTest(String sql, String expected) throws TranslatorException  {
		
		Command command = translationUtility.parseCommand(sql);
		
		HBaseExecutionFactory ef = new HBaseExecutionFactory();
		ef.start();
		
		SQLConversionVisitor vistor = new SQLConversionVisitor(ef);
		vistor.visitNode(command);
		
		System.out.println(vistor.getSQL());
		
		assertEquals(expected, vistor.getSQL());
		
	}

}
