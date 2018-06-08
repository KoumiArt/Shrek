package org.nicksun.shrek.validator;

/**
 * @author nicksun
 *
 */
public interface ValidatorHandlerResolver {

	/**
	 * <code>@Validator</code>处理器器
	 * @param arg
	 * @param groups
	 * @throws ValidException
	 */
	void handle(Object arg, Class<?>[] groups) throws ValidException;

}