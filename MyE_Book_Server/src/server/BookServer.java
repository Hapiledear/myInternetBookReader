package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

import DAO.BookDao;
import VO.Book;
import VO.Chapter;

/**
 * Servlet implementation class BookServer
 */
@WebServlet("/BookServer")
public class BookServer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	String MyCharEncoding = "UTF-8";
	BookDao bookDao=new BookDao();
     
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BookServer() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

	    	String req=request.getQueryString();
		  //  String req=new String(request.getQueryString().getBytes("iso-8859-1"),"utf-8"); 
		    System.out.println(req);
		    switchfunction(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding(MyCharEncoding);
	
		switchfunction(request, response);
	}

	private void switchfunction(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String flag = request.getParameter("flag");

		System.out.println("flag:" + flag);
		switch (Integer.parseInt(flag)) {
		case 1:
		by_recommend(request, response);
			break;
		case 2:
			by_book_id(request,response);
			break;
		case 3:
			by_book_classify(request,response);
			break;
		}
	}
	
	/**
	 * 根据书籍的分类来查找书籍
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void by_book_classify(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		String Book_classify=request.getParameter("classify");
		System.out.println("类别:"+Book_classify);
		JSONObject jsonObj=new JSONObject();
		
		if (Book_classify==null||Book_classify.equals("")) {
			jsonObj.put("result", false);
			jsonObj.put("info", "传入了未知类别");
		}else{
			List<Book> books=new ArrayList<Book>();
			books=bookDao.findBooksByClassify(Book_classify);
			jsonObj.put("result", true);
			jsonObj.put("books", books);
		}
		response.setCharacterEncoding(MyCharEncoding);
    	response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(jsonObj.toString());
		
		
	}

	/**
	 * 根据书籍的在库ID来获取书籍详细信息
	 * 详细信息包括：本书基本信息，本书章节信息和本书评论
	 * @param request
	 * @param response
	 */
	private void by_book_id(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		String Book_id=request.getParameter("Book_id");
		
		System.out.println("Book_id="+Book_id);
		
		List<Chapter> chapters=new ArrayList<Chapter>();
		String id;
		try {
			chapters=bookDao.findAllChapterByID(Book_id);
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		response.setCharacterEncoding(MyCharEncoding);
    	response.setContentType("text/html;charset=utf-8");
		PrintWriter out = null;
		boolean isEmpty=true;
		try {
			out = response.getWriter();
		    isEmpty = chapters.isEmpty();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject jsonObj = new JSONObject();
			if(isEmpty){
				System.out.println("类全为空");
				jsonObj.put("next", "false");
			}else{
				System.out.println("类不全为空");
				jsonObj.put("next", "true");
				jsonObj.put("chapters", chapters);
				
			}
			out.println(jsonObj.toString());
	}

	/**
	 * 根据点击的推荐图片，返回图书基本信息
	 * @param request
	 * @param response
	 *
	 * 
	 */
	@SuppressWarnings("null")
	private void by_recommend(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		String position=request.getParameter("position");
		
		System.out.println("position:" + position);
		Book book = new Book();
		String id;
		try {
			id = bookDao.getRecommendBookID(Integer.parseInt(position));
			System.out.println(id);
			book = bookDao.findBookByID(id);
			System.out.println("id:" + book.getId());
			System.out.println(book.toString());
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		response.setCharacterEncoding(MyCharEncoding);
    	response.setContentType("text/html;charset=utf-8");
		PrintWriter out = null;
		boolean isEmpty=true;
		try {
			out = response.getWriter();
		    isEmpty = book.getId().isEmpty();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject jsonObj = new JSONObject();
			if(isEmpty){
				System.out.println("类全为空");
				jsonObj.put("next", "false");
			}else{
				System.out.println("类不全为空");
				jsonObj.put("next", "true");
				
				jsonObj.put("book", book);
				
			}
			out.println(jsonObj.toString());
			
	}

	
}

	
	
