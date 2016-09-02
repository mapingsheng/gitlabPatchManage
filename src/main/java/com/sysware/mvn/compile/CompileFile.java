package com.sysware.mvn.compile;

import java.io.File;
import java.util.Arrays;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

public class CompileFile {
	
	
	/**
	 * 根据pom.xml文件地址进行编译、打包操作
	 * @param pomFilePath pom.xml文件的绝对地址
	 * 
	 */
	public static void compileFileByPom(String pomFilePath){
			InvocationRequest request = new DefaultInvocationRequest();
			request.setPomFile(new File(pomFilePath));
			request.setGoals(Arrays.asList("clean","compile","install","package"));
			Invoker invoker = new DefaultInvoker();
			try {
				invoker.execute(request);
				System.out.println("进入到编译方法中，执行编译操作！");
			} catch (MavenInvocationException e1) {
				e1.printStackTrace();
			}
	}
	
	
	
}
