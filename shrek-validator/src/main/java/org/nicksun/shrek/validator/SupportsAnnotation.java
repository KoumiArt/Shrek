package org.nicksun.shrek.validator;

import java.lang.annotation.Annotation;

/**
 * @author nicksun
 *
 */
public interface SupportsAnnotation {

	/**
	 * 支持的注解
	 * @return
	 */
	Class<? extends Annotation> supportsAnnotation();
}
