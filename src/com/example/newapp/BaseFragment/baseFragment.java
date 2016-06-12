package com.example.newapp.BaseFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class baseFragment extends Fragment {
	

	public Activity mActivity;
	
	//fragment创建
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mActivity = getActivity();
	}
	
	//处理fragment的布局
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return initView();
	}
	//依附activity创建完成
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		initData();
	}

	//子类必须实现初始化布局的方法
	public abstract View initView();
	//初始化数据，可以不实现
	public void initData(){
		
	}
		
}
