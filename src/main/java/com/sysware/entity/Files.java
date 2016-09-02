package com.sysware.entity;
public class Files {
	
	private String name;
	private String path;
	private String endPath;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getEndPath() {
		return endPath;
	}

	public void setEndPath(String endPath) {
		this.endPath = endPath;
	}

	public boolean equals(Object stu){
		Files stus = (Files) stu;
		if(this.endPath.equals(stus.getEndPath())){
			return true;
		}else{
			return false;
		}
	}
}
