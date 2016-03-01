package com.cspebank.www.utils;

import java.util.HashMap;

public class CalendarEvent {

	private HashMap<String,String> mMsg;
	public  CalendarEvent(HashMap<String,String> mMsg){
		this.mMsg = mMsg;
	}
	public HashMap<String,String> getMsg(){
		return this.mMsg;
	}
}
