package com.example.newapp.BaseFragment;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.example.newapp.R;
import com.example.newapp.SettingActivity;
import com.example.newapp.Base.BasePager;
import com.example.newapp.Base.impl.GovPager;
import com.example.newapp.Base.impl.HomePager;
import com.example.newapp.Base.impl.NewPager;
import com.example.newapp.Base.impl.SettingPager;
import com.example.newapp.Base.impl.SmartPager;
import com.example.newapp.view.SettingItemView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ContentFragment extends baseFragment {

	@ViewInject(R.id.bar_rg)
	private RadioGroup rgGroup;
	
	@ViewInject(R.id.content_vp)	
	private ViewPager mViewPager;
	private ArrayList<BasePager> pagerList;
	
	public View initView() {
		View view = View.inflate(mActivity,
				R.layout.fragment_content, null);
	//	rgGroup = (RadioGroup) view.findViewById(R.id.bar_rg);
		ViewUtils.inject(this, view);
      return view;
	}

	public void initData(){
		rgGroup.check(R.id.home_rb); //����Ĭ�Ͻ���
		
		//��ʼ��5����ҳ��
		
		pagerList = new ArrayList<BasePager>();
//		for(int i=0;i<5;i++){
//  		   BasePager pager = new BasePager(mActivity);
//  		   pagerList.add(pager);
//		}
		pagerList.add(new HomePager(mActivity));
		pagerList.add(new NewPager(mActivity));
		pagerList.add(new SmartPager(mActivity));
		pagerList.add(new GovPager(mActivity));
		pagerList.add(new SettingPager(mActivity));
		
	    mViewPager.setAdapter(new ContentAdapter());
	    
	    //����RadioGroupѡ������¼�
	    rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.home_rb:
					mViewPager.setCurrentItem(0, false);
					break;
				case R.id.new_rb:
					mViewPager.setCurrentItem(1, false);
					break;
				case R.id.smart_rb:
					mViewPager.setCurrentItem(2, false);
					break;
				case R.id.gov_rb:
					mViewPager.setCurrentItem(3, false);
					break;
				case R.id.setting_rb:
					mViewPager.setCurrentItem(4, false);
					break;
				default:
					break;
				}
			}
		});

	    mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				pagerList.get(position).initData(); //��ȡ��ǰ��ѡ�е�ҳ�棬��ʼ����ҳ������
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				
			}
		});
		pagerList.get(0).initData();  //��ʼ����ҳ����

	}
	
	class ContentAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return pagerList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		//��ʼ��
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pager = pagerList.get(position);
			container.addView(pager.mRootView);
			
			//pager.initData(); //��ʼ������  ��Ҫ���ڴ˴���ʼ������, �����Ԥ������һ��ҳ��
			
			return pager.mRootView;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			 container.removeView((View) object);
		
		}
	}
	
	/*
	 * ��ȡ��������ҳ��
	 */
	public NewPager getNewPager(){
		return (NewPager) pagerList.get(1);
		
	}
	
}
