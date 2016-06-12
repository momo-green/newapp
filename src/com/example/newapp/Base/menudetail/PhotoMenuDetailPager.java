package com.example.newapp.Base.menudetail;

import java.util.ArrayList;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newapp.R;
import com.example.newapp.Base.BaseMenuDetailPager;
import com.example.newapp.GolbalContants.GlobalContants;
import com.example.newapp.Util.CacheUtils;
import com.example.newapp.Util.bitmap.MyBitmapUtils;
import com.example.newapp.domian.PhotosData;
import com.example.newapp.domian.PhotosData.PhotoInfo;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/*
 * 菜单详情页-新闻
 */

public class PhotoMenuDetailPager extends BaseMenuDetailPager{

	private ArrayList<PhotoInfo> mPhotosList;
	private PhotoAdapter mAdapter;
	private ListView lvphoto;
	private GridView glphoto;
	private ImageButton btnPhoto;
	

	public PhotoMenuDetailPager(Activity actiity ,ImageButton btnPhoto) {
		super(actiity);
		this.btnPhoto = btnPhoto;
		
		btnPhoto.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				changeDisplay();
			}
		});
	}

	private boolean isListDisplay = true ;// 是否是列表展示
	
	private void changeDisplay(){
		if(isListDisplay){
			isListDisplay =false;
			lvphoto.setVisibility(View.GONE);
			glphoto.setVisibility(View.VISIBLE);
			
			btnPhoto.setImageResource(R.drawable.icon_pic_list_type);
		}else{
			isListDisplay = true;
			lvphoto.setVisibility(View.VISIBLE);
			glphoto.setVisibility(View.GONE);
			
			btnPhoto.setImageResource(R.drawable.icon_pic_grid_type);
		}
		
	}
	
	@Override
	public View initView() {

		View view = View.inflate(mActivity, R.layout.menu_photo_pager, null);
		
		lvphoto = (ListView) view.findViewById(R.id.lv_photo);
		glphoto = (GridView) view.findViewById(R.id.gv_photo);
		
		return view;
	}

	public void initData() {
		
		String cache = CacheUtils.getCache(GlobalContants.PHOTOS_URL, mActivity);
		
		if(!TextUtils.isEmpty(cache)){
			
		}
		getDataFromServer();
		
	}
	
	private void getDataFromServer() {
		 HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, GlobalContants.PHOTOS_URL, new RequestCallBack<String>() {

			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result =(String) responseInfo.result;
				
				parseData(result);
				
				CacheUtils.setCache(GlobalContants.PHOTOS_URL, result, mActivity);
				
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				error.printStackTrace();
				
				
			}
		});
		
	}

	protected void parseData(String result) {
		Gson gson = new Gson();
		PhotosData data = gson.fromJson(result, PhotosData.class);
		mPhotosList = data.data.news;
		
		if(mPhotosList != null){
			mAdapter = new PhotoAdapter();
			lvphoto.setAdapter(mAdapter);
			glphoto.setAdapter(mAdapter);
		}
	}

	/*
	 * 组图信息的适配器
	 */
	class PhotoAdapter extends BaseAdapter{

		//private BitmapUtils utils;
		
		private MyBitmapUtils utils;
		
		public PhotoAdapter(){
		//	utils = new BitmapUtils(mActivity);
		//	utils.configDefaultLoadFailedImage(R.drawable.news_pic_default); //设置默认图片
	
			utils = new MyBitmapUtils();
		}
		
		@Override
		public int getCount() {
			return mPhotosList.size();
		}

		@Override
		public PhotoInfo getItem(int position) {
			return mPhotosList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null){
				convertView = View.inflate(mActivity, R.layout.list_photo_item,
						null);
				holder = new ViewHolder();
				holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_text_title);
				holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
				
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			PhotoInfo item =  getItem(position);
			holder.tvTitle.setText(item.title);
		    utils.display(holder.ivPic,item.listimage);
			
			return convertView;
		}
		
	}
	
	static class ViewHolder{
		public TextView tvTitle;
		public ImageView ivPic;
	}
}
