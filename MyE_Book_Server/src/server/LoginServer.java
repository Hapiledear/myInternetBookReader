package server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import VO.User;

import com.alibaba.fastjson.JSONObject;


import DAO.UserDao;

/**
 * Servlet implementation class LoginServer
 */
@WebServlet("/LoginServer")
public class LoginServer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	UserDao userDao = new UserDao();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServer() {
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

		String flag = request.getParameter("flag");

		System.out.println("flag:" + flag);
		switch (Integer.parseInt(flag)) {
		case 1:
			login(request, response);// 登陆操作
			break;

		case 2:
			register(request, response);// 注册操作
			break;
		case 3:
			active(request,response);//激活操作
			break;

		}
	}
	private void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		// TODO Auto-generated method stub
		String name=request.getParameter("name");
		String acidCode=request.getParameter("acidCode");
		System.out.println("name:" + name);
		System.out.println("code:" + acidCode);
		
		response.setCharacterEncoding("UTF-8");
    	response.setContentType("text/html;charset=utf-8");
    	
		PrintWriter out;
		out = response.getWriter();
		try {
			if(userDao.updateState(name,acidCode)){
				out.println("SUCCESS!");
			}else{
				out.print("FAILD!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void register(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		String type = request.getParameter("type");
		System.out.println("type:" + type);
		switch (Integer.parseInt(type)) {
		case 1:
			normalRegister(request,response);//普通的注册
			break;
		case 2:
		//	emailRegister(request,response);//按邮箱注册
			break;

		default:
			break;
		}
		
	}

/*	private void emailRegister(HttpServletRequest request,
			HttpServletResponse response)throws ServletException, IOException {
		// TODO Auto-generated method stub
		String Name = request.getParameter("Name");
		String Pass = request.getParameter("Pass");
		System.out.println("name:" + Name);
		System.out.println("pass:" + Pass);
		response.setCharacterEncoding("UTF-8");
		PrintWriter out;
		out = response.getWriter();
		if (userDao.hasUser(Name)) {
			jsonObj.addProperty("register", "hasuser");
		} else{
			try {
				if (userDao.mailregister(Name, Pass)) {
					jsonObj.addProperty("register", "success");
				} else {
					jsonObj.addProperty("register", "faild");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		out.println(jsonObj);
	}
*/
	private void normalRegister(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		// TODO Auto-generated method stub
		String Name = request.getParameter("Name");
		String Pass = request.getParameter("Pass");
		System.out.println("name:" + Name);
		System.out.println("pass:" + Pass);
		
		JSONObject jsonObj = new JSONObject();
		response.setCharacterEncoding("UTF-8");
    	response.setContentType("text/html;charset=utf-8");
    	
		PrintWriter out;
		out = response.getWriter();
		if (userDao.hasUser(Name)) {
			jsonObj.put("register", "hasuser");
		} else {
			try {
				if (userDao.register(Name, Pass)) {
					jsonObj.put("register", "success");
				} else {
					jsonObj.put("register", "faild");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		out.println(jsonObj);
	}

	private void login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String Name = request.getParameter("Name");
		String Pass = request.getParameter("Pass");
		System.out.println("name:" + Name);
		System.out.println("pass:" + Pass);
		User user = null;
		try {
			user = userDao.login(Name, Pass);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JSONObject jsonObj = new JSONObject();
		response.setCharacterEncoding("UTF-8");
    	response.setContentType("text/html;charset=utf-8");
    	
		PrintWriter out;
		out = response.getWriter();

		if (user.getUserid()>0) {
			JSONObject userJson = new JSONObject();
			if(user.getState()==0){
				
				jsonObj.put("login", "nostate");
				userJson.put("id", user.getUserid());
				jsonObj.put("user", userJson);
				out.println(jsonObj.toString());
			}else{
			jsonObj.put("login", "success");

			userJson.put("id", user.getUserid());
			userJson.put("name", user.getName());
			jsonObj.put("user", userJson);
			out.println(jsonObj.toString());
			}
		} else {
			JSONObject userJson = new JSONObject();
			jsonObj.put("login", "faild");
			userJson.put("id", user.getUserid());
			jsonObj.put("user", userJson);
			out.println(jsonObj.toString());
		}
	}


}
