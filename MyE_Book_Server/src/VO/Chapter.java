package VO;

import java.sql.Date;

public class Chapter {
	
	private String book_id;
	private int chapter_id;
	private String chapter_name;
	private String url;
	private Date date;
	@Override
	public String toString() {
		return "Chapter [book_id=" + book_id + ", chapter_id=" + chapter_id
				+ ", chapter_name=" + chapter_name + ", url=" + url + ", date="
				+ date + "]";
	}
	public String getBook_id() {
		return book_id;
	}
	public void setBook_id(String book_id) {
		this.book_id = book_id;
	}
	public int getChapter_id() {
		return chapter_id;
	}
	public void setChapter_id(int chapter_id) {
		this.chapter_id = chapter_id;
	}
	public String getChapter_name() {
		return chapter_name;
	}
	public void setChapter_name(String chapter_name) {
		this.chapter_name = chapter_name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

}
