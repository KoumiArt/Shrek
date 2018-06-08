package org.nicksun.shrek.validator.util;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author nicksun
 *
 */
public class GroupUtil {

	public static boolean compare(Class<?>[] groups0, Class<?>[] groups1) {
		if (Objects.nonNull(groups0) && groups0.length > 0) {
			boolean noneMatch = Stream.of(groups0).noneMatch(c -> {
				return Stream.of(groups1).noneMatch(e -> {
					return !e.isAssignableFrom(c);
				});
			});
			if (noneMatch) {
				return true;
			}
		}
		return false;
	}
}
