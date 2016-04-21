<%@ page language="java" import="java.util.*"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%  
String path = request.getContextPath();  
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
%>  
<html>
<head>  
    <base href="<%=basePath%>">  
    <title>图片显示</title>  
  </head>  
  <body>  
    <img src="<%=basePath %>PictureServer">  
  </body>  
</html>