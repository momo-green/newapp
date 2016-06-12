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
	private ArrayList<BaseMenuDetailPager> mPagers; // 4个菜单详情页的集合


	public NewPager(Activity activity) {
		super(activity);
	}
	
	public void initData(){
		tvTitle.setText("新闻");
		setSlidingMenuEnable(true);  //打开侧边栏
		
		String cache = CacheUtils.getCache(GlobalContants.CATEGORIES_URL,
				     mActivity);
		
		if(!TextUtils.isEmpty(cache)){//如果缓存存在，直接解析数据，无需访问网络
			parseData(cache);
		}
			getDataFromServer();// 不管有没有缓存, 都获取最新数据, 保证数据最新
	}

	private void getDataFromServer() {
	   HttpUtils utils =new HttpUtils();
	   utils.send(HttpMethod.GET, GlobalContants.CATEGORIES_URL, 
			     new RequestCallBack<String>() {

		public void onSuccess(ResponseInfo responseInfo) {
			String result = (String) responseInfo.result;
			System.out.println("返回结果"+ result);
			parseData(result);
			
			//设置缓存
			CacheUtils.setCache(GlobalContants.CATEGORIES_URL, result, mActivity);
		}

		public void onFailure(HttpException error, String msg) {
			Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
			error.printStackTrace();
		}
	});
		
	}

	/*
	 * 解析网络数据
	 */
	protected void parseData(String result) {
		Gson gson = new Gson();
	    mNewsdata=gson.fromJson(result, NewData.class);
		System.out.println("解析结果："+mNewsdata);
		
		//刷新侧边栏的数据
	    MainActivity mainUi = (MainActivity) mActivity; //获取主函数对象
	    LeftMenuFragment leftMenuFragment = 
	    		       mainUi.getLeftMenuFragment();
	    leftMenuFragment.setMenuData(mNewsdata);

	
	    //准备4个菜单详情页
	    mPagers = new ArrayList<BaseMenuDetailPager>();
	    
	    mPagers.add(new NewsMenuDetailPager(mActivity,
	    		mNewsdata.data.get(0).children));
	    mPagers.add(new TopicMenuDetailPager(mActivity));
	    mPagers.add(new PhotoMenuDetailPager(mActivity,ibPhotos));
	    mPagers.add(new InteractMenuDetailPager(mActivity));
	    
	    setCurrentMenuDetailPager(0);
	    
	}

	/*
	 * 设置当前菜单详情页
	 */
	public void setCurrentMenuDetailPager(int position){
		BaseMenuDetailPager pager = mPagers.get(position); //获取当前要显示的菜单详情页
		rlContent.removeAllViews();//清除之前的布局
		rlContent.addView(pager.mRootView); //将菜单详情布局设置个帧布局
		
		//设置当前页的标题
		NewsMenuData menuData = mNewsdata.data.get(position);
		tvTitle.setText(menuData.title);
		
		pager.initData();  //初始化当前页面的数据
		
		if(pager instanceof PhotoMenuDetailPager){
			ibPhotos.setVisibility(View.VISIBLE);
		}else{
			ibPhotos.setVisibility(View.GONE);
		}
		
	}
}
