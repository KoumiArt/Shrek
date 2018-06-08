package org.nicksun.shrek.validator;

import org.springframework.core.MethodParameter;

/**
 * @author nicksun
 *
 */
public interface ValidatorArgumentResolver {

	/**
	 * <code>@Validator</code>参数的解析
	 * @param parameter
	 * @param arg
	 * @throws ValidException
	 */
	void resolve(MethodParameter parameter, Object arg) throws ValidException;

}