package com.cspebank.www.exception;

/**
 * @author yisinian.deng
 * 2015.11.18
 */
public class CspeException extends Exception{

	private static final long serialVersionUID = 1L;

	// 未知错误
	public static final String UNKONWEN_ERORR = "-1";

	// 连接超时
	public static final String CONNECTED_ERORR = "-1001";

	// 解析JSON错误
	public static final String PARSE_ERORR = "-1002";

	protected String errorCode = UNKONWEN_ERORR;

	public CspeException(String msg) {
		super(msg);
	}

	public CspeException(String msg, Exception cause) {
		super(msg, cause);
	}

	public String getErrorCode() {
		return errorCode;
	}
	
}
