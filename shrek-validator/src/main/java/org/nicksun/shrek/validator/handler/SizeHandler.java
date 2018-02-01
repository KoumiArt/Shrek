package org.nicksun.shrek.validator.handler;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;

import org.nicksun.shrek.validator.ValidException;
import org.nicksun.shrek.validator.annotation.Size;
import org.nicksun.shrek.validator.util.GroupUtil;
import org.nicksun.shrek.validator.util.ValidUtil;

/**
 * @author nicksun
 *
 */
public class SizeHandler implements ValidHandler {

	@Override
	public Class<? extends Annotation> supportsAnnotation() {
		return Size.class;
	}

	@Override
	public void handleRequest(ValidatedContext context) throws ValidException {
		Object fieldValue = context.getFieldValue();
		if (fieldValue instanceof Collection || fieldValue instanceof Map) {
			Size s = (Size) context.getAnnotation();
			if (GroupUtil.compare(s.groups(), context.getGroups())) {
				return;
			}
			int tmp = 0;
			if (fieldValue instanceof Collection) {
				tmp = ((Collection<?>) fieldValue).size();
			}
			if (fieldValue instanceof Map) {
				tmp = ((Map<?, ?>) fieldValue).size();
			}
			if (tmp < s.value()) {
				String msg = s.msg();
				ValidUtil.throwValidException(msg, context.getFieldName(), "长度必须等于" + s.value());
			}
		} else {
			throw new ValidException("@Size只能在collection ，map上使用");
		}
	}

}
