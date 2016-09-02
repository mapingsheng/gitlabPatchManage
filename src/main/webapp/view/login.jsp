<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta HTTP-EQUIV="Pragma" content="no-cache">
<title>基础数据库管理系统</title>
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
		<style type="text/css">
			.clear{height:0; visibility:hidden; clear:both;}
		</style>
      <script type="text/javascript" src="${basePath}/baseframe/js/jquery-1.11.1.min.js"></script>
      <meta name="viewport" content="width=device-width, initial-scale=1.0" />
	  <link rel="stylesheet" href="${basePath}/baseframe/bootstrap/css/bootstrap.min.css" />
	  <link rel="stylesheet" href="${basePath}/baseframe/bootstrap/css/bootstrap-responsive.min.css" />
	  <link rel="stylesheet" href="${basePath}/baseframe/bootstrap/css/fullcalendar.css" />	
	   <link rel="stylesheet" href="${basePath}/baseframe/bootstrap/css/unicorn.login.css" />
	  
	  
	  <link rel="stylesheet" href="${basePath}/baseframe/bootstrap/css/unicorn.main.css" />
	  <link rel="stylesheet" href="${basePath}/baseframe/bootstrap/css/unicorn.grey.css" class="skin-color" />
	  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>

<body>
		<div id="logo">
            <img src="${basePath}/baseframe/bootstrap/img/logo.png" alt="" />
        </div>
        <div id="loginbox">            
            <form id="loginform" method="post" class="form-vertical" action="${basePath}/LoginAction" />
				<p></p>
                <div class="control-group">
                    <div class="controls">
                        <div class="input-prepend">
                            <span class="add-on"><i class="icon-user"></i></span><input type="text" name="username" placeholder="用户名" />
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <div class="controls">
                        <div class="input-prepend">
                            <span class="add-on"><i class="icon-lock"></i></span><input type="password" name="password" placeholder="密码" />
                        </div>
                    </div>
                </div>
                <div class="form-actions">
                    <span class="pull-left"><a href="#" class="flip-link" id="to-recover"></a></span>
                    <span class="pull-right"><input type="submit" class="btn btn-inverse" value="Login" /></span>
                </div>
            </form>
        </div>
        
        <script src="${basePath}/baseframe/bootstrap/js/jquery.min.js"></script>  
        <script src="${basePath}/baseframe/bootstrap/js/unicorn.login.js"></script> 
	</body>
</html>