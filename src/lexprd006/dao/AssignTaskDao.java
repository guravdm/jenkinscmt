package lexprd006.dao;

import java.util.List;

import javax.servlet.http.HttpSession;

import lexprd006.domain.Task;

public interface AssignTaskDao {
	
	public List<Object> getEntityList(HttpSession session);
	public List<Object> getUnitList(String entity_id, HttpSession session);
	public List<Object> getFunctionList(int loca_id, int orga_id, HttpSession session);
	public List<Object> getExecutorList(int orga_id, int loca_id, int dept_id);
	public List<Object> getEvaluatorList(int orga_id, int loca_id, int dept_id);
	public List<Object> getFunHeadList(int orga_id, int loca_id, int dept_id);
	public <T> List<T> searchTaskFromMstTaskForAssign(String jsonString);
	public List<Object> checkIfExist(int orgaId, int unitId, int deptId, String lexcare_task_id, int pr_user_id,
			int rw_user_id, int fh_user_id, int task_id);
	public Task getTaskIdByLexcareTaskId(String lexcare_task_id);
	public List<Object> getExeListForActivationPage(int orga_id, int loca_id, int dept_id);
	public List<Object> getEvalListForActivationPage(int orga_id, int loca_id, int dept_id);
	public List<Object> searchEnableDisablePage(String jsonString);


}
