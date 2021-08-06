package lexprd006.service;

import javax.servlet.http.HttpSession;

public interface CommonLogsService {

	String getLoginLogs(HttpSession session);

	String getAssignLogs(HttpSession session);

	String changeComplianceOwnerLogs(HttpSession session);

	String tasksConfigLogs(HttpSession session);

	String complianceReportLogs(HttpSession session);

	String emailLogs(HttpSession session);

	String activateDeActivateLogs(HttpSession session);

	String reactivateTasks(HttpSession session);

	String getqueryBuilder(String jsonString, HttpSession session);

	String queryDeActivation(String jsonString, HttpSession session);

	String queryDisableTasks(String jsonString, HttpSession session);

}
