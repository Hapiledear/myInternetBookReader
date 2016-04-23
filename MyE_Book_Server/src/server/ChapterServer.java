package server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

import DAO.ChapterDao;
import VO.Chapter;

/**
 * Servlet implementation class ChapterServer
 */
@WebServlet("/ChapterServer")
public class ChapterServer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	ChapterDao chapterDao=new ChapterDao();
	String TAG="ChapterServer";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChapterServer() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");

		String flag = request.getParameter("flag");

		System.out.println("flag:" + flag);
		switch (Integer.parseInt(flag)) {
		case 1:
		  by_id(request, response);
			break;
		}
	}

	private void by_id(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String chapter_id=request.getParameter("chapter_id");
		
		System.out.println(TAG+" chapter_id="+chapter_id);
		Chapter chapter=new Chapter();
		try {
			chapter=chapterDao.findChapterByID(Integer.parseInt(chapter_id));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response.setCharacterEncoding("UTF-8");
    	response.setContentType("text/html;charset=utf-8");
		PrintWriter out = null;
		boolean isEmpty=true;
		try {
			out = response.getWriter();
		    isEmpty = chapter.getUrl().isEmpty();
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
			
			jsonObj.put("chapter", chapter);
			
		}
		out.println(jsonObj.toString());
		
		
	}

}
