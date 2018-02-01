package org.nicksun.shrek.spring.mvc.web.returnvalue.impl;

import org.nicksun.shrek.spring.mvc.web.returnvalue.ResponseData;

/**
 * @author nicksun
 *
 */
public class SuccessData implements ResponseData {

	private boolean success = true;

	private Object data;

	private Object msg;

	public SuccessData(Object data) {
		this.data = data;
	}

	public SuccessData(String msg) {
		this.msg = msg;
	}

	public SuccessData(Object data, String msg) {
		this.data = data;
		this.msg = msg;
	}

	public SuccessData(Object data, Object msg) {
		this.data = data;
		this.msg = msg;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getMsg() {
		return msg;
	}

	public void setMsg(Object msg) {
		this.msg = msg;
	}
}