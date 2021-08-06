package lexprd006.service;


import javax.servlet.http.HttpSession;

public interface TasksUserMappingService {

	public String saveTasksUserMapping(String jsonString,HttpSession session);
	public String deleteTasksUserMapping(String jsonString , HttpSession session);
	
	public String getDistinctCountries();
	public String getAllStateForCountry(String jsonString);
	public String getAllCat_lawFromMst_task(String jsonString);
	public String getlegislationFromMst_task(String jsonString);
	public String getRuleFromMst_task(String jsonString);
	public String SearchTaskFromMst_task(String jsonString);
	public String getAllMappedTasksForEnablingPage(String jsonString, HttpSession session);
	public String enablingOfTasks(String jsonString, HttpSession session);
	public String changeComplianceOwner(String jsonString, HttpSession session);//this is update task usr mapping 
	public String getTaskForChangeComplianceOwnerPage(String jsonString, HttpSession session);
	
}
