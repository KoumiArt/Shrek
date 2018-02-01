package org.nicksun.shrek.spring.mvc.web;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.nicksun.shrek.spring.mvc.web.annotation.ResponseJson;
import org.nicksun.shrek.spring.mvc.web.http.HttpAcceptEncodingHandler;
import org.nicksun.shrek.spring.mvc.web.http.MessageProtocol;
import org.nicksun.shrek.spring.mvc.web.protocol.MessageProtocolProcessor;
import org.nicksun.shrek.spring.mvc.web.returnvalue.ErrorCode;
import org.nicksun.shrek.spring.mvc.web.returnvalue.impl.FailData;
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

	private List<HttpAcceptEncodingHandler> httpAcceptEncodingHandlers;

	private MessageProtocol defaultProtocol = MessageProtocol.TEXT;

	private boolean enableHttpAcceptEncoding = false;

	public void setEnableHttpAcceptEncoding(boolean enableHttpAcceptEncoding) {
		this.enableHttpAcceptEncoding = enableHttpAcceptEncoding;
	}

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

	public void setHttpAcceptEncodingHandlers(List<HttpAcceptEncodingHandler> httpAcceptEncodingHandlers) {
		this.httpAcceptEncodingHandlers = httpAcceptEncodingHandlers;
	}

	public void setDefaultProtocol(MessageProtocol defaultProtocol) {
		this.defaultProtocol = defaultProtocol;
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
		FailData failData = new FailData();
		if (ex instanceof ErrorCode) {
			failData.setErrorCode(((ErrorCode) ex).getErrorCode());
		}
		String stackTrace = ExceptionUtils.getStackTrace(ex);
		failData.setData(stackTrace);
		failData.setMsg(ex.getMessage() == null ? "系统错误" : ex.getMessage());
		try {
			MessageProtocol protocol = defaultProtocol;
			if (handler instanceof HandlerMethod) {
				HandlerMethod hm = (HandlerMethod) handler;
				ResponseJson responseJson = hm.getMethodAnnotation(ResponseJson.class);
				protocol = responseJson.protocol() == MessageProtocol.DEFAULT ? defaultProtocol
						: responseJson.protocol();
			}
			// write byte[]
			byte[] bytes = objectMapper.writeValueAsBytes(failData);
			// 获取请求的 acceptEncodings
			String[] acceptEncodings = StringUtils.toStringArray(request.getHeaders(HttpHeaders.ACCEPT_ENCODING));
			if (ArrayUtils.isNotEmpty(acceptEncodings)) {
				if (CollectionUtils.isEmpty(httpAcceptEncodingHandlers)) {
					throw new Exception("httpAcceptEncodingHandlers undefined");
				}
				for (String a : acceptEncodings) {
					for (HttpAcceptEncodingHandler h : httpAcceptEncodingHandlers) {
						if (h.supports(a)) {
							h.encoding(bytes);
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
		if (enableHttpAcceptEncoding) {
			if (CollectionUtils.isEmpty(httpAcceptEncodingHandlers)) {
				throw new Exception("httpAcceptEncodingHandlers undefined");
			}
		}
	}

}