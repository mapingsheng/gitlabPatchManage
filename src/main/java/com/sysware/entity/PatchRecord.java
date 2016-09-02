package com.sysware.entity;

/**
 * 打补丁包记录表
 * 
 */

public class PatchRecord {
	private String pid;
	private String startCommitId;
	private String endCommitId;
	private String patchUserId;
	private String patchUserName;
	private String patchTime;
	private String patchDesc;
	private String projectId;
	private String projectName;
	private String projectUrl;
	private String patchFileUrl;
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getStartCommitId() {
		return startCommitId;
	}
	public void setStartCommitId(String startCommitId) {
		this.startCommitId = startCommitId;
	}
	public String getEndCommitId() {
		return endCommitId;
	}
	public void setEndCommitId(String endCommitId) {
		this.endCommitId = endCommitId;
	}
	public String getPatchUserId() {
		return patchUserId;
	}
	public void setPatchUserId(String patchUserId) {
		this.patchUserId = patchUserId;
	}
	public String getPatchTime() {
		return patchTime;
	}
	public void setPatchTime(String patchTime) {
		this.patchTime = patchTime;
	}
	public String getPatchDesc() {
		return patchDesc;
	}
	public void setPatchDesc(String patchDesc) {
		this.patchDesc = patchDesc;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getPatchFileUrl() {
		return patchFileUrl;
	}
	public void setPatchFileUrl(String patchFileUrl) {
		this.patchFileUrl = patchFileUrl;
	}
	public String getProjectUrl() {
		return projectUrl;
	}
	public void setProjectUrl(String projectUrl) {
		this.projectUrl = projectUrl;
	}
	public String getPatchUserName() {
		return patchUserName;
	}
	public void setPatchUserName(String patchUserName) {
		this.patchUserName = patchUserName;
	}
	
}
