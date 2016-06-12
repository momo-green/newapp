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
 * ���ػ���
 */
public class LocalCacheUtils {

	public  static final String CACHE_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()+"/zhbj_cache_52";
	/*
	 * �ӱ���sdcard��ͼƬ
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
	 * ��SDcardдͼƬ
	 */
	public void setBitmapToLocal(String url,Bitmap bitmap){
		String fileName;
		try {
			fileName = MD5Encoder.encode(url);
			File file = new File(CACHE_PATH,fileName);
			
			File parentFile = file.getParentFile();
			if(!parentFile.exists()){ //����ļ��в����ڡ������ļ���
				parentFile.mkdirs();
			}
			
			//����Ƭ�洢�ڱ���
			bitmap.compress(CompressFormat.JPEG, 100
					,new FileOutputStream(file));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
