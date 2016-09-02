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
	  
	  <link rel="stylesheet" href="${basePath}/baseframe/bootstrap/css/uniform.css" />
	  <link rel="stylesheet" href="${basePath}/baseframe/bootstrap/css/select2.css" />
	  
	  <script type="text/javascript">
	  		function downPatchFile(pId){
	  			var url ="${basePath}"+"/patchDownload?pId="+pId;
	  			window.location.href=url;
	  		}
	  </script>
	  	
</head>
<body>
				<div class="row-fluid">
					<div class="span12">
						<div class="widget-box">
							<div class="widget-title">
								<span class="icon">
									<i class="icon-th"></i>
								</span>
								<h5>补丁记录</h5>
							</div>
							<div class="widget-content nopadding">
								<table class="table table-bordered table-striped">
									<thead>
										<tr>
											<th>起始提交ID</th>
											<th>结束提交ID</th>
											<th>操作人</th>
											<th>时间</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${patchRecordList}" var="project">
											<tr>
												<td>${project.startCommitId}</td>
												<td>${project.endCommitId}</td>
												<td>${project.patchUserName}</td>
												<td>${project.patchTime}</td>
												<td><button class="btn tip-top" onclick="downPatchFile('${project.pid}')" data-original-title="Tooltip in top">下载</button></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>							
							</div>
						</div>
					</div>
				</div>
	</body>
</html>