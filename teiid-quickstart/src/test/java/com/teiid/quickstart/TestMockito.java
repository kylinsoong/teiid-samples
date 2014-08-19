package com.teiid.quickstart;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

public class TestMockito {

	@Test
	public void testVerify() {
		
		@SuppressWarnings("unchecked")
		List<String> mockedList = Mockito.mock(List.class);
		
		mockedList.add("one");
		mockedList.clear();
		
		Mockito.verify(mockedList).add("one");
		Mockito.verify(mockedList).clear();
		
		Mockito.stub(mockedList.size()).toReturn(100);
		assertEquals(100, mockedList.size());
		
	}
}
