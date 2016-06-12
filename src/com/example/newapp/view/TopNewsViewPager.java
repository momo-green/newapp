package com.example.newapp.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TopNewsViewPager extends ViewPager{

	int startX;
	int startY;
	public TopNewsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TopNewsViewPager(Context context) {
		super(context);
	}

	/**
	 * �¼��ַ������󸸿ؼ��Ƿ������¼�
	 * 1���һ��������ǵ�һ��ҳ�棬��Ҫ���ؼ�����
	 * 2���󻮣����������һ��ҳ�棬��Ҫ���ؼ�����
	 * 3�����»�������Ҫ���ؼ�����
	 */
	public boolean dispatchTouchEvent(MotionEvent ev) {

		
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			getParent().requestDisallowInterceptTouchEvent(true);  //������
			
			startX = (int) ev.getRawX();
			startY =(int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			int endX = (int) ev.getRawX();
			int endY = (int) ev.getRawY();
			
			if(Math.abs(endX-startX)>Math.abs(endY-startY)){ //���һ���
				if(endX>startX){//�һ�
					if(getCurrentItem() == 0){ //��һ��ҳ�棬��Ҫ���ؼ�����
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}else{//��
					//���һ��ҳ�棬��Ҫ����
					if(getCurrentItem() == getAdapter().getCount()-1){ 
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}
				
			}else{//���»���
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			
			break;
		default:
			break;
		}
		
		return super.dispatchTouchEvent(ev);
	}
}
