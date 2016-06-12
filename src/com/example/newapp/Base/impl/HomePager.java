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
		tvTitle.setText("智慧北京");  //修改标题
		ibMenu.setVisibility(View.GONE); //隐藏菜单按钮
		ibPhotos.setVisibility(View.GONE);
		setSlidingMenuEnable(false); //关闭侧边栏
		
		TextView text = new TextView(mActivity);
		text.setText("首页");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		
		//向FrameLayout中动态添加布局
		rlContent.addView(text);
	}
	
	

}
