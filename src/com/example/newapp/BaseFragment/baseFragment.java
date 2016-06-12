package com.example.newapp.BaseFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class baseFragment extends Fragment {
	

	public Activity mActivity;
	
	//fragment����
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mActivity = getActivity();
	}
	
	//����fragment�Ĳ���
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return initView();
	}
	//����activity�������
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		initData();
	}

	//�������ʵ�ֳ�ʼ�����ֵķ���
	public abstract View initView();
	//��ʼ�����ݣ����Բ�ʵ��
	public void initData(){
		
	}
		
}
