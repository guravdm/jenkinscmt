package lexprd006.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lexprd006.dao.CommonLogsDao;
import lexprd006.dao.SchedularDao;
import lexprd006.dao.TasksDao;
import lexprd006.dao.UsersDao;
import lexprd006.domain.EmailLogs;
import lexprd006.domain.SendMail;
import lexprd006.domain.SubTaskTranscational;
import lexprd006.domain.TaskTransactional;
import lexprd006.domain.User;
import lexprd006.service.UsersService;
import lexprd006.service.UtilitiesService;

/*
 * Author: Mahesh Kharote
 * Date: 19/02/2016
 * Updated By: Mahesh Kharote
 * Updated Date: 19/02/2016
 * 
 * */

@Service("utilitiesService")
public class UtilitiesServiceImpl implements UtilitiesService {

	@Autowired
	TasksDao tasksDao;
	@Autowired
	UsersService userService;
	@Autowired
	UsersDao usersDao;
	@Autowired
	SchedularDao schedularDao;
	@Autowired
	HttpSession httpSession;

	@Autowired
	CommonLogsDao cLogsDao;

	private @Value("#{config['mail_user_name'] ?: 'null'}") String username;
	private @Value("#{config['mail_password'] ?: 'null'}") String password;
	private @Value("#{config['mail_smtp_host'] ?: 'null'}") String hostName;
	private @Value("#{config['mail_smtp_port'] ?: 'null'}") String portNo;
	private @Value("#{config['mail_from'] ?: 'null'}") String mailFrom;
	private @Value("#{config['project_name'] ?: 'null'}") String projectName;
	private @Value("#{config['project_url'] ?: 'null'}") String url;

	SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdfOut = new SimpleDateFormat("dd-MM-yyyy");

	// Method Created By: Mahesh Kharote
	// Method Purpose: Get Current date
	@Override
	public Date getCurrentDate() {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Date date1 = null;

		try {
			date1 = dateFormat.parse(dateFormat.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date1;
	}

	// Method Created By: Mahesh Kharote
	// Method Purpose: Get Current date
	@Override
	public String getCurrentDateString() {

		try {
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date date = new Date();
			return dateFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}

	// Method Created By: Mahesh Kharote
	// Method Purpose: Get User Id set in session
	@Override
	public int getCurrentSessionUserId(HttpSession session) {

		try {
			return Integer.parseInt(session.getAttribute("sess_user_id").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// Method Created By: Mahesh Kharote
	// Method Purpose: Get User Id set in session
	@Override
	public int getCurrentYear() {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy");
			Date date = new Date();
			return Integer.parseInt(dateFormat.format(date));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void copy(InputStream is, OutputStream os) {
		// TODO Auto-generated method stub
		try {
			byte buf[] = new byte[4096]; // 4K buffer set
			int read = 0;
			while ((read = is.read(buf)) != -1) // reading
				os.write(buf, 0, read); // writing
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String encrypt(String algo, String path) {
		try {
			// generating key

			byte k[] = "HignDlPs".getBytes();
			SecretKeySpec key = new SecretKeySpec(k, algo.split("/")[0]);
			// creating and initializing cipher and cipher streams
			Cipher encrypt = Cipher.getInstance(algo);
			encrypt.init(Cipher.ENCRYPT_MODE, key);
			// opening streams
			FileOutputStream fos = new FileOutputStream(path + ".enc");
			try (FileInputStream fis = new FileInputStream(path)) {
				try (CipherOutputStream cout = new CipherOutputStream(fos, encrypt)) {
					copy(fis, cout);
				}
			}
			return "\"" + path + ".enc";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String decrypt(String algo, String path) {
		// TODO Auto-generated method stub
		try {
			// generating same key
			byte k[] = "HignDlPs".getBytes();
			SecretKeySpec key = new SecretKeySpec(k, algo.split("/")[0]);
			// creating and initializing cipher and cipher streams
			Cipher decrypt = Cipher.getInstance(algo);
			decrypt.init(Cipher.DECRYPT_MODE, key);
			// opening streams
			FileInputStream fis = new FileInputStream(path);
			try (CipherInputStream cin = new CipherInputStream(fis, decrypt)) {
				try (FileOutputStream fos = new FileOutputStream(path.substring(0, path.lastIndexOf(".")))) {
					copy(cin, fos);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return path.substring(0, path.lastIndexOf("."));
	}

	// Method Created By: Mahesh Kharote
	// Method Purpose: Get User Id set in session
	@Override
	public String sendUpcomingMailsToExecutor(String subject, int user_id, ArrayList<SendMail> mailsBody) {
		try {
			User user = usersDao.getUserById(user_id);

			/*----------------------------------------Code for generating mail---------------------------------------*/
			String client_tasks_id_for_email_appending = "";
			String email_body = "";

			email_body += "<div style='margin:0 auto;width:100%;height:auto;padding:20px;'>";
			email_body += "<h2 style='font-size:18px;'>Dear Executor,</h2><br>";
			email_body += "<p style='text-align:justify;width:70%;'>The following tasks are required to be attended on or before the due date/s. "
					+ "You are requested to follow the link by clicking on the Task ID and complete the tasks by login to system. Tasks not attended or "
					+ "reported after the statutory date will be reckoned as Non Complied against your name.</p>"
					+ "<h2 style='font-size:16px;font-weight:bold;'>Upcoming Task :</h2>";
			email_body += "<table style='width:80%;' border='1'>" + "<thead>"
					+ "<tr style='background:#0B6EC3;color:#fff;'>" + "<th>SN</th>" + "<th>Task ID</th>"
					+ "<th>Executor Name</th>" + "<th>Entity</th>" + "<th>Location</th>" + "<th>Function</th>"
					+ "<th>Name of Legislation</th>" + "<th>Name of Rule</th>" + "<th>When</th>" + "<th>Activity</th>"
					+ "<th>Frequency</th>" + "<th>Impact</th>" + "<th>Executor Due Date.</th>"
					+ "<th>Evaluator Due Date.</th>" + "<th>Function Head Due Date.</th>"
					+ "<th>Unit Head Due Date.</th>" + "<th>Legal Due Date</th>" + "</tr>" + "</thead>" + "<tbody>";
			int SN = 1;

			Iterator<SendMail> itr = mailsBody.iterator();
			while (itr.hasNext()) {
				SendMail sendMail = (SendMail) itr.next();

				email_body += "<tr>" + "<td>" + SN + "</td>" + "<td><a href=" + url + ">" + sendMail.getClient_task_id()
						+ "</a></td>" + "<td>" + user.getUser_first_name() + " " + user.getUser_last_name() + "</td>"
						+ "<td>" + sendMail.getEntity_name() + "</td>" + "<td>" + sendMail.getUnit_name() + "</td>"
						+ "<td>" + sendMail.getFunction_name() + "</td>" + "<td>" + sendMail.getName_of_legislation()
						+ "</td>" + "<td>" + sendMail.getName_of_rule() + "</td>" + "<td>" + sendMail.getWhen()
						+ "</td>" + "<td>" + sendMail.getActivity() + "</td>" + "<td>" + sendMail.getFrequency()
						+ "</td>" + "<td>" + sendMail.getImpact() + "</td>" + "<td>" + sendMail.getExecutor_date()
						+ "</td>" + "<td>" + sendMail.getEvaluator_date() + "</td>" + "<td>"
						+ sendMail.getFunc_head_date() + "</td>" + "<td>" + sendMail.getUnit_head_date() + "</td>"
						+ "<td>" + sendMail.getLegal_due_date() + "</td>" + "</tr>";

				SN++;
				client_tasks_id_for_email_appending += sendMail.getClient_task_id() + ",";

			}
			email_body += "</tbody>" + "</table>";
			email_body += "<p>This is a system generated mail. Please do not reply to this mail.<br/>"
					+ "In case of any doubt or difficulty, please call Helpdesk as per details given on the support page."
					+ "</p>" + "<h2 style='font-size:18px;font-weight:bold;'>Yours Sincerely</h2>"
					+ "<h2 style='font-size:19px;font-weight:bold;'>Team LexCare</h2>" + "</div>";
			/*---------------------------------------- Code for generating mail ends here -----------------------------*/

			/*-------------------------- Code to send mail ---------------------*/

			sendMainTaskMail(user.getUser_email(), subject, email_body, client_tasks_id_for_email_appending);

			/*----------------------Code to send mail ends here---------------*/
			return "Success";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void sendMainTaskMail(String user_email, String subject, String email_body, String client_tasks_id) {
		System.out.println("In SEND MAIL: " + client_tasks_id);
		try {
			/*----------------------Code to send mail Start here---------------*/
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

			try {

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(mailFrom));
				// message.setRecipients(Message.RecipientType.TO,address);
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(user_email));
				message.setSubject(subject);
				message.setContent(email_body, "text/html; charset=utf-8");
				Transport.send(message);

				schedularDao.updateEmailLog(client_tasks_id, subject);

				EmailLogs logs = new EmailLogs();
				logs.setTasksId(client_tasks_id);
				logs.setMailSentTo(user_email);
				logs.setEmailStatus(subject);
				logs.setCreatedTime(new Date());
				cLogsDao.saveUpcomingMail(logs);

				addMailToLog(user_email, subject, client_tasks_id); // Mail log
			}

			catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error in transport send");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Method Created By: Mahesh Kharote
	// Method Purpose: Get User Id set in session
	@Override
	public String sendEscalationMailsToEvaluator(String subject, int user_id, ArrayList<SendMail> mailsBody) {
		try {
			User user = usersDao.getUserById(user_id);

			/*---------------------------------------- Code for generating mail ---------------------------------------*/
			String client_tasks_id_for_email_appending = "";
			String email_body = "";

			email_body += "<div style='margin:0 auto;width:100%;height:auto;padding:20px;'>";
			email_body += "<h2 style='font-size:18px;'>Dear Evaluator,</h2><br>";
			email_body += "<p style='text-align:justify;width:70%;'>The following compliance activities are not attended and legal due date is "
					+ "approaching. </p>"
					+ "<p style='text-align:justify;width:70%;'>Kindly look into this on priority and ensure the completion of compliance activities. "
					+ "Please follow the link by clicking on the Task ID of respected tasks.</p>"
					+ "<h2 style='font-size:16px;font-weight:bold;'>Not Attended Activities</h2>";
			email_body += "<table style='width:80%;' border='1'> <thead>"
					+ "<tr style='background:#0B6EC3;color:#fff;'> <th>SN</th> <th>Task ID</th>"
					+ "<th>Executor Name</th> <th>Entity</th> <th>Location</th> <th>Function</th>"
					+ "<th>Name of Legislation</th> <th>Name of Rule</th> <th>When</th> <th>Activity</th>"
					+ "<th>Frequency</th> <th>Impact</th> <th>Executor Due Date.</th>"
					+ "<th>Evaluator Due Date.</th> <th>Function Head Due Date.</th>"
					+ "<th>Unit Head Due Date.</th> <th>Legal Due Date</th> </tr> </thead> <tbody>";
			int SN = 1;

			Iterator<SendMail> itr = mailsBody.iterator();
			while (itr.hasNext()) {
				SendMail sendMail = (SendMail) itr.next();
				User pr_user = usersDao.getUserById(sendMail.getExec_id());

				email_body += "<tr>" + "<td>" + SN + "</td>" + "<td><a href=" + url + ">" + sendMail.getClient_task_id()
						+ "</a></td>" + "<td>" + pr_user.getUser_first_name() + " " + pr_user.getUser_last_name()
						+ "</td>" + "<td>" + sendMail.getEntity_name() + "</td>" + "<td>" + sendMail.getUnit_name()
						+ "</td>" + "<td>" + sendMail.getFunction_name() + "</td>" + "<td>"
						+ sendMail.getName_of_legislation() + "</td>" + "<td>" + sendMail.getName_of_rule() + "</td>"
						+ "<td>" + sendMail.getWhen() + "</td>" + "<td>" + sendMail.getActivity() + "</td>" + "<td>"
						+ sendMail.getFrequency() + "</td>" + "<td>" + sendMail.getImpact() + "</td>" + "<td>"
						+ sendMail.getExecutor_date() + "</td>" + "<td>" + sendMail.getEvaluator_date() + "</td>"
						+ "<td>" + sendMail.getFunc_head_date() + "</td>" + "<td>" + sendMail.getUnit_head_date()
						+ "</td>" + "<td>" + sendMail.getLegal_due_date() + "</td>" + "</tr>";

				SN++;
				client_tasks_id_for_email_appending += sendMail.getClient_task_id() + ",";
			}
			email_body += "</tbody>" + "</table>";
			email_body += "<p>This is a system generated mail. Please do not reply to this mail.<br/>"
					+ "In case of any doubt or difficulty, please call Helpdesk as per details given on the support page."
					+ "</p>" + "<h2 style='font-size:18px;font-weight:bold;'>Yours Sincerely</h2>"
					+ "<h2 style='font-size:19px;font-weight:bold;'>Team LexCare</h2>" + "</div>";
			/*----------------------------------------Code for generating mail ends here-----------------------------*/

			/*--------------------------Code to send mail---------------------*/
			sendMainTaskMail(user.getUser_email(), subject, email_body, client_tasks_id_for_email_appending);
			/*----------------------Code to send mail ends here---------------*/
			return "Success";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String sendEscalationMailsToFunctionHead(String subject, int user_id, ArrayList<SendMail> mailsBody) {
		try {
			User user = usersDao.getUserById(user_id);

			/*----------------------------------------Code for generating mail---------------------------------------*/
			String client_tasks_id_for_email_appending = "";
			String email_body = "";

			email_body += "<div style='margin:0 auto;width:100%;height:auto;padding:20px;'>";
			email_body += "<h2 style='font-size:18px;'>Dear Function Head,</h2><br>";
			email_body += "<p style='text-align:justify;width:70%;'>The following compliance activities are not attended and legal due date is approaching. </p>"
					+ "<p style='text-align:justify;width:70%;'>Kindly look into this on priority and ensure the completion of compliance activities. Please follow the link by clicking on the Task ID of respected tasks.</p>"
					+ "<h2 style='font-size:16px;font-weight:bold;'>Not Attended Activities :</h2>";
			email_body += "<table style='width:80%;' border='1'>" + "<thead>"
					+ "<tr style='background:#0B6EC3;color:#fff;'>" + "<th>SN</th>" + "<th>Task ID</th>"
					+ "<th>Executor Name</th>" + "<th>Evaluator Name</th>" + "<th>Entity</th>" + "<th>Location</th>"
					+ "<th>Function</th>" + "<th>Name of Legislation</th>" + "<th>Name of Rule</th>" + "<th>When</th>"
					+ "<th>Activity</th>" + "<th>Frequency</th>" + "<th>Impact</th>" + "<th>Executor Due Date.</th>"
					+ "<th>Evaluator Due Date.</th>" + "<th>Function Head Due Date.</th>"
					+ "<th>Unit Head Due Date.</th>" + "<th>Legal Due Date</th>" + "</tr>" + "</thead>" + "<tbody>";
			int SN = 1;

			Iterator<SendMail> itr = mailsBody.iterator();
			while (itr.hasNext()) {
				SendMail sendMail = (SendMail) itr.next();
				User pr_user = usersDao.getUserById(sendMail.getExec_id());
				User rw_user = usersDao.getUserById(sendMail.getEval_id());

				email_body += "<tr>" + "<td>" + SN + "</td>" + "<td><a href=" + url + ">" + sendMail.getClient_task_id()
						+ "</a></td>" + "<td>" + pr_user.getUser_first_name() + " " + pr_user.getUser_last_name()
						+ "</td>" + "<td>" + rw_user.getUser_first_name() + " " + rw_user.getUser_last_name() + "</td>"
						+ "<td>" + sendMail.getEntity_name() + "</td>" + "<td>" + sendMail.getUnit_name() + "</td>"
						+ "<td>" + sendMail.getFunction_name() + "</td>" + "<td>" + sendMail.getName_of_legislation()
						+ "</td>" + "<td>" + sendMail.getName_of_rule() + "</td>" + "<td>" + sendMail.getWhen()
						+ "</td>" + "<td>" + sendMail.getActivity() + "</td>" + "<td>" + sendMail.getFrequency()
						+ "</td>" + "<td>" + sendMail.getImpact() + "</td>" + "<td>" + sendMail.getExecutor_date()
						+ "</td>" + "<td>" + sendMail.getEvaluator_date() + "</td>" + "<td>"
						+ sendMail.getFunc_head_date() + "</td>" + "<td>" + sendMail.getUnit_head_date() + "</td>"
						+ "<td>" + sendMail.getLegal_due_date() + "</td>" + "</tr>";

				SN++;
				client_tasks_id_for_email_appending += sendMail.getClient_task_id() + ",";
			}
			email_body += "</tbody>" + "</table>";
			email_body += "<p>This is a system generated mail. Please do not reply to this mail.<br/>"
					+ "In case of any doubt or difficulty, please call Helpdesk as per details given on the support page."
					+ "</p>" + "<h2 style='font-size:18px;font-weight:bold;'>Yours Sincerely</h2>"
					+ "<h2 style='font-size:19px;font-weight:bold;'>Team LexCare</h2>" + "</div>";
			/*----------------------------------------Code for generating mail ends here-----------------------------*/

			/*--------------------------Code to send mail---------------------*/
			sendMainTaskMail(user.getUser_email(), subject, email_body, client_tasks_id_for_email_appending);
			/*----------------------Code to send mail ends here---------------*/
			return "Success";

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Created By: Mahesh Kharote
	// Method Purpose: Get Current date
	@Override
	public void addMailToLog(String username, String subject, String ClienttaskIds) {

		File dir = new File(
				"C:" + File.separator + "CMS" + File.separator + projectName + File.separator + "EmailLogs");
		if (!dir.exists())
			dir.mkdirs();

		File newFile = new File(dir.getPath() + File.separator + "logs.txt");
		if (!newFile.exists()) {
			try {
				newFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try (FileWriter fw = new FileWriter("C:" + File.separator + "CMS" + File.separator + projectName
				+ File.separator + "EmailLogs" + File.separator + "logs.txt", true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			out.println("mail sent to: " + username);
			// more code
			java.util.Date date = new java.util.Date();
			out.println("Date and Time: " + new Timestamp(date.getTime()));
			// more code
			out.println("Subject: " + subject);
			out.println("Task Id : " + ClienttaskIds);

			EmailLogs logs = new EmailLogs();
			logs.setTasksId(ClienttaskIds);
			logs.setMailSentTo(username);
			logs.setEmailStatus(subject);
			logs.setCreatedTime(new Date());
			/*
			 * if (httpSession.getAttribute("sess_user_full_name") != null) {
			 * logs.setCreatedBy(httpSession.getAttribute("sess_user_full_name").toString())
			 * ; }
			 */
			cLogsDao.saveUpcomingMail(logs);

			out.println("-------------------------------------------------");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String sendTaskCompletionMailToEvaluator(ArrayList<TaskTransactional> taskTransactionals, String relatedTo) {
		try {

			Iterator<TaskTransactional> itr = taskTransactionals.iterator();
			while (itr.hasNext()) {
				TaskTransactional taskTransactional = (TaskTransactional) itr.next();
				String rev_email = " ";
				String taskIds = " ";
				String enti_name = "";
				String Unit = "";
				String Function = "";
				List<Object> taskList = schedularDao.getTaskDetailsToSend(taskTransactional.getTtrn_id(), relatedTo);
				String email_body = "";

				email_body += "<div style='margin:0 auto;width:100%;height:auto;padding:20px;'>";
				email_body += "<h2 style='font-size:18px;'>Dear Evaluator,</h2>";
				email_body += "<p style='text-align:justify;width:70%;'>The following compliance activities are completed. </p>"
						+ "<p style='text-align:justify;width:70%;'>Please evaluate the tasks mentioned herewith.</p>"
						+ "<h2 style='font-size:16px;font-weight:bold;'>Attended Activities</h2>";
				email_body += "<table style='width:80%;' border='1'>" + "<thead>"
						+ "<tr style='background:#0B6EC3;color:#fff;'>" + "<th>SN</th>" + "<th>Task ID</th>"
						+ "<th>Entity</th>" + "<th>Unit</th>" + "<th>Function</th>" + "<th>Legislation</th>"
						+ "<th>Rule</th>" + "<th>When</th>" + "<th>Activity</th>" + "<th>Frequency</th>"
						+ "<th>Impact</th>" + "<th>Executor</th>" + "<th>Evaluator</th>" + "<th>Executor Due Date.</th>"
						+ "<th>Evaluator Due Date.</th>" + "<th>Completed by</th>" + "<th>Completed Date.</th>"
						+ "<th>Legal Due Date</th>" + "</tr>" + "</thead>" + "<tbody>";
				int SN = 1;
				Iterator<Object> iterator = taskList.iterator();
				while (iterator.hasNext()) {
					Object[] objects = (Object[]) iterator.next();

					Date legalDueDate;
					Date uhDueDate;
					Date fhDueDate;
					Date revDueDate;
					Date perDueDate;
					Date complet_date;
					try {

						legalDueDate = sdfIn.parse(objects[15].toString());
						objects[15] = sdfOut.format(legalDueDate);

						uhDueDate = sdfIn.parse(objects[14].toString());
						objects[14] = sdfOut.format(uhDueDate);

						fhDueDate = sdfIn.parse(objects[13].toString());
						objects[13] = sdfOut.format(fhDueDate);

						revDueDate = sdfIn.parse(objects[12].toString());
						objects[12] = sdfOut.format(revDueDate);

						perDueDate = sdfIn.parse(objects[11].toString());
						objects[11] = sdfOut.format(perDueDate);

						complet_date = sdfIn.parse(objects[16].toString());
						objects[16] = sdfOut.format(complet_date);

					} catch (ParseException e) {
						e.printStackTrace();
					}

					String task_id = objects[1].toString();
					String orga_name = objects[2].toString();
					String loca_name = objects[3].toString();
					String dept_name = objects[4].toString();
					String legi_name = objects[5].toString();
					String rule_name = objects[6].toString();
					String activity = objects[7].toString();
					String impact = objects[8].toString();
					String when = objects[9].toString();
					String Frequency = objects[10].toString();
					String per_date = objects[11].toString();
					String rev_date = objects[12].toString();
					// String fun_date = objects[13].toString();
					// String unit_date = objects[14].toString();
					String legal_date = objects[15].toString();
					String completed_date = objects[16].toString();

					int com_by_id = Integer.parseInt(objects[17].toString());
					int pr_id = Integer.parseInt(objects[18].toString());
					int rw_id = Integer.parseInt(objects[19].toString());

					// String pr_email = objects[21].toString();

					User userper = usersDao.getUserById(pr_id);
					User userrev = usersDao.getUserById(rw_id);
					User usercom = usersDao.getUserById(com_by_id);

					String per_name = userper.getUser_first_name() + " " + userper.getUser_last_name();
					String rev_name = userrev.getUser_first_name() + " " + userrev.getUser_last_name();
					String comp_by = usercom.getUser_first_name() + " " + usercom.getUser_last_name();

					rev_email = userrev.getUser_email();

					taskIds = task_id;
					email_body += "<tr>" + "<td>" + SN + "</td>" + "<td><a href=" + url + ">" + task_id + "</a></td>"
							+ "<td>" + orga_name + "</td>" + "<td>" + loca_name + "</td>" + "<td>" + dept_name + "</td>"
							+ "<td>" + legi_name + "</td>" + "<td>" + rule_name + "</td>" + "<td>" + when + "</td>"
							+ "<td>" + activity + "</td>" + "<td>" + Frequency + "</td>" + "<td>" + impact + "</td>"
							+ "<td>" + per_name + "</td>" + "<td>" + rev_name + "</td>" + "<td>" + per_date + "</td>"
							+ "<td>" + rev_date + "</td>" + "<td>" + comp_by + "</td>" + "<td>" + completed_date
							+ "</td>" + "<td>" + legal_date + "</td>"

							+ "</tr>";
					SN++;
					enti_name = orga_name;
					Unit = loca_name;
					Function = dept_name;
				}

				email_body += "</tbody>" + "</table>";
				email_body += "<p>This is a system generated mail. Please do not reply to this mail.<br/>In case of any doubt or difficulty, please call Helpdesk as per details given on the home page.</p>"
						+ "<h2 style='font-size:18px;font-weight:bold;'>Yours Sincerely</h2>"
						+ "<h2 style='font-size:19px;font-weight:bold;'>Team LexCare</h2>" + "</div>";

				String email_subject = "Task Completion Alert_" + enti_name + "_" + Unit + "_" + Function;

				/*--------------------------Code to send mail---------------------*/
				sendMail(rev_email, email_subject, email_body, taskIds);
				/*----------------------Code to send mail ends here---------------*/
				return "Success";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Harshad Padole
	// Method Purpose: sent escalation mail to unit head
	@Override
	public String sendEscalationMailsUnitHead(String subject, String user_mail, ArrayList<SendMail> mailsBody) {
		try {
			String client_tasks_id_for_email_appending = "";
			String email_body = "";

			email_body += "<div style='margin:0 auto;width:100%;height:auto;padding:20px;'>";
			email_body += "<h2 style='font-size:18px;'>Dear Unit Head,</h2>";
			email_body += "<p style='text-align:justify;width:70%;'>The following compliance activities are not attended and legal due date is approaching. </p>"
					+ "<p style='text-align:justify;width:70%;'>Kindly look into this on priority and ensure the completion of compliance activities. Please follow the link by clicking on the Task ID of respected tasks.</p>"
					+ "<h2 style='font-size:16px;font-weight:bold;'>Not Attended Activities :</h2>";

			email_body += "<table style='width:80%;' border='1'>" + "<thead>"
					+ "<tr style='background:#0B6EC3;color:#fff;'>" + "<th>SN</th>" + "<th>Task ID</th>"
					+ "<th>Executor Name</th>" + "<th>Evaluator Name</th>" + "<th>Entity</th>" + "<th>Location</th>"
					+ "<th>Function</th>" + "<th>Name of Legislation</th>" + "<th>Name of Rule</th>" + "<th>When</th>"
					+ "<th>Activity</th>" + "<th>Frequency</th>" + "<th>Impact</th>" + "<th>Executor Due Date.</th>"
					+ "<th>Evaluator Due Date.</th>" + "<th>Function Head Due Date.</th>"
					+ "<th>Unit Head Due Date.</th>" + "<th>Legal Due Date</th>" + "</tr>" + "</thead>" + "<tbody>";
			int SN = 1;

			Iterator<SendMail> itr = mailsBody.iterator();
			while (itr.hasNext()) {
				SendMail sendMail = (SendMail) itr.next();
				User pr_user = usersDao.getUserById(sendMail.getExec_id());
				User rw_user = usersDao.getUserById(sendMail.getEval_id());

				email_body += "<tr>" + "<td>" + SN + "</td>" + "<td><a href=" + url + ">" + sendMail.getClient_task_id()
						+ "</a></td>" + "<td>" + pr_user.getUser_first_name() + " " + pr_user.getUser_last_name()
						+ "</td>" + "<td>" + rw_user.getUser_first_name() + " " + rw_user.getUser_last_name() + "</td>"
						+ "<td>" + sendMail.getEntity_name() + "</td>" + "<td>" + sendMail.getUnit_name() + "</td>"
						+ "<td>" + sendMail.getFunction_name() + "</td>" + "<td>" + sendMail.getName_of_legislation()
						+ "</td>" + "<td>" + sendMail.getName_of_rule() + "</td>" + "<td>" + sendMail.getWhen()
						+ "</td>" + "<td>" + sendMail.getActivity() + "</td>" + "<td>" + sendMail.getFrequency()
						+ "</td>" + "<td>" + sendMail.getImpact() + "</td>" + "<td>" + sendMail.getExecutor_date()
						+ "</td>" + "<td>" + sendMail.getEvaluator_date() + "</td>" + "<td>"
						+ sendMail.getFunc_head_date() + "</td>" + "<td>" + sendMail.getUnit_head_date() + "</td>"
						+ "<td>" + sendMail.getLegal_due_date() + "</td>" + "</tr>";

				SN++;
				client_tasks_id_for_email_appending += sendMail.getClient_task_id() + ",";
			}
			email_body += "</tbody>" + "</table>";
			email_body += "<p>This is a system generated mail. Please do not reply to this mail.<br/>"
					+ "In case of any doubt or difficulty, please call Helpdesk as per details given on the support page."
					+ "</p>" + "<h2 style='font-size:18px;font-weight:bold;'>Yours Sincerely</h2>"
					+ "<h2 style='font-size:19px;font-weight:bold;'>Team LexCare</h2>" + "</div>";
			/*----------------------------------------Code for generating mail ends here-----------------------------*/

			/*--------------------------Code to send mail---------------------*/
			sendMainTaskMail(user_mail, subject, email_body, client_tasks_id_for_email_appending);
			/*----------------------Code to send mail ends here---------------*/
			return "Success";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Harshad Padole
	// Method Purpose: sent escalation mail to Entity head
	@Override
	public String sendEscalationMailsEntityHead(String subject, String user_mail, ArrayList<SendMail> mailsBody) {

		try {
			String client_tasks_id_for_email_appending = "";
			String email_body = "";

			email_body += "<div style='margin:0 auto;width:100%;height:auto;padding:20px;'>";
			email_body += "<h2 style='font-size:18px;'>Dear Entity Head,</h2>";
			email_body += "<p style='text-align:justify;width:70%;'>The following compliance activities are not attended and legal due date is approaching. </p>"
					+ "<p style='text-align:justify;width:70%;'>Kindly look into this on priority and ensure the completion of compliance activities. Please follow the link by clicking on the Task ID of respected tasks.</p>"
					+ "<h2 style='font-size:16px;font-weight:bold;'>Not Attended Activities :</h2>";

			email_body += "<table style='width:80%;' border='1'>" + "<thead>"
					+ "<tr style='background:#0B6EC3;color:#fff;'>" + "<th>SN</th>" + "<th>Task ID</th>"
					+ "<th>Executor Name</th>" + "<th>Evaluator Name</th>" + "<th>Entity</th>" + "<th>Location</th>"
					+ "<th>Function</th>" + "<th>Name of Legislation</th>" + "<th>Name of Rule</th>" + "<th>When</th>"
					+ "<th>Activity</th>" + "<th>Frequency</th>" + "<th>Impact</th>" + "<th>Executor Due Date.</th>"
					+ "<th>Evaluator Due Date.</th>" + "<th>Function Head Due Date.</th>"
					+ "<th>Unit Head Due Date.</th>" + "<th>Legal Due Date</th>" + "</tr>" + "</thead>" + "<tbody>";
			int SN = 1;

			Iterator<SendMail> itr = mailsBody.iterator();
			while (itr.hasNext()) {
				SendMail sendMail = (SendMail) itr.next();
				User pr_user = usersDao.getUserById(sendMail.getExec_id());
				User rw_user = usersDao.getUserById(sendMail.getEval_id());

				email_body += "<tr>" + "<td>" + SN + "</td>" + "<td><a href=" + url + ">" + sendMail.getClient_task_id()
						+ "</a></td>" + "<td>" + pr_user.getUser_first_name() + " " + pr_user.getUser_last_name()
						+ "</td>" + "<td>" + rw_user.getUser_first_name() + " " + rw_user.getUser_last_name() + "</td>"
						+ "<td>" + sendMail.getEntity_name() + "</td>" + "<td>" + sendMail.getUnit_name() + "</td>"
						+ "<td>" + sendMail.getFunction_name() + "</td>" + "<td>" + sendMail.getName_of_legislation()
						+ "</td>" + "<td>" + sendMail.getName_of_rule() + "</td>" + "<td>" + sendMail.getWhen()
						+ "</td>" + "<td>" + sendMail.getActivity() + "</td>" + "<td>" + sendMail.getFrequency()
						+ "</td>" + "<td>" + sendMail.getImpact() + "</td>" + "<td>" + sendMail.getExecutor_date()
						+ "</td>" + "<td>" + sendMail.getEvaluator_date() + "</td>" + "<td>"
						+ sendMail.getFunc_head_date() + "</td>" + "<td>" + sendMail.getUnit_head_date() + "</td>"
						+ "<td>" + sendMail.getLegal_due_date() + "</td>" + "</tr>";

				SN++;
				client_tasks_id_for_email_appending += sendMail.getClient_task_id() + ",";
			}
			email_body += "</tbody>" + "</table>";
			email_body += "<p>This is a system generated mail. Please do not reply to this mail.<br/>"
					+ "In case of any doubt or difficulty, please call Helpdesk as per details given on the support page."
					+ "</p>" + "<h2 style='font-size:18px;font-weight:bold;'>Yours Sincerely</h2>"
					+ "<h2 style='font-size:19px;font-weight:bold;'>Team LexCare</h2>" + "</div>";
			/*----------------------------------------Code for generating mail ends here-----------------------------*/

			/*--------------------------Code to send mail---------------------*/
			sendMainTaskMail(user_mail, subject, email_body, client_tasks_id_for_email_appending);
			/*----------------------Code to send mail ends here---------------*/
			return "Success";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Harshad Padole
	// Method Purpose: sent task reopened mail to Executor
	@Override
	public String sendTaskReopenMailToExecutor(int ttrn_id, String comment, String task_type) {
		try {

			String rev_email = "";
			String per_email = "";
			String taskIds = "";
			String enti_name = "";
			String Unit = "";
			String Function = "";
			List<Object> taskList = schedularDao.getTaskDetailsToSend(ttrn_id, task_type);
			String email_body = "";

			email_body += "<div style='margin:0 auto;width:100%;height:auto;padding:20px;'>";
			email_body += "<h2 style='font-size:18px;'>Dear Executor,</h2>";
			email_body += "<p style='text-align:justify;width:70%;'>The following task has been re-opened because of the reason mentioned below.This task has to be attended on or before the due date/s. You are requested to follow the link by clicking on the Task ID and complete the tasks by login to system. Tasks not attended or reported after the statutory date will be reckoned as Non Complied against your name.</p>"
					+ "<h2 style='font-size:16px;font-weight:bold;'>Task Re-Opened Alert :</h2>";
			email_body += "<table style='width:80%;' border='1'>" + "<thead>"
					+ "<tr style='background:#0B6EC3;color:#fff;'>" + "<th>SN</th>" + "<th>Task ID</th>"
					+ "<th>Entity</th>" + "<th>Unit</th>" + "<th>Function</th>" + "<th>Legislation</th>"
					+ "<th>Rule</th>" + "<th>When</th>" + "<th>Activity</th>" + "<th>Frequency</th>" + "<th>Impact</th>"
					+ "<th>Executor</th>"
					// + "<th>Evaluator</th>"
					+ "<th>Executor Due Date.</th>" + "<th>Legal Due Date</th>" + "<th>Reason for Re-Open Task</th>"
					+ "</tr>" + "</thead>" + "<tbody>";
			int SN = 1;
			Iterator<Object> iterator = taskList.iterator();
			while (iterator.hasNext()) {
				Object[] objects = (Object[]) iterator.next();

				Date legalDueDate;
				/*
				 * Date uhDueDate; Date fhDueDate; Date revDueDate;
				 */
				Date perDueDate;
				// Date complet_date;
				try {

					legalDueDate = sdfIn.parse(objects[15].toString());
					objects[15] = sdfOut.format(legalDueDate);

					/*
					 * uhDueDate = sdfIn.parse(objects[14].toString()); objects[14] =
					 * sdfOut.format(uhDueDate);
					 * 
					 * fhDueDate = sdfIn.parse(objects[13].toString()); objects[13] =
					 * sdfOut.format(fhDueDate);
					 * 
					 * revDueDate = sdfIn.parse(objects[12].toString()); objects[12] =
					 * sdfOut.format(revDueDate);
					 */

					perDueDate = sdfIn.parse(objects[11].toString());
					objects[11] = sdfOut.format(perDueDate);

					// complet_date = sdfIn.parse(objects[16].toString());
					// objects[16] = sdfOut.format(complet_date);

				} catch (ParseException e) {
					e.printStackTrace();
				}

				String task_id = objects[1].toString();
				String orga_name = objects[2].toString();
				String loca_name = objects[3].toString();
				String dept_name = objects[4].toString();
				String legi_name = objects[5].toString();
				String rule_name = objects[6].toString();
				String activity = objects[7].toString();
				String impact = objects[8].toString();
				String when = objects[9].toString();
				String Frequency = objects[10].toString();
				String per_date = objects[11].toString();
				// String rev_date = objects[12].toString();
				// String fun_date = objects[13].toString();
				// String unit_date = objects[14].toString();
				String legal_date = objects[15].toString();
				// String completed_date = objects[16].toString();

				// int com_by_id = Integer.parseInt(objects[17].toString());
				int pr_id = Integer.parseInt(objects[18].toString());
				int rw_id = Integer.parseInt(objects[19].toString());

				// String pr_email = objects[21].toString();

				User userper = usersDao.getUserById(pr_id);
				User userrev = usersDao.getUserById(rw_id);
				// User usercom = usersDao.getUserById(com_by_id);

				String per_name = userper.getUser_first_name() + " " + userper.getUser_last_name();
				per_email = userper.getUser_email();
				// String rev_name = userrev.getUser_first_name()+"
				// "+userrev.getUser_last_name();
				// String comp_by = usercom.getUser_first_name()+"
				// "+usercom.getUser_last_name();

				rev_email = userrev.getUser_email();

				taskIds = task_id;
				email_body += "<tr>" + "<td>" + SN + "</td>" + "<td><a href=" + url + ">" + task_id + "</a></td>"
						+ "<td>" + orga_name + "</td>" + "<td>" + loca_name + "</td>" + "<td>" + dept_name + "</td>"
						+ "<td>" + legi_name + "</td>" + "<td>" + rule_name + "</td>" + "<td>" + when + "</td>" + "<td>"
						+ activity + "</td>" + "<td>" + Frequency + "</td>" + "<td>" + impact + "</td>" + "<td>"
						+ per_name + "</td>"
						// + "<td>"+rev_name+"</td>"
						+ "<td>" + per_date + "</td>"
						// + "<td>"+rev_date+"</td>"
						+ "<td>" + legal_date + "</td>" + "<td>" + comment + "</td>"

						+ "</tr>";
				SN++;
				enti_name = orga_name;
				Unit = loca_name;
				Function = dept_name;
			}

			email_body += "</tbody>" + "</table>";
			email_body += "<p>This is a system generated mail. Please do not reply to this mail.<br/>In case of any doubt or difficulty, please call Helpdesk as per details given on the home page.</p>"
					+ "<h2 style='font-size:18px;font-weight:bold;'>Yours Sincerely</h2>"
					+ "<h2 style='font-size:19px;font-weight:bold;'>Team LexCare</h2>" + "</div>";

			String email_subject = "Task Re-Opened Alert_" + enti_name + "_" + Unit + "_" + Function;

			/*----------------------Code to send mail Start here---------------*/
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

			try {

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(mailFrom));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(per_email));
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(rev_email));
				message.setSubject(email_subject);
				message.setContent(email_body, "text/html; charset=utf-8");
				Transport.send(message);
				addMailToLog(per_email, email_subject, taskIds); // Mail log
				System.out.println("Done");
				return "Success";
			}

			catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error in transport send");
				return "Fail";
			}
			/*----------------------Code to send mail ends here---------------*/

		} catch (Exception e) {
			e.printStackTrace();
			return "Fail";
		}
	}

	// Method Created By: Harshad Padole
	// Method Purpose: Task deletion log
	@Override
	public void addTaskDeletionLog(String ids, String relatedTo, int user_id, String user_name, String clientTaskID,
			String lexcareTaskId) {
		try {

			File dir = new File("C:" + File.separator + "CMS" + File.separator + projectName + File.separator + "Logs");
			if (!dir.exists())
				dir.mkdirs();

			File newFile = new File(dir.getPath() + File.separator + "TaskDeletion.txt");
			if (!newFile.exists()) {
				try {
					newFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try (FileWriter fw = new FileWriter("C:" + File.separator + "CMS" + File.separator + projectName
					+ File.separator + "Logs" + File.separator + "TaskDeletion.txt", true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw)) {
				out.println("User Id: " + user_id);
				out.println("User Name: " + user_name);
				java.util.Date date = new java.util.Date();
				out.println("Date and Time: " + new Timestamp(date.getTime()));
				out.println("Related To: " + relatedTo);
				out.println("Tmap Id : " + ids);
				out.println("Client Task Id : " + clientTaskID);
				out.println("Lexcare Task Id : " + lexcareTaskId);
				out.println("-------------------------------------------------");
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Method Created By: Harshad Padole
	// Method Purpose: task activation log
	@Override
	public void addTaskActivationLog(ArrayList<Integer> ids, String taskStatus, int user_id, String user_name) {
		try {

			File dir = new File("C:" + File.separator + "CMS" + File.separator + projectName + File.separator + "Logs");
			if (!dir.exists())
				dir.mkdirs();

			File newFile = new File(dir.getPath() + File.separator + "TaskActivation.txt");
			if (!newFile.exists()) {
				try {
					newFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try (FileWriter fw = new FileWriter("C:" + File.separator + "CMS" + File.separator + projectName
					+ File.separator + "Logs" + File.separator + "TaskActivation.txt", true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw)) {
				String ttrn_ids = "";

				for (int i = 0; i < ids.size(); i++) {
					ttrn_ids += ids.get(i) + ",";

				}

				out.println("User Id: " + user_id);
				out.println("User Name: " + user_name);
				java.util.Date date = new java.util.Date();
				out.println("Date and Time: " + new Timestamp(date.getTime()));
				out.println("Task Status: " + taskStatus);
				out.println("Ttrn Id : " + ttrn_ids);
				out.println("-------------------------------------------------");
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Method Created By: Harshad Padole
	// Method Purpose: task enable/disable log
	@Override
	public void addTaskEnableLog(ArrayList<Integer> ids, String taskStatus, int user_id, String user_name) {

		try {

			File dir = new File("C:" + File.separator + "CMS" + File.separator + projectName + File.separator + "Logs");
			if (!dir.exists())
				dir.mkdirs();

			File newFile = new File(dir.getPath() + File.separator + "TaskEnable.txt");
			if (!newFile.exists()) {
				try {
					newFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try (FileWriter fw = new FileWriter("C:" + File.separator + "CMS" + File.separator + projectName
					+ File.separator + "Logs" + File.separator + "TaskEnable.txt", true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw)) {
				String ttrn_ids = "";
				for (int i = 0; i < ids.size(); i++) {
					ttrn_ids += ids.get(i) + ",";
				}
				out.println("User Id: " + user_id);
				out.println("User Name: " + user_name);
				java.util.Date date = new java.util.Date();
				out.println("Date and Time: " + new Timestamp(date.getTime()));
				out.println("Task Status: " + taskStatus);
				out.println("Tmap Ids : " + ttrn_ids);
				out.println("-------------------------------------------------");
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Method Created By: Harshad Padole
	// Method Purpose: task change compliance owner log
	@Override
	public void addChangeComplianceOwnerLog(int tmap_id, String previous, String changed, int user_id,
			String user_name) {

		try {

			File dir = new File("C:" + File.separator + "CMS" + File.separator + projectName + File.separator + "Logs");
			if (!dir.exists())
				dir.mkdirs();

			File newFile = new File(dir.getPath() + File.separator + "ChangeComplianceOwner.txt");
			if (!newFile.exists()) {
				try {
					newFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try (FileWriter fw = new FileWriter("C:" + File.separator + "CMS" + File.separator + projectName
					+ File.separator + "Logs" + File.separator + "ChangeComplianceOwner.txt", true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw)) {

				out.println("User Id: " + user_id);
				out.println("User Name: " + user_name);
				java.util.Date date = new java.util.Date();
				out.println("Date and Time: " + new Timestamp(date.getTime()));
				out.println("tmap_id: " + tmap_id);
				out.println("Old Owner: " + previous);
				out.println("New Owner: " + changed);
				out.println("-------------------------------------------------");
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Method Created By: Harshad Padole
	// Method Purpose: Task Assigned log
	@Override
	public void addTaskAssignedLog(String assigned, int user_id, String user_name) {

		try {

			File dir = new File("C:" + File.separator + "CMS" + File.separator + projectName + File.separator + "Logs");
			if (!dir.exists())
				dir.mkdirs();

			File newFile = new File(dir.getPath() + File.separator + "TaskAssinged.txt");
			if (!newFile.exists()) {
				try {
					newFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try (FileWriter fw = new FileWriter("C:" + File.separator + "CMS" + File.separator + projectName
					+ File.separator + "Logs" + File.separator + "TaskAssinged.txt", true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw)) {

				out.println("User Id: " + user_id);
				out.println("User Name: " + user_name);
				java.util.Date date = new java.util.Date();
				out.println("Date and Time: " + new Timestamp(date.getTime()));
				out.println("Assigned To: " + assigned);
				out.println("-------------------------------------------------");
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void addTaskCofigurationLog(String clientTaskId, int user_id, String user_name, String relatedTo,
			String Action) {

		try {

			File dir = new File("C:" + File.separator + "CMS" + File.separator + projectName + File.separator + "Logs");
			if (!dir.exists())
				dir.mkdirs();

			File newFile = new File(dir.getPath() + File.separator + "TaskConfiguration.txt");
			if (!newFile.exists()) {
				try {
					newFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try (FileWriter fw = new FileWriter("C:" + File.separator + "CMS" + File.separator + projectName
					+ File.separator + "Logs" + File.separator + "TaskConfiguration.txt", true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw)) {

				out.println("User Id: " + user_id);
				out.println("User Name: " + user_name);
				java.util.Date date = new java.util.Date();
				out.println("Date and Time: " + new Timestamp(date.getTime()));
				out.println("Related To: " + relatedTo);
				out.println("Action: " + Action);
				out.println("Client Task Id: " + clientTaskId);
				out.println("-------------------------------------------------");
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void addLegalUpdateLog(String clientTaskIds, int user_id, String user_name, String relatedTo,
			String Action) {

		try {

			File dir = new File("C:" + File.separator + "CMS" + File.separator + projectName + File.separator + "Logs");
			if (!dir.exists())
				dir.mkdirs();

			File newFile = new File(dir.getPath() + File.separator + "TaskLegalUpdate.txt");
			if (!newFile.exists()) {
				try {
					newFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try (FileWriter fw = new FileWriter("C:" + File.separator + "CMS" + File.separator + projectName
					+ File.separator + "Logs" + File.separator + "TaskLegalUpdate.txt", true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw)) {

				out.println("User Id: " + user_id);
				out.println("User Name: " + user_name);
				java.util.Date date = new java.util.Date();
				out.println("Date and Time: " + new Timestamp(date.getTime()));
				out.println("Related To: " + relatedTo);
				out.println("Action: " + Action);
				out.println("Client Task Id: " + clientTaskIds);
				out.println("-------------------------------------------------");
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void addRemoveUserAccessLog(int user_id, String user_name, String orga_loca_dept, String action) {

		try {

			File dir = new File("C:" + File.separator + "CMS" + File.separator + projectName + File.separator + "Logs");
			if (!dir.exists())
				dir.mkdirs();

			File newFile = new File(dir.getPath() + File.separator + "UserAccessRemove.txt");
			if (!newFile.exists()) {
				try {
					newFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try (FileWriter fw = new FileWriter("C:" + File.separator + "CMS" + File.separator + projectName
					+ File.separator + "Logs" + File.separator + "UserAccessRemove.txt", true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw)) {

				out.println("User Id: " + user_id);
				out.println("User Name: " + user_name);
				java.util.Date date = new java.util.Date();
				out.println("Date and Time: " + new Timestamp(date.getTime()));
				out.println("Action: " + action);
				out.println("umap Details: " + orga_loca_dept);
				out.println("-------------------------------------------------");
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Method Written By: Harshad Padole
	// Method Purpose: send sub task completion mail
	@Override
	public String sendSubTaskCompletionMailToEvaluator(SubTaskTranscational subTaskTranscational) {
		try {

			String rev_email = " ";
			String taskIds = " ";
			String enti_name = "";
			String Unit = "";
			String Function = "";
			List<Object> taskList = schedularDao.getTaskDetailsToSend(subTaskTranscational.getTtrn_sub_id(), "SubTask");
			String email_body = "";

			email_body += "<div style='margin:0 auto;width:100%;height:auto;padding:20px;'>";
			email_body += "<h2 style='font-size:18px;'>Dear Evaluator,</h2>";
			email_body += "<p style='text-align:justify;width:70%;'>The following compliance activities are completed. </p>"
					+ "<p style='text-align:justify;width:70%;'>Please evaluate the tasks mentioned herewith.</p>"
					+ "<h2 style='font-size:16px;font-weight:bold;'>Attended Activities</h2>";
			email_body += "<table style='width:80%;' border='1'>" + "<thead>"
					+ "<tr style='background:#0B6EC3;color:#fff;'>" + "<th>SN</th>" + "<th>Task ID</th>"
					+ "<th>Entity</th>" + "<th>Unit</th>" + "<th>Function</th>" + "<th>Legislation</th>"
					+ "<th>Rule</th>" + "<th>When</th>" + "<th>Activity</th>" + "<th>Frequency</th>" + "<th>Impact</th>"
					+ "<th>Executor</th>" + "<th>Evaluator</th>" + "<th>Executor Due Date.</th>"
					+ "<th>Evaluator Due Date.</th>" + "<th>Completed by</th>" + "<th>Completed Date.</th>"
					+ "<th>Legal Due Date</th>" + "</tr>" + "</thead>" + "<tbody>";
			int SN = 1;
			Iterator<Object> iterator = taskList.iterator();
			while (iterator.hasNext()) {
				Object[] objects = (Object[]) iterator.next();

				Date legalDueDate;
				Date uhDueDate;
				Date fhDueDate;
				Date revDueDate;
				Date perDueDate;
				Date complet_date;
				try {

					legalDueDate = sdfIn.parse(objects[15].toString());
					objects[15] = sdfOut.format(legalDueDate);

					uhDueDate = sdfIn.parse(objects[14].toString());
					objects[14] = sdfOut.format(uhDueDate);

					fhDueDate = sdfIn.parse(objects[13].toString());
					objects[13] = sdfOut.format(fhDueDate);

					revDueDate = sdfIn.parse(objects[12].toString());
					objects[12] = sdfOut.format(revDueDate);

					perDueDate = sdfIn.parse(objects[11].toString());
					objects[11] = sdfOut.format(perDueDate);

					complet_date = sdfIn.parse(objects[16].toString());
					objects[16] = sdfOut.format(complet_date);

				} catch (ParseException e) {
					e.printStackTrace();
				}

				String task_id = objects[1].toString();
				String orga_name = objects[2].toString();
				String loca_name = objects[3].toString();
				String dept_name = objects[4].toString();
				String legi_name = objects[5].toString();
				String rule_name = objects[6].toString();
				String activity = objects[7].toString();
				String impact = objects[8].toString();
				String when = objects[9].toString();
				String Frequency = objects[10].toString();
				String per_date = objects[11].toString();
				String rev_date = objects[12].toString();
				// String fun_date = objects[13].toString();
				// String unit_date = objects[14].toString();
				String legal_date = objects[15].toString();
				String completed_date = objects[16].toString();

				int com_by_id = Integer.parseInt(objects[17].toString());
				int pr_id = Integer.parseInt(objects[18].toString());
				int rw_id = Integer.parseInt(objects[19].toString());

				// String pr_email = objects[21].toString();

				User userper = usersDao.getUserById(pr_id);
				User userrev = usersDao.getUserById(rw_id);
				User usercom = usersDao.getUserById(com_by_id);

				String per_name = userper.getUser_first_name() + " " + userper.getUser_last_name();
				String rev_name = userrev.getUser_first_name() + " " + userrev.getUser_last_name();
				String comp_by = usercom.getUser_first_name() + " " + usercom.getUser_last_name();

				rev_email = userrev.getUser_email();

				taskIds = task_id;
				email_body += "<tr>" + "<td>" + SN + "</td>" + "<td><a href=" + url + ">" + task_id + "</a></td>"
						+ "<td>" + orga_name + "</td>" + "<td>" + loca_name + "</td>" + "<td>" + dept_name + "</td>"
						+ "<td>" + legi_name + "</td>" + "<td>" + rule_name + "</td>" + "<td>" + when + "</td>" + "<td>"
						+ activity + "</td>" + "<td>" + Frequency + "</td>" + "<td>" + impact + "</td>" + "<td>"
						+ per_name + "</td>" + "<td>" + rev_name + "</td>" + "<td>" + per_date + "</td>" + "<td>"
						+ rev_date + "</td>" + "<td>" + comp_by + "</td>" + "<td>" + completed_date + "</td>" + "<td>"
						+ legal_date + "</td>"

						+ "</tr>";
				SN++;
				enti_name = orga_name;
				Unit = loca_name;
				Function = dept_name;
			}

			email_body += "</tbody>" + "</table>";
			email_body += "<p>This is a system generated mail. Please do not reply to this mail.<br/>In case of any doubt or difficulty, please call Helpdesk as per details given on the home page.</p>"
					+ "<h2 style='font-size:18px;font-weight:bold;'>Yours Sincerely</h2>"
					+ "<h2 style='font-size:19px;font-weight:bold;'>Team LexCare</h2>" + "</div>";

			String email_subject = "Task Completion Alert_" + enti_name + "_" + Unit + "_" + Function;

			/*--------------------------Code to send mail---------------------*/
			sendMail(rev_email, email_subject, email_body, taskIds);
			/*----------------------Code to send mail ends here---------------*/
			return "Success";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created : Harshad Padole
	// Method Purpose : Common method to send mails
	@Override
	public void sendMail(String user_mail_id, String email_subject, String email_body, String client_task_id_for_log) {
		try {
			/*----------------------Code to send mail Start here---------------*/
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

			try {

				EmailLogs logs = new EmailLogs();
				logs.setTasksId(client_task_id_for_log);
				logs.setMailSentTo(user_mail_id);
				logs.setEmailStatus(email_subject);
				logs.setCreatedTime(new Date());
				cLogsDao.saveUpcomingMail(logs);

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(mailFrom));
				// message.setRecipients(Message.RecipientType.TO,address);
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(user_mail_id));
				message.setSubject(email_subject);
				message.setContent(email_body, "text/html; charset=utf-8");
				Transport.send(message);
				addMailToLog(user_mail_id, email_subject, client_task_id_for_log); // Mail log
			}

			catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error in transport send");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void addMonthlyReportToLog(String username, String subject) {

		File dir = new File(
				"C:" + File.separator + "CMS" + File.separator + projectName + File.separator + "EmailLogs");
		if (!dir.exists())
			dir.mkdirs();

		File newFile = new File(dir.getPath() + File.separator + "logs.txt");
		if (!newFile.exists()) {
			try {
				newFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try (FileWriter fw = new FileWriter("C:" + File.separator + "CMS" + File.separator + projectName
				+ File.separator + "EmailLogs" + File.separator + "monthlyreport.txt", true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			out.println("mail sent to: " + username);
			// more code
			java.util.Date date = new java.util.Date();
			out.println("Date and Time: " + new Timestamp(date.getTime()));
			// more code
			out.println("Subject: " + subject);
			out.println("-------------------------------------------------");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String sendUpcomingMailsToExecutorForSubtask(String subject, int pr_user_id, ArrayList<SendMail> mailsBody) {
		System.out.println("IN MAIL BODY");
		try {
			User user = usersDao.getUserById(pr_user_id);
			System.out.println("IN TRY BLOCK:" + pr_user_id);
			/*----------------------------------------Code for generating mail---------------------------------------*/
			String client_tasks_id_for_email_appending = "";
			String email_body = "";

			email_body += "<div style='margin:0 auto;width:100%;height:auto;padding:20px;'>";
			email_body += "<h2 style='font-size:18px;'>Dear Executor,</h2><br>";
			email_body += "<p style='text-align:justify;width:70%;'>The following tasks are required to be attended on or before the due date/s. You are requested to follow the link by clicking on the Task ID and complete the tasks by login to system. Tasks not attended or reported after the statutory date will be reckoned as Non Complied against your name.</p>"
					+ "<h2 style='font-size:16px;font-weight:bold;'>Upcoming Task :</h2>";
			email_body += "<table style='width:80%;' border='1'>" + "<thead>"
					+ "<tr style='background:#0B6EC3;color:#fff;'>" + "<th>SN</th>" + "<th>Task ID</th>"
					+ "<th>Sub Task ID</th>" + "<th>Executor Name</th>" + "<th>Entity</th>" + "<th>Location</th>"
					+ "<th>Function</th>" + "<th>Name of Legislation</th>" + "<th>Name of Rule</th>"
					+ "<th>Name of contractor</th>" + "<th>Compliance Title</th>" + "<th>Activity</th>"
					+ "<th>Frequency</th>" + "<th>Sub Task Location</th>" + "<th>Executor Due Date.</th>"
					+ "<th>Evaluator Due Date.</th>" + "<th>Function Head Due Date.</th>"
					+ "<th>Unit Head Due Date.</th>" + "<th>Legal Due Date</th>" + "</tr>" + "</thead>" + "<tbody>";
			int SN = 1;

			Iterator<SendMail> itr = mailsBody.iterator();
			while (itr.hasNext()) {
				SendMail sendMail = (SendMail) itr.next();

				email_body += "<tr>" + "<td>" + SN + "</td>" + "<td><a href=" + url + ">" + sendMail.getClient_task_id()
						+ "<td>" + sendMail.getSub_client_task_id() + "</td>" + "</a></td>" + "<td>"
						+ user.getUser_first_name() + " " + user.getUser_last_name() + "</td>" + "<td>"
						+ sendMail.getEntity_name() + "</td>" + "<td>" + sendMail.getUnit_name() + "</td>" + "<td>"
						+ sendMail.getFunction_name() + "</td>" + "<td>" + sendMail.getName_of_legislation() + "</td>"
						+ "<td>" + sendMail.getName_of_rule() + "</td>" + "<td>" + sendMail.getName_of_contractor()
						+ "</td>" + "<td>" + sendMail.getCompliance_title() + "</td>" + "<td>"
						+ sendMail.getCompliance_activity() + "</td>" + "<td>" + sendMail.getFrequency() + "</td>"
						+ "<td>" + sendMail.getSub_task_unit() + "</td>" + "<td>" + sendMail.getExecutor_date()
						+ "</td>" + "<td>" + sendMail.getEvaluator_date() + "</td>" + "<td>"
						+ sendMail.getFunc_head_date() + "</td>" + "<td>" + sendMail.getUnit_head_date() + "</td>"
						+ "<td>" + sendMail.getLegal_due_date() + "</td>" + "</tr>";

				SN++;
				client_tasks_id_for_email_appending += sendMail.getSub_client_task_id() + ",";
			}
			email_body += "</tbody>" + "</table>";
			email_body += "<p>This is a system generated mail. Please do not reply to this mail.<br/>"
					+ "In case of any doubt or difficulty, please call Helpdesk as per details given on the support page."
					+ "</p>" + "<h2 style='font-size:18px;font-weight:bold;'>Yours Sincerely</h2>"
					+ "<h2 style='font-size:19px;font-weight:bold;'>Team LexCare</h2>" + "</div>";
			/*----------------------------------------Code for generating mail ends here-----------------------------*/

			/*--------------------------Code to send mail---------------------*/

			sendSubTaskMail(user.getUser_email(), subject, email_body, client_tasks_id_for_email_appending);

			/*----------------------Code to send mail ends here---------------*/
			return "Success";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void sendSubTaskMail(String user_email, String subject, String email_body, String client_task_id) {
		System.out.println("In SEND MAIL: " + client_task_id);
		try {
			/*----------------------Code to send mail Start here---------------*/
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

			try {

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(mailFrom));
				// message.setRecipients(Message.RecipientType.TO,address);
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(user_email));
				message.setSubject(subject);
				message.setContent(email_body, "text/html; charset=utf-8");
				Transport.send(message);

				schedularDao.updateSubTaskEmailLog(client_task_id, subject);

				EmailLogs logs = new EmailLogs();
				logs.setTasksId(client_task_id);
				logs.setMailSentTo(user_email);
				logs.setEmailStatus(subject);
				logs.setCreatedTime(new Date());
				cLogsDao.saveUpcomingMail(logs);

				addMailToLog(user_email, subject, client_task_id); // Mail log
			}

			catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error in transport send");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String sendEscalationMailsToEvaluatorForSubtask(String subject, int rw_user_id,
			ArrayList<SendMail> mailsBody) {
		System.out.println("IN MAIL BODY");
		try {
			User user = usersDao.getUserById(rw_user_id);
			System.out.println("IN TRY BLOCK:" + rw_user_id);
			/*----------------------------------------Code for generating mail---------------------------------------*/
			String client_tasks_id_for_email_appending = "";
			String email_body = "";

			email_body += "<div style='margin:0 auto;width:100%;height:auto;padding:20px;'>";
			email_body += "<h2 style='font-size:18px;'>Dear Evaluator,</h2><br>";
			email_body += "<p style='text-align:justify;width:70%;'>The following tasks are required to be attended on or before the due date/s. You are requested to follow the link by clicking on the Task ID and complete the tasks by login to system. Tasks not attended or reported after the statutory date will be reckoned as Non Complied against your name.</p>"
					+ "<h2 style='font-size:16px;font-weight:bold;'>Not Attended Activities :</h2>";
			email_body += "<table style='width:80%;' border='1'>" + "<thead>"
					+ "<tr style='background:#0B6EC3;color:#fff;'>" + "<th>SN</th>" + "<th>Task ID</th>"
					+ "<th>Sub Task ID</th>" + "<th>Executor Name</th>" + "<th>Entity</th>" + "<th>Location</th>"
					+ "<th>Function</th>" + "<th>Name of Legislation</th>" + "<th>Name of Rule</th>"
					+ "<th>Name of contractor</th>" + "<th>Compliance Title</th>" + "<th>Activity</th>"
					+ "<th>Frequency</th>" + "<th>Sub Task Location</th>" + "<th>Executor Due Date.</th>"
					+ "<th>Evaluator Due Date.</th>" + "<th>Function Head Due Date.</th>"
					+ "<th>Unit Head Due Date.</th>" + "<th>Legal Due Date</th>" + "</tr>" + "</thead>" + "<tbody>";
			int SN = 1;

			Iterator<SendMail> itr = mailsBody.iterator();
			while (itr.hasNext()) {
				SendMail sendMail = (SendMail) itr.next();
				User pr_user = usersDao.getUserById(sendMail.getExec_id());
				email_body += "<tr>" + "<td>" + SN + "</td>" + "<td><a href=" + url + ">" + sendMail.getClient_task_id()
						+ "<td>" + sendMail.getSub_client_task_id() + "</td>" + "</a></td>" + "<td>"
						+ pr_user.getUser_first_name() + " " + pr_user.getUser_last_name() + "</td>" + "<td>"
						+ sendMail.getEntity_name() + "</td>" + "<td>" + sendMail.getUnit_name() + "</td>" + "<td>"
						+ sendMail.getFunction_name() + "</td>" + "<td>" + sendMail.getName_of_legislation() + "</td>"
						+ "<td>" + sendMail.getName_of_rule() + "</td>" + "<td>" + sendMail.getName_of_contractor()
						+ "</td>" + "<td>" + sendMail.getCompliance_title() + "</td>" + "<td>"
						+ sendMail.getCompliance_activity() + "</td>" + "<td>" + sendMail.getFrequency() + "</td>"
						+ "<td>" + sendMail.getSub_task_unit() + "</td>" + "<td>" + sendMail.getExecutor_date()
						+ "</td>" + "<td>" + sendMail.getEvaluator_date() + "</td>" + "<td>"
						+ sendMail.getFunc_head_date() + "</td>" + "<td>" + sendMail.getUnit_head_date() + "</td>"
						+ "<td>" + sendMail.getLegal_due_date() + "</td>" + "</tr>";

				SN++;
				client_tasks_id_for_email_appending += sendMail.getSub_client_task_id() + ",";
			}
			email_body += "</tbody>" + "</table>";
			email_body += "<p>This is a system generated mail. Please do not reply to this mail.<br/>"
					+ "In case of any doubt or difficulty, please call Helpdesk as per details given on the support page."
					+ "</p>" + "<h2 style='font-size:18px;font-weight:bold;'>Yours Sincerely</h2>"
					+ "<h2 style='font-size:19px;font-weight:bold;'>Team LexCare</h2>" + "</div>";
			/*----------------------------------------Code for generating mail ends here-----------------------------*/

			/*--------------------------Code to send mail---------------------*/

			sendSubTaskMail(user.getUser_email(), subject, email_body, client_tasks_id_for_email_appending);

			/*----------------------Code to send mail ends here---------------*/
			return "Success";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String sendEscalationMailsToFHForSubtask(String subject, int fh_user_id,
			ArrayList<SendMail> sendingMailList) {
		System.out.println("IN MAIL BODY");
		try {
			User user = usersDao.getUserById(fh_user_id);
			System.out.println("IN TRY BLOCK:" + fh_user_id);
			/*----------------------------------------Code for generating mail---------------------------------------*/
			String client_tasks_id_for_email_appending = "";
			String email_body = "";

			email_body += "<div style='margin:0 auto;width:100%;height:auto;padding:20px;'>";
			email_body += "<h2 style='font-size:18px;'>Dear Function Head,</h2><br>";
			email_body += "<p style='text-align:justify;width:70%;'>The following tasks are required to be attended on or before the due date/s. You are requested to follow the link by clicking on the Task ID and complete the tasks by login to system. Tasks not attended or reported after the statutory date will be reckoned as Non Complied against your name.</p>"
					+ "<h2 style='font-size:16px;font-weight:bold;'>Not Attended Activities :</h2>";
			email_body += "<table style='width:80%;' border='1'>" + "<thead>"
					+ "<tr style='background:#0B6EC3;color:#fff;'>" + "<th>SN</th>" + "<th>Task ID</th>"
					+ "<th>Sub Task ID</th>" + "<th>Executor Name</th>" + "<th>Entity</th>" + "<th>Location</th>"
					+ "<th>Function</th>" + "<th>Name of Legislation</th>" + "<th>Name of Rule</th>"
					+ "<th>Compliance Title</th>" + "<th>Name of Contractor</th>" + "<th>Activity</th>"
					+ "<th>Frequency</th>" + "<th>Sub Task Location</th>" + "<th>Executor Due Date.</th>"
					+ "<th>Evaluator Due Date.</th>" + "<th>Function Head Due Date.</th>"
					+ "<th>Unit Head Due Date.</th>" + "<th>Legal Due Date</th>" + "</tr>" + "</thead>" + "<tbody>";
			int SN = 1;

			Iterator<SendMail> itr = sendingMailList.iterator();
			while (itr.hasNext()) {
				SendMail sendMail = (SendMail) itr.next();
				User pr_user = usersDao.getUserById(sendMail.getExec_id());
				email_body += "<tr>" + "<td>" + SN + "</td>" + "<td><a href=" + url + ">" + sendMail.getClient_task_id()
						+ "<td>" + sendMail.getSub_client_task_id() + "</td>" + "</a></td>" + "<td>"
						+ pr_user.getUser_first_name() + " " + pr_user.getUser_last_name() + "</td>" + "<td>"
						+ sendMail.getEntity_name() + "</td>" + "<td>" + sendMail.getUnit_name() + "</td>" + "<td>"
						+ sendMail.getFunction_name() + "</td>" + "<td>" + sendMail.getName_of_legislation() + "</td>"
						+ "<td>" + sendMail.getName_of_rule() + "</td>" + "<td>" + sendMail.getCompliance_title()
						+ "</td>" + "<td>" + sendMail.getName_of_contractor() + "</td>" + "<td>"
						+ sendMail.getCompliance_activity() + "</td>" + "<td>" + sendMail.getFrequency() + "</td>"
						+ "<td>" + sendMail.getSub_task_unit() + "</td>" + "<td>" + sendMail.getExecutor_date()
						+ "</td>" + "<td>" + sendMail.getEvaluator_date() + "</td>" + "<td>"
						+ sendMail.getFunc_head_date() + "</td>" + "<td>" + sendMail.getUnit_head_date() + "</td>"
						+ "<td>" + sendMail.getLegal_due_date() + "</td>" + "</tr>";

				SN++;
				client_tasks_id_for_email_appending += sendMail.getSub_client_task_id() + ",";
			}
			email_body += "</tbody>" + "</table>";
			email_body += "<p>This is a system generated mail. Please do not reply to this mail.<br/>"
					+ "In case of any doubt or difficulty, please call Helpdesk as per details given on the support page."
					+ "</p>" + "<h2 style='font-size:18px;font-weight:bold;'>Yours Sincerely</h2>"
					+ "<h2 style='font-size:19px;font-weight:bold;'>Team LexCare</h2>" + "</div>";
			/*----------------------------------------Code for generating mail ends here-----------------------------*/

			/*--------------------------Code to send mail---------------------*/

			sendSubTaskMail(user.getUser_email(), subject, email_body, client_tasks_id_for_email_appending);

			/*----------------------Code to send mail ends here---------------*/
			return "Success";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String sendEscalationAlertToUHForSubtask(String subject, int user_id, ArrayList<SendMail> sendingMailList) {
		System.out.println("IN MAIL BODY");
		try {
			User user = usersDao.getUserById(user_id);
			System.out.println("IN TRY BLOCK:" + user_id);
			/*----------------------------------------Code for generating mail---------------------------------------*/
			String client_tasks_id_for_email_appending = "";
			String email_body = "";

			email_body += "<div style='margin:0 auto;width:100%;height:auto;padding:20px;'>";
			email_body += "<h2 style='font-size:18px;'>Dear Unit Head,</h2><br>";
			email_body += "<p style='text-align:justify;width:70%;'>The following tasks are required to be attended on or before the due date/s. You are requested to follow the link by clicking on the Task ID and complete the tasks by login to system. Tasks not attended or reported after the statutory date will be reckoned as Non Complied against your name.</p>"
					+ "<h2 style='font-size:16px;font-weight:bold;'>Not Attended Activities :</h2>";
			email_body += "<table style='width:80%;' border='1'>" + "<thead>"
					+ "<tr style='background:#0B6EC3;color:#fff;'>" + "<th>SN</th>" + "<th>Task ID</th>"
					+ "<th>Sub Task ID</th>" + "<th>Executor Name</th>" + "<th>Entity</th>" + "<th>Location</th>"
					+ "<th>Function</th>" + "<th>Name of Legislation</th>" + "<th>Name of Rule</th>"
					+ "<th>Compliance Title</th>" + "<th>Name of contractor</th>" + "<th>Activity</th>"
					+ "<th>Frequency</th>" + "<th>Sub Task Location</th>" + "<th>Executor Due Date.</th>"
					+ "<th>Evaluator Due Date.</th>" + "<th>Function Head Due Date.</th>"
					+ "<th>Unit Head Due Date.</th>" + "<th>Legal Due Date</th>" + "</tr>" + "</thead>" + "<tbody>";
			int SN = 1;

			Iterator<SendMail> itr = sendingMailList.iterator();
			while (itr.hasNext()) {
				SendMail sendMail = (SendMail) itr.next();
				User pr_user = usersDao.getUserById(sendMail.getExec_id());
				email_body += "<tr>" + "<td>" + SN + "</td>" + "<td><a href=" + url + ">" + sendMail.getClient_task_id()
						+ "<td>" + sendMail.getSub_client_task_id() + "</td>" + "</a></td>" + "<td>"
						+ pr_user.getUser_first_name() + " " + pr_user.getUser_last_name() + "</td>" + "<td>"
						+ sendMail.getEntity_name() + "</td>" + "<td>" + sendMail.getUnit_name() + "</td>" + "<td>"
						+ sendMail.getFunction_name() + "</td>" + "<td>" + sendMail.getName_of_legislation() + "</td>"
						+ "<td>" + sendMail.getName_of_rule() + "</td>" + "<td>" + sendMail.getCompliance_title()
						+ "</td>" + "<td>" + sendMail.getName_of_contractor() + "</td>" + "<td>"
						+ sendMail.getCompliance_activity() + "</td>" + "<td>" + sendMail.getFrequency() + "</td>"
						+ "<td>" + sendMail.getSub_task_unit() + "</td>" + "<td>" + sendMail.getExecutor_date()
						+ "</td>" + "<td>" + sendMail.getEvaluator_date() + "</td>" + "<td>"
						+ sendMail.getFunc_head_date() + "</td>" + "<td>" + sendMail.getUnit_head_date() + "</td>"
						+ "<td>" + sendMail.getLegal_due_date() + "</td>" + "</tr>";

				SN++;
				client_tasks_id_for_email_appending += sendMail.getSub_client_task_id() + ",";
			}
			email_body += "</tbody>" + "</table>";
			email_body += "<p>This is a system generated mail. Please do not reply to this mail.<br/>"
					+ "In case of any doubt or difficulty, please call Helpdesk as per details given on the support page."
					+ "</p>" + "<h2 style='font-size:18px;font-weight:bold;'>Yours Sincerely</h2>"
					+ "<h2 style='font-size:19px;font-weight:bold;'>Team LexCare</h2>" + "</div>";
			/*----------------------------------------Code for generating mail ends here-----------------------------*/

			/*--------------------------Code to send mail---------------------*/

			sendSubTaskMail(user.getUser_email(), subject, email_body, client_tasks_id_for_email_appending);

			/*----------------------Code to send mail ends here---------------*/
			return "Success";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String sendEscalationAlertToEHForSubtask(String subject, int user_id, ArrayList<SendMail> sendingMailList) {
		System.out.println("IN MAIL BODY");
		try {
			User user = usersDao.getUserById(user_id);
			System.out.println("IN TRY BLOCK:" + user_id);
			/*----------------------------------------Code for generating mail---------------------------------------*/
			String client_tasks_id_for_email_appending = "";
			String email_body = "";

			email_body += "<div style='margin:0 auto;width:100%;height:auto;padding:20px;'>";
			email_body += "<h2 style='font-size:18px;'>Dear Entity Head,</h2><br>";
			email_body += "<p style='text-align:justify;width:70%;'>The following tasks are required to be attended on or before the due date/s. You are requested to follow the link by clicking on the Task ID and complete the tasks by login to system. Tasks not attended or reported after the statutory date will be reckoned as Non Complied against your name.</p>"
					+ "<h2 style='font-size:16px;font-weight:bold;'>Not Attended Activities :</h2>";
			email_body += "<table style='width:80%;' border='1'>" + "<thead>"
					+ "<tr style='background:#0B6EC3;color:#fff;'>" + "<th>SN</th>" + "<th>Task ID</th>"
					+ "<th>Sub Task ID</th>" + "<th>Executor Name</th>" + "<th>Entity</th>" + "<th>Location</th>"
					+ "<th>Function</th>" + "<th>Name of Legislation</th>" + "<th>Name of Rule</th>"
					+ "<th>Compliance Title</th>" + "<th>Name of contractor</th>" + "<th>Activity</th>"
					+ "<th>Frequency</th>" + "<th>Sub Task Location</th>" + "<th>Executor Due Date.</th>"
					+ "<th>Evaluator Due Date.</th>" + "<th>Function Head Due Date.</th>"
					+ "<th>Unit Head Due Date.</th>" + "<th>Legal Due Date</th>" + "</tr>" + "</thead>" + "<tbody>";
			int SN = 1;

			Iterator<SendMail> itr = sendingMailList.iterator();
			while (itr.hasNext()) {
				SendMail sendMail = (SendMail) itr.next();
				User pr_user = usersDao.getUserById(sendMail.getExec_id());
				email_body += "<tr>" + "<td>" + SN + "</td>" + "<td><a href=" + url + ">" + sendMail.getClient_task_id()
						+ "<td>" + sendMail.getSub_client_task_id() + "</td>" + "</a></td>" + "<td>"
						+ pr_user.getUser_first_name() + " " + pr_user.getUser_last_name() + "</td>" + "<td>"
						+ sendMail.getEntity_name() + "</td>" + "<td>" + sendMail.getUnit_name() + "</td>" + "<td>"
						+ sendMail.getFunction_name() + "</td>" + "<td>" + sendMail.getName_of_legislation() + "</td>"
						+ "<td>" + sendMail.getName_of_rule() + "</td>" + "<td>" + sendMail.getCompliance_title()
						+ "</td>" + "<td>" + sendMail.getName_of_contractor() + "</td>" + "<td>"
						+ sendMail.getCompliance_activity() + "</td>" + "<td>" + sendMail.getFrequency() + "</td>"
						+ "<td>" + sendMail.getSub_task_unit() + "</td>" + "<td>" + sendMail.getExecutor_date()
						+ "</td>" + "<td>" + sendMail.getEvaluator_date() + "</td>" + "<td>"
						+ sendMail.getFunc_head_date() + "</td>" + "<td>" + sendMail.getUnit_head_date() + "</td>"
						+ "<td>" + sendMail.getLegal_due_date() + "</td>" + "</tr>";

				SN++;
				client_tasks_id_for_email_appending += sendMail.getSub_client_task_id() + ",";
			}
			email_body += "</tbody>" + "</table>";
			email_body += "<p>This is a system generated mail. Please do not reply to this mail.<br/>"
					+ "In case of any doubt or difficulty, please call Helpdesk as per details given on the support page."
					+ "</p>" + "<h2 style='font-size:18px;font-weight:bold;'>Yours Sincerely</h2>"
					+ "<h2 style='font-size:19px;font-weight:bold;'>Team LexCare</h2>" + "</div>";
			/*----------------------------------------Code for generating mail ends here-----------------------------*/

			/*--------------------------Code to send mail---------------------*/

			sendSubTaskMail(user.getUser_email(), subject, email_body, client_tasks_id_for_email_appending);

			/*----------------------Code to send mail ends here---------------*/
			return "Success";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void addMailToLog(String username, String subject, String ClienttaskIds, String address) {

		File dir = new File(
				"C:" + File.separator + "CMS" + File.separator + projectName + File.separator + "EmailLogs");
		if (!dir.exists())
			dir.mkdirs();

		File newFile = new File(dir.getPath() + File.separator + "logs.txt");
		if (!newFile.exists()) {
			try {
				newFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try (FileWriter fw = new FileWriter("C:" + File.separator + "CMS" + File.separator + projectName
				+ File.separator + "EmailLogs" + File.separator + "logs.txt", true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			out.println("mail sent to: " + username);
			out.println("mail sent cc: " + address);
			// more code
			java.util.Date date = new java.util.Date();
			out.println("Date and Time: " + new Timestamp(date.getTime()));
			// more code
			out.println("Subject: " + subject);
			out.println("Task Id : " + ClienttaskIds);
			out.println("-------------------------------------------------");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Created by Navdeep Upadhyay TO Add email log to the server for sending
	 * credentials to the user
	 */

	@Override
	public void saveLogForSendCredentials(User user, StringBuffer password, Message message) throws MessagingException {

		File dir = new File(
				"C:" + File.separator + "CMS" + File.separator + projectName + File.separator + "EmailLogs");
		if (!dir.exists())
			dir.mkdirs();

		File newFile = new File(dir.getPath() + File.separator + "Credentialslogs.txt");
		if (!newFile.exists()) {
			try {
				newFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try (FileWriter fw = new FileWriter("C:" + File.separator + "CMS" + File.separator + projectName
				+ File.separator + "EmailLogs" + File.separator + "Credentialslogs.txt", true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {

			out.println("Sent to : " + user.getUser_email());

			out.println("\nCredential Sent by : " + httpSession.getAttribute("sess_user_full_name").toString());

			/*
			 * out.println("\nHead of organization CC'd: " +
			 * InternetAddress.toString(message.getRecipients(Message.RecipientType.CC)));
			 */
			out.println("\nUsername : " + user.getUser_username());
			out.println("\nPassword : " + password.toString());
			// more code
			java.util.Date date = new java.util.Date();
			out.println("\nDate and Time: " + new Timestamp(date.getTime()));
			// more code
			// out.println("Subject: " + subject);
			// out.println("Task Id : " + ClienttaskIds);
			out.println("\n-------------------------------------------------\n\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	//
}
