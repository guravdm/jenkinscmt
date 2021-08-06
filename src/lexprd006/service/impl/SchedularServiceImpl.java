package lexprd006.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import lexprd006.dao.CommonLogsDao;
import lexprd006.dao.EntityDao;
import lexprd006.dao.FunctionDao;
import lexprd006.dao.SchedularDao;
import lexprd006.dao.SubTaskDao;
import lexprd006.dao.TasksConfigurationDao;
import lexprd006.dao.TasksDao;
import lexprd006.dao.UsersDao;
import lexprd006.domain.Department;
import lexprd006.domain.LogReactivation;
import lexprd006.domain.MainTaskEmailLog;
import lexprd006.domain.Organization;
import lexprd006.domain.SendMail;
import lexprd006.domain.SubTask;
import lexprd006.domain.SubTaskEmailLog;
import lexprd006.domain.SubTaskTranscational;
import lexprd006.domain.TaskTransactional;
import lexprd006.domain.User;
import lexprd006.service.SchedularService;
import lexprd006.service.UtilitiesService;

@Service(value = "schedularService")
public class SchedularServiceImpl implements SchedularService {

	public final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public final SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// public final SimpleDateFormat sdfOut = new SimpleDateFormat("dd-MM-yyyy");
	public final SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd");

	private @Value("#{config['mail_user_name'] ?: 'null'}") String username;
	private @Value("#{config['mail_password'] ?: 'null'}") String password;
	private @Value("#{config['mail_smtp_host'] ?: 'null'}") String hostName;
	private @Value("#{config['mail_smtp_port'] ?: 'null'}") String portNo;
	private @Value("#{config['mail_from'] ?: 'null'}") String mailFrom;
	private @Value("#{config['project_url'] ?: 'null'}") String url;
	private @Value("#{config['project_name'] ?: 'null'}") String projectName;

	@Autowired
	SchedularDao schedularDao;
	@Autowired
	UtilitiesService utilitiesService;
	@Autowired
	TasksConfigurationDao tasksConfigurationDao;

	@Autowired
	TasksDao tasksDao;
	@Autowired
	SubTaskDao subTaskDao;

	@Autowired
	CommonLogsDao cLogsDao;
	@Autowired
	UsersDao userDao;

	@Autowired
	FunctionDao functionDao;
	@Autowired
	EntityDao entityDao;

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all upcoming Tasks rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String getUpcomingTasks(String jsonString) {
		try {
			List<Object> tasksList = schedularDao.getUpcomingTasks();
			int pr_user_id = 0;
			System.out.println("size:" + tasksList.size());
			ArrayList<SendMail> sendingMailList = new ArrayList<>();
			Iterator<Object> itr = tasksList.iterator();

			String created_at = sdfOut.format(new Date());
			// System.out.println("date:" + sdfOut.format(new Date()));

			Date date = new Date();
			while (itr.hasNext()) {
				System.out.println("inside while loop tasksList size : " + tasksList.size());
				Object[] object = (Object[]) itr.next();
				User user = userDao.getUserById(Integer.parseInt(object[0].toString()));
				MainTaskEmailLog log = new MainTaskEmailLog();
				log.setClient_task_id(object[1].toString());
				log.setEntity_name(object[2].toString());
				log.setUnit_name(object[3].toString());
				log.setFunction_name(object[4].toString());
				log.setName_of_legislation(object[5].toString());
				log.setName_of_rule(object[6].toString());
				log.setLog_when(object[7].toString());
				log.setActivity(object[8].toString());
				log.setFrequency(object[9].toString());
				log.setImpact(object[10].toString());
				log.setExec_id(Integer.parseInt(object[0].toString()));
				log.setEval_id(Integer.parseInt(object[19].toString()));
				log.setFh_id(Integer.parseInt(object[21].toString()));
				log.setExecutor_date(sdfOut.format(sdfIn.parse(object[11].toString())));
				log.setEvaluator_date(sdfOut.format(sdfIn.parse(object[12].toString())));
				log.setFunc_head_date(sdfOut.format(sdfIn.parse(object[13].toString())));
				log.setUnit_head_date(sdfOut.format(sdfIn.parse(object[14].toString())));
				log.setLegal_due_date(sdfOut.format(sdfIn.parse(object[15].toString())));
				System.out.println("legal date:" + sdfOut.format(sdfIn.parse(object[15].toString())));
				log.setEmail_subject("Upcoming Task Alert");
				log.setEmail_to(user.getUser_email());
				log.setEmail_status("0");
				log.setEmail_sent_at(sdfOut.format(new Date()));

				log.setCreated_at(sdfOut.format(new Date()));
				schedularDao.saveEmailLog(log);

				// Need to check the reason why we add the below code here
				/*
				 * TaskTransactional tsk = new TaskTransactional(); tsk.setIsDocumentUpload(0);
				 * tsk.setTtrn_client_task_id(object[1].toString());
				 * System.out.println("updateIsDocumentDownloadStatus 0");
				 * schedularDao.updateIsDocumentDownloadStatus(tsk);
				 */

			}

			// All the records has been stored in main_task_email log table. Now we will
			// fatch

			List<Object> mailList = schedularDao.getTodaysMainTask("Upcoming Task Alert", created_at);

			int totSize = mailList.size();
			int numOfCycle = totSize / 20;
			if (numOfCycle == 0) {
				for (int i = 0; i < totSize; i++) {
					SendMail sendMail = new SendMail();
					Object[] object = (Object[]) mailList.get(i);
					if (pr_user_id != Integer.parseInt(object[9].toString()) && pr_user_id != 0) {

						// This will give time to send the email
						Thread.sleep(2000);

						System.out.println("Send email");
						utilitiesService.sendUpcomingMailsToExecutor("Upcoming Task Alert", pr_user_id,
								sendingMailList);

						sendingMailList.clear();
						pr_user_id = Integer.parseInt(object[9].toString());

						sendMail.setClient_task_id(object[2].toString());
						sendMail.setEntity_name(object[6].toString());
						sendMail.setUnit_name(object[20].toString());
						sendMail.setFunction_name(object[13].toString());
						sendMail.setName_of_legislation(object[17].toString());
						sendMail.setName_of_rule(object[18].toString());
						sendMail.setWhen(object[16].toString());
						sendMail.setActivity(object[1].toString());
						sendMail.setFrequency(object[11].toString());
						sendMail.setImpact(object[14].toString());
						sendMail.setExecutor_date(object[10].toString());
						sendMail.setEvaluator_date(object[8].toString());
						sendMail.setFunc_head_date(object[12].toString());
						sendMail.setUnit_head_date(object[19].toString());
						sendMail.setLegal_due_date(object[15].toString());
						sendMail.setExec_id(Integer.parseInt(object[9].toString()));
						sendMail.setEval_id(Integer.parseInt(object[7].toString()));
						System.out.println("IN FIRST IF LOOP:" + object[15].toString());
						sendingMailList.add(sendMail);

					} else {
						pr_user_id = Integer.parseInt(object[9].toString());

						sendMail.setClient_task_id(object[2].toString());
						sendMail.setEntity_name(object[6].toString());
						sendMail.setUnit_name(object[20].toString());
						sendMail.setFunction_name(object[13].toString());
						sendMail.setName_of_legislation(object[17].toString());
						sendMail.setName_of_rule(object[18].toString());
						sendMail.setWhen(object[16].toString());
						sendMail.setActivity(object[1].toString());
						sendMail.setFrequency(object[11].toString());
						sendMail.setImpact(object[14].toString());
						sendMail.setExecutor_date(object[10].toString());
						sendMail.setEvaluator_date(object[8].toString());
						sendMail.setFunc_head_date(object[12].toString());
						sendMail.setUnit_head_date(object[19].toString());
						sendMail.setLegal_due_date(object[15].toString());
						sendMail.setExec_id(Integer.parseInt(object[9].toString()));
						sendMail.setEval_id(Integer.parseInt(object[7].toString()));
						System.out.println("IN FIRST ELSE LOOP:" + object[15].toString());
						sendingMailList.add(sendMail);

					}
				}
			} else {
				int first = 0;
				int last = 20;

				for (int p = 0; p < numOfCycle; p++) {

					for (int k = first; k < last; k++) {
						SendMail sendMail = new SendMail();
						Object[] object = (Object[]) mailList.get(k);
						if (pr_user_id != Integer.parseInt(object[9].toString()) && pr_user_id != 0) {

							utilitiesService.sendUpcomingMailsToExecutor("Upcoming Task Alert", pr_user_id,
									sendingMailList);

							// This will give time to send the email
							Thread.sleep(2000);

							sendingMailList.clear();
							pr_user_id = Integer.parseInt(object[9].toString());

							sendMail.setClient_task_id(object[2].toString());
							sendMail.setEntity_name(object[6].toString());
							sendMail.setUnit_name(object[20].toString());
							sendMail.setFunction_name(object[13].toString());
							sendMail.setName_of_legislation(object[17].toString());
							sendMail.setName_of_rule(object[18].toString());
							sendMail.setWhen(object[16].toString());
							sendMail.setActivity(object[1].toString());
							sendMail.setFrequency(object[11].toString());
							sendMail.setImpact(object[14].toString());
							sendMail.setExecutor_date(object[10].toString());
							sendMail.setEvaluator_date(object[8].toString());
							sendMail.setFunc_head_date(object[12].toString());
							sendMail.setUnit_head_date(object[19].toString());
							sendMail.setLegal_due_date(object[15].toString());
							sendMail.setExec_id(Integer.parseInt(object[9].toString()));
							sendMail.setEval_id(Integer.parseInt(object[7].toString()));
							System.out.println("IN SECOND IF LOOP:" + object[15].toString());
							sendingMailList.add(sendMail);

						} else {

							pr_user_id = Integer.parseInt(object[9].toString());

							sendMail.setClient_task_id(object[2].toString());
							sendMail.setEntity_name(object[6].toString());
							sendMail.setUnit_name(object[20].toString());
							sendMail.setFunction_name(object[13].toString());
							sendMail.setName_of_legislation(object[17].toString());
							sendMail.setName_of_rule(object[18].toString());
							sendMail.setWhen(object[16].toString());
							sendMail.setActivity(object[1].toString());
							sendMail.setFrequency(object[11].toString());
							sendMail.setImpact(object[14].toString());
							sendMail.setExecutor_date(object[10].toString());
							sendMail.setEvaluator_date(object[8].toString());
							sendMail.setFunc_head_date(object[12].toString());
							sendMail.setUnit_head_date(object[19].toString());
							sendMail.setLegal_due_date(object[15].toString());
							sendMail.setExec_id(Integer.parseInt(object[9].toString()));
							sendMail.setEval_id(Integer.parseInt(object[7].toString()));
							System.out.println("IN SECOND ELSE LOOP:" + object[15].toString());
							sendingMailList.add(sendMail);
						}
					}

					Thread.sleep(2000);
					first = first + 20;

					last = last + 20;
					// arr2 = null;
					// System.out.println("first:" + first + "last:" + last);
				}

				if (totSize % 20 != 0) {
					for (int a = first; a < totSize; a++) {
						SendMail sendMail = new SendMail();
						Object[] object = (Object[]) mailList.get(a);
						if (pr_user_id != Integer.parseInt(object[9].toString()) && pr_user_id != 0) {

							utilitiesService.sendUpcomingMailsToExecutor("Upcoming Task Alert", pr_user_id,
									sendingMailList);

							// This will give time to send the email
							Thread.sleep(2000);

							sendingMailList.clear();
							pr_user_id = Integer.parseInt(object[9].toString());

							sendMail.setClient_task_id(object[2].toString());
							sendMail.setEntity_name(object[6].toString());
							sendMail.setUnit_name(object[20].toString());
							sendMail.setFunction_name(object[13].toString());
							sendMail.setName_of_legislation(object[17].toString());
							sendMail.setName_of_rule(object[18].toString());
							sendMail.setWhen(object[16].toString());
							sendMail.setActivity(object[1].toString());
							sendMail.setFrequency(object[11].toString());
							sendMail.setImpact(object[14].toString());
							sendMail.setExecutor_date(object[10].toString());
							sendMail.setEvaluator_date(object[8].toString());
							sendMail.setFunc_head_date(object[12].toString());
							sendMail.setUnit_head_date(object[19].toString());
							sendMail.setLegal_due_date(object[15].toString());
							sendMail.setExec_id(Integer.parseInt(object[9].toString()));
							sendMail.setEval_id(Integer.parseInt(object[7].toString()));
							System.out.println("IN THIRD IF LOOP:" + object[15].toString());
							sendingMailList.add(sendMail);
						} else {
							pr_user_id = Integer.parseInt(object[9].toString());

							sendMail.setClient_task_id(object[2].toString());
							sendMail.setEntity_name(object[6].toString());
							sendMail.setUnit_name(object[20].toString());
							sendMail.setFunction_name(object[13].toString());
							sendMail.setName_of_legislation(object[17].toString());
							sendMail.setName_of_rule(object[18].toString());
							sendMail.setWhen(object[16].toString());
							sendMail.setActivity(object[1].toString());
							sendMail.setFrequency(object[11].toString());
							sendMail.setImpact(object[14].toString());
							sendMail.setExecutor_date(object[10].toString());
							sendMail.setEvaluator_date(object[8].toString());
							sendMail.setFunc_head_date(object[12].toString());
							sendMail.setUnit_head_date(object[19].toString());
							sendMail.setLegal_due_date(object[15].toString());
							sendMail.setExec_id(Integer.parseInt(object[9].toString()));
							sendMail.setEval_id(Integer.parseInt(object[7].toString()));
							System.out.println("IN THIRD ELSE LOOP:" + object[15].toString());
							sendingMailList.add(sendMail);
						}
					}

					if (sendingMailList.size() > 0) {
						utilitiesService.sendUpcomingMailsToExecutor("Upcoming Task Alert", pr_user_id,
								sendingMailList);
					}

				}
			}

			sendingMailList.clear();
			pr_user_id = 0;
			// Send Sub task start
			List<Object> subtaskList = schedularDao.getUpcomingSubTasks();
			System.out.println("sub task size:" + subtaskList.size());
			Iterator<Object> itr1 = subtaskList.iterator();

			while (itr1.hasNext()) {
				Object[] object = (Object[]) itr1.next();
				User user = userDao.getUserById(Integer.parseInt(object[0].toString()));
				SubTaskEmailLog log = new SubTaskEmailLog();
				log.setClient_task_id(object[1].toString());
				log.setEntity_name(object[2].toString());
				log.setUnit_name(object[3].toString());
				log.setFunction_name(object[4].toString());
				log.setName_of_legislation(object[5].toString());
				log.setName_of_rule(object[6].toString());
				log.setLog_when(object[7].toString());
				log.setActivity(object[8].toString());
				log.setFrequency(object[9].toString());
				log.setImpact(object[10].toString());
				log.setExec_id(Integer.parseInt(object[0].toString()));
				log.setEval_id(Integer.parseInt(object[19].toString()));
				log.setFh_id(Integer.parseInt(object[21].toString()));
				log.setSub_client_task_id(object[18].toString());
				log.setExecutor_date(sdfOut.format(sdfIn.parse(object[11].toString())));
				log.setEvaluator_date(sdfOut.format(sdfIn.parse(object[12].toString())));
				log.setFunc_head_date(sdfOut.format(sdfIn.parse(object[13].toString())));
				log.setUnit_head_date(sdfOut.format(sdfIn.parse(object[14].toString())));
				log.setLegal_due_date(sdfOut.format(sdfIn.parse(object[15].toString())));
				System.out.println("legal date:" + sdfOut.format(sdfIn.parse(object[15].toString())));
				log.setEmail_subject("Upcoming Sub Task Alert");
				log.setEmail_to(user.getUser_email());
				// log.setEmail_cc("swapnali.kumbhar@lexcareglobal.com");
				log.setEmail_status("0");
				log.setEmail_sent_at(sdfOut.format(new Date()));
				log.setCompliance_activity(object[23].toString());
				log.setSub_task_unit(object[24].toString());
				log.setName_of_contractor(object[25].toString());
				log.setCompliance_title(object[26].toString());

				log.setCreated_at(sdfOut.format(new Date()));
				schedularDao.saveEmailLog(log);

			}

			int subI = 0;

			if (subI > 1) {

				List<Object> subTaskMailList = schedularDao.getTodaysSubTask("Upcoming Sub Task Alert", created_at);
				if (subTaskMailList != null || !subTaskMailList.isEmpty()) {
					for (int i = 0; i < subTaskMailList.size(); i++) {
						SendMail sendMail = new SendMail();
						Object[] object1 = (Object[]) subTaskMailList.get(i);
						System.out.println("user_id:" + Integer.parseInt(object1[9].toString()));
						if (pr_user_id != Integer.parseInt(object1[9].toString()) && pr_user_id != 0) {
							utilitiesService.sendUpcomingMailsToExecutorForSubtask("Upcoming Sub Task Alert",
									pr_user_id, sendingMailList);

							sendingMailList.clear();
							sendMail.setClient_task_id(object1[2].toString());
							sendMail.setEntity_name(object1[6].toString());
							sendMail.setUnit_name(object1[20].toString());
							sendMail.setFunction_name(object1[13].toString());
							sendMail.setName_of_legislation(object1[17].toString());
							sendMail.setName_of_rule(object1[18].toString());
							sendMail.setWhen(object1[16].toString());
							sendMail.setActivity(object1[1].toString());
							sendMail.setFrequency(object1[11].toString());
							sendMail.setImpact(object1[14].toString());
							sendMail.setExecutor_date(object1[10].toString());
							sendMail.setEvaluator_date(object1[8].toString());
							sendMail.setFunc_head_date(object1[12].toString());
							sendMail.setUnit_head_date(object1[19].toString());
							sendMail.setLegal_due_date(object1[15].toString());
							sendMail.setExec_id(Integer.parseInt(object1[9].toString()));
							sendMail.setEval_id(Integer.parseInt(object1[7].toString()));
							sendMail.setCompliance_activity(object1[23].toString());
							sendMail.setSub_task_unit(object1[24].toString());
							sendMail.setName_of_contractor(object1[25].toString());
							sendMail.setCompliance_title(object1[26].toString());
							System.out.println("IN FIRST IF LOOP:" + object1[15].toString());
							sendMail.setSub_client_task_id(object1[21].toString());
							System.out.println("IN LAST ELSE LOOP:" + object1[15].toString());
							sendingMailList.add(sendMail);

						} else {
							pr_user_id = Integer.parseInt(object1[9].toString());

							sendMail.setClient_task_id(object1[2].toString());
							sendMail.setEntity_name(object1[6].toString());
							sendMail.setUnit_name(object1[20].toString());
							sendMail.setFunction_name(object1[13].toString());
							sendMail.setName_of_legislation(object1[17].toString());
							sendMail.setName_of_rule(object1[18].toString());
							sendMail.setWhen(object1[16].toString());
							sendMail.setActivity(object1[1].toString());
							sendMail.setFrequency(object1[11].toString());
							sendMail.setImpact(object1[14].toString());
							sendMail.setExecutor_date(object1[10].toString());
							sendMail.setEvaluator_date(object1[8].toString());
							sendMail.setFunc_head_date(object1[12].toString());
							sendMail.setUnit_head_date(object1[19].toString());
							sendMail.setLegal_due_date(object1[15].toString());
							sendMail.setExec_id(Integer.parseInt(object1[9].toString()));
							sendMail.setEval_id(Integer.parseInt(object1[7].toString()));
							sendMail.setCompliance_activity(object1[23].toString());
							sendMail.setSub_task_unit(object1[24].toString());
							sendMail.setName_of_contractor(object1[25].toString());
							sendMail.setCompliance_title(object1[26].toString());
							System.out.println("IN FIRST IF LOOP:" + object1[15].toString());
							sendMail.setSub_client_task_id(object1[21].toString());
							System.out.println("IN LAST ELSE LOOP:" + object1[16].toString());
							sendingMailList.add(sendMail);

						}

					} // send upcoming task
				}

			} // end if
			if (sendingMailList.size() > 0) {
				utilitiesService.sendUpcomingMailsToExecutorForSubtask("Upcoming Sub Task Alert", pr_user_id,
						sendingMailList);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all upcoming Tasks rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String getEscalationsToEvaluator(String jsonString) {
		JSONObject objForSend = new JSONObject();
		int rw_user_id = 0;
		try {
			List<Object> tasksList = schedularDao.getEscalationsToEvaluator();
			// System.out.println("tasksize:" + tasksList.size());
			ArrayList<SendMail> sendingMailList = new ArrayList<>();
			Iterator<Object> itr = tasksList.iterator();

			String created_at = sdfOut.format(new Date());
			// System.out.println("date:" + sdfOut.format(new Date()));

			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				User user = userDao.getUserById(Integer.parseInt(object[0].toString()));
				MainTaskEmailLog log = new MainTaskEmailLog();
				log.setClient_task_id(object[1].toString());
				log.setEntity_name(object[2].toString());
				log.setUnit_name(object[3].toString());
				log.setFunction_name(object[4].toString());
				log.setName_of_legislation(object[5].toString());
				log.setName_of_rule(object[6].toString());
				log.setLog_when(object[7].toString());
				log.setActivity(object[8].toString());
				log.setFrequency(object[9].toString());
				log.setImpact(object[10].toString());
				log.setExec_id(Integer.parseInt(object[19].toString()));
				log.setEval_id(Integer.parseInt(object[0].toString()));
				log.setFh_id(Integer.parseInt(object[21].toString()));
				log.setExecutor_date(sdfOut.format(sdfIn.parse(object[11].toString())));
				log.setEvaluator_date(sdfOut.format(sdfIn.parse(object[12].toString())));
				log.setFunc_head_date(sdfOut.format(sdfIn.parse(object[13].toString())));
				log.setUnit_head_date(sdfOut.format(sdfIn.parse(object[14].toString())));
				log.setLegal_due_date(sdfOut.format(sdfIn.parse(object[15].toString())));
				// System.out.println("legal date:" +
				// sdfOut.format(sdfIn.parse(object[15].toString())));
				log.setEmail_subject("Escalation Alert To Evaluator");
				log.setEmail_to(user.getUser_email());
				log.setEmail_status("0");
				log.setEmail_sent_at(sdfOut.format(new Date()));

				log.setCreated_at(sdfOut.format(new Date()));
				schedularDao.saveEmailLog(log);

			}

			List<Object> mailList = schedularDao.getTodaysMainTask("Escalation Alert To Evaluator", created_at);
			for (int i = 0; i < mailList.size(); i++) {
				SendMail sendMail = new SendMail();
				Object[] object = (Object[]) mailList.get(i);
				if (rw_user_id != Integer.parseInt(object[7].toString()) && rw_user_id != 0) {

					Thread.sleep(2000);
					utilitiesService.sendEscalationMailsToEvaluator("Escalation Alert To Evaluator", rw_user_id,
							sendingMailList);

					sendingMailList.clear();
					rw_user_id = Integer.parseInt(object[7].toString());

					sendMail.setClient_task_id(object[2].toString());
					sendMail.setEntity_name(object[6].toString());
					sendMail.setUnit_name(object[20].toString());
					sendMail.setFunction_name(object[13].toString());
					sendMail.setName_of_legislation(object[17].toString());
					sendMail.setName_of_rule(object[18].toString());
					sendMail.setWhen(object[16].toString());
					sendMail.setActivity(object[1].toString());
					sendMail.setFrequency(object[11].toString());
					sendMail.setImpact(object[14].toString());
					sendMail.setExecutor_date(object[10].toString());
					sendMail.setEvaluator_date(object[8].toString());
					sendMail.setFunc_head_date(object[12].toString());
					sendMail.setUnit_head_date(object[19].toString());
					sendMail.setLegal_due_date(object[15].toString());
					sendMail.setExec_id(Integer.parseInt(object[9].toString()));
					sendMail.setEval_id(Integer.parseInt(object[7].toString()));
					// System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendingMailList.add(sendMail);

				} else {
					rw_user_id = Integer.parseInt(object[7].toString());

					sendMail.setClient_task_id(object[2].toString());
					sendMail.setEntity_name(object[6].toString());
					sendMail.setUnit_name(object[20].toString());
					sendMail.setFunction_name(object[13].toString());
					sendMail.setName_of_legislation(object[17].toString());
					sendMail.setName_of_rule(object[18].toString());
					sendMail.setWhen(object[16].toString());
					sendMail.setActivity(object[1].toString());
					sendMail.setFrequency(object[11].toString());
					sendMail.setImpact(object[14].toString());
					sendMail.setExecutor_date(object[10].toString());
					sendMail.setEvaluator_date(object[8].toString());
					sendMail.setFunc_head_date(object[12].toString());
					sendMail.setUnit_head_date(object[19].toString());
					sendMail.setLegal_due_date(object[15].toString());
					// System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendMail.setExec_id(Integer.parseInt(object[9].toString()));
					sendMail.setEval_id(Integer.parseInt(object[7].toString()));
					// System.out.println("IN FIRST ELSE LOOP:" + object[16].toString());
					sendingMailList.add(sendMail);

				}
			}

			if (sendingMailList.size() > 0) {
				utilitiesService.sendEscalationMailsToEvaluator("Escalation Alert To Evaluator", rw_user_id,
						sendingMailList);
			}

			sendingMailList.clear();
			rw_user_id = 0;
			List<Object> subTaskList = schedularDao.getEscalationsToEvaluatorSubTasks();
			// System.out.println("sub task size:" + subTaskList.size());
			Iterator<Object> itr1 = subTaskList.iterator();

			while (itr1.hasNext()) {
				Object[] object = (Object[]) itr1.next();
				User user = userDao.getUserById(Integer.parseInt(object[0].toString()));
				SubTaskEmailLog log = new SubTaskEmailLog();
				log.setClient_task_id(object[1].toString());
				log.setEntity_name(object[2].toString());
				log.setUnit_name(object[3].toString());
				log.setFunction_name(object[4].toString());
				log.setName_of_legislation(object[5].toString());
				log.setName_of_rule(object[6].toString());
				log.setLog_when(object[7].toString());
				log.setActivity(object[8].toString());
				log.setFrequency(object[9].toString());
				log.setImpact(object[10].toString());
				log.setExec_id(Integer.parseInt(object[19].toString()));
				log.setEval_id(Integer.parseInt(object[0].toString()));
				log.setSub_client_task_id(object[18].toString());
				log.setFh_id(Integer.parseInt(object[21].toString()));
				log.setExecutor_date(sdfOut.format(sdfIn.parse(object[11].toString())));
				log.setEvaluator_date(sdfOut.format(sdfIn.parse(object[12].toString())));
				log.setFunc_head_date(sdfOut.format(sdfIn.parse(object[13].toString())));
				log.setUnit_head_date(sdfOut.format(sdfIn.parse(object[14].toString())));
				log.setLegal_due_date(sdfOut.format(sdfIn.parse(object[15].toString())));
				// System.out.println("legal date:" +
				// sdfOut.format(sdfIn.parse(object[15].toString())));
				log.setEmail_subject("Escalation Sub Task Alert To Evaluator");
				log.setEmail_to(user.getUser_email());
				log.setEmail_status("0");
				log.setEmail_sent_at(sdfOut.format(new Date()));
				log.setCompliance_activity(object[22].toString());
				log.setSub_task_unit(object[23].toString());
				log.setName_of_contractor(object[24].toString());
				log.setCompliance_title(object[25].toString());

				log.setCreated_at(sdfOut.format(new Date()));
				schedularDao.saveEmailLog(log);

			}

			List<Object> subTaskMailList = schedularDao.getTodaysSubTask("Escalation Sub Task Alert To Evaluator",
					created_at);
			for (int i = 0; i < subTaskMailList.size(); i++) {
				SendMail sendMail = new SendMail();

				Object[] object = (Object[]) subTaskMailList.get(i);
				if (rw_user_id != Integer.parseInt(object[7].toString()) && rw_user_id != 0) {
					utilitiesService.sendEscalationMailsToEvaluatorForSubtask("Escalation Sub Task Alert To Evaluator",
							rw_user_id, sendingMailList);

					sendingMailList.clear();
					rw_user_id = Integer.parseInt(object[7].toString());

					sendMail.setClient_task_id(object[2].toString());
					sendMail.setEntity_name(object[6].toString());
					sendMail.setUnit_name(object[20].toString());
					sendMail.setFunction_name(object[13].toString());
					sendMail.setName_of_legislation(object[17].toString());
					sendMail.setName_of_rule(object[18].toString());
					sendMail.setWhen(object[16].toString());
					sendMail.setActivity(object[1].toString());
					sendMail.setFrequency(object[11].toString());
					sendMail.setImpact(object[14].toString());
					sendMail.setExecutor_date(object[10].toString());
					sendMail.setEvaluator_date(object[8].toString());
					sendMail.setFunc_head_date(object[12].toString());
					sendMail.setUnit_head_date(object[19].toString());
					sendMail.setLegal_due_date(object[15].toString());
					sendMail.setCompliance_activity(object[23].toString());
					sendMail.setSub_task_unit(object[24].toString());
					sendMail.setName_of_contractor(object[25].toString());
					sendMail.setCompliance_title(object[26].toString());
					// System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendMail.setSub_client_task_id(object[21].toString());
					sendMail.setExec_id(Integer.parseInt(object[9].toString()));
					sendMail.setEval_id(Integer.parseInt(object[7].toString()));
					// System.out.println("IN LAST ELSE LOOP:" + object[16].toString());
					sendingMailList.add(sendMail);

				} else {
					rw_user_id = Integer.parseInt(object[7].toString());

					sendMail.setClient_task_id(object[2].toString());
					sendMail.setEntity_name(object[6].toString());
					sendMail.setUnit_name(object[20].toString());
					sendMail.setFunction_name(object[13].toString());
					sendMail.setName_of_legislation(object[17].toString());
					sendMail.setName_of_rule(object[18].toString());
					sendMail.setWhen(object[16].toString());
					sendMail.setActivity(object[1].toString());
					sendMail.setFrequency(object[11].toString());
					sendMail.setImpact(object[14].toString());
					sendMail.setExecutor_date(object[10].toString());
					sendMail.setEvaluator_date(object[8].toString());
					sendMail.setFunc_head_date(object[12].toString());
					sendMail.setUnit_head_date(object[19].toString());
					sendMail.setLegal_due_date(object[15].toString());
					sendMail.setCompliance_activity(object[23].toString());
					sendMail.setSub_task_unit(object[24].toString());
					sendMail.setName_of_contractor(object[25].toString());
					sendMail.setCompliance_title(object[26].toString());
					// System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendMail.setExec_id(Integer.parseInt(object[9].toString()));
					sendMail.setEval_id(Integer.parseInt(object[7].toString()));
					sendMail.setSub_client_task_id(object[21].toString());
					// System.out.println("IN LAST ELSE LOOP:" + object[15].toString());
					sendingMailList.add(sendMail);

				}

			} // send upcoming task

			if (sendingMailList.size() > 0) {
				utilitiesService.sendEscalationMailsToEvaluatorForSubtask("Escalation Sub Task Alert To Evaluator",
						rw_user_id, sendingMailList);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all Escalation Tasks rest Call to function head
	@SuppressWarnings("unchecked")
	@Override
	public String getEscalationsToFunctionHead(String jsonString) {
		JSONObject objForSend = new JSONObject();
		int fh_user_id = 0;
		try {
			List<Object> tasksList = schedularDao.getEscalationsToFunctionHead();
			// System.out.println("tasksize:" + tasksList.size());

			ArrayList<SendMail> sendingMailList = new ArrayList<>();
			Iterator<Object> itr = tasksList.iterator();

			String created_at = sdfOut.format(new Date());
			// System.out.println("date:" + sdfOut.format(new Date()));

			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				User user = userDao.getUserById(Integer.parseInt(object[0].toString()));
				MainTaskEmailLog log = new MainTaskEmailLog();
				log.setClient_task_id(object[1].toString());
				log.setEntity_name(object[2].toString());
				log.setUnit_name(object[3].toString());
				log.setFunction_name(object[4].toString());
				log.setName_of_legislation(object[5].toString());
				log.setName_of_rule(object[6].toString());
				log.setLog_when(object[7].toString());
				log.setActivity(object[8].toString());
				log.setFrequency(object[9].toString());
				log.setImpact(object[10].toString());
				log.setExec_id(Integer.parseInt(object[19].toString()));
				log.setEval_id(Integer.parseInt(object[21].toString()));
				log.setFh_id(Integer.parseInt(object[0].toString()));
				log.setExecutor_date(sdfOut.format(sdfIn.parse(object[11].toString())));
				log.setEvaluator_date(sdfOut.format(sdfIn.parse(object[12].toString())));
				log.setFunc_head_date(sdfOut.format(sdfIn.parse(object[13].toString())));
				log.setUnit_head_date(sdfOut.format(sdfIn.parse(object[14].toString())));
				log.setLegal_due_date(sdfOut.format(sdfIn.parse(object[15].toString())));
				// System.out.println("legal date:" +
				// sdfOut.format(sdfIn.parse(object[15].toString())));
				log.setEmail_subject("Escalation Alert To Function Head");
				log.setEmail_to(user.getUser_email());
				log.setEmail_status("0");
				log.setEmail_sent_at(sdfOut.format(new Date()));

				log.setCreated_at(sdfOut.format(new Date()));
				schedularDao.saveEmailLog(log);

			}

			List<Object> mailList = schedularDao.getTodaysMainTask("Escalation Alert To Function Head", created_at);
			for (int i = 0; i < mailList.size(); i++) {
				SendMail sendMail = new SendMail();
				Object[] object = (Object[]) mailList.get(i);
				if (fh_user_id != Integer.parseInt(object[21].toString()) && fh_user_id != 0) {

					Thread.sleep(2000);
					utilitiesService.sendEscalationMailsToFunctionHead("Escalation Alert To Function Head", fh_user_id,
							sendingMailList);

					sendingMailList.clear();
					fh_user_id = Integer.parseInt(object[21].toString());

					sendMail.setClient_task_id(object[2].toString());
					sendMail.setEntity_name(object[6].toString());
					sendMail.setUnit_name(object[20].toString());
					sendMail.setFunction_name(object[13].toString());
					sendMail.setName_of_legislation(object[17].toString());
					sendMail.setName_of_rule(object[18].toString());
					sendMail.setWhen(object[16].toString());
					sendMail.setActivity(object[1].toString());
					sendMail.setFrequency(object[11].toString());
					sendMail.setImpact(object[14].toString());
					sendMail.setExecutor_date(object[10].toString());
					sendMail.setEvaluator_date(object[8].toString());
					sendMail.setFunc_head_date(object[12].toString());
					sendMail.setUnit_head_date(object[19].toString());
					sendMail.setLegal_due_date(object[15].toString());
					// System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendMail.setExec_id(Integer.parseInt(object[9].toString()));
					sendMail.setEval_id(Integer.parseInt(object[7].toString()));
					// System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendingMailList.add(sendMail);

				} else {
					fh_user_id = Integer.parseInt(object[21].toString());

					sendMail.setClient_task_id(object[2].toString());
					sendMail.setEntity_name(object[6].toString());
					sendMail.setUnit_name(object[20].toString());
					sendMail.setFunction_name(object[13].toString());
					sendMail.setName_of_legislation(object[17].toString());
					sendMail.setName_of_rule(object[18].toString());
					sendMail.setWhen(object[16].toString());
					sendMail.setActivity(object[1].toString());
					sendMail.setFrequency(object[11].toString());
					sendMail.setImpact(object[14].toString());
					sendMail.setExecutor_date(object[10].toString());
					sendMail.setEvaluator_date(object[8].toString());
					sendMail.setFunc_head_date(object[12].toString());
					sendMail.setUnit_head_date(object[19].toString());
					sendMail.setLegal_due_date(object[15].toString());
					// System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendMail.setExec_id(Integer.parseInt(object[9].toString()));
					sendMail.setEval_id(Integer.parseInt(object[7].toString()));
					// System.out.println("IN FIRST ELSE LOOP:" + object[15].toString());
					sendingMailList.add(sendMail);

				}
			}

			if (sendingMailList.size() > 0) {
				utilitiesService.sendEscalationMailsToFunctionHead("Escalation Alert To Function Head", fh_user_id,
						sendingMailList);
			}

			sendingMailList.clear();
			fh_user_id = 0;
			List<Object> subTaskList = schedularDao.getEscalationsToFuntionHeadSubTasks();
			// System.out.println("sub task size:" + subTaskList.size());
			Iterator<Object> itr1 = subTaskList.iterator();

			while (itr1.hasNext()) {
				Object[] object = (Object[]) itr1.next();
				User user = userDao.getUserById(Integer.parseInt(object[0].toString()));
				SubTaskEmailLog log = new SubTaskEmailLog();
				log.setClient_task_id(object[1].toString());
				log.setEntity_name(object[2].toString());
				log.setUnit_name(object[3].toString());
				log.setFunction_name(object[4].toString());
				log.setName_of_legislation(object[5].toString());
				log.setName_of_rule(object[6].toString());
				log.setLog_when(object[7].toString());
				log.setActivity(object[8].toString());
				log.setFrequency(object[9].toString());
				log.setImpact(object[10].toString());
				log.setExec_id(Integer.parseInt(object[19].toString()));
				log.setEval_id(Integer.parseInt(object[21].toString()));
				log.setFh_id(Integer.parseInt(object[0].toString()));
				log.setSub_client_task_id(object[18].toString());
				log.setExecutor_date(sdfOut.format(sdfIn.parse(object[11].toString())));
				log.setEvaluator_date(sdfOut.format(sdfIn.parse(object[12].toString())));
				log.setFunc_head_date(sdfOut.format(sdfIn.parse(object[13].toString())));
				log.setUnit_head_date(sdfOut.format(sdfIn.parse(object[14].toString())));
				log.setLegal_due_date(sdfOut.format(sdfIn.parse(object[15].toString())));
				// System.out.println("legal date:" +
				// sdfOut.format(sdfIn.parse(object[15].toString())));
				log.setEmail_subject("Escalation Sub Task Alert To Function Head");
				log.setEmail_to(user.getUser_email());
				log.setEmail_status("0");
				log.setEmail_sent_at(sdfOut.format(new Date()));
				log.setCompliance_activity(object[23].toString());
				log.setSub_task_unit(object[24].toString());
				log.setName_of_contractor(object[25].toString());
				log.setCompliance_title(object[26].toString());

				log.setCreated_at(sdfOut.format(new Date()));
				schedularDao.saveEmailLog(log);

			}

			List<Object> subTaskMailList = schedularDao.getTodaysSubTask("Escalation Sub Task Alert To Function Head",
					created_at);
			for (int i = 0; i < subTaskMailList.size(); i++) {
				SendMail sendMail = new SendMail();

				Object[] object = (Object[]) subTaskMailList.get(i);
				if (fh_user_id != Integer.parseInt(object[22].toString()) && fh_user_id != 0) {
					utilitiesService.sendEscalationMailsToFHForSubtask("Escalation Sub Task Alert To Function Head",
							fh_user_id, sendingMailList);

					sendingMailList.clear();
					fh_user_id = Integer.parseInt(object[22].toString());

					sendMail.setClient_task_id(object[2].toString());
					sendMail.setEntity_name(object[6].toString());
					sendMail.setUnit_name(object[20].toString());
					sendMail.setFunction_name(object[13].toString());
					sendMail.setName_of_legislation(object[17].toString());
					sendMail.setName_of_rule(object[18].toString());
					sendMail.setWhen(object[16].toString());
					sendMail.setActivity(object[1].toString());
					sendMail.setFrequency(object[11].toString());
					sendMail.setImpact(object[14].toString());
					sendMail.setExecutor_date(object[10].toString());
					sendMail.setEvaluator_date(object[8].toString());
					sendMail.setFunc_head_date(object[12].toString());
					sendMail.setUnit_head_date(object[19].toString());
					sendMail.setLegal_due_date(object[15].toString());
					sendMail.setCompliance_activity(object[23].toString());
					sendMail.setSub_task_unit(object[24].toString());
					sendMail.setName_of_contractor(object[25].toString());
					sendMail.setCompliance_title(object[26].toString());
					// System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendMail.setSub_client_task_id(object[21].toString());
					sendMail.setExec_id(Integer.parseInt(object[9].toString()));
					sendMail.setEval_id(Integer.parseInt(object[7].toString()));
					// System.out.println("IN LAST ELSE LOOP:" + object[15].toString());
					sendingMailList.add(sendMail);

				} else {
					fh_user_id = Integer.parseInt(object[22].toString());

					sendMail.setClient_task_id(object[2].toString());
					sendMail.setEntity_name(object[6].toString());
					sendMail.setUnit_name(object[20].toString());
					sendMail.setFunction_name(object[13].toString());
					sendMail.setName_of_legislation(object[17].toString());
					sendMail.setName_of_rule(object[18].toString());
					sendMail.setWhen(object[16].toString());
					sendMail.setActivity(object[1].toString());
					sendMail.setFrequency(object[11].toString());
					sendMail.setImpact(object[14].toString());
					sendMail.setExecutor_date(object[10].toString());
					sendMail.setEvaluator_date(object[8].toString());
					sendMail.setFunc_head_date(object[12].toString());
					sendMail.setUnit_head_date(object[19].toString());
					sendMail.setLegal_due_date(object[15].toString());
					sendMail.setCompliance_activity(object[23].toString());
					sendMail.setSub_task_unit(object[24].toString());
					sendMail.setName_of_contractor(object[25].toString());
					sendMail.setCompliance_title(object[26].toString());
					// System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendMail.setExec_id(Integer.parseInt(object[9].toString()));
					sendMail.setEval_id(Integer.parseInt(object[7].toString()));
					sendMail.setSub_client_task_id(object[21].toString());
					// System.out.println("IN LAST ELSE LOOP:" + object[15].toString());
					sendingMailList.add(sendMail);

				}

			} // send upcoming task

			if (sendingMailList.size() > 0) {
				utilitiesService.sendEscalationMailsToEvaluatorForSubtask("Escalation Sub Task Alert To Function Head",
						fh_user_id, sendingMailList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Harsahd Padole
	// Method Purpose: Get all Escalation Tasks to Unit head
	@SuppressWarnings("unchecked")
	@Override
	public String getExcalationsToUnitHead() {
		JSONObject objForSend = new JSONObject();
		try {
			ArrayList<SendMail> sendingMailList = new ArrayList<>();
			List<User> userList = schedularDao.getUserList(4); // 4 - Unit Head Role
			// System.out.println("userList.size(): " + userList.size());
			Iterator<User> itr_user = userList.iterator();
			String created_at = sdfOut.format(new Date());
			String user_email = " ";
			while (itr_user.hasNext()) {
				User user = itr_user.next();
				// System.out.println("username:" + user.getUser_username() + "user_id:" +
				// user.getUser_id());
				List<Object> taskList = schedularDao.getEscalationsToUnitHead(user.getUser_id());
				// System.out.println("size:" + taskList.size());
				// if(taskList.size()>0){

				Iterator<Object> tsk_itr = taskList.iterator();
				while (tsk_itr.hasNext()) {
					Object[] object = (Object[]) tsk_itr.next();
					// User user = UserDao.getUserById(Integer.parseInt(object[0].toString()));
					MainTaskEmailLog log = new MainTaskEmailLog();
					log.setClient_task_id(object[1].toString());
					log.setEntity_name(object[2].toString());
					log.setUnit_name(object[3].toString());
					log.setFunction_name(object[4].toString());
					log.setName_of_legislation(object[5].toString());
					log.setName_of_rule(object[6].toString());
					log.setLog_when(object[7].toString());
					log.setActivity(object[8].toString());
					log.setFrequency(object[9].toString());
					log.setImpact(object[10].toString());
					log.setExec_id(Integer.parseInt(object[21].toString()));
					log.setEval_id(Integer.parseInt(object[22].toString()));
					log.setFh_id(Integer.parseInt(object[23].toString()));
					log.setExecutor_date(sdfOut.format(sdfIn.parse(object[11].toString())));
					log.setEvaluator_date(sdfOut.format(sdfIn.parse(object[12].toString())));
					log.setFunc_head_date(sdfOut.format(sdfIn.parse(object[13].toString())));
					log.setUnit_head_date(sdfOut.format(sdfIn.parse(object[14].toString())));
					log.setLegal_due_date(sdfOut.format(sdfIn.parse(object[15].toString())));
					// System.out.println("legal date:" +
					// sdfOut.format(sdfIn.parse(object[15].toString())));
					log.setEmail_subject("Escalation Alert To Unit Head");
					log.setEmail_to(user.getUser_email());
					log.setEmail_status("0");
					log.setEmail_sent_at(sdfOut.format(new Date()));

					log.setCreated_at(sdfOut.format(new Date()));
					schedularDao.saveEmailLog(log);
				}
			}
			List<Object> mailList = schedularDao.getTodaysMainTask("Escalation Alert To Unit Head", created_at);
			for (int i = 0; i < mailList.size(); i++) {
				SendMail sendMail = new SendMail();
				Object[] object = (Object[]) mailList.get(i);
				String mail_id = object[5].toString();
				if (!mail_id.equals(user_email) && !user_email.equals(" ")) {
					// System.out.println("user_email:" +user_email);
					Thread.sleep(2000);
					utilitiesService.sendEscalationMailsUnitHead("Escalation Alert To Unit Head", user_email,
							sendingMailList);

					sendingMailList.clear();
					user_email = object[5].toString();

					sendMail.setClient_task_id(object[2].toString());
					sendMail.setEntity_name(object[6].toString());
					sendMail.setUnit_name(object[20].toString());
					sendMail.setFunction_name(object[13].toString());
					sendMail.setName_of_legislation(object[17].toString());
					sendMail.setName_of_rule(object[18].toString());
					sendMail.setWhen(object[16].toString());
					sendMail.setActivity(object[1].toString());
					sendMail.setFrequency(object[11].toString());
					sendMail.setImpact(object[14].toString());
					sendMail.setExecutor_date(object[10].toString());
					sendMail.setEvaluator_date(object[8].toString());
					sendMail.setFunc_head_date(object[12].toString());
					sendMail.setUnit_head_date(object[19].toString());
					sendMail.setLegal_due_date(object[15].toString());
					// System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendMail.setExec_id(Integer.parseInt(object[9].toString()));
					sendMail.setEval_id(Integer.parseInt(object[7].toString()));
					// System.out.println("IN FIRST IF LOOP:" + object[16].toString());
					sendingMailList.add(sendMail);

				} else {

					user_email = object[5].toString();
					sendMail.setClient_task_id(object[2].toString());
					sendMail.setEntity_name(object[6].toString());
					sendMail.setUnit_name(object[20].toString());
					sendMail.setFunction_name(object[13].toString());
					sendMail.setName_of_legislation(object[17].toString());
					sendMail.setName_of_rule(object[18].toString());
					sendMail.setWhen(object[16].toString());
					sendMail.setActivity(object[1].toString());
					sendMail.setFrequency(object[11].toString());
					sendMail.setImpact(object[14].toString());
					sendMail.setExecutor_date(object[10].toString());
					sendMail.setEvaluator_date(object[8].toString());
					sendMail.setFunc_head_date(object[12].toString());
					sendMail.setUnit_head_date(object[19].toString());
					sendMail.setLegal_due_date(object[15].toString());
					// System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendMail.setExec_id(Integer.parseInt(object[9].toString()));
					sendMail.setEval_id(Integer.parseInt(object[7].toString()));

					// System.out.println("IN FIRST IF LOOP:" + object[16].toString());
					sendingMailList.add(sendMail);

				}
			}

			if (sendingMailList.size() > 0) {
				utilitiesService.sendEscalationMailsUnitHead("Escalation Alert To Unit Head", user_email,
						sendingMailList);
			}

			sendingMailList.clear();

			while (itr_user.hasNext()) {
				User user = itr_user.next();
				List<Object> SubtaskList = schedularDao.getEscalationsToUnitHeadSubTasks(user.getUser_id());
				// System.out.println("size:" + SubtaskList.size());
				Iterator<Object> itr = SubtaskList.iterator();
				while (itr.hasNext()) {
					Object[] object = (Object[]) itr.next();
					// User user = UserDao.getUserById(Integer.parseInt(object[0].toString()));
					SubTaskEmailLog log = new SubTaskEmailLog();
					log.setClient_task_id(object[1].toString());
					log.setEntity_name(object[2].toString());
					log.setUnit_name(object[3].toString());
					log.setFunction_name(object[4].toString());
					log.setName_of_legislation(object[5].toString());
					log.setName_of_rule(object[6].toString());
					log.setLog_when(object[7].toString());
					log.setActivity(object[8].toString());
					log.setFrequency(object[9].toString());
					log.setImpact(object[10].toString());
					log.setExec_id(Integer.parseInt(object[19].toString()));
					log.setEval_id(Integer.parseInt(object[21].toString()));
					log.setFh_id(Integer.parseInt(object[22].toString()));
					log.setSub_client_task_id(object[23].toString());
					log.setExecutor_date(sdfOut.format(sdfIn.parse(object[11].toString())));
					log.setEvaluator_date(sdfOut.format(sdfIn.parse(object[12].toString())));
					log.setFunc_head_date(sdfOut.format(sdfIn.parse(object[13].toString())));
					log.setUnit_head_date(sdfOut.format(sdfIn.parse(object[14].toString())));
					log.setLegal_due_date(sdfOut.format(sdfIn.parse(object[15].toString())));
					// System.out.println("legal date:" +
					// sdfOut.format(sdfIn.parse(object[15].toString())));
					log.setEmail_subject("Subtask Escalation Alert To Unit Head");
					log.setEmail_to(user.getUser_email());
					log.setEmail_status("0");
					log.setEmail_sent_at(sdfOut.format(new Date()));
					log.setCompliance_activity(object[24].toString());
					log.setSub_task_unit(object[25].toString());
					log.setName_of_contractor(object[26].toString());
					log.setCompliance_title(object[27].toString());

					log.setCreated_at(sdfOut.format(new Date()));
					schedularDao.saveEmailLog(log);
				}
			}

			while (itr_user.hasNext()) {
				User user = itr_user.next();
				List<Object> subTaskMailList = schedularDao.getTodaysSubTask("Subtask Escalation Alert To Unit Head",
						created_at);
				for (int i = 0; i < subTaskMailList.size(); i++) {
					SendMail sendMail = new SendMail();

					Object[] object = (Object[]) subTaskMailList.get(i);

					sendMail.setClient_task_id(object[2].toString());
					sendMail.setEntity_name(object[6].toString());
					sendMail.setUnit_name(object[20].toString());
					sendMail.setFunction_name(object[13].toString());
					sendMail.setName_of_legislation(object[17].toString());
					sendMail.setName_of_rule(object[18].toString());
					sendMail.setWhen(object[16].toString());
					sendMail.setActivity(object[1].toString());
					sendMail.setFrequency(object[11].toString());
					sendMail.setImpact(object[14].toString());
					sendMail.setExecutor_date(object[10].toString());
					sendMail.setEvaluator_date(object[8].toString());
					sendMail.setFunc_head_date(object[12].toString());
					sendMail.setUnit_head_date(object[19].toString());
					sendMail.setLegal_due_date(object[15].toString());
					// System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendMail.setSub_client_task_id(object[21].toString());
					sendMail.setExec_id(Integer.parseInt(object[9].toString()));
					sendMail.setEval_id(Integer.parseInt(object[7].toString()));
					sendMail.setCompliance_activity(object[23].toString());
					sendMail.setSub_task_unit(object[24].toString());
					sendMail.setName_of_contractor(object[25].toString());
					sendMail.setCompliance_title(object[26].toString());
					// System.out.println("IN LAST ELSE LOOP:" + object[15].toString());
					sendingMailList.add(sendMail);

				}

				if (sendingMailList.size() > 0) {
					utilitiesService.sendEscalationAlertToUHForSubtask("Subtask Escalation Alert To Unit Head",
							user.getUser_id(), sendingMailList);
				}
				sendingMailList.clear();
			}
			objForSend.put("responseMessage", "Success");
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("responseMessage", "Failed");
			return objForSend.toJSONString();
		}
	}

	// Method Written By: Harsahd Padole
	// Method Purpose: Get all Escalation Tasks to Entity head
	@SuppressWarnings("unchecked")
	@Override
	public String getExcalationsToEntityHead() {

		JSONObject objForSend = new JSONObject();
		try {
			List<User> userList = schedularDao.getUserList(5); // 5 - Entity Head Role
			Iterator<User> itr_user = userList.iterator();
			String created_at = sdfOut.format(new Date());
			ArrayList<SendMail> sendingMailList = new ArrayList<>();
			String user_email = " ";

			while (itr_user.hasNext()) {
				User user = itr_user.next();
				List<Object> taskList = schedularDao.getEscalationsToEntityHead(user.getUser_id());
				// System.out.println("size:" + taskList.size());

				Iterator<Object> tsk_itr = taskList.iterator();
				while (tsk_itr.hasNext()) {
					Object[] object = (Object[]) tsk_itr.next();
					// User user = UserDao.getUserById(Integer.parseInt(object[0].toString()));
					MainTaskEmailLog log = new MainTaskEmailLog();
					log.setClient_task_id(object[1].toString());
					log.setEntity_name(object[2].toString());
					log.setUnit_name(object[3].toString());
					log.setFunction_name(object[4].toString());
					log.setName_of_legislation(object[5].toString());
					log.setName_of_rule(object[6].toString());
					log.setLog_when(object[7].toString());
					log.setActivity(object[8].toString());
					log.setFrequency(object[9].toString());
					log.setImpact(object[10].toString());
					log.setExec_id(Integer.parseInt(object[21].toString()));
					log.setEval_id(Integer.parseInt(object[22].toString()));
					log.setFh_id(Integer.parseInt(object[23].toString()));
					log.setExecutor_date(sdfOut.format(sdfIn.parse(object[11].toString())));
					log.setEvaluator_date(sdfOut.format(sdfIn.parse(object[12].toString())));
					log.setFunc_head_date(sdfOut.format(sdfIn.parse(object[13].toString())));
					log.setUnit_head_date(sdfOut.format(sdfIn.parse(object[14].toString())));
					log.setLegal_due_date(sdfOut.format(sdfIn.parse(object[15].toString())));
					// System.out.println("legal date:" +
					// sdfOut.format(sdfIn.parse(object[15].toString())));
					log.setEmail_subject("Escalation Alert To Entity Head");
					log.setEmail_to(user.getUser_email());
					log.setEmail_status("0");
					log.setEmail_sent_at(sdfOut.format(new Date()));

					log.setCreated_at(sdfOut.format(new Date()));
					schedularDao.saveEmailLog(log);
				}
			}

			List<Object> mailList = schedularDao.getTodaysMainTask("Escalation Alert To Entity Head", created_at);

			if (mailList.size() == 0) {
				System.out.println("NO RECORDS FOUND");
			}

			for (int i = 0; i < mailList.size(); i++) {

				SendMail sendMail = new SendMail();
				Object[] object = (Object[]) mailList.get(i);

				String mail_id = object[5].toString();
				if (!mail_id.equals(user_email) && !user_email.equals(" ")) {

					Thread.sleep(2000);
					utilitiesService.sendEscalationMailsEntityHead("Escalation Alert To Entity Head", user_email,
							sendingMailList);

					sendingMailList.clear();
					user_email = object[5].toString();

					sendMail.setClient_task_id(object[2].toString());
					sendMail.setEntity_name(object[6].toString());
					sendMail.setUnit_name(object[20].toString());
					sendMail.setFunction_name(object[13].toString());
					sendMail.setName_of_legislation(object[17].toString());
					sendMail.setName_of_rule(object[18].toString());
					sendMail.setWhen(object[16].toString());
					sendMail.setActivity(object[1].toString());
					sendMail.setFrequency(object[11].toString());
					sendMail.setImpact(object[14].toString());
					sendMail.setExecutor_date(object[10].toString());
					sendMail.setEvaluator_date(object[8].toString());
					sendMail.setFunc_head_date(object[12].toString());
					sendMail.setUnit_head_date(object[19].toString());
					sendMail.setLegal_due_date(object[15].toString());
					// System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendMail.setExec_id(Integer.parseInt(object[9].toString()));
					sendMail.setEval_id(Integer.parseInt(object[7].toString()));

					// System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendingMailList.add(sendMail);
				} else {
					user_email = object[5].toString();

					sendMail.setClient_task_id(object[2].toString());
					sendMail.setEntity_name(object[6].toString());
					sendMail.setUnit_name(object[20].toString());
					sendMail.setFunction_name(object[13].toString());
					sendMail.setName_of_legislation(object[17].toString());
					sendMail.setName_of_rule(object[18].toString());
					sendMail.setWhen(object[16].toString());
					sendMail.setActivity(object[1].toString());
					sendMail.setFrequency(object[11].toString());
					sendMail.setImpact(object[14].toString());
					sendMail.setExecutor_date(object[10].toString());
					sendMail.setEvaluator_date(object[8].toString());
					sendMail.setFunc_head_date(object[12].toString());
					sendMail.setUnit_head_date(object[19].toString());
					sendMail.setLegal_due_date(object[15].toString());
					// System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendMail.setExec_id(Integer.parseInt(object[9].toString()));
					sendMail.setEval_id(Integer.parseInt(object[7].toString()));

					// System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendingMailList.add(sendMail);
				}

			}

			if (sendingMailList.size() > 0) {
				utilitiesService.sendEscalationMailsEntityHead("Escalation Alert To Entity Head", user_email,
						sendingMailList);

			}
			sendingMailList.clear();
			while (itr_user.hasNext()) {
				User user = itr_user.next();
				List<Object> SubtaskList = schedularDao.getEscalationsToEntityHeadSubTasks(user.getUser_id());

				Iterator<Object> itr = SubtaskList.iterator();
				while (itr.hasNext()) {
					Object[] object = (Object[]) itr.next();
					SubTaskEmailLog log = new SubTaskEmailLog();
					log.setClient_task_id(object[1].toString());
					log.setEntity_name(object[2].toString());
					log.setUnit_name(object[3].toString());
					log.setFunction_name(object[4].toString());
					log.setName_of_legislation(object[5].toString());
					log.setName_of_rule(object[6].toString());
					log.setLog_when(object[7].toString());
					log.setActivity(object[8].toString());
					log.setFrequency(object[9].toString());
					log.setImpact(object[10].toString());
					log.setExec_id(Integer.parseInt(object[19].toString()));
					log.setEval_id(Integer.parseInt(object[21].toString()));
					log.setFh_id(Integer.parseInt(object[22].toString()));
					log.setSub_client_task_id(object[23].toString());
					log.setExecutor_date(sdfOut.format(sdfIn.parse(object[11].toString())));
					log.setEvaluator_date(sdfOut.format(sdfIn.parse(object[12].toString())));
					log.setFunc_head_date(sdfOut.format(sdfIn.parse(object[13].toString())));
					log.setUnit_head_date(sdfOut.format(sdfIn.parse(object[14].toString())));
					log.setLegal_due_date(sdfOut.format(sdfIn.parse(object[15].toString())));
					// System.out.println("legal date:" +
					// sdfOut.format(sdfIn.parse(object[15].toString())));
					log.setEmail_subject("Subtask Escalation Alert To Entity Head");
					log.setEmail_to(user.getUser_email());
					log.setEmail_status("0");
					log.setEmail_sent_at(sdfOut.format(new Date()));

					log.setCreated_at(sdfOut.format(new Date()));
					log.setCompliance_activity(object[24].toString());
					log.setSub_task_unit(object[25].toString());
					log.setName_of_contractor(object[26].toString());
					log.setCompliance_title(object[27].toString());

					schedularDao.saveEmailLog(log);
				}

				List<Object> subTaskMailList = schedularDao.getTodaysSubTask("Subtask Escalation Alert To Entity Head",
						created_at);
				if (SubtaskList.size() == 0) {
					System.out.println("NO RECORDS FOUND");
				}
				for (int i = 0; i < subTaskMailList.size(); i++) {
					SendMail sendMail = new SendMail();
					Object[] object = (Object[]) subTaskMailList.get(i);
					sendMail.setClient_task_id(object[2].toString());
					sendMail.setEntity_name(object[6].toString());
					sendMail.setUnit_name(object[20].toString());
					sendMail.setFunction_name(object[13].toString());
					sendMail.setName_of_legislation(object[17].toString());
					sendMail.setName_of_rule(object[18].toString());
					sendMail.setWhen(object[16].toString());
					sendMail.setActivity(object[1].toString());
					sendMail.setFrequency(object[11].toString());
					sendMail.setImpact(object[14].toString());
					sendMail.setExecutor_date(object[10].toString());
					sendMail.setEvaluator_date(object[8].toString());
					sendMail.setFunc_head_date(object[12].toString());
					sendMail.setUnit_head_date(object[19].toString());
					sendMail.setLegal_due_date(object[15].toString());
					// System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendMail.setSub_client_task_id(object[21].toString());
					sendMail.setExec_id(Integer.parseInt(object[9].toString()));
					sendMail.setEval_id(Integer.parseInt(object[7].toString()));
					sendMail.setCompliance_activity(object[23].toString());
					sendMail.setSub_task_unit(object[24].toString());
					sendMail.setName_of_contractor(object[25].toString());
					sendMail.setCompliance_title(object[26].toString());
					// System.out.println("IN LAST ELSE LOOP:" + object[15].toString());
					sendingMailList.add(sendMail);

				}

				if (sendingMailList.size() > 0) {
					utilitiesService.sendEscalationAlertToEHForSubtask("Subtask Escalation Alert To Entity Head",
							user.getUser_id(), sendingMailList);
					sendingMailList.clear();
				}
			}

			objForSend.put("responseMessage", "Success");
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("responseMessage", "Failed");
			return objForSend.toJSONString();
		}
	}

	// Method Written By: Harshad Padole
	// Method Purpose: auto activate tasks
	@Override
	public String autoActivateTask() {
		try {

			final String[] frequency = new String[] { "Weekly", "Fortnightly", "Monthly", "Two_Monthly", "Quarterly",
					"Four_Monthly", "Five_Monthly", "Half_Yearly", "Seven_Monthly", "Eight_Monthly", "Nine_Monthly",
					"Ten_Monthly", "Yearly", "Fourteen_Monthly", "Eighteen_Monthly", "Two_Yearly", "Three_Yearly",
					"Four_Yearly", "Five_Yearly", "Six_Yearly", "Seven_Yearly", "Eight_Yearly", "Nine_Yearly",
					"Ten_Yearly", "Twenty_Yearly" };

			Set<String> frequency_allowed = new HashSet<String>(Arrays.asList(frequency));

			// final String[] frequency_static = new String[]
			// {"One_Time","Ongoing","Event_Based","User_Defined"};
			// Set<String> frequency_not_allowed = new
			// HashSet<String>(Arrays.asList(frequency_static));

			List<Object> taskList = schedularDao.getTaskForAutoActivate();
			Iterator<Object> iterator = taskList.iterator();
			while (iterator.hasNext()) {
				Object[] objects = (Object[]) iterator.next();

				String clientTaskId = objects[0].toString();
				// String task_status = objects[1].toString();
				int pr_id = Integer.parseInt(objects[2].toString());
				String freq_operation = objects[3].toString();
				String freq_alerts = objects[4].toString();
				String impact = objects[5].toString();
				String imapct_orga = objects[6].toString();
				String imapct_unit = objects[7].toString();
				String allow_reopen = "0";
				String allow_back_date = "0";
				int no_of_days_allowed = 0;
				int alert_days = 0;
				String document = "0";
				String historical = "0";
				int prior_days_buffer = 0;
				int added_by = Integer.parseInt(objects[23].toString());

				Date auditDate = null;

				Date performer_date = null;
				Date reviewer_date = null;
				Date function_date = null;
				Date unit_date = null;
				Date leagl_date = null;
				Date first_alert_date = null;
				Date second_alert_date = null;
				Date third_alert_date = null;

				if (objects[8] != null)
					allow_reopen = objects[8].toString();

				if (objects[9] != null)
					allow_back_date = objects[9].toString();

				if (objects[10] != null)
					no_of_days_allowed = Integer.parseInt(objects[10].toString());

				if (objects[11] != null)
					alert_days = Integer.parseInt(objects[11].toString());

				if (objects[12] != null)
					document = objects[12].toString();

				if (objects[13] != null)
					historical = objects[13].toString();

				if (objects[14] != null)
					prior_days_buffer = Integer.parseInt(objects[14].toString());

				if (freq_operation.equals("Weekly") || freq_operation.equals("Fortnightly")) {

					int add_days = 0;

					if (freq_operation.equals("Weekly"))
						add_days = 7;

					if (freq_operation.equals("Fortnightly"))
						add_days = 14;

					if (objects[15] != null)
						performer_date = addDays(sdfIn.parse(objects[15].toString()), add_days);

					if (objects[16] != null)
						reviewer_date = addDays(sdfIn.parse(objects[16].toString()), add_days);

					if (objects[17] != null)
						function_date = addDays(sdfIn.parse(objects[17].toString()), add_days);

					if (objects[18] != null)
						unit_date = addDays(sdfIn.parse(objects[18].toString()), add_days);

					if (objects[19] != null)
						leagl_date = addDays(sdfIn.parse(objects[19].toString()), add_days);

					if (objects[25] != null) {
						auditDate = addDays(sdfIn.parse(objects[25].toString()), add_days);
					}

					if (objects[20] != null)
						first_alert_date = addDays(sdfIn.parse(objects[20].toString()), add_days);

					if (objects[21] != null)
						second_alert_date = addDays(sdfIn.parse(objects[21].toString()), add_days);

					if (objects[22] != null)
						third_alert_date = addDays(sdfIn.parse(objects[22].toString()), add_days);

				} // End add days IF

				/* Add Months if Start */
				if (freq_operation.equals("Half_Yearly") || freq_operation.equals("Quarterly")
						|| freq_operation.equals("Two_Monthly") || freq_operation.equals("Monthly")
						|| freq_operation.equals("Four_Monthly") || freq_operation.equals("Five_Monthly")
						|| freq_operation.equals("Seven_Monthly") || freq_operation.equals("Eight_Monthly")
						|| freq_operation.equals("Nine_Monthly") || freq_operation.equals("Ten_Monthly")
						|| freq_operation.equals("Fourteen_Monthly") || freq_operation.equals("Eighteen_Monthly")) {
					int add_months = 0;

					if (freq_operation.equals("Eighteen_Monthly"))
						add_months = 18;

					if (freq_operation.equals("Fourteen_Monthly"))
						add_months = 14;

					if (freq_operation.equals("Ten_Monthly"))
						add_months = 10;

					if (freq_operation.equals("Nine_Monthly"))
						add_months = 9;

					if (freq_operation.equals("Eight_Monthly"))
						add_months = 8;

					if (freq_operation.equals("Seven_Monthly"))
						add_months = 7;

					if (freq_operation.equals("Half_Yearly"))
						add_months = 6;

					if (freq_operation.equals("Five_Monthly"))
						add_months = 5;

					if (freq_operation.equals("Four_Monthly"))
						add_months = 4;

					if (freq_operation.equals("Quarterly"))
						add_months = 3;

					if (freq_operation.equals("Two_Monthly"))
						add_months = 2;

					if (freq_operation.equals("Monthly"))
						add_months = 1;

					if (objects[15] != null)
						performer_date = addMonths(sdfIn.parse(objects[15].toString()), add_months);

					if (objects[16] != null)
						reviewer_date = addMonths(sdfIn.parse(objects[16].toString()), add_months);

					if (objects[17] != null)
						function_date = addMonths(sdfIn.parse(objects[17].toString()), add_months);

					if (objects[18] != null)
						unit_date = addMonths(sdfIn.parse(objects[18].toString()), add_months);

					if (objects[19] != null)
						leagl_date = addMonths(sdfIn.parse(objects[19].toString()), add_months);

					if (objects[25] != null) {
						auditDate = addMonths(sdfIn.parse(objects[25].toString()), add_months);
					}

					if (objects[20] != null)
						first_alert_date = addMonths(sdfIn.parse(objects[20].toString()), add_months);

					if (objects[21] != null)
						second_alert_date = addMonths(sdfIn.parse(objects[21].toString()), add_months);

					if (objects[22] != null)
						third_alert_date = addMonths(sdfIn.parse(objects[22].toString()), add_months);

				} // End add month IF

				if (freq_operation.equals("Yearly") || freq_operation.equals("Two_Yearly")
						|| freq_operation.equals("Three_Yearly") || freq_operation.equals("Four_Yearly")
						|| freq_operation.equals("Five_Yearly") || freq_operation.equals("Ten_Yearly")
						|| freq_operation.equals("Twenty_Yearly")) {

					int add_year = 0;

					if (freq_operation.equals("Yearly"))
						add_year = 1;
					if (freq_operation.equals("Two_Yearly"))
						add_year = 2;
					if (freq_operation.equals("Three_Yearly"))
						add_year = 3;
					if (freq_operation.equals("Four_Yearly"))
						add_year = 4;
					if (freq_operation.equals("Five_Yearly"))
						add_year = 5;
					if (freq_operation.equals("Six_Yearly"))
						add_year = 6;
					if (freq_operation.equals("Seven_Yearly"))
						add_year = 7;
					if (freq_operation.equals("Eight_Yearly"))
						add_year = 8;
					if (freq_operation.equals("Nine_Yearly"))
						add_year = 9;
					if (freq_operation.equals("Ten_Yearly"))
						add_year = 10;
					if (freq_operation.equals("Twenty_Yearly"))
						add_year = 20;

					if (objects[15] != null)
						performer_date = addYears(sdfIn.parse(objects[15].toString()), add_year);

					if (objects[16] != null)
						reviewer_date = addYears(sdfIn.parse(objects[16].toString()), add_year);

					if (objects[17] != null)
						function_date = addYears(sdfIn.parse(objects[17].toString()), add_year);

					if (objects[18] != null)
						unit_date = addYears(sdfIn.parse(objects[18].toString()), add_year);

					if (objects[19] != null)
						leagl_date = addYears(sdfIn.parse(objects[19].toString()), add_year);

					if (objects[25] != null) {
						auditDate = addYears(sdfIn.parse(objects[25].toString()), add_year);
					}

					if (objects[20] != null)
						first_alert_date = addYears(sdfIn.parse(objects[20].toString()), add_year);

					if (objects[21] != null)
						second_alert_date = addYears(sdfIn.parse(objects[21].toString()), add_year);

					if (objects[22] != null)
						third_alert_date = addYears(sdfIn.parse(objects[22].toString()), add_year);
				}

				if (frequency_allowed.contains(freq_operation)) {

					TaskTransactional taskTransactional = new TaskTransactional();
					taskTransactional.setIsDocumentUpload(0);
					taskTransactional.setTtrn_activation_date(new Date());
					taskTransactional.setTtrn_added_by(added_by);
					taskTransactional.setTtrn_alert_days(alert_days);
					taskTransactional.setTtrn_allow_approver_reopening(allow_reopen);
					taskTransactional.setTtrn_allow_back_date_completion(allow_back_date);
					taskTransactional.setTtrn_client_task_id(clientTaskId);
					taskTransactional.setTtrn_completed_date(null);
					taskTransactional.setTtrn_created_at(new Date());
					taskTransactional.setTtrn_document(document);
					taskTransactional.setTtrn_fh_due_date(function_date);
					taskTransactional.setTtrn_first_alert(first_alert_date);
					taskTransactional.setTtrn_frequency_for_alerts(freq_alerts);
					taskTransactional.setTtrn_frequency_for_operation(freq_operation);
					taskTransactional.setTtrn_historical(historical);
					taskTransactional.setTtrn_impact(impact);
					taskTransactional.setTtrn_impact_on_organization(imapct_orga);
					taskTransactional.setTtrn_impact_on_unit(imapct_unit);
					taskTransactional.setTtrn_legal_due_date(leagl_date);
					taskTransactional.setTtrn_no_of_back_days_allowed(no_of_days_allowed);
					taskTransactional.setTtrn_performer_comments("");
					taskTransactional.setTtrn_performer_user_id(pr_id);
					taskTransactional.setTtrn_pr_due_date(performer_date);
					taskTransactional.setTtrn_prior_days_buffer(prior_days_buffer);
					taskTransactional.setTtrn_reason_for_non_compliance("");
					taskTransactional.setTtrn_rw_due_date(reviewer_date);
					taskTransactional.setTtrn_second_alert(second_alert_date);
					taskTransactional.setTtrn_status("Active");
					taskTransactional.setTtrn_submitted_date(null);
					taskTransactional.setTtrn_task_approved_by(0);
					taskTransactional.setTtrn_task_approved_date(new Date());
					taskTransactional.setTtrn_task_completed_by(0);
					taskTransactional.setTtrn_third_alert(third_alert_date);
					taskTransactional.setTtrn_uh_due_date(unit_date);
					taskTransactional.setTtrn_tasks_status("UpcomingTasks");

					/**
					 * For Audit
					 */

					taskTransactional.setIsAuditTasks(0);
					taskTransactional.setTtrn_is_Task_Audited("No");
					taskTransactional.setIsDocumentUpload(0);
					taskTransactional.setAuditDate(auditDate);

					tasksConfigurationDao.persist(taskTransactional);
					// System.out.println("Persist Client Task ID " + clientTaskId);

					LogReactivation log = new LogReactivation();
					log.setUserId(added_by);
					log.setTasksStatus("UpcomingTasks");
					log.setFrequency(freq_operation);
					log.setExeutorId(Integer.toString(pr_id));
					log.setTasksId(clientTaskId);
					log.setStatus(taskTransactional.getTtrn_status());
					log.setReactivationTime(new Date());
					cLogsDao.saveLogReActivation(log);
				}
			} // End main while
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Harshad Padole
	// Method Purpose: Add days in date
	@Override
	public Date addDays(Date date, int no_of_days) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DAY_OF_YEAR, no_of_days);
			return cal.getTime();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Harshad Padole
	// Method Purpose: Add months in date
	@Override
	public Date addMonths(Date date, int no_of_months) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MONTH, no_of_months);
			return cal.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Harshad Padole
	// Method Purpose: Add year in date
	@Override
	public Date addYears(Date date, int no_of_years) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.YEAR, no_of_years);
			return cal.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Harshad Padole
	// Method Purpose : Send reminder mail to responsible person
	@Override
	public String sendShowCauseNoticeReminder() {
		try {
			List<Object> getResposible = schedularDao.showCauseNoticeReminder();

			String resposibleEmail = "";
			String resposibleFname = "";
			String resposibleLname = "";
			String relatedTo = "";
			Date noticeDate = null;
			Date receivedDate = null;
			Date deadLineDate = null;
			String nextActionItem = "";
			String noticed = null;
			String receivedD = null;
			String deadLineD = null;
			if (getResposible != null) {
				Iterator<Object> iterator = getResposible.iterator();
				try {
					while (iterator.hasNext()) {
						Object[] objects = (Object[]) iterator.next();

						String id = objects[0].toString();
						resposibleEmail = objects[1].toString();
						resposibleFname = objects[2].toString();
						resposibleLname = objects[3].toString();
						relatedTo = objects[4].toString();
						noticeDate = sdfIn.parse(objects[5].toString());
						;
						noticed = sdfOut.format(noticeDate);

						receivedDate = sdfIn.parse(objects[6].toString());
						;
						receivedD = sdfOut.format(receivedDate);

						deadLineDate = sdfIn.parse(objects[7].toString());
						;
						deadLineD = sdfOut.format(deadLineDate);

						nextActionItem = objects[8].toString();

						// System.out.println("User : " + id +""+ resposibleEmail +""+resposibleMobile);

						/*--------------------------Code to send mail---------------------*/

						Properties props = new Properties();
						props.put("mail.smtp.auth", "true");
						// props.put("mail.smtp.starttls.enable", "true");
						props.put("mail.smtp.host", hostName);
						props.put("mail.smtp.port", portNo);
						Session session = Session.getInstance(props, new javax.mail.Authenticator() {
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(username, password);
							}
						});

						String email_body = "<div style='margin:0 auto;width:100%;height:auto;padding:16px;'>";
						email_body += "<p style='font-size:18px;'>Dear " + resposibleFname + "" + " " + ""
								+ resposibleLname + ",</p>";
						email_body += "<p style='text-align:justify;width:70%;'>Show Cause Notice has been entered related to "
								+ relatedTo + "  , where resposible person is  " + resposibleFname + "" + " " + ""
								+ resposibleLname + " and  notice date is " + noticed + ", notice was received on "
								+ receivedD + " , Last date for sending reply to the notice is " + deadLineD
								+ " and action item is " + nextActionItem + "  </p>";
						email_body += "<p>In case of any doubt or difficulty, please call Helpdesk as per details given on the support page.</p>";
						email_body += "<p ><span style='color:blue'>This is System Generated Mail.</span>&nbsp;<span>Please do not reply.</span></p>"
								+ "<h2 style='font-size:18px;font-weight:bold;'>Yours Sincerely</h2>"
								+ "<h2 style='font-size:19px;font-weight:bold;'>Team LexCare</h2>" + "</div>";
						Message message = new MimeMessage(session);
						message.setFrom(new InternetAddress(mailFrom));
						message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(resposibleEmail));
						message.setSubject("Show Cause Notice-Reminder");
						message.setContent(email_body, "text/html; charset=utf-8");
						Transport.send(message);
						// Add log
						utilitiesService.addMailToLog(resposibleEmail, "Show Cause Notice-Reminder Mail", id);

						// System.out.println("Mail Send on Reminder Date");
					}

				}

				catch (Exception e) {
					// throw new RuntimeException(e);
					e.printStackTrace();

				}

				/*----------------------Code to send mail ends here---------------*/
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Harshad Padole
	// Method Purpose : send one day before deadline
	@Override
	public String showCauseNoticeOneDayBeforeDeadline() {
		try {

			List<Object> getResposible = schedularDao.showCauseNoticeOneDayBeforeDeadline();

			String resposibleEmail = "";
			String resposibleFname = "";
			String resposibleLname = "";
			String relatedTo = "";
			Date noticeDate = null;
			Date receivedDate = null;
			Date deadLineDate = null;
			String nextActionItem = "";
			String noticed = null;
			String receivedD = null;
			String deadLineD = null;
			if (getResposible != null) {
				Iterator<Object> iterator = getResposible.iterator();
				try {
					while (iterator.hasNext()) {
						Object[] objects = (Object[]) iterator.next();

						String id = objects[0].toString();
						resposibleEmail = objects[1].toString();
						resposibleFname = objects[2].toString();
						resposibleLname = objects[3].toString();
						relatedTo = objects[4].toString();
						noticeDate = sdfIn.parse(objects[5].toString());
						;
						noticed = sdfOut.format(noticeDate);

						receivedDate = sdfIn.parse(objects[6].toString());
						;
						receivedD = sdfOut.format(receivedDate);

						deadLineDate = sdfIn.parse(objects[7].toString());
						;
						deadLineD = sdfOut.format(deadLineDate);

						nextActionItem = objects[8].toString();

						// System.out.println("User : " + id +""+ resposibleEmail +""+resposibleMobile);

						/*--------------------------Code to send mail---------------------*/

						Properties props = new Properties();
						props.put("mail.smtp.auth", "true");
						// props.put("mail.smtp.starttls.enable", "true");
						props.put("mail.smtp.host", hostName);
						props.put("mail.smtp.port", portNo);
						Session session = Session.getInstance(props, new javax.mail.Authenticator() {
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(username, password);
							}
						});

						String email_body = "<div style='margin:0 auto;width:100%;height:auto;padding:16px;'>";
						email_body += "<p style='font-size:18px;'>Dear " + resposibleFname + "" + " " + ""
								+ resposibleLname + ",</p>";
						email_body += "<p style='text-align:justify;width:70%;'>Show Cause Notice has been entered related to "
								+ relatedTo + "  , where resposible person is  " + resposibleFname + "" + " " + ""
								+ resposibleLname + " and  notice date is " + noticed + ", notice was received on "
								+ receivedD + " , Last date for sending reply to the notice is " + deadLineD
								+ " and action item is " + nextActionItem + "  </p>";
						email_body += "<p>In case of any doubt or difficulty, please call Helpdesk as per details given on the support page.</p>";
						email_body += "<p ><span style='color:blue'>This is System Generated Mail.</span>&nbsp;<span>Please do not reply.</span></p>"
								+ "<h2 style='font-size:18px;font-weight:bold;'>Yours Sincerely</h2>"
								+ "<h2 style='font-size:19px;font-weight:bold;'>Team LexCare</h2>" + "</div>";
						Message message = new MimeMessage(session);
						message.setFrom(new InternetAddress(mailFrom));
						message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(resposibleEmail));
						message.setSubject("Show Cause Notice-Reminder");
						message.setContent(email_body, "text/html; charset=utf-8");
						Transport.send(message);
						// Add log
						utilitiesService.addMailToLog(resposibleEmail, "Show Cause Notice-One day before deadline Mail",
								id);

						// System.out.println("Mail Send on on day before deadlene Date");
					}

				}

				catch (Exception e) {
					// throw new RuntimeException(e);
					e.printStackTrace();

				}

				/*----------------------Code to send mail ends here---------------*/
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Harshad Padole
	// Method Purpose : Send action item reminder
	@Override
	public String sendActionItemReminder() {
		try {
			List<Object> result = schedularDao.actionItemReminder();
			if (result != null) {
				Iterator<Object> iterator = result.iterator();
				while (iterator.hasNext()) {
					Object[] objects = (Object[]) iterator.next();

					String responsible_email = objects[1].toString();
					String responsible_name = objects[2].toString() + " " + objects[3].toString();
					String reporting_email = objects[4].toString();
					// String reporting_name = objects[5].toString()+" "+objects[6].toString();
					String id = objects[0].toString();
					/*--------------------------Code to send mail---------------------*/

					Properties props = new Properties();
					props.put("mail.smtp.auth", "true");
					// props.put("mail.smtp.starttls.enable", "true");
					props.put("mail.smtp.host", hostName);
					props.put("mail.smtp.port", portNo);
					Session session = Session.getInstance(props, new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(username, password);
						}
					});

					String email_body = "<div style='margin:0 auto;width:100%;height:auto;padding:16px;'>";
					email_body += "<p style='font-size:18px;'>Dear " + responsible_name + "</p>";
					email_body += "<p style='text-align:justify;width:70%;'>The action taken on action item is <b>"
							+ objects[7].toString() + "</b> related to <b>" + objects[10].toString()
							+ "</b>,where next action item is <b>" + objects[8].toString()
							+ "</b> and next due date for action item is <b>"
							+ sdfOut.format(sdfIn.parse(objects[9].toString())) + "</b></p>";
					email_body += "<p>In case of any doubt or difficulty, please call Helpdesk as per details given on the support page.</p>";
					email_body += "<p ><span style='color:blue'>This is System Generated Mail.</span>&nbsp;<span>Please do not reply.</span></p>"
							+ "<h2 style='font-size:18px;font-weight:bold;'>Yours Sincerely</h2>"
							+ "<h2 style='font-size:19px;font-weight:bold;'>Team LexCare</h2>" + "</div>";

					Message message = new MimeMessage(session);
					message.setFrom(new InternetAddress(mailFrom));

					message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(responsible_email));
					message.addRecipient(Message.RecipientType.CC, new InternetAddress(reporting_email));
					message.setSubject("Action item reminder alert");
					message.setContent(email_body, "text/html; charset=utf-8");
					Transport.send(message);
					// Add log
					String send_to = "Responsible email-" + responsible_email + " Reporting email-" + reporting_email;
					utilitiesService.addMailToLog(send_to, "Action Item Reminder", id);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created : Harshad Padole
	// Method Purpose : Auto activate sub task
	@Override
	public void subTaskAutoActivate(SubTaskTranscational subTaskTranscational, Date ttrn_next_examination_date) {
		try {

			SubTask subTask = tasksDao.getSubTaskDetailsBysub_task_id(subTaskTranscational.getTtrn_sub_task_id());

			String clientTaskId = subTaskTranscational.getTtrn_sub_client_task_id();
			// String task_status = objects[1].toString();
			// int pr_id = Integer.parseInt(objects[2].toString());
			String freq_operation = subTask.getSub_frequency();
			int no_of_days_allowed = 0;
			int alert_days = 0;
			int document = 0;
			int historical = 0;
			int prior_days_buffer = 0;

			Date performer_date = null;
			Date reviewer_date = null;
			Date function_date = null;
			Date unit_date = null;
			Date leagl_date = null;
			Date first_alert_date = null;
			Date second_alert_date = null;
			Date third_alert_date = null;

			no_of_days_allowed = subTaskTranscational.getTtrn_sub_task_back_date_allowed();

			alert_days = subTaskTranscational.getTtrn_sub_task_alert_prior_day();

			document = subTaskTranscational.getTtrn_sub_task_document();

			historical = subTaskTranscational.getTtrn_sub_task_historical();

			prior_days_buffer = subTaskTranscational.getTtrn_sub_task_buffer_days();

			if (freq_operation.equals("Weekly") || freq_operation.equals("Fortnightly")) {

				int add_days = 0;

				if (freq_operation.equals("Weekly"))
					add_days = 7;

				if (freq_operation.equals("Fortnightly"))
					add_days = 14;

				if (subTaskTranscational.getTtrn_sub_task_pr_due_date() != null)
					performer_date = addDays(
							sdfIn.parse(subTaskTranscational.getTtrn_sub_task_pr_due_date().toString()), add_days);

				if (subTaskTranscational.getTtrn_sub_task_rw_date() != null)
					reviewer_date = addDays(sdfIn.parse(subTaskTranscational.getTtrn_sub_task_rw_date().toString()),
							add_days);

				if (subTaskTranscational.getTtrn_sub_task_FH_due_date() != null)
					function_date = addDays(sdfIn.parse(subTaskTranscational.getTtrn_sub_task_FH_due_date().toString()),
							add_days);

				if (subTaskTranscational.getTtrn_sub_task_UH_due_date() != null)
					unit_date = addDays(sdfIn.parse(subTaskTranscational.getTtrn_sub_task_UH_due_date().toString()),
							add_days);

				if (subTaskTranscational.getTtrn_sub_task_ENT_due_date() != null)
					leagl_date = addDays(sdfIn.parse(subTaskTranscational.getTtrn_sub_task_ENT_due_date().toString()),
							add_days);

				if (subTaskTranscational.getTtrn_sub_task_first_alert() != null)
					first_alert_date = addDays(
							sdfIn.parse(subTaskTranscational.getTtrn_sub_task_first_alert().toString()), add_days);

				if (subTaskTranscational.getTtrn_sub_task_second_alert() != null)
					second_alert_date = addDays(
							sdfIn.parse(subTaskTranscational.getTtrn_sub_task_second_alert().toString()), add_days);

				if (subTaskTranscational.getTtrn_sub_task_third_alert() != null)
					third_alert_date = addDays(
							sdfIn.parse(subTaskTranscational.getTtrn_sub_task_third_alert().toString()), add_days);

			} // End add days IF

			/* Add Months if Start */
			if (freq_operation.equals("Half_Yearly") || freq_operation.equals("Quarterly")
					|| freq_operation.equals("Two_Monthly") || freq_operation.equals("Monthly")) {
				int add_months = 0;

				if (freq_operation.equals("Half_Yearly"))
					add_months = 6;

				if (freq_operation.equals("Quarterly"))
					add_months = 3;

				if (freq_operation.equals("Two_Monthly"))
					add_months = 2;

				if (freq_operation.equals("Monthly"))
					add_months = 1;

				if (subTaskTranscational.getTtrn_sub_task_pr_due_date() != null)
					performer_date = addMonths(
							sdfIn.parse(subTaskTranscational.getTtrn_sub_task_pr_due_date().toString()), add_months);

				if (subTaskTranscational.getTtrn_sub_task_rw_date() != null)
					reviewer_date = addMonths(sdfIn.parse(subTaskTranscational.getTtrn_sub_task_rw_date().toString()),
							add_months);

				if (subTaskTranscational.getTtrn_sub_task_FH_due_date() != null)
					function_date = addMonths(
							sdfIn.parse(subTaskTranscational.getTtrn_sub_task_FH_due_date().toString()), add_months);

				if (subTaskTranscational.getTtrn_sub_task_UH_due_date() != null)
					unit_date = addMonths(sdfIn.parse(subTaskTranscational.getTtrn_sub_task_UH_due_date().toString()),
							add_months);

				if (subTaskTranscational.getTtrn_sub_task_ENT_due_date() != null)
					leagl_date = addMonths(sdfIn.parse(subTaskTranscational.getTtrn_sub_task_ENT_due_date().toString()),
							add_months);

				if (subTaskTranscational.getTtrn_sub_task_first_alert() != null)
					first_alert_date = addMonths(
							sdfIn.parse(subTaskTranscational.getTtrn_sub_task_first_alert().toString()), add_months);

				if (subTaskTranscational.getTtrn_sub_task_second_alert() != null)
					second_alert_date = addMonths(
							sdfIn.parse(subTaskTranscational.getTtrn_sub_task_second_alert().toString()), add_months);

				if (subTaskTranscational.getTtrn_sub_task_third_alert() != null)
					third_alert_date = addMonths(
							sdfIn.parse(subTaskTranscational.getTtrn_sub_task_third_alert().toString()), add_months);

			} // End add month IF

			if (freq_operation.equals("Yearly") || freq_operation.equals("Two_yearly")
					|| freq_operation.equals("Three_yearly") || freq_operation.equals("Four_yearly")
					|| freq_operation.equals("Five_Yearly") || freq_operation.equals("Ten_Yearly")
					|| freq_operation.equals("Twenty_Yearly")) {

				int add_year = 0;

				if (freq_operation.equals("Yearly"))
					add_year = 1;
				if (freq_operation.equals("Two_yearly") || freq_operation.equals("Two_Yearly"))
					add_year = 2;
				if (freq_operation.equals("Three_yearly") || freq_operation.equals("Three_Yearly"))
					add_year = 3;
				if (freq_operation.equals("Four_yearly") || freq_operation.equals("Four_Yearly"))
					add_year = 4;
				if (freq_operation.equals("Five_Yearly"))
					add_year = 5;
				if (freq_operation.equals("Ten_Yearly"))
					add_year = 10;
				if (freq_operation.equals("Twenty_Yearly"))
					add_year = 20;

				if (subTaskTranscational.getTtrn_sub_task_pr_due_date() != null)
					performer_date = addYears(
							sdfIn.parse(subTaskTranscational.getTtrn_sub_task_pr_due_date().toString()), add_year);

				if (subTaskTranscational.getTtrn_sub_task_rw_date() != null)
					reviewer_date = addYears(sdfIn.parse(subTaskTranscational.getTtrn_sub_task_rw_date().toString()),
							add_year);

				if (subTaskTranscational.getTtrn_sub_task_FH_due_date() != null)
					function_date = addYears(
							sdfIn.parse(subTaskTranscational.getTtrn_sub_task_FH_due_date().toString()), add_year);

				if (subTaskTranscational.getTtrn_sub_task_UH_due_date() != null)
					unit_date = addYears(sdfIn.parse(subTaskTranscational.getTtrn_sub_task_UH_due_date().toString()),
							add_year);

				if (subTaskTranscational.getTtrn_sub_task_ENT_due_date() != null)
					leagl_date = addYears(sdfIn.parse(subTaskTranscational.getTtrn_sub_task_ENT_due_date().toString()),
							add_year);

				if (subTaskTranscational.getTtrn_sub_task_first_alert() != null)
					first_alert_date = addYears(
							sdfIn.parse(subTaskTranscational.getTtrn_sub_task_first_alert().toString()), add_year);

				if (subTaskTranscational.getTtrn_sub_task_second_alert() != null)
					second_alert_date = addYears(
							sdfIn.parse(subTaskTranscational.getTtrn_sub_task_second_alert().toString()), add_year);

				if (subTaskTranscational.getTtrn_sub_task_third_alert() != null)
					third_alert_date = addYears(
							sdfIn.parse(subTaskTranscational.getTtrn_sub_task_third_alert().toString()), add_year);

			}

			SubTaskTranscational newSubTaskTransactional = new SubTaskTranscational();
			newSubTaskTransactional.setTtrn_sub_client_task_id(clientTaskId);
			newSubTaskTransactional.setTtrn_sub_task_id(subTaskTranscational.getTtrn_sub_task_id());
			newSubTaskTransactional.setTtrn_sub_task_created_at(new Date());
			newSubTaskTransactional.setTtrn_sub_task_pr_due_date(performer_date);
			newSubTaskTransactional.setTtrn_sub_task_rw_date(reviewer_date);
			newSubTaskTransactional.setTtrn_sub_task_FH_due_date(function_date);
			newSubTaskTransactional.setTtrn_sub_task_UH_due_date(unit_date);
			newSubTaskTransactional.setTtrn_sub_task_ENT_due_date(leagl_date);
			newSubTaskTransactional.setTtrn_sub_task_first_alert(first_alert_date);
			newSubTaskTransactional.setTtrn_sub_task_second_alert(second_alert_date);
			newSubTaskTransactional.setTtrn_sub_task_third_alert(third_alert_date);
			newSubTaskTransactional.setTttn_sub_task_next_examination_date(ttrn_next_examination_date);
			newSubTaskTransactional.setTtrn_sub_task_activation_date(new Date());
			newSubTaskTransactional.setTtrn_sub_task_alert_prior_day(alert_days);
			newSubTaskTransactional.setTtrn_sub_task_buffer_days(prior_days_buffer);
			newSubTaskTransactional.setTtrn_sub_task_back_date_allowed(no_of_days_allowed);
			newSubTaskTransactional.setTtrn_sub_task_document(document);
			newSubTaskTransactional.setTtrn_sub_task_historical(historical);
			newSubTaskTransactional.setTtrn_sub_task_status("Active");
			newSubTaskTransactional.setTtrn_sub_task_updated_at(new Date());

			subTaskDao.saveObject(newSubTaskTransactional);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendEscalationsToFuntionHead() {
		try {
			// Send mail to function head

			List<Object> id_list_main = schedularDao.getFunctionHead("Main");
			if (id_list_main != null) {
				for (int i = 0; i < id_list_main.size(); i++) {
					// System.out.println("FH ID "+id_list_main.get(i));

					String ClientTasksList = "(";
					String fun_email = "";

					List<Object> list = schedularDao
							.getDetailsToSendFH(Integer.parseInt(id_list_main.get(i).toString()), "Main");
					Iterator<Object> iterator = list.iterator();

					String email_body = "";

					email_body += "<div style='margin:0 auto;width:100%;height:auto;padding:20px;'>";
					email_body += "<h2 style='font-size:18px;'>Dear Function Head,</h2><br>";
					email_body += "<p style='text-align:justify;width:70%;'>The following compliance activities are not attended and legal due date is approaching. </p>"
							+ "<p style='text-align:justify;width:70%;'>Kindly look into this on priority and ensure the completion of compliance activities. Please follow the link by clicking on the Task ID of respected tasks.</p>"
							+ "<h2 style='font-size:16px;font-weight:bold;'>Not Attended Activities :</h2>";
					email_body += "<table style='width:80%;font-size: 16px' border='1'>" + "<thead>"
							+ "<tr style='background:#0B6EC3;color:#fff;'>" + "<th>SN</th>" + "<th>Executor</th>"
							+ "<th>Task ID</th>" + "<th>Entity</th>" + "<th>Location</th>" + "<th>Function</th>"
							+ "<th>Name of Legislation</th>" + "<th>Name of Rule</th>" + "<th>When</th>"
							+ "<th>Activity</th>" + "<th>Frequency</th>" + "<th>Impact</th>"
							+ "<th>Executor Due Date.</th>" + "<th>Evaluator Due Date.</th>"
							+ "<th>Function Head Due Date.</th>" + "<th>Legal Due Date</th>" + "</tr>" + "</thead>"
							+ "<tbody>";
					int SN = 1;
					while (iterator.hasNext()) {
						Object[] objects = (Object[]) iterator.next();

						Date legalDueDate;
						Date perDueDate;
						Date revDueDate;
						Date fhDueDate;
						try {
							legalDueDate = sdfIn.parse(objects[7].toString());
							objects[7] = sdfOut.format(legalDueDate);

							fhDueDate = sdfIn.parse(objects[8].toString());
							objects[8] = sdfOut.format(fhDueDate);

							revDueDate = sdfIn.parse(objects[9].toString());
							objects[9] = sdfOut.format(revDueDate);

							perDueDate = sdfIn.parse(objects[10].toString());
							objects[10] = sdfOut.format(perDueDate);

						} catch (ParseException e) {
							e.printStackTrace();
						}

						String task_id = objects[1].toString();
						String loca_name = objects[2].toString();
						String legi_name = objects[3].toString();
						String rule_name = objects[4].toString();
						String activity = objects[5].toString();
						String impact = objects[6].toString();
						String legal_date = objects[7].toString();
						String fhead_date = objects[8].toString();
						String rev_date = objects[9].toString();
						String per_date = objects[10].toString();
						fun_email = objects[16].toString();
						// String per_email = objects[17].toString();
						// String rev_email = objects[18].toString();
						String fun_name = objects[19].toString();
						String when = objects[20].toString();
						String Frequency = objects[21].toString();
						String entity = objects[22].toString();
						String per_name = objects[23].toString() + " " + objects[24].toString();

						if (SN < list.size()) {
							ClientTasksList += task_id + ",";
						} else {
							ClientTasksList += task_id + ")";
						}

						email_body += "<tr>" + "<td>" + SN + "</td>" + "<td>" + per_name + "</td>" + "<td><a href="
								+ url + ">" + task_id + "</a></td>" + "<td>" + entity + "</td>" + "<td>" + loca_name
								+ "</td>" + "<td>" + fun_name + "</td>" + "<td>" + legi_name + "</td>" + "<td>"
								+ rule_name + "</td>" + "<td>" + when + "</td>" + "<td>" + activity + "</td>" + "<td>"
								+ Frequency + "</td>" + "<td>" + impact + "</td>" + "<td>" + per_date + "</td>" + "<td>"
								+ rev_date + "</td>" + "<td>" + fhead_date + "</td>" + "<td>" + legal_date + "</td>"
								+ "</tr>";

						SN++;
					}

					email_body += "</tbody>" + "</table>";
					email_body += "<p>This is a system generated mail. Please do not reply to this mail.<br/> In case of any doubt or difficulty, please call Helpdesk as per details given on the support page.</p>"
							+ "<h2 style='font-size:18px;font-weight:bold;'>Yours Sincerely</h2>"
							+ "<h2 style='font-size:19px;font-weight:bold;'>Team LexCare</h2>" + "</div>";

					/*--------------------------Code to send mail---------------------*/
					utilitiesService.sendMail(fun_email, "Excalation Alerts", email_body, ClientTasksList);
					/*----------------------Code to send mail ends here---------------*/

				}
			} // Main task id end

			List<Object> id_list_sub = schedularDao.getFunctionHead("Sub");
			if (id_list_sub != null) {
				for (int i = 0; i < id_list_sub.size(); i++) {

					String ClientTasksList = "(";
					String fun_email = "";

					List<Object> list = schedularDao.getDetailsToSendFH(Integer.parseInt(id_list_sub.get(i).toString()),
							"Sub");
					Iterator<Object> iterator = list.iterator();

					String email_body = "";

					email_body += "<div style='margin:0 auto;width:100%;height:auto;padding:20px;'>";
					email_body += "<h2 style='font-size:18px;'>Dear Function Head,</h2><br>";
					email_body += "<p style='text-align:justify;width:70%;'>The following compliance activities are not attended and legal due date is approaching. </p>"
							+ "<p style='text-align:justify;width:70%;'>Kindly look into this on priority and ensure the completion of compliance activities. Please follow the link by clicking on the Task ID of respected tasks.</p>"
							+ "<h2 style='font-size:16px;font-weight:bold;'>Not Attended Activities :</h2>";
					email_body += "<table style='width:80%;font-size: 16px' border='1'>" + "<thead>"
							+ "<tr style='background:#0B6EC3;color:#fff;'>" + "<th>SN</th>" + "<th>Executor</th>"
							+ "<th>Task ID</th>" + "<th>Entity</th>" + "<th>Location</th>" + "<th>Function</th>"
							+ "<th>Name of Legislation</th>" + "<th>Name of Rule</th>" + "<th>When</th>"
							+ "<th>Activity</th>" + "<th>Frequency</th>" + "<th>Impact</th>"
							+ "<th>Executor Due Date.</th>" + "<th>Evaluator Due Date.</th>"
							+ "<th>Function Head Due Date.</th>" + "<th>Legal Due Date</th>" + "</tr>" + "</thead>"
							+ "<tbody>";
					int SN = 1;
					while (iterator.hasNext()) {
						Object[] objects = (Object[]) iterator.next();

						Date legalDueDate;
						Date perDueDate;
						Date revDueDate;
						Date fhDueDate;
						try {
							legalDueDate = sdfIn.parse(objects[7].toString());
							objects[7] = sdfOut.format(legalDueDate);

							fhDueDate = sdfIn.parse(objects[8].toString());
							objects[8] = sdfOut.format(fhDueDate);

							revDueDate = sdfIn.parse(objects[9].toString());
							objects[9] = sdfOut.format(revDueDate);

							perDueDate = sdfIn.parse(objects[10].toString());
							objects[10] = sdfOut.format(perDueDate);

						} catch (ParseException e) {
							e.printStackTrace();
						}

						String task_id = objects[1].toString();
						String loca_name = objects[2].toString();
						String legi_name = objects[3].toString();
						String rule_name = objects[4].toString();
						String activity = objects[5].toString();
						String impact = objects[6].toString();
						String legal_date = objects[7].toString();
						String fhead_date = objects[8].toString();
						String rev_date = objects[9].toString();
						String per_date = objects[10].toString();
						fun_email = objects[16].toString();
						// String per_email = objects[17].toString();
						// String rev_email = objects[18].toString();
						String fun_name = objects[19].toString();
						String when = objects[20].toString();
						String Frequency = objects[21].toString();
						String entity = objects[22].toString();
						String per_name = objects[23].toString() + " " + objects[24].toString();

						if (SN < list.size()) {
							ClientTasksList += task_id + ",";
						} else {
							ClientTasksList += task_id + ")";
						}

						email_body += "<tr>" + "<td>" + SN + "</td>" + "<td>" + per_name + "</td>" + "<td><a href="
								+ url + ">" + task_id + "</a></td>" + "<td>" + entity + "</td>" + "<td>" + loca_name
								+ "</td>" + "<td>" + fun_name + "</td>" + "<td>" + legi_name + "</td>" + "<td>"
								+ rule_name + "</td>" + "<td>" + when + "</td>" + "<td>" + activity + "</td>" + "<td>"
								+ Frequency + "</td>" + "<td>" + impact + "</td>" + "<td>" + per_date + "</td>" + "<td>"
								+ rev_date + "</td>" + "<td>" + fhead_date + "</td>" + "<td>" + legal_date + "</td>"
								+ "</tr>";

						SN++;
					}

					email_body += "</tbody>" + "</table>";
					email_body += "<p>This is a system generated mail. Please do not reply to this mail.<br/> In case of any doubt or difficulty, please call Helpdesk as per details given on the support page.</p>"
							+ "<h2 style='font-size:18px;font-weight:bold;'>Yours Sincerely</h2>"
							+ "<h2 style='font-size:19px;font-weight:bold;'>Team LexCare</h2>" + "</div>";

					/*--------------------------Code to send mail---------------------*/
					utilitiesService.sendMail(fun_email, "Excalation Alerts", email_body, ClientTasksList);
					/*----------------------Code to send mail ends here---------------*/

				}
			} // Sub task id end

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String sendLogToAdmin() {
		try {

			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			// props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", hostName);
			props.put("mail.smtp.port", portNo);
			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

			String email_body = "Please find attached activity log\n";

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailFrom));

			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("support@lexcareglobal.com"));
			message.addRecipient(Message.RecipientType.CC, new InternetAddress("upadhyay.navdeep@lexcareglobal.com"));
			message.setSubject("Email Logs for " + projectName);
			message.setContent(email_body, "text/html; charset=utf-8");

			// Part two is attachment
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			String filename = "C:/CMS/" + projectName + "/EmailLogs/logs.txt";
			DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			multipart.addBodyPart(messageBodyPart);

			// Send the complete message parts
			message.setContent(multipart);

			Transport.send(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String sendUpcomingNonDeliveredMails(String jsonString) {
		try {
			String created_at = sdfOut.format(new Date());
			List<Object> mailList = schedularDao.getNonDeliveredMails("Upcoming Task Alert", created_at);
			int pr_user_id = 0;
			ArrayList<SendMail> sendingMailList = new ArrayList<>();
			if (mailList.size() == 0 || mailList != null) {
				sendingMailList.clear();
			}
			for (int a = 0; a < mailList.size(); a++) {
				SendMail sendMail = new SendMail();
				Object[] object = (Object[]) mailList.get(a);
				if (pr_user_id != Integer.parseInt(object[9].toString()) && pr_user_id != 0) {

					Thread.sleep(2000);
					utilitiesService.sendUpcomingMailsToExecutor("Upcoming Task Alert", pr_user_id, sendingMailList);

					sendingMailList.clear();
					pr_user_id = Integer.parseInt(object[9].toString());

					sendMail.setClient_task_id(object[2].toString());
					sendMail.setEntity_name(object[6].toString());
					sendMail.setUnit_name(object[20].toString());
					sendMail.setFunction_name(object[13].toString());
					sendMail.setName_of_legislation(object[17].toString());
					sendMail.setName_of_rule(object[18].toString());
					sendMail.setWhen(object[16].toString());
					sendMail.setActivity(object[1].toString());
					sendMail.setFrequency(object[11].toString());
					sendMail.setImpact(object[14].toString());
					sendMail.setExecutor_date(object[10].toString());
					sendMail.setEvaluator_date(object[8].toString());
					sendMail.setFunc_head_date(object[12].toString());
					sendMail.setUnit_head_date(object[19].toString());
					sendMail.setLegal_due_date(object[15].toString());
					// System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendMail.setExec_id(Integer.parseInt(object[9].toString()));
					sendMail.setEval_id(Integer.parseInt(object[7].toString()));
					// System.out.println("IN LAST IF LOOP:" + object[15].toString());
					sendingMailList.add(sendMail);
				} else {
					pr_user_id = Integer.parseInt(object[9].toString());

					sendMail.setClient_task_id(object[2].toString());
					sendMail.setEntity_name(object[6].toString());
					sendMail.setUnit_name(object[20].toString());
					sendMail.setFunction_name(object[13].toString());
					sendMail.setName_of_legislation(object[17].toString());
					sendMail.setName_of_rule(object[18].toString());
					sendMail.setWhen(object[16].toString());
					sendMail.setActivity(object[1].toString());
					sendMail.setFrequency(object[11].toString());
					sendMail.setImpact(object[14].toString());
					sendMail.setExecutor_date(object[10].toString());
					sendMail.setEvaluator_date(object[8].toString());
					sendMail.setFunc_head_date(object[12].toString());
					sendMail.setUnit_head_date(object[19].toString());
					sendMail.setLegal_due_date(object[15].toString());
					// System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendMail.setExec_id(Integer.parseInt(object[9].toString()));
					sendMail.setEval_id(Integer.parseInt(object[7].toString()));
					// System.out.println("IN LAST ELSE LOOP:" + object[15].toString());
					sendingMailList.add(sendMail);
				}
			}

			utilitiesService.sendUpcomingMailsToExecutor("Upcoming Task Alert", pr_user_id, sendingMailList);

			/* For Sutask */
			sendingMailList.clear();
			List<Object> subtaskMailList = schedularDao.getNonDeliveredMailsOfSubtask("Upcoming Task Alert",
					created_at);
			if (subtaskMailList.size() == 0) {
				sendingMailList.clear();
			}
			for (int a = 0; a < subtaskMailList.size(); a++) {
				SendMail sendMail = new SendMail();
				Object[] object = (Object[]) subtaskMailList.get(a);
				if (pr_user_id != Integer.parseInt(object[9].toString()) && pr_user_id != 0) {

					utilitiesService.sendUpcomingMailsToExecutorForSubtask("Upcoming Sub Task Alert", pr_user_id,
							sendingMailList);

					sendingMailList.clear();
					pr_user_id = Integer.parseInt(object[9].toString());

					sendMail.setClient_task_id(object[2].toString());
					sendMail.setEntity_name(object[6].toString());
					sendMail.setUnit_name(object[20].toString());
					sendMail.setFunction_name(object[13].toString());
					sendMail.setName_of_legislation(object[17].toString());
					sendMail.setName_of_rule(object[18].toString());
					sendMail.setWhen(object[16].toString());
					sendMail.setActivity(object[1].toString());
					sendMail.setFrequency(object[11].toString());
					sendMail.setImpact(object[14].toString());
					sendMail.setExecutor_date(object[10].toString());
					sendMail.setEvaluator_date(object[8].toString());
					sendMail.setFunc_head_date(object[12].toString());
					sendMail.setUnit_head_date(object[19].toString());
					sendMail.setLegal_due_date(object[15].toString());
					// System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendMail.setExec_id(Integer.parseInt(object[9].toString()));
					sendMail.setEval_id(Integer.parseInt(object[7].toString()));
					sendMail.setSub_client_task_id(object[21].toString());
					// System.out.println("IN LAST IF LOOP:" + object[15].toString());
					sendingMailList.add(sendMail);
				} else {
					pr_user_id = Integer.parseInt(object[9].toString());

					sendMail.setClient_task_id(object[2].toString());
					sendMail.setEntity_name(object[6].toString());
					sendMail.setUnit_name(object[20].toString());
					sendMail.setFunction_name(object[13].toString());
					sendMail.setName_of_legislation(object[17].toString());
					sendMail.setName_of_rule(object[18].toString());
					sendMail.setWhen(object[16].toString());
					sendMail.setActivity(object[1].toString());
					sendMail.setFrequency(object[11].toString());
					sendMail.setImpact(object[14].toString());
					sendMail.setExecutor_date(object[10].toString());
					sendMail.setEvaluator_date(object[8].toString());
					sendMail.setFunc_head_date(object[12].toString());
					sendMail.setUnit_head_date(object[19].toString());
					sendMail.setLegal_due_date(object[15].toString());
					System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendMail.setExec_id(Integer.parseInt(object[9].toString()));
					sendMail.setEval_id(Integer.parseInt(object[7].toString()));
					sendMail.setSub_client_task_id(object[21].toString());
					System.out.println("IN LAST ELSE LOOP:" + object[15].toString());
					sendingMailList.add(sendMail);
				}
			}

			utilitiesService.sendUpcomingMailsToExecutorForSubtask("Upcoming Sub Task Alert", pr_user_id,
					sendingMailList);
			sendingMailList.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public void autoActivateSubTask() {
		try {

			final String[] frequency = new String[] { "Weekly", "Fortnightly", "Monthly", "Two_Monthly", "Quarterly",
					"Four_Monthly", "Five_Monthly", "Half_Yearly", "Seven_Monthly", "Eight_Monthly", "Nine_Monthly",
					"Ten_Monthly", "Yearly", "Fourteen_Monthly", "Eighteen_Monthly", "Two_Yearly", "Three_Yearly",
					"Four_Yearly", "Five_Yearly", "Six_Yearly", "Seven_Yearly", "Eight_Yearly", "Nine_Yearly",
					"Ten_Yearly", "Twenty_Yearly" };

			Set<String> frequency_allowed = new HashSet<String>(Arrays.asList(frequency));
			List<Object> taskList = schedularDao.getSubTaskForAutoActivate();
			Iterator<Object> iterator = taskList.iterator();
			while (iterator.hasNext()) {
				Object[] objects = (Object[]) iterator.next();

				String clientTaskId = objects[0].toString();
				String freq_operation = objects[2].toString();
				String allow_reopen = "0";
				String allow_back_date = "0";
				int no_of_days_allowed = 0;
				int alert_days = 0;
				int document = 0;
				int historical = 0;
				int prior_days_buffer = 0;

				Date performer_date = null;
				Date reviewer_date = null;
				Date function_date = null;
				Date unit_date = null;
				Date leagl_date = null;
				Date first_alert_date = null;
				Date second_alert_date = null;
				Date third_alert_date = null;

				if (objects[3] != null)
					allow_reopen = objects[3].toString();

				if (objects[4] != null)
					no_of_days_allowed = Integer.parseInt(objects[4].toString());

				if (objects[5] != null)
					alert_days = Integer.parseInt(objects[5].toString());

				if (objects[6] != null)
					document = Integer.parseInt(objects[6].toString());

				if (objects[7] != null)
					historical = Integer.parseInt(objects[7].toString());

				if (objects[8] != null)
					prior_days_buffer = Integer.parseInt(objects[8].toString());

				if (freq_operation.equals("Weekly") || freq_operation.equals("Fortnightly")) {

					int add_days = 0;

					if (freq_operation.equals("Weekly"))
						add_days = 7;

					if (freq_operation.equals("Fortnightly"))
						add_days = 14;

					if (objects[9] != null)
						performer_date = addDays(sdfIn.parse(objects[9].toString()), add_days);

					if (objects[10] != null)
						reviewer_date = addDays(sdfIn.parse(objects[10].toString()), add_days);

					if (objects[11] != null)
						function_date = addDays(sdfIn.parse(objects[11].toString()), add_days);

					if (objects[12] != null)
						unit_date = addDays(sdfIn.parse(objects[12].toString()), add_days);

					if (objects[13] != null)
						leagl_date = addDays(sdfIn.parse(objects[13].toString()), add_days);

					if (objects[14] != null)
						first_alert_date = addDays(sdfIn.parse(objects[14].toString()), add_days);

					if (objects[15] != null)
						second_alert_date = addDays(sdfIn.parse(objects[15].toString()), add_days);

					if (objects[16] != null)
						third_alert_date = addDays(sdfIn.parse(objects[16].toString()), add_days);

				} // End add days IF

				/* Add Months if Start */
				if (freq_operation.equals("Half_Yearly") || freq_operation.equals("Quarterly")
						|| freq_operation.equals("Two_Monthly") || freq_operation.equals("Monthly")
						|| freq_operation.equals("Four_Monthly") || freq_operation.equals("Five_Monthly")
						|| freq_operation.equals("Seven_Monthly") || freq_operation.equals("Eight_Monthly")
						|| freq_operation.equals("Nine_Monthly") || freq_operation.equals("Ten_Monthly")
						|| freq_operation.equals("Fourteen_Monthly") || freq_operation.equals("Eighteen_Monthly")) {
					int add_months = 0;

					if (freq_operation.equals("Half_Yearly"))
						add_months = 6;

					if (freq_operation.equals("Quarterly"))
						add_months = 3;

					if (freq_operation.equals("Two_Monthly"))
						add_months = 2;

					if (freq_operation.equals("Monthly"))
						add_months = 1;

					if (freq_operation.equals("Four_Monthly"))
						add_months = 4;

					if (freq_operation.equals("Five_Monthly"))
						add_months = 5;

					if (freq_operation.equals("Seven_Monthly"))
						add_months = 7;

					if (freq_operation.equals("Eight_Monthly"))
						add_months = 8;

					if (freq_operation.equals("Nine_Monthly"))
						add_months = 9;

					if (freq_operation.equals("Ten_Monthly"))
						add_months = 10;

					if (freq_operation.equals("Fourteen_Monthly"))
						add_months = 14;

					if (freq_operation.equals("Eighteen_Monthly"))
						add_months = 18;

					if (objects[9] != null)
						performer_date = addMonths(sdfIn.parse(objects[9].toString()), add_months);

					if (objects[10] != null)
						reviewer_date = addMonths(sdfIn.parse(objects[10].toString()), add_months);

					if (objects[11] != null)
						function_date = addMonths(sdfIn.parse(objects[11].toString()), add_months);

					if (objects[12] != null)
						unit_date = addMonths(sdfIn.parse(objects[12].toString()), add_months);

					if (objects[13] != null)
						leagl_date = addMonths(sdfIn.parse(objects[13].toString()), add_months);

					if (objects[14] != null)
						first_alert_date = addMonths(sdfIn.parse(objects[14].toString()), add_months);

					if (objects[15] != null)
						second_alert_date = addMonths(sdfIn.parse(objects[15].toString()), add_months);

					if (objects[16] != null)
						third_alert_date = addMonths(sdfIn.parse(objects[16].toString()), add_months);

				} // End add month IF

				if (freq_operation.equals("Yearly") || freq_operation.equals("Two_Yearly")
						|| freq_operation.equals("Three_Yearly") || freq_operation.equals("Four_Yearly")
						|| freq_operation.equals("Five_Yearly") || freq_operation.equals("Ten_Yearly")
						|| freq_operation.equals("Twenty_Yearly")) {

					int add_year = 0;

					if (freq_operation.equals("Yearly"))
						add_year = 1;
					if (freq_operation.equals("Two_Yearly") || freq_operation.equals("Two_Yearly"))
						add_year = 2;
					if (freq_operation.equals("Three_Yearly") || freq_operation.equals("Three_Yearly"))
						add_year = 3;
					if (freq_operation.equals("Four_Yearly") || freq_operation.equals("Four_Yearly"))
						add_year = 4;
					if (freq_operation.equals("Five_Yearly"))
						add_year = 5;
					if (freq_operation.equals("Ten_Yearly"))
						add_year = 10;
					if (freq_operation.equals("Twenty_Yearly"))
						add_year = 20;

					if (objects[9] != null)
						performer_date = addYears(sdfIn.parse(objects[9].toString()), add_year);

					if (objects[10] != null)
						reviewer_date = addYears(sdfIn.parse(objects[10].toString()), add_year);

					if (objects[11] != null)
						function_date = addYears(sdfIn.parse(objects[11].toString()), add_year);

					if (objects[12] != null)
						unit_date = addYears(sdfIn.parse(objects[12].toString()), add_year);

					if (objects[13] != null)
						leagl_date = addYears(sdfIn.parse(objects[13].toString()), add_year);

					if (objects[14] != null)
						first_alert_date = addYears(sdfIn.parse(objects[14].toString()), add_year);

					if (objects[15] != null)
						second_alert_date = addYears(sdfIn.parse(objects[15].toString()), add_year);

					if (objects[16] != null)
						third_alert_date = addYears(sdfIn.parse(objects[16].toString()), add_year);

				}

				if (frequency_allowed.contains(freq_operation)) {
					List<SubTaskTranscational> subTask = schedularDao.getSubtaskIfExist(objects[17].toString(),
							leagl_date);
					// System.out.println("size:" +subTask.size());
					if (subTask.size() == 0) {
						SubTaskTranscational newSubTaskTransactional = new SubTaskTranscational();
						System.out.println("client Task ID:" + objects[0].toString());
						newSubTaskTransactional.setTtrn_sub_client_task_id(clientTaskId);
						newSubTaskTransactional.setTtrn_sub_task_id(objects[17].toString());
						newSubTaskTransactional.setTtrn_sub_task_created_at(new Date());
						newSubTaskTransactional.setTtrn_sub_task_pr_due_date(performer_date);
						newSubTaskTransactional.setTtrn_sub_task_rw_date(reviewer_date);
						newSubTaskTransactional.setTtrn_sub_task_FH_due_date(function_date);
						newSubTaskTransactional.setTtrn_sub_task_UH_due_date(unit_date);
						newSubTaskTransactional.setTtrn_sub_task_ENT_due_date(leagl_date);
						newSubTaskTransactional.setTtrn_sub_task_first_alert(first_alert_date);
						newSubTaskTransactional.setTtrn_sub_task_second_alert(second_alert_date);
						newSubTaskTransactional.setTtrn_sub_task_third_alert(third_alert_date);
						// newSubTaskTransactional.setTttn_sub_task_next_examination_date(ttrn_next_examination_date);
						newSubTaskTransactional.setTtrn_sub_task_activation_date(new Date());
						newSubTaskTransactional.setTtrn_sub_task_alert_prior_day(alert_days);
						newSubTaskTransactional.setTtrn_sub_task_buffer_days(prior_days_buffer);
						newSubTaskTransactional.setTtrn_sub_task_back_date_allowed(no_of_days_allowed);
						newSubTaskTransactional.setTtrn_sub_task_document(document);
						newSubTaskTransactional.setTtrn_sub_task_historical(historical);
						newSubTaskTransactional.setTtrn_sub_task_status("Active");
						newSubTaskTransactional.setTtrn_sub_task_updated_at(new Date());
						newSubTaskTransactional.setTtrn_sub_task_allow_approver_reopening(allow_reopen);
						newSubTaskTransactional.setTtrn_sub_task_approved_by(0);
						newSubTaskTransactional.setTtrn_sub_task_approved_date(new Date());
						subTaskDao.saveObject(newSubTaskTransactional);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String sendNonDeliveredEscalationMails(String jsonString) {
		try {
			String created_at = sdfOut.format(new Date());
			List<Object> mailList = schedularDao.getNonDeliveredMails("Escalation Alert To Evaluator", created_at);
			int rw_user_id = 0;
			ArrayList<SendMail> sendingMailList = new ArrayList<>();
			if (mailList.size() == 0) {
				sendingMailList.clear();
			}
			for (int a = 0; a < mailList.size(); a++) {
				SendMail sendMail = new SendMail();
				Object[] object = (Object[]) mailList.get(a);
				if (rw_user_id != Integer.parseInt(object[7].toString()) && rw_user_id != 0) {

					Thread.sleep(2000);

					utilitiesService.sendEscalationMailsToEvaluator("Escalation Alert To Evaluator", rw_user_id,
							sendingMailList);

					sendingMailList.clear();
					rw_user_id = Integer.parseInt(object[7].toString());

					sendMail.setClient_task_id(object[2].toString());
					sendMail.setEntity_name(object[6].toString());
					sendMail.setUnit_name(object[20].toString());
					sendMail.setFunction_name(object[13].toString());
					sendMail.setName_of_legislation(object[17].toString());
					sendMail.setName_of_rule(object[18].toString());
					sendMail.setWhen(object[16].toString());
					sendMail.setActivity(object[1].toString());
					sendMail.setFrequency(object[11].toString());
					sendMail.setImpact(object[14].toString());
					sendMail.setExecutor_date(object[10].toString());
					sendMail.setEvaluator_date(object[8].toString());
					sendMail.setFunc_head_date(object[12].toString());
					sendMail.setUnit_head_date(object[19].toString());
					sendMail.setLegal_due_date(object[15].toString());
					System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendMail.setExec_id(Integer.parseInt(object[9].toString()));
					sendMail.setEval_id(Integer.parseInt(object[7].toString()));
					System.out.println("IN LAST IF LOOP:" + object[15].toString());
					sendingMailList.add(sendMail);
				} else {
					rw_user_id = Integer.parseInt(object[7].toString());

					sendMail.setClient_task_id(object[2].toString());
					sendMail.setEntity_name(object[6].toString());
					sendMail.setUnit_name(object[20].toString());
					sendMail.setFunction_name(object[13].toString());
					sendMail.setName_of_legislation(object[17].toString());
					sendMail.setName_of_rule(object[18].toString());
					sendMail.setWhen(object[16].toString());
					sendMail.setActivity(object[1].toString());
					sendMail.setFrequency(object[11].toString());
					sendMail.setImpact(object[14].toString());
					sendMail.setExecutor_date(object[10].toString());
					sendMail.setEvaluator_date(object[8].toString());
					sendMail.setFunc_head_date(object[12].toString());
					sendMail.setUnit_head_date(object[19].toString());
					sendMail.setLegal_due_date(object[15].toString());
					// System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendMail.setExec_id(Integer.parseInt(object[9].toString()));
					sendMail.setEval_id(Integer.parseInt(object[7].toString()));
					// System.out.println("IN LAST ELSE LOOP:" + object[15].toString());
					sendingMailList.add(sendMail);
				}
			}

			utilitiesService.sendEscalationMailsToEvaluator("Escalation Alert To Evaluator", rw_user_id,
					sendingMailList);

			/* For Sutask */
			sendingMailList.clear();
			List<Object> subtaskMailList = schedularDao
					.getNonDeliveredMailsOfSubtask("Escalation Sub Task Alert To Evaluator", created_at);
			if (subtaskMailList.size() == 0) {
				sendingMailList.clear();
			}
			for (int a = 0; a < subtaskMailList.size(); a++) {
				SendMail sendMail = new SendMail();
				Object[] object = (Object[]) subtaskMailList.get(a);
				if (rw_user_id != Integer.parseInt(object[8].toString()) && rw_user_id != 0) {

					utilitiesService.sendEscalationMailsToEvaluatorForSubtask("Escalation Sub Task Alert To Evaluator",
							rw_user_id, sendingMailList);

					sendingMailList.clear();
					rw_user_id = Integer.parseInt(object[7].toString());

					sendMail.setClient_task_id(object[2].toString());
					sendMail.setEntity_name(object[6].toString());
					sendMail.setUnit_name(object[20].toString());
					sendMail.setFunction_name(object[13].toString());
					sendMail.setName_of_legislation(object[17].toString());
					sendMail.setName_of_rule(object[18].toString());
					sendMail.setWhen(object[16].toString());
					sendMail.setActivity(object[1].toString());
					sendMail.setFrequency(object[11].toString());
					sendMail.setImpact(object[14].toString());
					sendMail.setExecutor_date(object[10].toString());
					sendMail.setEvaluator_date(object[8].toString());
					sendMail.setFunc_head_date(object[12].toString());
					sendMail.setUnit_head_date(object[19].toString());
					sendMail.setLegal_due_date(object[15].toString());
					// System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendMail.setExec_id(Integer.parseInt(object[9].toString()));
					sendMail.setEval_id(Integer.parseInt(object[7].toString()));
					sendMail.setSub_client_task_id(object[21].toString());
					// System.out.println("IN LAST IF LOOP:" + object[15].toString());
					sendingMailList.add(sendMail);
				} else {
					rw_user_id = Integer.parseInt(object[7].toString());

					sendMail.setClient_task_id(object[2].toString());
					sendMail.setEntity_name(object[6].toString());
					sendMail.setUnit_name(object[20].toString());
					sendMail.setFunction_name(object[13].toString());
					sendMail.setName_of_legislation(object[17].toString());
					sendMail.setName_of_rule(object[18].toString());
					sendMail.setWhen(object[16].toString());
					sendMail.setActivity(object[1].toString());
					sendMail.setFrequency(object[11].toString());
					sendMail.setImpact(object[14].toString());
					sendMail.setExecutor_date(object[10].toString());
					sendMail.setEvaluator_date(object[8].toString());
					sendMail.setFunc_head_date(object[12].toString());
					sendMail.setUnit_head_date(object[19].toString());
					sendMail.setLegal_due_date(object[15].toString());
					// System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendMail.setExec_id(Integer.parseInt(object[9].toString()));
					sendMail.setEval_id(Integer.parseInt(object[7].toString()));
					sendMail.setSub_client_task_id(object[21].toString());
					// System.out.println("IN LAST ELSE LOOP:" + object[15].toString());
					sendingMailList.add(sendMail);
				}
			}

			utilitiesService.sendEscalationMailsToEvaluatorForSubtask("Escalation Sub Task Alert To Evaluator",
					rw_user_id, sendingMailList);
			sendingMailList.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public String sendNonDeliveredEscalationMailsTOFH(String jsonString) {
		try {
			String created_at = sdfOut.format(new Date());
			List<Object> mailList = schedularDao.getNonDeliveredMails("Escalation Alert To Function Head", created_at);
			int fh_user_id = 0;
			ArrayList<SendMail> sendingMailList = new ArrayList<>();
			if (mailList.size() == 0 && mailList != null) {
				sendingMailList.clear();
			}
			for (int a = 0; a < mailList.size(); a++) {
				SendMail sendMail = new SendMail();
				Object[] object = (Object[]) mailList.get(a);
				if (fh_user_id != Integer.parseInt(object[21].toString()) && fh_user_id != 0) {

					utilitiesService.sendEscalationMailsToFunctionHead("Escalation Alert To Function Head", fh_user_id,
							sendingMailList);

					sendingMailList.clear();
					fh_user_id = Integer.parseInt(object[21].toString());

					sendMail.setClient_task_id(object[2].toString());
					sendMail.setEntity_name(object[6].toString());
					sendMail.setUnit_name(object[20].toString());
					sendMail.setFunction_name(object[13].toString());
					sendMail.setName_of_legislation(object[17].toString());
					sendMail.setName_of_rule(object[18].toString());
					sendMail.setWhen(object[16].toString());
					sendMail.setActivity(object[1].toString());
					sendMail.setFrequency(object[11].toString());
					sendMail.setImpact(object[14].toString());
					sendMail.setExecutor_date(object[10].toString());
					sendMail.setEvaluator_date(object[8].toString());
					sendMail.setFunc_head_date(object[12].toString());
					sendMail.setUnit_head_date(object[19].toString());
					sendMail.setLegal_due_date(object[15].toString());
					// System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendMail.setExec_id(Integer.parseInt(object[9].toString()));
					sendMail.setEval_id(Integer.parseInt(object[7].toString()));
					// System.out.println("IN LAST IF LOOP:" + object[15].toString());
					sendingMailList.add(sendMail);
				} else {
					fh_user_id = Integer.parseInt(object[21].toString());

					sendMail.setClient_task_id(object[2].toString());
					sendMail.setEntity_name(object[6].toString());
					sendMail.setUnit_name(object[20].toString());
					sendMail.setFunction_name(object[13].toString());
					sendMail.setName_of_legislation(object[17].toString());
					sendMail.setName_of_rule(object[18].toString());
					sendMail.setWhen(object[16].toString());
					sendMail.setActivity(object[1].toString());
					sendMail.setFrequency(object[11].toString());
					sendMail.setImpact(object[14].toString());
					sendMail.setExecutor_date(object[10].toString());
					sendMail.setEvaluator_date(object[8].toString());
					sendMail.setFunc_head_date(object[12].toString());
					sendMail.setUnit_head_date(object[19].toString());
					sendMail.setLegal_due_date(object[15].toString());
					// System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendMail.setExec_id(Integer.parseInt(object[9].toString()));
					sendMail.setEval_id(Integer.parseInt(object[7].toString()));
					// System.out.println("IN LAST ELSE LOOP:" + object[15].toString());
					sendingMailList.add(sendMail);
				}
			}

			utilitiesService.sendEscalationMailsToFunctionHead("Escalation Alert To Function Head", fh_user_id,
					sendingMailList);

			/* For Sutask */
			sendingMailList.clear();
			List<Object> subtaskMailList = schedularDao
					.getNonDeliveredMailsOfSubtask("Escalation Sub Task Alert To Function Head", created_at);
			if (subtaskMailList.size() == 0) {
				sendingMailList.clear();
			}
			for (int a = 0; a < subtaskMailList.size(); a++) {
				SendMail sendMail = new SendMail();
				Object[] object = (Object[]) subtaskMailList.get(a);
				if (fh_user_id != Integer.parseInt(object[22].toString()) && fh_user_id != 0) {

					utilitiesService.sendEscalationMailsToFHForSubtask("Escalation Sub Task Alert To Function Head",
							fh_user_id, sendingMailList);

					sendingMailList.clear();
					fh_user_id = Integer.parseInt(object[22].toString());

					sendMail.setClient_task_id(object[2].toString());
					sendMail.setEntity_name(object[6].toString());
					sendMail.setUnit_name(object[20].toString());
					sendMail.setFunction_name(object[13].toString());
					sendMail.setName_of_legislation(object[17].toString());
					sendMail.setName_of_rule(object[18].toString());
					sendMail.setWhen(object[16].toString());
					sendMail.setActivity(object[1].toString());
					sendMail.setFrequency(object[11].toString());
					sendMail.setImpact(object[14].toString());
					sendMail.setExecutor_date(object[10].toString());
					sendMail.setEvaluator_date(object[8].toString());
					sendMail.setFunc_head_date(object[12].toString());
					sendMail.setUnit_head_date(object[19].toString());
					sendMail.setLegal_due_date(object[15].toString());
					// System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendMail.setExec_id(Integer.parseInt(object[9].toString()));
					sendMail.setEval_id(Integer.parseInt(object[7].toString()));
					sendMail.setSub_client_task_id(object[21].toString());
					// System.out.println("IN LAST IF LOOP:" + object[15].toString());
					sendingMailList.add(sendMail);
				} else {
					fh_user_id = Integer.parseInt(object[22].toString());

					sendMail.setClient_task_id(object[2].toString());
					sendMail.setEntity_name(object[6].toString());
					sendMail.setUnit_name(object[20].toString());
					sendMail.setFunction_name(object[13].toString());
					sendMail.setName_of_legislation(object[17].toString());
					sendMail.setName_of_rule(object[18].toString());
					sendMail.setWhen(object[16].toString());
					sendMail.setActivity(object[1].toString());
					sendMail.setFrequency(object[11].toString());
					sendMail.setImpact(object[14].toString());
					sendMail.setExecutor_date(object[10].toString());
					sendMail.setEvaluator_date(object[8].toString());
					sendMail.setFunc_head_date(object[12].toString());
					sendMail.setUnit_head_date(object[19].toString());
					sendMail.setLegal_due_date(object[15].toString());
					// System.out.println("IN FIRST IF LOOP:" + object[15].toString());
					sendMail.setExec_id(Integer.parseInt(object[9].toString()));
					sendMail.setEval_id(Integer.parseInt(object[7].toString()));
					sendMail.setSub_client_task_id(object[21].toString());
					// System.out.println("IN LAST ELSE LOOP:" + object[15].toString());
					sendingMailList.add(sendMail);
				}
			}

			utilitiesService.sendEscalationMailsToFHForSubtask("Escalation Sub Task Alert To Function Head", fh_user_id,
					sendingMailList);
			sendingMailList.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	@Override
	public String autoComplianceReport(int orga_id) {
		JSONObject objForSend = new JSONObject();
		try {
			JSONObject objForFetch = new JSONObject();
			Date currentDate = sdfIn.parse(sdfIn.format(new Date()));
			String startDate = "2019-10-01";
			List<User> fh_users = userDao.getUsersByRole(3);
			String dept_name = null;
			Iterator<User> usr_itr = fh_users.iterator();
			while (usr_itr.hasNext()) {

				User user = (User) usr_itr.next();
				objForFetch.put("task_impact", "NA");
				objForFetch.put("user_id", user.getUser_id());
				objForFetch.put("from_date", startDate);
				objForFetch.put("to_date", sdf.format(new Date()));

				int complied = 0;
				int nonComplied = 0;
				int delayed = 0;
				int delayed_reported = 0;

				String format = "";
				HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet spreadsheet = workbook.createSheet("LastWeekNonCompliedTask");

				format = "" + "Compliance Report" + "-" + sdfOut.format(new Date()) + "";
				HSSFRow row = spreadsheet.createRow(0);

				row.createCell(0).setCellValue("Task Id");
				row.createCell(1).setCellValue("Entity");
				row.createCell(2).setCellValue("Unit");
				row.createCell(3).setCellValue("Function");
				row.createCell(4).setCellValue("Executor Name");
				row.createCell(5).setCellValue("Evaluator Name");
				row.createCell(6).setCellValue("Function Head Name");
				row.createCell(7).setCellValue("Reference");
				row.createCell(8).setCellValue("Who");
				row.createCell(9).setCellValue("When");
				row.createCell(10).setCellValue("Compliance Activity");
				row.createCell(11).setCellValue("Impact");
				row.createCell(12).setCellValue("Frequency");
				row.createCell(13).setCellValue("Comments");
				row.createCell(14).setCellValue("Reason for non compliance");
				row.createCell(15).setCellValue("Legal due date");
				row.createCell(16).setCellValue("Completed date");
				row.createCell(17).setCellValue("Submitted date");
				row.createCell(18).setCellValue("Status");

				List<Object> task_list = schedularDao.autoComplianceReport(objForFetch.toJSONString(), orga_id);

				if (task_list.size() > 0) {
					Iterator<Object> itr = task_list.iterator();
					int i = 1;

					while (itr.hasNext()) {
						Object[] object = (Object[]) itr.next();
						JSONObject objForAppend = new JSONObject();
						if (object[14] != null) {
							String taskStatus = object[8].toString();

							Date legalDueDate = sdfOut.parse(object[14].toString());
							if (taskStatus.equals("Completed")) {
								Date submittedDate = sdfOut.parse(object[15].toString());
								Date completedDate = sdfOut.parse(object[16].toString());
								if (legalDueDate.after(submittedDate) || legalDueDate.equals(submittedDate)) {
									complied++;
									objForAppend.put("task_status", "Complied");

									objForAppend.put("ttrn_submitted_date", sdfIn.format(submittedDate));
									objForAppend.put("ttrn_completed_date",
											sdfIn.format(sdfOut.parse(object[16].toString())));
									objForAppend.put("tmap_client_task_id", object[0]);
									System.out.println("Task ID: " + object[0] + " Completed Date:"
											+ sdfIn.format(sdfOut.parse(object[16].toString())));
									objForAppend.put("task_legi_name", object[1]);
									objForAppend.put("task_rule_name", object[2]);
									objForAppend.put("task_who", object[3]);
									objForAppend.put("task_when", object[4]);
									objForAppend.put("task_activity", object[5]);
									objForAppend.put("task_impact", object[6]);
									objForAppend.put("task_frequency", object[7]);
									objForAppend.put("ttrn_performer_comments", object[9]);
									if (object[10] != null) {
										objForAppend.put("ttrn_reason_for_non_compliance", object[10]);
									} else {
										objForAppend.put("ttrn_reason_for_non_compliance", "");
									}
									objForAppend.put("executor_name",
											object[12].toString() + " " + object[13].toString());
									objForAppend.put("evaluator_name",
											object[23].toString() + " " + object[24].toString());
									objForAppend.put("fun_head_name",
											object[25].toString() + " " + object[26].toString());
									objForAppend.put("ttrn_legal_due_date", sdfIn.format(legalDueDate));
									objForAppend.put("entity_name", object[18]);
									objForAppend.put("unit_name", object[19]);
									objForAppend.put("function_name", object[20]);
									dept_name = object[20].toString();
									objForAppend.put("task_reference", object[17]);

								} else {
									if (submittedDate.after(legalDueDate) && completedDate.after(legalDueDate)) {
										delayed++;
										objForAppend.put("task_status", "Delayed");

										objForAppend.put("ttrn_submitted_date", sdfIn.format(submittedDate));
										objForAppend.put("ttrn_completed_date",
												sdfIn.format(sdfOut.parse(object[16].toString())));
										objForAppend.put("tmap_client_task_id", object[0]);
										objForAppend.put("task_legi_name", object[1]);
										objForAppend.put("task_rule_name", object[2]);
										objForAppend.put("task_who", object[3]);
										objForAppend.put("task_when", object[4]);
										objForAppend.put("task_activity", object[5]);
										objForAppend.put("task_impact", object[6]);
										objForAppend.put("task_frequency", object[7]);
										objForAppend.put("ttrn_performer_comments", object[9]);
										if (object[10] != null) {
											objForAppend.put("ttrn_reason_for_non_compliance", object[10]);
										} else {
											objForAppend.put("ttrn_reason_for_non_compliance", "");
										}
										objForAppend.put("executor_name",
												object[12].toString() + " " + object[13].toString());
										objForAppend.put("evaluator_name",
												object[23].toString() + " " + object[24].toString());
										objForAppend.put("fun_head_name",
												object[25].toString() + " " + object[26].toString());
										objForAppend.put("ttrn_legal_due_date", sdfIn.format(legalDueDate));
										objForAppend.put("entity_name", object[18]);
										objForAppend.put("unit_name", object[19]);
										objForAppend.put("function_name", object[20]);
										dept_name = object[20].toString();
										objForAppend.put("task_reference", object[17]);
									}

									if (submittedDate.after(legalDueDate) && (completedDate.before(legalDueDate)
											|| completedDate.equals(legalDueDate))) {
										delayed_reported++;
										objForAppend.put("task_status", "Delayed_Reported");

										objForAppend.put("ttrn_submitted_date", sdfIn.format(submittedDate));
										objForAppend.put("ttrn_completed_date",
												sdfIn.format(sdfOut.parse(object[16].toString())));
										objForAppend.put("tmap_client_task_id", object[0]);
										objForAppend.put("task_legi_name", object[1]);
										objForAppend.put("task_rule_name", object[2]);
										objForAppend.put("task_who", object[3]);
										objForAppend.put("task_when", object[4]);
										objForAppend.put("task_activity", object[5]);
										objForAppend.put("task_impact", object[6]);
										objForAppend.put("task_frequency", object[7]);
										objForAppend.put("ttrn_performer_comments", object[9]);
										if (object[10] != null) {
											objForAppend.put("ttrn_reason_for_non_compliance", object[10]);
										} else {
											objForAppend.put("ttrn_reason_for_non_compliance", "");
										}
										objForAppend.put("executor_name",
												object[12].toString() + " " + object[13].toString());
										objForAppend.put("evaluator_name",
												object[23].toString() + " " + object[24].toString());
										objForAppend.put("fun_head_name",
												object[25].toString() + " " + object[26].toString());
										objForAppend.put("ttrn_legal_due_date", sdfIn.format(legalDueDate));
										objForAppend.put("entity_name", object[18]);
										objForAppend.put("unit_name", object[19]);
										objForAppend.put("function_name", object[20]);
										dept_name = object[20].toString();
										objForAppend.put("task_reference", object[17]);
									}
								}
								// dataForAppend.add(objForAppend);
								// System.out.println("Legal Date: "+legalDueDate);
							} else {
								if (taskStatus.equals("Active")) {
									if (currentDate.after(legalDueDate)) {
										nonComplied++;
										objForAppend.put("task_status", "Non Complied");
										objForAppend.put("ttrn_submitted_date", "");
										objForAppend.put("ttrn_completed_date", "");
										objForAppend.put("tmap_client_task_id", object[0]);
										objForAppend.put("task_legi_name", object[1]);
										objForAppend.put("task_rule_name", object[2]);
										objForAppend.put("task_who", object[3]);
										objForAppend.put("task_when", object[4]);
										objForAppend.put("task_activity", object[5]);
										objForAppend.put("task_impact", object[6]);
										objForAppend.put("task_frequency", object[7]);
										objForAppend.put("ttrn_performer_comments", "");

										objForAppend.put("executor_name",
												object[12].toString() + " " + object[13].toString());
										objForAppend.put("evaluator_name",
												object[23].toString() + " " + object[24].toString());
										objForAppend.put("fun_head_name",
												object[25].toString() + " " + object[26].toString());
										objForAppend.put("ttrn_legal_due_date", sdfIn.format(legalDueDate));
										objForAppend.put("entity_name", object[18]);
										objForAppend.put("unit_name", object[19]);
										objForAppend.put("function_name", object[20]);
										dept_name = object[20].toString();
										objForAppend.put("task_reference", object[17]);

										if (object[10] != null) {
											objForAppend.put("ttrn_reason_for_non_compliance", object[10]);
										} else {
											objForAppend.put("ttrn_reason_for_non_compliance", "");
										}
										objForSend.putAll(objForAppend);
										// dataForAppend.add(objForAppend);
										// System.out.println("Legal Date:
										// "+legalDueDate);
									}

								}
							}
						}

						row = spreadsheet.createRow(i);
						if (objForAppend.get("tmap_client_task_id") != null) {
							System.out.println("Client_Task Id:" + objForAppend.get("tmap_client_task_id").toString());
							row.createCell(0).setCellValue(objForAppend.get("tmap_client_task_id").toString());
						}
						if (objForAppend.get("entity_name") != null) {
							row.createCell(1).setCellValue(objForAppend.get("entity_name").toString());
						}
						if (objForAppend.get("unit_name") != null) {
							row.createCell(2).setCellValue(objForAppend.get("unit_name").toString());
						}
						if (objForAppend.get("function_name") != null) {
							row.createCell(3).setCellValue(objForAppend.get("function_name").toString());
						}
						if (objForAppend.get("executor_name") != null) {
							row.createCell(4).setCellValue(objForAppend.get("executor_name").toString());
						}
						if (objForAppend.get("evaluator_name") != null) {
							row.createCell(5).setCellValue(objForAppend.get("evaluator_name").toString());
						}
						if (objForAppend.get("fun_head_name") != null) {
							row.createCell(6).setCellValue(objForAppend.get("fun_head_name").toString());
						}
						if (objForAppend.get("task_reference") != null) {
							row.createCell(7).setCellValue(objForAppend.get("task_reference").toString());
						}
						if (objForAppend.get("task_who") != null) {
							row.createCell(8).setCellValue(objForAppend.get("task_who").toString());
						}
						if (objForAppend.get("task_when") != null) {
							row.createCell(9).setCellValue(objForAppend.get("task_when").toString());
						}
						if (objForAppend.get("task_activity") != null) {
							row.createCell(10).setCellValue(objForAppend.get("task_activity").toString());
						}
						if (objForAppend.get("task_impact") != null) {
							row.createCell(11).setCellValue(objForAppend.get("task_impact").toString());
						}
						if (objForAppend.get("task_frequency") != null) {
							row.createCell(12).setCellValue(objForAppend.get("task_frequency").toString());
						}
						if (objForAppend.get("ttrn_performer_comments") != null) {
							row.createCell(13).setCellValue(objForAppend.get("ttrn_performer_comments").toString());
						}
						if (objForAppend.get("ttrn_reason_for_non_compliance") != null) {
							row.createCell(14)
									.setCellValue(objForAppend.get("ttrn_reason_for_non_compliance").toString());
						}

						if (objForAppend.get("ttrn_legal_due_date") != null) {
							row.createCell(15).setCellValue(
									sdfOut.format(sdfIn.parse(objForAppend.get("ttrn_legal_due_date").toString())));
						} else {
							row.createCell(15).setCellValue("");
						}
						// System.out.println("ttrn_completed_date: "
						// +sdfOut.format(sdfIn.parse(objForAppend.get("ttrn_completed_date").toString())));
						if (objForAppend.get("ttrn_completed_date") != "") {
							System.out.println("ttrn_completed_date: "
									+ sdfOut.format(sdfIn.parse(objForAppend.get("ttrn_completed_date").toString())));
							row.createCell(16).setCellValue(
									sdfOut.format(sdfIn.parse(objForAppend.get("ttrn_completed_date").toString())));
						} else {
							row.createCell(16).setCellValue("");
						}

						if (objForAppend.get("ttrn_submitted_date") != "") {
							row.createCell(17).setCellValue(
									sdf.format(sdf.parse(objForAppend.get("ttrn_submitted_date").toString())));
						} else {
							row.createCell(17).setCellValue("");
						}

						if (objForAppend.get("task_status") != null) {
							row.createCell(18).setCellValue(objForAppend.get("task_status").toString());
						}
						i++;
					} // End of task while loop
				}

				List<Object> evalList = schedularDao.getEvaluatorList(objForFetch.toJSONString(), orga_id);

				if (task_list.size() > 0) {
					System.out.println("In Create file loop");
					File dir1 = new File("C:" + File.separator + "CMS" + File.separator + projectName + File.separator
							+ "Compliance Report");

					if (!dir1.exists())
						dir1.mkdirs();

					Object generatedExcelFile = "Compliance Report" + ".csv";
					File newExcelFile = new File(dir1.getPath() + File.separator + generatedExcelFile);

					if (!newExcelFile.exists()) {
						newExcelFile.createNewFile();
					}

					File file = new File(dir1.getPath() + File.separator + generatedExcelFile);
					FileOutputStream outputStream;
					outputStream = new FileOutputStream(newExcelFile);
					workbook.write(outputStream);

					Properties props = new Properties();

					props.put("mail.smtp.auth", "true");
					// props.put("mail.smtp.starttls.enable", "true");
					props.put("mail.smtp.host", hostName);
					props.put("mail.smtp.port", portNo);
					Session session = Session.getInstance(props, new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(username, password);
						}
					});

					String email_body = "<div style='margin:0 auto;width:100%;height:auto;padding:16px;'>";

					email_body += "<h2 style='font-size:18px;'>Dear Sir/Madam,</h2>";
					email_body += "<p style='text-align:justify;width:70%;'>Kindly find below the dashboard of Compliances and Evaluator wise breakup of pending tasks as on date. (Detailed list attached herewith for your perusal.) "
							+ ".</p>";
					email_body += "<h2 style='font-size:18px;'>Compliance Dashboard: </h2><br>";
					email_body += "<table style='width:80%;' border='1'>" + "<thead>"
							+ "<tr style='background:#0B6EC3;color:#fff;'>" + "<th>Complied</th>" + "<th>Delayed</th>"
							+ "<th>Delayed-Reported</th>" + "<th>Non-Complied</th>" + "</tr>" + "</thead>" + "<tbody>";

					email_body += "<tr>" + "<td style='text-align:center'>" + complied + "</td>"
							+ "<td style='text-align:center'>" + delayed + "</td>" + "<td style='text-align:center'>"
							+ delayed_reported + "</td>" + "<td style='text-align:center'>" + nonComplied + "</td>"
							+ "</tr>";

					email_body += "</tbody>" + "</table>";
					email_body += "<br>";
					email_body += "<h2 style='font-size:18px;'>Evaluator wise break-up for pending tasks: </h2><br>";
					email_body += "<table style='width:80%;' border='1'>" + "<thead>"
							+ "<tr style='background:#0B6EC3;color:#fff;'>" + "<th>Evaluator</th>" + "<th>Count</th>"
							+ "</tr>" + "</thead>" + "<tbody>";

					if (evalList != null) {
						Iterator<Object> itr1 = evalList.iterator();
						int j = 0;
						while (itr1.hasNext()) {
							Object[] objects = (Object[]) itr1.next();
							User rw_user = userDao.getUserById(Integer.parseInt(objects[0].toString()));
							email_body += "<tr>" + "<td style='text-align:center'>" + rw_user.getUser_first_name() + " "
									+ rw_user.getUser_last_name() + "</td>" + "<td style='text-align:center'>"
									+ objects[1].toString() + "</td>" + "</tr>";

						}
					} else {
						email_body += "<tr>" + "<td style='text-align:center'>" + "No Pending Activities" + "</td>"
								+ "<td style='text-align:center'>" + "0" + "</td>" + "</tr>";
					}
					email_body += "</tbody>" + "</table>";

					email_body += "<p >This is a system generated mail. Please do not reply to this mail.</p>"
							+ "<h2 style='font-size:18px;'>Yours Sincerely</h2>"
							+ "<h2 style='font-size:18px;'>One Compliance Team</h2>" + "</div>";

					// Create a default MimeMessage object.
					Message message = new MimeMessage(session);

					// Set From: header field of the header.
					message.setFrom(new InternetAddress(mailFrom));
					message.setSentDate(new Date());
					String to = user.getUser_email();
					Department dept = functionDao.getDepartmentById(user.getUser_department_id());
					Organization org = entityDao.getOrganizationById(orga_id);
					// String cc = monthly_report_cc;

					message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

					// message.setRecipients(Message.RecipientType.CC,
					/// InternetAddress.parse(cc));
					message.addRecipient(Message.RecipientType.CC,
							new InternetAddress("swapnali.kumbhar@lexcareglobal.com"));
					message.addRecipient(Message.RecipientType.CC,
							new InternetAddress("swapnali.kumbhar@lexcareglobal.com"));
					message.setSubject("One Compliance Report - " + org.getOrga_name() + " - " + dept_name);
					message.setContent(email_body, "text/html; charset=utf-8");
					// Create the message part
					BodyPart messageBodyPart = new MimeBodyPart();

					// Now set the actual message
					messageBodyPart.setText(email_body);
					messageBodyPart.setContent(email_body, "text/html; charset=utf-8");
					// Create a multipart message
					Multipart multipart = new MimeMultipart();
					// Set text message part
					multipart.addBodyPart(messageBodyPart);
					// Part two is attachment
					messageBodyPart = new MimeBodyPart();

					DataSource source = new FileDataSource(file.getAbsolutePath().toString());
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(file.getName());
					multipart.addBodyPart(messageBodyPart);

					// Send the complete message parts
					message.setContent(multipart);

					// Send message
					Transport.send(message);

					// utilitiesService.addMailToLog(entityObj[0].toString(),
					// "Weekly Excel Sheet", "");
					workbook.removeSheetAt(0);
					// workbook.close();
					// workbook.removeSheetAt(1);
					System.out.println("Send mail with Sheet");
					file.delete();
				}
			}
			objForSend.put("responseMessage", "Success");
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("responseMessage", "Failed");
			return objForSend.toJSONString();
		}

	}

	@Override
	public String toUnlockUserAccount() {

		List<Object> userList = userDao.getAllUsers();

		System.out.println("size:" + userList.size());
		Iterator<Object> itr = userList.iterator();
		while (itr.hasNext()) {
			Object[] object = (Object[]) itr.next();
			System.out.println("in iterator loop:" + object[4].toString());
			User user = userDao.getUserById(Integer.parseInt(object[0].toString()));
			Date locked_at = user.getAccount_locked_at();
			System.out.println("loced_at:" + locked_at);
			LocalDateTime locked_time = locked_at.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			LocalDateTime now = LocalDateTime.now();

			Duration duration = Duration.between(now, locked_time);
			long diff = Math.abs(duration.toMinutes());

			System.out.println("diff in minutes:" + diff);

			if (diff >= 10) {
				user.setUser_enable_status("1");
				user.setAccount_locked_at(null);
				user.setAccount_locked_status(0);
				user.setLogin_attempts(0);
				user.setLogin_attempts_left(3);
				userDao.updateUser(user);

			}
		}
		return null;

	}

	@Override
	public int deleteAllLoggedInUsers() {
		userDao.deleteAllLoggedInUsers();
		return 0;
	}
}
