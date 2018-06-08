package org.nicksun.shrek.spring.mvc.web.protocol;

import org.nicksun.shrek.spring.mvc.web.http.MessageProtocol;

/**
 * @author nicksun
 *
 */
public interface MessageProtocolProcessor {

	/**
	 * 支持的协议
	 * @param protocol
	 * @return
	 */
	boolean support(MessageProtocol protocol);
	
	/**
	 * 编码
	 * @param bytes
	 * @return
	 */
	byte[] encoding(byte[] bytes);
	
	/**
	 * 解密
	 * @param bytes
	 * @return
	 */
	byte[] decoding(byte[] bytes);
}
