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
	private int mCurrentPos; //��ǰ������Ĳ˵���
	private MenuApdapter mApdapter;
	
	/*
	 * ��ʼ����ͼ
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
				mApdapter.notifyDataSetChanged();  //ˢ��
				
				setCurrentMenuDetailPager(position);
				
				toggleSlidingMenu(); //�л�
			}
		});
	}
	
	
	/*
	 * �л�SlidingMenu��״̬
	 */
	protected void toggleSlidingMenu() {
		MainActivity mainUi = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUi.getSlidingMenu();
		slidingMenu.toggle();//�л�״̬����ʾʱ���أ�����ʱ��ʾ
	}

	/*
	 * ���õ�ǰ�˵�����ҳ
	 */
	protected void setCurrentMenuDetailPager(int position) {

		 MainActivity mainUi = (MainActivity) mActivity;
		 ContentFragment fragment = mainUi.getContentFragment(); //��ȡ��ҳ��fragment
		 NewPager pager = fragment.getNewPager(); //��ȡ��������ҳ��
		 pager.setCurrentMenuDetailPager(position); //���õ�ǰ�˵�����
		 
	}

	//������������
	public void setMenuData(NewData data){
		//System.out.println("������õ�������"+data);
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
			
			if(mCurrentPos == position){ //ѡ��Ϊ��ɫ
				tvTitle.setEnabled(true);
			}else{//����Ϊ��ɫ
				tvTitle.setEnabled(false);
			}
			
			return view;
		}
		
	}

}
