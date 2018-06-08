package org.nicksun.shrek.validator.resolve;

import java.lang.annotation.Annotation;
import java.util.Objects;

import org.nicksun.shrek.validator.ValidException;
import org.nicksun.shrek.validator.annotation.NotNull;
import org.nicksun.shrek.validator.util.GroupUtil;
import org.nicksun.shrek.validator.util.ValidUtil;

/**
 * @author nicksun
 *
 */
public class NotNullResolveArgument implements ResolveArgument {

	@Override
	public Class<? extends Annotation> supportsAnnotation() {
		return NotNull.class;
	}

	@Override
	public void resolveArgument(ArgumentContext context) throws ValidException {
		NotNull n = context.getParameterAnnotation(NotNull.class);
		if (Objects.isNull(context.getArg())) {
			if (GroupUtil.compare(n.groups(), context.getGroups())) {
				return;
			}
			String value = n.value();
			ValidUtil.throwValidException(value, "入参不能为null");
		}
	}
}
