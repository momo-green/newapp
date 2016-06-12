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
	protected static final int CODE_ENTER_HOME = 4; //������ҳ��
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
				Toast.makeText(SplashActivity.this, "url����", Toast.LENGTH_SHORT)
				      .show();
				enterHome();

				break;
			case CODE_NET_ERROR:
				Toast.makeText(SplashActivity.this, "�������", Toast.LENGTH_SHORT)
			      .show();
				enterHome();

				break;
			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "���ݴ���", Toast.LENGTH_SHORT)
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
        tvVersion.setText("�汾�� �� "+ getVersionName());
        
        //checkVerson();
      
    }
    
   private void startAnim(){
    	
    	//��������
    	AnimationSet set = new AnimationSet(false);
    	
    	//��ת����
    	RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, 
    			                     Animation.RELATIVE_TO_SELF, 0.5f);
    	
    	rotate.setDuration(1000); //����ʱ��
    	rotate.setFillAfter(true);//���ֶ���״̬
    	//���Ŷ���
    	ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1,
    			Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    	
    	scale.setDuration(2000);
    	scale.setFillAfter(true);
    	
    	//͸���ȶ���Ч��
    	
    	AlphaAnimation alpha = new AlphaAnimation(0,1);
    	
    	set.addAnimation(rotate);
    	set.addAnimation(scale);
    	set.addAnimation(alpha);
     
    	//���ö�������
    	set.setAnimationListener(new AnimationListener() {
			
    		//��ʼ����
			public void onAnimationStart(Animation animation) {
				
			}
			
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			//��������
			public void onAnimationEnd(Animation animation) {
			
				jnmpNexPage();
			    //finish();
			}
		});
    	
    	rlRoot.startAnimation(set);
    	
    }
 
     //��ת��һ��ҳ��
     public void jnmpNexPage(){
    	 //�ж�֮ǰ��û����ʾ��������ҳ
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
 	 * ��ȡ�汾��
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
	 * ��ȡ�汾��
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
     * �ӷ�������ȡ�汾��Ϣ����У��
     * @throws MalformedURLException 
     * @throws URISyntaxException 
     */
	private void checkVerson(){
		final long startTime = System.currentTimeMillis();
		 //�������߳��첽��������
		new Thread(){
			public void run() {
				Message msg = new Message();
				HttpURLConnection conn = null;
				try {
					//������ַ��localhost�����������ģ�������ر����ĵ�ַʱ��������ip(10.0.2.2)���滻
					URL	url = new URL("http://10.0.2.2:8080/update.json");
					 conn = (HttpURLConnection) url.openConnection();
				    conn.setRequestMethod("GET");  //�������󷽷�
					conn.setConnectTimeout(5000); //�趨���ӳ�ʱΪ5s
					conn.setReadTimeout(5000); //�趨��ȡ��ʱΪ5s
				   
				    
				    int responseCode = conn.getResponseCode();//��ȡ��Ӧ��
				    if(responseCode == 200){ //����Ӧ��Ϊ200�����ʾ���ӷ���������
				    	InputStream inputStream = conn.getInputStream(); //��ȡ������
				    	String result = StreamUtils.ReadFromStream(inputStream);
				    	
				    	System.out.println("���緵��" + result);
				    	//����json
				    	JSONObject jo = new JSONObject(result);
				    	mVersionName = jo.getString("versionName");
				    	mVersionCode = jo.getInt("versionCode");
				    	mDesc = jo.getString("description");
				    	mDownloadUrl = jo.getString("downloadUrl");
				    	
				    	System.out.println("�汾������" +mDesc);
				    	
				    	if(mVersionCode > getVersionCode()){//�ж��Ƿ��и���
				    		//��������VersionCode���ڱ���VersioCode
				    		//˵���и��£����������Ի���
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
					long timeUsed = endTime - startTime; //������������Ҫ��ʱ��
					
					if(timeUsed<2000){
						//ǿ������һ��ʱ�䣬��֤����ҳչʾ2����
						try {
							Thread.sleep(2000-timeUsed);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					mHandler.sendMessage(msg);
					
					if(conn!= null){
						conn.disconnect(); //�ر�����
					}
				}
			}
		}.start();
		
		
		
	}
	
	 /**
     * ������ҳ��
     */
	public void enterHome(){
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * �����Ի���
	 */

	protected void showUpdateDailog() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("���°汾:" +mVersionName );
	//	builder.setMessage(mDesc);
	//	builder.setCancelable(false); //�����û�ȡ���Ի����û�����̫�������Ҫ��
		builder.setPositiveButton("��������", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.out.println("��������");
				download();
				
			}
		});
		
		builder.setNegativeButton("�Ժ���˵", new OnClickListener() {
			
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
	 * ����apk�ļ�
	 */
	protected void download() {
		
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			
			tvProgess.setVisibility(View.VISIBLE);
			
			String target = Environment.getExternalStorageDirectory()+"/update.apk";
			
			
			HttpUtils http = new HttpUtils();
			HttpHandler handler = http.download(mDownloadUrl, target,new RequestCallBack<File>() {
				
				//�����ļ�����
				public void onLoading(long total, long current, boolean isUploading) {
					super.onLoading(total, current, isUploading);
					System.out.println("���ؽ���" + current + "/" +total);
					
					tvProgess.setText("���ؽ���" +current*100/total + "%");
				}
				
				//���سɹ�
				public void onSuccess(ResponseInfo<File> arg0) {
					System.out.println("���سɹ�");
					
					//��ת��ϵͳ����ҳ��
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.setDataAndType(Uri.fromFile(arg0.result), 
							"application/vnd.android.package-archive");
					//startActivity(intent);
					startActivityForResult(intent, 0); //����û�ȡ�����᷵�ؽ�����ص�����onActivityResult
				}
				//����ʧ��
				public void onFailure(HttpException arg0, String arg1) {
					Toast.makeText(SplashActivity.this, "����ʧ��", Toast.LENGTH_SHORT)
					 .show();
				}
			}); 
		}else{
			Toast.makeText(SplashActivity.this, "û��SD��", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	//����û�ȡ����װ���ص��˷���
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {

			 enterHome();
			super.onActivityResult(requestCode, resultCode, data);
		}
}
