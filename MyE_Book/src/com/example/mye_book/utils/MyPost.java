package com.example.mye_book.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


public class MyPost {
	String TAG="MyPost";
	HttpClient http ;
	HttpPost post ;
	String url;
	List<NameValuePair> parameters = new ArrayList<NameValuePair>();
	public MyPost(String url) {
		// TODO Auto-generated constructor stub
		 http = new DefaultHttpClient();
		 post = new HttpPost(url);
		 this.url=url;
	}
	
	public void addPostValue(String name,String value){
		parameters.add(new BasicNameValuePair(name,value));
		Log.v(TAG, name+"="+value);
	}

	public String Startpost() {
		
			try {
				post.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));//���ô�������������д���
				HttpResponse httpResponse = http.execute(post);//��ȡ����ķ��ؽ��
				if (httpResponse.getStatusLine().getStatusCode() == 200) {// �������Ӧ
					Log.e(TAG, "success!");
					String result = EntityUtils.toString(httpResponse
							.getEntity(),"utf-8");//获取响应结果
					return URLDecoder.decode(result,"utf-8");
				} else {
					Log.e(TAG,"fail");
					return null;
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		
	}
	
	 public Bitmap getURLimage() {  
	        Bitmap bmp = null;  
	        try {  
	            URL myurl = new URL(url);  
	            // �������  
	            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();  
	            conn.setConnectTimeout(6000);//���ó�ʱ  
	            conn.setDoInput(true);  
	            conn.setUseCaches(false);//������  
	            conn.connect();  
	            InputStream is = conn.getInputStream();//���ͼƬ��������  
	            bmp = BitmapFactory.decodeStream(is);  
	            is.close();  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        return bmp;  
	    }  
}
