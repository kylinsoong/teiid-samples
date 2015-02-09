package org.something;

public class TempConv {
	
	public static String sayHello(String name) {
		return "Hello " + name ;
	}

	public static Double celsiusToFahrenheit(Double doubleCelsiusTemp) {
		if (doubleCelsiusTemp == null) {
			return null;
		}
		return (doubleCelsiusTemp) * 9 / 5 + 32;
	}

}
