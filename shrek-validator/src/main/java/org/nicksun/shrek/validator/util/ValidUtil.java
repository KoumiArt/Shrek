package org.nicksun.shrek.validator.util;

import java.util.Objects;

import org.nicksun.shrek.validator.ValidException;

public class ValidUtil {

	private ValidUtil() {
	}

	public static void throwValidException(final String checkValue, final String fileName, final String msg)
			throws ValidException {
		String excMsg = checkValue;
		if (Objects.isNull(checkValue) || checkValue.trim().length() <= 0) {
			excMsg = Objects.isNull(fileName) ? msg : fileName + msg;
		}
		throwValidException(excMsg);
	}

	public static void throwValidException(final String checkValue, String msg) throws ValidException {
		throwValidException(checkValue, null, msg);
	}

	public static void throwValidException(String msg) throws ValidException {
		throw new ValidException(msg);
	}
}
