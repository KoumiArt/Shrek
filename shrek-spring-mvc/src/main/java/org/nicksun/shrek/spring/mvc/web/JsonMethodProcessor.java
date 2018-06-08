package org.nicksun.shrek.spring.mvc.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.nicksun.shrek.spring.mvc.web.annotation.RequestJson;
import org.nicksun.shrek.spring.mvc.web.annotation.ResponseJson;
import org.nicksun.shrek.spring.mvc.web.annotation.ResponseJson.Location;
import org.nicksun.shrek.spring.mvc.web.http.HttpContentEncodingHandler;
import org.nicksun.shrek.spring.mvc.web.http.MessageProtocol;
import org.nicksun.shrek.spring.mvc.web.protocol.MessageProtocolProcessor;
import org.nicksun.shrek.spring.mvc.web.returnvalue.BeanWrapper;
import org.nicksun.shrek.spring.mvc.web.returnvalue.impl.SuccessData;
import org.nicksun.shrek.utils.ObjectMapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

/**
 * @author nicksun
 *
 */
public class JsonMethodProcessor
		implements HandlerMethodReturnValueHandler, HandlerMethodArgumentResolver, InitializingBean {

	private static final String DELETE = "DELETE";

	private static final String GET = "GET";

	private static final int NUM_1 = 1;

	private static final int NUM_2 = 2;

	private static final Logger LOG = LoggerFactory.getLogger(JsonMethodProcessor.class);

	private static final String PATH_DELIMITER = "/";

	private HttpMessageConverter<byte[]> messageConverter;

	private List<BeanWrapper> beanWrappers;

	private ObjectMapper objectMapper;

	private MessageProtocol defaultProtocol = MessageProtocol.TEXT;

	private List<MessageProtocolProcessor> protocolProcessors;

	private List<HttpContentEncodingHandler> httpContentEncodingHandlers;

	private boolean enableHttpContentEncoding = false;

	public void setHttpContentEncodingHandlers(List<HttpContentEncodingHandler> httpContentEncodingHandlers) {
		this.httpContentEncodingHandlers = httpContentEncodingHandlers;
	}

	public void setEnableHttpContentEncoding(boolean enableHttpContentEncoding) {
		this.enableHttpContentEncoding = enableHttpContentEncoding;
	}

	public void setDefaultProtocol(MessageProtocol defaultProtocol) {
		this.defaultProtocol = defaultProtocol;
	}

	public void setProtocolProcessors(List<MessageProtocolProcessor> protocolProcessors) {
		this.protocolProcessors = protocolProcessors;
	}

	public JsonMethodProcessor() {
		this.objectMapper = ObjectMapperFactory.getDefaultObjectMapper();
	}

	public void setMessageConverter(HttpMessageConverter<byte[]> messageConverter) {
		this.messageConverter = messageConverter;
	}

	public List<BeanWrapper> getBeanWrappers() {
		return beanWrappers;
	}

	public void setBeanWrappers(List<BeanWrapper> beanWrappers) {
		this.beanWrappers = beanWrappers;
	}

	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
		return methodParameter.hasParameterAnnotation(RequestJson.class);
	}

	@Override
	public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		try {
			String params = getRequestParams(webRequest);
			if (StringUtils.isEmpty(params)) {
				return null;
			}
			byte[] bytes = params.getBytes();
			if (enableHttpContentEncoding) {
				// http header content-encoding
				String[] contentEncodings = webRequest.getHeaderValues(HttpHeaders.CONTENT_ENCODING);
				if (ArrayUtils.isNotEmpty(contentEncodings)) {
					for (String c : contentEncodings) {
						for (HttpContentEncodingHandler h : httpContentEncodingHandlers) {
							if (h.supports(c)) {
								h.decompress(bytes);
							}
						}
					}
				}
			}
			// RequestJson
			RequestJson requestJson = methodParameter.getParameterAnnotation(RequestJson.class);
			MessageProtocol protocol = requestJson.protocol() == MessageProtocol.DEFAULT ? defaultProtocol
					: requestJson.protocol();
			for (MessageProtocolProcessor p : protocolProcessors) {
				if (p.support(protocol)) {
					bytes = p.decoding(bytes);
				}
			}
			String path = requestJson.path();
			JsonNode node = objectMapper.readTree(bytes);
			if (StringUtils.isEmpty(path)) {
				String parameterName = methodParameter.getParameterName();
				ObjectReader reader = objectMapper.readerFor(getReferenceType(methodParameter));
				if (node.has(parameterName)) {
					return reader.readValue(node.path(parameterName));
				}
				return reader.readValue(node);
			} else {
				String[] paths = StringUtils.split(path, PATH_DELIMITER);
				for (String p : paths) {
					node = node.path(p);
				}
				if (Objects.isNull(node)) {
					return null;
				}
				ObjectReader reader = objectMapper.readerFor(getReferenceType(methodParameter));
				return reader.readValue(node);
			}
		} catch (Exception e) {
			LOG.error("can't generate param [" + methodParameter.getParameterName() + "] for url "
					+ webRequest.getNativeRequest(HttpServletRequest.class).getServletPath() + ", and source input is "
					+ getRequestParams(webRequest));
			throw new Exception("请求参数格式错误", e);
		}
	}

	private JavaType getReferenceType(MethodParameter parameter) {
		Type type = parameter.getGenericParameterType();
		return getReferenceType(type);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JavaType getReferenceType(Type type) {
		if (type instanceof ParameterizedType) {
			Type[] genericTypes = ((ParameterizedType) type).getActualTypeArguments();
			Class<?> parameterType = (Class<?>) ((ParameterizedType) type).getRawType();
			if (Collection.class.isAssignableFrom(parameterType)) {
				if (genericTypes.length >= NUM_1) {
					return objectMapper.getTypeFactory().constructCollectionType(
							(Class<? extends Collection>) parameterType, getReferenceType(genericTypes[0]));
				}

			} else if (Map.class.isAssignableFrom(parameterType)) {
				if (genericTypes.length >= NUM_2) {
					return objectMapper.getTypeFactory().constructMapType((Class<? extends Map>) parameterType,
							getReferenceType(genericTypes[0]), getReferenceType(genericTypes[1]));
				} else if (genericTypes.length == 1) {
					return objectMapper.getTypeFactory().constructMapType((Class<? extends Map>) parameterType,
							getReferenceType(genericTypes[0]), getReferenceType(Object.class));
				} else {
					return objectMapper.getTypeFactory().constructMapType((Class<? extends Map>) parameterType,
							Object.class, Object.class);
				}
			}
		}
		return objectMapper.getTypeFactory().constructType(type);
	}

	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		return returnType.hasMethodAnnotation(ResponseJson.class);
	}

	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) throws Exception {
		Object result = returnValue;
		mavContainer.setRequestHandled(true);
		ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);
		ResponseJson responseJson = returnType.getMethodAnnotation(ResponseJson.class);
		MessageProtocol protocol = responseJson.protocol() == MessageProtocol.DEFAULT ? defaultProtocol
				: responseJson.protocol();
		if (returnValue == null) {
			result = new SuccessData(new HashMap<String, String>(1));
		} else {
			if (Objects.nonNull(responseJson)) {
				boolean successData = (responseJson.location() == Location.MESSAGE)
						|| ((returnValue instanceof String) && (responseJson.location() == Location.UNDEFINED));
				if (successData) {
					result = new SuccessData(new HashMap<String, String>(1), returnValue);
				} else {
					for (BeanWrapper beanWrapper : beanWrappers) {
						if (beanWrapper.supportsType(returnType)) {
							result = beanWrapper.wrap(returnValue);
							break;
						}
					}
				}
			}
		}

		// write byte[]
		byte[] bytes = objectMapper.writeValueAsBytes(result);
		for (MessageProtocolProcessor p : protocolProcessors) {
			if (p.support(protocol)) {
				bytes = p.encoding(bytes);
			}
		}

		if (enableHttpContentEncoding) {
			// 获取请求的 contentEncodings
			String[] contentEncodings = webRequest.getHeaderValues(HttpHeaders.CONTENT_ENCODING);
			if (ArrayUtils.isNotEmpty(contentEncodings)) {
				for (String c : contentEncodings) {
					for (HttpContentEncodingHandler h : httpContentEncodingHandlers) {
						if (h.supports(c)) {
							h.compress(bytes);
						}
					}
				}
			}
		}
		messageConverter.write(bytes,
				new MediaType(MediaType.APPLICATION_JSON, Collections.singletonMap("charset", "UTF-8")), outputMessage);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (CollectionUtils.isEmpty(beanWrappers)) {
			throw new Exception("beanWrappers undefined");
		}
		if (Objects.isNull(messageConverter)) {
			throw new Exception("messageConverter undefined");
		}
		if (CollectionUtils.isEmpty(protocolProcessors)) {
			throw new Exception("protocolProcessors undefined");
		}
		if (enableHttpContentEncoding) {
			if (CollectionUtils.isEmpty(httpContentEncodingHandlers)) {
				throw new Exception("httpContentEncodingHandlers undefined");
			}
		}
	}

	private ServletServerHttpResponse createOutputMessage(NativeWebRequest webRequest) {
		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
		ServletServerHttpResponse servletServerHttpResponse = new ServletServerHttpResponse(response);
		return servletServerHttpResponse;
	}

	private String getRequestParams(NativeWebRequest webRequest) throws IOException {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		String method = request.getMethod();
		if (GET.equals(method) || DELETE.equals(method)) {
			return request.getQueryString();
		}
		StringBuffer bf = new StringBuffer();
		String line;
		BufferedReader reader = request.getReader();
		while ((line = reader.readLine()) != null) {
			bf.append(line);
		}
		return bf.toString();
	}

}