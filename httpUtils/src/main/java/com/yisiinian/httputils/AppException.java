package com.yisiinian.httputils;

public class AppException extends Exception{

	public int statusCode;
	public String responseMessage;
	public enum ErrorType {CANCEL, TIMEOUT, SERVER, JSON, IO, FILE_NOT_FOUND, MANUAL, REQUEST_INFO}
	public ErrorType type;
	
	public AppException(int status, String responseMessage) {
		super();
		this.type = ErrorType.SERVER;
		this.statusCode = status;
		this.responseMessage = responseMessage;
	}

	public AppException(ErrorType type, String detailMessage) {
		super(detailMessage);
		this.type = type;
	}

}
