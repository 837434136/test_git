package com.yisiinian.httputils;

import android.os.AsyncTask;

import java.net.HttpURLConnection;

/**
 * @author deng
 * 2015.12.22
 */
public class RequestTask extends AsyncTask<String, Integer, Object>{

	private Request request;
	private int curRetryIndex;


	public RequestTask(Request request) {
		this.request = request;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Object doInBackground(String... params) {
		return request(0);
	}

	/**
	 * @param retry
	 * @return
	 * 递归重连
	 */
	public Object request(int retry){
		try {
			isCancelled();
			HttpURLConnection connection = HttpUrlConnectionUtil.execute(request);

			//不需要每次都需要new 一个listener出来，这里需要一个开关控制
			if (request.enableProgressUpated == true) {
				return request.iCallback.parse(connection, new OnProgressUpdatedListener() {

					@Override
					public void onProgressUpdate(int curLen, int totalLen) {
						publishProgress(curLen, totalLen);
					}
				});
			}else {
				return request.iCallback.parse(connection);
			}
		} catch (AppException e) {
			if (e.type == AppException.ErrorType.TIMEOUT) {
				if (retry < request.maxRetryCount) {
					retry ++ ;
					return request(retry);
				}
			}
			return e;
		}
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if (result instanceof AppException) {

			request.iCallback.onFailure((AppException) result);
		}else {
			request.iCallback.onSuccess(result);
		}
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		request.iCallback.onProgressUpdate(values[0], values[1]);
	}
}
