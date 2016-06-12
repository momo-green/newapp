package com.example.newapp;

import com.example.newapp.BaseFragment.ContentFragment;
import com.example.newapp.BaseFragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

public class MainActivity extends SlidingFragmentActivity {
   private static final String FRAGMENT_LEFT_MENU ="fragment_left_menu";
   private static final String FRAGMENT_CONTENT ="fragment_content";

@Override
  public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  requestWindowFeature(Window.FEATURE_NO_TITLE);
	  setContentView(R.layout.activity_main);
	  setBehindContentView(R.layout.left_menu); //设置侧边栏布局
	  
	  SlidingMenu slidingMenu = getSlidingMenu(); // 获取侧边栏对象
	  slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//设置全屏触摸
	  
	 /* slidingMenu.setSecondaryMenu(R.layout.right_menu); //设置右侧边栏
	  slidingMenu.setMode(SlidingMenu.LEFT_RIGHT); //设置展现模式
	  */
	  slidingMenu.setBehindOffset(300);//设置预留屏幕的宽度
	  
	  initFragment();
	  
     }
   
   /*
    * 初始化fragment，将fragment数据填充
    * 
    */
   private void initFragment(){
	   FragmentManager fm = getSupportFragmentManager(); //获取实例
	   FragmentTransaction transaction = fm.beginTransaction(); //开启事务
	   
	   transaction.replace(R.id.fl_content, new ContentFragment(),
			   FRAGMENT_CONTENT	   );
	   transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(),
			   FRAGMENT_LEFT_MENU );
	   
	   transaction.commit(); //提交事务
   }
   
   //获取侧边栏
   public LeftMenuFragment getLeftMenuFragment(){
	   FragmentManager fm = getSupportFragmentManager();
	   LeftMenuFragment fragment = (LeftMenuFragment)
			       fm.findFragmentByTag(FRAGMENT_LEFT_MENU);
	return fragment;
   }
   
   //获取主页面
   public ContentFragment getContentFragment(){
	   FragmentManager fm = getSupportFragmentManager();
	   ContentFragment fragment = (ContentFragment)
			       fm.findFragmentByTag(FRAGMENT_CONTENT);
	return fragment;
   }
   
}
