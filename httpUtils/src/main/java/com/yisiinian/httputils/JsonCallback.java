package com.yisiinian.httputils;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author deng
 * Json callback 返回的是T
 */
public abstract class JsonCallback<T> extends AbstractCallback<T>{

	@Override
	protected T bindData(String result) throws AppException {
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			JSONObject data = json.optJSONObject("data");

			Gson gson = new Gson();
			//获取父类的泛型
			Type type = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			//通过gson包反序列化成相对应的对象
			return gson.fromJson(data.toString(), type);//type代表User
		} catch (JSONException e) {
			throw new AppException(AppException.ErrorType.JSON, e.getMessage());
		}
	}

}
