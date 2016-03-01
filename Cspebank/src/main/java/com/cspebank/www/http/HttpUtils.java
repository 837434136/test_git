package com.cspebank.www.http;

import android.text.TextUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class HttpUtils {
	
	//网络码
	public static final String NETWORK_CODE = "network_code";
	//网络结果码
	public static final String RESULT = "result";   
	//表示第一次登陆
	public static final int FIRST_TIME_TO_SERVER = 0;
	
    /**
     * Function:发送Post请求到服务器
     * Param:params请求体内容，encode编码格式
     */
    public static String getEntity(String urlString, Map<String, String> params)  {
    	com.cspebank.www.utils.LogTrace.e("http请求开始...");
    	int i=0;
    	String encode="utf-8";
    	URL url = null;
    	OutputStream outputStream = null;
    	HttpURLConnection httpURLConnection =null;
    	InputStream inputStream = null;
    	while (true) {
    		 try { 
    			//获得请求体
    			byte[] data = getRequestData(params, encode).toString().getBytes();
    			 
	        	url= new URL(urlString);
	        	httpURLConnection = (HttpURLConnection)url.openConnection();
	        	setHttpUrlConParams(i, httpURLConnection, data);
	            
	            //获得输出流，向服务器写入数据
	            outputStream = httpURLConnection.getOutputStream();
	            outputStream.write(data);
	            
	            if (outputStream!=null) {
	        		outputStream.flush();
					outputStream.close();
				}
	            
	            // 要注意的是httpURLConnection.getOutputStream会隐含的进行connect。
	            inputStream = httpURLConnection.getInputStream();   
	          	//获得服务器的响应码
	            int response = httpURLConnection.getResponseCode();         
	            
	            com.cspebank.www.utils.LogTrace.e("http 响应码 : " + response);
	            
	            if (inputStream!=null) {
            		String returnString = null;
            		//处理服务器的响应结果
            		returnString = dealResponseResult(response,inputStream); 
            		return returnString;                     
				}
	        } catch (Exception e) {
	            e.printStackTrace();
	            if (i>=1) {
	            	com.cspebank.www.utils.LogTrace.e("增加一次" + i);
	            	//捕捉到异常就返回的就是空。
					return null;
				}
	            i++;
	            com.cspebank.www.utils.LogTrace.e("增加一次" + i);
	        }
	        finally{
	        	//多关闭几次也不会报错
	        	if (inputStream!=null) {
	        		try {
						inputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	        	
	        	if (httpURLConnection!=null) {
	        		 httpURLConnection.disconnect();
				}
	        }
		}
    }

	/**
	 * 设置httpUrlConnection参数
	 */
	private static void setHttpUrlConParams(int i,HttpURLConnection httpURLConnection, byte[] data)
			throws ProtocolException {
		
		if (i == FIRST_TIME_TO_SERVER) {
			
			//读取时间最大是13s
			httpURLConnection.setReadTimeout(13000);	
			//设置连接超时时间，最大是10秒
			httpURLConnection.setConnectTimeout(10000);    
		
		}else {//第二次连接服务器 
			
			httpURLConnection.setReadTimeout(16000);
			httpURLConnection.setConnectTimeout(10000);  
			
		}
		
		//打开输入流，以便从服务器获取数据
		httpURLConnection.setDoInput(true);   
		//打开输出流，以便向服务器提交数据
		httpURLConnection.setDoOutput(true);    
		//设置主体的格式是utf-8
		httpURLConnection.setRequestProperty("contentType", "utf-8");
		//设置这个连接是否遵循重定向。
		httpURLConnection.setInstanceFollowRedirects(true);
		// 设置请求的头  设置长连接暂时不用
//		httpURLConnection.setRequestProperty("Connection", "keep-alive");
		//设置以Post方式提交数据
		httpURLConnection.setRequestMethod("POST");   
		//使用Post方式不能使用缓存
		httpURLConnection.setUseCaches(false);              
		//设置请求体的类型是文本类型
		httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		//设置请求体的长度
		httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
	}
	
    /**
     *Function:封装请求体信息
     *Param:params请求体内容，encode编码格式
     */
    public static StringBuffer getRequestData(Map<String, String> params, String encode) throws Exception{
    	if (params == null || params.size() == 0) {
			
		}
    	//存储封装好的请求体信息
        StringBuffer stringBuffer = new StringBuffer();    
        
        for(Map.Entry<String, String> entry : params.entrySet()) {
            stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
        }
       //删除最后的一个"&"
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);    
            
        return stringBuffer;
    }
    
     /**
      * Function:处理服务器的响应结果（将输入流转化成字符串）
      * Param:inputStream服务器的响应输入流
     */
    public static String dealResponseResult(int response,InputStream inputStream) throws Exception{
    	//存储处理结果
	     String resultData = "";      
	     InputStreamReader mInputStreamReader=null;
	     BufferedReader mBufferedReader=null;
       //返回的是200码
         if (response==200) {
    	   if (inputStream!=null) {
    		   
         		 mInputStreamReader = new InputStreamReader(inputStream,"utf-8");
         		 if (mInputStreamReader!=null) {
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
		}
         
         if (TextUtils.isEmpty(resultData)) {
        	//就是当解析完还是空，就直接返回空，不进行json打包
			return null;
		}
         
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(NETWORK_CODE, response);
		jsonObject.put(RESULT, resultData);
		String returnJsonString=null;
		returnJsonString = jsonObject.toString();
		com.cspebank.www.utils.LogTrace.w("http请求结果：" + returnJsonString);
		
		return returnJsonString;
	}
         
	

	public static String getString(String urlString){
		
		String encode="utf-8";
		HttpURLConnection httpURLConnection=null;
		OutputStream outputStream =null;
		InputStream inputStream =null;
		URL url = null;
		try {
		  	url= new URL(urlString);
		  	httpURLConnection = (HttpURLConnection)url.openConnection();
		 	httpURLConnection.setReadTimeout(13000);	//读取时间朿大是30s
        	httpURLConnection.setConnectTimeout(10000);
        	//打开输入流，以便从服务器获取数据
            httpURLConnection.setDoInput(true); 
            //打开输出流，以便向服务器提交数据
            httpURLConnection.setDoOutput(true);                 
            httpURLConnection.setRequestProperty("contentType", "utf-8");
            httpURLConnection.setRequestMethod("POST"); 
            httpURLConnection.setUseCaches(false);  
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	            
            // 要注意的是httpURLConnection.getOutputStream会隐含的进行connect
            inputStream = httpURLConnection.getInputStream();  
            int response = httpURLConnection.getResponseCode();    
            
            if (response==200) {
            	String a=dealResponseResult(200,inputStream);
            	return a;
			}
            else {
				return "NONO";
			}
		} catch (Exception e) {
			// TODO: handle Exception
		}
		return null;
		
	}
	
	public static String getWxToken(String urlString){
		
		String encode="utf-8";
		HttpURLConnection httpURLConnection=null;
		OutputStream outputStream =null;
		InputStream inputStream =null;
		URL url = null;
		try {
			url= new URL(urlString);
			httpURLConnection = (HttpURLConnection)url.openConnection();
			httpURLConnection.setReadTimeout(13000);	//读取时间朿大是30s
			httpURLConnection.setConnectTimeout(10000);
			//打开输入流，以便从服务器获取数据
			httpURLConnection.setDoInput(true); 
			//打开输出流，以便向服务器提交数据
			httpURLConnection.setDoOutput(true);                 
			httpURLConnection.setRequestProperty("contentType", "utf-8");
			httpURLConnection.setRequestMethod("GET"); 
			httpURLConnection.setUseCaches(false);  
			httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			// 要注意的是httpURLConnection.getOutputStream会隐含的进行connect
			inputStream = httpURLConnection.getInputStream();  
			int response = httpURLConnection.getResponseCode();    
			
			if (response==200) {
				String a=dealResponseResult(200,inputStream);
				return a;
			}
			else {
				return "NONO";
			}
		} catch (Exception e) {
			// TODO: handle Exception
		}
		return null;
		
	}
}
