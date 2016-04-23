package DAO;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import JDBC.DatabaseConnection;
import VO.Book;
import VO.Chapter;
import VO.Seting;
public class BookDao {
	private DatabaseConnection dbc;
	private Connection conn;
	
	public  BookDao() {
		
	}
	
	public String getRecommendBookID(int position) throws Exception{
		dbc=new DatabaseConnection();
		conn=dbc.getConnection();
		String sql="SELECT * FROM tb_recommend WHERE id=?";
		String bid=null;
		PreparedStatement pstm=conn.prepareStatement(sql);
		pstm.setInt(1, position);
		ResultSet rs=pstm.executeQuery();
		if(rs.next()){
			bid=rs.getString("b_id");
			conn.commit();
			pstm.close();
			conn.close();
			return bid;
		}else{
			return bid;
		}
	}
	
	
     public Book  findBookByID(String id) throws Exception{
    	dbc=new DatabaseConnection();
 		conn=dbc.getConnection();
 		String sql="SELECT * FROM tb_book WHERE id=?";
 		PreparedStatement pstm=conn.prepareStatement(sql);
 		pstm.setString(1, id);
 		ResultSet rs=pstm.executeQuery();
 		Book book=new Book();
		if(rs.next()){
			booksets(rs, book);
			conn.commit();
			pstm.close();
			conn.close();
			return book;
		}else{
			return book;
		}
		
     }

	private void booksets(ResultSet rs, Book book) throws SQLException {
		book.setId(rs.getString("id"));
		book.setUrl(rs.getString("url"));
		book.setAuthor(rs.getString("author"));
		book.setLab(rs.getString("lab"));
		book.setClick_num(rs.getInt("click_num"));
		book.setCollection_num(rs.getInt("collection_num"));
		book.setUpdate(rs.getDate("update"));
		book.setState(rs.getInt("state"));
		book.setName(rs.getString("name"));
		book.setIntroduction(rs.getString("Introduction"));
	}

     /**
      * 热门书籍规则：点击量和收藏量达到一定数量级
      * @return
      * @throws Exception
      */
	public List<Book> getHotbooks() throws Exception {
		// TODO Auto-generated method stub
		dbc=new DatabaseConnection();
 		conn=dbc.getConnection();
 		String sql="SELECT * FROM tb_book WHERE  click_num>? AND collection_num>?";
 		PreparedStatement pstm=conn.prepareStatement(sql);
 		pstm.setInt(1, Seting.hot_book_click_number);
 		pstm.setInt(2,Seting.hot_book_collection_number);
 		ResultSet rs=pstm.executeQuery();
 		List<Book> books=new ArrayList<Book>();
 		if(rs!=null){
 			while (rs.next()) {
				Book book = new Book();
				booksets(rs, book);
				
				books.add(book);
			}
 			conn.commit();
			pstm.close();
			conn.close();
 		}else{
 			
 		}
 		return books;

	}

	/**
	 * 根据更新日期降序排序
	 * @return
	 * @throws Exception 
	 */
	public List<Book> getUpdatebooks() throws Exception {
		// TODO Auto-generated method stub
		dbc=new DatabaseConnection();
 		conn=dbc.getConnection();
 		String sql="SELECT * FROM tb_book ORDER BY tb_book.update DESC";
 		PreparedStatement pstm=conn.prepareStatement(sql);
 		ResultSet rs=pstm.executeQuery();
 		List<Book> books=new ArrayList<Book>();
 		if(rs!=null){
 			while (rs.next()) {
				Book book = new Book();
				booksets(rs, book);
				books.add(book);
			}
 			conn.commit();
			pstm.close();
			conn.close();
 		}else{
 			
 		}
 		return books;
	}

	/**
	 * state字段为1，表示完结书籍
	 * @return
	 * @throws Exception
	 */
	public List<Book> getFinishbooks() throws Exception {
		// TODO Auto-generated method stub
		dbc=new DatabaseConnection();
 		conn=dbc.getConnection();
 		String sql="SELECT * FROM `tb_book` WHERE state=1 ORDER BY tb_book.`update` DESC";
 		PreparedStatement pstm=conn.prepareStatement(sql);
 		ResultSet rs=pstm.executeQuery();
 		List<Book> books=new ArrayList<Book>();
 		if(rs!=null){
 			while (rs.next()) {
				Book book = new Book();
				booksets(rs, book);
				books.add(book);
			}
 			conn.commit();
			pstm.close();
			conn.close();
 		}else{
 			
 		}
 		return books;
	}

	public List<Chapter> findAllChapterByID(String book_id) throws Exception {
		// TODO Auto-generated method stub
		dbc=new DatabaseConnection();
		conn=dbc.getConnection();
		String sql="SELECT * FROM tb_chapter WHERE b_id=?";
		PreparedStatement pstm=conn.prepareStatement(sql);
		pstm.setString(1, book_id);
 		ResultSet rs=pstm.executeQuery();
 		List<Chapter> chapters=new ArrayList<Chapter>();
 		if(rs!=null){
 			while (rs.next()) {
			Chapter chapter=new Chapter();
			chapter_sets(rs, chapter);
			chapters.add(chapter);
			}
 			conn.commit();
			pstm.close();
			conn.close();
 		}else{
 			
 		}
		return chapters;
	}

	private void chapter_sets(ResultSet rs, Chapter chapter)
			throws SQLException {
		chapter.setBook_id(rs.getString("b_id"));
		chapter.setChapter_id(rs.getInt("c_id"));
		chapter.setChapter_name(rs.getString("c_name"));
		chapter.setDate(rs.getDate("date"));
		chapter.setUrl(rs.getString("url"));
	}

	public List<Book> findBooksByClassify(String book_classify) {
		// TODO Auto-generated method stub
		List<Book> books=new ArrayList<Book>();
		try {
			dbc=new DatabaseConnection();
			conn=dbc.getConnection();
	 		String sql="SELECT * FROM tb_book WHERE lab LIKE ? ORDER BY click_num DESC";
	 		PreparedStatement pstm=conn.prepareStatement(sql);
	 		String parma1=new String("%"+book_classify+"%");
	 		System.out.println("标签为:"+parma1);
	 		pstm.setString(1, parma1);
	 		ResultSet rs=pstm.executeQuery();
	 		
	 		if(rs!=null){
	 			while (rs.next()) {
					Book book = new Book();
					booksets(rs, book);
					books.add(book);
				}
	 			conn.commit();
				pstm.close();
				conn.close();
	 		}else{
	 			
	 		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
		return books;
	}
}
