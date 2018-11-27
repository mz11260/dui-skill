package com.zm.common.openapi;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsoupUtils {

	public final static int TIMEOUT = 30000;


	/**
	 * 设置请求 header
	 * @return response
	 */
	public static Map<String, String> setHeaders() {
    	Map<String, String> headers = new HashMap<>();
    	headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    	headers.put("Accept-Encoding", "gzip, deflate");
    	headers.put("Accept-charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
    	headers.put("Accept-Language", "en-us,en;q=0.5");
    	headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
    	return headers;
    }
	
	/**
     * 请求Response
     * @param url 源URL
     * @return response
     */
	static public Response getResponse(String url) throws IOException {
		return Jsoup.connect(url)
				.headers(setHeaders())
				.timeout(TIMEOUT).ignoreContentType(true).execute();
    }
	
	/**
     * 请求JSON
     * @param url 源URL
     * @return string
     */
	public static String getResponseJson(String url) throws IOException {
		Response response = Jsoup.connect(url)
				.headers(setHeaders())
				.timeout(TIMEOUT).ignoreContentType(true).execute();
		return response.body();
    }

	/**
	 * 加载html页面
	 * @param url 源URL
	 * @return html document
	 */
	public static Document getHtmlDocument(String url) throws IOException {
		return Jsoup.connect(url)
				.headers(setHeaders())
				.timeout(TIMEOUT).get();
	}
}
