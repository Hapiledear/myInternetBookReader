package Fliter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


public class SetCharacterEncodingFilter implements Filter {

	 private FilterConfig filterConfig; 
	   private String encoding=null; 
	   //Handle the passed-in FilterConfig 
	   public void init(FilterConfig filterConfig){ 
	      this.filterConfig=filterConfig; 
	      encoding=filterConfig.getInitParameter("encoding"); 
	     
	   } 
	   //Process the request/response pair 
	   public void doFilter(ServletRequest request,ServletResponse response,FilterChain filterChain){
		   System.out.println("filter start ...");
	      try{ 
	    		 System.out.println(encoding);
	         request.setCharacterEncoding(encoding); 
	         filterChain.doFilter(request,response); 
	      } catch(ServletException sx){ 
	         filterConfig.getServletContext().log(sx.getMessage()); 
	      } catch(IOException iox){ 
	         filterConfig.getServletContext().log(iox.getMessage()); 
	      } 
	   } 
	   //Clean up resources 
	   public void destroy(){ 
	   } 
}
