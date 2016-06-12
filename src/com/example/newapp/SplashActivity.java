package com.example.newapp;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.newapp.Util.PrefUtils;
import com.example.newapp.Util.StreamUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {

    protected static final int CODE_UPDATE_DIALOG = 0;
	protected static final int CODE_URL_ERROR = 1;
	protected static final int CODE_NET_ERROR = 2;
	protected static final int CODE_JSON_ERROR = 3;
	protected static final int CODE_ENTER_HOME = 4; //进入主页面
	private String mDownloadUrl;
	private String mDesc;
	private int mVersionCode;
	private String mVersionName;

	private RelativeLayout rlRoot;
	private TextView tvVersion;	
	private TextView tvProgess;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CODE_UPDATE_DIALOG:
				showUpdateDailog();
				
				break;
			case CODE_URL_ERROR:
				Toast.makeText(SplashActivity.this, "url错误", Toast.LENGTH_SHORT)
				      .show();
				enterHome();

				break;
			case CODE_NET_ERROR:
				Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_SHORT)
			      .show();
				enterHome();

				break;
			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "数据错误", Toast.LENGTH_SHORT)
			      .show();
				enterHome();
				break;
			case CODE_ENTER_HOME:
				enterHome();
				break;
			default:
				break;
			}
		};
	};
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        tvVersion = (TextView) findViewById(R.id.tv_version);
        tvProgess = (TextView) findViewById(R.id.tv_Progress);
        
        startAnim();
        getVersionName();
        tvVersion.setText("版本名 ： "+ getVersionName());
        
        //checkVerson();
      
    }
    
   private void startAnim(){
    	
    	//动画集合
    	AnimationSet set = new AnimationSet(false);
    	
    	//旋转动画
    	RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, 
    			                     Animation.RELATIVE_TO_SELF, 0.5f);
    	
    	rotate.setDuration(1000); //动画时间
    	rotate.setFillAfter(true);//保持动画状态
    	//缩放动画
    	ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1,
    			Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    	
    	scale.setDuration(2000);
    	scale.setFillAfter(true);
    	
    	//透明度动画效果
    	
    	AlphaAnimation alpha = new AlphaAnimation(0,1);
    	
    	set.addAnimation(rotate);
    	set.addAnimation(scale);
    	set.addAnimation(alpha);
     
    	//设置动画监听
    	set.setAnimationListener(new AnimationListener() {
			
    		//开始动画
			public void onAnimationStart(Animation animation) {
				
			}
			
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			//结束动画
			public void onAnimationEnd(Animation animation) {
			
				jnmpNexPage();
			    //finish();
			}
		});
    	
    	rlRoot.startAnimation(set);
    	
    }
 
     //跳转下一个页面
     public void jnmpNexPage(){
    	 //判断之前有没有显示新手引导页
        boolean userGuide = PrefUtils.getBoolean(this, "is_user_guide_showed", false);
    	 
    	 if(!userGuide){
    		 startActivity(new Intent(SplashActivity.this,GuideAtivity.class));
    	 }else
    	 {
    		 //startActivity(new Intent(SplashActivity.this,MainActivity.class));
    		 checkVerson();
    	 }
    	 

     }
     
     /**
 	 * 获取版本号
 	 * 
 	 */
 	private int getVersionCode(){
 		
 		PackageManager packageManager = getPackageManager();
 		try {
 			PackageInfo packageInfo = packageManager.getPackageInfo(
 					getPackageName(), 0);
 			
 		    int versionCode = packageInfo.versionCode;
 			
 			return versionCode;
 		} catch (NameNotFoundException e) {
 			e.printStackTrace();
 		}
 		
 		return -1;
 	}
 	
 	/**
	 * 获取版本名
	 * 
	 */
	private String getVersionName(){
		
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			
		    int versionCode = packageInfo.versionCode;
			String versionName = packageInfo.versionName;
			
			return versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		return " ";
	}
	
	/**
     * 从服务器获取版本信息进行校检
     * @throws MalformedURLException 
     * @throws URISyntaxException 
     */
	private void checkVerson(){
		final long startTime = System.currentTimeMillis();
		 //启动子线程异步加载数据
		new Thread(){
			public void run() {
				Message msg = new Message();
				HttpURLConnection conn = null;
				try {
					//本机地址用localhost，但是如果用模拟器加载本机的地址时，可以用ip(10.0.2.2)来替换
					URL	url = new URL("http://10.0.2.2:8080/update.json");
					 conn = (HttpURLConnection) url.openConnection();
				    conn.setRequestMethod("GET");  //设置请求方法
					conn.setConnectTimeout(5000); //设定链接超时为5s
					conn.setReadTimeout(5000); //设定读取超时为5s
				   
				    
				    int responseCode = conn.getResponseCode();//获取响应码
				    if(responseCode == 200){ //若响应码为200，则表示链接服务器正常
				    	InputStream inputStream = conn.getInputStream(); //获取输入流
				    	String result = StreamUtils.ReadFromStream(inputStream);
				    	
				    	System.out.println("网络返回" + result);
				    	//解析json
				    	JSONObject jo = new JSONObject(result);
				    	mVersionName = jo.getString("versionName");
				    	mVersionCode = jo.getInt("versionCode");
				    	mDesc = jo.getString("description");
				    	mDownloadUrl = jo.getString("downloadUrl");
				    	
				    	System.out.println("版本描述：" +mDesc);
				    	
				    	if(mVersionCode > getVersionCode()){//判断是否有更新
				    		//服务器的VersionCode大于本地VersioCode
				    		//说明有更新，弹出升级对话框
				    		msg.what = CODE_UPDATE_DIALOG;
				    	
				    	}else{
				    		 msg.what = CODE_ENTER_HOME;
				    	}
				    }
				} catch (MalformedURLException e) {
					msg.what = CODE_URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					msg.what = CODE_NET_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					msg.what =CODE_JSON_ERROR;
					e.printStackTrace();
				}finally{
					
					long endTime = System.currentTimeMillis();
					long timeUsed = endTime - startTime; //访问网络所用要的时间
					
					if(timeUsed<2000){
						//强制休眠一段时间，保证闪屏页展示2秒钟
						try {
							Thread.sleep(2000-timeUsed);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					mHandler.sendMessage(msg);
					
					if(conn!= null){
						conn.disconnect(); //关闭网络
					}
				}
			}
		}.start();
		
		
		
	}
	
	 /**
     * 进入主页面
     */
	public void enterHome(){
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 升级对话框
	 */

	protected void showUpdateDailog() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("最新版本:" +mVersionName );
	//	builder.setMessage(mDesc);
	//	builder.setCancelable(false); //不让用户取消对话框，用户体验太差，尽量不要用
		builder.setPositiveButton("立即更新", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.out.println("立即更新");
				download();
				
			}
		});
		
		builder.setNegativeButton("以后再说", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();
			}
		});
		
		builder.setOnCancelListener(new OnCancelListener() {
			
			public void onCancel(DialogInterface dialog) {
				enterHome();
				
			}
		});
		
		builder.show();
	}
	
	/**
	 * 下载apk文件
	 */
	protected void download() {
		
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			
			tvProgess.setVisibility(View.VISIBLE);
			
			String target = Environment.getExternalStorageDirectory()+"/update.apk";
			
			
			HttpUtils http = new HttpUtils();
			HttpHandler handler = http.download(mDownloadUrl, target,new RequestCallBack<File>() {
				
				//下载文件进度
				public void onLoading(long total, long current, boolean isUploading) {
					super.onLoading(total, current, isUploading);
					System.out.println("下载进度" + current + "/" +total);
					
					tvProgess.setText("下载进度" +current*100/total + "%");
				}
				
				//下载成功
				public void onSuccess(ResponseInfo<File> arg0) {
					System.out.println("下载成功");
					
					//跳转到系统下载页面
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.setDataAndType(Uri.fromFile(arg0.result), 
							"application/vnd.android.package-archive");
					//startActivity(intent);
					startActivityForResult(intent, 0); //如果用户取消，会返回结果，回调方法onActivityResult
				}
				//下载失败
				public void onFailure(HttpException arg0, String arg1) {
					Toast.makeText(SplashActivity.this, "下载失败", Toast.LENGTH_SHORT)
					 .show();
				}
			}); 
		}else{
			Toast.makeText(SplashActivity.this, "没有SD卡", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	//如果用户取消安装，回调此方法
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {

			 enterHome();
			super.onActivityResult(requestCode, resultCode, data);
		}
}
