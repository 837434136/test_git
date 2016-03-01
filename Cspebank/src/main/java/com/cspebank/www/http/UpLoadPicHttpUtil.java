package com.cspebank.www.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import java.io.IOException;

public class UpLoadPicHttpUtil {
	private static final int REQUEST_TIMEOUT = 15 * 1000;// 设置请求超时时间
	private static final int SO_TIME_OUT = 15 * 1000;// 设置等待数据超时时间
	
	/**
	 * 重写，传入MultipartEntity 上传图片，上传文件。
	 * 调用第三方包
	 * 传图片，传文件
	 */
	public static HttpEntity getEntity(String uri, MultipartEntity reqEntity)
			throws IOException {
		HttpEntity entity = null;
		try {
			// 创建可以设置请求超时的参数
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					REQUEST_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, SO_TIME_OUT);
			// 创建客户端
			HttpClient client = new DefaultHttpClient(httpParams);
			// 创建请求
			HttpUriRequest request = new HttpPost(uri);

			// 如果请求参数集合不为空
			if (reqEntity != null) {
				// 为请求对象设置请求实体
				((HttpPost) request).setEntity(reqEntity);
			}
			// 执行请求 获得响应
			HttpResponse response = client.execute(request);
			com.cspebank.www.utils.LogTrace.e("statusCode:" + response.getStatusLine().getStatusCode());
			// 判断响应码
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 如果响应码为200，则获取响应实体
				entity = response.getEntity();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}
}
