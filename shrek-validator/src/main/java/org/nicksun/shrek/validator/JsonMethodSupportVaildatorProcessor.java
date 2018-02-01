package org.nicksun.shrek.validator;

import java.util.Objects;

import org.nicksun.shrek.spring.mvc.web.JsonMethodProcessor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * support @Vaildator
 * 
 * @author nicksun Override
 *         org.nicksun.shrek.spring.mvc.web.JsonMethodProcessor
 */
public class JsonMethodSupportVaildatorProcessor extends JsonMethodProcessor {

	private ValidatorArgumentResolver validatorArgumentResolver;

	public void setValidatorArgumentResolver(ValidatorArgumentResolver validatorArgumentResolver) {
		this.validatorArgumentResolver = validatorArgumentResolver;
	}

	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Object arg = super.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
		validatorArgumentResolver.resolve(parameter, arg);
		return arg;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (Objects.isNull(validatorArgumentResolver)) {
			throw new Exception("validatorArgumentResolver undefined");
		}
		super.afterPropertiesSet();
	}

}
