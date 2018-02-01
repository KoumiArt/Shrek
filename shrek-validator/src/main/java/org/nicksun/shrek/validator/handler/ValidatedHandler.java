package org.nicksun.shrek.validator.handler;

import java.lang.annotation.Annotation;

import org.nicksun.shrek.validator.ValidatorHandlerResolver;
import org.nicksun.shrek.validator.ValidException;
import org.nicksun.shrek.validator.annotation.Validated;

/**
 * @author nicksun
 *
 */
public class ValidatedHandler implements ValidHandler {

	private ValidatorHandlerResolver handleManager;

	public ValidatedHandler(ValidatorHandlerResolver handleManager) {
		this.handleManager = handleManager;
	}

	@Override
	public Class<? extends Annotation> supportsAnnotation() {
		return Validated.class;
	}

	@Override
	public void handleRequest(ValidatedContext context) throws ValidException {
		try {
			if (null == context.getFieldValue()) {
				return;
			}
			handleManager.handle(context.getFieldValue(), context.getGroups());
		} catch (ValidException e) {
			throw e;
		} catch (Exception e) {
			throw new ValidException("@Validated只能在自定义类型上使用");
		}
	}

}
