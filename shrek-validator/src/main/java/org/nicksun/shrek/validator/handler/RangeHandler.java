package org.nicksun.shrek.validator.handler;

import java.lang.annotation.Annotation;

import org.nicksun.shrek.validator.ValidException;
import org.nicksun.shrek.validator.annotation.Range;
import org.nicksun.shrek.validator.util.GroupUtil;
import org.nicksun.shrek.validator.util.ValidUtil;

/**
 * @author nicksun
 *
 */
public class RangeHandler implements ValidHandler {

	@Override
	public Class<? extends Annotation> supportsAnnotation() {
		return Range.class;
	}

	@Override
	public void handleRequest(ValidatedContext context) throws ValidException {
		Object fieldValue = context.getFieldValue();
		String str = String.valueOf(fieldValue);
		if (str.matches("\\D")) {
			throw new ValidException("@Range只能在用int类型上");
		}
		Range r = (Range) context.getAnnotation();
		if (GroupUtil.compare(r.groups(), context.getGroups())) {
			return;
		}
		int tmp = Integer.parseInt(str);
		if (tmp < r.min() || tmp > r.max()) {
			String msg = r.msg();
			ValidUtil.throwValidException(msg, context.getFieldName(), "必须在" + r.min() + "~" + r.max() + "之间");
		}
	}

}
