package com.yisiinian.httputils;

/**
 * @author deng
 * String callback 返回的是string
 */
public abstract class StringCallback extends AbstractCallback<String>{

	@Override
	protected String bindData(String result) throws AppException {
		return result;
	}

}
								