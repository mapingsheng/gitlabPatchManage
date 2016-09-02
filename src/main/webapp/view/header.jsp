<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="none" %>
<%@ page isELIgnored="false"%>
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

	<script type="text/javascript">
		var basePath = "${basePath}";
	</script>

	  <script type="text/javascript" src="${basePath}/baseframe/js/jquery-1.11.1.min.js"></script>
      <meta name="viewport" content="width=device-width, initial-scale=1.0" />
	  <link rel="stylesheet" href="${basePath}/baseframe/bootstrap/css/bootstrap.min.css" />
	  <link rel="stylesheet" href="${basePath}/baseframe/bootstrap/css/bootstrap-responsive.min.css" />
	  <link rel="stylesheet" href="${basePath}/baseframe/bootstrap/css/fullcalendar.css" />	
	  <link rel="stylesheet" href="${basePath}/baseframe/bootstrap/css/unicorn.main.css" />
	  <link rel="stylesheet" href="${basePath}/baseframe/bootstrap/css/unicorn.grey.css" class="skin-color" />
</head>
