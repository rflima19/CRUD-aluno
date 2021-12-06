package main.java.com.util;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Console {

	public static final Scanner INPUT_CONSOLE = new Scanner(System.in);
	
	public static Integer readInteger() {
		String str = Console.INPUT_CONSOLE.nextLine();
		Integer i = Integer.parseInt(str);
		return i;
	}
	
	public static Double readDouble() {
		String str = Console.INPUT_CONSOLE.nextLine();
		Double d = Double.parseDouble(str);
		return d;
	}
	
	public static String readString() {
		String str = Console.INPUT_CONSOLE.nextLine();
		return str;
	}
	
	public static String readString(String regex) {
		Pattern pattern = Pattern.compile(regex);
		String str = Console.INPUT_CONSOLE.nextLine();
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches() == false) {
			return null;
		}
		return str;
	}
}
