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
 * 页签详情页
 */
public class TabDetailPager extends BaseMenuDetailPager implements OnPageChangeListener {

	NewsTabData mTabData;
	private TextView tvText;
	
	private String mUrl;
	private TabData mTabDetailData;
	
	@ViewInject(R.id.vp_news)
	private ViewPager mViewPager;
	
	@ViewInject(R.id.news_title)
	private TextView tvTitle;  //头条新闻的标题
	
	private ArrayList<TopNewsData> mTopNewsList;//头条新闻数据集合
	
	@ViewInject(R.id.indicator)
	private CirclePageIndicator mIndicator; //头条新闻位置指示器
	
	@ViewInject(R.id.lv_list)
	private RefreshListView lvList;//新闻列表
	private ArrayList<TabNewsData> mNewList;//新闻列表集合
	private NewsAdapter mNewsAdapter;
	private String mMoreUrl; //更多页面的地址
	
	private Handler mHandler;
	
	public TabDetailPager(Activity actiity, NewsTabData newsTabData) {
		super(actiity);
		
		mTabData = newsTabData;
		
		mUrl = GlobalContants.SERVER_URL + mTabData.url;
	}

	@Override
	public View initView() {

		View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);
	    //加载头布局
		View headerView = View.inflate(mActivity, R.layout.list_header_topnews, null);
		ViewUtils.inject(this, view);
		ViewUtils.inject(this,headerView);
		
		//将头条新闻以头布局的形式加给listview
		lvList.addHeaderView(headerView);
		//设置下拉刷新的监听
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
				Toast.makeText(mActivity, "最后一页了", Toast.LENGTH_SHORT)
				    .show();
				lvList.onRefreshComplete(false);//收起加载更多的布局
			   }
				
			}
		});
		
		lvList.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View view, 
					int position,long id) {
				System.out.println("被点击"+position);
				
				String ids = PrefUtils.getString(mActivity, "read_ids", "");
				String readId = mNewList.get(position).id;
				if(!ids.contains(readId)){
					ids = ids+ readId + ",";
					PrefUtils.setString(mActivity, "read_ids", ids);
				}
			//	mNewsAdapter.notifyDataSetChanged();
				changeReadState(view);//实现局部界面刷新,这个view就是被点击的item布局对象
			
			    //跳转新闻详情页
				Intent intent = new Intent();
				intent.setClass(mActivity, NewsDetailActivity.class);
				intent.putExtra("url",mNewList.get(position).url);
				mActivity.startActivity(intent);
			}
			
		});
		return view;
	}

	 /*
	   * 改变已读新闻的颜色
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
				System.out.println("页签详情页返回结果：" +result);
				
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
		 System.out.println("页签详情解析：" + mTabDetailData);
		 
		 String more = mTabDetailData.data.more;
		 
		 //处理下一页链接
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
		     mIndicator.setSnap(true); //快照显示
		     
		     mIndicator.setOnPageChangeListener(this);
		     mIndicator.onPageSelected(0); //让指示器重新定位到第一个点
		     
		     tvTitle.setText(mTopNewsList.get(0).title);
			 }
			 
		     if(mNewList != null){
		       mNewsAdapter = new NewsAdapter();
		      lvList.setAdapter(mNewsAdapter);
		     }
		     
		     //自动轮播条显示
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
		    		    mHandler.sendEmptyMessageDelayed(0, 3000);//继续延迟3秒钟，形成循环
		    			
		    		}; 
		    	 };
		    	 
		    	 mHandler.sendEmptyMessageDelayed(0, 3000);//延迟3秒钟后发消息
		     }
		 }else{//如果是加载下一页，需要将数据追加给原来的集合
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
			utils.configDefaultLoadFailedImage(R.drawable.topnews_item_default);//设置默认加载的图片
			
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
			image.setScaleType(ScaleType.FIT_XY);//基于控件大小填充图片
			
			TopNewsData topNewsData = mTopNewsList.get(position);
			utils.display(image, topNewsData.topimage);//传递imageView对象和图片地址
		
			container.addView(image);
			
			image.setOnTouchListener(new TopNewsTouchListener());//设置触摸监听
			
			return image;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
	
	/*
	 * 头条新闻的触摸监听
	 * 
	 */
	
	 class TopNewsTouchListener implements OnTouchListener{

		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				System.out.println("按下");
				mHandler.removeCallbacksAndMessages(null); //删除Handler中的所有消息
				break;
			case MotionEvent.ACTION_CANCEL:
				System.out.println("取消");
				mHandler.sendEmptyMessageDelayed(0, 3000);
				break;
			case MotionEvent.ACTION_UP:
				System.out.println("松开");
				mHandler.sendEmptyMessageDelayed(0, 3000);
				break;
			default:
				break;
			}
			return true;
		}
		 
	 }
	
	/*
	 * 新闻列表的适配器
	 * 
	 */
	
	class NewsAdapter extends BaseAdapter{


		private BitmapUtils utils;

		public NewsAdapter(){
			utils = new BitmapUtils(mActivity);
			utils.configDefaultLoadFailedImage(R.drawable.pic_item_list_default); //设置加载中的默认图片
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
	 * 加载下一页数据
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
	 * 自己定义一个 类如ViewHolder 来保存下item 图层的每个View 对象, 方便复用.提升程序的效率
	 */
	  static class ViewHolder{
		public ImageView image;
		public TextView title;
		public TextView date;
	   }
	  
	 
	  

}
