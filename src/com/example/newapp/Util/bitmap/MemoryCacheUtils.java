package com.example.newapp.Util.bitmap;

import java.util.HashMap;

import android.graphics.Bitmap;

/*
 * 内存缓存
 */
public class MemoryCacheUtils {

	private HashMap<String,Bitmap> mMemoryCache = new HashMap<String,Bitmap>();
	
	/*
	 * 从内存读
	 */
	public Bitmap getBitmapFromMemory(String url){
         return mMemoryCache.get(url);
	}
	
	/*
	 * 写内存
	 */
	public void setBitmapToMemory(String url,Bitmap bitmap){
		mMemoryCache.put(url, bitmap);
	}
}
