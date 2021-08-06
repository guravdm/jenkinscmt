package lexprd006.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import lexprd006.service.SchedularService;

public class Scheduler {

	@Autowired
	SchedularService schedularService;

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all upcoming Tasks rest Call
//	@Scheduled(fixedDelay = 2000)
//	@Scheduled(cron = "[Seconds] [Minutes] [Hours] [Day of month] [Month] [Day of week] [Year]")
	@Scheduled(cron = "00 * 01 * * ?")
	public void sendUpcomingTaskMail() {
//		System.out.println("Scheduler running for upcoming tasks");
		String jsonString = "";
		schedularService.getUpcomingTasks(jsonString);
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all upcoming Tasks rest Call
	@Scheduled(cron = "00 01 01 * * ?")
	public void sendEscalationsToEvaluator() {
//		System.out.println("Scheduler running for Escalations of evaluator");
		String jsonString = "";
		schedularService.getEscalationsToEvaluator(jsonString);
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all upcoming Tasks rest Call
	@Scheduled(cron = "00 12 02 * * ?")
	public void sendEscalationsToFunctionHead() {
//		System.out.println("Scheduler running for Escalations of Function Head");
		String jsonString = "";
		schedularService.getEscalationsToFunctionHead(jsonString);
	}

	// Method Written By:Harshad Padole(14/04/2017)
	// Method Purpose: Get all escalation alerts to Unit Head
	@Scheduled(cron = "00 30 02 * * ?")
	public void getExcalationsToUnitHead() {
//		System.out.println("Scheduler running for Escalations of Unit Head");
		schedularService.getExcalationsToUnitHead();
	}

	// Method Written By:Harshad Padole(14/04/2017)
	// Method Purpose: Get all escalation alerts to Entity Head
	@Scheduled(cron = "00 02 03 * * ?")
	public void getExcalationsToEntityHead() {
//		System.out.println("Scheduler running for Escalations of Entity Head");
		schedularService.getExcalationsToEntityHead();
	}

	// Method Written By:Harshad Padole(18/04/2017)
	// Method Purpose: Auto Activate Task
	@Scheduled(cron = "00 04 * * * ?")
	public void autoActivateTask() {
//		System.out.println("Scheduler running for Auto Activate convert tasks into upcoming ");
		schedularService.autoActivateTask();
	}

	// Method Written By: Harshad Padole
	// Method Purpose : Send reminder mail to responsible person
	@Scheduled(cron = "00 37 4 * * ?")
	public void sendShowCauseNoticeReminder() {
		schedularService.sendShowCauseNoticeReminder();
	}

	// Method Written By: Harshad Padole
	// Method Purpose : Send one day before deadline mail
	@Scheduled(cron = "00 38 6 * * ?")
	public void sendShowCauseNoticeOneDayBeforeDeadline() {
		schedularService.showCauseNoticeOneDayBeforeDeadline();
	}

	// Method Written By: Harshad Padole
	// Method Purpose : Send action item reminder
	@Scheduled(cron = "00 9 6 * * ?")
	public void sendActionItemReminder() {
		schedularService.sendActionItemReminder();
	}

	@Scheduled(cron = "00 1 00 * * ?")
	public void sendUpcomingNonDeliveredMails() {
//		System.out.println("scheduler running");
		String jsonString = "";
		schedularService.sendUpcomingNonDeliveredMails(jsonString);
	}

	@Scheduled(cron = "00 48 15 * * ?")
	public void sendNonDeliveredEscalationMails() {
//		System.out.println("sendNonDeliveredEscalationMails scheduler running");
		String jsonString = "";
		schedularService.sendNonDeliveredEscalationMails(jsonString);
	}

	@Scheduled(cron = "00 30 01 * * ?")
	public void sendNonDeliveredEscalationMailsTOFH() {
//		System.out.println("scheduler running");
		String jsonString = "";
		schedularService.sendNonDeliveredEscalationMailsTOFH(jsonString);
	}

	@Scheduled(cron = "00 38 14 * * ?")
	public void autoActivateSubTask() {
//		System.out.println("Scheduler running for Auto Activate Sub Task");
		schedularService.autoActivateSubTask();
	}

	@Scheduled(cron = "00 * * * * *")
	public void toUnlockUserAccount() {
//		System.out.println("Scheduler running to Unlock the User Account");
		schedularService.toUnlockUserAccount();
	}

	@Scheduled(cron = "00 * * * * *")
	public void deleteAllLoggedInUsers() {
//		System.out.println("Scheduler running to delete all logged in users.");
		schedularService.deleteAllLoggedInUsers();
	}
	// send email log file on mail.
	/*
	 * @Scheduled(cron = "00 00 22 * * ?") public void sendLogFileToAdmin() {
	 * 
	 * System.out.println("Schedular running to send the Log file");
	 * schedularService.sendLogToAdmin();
	 * System.out.println("Log file sent successfully.."); }
	 */

	/*
	 * @Scheduled(cron="00 15 5 * * ?") public void sendEscalationtoFH(){
	 * schedularService.sendEscalationsToFuntionHead(); }
	 */
	/*
	 * @Scheduled(cron = "6 * * * * ?") public void sendMailDetails() {
	 * System.out.println("hello world! from CMT project -" +
	 * "I'm running from cron");
	 * System.out.println("Method executed at every 5 seconds. Current time is :: "
	 * + new Date()); }
	 */

}
