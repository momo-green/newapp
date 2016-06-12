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
    public View mRootView;  //布局对象
    
    public TextView tvTitle; //标题对象
    
    public FrameLayout rlContent;//内容
    
    public ImageButton ibMenu;
	public  ImageButton ibPhotos;  //组图切换按钮
    
	public BasePager(Activity activity){
		mActivity = activity;
		initView();
		
	}
	/*
	 * 初始化布局
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
	 * 切换SlidingMenu的状态
	 */
	protected void toggleSlidingMenu() {
		MainActivity mainUi = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUi.getSlidingMenu();
		slidingMenu.toggle();//切换状态，显示时隐藏，隐藏时显示
	}
	/*
	 * 初始化数据
	 */
	public void initData(){
	}
	/*
	 * 设置侧边栏开启或关闭
	 */
	
	public void setSlidingMenuEnable(boolean enable){
		MainActivity mainUi = (MainActivity) mActivity;
		
		SlidingMenu slidingMenu = mainUi.getSlidingMenu();  //获取侧边栏对象
		if(enable)
		{
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		}else{
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
}
