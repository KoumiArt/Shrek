package org.nicksun.shrek.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


/**
 * http https 请求
 * @author nicksun
 *
 */
public class HttpClientUtil {

	private final static String PROTOCOL_TLS = "TLS";

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
	public static String postByJson(String url, String params, String charset)
			throws ParseException, IOException, URISyntaxException {
		return postByJson(url, params, charset, null);
	}

	public static String postByJson(String url, String params, String charset, Map<String, String> headers)
			throws ParseException, IOException, URISyntaxException {
		return post(url, params, charset, "application/json;charset=" + charset);
	}

	public static String post(String url, String params, String charset, String contentType)
			throws ParseException, IOException, URISyntaxException {
		return post(url, params, charset, contentType, null);
	}

	public static String post(String url, String params, String charset, String contentType,
			Map<String, String> headers) throws ParseException, IOException, URISyntaxException {
		RequestBuilder requestBuilder = RequestBuilder.post().setUri(new URI(url));
		requestBuilder.addHeader("Content-Type", contentType);
		if (Objects.nonNull(headers) && headers.size() > 0) {
			headers.forEach((k, v) -> {
				requestBuilder.addHeader(k, v);
			});
		}
		requestBuilder.setEntity(new StringEntity(params, Charset.forName(charset)));
		return execute(requestBuilder);
	}

	public static String postByJson(String url, String params) throws ParseException, IOException, URISyntaxException {
		return postByJson(url, params, "UTF-8");
	}

	public static String postByJsonSSL(String url, String params, String charset)
			throws ParseException, IOException, URISyntaxException, KeyManagementException, NoSuchAlgorithmException {
		return postByJsonSSL(url, params, charset, null);
	}

	public static String postByJsonSSL(String url, String params, String charset, Map<String, String> headers)
			throws ParseException, IOException, URISyntaxException, KeyManagementException, NoSuchAlgorithmException {
		return postSSL(url, params, charset, "application/json;charset=" + charset, headers);
	}

	public static String postSSL(String url, String params, String charset, String contentType)
			throws ParseException, IOException, URISyntaxException, KeyManagementException, NoSuchAlgorithmException {
		return postSSL(url, params, charset, contentType, null);
	}

	public static String postSSL(String url, String params, String charset, String contentType,
			Map<String, String> headers)
			throws ParseException, IOException, URISyntaxException, KeyManagementException, NoSuchAlgorithmException {
		RequestBuilder requestBuilder = RequestBuilder.post().setUri(new URI(url));
		requestBuilder.addHeader("Content-Type", contentType);
		if (Objects.nonNull(headers) && headers.size() > 0) {
			headers.forEach((k, v) -> {
				requestBuilder.addHeader(k, v);
			});
		}
		requestBuilder.setEntity(new StringEntity(params, Charset.forName(charset)));
		return executeSSL(requestBuilder);
	}

	public static String postByJsonSSL(String url, String params)
			throws ParseException, IOException, URISyntaxException, KeyManagementException, NoSuchAlgorithmException {
		return postByJsonSSL(url, params, "UTF-8");
	}

	public static String postByJsonSSL(String url, String params, Map<String, String> headers)
			throws ParseException, IOException, URISyntaxException, KeyManagementException, NoSuchAlgorithmException {
		return postByJsonSSL(url, params, "UTF-8", headers);
	}

	public static String execute(RequestBuilder requestBuilder) throws ParseException, IOException, URISyntaxException {
		return execute(requestBuilder, StandardCharsets.UTF_8.displayName());
	}

	public static String execute(CloseableHttpClient httpClient, RequestBuilder requestBuilder, String defaultCharset)
			throws ParseException, IOException, URISyntaxException {
		CloseableHttpResponse response = httpClient.execute(requestBuilder.build());
		return EntityUtils.toString(response.getEntity(), defaultCharset);
	}

	public static String execute(RequestBuilder requestBuilder, String defaultCharset)
			throws ParseException, IOException, URISyntaxException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = httpClient.execute(requestBuilder.build());
		return EntityUtils.toString(response.getEntity(), defaultCharset);
	}

	public static String executeSSL(RequestBuilder requestBuilder)
			throws ParseException, IOException, URISyntaxException, KeyManagementException, NoSuchAlgorithmException {
		return executeSSL(requestBuilder, StandardCharsets.UTF_8.displayName());
	}

	public static String executeSSL(RequestBuilder requestBuilder, String defaultCharset)
			throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException {
		CloseableHttpClient httpClient = getSSLHttpClient();
		CloseableHttpResponse response = httpClient.execute(requestBuilder.build());
		return EntityUtils.toString(response.getEntity(), defaultCharset);
	}

	public static String getByJson(String url, String params) throws ParseException, IOException, URISyntaxException {
		return getByJson(url, params, "UTF-8");
	}

	public static String getByJson(String url, String params, Map<String, String> headers)
			throws ParseException, IOException, URISyntaxException {
		return getByJson(url, params, "UTF-8", headers);
	}

	public static String get(String url, String params, String charset, String contentType)
			throws ParseException, IOException, URISyntaxException {
		return get(url, params, charset, contentType, null);
	}

	public static String get(String url, String params, String charset, String contentType, Map<String, String> headers)
			throws ParseException, IOException, URISyntaxException {
		RequestBuilder requestBuilder = RequestBuilder.get().setUri(new URI(url));
		requestBuilder.addHeader("Content-Type", contentType);
		if (Objects.nonNull(headers) && headers.size() > 0) {
			headers.forEach((k, v) -> {
				requestBuilder.addHeader(k, v);
			});
		}
		requestBuilder.setEntity(new StringEntity(params, Charset.forName(charset)));
		return execute(requestBuilder);
	}

	public static String getSSL(String url, String params, String charset, String contentType,
			Map<String, String> headers)
			throws ParseException, IOException, URISyntaxException, KeyManagementException, NoSuchAlgorithmException {
		RequestBuilder requestBuilder = RequestBuilder.get().setUri(new URI(url));
		requestBuilder.addHeader("Content-Type", contentType);
		if (Objects.nonNull(headers) && headers.size() > 0) {
			headers.forEach((k, v) -> {
				requestBuilder.addHeader(k, v);
			});
		}
		requestBuilder.setEntity(new StringEntity(params, Charset.forName(charset)));
		return executeSSL(requestBuilder);
	}

	public static String getSSL(String url, String params, String charset, String contentType)
			throws ParseException, IOException, URISyntaxException, KeyManagementException, NoSuchAlgorithmException {
		return getSSL(url, params, charset, contentType, null);
	}

	public static String getByJson(String url, String params, String charset)
			throws ParseException, IOException, URISyntaxException {
		return getByJson(url, params, charset, null);
	}

	public static String getByJson(String url, String params, String charset, Map<String, String> headers)
			throws ParseException, IOException, URISyntaxException {
		return get(url, params, charset, "application/json;charset=" + charset, headers);
	}

	public static String getByJsonSSL(String url, String params)
			throws ParseException, IOException, URISyntaxException, KeyManagementException, NoSuchAlgorithmException {
		return getByJsonSSL(url, params, "UTF-8", null);
	}

	public static String getByJsonSSL(String url, String params, Map<String, String> headers)
			throws ParseException, IOException, URISyntaxException, KeyManagementException, NoSuchAlgorithmException {
		return getByJsonSSL(url, params, "UTF-8", headers);
	}

	public static String getByJsonSSL(String url, String params, String charset)
			throws ParseException, IOException, URISyntaxException, KeyManagementException, NoSuchAlgorithmException {
		return getByJsonSSL(url, params, charset, null);
	}

	public static String getByJsonSSL(String url, String params, String charset, Map<String, String> headers)
			throws ParseException, IOException, URISyntaxException, KeyManagementException, NoSuchAlgorithmException {
		return getSSL(url, params, charset, "application/json;charset=" + charset);
	}

	/**
	 * 创建一个不进行正式验证的请求客户端对象 不用导入SSL证书
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	private static CloseableHttpClient getSSLHttpClient() throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext ctx = SSLContext.getInstance(PROTOCOL_TLS);
		X509TrustManager tm = new X509TrustManager() {
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			}
		};
		ctx.init(null, new TrustManager[] { tm }, null);
		SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE);
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(ssf).build();
		return httpClient;
	}

}