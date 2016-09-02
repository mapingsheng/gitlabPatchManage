package com.sysware.git.patch;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.gitlab.api.models.GitlabProject;

import com.sysware.db.conn.DBUtil;
import com.sysware.entity.Files;
import com.sysware.entity.PatchRecord;
import com.sysware.entity.User;
import com.sysware.git.diff.Diff;
import com.sysware.mvn.compile.CompileFile;
import com.sysware.utils.FilesUtil;
import com.sysware.utils.GitUtils;
import com.sysware.utils.HashCodeUtil;
import com.sysware.utils.ReadXmlUtil;
import com.sysware.utils.SyswareUtil;

/**
 * 打包请求业务处理servlet
 * 1、在patchFiles中新建此次打包的文件夹
 * 2、
 * 
 */
@WebServlet("/toPatchManage")
public class PatchManageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public PatchManageServlet() {
        super();
    }
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(SyswareUtil.isEmpty(user)){
			request.getRequestDispatcher("view/login.jsp").forward(request, response);
			return;
		}
		
		Diff.connection(user.getToken());
		List<GitlabProject> projectList = Diff.getProjects();
		request.setAttribute("projectList", projectList);
		request.getRequestDispatcher("view/manage.jsp").forward(request, response);
	}
}
