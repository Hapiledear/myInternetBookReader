package com.example.mye_book;

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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignInActivity extends Activity implements OnClickListener{
    String TAG="SignInActivity";
	TextView txt_account,txt_passwd,txt_passwd_again,txt_hit;
	EditText edt_account,edt_passwd,edt_passwd_again;
	String   str_account,str_passwd,str_passwd_again;
	Button   btn_ok;
	boolean  check_ok=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_signin);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		txt_account=(TextView) findViewById(R.id.txt_account);
		txt_passwd=(TextView) findViewById(R.id.txt_passwd);
		txt_passwd_again=(TextView) findViewById(R.id.txt_passwd_again);
		txt_hit=(TextView) findViewById(R.id.txt_hit);
		txt_hit.setVisibility(View.GONE);
		
		edt_account=(EditText) findViewById(R.id.edt_zhanghao);
		edt_passwd=(EditText) findViewById(R.id.edt_mima);
		edt_passwd_again=(EditText) findViewById(R.id.edt_passwd_again);
		
		
		btn_ok=(Button) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.btn_ok:
			check();
			Log.v(TAG, "btn_ok click!");
			if(check_ok){
				new Thread(sign_in).start();
			}
			break;
		default:
			break;
		}
	}
/***
 * ���������Ƿ���Ϲ淶
 */
	private void check() {
		// TODO Auto-generated method stub
		/*���������������ͬ*/
		
		str_account=edt_account.getText().toString();
		str_passwd=edt_passwd.getText().toString();
		str_passwd_again=edt_passwd_again.getText().toString();
		if(!str_passwd.equals(str_passwd_again)){
			txt_hit.setVisibility(View.VISIBLE);
			txt_hit.setText("两次输入的密码不一致！");
			check_ok=false;
		}else{
			txt_hit.setVisibility(View.GONE);
		}
		check_ok=true;
	}



private Runnable sign_in=new Runnable(){

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Log.v(TAG, "账号:"+str_account+"密码:"+str_passwd);
		HttpClient http = new DefaultHttpClient();
		HttpPost post = new HttpPost(URL.REGISTER);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("Name", str_account));
		parameters.add(new BasicNameValuePair("Pass", str_passwd));
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
	JSONObject jsonObject=new  JSONObject(result);
	String signin_flag=jsonObject.getString("register");
	if(signin_flag.equals("success")){
		signHander.sendEmptyMessage(SIGNIN_SUCCESS);
	}
	if(signin_flag.equals("hasuser")){
		signHander.sendEmptyMessage(HAS_USER);
	}
}

final int SIGNIN_SUCCESS=1,SIGNIN_FAILD=2,HAS_USER=3;
Handler signHander=new Handler(){
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case SIGNIN_SUCCESS:
			showToast("ע注册成功！");
			Intent intent=new Intent();
			intent.setClass(SignInActivity.this, LogInActivity.class);//��ת����½����
			startActivity(intent);
			finish();
			break;
		case SIGNIN_FAILD:
			showToast("注册失败.");
			break;
		case HAS_USER:
			showToast("该用户已存在");
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
