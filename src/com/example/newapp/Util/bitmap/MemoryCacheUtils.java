package com.example.newapp.Util.bitmap;

import java.util.HashMap;

import android.graphics.Bitmap;

/*
 * �ڴ滺��
 */
public class MemoryCacheUtils {

	private HashMap<String,Bitmap> mMemoryCache = new HashMap<String,Bitmap>();
	
	/*
	 * ���ڴ��
	 */
	public Bitmap getBitmapFromMemory(String url){
         return mMemoryCache.get(url);
	}
	
	/*
	 * д�ڴ�
	 */
	public void setBitmapToMemory(String url,Bitmap bitmap){
		mMemoryCache.put(url, bitmap);
	}
}
