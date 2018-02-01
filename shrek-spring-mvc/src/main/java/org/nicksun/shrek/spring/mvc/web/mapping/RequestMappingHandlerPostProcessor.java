package org.nicksun.shrek.spring.mvc.web.mapping;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * 从RequestMappingHandlerAdapter移除指定的ArgumentResolvers与ReturnValueHandlers
 * 
 * @author nicksun
 *
 */
public class RequestMappingHandlerPostProcessor implements BeanPostProcessor {

	private List<Class<? extends HandlerMethodArgumentResolver>> removedArgumentResolvers = new ArrayList<>();

	private List<Class<? extends HandlerMethodReturnValueHandler>> removedReturnValueHandlers = new ArrayList<>();

	public List<Class<? extends HandlerMethodArgumentResolver>> getRemovedArgumentResolvers() {
		return removedArgumentResolvers;
	}

	public void setRemovedArgumentResolvers(
			List<Class<? extends HandlerMethodArgumentResolver>> removedArgumentResolvers) {
		this.removedArgumentResolvers = removedArgumentResolvers;
	}

	public List<Class<? extends HandlerMethodReturnValueHandler>> getRemovedReturnValueHandlers() {
		return removedReturnValueHandlers;
	}

	public void setRemovedReturnValueHandlers(
			List<Class<? extends HandlerMethodReturnValueHandler>> removedReturnValueHandlers) {
		this.removedReturnValueHandlers = removedReturnValueHandlers;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof RequestMappingHandlerAdapter) {
			RequestMappingHandlerAdapter adapter = (RequestMappingHandlerAdapter) bean;
			reomveArgumentResolvers(adapter.getArgumentResolvers(), adapter);
			reomveReturnValueHandlers(adapter.getReturnValueHandlers(), adapter);
		}
		return bean;
	}

	private void reomveReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers,
			RequestMappingHandlerAdapter adapter) {
		if (!CollectionUtils.isEmpty(removedReturnValueHandlers)) {
			List<HandlerMethodReturnValueHandler> handlerMethodReturnValueHandlers = new ArrayList<>();
			for (HandlerMethodReturnValueHandler h : returnValueHandlers) {
				if (removedReturnValueHandlers.contains(h.getClass())) {
					continue;
				}
				handlerMethodReturnValueHandlers.add(h);
			}
			adapter.setReturnValueHandlers(handlerMethodReturnValueHandlers);
		}
	}

	private void reomveArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers,
			RequestMappingHandlerAdapter adapter) {
		if (!CollectionUtils.isEmpty(removedArgumentResolvers)) {
			List<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers = new ArrayList<>();
			for (HandlerMethodArgumentResolver h : argumentResolvers) {
				if (removedArgumentResolvers.contains(h.getClass())) {
					continue;
				}
				handlerMethodArgumentResolvers.add(h);
			}
			adapter.setArgumentResolvers(handlerMethodArgumentResolvers);
		}
	}

}
