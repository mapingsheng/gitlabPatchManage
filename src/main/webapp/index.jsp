<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<%
	    String path = request.getContextPath();
	    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
	    request.setAttribute("basePath",basePath);
	    
	    response.setHeader("Pragrma", "no-cache");
	    response.setHeader("Cache-Control", "no-store");
	    response.setDateHeader("Expires", 0);
	%>
</head>
<body>
	<%response.sendRedirect("LoginAction"); %>
</body>
</html>