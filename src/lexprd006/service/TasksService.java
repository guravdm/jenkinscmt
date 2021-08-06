package lexprd006.service;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

public interface TasksService {
	
	public String getAllImportedTasks();
	public String getTasksForLegalUpdate(String jsonString);
	public String addupdateTaskLegalUpdate(MultipartFile multipartFile,String jsonString,HttpSession session);
	public String getTaskHistoryByClientTaskId(String jsonString , HttpSession session);
	public String getTaskDetailsByClientTaskId(String jsonString , HttpSession session);
	public String getMultipleTaskForCompletion(String jsonString,HttpSession session);

}
