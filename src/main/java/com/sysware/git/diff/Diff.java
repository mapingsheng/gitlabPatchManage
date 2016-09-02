package com.sysware.git.diff;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabCommit;
import org.gitlab.api.models.GitlabCommitBetweenDiff;
import org.gitlab.api.models.GitlabCommitDiff;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabRepositoryFiles;
import org.gitlab.api.models.GitlabUser;

import com.sysware.utils.FilesUtil;

public class Diff {
	
	static GitlabAPI api;
	private static final String TEST_URL = System.getProperty("TEST_URL", "http://192.168.0.200");
	/**
     * 根据登录的用户名和密码连接git仓库服务器
     *
     * @param username 用户名
     * @param password 密码
     */
	public static void connection(String token){
		try {
			api = GitlabAPI.connect(TEST_URL, token);
			api.dispatch().with("login", "huanglm").with("password", "sysware123456").to("session", GitlabUser.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
     * 根据获取GIT服务器中的所有的仓库列表
     *
     *@return List<GitlabProject> 仓库数据列表
     */
	public static List<GitlabProject> getAllProjects(){
		List<GitlabProject> projectList = new ArrayList<GitlabProject>();
		try {
			projectList = api.getAllProjects();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return projectList;
	}
	
	
	/**
     * 根据当前认证的用户信息获取GIT服务器中的该用户相关的的仓库列表
     *
     *@return List<GitlabProject> 仓库数据列表
     */
	public static List<GitlabProject> getProjects(){
		List<GitlabProject> projectList = new ArrayList<GitlabProject>();
		try {
			projectList = api.getProjects();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return projectList;
	}
	
	/**
     * 把字节数组转换成输入流
     *
     *@param  byte[] 字节数组
     *@return InputStream 输入流对象
     */
	private static InputStream byte2Input(byte[] buf){
		return new ByteArrayInputStream(buf);
	}
	
	
	/**
     * 根据两个提交id之间的区间，获取区间中的改动的文件记录
     *
     *@return List<GitlabCommitBetweenDiff> 改动的文件列表
     */
	private static List<GitlabCommitBetweenDiff> getBetweenCommitData(GitlabProject project,String fromCommitHash,String toCommitHash) throws IOException{
		List<GitlabCommitBetweenDiff> list = api.getBetweenCommitStatuses(project, fromCommitHash, toCommitHash);
		return list;
	}
	
	
	/**
     * 从两个版本id之间的总数据集合中获取文件改动数据列表
     *
     *@return List<GitlabCommitBetweenDiff> 改动的文件列表
     */
	public static List<GitlabCommitDiff> getDiffFileDataList(List<GitlabCommitBetweenDiff> list){
		List<GitlabCommitDiff> commitBetweenDiffDataList = new ArrayList<GitlabCommitDiff>();
		
		for(GitlabCommitBetweenDiff commitData : list){
			commitBetweenDiffDataList = commitData.getDiffs();
		}
		
		return commitBetweenDiffDataList;
	}
	
	/**
     * 从两个版本id之间的总数据集合中获取提交的记录列表
     *
     *@param List<GitlabCommitBetweenDiff> 版本区间的数据集合
     *@return List<GitlabCommit> 版本id之间的提交数据列表
     */
	public static List<GitlabCommit> getCommitDataList(List<GitlabCommitBetweenDiff> list){
		List<GitlabCommit> commitList = new ArrayList<GitlabCommit>();
		
		for(GitlabCommitBetweenDiff commitData : list){
			commitList = commitData.getCommits();
		}
		
		return commitList;
	}
	
	
	
	/**
     * 从两次提交版本id中的记录进行比对操作
     *  1、获取此区间的提交记录数据
     *  3、便利每一次提交的文件的变化数据
     *  4、获取变化的文件的内容
     *
     *@author maps
     *@param projectId 单个项目id
     *@param fromCommitHash 提交节点的开始id
     *@param toCommitHash 提交节点的结束id
     *@param deployFilePath 此次补丁包的存放地址
     *@param token 用户通行证
	 *@throws IOException 
     */
	public void doDiffByBetweenCommitDiff(Integer projectId,String fromCommitHash,String toCommitHash,String deployFilePath,String token){
		Diff.connection(token);

		try {
			GitlabProject gitlabProject = api.getProject(projectId);
			List<GitlabCommitBetweenDiff> commitBetweenDiffList =  getBetweenCommitData(gitlabProject,fromCommitHash,toCommitHash);//根据起始提交id，获取需要打补丁的项目综合提交信息
			
			List<GitlabCommitDiff> gitlabCommitDiff = api.getCommitDiffs(projectId, fromCommitHash);//单独获取fromCommitHash提交id对应的变更文件数据列表
			List<GitlabCommitDiff> getDiffFileDataList = getDiffFileDataList(commitBetweenDiffList);//从综合提交信息中获取所有的变更文件的数据列表
			getDiffFileDataList.addAll(gitlabCommitDiff);//把fromCommitHash对应的文件变更列表添加到综合信息想中的所有变更文件数据集合中
			
			GitlabCommitDiff gitlabComDiff = new GitlabCommitDiff();
			gitlabComDiff.setNewPath("pom.xml");
			getDiffFileDataList.add(gitlabComDiff);
			
			
				for(GitlabCommitDiff commitDiff:getDiffFileDataList){
					boolean deleteFlag = commitDiff.getDeletedFile();
					if(!deleteFlag){
						String filePath = commitDiff.getNewPath();
						String fileRealPath = deployFilePath+File.separator+filePath;
						
						GitlabRepositoryFiles gitlabFile = api.getRepositoryFiles(gitlabProject, "release", filePath);
						String base64Content = gitlabFile.getContent();
						byte[] buf;
						Class clazz = null;
						try {
							clazz = Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
						Method mainMethod = null;
						try {
							mainMethod = clazz.getMethod("decode", String.class);
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						} catch (SecurityException e) {
							e.printStackTrace();
						}
						mainMethod.setAccessible(true);
						Object retObj = null;
						try {
							retObj = mainMethod.invoke(null, base64Content);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
						buf = (byte[]) retObj;
						InputStream inputSteam = byte2Input(buf);//转换字节数组为字节输入流
						File file = FilesUtil.initDeployFolder(fileRealPath);
						if(file.exists()){
							FilesUtil.writeFile(file,inputSteam);
						}
					}
				}
			System.out.println("OK");
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
	
	
	public static GitlabProject getProject(String projectId){
		GitlabProject gitLabProject = new GitlabProject();
		try {
			gitLabProject = api.getProject(projectId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return gitLabProject;
	}
	
	
	/**
     * 从两次提交版本id中的记录进行比对操作
     *  1、获取此区间的提交记录数据
     *  3、便利每一次提交的文件的变化数据
     *  4、获取变化的文件的内容
     *
     *@author maps
     *@param projectId 单个项目id
     *@param fromCommitHash 提交节点的开始id
     *@param toCommitHash 提交节点的结束id
     *@param deployFilePath 此次补丁包的存放地址
     *@param token 用户通行证
	 *@throws IOException 
     */
	public void doDiffByCommit(Integer projectId,String fromCommitHash,String toCommitHash,String deployFilePath,String token){
		Diff.connection(token);

		try {
			GitlabProject gitlabProject = api.getProject(projectId);
			List<GitlabCommitBetweenDiff> commitBetweenDiffList =  getBetweenCommitData(gitlabProject,fromCommitHash,toCommitHash);//根据起始提交id，获取需要打补丁的项目综合提交信息
			
			List<GitlabCommitDiff> gitlabCommitDiff = api.getCommitDiffs(projectId, fromCommitHash);
			
			List<GitlabCommit> commitDataList = getCommitDataList(commitBetweenDiffList);//从综合提交信息中获取基本提交信息数据列表
			for(GitlabCommit commit:commitDataList){
				String commitId = commit.getId();//提交id标示
				
				List<GitlabCommitDiff> commidDiffList =  api.getCommitDiffs(projectId,commitId);
				
				for(GitlabCommitDiff commitDiff:commidDiffList){
					boolean deleteFlag = commitDiff.getDeletedFile();
					if(!deleteFlag){
						String filePath = commitDiff.getNewPath();
						String fileRealPath = deployFilePath+File.separator+filePath;
						
						GitlabRepositoryFiles gitlabFile = api.getRepositoryFiles(gitlabProject, "develop", filePath);
						
						//GitlabRepositoryFiles gitlabFile = api.getRepositoryFiles(gitlabProject, commitId, filePath);
						String base64Content = gitlabFile.getContent();
						byte[] buf;
						Class clazz = null;
						try {
							clazz = Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
						Method mainMethod = null;
						try {
							mainMethod = clazz.getMethod("decode", String.class);
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						} catch (SecurityException e) {
							e.printStackTrace();
						}
						mainMethod.setAccessible(true);
						Object retObj = null;
						try {
							retObj = mainMethod.invoke(null, base64Content);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
						buf = (byte[]) retObj;
						InputStream inputSteam = byte2Input(buf);//转换字节数组为字节输入流
						File file = FilesUtil.initDeployFolder(fileRealPath);
						if(file.exists()){
							FilesUtil.writeFile(file,inputSteam);
						}
					}
				}
			}
			System.out.println("OK");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
     * 从两次提交版本id中的记录进行比对操作
     *  1、获取变化的文件记录数据
     *  2、获取此区间的提交记录数据
     *  3、通过1和2遍历出区间的数据的
     *  
     *@param GitlabProject 单个项目对象
     *@param List<GitlabCommitBetweenDiff> 版本区间的数据集合
     *@param List<GitlabCommit> 版本id之间的提交数据列表
	 * @throws IOException 
     */
	public void doDiff(Integer projectId,String fromCommitHash,String toCommitHash,String deployFilePath,String token) throws IOException{
		Diff.connection(token);
		
		List<GitlabProject> allProjectList = getAllProjects();
		for(GitlabProject project :allProjectList){
			if(project.getId()==projectId){
				List<GitlabCommitBetweenDiff> commitBetweenDiffList =  getBetweenCommitData(project,fromCommitHash,toCommitHash);//根据起始提交id，获取需要打补丁的项目综合提交信息
				
				List<GitlabCommit> commitDataList = getCommitDataList(commitBetweenDiffList);//从综合提交信息中获取基本提交信息数据列表
				List<GitlabCommitDiff> diffDataList = getDiffFileDataList(commitBetweenDiffList);//从综合提交信息数据集合中获取所有改动文件的数据数据集合
				
				for(GitlabCommitDiff gitlabFileDiff:diffDataList){
					boolean deleteFlag = gitlabFileDiff.getDeletedFile();//是否是删除状态
					if(!deleteFlag){
						String filePath = gitlabFileDiff.getNewPath();//获取变化的文件地址
						int index = filePath.indexOf("webapp");
						if(index!=-1){
							String fileRealPath = deployFilePath+File.separator+filePath;
							for(GitlabCommit gitLabCommit :commitDataList){
								String commitId = gitLabCommit.getId();//提交id标示
								
								//采用第一种方式
								/*byte[] buf;
								try {
									buf = api.getRawFileContent(project, commitId, filePath);//获取文件的字节数组
									InputStream inputSteam = byte2Input(buf);//转换字节数组为字节输入流
									
									InputStreamReader reader = new InputStreamReader(inputSteam);
									String data = IOUtils.toString(reader);
									if(data!=null && !data.equals("")){
										
									}
									File file = initDeployFolder(fileRealPath);
									if(file.exists()){
										writeFile(file,inputSteam);
									}
								} catch (IOException e) {
									e.printStackTrace();
								}*/
								
								//采用第二种方式
								try{
									GitlabRepositoryFiles gitlabFile = api.getRepositoryFiles(project, commitId, filePath);
									String base64Content = gitlabFile.getContent();
									
									byte[] buf;
									Class clazz = Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
									Method mainMethod = clazz.getMethod("decode", String.class);
									mainMethod.setAccessible(true);
									Object retObj = mainMethod.invoke(null, base64Content);
									buf = (byte[]) retObj;
									InputStream inputSteam = byte2Input(buf);//转换字节数组为字节输入流
									File file = FilesUtil.initDeployFolder(fileRealPath);
									if(file.exists()){
										FilesUtil.writeFile(file,inputSteam);
									}
								}catch(Exception e){
									e.printStackTrace();
									continue;
								}
							}
						}
					}
				}
			}
		}
	}
	
	public static void main(String[] args){
		Diff.connection("u_fMTRL7crLBVuP7Jy83");
		List<GitlabProject> projectList = Diff.getProjects();
		System.out.println();
	}
}
