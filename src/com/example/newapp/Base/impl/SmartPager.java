package com.example.newapp.Base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.newapp.Base.BasePager;

public class SmartPager extends BasePager {

	public SmartPager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
	
	public void initData(){
		tvTitle.setText("����");
		setSlidingMenuEnable(true);
		ibPhotos.setVisibility(View.GONE);

		TextView  text = new TextView(mActivity);
		text.setText("�ǻ۷���");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		
		rlContent.addView(text);
	}

}
