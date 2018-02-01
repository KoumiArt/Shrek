package org.nicksun.shrek.validator.handler;

import java.lang.annotation.Annotation;

import org.nicksun.shrek.validator.ValidException;
import org.nicksun.shrek.validator.annotation.Max;
import org.nicksun.shrek.validator.util.GroupUtil;
import org.nicksun.shrek.validator.util.ValidUtil;

/**
 * @author nicksun
 *
 */
public class MaxHandler implements ValidHandler {

	@Override
	public Class<? extends Annotation> supportsAnnotation() {
		return Max.class;
	}

	@Override
	public void handleRequest(ValidatedContext context) throws ValidException {
		Object fieldValue = context.getFieldValue();
		String str = String.valueOf(fieldValue);
		if (str.matches("\\D")) {
			throw new ValidException("@Max只能在用int类型上");
		}
		Max m = (Max) context.getAnnotation();
		if (GroupUtil.compare(m.groups(), context.getGroups())) {
			return;
		}
		int tmp = Integer.parseInt(str);
		if (tmp > m.value()) {
			String msg = m.msg();
			ValidUtil.throwValidException(msg, context.getFieldName(), "必须小于等于" + m.value());
		}
	}

}
