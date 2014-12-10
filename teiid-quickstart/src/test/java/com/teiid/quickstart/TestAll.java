package com.teiid.quickstart;

public class TestAll {

	public static void main(String[] args) {
		String test = "customer:city";

		String[] array = test.split(":");

		for (String str : array)
			System.out.println(str);
	}

}
