package lexprd006.service;

import java.util.Date;

import lexprd006.domain.SubTaskTranscational;

public interface SchedularService {

	public String getUpcomingTasks(String jsonString);

	public String getEscalationsToEvaluator(String jsonString);

	public String getEscalationsToFunctionHead(String jsonString);

	public String getExcalationsToUnitHead();

	public String getExcalationsToEntityHead();

	public String autoActivateTask();

	public Date addDays(Date date, int no_of_days);

	public Date addMonths(Date date, int no_of_months);

	public Date addYears(Date date, int no_of_years);

	public String sendShowCauseNoticeReminder();

	public String showCauseNoticeOneDayBeforeDeadline();

	public String sendActionItemReminder();

	public void subTaskAutoActivate(SubTaskTranscational subTaskTranscational, Date ttrn_next_examination_date);

	public void sendEscalationsToFuntionHead();

	public String sendLogToAdmin();

	public String sendUpcomingNonDeliveredMails(String jsonString);

	public void autoActivateSubTask();

	public String sendNonDeliveredEscalationMails(String jsonString);

	public String sendNonDeliveredEscalationMailsTOFH(String jsonString);

	public String autoComplianceReport(int orga_id);

	public String toUnlockUserAccount();

	public int deleteAllLoggedInUsers();
}
