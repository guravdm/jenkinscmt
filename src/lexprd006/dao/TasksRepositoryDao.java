package lexprd006.dao;

import java.util.List;

import javax.servlet.http.HttpSession;

public interface TasksRepositoryDao {

	public <T> List<T> getAllTask(int user_id, int user_role_id);

	public <T> List<T> getClientTaskIdFromDefaultConfiguartion(String jsonString);

	public <T> List<T> gerTaskForExport(String json, HttpSession session);

	public <T> List<T> listOfUpcommingTask(int parseInt, int parseInt2);

	public <T> List<T> listWaitingForApprovalTasksTask(int parseInt, int parseInt2);

	public <T> List<T> listreopenedTask(int parseInt, int parseInt2);

	public List<Object> searchForRepository(String jsonString, int user_id, int user_role_id);

	//public List<Object> getAllTaskForRepository(int user_id, int user_role_id);

	List<Object> getCategoryList(HttpSession session);

	List<Object> getTypeOfTask(HttpSession session);

	List<Object> getFrequencyList(HttpSession session);

	List<Object> getExeEvalListByEntity(int user_id, int user_role_id, String orga_id);

	List<Object> getAllTaskForRepository(int user_id, int user_role_id, int orga_id, int loca_id, int dept_id);

	public List<Object> getAllTask(String jsonString, int user_id, int user_role_id);

}
