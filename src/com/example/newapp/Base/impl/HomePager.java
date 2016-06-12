package com.example.newapp.Base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.newapp.MainActivity;
import com.example.newapp.Base.BasePager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class HomePager extends BasePager {

	public HomePager(Activity activity) {
		super(activity);
	}
	
	public void initData(){
		tvTitle.setText("�ǻ۱���");  //�޸ı���
		ibMenu.setVisibility(View.GONE); //���ز˵���ť
		ibPhotos.setVisibility(View.GONE);
		setSlidingMenuEnable(false); //�رղ����
		
		TextView text = new TextView(mActivity);
		text.setText("��ҳ");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		
		//��FrameLayout�ж�̬��Ӳ���
		rlContent.addView(text);
	}
	
	

}
