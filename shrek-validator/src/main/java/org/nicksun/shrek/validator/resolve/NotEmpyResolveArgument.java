package org.nicksun.shrek.validator.resolve;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Objects;

import org.nicksun.shrek.validator.ValidException;
import org.nicksun.shrek.validator.annotation.NotEmpy;
import org.nicksun.shrek.validator.util.GroupUtil;
import org.nicksun.shrek.validator.util.ValidUtil;

/**
 * @author nicksun
 *
 */
public class NotEmpyResolveArgument implements ResolveArgument {

	@Override
	public Class<? extends Annotation> supportsAnnotation() {
		return NotEmpy.class;
	}

	@Override
	public void resolveArgument(ArgumentContext context) throws ValidException {
		NotEmpy n = context.getParameterAnnotation(NotEmpy.class);
		if (Objects.isNull(context.getArg()) || collectionEmpy(context.getArg())) {
			if (GroupUtil.compare(n.groups(), context.getGroups())) {
				return;
			}
			String value = n.value();
			ValidUtil.throwValidException(value, "入参不能为空");
		}
	}

	@SuppressWarnings("rawtypes")
	private boolean collectionEmpy(Object obj) {
		if (Objects.isNull(obj)) {
			return true;
		}
		if (Collection.class.isAssignableFrom(obj.getClass())) {
			return ((Collection) obj).isEmpty();
		}
		return false;
	}
}
