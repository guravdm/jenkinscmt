package lexprd006.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public interface TasksRepositoryService {

	public String getAllTasksForRepository(String jsonString, HttpSession session);

	public String getClientTaskIdFromDefaultConfiguartion(String jsonString);

	public String getTaskForExport(String json, HttpSession session);

	public String getAllDocumentForRepository(String jsonString, HttpSession session);

	public String listOfUpcommingTask(HttpSession session);

	public String listWaitingForApprovalTasksTask(String jsonString, HttpSession session);

	public String reopenedTask(String jsonString, HttpSession session);

	public String searchRepository(String jsonString, HttpSession session);

	public String getalltasks(String jsonString, HttpSession session);

	
	public String getCategoryList(HttpSession session);

	public String getTypeOfTask(HttpSession session);

	public String getFrequencyList(HttpSession session);

	public String getExeEvalListByEntity(String orga_id, HttpSession session);

}
