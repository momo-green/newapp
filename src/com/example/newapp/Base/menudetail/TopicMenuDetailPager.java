package com.example.newapp.Base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.newapp.Base.BaseMenuDetailPager;

/*
 * 菜单详情页-组图
 */

public class TopicMenuDetailPager extends BaseMenuDetailPager{

	public TopicMenuDetailPager(Activity actiity) {
		super(actiity);
	}

	@Override
	public View initView() {

		TextView text = new TextView(mActivity);
		text.setText("菜单详情页-专题");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		
		return text;
	}

}
