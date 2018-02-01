package org.nicksun.shrek.spring.mvc.web.protocol.impl;

import java.util.Base64;

import org.nicksun.shrek.spring.mvc.web.http.MessageProtocol;
import org.nicksun.shrek.spring.mvc.web.protocol.MessageProtocolProcessor;

/**
 * base64 消息协议处理器
 * @author nicksun
 *
 */
public class Base64MessageProtocolProcessor implements MessageProtocolProcessor {

	@Override
	public boolean support(MessageProtocol protocol) {
		return protocol == MessageProtocol.BASE64;
	}

	@Override
	public byte[] encoding(byte[] bytes) {
		return Base64.getEncoder().encode(bytes);
	}

	@Override
	public byte[] decoding(byte[] bytes) {
		return Base64.getDecoder().decode(bytes);
	}

}
