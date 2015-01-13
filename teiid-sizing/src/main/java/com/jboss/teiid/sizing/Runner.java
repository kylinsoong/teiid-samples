package com.jboss.teiid.sizing;

public class Runner {

	public static void main(String[] args) {
		
		testHeapCacualtion1();
		
		testHeapCacualtion2();
		
		testCoreSizeCaculation1();
		
		testCoreSizeCaculation2();
		
		testCoreSizeCaculation3();
	}
	
	static void testHeapCacualtion1() {
		CaculationEntity entity = new CaculationEntity(2, 500, 100, 10000, 100, 1000, 10000, 1000, 2000, false);
		CaculationTool tool = new CaculationTool(entity);
		System.out.println(tool.heapCaculation());
	}
	
	static void testHeapCacualtion2() {
		CaculationEntity entity = new CaculationEntity(10, 1000, 100, 10000, 100, 1000, 10000, 1000, 2000, false);
		CaculationTool tool = new CaculationTool(entity);
		System.out.println(tool.heapCaculation());
	}
	
	static void testCoreSizeCaculation1() {
		CaculationEntity entity = new CaculationEntity(2, 500, 100, 10000, 100, 1000, 10000, 1000, 2000, false);
		CaculationTool tool = new CaculationTool(entity);
		System.out.println(tool.coreCaculation());
	}
	
	static void testCoreSizeCaculation2() {
		CaculationEntity entity = new CaculationEntity(2, 500, 100, 10000, 100, 500, 10000, 1000, 2000, false);
		CaculationTool tool = new CaculationTool(entity);
		System.out.println(tool.coreCaculation());
	}
	
	static void testCoreSizeCaculation3() {
		CaculationEntity entity = new CaculationEntity(2, 500, 500, 10000, 100, 500, 10000, 1000, 2000, false);
		CaculationTool tool = new CaculationTool(entity);
		System.out.println(tool.coreCaculation());
	}

}
