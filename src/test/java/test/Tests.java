package test;

import java.io.File;
import java.io.IOException;

public class Tests {

	public static void main(String[] args) {
		String dir = "src/main/webapp/doIndex.jsp";
		
		int index = dir.indexOf("webapp");
		System.out.println(dir.substring(index, dir.length()));
		System.out.println(index);
		
		File file = new File("D:/aaa/bbb/t.txt");
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
