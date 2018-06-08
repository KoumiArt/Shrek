package org.nicksun.shrek.spring.mvc.web.http.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;
import org.nicksun.shrek.spring.mvc.web.http.HttpContentEncodingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author nicksun
 *
 */
public class GzipHttpContentEncodingHandler implements HttpContentEncodingHandler {

	private static final Logger LOG = LoggerFactory.getLogger(GzipHttpContentEncodingHandler.class);
	private static final int DEFAULT_THRESHOLD = 16384;

	@Override
	public boolean supports(String contentEncoding) {
		return StringUtils.isEmpty(contentEncoding) ? false : contentEncoding.equalsIgnoreCase("gzip");
	}

	@Override
	public byte[] compress(byte[] bytes) {
		if (Objects.isNull(bytes) || bytes.length < DEFAULT_THRESHOLD) {
			return bytes;
		}
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				GZIPOutputStream out = new GZIPOutputStream(baos);
				ByteArrayInputStream in = new ByteArrayInputStream(bytes);) {
			IOUtils.copy(in, out);
			return baos.toByteArray();
		} catch (IOException e) {
			LOG.error("Gzip压缩失败", e);
			throw new RuntimeException("IO exception compressing data", e);
		}
	}

	@Override
	public byte[] decompress(byte[] bytes) {
		if (Objects.isNull(bytes)) {
			return null;
		}
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
				GZIPInputStream in = new GZIPInputStream(bais);) {
			IOUtils.copy(in, out);
			return out.toByteArray();
		} catch (IOException e) {
			LOG.error("Gzip解压缩失败", e);
			throw new RuntimeException("IO exception compressing data", e);
		}
	}

}
