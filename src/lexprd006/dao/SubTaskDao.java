package lexprd006.dao;

import java.util.List;

import javax.servlet.http.HttpSession;

import lexprd006.domain.SubTask;
import lexprd006.domain.SubTaskTranscational;
import lexprd006.domain.UploadedSubTaskDocuments;

public interface SubTaskDao {

	public <T>List<T> getUserDefinedTask(HttpSession session);
	public <T>List<T> checkTaskExist(String client_task_id,String equipment_number);
	public int getLastGeneratedValue(String client_task_id);
	public void saveObject(Object object);
	public <T> List<T> getImportedTask(HttpSession session);
	public <T>List<T> getTaskForConfiguration();
	public <T>List<T> getConfiguredTask(HttpSession session);
	public SubTaskTranscational getTaskConfigurationById(int sub_id);
	public void updateObject(Object object);
	public SubTaskTranscational getSubTaskForCompletion(int ttrn_id);
	public void updateTaskConfiguration(SubTaskTranscational taskTransactional);
	public SubTask getTaskToChangeTheFrequency(String ttrn_sub_task_id);
	public void saveConfiguration(SubTask task);
	public List<UploadedSubTaskDocuments> getAllDocumentByTtrnSubId(int ttrn_id);
	public void deleteDocument(int udoc_sub_task_id);
	public String getProofFilePath(int udoc_sub_id);
	public void deleteTaskDocument(int udoc_sub_task_id);
	public void deleteTaskHistory(int ttrn_sub_task_id);
	public SubTaskTranscational getTaskForCompletion(int ttrn_sub_id);
	public void updateSubTaskConfiguration(SubTaskTranscational subTasktransactional);
	public String getDocumentDownloadStatus(String string);
}
