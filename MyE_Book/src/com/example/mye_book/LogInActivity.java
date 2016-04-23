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

import com.example.mye_book.vo.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogInActivity extends Activity implements OnClickListener{
	String TAG="LogInActivity"; 
    Button btn_login,btn_singup;
    EditText account,passwd;
    
    User user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_login);
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		btn_login=(Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
		btn_singup=(Button) findViewById(R.id.btn_sign_up);
		btn_singup.setOnClickListener(this);
		
		account=(EditText) findViewById(R.id.edt_zhanghao);
		passwd=(EditText) findViewById(R.id.edt_mima);
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_login:
			Log.v(TAG, "login click!");
			
			new Thread(login).start();
			break;
		case R.id.btn_sign_up:
			Log.v(TAG, "sign click!");
			Intent intent=new Intent();
			intent.setClass(LogInActivity.this, SignInActivity.class);//��ת��ע�����
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}
	
	private Runnable login=new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String name=account.getText().toString();
			String password=passwd.getText().toString();
			Log.v(TAG, name+" "+password);
			HttpClient http = new DefaultHttpClient();
			HttpPost post = new HttpPost(URL.LOGIN_URL);
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("Name", name));
			parameters.add(new BasicNameValuePair("Pass", password));
			logHandler.sendEmptyMessage(CONNECTION);
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
					logHandler.sendEmptyMessage(CONNECTION_FAILD);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	};
	protected void parseJson(String result) throws JSONException {
		// TODO Auto-generated method stub
		Log.e(TAG, result);
		JSONObject jsonObject=new  JSONObject(result);
		String login_flag=jsonObject.getString("login");

		JSONObject userJson=jsonObject.getJSONObject("user");
		int userid= userJson.getInt("id");

		user=new User();
		user.setUserid(userid);
	
		if(login_flag.equals("success")){
			logHandler.sendEmptyMessage(LOGIN_SUCCESS);
		}
		if(login_flag.equals("faild")){
			logHandler.sendEmptyMessage(LOGIN_FAILD);
		}
		if(login_flag.equals("nostate")){
                logHandler.sendEmptyMessage(NOSTATE);
			
		}
		if(login_flag.equals("")){
			logHandler.sendEmptyMessage(TIMEOUT);
		}
	}
	
	/**
	 * �������������
	 */
	final int CONNECTION_FAILD=2,CONNECTION=3,LOGIN_SUCCESS=4,LOGIN_FAILD=5,TIMEOUT=6,NOSTATE=7,NO_ACCOUNT=8;
	Handler logHandler=new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case CONNECTION_FAILD:
			   showToast("登陆失败！");
			   break;
			case CONNECTION:
				showToast("正在连接...");
				break;
			case LOGIN_SUCCESS:
				showToast("登陆成功");
				//跳转至主界面
				Intent intent=new Intent();
				intent.setClass(LogInActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
				break;
			case LOGIN_FAILD:
				showToast("登陆失败，请检查账号密码是否正确");
				break;
			case TIMEOUT:
				showToast("服务器连接超时");
				break;
			case NOSTATE:
				showToast("该账号未激活！");
				break;
			}
		}
		
	};

	protected void showToast(String message) {
		// TODO Auto-generated method stub
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

}
