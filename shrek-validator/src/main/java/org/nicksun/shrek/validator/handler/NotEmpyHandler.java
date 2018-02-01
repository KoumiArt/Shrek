package org.nicksun.shrek.validator.handler;

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
public class NotEmpyHandler implements ValidHandler {

	@Override
	public Class<? extends Annotation> supportsAnnotation() {
		return NotEmpy.class;
	}

	@Override
	public void handleRequest(ValidatedContext context) throws ValidException {
		if (objectEmpy(context.getFieldValue()) || collectionEmpy(context.getFieldValue())) {
			NotEmpy n = (NotEmpy) context.getAnnotation();
			if (GroupUtil.compare(n.groups(), context.getGroups())) {
				return;
			}
			String value = n.value();
			ValidUtil.throwValidException(value, context.getFieldName(), "不能为空");
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

	private boolean objectEmpy(Object obj) {
		if (Objects.isNull(obj)) {
			return true;
		}
		return obj.toString().trim().length() <= 0;
	}
}
