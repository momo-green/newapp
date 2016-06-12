package com.example.newapp.Util.bitmap;

import com.example.newapp.R;

import android.graphics.Bitmap;
import android.widget.ImageView;

/*
 * �Զ���ͼƬ����
 * 
 */
public class MyBitmapUtils {

	NetCacheUtils mNetCacheUtils;
	LocalCacheUtils mLocalCacheUtils;
	MemoryCacheUtils mMemoryCacheUtils;
	
	
	public MyBitmapUtils(){
		mMemoryCacheUtils = new MemoryCacheUtils();
		mLocalCacheUtils = new LocalCacheUtils();
		mNetCacheUtils = new NetCacheUtils(mLocalCacheUtils,mMemoryCacheUtils);
		
		
	}
	
	public void display(ImageView ivPic,String url){
		ivPic.setImageResource(R.drawable.news_pic_default);//Ĭ�ϼ���ͼƬ
		
		Bitmap bitmap = null;
		//���ڴ��
		bitmap = mMemoryCacheUtils.getBitmapFromMemory(url);
		if(bitmap != null){
			ivPic.setImageBitmap(bitmap);
			System.out.println("���ڴ��ȡͼƬ��");
			return ;
		}
		//�ӱ��ض�
		 bitmap = mLocalCacheUtils.getBitmapFromLocal(url);
		if(bitmap != null){
			ivPic.setImageBitmap(bitmap);
			System.out.println("�ӱ��ض�ȡͼƬ��������");
			mMemoryCacheUtils.setBitmapToMemory(url, bitmap); //��ͼƬ�������ڴ�
			return;
		}
		
		//�������
		mNetCacheUtils.getBitmapFromNet(ivPic,url);
	}
}
