package com.example.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.widget.Toast;

import com.example.config.ServerConfiguration;

public class HttpOperation {

	public String getStringFromServer(String url, String jsondata){
		String result = "false";
		ServerConfiguration connNet = new ServerConfiguration();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("jsondata", jsondata));
		try {
			HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			HttpPost httpPost = connNet.gethttPost(url);
			System.out.println(httpPost.toString());
			httpPost.setEntity(entity);
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 1000);// 连接超时，5秒
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
					1000);// 读取超时，10秒
			HttpResponse httpResponse = client.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils
						.toString(httpResponse.getEntity(), "utf-8");
			} else {
				result = "false";
			}
		} catch (ConnectTimeoutException e) {
			return "timeout";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
//	/**
//	 * 上传核录信息
//	 */
//	public String Upload_Normal(String url, String jsondata) {
//		String result = "false";
//		ServerConfiguration connNet = new ServerConfiguration();
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("jsondata", jsondata));
//		try {
//			HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
//			HttpPost httpPost = connNet.gethttPost(url);
//			System.out.println(httpPost.toString());
//			httpPost.setEntity(entity);
//			HttpClient client = new DefaultHttpClient();
//			client.getParams().setParameter(
//					CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);// 连接超时，5秒
//			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
//					10000);// 读取超时，10秒
//			HttpResponse httpResponse = client.execute(httpPost);
//			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//				result = EntityUtils
//						.toString(httpResponse.getEntity(), "utf-8");
//			} else {
//				result = "false";
//			}
//		} catch (ConnectTimeoutException e) {
//			return "timeout";
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (ParseException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
//
//	/**
//	 * 从服务器拉取100条在逃人员信息
//	 * 
//	 * @param url
//	 * @return
//	 */
//	public String GetHundredEscaped(String url, String jsondata) {
//		String result = "false";
//		ServerConfiguration connNet = new ServerConfiguration();
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("jsondata", jsondata));
//		try {
//			HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
//			HttpPost httpPost = connNet.gethttPost(url);
//			System.out.println(httpPost.toString());
//			httpPost.setEntity(entity);
//			HttpClient client = new DefaultHttpClient();
//			client.getParams().setParameter(
//					CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);// 连接超时，5秒
//			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
//					10000);// 读取超时，10秒
//			HttpResponse httpResponse = client.execute(httpPost);
//			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//				result = EntityUtils
//						.toString(httpResponse.getEntity(), "utf-8");
//			} else {
//				result = "false";
//			}
//		} catch (ConnectTimeoutException e) {
//			return "timeout";
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (ParseException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return result;
//	}

}
