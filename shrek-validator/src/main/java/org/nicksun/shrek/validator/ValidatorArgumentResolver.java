package org.nicksun.shrek.validator;

import org.springframework.core.MethodParameter;

public interface ValidatorArgumentResolver {

	void resolve(MethodParameter parameter, Object arg) throws ValidException;

}