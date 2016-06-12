package com.example.newapp.Base;

import android.app.Activity;
import android.view.View;

public abstract class BaseMenuDetailPager {

	protected Activity mActivity;
	
	public View mRootView; //布局对象

	public BaseMenuDetailPager(Activity actiity){
		mActivity = actiity;
		mRootView = initView();
	}
	
	/*
	 * 初始化界面
	 */

	public abstract View initView();
	
	/*
	 * 初始化数据
	 */
	
	public void initData(){
		
	}
}
