package org.nicksun.shrek.validator.handler;

import org.nicksun.shrek.validator.SupportsAnnotation;
import org.nicksun.shrek.validator.ValidException;

/**
 * @author nicksun
 *
 */
public interface ValidHandler extends SupportsAnnotation {

	void handleRequest(ValidatedContext content) throws ValidException;
}
