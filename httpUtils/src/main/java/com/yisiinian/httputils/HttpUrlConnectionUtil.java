
package com.yisiinian.httputils;

import android.webkit.URLUtil;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;


/**
 * @author deng
 * 2015.12.22
 */
public class HttpUrlConnectionUtil {

	/**
	 * 不能暴露太多方法给上层，只是提供一个方法给上层调用
	 */
	public static HttpURLConnection execute(Request request) throws AppException{
		if (!URLUtil.isNetworkUrl(request.url)) {//面向开发者
			throw new AppException(AppException.ErrorType.MANUAL, "url : " + request.url + " is not valid");
		}
		switch (request.method) {
			case GET:
			case DELETE:
				return get(request);

			case POST:
			case PUT:
				return post(request);

		}
		return null;
	}

	/**
	 * get方法
	 */
	private static HttpURLConnection get(Request request) throws AppException{
		try {
			//TODO check if cancelled
			request.checkIfCancelled();
			HttpURLConnection connection = (HttpURLConnection) new URL(request.url).openConnection();
			connection.setRequestMethod(request.method.name());
			connection.setConnectTimeout(15 * 1000);
			connection.setReadTimeout(15 * 1000);

//			addHeader(request.headers, connection);

			request.checkIfCancelled();

			return connection;
		} catch (InterruptedIOException e) {//超时判断
			throw new AppException(AppException.ErrorType.TIMEOUT, e.getMessage());

		} catch (IOException e) {
			throw new AppException(AppException.ErrorType.SERVER, e.getMessage());
		}

	}

	/**
	 * post方法
	 */
	private static HttpURLConnection post(Request request) throws AppException{
		HttpURLConnection connection = null;
		try {
			request.checkIfCancelled();

			connection = (HttpURLConnection) new URL(request.url).openConnection();
			connection.setRequestMethod(request.method.name());
			connection.setConnectTimeout(15 * 1000);
			connection.setReadTimeout(15 * 1000);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(true);
			connection.setUseCaches(false);

			StringBuffer sb = getContent(request.params);
			request.content = sb.toString();

			//设置请求体的类型是文本类型
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			//设置请求体的长度
			int length = request.content.getBytes("utf-8").length;
			connection.setRequestProperty("Content-Length", String.valueOf(length));

//			addHeader(request.headers, connection);
			request.checkIfCancelled();

			//把数据传到服务器
			OutputStream os = connection.getOutputStream();
			os.write(request.content.getBytes("utf-8"));

			request.checkIfCancelled();

		} catch (InterruptedIOException e) {//超时判断
			throw new AppException(AppException.ErrorType.TIMEOUT, e.getMessage());

		} catch (IOException e) {
			throw new AppException(AppException.ErrorType.SERVER, e.getMessage());
		}
		return connection;
	}

	/**
	 *Function:封装请求体信息
	 *Param:params请求体内容，encode编码格式
	 */
	public static StringBuffer getContent(Map<String, String> params) throws AppException{
		if (params == null || params.size() == 0) {
			return null;
		}
		//存储封装好的请求体信息
		StringBuffer stringBuffer = new StringBuffer();

		for(Map.Entry<String, String> entry : params.entrySet()) {
			stringBuffer.append(entry.getKey())
					.append("=")
					.append(entry.getValue())
					.append("&");
		}
		//删除最后的一个"&"
		stringBuffer.deleteCharAt(stringBuffer.length() - 1);


		return stringBuffer;
	}

	private static void addHeader(Map<String, String> headers,
								  HttpURLConnection connection) {

		if (headers == null || headers.size() == 0) {
			return;
		}

		for(Map.Entry<String, String> entry : headers.entrySet()){
			connection.addRequestProperty(entry.getKey(), entry.getValue());
		}
	}


}
