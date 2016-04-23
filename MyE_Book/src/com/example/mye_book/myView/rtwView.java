package com.example.mye_book.myView;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.mye_book.BookInfoActivity;
import com.example.mye_book.R;
import com.example.mye_book.URL;
import com.example.mye_book.utils.MyPost;
import com.example.mye_book.vo.Book;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;

public class rtwView extends FrameLayout {
	public class BookCellClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			LinearLayout layout=(LinearLayout) findViewById(v.getId());
			Log.v(TAG, "book_cell_"+v.getId()+" clicked!");
			Log.v(TAG, "book_id"+layout.getTag().toString());
			int Book_id=Integer.parseInt(layout.getTag().toString());
			Book theBook=books.get(Book_id-1);//�����±���㿪ʼ
			Log.v(TAG, theBook.toString());
			
			Intent intent_to_bookinfo=new Intent(getContext(),BookInfoActivity.class);
			Bundle bundle=new Bundle();
			bundle.putSerializable("theBook", (Serializable) theBook);
			intent_to_bookinfo.putExtras(bundle);
		    getContext().startActivity(intent_to_bookinfo);
		    
		}

	}

	public class GetImgTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				getImgUrl();
				return true;
			} catch (Exception e) {
				// TODO: handle exception
				 e.printStackTrace(); 
				return false;
			}
		}
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			initUI(context);
		}
	}

	public class MyOnCheckedChagneListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
		RadioButton checkedrb=(RadioButton) findViewById(checkedId);
		Log.v(TAG, "radiobutton"+checkedrb.getId()+"clicked!");

		}

	}

	public class MyButtonClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.v(TAG, "btn_more click!");

		}

	}
    String TAG="rtwView";
 	/**
 	 * 0为热门1为更新2为完结
 	 */
	int showType;
	String[] imgUrls;
	private List<Book>books=new ArrayList<Book>();
	private Context context;  
	private ImageLoader imageLoader = ImageLoader.getInstance(); 
	private DisplayImageOptions options;
	
	Button btn_more;
	TextView txt_show;
	ImageView img_icon;
	ScrollView scrollView;
	List<LinearLayout> book_cell_list= new ArrayList<LinearLayout>();
	

	public void getImg(String result) {
		// TODO Auto-generated method stub
		JSONObject jsonObject=JSON.parseObject(result);
		String next=jsonObject.getString("next");
		if (next.equals("true")) {
			JSONArray bookjson=jsonObject.getJSONArray("books");
			books=JSON.parseArray(bookjson.toJSONString(), Book.class);
			Log.v(TAG, books.size()+"");
			imgUrls=new String[books.size()];
			for (int i = 0; i < books.size(); i++) {
				imgUrls[i]=URL.localhost+URL.ServerName+"/"+books.get(i).getUrl();
			}
		}else{
			Log.v(TAG, "获取失败!");
		}
	}
	public boolean getImgUrl() {
		// TODO Auto-generated method stub
		MyPost post=new MyPost(URL.GET_IMG_URL);
		post.addPostValue("showType", String.valueOf(showType));
		String result = null;
		boolean isEmpty=true;
		try {
			 result=post.Startpost();
			 isEmpty=result.isEmpty();
			Log.v(TAG, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (isEmpty) {
			return false;
		}else{
			getImg(result);
			return true;
		}
	
	}

	public rtwView(Context context) {
		// TODO Auto-generated constructor stub
		this(context,null);
	}
	public rtwView(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		this(context,attrs,0);
	}
	public rtwView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (!isInEditMode()) {
			// TODO Auto-generated constructor stu
			this.context = context;
			init(context);
			initImageLoader(context);
			initDate();
		}
		
		
	}
	
	private void init(Context context) {
		// TODO Auto-generated method stub
		LayoutInflater.from(context).inflate(R.layout.layout_rtw_view, this,true);
		btn_more=(Button) findViewById(R.id.btn_detail);
		btn_more.setOnClickListener(new MyButtonClickListener());
		txt_show=(TextView) findViewById(R.id.txt_show);
		img_icon=(ImageView) findViewById(R.id.img_show);
	}
	private void initImageLoader(Context context2) {
		// TODO Auto-generated method stub
		  ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
	        .threadPriority(Thread.NORM_PRIORITY - 2)//����ͼƬ���߳���
	        		.denyCacheImageMultipleSizesInMemory()///����ͼ��Ĵ�ߴ罫���ڴ��л�����ǰ����ͼ���С�ߴ�
	        		.discCacheFileNameGenerator(new Md5FileNameGenerator())//���ô��̻����ļ�����
	        		.tasksProcessingOrder(QueueProcessingType.LIFO)//���ü�����ʾͼƬ���н���
	        		.writeDebugLogs()
	        		.build();  
	      //提交设置
	        ImageLoader.getInstance().init(config);  
	        
	        options = new DisplayImageOptions.Builder()  
            .showImageOnLoading(R.drawable.ic_launcher)  
            .showImageOnFail(R.drawable.defualt)  
            .cacheInMemory(true)  
            .cacheOnDisk(true)  
            .build();  
	        
	}
	private void initDate() {
		// TODO Auto-generated method stub
		
		new GetImgTask().execute("");
	}
	private void initUI(Context context) {
		// TODO Auto-generated method stub
		 if(imgUrls == null || imgUrls.length == 0)  
	            return;  
		
		LinearLayout scroll_ly=(LinearLayout) findViewById(R.id.scroll_ly);
		scroll_ly.removeAllViews();
		
		for (int i = 0; i < imgUrls.length; i++) {
			LinearLayout layout=new LinearLayout(context);
			LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(135, 180);//���
			layoutParams.setMargins(5, 0, 5, 0);//�߾�
			layout.setLayoutParams(layoutParams);
			layout.setGravity(Gravity.CENTER);//���뷽ʽ
			layout.setOrientation(LinearLayout.VERTICAL);//��ֱ����(����)
			layout.setId(i);
			layout.setTag(books.get(i).getId());
			layout.setOnClickListener(new BookCellClickListener());
			
			ImageView imageView=new ImageView(context);
			LayoutParams params=new LayoutParams(135, 150);
			imageView.setLayoutParams(params);
			imageLoader.displayImage(imgUrls[i], imageView,options);//����ͼƬ
			layout.addView(imageView);
			
			TextView textView=new TextView(context);
			textView.setText(books.get(i).getName());
			layout.addView(textView);
			
		    scroll_ly.addView(layout);
			book_cell_list.add(layout);
		}
		
		
	}
	
	public void setTxt_show(String txt) {
		Log.v(TAG, ""+txt);
		this.txt_show.setText(txt);
	}
	public void setShowType(int showType){
		this.showType=showType;
	}
	public void setImg_icon(Drawable img) {
		this.img_icon.setImageDrawable(img);
	}


}
