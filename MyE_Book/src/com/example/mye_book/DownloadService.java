package com.example.mye_book;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.mye_book.myView.rtwView.GetImgTask;
import com.example.mye_book.vo.Chapter;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

public class DownloadService extends Service {

	
	public class Download extends Thread {
		private boolean downloading=true;
		private Chapter chapter;
		private int progress=0;
		public Download(Chapter chapter) {
			// TODO Auto-generated constructor stub
			 this.chapter=chapter;
			
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {  
                //服务器上新版apk地址  
				String cp_url="http://192.168.199.129:8080/MyE_Book_Server/"+chapter.getUrl();
                URL url = new URL(cp_url);  
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
                conn.connect();  
                int length = conn.getContentLength();  
                InputStream is = conn.getInputStream();  
                File file = new File(filePath+chapter.getBook_id()+"/"); //书籍 文件夹,id编号
                if(!file.exists()){  
                    //如果文件夹不存在,则创建  
                    file.mkdir();  
                }  
                //下载服务器中新版本软件（写文件）  
                String bookFile = filePath;  
                File ChapterFile = new File(bookFile); //章节文件路径
                @SuppressWarnings("resource")
				FileOutputStream fos = new FileOutputStream(ChapterFile);  
                int count = 0;  
                byte buf[] = new byte[1024];  
                do{  
                    int numRead = is.read(buf);  
                    count += numRead;  
                    //更新进度条  
                    progress = (int) (((float) count / length) * 100);  
                    Message message=new Message();
                    message.arg1=chapter.getChapter_id();
                    message.arg2=progress;
                    message.what=1;
                    handler.sendMessage(message);
                  
                    try {
						sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                  
                    if(numRead <= 0){  
                        //下载完成通知安装  
                    	 Message message1=new Message();
                         message1.arg1=chapter.getChapter_id();
                         message1.arg2=100;
                         message1.what=0;
                       handler.sendMessage(message1);
                       downloading=false;
                       break;  
                    }  
                    fos.write(buf,0,numRead);  
                    //当点击取消时，则停止下载  
                }while(downloading); 
               
                
            } catch (MalformedURLException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
		}

	}
	public String TAG="DownloadService";
	public  static final int Download_Notification_id=1;
	
	private NotificationManager mNotificationManager;

	
	private String filePath;
	
	private List<Chapter> chapters=new ArrayList<Chapter>();
	private List<Notification> noti=new ArrayList<Notification>();
	
	private int ThreadSize=3,doneThread=0;
	private ExecutorService fixedThreadPool;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.v(TAG, "Service启动");
		//初始化操作
		chapters=(List<Chapter>) intent.getSerializableExtra("chapters");
		Log.v(TAG, "chapters:"+chapters.toString());
		filePath=Environment.getExternalStorageDirectory().getAbsolutePath() + "/DownloadBookFile/";
		
		
		//downlode_sample.start();//模拟下载线程
		for (Chapter chapter:chapters) {
			fixedThreadPool =Executors.newFixedThreadPool(ThreadSize);
			Download download=new Download(chapter);
			fixedThreadPool.execute(download);
			creatNotification(chapter.getChapter_name(),chapter.getChapter_id());
		}
		fixedThreadPool.shutdown();
		
		
		return START_REDELIVER_INTENT;
	}

	private void creatNotification(String name, int id) {
		mNotificationManager = (NotificationManager) getSystemService(  
		            android.content.Context.NOTIFICATION_SERVICE);  
		//notification的设置，绝对不能省去
		
		Notification mNotification= new Notification();  
		mNotification.icon=R.drawable.ic_launcher;
		mNotification.when=System.currentTimeMillis();
		noti.add(mNotification);
		//自定义的
		RemoteViews mRemoteView = new RemoteViews(this.getPackageName(), R.layout.remote_view_layout);
		mRemoteView.setImageViewResource(R.id.image	, R.drawable.ic_launcher);
		mRemoteView.setTextViewText(R.id.text, name);
		mRemoteView.setProgressBar(R.id.progress_horizontal, 100, 0, false);
		
		mNotification.contentView=mRemoteView;
		mNotification.flags=Notification.FLAG_NO_CLEAR;
		mNotificationManager.notify(id, mNotification);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v(TAG, "service结束");
		mNotificationManager.cancelAll();
	}
	
	 private Handler handler = new Handler() {  
	        public void handleMessage(Message msg) {  
	        	int id=msg.arg1;
	        	int progress=msg.arg2;

	        	Notification mNotification=noti.get(id-1);
            	RemoteViews mRemoteView =mNotification.contentView;
	            switch (msg.what) {  
	            case 1:  
	            	Log.v(TAG, id+"更新进度:"+progress);
	                // 更新进度情况  
	            	
	            	mRemoteView.setProgressBar(R.id.progress_horizontal, 100, progress, false);
					mNotificationManager.notify(id, mNotification);
	                break;  
	            case 0:  
	                // 下载任务完成
	            	Log.v(TAG, id+"下载完成");
	            	mRemoteView.setTextViewText(R.id.text, id+"下载完成");
	    			mNotification.flags=Notification.FLAG_AUTO_CANCEL;
	    			mNotificationManager.notify(id, mNotification);
	    			doneThread=doneThread+1;
	    			if (doneThread==chapters.size()) {
						Log.v(TAG, "所有任务都完成了");
						onDestroy();
					}
	                break;  
	           
	            default:  
	                break;  
	            }  
	        };  
	    };  
	
	 
	
}
