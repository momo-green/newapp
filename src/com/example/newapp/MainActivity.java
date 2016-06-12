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
	  setBehindContentView(R.layout.left_menu); //���ò��������
	  
	  SlidingMenu slidingMenu = getSlidingMenu(); // ��ȡ���������
	  slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//����ȫ������
	  
	 /* slidingMenu.setSecondaryMenu(R.layout.right_menu); //�����Ҳ����
	  slidingMenu.setMode(SlidingMenu.LEFT_RIGHT); //����չ��ģʽ
	  */
	  slidingMenu.setBehindOffset(300);//����Ԥ����Ļ�Ŀ��
	  
	  initFragment();
	  
     }
   
   /*
    * ��ʼ��fragment����fragment�������
    * 
    */
   private void initFragment(){
	   FragmentManager fm = getSupportFragmentManager(); //��ȡʵ��
	   FragmentTransaction transaction = fm.beginTransaction(); //��������
	   
	   transaction.replace(R.id.fl_content, new ContentFragment(),
			   FRAGMENT_CONTENT	   );
	   transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(),
			   FRAGMENT_LEFT_MENU );
	   
	   transaction.commit(); //�ύ����
   }
   
   //��ȡ�����
   public LeftMenuFragment getLeftMenuFragment(){
	   FragmentManager fm = getSupportFragmentManager();
	   LeftMenuFragment fragment = (LeftMenuFragment)
			       fm.findFragmentByTag(FRAGMENT_LEFT_MENU);
	return fragment;
   }
   
   //��ȡ��ҳ��
   public ContentFragment getContentFragment(){
	   FragmentManager fm = getSupportFragmentManager();
	   ContentFragment fragment = (ContentFragment)
			       fm.findFragmentByTag(FRAGMENT_CONTENT);
	return fragment;
   }
   
}
