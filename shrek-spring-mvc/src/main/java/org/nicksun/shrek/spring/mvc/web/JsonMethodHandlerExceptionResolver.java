package org.nicksun.shrek.spring.mvc.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.nicksun.shrek.spring.mvc.web.annotation.ResponseJson;
import org.nicksun.shrek.spring.mvc.web.http.HttpContentEncodingHandler;
import org.nicksun.shrek.spring.mvc.web.http.MessageProtocol;
import org.nicksun.shrek.spring.mvc.web.protocol.MessageProtocolProcessor;
import org.nicksun.shrek.spring.mvc.web.returnvalue.ExceptionBeanWrapper;
import org.nicksun.shrek.spring.mvc.web.returnvalue.impl.FailDataBeanWrapper;
import org.nicksun.shrek.utils.ObjectMapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author nicksun
 *
 */
public class JsonMethodHandlerExceptionResolver extends AbstractHandlerExceptionResolver implements InitializingBean {

	private static final Logger LOG = LoggerFactory.getLogger(JsonMethodHandlerExceptionResolver.class);

	private HttpMessageConverter<byte[]> messageConverter;

	private ObjectMapper objectMapper;

	private List<MessageProtocolProcessor> protocolProcessors;

	private List<HttpContentEncodingHandler> httpContentEncodingHandlers;

	private MessageProtocol defaultProtocol = MessageProtocol.TEXT;

	private boolean enableHttpContentEncoding = false;
	
	private List<ExceptionBeanWrapper> exceptionBeanWrappers;

	public JsonMethodHandlerExceptionResolver() {
		this.objectMapper = ObjectMapperFactory.getDefaultObjectMapper();
	}

	public HttpMessageConverter<byte[]> getMessageConverter() {
		return messageConverter;
	}

	public void setMessageConverter(HttpMessageConverter<byte[]> messageConverter) {
		this.messageConverter = messageConverter;
	}

	public void setProtocolProcessors(List<MessageProtocolProcessor> protocolProcessors) {
		this.protocolProcessors = protocolProcessors;
	}

	public void setDefaultProtocol(MessageProtocol defaultProtocol) {
		this.defaultProtocol = defaultProtocol;
	}

	public void setHttpContentEncodingHandlers(List<HttpContentEncodingHandler> httpContentEncodingHandlers) {
		this.httpContentEncodingHandlers = httpContentEncodingHandlers;
	}

	public void setEnableHttpContentEncoding(boolean enableHttpContentEncoding) {
		this.enableHttpContentEncoding = enableHttpContentEncoding;
	}

	public void setExceptionBeanWrappers(List<ExceptionBeanWrapper> exceptionBeanWrappers) {
		this.exceptionBeanWrappers = exceptionBeanWrappers;
	}

	protected boolean support(Object handler) {
		if (handler instanceof HandlerMethod) {
			return ((HandlerMethod) handler).getMethod().getAnnotation(ResponseJson.class) != null;
		}
		return false;
	}

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		if (!support(handler)) {
			return null;
		}
		Object result = null;
		for (ExceptionBeanWrapper beanWrapper : exceptionBeanWrappers) {
			if (beanWrapper.supportsType(ex.getClass())) {
				result = beanWrapper.wrap(ex);
				break;
			}
		}
		try {
			MessageProtocol protocol = defaultProtocol;
			if (handler instanceof HandlerMethod) {
				HandlerMethod hm = (HandlerMethod) handler;
				ResponseJson responseJson = hm.getMethodAnnotation(ResponseJson.class);
				protocol = responseJson.protocol() == MessageProtocol.DEFAULT ? defaultProtocol
						: responseJson.protocol();
			}
			// write byte[]
			byte[] bytes = objectMapper.writeValueAsBytes(result);
			// 获取请求的 contentEncodings
			String[] contentEncodings = StringUtils.toStringArray(request.getHeaders(HttpHeaders.CONTENT_ENCODING));
			if (ArrayUtils.isNotEmpty(contentEncodings)) {
				for (String c : contentEncodings) {
					for (HttpContentEncodingHandler h : httpContentEncodingHandlers) {
						if (h.supports(c)) {
							h.compress(bytes);
						}
					}
				}
			}
			// 处理协议
			for (MessageProtocolProcessor p : protocolProcessors) {
				if (p.support(protocol)) {
					bytes = p.encoding(bytes);
				}
			}
			messageConverter.write(bytes, MediaType.APPLICATION_JSON, new ServletServerHttpResponse(response));
		} catch (HttpMessageNotWritableException e) {
			LOG.error(e.getMessage(), e);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
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
		if (exceptionBeanWrappers == null) {
			exceptionBeanWrappers = new ArrayList<>();
			exceptionBeanWrappers.add(new FailDataBeanWrapper());
		}
	}

}