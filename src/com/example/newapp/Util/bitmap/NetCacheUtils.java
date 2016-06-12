package com.example.newapp.Util.bitmap;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

/*
 * ���绺��
 */
public class NetCacheUtils {
	
	private LocalCacheUtils mLocalCacheUtils;
	private MemoryCacheUtils mMemoryCacheUtils;
	
	public NetCacheUtils(LocalCacheUtils localCacheUtils, MemoryCacheUtils MemoryCacheUtils){
		mLocalCacheUtils = localCacheUtils;
		mMemoryCacheUtils = MemoryCacheUtils;
	}

	/*
	 * ����������ͼƬ
	 */
	public void getBitmapFromNet(ImageView ivPic, String url) {
		new BitmapTask().execute(ivPic,url);//����AsyncTask,��������doInBackground��ȡ
	}

	/*
	 * Handler ���̳߳صķ�װ
	 * 
	 * ��һ�����ͣ���������
	 * �ڶ������ͣ����½��ȵķ���
	 * ���������ͣ�onPostExecute���ؽ��
	 */
	class BitmapTask extends AsyncTask<Object,Void,Bitmap>{

		private ImageView ivPic;
		private String url;

		/*
		 * ��̨��ʱ�����ڴ�ִ�У����߳�
		 */
		protected Bitmap doInBackground(Object... params) {
			
			ivPic = (ImageView) params[0];
			url = (String) params[1];
			
			
			ivPic.setTag(url); //��url��imageview��
			
			try {
				return downloadBitmap(url);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		
		/*
         *���½��ȣ����߳�
		 */
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		/*
		 * ��ʱ����������ִ�и÷��������߳�
		 */
		protected void onPostExecute(Bitmap result) {
			
			if(result != null){
				String bindurl = (String) ivPic.getTag();
				
				if(url.equals(bindurl)){ //ȷ��ͼƬ�趨������ȷ��imageview
					ivPic.setImageBitmap(result);
					mLocalCacheUtils.setBitmapToLocal(url, result);  //��ͼƬ����������
					mMemoryCacheUtils.setBitmapToMemory(url, result); //��ͼƬ�������ڴ�
					
					System.out.println("�����绺���ȡͼƬ�ˡ�����");
				}
				
			}
		}

		/*
		 * ����ͼƬ
		 */
		private Bitmap downloadBitmap(String url) throws IOException {
			
			HttpURLConnection conn = null;
			try {
				conn = (HttpURLConnection) new URL(url).openConnection();
				
				    conn.setConnectTimeout(5000);
				    conn.setReadTimeout(5000);
				    conn.setRequestMethod("GET");
				    
				    conn.connect();
				    int responseCode = conn.getResponseCode();
				    if(responseCode == 200){
				    	InputStream inputStream = conn.getInputStream();
				    	Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				    	return bitmap;
				    }
				    
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				conn.disconnect();
			}
	
		   return null;
		    
		}
	
	}
}
