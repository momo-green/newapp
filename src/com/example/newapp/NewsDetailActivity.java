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
    	settings.setJavaScriptEnabled(true); //��ʾ֧��js
    	settings.setBuiltInZoomControls(true); //��ʾ�Ŵ���С��ť
    	settings.setUseWideViewPort(true);//֧��˫������
    	
    	mWebView.setWebViewClient(new WebViewClient(){
    		/*
    		 * ��ҳ���ؿ�ʼ
    		 */
    		public void onPageStarted(WebView view, String url, Bitmap favicon) {
    			super.onPageStarted(view, url, favicon);
    			System.out.println("��ҳ���ؿ�ʼ");
    			pbProgress.setVisibility(View.VISIBLE);
    		}
    		/*
    		 * ��ҳ���ؽ���
    		 */
    		public void onPageFinished(WebView view, String url) {
    			super.onPageFinished(view, url);
    			System.out.println("��ҳ���ؽ���");
    			pbProgress.setVisibility(View.INVISIBLE);
    		}
    		
    		@Override
    		public boolean shouldOverrideUrlLoading(WebView view, String url)
    		{
    			System.out.println("��תurl :" + url);
    			view.loadUrl(url);
    			return true;
    		//	return super.shouldOverrideUrlLoading(view, url);
    		}
    	});
    	
    	mWebView.setWebChromeClient(new WebChromeClient(){
    		
    		/*
    		 * ���ȷ����仯
    		 * (non-Javadoc)
    		 * @see android.webkit.WebChromeClient#onProgressChanged(android.webkit.WebView, int)
    		 */
    		@Override
    		public void onProgressChanged(WebView view, int newProgress) {

    			System.out.println("���ؽ��ȣ�"+newProgress);
    			super.onProgressChanged(view, newProgress);
    		}
    		
            /*
             * ��ȡ��ҳ����
             * (non-Javadoc)
             * @see android.webkit.WebChromeClient#onReceivedTitle(android.webkit.WebView, java.lang.String)
             */
    		public void onReceivedTitle(WebView view, String title) {
    			System.out.println("��ҳ����" + title);
    			super.onReceivedTitle(view, title);
    		}
    	});
    	//mWebView.loadUrl("http://www.itheima.com"); //������ҳ
    	mWebView.loadUrl(url);  //������ҳ
    	
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
	
	private int mCurrentChooseItem;// ��¼��ǰѡ�е�item�����ȷ��ǰ
	private int mCurrentIemt = 2;//��¼��ǰȷ��ѡ�е�item

	private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("��������");
        String[] items =new String[]{"���������","�������","��������","С������","��С������"};
        builder.setSingleChoiceItems(items, mCurrentIemt,
        		new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				System.out.println("ѡ��" + which);
				mCurrentChooseItem = which;
			}
		});
        
        builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
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
        builder.setNegativeButton("ȡ��", null);
        builder.show();
	}
	
	/*
	 * ����
	 */
	private void showShare() {
		 ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 
         oks.setTheme(OnekeyShareTheme.SKYBLUE);
		 
		 //�ر�sso��Ȩ
		 oks.disableSSOWhenAuthorize(); 

		// ����ʱNotification��ͼ�������  2.5.9�Ժ�İ汾�����ô˷���
		 oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
		 oks.setTitle(getString(R.string.share));
		 // titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
		 oks.setTitleUrl("http://sharesdk.cn");
		 // text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		 oks.setText("���Ƿ����ı�");
		 // imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
		 //oks.setImagePath("/sdcard/test.jpg");//ȷ��SDcard������ڴ���ͼƬ
		 // url����΢�ţ��������Ѻ�����Ȧ����ʹ��
		 oks.setUrl("http://sharesdk.cn");
		 // comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
		 oks.setComment("���ǲ��������ı�");
		 // site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
		 oks.setSiteUrl("http://sharesdk.cn");

		// ��������GUI
		 oks.show(this);
		 }
}
