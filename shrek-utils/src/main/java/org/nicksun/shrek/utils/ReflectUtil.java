package org.nicksun.shrek.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ReflectUtil {

	private ReflectUtil() {
	}

	public static Field[] getDeclaredFields(Class<? extends Object> clzss) {
		List<Field> field = new ArrayList<>();
		if (Objects.nonNull(clzss.getGenericSuperclass())) {
			Field[] fields = getDeclaredFields(clzss.getSuperclass());
			field.addAll(Arrays.asList(fields));
		}
		field.addAll(Arrays.asList(clzss.getDeclaredFields()));
		Field[] f = new Field[field.size()];
		return field.toArray(f);
	}

}
