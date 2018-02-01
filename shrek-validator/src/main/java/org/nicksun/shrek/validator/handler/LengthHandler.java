package org.nicksun.shrek.validator.handler;

import java.lang.annotation.Annotation;

import org.nicksun.shrek.validator.ValidException;
import org.nicksun.shrek.validator.annotation.Length;
import org.nicksun.shrek.validator.util.GroupUtil;
import org.nicksun.shrek.validator.util.ValidUtil;

/**
 * @author nicksun
 *
 */
public class LengthHandler implements ValidHandler {

	@Override
	public Class<? extends Annotation> supportsAnnotation() {
		return Length.class;
	}

	@Override
	public void handleRequest(ValidatedContext context) throws ValidException {
		Object fieldValue = context.getFieldValue();
		if (!fieldValue.getClass().isArray()) {
			throw new ValidException("@Length只能在数组上使用");
		}
		Length len = (Length) context.getAnnotation();
		if (GroupUtil.compare(len.groups(), context.getGroups())) {
			return;
		}
		int tmp = ((Object[]) fieldValue).length;
		if (tmp != len.value()) {
			String msg = len.msg();
			ValidUtil.throwValidException(msg, context.getFieldName(), "长度必须等于" + len.value());
		}
	}

}
