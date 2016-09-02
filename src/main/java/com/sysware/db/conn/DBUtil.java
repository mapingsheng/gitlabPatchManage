package com.sysware.db.conn;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.sysware.entity.PatchRecord;
import com.sysware.entity.User;
import com.sysware.utils.StringUtil;
import com.sysware.utils.SyswareUtil;

public class DBUtil {
	private static String dbDriver = "";
	private static String url = "";
	private static String dbUserName = "";
	private static String dbPassWord = "";
	
	{
		try {
			Map<String,String> map = getDbProperties();
			dbDriver = map.get("driverClassName");
			url = map.get("url");
			dbUserName = map.get("username");
			dbPassWord = map.get("password");
			Class.forName(dbDriver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() throws SQLException{
		return  DriverManager.getConnection(url, dbUserName, dbPassWord);
	}
	
	/**
	 * 添加打补丁记录数据
	 * @param patchRecord 补丁记录实体
	 * 
	 */
	public PatchRecord addPatchRecord(PatchRecord patchRecord){
		Connection conn = null;
		Statement statement = null;
		Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dataStr = sf.format(date);
		
		try {
			conn = DBUtil.getConnection();
			statement = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		patchRecord.setPid(StringUtil.getGuid());
		
		StringBuilder sbf = new StringBuilder(" insert into PM_PATCHRECORD (PID,STARTCOMMITID,ENDCOMMITID,PATCHUSERID,PATCHTIME,PATCHDESC,PROJECTID,PROJECTNAME,PATCHFILEURL,PROJECTURL) ");
		sbf.append(" values (");
		sbf.append("'").append(patchRecord.getPid()).append("',")
		   .append("'").append(patchRecord.getStartCommitId()).append("',")
		   .append("'").append(patchRecord.getEndCommitId()).append("',")
		   .append("'").append(patchRecord.getPatchUserId()).append("',")
		   .append("'").append(dataStr).append("',")
		   .append("'").append(patchRecord.getPatchDesc()).append("',")
		   .append("'").append(patchRecord.getProjectId()).append("',")
		   .append("'").append(patchRecord.getProjectName()).append("',")
		   .append("'").append(patchRecord.getPatchFileUrl()).append("',")
		   .append("'").append(patchRecord.getProjectUrl()).append("')");
			
		try {
			statement.executeUpdate(sbf.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		release(conn, statement, null);
		return patchRecord;
	}
	
	/**
	 * 获取补丁记录数据
	 * @param patchRecord 补丁记录实体
	 * 
	 */
	public List<PatchRecord> getPatchRecordList(PatchRecord patchRecord){
		Connection conn = null;
		Statement statement = null;
		ResultSet result = null;
		List<PatchRecord> list = new ArrayList<PatchRecord>();
		
		try {
			conn = DBUtil.getConnection();
			statement = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		StringBuilder sbf = new StringBuilder(" select PID,STARTCOMMITID,ENDCOMMITID,PATCHUSERID,PATCHTIME,PATCHDESC,PROJECTID,PROJECTNAME,PATCHFILEURL,PROJECTURL,   ");
		sbf.append("  (select pu.username from PM_USER pu where pu.pid=PATCHUSERID) as PATCHUSERNAME  from PM_PATCHRECORD where 1=1  ");
		if(!SyswareUtil.isEmpty(patchRecord.getPid())){
			sbf.append(" and PID='"+patchRecord.getPid()+"'");
		}
		
		if(!SyswareUtil.isEmpty(patchRecord.getProjectId())){
			sbf.append(" and PROJECTID='"+patchRecord.getProjectId()+"'");
		}
		
		sbf.append(" order by PATCHTIME desc ");
		
		try {
			result = statement.executeQuery(sbf.toString());
			
			while(result.next()){
				PatchRecord patchRecordTemp = new PatchRecord();
				patchRecordTemp.setPid(result.getString("PID"));
				patchRecordTemp.setProjectId(result.getString("PROJECTID"));
				patchRecordTemp.setStartCommitId(result.getString("STARTCOMMITID"));
				patchRecordTemp.setEndCommitId(result.getString("ENDCOMMITID"));
				patchRecordTemp.setPatchFileUrl(result.getString("PATCHFILEURL"));
				patchRecordTemp.setPatchDesc(result.getString("PATCHDESC"));
				patchRecordTemp.setProjectUrl(result.getString("PROJECTURL"));
				patchRecordTemp.setPatchTime(result.getString("PATCHTIME"));
				patchRecordTemp.setPatchUserName(result.getString("PATCHUSERNAME"));
				
				list.add(patchRecordTemp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		release(conn, statement, result);
		return list;
	}
	
	
	
	/**
	 * 添加用户数据
	 * @param user 用户实体
	 * 
	 */
	public User addUser(User user){
		Connection conn = null;
		Statement statement = null;
		try {
			conn = DBUtil.getConnection();
			statement = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		user.setPid(StringUtil.getGuid());
		StringBuilder sbf = new StringBuilder(" insert into PM_USER (PID,USERNAME,PASSWORD,TOKEN) ");
		sbf.append("values(")
		   .append(user.getPid()).append(",")
		   .append(user.getUserName()).append(",")
		   .append(user.getPassword()).append(",")
		   .append(user.getToken()).append(")");
		try {
			statement.executeUpdate(sbf.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		release(conn, statement, null);
		return user;
	}
	
	/**
	 * 查询用户记录数据
	 * @param user 用户实体
	 * @return list 查询出来的用户列表数据
	 * 
	 */
	public List<User> queryUser(User user){
		if(user==null){
			return null;
		}
		List<User> userList = new ArrayList<User>();
		Connection conn = null;
		Statement statement = null;
		ResultSet result = null;
		
		try {
			conn = DBUtil.getConnection();
			statement = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		StringBuilder sbf = new StringBuilder(" ");
		sbf.append(" select PID,USERNAME,PASSWORD,TOKEN from PM_USER where 1=1 ");
	    if(user.getPid()!=null){
		   sbf.append(" and PID='"+user.getPid()+"' ");   
	    }
	    if(user.getUserName()!=null){
		   sbf.append(" and USERNAME='"+user.getUserName()+"'");
	    }
	    if(user.getPassword()!=null){
	    	sbf.append(" and PASSWORD='"+user.getPassword()+"'");
	    }
	    
	    try {
			result = statement.executeQuery(sbf.toString());
			while(result.next()){
				User userTemp = new User();
				userTemp.setPid(result.getString("PID"));
				userTemp.setUserName(result.getString("USERNAME"));
				userTemp.setPassword(result.getString("PASSWORD"));
				userTemp.setToken(result.getString("TOKEN"));
				userList.add(userTemp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    
	    release(conn, statement, result);
		return userList;
	}
	
	
	/**
	 * 加载数据库资源配置文件文件到资源文件实体对象中 
	 * 
	 */
	protected Properties loadProperties(String fileName){
		Properties properties = new Properties();
		try(InputStream in = DBUtil.class.getClassLoader().getResourceAsStream(fileName)) {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	} 
	
	/**
	 * 读取数据库资源配置文件中的配置选项，并存储到集合中 
	 */
	private Map<String, String> getDbProperties(){
		Map<String,String> maps = new HashMap<String, String>();
		
		Properties pfile = loadProperties("context/boot/oracle.properties");
		String driverClassName = pfile.getProperty("sysware.jdbc.driverClassName");
		String url = pfile.getProperty("sysware.jdbc.url");
		String username = pfile.getProperty("sysware.jdbc.username");
		String password = pfile.getProperty("sysware.jdbc.password");
		
		maps.put("driverClassName", driverClassName);
		maps.put("url", url);
		maps.put("username", username);
		maps.put("password", password);
		return maps;
	}
	
	/**
	 * 释放数据库的资源 
	 */
	public static void release(Connection conn,Statement st,ResultSet rs){
		if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				rs = null;
		}
		
		if(st!=null){
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			st = null;
		}
		
		if(conn!=null){
			try{
				conn.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	
	
	public static void main(String[] args) {
		System.out.println(StringUtil.getGuid());
	}

}
