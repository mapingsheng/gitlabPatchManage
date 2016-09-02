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

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;

import com.sysware.db.conn.DBUtil;
import com.sysware.entity.Files;
import com.sysware.entity.PatchRecord;
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
@WebServlet("/patchList")
public class PatchListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public PatchListServlet() {
        super();
    }
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String projectId = request.getParameter("projectId");
		
		if(SyswareUtil.isEmpty(projectId)){
			return;
		}
		
		PatchRecord patchRecord = new PatchRecord();
		patchRecord.setProjectId(projectId);
		
		DBUtil dbUtil = new DBUtil();
		List<PatchRecord> patchRecordList = dbUtil.getPatchRecordList(patchRecord);
		
		request.setAttribute("patchRecordList", patchRecordList);
		request.getRequestDispatcher("view/patchList.jsp").forward(request, response);
	}

}
