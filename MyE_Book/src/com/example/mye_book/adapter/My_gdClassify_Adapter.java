package com.example.mye_book.adapter;


import java.util.ArrayList;
import java.util.HashMap;

import com.example.mye_book.R;





import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class My_gdClassify_Adapter extends BaseAdapter {
	String TAG="My_gdClassify_Adapter";
	
	 private Context context;
	 private LayoutInflater mInflater;// ∂ØÃ¨≤ºæ÷”≥…‰ 
	 private ArrayList<HashMap<String,Object>> mDate;
	 
	 public final class ViewHolder{
		 public TextView text;
		 public ImageView image;
	 }

	 public My_gdClassify_Adapter(Context context, ArrayList<HashMap<String,Object>> mDate) {
		// TODO Auto-generated constructor stub
		 this.context=context;
		 this.mInflater=LayoutInflater.from(context);
		 this.mDate=mDate;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDate.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	

	@Override
	public View getView( int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 ViewHolder holder=null;
		if (convertView==null) {
			holder=new ViewHolder();
			convertView=mInflater.inflate(R.layout.layout_gdclassify_item, null);
			holder.text=(TextView) convertView.findViewById(R.id.gdclassify_item_title);
			holder.image=(ImageView) convertView.findViewById(R.id.image);
			convertView.setTag(holder);
		}else {
			holder=(ViewHolder) convertView.getTag();
		}
	    final String show_txt=mDate.get(position).get("text").toString();
		holder.text.setText(show_txt);
		holder.image.setImageResource((Integer) mDate.get(position).get("image"));
		
		holder.image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v(TAG, show_txt+"clicked");
			}
		});
		return convertView;
	}

}
