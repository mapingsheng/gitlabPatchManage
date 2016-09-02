<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="none" %>
<%@ page isELIgnored="false"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta HTTP-EQUIV="Pragma" content="no-cache">
<title>基础数据库管理系统</title>
		<style type="text/css">
			.clear{height:0; visibility:hidden; clear:both;}
		</style>
	  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	  <jsp:include page="header.jsp"></jsp:include>
	  
	  <script type="text/javascript">
	  	function toPatch(projectId,projectUrl){
	  		var url = "${basePath}/toPatch?projectId="+projectId+"&projectUrl="+projectUrl;
	  		$('#showContent').html('<iframe frameborder="0" src='+url+' height="800px" width="100%"></iframe>');
	  		
	  	}
	  	
	  	function patchRecore(projectid){
	  		var url = "${basePath}/patchList?projectId="+projectid;
	  		$('#showContent').html('<iframe frameborder="0" src='+url+' height="800px" width="100%"></iframe>');
	  	}
	  
	  	$(document).ready(function(){
	  		$('#button').click(function(){
	  			var projectId = $('#projectId').val();
	  			var projectUrl = $('#projectUrl').val();
	  			var havePatch = $('#havePatch').val();
	  			if(havePatch=='false' || (projectId!="" && projectUrl!="")){
	  				$('#basic_validate').submit();
	  			}else{
	  				alert('请选择左侧项目');
	  			}
	  		})
	  	})
	  
	  </script>
	  
</head>
<body>
		<div id="header">
			<h2>打包工具</h2>		
		</div>
		<div id="user-nav" class="navbar navbar-inverse">
        </div>
            
		<div id="sidebar">
			<ul>
				<li class="active"><a href="#"><i class="icon icon-home"></i> <span>项目</span></a></li>
				<c:forEach items="${projectList}" var="project">
					<li class="submenu" id="${project.id}">
						<a href="#"><i class="icon icon-th-list"></i> <span>${project.name}</span> <span class="label"></span></a>
						<ul>
							<li><a style="cursor: pointer;" onclick="toPatch('${project.id}','${project.httpUrl}')">打补丁</a></li>
							<li><a style="cursor: pointer;" onclick="patchRecore('${project.id}','${project.httpUrl}')">补丁记录</a></li>
						</ul>
					</li>	
				</c:forEach>
			</ul>
		</div>
		
		<div id="content">
			<div id="content-header">
				<h1>打包工具</h1>
			</div>
			<div id="breadcrumb"> </div>
			
			<div class="container-fluid" id="showContent">
				<div class="row-fluid" style="margin-top: 800px">
					<div id="footer" class="span12">
						2015 &copy; 补丁管理. By Sysware.FDM Team
					</div>
				</div>
			</div>
		</div>

        <script src="${basePath}/baseframe/bootstrap/js/excanvas.min.js"></script>
        <script src="${basePath}/baseframe/bootstrap/js/jquery.min.js"></script>
        <script src="${basePath}/baseframe/bootstrap/js/jquery.ui.custom.js"></script>
        <script src="${basePath}/baseframe/bootstrap/js/bootstrap.min.js"></script>
       <%--  <script src="${basePath}/baseframe/bootstrap/js/jquery.flot.min.js"></script> --%>
        <%-- <script src="${basePath}/baseframe/bootstrap/js/jquery.flot.resize.min.js"></script> --%>
        <script src="${basePath}/baseframe/bootstrap/js/jquery.peity.min.js"></script>
        <script src="${basePath}/baseframe/bootstrap/js/fullcalendar.min.js"></script>
        <script src="${basePath}/baseframe/bootstrap/js/unicorn.js"></script>
         <script src="${basePath}/baseframe/bootstrap/js/select2.min.js"></script>
        <%-- <script src="${basePath}/baseframe/bootstrap/js/unicorn.dashboard.js"></script> --%>
         <script src="${basePath}/baseframe/bootstrap/js/jquery.validate.js"></script>
        <script src="${basePath}/baseframe/bootstrap/js/unicorn.form_validation.js"></script>
        <script src="${basePath}/baseframe/bootstrap/js/jquery.uniform.js"></script>
        
        <script src="${basePath}/baseframe/bootstrap/js/jquery.dataTables.min.js"></script>
        <script src="${basePath}/baseframe/bootstrap/js/unicorn.tables.js"></script>
	</body>
</html>