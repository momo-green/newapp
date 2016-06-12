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
 * 网络缓存
 */
public class NetCacheUtils {
	
	private LocalCacheUtils mLocalCacheUtils;
	private MemoryCacheUtils mMemoryCacheUtils;
	
	public NetCacheUtils(LocalCacheUtils localCacheUtils, MemoryCacheUtils MemoryCacheUtils){
		mLocalCacheUtils = localCacheUtils;
		mMemoryCacheUtils = MemoryCacheUtils;
	}

	/*
	 * 从网络下载图片
	 */
	public void getBitmapFromNet(ImageView ivPic, String url) {
		new BitmapTask().execute(ivPic,url);//启动AsyncTask,参数会在doInBackground获取
	}

	/*
	 * Handler 和线程池的封装
	 * 
	 * 第一个泛型：参数类型
	 * 第二个泛型：更新进度的泛型
	 * 第三个泛型：onPostExecute返回结果
	 */
	class BitmapTask extends AsyncTask<Object,Void,Bitmap>{

		private ImageView ivPic;
		private String url;

		/*
		 * 后台耗时方法在此执行，子线程
		 */
		protected Bitmap doInBackground(Object... params) {
			
			ivPic = (ImageView) params[0];
			url = (String) params[1];
			
			
			ivPic.setTag(url); //将url和imageview绑定
			
			try {
				return downloadBitmap(url);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		
		/*
         *更新进度，主线程
		 */
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		/*
		 * 耗时方法结束后，执行该方法，主线程
		 */
		protected void onPostExecute(Bitmap result) {
			
			if(result != null){
				String bindurl = (String) ivPic.getTag();
				
				if(url.equals(bindurl)){ //确保图片设定给了正确的imageview
					ivPic.setImageBitmap(result);
					mLocalCacheUtils.setBitmapToLocal(url, result);  //将图片保存至本地
					mMemoryCacheUtils.setBitmapToMemory(url, result); //将图片保存在内存
					
					System.out.println("从网络缓存读取图片了。。。");
				}
				
			}
		}

		/*
		 * 下载图片
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
