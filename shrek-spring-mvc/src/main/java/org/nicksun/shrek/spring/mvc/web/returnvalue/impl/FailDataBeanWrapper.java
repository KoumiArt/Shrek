package org.nicksun.shrek.spring.mvc.web.returnvalue.impl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.nicksun.shrek.spring.mvc.web.returnvalue.ErrorCode;
import org.nicksun.shrek.spring.mvc.web.returnvalue.ExceptionBeanWrapper;

/**
 * @author nicksun
 *
 */
public class FailDataBeanWrapper implements ExceptionBeanWrapper {

	@Override
	public boolean supportsType(Class<?> clazz) {
		return ErrorCode.class.isAssignableFrom(clazz);
	}

	@Override
	public Object wrap(Exception ex) {
		FailData failData = new FailData();
		if (ex instanceof ErrorCode) {
			failData.setErrorCode(((ErrorCode) ex).getErrorCode());
		}
		failData.setData(ExceptionUtils.getStackTrace(ex));
		failData.setMsg(ex.getMessage() == null ? "系统错误" : ex.getMessage());
		return failData;
	}

}
