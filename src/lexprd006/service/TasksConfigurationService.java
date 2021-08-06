package lexprd006.service;


import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

public interface TasksConfigurationService {

	public String savetasksconfiguration(String jsonString, HttpSession session);
	public String activationOfTasks(String jsonString, HttpSession session);
	//public String getAllConfiguredTaskForActivationPage(String jsonString, HttpSession session);
	public String searchTaskForConfiguration(String json);
	public String saveTaskCompletion(ArrayList<MultipartFile> file , String jsonString , HttpSession session);
	public String downloadProofOfCompliance(String jsonString, HttpServletResponse response) throws Throwable;
	public String updateTasksConfiguration(String jsonString , HttpSession session);
	public String savedefaulttaskconfiguration(String jsonString , HttpSession session);
	public String initiateDefaultConfiguredTask(String json,HttpSession session);
	public String approveTask(String json,HttpSession session);
	public String reOpenTask(String json,HttpSession session);
	public String deleteTaskHistory(String json,HttpSession session);
	public String deleteTaskMapping(String json,HttpSession session);
	public String getDefaultTaskConfiguration(String json);
	public String updateDefaultTaskConfiguration(String json,HttpSession session);
	public String updateResoneOfNonCompliance(String jsonString,HttpSession session);
	public String saveSubTaskCompletion(ArrayList<MultipartFile> file,String json,HttpSession session);
	public String deleteTaskDocument(String json,HttpSession session);
	public String searchTaskForConfigurationPage(String json);
	public String getTaskListForDefaultTaskConfiguration(String jsonString);
	public String checkDocumentBeforeDownload(String jsonString, HttpServletResponse response);
}
