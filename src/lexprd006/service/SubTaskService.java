package lexprd006.service;


import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

public interface SubTaskService {

	public String getUserDefinedTask(HttpSession session);
	public String uploadSubTask(String json,MultipartFile file,HttpSession session);
	public String importedSubTask(String json,HttpSession session);
	public String getTaskForConfiguration(String json,HttpSession session);
	public String saveSubtTaskConfiguration(String json,HttpSession session);
	public String getConfiguredTask(String json,HttpSession session);
	public String activateDeactivateSubTask(String json,HttpSession session);
	public String updateSubTasksConfigurationDates(String jsonString, HttpSession session);
	public String approveSubTask(String json, HttpSession session);
	public String reOpenSubTask(String json, HttpSession session);
	public String updateSubTasksConfiguration(String jsonString, HttpSession session);
	public String downloadSubtaskDocument(String jsonString, HttpServletResponse response) throws Throwable;
	public String deleteSubTaskDocument(String json, HttpSession session);
	public String deleteSubTaskHistory(String json, HttpSession session);
}
