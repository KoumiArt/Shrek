package org.nicksun.shrek.spring.mvc.web.returnvalue.impl;

/**
 * @author nicksun
 *
 */
public class FailData {

	public static final String DEFAULT_ERRORCODE = "900000";

	private boolean success = false;
	private String errorCode = DEFAULT_ERRORCODE;
	private String msg = "";
	private String data = "";

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String message) {
		this.msg = message;
	}
}
