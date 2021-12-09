package main.java.com.util;

import java.util.regex.Pattern;

import main.java.com.view.ValidadorString;

public class ValidadorNome implements ValidadorString {

	@Override
	public boolean test(String str) {
		if ((str.isBlank() == true) || (str.isEmpty() == true) || 
				(Pattern.matches("^[^A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ'\\\\s]+$", str) == true)) {
			return false;
		}
		return true;
	}
}
