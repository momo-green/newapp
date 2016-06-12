package com.example.newapp.Base;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.newapp.R;

public class RefreshListView extends ListView implements OnScrollListener,android.widget.AdapterView.OnItemClickListener{

	private static final int STATE_PULL_REFRESH = 0; //����ˢ��
	private static final int STATE_RELEASE_REFRESH = 1; //�ɿ�ˢ��
	private static final int STATE_REFRESHING = 2; //����ˢ��

	private View mHeaderView;
	private int startY = -1; //��������ʼλ��
	private int mHeaderViewHeight;
	
	private int mCurrentState = STATE_PULL_REFRESH; //����Ĭ�ϵ�ǰ״̬Ϊ����ˢ��
	private TextView tvTitle;
	private TextView tvTiem;
	private ImageView image;
	private ProgressBar progress;
	private RotateAnimation animUp;
	private RotateAnimation animDown;
	
	private View mfooterView;
	private int mFooterHeight;

	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHeaderView();
		initFooterView();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView();
		initFooterView();

	}

	public RefreshListView(Context context) {
		super(context);
		initHeaderView();
		initFooterView();

	}
	
	/*
	 * ��ʼ��ͷ����
	 */
	private void initHeaderView() {
		mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
		this.addHeaderView(mHeaderView);
		
		mHeaderView.measure(0, 0);
		
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		
		mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0); //����ͷ����
		
		tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
		tvTiem = (TextView) mHeaderView.findViewById(R.id.tv_time);
		image = (ImageView) mHeaderView.findViewById(R.id.iv_arr);
		progress = (ProgressBar) mHeaderView.findViewById(R.id.progressBar);
		
		initArrowAnim(); //��ʼ������
		
		tvTiem.setText("���ˢ��ʱ��" + getCurrentTime());
		
	}

	
	/*
	 *��ʼ���Ų��� 
	 */
	public void initFooterView(){
		mfooterView = View.inflate(getContext(), R.layout.refresh_listview_footer, null);
		this.addFooterView(mfooterView);
		
		mfooterView.measure(0, 0);
		mFooterHeight = mfooterView.getMeasuredHeight();
		
		mfooterView.setPadding(0, -mFooterHeight, 0, 0); //���ؽŲ���
		
		this.setOnScrollListener(this);
	}
	
	
	/*
	 * �ַ��¼�
	 * 
	 */
	public boolean onTouchEvent(MotionEvent ev) {
		
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getRawY();
			break;
        case MotionEvent.ACTION_MOVE:
        	if(startY == -1){
        		startY = (int) ev.getRawY();
        	 }
        	int endY = (int) ev.getRawY();
        	int dy = endY - startY;//�ƶ�ƫ����
        	
        	if(dy >0 && getFirstVisiblePosition() ==0){//ֻ���������ҵ�ǰ��һ��item������������
        		int padding = dy - mHeaderViewHeight; //����padding
        		mHeaderView.setPadding(0, padding, 0, 0);//���õ�ǰpadding
        		
        		if(padding > 0 && mCurrentState != STATE_RELEASE_REFRESH){//״̬��Ϊ�ɿ�״̬
        			mCurrentState = STATE_RELEASE_REFRESH;
        			refreshState();
            	}else if(padding < 0 && mCurrentState != STATE_PULL_REFRESH){
            		mCurrentState = STATE_PULL_REFRESH;
            		refreshState();
            	}
        		return true;
        	}
			break;
        case MotionEvent.ACTION_UP:
			startY = -1; //����
			if(mCurrentState == STATE_RELEASE_REFRESH){
				mCurrentState = STATE_REFRESHING; //����ˢ��
				mHeaderView.setPadding(0, 0, 0, 0); //��ʾ
				refreshState();
			}else if(mCurrentState == STATE_PULL_REFRESH){
				mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0); //����
			}
        	break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	/*
	 * ˢ�������ؼ��Ĳ���
	 */
	private void refreshState() {
		switch (mCurrentState) {
		case STATE_PULL_REFRESH:
			tvTitle.setText("����ˢ��");
			image.startAnimation(animDown);
			image.setVisibility(View.VISIBLE);
			progress.setVisibility(View.INVISIBLE);
			break;
        case STATE_RELEASE_REFRESH:
        	tvTitle.setText("�ɿ�ˢ��");
        	image.startAnimation(animUp);
        	image.setVisibility(View.VISIBLE);
			progress.setVisibility(View.INVISIBLE);
			break;
	   case STATE_REFRESHING:
		   tvTitle.setText("����ˢ��");	
		   image.clearAnimation(); ///�����������������������
		   image.setVisibility(View.INVISIBLE);
		   progress.setVisibility(View.VISIBLE);
		   if(mListener != null)
		   {
			   mListener.onRefresh();
		   }
			break;
		default:
			break;
		}
	}
	
	private void initArrowAnim(){
		//��ͷ���϶���
		animUp = new RotateAnimation(0, -180,
				       Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		animUp.setDuration(200);
		animUp.setFillAfter(true);
		
		//��ͷ���¶���
		animDown = new RotateAnimation(-180,0,
				Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		animUp.setDuration(200);
		animUp.setFillAfter(true);
	}
	
	onRefreshListener mListener;
	public void setOnRefreshListener(onRefreshListener listener){
		mListener = listener;
	}
	
	public interface onRefreshListener{
		public void onRefresh();
		
		public void onLoadMore(); //������һҳ
	}
	
	/*
	 * ��������ˢ�µĿؼ�
	 * 
	 */
	public void onRefreshComplete(boolean success){
		if(isLoadingMore){//���ڼ��ظ���
			mfooterView.setPadding(0, -mFooterHeight, 0, 0); //���ؽŲ���
			isLoadingMore = false;
		}else{
			mCurrentState = STATE_PULL_REFRESH;
			tvTitle.setText("����ˢ��");
			image.setVisibility(View.VISIBLE);
			progress.setVisibility(View.INVISIBLE);
			
			mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
			
			if(success){
				tvTiem.setText("���ˢ��ʱ��" + getCurrentTime());
			}
			
		}
		
	}

	private String getCurrentTime() {
        
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
		
	
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
	}

	private boolean isLoadingMore;
	
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING){
			
			if(getLastVisiblePosition() == getCount()-1 && !isLoadingMore){
				System.out.println("������...");
				
				mfooterView.setPadding(0, 0, 0, 0); //��ʾ
				setSelection(getCount()-1); //�ı�listview��ʾλ��
				
				isLoadingMore = true;
				
				if(mListener != null){
					mListener.onLoadMore();
				}
			}
			
		}
		
	}
	
	OnItemClickListener mItemClicklistener;
	public void setOnItemClickListener(
			android.widget.AdapterView.OnItemClickListener listener) {
		super.setOnItemClickListener(this);
		mItemClicklistener = listener;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(mItemClicklistener != null){
			mItemClicklistener.onItemClick(parent, view, 
					        position-getHeaderViewsCount(), id);
		}
	}
}
