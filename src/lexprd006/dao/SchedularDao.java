package lexprd006.dao;

import java.util.Date;
import java.util.List;

import lexprd006.domain.MainTaskEmailLog;
import lexprd006.domain.SubTaskEmailLog;
import lexprd006.domain.SubTaskTranscational;
import lexprd006.domain.TaskTransactional;
import lexprd006.domain.User;

public interface SchedularDao {
	public <T> List<T> getUpcomingTasks();
	public <T> List<T> getEscalationsToEvaluator();
	public <T> List<T> getEscalationsToFunctionHead();
	public <T> List<T> getEscalationsToUnitHead(int user_id);
	public <T> List<T> getTaskDetailsToSend(int task_id ,String task_type);
	public List<User> getUserList(int role_id);
	public <T> List<T> getEscalationsToEntityHead(int user_id);
	public <T> List <T> getTaskForAutoActivate();
	public <T> List <T> showCauseNoticeReminder();
	public <T> List <T> showCauseNoticeOneDayBeforeDeadline();
	public <T> List <T> actionItemReminder();
	public List<Object> getFunctionHead(String type);
	public <T>List<T> getDetailsToSendFH(int user_id,String type);
	
	public <T> List<T> getUpcomingSubTasks();
	public <T> List<T> getEscalationsToEvaluatorSubTasks();
	public <T> List<T> getEscalationsToFuntionHeadSubTasks();
	public <T> List<T> getEscalationsToUnitHeadSubTasks(int user_id);
	public <T> List<T> getEscalationsToEntityHeadSubTasks(int user_id);
	public void saveEmailLog(MainTaskEmailLog log);
	public List<Object> getTodaysMainTask(String subject, String curr_date);
	public void saveEmailLog(SubTaskEmailLog log);
	public List<Object> getTodaysSubTask(String subject, String curr_date);
	public int updateEmailLog(String client_task_id, String subject);
	public int updateSubTaskEmailLog(String client_task_id, String subject);
	public List<Object> getNonDeliveredMails(String subject, String curr_date);
	public List<Object> getSubTaskForAutoActivate();
	public List<SubTaskTranscational> getSubtaskIfExist(String sub_task_id, Date leagl_date);
	public List<Object> getNonDeliveredMailsOfSubtask(String subject, String curr_date);
	public List<Object> autoComplianceReport(String jsonString, int orga_id);
	public List<Object> getEvaluatorList(String jsonString, int orga_id);
	public void updateIsDocumentDownloadStatus(TaskTransactional tsk);

}
