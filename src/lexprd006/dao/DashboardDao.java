package lexprd006.dao;

import java.util.List;

import lexprd006.domain.SubTaskTranscational;

public interface DashboardDao {
	
	public <T> List<T> getOverallComplianceGraph(int user_id, int user_role_id, String jsonString);
	public <T> List<T> getOverallComplianceGraphSubTask(int user_id, int user_role_id);
	public <T>List<T> searchGetOverallComplianceGraph(int user_id, int user_role_id, String fromDate, String toDate, String orgaId);
	public List<Object> searchGraph(String jsonString, int user_id, int user_role_id);
	public <T> List<T>  getOverallComplianceGrapheExportReport(int user_id, int user_role_id, String jsonString);
	public List<SubTaskTranscational> getSubTaskHitoryByclientTaskID(String ttrn_sub_task_id);
	public List<Object> getSearchComplianceGraphSubTask(int user_id, int user_role_id, String fromDate, String toDate,
			String orgaId);
	
}
