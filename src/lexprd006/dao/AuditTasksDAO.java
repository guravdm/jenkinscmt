package lexprd006.dao;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.LocalDate;

import lexprd006.domain.TaskTransactional;
import lexprd006.domain.TaskUserMapping;
import lexprd006.domain.UploadedPODDocuments;

public interface AuditTasksDAO {

	<T> List<T> getMonthlyComplianceStatus(int user_id, int user_role_id, String jsonString);

	<T> List<T> getHeadCountsByLocation(HttpSession session, HttpServletResponse res);

	<T> List<T> searchAuditRepository(String jsonString, int user_id, int user_role_id);

	<T>List<T> auditTaskDashboard(int parseInt, int parseInt2, String jsonString);

	String approverCompliedTasksURL(HttpSession session, String auditoComments, String ttrn_id);

	<T>List<T> searhMonthlyComplianceStatus(int parseInt, int parseInt2, String orga_id, String loca_id,
			String dept_id, LocalDate pL1, LocalDate pL2);

	void insertNewTaskUserMapping(TaskUserMapping taskUserMapping);

	TaskTransactional getClientTaskDetailById(String string);

	void copyCompliance(TaskTransactional tsk);

	void savePODDocuments(UploadedPODDocuments documents);

	<T>List<T> getListOfSimplyCompDocuments(String jsonString, HttpSession session);

	public String getProofFilePath(int docId);

	String decrypt(String algo, String string);

	void makeNonCompliedTasks(TaskTransactional taskTransactional);

	<T>List<T>  verticalWiceReport(String fromDate, String toDate, HttpSession session);

	<T>List<T> statusCompletionReport(String fromDate, String toDate, HttpSession session);

	<T>List<T> searchExecutorList(HttpSession session, String orga_id, String loca_id, String dept_id);

	<T>List<T> searchMonthlyComplianceAuditChartURL(int parseInt, int parseInt2, String jsonString);

	<T>List<T> searchAuditDashboard(int userId, int userRoleId, String jsonString);

	<T>List<T>  getTaskActivityListURL(String jsonString, HttpSession session);

	<T>List<T> finalComplianceAuditReport(String orga_id, String loca_id, String dept_id, String date_from, String date_to, HttpSession session);

	<T>List<T> getFunctionListByOrgaId(String orgaId, String locaId, HttpSession session);

}
