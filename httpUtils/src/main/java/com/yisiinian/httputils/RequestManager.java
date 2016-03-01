package com.yisiinian.httputils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author deng
 * 2015.12.22
 */
public class RequestManager {

	//	private static RequestManager mInstance;
	private final ExecutorService mExecutors;
	private HashMap<String, ArrayList<Request>> mCacheRequest;

//	public static RequestManager getInstance(){
//		if (mInstance == null) {
//			mInstance = new RequestManager();
//		}
//		return mInstance;
//	}

	public RequestManager(){
		mCacheRequest = new HashMap<String, ArrayList<Request>>();
		//new 一个线程池，定位5个
		mExecutors = Executors.newFixedThreadPool(5);
	}

	//执行request
	public void prefromRequest(Request request){
		request.execute(mExecutors);
		if (!mCacheRequest.containsKey(request.tag)) {
			ArrayList<Request> requests = new ArrayList<Request>();
			mCacheRequest.put(request.tag, requests);
		}
		mCacheRequest.get(request.tag).add(request);
	}
	public void cancelRequest(String tag){
		cancelRequest(tag, false);
	}
	public void cancelRequest(String tag, boolean froce){
		if (tag == null || "".equals(tag.trim())) {
			return;
		}
		//TODO find request by tag, and cancel them
		if (mCacheRequest.containsKey(tag)) {
			ArrayList<Request> requests = mCacheRequest.remove(tag);
			//移出来，一个一个遍历cancel
			for (Request request : requests) {
				if (!request.isCancelled && tag.equals(request.tag)) {
					request.cancel(froce);

				}
			}
		}

	}

	public void cancelAll(){
		for(Map.Entry<String, ArrayList<Request>> entry : mCacheRequest.entrySet()){
			ArrayList<Request> requests = entry.getValue();
			for (Request request : requests) {
				if (!request.isCancelled) {
					request.cancel(true);
				}
			}
		}
	}
}
