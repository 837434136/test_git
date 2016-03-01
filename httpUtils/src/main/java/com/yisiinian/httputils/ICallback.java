package com.yisiinian.httputils;

import java.net.HttpURLConnection;

/**
 * @author deng
 * 2015.12.22
 */
public interface ICallback<T> {

	void onSuccess(T result);
	void onFailure(AppException e);

	/**提前做，要返回的数据库的内容,子线程
	 * @return
	 */
	T preRequest();

	/**子线程
	 * @return
	 */
	T postRequest(T t);
	T parse(HttpURLConnection connection, OnProgressUpdatedListener listener) throws AppException;
	T parse(HttpURLConnection connection) throws AppException;

	void onProgressUpdate(int curLen, int totalLen);
	void cancel();
}
