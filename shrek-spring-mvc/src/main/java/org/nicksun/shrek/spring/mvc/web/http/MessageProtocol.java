package org.nicksun.shrek.spring.mvc.web.http;

/**
 * 消息体协议
 * @author nicksun
 *
 */
public enum MessageProtocol {

	/**
	 * 默认，以EncodingJsonHttpMessageConverterWrapper为准
	 */
	DEFAULT,
	/**
	 * 纯文本传输
	 */
	TEXT,

	/**
	 * base64编码传输
	 */
	BASE64
}
