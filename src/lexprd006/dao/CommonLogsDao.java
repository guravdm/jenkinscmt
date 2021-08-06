package lexprd006.dao;

import java.util.List;

import javax.servlet.http.HttpSession;

import lexprd006.domain.ComplianceReportLogs;
import lexprd006.domain.EmailLogs;
import lexprd006.domain.LogActivateDeActivateTasks;
import lexprd006.domain.LogReactivation;
import lexprd006.domain.LogTasksConfiguration;
import lexprd006.domain.TaskAssignLogs;
import lexprd006.domain.TaskChangeComplianeAssignLogs;

public interface CommonLogsDao {

	<T> List<T> getLoginLogsData(HttpSession session);

	void persist(TaskAssignLogs assignLogs);

	void persistChangeCompliane(TaskChangeComplianeAssignLogs assignLogs);

	void persistLogTasksConfiguration(LogTasksConfiguration assignLogs);

	void prsistActivateDeactivateLog(LogActivateDeActivateTasks aTasks);

	void saveLogReActivation(LogReactivation log);

	<T> List<T> getAssignLogsData(HttpSession session);

	<T> List<T> changeComplianceOwnerLogsData(HttpSession session);

	<T> List<T> tasksConfigLogsData(HttpSession session);

	void persistComplianceReportLogs(ComplianceReportLogs logs);

	<T> List<T> complianceReportLogsData(HttpSession session);

	<T> List<T> activateDeActivateLogsData(HttpSession session);

	<T>List<T> getReactivateTasksData(HttpSession session);

	void saveUpcomingMail(EmailLogs logs);

	<T>List<T> emailLogsData(HttpSession session);

	void tasksDeletionQuery(String newString);

	void queryDeActivation(String newString);

	void queryDisableTasks(String newString);

}
