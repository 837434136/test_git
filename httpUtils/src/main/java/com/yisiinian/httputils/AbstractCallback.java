package com.yisiinian.httputils;

import android.text.TextUtils;

import com.yisiinian.httputils.utils.BackAES;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * 父类抽出共性
 */
public abstract class AbstractCallback<T> implements ICallback<T>{

	private String path;
	private volatile boolean isCancelled;
	private boolean encryptable;
	private static final String STATUS_CODE = "statusCode";
	private static final String MSG = "msg";

	@Override
	public T parse(HttpURLConnection connection) throws AppException {
		return parse(connection, null);
	}

	@Override
	public T parse(HttpURLConnection connection, OnProgressUpdatedListener listener) throws AppException{
		try {

			checkIfCancelled();

			//具体的解析代码
			int status = connection.getResponseCode();
			if (status == HttpStatus.SC_OK) {
				if (path == null) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					InputStream is = connection.getInputStream();
//					byte[] buffer = new byte[2048];
//					int len;
//					while ((len = is.read(buffer)) != -1) {
//						checkIfCancelled();
//						out.write(buffer, 0, len);
//					}

					String result = dealResponseResult(is);

					is.close();
					out.close();
					out.close();
//					String result = new String (out.toByteArray());

					System.out.println("encryprable : " + encryptable);
					if (encryptable) {
						//对返回数据进行解密
						result = BackAES.decrypt(result, "res");
					}

					JSONObject jsonObject = new JSONObject(result);
					System.out.println("result : " + result);

					int statusCode =jsonObject.getInt(STATUS_CODE);
					String msg = jsonObject.getString(MSG);

					if (statusCode == 10001|| statusCode == 10024 || statusCode == 10020/*表示需要进行同步*/
							|| statusCode == 10005 || statusCode == 10019
							|| statusCode == 10012 || statusCode == 10011/*表示已经最新*/) {
						T t = bindData(result);
						//保存数据库或者编辑等操作，声明到父类里面去
						return postRequest(t);
					}else {
						throw new AppException(AppException.ErrorType.REQUEST_INFO, msg);
					}

				}else {
					FileOutputStream out = new FileOutputStream(path);
					InputStream is = connection.getInputStream();

					int totalLen = connection.getContentLength();
					int curLen = 0;

					byte[] buffer = new byte[2048];
					int len;
					while ((len = is.read(buffer)) != -1) {
						checkIfCancelled();
						out.write(buffer, 0, len);
						curLen += len;
						listener.onProgressUpdate(curLen, totalLen);
					}
					is.close();
					out.close();
					out.close();
					T t = bindData(path);
					return postRequest(t);
				}
			}else {
//				connection.getResponseMessage();
//				connection.getErrorStream();
				throw new AppException(status, connection.getResponseMessage());
			}
		} catch (Exception e) {
			throw new AppException(AppException.ErrorType.IO, e.getMessage());
		}
	}

	protected void checkIfCancelled() throws AppException {
		if (isCancelled) {
			throw new AppException(AppException.ErrorType.CANCEL, "the request has been cancelled");
		}
	}

	@Override
	public void cancel() {
		isCancelled = true;
	}

	//避免回调接口不需要重写该方法时回调多余
	@Override
	public void onProgressUpdate(int curLen, int totalLen) {

	}

	@Override
	public T preRequest() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T postRequest(T t) {
		return t;
	}

	protected abstract T bindData(String result) throws AppException;

	public ICallback setCachePath(String path) {
		this.path = path;
		return this;
	}

	public ICallback setEncryptable(boolean b) {
		this.encryptable = b;
		return this;
	}

	public static String dealResponseResult(InputStream inputStream) throws AppException{
		//存储处理结果
		String resultData = "";
		InputStreamReader mInputStreamReader=null;
		BufferedReader mBufferedReader=null;

		try {
			if (inputStream != null) {

				mInputStreamReader = new InputStreamReader(inputStream, "utf-8");
				if (mInputStreamReader != null) {
					mBufferedReader =new BufferedReader(mInputStreamReader);
					String line = "";

					while((line = mBufferedReader.readLine()) != null)
					{
						resultData += line;
					}
				}

				if (mBufferedReader!=null) {
					mBufferedReader.close();
				}
				if (mInputStreamReader!=null) {
					mInputStreamReader.close();
				}

			}else {//流为空，直接返回
				return null;
			}

			if (TextUtils.isEmpty(resultData)) {
				//就是当解析完还是空，就直接返回空
				return null;
			}
		} catch (IOException e) {
			throw new AppException(AppException.ErrorType.IO, e.getMessage());
		}

		return resultData;
	}
}
