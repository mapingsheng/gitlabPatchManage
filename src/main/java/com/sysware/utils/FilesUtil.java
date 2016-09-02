package com.sysware.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

import com.sysware.entity.Files;

public class FilesUtil {
	
	
	
	/**
     * 根据文件的路径新建对应的补丁包文件目录结构，并且把对应的filepath的文件写入到目录中
     *
     *@param filePath 文件地址
     */
	public static void writeFile(File file,InputStream inputSteam){
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		byte[] b = new byte[1024];
		try {
			while(inputSteam.read(b)!=-1){
				fileOut.write(b);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				inputSteam.close();
				fileOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
     * 根据文件的路径新建对应的补丁包文件目录结构，并且把对应的filepath的文件写入到目录中
     *
     *@param filePath 文件地址
     */
	public static void writeFile(String srcFilePath,String targetFilePath){
		File targetFile = new File(targetFilePath);
		File srcFile = new File(srcFilePath);
		InputStream inputSteam = null;
		int byteread = -1;
		
		try {
			if(targetFile.getParentFile().exists()){
				targetFile.createNewFile();
			}else{
				targetFile.getParentFile().mkdirs();
				targetFile.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileOutputStream fileOut = null;
		try {
			inputSteam = new FileInputStream(srcFile);
			fileOut = new FileOutputStream(targetFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		byte[] b = new byte[1024];
		try {
			while((byteread = inputSteam.read(b))!=-1){
				fileOut.write(b,0,byteread);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				inputSteam.close();
				fileOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
     * 针对java文件把对应的java文件编译后复制到对应的WEB-INF下面的class目录中
     *
     *@param filePath 文件地址
     */
	public static String filePathPre(String fileRealPath){
		if(fileRealPath.indexOf("src/main/java")!=-1 || fileRealPath.indexOf("src/main/resources")!=-1){
			fileRealPath = fileRealPath.replace("src/main/java", "src/main/webapp/WEB-INF/classes");
			fileRealPath = fileRealPath.replace("src/main/resources", "src/main/webapp/WEB-INF/classes");
		}
		return fileRealPath;
	}
	
	/**
     * 当发布补丁包时初始化根目录文件
     *
     *@param filePath 文件地址
     */
	public static File initDeployFolder(String fileRealPath){
		File file = new File(fileRealPath);
		if(!file.exists()){
			file.mkdirs();
		}
		return file;
	}
	
	
	
	public static String mkdirFolder(String olderPath,String folderName){
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyMMddHHmmss");
		if(folderName==null || folderName.trim().equals("")){
			folderName = df.format(date);
		}
		
		String newFolderPath = olderPath+File.separator+folderName;
		File floder = new File(newFolderPath);
		try{
			if(!floder.exists() && !floder.isDirectory()){
				floder.mkdir();
			}
		}catch(Exception e){
			e.printStackTrace();
			newFolderPath = "";
		}
		return newFolderPath;
	}
	
	public String  mkdirFolder(String contextPath){
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyMMddHHmmss");
		String folderName = df.format(date);
		
		String newFolderPath = contextPath+File.separator+"patchFiles"+File.separator+folderName;
		File floder = new File(newFolderPath);
		try{
			if(!floder.exists() && !floder.isDirectory()){
				floder.mkdirs();
			}
		}catch(Exception e){
			e.printStackTrace();
			newFolderPath = "";
		}
		return newFolderPath;
	}
	

	/**
	 * 解压缩文件
	 * 
	 * @param sourceZip 目标文件
	 * @param targeFile  解压的目录
	 */
	public static void unZip(String sourceZip, String targeFile){
		File targetFile = new File(targeFile);
		File sourceFile = new File(sourceZip);
		if(!sourceFile.exists()){
			return;
		}
		
		if(!targetFile.exists()){
			Project prj = new Project();
			Expand ex = new Expand();
			ex.setProject(prj);
			ex.setDest(targetFile);
			ex.setOverwrite(false);
			ex.setEncoding("gbk");
			ex.setSrc(sourceFile);
			ex.execute();
		}
		
		sourceFile.deleteOnExit();
	}
	
	/**
	 * 压缩文件
	 * 
	 * @param sourceZip 目标文件
	 * @param targeFile  解压的目录
	 */
	public static String zip(String sourceZip, String targeFile){
		String targetFilePath = targeFile+".zip";
		File sourceFile = new File(sourceZip);
		File targetFile = new File(targetFilePath);
		
		if(!sourceFile.exists()){
			throw new RuntimeException("source file or directory "+sourceZip+" does not exist.");
		}
		
		try{
			Project prj = new Project();
			FileSet fileSet = new FileSet();
			fileSet.setProject(prj);
			//判断是目录还是文件
			if(sourceFile.isDirectory()){
				fileSet.setDir(sourceFile);
			}else{
				fileSet.setFile(sourceFile);
			}
			
			Zip zip = new Zip();
			zip.setProject(prj);
			zip.setDestFile(targetFile);
			zip.addFileset(fileSet);
			zip.setEncoding("gbk");
			zip.execute();
		}catch(Exception e){
			targetFilePath = "";
			e.printStackTrace();
		}
		
		return targetFilePath;
	}
	
	
	/**
	 * 获取制定路径下面的文件列表
	 * 
	 * @param sourceZip   目标文件
	 * @param List<File>  文件集合
	 */
	public static List<File> getFileListFromFolder(String folderPath){
		List<File> fileList = new ArrayList<File>();
		
		File file = new File(folderPath);
		File[] fileArry = file.listFiles();
		for(int i=0;i<fileArry.length;i++){
			fileList.add(fileArry[i]);
		}
		
		return fileList;
	}
	
	/**
	 * 获取制定路径下面的自定义文件实体数据列表
	 * 
	 * @param sourceZip   目标文件
	 * @param List<Files>  文件集合
	 */
	public static List<Files> getFileListFromFolder(String folderPath,String projectName,List<Files> filesList){
		File file = new File(folderPath);
		File[] fileArry = file.listFiles();
		for(int i=0;i<fileArry.length;i++){
			File currFile =fileArry[i];
			if(currFile.isDirectory()){
				getFileListFromFolder(currFile.getAbsolutePath(),projectName,filesList);
			}else{
				Files files = new Files();
				String filePath = currFile.getPath();
				int index = filePath.indexOf(projectName)+projectName.length();
				String fileEndPathStr = filePath.substring(index, filePath.length());
				
				files.setName(currFile.getName());
				files.setPath(currFile.getPath());
				files.setEndPath(fileEndPathStr);
				
				filesList.add(files);
			}
		}
		return filesList;
	}
	
	
	
}
