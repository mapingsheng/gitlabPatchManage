package com.sysware.git.patch;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
@WebServlet("/patchDownload")
public class PatchDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public PatchDownloadServlet() {
        super();
    }
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pId =request.getParameter("pId");
		
		if(SyswareUtil.isEmpty(pId) || SyswareUtil.isEmpty(pId)){
			return;
		}
		
		PatchRecord patchRecord = new PatchRecord();
		patchRecord.setPid(pId);
		
		DBUtil dbUtil = new DBUtil();
		List<PatchRecord> patchRecordList = dbUtil.getPatchRecordList(patchRecord);
		if(patchRecordList.size()!=0){
			patchRecord = patchRecordList.get(0);
			String patchFileName = patchRecord.getStartCommitId().substring(0, 7)+"-"+patchRecord.getEndCommitId().substring(0, 7); 
			
			String patchFilePath = patchRecord.getPatchFileUrl();
			File file = new File(patchFilePath);
			String fileName = patchFileName+".zip";
			InputStream ins = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[ins.available()];
			ins.read(buffer);
			ins.close();
			response.reset();
			
			response.addHeader("Content-Disposition","attachment;filename="+fileName);
			response.addHeader("Content-Length", (new Long(file.length())).toString());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		}
	}

}
