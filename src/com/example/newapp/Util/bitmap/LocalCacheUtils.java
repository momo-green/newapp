package com.example.newapp.Util.bitmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.example.newapp.Util.MD5Encoder;

/*
 * 本地缓冲
 */
public class LocalCacheUtils {

	public  static final String CACHE_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()+"/zhbj_cache_52";
	/*
	 * 从本地sdcard读图片
	 */
	public Bitmap getBitmapFromLocal(String url){
		String fileName;
		try {
			fileName = MD5Encoder.encode(url);
			File file = new File(CACHE_PATH,fileName);
			
			if(file.exists()){
			    Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
			    return bitmap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * 向SDcard写图片
	 */
	public void setBitmapToLocal(String url,Bitmap bitmap){
		String fileName;
		try {
			fileName = MD5Encoder.encode(url);
			File file = new File(CACHE_PATH,fileName);
			
			File parentFile = file.getParentFile();
			if(!parentFile.exists()){ //如果文件夹不存在。创建文件夹
				parentFile.mkdirs();
			}
			
			//将相片存储在本地
			bitmap.compress(CompressFormat.JPEG, 100
					,new FileOutputStream(file));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
