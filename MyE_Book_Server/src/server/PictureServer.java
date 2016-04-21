package server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/PictureServer")
public class PictureServer extends HttpServlet {

	  /**
     * @see HttpServlet#HttpServlet()
     */
    public PictureServer() {
        super();
        // TODO Auto-generated constructor stub
    }
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {  
        doPost(request, response);  
    }  
      
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {  
    	request.setCharacterEncoding("UTF-8");
        String imagePath = "E:/编程用/电子书阅读器/MyE_Book_Server/WebContent/WEB-INF/img/p1.jpg";           
        FileInputStream fis = new FileInputStream(imagePath);  
        int size =fis.available(); //得到文件大小   
        byte data[]=new byte[size];   
        fis.read(data);  //读数据   
        fis.close();   
        response.setContentType("image/jpg"); //设置返回的文件类型   
        OutputStream os = response.getOutputStream();  
        os.write(data);  
        os.flush();  
        os.close();
    }  
}
