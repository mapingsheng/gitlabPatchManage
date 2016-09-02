package com.sysware.utils;

import java.io.File;
import java.util.Arrays;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

public class CompileUtilTest {
	
	public static void main(String[] args){
		CompileUtilTest.compile();
	}
	
	public static void compile(){
		Process process = null;
		Runtime runtime = Runtime.getRuntime();
		
		String str;
		String strings =null;
		int num=0;
		
		//直接运行pom文件执行编译操作
		InvocationRequest request = new DefaultInvocationRequest();
		request.setPomFile(new File("D:/sysware/SYSWARE.PATCHMANAGE/target/syswareGitManage/patchFiles/20151208142108/pom.xml"));
		request.setGoals(Arrays.asList("clean","compile","install","package"));
		//File file = new File("D:/sysware/SYSWARE.PATCHMANAGE/target/syswareGitManage/patchFiles/20151208142108");
		//request.setBaseDirectory(file);
		
		
		Invoker invoker = new DefaultInvoker();
		try {
			invoker.execute(request);
		} catch (MavenInvocationException e1) {
			e1.printStackTrace();
		}
		
		/*try {
			
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			compiler.run(null, null, null, fileRealPath);
			
			process = runtime.exec("javac "+fileRealPath);
			InputStream inputStream = process.getInputStream();
			fileRealPath = fileRealPath.replace(".java", ".class");
			File file = FilesUtil.initDeployFolder(fileRealPath);
			if(file.exists()){
				FilesUtil.writeFile(file,inputStream);
			}
			
			BufferedReader buff = new BufferedReader(new InputStreamReader(inputStream));
			while((str = buff.readLine())!=null){
				strings +=str;
			}
			System.out.println(strings);
			
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
}
