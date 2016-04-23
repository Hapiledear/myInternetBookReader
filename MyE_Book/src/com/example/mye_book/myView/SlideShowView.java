package com.example.mye_book.myView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.example.mye_book.R;
import com.example.mye_book.URL;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class SlideShowView extends FrameLayout {
	String TAG="SlideShowView";
	//接口
	public interface SlideShowViewListener 
	{
		public void onClick(int position); //接口方法
	}

     private SlideShowViewListener mSlideShowViewListener;//接口对象
     public void setOnPageClickListener(SlideShowViewListener mSlideShowViewListener)
	 {
	  this.mSlideShowViewListener=mSlideShowViewListener;//绑定接口
	 }
	// ʹ��universal-image-loader�����ȡ����ͼƬ����Ҫ���̵���universal-image-loader-1.8.6-with-sources.jar  
    private ImageLoader imageLoader = ImageLoader.getInstance();  
  
    //显示的最大数
    private final static int IMAGE_COUNT = 5;  
    //�Զ��ֲ���ʱ����  
    private final static int TIME_INTERVAL = 5;  
    //是否自动滚动
    private final static boolean isAutoPlay = true;   
      
    //�Զ����ֲ�ͼ����Դ  
    private String[] imageUrls;  
    //���ֲ�ͼƬ��ImageView ��list  
    private List<ImageView> imageViewsList;  
    //��Բ���View��list  
    private List<View> dotViewsList;  
      
    private ViewPager viewPager;  
    //��ǰ�ֲ�ҳ  
    private int currentItem  = 0;  

	public int getCurrentItem() {
		return currentItem;
	}
	//��ʱ����  
    private ScheduledExecutorService scheduledExecutorService;  
      
    private Context context;  
      
    //Handler  
    private Handler handler = new Handler(){  
  
        @Override  
        public void handleMessage(Message msg) {  
            // TODO Auto-generated method stub  
            super.handleMessage(msg);  
            viewPager.setCurrentItem(currentItem);  
            Log.v(TAG, currentItem+"");
        }  
          
    };  

	  public SlideShowView(Context context) {  
	        this(context,null);  
	        // TODO Auto-generated constructor stub  
	    }  
	    public SlideShowView(Context context, AttributeSet attrs) {  
	        this(context, attrs, 0);  
	        // TODO Auto-generated constructor stub  
	    }  
	    public SlideShowView(Context context, AttributeSet attrs, int defStyle) {  
		super(context, attrs, defStyle);
		if (!isInEditMode()) {
			this.context = context;

			initImageLoader(context);

			initData();
			if (isAutoPlay) {
				startPlay();
			}


		}

	}

	    /** 
	     * ��ʼ�ֲ�ͼ�л� 
	     */  
	    private void startPlay(){  
	        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();  
	        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4, TimeUnit.SECONDS);  
	    }  

	    /** 
	     * ֹͣ�ֲ�ͼ�л� 
	     */  
	    private void stopPlay(){  
	        scheduledExecutorService.shutdown();  
	    }  
	    /** 
	     * ����ImageView��Դ�������ڴ� 
	     *  
	     */  
	    private void destoryBitmaps() {  
	  
	        for (int i = 0; i < IMAGE_COUNT; i++) {  
	            ImageView imageView = imageViewsList.get(i);  
	            Drawable drawable = imageView.getDrawable();  
	            if (drawable != null) {  
	                //���drawable��view������  
	                drawable.setCallback(null);  
	            }  
	        }  
	    }  
	    /** 
	     * ��ʼ�����Data 
	     */  
	    private void initData(){  
	        imageViewsList = new ArrayList<ImageView>();  
	        dotViewsList = new ArrayList<View>();  
	  
	        // �첽�����ȡͼƬ  
	        new GetListTask().execute("");  
	    }  
	  
	    /** 
	     *ִ���ֲ�ͼ�л����� 
	     * 
	     */  
	    private class SlideShowTask implements Runnable{  
	  
	        @Override  
	        public void run() {  
	            // TODO Auto-generated method stub  
	            synchronized (viewPager) {  
	                currentItem = (currentItem+1)%imageViewsList.size();  
	                handler.obtainMessage().sendToTarget();
	            }  
	        }  
	          
	    }  
	    
	    /** 
	     * �첽����,��ȡ���� 
	     *  
	     */  
	    class GetListTask extends AsyncTask<String, Integer, Boolean> {  
	  
	        @Override  
	        protected Boolean doInBackground(String... params) {  
	            try {  
	                // ����һ����÷���˽ӿڻ�ȡһ���ֲ�ͼƬ   
	              imageUrls=URL.RECOMMEND_IMAGE_URL;
	                return true;  
	            } catch (Exception e) {  
	                e.printStackTrace();  
	                return false;  
	            }  
	        }  
	  
	        @Override  
	        protected void onPostExecute(Boolean result) {  
	            super.onPostExecute(result);  
	            if (result) {  
	                initUI(context);  
	            }  
	        }  
	    }  
	    
	    /** 
	     * ��ʼ��Views��UI 
	     */  
	    private void initUI(Context context){  
	        if(imageUrls == null || imageUrls.length == 0)  
	            return;  
	          
	        LayoutInflater.from(context).inflate(R.layout.layout_sildeshow, this, true);  
	          
	        LinearLayout dotLayout = (LinearLayout)findViewById(R.id.dotLayout);  
	        dotLayout.removeAllViews();  
	          
	        // �ȵ������ͼƬ�������  
	        for (int i = 0; i < imageUrls.length; i++) {  
	            ImageView view =  new ImageView(context);  
	            view.setTag(imageUrls[i]);  
	            if(i==0)//��һ��Ĭ��ͼ  
	                view.setBackgroundResource(R.drawable.ic_launcher);  
	            view.setScaleType(ScaleType.FIT_XY);  
	            imageViewsList.add(view);  
	              
	            ImageView dotView =  new ImageView(context);  
	            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);  
	            params.leftMargin = 4;  
	            params.rightMargin = 4;  
	            dotLayout.addView(dotView, params);  
	            dotViewsList.add(dotView);  
	        }  
	          
	        viewPager = (ViewPager) findViewById(R.id.viewPager);  
	        viewPager.setFocusable(true);  
	      
	        viewPager.setAdapter(new MyPagerAdapter());  
	        viewPager.setOnPageChangeListener(new MyPageChangeListener());  
	    }  
	    /** 
	     * ImageLoader ͼƬ�����ʼ�� 
	     *  
	     * @param context 
	     */  
	    public static void initImageLoader(Context context) {  
	    	 // This configuration tuning is custom. You can tune every option, you may tune some of them,
	        // or you can create default configuration by
	        //  ImageLoaderConfiguration.createDefault(this);
	        // method.
	        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
	        .threadPriority(Thread.NORM_PRIORITY - 2)//����ͼƬ���߳���
	        		.denyCacheImageMultipleSizesInMemory()///����ͼ��Ĵ�ߴ罫���ڴ��л�����ǰ����ͼ���С�ߴ�
	        		.discCacheFileNameGenerator(new Md5FileNameGenerator())//���ô��̻����ļ�����
	        		.tasksProcessingOrder(QueueProcessingType.LIFO)//���ü�����ʾͼƬ���н���
	        		.writeDebugLogs()
	        		.build();  
	      //�ύ����
	        ImageLoader.getInstance().init(config);  
	    }  
	    
	    /** 
	     * ���ViewPager��ҳ�������� 
	     *  
	     */  
	    private class MyPagerAdapter  extends PagerAdapter{  
	  
	        @Override  
	        public void destroyItem(View container, int position, Object object) {  
	            // TODO Auto-generated method stub  
	            //((ViewPag.er)container).removeView((View)object);  
	            ((ViewPager)container).removeView(imageViewsList.get(position));  
	        }  
	  
	        @Override  
	        public Object instantiateItem(View container, final int position) {  
	            ImageView imageView = imageViewsList.get(position);  
	  
	            imageLoader.displayImage(imageView.getTag() + "", imageView);  
	           
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (mSlideShowViewListener != null) {
						mSlideShowViewListener.onClick(position);
					}
				}
			});
	            ((ViewPager)container).addView(imageViewsList.get(position));  
	            return imageViewsList.get(position);  
	        }  
	  
	        @Override  
	        public int getCount() {  
	            // TODO Auto-generated method stub  
	            return imageViewsList.size();  
	        }  
	  
	        @Override  
	        public boolean isViewFromObject(View arg0, Object arg1) {  
	            // TODO Auto-generated method stub  
	            return arg0 == arg1;  
	        }  
	        @Override  
	        public void restoreState(Parcelable arg0, ClassLoader arg1) {  
	            // TODO Auto-generated method stub  
	  
	        }  
	  
	        @Override  
	        public Parcelable saveState() {  
	            // TODO Auto-generated method stub  
	            return null;  
	        }  
	  
	        @Override  
	        public void startUpdate(View arg0) {  
	            // TODO Auto-generated method stub  
	  
	        }  
	  
	        @Override  
	        public void finishUpdate(View arg0) {  
	            // TODO Auto-generated method stub  
	              
	        }  
	          
	    }  
	    
	    /** 
	     * ViewPager�ļ����� 
	     * ��ViewPager��ҳ���״̬�����ı�ʱ���� 
	     *  
	     */  
	    private class MyPageChangeListener implements OnPageChangeListener{  
	  
	        boolean isAutoPlay = false;  
	  
	        @Override  
	        public void onPageScrollStateChanged(int arg0) {  
	            // TODO Auto-generated method stub  
	            switch (arg0) {  
	            case 1:// ���ƻ�����������  
	                isAutoPlay = false;  
	                break;  
	            case 2:// �����л���  
	                isAutoPlay = true;  
	                break;  
	            case 0:// �������������л���ϻ��߼������  
	                // ��ǰΪ���һ�ţ���ʱ�������󻬣����л�����һ��  
	                if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {  
	                    viewPager.setCurrentItem(0);  
	                }  
	                // ��ǰΪ��һ�ţ���ʱ�������һ������л������һ��  
	                else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {  
	                    viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);  
	                }  
	                break;  
	        }  
	        }  
	  
	        @Override  
	        public void onPageScrolled(int arg0, float arg1, int arg2) {  
	            // TODO Auto-generated method stub  
	              
	        }  
	  
	        @Override  
	        public void onPageSelected(int pos) {  
	            // TODO Auto-generated method stub  
	              
	            currentItem = pos;  
	            for(int i=0;i < dotViewsList.size();i++){  
	                if(i == pos){  
	                    ((View)dotViewsList.get(pos)).setBackgroundResource(R.drawable.dot_focus);  
	                }else {  
	                    ((View)dotViewsList.get(i)).setBackgroundResource(R.drawable.dot);  
	                }  
	            }  
	        }  
	          
	    }  
	
	    
}
