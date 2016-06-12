package com.example.newapp.BaseFragment;

import java.util.ArrayList;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.newapp.MainActivity;
import com.example.newapp.R;
import com.example.newapp.Base.impl.NewPager;
import com.example.newapp.domian.NewData;
import com.example.newapp.domian.NewData.NewsMenuData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class LeftMenuFragment extends baseFragment{

	@ViewInject(R.id.lv_list)
	private ListView list;
	public ArrayList<NewsMenuData> menuList;
	private int mCurrentPos; //当前被点击的菜单项
	private MenuApdapter mApdapter;
	
	/*
	 * 初始化视图
	 * (non-Javadoc)
	 * @see com.example.newapp.BaseFragment.baseFragment#initView()
	 */
	public View initView() {
       View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);		
	   ViewUtils.inject(this, view);
       return view ;
	}

	public void initData() {

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {

				mCurrentPos = position;
				mApdapter.notifyDataSetChanged();  //刷新
				
				setCurrentMenuDetailPager(position);
				
				toggleSlidingMenu(); //切换
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
	 * 设置当前菜单详情页
	 */
	protected void setCurrentMenuDetailPager(int position) {

		 MainActivity mainUi = (MainActivity) mActivity;
		 ContentFragment fragment = mainUi.getContentFragment(); //获取主页面fragment
		 NewPager pager = fragment.getNewPager(); //获取新闻中心页面
		 pager.setCurrentMenuDetailPager(position); //设置当前菜单详情
		 
	}

	//设置网络数据
	public void setMenuData(NewData data){
		//System.out.println("侧边栏拿到数据啦"+data);
	    menuList = data.data;
	    mApdapter = new MenuApdapter();
	    list.setAdapter(mApdapter);
	    
	    
		
	}
  
	class MenuApdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return menuList.size();
		}

		@Override
		public NewsMenuData getItem(int position) {
			return menuList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(mActivity, R.layout.list_menu_item, null);
			TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
			
			NewsMenuData newsMenuData = getItem(position);
			
			tvTitle.setText(newsMenuData.title);
			
			if(mCurrentPos == position){ //选中为红色
				tvTitle.setEnabled(true);
			}else{//否则为白色
				tvTitle.setEnabled(false);
			}
			
			return view;
		}
		
	}

}
