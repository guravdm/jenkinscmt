package lexprd006.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.csvreader.CsvReader;

import lexprd006.dao.TasksDao;
import lexprd006.dao.UploadedDocumentsDao;
import lexprd006.dao.UsersDao;
import lexprd006.domain.SubTask;
import lexprd006.domain.SubTaskTranscational;
import lexprd006.domain.Task;
import lexprd006.domain.UploadedDocuments;
import lexprd006.domain.UploadedSubTaskDocuments;
import lexprd006.domain.User;
import lexprd006.service.TasksService;
import lexprd006.service.UtilitiesService;

@Service(value = "tasksService")
public class TasksServiceImpl implements TasksService {

	public final SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd");
	public final SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd");
	public final SimpleDateFormat sdfOutForDisplay = new SimpleDateFormat("dd-MM-yyyy");

	@Autowired
	TasksDao tasksDao;

	@Autowired
	UsersDao usersDao;

	@Autowired
	UploadedDocumentsDao uploadedDocumentsDao;

	@Autowired
	UtilitiesService utilitiesService;

	private @Value("#{config['mail_user_name'] ?: 'null'}") String username;
	private @Value("#{config['mail_password'] ?: 'null'}") String password;
	private @Value("#{config['mail_smtp_host'] ?: 'null'}") String hostName;
	private @Value("#{config['mail_smtp_port'] ?: 'null'}") String portNo;
	private @Value("#{config['mail_from'] ?: 'null'}") String mailFrom;

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all Tasks rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String getAllImportedTasks() {
		JSONArray dataForSend = new JSONArray();
		try {
			List<Task> allTasksList = tasksDao.getAll(Task.class);

			Iterator<Task> iterator = allTasksList.iterator();
			while (iterator.hasNext()) {
				Task task = (Task) iterator.next();
				JSONObject objForAppend = new JSONObject();

				objForAppend.put("task_id", task.getTask_id());
				objForAppend.put("task_activity", task.getTask_activity());
				objForAppend.put("task_activity_who", task.getTask_activity_who());
				objForAppend.put("task_activity_when", task.getTask_activity_when());
				objForAppend.put("task_cat_law_id", task.getTask_cat_law_id());
				objForAppend.put("task_cat_law_name", task.getTask_cat_law_name());
				objForAppend.put("task_country_id", task.getTask_country_id());
				objForAppend.put("task_country_name", task.getTask_country_name());
				if (task.getTask_legal_due_date() != null)
					objForAppend.put("task_legal_due_date", sdfOutForDisplay.format(task.getTask_legal_due_date()));
				else
					objForAppend.put("task_legal_due_date", "NA");
				objForAppend.put("task_event", task.getTask_event());
				objForAppend.put("task_excemption_criteria", task.getTask_excemption_criteria());
				objForAppend.put("task_fine_amount", task.getTask_fine_amount());
				objForAppend.put("task_form_no", task.getTask_form_no());
				objForAppend.put("task_frequency", task.getTask_frequency());
				objForAppend.put("task_impact", task.getTask_impact());
				objForAppend.put("task_impact_on_organization", task.getTask_impact_on_organization());
				objForAppend.put("task_impact_on_unit", task.getTask_impact_on_unit());
				objForAppend.put("task_implication", task.getTask_implication());
				objForAppend.put("task_imprisonment_duration", task.getTask_imprisonment_duration());
				objForAppend.put("task_imprisonment_implies_to", task.getTask_imprisonment_implies_to());
				objForAppend.put("task_interlinkage", task.getTask_interlinkage());
				objForAppend.put("task_legi_id", task.getTask_legi_id());
				objForAppend.put("task_legi_name", task.getTask_legi_name());
				objForAppend.put("task_level", task.getTask_level());
				objForAppend.put("task_lexcare_task_id", task.getTask_lexcare_task_id());
				objForAppend.put("task_linked_task_id", task.getTask_linked_task_id());
				objForAppend.put("task_more_info", task.getTask_more_info());
				objForAppend.put("task_procedure", task.getTask_procedure());
				objForAppend.put("task_prohibitive", task.getTask_prohibitive());
				objForAppend.put("task_document_reference", task.getTask_document_reference());
				objForAppend.put("task_form_format", task.getTask_form_format());
				objForAppend.put("task_reference", task.getTask_reference());
				objForAppend.put("task_rule_id", task.getTask_rule_id());
				objForAppend.put("task_rule_name", task.getTask_rule_name());
				objForAppend.put("task_sequence", task.getTask_sequence());
				objForAppend.put("due_date", task.getDue_date());
				objForAppend.put("task_specific_due_date", task.getTask_specific_due_date());
				objForAppend.put("task_state_id", task.getTask_state_id());
				objForAppend.put("task_state_name", task.getTask_state_name());
				objForAppend.put("task_sub_event", task.getTask_sub_event());
				objForAppend.put("task_subsequent_amount_per_day", task.getTask_subsequent_amount_per_day());
				objForAppend.put("task_task_type_of_task", task.getTask_task_type_of_task());
				objForAppend.put("task_subtask", task.getTask_subtask());
				objForAppend.put("task_effective_date", task.getTask_effective_date());
				objForAppend.put("task_weblinks", task.getTask_weblinks());
				objForAppend.put("task_approval_status", task.getTask_approval_status());
				objForAppend.put("task_added_by", task.getTask_added_by());
				if (task.getTask_legal_due_date() != null) {
					objForAppend.put("task_created_at", sdfOutForDisplay.format(task.getTask_created_at()));
				} else {
					objForAppend.put("task_created_at", "NA");
				}
				objForAppend.put("task_enable_status", task.getTask_enable_status());
				objForAppend.put("task_statutory_authority", task.getTask_statutory_authority());

				dataForSend.add(objForAppend);

			}

			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("responseMessage", e);
			dataForSend.add(objForAppend);
			return dataForSend.toJSONString();
		}
	}

	@Override
	public String getTasksForLegalUpdate(String jsonString) {
		// TODO Auto-generated method stub
		return null;
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all Tasks rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String addupdateTaskLegalUpdate(MultipartFile multipartFile, String jsonString, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		String user_name = session.getAttribute("sess_user_full_name").toString();
		int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			if (!multipartFile.isEmpty()) {
				JSONArray neglectedTask = new JSONArray();
				JSONArray addedTask = new JSONArray();
				ArrayList<Task> sendingMailList = new ArrayList<>();
				String name = jsonObj.get("name").toString();
				byte[] bytes = multipartFile.getBytes();

				String fileExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
				if (fileExtension.equalsIgnoreCase("csv")) {
					// Create Temp File
					File temp = File.createTempFile(multipartFile.getName(), ".csv");

					String absolutePath = temp.getAbsolutePath();
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(absolutePath));
					stream.write(bytes);
					stream.close();
					CsvReader legalUpdates = new CsvReader(absolutePath);

					legalUpdates.readHeaders();

					final String[] impact = new String[] { "Severe", "Major", "Moderate", "Low" };
					final String[] frequency = new String[] { "One_Time", "Ongoing", "Weekly", "Fortnightly", "Monthly",
							"Two_Monthly", "Quarterly", "Four_Monthly", "Five_Monthly", "Half_Yearly", "Seven_Monthly",
							"Eight_Monthly", "Nine_Monthly", "Ten_Monthly", "Yearly", "Fourteen_Monthly",
							"Eighteen_Monthly", "Two_Yearly", "Three_Yearly", "Four_Yearly", "Five_Yearly",
							"Six_Yearly", "Event_Based", "Seven_Yearly", "Eight_Yearly", "Nine_Yearly", "Ten_Yearly",
							"Twenty_Yearly" };
					final String[] typeOfTask = new String[] { "Approval or Licensing", "Registration", "Renewal",
							"Amendment", "Intimation", "Transfer or Conversion", "Termination", "Payment", "Safety",
							"Welfare", "Health", "Register and Records", "Returns", "Display", "Policies", "Committee",
							"Inspection of Equipment", "Inspection by Authority", "Allotment", "Appointment",
							"Meetings", "Reports", "Resignation", "Retirement", "Website Disclosure", "Verification",
							"Disclosure", "Notices", "Employment and Nomination", "Loans and Advances", "Submission",
							"Insurance", "Deduction", "Remuneration", "Appointment", "Installations", "Environment" };
					final String[] prescriptive = new String[] { "Prescriptive", "Prohibitive" };
					int i = 0;
					String client_task_ids_new = "";
					String client_task_ids_to_update = "";
					int add = 0;
					int update = 0;
					String email_body = "";

					email_body += "<div style='margin:0 auto;width:100%;height:auto;padding:20px;'>";
					email_body += "<h2 style='font-size:18px;'>Dear User,</h2><br>";
					email_body += "<p style='text-align:justify;width:70%;'>The following tasks are uploaded successfully.</p>"
							+ "<h2 style='font-size:16px;font-weight:bold;'>Imported Task :</h2>";
					email_body += "<table style='width:80%;' border='1'>" + "<thead>"
							+ "<tr style='background:#0B6EC3;color:#fff;'>" + "<th>SN</th>" + "<th>Task ID</th>"
							+ "<th>Country</th>" + "<th>State</th>" + "<th>Category Of Law</th>"
							+ "<th>Name of Legislation</th>" + "<th>Name of Rule</th>" + "<th>Who</th>"
							+ "<th>When</th>" + "<th>Reference</th>" + "<th>Activity</th>" + "<th>Procedure</th>"
							+ "<th>Prohibitive/Prescriptive</th>" + "<th>Type Of Task</th>" + "<th>Frequency</th>"
							+ "<th>Impact</th>" + "<th>Impact on unit</th>" + "<th>Impact on organization</th>"
							+ "<th>Implications</th>" + "</tr>" + "</thead>" + "<tbody>";
					while (legalUpdates.readRecord()) {
						i++;
						System.out.println("this is record no: " + i);
						System.out.println("this is task id:" + legalUpdates.get("Task_id"));

						JSONObject objForAppend = new JSONObject();
						Task tsk = null;
						Task task = tasksDao.getTasksForLegalUpdate(legalUpdates.get("Task_id"));

						if (task != null) {
							tsk = task;
							update = 1;
							client_task_ids_to_update += task.getTask_lexcare_task_id() + ",";
						} else {
							tsk = new Task();
							add = 1;
							client_task_ids_new += legalUpdates.get("Task_id") + ",";
						}

						if (Arrays.asList(impact).contains(legalUpdates.get("Impact"))) {
							if (Arrays.asList(impact).contains(legalUpdates.get("Impact on unit"))) {
								if (Arrays.asList(impact).contains(legalUpdates.get("Impact on organisation"))) {
									if (Arrays.asList(frequency).contains(legalUpdates.get(("Frequency")))) {
										if (Arrays.asList(typeOfTask).contains(legalUpdates.get(("Type of task")))) {
											if (Arrays.asList(prescriptive)
													.contains(legalUpdates.get(("Prohibitive/Prescriptive")))) {
												tsk.setTask_lexcare_task_id(legalUpdates.get("Task_id"));
												System.out.println(
														"Country ID : " + legalUpdates.get("Country id") != null
																? legalUpdates.get("Country id")
																: "0");
												tsk.setTask_country_id(
														Integer.parseInt(legalUpdates.get("Country id")));
												tsk.setTask_country_name(legalUpdates.get("Country"));
												tsk.setTask_state_id(Integer.parseInt(legalUpdates.get("State id")));
												tsk.setTask_state_name(legalUpdates.get("State"));
												tsk.setTask_cat_law_id(
														Integer.parseInt(legalUpdates.get("Category_of_law_id")));
												tsk.setTask_cat_law_name(legalUpdates.get("Category of law"));
												tsk.setTask_legi_id(
														Integer.parseInt(legalUpdates.get("Legislation_id")));
												tsk.setTask_legi_name(legalUpdates.get("Legislation"));
												tsk.setTask_rule_id(Integer.parseInt(legalUpdates.get("Rule_id")));
												tsk.setTask_rule_name(legalUpdates.get("Rule"));
												tsk.setTask_activity_who(legalUpdates.get("Who"));
												tsk.setTask_activity_when(legalUpdates.get("When"));
												tsk.setTask_reference(legalUpdates.get("Reference"));
												tsk.setTask_activity(legalUpdates.get("Compliance Activity"));
												tsk.setTask_procedure(legalUpdates.get("Procedure"));
												tsk.setTask_more_info(legalUpdates.get("More Information"));
												tsk.setTask_prohibitive(legalUpdates.get("Prohibitive/Prescriptive"));
												tsk.setTask_frequency(legalUpdates.get("Frequency"));
												tsk.setTask_form_no(legalUpdates.get("Form No"));
												tsk.setDue_date(legalUpdates.get("Due date"));
												tsk.setTask_specific_due_date(legalUpdates.get("Specific due date"));
												tsk.setTask_task_type_of_task(legalUpdates.get("Type of task"));
												tsk.setTask_level(legalUpdates.get("Corporate Level or Unit Level"));
												tsk.setTask_excemption_criteria(legalUpdates.get("Exemption criteria"));
												tsk.setTask_event(legalUpdates.get("Event"));
												tsk.setTask_sub_event(legalUpdates.get("Sub event"));
												tsk.setTask_implication(legalUpdates.get("Implications"));
												tsk.setTask_imprisonment_duration(
														legalUpdates.get("Imprisonment duration"));
												tsk.setTask_imprisonment_implies_to(
														legalUpdates.get("Imprisonment applies to"));
												tsk.setTask_statutory_authority(
														legalUpdates.get("Statutory Authority"));

												BigDecimal BGD_fine_amount = new BigDecimal(
														legalUpdates.get("Fine amount").toString());
												tsk.setTask_fine_amount(BGD_fine_amount);

												BigDecimal BGD_subsequent_amount = new BigDecimal(
														legalUpdates.get("Subsequent amount per day").toString());
												tsk.setTask_subsequent_amount_per_day(BGD_subsequent_amount);
												tsk.setTask_impact(legalUpdates.get("Impact"));
												tsk.setTask_impact_on_unit(legalUpdates.get("Impact on unit"));
												tsk.setTask_impact_on_organization(
														legalUpdates.get("Impact on organisation"));
												tsk.setTask_interlinkage(legalUpdates.get("Interlinkage"));
												tsk.setTask_linked_task_id(legalUpdates.get("Linked task id"));
												tsk.setTask_weblinks(legalUpdates.get("Weblinks"));
												tsk.setTask_document_reference(null);
												tsk.setTask_created_at(new Date());
												tsk.setTask_added_by(1);
												tsk.setTask_enable_status("1");
												tsk.setTask_approval_status("1");

												sendingMailList.add(tsk);
												tasksDao.updateTaskLegalUpdate(tsk);

												objForAppend.put("responseMessage", "Success");
												objForAppend.put("Added_Id", legalUpdates.get("Task_id"));
												addedTask.add(objForAppend);
											} else {
												objForAppend.put("Reason_for_negligence",
														"Prohibitive/Prescriptive incorrect");
												objForAppend.put("Neglected_Id", legalUpdates.get("Task_id"));
												neglectedTask.add(objForAppend);
											}

										} else {
											objForAppend.put("Reason_for_negligence", "Type of task incorrect");
											objForAppend.put("Neglected_Id", legalUpdates.get("Task_id"));
											neglectedTask.add(objForAppend);
										}

									} else {
										objForAppend.put("Reason_for_negligence", "Frequency incorrect");
										objForAppend.put("Neglected_Id", legalUpdates.get("Task_id"));
										neglectedTask.add(objForAppend);
									}

								} else {
									objForAppend.put("Reason_for_negligence", "Impact on organisation incorrect");
									objForAppend.put("Neglected_Id", legalUpdates.get("Task_id"));
									neglectedTask.add(objForAppend);
								}
							} else {
								objForAppend.put("Reason_for_negligence", "Impact on unit incorrect");
								objForAppend.put("Neglected_Id", legalUpdates.get("Task_id"));
								neglectedTask.add(objForAppend);
							}

						} else {
							objForAppend.put("Reason_for_negligence", "Impact incorrect");
							objForAppend.put("Neglected_Id", legalUpdates.get("Task_id"));
							neglectedTask.add(objForAppend);
						}

						objForAppend.put("name", name);

					}

					// Set Temp data

					dataForSend.put("neglectedTasks", neglectedTask);
					dataForSend.put("addedTasks", addedTask);

					if (add == 1)
						utilitiesService.addLegalUpdateLog(client_task_ids_new, user_id, user_name, "Import", "Add");

					if (update == 1) {

						User usr = usersDao.getUserById(user_id);
						Iterator<Task> itr = sendingMailList.iterator();
						while (itr.hasNext()) {
							int SN = 1;
							Task task = itr.next();
							email_body += "<tr>" + "<td>" + SN + "</td>" + "<td>" + task.getTask_lexcare_task_id()
									+ "</td>" + "<td>" + task.getTask_country_name() + "</td>" + "<td>"
									+ task.getTask_state_name() + "</td>" + "<td>" + task.getTask_cat_law_name()
									+ "</td>" + "<td>" + task.getTask_legi_name() + "</td>" + "<td>"
									+ task.getTask_rule_name() + "</td>" + "<td>" + task.getTask_activity_who()
									+ "</td>" + "<td>" + task.getTask_activity_when() + "</td>" + "<td>"
									+ task.getTask_reference() + "</td>" + "<td>" + task.getTask_activity() + "</td>"
									+ "<td>" + task.getTask_procedure() + "</td>" + "<td>" + task.getTask_prohibitive()
									+ "</td>" + "<td>" + task.getTask_task_type_of_task() + "</td>" + "<td>"
									+ task.getTask_frequency() + "</td>" + "<td>" + task.getTask_impact() + "</td>"
									+ "<td>" + task.getTask_impact_on_unit() + "</td>" + "<td>"
									+ task.getTask_impact_on_organization() + "</td>" + "<td>"
									+ task.getTask_implication() + "</td>" + "</tr>";

							SN++;
						}
						email_body += "</tbody>" + "</table>";
						email_body += "<p>This is a system generated mail. Please do not reply to this mail.<br/>"
								+ "In case of any doubt or difficulty, please call Helpdesk as per details given on the support page."
								+ "</p>" + "<h2 style='font-size:18px;font-weight:bold;'>Yours Sincerely</h2>"
								+ "<h2 style='font-size:19px;font-weight:bold;'>Team LexCare</h2>" + "</div>";
						/*----------------------------------------Code for generating mail ends here-----------------------------*/

						Properties props = new Properties();
						props.put("mail.smtp.auth", "true");
						props.put("mail.smtp.starttls.enable", "true");
						props.put("mail.smtp.host", hostName);
						props.put("mail.smtp.port", portNo);

						Session sess = Session.getInstance(props, new javax.mail.Authenticator() {
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(username, password);
							}
						});

						try {

							Message message = new MimeMessage(sess);
							message.setFrom(new InternetAddress(mailFrom));
							// message.setRecipients(Message.RecipientType.TO,address);
							message.addRecipient(Message.RecipientType.TO, new InternetAddress(usr.getUser_email()));
							message.setSubject("Legal Updates");
							message.setContent(email_body, "text/html; charset=utf-8");
							Transport.send(message);
							// addMailToLog(user_mail_id, email_subject, client_task_id_for_log); // Mail
							// log
							System.out.println("Email Sent successfully");
						}

						catch (Exception e) {
							e.printStackTrace();
							System.out.println("Error in transport send");
						}
						utilitiesService.addLegalUpdateLog(client_task_ids_to_update, user_id, user_name, "LegalUpdate",
								"Update");
					}

					legalUpdates.close();
				} else {

					JSONObject objForAppend = new JSONObject();
					objForAppend.put("responseMessage", "File type mismatch");
					return objForAppend.toJSONString();
				}

			}

			return dataForSend.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("responseMessage", "Failed");
			return objForAppend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all Tasks rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String getTaskHistoryByClientTaskId(String jsonString, HttpSession session) {
		JSONObject objForSend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			Date currentDate = sdfIn.parse(sdfOut.format(new Date()));

			JSONArray dataForAppend = new JSONArray();
			String tmap_client_task_id = jsonObj.get("tmap_client_task_id").toString();
			List<Object> taskHistoryList = tasksDao.getTaskHistoryByClientTaskId(tmap_client_task_id);
			Iterator<Object> itr = taskHistoryList.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				Date performerDueDate = sdfIn.parse(object[3].toString());
				Date legalDueDate = sdfIn.parse(object[7].toString());
				String legalTaskStatus = "";
				String legalClass = "";
				String taskStatus = object[10].toString();

				if (taskStatus.equals("Completed")) {
					Date submittedDate = sdfIn.parse(object[12].toString());
					Date completedDate = sdfIn.parse(object[8].toString());
					if (submittedDate.before(legalDueDate) || submittedDate.equals(legalDueDate)) {
						legalTaskStatus = "Complied";
						legalClass = "Complied";
					} else {
						/*
						 * if(submittedDate.after(legalDueDate)) legalTaskStatus = "Delayed"; legalClass
						 * = "Delayed";
						 */
						if (submittedDate.after(legalDueDate) && completedDate.after(legalDueDate)) {
							legalTaskStatus = "Delayed";
							legalClass = "Delayed";
						}
						if (submittedDate.after(legalDueDate)
								&& (completedDate.before(legalDueDate) || completedDate.equals(legalDueDate))) {
							legalTaskStatus = "Delayed Reported";
							legalClass = "Delayed_Reported";
						}
					}
				} else {
					if (taskStatus.equals("Active")) {
						if (legalDueDate.after(currentDate) || legalDueDate.equals(currentDate)) {
							if (currentDate.after(performerDueDate)) {
								legalTaskStatus = "Posing Risk";
								legalClass = "Posing-Risk";
							} else {
								legalTaskStatus = "Pending";
								legalClass = "";
							}
						} else {
							if (currentDate.after(legalDueDate))
								legalTaskStatus = "Non Complied";
							legalClass = "Non-Complied";
						}

					} else {
						if (taskStatus.equals("Inactive"))
							legalTaskStatus = "Inactive";
						legalClass = "";

						if (taskStatus.equals("Partially_Completed")) {
							legalTaskStatus = "Wating for Approval";
							legalClass = "Partially_Completed";
						}

						if (taskStatus.equals("Re_Opened")) {
							legalTaskStatus = "Re_Opened";
							legalClass = "ReOpened";
						}
					}
				}

				objForAppend.put("ttrn_id", object[0]);
				objForAppend.put("ttrn_performer_name", object[1].toString() + " " + object[2].toString());
				objForAppend.put("ttrn_pr_due_date", sdfOutForDisplay.format(performerDueDate));
				objForAppend.put("ttrn_rw_due_date", sdfOutForDisplay.format(sdfIn.parse(object[4].toString())));
				objForAppend.put("ttrn_fh_due_date", sdfOutForDisplay.format(sdfIn.parse(object[5].toString())));
				objForAppend.put("ttrn_uh_due_date", sdfOutForDisplay.format(sdfIn.parse(object[6].toString())));
				objForAppend.put("ttrn_legal_due_date", sdfOutForDisplay.format(legalDueDate));
				if (object[8] != null)
					objForAppend.put("ttrn_completed_date", sdfOutForDisplay.format(sdfIn.parse(object[8].toString())));
				else
					objForAppend.put("ttrn_completed_date", "");
				if (object[9] != null)
					objForAppend.put("ttrn_performer_comments", object[9]);
				else
					objForAppend.put("ttrn_performer_comments", "");

				objForAppend.put("ttrn_task_status", taskStatus);
				objForAppend.put("ttrn_legal_task_status", legalTaskStatus);
				objForAppend.put("ttrn_legal_task_style", legalClass);
				objForAppend.put("ttrn_reason_for_non_compliance", object[14]);

				/*-----------Getting Completed by first name last name------------------------------------------------------*/
				int user_id = Integer.parseInt(object[11].toString());
				if (user_id > 0) {
					User user = usersDao.getUserById(user_id);
					objForAppend.put("ttrn_task_completed_by",
							user.getUser_first_name() + " " + user.getUser_last_name());
				} else
					objForAppend.put("ttrn_task_completed_by", "");
				/*-----------Getting Completed by first name last name ends here--------------------------------------------*/
				objForAppend.put("ttrn_document", object[13]);
				objForAppend.put("tmap_pr_user_id", object[15]);
				objForAppend.put("tmap_rw_user_id", object[16]);
				objForAppend.put("tmap_fh_user_id", object[17]);
				objForAppend.put("user_email", object[18]);
				objForAppend.put("auditoComments", object[19] != null ? object[19].toString() : "NA");

				String auditorAuditTime = "";
				if (object[20] != null) {
					auditorAuditTime = sdfOutForDisplay.format(sdfIn.parse(object[20].toString()));
				} else {
					auditorAuditTime = "";
				}

				objForAppend.put("auditorAuditTime", auditorAuditTime);
				// object[20] != null ? object[20].toString() : "NA"
				objForAppend.put("auditorStatus", object[21] != null ? object[21].toString() : "NA");
				objForAppend.put("auditor_performer_by_id", object[22] != null ? object[22].toString() : "0");

				objForAppend.put("auditDate", object[23] != null ? object[23].toString() : "0");
				System.out.println("object[24] : " + object[24]);

				if (object[24] != null) {
					objForAppend.put("reOpenDateWindow", "Yes");
				} else {
					objForAppend.put("reOpenDateWindow", "No");
				}

				// System.out.println("auditCondition : " + object[25].toString());
				// objForAppend.put("auditCondition", object[25] != null ? object[25].toString()
				// : "0");
				objForAppend.put("auditCondition", "");

				// objForAppend.put("reOpenDateWindow", object[26] != null ?
				// object[26].toString() : "0");

				// System.out.println("object[25] : " + object[25]);

				SimpleDateFormat sdfo = new SimpleDateFormat("yyyy-MM-dd");
				Date cuDate = new Date();
				String currentDateD2 = sdfo.format(cuDate);
				Date d1 = null;
				Date d2 = null;
				if (object[25] != null) {
					d1 = sdfo.parse(object[23].toString()); // Audit Date
					d2 = sdfo.parse(currentDateD2); // Current Date

					String adDate = sdfo.format(d1); // Audit Date
					String cDate = sdfo.format(d2); // Current Date

					LocalDate currentDates = LocalDate.parse(cDate);
					LocalDate auditDate = LocalDate.parse(adDate);

					System.out.println("AudiDate : " + object[23].toString());

					// if (d1.before(d2) || d1.equals(d2)) {
					if ((auditDate.isAfter(currentDates) || auditDate.equals(currentDates)) && auditDate != null) {
						System.out.println("before and equals currentDates : " + currentDates + "\t d2 : " + auditDate);
						objForAppend.put("auditConditionTwo", "Yes");
					} else {
						System.out.println("before and equals else part currentDates : " + currentDates
								+ "\t auditDate : " + auditDate);
						objForAppend.put("auditConditionTwo", "No");
					}
				} else {
					objForAppend.put("auditConditionTwo", "Yes");
				}

				/*
				 * Below is the code for show and hide complete task button as per the frequency
				 * 
				 * Complete task button will be available before 2 month from Executor date
				 */

				SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
				Date parseDate = ss.parse(object[3].toString());
				String formatDate = ss.format(parseDate);

				LocalDate locDate = LocalDate.now();
				LocalDate prDueDate = LocalDate.parse(formatDate);
				// System.out.println("current Date : " + locDate + "\t prDueDate : " +
				// prDueDate);

				LocalDate minusDays = prDueDate.minusDays(60);
				// System.out.println("minusDays : " + minusDays);

				if (minusDays.isBefore(locDate) || locDate.isEqual(minusDays)) {
					// System.out.println("inside if condition and allow YES : " + locDate + "\t pr
					// : " + minusDays);
					objForAppend.put("ttrn_allow_completion", "yes");
				} else {
					objForAppend.put("ttrn_allow_completion", "no");
				}

				// Completed Hide and show complete task button

				List<UploadedDocuments> attachedDocuments = uploadedDocumentsDao
						.getAllDocumentByTtrnId(Integer.parseInt(object[0].toString()));

				if (attachedDocuments != null) {
					Iterator<UploadedDocuments> itre = attachedDocuments.iterator();
					JSONArray docArray = new JSONArray();

					while (itre.hasNext()) {
						UploadedDocuments uploadedDocuments = (UploadedDocuments) itre.next();
						JSONObject docObj = new JSONObject();
						docObj.put("udoc_id", uploadedDocuments.getUdoc_id());
						docObj.put("udoc_original_file_name", uploadedDocuments.getUdoc_original_file_name());
						docArray.add(docObj);
					}

					objForAppend.put("document_list", docArray);
				} else {
					objForAppend.put("document_list", new JSONArray());
				}

				objForAppend.put("task_frequency",
						tasksDao.getOriginalFrequency(Integer.parseInt(object[0].toString())));
				objForAppend.put("client_task_id", tmap_client_task_id);
				dataForAppend.add(objForAppend);
			}

			objForSend.put("task_history", dataForAppend);

			JSONArray subTaskdataForAppend = new JSONArray();
			List<SubTaskTranscational> subTaskList = tasksDao.getSubTaskHitoryByclientTaskID(tmap_client_task_id);
			if (subTaskList != null) {
				Iterator<SubTaskTranscational> iterator = subTaskList.iterator();

				while (iterator.hasNext()) {

					SubTaskTranscational subTaskTranscational = iterator.next();

					JSONObject objForAppend = new JSONObject();
					Date performerDueDate = sdfIn.parse(subTaskTranscational.getTtrn_sub_task_pr_due_date().toString());
					Date legalDueDate = sdfIn.parse(subTaskTranscational.getTtrn_sub_task_ENT_due_date().toString());
					String legalTaskStatus = "";
					String legalClass = "";
					String taskStatus = subTaskTranscational.getTtrn_sub_task_status();

					if (taskStatus.equals("Completed")) {
						Date submittedDate = sdfIn
								.parse(subTaskTranscational.getTtrn_sub_task_submition_date().toString());
						if (submittedDate.before(legalDueDate) || submittedDate.equals(legalDueDate)) {
							legalTaskStatus = "Complied";
							legalClass = "Complied";
						} else {
							if (submittedDate.after(legalDueDate))
								legalTaskStatus = "Delayed";
							legalClass = "Delayed";
						}
					} else {
						if (taskStatus.equals("Active")) {
							if (legalDueDate.after(currentDate) || legalDueDate.equals(currentDate)) {
								if (currentDate.after(performerDueDate)) {
									legalTaskStatus = "Posing Risk";
									legalClass = "Posing-Risk";
								} else {
									legalTaskStatus = "Pending";
									legalClass = "";
								}
							} else {
								if (currentDate.after(legalDueDate))
									legalTaskStatus = "Non Complied";
								legalClass = "Non-Complied";
							}

						} else {
							if (taskStatus.equals("Inactive"))
								legalTaskStatus = "Inactive";
							legalClass = "";

							if (taskStatus.equals("Partially_Completed")) {
								legalTaskStatus = "Waiting for Approval";
								legalClass = "Partially_Completed";
							}

							if (taskStatus.equals("Re_Opened")) {
								legalTaskStatus = "Re_Opened";
								legalClass = "ReOpened";
							}
						}
					}

					objForAppend.put("ttrn_id", subTaskTranscational.getTtrn_sub_id());
					// objForAppend.put("ttrn_performer_name", object[1].toString() +" "+
					// object[2].toString());
					objForAppend.put("ttrn_pr_due_date", sdfOutForDisplay.format(performerDueDate));
					objForAppend.put("ttrn_rw_due_date", sdfOutForDisplay
							.format(sdfIn.parse(subTaskTranscational.getTtrn_sub_task_rw_date().toString())));
					objForAppend.put("ttrn_fh_due_date", sdfOutForDisplay
							.format(sdfIn.parse(subTaskTranscational.getTtrn_sub_task_FH_due_date().toString())));
					objForAppend.put("ttrn_uh_due_date", sdfOutForDisplay
							.format(sdfIn.parse(subTaskTranscational.getTtrn_sub_task_UH_due_date().toString())));
					objForAppend.put("ttrn_legal_due_date", sdfOutForDisplay.format(legalDueDate));
					if (subTaskTranscational.getTtrn_sub_task_compl_date() != null)
						objForAppend.put("ttrn_completed_date", sdfOutForDisplay
								.format(sdfIn.parse(subTaskTranscational.getTtrn_sub_task_compl_date().toString())));
					else
						objForAppend.put("ttrn_completed_date", "");
					if (subTaskTranscational.getTtrn_sub_task_comment() != null)
						objForAppend.put("ttrn_performer_comments", subTaskTranscational.getTtrn_sub_task_comment());
					else
						objForAppend.put("ttrn_performer_comments", "");

					objForAppend.put("ttrn_task_status", taskStatus);
					objForAppend.put("ttrn_legal_task_status", legalTaskStatus);
					objForAppend.put("ttrn_legal_task_style", legalClass);
					objForAppend.put("ttrn_reason_for_non_compliance",
							subTaskTranscational.getTtrn_sub_task_reason_for_non_compliance());
					// objForAppend.put("ttrn_next_examination_date",
					// sdfOutForDisplay.format(sdfIn.parse(subTaskTranscational.getTttn_sub_task_next_examination_date().toString())));

					/*-----------Getting Completed by first name last name------------------------------------------------------*/
					int user_id = 0;
					if (subTaskTranscational.getTtrn_sub_task_completed_by() != null) {
						user_id = Integer.parseInt(subTaskTranscational.getTtrn_sub_task_completed_by().toString());
					}
					if (user_id > 0) {
						User user = usersDao.getUserById(user_id);
						objForAppend.put("ttrn_task_completed_by",
								user.getUser_first_name() + " " + user.getUser_last_name());
					} else
						objForAppend.put("ttrn_task_completed_by", "");

					/*-----------Getting Completed by first name last name ends here--------------------------------------------*/
					objForAppend.put("ttrn_document", subTaskTranscational.getTtrn_sub_task_document());
					objForAppend.put("ttrn_sub_id", subTaskTranscational.getTtrn_sub_task_id());
					objForAppend.put("ttrn_sub_task_id", subTaskTranscational.getTtrn_sub_id());

					SubTask subTask = tasksDao
							.getSubTaskDetailsBysub_task_id(subTaskTranscational.getTtrn_sub_task_id());

					objForAppend.put("ttrn_sub_equip_number", subTask.getSub_equipment_number());
					objForAppend.put("ttrn_sub_equip_type", subTask.getSub_equipment_type());
					objForAppend.put("ttrn_sub_equip_loca", subTask.getSub_equipment_location());
					objForAppend.put("ttrn_sub_equip_desc", subTask.getSub_equipment_description());
					objForAppend.put("ttrn_sub_equip_frequency", subTask.getSub_frequency());

					List<UploadedSubTaskDocuments> attachedDocuments = uploadedDocumentsDao
							.getAllDocumentBySubTtrnId(subTaskTranscational.getTtrn_sub_id());

					if (attachedDocuments != null) {
						Iterator<UploadedSubTaskDocuments> itre = attachedDocuments.iterator();
						JSONArray docArray = new JSONArray();

						while (itre.hasNext()) {
							UploadedSubTaskDocuments uploadedDocuments = (UploadedSubTaskDocuments) itre.next();
							JSONObject docObj = new JSONObject();
							docObj.put("udoc_id", uploadedDocuments.getUdoc_sub_task_id());
							docObj.put("udoc_original_file_name",
									uploadedDocuments.getUdoc_sub_task_original_file_name());
							docArray.add(docObj);
						}

						objForAppend.put("document_list", docArray);
					} else {
						objForAppend.put("document_list", new JSONArray());
					}

					// objForAppend.put("task_frequency",
					// tasksDao.getOriginalFrequency(Integer.parseInt(object[0].toString())));
					objForAppend.put("client_task_id", tmap_client_task_id);
					subTaskdataForAppend.add(objForAppend);

				}
			}

			// }
			objForSend.put("subTaskHistory", subTaskdataForAppend);

			return objForSend.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("repsonseMessage", "Failed");
			return objForSend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all Tasks rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String getTaskDetailsByClientTaskId(String jsonString, HttpSession session) {
		JSONObject objForSend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			JSONArray dataForAppend = new JSONArray();

			String tmap_client_task_id = jsonObj.get("tmap_client_task_id").toString();
			List<Object> taskDetails = tasksDao.getTaskDetailsByClientTaskid(tmap_client_task_id);
			Object[] object = (Object[]) taskDetails.get(0);
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("unit_task_id", object[0]);
			objForAppend.put("entity", object[1]);
			objForAppend.put("unit", object[2]);
			objForAppend.put("function", object[3]);
			objForAppend.put("executor", object[4].toString() + " " + object[5].toString());
			objForAppend.put("evaluator", object[6].toString() + " " + object[7].toString());
			objForAppend.put("function_head", object[8].toString() + " " + object[9].toString());
			objForAppend.put("legislation", object[10]);
			objForAppend.put("rule", object[11]);
			objForAppend.put("reference", object[12]);
			objForAppend.put("who", object[13]);
			objForAppend.put("when", object[14]);
			objForAppend.put("activity", object[15]);
			objForAppend.put("procedure", object[16]);
			objForAppend.put("more_information", object[17]);
			objForAppend.put("prescriptive", object[18]);
			objForAppend.put("frequency", object[19]);
			objForAppend.put("form_no", object[20]);
			objForAppend.put("specific_due_date", object[21]);
			objForAppend.put("type_of_task", object[22]);
			objForAppend.put("level", object[23]);
			objForAppend.put("excemption_criteria", object[24]);
			objForAppend.put("event", object[25]);
			objForAppend.put("sub_event", object[26]);
			objForAppend.put("implications", object[27]);
			objForAppend.put("imprisonment", object[28]);
			objForAppend.put("imprisonment_applies_to", object[29]);
			objForAppend.put("fine_amount", object[30]);
			objForAppend.put("fine_amount_per_day", object[31]);
			objForAppend.put("impact", object[32]);
			objForAppend.put("impact_on_entity", object[33]);
			objForAppend.put("impact_on_unit", object[34]);
			objForAppend.put("interlinkage", object[35]);
			objForAppend.put("linked_task_id", object[36]);
			objForAppend.put("weblink", object[37]);
			objForAppend.put("due_date", object[38]);
			objForAppend.put("lexcare_task_id", object[39]);

			objForAppend.putIfAbsent("auditoComments", object[40] != null ? object[40].toString() : "NA");

			String auditorAuditTime = "";
			if (object[41] != null) {
				auditorAuditTime = sdfOutForDisplay.format(sdfIn.parse(object[41].toString()));
			} else {
				auditorAuditTime = "";
			}

			objForAppend.putIfAbsent("auditorAuditTime", auditorAuditTime);
			objForAppend.putIfAbsent("auditorStatus", object[42] != null ? object[42].toString() : "NA");
			objForAppend.putIfAbsent("auditor_performer_by_id", object[43] != null ? object[43].toString() : "0");

			// SimpleDateFormat sdfo = new SimpleDateFormat("yyyy-MM-dd");
			// Date cuDate = new Date();
			// String currentDateD2 = sdfo.format(cuDate);
			// Date d1 = null;
			// Date d2 = null;
			// if (object[46] != null) {
			// d1 = sdfo.parse(object[44].toString()); // Audit Date
			// d2 = sdfo.parse(currentDateD2); // Current Date
			//
			// String adDate = sdfo.format(d1); // Audit Date
			// String cDate = sdfo.format(d2); // Current Date
			//
			// LocalDate currentDates = LocalDate.parse(cDate);
			// LocalDate auditDate = LocalDate.parse(adDate);
			//
			// System.out.println("AudiDate : " + object[44].toString());
			//
			//// if (d1.before(d2) || d1.equals(d2)) {
			// if ((auditDate.isAfter(currentDates) || auditDate.equals(currentDates)) &&
			// auditDate != null) {
			// System.out.println("before and equals currentDates : " + currentDates + "\t
			// d2 : " + auditDate);
			// objForAppend.put("auditConditionTwo", "Yes");
			// } else {
			// System.out.println("before and equals else part currentDates : " +
			// currentDates
			// + "\t auditDate : " + auditDate);
			// objForAppend.put("auditConditionTwo", "No");
			// }
			// } else {
			// objForAppend.put("auditConditionTwo", "Yes");
			// }

			dataForAppend.add(objForAppend);
			objForSend.put("task_details", dataForAppend);
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method created : Harshad Padole
	// Method Purpose : get multiple task for completion where same task assigned to
	// multiple location with same performer/executor
	@SuppressWarnings("unchecked")
	@Override
	public String getMultipleTaskForCompletion(String jsonString, HttpSession session) {
		JSONObject dataToSend = new JSONObject();
		try {

			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String tmap_client_task_id = jsonObj.get("tmap_client_task_id").toString();
			int ttrn_id = Integer.parseInt(jsonObj.get("ttrn_id").toString());
			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			int role_id = Integer.parseInt(session.getAttribute("sess_role_id").toString());
			List<Object> taskDetails = tasksDao.getTaskDetailsByClientTaskid(tmap_client_task_id);
			Object[] objects = (Object[]) taskDetails.get(0);

			String lexcare_id = objects[39].toString();
			JSONArray task_list = new JSONArray();
			System.out.println("lexcare_id:" + lexcare_id + "ttrn_id:" + ttrn_id);
			List<Object> Task_list = tasksDao.getTaskForMultipleCompletion(lexcare_id, user_id, role_id, ttrn_id);

			Iterator<Object> iterator = Task_list.iterator();
			while (iterator.hasNext()) {

				Object[] object = (Object[]) iterator.next();
				JSONObject objForAppend = new JSONObject();

				objForAppend.put("ttrn_id", object[0]);
				objForAppend.put("client_task_id", object[1]);
				objForAppend.put("orga_name", object[2]);
				objForAppend.put("loca_name", object[3]);
				objForAppend.put("dept_name", object[4]);
				objForAppend.put("pr_name", object[5] + " " + object[6]);
				objForAppend.put("rw_name", object[7] + " " + object[8]);
				objForAppend.put("fh_name", object[9] + " " + object[10]);
				objForAppend.put("legi_name", object[11].toString());
				objForAppend.put("rule_name", object[12].toString());
				objForAppend.put("ref", object[13].toString());
				objForAppend.put("who", object[14].toString());
				objForAppend.put("when", object[15].toString());
				objForAppend.put("activity", object[16].toString());
				objForAppend.put("procedure", object[17].toString());
				objForAppend.put("more_info", object[18].toString());
				objForAppend.put("lexcare_id", object[19]);
				objForAppend.put("interlink_id", object[20]);
				objForAppend.put("status", object[21]);
				objForAppend.put("pr_date", sdfOutForDisplay.format(sdfIn.parse(object[22].toString())));
				objForAppend.put("legal_date", sdfOutForDisplay.format(sdfIn.parse(object[23].toString())));
				objForAppend.put("frequency_for_operation", object[24].toString());
				task_list.add(objForAppend);
			}

			dataToSend.put("task_list", task_list);
			dataToSend.put("response", "success");
			return dataToSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			dataToSend.put("response", "fail");
			return dataToSend.toJSONString();
		}
	}

}
