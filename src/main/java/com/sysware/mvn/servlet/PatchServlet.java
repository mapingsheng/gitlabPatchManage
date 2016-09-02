package com.sysware.mvn.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sysware.git.diff.Diff;
import com.sysware.utils.FilesUtil;

/**
 * 打包请求业务处理servlet
 * 1、在patchFiles中新建此次打包的文件夹
 * 2、
 * 
 */
@WebServlet("/mvnPatchServlet")
public class PatchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public PatchServlet() {
        super();
    }
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextPath = request.getServletContext().getRealPath("/");//获取当前系统的上下文 \webapp
		FilesUtil fileUtil = new FilesUtil();
		String folderPath = fileUtil.mkdirFolder(contextPath);
		
		String projectId = request.getParameter("projectId");
		String startCommitId = request.getParameter("startCommitId");
		String endCommitId = request.getParameter("endCommitId");
		String token = "Vi_5sGK2kssPnVgEjcjs";
		
		String fromCommitHash = "13f643ae3c54050df7c6ca40bd740bc1fe276bdf";
		String toCommitHash="8dc3fa946414341d46b5dc8d7cb17fdb45766ef8";
		
		Diff diff = new Diff();
		diff.doDiffByCommit(40, fromCommitHash, toCommitHash, folderPath,token);
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

}
