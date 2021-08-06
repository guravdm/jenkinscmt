package lexprd006.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import lexprd006.domain.LoggedInUsers;
import lexprd006.domain.LogoutTimeLog;
import lexprd006.domain.User;

public interface UsersDao {

	public void persist(Object obj);
	public <T> Set<User> getAll(Class<T> clazz);
	public User getUserById(int user_id);
	public User getUserByUserName(String user_username);
	public void updateUser(User user);
	public int isUserNameExist(int user_id, String user_username);
	public <T> List<T> downloaduserlist();
	public List<User> getUsersByRole(int role_id);
	public <T> Set<User> getUserByAdminAccess(HttpSession httpSession);
	public List<Object> getTasksForUserUpdate(String user_username);
	public void saveUser(User user);
	public User getUserIdByName(String user_name);
	public void saveLoginLogOutLogs(LogoutTimeLog log);
	public void persistUserLoginLog(int user_id, User usr);
	public void updateLogoutTimeByUserId(int userId, Date currentTime, String sessionId);
	public List<Object> getAllUsers();
	public List<Object> checkIfExist(String user_username);
	public void saveLoggedInUser(LoggedInUsers loggedInUser);
	public int deleteLoggedInUser(int userId);
	public int deleteAllLoggedInUsers();
	
}
