package com.example.mye_book.vo;

public class User {
	private int userid;
	private String name;
	private String password;
	public int getUserid(){
		return userid;
	}
	public void setUserid(int i){
		this.userid=i;
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name=name;
	}
	public String getPassword(){
		return password;
	}
	public void setPassword(String password){
		this.password=password;
	}

}
