package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import JDBC.DatabaseConnection;
import VO.Book;
import VO.Chapter;

public class ChapterDao {

	private DatabaseConnection dbc;
	private Connection conn;
	
	public ChapterDao() {
		// TODO Auto-generated constructor stub
	}
	
	   public Chapter  findChapterByID(int id) throws Exception{
	    	dbc=new DatabaseConnection();
	 		conn=dbc.getConnection();
	 		String sql="SELECT * FROM tb_chapter WHERE c_id=?";
	 		PreparedStatement pstm=conn.prepareStatement(sql);
	 		pstm.setInt(1, id);
	 		ResultSet rs=pstm.executeQuery();
	 		Chapter chapter=new Chapter();
			if(rs.next()){
				chapter_sets(rs, chapter);
				conn.commit();
				pstm.close();
				conn.close();
				return chapter;
			}else{
				return chapter;
			}
			
	     }
	
	private void chapter_sets(ResultSet rs, Chapter chapter)
			throws SQLException {
		chapter.setBook_id(rs.getString("b_id"));
		chapter.setChapter_id(rs.getInt("c_id"));
		chapter.setChapter_name(rs.getString("c_name"));
		chapter.setDate(rs.getDate("date"));
		chapter.setUrl(rs.getString("url"));
	}
	
}
