package org.nicksun.shrek.validator.resolve;

import org.nicksun.shrek.validator.ValidatorHandlerResolver;

public class ArgumentContext {

	private ValidatorHandlerResolver validatorHandleManager;

	private Object arg;

	private Object parameterAnnotation;

	private Class<?>[] groups;

	public ValidatorHandlerResolver getValidatorHandleManager() {
		return validatorHandleManager;
	}

	public void setValidatorHandleManager(ValidatorHandlerResolver validatorHandleManager) {
		this.validatorHandleManager = validatorHandleManager;
	}

	public Object getArg() {
		return arg;
	}

	public void setArg(Object arg) {
		this.arg = arg;
	}

	@SuppressWarnings("unchecked")
	public <T> T getParameterAnnotation(Class<T> annotationType) {
		if (annotationType.isInstance(parameterAnnotation)) {
			return (T) parameterAnnotation;
		}
		return null;
	}

	public void setParameterAnnotation(Object parameterAnnotation) {
		this.parameterAnnotation = parameterAnnotation;
	}

	public Class<?>[] getGroups() {
		return groups;
	}

	public void setGroups(Class<?>[] groups) {
		this.groups = groups;
	}

}
