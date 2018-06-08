package org.nicksun.shrek.spring.mvc.web.returnvalue.impl;

import org.springframework.core.MethodParameter;

/**
 * @author nicksun
 *
 */
public class DefaultBeanWrapper extends AbstractBeanWrapper {

	@Override
	public Object wrap(Object bean) {
		return new SuccessData(bean);
	}

	@Override
	public boolean supports(MethodParameter returnType) {
		return true;
	}

}
