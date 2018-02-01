package org.nicksun.shrek.spring.mvc.web.http.impl;

import org.nicksun.shrek.spring.mvc.web.http.HttpAcceptEncodingHandler;

public class GzipHttpAcceptEncodingHandler implements HttpAcceptEncodingHandler {

	@Override
	public boolean supports(String acceptEncoding) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public byte[] encoding(byte[] bytes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] decoding(byte[] bytes) {
		// TODO Auto-generated method stub
		return null;
	}

}
