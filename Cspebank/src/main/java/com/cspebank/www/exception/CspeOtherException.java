package com.cspebank.www.exception;

/**
 * 类说明： 其他异常
 * @author yisinian.deng
 * 2015.11.18
 */
public class CspeOtherException extends CspeException {

	private static final long serialVersionUID = 1L;

	public CspeOtherException(String msg, Exception cause) {
		super(msg, cause);
	}

	public CspeOtherException(String msg) {
		super(msg);
	}

}
