package com.example.newapp.Base;

import android.app.Activity;
import android.view.View;

public abstract class BaseMenuDetailPager {

	protected Activity mActivity;
	
	public View mRootView; //���ֶ���

	public BaseMenuDetailPager(Activity actiity){
		mActivity = actiity;
		mRootView = initView();
	}
	
	/*
	 * ��ʼ������
	 */

	public abstract View initView();
	
	/*
	 * ��ʼ������
	 */
	
	public void initData(){
		
	}
}
