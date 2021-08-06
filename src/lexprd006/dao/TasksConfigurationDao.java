package lexprd006.dao;

import java.util.ArrayList;
import java.util.List;

import lexprd006.domain.DefaultTaskConfiguration;
import lexprd006.domain.SubTaskTranscational;
import lexprd006.domain.TaskTransactional;

public interface TasksConfigurationDao {

	public void persist(Object obj);
	public void updateTaskConfiguration(TaskTransactional taskTransactional);
	public <T> List<T> getAllConfiguredTaskForActivationPage(String jsonString);
	public String activateTasks(ArrayList<Integer> ttrn_ids);
	public String deactivateTasks(ArrayList<Integer> ttrn_ids);
	public <T> List<T> searchTaskForConfiguration(String json);
	public TaskTransactional getTasksForCompletion(int ttrn_id);
	public void saveTaskCompletion(ArrayList<TaskTransactional> taskTransactionals);
	public <T> List<T> getLatestTtrnForChangeComplianceOwner(int tmap_id);
	public void saveDefaultTaskConfiguration(DefaultTaskConfiguration configuration);
	public List<Object> getTaskDetailsToConfigure(int dtco_id);
	public DefaultTaskConfiguration getDetails(int dtco_id);
	public void updateDefaultConfiguration(DefaultTaskConfiguration configuration);
	public void deleteTaskHistory(int ttrn_id);
	public List<TaskTransactional> getTaskHistoryByClientTaskId(String clientTaskId);
	public void deleteTaskMapping(int tmap_id);
	public <T>List<T> getDefaultTask();
	public SubTaskTranscational getSubTaskTransactionalDetailsById(int sub_id);
	public void merge(Object object);
	public void deleteTaskDocument(int udoc_id);
	public List<Object> searchTasksForConfigurationPage(String json);
	public List<Object> searchTasksForDefaultConfigurationPage(String jsonString);
	public String getDocumentDownloadStatus(String ttrn_id);
	public <T>List<T> getTasksForCompletionWithNativeQuery(int ttrn_id);
	public void updateTaskConfigurationWithNativeQuery(TaskTransactional taskTransactional, int ttrn_id);
	public <T>List<T> getTasksForCompletionNativeQuery(Integer ttrnId);
	public void updateTaskConfigurationNativeQuery(TaskTransactional taskTransactional1, Integer ttrnId);
	public <T>List<T> saveStatusByCompletion(int ttrn_id);
	
} 
