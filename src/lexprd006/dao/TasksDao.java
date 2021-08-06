package lexprd006.dao;

import java.util.List;

import lexprd006.domain.SubTask;
import lexprd006.domain.SubTaskTranscational;
import lexprd006.domain.Task;

public interface TasksDao {

	public <T> List<T> getAll(Class<T> clazz);
	public Task getTasksForLegalUpdate(String lexcare_task_id);
	public void updateTaskLegalUpdate(Task task);
	public <T> List<T> getTaskHistoryByClientTaskId(String tmap_client_task_id);
	public <T> List<T> getTaskDetailsByClientTaskid(String tmap_client_task_id);
	public String getOriginalFrequency(int ttrn_id);
	public Task getTaskUsingClientTaskID(String tmap_client_task_id);
	public List<SubTaskTranscational> getSubTaskHitoryByclientTaskID(String tmap_client_task_id);
	public SubTask getSubTaskDetailsBysub_task_id(String tmap_client_task_id);
	public List<Object> getTaskForMultipleCompletion(String lexcare_id,int user_id,int role_id,int ttrn_id);
	
}
