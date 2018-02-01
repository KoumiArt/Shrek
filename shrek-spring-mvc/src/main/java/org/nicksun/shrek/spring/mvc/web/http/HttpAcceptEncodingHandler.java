package org.nicksun.shrek.spring.mvc.web.http;

/**
 * 处理http header中accept-encoding
 * @author nicksun
 *  
 */
public interface HttpAcceptEncodingHandler {
	
	/**
	 * 支持的accept-encoding
	 * @param acceptEncoding
	 * @return
	 */
	boolean supports(String acceptEncoding);
	
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
