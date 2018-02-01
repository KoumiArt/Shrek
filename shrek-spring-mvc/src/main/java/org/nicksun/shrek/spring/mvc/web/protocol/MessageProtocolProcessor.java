package org.nicksun.shrek.spring.mvc.web.protocol;

import org.nicksun.shrek.spring.mvc.web.http.MessageProtocol;

public interface MessageProtocolProcessor {

	boolean support(MessageProtocol protocol);
	
	byte[] encoding(byte[] bytes);
	
	byte[] decoding(byte[] bytes);
}
