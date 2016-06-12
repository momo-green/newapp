package com.example.newapp.domian;

import java.util.ArrayList;

import com.example.newapp.Base.TabDetailPager;


/**
 * ���������Ϣ�ķ�װ
 * 
 * �ֶ����ֱ���ͷ��������ص��ֶ���һ��, ����gson����
 * 
 * @author Kevin
 * 
 */


public class NewData {

	public int retcode;
	public ArrayList<NewsMenuData> data;
  
	
	
	// ��������ݶ���
	public class NewsMenuData {
		public String id;
		public String title;
		public int type;
		public String url;

		public ArrayList<NewsTabData> children;

		@Override
		public String toString() {
			return "NewsMenuData [title=" + title + ", children=" + children
					+ "]";
		}
	}

	// ����ҳ����11����ҳǩ�����ݶ���
	public class NewsTabData {
		public String id;
		public String title;
		public int type;
		public String url;

		@Override
		public String toString() {
			return "NewsTabData [title=" + title + "]";
		}
	}

	@Override
	public String toString() {
		return "NewsData [data=" + data + "]";
	}

}
