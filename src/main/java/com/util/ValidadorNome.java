package main.java.com.util;

import java.util.regex.Pattern;

public class ValidadorNome implements Validador<String> {

	@Override
	public boolean test(String str) {
		if ((str.isBlank() == true) || (str.isEmpty() == true) || 
				(Pattern.matches("^.*[^A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ'\\s]+.*$", str) == true)) {
			return false;
		}
		return true;
	}
}
