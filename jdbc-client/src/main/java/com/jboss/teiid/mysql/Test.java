package com.jboss.teiid.mysql;

import java.util.Random;



public class Test {

	public static void main(String[] args) {
		long sum = 0;
		for(int i = 0 ; i < 10 ; i ++) {
			sum += caculation();
		}
		
		System.out.println(sum + " " + sum/10);
	}

	private static long caculation() {
		int value = new Random().nextInt(10000);
		System.out.println("  " + value);
		return value;
	}

}
