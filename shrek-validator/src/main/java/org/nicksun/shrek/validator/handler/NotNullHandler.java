package org.nicksun.shrek.validator.handler;

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
public class NotNullHandler implements ValidHandler {

	@Override
	public Class<? extends Annotation> supportsAnnotation() {
		return NotNull.class;
	}

	@Override
	public void handleRequest(ValidatedContext context) throws ValidException {
		if (Objects.isNull(context.getFieldValue())) {
			NotNull n = (NotNull) context.getAnnotation();
			if (GroupUtil.compare(n.groups(), context.getGroups())) {
				return;
			}
			String value = n.value();
			ValidUtil.throwValidException(value, context.getFieldName(), "不能为null");
		}
	}

}
