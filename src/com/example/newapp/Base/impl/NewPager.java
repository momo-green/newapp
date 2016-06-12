package com.example.newapp.Base.impl;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newapp.MainActivity;
import com.example.newapp.Base.BaseMenuDetailPager;
import com.example.newapp.Base.BasePager;
import com.example.newapp.Base.TabDetailPager;
import com.example.newapp.Base.menudetail.InteractMenuDetailPager;
import com.example.newapp.Base.menudetail.NewsMenuDetailPager;
import com.example.newapp.Base.menudetail.PhotoMenuDetailPager;
import com.example.newapp.Base.menudetail.TopicMenuDetailPager;
import com.example.newapp.BaseFragment.LeftMenuFragment;
import com.example.newapp.GolbalContants.GlobalContants;
import com.example.newapp.Util.CacheUtils;
import com.example.newapp.domian.NewData;
import com.example.newapp.domian.NewData.NewsMenuData;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class NewPager extends BasePager {
	
	private NewData mNewsdata;
	private ArrayList<BaseMenuDetailPager> mPagers; // 4���˵�����ҳ�ļ���


	public NewPager(Activity activity) {
		super(activity);
	}
	
	public void initData(){
		tvTitle.setText("����");
		setSlidingMenuEnable(true);  //�򿪲����
		
		String cache = CacheUtils.getCache(GlobalContants.CATEGORIES_URL,
				     mActivity);
		
		if(!TextUtils.isEmpty(cache)){//���������ڣ�ֱ�ӽ������ݣ������������
			parseData(cache);
		}
			getDataFromServer();// ������û�л���, ����ȡ��������, ��֤��������
	}

	private void getDataFromServer() {
	   HttpUtils utils =new HttpUtils();
	   utils.send(HttpMethod.GET, GlobalContants.CATEGORIES_URL, 
			     new RequestCallBack<String>() {

		public void onSuccess(ResponseInfo responseInfo) {
			String result = (String) responseInfo.result;
			System.out.println("���ؽ��"+ result);
			parseData(result);
			
			//���û���
			CacheUtils.setCache(GlobalContants.CATEGORIES_URL, result, mActivity);
		}

		public void onFailure(HttpException error, String msg) {
			Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
			error.printStackTrace();
		}
	});
		
	}

	/*
	 * ������������
	 */
	protected void parseData(String result) {
		Gson gson = new Gson();
	    mNewsdata=gson.fromJson(result, NewData.class);
		System.out.println("���������"+mNewsdata);
		
		//ˢ�²����������
	    MainActivity mainUi = (MainActivity) mActivity; //��ȡ����������
	    LeftMenuFragment leftMenuFragment = 
	    		       mainUi.getLeftMenuFragment();
	    leftMenuFragment.setMenuData(mNewsdata);

	
	    //׼��4���˵�����ҳ
	    mPagers = new ArrayList<BaseMenuDetailPager>();
	    
	    mPagers.add(new NewsMenuDetailPager(mActivity,
	    		mNewsdata.data.get(0).children));
	    mPagers.add(new TopicMenuDetailPager(mActivity));
	    mPagers.add(new PhotoMenuDetailPager(mActivity,ibPhotos));
	    mPagers.add(new InteractMenuDetailPager(mActivity));
	    
	    setCurrentMenuDetailPager(0);
	    
	}

	/*
	 * ���õ�ǰ�˵�����ҳ
	 */
	public void setCurrentMenuDetailPager(int position){
		BaseMenuDetailPager pager = mPagers.get(position); //��ȡ��ǰҪ��ʾ�Ĳ˵�����ҳ
		rlContent.removeAllViews();//���֮ǰ�Ĳ���
		rlContent.addView(pager.mRootView); //���˵����鲼�����ø�֡����
		
		//���õ�ǰҳ�ı���
		NewsMenuData menuData = mNewsdata.data.get(position);
		tvTitle.setText(menuData.title);
		
		pager.initData();  //��ʼ����ǰҳ�������
		
		if(pager instanceof PhotoMenuDetailPager){
			ibPhotos.setVisibility(View.VISIBLE);
		}else{
			ibPhotos.setVisibility(View.GONE);
		}
		
	}
}
