package org.nicksun.shrek.validator.handler;

import org.nicksun.shrek.validator.SupportsAnnotation;
import org.nicksun.shrek.validator.ValidException;

/**
 * @author nicksun
 *
 */
public interface ValidHandler extends SupportsAnnotation {

	/**
	 * 处理请求
	 * @param content
	 * @throws ValidException
	 */
	void handleRequest(ValidatedContext content) throws ValidException;
}
