package com.example.newapp.Base;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newapp.NewsDetailActivity;
import com.example.newapp.R;
import com.example.newapp.R.color;
import com.example.newapp.Base.RefreshListView.onRefreshListener;
import com.example.newapp.Base.impl.GovPager;
import com.example.newapp.GolbalContants.GlobalContants;
import com.example.newapp.Util.CacheUtils;
import com.example.newapp.Util.PrefUtils;
import com.example.newapp.domian.NewData.NewsTabData;
import com.example.newapp.domian.TabData;
import com.example.newapp.domian.TabData.TabNewsData;
import com.example.newapp.domian.TabData.TopNewsData;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

/*
 * ҳǩ����ҳ
 */
public class TabDetailPager extends BaseMenuDetailPager implements OnPageChangeListener {

	NewsTabData mTabData;
	private TextView tvText;
	
	private String mUrl;
	private TabData mTabDetailData;
	
	@ViewInject(R.id.vp_news)
	private ViewPager mViewPager;
	
	@ViewInject(R.id.news_title)
	private TextView tvTitle;  //ͷ�����ŵı���
	
	private ArrayList<TopNewsData> mTopNewsList;//ͷ���������ݼ���
	
	@ViewInject(R.id.indicator)
	private CirclePageIndicator mIndicator; //ͷ������λ��ָʾ��
	
	@ViewInject(R.id.lv_list)
	private RefreshListView lvList;//�����б�
	private ArrayList<TabNewsData> mNewList;//�����б���
	private NewsAdapter mNewsAdapter;
	private String mMoreUrl; //����ҳ��ĵ�ַ
	
	private Handler mHandler;
	
	public TabDetailPager(Activity actiity, NewsTabData newsTabData) {
		super(actiity);
		
		mTabData = newsTabData;
		
		mUrl = GlobalContants.SERVER_URL + mTabData.url;
	}

	@Override
	public View initView() {

		View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);
	    //����ͷ����
		View headerView = View.inflate(mActivity, R.layout.list_header_topnews, null);
		ViewUtils.inject(this, view);
		ViewUtils.inject(this,headerView);
		
		//��ͷ��������ͷ���ֵ���ʽ�Ӹ�listview
		lvList.addHeaderView(headerView);
		//��������ˢ�µļ���
		lvList.setOnRefreshListener(new onRefreshListener() {
			
			@Override
			public void onRefresh() {
				getDataFromServer();
			}

			@Override
			public void onLoadMore() {
			if(mMoreUrl != null){
				getMoreDataFromServer();
				
			}else{
				Toast.makeText(mActivity, "���һҳ��", Toast.LENGTH_SHORT)
				    .show();
				lvList.onRefreshComplete(false);//������ظ���Ĳ���
			   }
				
			}
		});
		
		lvList.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View view, 
					int position,long id) {
				System.out.println("�����"+position);
				
				String ids = PrefUtils.getString(mActivity, "read_ids", "");
				String readId = mNewList.get(position).id;
				if(!ids.contains(readId)){
					ids = ids+ readId + ",";
					PrefUtils.setString(mActivity, "read_ids", ids);
				}
			//	mNewsAdapter.notifyDataSetChanged();
				changeReadState(view);//ʵ�־ֲ�����ˢ��,���view���Ǳ������item���ֶ���
			
			    //��ת��������ҳ
				Intent intent = new Intent();
				intent.setClass(mActivity, NewsDetailActivity.class);
				intent.putExtra("url",mNewList.get(position).url);
				mActivity.startActivity(intent);
			}
			
		});
		return view;
	}

	 /*
	   * �ı��Ѷ����ŵ���ɫ
	   */
	  private void changeReadState(View view){
		  TextView tvTitle = (TextView) view.findViewById(R.id.tv_news_title);
		  tvTitle.setTextColor(Color.GRAY);
		  
	  }

	
	@Override
	public void initData() {
		
		String cache = CacheUtils.getCache(mUrl, mActivity);
		
		if(!TextUtils.isEmpty(cache)){
			parseData(cache, false);
		}
		
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mUrl,new RequestCallBack<String>(){

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result =(String)responseInfo.result;
				System.out.println("ҳǩ����ҳ���ؽ����" +result);
				
				parseData(result,false);
				
				lvList.onRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				error.printStackTrace();
				
				lvList.onRefreshComplete(false);
			}
			
		  });
		}

	protected void parseData(String result,boolean isMore ) {
		 Gson gson = new Gson();
		 mTabDetailData = gson.fromJson(result,TabData.class);
		 System.out.println("ҳǩ���������" + mTabDetailData);
		 
		 String more = mTabDetailData.data.more;
		 
		 //������һҳ����
		 if(!TextUtils.isEmpty(more)){
			 mMoreUrl = GlobalContants.SERVER_URL + more;
		 }else{
			 mMoreUrl = null;
		 }
		 
		 if(!isMore){
			 mTopNewsList = mTabDetailData.data.topnews;
			 
			 mNewList = mTabDetailData.data.news;
			 
			 if(mTopNewsList != null){
			 mViewPager.setAdapter(new TopNewsAdapter());
		     mIndicator.setViewPager(mViewPager); 
		     mIndicator.setSnap(true); //������ʾ
		     
		     mIndicator.setOnPageChangeListener(this);
		     mIndicator.onPageSelected(0); //��ָʾ�����¶�λ����һ����
		     
		     tvTitle.setText(mTopNewsList.get(0).title);
			 }
			 
		     if(mNewList != null){
		       mNewsAdapter = new NewsAdapter();
		      lvList.setAdapter(mNewsAdapter);
		     }
		     
		     //�Զ��ֲ�����ʾ
		     if(mHandler == null){
		    	 mHandler = new Handler(){
		    		@Override
		    		public void handleMessage(android.os.Message msg) {

		    			int currentItem = mViewPager.getCurrentItem();
		    			
		    			if(currentItem < mTopNewsList.size() -1){
		    				currentItem++;
		    			}else{
		    				currentItem = 0;
		    			}
		    			mViewPager.setCurrentItem(currentItem);
		    		    mHandler.sendEmptyMessageDelayed(0, 3000);//�����ӳ�3���ӣ��γ�ѭ��
		    			
		    		}; 
		    	 };
		    	 
		    	 mHandler.sendEmptyMessageDelayed(0, 3000);//�ӳ�3���Ӻ���Ϣ
		     }
		 }else{//����Ǽ�����һҳ����Ҫ������׷�Ӹ�ԭ���ļ���
			 ArrayList<TabNewsData> news = mTabDetailData.data.news;
			 mNewList.addAll(news);
			 mNewsAdapter.notifyDataSetChanged();
		 }
		 
		 
		
	}
		
	class TopNewsAdapter extends PagerAdapter{

		private BitmapUtils utils;
		public TopNewsAdapter()
		{
			utils = new BitmapUtils(mActivity);
			utils.configDefaultLoadFailedImage(R.drawable.topnews_item_default);//����Ĭ�ϼ��ص�ͼƬ
			
		}
		
		
		@Override
		public int getCount() {
			return mTabDetailData.data.topnews.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView image = new ImageView(mActivity);
			image.setScaleType(ScaleType.FIT_XY);//���ڿؼ���С���ͼƬ
			
			TopNewsData topNewsData = mTopNewsList.get(position);
			utils.display(image, topNewsData.topimage);//����imageView�����ͼƬ��ַ
		
			container.addView(image);
			
			image.setOnTouchListener(new TopNewsTouchListener());//���ô�������
			
			return image;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
	
	/*
	 * ͷ�����ŵĴ�������
	 * 
	 */
	
	 class TopNewsTouchListener implements OnTouchListener{

		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				System.out.println("����");
				mHandler.removeCallbacksAndMessages(null); //ɾ��Handler�е�������Ϣ
				break;
			case MotionEvent.ACTION_CANCEL:
				System.out.println("ȡ��");
				mHandler.sendEmptyMessageDelayed(0, 3000);
				break;
			case MotionEvent.ACTION_UP:
				System.out.println("�ɿ�");
				mHandler.sendEmptyMessageDelayed(0, 3000);
				break;
			default:
				break;
			}
			return true;
		}
		 
	 }
	
	/*
	 * �����б��������
	 * 
	 */
	
	class NewsAdapter extends BaseAdapter{


		private BitmapUtils utils;

		public NewsAdapter(){
			utils = new BitmapUtils(mActivity);
			utils.configDefaultLoadFailedImage(R.drawable.pic_item_list_default); //���ü����е�Ĭ��ͼƬ
		}
		
		public int getCount() {
			return mNewList.size();
		}

		@Override
		public TabNewsData getItem(int position) {
			return mNewList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
		 ViewHolder holder;
		  if(convertView == null){
			  convertView = View.inflate(mActivity, R.layout.list_news_item, null);
			  holder = new ViewHolder();
			  holder.image = (ImageView) convertView.findViewById(R.id.iv_image);
			  holder.title = (TextView) convertView.findViewById(R.id.tv_news_title);
			  holder.date = (TextView) convertView.findViewById(R.id.tv_date);
			  
			  convertView.setTag(holder);
		  }else{
			  holder = (ViewHolder) convertView.getTag();
		  }
		  
		   TabNewsData item = getItem(position);
		   
		   holder.title.setText(item.title);
		   holder.date.setText(item.pubdate);
		   
		   utils.display(holder.image, item.listimage);
		   
		   String ids = PrefUtils.getString(mActivity, "read_ids", "");
		//   Log.d("hello", getItem(position).id);
		   if(ids.contains(getItem(position).id)){
			   holder.title.setTextColor(Color.GRAY);
		   }else{
			   holder.title.setTextColor(Color.BLACK);
		   }
		   
			return convertView;
		}
		
	}
	
	/*
	 * ������һҳ����
	 */
	private void getMoreDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mMoreUrl,new RequestCallBack<String>(){

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result =(String)responseInfo.result;
				
				parseData(result,true);
				
				lvList.onRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				error.printStackTrace();
				
				lvList.onRefreshComplete(false);
			}
			
		  });
		}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		
	}

	@Override
	public void onPageSelected(int position) {
		TopNewsData topNewsData = mTopNewsList.get(position);
		tvTitle.setText( topNewsData.title);
		
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		
	}
	/*
	 * �Լ�����һ�� ����ViewHolder ��������item ͼ���ÿ��View ����, ���㸴��.���������Ч��
	 */
	  static class ViewHolder{
		public ImageView image;
		public TextView title;
		public TextView date;
	   }
	  
	 
	  

}
