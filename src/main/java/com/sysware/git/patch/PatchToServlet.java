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

import net.sf.json.JSONObject;

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
@WebServlet("/toPatch")
public class PatchToServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public PatchToServlet() {
        super();
    }
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String projectId = request.getParameter("projectId");
		String projectUrl = request.getParameter("projectUrl");
		
		if(SyswareUtil.isEmpty(projectId) || SyswareUtil.isEmpty(projectUrl)){
			return;
		}
		
		PatchRecord patchRecord = new PatchRecord();
		patchRecord.setProjectId(projectId);
		
		DBUtil dbUtil = new DBUtil();
		List<PatchRecord> patchRecordList = dbUtil.getPatchRecordList(patchRecord);
		if(patchRecordList.size()!=0){
			patchRecord = patchRecordList.get(0);
			/*JSONObject jsobj = JSONObject.fromObject(patchRecordTemp);
			response.getWriter().print(jsobj.toString());*/
		}else{
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			Diff.connection(user.getToken());
			GitlabProject gitLabProject = Diff.getProject(projectId);
			patchRecord.setProjectUrl(gitLabProject.getHttpUrl());
		}
		
		request.setAttribute("patchRecord", patchRecord);
		request.getRequestDispatcher("view/toPatch.jsp").forward(request, response);
	}

}
