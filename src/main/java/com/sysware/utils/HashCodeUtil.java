package com.sysware.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

import com.sysware.entity.Files;

public class HashCodeUtil {

	
	
	
	 /**
     * 获取指定路径文件对应的code值
     * @param filePath 文件的绝对路径地址
     * @return String 文件对应的code值
     */
	public static String getHashCode(String filePath){
		File file = new File(filePath);
		InputStream fin = null;
		try {
			fin = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		CheckedInputStream checkedInputStream = null;
		String crc32Code=null;
		byte[] b=new byte[4096];
	    checkedInputStream = new CheckedInputStream(fin, new CRC32());
	    try {
			while (checkedInputStream.read(b)>=0){
			}
			
			long crc32Code_long = checkedInputStream.getChecksum().getValue();
		    crc32Code = Long.toHexString(crc32Code_long);//获取文件crc32code的十六进制代码
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				fin.close();
				checkedInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		    return crc32Code;
	}
	
	 /**
     * 获取指定文件集合中每一个文件对应的code值
     * @param fileList 文件集合
     * @return Map 文件对应的code值集合
     * 
     */
	public static Map<String,String> getHashCode(List<Files> fileList){
		Map<String,String> fileCodeMap = new HashMap<String, String>();
		for(Files files:fileList){
			String filePath = files.getPath();
			String endPath = files.getEndPath();
			
			String code = getHashCode(filePath);
			fileCodeMap.put(endPath, code);
		} 
		return fileCodeMap;
	}
	
	
	public static Map<String,String> getHashCodeFormatPath(List<File> fileList,String projectName){
		Map<String,String> fileCodeMap = new HashMap<String, String>();
		for(File file:fileList){
			if(!file.isDirectory()){
				String filePath = file.getPath();
				int index = filePath.indexOf(projectName)+projectName.length();
				String fileEndPathStr = filePath.substring(index, filePath.length());
				
				String code = getHashCode(filePath);
				fileCodeMap.put(fileEndPathStr, code);
			}
		} 
		return fileCodeMap;
	}
	
	
	
	
	
	public static void main(String[] args) {
		String ss = getHashCode("D:/patch/a1ab4f5c235fe8451a3644c1c3e42aefb3e3a649/target/sysware-galaxy/apps/flow/_static/css/grapheditor.css");
		String st = getHashCode("D:/patch/b0b4a7338a9368e4350cff48a3611f2be0816d61/target/sysware-galaxy/apps/flow/_static/css/grapheditor.css");
		System.out.println(ss+"   "+st);
	}

}
