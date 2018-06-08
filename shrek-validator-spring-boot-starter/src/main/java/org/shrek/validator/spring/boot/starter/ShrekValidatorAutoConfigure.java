package org.shrek.validator.spring.boot.starter;

import java.util.Arrays;
import java.util.List;

import org.nicksun.shrek.spring.mvc.web.JsonMethodHandlerExceptionResolver;
import org.nicksun.shrek.spring.mvc.web.JsonMethodHttpMessageConverter;
import org.nicksun.shrek.spring.mvc.web.JsonMethodProcessor;
import org.nicksun.shrek.spring.mvc.web.http.HttpContentEncodingHandler;
import org.nicksun.shrek.spring.mvc.web.http.MessageProtocol;
import org.nicksun.shrek.spring.mvc.web.http.impl.GzipHttpContentEncodingHandler;
import org.nicksun.shrek.spring.mvc.web.mapping.RequestMappingHandlerAdapterPostProcessor;
import org.nicksun.shrek.spring.mvc.web.protocol.MessageProtocolProcessor;
import org.nicksun.shrek.spring.mvc.web.protocol.impl.Base64MessageProtocolProcessor;
import org.nicksun.shrek.spring.mvc.web.protocol.impl.DefaultMessageProtocolProcessor;
import org.nicksun.shrek.spring.mvc.web.returnvalue.BeanWrapper;
import org.nicksun.shrek.spring.mvc.web.returnvalue.impl.DefaultBeanWrapper;
import org.nicksun.shrek.validator.DefaultValidatorArgumentResolver;
import org.nicksun.shrek.validator.DefaultValidatorHandlerResolver;
import org.nicksun.shrek.validator.JsonMethodSupportVaildatorProcessor;
import org.nicksun.shrek.validator.ValidatorArgumentResolver;
import org.nicksun.shrek.validator.ValidatorHandlerResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

/**
 * @author nicksun
 *
 */
@Configuration
public class ShrekValidatorAutoConfigure {
	
	/**
	 * @return
	 */
	@Bean
	public RequestMappingHandlerAdapterPostProcessor requestMappingHandlerAdapterPostProcessor() {
		RequestMappingHandlerAdapterPostProcessor processor = new RequestMappingHandlerAdapterPostProcessor();
		processor.setJsonMethodProcessor(this.jsonMethodProcessor());
		return processor;
	}

	/**
	 * @return
	 */
	@Bean
	public JsonMethodProcessor jsonMethodProcessor() {
		JsonMethodSupportVaildatorProcessor jsonMethodProcessor = new JsonMethodSupportVaildatorProcessor();
		jsonMethodProcessor.setBeanWrappers(this.beanWrappers());
		jsonMethodProcessor.setDefaultProtocol(this.defaultProtocol());
		jsonMethodProcessor.setEnableHttpContentEncoding(this.enableHttpContentEncoding());
		jsonMethodProcessor.setHttpContentEncodingHandlers(this.httpContentEncodingHandlers());
		jsonMethodProcessor.setMessageConverter(this.messageConverter());
		jsonMethodProcessor.setProtocolProcessors(this.protocolProcessors());
		jsonMethodProcessor.setValidatorArgumentResolver(this.validatorArgumentResolver());
		return jsonMethodProcessor;
	}

	/**
	 * @return
	 */
	@Bean
	public ValidatorArgumentResolver validatorArgumentResolver() {
		DefaultValidatorArgumentResolver argumentResolver = new DefaultValidatorArgumentResolver();
		argumentResolver.setValidatorHandlerManager(this.validatorHandlerManager());
		return argumentResolver;
	}

	/**
	 * @return
	 */
	@Bean
	public ValidatorHandlerResolver validatorHandlerManager() {
		return new DefaultValidatorHandlerResolver();
	}

	/**
	 * @return
	 */
	@Bean
	public List<MessageProtocolProcessor> protocolProcessors() {
		return Arrays.asList(new DefaultMessageProtocolProcessor(), new Base64MessageProtocolProcessor());
	}

	/**
	 * @return
	 */
	@Bean
	public List<HttpContentEncodingHandler> httpContentEncodingHandlers() {
		return Arrays.asList(new GzipHttpContentEncodingHandler());
	}

	/**
	 * @return
	 */
	@Bean
	public HttpMessageConverter<byte[]> messageConverter() {
		return new JsonMethodHttpMessageConverter();
	}

	/**
	 * @return
	 */
	@Bean
	public boolean enableHttpContentEncoding() {
		return true;
	}

	/**
	 * @return
	 */
	@Bean
	public MessageProtocol defaultProtocol() {
		return MessageProtocol.TEXT;
	}

	/**
	 * @return
	 */
	@Bean
	public List<BeanWrapper> beanWrappers() {
		return Arrays.asList(new DefaultBeanWrapper());
	}

	/**
	 * @return
	 */
	@Bean
	public JsonMethodHandlerExceptionResolver exceptionResolver() {
		JsonMethodHandlerExceptionResolver jsonMethodHandlerExceptionResolver = new JsonMethodHandlerExceptionResolver();
		jsonMethodHandlerExceptionResolver.setDefaultProtocol(this.defaultProtocol());
		jsonMethodHandlerExceptionResolver.setEnableHttpContentEncoding(this.enableHttpContentEncoding());
		jsonMethodHandlerExceptionResolver.setHttpContentEncodingHandlers(this.httpContentEncodingHandlers());
		jsonMethodHandlerExceptionResolver.setMessageConverter(this.messageConverter());
		jsonMethodHandlerExceptionResolver.setProtocolProcessors(this.protocolProcessors());
		return jsonMethodHandlerExceptionResolver;
	}
}
