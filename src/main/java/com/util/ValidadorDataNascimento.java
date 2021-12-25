package main.java.com.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidadorDataNascimento implements Validador<String> {

	@Override
	public boolean test(String str) {
		Pattern p = Pattern.compile("\\d{2}/\\d{2}/\\d{4}");
		Matcher m = p.matcher(str);
		if (m.matches() == false) {
			return false;
		}
		try {
			@SuppressWarnings("unused")
			LocalDate date = LocalDate.parse(str, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		} catch (DateTimeParseException e) {
			return false;
		}
		return true;
	}
}
