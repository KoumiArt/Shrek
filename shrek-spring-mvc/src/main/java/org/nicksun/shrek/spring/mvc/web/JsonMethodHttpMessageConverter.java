package org.nicksun.shrek.spring.mvc.web;

import java.io.IOException;
import java.lang.reflect.Type;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.FileCopyUtils;

/**
 * 处理@ResoponseJson的客户端输出
 * 
 * @author nicksun
 *
 */
public class JsonMethodHttpMessageConverter extends AbstractGenericHttpMessageConverter<byte[]> {

	
	@Override
	public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
		return false;
	}

	@Override
	protected byte[] readInternal(Class<? extends byte[]> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		throw new UnsupportedOperationException();
	}

	@Override
	public byte[] read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void writeInternal(byte[] bytes, Type type, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		FileCopyUtils.copy(bytes, outputMessage.getBody());
	}

}
