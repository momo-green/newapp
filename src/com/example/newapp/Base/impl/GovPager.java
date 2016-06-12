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
		tvTitle.setText("�˿ڹ���");
		setSlidingMenuEnable(true); //�򿪲����
		ibPhotos.setVisibility(View.GONE);

		
		TextView text = new TextView(mActivity);
		text.setText("����");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		
		//��FrameLayout�ж�̬��Ӳ���
		rlContent.addView(text);
	}

}
