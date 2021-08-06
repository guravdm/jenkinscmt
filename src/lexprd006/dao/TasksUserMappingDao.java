package lexprd006.dao;

import java.util.ArrayList;
import java.util.List;

import lexprd006.domain.TaskUserMapping;

public interface TasksUserMappingDao {
	
	public void persist(Object obj);
	public void updateTaskUserMapping(TaskUserMapping taskUserMapping);
	public int getMaxLastGeneratedValue(int loca_id,int dept_id);
	
	public <T> List<T> getDistinctCountries();
	public <T> List<T> getAllStateForCountry(int task_country_id);
	public <T> List<T> getAllCat_lawFromMst_task(String json);
	public <T> List<T> getlegislationFromMst_task(String json);
	public <T> List<T> getRuleFromMst_task(String json);
	public <T> List<T> searchTaskFromMstTaskForAssign(String jsonString);
	public <T> List<T> getAllMappedTasksForEnablingPage(String jsonString);
	public String enableTasks(ArrayList<Integer> tmap_ids);
	public String disableTasks(ArrayList<Integer> tmap_ids);
	public TaskUserMapping getTmapForchangeComplianceOwner(int tmap_id);
	public void deleteTaskUserMapping(TaskUserMapping taskUserMapping);
	public <T> List<T> getDisabledTaskDetails(int tmap_id);
	public List<Object> getAssignedRecord(String jsonString, int tmap_task_id, String tmap_lexcare_task_id, int loca_id);
	public <T> List<T> getAllMappedTasks();
	public List<Object> checkIfMappingExist(int tmap_orga_id, int loca_id, int tmap_dept_id);

}
