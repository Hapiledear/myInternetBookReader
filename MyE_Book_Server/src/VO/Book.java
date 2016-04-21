package VO;

import java.io.Serializable;
import java.sql.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class Book implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -482993740333508937L;
	private String id;
	private String url;
	private String author;
	private String lab;
	private int click_num;
	private int collection_num;
	@JSONField(format="yyyy-MM-dd")
	private Date update;
	private int state;
	private String name;
	private String Introduction;
	public String getIntroduction() {
		return Introduction;
	}
	public void setIntroduction(String introduction) {
		Introduction = introduction;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Book [id=" + id + ", url=" + url + ", author=" + author
				+ ", lab=" + lab + ", click_num=" + click_num
				+ ", collection_num=" + collection_num + ", update=" + update
				+ ", state=" + state + ", name=" + name + ", Introduction="
				+ Introduction + "]";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getLab() {
		return lab;
	}
	public void setLab(String lab) {
		this.lab = lab;
	}
	public int getClick_num() {
		return click_num;
	}
	public void setClick_num(int click_num) {
		this.click_num = click_num;
	}
	public int getCollection_num() {
		return collection_num;
	}
	public void setCollection_num(int collection_num) {
		this.collection_num = collection_num;
	}
	public Date getUpdate() {
		return update;
	}
	public void setUpdate(Date date) {
		this.update = date;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}

	
	
}
