package com.teiid.quickstart.loopback;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.teiid.language.Command;
import org.teiid.metadata.RuntimeMetadata;
import org.teiid.translator.DataNotAvailableException;
import org.teiid.translator.Execution;
import org.teiid.translator.ResultSetExecution;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.UpdateExecution;
import org.teiid.translator.loopback.LoopbackExecutionFactory;

import com.teiid.quickstart.FakeTranslationFactory;
import com.teiid.quickstart.TranslationUtility;

public class TestLoopback {
	
	@Test
	public void testInit() {
		LoopbackExecutionFactory factory = new LoopbackExecutionFactory();
		factory.setWaitTime(1000 * 5);
		factory.setRowCount(100);
		assertNotNull(factory);
	}
	
	@Test
	public void testSearch() throws TranslatorException {
		
		LoopbackExecutionFactory factory = new LoopbackExecutionFactory();
		factory.setWaitTime(0);
		factory.setRowCount(1);
		factory.setIncrementRows(false);
		
		String sql = "SELECT intkey FROM BQT1.SmallA";
		TranslationUtility util = FakeTranslationFactory.getInstance().getBQTTranslationUtility();
		Object[][] expectedResults = new Object[][] {new Object[] { new Integer(0) }};
		
		Command command = util.parseCommand(sql);
		RuntimeMetadata metadata = util.createRuntimeMetadata();
		
		Execution exec = factory.createExecution(command, null, metadata, null);
		exec.execute();
		
		List results = readResultsFromExecution(exec);
		exec.close();
		
		assertEquals(results.size(), expectedResults.length);
		
		compareResults(expectedResults, results);
	}
	
	@Test
	public void testInsert() throws Exception {
		LoopbackExecutionFactory factory = new LoopbackExecutionFactory();
		factory.setWaitTime(0);
		factory.setRowCount(1);
		factory.setIncrementRows(false);
		
		String sql = "INSERT INTO BQT1.SmallA (stringkey) VALUES ('x')";
		TranslationUtility util = FakeTranslationFactory.getInstance().getBQTTranslationUtility();
		Object[][] expectedResults = new Object[][] {new Object[] { new Integer(0) }};
		
		Command command = util.parseCommand(sql);
		RuntimeMetadata metadata = util.createRuntimeMetadata();
		
		Execution exec = factory.createExecution(command, null, metadata, null);
		exec.execute();
		
		List results = readResultsFromExecution(exec);
		exec.close();
		
		assertEquals(results.size(), expectedResults.length);
		
		compareResults(expectedResults, results);
	}
	
	@Test
	public void testUpdate() throws Exception {
		LoopbackExecutionFactory factory = new LoopbackExecutionFactory();
		factory.setWaitTime(0);
		factory.setRowCount(1);
		factory.setIncrementRows(false);
		
		String sql = "UPDATE BQT1.SmallA SET stringkey = 'x'";
		TranslationUtility util = FakeTranslationFactory.getInstance().getBQTTranslationUtility();
		Object[][] expectedResults = new Object[][] {new Object[] { new Integer(0) }};
		
		Command command = util.parseCommand(sql);
		RuntimeMetadata metadata = util.createRuntimeMetadata();
		
		Execution exec = factory.createExecution(command, null, metadata, null);
		exec.execute();
		
		List results = readResultsFromExecution(exec);
		exec.close();
		
		assertEquals(results.size(), expectedResults.length);
		
		compareResults(expectedResults, results);
	}
	
	@Test
	public void testDelete() throws Exception {
		LoopbackExecutionFactory factory = new LoopbackExecutionFactory();
		factory.setWaitTime(0);
		factory.setRowCount(1);
		factory.setIncrementRows(false);
		
		String sql = "DELETE FROM BQT1.SmallA";
		TranslationUtility util = FakeTranslationFactory.getInstance().getBQTTranslationUtility();
		Object[][] expectedResults = new Object[][] {new Object[] { new Integer(0) }};
		
		Command command = util.parseCommand(sql);
		RuntimeMetadata metadata = util.createRuntimeMetadata();
		
		Execution exec = factory.createExecution(command, null, metadata, null);
		exec.execute();
		
		List results = readResultsFromExecution(exec);
		exec.close();
		
		assertEquals(results.size(), expectedResults.length);
		
		compareResults(expectedResults, results);
	}
	
	private void compareResults(Object[][] expectedResults, List results) {

		for(int r=0; r<expectedResults.length; r++) {
			Object[] expectedRow = expectedResults[r];
			List actualRow = (List) results.get(r);
			for (int c = 0; c < expectedRow.length; c++) {
				Object expectedValue = expectedRow[c];
				Object actualValue = actualRow.get(c);
				if (expectedValue == null) {
					if (actualValue != null) {
						Assert.fail("Row " + r + ", Col " + c + ": Expected null but got " + actualValue + " of type " + actualValue.getClass().getName()); 
					}
				} else if(actualValue == null) {
					Assert.fail("Row " + r + ", Col " + c + ": Expected " + expectedValue + " but got null"); 
				} else {
					Assert.assertEquals("Row " + r + ", Col " + c + ": Expected " + expectedValue + " but got " + actualValue, expectedValue, actualValue); 
				}
			}
		}
	}

	
	
	private List<List> readResultsFromExecution(Execution execution) throws TranslatorException {
    	List<List> results = new ArrayList<List>();
    	while (true) {
	    	try {
		    	if (execution instanceof ResultSetExecution) {
		    		ResultSetExecution rs = (ResultSetExecution)execution;
		    		List result = null;
		    		while ((result = rs.next()) != null) {
		    			results.add(result);
		    		}
		    		break;
		    	} 
		    	UpdateExecution rs = (UpdateExecution)execution;
	    		int[] result = rs.getUpdateCounts();
	    		for (int i = 0; i < result.length; i++) {
	    			results.add(Arrays.asList(result[i]));
	    		}
	    		break;
	    	} catch (DataNotAvailableException e) {
	    		if (e.getRetryDelay() > 0) {
		    		try {
						Thread.sleep(e.getRetryDelay());
					} catch (InterruptedException e1) {
						throw new TranslatorException(e1);
					}
	    		}
	    	}
    	}
    	return results;
    }

}
