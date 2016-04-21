package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;





import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import VO.Book;
import DAO.BookDao;

/**
 * Servlet implementation class GetimgServer
 */
@WebServlet("/GetimgServer")
public class GetimgServer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	BookDao bookDao=new BookDao();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetimgServer() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");

		String showType = request.getParameter("showType");
		System.out.println(showType);
		
		switch (Integer.parseInt(showType)) {
		case 0:
			getHotBookImg(request,response);
			break;
		case 1:
			getUpdateBook(request,response);
			break;
		case 2:
			getFinishBook(request,response);
			break;

		default:
			break;
		}
	}

	private void getFinishBook(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		List<Book> books = new ArrayList<Book>();

		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		boolean isEmpty = true;
		try {
			out = response.getWriter();
			books = bookDao.getFinishbooks();
			isEmpty = books.isEmpty();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JSONObject jsonObj = new JSONObject();
		if (isEmpty) {
			System.out.println("完结书籍为空！");
			jsonObj.put("next", "false");
		} else {
			System.out.println("完结书籍数量为" + books.toArray().length);
			jsonObj.put("next", "true");
			jsonObj.put("books", books);
		}
		out.println(jsonObj.toString());
	}


	/**
	 * 获取最近更新书籍信息
	 * @param request
	 * @param response
	 */
	private void getUpdateBook(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		List<Book> books = new ArrayList<Book>();

		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		boolean isEmpty = true;
		try {
			out = response.getWriter();
			books = bookDao.getUpdatebooks();
			isEmpty = books.isEmpty();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JSONObject jsonObj = new JSONObject();
		if (isEmpty) {
			System.out.println("最近更新书籍为空！");

			jsonObj.put("next", "false");
		} else {
			System.out.println("最近更新书籍数量为" + books.toArray().length);
			jsonObj.put("next", "true");
			jsonObj.put("books", books);
		}
		out.println(jsonObj.toString());
	}
	

/**
 * 获取热门书籍的id，name，和图片url
 * @param request
 * @param response
 */
	private void getHotBookImg(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		List<Book> books=new ArrayList<Book>();
		
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		boolean isEmpty=true;
		try {
			out = response.getWriter();
			books=bookDao.getHotbooks();
			isEmpty=books.isEmpty();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		JSONObject jsonObj = new JSONObject();
		if(isEmpty){
			System.out.println("热门书籍为空！");
			
			jsonObj.put("next", "false");
		}else{
			System.out.println("热门书籍数量为"+books.toArray().length);
			jsonObj.put("next", "true");
			jsonObj.put("books", books);
		}
		out.println(jsonObj.toString());
	}

}
