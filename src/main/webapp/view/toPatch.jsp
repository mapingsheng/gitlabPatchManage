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
	  	$(document).ready(function(){
	  		$('#button').click(function(){
	  			var projectId = $('#projectId').val();
	  			var projectUrl = $('#projectUrl').val();
	  			var havePatch = $('#havePatch').val();
	  			var startCommitId = $('#startCommitId').val();
	  			var endCommitId = $('#endCommitId').val();
	  			
	  			if(havePatch=='false' || (projectId!="" && projectUrl!="")){
	  				var url = "${basePath}/doPatch";
	  				if(startCommitId==null || startCommitId==""||endCommitId==null ||endCommitId==""){
	  					alert('提交起始ID不能为空');
	  					return false;
	  				}
	  				$.post(url,{projectId:projectId,projectUrl:projectUrl,startCommitId:startCommitId,endCommitId:endCommitId},function(data){
	  					window.parent.patchRecore(projectId);
	  				})
	  			}else{
	  				alert('请选择左侧项目');
	  			}
	  		})
	  	})
	  </script>
</head>
<body>
				<div class="row-fluid" id="toPatchDiv">
					<div class="span12">
						<div class="widget-box">
							<div class="widget-content">
								<div class="row-fluid">
											<div class="container-fluid">
													<div class="row-fluid">
														<div class="span12">
																	<form class="form-horizontal" method="post" action="" name="basic_validate" id="basic_validate" novalidate="novalidate" />
										                                 <div class="control-group">
										                                     <label class="control-label">从</label>
										                                     <div class="controls" style="width: 450px">
										                                         <input type="text" name="startCommitId" id="startCommitId" <c:if test="${patchRecord!=null&&patchRecord.endCommitId!=null}">readonly="readonly"</c:if> value="${patchRecord.endCommitId}" class="required"  placeholder="提交开始id" />
										                                     </div>
										                                     <label class="control-label">到</label>
										                                     <div class="controls" style="width: 450px">
										                                         <input type="text" name="endCommitId" id="endCommitId" value="" class="required"  placeholder="提交结束id" />
										                                     </div>
										                                     <div class="controls" style="width: 450px">
										                                     	<input type="hidden"  name="projectId" id="projectId" value="${patchRecord.projectId}" />
										                                     	<input type="hidden"  name="projectUrl" id="projectUrl" value="${patchRecord.projectUrl}" />
										                                     	<input type="hidden" name="havePatch" id="havePatch"<c:if test="${patchRecord==null||patchRecord.endCommitId==null}"> value="false" </c:if>  >
										                                         <input type="button" id="button" value="打补丁包" class="btn btn-primary" />
										                                     </div>
										                                     <div class="clear"></div>
										                                 </div>
												                   </form>
														</div>
													</div>
												</div>
								</div>							
							</div>
						</div>					
					</div>
				</div>
				
				 <script src="${basePath}/baseframe/bootstrap/js/excanvas.min.js"></script>
		        <script src="${basePath}/baseframe/bootstrap/js/jquery.min.js"></script>
		        <script src="${basePath}/baseframe/bootstrap/js/jquery.ui.custom.js"></script>
		        <script src="${basePath}/baseframe/bootstrap/js/bootstrap.min.js"></script>
		        <script src="${basePath}/baseframe/bootstrap/js/jquery.peity.min.js"></script>
		        <script src="${basePath}/baseframe/bootstrap/js/fullcalendar.min.js"></script>
		        <script src="${basePath}/baseframe/bootstrap/js/unicorn.js"></script>
		        <script src="${basePath}/baseframe/bootstrap/js/select2.min.js"></script>
		        <script src="${basePath}/baseframe/bootstrap/js/jquery.validate.js"></script>
		        <script src="${basePath}/baseframe/bootstrap/js/unicorn.form_validation.js"></script>
		        <script src="${basePath}/baseframe/bootstrap/js/jquery.uniform.js"></script>
	</body>
</html>