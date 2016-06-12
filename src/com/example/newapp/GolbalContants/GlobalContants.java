package com.example.newapp.GolbalContants;

public class GlobalContants {

	public static final String SERVER_URL="http://10.0.2.2:8080/zhbj"; //本地
//	public static final String SERVER_URL="http://zhihuibj.sinaapp.com/zhbj";
	public static final String CATEGORIES_URL=SERVER_URL
			    +"/categories.json";//获取分类信息接口
	
	public static final String PHOTOS_URL=SERVER_URL
			  + "/photos/photos_1.json";//获取组图信息的接口
}
