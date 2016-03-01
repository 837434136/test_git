package com.yisiinian.httputils.test;

import android.test.AndroidTestCase;
import android.util.Log;

import com.yisiinian.httputils.AppException;
import com.yisiinian.httputils.HttpUrlConnectionUtil;
import com.yisiinian.httputils.Request;
import com.yisiinian.httputils.RequestTask;
import com.yisiinian.httputils.StringCallback;

import java.net.HttpURLConnection;

/**
 * @author deng
 * HttpUtils 的测试单元
 */
public class TestHttp extends AndroidTestCase{

	public void testHttpGets() throws Throwable{
		String url = "http://api.stay4it.com";
		Request request = new Request(url);
		HttpURLConnection result = HttpUrlConnectionUtil.execute(request) ;
		Log.e("deng", "testHttpGet return: " + result);
	}

	public void testHttpPost() throws Throwable{
		String url = "http://api.stay4it.com/v1/public/core/?service=user.login";
		String content = "account=stay4it&password=123456";
		Request request = new Request(url, Request.RequestMethod.POST);
		request.content = content;
		HttpURLConnection result = HttpUrlConnectionUtil.execute(request);
		Log.e("deng", "testHttpGet return: " + result);
	}

	public void testHttpPostSubThread() throws Throwable{
		String url = "http://api.stay4it.com/v1/public/core/?service=user.login";
		String content = "account=stay4it&password=123456";
		Request request = new Request(url, Request.RequestMethod.POST);
		request.setCallback(new StringCallback() {

			@Override
			public void onSuccess(String result) {
				Log.e("deng", "testHttpGet return: " + result.toString());

			}

			@Override
			public void onFailure(AppException e) {
				// TODO Auto-generated method stub
				e.printStackTrace();

			}
		});
		request.content = content;
		request.enableProgressUpated(true);
		request.maxRetryCount = 0;//手动设置不重连 
		request.setTag(toString());
		RequestTask task = new RequestTask(request);
		task.execute();
		task.cancel(true);
		request.cancel(true);
	}
}
