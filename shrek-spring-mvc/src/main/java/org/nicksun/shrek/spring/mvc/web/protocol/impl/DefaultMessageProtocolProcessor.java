package org.nicksun.shrek.spring.mvc.web.protocol.impl;

import org.nicksun.shrek.spring.mvc.web.http.MessageProtocol;
import org.nicksun.shrek.spring.mvc.web.protocol.MessageProtocolProcessor;

/**
 * 
 * 默认协议、纯文本 消息协议处理器
 * 
 * @author nicksun
 *
 */
public class DefaultMessageProtocolProcessor implements MessageProtocolProcessor {

	@Override
	public boolean support(MessageProtocol protocol) {
		return MessageProtocol.DEFAULT == protocol || MessageProtocol.TEXT == protocol;
	}

	@Override
	public byte[] encoding(byte[] bytes) {
		return bytes;
	}

	@Override
	public byte[] decoding(byte[] bytes) {
		return bytes;
	}

}
