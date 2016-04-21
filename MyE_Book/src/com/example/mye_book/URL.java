package com.example.mye_book;

public class URL {
	public static final String localhost="http://192.168.199.129:8080";
	public static final String ServerName="/MyE_Book_Server";
	public static final String LOGIN_URL=localhost+ServerName+"/LoginServer?flag=1";
	public static final String REGISTER=localhost+ServerName+"/LoginServer?flag=2&type=1";
	public static final String REGISTER_MAIL=localhost+ServerName+"/LoginServer?flag=2&type=2"; 
    public static final String[] RECOMMEND_IMAGE_URL=new String[]{
    	localhost+ServerName+"/img/recommend/p1.jpg",
    	localhost+ServerName+"/img/recommend/p2.jpg",
    	localhost+ServerName+"/img/recommend/p3.jpg",
    	localhost+ServerName+"r/img/recommend/p4.jpg"
    	};
    public static final String GET_RECOMMEND_BOOK_INFORMATION=localhost+ServerName+"/BookServer?flag=1";
    public static final String GET_BOOK_INFORMATION_BY_ID=localhost+ServerName+"/BookServer?flag=2";
    
    public static final String GET_IMG_URL=localhost+ServerName+"/GetimgServer";
    
    public static final String GET_CHAPTER=localhost+ServerName+"/ChapterServer?flag=1";
    
}
