package org.nicksun.shrek.validator.resolve;

import org.nicksun.shrek.validator.SupportsAnnotation;
import org.nicksun.shrek.validator.ValidException;

public interface ResolveArgument extends SupportsAnnotation {

	void resolveArgument(ArgumentContext context) throws ValidException;
}
