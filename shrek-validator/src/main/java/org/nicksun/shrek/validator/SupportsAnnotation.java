package org.nicksun.shrek.validator;

import java.lang.annotation.Annotation;

/**
 * @author nicksun
 *
 */
public interface SupportsAnnotation {

	Class<? extends Annotation> supportsAnnotation();
}
