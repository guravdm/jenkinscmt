package lexprd006.service;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

public interface AssignTaskService {

	public String getEntityList(HttpSession session);
	public String getUnitList(String entity_id, HttpSession session);
	public String getFunctionList(String jsonString, HttpSession session);
	public String getExecutorList(String jsonString);
	public String getEvaluatorList(String json);
	public String getFunHeadList(String json);
	public String getExeListForChangeOwner(String jsonString);
	public String getEvalListForChangeOwner(String jsonString);
	public String getFunHeadListForChangeOwner(String json);
	public String searchcomplianceownerpage(String jsonString, HttpSession session);
	public String uploadAssignTaskList(MultipartFile assign_task_list, String jsonString, HttpSession session);
	public String getExeListForActivationPage(String jsonString);
	public String getEvalListForActivationPageURL(String jsonString);
	public String searchActivationPage(String jsonString, HttpSession session);
	public String searchEnableDisablePage(String jsonString, HttpSession session);
	public String getTaskListToAssign(String jsonString, HttpSession session);
}
