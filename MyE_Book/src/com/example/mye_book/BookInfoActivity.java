package com.example.mye_book;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.mye_book.utils.MyPost;
import com.example.mye_book.vo.Book;
import com.example.mye_book.vo.Chapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

public class BookInfoActivity extends Activity {
	
	/**
	 * 章节监听器，点击章节就会跳转至对应章节的阅读界面
	 * @author Administrator
	 *
	 */
	public class OnChapterClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
            TextView txt_clicked=(TextView) v;
            String id= txt_clicked.getTag().toString();
            String name=txt_clicked.getText().toString();
            Log.v(TAG, "id:"+id+" name:"+name);
            //跳转至阅读界面
            to_ReadActivity(Integer.parseInt(id)-1);
		}

	}

	/**
	 * 同步任务，获取这本书籍的详细
	 * @author Administrator
	 *
	 */
	public class getBookInfoTask extends AsyncTask<String, String,String> {
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog=new ProgressDialog(BookInfoActivity.this);
			pDialog.setMessage("正在通信，请稍后...");
			
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			MyPost post=new MyPost(URL.GET_BOOK_INFORMATION_BY_ID);
			post.addPostValue("Book_id", book.getId());
			String result= post.Startpost();
			parseJson(result);
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
		}

	}

	String TAG="BookInfoActivity";
	private ProgressDialog pDialog;
	private String Book_id;
	private Book book;
	private List<Chapter> chapters;
	private boolean isCollect=false;
	private List<TextView> txt_chapters=new ArrayList<TextView>();

	
	private ImageView book_cover;
	private RadioButton show_author,show_tag,show_click,show_collection,show_update;
	private Button btn_collection,btn_read,btn_downlode;
	private TextView txt_introduction,txt_title,txt_new;
	private ToggleButton btn_more,btn_sort;
	private TableLayout list_chapter;
	private TableRow.LayoutParams params=
			new TableRow.LayoutParams(
					TableRow.LayoutParams.MATCH_PARENT, 
					TableRow.LayoutParams.WRAP_CONTENT, 
		    	1.0f);
	private TableLayout.LayoutParams row_params=
			new TableLayout.LayoutParams(
					TableLayout.LayoutParams.MATCH_PARENT, 
					TableLayout.LayoutParams.WRAP_CONTENT);
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bookinfo);
		
		init();
		getBookInfo();
	}

	public void parseJson(String result) {
		// TODO Auto-generated method stub
		JSONObject jsonObject=JSON.parseObject(result);
		String next=jsonObject.getString("next");
		if(next.equals("true")){
			JSONArray chaptersjson=jsonObject.getJSONArray("chapters");
			chapters=JSONArray.parseArray(chaptersjson.toJSONString(), Chapter.class);
			Log.v(TAG, chapters.toString());
			//加载章节信息
			runOnUiThread(new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					order_add_chapter();
				}
			});

		}else{
			Log.v(TAG, "无章节信息");
			
		}
	}

	/**
	 * 获取书籍基本信息
	 */
	private void getBookInfo() {
		// TODO Auto-generated method stub
		Intent intent=getIntent();
		book=(Book) intent.getSerializableExtra("theBook");
		Log.v(TAG, "书籍信息:"+book.toString());
		new getBookInfoTask().execute();
		//加载书籍的图片
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String imageUrl=URL.localhost+URL.ServerName+"/"+book.getUrl();
				MyPost getimg=new MyPost(imageUrl);
				Bitmap bmp= getimg.getURLimage();
				book_cover.setImageBitmap(bmp);
			}
		}).start();
		//加载书籍的基本信息
		
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				txt_title.setText(book.getName());
				
				show_author.setText(book.getAuthor());
				show_tag.setText(book.getLab());
				show_click.setText(String.valueOf(book.getClick_num()));
				show_collection.setText(String.valueOf(book.getCollection_num()));
				show_update.setText(book.getUpdate().toString());
				
				txt_introduction.setText(txt_introduction.getText()+book.getIntroduction());
			}
		});
	}

	private void init() {
		// TODO Auto-generated method stub
		//title bar 
		txt_title=(TextView) findViewById(R.id.txt_title);
		btn_downlode=(Button) findViewById(R.id.btn_right);
		btn_downlode.setText("缓存全本");
		btn_downlode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v(TAG, "btn_downlode clicked!");
				Intent intent=new Intent(BookInfoActivity.this,DownloadService.class);
				intent.putExtra("chapters", (Serializable)chapters);
				startService(intent);
			}
		});
		
		//book information
		book_cover=(ImageView) findViewById(R.id.img_book_cover);

		show_author=(RadioButton) findViewById(R.id.show_author);
		show_tag=(RadioButton) findViewById(R.id.show_tag);
		show_click=(RadioButton) findViewById(R.id.show_click);
		show_collection=(RadioButton) findViewById(R.id.show_collection);
		show_update=(RadioButton) findViewById(R.id.show_update);
		
		btn_collection=(Button) findViewById(R.id.btn_collection);
		btn_collection.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (isCollect) {
					btn_collection.setText("收藏本书");
					//回传服务器，写入本地数据
					isCollect=false;
				}else{
					//回传服务器，更新本地数据
					btn_collection.setText("取消收藏");
					isCollect=true;
				}
			}
		});
		
		btn_read=(Button) findViewById(R.id.btn_read);
		btn_read.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v(TAG, "btn_read clicked!");
				//跳转至阅读界面，从第一章节开始
				to_ReadActivity(0);
			}
		});
	
		//introduction
		txt_introduction=(TextView) findViewById(R.id.txt_introduction);
		
		
		btn_more=(ToggleButton) findViewById(R.id.btn_more);
		btn_more.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					txt_introduction.setMaxLines(20);
				}else{
					txt_introduction.setMaxLines(2);
				}
				
			}
		});
		
		txt_new=(TextView) findViewById(R.id.txt_new);
		txt_new.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//跳转至阅读界面，阅读最新章节
				Log.v(TAG, "txt_new clicked!");
				to_ReadActivity(chapters.size()-1);
			}
		});
		
		btn_sort=(ToggleButton) findViewById(R.id.btn_sort);
		btn_sort.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					//章节倒叙排列
					flashback_add_chapter();
				}else{
					//章节顺序排列
					order_add_chapter();
				}
			}
		});
		
		list_chapter=(TableLayout) findViewById(R.id.list_chapter);
		
		
	}

	public void order_add_chapter() {
		int index=0;
		list_chapter.removeAllViews();
		
		Log.v(TAG, "章节数量:"+chapters.size());
		for (int i = 0; i < chapters.size()/2; i++) {
			TableRow row1=new TableRow(getBaseContext());
			
			TextView rd1 = creat_txt_chapters(index);
			
			txt_chapters.add(rd1);
			row1.addView(rd1,params);
			
			
			index=index+1;
			
			TextView rd2=creat_txt_chapters(index);
			txt_chapters.add(rd2);
			row1.addView(rd2,params);
			index=index+1;

			list_chapter.addView(row1,row_params);
		}
		if (index==chapters.size()-1) {
		    TableRow row1=new TableRow(getBaseContext());
			
		    TextView rd1=creat_txt_chapters(index);
		    
			txt_chapters.add(rd1);
			row1.addView(rd1,params);
			index=index+1;
			

			list_chapter.addView(row1,row_params);
		}
		
	}
	
	public void flashback_add_chapter(){
		int index=chapters.size()-1;
		list_chapter.removeAllViews();
		
		Log.v(TAG, "章节数量:"+chapters.size());
		for (int i = 0; i < chapters.size()/2; i++) {

			TableRow row1=new TableRow(getBaseContext());

			TextView rd1 = creat_txt_chapters(index);
			txt_chapters.add(rd1);
			row1.addView(rd1,params);
			index=index-1;
			
			TextView rd2=creat_txt_chapters(index);
			txt_chapters.add(rd2);
			row1.addView(rd2,params);
			index=index-1;

			list_chapter.addView(row1,row_params);
		}
		if (index==0) {

		    TableRow row1=new TableRow(getBaseContext());
			
		    TextView rd1=creat_txt_chapters(index);
		    
			txt_chapters.add(rd1);
			row1.addView(rd1,params);
			index=index-1;
			
			list_chapter.addView(row1,row_params);
		}
	}
	
	public TextView creat_txt_chapters(int index) {

		TextView rd1=new TextView(getBaseContext());
		rd1.setText(chapters.get(index).getChapter_name());
		rd1.setTag(chapters.get(index).getChapter_id());
		rd1.setTextColor(Color.BLACK);
		rd1.setTextSize(20f);
		rd1.setOnClickListener(new OnChapterClickListener());

		Log.v(TAG, "创建了章节:"+index+rd1.getText());
		
		return rd1;
	}

	private void to_ReadActivity(int id) {
		Bundle mBundle=new Bundle();
		mBundle.putSerializable("Chapter", chapters.get(id));
		Intent intent=new Intent(BookInfoActivity.this, ReadActivity.class);
		intent.putExtras(mBundle);
		startActivity(intent);
	}

}
