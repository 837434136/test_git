package com.cspebank.www.db;

import android.database.sqlite.SQLiteDatabase;

import org.litepal.tablemanager.Connector;

/**
 * @author yisinian.deng 
 * 2015.11.18
 * 基本的数据库方法封装
 */
public class CspeDB {

	private SQLiteDatabase db;

	public CspeDB(){
		//生成数据库
		db = Connector.getDatabase();
	}
	
	private static class SingleInstanceHolder{
		private static final CspeDB INSTANCE = new CspeDB();
	}
	
	/**
	 * @return
	 * 获取单例
	 */
	public static CspeDB getInstance(){
		return SingleInstanceHolder.INSTANCE;
	}
	
	/**
	 * 关闭数据库
	 */
	public void closeDB(){
		if (db != null) {
			db.close();
		}
	}
}
