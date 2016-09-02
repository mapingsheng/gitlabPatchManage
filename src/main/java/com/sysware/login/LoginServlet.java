package com.sysware.login;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.gitlab.api.models.GitlabProject;

import com.sysware.db.conn.DBUtil;
import com.sysware.entity.User;
import com.sysware.git.diff.Diff;
import com.sysware.utils.ReadXmlUtil;
import com.sysware.utils.SyswareUtil;

/**
 * 用户登录控制
 */
@WebServlet("/LoginAction")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public LoginServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getContextPath();
	    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
	    
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if(SyswareUtil.isEmpty(username) || SyswareUtil.isEmpty(password)){
			response.sendRedirect(basePath+"/view/login.jsp");
		}else{
			HttpSession session = request.getSession();
			
			User user = (User) session.getAttribute("user");
			if(!SyswareUtil.isEmpty(user)){//如果用户已经登录，则转向到管理界面
				response.sendRedirect(basePath+"/toPatchManage");
			}else{
				user = new User();
				user.setUserName(username);
				user.setPassword(password);
				
				DBUtil dbUtil = new DBUtil();
				List<User> userList = dbUtil.queryUser(user);
				if(userList.size()!=0){
					User currentUser = userList.get(0);
					session.setAttribute("user", currentUser);
					session.setAttribute(currentUser.getPid(), currentUser.getToken());
					
					Diff.connection(currentUser.getToken());
					List<GitlabProject> projectList = Diff.getProjects();
					request.setAttribute("projectList", projectList);
					request.getRequestDispatcher("view/manage.jsp").forward(request, response);
				}else{
					response.sendRedirect(basePath+"/view/login.jsp");
				}
			}
		}
	}
}
