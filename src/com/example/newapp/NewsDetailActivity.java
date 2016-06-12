package com.example.newapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

public class NewsDetailActivity extends Activity implements OnClickListener{

	private WebView mWebView;
	private ImageButton mBack;
	private ImageButton mShare;
	private ImageButton mTextsize;
	private ProgressBar pbProgress;

	protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	setContentView(R.layout.activity_news_detail);
    	
    	mWebView = (WebView) findViewById(R.id.wv_web);
    	mBack = (ImageButton) findViewById(R.id.Ib_back);
    	mShare = (ImageButton) findViewById(R.id.Ib_share);
    	mTextsize = (ImageButton) findViewById(R.id.Ib_textsize);
    	pbProgress = (ProgressBar) findViewById(R.id.pb_progressBar);
    	
    	mBack.setOnClickListener(this);
    	mShare.setOnClickListener(this);
    	mTextsize.setOnClickListener(this);
    	
    	String url = getIntent().getStringExtra("url");
    	
    	WebSettings settings = mWebView.getSettings();
    	settings.setJavaScriptEnabled(true); //表示支持js
    	settings.setBuiltInZoomControls(true); //表示放大缩小按钮
    	settings.setUseWideViewPort(true);//支持双击缩放
    	
    	mWebView.setWebViewClient(new WebViewClient(){
    		/*
    		 * 网页加载开始
    		 */
    		public void onPageStarted(WebView view, String url, Bitmap favicon) {
    			super.onPageStarted(view, url, favicon);
    			System.out.println("网页加载开始");
    			pbProgress.setVisibility(View.VISIBLE);
    		}
    		/*
    		 * 网页加载结束
    		 */
    		public void onPageFinished(WebView view, String url) {
    			super.onPageFinished(view, url);
    			System.out.println("网页加载结束");
    			pbProgress.setVisibility(View.INVISIBLE);
    		}
    		
    		@Override
    		public boolean shouldOverrideUrlLoading(WebView view, String url)
    		{
    			System.out.println("跳转url :" + url);
    			view.loadUrl(url);
    			return true;
    		//	return super.shouldOverrideUrlLoading(view, url);
    		}
    	});
    	
    	mWebView.setWebChromeClient(new WebChromeClient(){
    		
    		/*
    		 * 进度发生变化
    		 * (non-Javadoc)
    		 * @see android.webkit.WebChromeClient#onProgressChanged(android.webkit.WebView, int)
    		 */
    		@Override
    		public void onProgressChanged(WebView view, int newProgress) {

    			System.out.println("加载进度："+newProgress);
    			super.onProgressChanged(view, newProgress);
    		}
    		
            /*
             * 获取网页标题
             * (non-Javadoc)
             * @see android.webkit.WebChromeClient#onReceivedTitle(android.webkit.WebView, java.lang.String)
             */
    		public void onReceivedTitle(WebView view, String title) {
    			System.out.println("网页标题" + title);
    			super.onReceivedTitle(view, title);
    		}
    	});
    	//mWebView.loadUrl("http://www.itheima.com"); //加载网页
    	mWebView.loadUrl(url);  //加载网页
    	
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Ib_back:
			finish();
			break;
	    case R.id.Ib_textsize:
			showChooseDialog();
			break;
        case R.id.Ib_share:
        	showShare();
			break;
		default:
			break;
		}
	}
	
	private int mCurrentChooseItem;// 记录当前选中的item，点击确定前
	private int mCurrentIemt = 2;//记录当前确定选中的item

	private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("字体设置");
        String[] items =new String[]{"超大号字体","大号字体","正常字体","小号字体","超小号字体"};
        builder.setSingleChoiceItems(items, mCurrentIemt,
        		new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				System.out.println("选中" + which);
				mCurrentChooseItem = which;
			}
		});
        
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				
				WebSettings settings = mWebView.getSettings();
				
				switch (mCurrentChooseItem) {
				case 0:
					settings.setTextSize(TextSize.LARGEST);
			        break;
				case 1:
					settings.setTextSize(TextSize.LARGER);
					break;
				case 2:
					settings.setTextSize(TextSize.NORMAL);
					break;
				case 3:
					settings.setTextSize(TextSize.SMALLER);
					break;
				case 4:
                    settings.setTextSize(TextSize.SMALLEST);					
					break;
				default:
					break;
				}
				mCurrentIemt = mCurrentChooseItem;
			}
		});
        builder.setNegativeButton("取消", null);
        builder.show();
	}
	
	/*
	 * 分享
	 */
	private void showShare() {
		 ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 
         oks.setTheme(OnekeyShareTheme.SKYBLUE);
		 
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 

		// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		 oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		 oks.setTitle(getString(R.string.share));
		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		 oks.setTitleUrl("http://sharesdk.cn");
		 // text是分享文本，所有平台都需要这个字段
		 oks.setText("我是分享文本");
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		 // url仅在微信（包括好友和朋友圈）中使用
		 oks.setUrl("http://sharesdk.cn");
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 oks.setComment("我是测试评论文本");
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		 oks.show(this);
		 }
}
