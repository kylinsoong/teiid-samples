package org.teiid.test.bufferservice;

import java.util.ArrayList;
import java.util.List;

import org.teiid.common.buffer.TupleBatch;

public class TupleBatchTest {

	public static void main(String[] args) {

		List<List<String>> rows = new ArrayList<List<String>>();
		for(int i = 0 ; i < 10 ; i++ ){
			List<String> row = new ArrayList<String>();
			for(int j = 0 ; j < 2 ; j ++) {
				row.add("data-" + i + "-"+ j);
			}
			rows.add(row);
		}
		
		TupleBatch batch = new TupleBatch(0, rows);
		
		System.out.println(batch);
		System.out.println(batch.getTerminationFlag());
		System.out.println(batch.getBeginRow());
		System.out.println(batch.getEndRow());
		System.out.println(batch.getRowCount());
		System.out.println(batch.getAllTuples());
		System.out.println(batch.containsRow(0));
		System.out.println(batch.containsRow(9));
		System.out.println(batch.containsRow(10));
		System.out.println(batch.getTermination());
		System.out.println(batch.getTuple(0));
		System.out.println(batch.getTuple(1));
		System.out.println(batch.getTuple(2));
		System.out.println(batch.getTuple(9));
		System.out.println(batch.getTuples());
		
	}

}
