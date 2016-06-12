package com.example.newapp.Base.impl;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;

import com.example.newapp.R;
import com.example.newapp.Base.BasePager;
import com.example.newapp.view.SettingItemView;

public class SettingPager extends BasePager {

	
	private SettingItemView sivUpdate;// 设置升级
//	private SharedPreferences mPref; //轻量级存储
	
	public SettingPager(Activity activity) {
		super(activity);
		
	}
	
	public void initData() {
		tvTitle.setText("设置");
		ibMenu.setVisibility(View.GONE); // 隐藏菜单按钮
		ibPhotos.setVisibility(View.GONE);
		setSlidingMenuEnable(false);
		
		

	}

	@Override
	public void initView() {
		super.initView();
		View view = View.inflate(mActivity, R.layout.activity_setting, null);
		rlContent.removeAllViews();// 清除之前的布局
		rlContent.addView(view);
		
	}

}
