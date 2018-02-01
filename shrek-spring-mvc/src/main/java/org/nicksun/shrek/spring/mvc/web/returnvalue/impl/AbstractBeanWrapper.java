package org.nicksun.shrek.spring.mvc.web.returnvalue.impl;

import org.nicksun.shrek.spring.mvc.web.returnvalue.BeanWrapper;
import org.nicksun.shrek.spring.mvc.web.returnvalue.ResponseData;
import org.springframework.core.MethodParameter;

public abstract class AbstractBeanWrapper implements BeanWrapper {

	@Override
	public boolean supportsType(MethodParameter returnType) {
		if (ResponseData.class.isAssignableFrom(returnType.getParameterType())) {
			return false;
		}
		return supports(returnType);
	}

	public abstract boolean supports(MethodParameter returnType);

}
