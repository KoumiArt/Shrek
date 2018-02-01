package org.nicksun.shrek.utils;

import java.util.UUID;

/**
 * @author nicksun
 *
 */
public class UuidUtil {

	private static final ThreadLocal<String> uuidThreadLocal = new ThreadLocal<>();

	private UuidUtil() {
	}

	public static String getUuid() {
		return uuidThreadLocal.get();
	}

	public static void randomUuid() {
        randomUuid("");
	}

	public static void randomUuid(String title) {
		uuidThreadLocal.set(title +  "[" + UUID.randomUUID().toString() + "]");
	}
}
