package com.example.newapp.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoSrocllViewPager extends ViewPager{

	public NoSrocllViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoSrocllViewPager(Context context) {
		super(context);
	}
  
	//表示事件是否拦截，返回false表示不拦截
	public boolean onInterceptTouchEvent(MotionEvent ev) {
	
		return false;
	}
	
	/*
	 * 重写onTouchEvent事件，什么都不用做
	 * (non-Javadoc)
	 * @see android.support.v4.view.ViewPager#onTouchEvent(android.view.MotionEvent)
	 */
    public boolean onTouchEvent(MotionEvent ev) {
    	return false;
    }
}
