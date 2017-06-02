package com.example.config;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;

/**
 * 
 * @author tlkj 客户端与服务器端的通信配置
 */
public class ServerConfiguration {
	public static final String IP = "192.168.2.30";// 服务器地址
	public static final int PORT = 8080;// 要根据应用服务器tomcat的端口改变
	private static final String URLVAR = "http://" + IP + ":" + PORT
			+ "/MobilePoliceServer/";

	/**
	 * 通过URL获取网络连接 connection
	 */
	public HttpURLConnection getConn(String urlpath) {
		String finalurl = URLVAR + urlpath;
		HttpURLConnection connection = null;
		try {
			URL url = new URL(finalurl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true); // 允许输入流
			connection.setDoOutput(true); // 允许输出流
			connection.setConnectTimeout(2000);// 连接超时，2秒
			connection.setReadTimeout(5000);// 请求超时，5秒
			connection.setUseCaches(false); // 不允许使用缓存
			connection.setRequestMethod("POST"); // 请求方式
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return connection;
	}

	public static HttpPost gethttPost(String uripath) {
		HttpPost httpPost = new HttpPost(URLVAR + uripath);
		System.out.println(URLVAR + uripath);
		return httpPost;
	}
}
