package com.yisiinian.httputils;

/**
 * @author deng
 * 文件callback，返回的是路径
 */
public abstract class FileCallback extends AbstractCallback<String>{

	@Override
	protected String bindData(String result) throws AppException {
		return result;
	}

}
