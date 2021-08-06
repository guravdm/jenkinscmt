package lexprd006.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import lexprd006.domain.SendMail;
import lexprd006.domain.SubTaskTranscational;
import lexprd006.domain.TaskTransactional;
import lexprd006.domain.User;


public interface UtilitiesService {
	public Date getCurrentDate();
	public String getCurrentDateString();
	public int getCurrentYear();
	public int getCurrentSessionUserId(HttpSession session);
	public String encrypt(String algo, String path);
	public String decrypt(String algo, String path);
	public void copy(InputStream is,OutputStream os);
	public String sendUpcomingMailsToExecutor(String subject, int user_id, ArrayList<SendMail> mailsBody);
	public String sendEscalationMailsToEvaluator(String subject, int user_id, ArrayList<SendMail> mailsBody);
	public String sendEscalationMailsToFunctionHead(String subject, int user_id, ArrayList<SendMail> mailsBody);
	public void addMailToLog(String username, String subject,String ClienttaskIds);
	public String sendTaskCompletionMailToEvaluator(ArrayList<TaskTransactional> taskTransactionals,String relatedTo);
	public String sendEscalationMailsUnitHead(String subject,String user_mail,ArrayList<SendMail> mailsBody);
	public String sendEscalationMailsEntityHead(String subject,String user_mail,ArrayList<SendMail> mailsBody);
	public String sendTaskReopenMailToExecutor(int ttrn_id,String comment,String task_type);
	public void addTaskDeletionLog(String ids,String relatedTo,int user_id,String user_name,String clientTaskID,String lexcareTaskId);
	public void addTaskActivationLog(ArrayList<Integer> ids,String taskStatus,int user_id,String user_name);
	public void addTaskEnableLog(ArrayList<Integer> ids,String taskStatus,int user_id,String user_name);
	public void addChangeComplianceOwnerLog(int tmap_id,String previous,String changed,int user_id,String user_name);
	public void addTaskAssignedLog(String assined,int user_id,String user_name);
	public void addTaskCofigurationLog(String clientTaskId,int user_id,String user_name,String relatedTo,String action);
	public void addLegalUpdateLog(String clientTaskIds,int user_id,String user_name,String relatedTo,String action);
	public void addRemoveUserAccessLog(int user_id,String user_name,String orga_loca_dept,String action);
	public String sendSubTaskCompletionMailToEvaluator(SubTaskTranscational subTaskTranscational);
	
	public void sendMail(String user_mail_id,String email_subject,String email_body,String client_task_id_for_log);
	public void addMonthlyReportToLog(String username, String subject);
	public String sendUpcomingMailsToExecutorForSubtask(String subject, int pr_user_id,
			ArrayList<SendMail> sendingMailList);
	public String sendEscalationMailsToEvaluatorForSubtask(String subject, int rw_user_id,
			ArrayList<SendMail> mailsBody);
	public String sendEscalationMailsToFHForSubtask(String subject, int fh_user_id, ArrayList<SendMail> sendingMailList);
	public String sendEscalationAlertToUHForSubtask(String subject, int user_id, ArrayList<SendMail> sendingMailList);
	public String sendEscalationAlertToEHForSubtask(String subject, int user_id, ArrayList<SendMail> sendingMailList);
	public void addMailToLog(String username, String subject,String ClienttaskIds, String address);
	public void saveLogForSendCredentials(User user, StringBuffer randStr, Message message) throws MessagingException;
}
