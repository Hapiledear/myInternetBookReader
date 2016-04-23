package com.example.mye_book;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.mye_book.adapter.My_gdClassify_Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ClassifyActivity extends Activity {

	String TAG="ClassifyActivity";


	private int img[]={R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,
			R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,
			R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,
			R.drawable.ic_launcher,R.drawable.ic_launcher};
	private String[] txt={"测试","搞笑","战斗","科幻","恋爱","侦探","魔法","神鬼","校园","恐怖","其他"};
	private ArrayList<HashMap<String,Object>>mDate;
	private TextView txt_title;
	private Button btn_right;
	private GridView gd_classify;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_classify);
		mDate=AdaptGrid();
		init();
		
	}

	private ArrayList<HashMap<String,Object>> AdaptGrid() {
		// TODO Auto-generated method stub
		ArrayList<HashMap<String,Object>>list=new ArrayList<HashMap<String,Object>>();
		for (int i = 0; i < txt.length; i++) {
			HashMap< String, Object> map= new HashMap<String, Object>();
			map.put("image", img[i]);
			map.put("text", txt[i]);
			list.add(map);
		}
		return list;
	
	}

	private void init() {
		// TODO Auto-generated method stub
		txt_title=(TextView) findViewById(R.id.txt_title);
		
		btn_right=(Button) findViewById(R.id.btn_right);
		btn_right.setVisibility(View.GONE);
		
		gd_classify=(GridView) findViewById(R.id.gd_classify);
		My_gdClassify_Adapter adapter=new My_gdClassify_Adapter(getBaseContext(),mDate);
		gd_classify.setAdapter(adapter);
		
	}
	
}
