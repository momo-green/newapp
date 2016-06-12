package com.example.newapp.Base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.newapp.Base.BasePager;

public class GovPager extends BasePager {

	public GovPager(Activity activity) {
		super(activity);
	}
	
	public void initData(){
		tvTitle.setText("人口管理");
		setSlidingMenuEnable(true); //打开侧边栏
		ibPhotos.setVisibility(View.GONE);

		
		TextView text = new TextView(mActivity);
		text.setText("政务");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		
		//向FrameLayout中动态添加布局
		rlContent.addView(text);
	}

}
