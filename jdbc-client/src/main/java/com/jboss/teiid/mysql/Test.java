package com.jboss.teiid.mysql;

import java.util.Date;
import java.util.Random;



public class Test {

	public static void main(String[] args) {
		System.out.println(new Date());
	}

	private static long caculation() {
		int value = new Random().nextInt(10000);
		System.out.println("  " + value);
		return value;
	}

}
