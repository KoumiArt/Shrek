package org.nicksun.shrek.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @author nicksun
 *
 */
public class HttpClientUtil {
	/**
	 * 
	 * @param url
	 *            要提交的目标url
	 * @param map
	 *            参数集合
	 * @param charset
	 *            编码
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * @throws URISyntaxException
	 */
	public static String postByJson(String url, String params) throws ParseException, IOException, URISyntaxException {
		RequestBuilder requestBuilder = RequestBuilder.post().setUri(new URI(url));
		requestBuilder.addHeader("Content-Type", "application/json;charset=UTF-8");
		requestBuilder.setEntity(new StringEntity(params, Consts.UTF_8));
		return execute(requestBuilder);
	}

	public static String execute(RequestBuilder requestBuilder) throws ParseException, IOException, URISyntaxException {
		return execute(requestBuilder, StandardCharsets.UTF_8.displayName());
	}

	public static String execute(RequestBuilder requestBuilder, String defaultCharset)
			throws ParseException, IOException, URISyntaxException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = httpClient.execute(requestBuilder.build());
		if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
			return EntityUtils.toString(response.getEntity(), defaultCharset);
		}
		return null;
	}

	public static String getByJson(String url, String params) throws ParseException, IOException, URISyntaxException {
		RequestBuilder requestBuilder = RequestBuilder.get().setUri(new URI(url));
		requestBuilder.addHeader("Content-Type", "application/json;charset=UTF-8");
		requestBuilder.setEntity(new StringEntity(params, Consts.UTF_8));
		return execute(requestBuilder);
	}

}