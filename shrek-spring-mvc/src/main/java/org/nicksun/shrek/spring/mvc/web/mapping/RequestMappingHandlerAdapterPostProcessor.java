package org.nicksun.shrek.spring.mvc.web.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.nicksun.shrek.spring.mvc.web.JsonMethodProcessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

/**
 * @author nicksun
 *
 */
public class RequestMappingHandlerAdapterPostProcessor implements BeanPostProcessor, InitializingBean {

	private JsonMethodProcessor jsonMethodProcessor;

	public void setJsonMethodProcessor(JsonMethodProcessor jsonMethodProcessor) {
		this.jsonMethodProcessor = jsonMethodProcessor;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof RequestMappingHandlerAdapter) {
			RequestMappingHandlerAdapter adp = (RequestMappingHandlerAdapter) bean;
			List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>(adp.getArgumentResolvers());
			int requestResponseBodyArgumentResolverIndex = 0;
			for (int i = 0; i < argumentResolvers.size(); i++) {
				HandlerMethodArgumentResolver a = argumentResolvers.get(0);
				if (a instanceof RequestResponseBodyMethodProcessor) {
					requestResponseBodyArgumentResolverIndex = i;
				}
			}
			argumentResolvers.add(requestResponseBodyArgumentResolverIndex, jsonMethodProcessor);
			adp.setArgumentResolvers(argumentResolvers);
			List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<>(adp.getReturnValueHandlers());
			int requestResponseBodyReturnValueHandlerIndex = 0;
			for (int i = 0; i < returnValueHandlers.size(); i++) {
				HandlerMethodReturnValueHandler r = returnValueHandlers.get(i);
				if (r instanceof RequestResponseBodyMethodProcessor) {
					requestResponseBodyReturnValueHandlerIndex = i;
				}
			}
			returnValueHandlers.add(requestResponseBodyReturnValueHandlerIndex, jsonMethodProcessor);
			adp.setReturnValueHandlers(returnValueHandlers);
		}
		return bean;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (Objects.isNull(jsonMethodProcessor)) {
			throw new Exception("jsonMethodProcessor undefined");
		}
	}

}
