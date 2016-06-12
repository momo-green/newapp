package com.example.newapp.view;



import com.example.newapp.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemView extends RelativeLayout {
	private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.example.newapp";
	private TextView tvTitle;
	private TextView tvDes;
	private CheckBox cbStatus;
	private String mTitle;
	private String mDescOn;
	private String mDescOff;

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		int attributeCount = attrs.getAttributeCount();

		for (int i = 0; i < attributeCount; i++) {
			String attributeName = attrs.getAttributeName(i);
			String attributeValue = attrs.getAttributeValue(i);

			System.out.println(attributeName + "=" + attributeValue);
		}

	  mTitle = attrs.getAttributeValue(NAMESPACE, "title"); //根据属性名称，获取属性值
	  mDescOn = attrs.getAttributeValue(NAMESPACE, "desc_on");
	  mDescOff = attrs.getAttributeValue(NAMESPACE, "desc_off");
	  
	  initView();

	  
	}

	public SettingItemView(Context context) {
		super(context);
		initView();
	}

	/*
	 * 初始化布局
	 */
	private void initView() {
		// 将自定义的布局文件设置给当前的SettingItemView
		View.inflate(getContext(), R.layout.view_setting, this);

		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvDes = (TextView) findViewById(R.id.tv_desc);
		cbStatus = (CheckBox) findViewById(R.id.cb_status);
		
		setTitle(mTitle); //设置标题
	}

	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public void setDesc(String des) {
		tvDes.setText(des);
	}

	/**
	 * 返回勾选状态
	 */
	public boolean ischecked() {
		return cbStatus.isChecked();
	}

	public void setChecked(boolean check) {
		cbStatus.setChecked(check);
		
		//根据选择的状态，更新文本描述
		if(check){
			setDesc(mDescOn);
		}else{
			setDesc(mDescOff);
		}
	}
}
