package diff;

import java.io.IOException;
import java.util.List;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabSession;
import org.gitlab.api.models.GitlabUser;

public class DiffTest {
	
	static GitlabAPI api;
	private static final String TEST_URL = System.getProperty("TEST_URL", "http://192.168.0.200");
	private static final String TEST_TOKEN = System.getProperty("TEST_TOKEN", "Vi_5sGK2kssPnVgEjcjs");
	/**
     * 根据登录的用户名和密码连接git仓库服务器
     *
     * @param username 用户名
     * @param password 密码
     */
	public void connection(String username,String password){
		try {
			api = GitlabAPI.connect(TEST_URL, TEST_TOKEN);
			//api.dispatch().with("login", username).with("password", password).to("session", GitlabUser.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		DiffTest diffTest = new DiffTest();
		diffTest.connection("root", "sysware123456");
		
		try {
			List<GitlabProject> list = api.getAllProjects();
			System.out.println(list.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

}
