package org.nicksun.shrek.utils;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author nicksun
 *
 */
public class JackJsonUtil {
	private static final Logger LOG = LoggerFactory.getLogger(JackJsonUtil.class);
	private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	static {
		OBJECT_MAPPER = ObjectMapperFactory.getDefaultObjectMapper();
	}

	public static JsonNode readTree(String content) {
		try {
			return OBJECT_MAPPER.readTree(content);
		} catch (Exception e) {
			LOG.error("JackJsonUtil.readTree error: ", e);
		}
		return null;
	}

	public static ObjectMapper getObjectMapper() {
		return OBJECT_MAPPER;
	}

	public static <T> T convertValue(JsonNode node, TypeReference<T> toValueTypeRef) {
		try {
			return OBJECT_MAPPER.convertValue(node, toValueTypeRef);
		} catch (Exception e) {
			LOG.error("JackJsonUtil.convertValue error: ", e);
		}
		return null;
	}

	public static <T> T convertValue(JsonNode node, Class<T> toValueType) {
		try {
			return OBJECT_MAPPER.convertValue(node, toValueType);
		} catch (Exception e) {
			LOG.error("JackJsonUtil.convertValue error: ", e);
		}
		return null;
	}

	public static String toJson(Object obj) {
		if (Objects.isNull(obj)) {
			return null;
		} else if (obj instanceof String) {
			return (String) obj;
		}
		try {
			return OBJECT_MAPPER.writeValueAsString(obj);
		} catch (Exception e) {
			LOG.error("JackJsonUtil.toJsonString error: ", e);
		}
		return null;
	}

	public static <T> T toBean(String str, Class<T> cls) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		try {
			return OBJECT_MAPPER.readValue(str, cls);
		} catch (Exception e) {
			LOG.error("JackJsonUtil.toObject error: ", e);
		}
		return null;
	}

	public static <T> T toBean(String str, TypeReference<T> typeReference) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		try {
			return OBJECT_MAPPER.readValue(str, typeReference);
		} catch (Exception e) {
			LOG.error("JackJsonUtil.toObject error: ", e);
		}
		return null;
	}

	public static <T> T convertValue(Object o, Class<T> toValueType) {
		try {
			return OBJECT_MAPPER.convertValue(o, toValueType);
		} catch (Exception e) {
			LOG.error("JackJsonUtil.toMap error: ", e);
		}
		return null;
	}

	public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
		try {
			return OBJECT_MAPPER.convertValue(fromValue, toValueTypeRef);
		} catch (Exception e) {
			LOG.error("JackJsonUtil.toMap error: ", e);
		}
		return null;
	}

	public static <T> T convertValue(Object fromValue, JavaType toValueType) {
		try {
			return OBJECT_MAPPER.convertValue(fromValue, toValueType);
		} catch (Exception e) {
			LOG.error("JackJsonUtil.toMap error: ", e);
		}
		return null;
	}

}
