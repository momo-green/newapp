package com.example.newapp.Base;



import com.example.newapp.MainActivity;
import com.example.newapp.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class BasePager {
	
	public Activity mActivity;
    public View mRootView;  //���ֶ���
    
    public TextView tvTitle; //�������
    
    public FrameLayout rlContent;//����
    
    public ImageButton ibMenu;
	public  ImageButton ibPhotos;  //��ͼ�л���ť
    
	public BasePager(Activity activity){
		mActivity = activity;
		initView();
		
	}
	/*
	 * ��ʼ������
	 */
	public void initView(){
		mRootView = View.inflate(mActivity, R.layout.base_pager, null);
		
		tvTitle = (TextView) mRootView.findViewById(R.id.tv_titile);
		rlContent = (FrameLayout) mRootView.findViewById(R.id.rl_content);
		ibMenu = (ImageButton) mRootView.findViewById(R.id.btn_menu);
		ibPhotos = (ImageButton) mRootView.findViewById(R.id.btn_photos);
		
		
		ibMenu.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				toggleSlidingMenu();
			}
		});
	}
	/*
	 * �л�SlidingMenu��״̬
	 */
	protected void toggleSlidingMenu() {
		MainActivity mainUi = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUi.getSlidingMenu();
		slidingMenu.toggle();//�л�״̬����ʾʱ���أ�����ʱ��ʾ
	}
	/*
	 * ��ʼ������
	 */
	public void initData(){
	}
	/*
	 * ���ò����������ر�
	 */
	
	public void setSlidingMenuEnable(boolean enable){
		MainActivity mainUi = (MainActivity) mActivity;
		
		SlidingMenu slidingMenu = mainUi.getSlidingMenu();  //��ȡ���������
		if(enable)
		{
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		}else{
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
}
