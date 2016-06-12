package com.example.newapp.Base.menudetail;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.newapp.MainActivity;
import com.example.newapp.R;
import com.example.newapp.Base.BaseMenuDetailPager;
import com.example.newapp.Base.TabDetailPager;
import com.example.newapp.domian.NewData.NewsTabData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

/*
 * �˵�����ҳ-ר��
 */

public class NewsMenuDetailPager extends BaseMenuDetailPager implements OnPageChangeListener{

	private ViewPager mViewPager;
	private ArrayList<TabDetailPager> mPagerList;   //��ǩҳ��������
	private ArrayList<NewsTabData> mNewsTabData;    //ҳǩ��������
	private TabPageIndicator mIndicator;
	
	public NewsMenuDetailPager(Activity actiity,   
			ArrayList<NewsTabData> children) {
		super(actiity);
		mNewsTabData = children;
	}

	@Override
	public View initView() {
     
		View view = View.inflate(mActivity, R.layout.news_menu_detail, null);
		 mViewPager= (ViewPager) view.findViewById(R.id.vp_menu_detail);
		 

		 ViewUtils.inject(this, view);
		 
		 //��ʼ���Զ���ؼ�TabPageIndicator
		   mIndicator = (TabPageIndicator)
				  view.findViewById(R.id.indicator);
	       mIndicator.setOnPageChangeListener(this);
		 
		return view;
	}
	
	public void initData(){
	
		mPagerList = new ArrayList<TabDetailPager>();
		//��ʼ��ҳǩ
		for(int i=0;i<mNewsTabData.size();i++){
			TabDetailPager pager = new TabDetailPager(mActivity,mNewsTabData.get(i));
			mPagerList.add(pager);
			
		}
		
		mViewPager.setAdapter(new menuDetailAdapter());
	    mIndicator.setViewPager(mViewPager);  //��viewpager��mIndication����������������viewpager����adapter����ܵ���
	}
	
    //��ת��һ��ҳ��
	@OnClick(R.id.btn_text)
	public void nextPager(View view){
		int currentItem = mViewPager.getCurrentItem();
		mViewPager.setCurrentItem(++currentItem);
	}

	
	class menuDetailAdapter extends PagerAdapter{

		
		/*
		 * ��д�˷���������ҳ����⣬����ҳǩ��ʾ(non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#getPageTitle(int)
		 */
		public CharSequence getPageTitle(int position) {
			return mNewsTabData.get(position).title;
		}
		@Override
		public int getCount() {
			return mPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
		
			TabDetailPager pager = mPagerList.get(position);
            container.addView(pager.mRootView);
            pager.initData();
			return pager.mRootView;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		
		}
		
	}


	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		
	}

	@Override
	public void onPageSelected(int position) {
		MainActivity mainUi = (MainActivity) mActivity;
	    SlidingMenu slidingMenu = mainUi.getSlidingMenu();
	    
	    if(position == 0){
	    	slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	    }else
	    {
	    	slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
	    }
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		
	}
}
