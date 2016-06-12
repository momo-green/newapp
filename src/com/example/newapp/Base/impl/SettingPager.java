package com.example.newapp.Base.impl;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;

import com.example.newapp.R;
import com.example.newapp.Base.BasePager;
import com.example.newapp.view.SettingItemView;

public class SettingPager extends BasePager {

	
	private SettingItemView sivUpdate;// ��������
//	private SharedPreferences mPref; //�������洢
	
	public SettingPager(Activity activity) {
		super(activity);
		
	}
	
	public void initData() {
		tvTitle.setText("����");
		ibMenu.setVisibility(View.GONE); // ���ز˵���ť
		ibPhotos.setVisibility(View.GONE);
		setSlidingMenuEnable(false);
		
		

	}

	@Override
	public void initView() {
		super.initView();
		View view = View.inflate(mActivity, R.layout.activity_setting, null);
		rlContent.removeAllViews();// ���֮ǰ�Ĳ���
		rlContent.addView(view);
		
	}

}
