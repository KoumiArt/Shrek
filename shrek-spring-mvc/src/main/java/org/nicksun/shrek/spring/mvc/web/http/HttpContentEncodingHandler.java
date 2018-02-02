package org.nicksun.shrek.spring.mvc.web.http;

/**
 * 处理http header中accept-encoding
 * 
 * @author nicksun
 * 
 */
public interface HttpContentEncodingHandler {

	/**
	 * 支持的accept-encoding
	 * 
	 * @param acceptEncoding
	 * @return
	 */
	boolean supports(String acceptEncoding);

	/**
	 * 压缩
	 * 
	 * @param bytes
	 * @return
	 */
	byte[] compress(byte[] bytes);

	/**
	 * 解压
	 * 
	 * @param bytes
	 * @return
	 */
	byte[] decompress(byte[] bytes);
}
