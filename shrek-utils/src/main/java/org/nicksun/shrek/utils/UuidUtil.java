package org.nicksun.shrek.utils;

import java.util.UUID;

/**
 * @author nicksun
 *
 */
public class UuidUtil {

	private static final ThreadLocal<String> UUID_THREADLOCAL = new ThreadLocal<>();

	private UuidUtil() {
	}

	public static String getUuid() {
		return UUID_THREADLOCAL.get();
	}

	public static void remove() {
		UUID_THREADLOCAL.remove();
	}

	public static void randomUuid() {
		randomUuid("");
	}

	public static void randomUuid(String title) {
		UUID_THREADLOCAL.set(title + "[" + UUID.randomUUID().toString() + "]");
	}
}
