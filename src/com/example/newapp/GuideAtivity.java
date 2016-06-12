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
                
                
                 * edit() ����sharePreferences���ڲ��ӿ�SharedPreferences.Editor;
                 * putBoolean() ����һ��booleanֵ
                 * commit() ����
                 * 
                 
                sp.edit().putBoolean("is_user_guide_showed", true).commit();*/
				PrefUtils.setBoolean(GuideAtivity.this, "is_user_guide_showed", true);
				startActivity(new Intent(GuideAtivity.this,MainActivity.class));
				finish();
			}
		});
	}
	
	/*
	 * ��ʼ������
	 */
	public void initViews(){
		
		  mImageViewList = new ArrayList<ImageView>();
			
			for(int i=0;i<mImageIds.length;i++){
				ImageView image = new ImageView(this);
				image.setBackgroundResource(mImageIds[i]); //��������ҳ
				mImageViewList.add(image);
			}
			
			//��ʼ������ҳ��СԲ��
			
			for(int i= 0;i<mImageIds.length;i++){
				View point = new View(this);
				point.setBackgroundResource(R.drawable.shape_point_gray);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15,15);
				if(i>0){
					params.leftMargin=15; //����Բ��ļ��
				}
				point.setLayoutParams(params);  //����Բ���С
				pointGary.addView(point);  //��Բ����Ӹ����Բ���
			}
			
		
		 
		 //��ȡ��ͼ��,��layout�����¼����м���
		 pointGary.getViewTreeObserver().addOnGlobalLayoutListener(
				 new OnGlobalLayoutListener() {
			
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				System.out.println("layout ����");
				
				pointGary.getViewTreeObserver()
				        .removeGlobalOnLayoutListener(this);
				
				 mpointWidth = pointGary.getChildAt(1).getLeft()
						    -pointGary.getChildAt(0).getLeft();
				 System.out.println("Բ�����"+ mpointWidth);
			}
		});
		}
		
		
		class GuideAdapter extends PagerAdapter{


			//��ȡҪ�����Ŀؼ�������
			public int getCount() {
				return mImageIds.length;
			} 
			
			//�ж���ʾ���Ƿ���ͬһ��ͼƬ
			public boolean isViewFromObject(View arg0, Object arg1) {

				return arg0 == arg1;
			}
			//��ʼ��
			public Object instantiateItem(ViewGroup container,int position){
				container.addView((View) mImageViewList.get(position));
				return mImageViewList.get(position);
			}
			
			public void destroyItem(ViewGroup container,int position,Object object){
			    container.removeView((View) object);
			}
			
		}
		
		class GuidePageListener implements OnPageChangeListener{

			//����״̬�����ı�
			public void onPageScrollStateChanged(int state) {
				
			}
          
			//�����¼�
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
//				System.out.println("��ǰλ��" + position +";�ٷֱ�" 
//					 +positionOffset +";�ƶ�����"+positionOffsetPixels);
				int len= (int) (mpointWidth * positionOffset)+position *mpointWidth;
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) 
						          pointRed.getLayoutParams();  //���õ�ǰ���Ĳ��ֲ���
				params.leftMargin=len;
				
				pointRed.setLayoutParams(params);
				
				
			
			}
			//ĳ��ҳ�汻ѡ��
			public void onPageSelected(int position) {
				if(position == mImageIds.length-1){
					btnStart.setVisibility(View.VISIBLE);
				}else{
					btnStart.setVisibility(View.INVISIBLE);
				}
			}
			
		}
}
