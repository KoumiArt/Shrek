package org.nicksun.shrek.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

/**
 * @author nicksun
 *
 */
public class LocalDateUtil {

	public static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

	public static LocalDateTime parseLocalDateTime(CharSequence text) {
		if (StringUtils.isNotEmpty(text)) {
			return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN));
		}
		return null;
	}

	public static LocalDateTime parseLocalDateTime(CharSequence text, String pattern) {
		if (StringUtils.isNotEmpty(text) && StringUtils.isNotEmpty(pattern)) {
			return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(pattern));
		}
		return null;
	}

	public static LocalDate parseLocalDate(CharSequence text) {
		if (StringUtils.isNotEmpty(text)) {
			return LocalDate.parse(text, DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN));
		}
		return null;
	}

	public static LocalDate parseLocalDate(CharSequence text, String pattern) {
		if (StringUtils.isNotEmpty(text) && StringUtils.isNotEmpty(pattern)) {
			return LocalDate.parse(text, DateTimeFormatter.ofPattern(pattern));
		}
		return null;
	}

	public static String formatLocalDateTime(LocalDateTime localDateTime) {
		if (Objects.nonNull(localDateTime)) {
			return localDateTime.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN));
		}
		return null;
	}

	public static String formatLocalDateTime(LocalDateTime localDateTime, String pattern) {
		if (Objects.nonNull(localDateTime) && StringUtils.isNotEmpty(pattern)) {
			return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
		}
		return null;
	}

	public static String formatLocalDate(LocalDate localDate) {
		if (Objects.nonNull(localDate)) {
			return localDate.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN));
		}
		return null;
	}

	public static LocalDate toLocalDate(Date date) {
		if (Objects.nonNull(date)) {
			return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		}
		return null;
	}

	public static LocalDateTime toLocalDateTime(Date date) {
		if (Objects.nonNull(date)) {
			return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		}
		return null;
	}

	public static Date toDate(LocalDateTime date) {
		if (Objects.nonNull(date)) {
			return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
		}
		return null;
	}

	public static Date toDate(LocalDate date) {
		if (Objects.isNull(date)){
			return null;
		}
		return toDate(date.atStartOfDay());
	}
}
