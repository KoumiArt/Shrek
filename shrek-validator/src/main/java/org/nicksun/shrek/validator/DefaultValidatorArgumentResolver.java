package org.nicksun.shrek.validator;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.nicksun.shrek.validator.resolve.ArgumentContext;
import org.nicksun.shrek.validator.resolve.NotEmpyResolveArgument;
import org.nicksun.shrek.validator.resolve.NotNullResolveArgument;
import org.nicksun.shrek.validator.resolve.ResolveArgument;
import org.nicksun.shrek.validator.resolve.ValidatedResolveArgument;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.MethodParameter;

/**
 * @author nicksun
 *
 */
public class DefaultValidatorArgumentResolver implements ValidatorArgumentResolver, InitializingBean {

	public List<ResolveArgument> customResolveArgument;
	private List<ResolveArgument> resolveArgument;
	private ValidatorHandlerResolver validatorHandlerManager;

	public void setCustomResolveArgument(List<ResolveArgument> customResolveArgument) {
		this.customResolveArgument = customResolveArgument;
	}

	public void setValidatorHandlerManager(ValidatorHandlerResolver validatorHandlerManager) {
		this.validatorHandlerManager = validatorHandlerManager;
	}

	public DefaultValidatorArgumentResolver() {
		resolveArgument = new ArrayList<>();
		resolveArgument.add(new NotNullResolveArgument());
		resolveArgument.add(new NotEmpyResolveArgument());
		resolveArgument.add(new ValidatedResolveArgument());
		if (Objects.nonNull(customResolveArgument) && customResolveArgument.size() > 0) {
			resolveArgument.addAll(customResolveArgument);
		}
	}

	@Override
	public void resolve(MethodParameter parameter, Object arg) throws ValidException {
		for (ResolveArgument r : resolveArgument) {
			Class<? extends Annotation> t = r.supportsAnnotation();
			if (parameter.hasParameterAnnotation(t)) {
				ArgumentContext context = new ArgumentContext();
				context.setParameterAnnotation(parameter.getParameterAnnotation(t));
				context.setArg(arg);
				context.setValidatorHandleManager(validatorHandlerManager);
				r.resolveArgument(context);
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (Objects.isNull(validatorHandlerManager)) {
			throw new ValidException("validatorHandlerManager undefined");
		}
	}
}
