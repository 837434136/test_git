package com.yisiinian.httputils;

import android.os.Build;

import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author yisinian.deng
 * 2015.12.22
 */
public class Request {

	public enum RequestMethod{GET,POST,PUT,DELETE}

	public String url;
	public String content;
	public Map<String, String> params;

	public RequestMethod method;
	public ICallback iCallback;
	public boolean enableProgressUpated = false;
	//设置最大重连次数
	public int maxRetryCount = 3;

	public volatile boolean isCancelled;
	public String tag;
	public RequestTask task;

	public Request(String url, Map<String, String> params, RequestMethod method){
		this.url = url;
		this.method = method;
		this.params = params;
	}

	public Request(String url, RequestMethod method){
		this.url = url;
		this.method = method;
	}

	public Request(String url){
		this.url = url;
		this.method = RequestMethod.GET;
	}

	public void setCallback() {

	}

	public void setCallback(ICallback iCallback) {
		this.iCallback = iCallback;
	}

	public void enableProgressUpated(boolean enable) {
		this.enableProgressUpated  = enable;
	}

	public void checkIfCancelled() throws AppException {
		if (isCancelled) {
			throw new AppException(AppException.ErrorType.CANCEL, "the request has been cancelled");
		}
	}

	public void cancel(boolean b) {
		isCancelled = true;
		iCallback.cancel();
		if (b && task != null) {
			task.cancel(b);
		}
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void execute(Executor mExecutors) {
		task = new RequestTask(this);
		if (Build.VERSION.SDK_INT > 11) {//android 系统3.0以上AsyncTask是默认同步执行线程，一次只能执行一个线程
			//实现多线程同时执行任务
			task.executeOnExecutor(mExecutors);
		}else {
			task.execute();
		}
	}
}
