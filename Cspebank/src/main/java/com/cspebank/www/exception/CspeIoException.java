package com.cspebank.www.exception;

/**
 * 类说明： IO类异常
 * @author yisinian.deng
 * 2015.11.18
 */
public class CspeIoException extends CspeException {

	private static final long serialVersionUID = 1L;

	public CspeIoException(String msg) {
		super(msg);
		this.errorCode = CONNECTED_ERORR;
	}

	public CspeIoException(String msg, Exception cause) {
		super(msg, cause);
		this.errorCode = CONNECTED_ERORR;
	}
	
}
