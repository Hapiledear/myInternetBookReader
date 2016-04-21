package com.example.mye_book;

import java.sql.Date;
import java.text.Format;
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
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.mye_book.myView.SlideShowView;
import com.example.mye_book.myView.SlideShowView.SlideShowViewListener;
import com.example.mye_book.myView.rtwView;
import com.example.mye_book.vo.Book;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener,OnCheckedChangeListener{
	String TAG="MainActivity";
	private long mExitTime = 0; 
	private Button btn_find;
	private SlideShowView slideShowView;
	private String position;
	private RadioGroup rg;
	private RadioButton rb_1,rb_2,rb_3;
	private rtwView rView,tView,wView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		init();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {  
          if ((System.currentTimeMillis() - mExitTime) > 2000) {
              // ������ΰ���ʱ��������2000���룬���˳�  
              Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();  
              mExitTime = System.currentTimeMillis();// ����mExitTime  
          } else {  
              System.exit(0);// �����˳�����  
          }  
          return true;  
      }  
      return super.onKeyDown(keyCode, event);  
	}
	private void init() {
		// TODO Auto-generated method stub
		btn_find=(Button) findViewById(R.id.btn_right);
		btn_find.setText("find");
		btn_find.setOnClickListener(this);
		
		slideShowView=(SlideShowView) findViewById(R.id.slideshowView);
		slideShowView.setOnPageClickListener(new SlideShowViewListener() {
			
			@Override
			public void onClick(int position) {
				// TODO Auto-generated method stub
				Log.v(TAG, position+"�������");
				MainActivity.this.position=position+1+"";
				new Thread(getb_id).start();
			}
		});
		
		rg=(RadioGroup) findViewById(R.id.rg);
		rg.setOnCheckedChangeListener(this);
		rb_1=(RadioButton) findViewById(R.id.radioButton1);
		rb_2=(RadioButton) findViewById(R.id.radioButton2);
		rb_3=(RadioButton) findViewById(R.id.radioButton3);
		
		rView=(rtwView) findViewById(R.id.rView);
		rView.setTxt_show("�������");
		rView.setShowType(0);
		
		tView=(rtwView) findViewById(R.id.tView);
		tView.setTxt_show("�������");
		tView.setShowType(1);
		
		wView=(rtwView) findViewById(R.id.wView);
		wView.setTxt_show("��ᾭ��");
		wView.setShowType(2);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_right:
			Log.v(TAG, "btn_find click!");
			break;
		default:
			break;
		}
	}
	
	private Runnable getb_id=new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			HttpClient http = new DefaultHttpClient();
			HttpPost post = new HttpPost(URL.GET_RECOMMEND_BOOK_INFORMATION);
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("position", position));
			try {
				post.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));//���ô�������������д���
				HttpResponse httpResponse = http.execute(post);//��ȡ����ķ��ؽ��
				
				if (httpResponse.getStatusLine().getStatusCode() == 200) {// �������Ӧ
					Log.e(TAG, "success!");
					String result = EntityUtils.toString(httpResponse
							.getEntity());// ��ȡ���е��ַ���
					parseJson(result);
				} else {
					Log.e(TAG,"fail");
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	protected void parseJson(String result) throws JSONException {
		// TODO Auto-generated method stub
		Log.v(TAG, result);
		
		JSONObject jsonObject=JSON.parseObject(result);
		String next=jsonObject.getString("next");
		if(next.equals("true")){
			JSONObject bookjson=jsonObject.getJSONObject("book");
			Book book=JSON.parseObject(bookjson.toJSONString(),Book.class);
			handler.sendEmptyMessage(SUCCESS);
			Log.v(TAG, book.toString());
		}else{
			Log.v(TAG, "������δ֪����");
			handler.sendEmptyMessage(FAIL);
		}
	}
	final int SUCCESS=1,FAIL=2;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				showToast("��ȡ���ݳɹ���");
				//�鼮��ϸ�����л�
				break;
			case FAIL:
				showToast("û���鼮��Ϣ��֮��Ӧ");
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

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.radioButton1:
			showToast("���౻ѡ��");
			Intent intent=new Intent(MainActivity.this,ClassifyActivity.class);
			startActivity(intent);
			break;
        case R.id.radioButton2:
			showToast("���б�ѡ��");
			break;
		case R.id.radioButton3:
			showToast("�Ƽ���ѡ��");
			break;

		default:
			break;
		}
	}

}
