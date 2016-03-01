package com.cspebank.www.task;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.cspebank.www.events.BaseEvent;
import com.cspebank.www.utils.LogTrace;

import de.greenrobot.event.EventBus;

public class EventTask extends AsyncTask<String, Void, Void>{

	@Override
	protected Void doInBackground(String... params) {
		String tag = params[0];
		if (!TextUtils.isEmpty(tag)) {
			EventBus.getDefault().post(new BaseEvent(tag));
			LogTrace.i("------eventbus post");
		}
		return null;
	}

}
