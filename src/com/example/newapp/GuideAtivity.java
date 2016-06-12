package com.example.newapp;



import java.util.ArrayList;

import com.example.newapp.Util.PrefUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class GuideAtivity extends Activity {
	
	private static final int[] mImageIds = new int[]{R.drawable.guide_1,
        R.drawable.guide_2,R.drawable.guide_3};

		private ViewPager vpGuide;
		private LinearLayout pointGary;
	    private	ArrayList<ImageView> mImageViewList;
	    private View pointRed;
	    private Button btnStart;
	    
	    private int mpointWidth;

	

	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		 setContentView(R.layout.activity_guide);
		 
		 vpGuide =  (ViewPager) findViewById(R.id.guide_vp);
		 pointGary = (LinearLayout) findViewById(R.id.point_gary_ll);
		 pointRed = findViewById(R.id.red_point_vw);
		 btnStart = (Button) findViewById(R.id.start_bt);
		 
		 initViews();
		 vpGuide.setAdapter(new GuideAdapter());
		 
		 vpGuide.setOnPageChangeListener(new GuidePageListener());
		 
		 btnStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
              /*  SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
                
                
                 * edit() 返回sharePreferences的内部接口SharedPreferences.Editor;
                 * putBoolean() 保存一个boolean值
                 * commit() 保存
                 * 
                 
                sp.edit().putBoolean("is_user_guide_showed", true).commit();*/
				PrefUtils.setBoolean(GuideAtivity.this, "is_user_guide_showed", true);
				startActivity(new Intent(GuideAtivity.this,MainActivity.class));
				finish();
			}
		});
	}
	
	/*
	 * 初始化界面
	 */
	public void initViews(){
		
		  mImageViewList = new ArrayList<ImageView>();
			
			for(int i=0;i<mImageIds.length;i++){
				ImageView image = new ImageView(this);
				image.setBackgroundResource(mImageIds[i]); //设置引导页
				mImageViewList.add(image);
			}
			
			//初始化引导页的小圆点
			
			for(int i= 0;i<mImageIds.length;i++){
				View point = new View(this);
				point.setBackgroundResource(R.drawable.shape_point_gray);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15,15);
				if(i>0){
					params.leftMargin=15; //设置圆点的间隔
				}
				point.setLayoutParams(params);  //设置圆点大小
				pointGary.addView(point);  //将圆点添加给线性布局
			}
			
		
		 
		 //获取视图树,对layout结束事件进行监听
		 pointGary.getViewTreeObserver().addOnGlobalLayoutListener(
				 new OnGlobalLayoutListener() {
			
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				System.out.println("layout 结束");
				
				pointGary.getViewTreeObserver()
				        .removeGlobalOnLayoutListener(this);
				
				 mpointWidth = pointGary.getChildAt(1).getLeft()
						    -pointGary.getChildAt(0).getLeft();
				 System.out.println("圆点距离"+ mpointWidth);
			}
		});
		}
		
		
		class GuideAdapter extends PagerAdapter{


			//获取要滑动的控件的数量
			public int getCount() {
				return mImageIds.length;
			} 
			
			//判断显示的是否是同一张图片
			public boolean isViewFromObject(View arg0, Object arg1) {

				return arg0 == arg1;
			}
			//初始化
			public Object instantiateItem(ViewGroup container,int position){
				container.addView((View) mImageViewList.get(position));
				return mImageViewList.get(position);
			}
			
			public void destroyItem(ViewGroup container,int position,Object object){
			    container.removeView((View) object);
			}
			
		}
		
		class GuidePageListener implements OnPageChangeListener{

			//滑动状态发生改变
			public void onPageScrollStateChanged(int state) {
				
			}
          
			//滑动事件
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
//				System.out.println("当前位置" + position +";百分比" 
//					 +positionOffset +";移动距离"+positionOffsetPixels);
				int len= (int) (mpointWidth * positionOffset)+position *mpointWidth;
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) 
						          pointRed.getLayoutParams();  //设置当前红点的布局参数
				params.leftMargin=len;
				
				pointRed.setLayoutParams(params);
				
				
			
			}
			//某个页面被选中
			public void onPageSelected(int position) {
				if(position == mImageIds.length-1){
					btnStart.setVisibility(View.VISIBLE);
				}else{
					btnStart.setVisibility(View.INVISIBLE);
				}
			}
			
		}
}
