package org.nicksun.shrek.validator.resolve;

import org.nicksun.shrek.validator.SupportsAnnotation;
import org.nicksun.shrek.validator.ValidException;

/**
 * @author nicksun
 *
 */
public interface ResolveArgument extends SupportsAnnotation {

	/**
	 * 解析参数
	 * @param context
	 * @throws ValidException
	 */
	void resolveArgument(ArgumentContext context) throws ValidException;
}
