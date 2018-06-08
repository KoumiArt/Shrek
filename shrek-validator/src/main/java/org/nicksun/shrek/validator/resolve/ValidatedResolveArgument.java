package org.nicksun.shrek.validator.resolve;

import java.lang.annotation.Annotation;

import org.nicksun.shrek.validator.ValidException;
import org.nicksun.shrek.validator.annotation.Validated;

/**
 * @author nicksun
 *
 */
public class ValidatedResolveArgument implements ResolveArgument {

	@Override
	public Class<? extends Annotation> supportsAnnotation() {
		return Validated.class;
	}

	@Override
	public void resolveArgument(ArgumentContext context) throws ValidException {
		Validated v = context.getParameterAnnotation(Validated.class);
		Class<?>[] groups = v.groups();
		context.getValidatorHandleManager().handle(context.getArg(), groups);
	}

}
