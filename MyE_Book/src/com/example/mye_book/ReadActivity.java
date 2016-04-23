package com.example.mye_book;

import com.alibaba.fastjson.JSONObject;
import com.example.mye_book.utils.MyPost;
import com.example.mye_book.vo.Chapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ReadActivity extends Activity {
	
	public class GetChapter extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			MyPost post=new MyPost(URL.GET_CHAPTER);
			if (nextState<0) {
				int id=chapter.getChapter_id()-1;
				post.addPostValue("chapter_id", id+"");
				
			}
			if(nextState>0){
                int id=chapter.getChapter_id()+1;
				post.addPostValue("chapter_id", id+"");
			}
			String result=post.Startpost();
			JSONObject json=JSONObject.parseObject(result);
			Log.v(TAG, "json:"+json.toString());
			
			String next=json.getString("next");
			if (next.equals("false")) {
				
				handler_set_content.sendEmptyMessage(NO_CHAPTER);
			}else{
				chapter=json.getObject("chapter", Chapter.class);
				handler_set_content.sendEmptyMessage(HAS_CHAPTER);
			}
			
		}
		
	}
	/**
	 * 改变字体大小，背景颜色等设置操作
	 * @author Administrator
	 *
	 */
	public class OnChangeContextClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btn_txtsize_plus:
				Log.v(TAG, "btn_txtsize_plus clicked!");
				txt_show.setTextSize(TypedValue.COMPLEX_UNIT_PX,txt_show.getTextSize()+1);
				break;
			case R.id.btn_txtsize_minus:
				Log.v(TAG, "btn_txtsize_minus clicked!");
				txt_show.setTextSize(TypedValue.COMPLEX_UNIT_PX,txt_show.getTextSize()-1);
				break;
			case R.id.btn_color_black:
				Log.v(TAG, "btn_color_black clicked!");
				layer_context.setBackgroundColor(Color.BLACK);
				txt_show.setTextColor(Color.WHITE);
				break;
			case R.id.btn_color_blue:
				Log.v(TAG, "btn_color_blue clicked!");
				layer_context.setBackgroundColor(Color.BLUE);
				txt_show.setTextColor(Color.WHITE);
				break;
			case R.id.btn_color_green:
				Log.v(TAG, "btn_color_green clicked!");
				layer_context.setBackgroundColor(Color.GREEN);
				txt_show.setTextColor(Color.YELLOW);
		    	break;
			case R.id.btn_color_red:
				Log.v(TAG, "btn_color_red clicked!");
				layer_context.setBackgroundColor(Color.RED);
				txt_show.setTextColor(Color.BLACK);
				break;
			case R.id.btn_color_white:
				Log.v(TAG, "btn_color_white clicked!");
				layer_context.setBackgroundColor(Color.WHITE);
				txt_show.setTextColor(Color.BLACK);
			    break;
			
			default:
				break;
			}

		}

	}
	/**
	 * 选择基本的设置
	 * @author Administrator
	 *
	 */
	public class OnChooseClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btn_set:
				Log.v(TAG, "btn_set clicked!");
				layer_choise.setVisibility(View.GONE);
				layer_detail.setVisibility(View.VISIBLE);
				break;
			case R.id.btn_close:
				Log.v(TAG, "btn_close clicked");
				layer_choise.setVisibility(View.VISIBLE);
				layer_detail.setVisibility(View.GONE);
				break;
			case R.id.btn_mode:
				Log.v(TAG, "btn_mode clicked!");
				break;
			case R.id.btn_index:
				Log.v(TAG, "btn_index clicked");
				break;
			case R.id.btn_comment:
				Log.v(TAG, "btn_comment clicked!");
				break;

			default:
				break;
			}

		}

	}
	public class getBookContent extends AsyncTask<String,String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog=new ProgressDialog(ReadActivity.this);
			pDialog.setMessage("正在加载，请稍后");
			
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url=URL.localhost+URL.ServerName+"/"+chapter.getUrl();
			MyPost post=new MyPost(url);
			str_context=post.Startpost();
			Log.v(TAG, ""+str_context);
			//������Ϣ֪ͨ�ı��ı�
			if (str_context==null) {
				//��ʾ�����������
				handler_set_content.sendEmptyMessage(FAILE);
			}else{
				handler_set_content.sendEmptyMessage(SUCCESS);
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
		}

	}

	String TAG="ReadActivity";
	
	private Chapter chapter;
	private String str_context;
	private int page_all_num,page_now_num=1;
	private int  w_screen;
	private int h_ScrollView,h_context;
	private int nextState=0;
	private int page_num=1;
	
	private ProgressDialog pDialog;
	private ScrollView mScrollView;
	private TextView txt_show,txt_state,txt_title;
	private Button btn_right,btn_set,btn_mode,btn_index,btn_comment,btn_close;
	private Button btn_txtsize_plus,btn_txtsize_minus;
	private Button btn_color_black,btn_color_red,btn_color_green,btn_color_blue,btn_color_white;
	private LinearLayout layer_control,layer_choise,layer_detail,layer_context;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_read);
		
		init();
		Intent intent=getIntent();
	    chapter=(Chapter)intent.getSerializableExtra("Chapter");
	    Log.v(TAG, "章节:"+chapter.toString());
	    new getBookContent().execute();
	    initControl();
	}

	private void initControl() {
		// TODO Auto-generated method stub
		txt_title=(TextView)findViewById(R.id.txt_title);
		txt_title.setText(chapter.getChapter_name());
		
		btn_right=(Button) findViewById(R.id.btn_right);
		btn_right.setText("帮助");
		btn_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v(TAG, "帮助按钮被点击");
			}
		});
		
		btn_set=(Button) findViewById(R.id.btn_set);
		btn_mode=(Button) findViewById(R.id.btn_mode);
		btn_index=(Button) findViewById(R.id.btn_index);
		btn_comment=(Button) findViewById(R.id.btn_comment);
		
		btn_set.setOnClickListener(new OnChooseClickListener());
		btn_mode.setOnClickListener(new OnChooseClickListener());
		btn_index.setOnClickListener(new OnChooseClickListener());
		btn_comment.setOnClickListener(new OnChooseClickListener());
		
		btn_close=(Button) findViewById(R.id.btn_close);
		btn_close.setOnClickListener(new OnChooseClickListener());
		
		btn_txtsize_minus=(Button) findViewById(R.id.btn_txtsize_minus);
		btn_txtsize_plus=(Button) findViewById(R.id.btn_txtsize_plus);
		btn_color_black=(Button) findViewById(R.id.btn_color_black);
		btn_color_red=(Button) findViewById(R.id.btn_color_red);
		btn_color_green=(Button) findViewById(R.id.btn_color_green);
		btn_color_blue=(Button) findViewById(R.id.btn_color_blue);
		btn_color_white=(Button) findViewById(R.id.btn_color_white);
		
		btn_txtsize_minus.setOnClickListener(new OnChangeContextClickListener());
		btn_txtsize_plus.setOnClickListener(new OnChangeContextClickListener());
		btn_color_black.setOnClickListener(new OnChangeContextClickListener());
		btn_color_red.setOnClickListener(new OnChangeContextClickListener());
		btn_color_green.setOnClickListener(new OnChangeContextClickListener());
		btn_color_blue.setOnClickListener(new OnChangeContextClickListener());
		btn_color_white.setOnClickListener(new OnChangeContextClickListener());
	}

	private void init() {
		// TODO Auto-generated method stub
		w_screen = getResources().getDisplayMetrics().widthPixels; 
		
		layer_control=(LinearLayout) findViewById(R.id.layer_control);
		layer_choise=(LinearLayout) findViewById(R.id.layer_choise);
		layer_detail=(LinearLayout) findViewById(R.id.layer_detail);
		layer_context=(LinearLayout) findViewById(R.id.layer_context);

		
		mScrollView=(ScrollView) findViewById(R.id.page_control);
		mScrollView.setOnTouchListener(new OnTouchListener() {
			//左右翻页，中间呼出菜单
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					 float zone=w_screen/3;
					 float x=event.getRawX();
					 if (x<zone) {
						//左屏，向上翻页，单到顶时加载上一章节内容
						 page_num=page_now_num;
						 page_now_num-=1;
						 handler_set_content.sendEmptyMessage(SCROL_PAGE);
					} 
					 if(x>zone*2){
						//右屏，向下翻页，当到底时加载下一章节内容
						 page_num=page_now_num;
						 page_now_num+=1;
						 handler_set_content.sendEmptyMessage(SCROL_PAGE);
					}
					 if (x>=zone && x<=zone*2) {
						if (layer_control.getVisibility()==View.GONE) {
							layer_control.setVisibility(View.VISIBLE);
						}else{
							layer_control.setVisibility(View.GONE);
							layer_choise.setVisibility(View.VISIBLE);
							layer_detail.setVisibility(View.GONE);
						}
					}
					break;
				case MotionEvent.ACTION_UP:
					
					break;

				default:
					break;
				}
				
				
				return true;
			}
		});

		txt_show=(TextView) findViewById(R.id.txt_context);
		txt_show.setText(" ");
		
		txt_state=(TextView) findViewById(R.id.txt_state);
		
		//创建观察者，获取文本框的实际大小
		ViewTreeObserver observer=txt_show.getViewTreeObserver();
		observer.addOnPreDrawListener(new OnPreDrawListener() {
			
			@Override
			public boolean onPreDraw() {
				// TODO Auto-generated method stub
//				 Log.v(TAG,"ʹ��ViewTreeObserver ��ȡ��ߣ�width:"  
//                         + txt_show.getMeasuredWidth() + "---->height:"  
//                         + txt_show.getMeasuredHeight());  
				 h_context=txt_show.getMeasuredHeight();//�ı��ܸ߶�
				 h_ScrollView = mScrollView.getHeight();// �����ı��߶�
				 page_all_num = h_context / h_ScrollView+1;
				 txt_state.setText(page_now_num + "/" + page_all_num);
				return true;
			}
		});
		
	}
	
	final int FAILE=1,SUCCESS=2,SCROL_PAGE=3,NO_CHAPTER=4,HAS_CHAPTER=5;
	Handler handler_set_content=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case FAILE:
				txt_show.setText("request fail");
				txt_state.setText("null/null");
				break;
			case SUCCESS:
				nextState=0;
				page_now_num=1;
				page_num=1;
          		txt_show.setText(str_context);
//				int w = View.MeasureSpec.makeMeasureSpec(0,
//						View.MeasureSpec.UNSPECIFIED);
//				int h = View.MeasureSpec.makeMeasureSpec(0,
//						View.MeasureSpec.UNSPECIFIED);
//				txt_show.measure(w, h);
//				h_context = txt_show.getMeasuredHeight();
			
			
				break;
			case SCROL_PAGE:
			//	Log.v(TAG, "������:������=" + h_ScrollView + ":" + h_context);
				
				if (page_now_num<=0) {
					nextState=-1;
					GetChapter getChapter=new GetChapter();
					getChapter.start();;
				}
				if (page_now_num>page_all_num) {
					nextState=1;
					GetChapter getChapter=new GetChapter();
					getChapter.start();
					
				}
				if (page_now_num>0 && page_now_num<=page_all_num) {
				    mScrollView.scrollTo(0, h_ScrollView*(page_now_num-1));
					//txt_state.setText(page_now_num + "/" + page_all_num);
				}
				
				break;
			case NO_CHAPTER:
				page_now_num=page_num;
				showToast("没有更多章节了！");
			   break;
			case HAS_CHAPTER:
				new getBookContent().execute();
				break;
			default:
				break;
			}
		}
	};
	
	
	protected void showToast(String message) {
		// TODO Auto-generated method stub

		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

	}
}
