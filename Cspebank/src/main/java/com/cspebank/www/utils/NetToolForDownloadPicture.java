package com.cspebank.www.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetToolForDownloadPicture {

	public static boolean writeToStorage(String path, File file){
		if (file.exists()) {
			file.delete();
		}
		try {
			byte[] bytes = readImage(path);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(bytes);
			fos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public static byte[] readImage(String path) throws Exception{
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5*1000);
		InputStream is = conn.getInputStream();
		return readStream(is);
	}
	
	public static byte[] readStream(InputStream inStream) throws Exception{
		ByteArrayOutputStream baos = new 	ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while((len = inStream.read(buffer)) != -1){
			baos.write(buffer,0 , len);
		}
		baos.flush();
		baos.close();
		return baos.toByteArray();
		
	}
}
