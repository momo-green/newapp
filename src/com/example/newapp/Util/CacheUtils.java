package com.example.newapp.Util;

import android.content.Context;

public class CacheUtils {

	/*
	 * ���û���key��url,value��json
	 */
	public static void setCache(String key,String value,Context ctx){
		PrefUtils.setString(ctx, key, value);
	}
	/*
	 * ��ȡ����
	 * key��url
	 */
	public static String getCache(String key,Context ctx){
		return PrefUtils.getString(ctx, key, null);
	}
}
