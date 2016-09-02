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
@WebServlet("/doPatch")
public class PatchDoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public PatchDoServlet() {
        super();
    }
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getContextPath();
	    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
	    
		String contextPath = request.getServletContext().getRealPath("/");//获取当前系统的上下文 \webapp
		FilesUtil fileUtil = new FilesUtil();
		String folderPath = fileUtil.mkdirFolder(contextPath);
		String separator = File.separator;
		
		String projectId = request.getParameter("projectId");
		String projectUrl = request.getParameter("projectUrl");
		String fromCommitHash = request.getParameter("startCommitId");
		String toCommitHash = request.getParameter("endCommitId");
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(SyswareUtil.isEmpty(user)){
			request.getRequestDispatcher("view/login.jsp").forward(request, response);
			return;
		}
		
		String userName = user.getUserName();
		String password = user.getPassword();
		
		//获取打补丁包的存放地址
		String fetch = GitUtils.fetch;
		
		String patchFileName = fromCommitHash.substring(0, 7)+"-"+toCommitHash.substring(0, 7); 
		String patchFilePath = fetch+separator+patchFileName;
		FilesUtil.initDeployFolder(patchFilePath);
		
		try{
			//克隆操作,如果已经克隆过则不进行克隆，只会克隆一次
			GitUtils.clone(projectUrl, userName, password);
			
			//拉取操作
			GitUtils.pull(projectUrl, userName, password);
		}catch(Exception e){
			e.printStackTrace();
			return;
		}
		
		//分别把开始和结束节点对应的源代码进行压缩zip包处理
		String startPatchPath = GitUtils.fetchFilesByRepositoryId(fromCommitHash);
		String endPatchPath = GitUtils.fetchFilesByRepositoryId(toCommitHash);
		
		//解压缩源代码包
		String startUnzipFloder = fetch+separator+fromCommitHash;
		String endUnzipFloder = fetch+separator+toCommitHash;
		FilesUtil.unZip(startPatchPath, startUnzipFloder);
		FilesUtil.unZip(endPatchPath, endUnzipFloder);
		
		//根据源代码包中的pom.xml文件进行编译打包处理
		String startPomFilePath = startUnzipFloder+separator+"pom.xml";
		String endPomFilePath = endUnzipFloder+separator+"pom.xml";
		String startCompileFilePath = startUnzipFloder+separator+"target";
		System.out.println("编译处理："+startPomFilePath+"   "+endPomFilePath+"  "+startCompileFilePath);
		File startCompileFile = new File(startCompileFilePath);
		if(!startCompileFile.exists()){
			CompileFile.compileFileByPom(startPomFilePath);
		}
		CompileFile.compileFileByPom(endPomFilePath);
		
		//根据pom.xml获取编译后的源代码的target/下面的项目名称对应的目录路径
		InputStream inputStream = new FileInputStream(new File(startPomFilePath));
		String[] keyNameArry = {"groupId","artifactId"};
		String[] valueNameArry = ReadXmlUtil.getXmlFilePathNodeTextByKey(inputStream, keyNameArry);
		String projectName = ReadXmlUtil.getValueFormat(valueNameArry, "-");
		
		//分别获取start和end对应的项目目录下面的自定义实体的目录
		System.out.println("分别获取start和end对应的项目目录下面的自定义实体的目录");
		String compileProjectFolderStart = startUnzipFloder+separator+"target"+separator+projectName;
		String compileProjectFolderEnd = endUnzipFloder+separator+"target"+separator+projectName;
		List<Files> startFileList = new ArrayList<Files>(); 
		FilesUtil.getFileListFromFolder(compileProjectFolderStart,projectName,startFileList);
		List<Files> endFileList = new ArrayList<Files>();
		FilesUtil.getFileListFromFolder(compileProjectFolderEnd,projectName,endFileList);
		
		Map<String, String> startFileCodeMap = HashCodeUtil.getHashCode(startFileList);
		Map<String, String> endFileCodeMap = HashCodeUtil.getHashCode(endFileList);
		
		List<Files> endFileListTemp = new ArrayList<Files>();
		endFileListTemp.addAll(endFileList);
		
		List<Files> updateFileList = new ArrayList<Files>();
		endFileList.retainAll(startFileList);//读取两个目录文件下面的文件交集
		updateFileList.addAll(endFileList);//存放需要比对code的文件实体数据集合
		
		List<Files> addFileList = new ArrayList<Files>();
		endFileListTemp.removeAll(startFileList);//存放新增的文件实体数据集合
		addFileList.addAll(endFileListTemp);
		
		for(Files files:addFileList){
			String endPath = files.getEndPath();
			String srcFileRealPath = compileProjectFolderEnd+endPath;//新增的源文件的绝对路径
			String targetFileRealPath = patchFilePath+endPath;//需要写入的补丁目录路径
			FilesUtil.writeFile(srcFileRealPath, targetFileRealPath);
		}
		
		for(Files files:updateFileList){
			String endPath = files.getEndPath();
			String endFileCode = endFileCodeMap.get(endPath);
			String startFileCode = startFileCodeMap.get(endPath);
			if(!endFileCode.equals(startFileCode)){//如果两个文件的code一样，则忽略，不一样则说明修改过
				String srcFileRealPath = compileProjectFolderEnd+endPath;//改动的源文件的绝对路径
				String targetFileRealPath = patchFilePath+endPath;//需要写入的补丁目录路径
				FilesUtil.writeFile(srcFileRealPath, targetFileRealPath);
			}
		}
		
		String patchZipFile = FilesUtil.zip(patchFilePath, patchFilePath);
		
		//删除startCommit对应的编译后的文件目录
		File startUPnZipFile = new File(startUnzipFloder);
		startUPnZipFile.deleteOnExit();
		
		PatchRecord patchRecord = new PatchRecord();
		patchRecord.setProjectId(projectId);
		patchRecord.setStartCommitId(fromCommitHash);
		patchRecord.setEndCommitId(toCommitHash);
		patchRecord.setPatchUserId(user.getPid());
		patchRecord.setProjectUrl(projectUrl);
		patchRecord.setPatchFileUrl(patchZipFile);
		DBUtil dbUtil = new DBUtil();
		dbUtil.addPatchRecord(patchRecord);
		
		
		response.getWriter().print("success");
		//Diff.connection(user.getToken());
		//List<GitlabProject> projectList = Diff.getProjects();
		//request.setAttribute("projectList", projectList);
		//request.getRequestDispatcher(basePath+"/toPatchManage").forward(request, response);
	}
}
