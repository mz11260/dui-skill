package com.zm.utils.http;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;


/**
 * 
 * 带连接池的httpClient 工具类
 * @author zhangZhenfei
 *
 */
public class HttpClientUtil {
	
	private static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

	public static final String CONTENT_TYPE_JSON = "application/json";
	public static final String CONTENT_TYPE_XML = "text/xml";
	
	private static final String EMPTY_STR = "";
	private static final String ENCODE = "UTF-8";

	private static PoolingHttpClientConnectionManager cm;
	
	private static void init() {
		if (cm == null) {
			cm = new PoolingHttpClientConnectionManager();
			cm.setMaxTotal(50);// 整个连接池最大连接数
			cm.setDefaultMaxPerRoute(3);// 每路由最大连接数，默认值是2
			log.info("创建连接池完成...");
		}
	}

	/**
	 * 通过连接池获取HttpClient
	 * 
	 * @return http client
	 */
	private static CloseableHttpClient getHttpClient() {
		init();
		return HttpClients.custom().setConnectionManager(cm).build();
	}
	
	/**
	 * 设置请求配置
	 * @return request config
	 */
	private static RequestConfig setRequestConfig () {
		return RequestConfig.custom()
				  .setSocketTimeout(1000 * 10)
				  .setConnectTimeout(1000 * 10)
				  .setConnectionRequestTimeout(1000 * 10)
				  .build();
	}

	/**
	 * HTTP GET 请求
	 *
	 * @param url    url
	 * @param params params
	 * @return response data
	 */
	public static String httpGetRequest(String url, Map<String, Object> params) {
		log.info("http GET 请求 {}", url);
		try {
			URIBuilder ub = new URIBuilder(url);
			//ub.setPath(url); //4.5.3版本使用这个方法会在URI前面多一个/
			if (params != null && params.size() > 0) {
				ArrayList<NameValuePair> pairs = setParams(params);
				ub.setParameters(pairs);
			}
			log.info(ub.build().toString());
			HttpGet httpGet = new HttpGet(ub.build());
			httpGet.setConfig(setRequestConfig());
			return getResponse(httpGet);
		} catch (URISyntaxException e) {
			log.error("uri build error ", e);
		}
		return EMPTY_STR;
	}


	/**
	 * HTTP POST 请求
	 *
	 * @param url      url
	 * @param formData form data
	 * @return response data
	 */
	public static String httpPostFormRequest(String url, NameValues formData) {
		return httpPostRequest(url, null, formData, PostType.FORM);
	}

	/**
	 * HTTP POST 请求
	 *
	 * @param url         请求地址
	 * @param requestData json参数
	 * @return response data
	 */
	public static String httpPostJsonRequest(String url, String requestData) {
		return httpPostRequest(url, requestData, null, PostType.JSON);
	}

	/**
	 * HTTP POST 请求
	 *
	 * @param url         请求地址
	 * @param requestData json参数
	 * @return response data
	 */
	public static String httpPostXmlRequest(String url, String requestData) {
		return httpPostRequest(url, requestData, null, PostType.XML);
	}

	private static String httpPostRequest(String url, String requestData, NameValues formData, PostType postType) {
		log.info("http POST 请求 {}", url);
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(setRequestConfig());
		switch (postType) {
			case JSON:
				httpPost.setHeader("Content-Type", CONTENT_TYPE_JSON);
				httpPost.setEntity(getStringEntity(requestData, CONTENT_TYPE_JSON));
				break;
			case XML:
				httpPost.setEntity(getStringEntity(requestData, CONTENT_TYPE_XML));
				break;
			case FORM:
				httpPost.setEntity(new UrlEncodedFormEntity(formData, Charset.forName(ENCODE)));
				break;
		}
		return getResponse(httpPost);
	}

	private static StringEntity getStringEntity(String requestData, String contentType) {
		StringEntity stringEntity = new StringEntity(requestData, ENCODE);//解决中文乱码问题
		stringEntity.setContentEncoding(ENCODE);
		stringEntity.setContentType(contentType);
		return stringEntity;
	}

	/**
	 * 处理 HTTP 请求
	 *
	 * @param request http request
	 * @return response data
	 */
	private static String getResponse(HttpRequestBase request) {
		log.info("请求开始...");
		CloseableHttpClient httpClient = getHttpClient();
		try {
			CloseableHttpResponse response = httpClient.execute(request);
			int status = response.getStatusLine().getStatusCode();
			if (200 == status) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					// long len = entity.getContentLength();// -1 表示长度未知
					String result = EntityUtils.toString(entity);
					log.info("请求完成...");
					return result;
				}
			} else {
				log.info("请求失败: status = {}, response = {}", status, response);
			}
		} catch (Exception e) {
			log.error("请求异常: ", e);
		}
		return EMPTY_STR;
	}

	/**
	 * 设置请求参数
	 *
	 * @param params params
	 * @return list param
	 */
	private static ArrayList<NameValuePair> setParams(Map<String, Object> params) {
		ArrayList<NameValuePair> pairs = new ArrayList<>();
		if (params != null) {
			for (Map.Entry<String, Object> param : params.entrySet()) {
				pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
			}
		}
		return pairs;
	}

	private enum PostType {
		XML,
		JSON,
		FORM
	}
	
}
