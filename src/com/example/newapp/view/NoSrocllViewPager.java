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
  
	//��ʾ�¼��Ƿ����أ�����false��ʾ������
	public boolean onInterceptTouchEvent(MotionEvent ev) {
	
		return false;
	}
	
	/*
	 * ��дonTouchEvent�¼���ʲô��������
	 * (non-Javadoc)
	 * @see android.support.v4.view.ViewPager#onTouchEvent(android.view.MotionEvent)
	 */
    public boolean onTouchEvent(MotionEvent ev) {
    	return false;
    }
}
