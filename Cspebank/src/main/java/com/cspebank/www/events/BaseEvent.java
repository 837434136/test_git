package com.cspebank.www.events;

public class BaseEvent {

	private String type;

	public BaseEvent(String type){
		this.type = type;
	}
	
	public String getTitle() {
		return type;
	}
	
	
}
