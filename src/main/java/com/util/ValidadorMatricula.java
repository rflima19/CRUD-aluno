package main.java.com.util;

public class ValidadorMatricula implements Validador<Integer> {

	@Override
	public boolean test(Integer num) {
		if (((num < 1) == true) || ((num > Integer.MAX_VALUE) == true)) {
			return false;
		}
		return true;
	}
}
