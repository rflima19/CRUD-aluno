package main.java.com.util;

import main.java.com.view.ValidadorInteiro;

public class ValidadorMatricula implements ValidadorInteiro {

	@Override
	public boolean test(Integer num) {
		if (((num < 1) == true) || ((num > Integer.MAX_VALUE) == true)) {
			return false;
		}
		return true;
	}
}
