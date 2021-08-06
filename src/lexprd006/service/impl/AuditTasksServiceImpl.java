package lexprd006.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.LocalDate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lexprd006.dao.AuditTasksDAO;
import lexprd006.dao.EntityDao;
import lexprd006.dao.EntityMappingDao;
import lexprd006.dao.FunctionDao;
import lexprd006.dao.TasksConfigurationDao;
import lexprd006.dao.TasksUserMappingDao;
import lexprd006.dao.UnitDao;
import lexprd006.dao.UploadedDocumentsDao;
import lexprd006.dao.UserEntityMappingDao;
import lexprd006.dao.UsersDao;
import lexprd006.domain.Department;
import lexprd006.domain.Location;
import lexprd006.domain.Organization;
import lexprd006.domain.TaskTransactional;
import lexprd006.domain.TaskUserMapping;
import lexprd006.domain.UploadedDocuments;
import lexprd006.domain.UploadedPODDocuments;
import lexprd006.domain.User;
import lexprd006.service.AuditTasksService;
import lexprd006.service.UtilitiesService;

@Service
public class AuditTasksServiceImpl implements AuditTasksService {

	@Autowired
	AuditTasksDAO auditTasksDAO;

	@Autowired
	UploadedDocumentsDao uploadedDocumentsDao;

	@Autowired
	UserEntityMappingDao userEntityMappingDao;

	@Autowired
	TasksConfigurationDao tasksconfigurationdao;

	@Autowired
	EntityMappingDao entityMappingDao;

	@Autowired
	UtilitiesService utilitiesService;

	@Autowired
	UsersDao userDao;

	@Autowired
	EntityDao entityDao;

	@Autowired
	UnitDao unitDao;

	@Autowired
	FunctionDao functionDao;

	@Autowired
	TasksUserMappingDao tasksUserMappingDao;

	private @Value("#{config['mail_user_name'] ?: 'null'}") String username;
	private @Value("#{config['mail_password'] ?: 'null'}") String password;
	private @Value("#{config['mail_smtp_host'] ?: 'null'}") String hostName;
	private @Value("#{config['mail_smtp_port'] ?: 'null'}") String portNo;
	private @Value("#{config['mail_from'] ?: 'null'}") String mailFrom;
	private @Value("#{config['project_name'] ?: 'null'}") String projectName;
	private @Value("#{config['project_url'] ?: 'null'}") String url;

	public final SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd");
	public final SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd");
	public final SimpleDateFormat sdfOutDisplay = new SimpleDateFormat("dd-MM-yyyy");

	@SuppressWarnings("unchecked")
	@Override
	public String getMonthlyComplianceStatus(String jsonString, HttpSession session) {
		JSONObject objForSend = new JSONObject();
		JSONArray tasksList = new JSONArray();
		int totalTasksInLoop = 0;
		int totalActiveTasks = 0;
		int totalCompletedTasks = 0;
		int complied = 0;
		int noncomplied = 0;
		int posingrisk = 0;
		int delayed = 0;
		int pending = 0;
		int partially_Completed = 0;
		int re_opened = 0;
		int delayed_reported = 0;
		try {
			List<Object> allTask = auditTasksDAO.getMonthlyComplianceStatus(
					Integer.parseInt(session.getAttribute("sess_user_id").toString()),
					Integer.parseInt(session.getAttribute("sess_role_id").toString()), jsonString);
			Iterator<Object> itr = allTask.iterator();
			objForSend.put("totalTasks", allTask.size());
			Date currentDate = sdfIn.parse(sdfOut.format(new Date()));
			Date CompletedDate = null;

			// System.out.println("allTask : " + allTask.size());

			while (itr.hasNext()) {

				totalTasksInLoop++;
				Object[] object = (Object[]) itr.next();

				Date performerDueDate = sdfIn.parse(object[3].toString());
				Date legalDueDate = sdfIn.parse(object[7].toString());

				if (object[7] != null) {
					if (object[2].toString().equals("Completed")) {
						totalCompletedTasks++;
						legalDueDate = sdfIn.parse(object[7].toString());
						Date submittedDate = sdfIn.parse(object[8].toString());
						if (object[27] != null) {
							CompletedDate = sdfIn.parse(object[27].toString());
						}

						/**
						 * if opens
						 */

						if (legalDueDate.after(submittedDate) || legalDueDate.equals(submittedDate)) {
							complied++;

							// System.out.println("object[9] orga_id : " + object[9]);
							JSONObject taskObj = new JSONObject();
							taskObj.put("date", sdfOut.format(submittedDate));
							taskObj.put("status", "complied");
							taskObj.put("orga_id", object[9]);
							taskObj.put("orga_name", object[10]);
							taskObj.put("loca_id", object[11]);
							taskObj.put("loca_name", object[12]);
							taskObj.put("dept_id", object[13]);
							taskObj.put("dept_name", object[14]);
							taskObj.put("tsk_impact", object[22]);
							// Task Details
							taskObj.put("ttrn_client_task_id", object[1]);
							taskObj.put("ttrn_legal_due_date", sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
							taskObj.put("task_legi_name", object[15]);
							taskObj.put("task_rule_name", object[16]);
							taskObj.put("task_reference", object[17]);
							taskObj.put("task_who", object[18]);
							taskObj.put("task_when", object[19]);
							taskObj.put("task_activity", object[20]);
							taskObj.put("task_executor", object[25].toString() + " " + object[26].toString());
							taskObj.put("ttrn_pr_due_date", sdfOutDisplay.format(sdfIn.parse(object[3].toString())));
							taskObj.put("ttrn_rw_due_date", sdfOutDisplay.format(sdfIn.parse(object[4].toString())));
							taskObj.put("ttrn_fh_due_date", sdfOutDisplay.format(sdfIn.parse(object[5].toString())));
							taskObj.put("ttrn_uh_due_date", sdfOutDisplay.format(sdfIn.parse(object[6].toString())));
							taskObj.put("task_cat_law", object[28].toString());
							taskObj.put("ttrn_frequency_for_operation", object[23].toString());
							taskObj.put("ttrn_id", object[0]);

							taskObj.put("ttrn_legal_task_status", "Complied");
							taskObj.put("ttrn_legal_task_style", "Complied");

							taskObj.put("task_implication", object[31].toString());
							taskObj.put("task_evaluator", object[32].toString() + " " + object[33].toString());
							taskObj.put("task_fun_head", object[34].toString() + " " + object[35].toString());

							String completedDate = "";
							if (object[35] != null) {
								completedDate = object[35].toString();
							} else {
								completedDate = "";
							}

							taskObj.put("performBy", object[36] != null ? object[36].toString() : "NA");
							taskObj.put("ttrn_performer_comments", object[37] != null ? object[37].toString() : "NA");

							taskObj.put("completedDate", completedDate);

							if (object[29] != null)
								taskObj.put("comments", object[29].toString());
							else
								taskObj.put("comments", " ");

							if (object[30] != null)
								taskObj.put("reasonForNonCompliance", object[30].toString());
							else
								taskObj.put("reasonForNonCompliance", " ");

							// System.out.println("object[0] : " + object[0] != null ? object[0].toString()
							// : "NA");

							if (object[0] != null) {
								List<UploadedDocuments> attachedDocuments = uploadedDocumentsDao
										.getAllDocumentByTtrnId(Integer.parseInt(object[0].toString()));
								if (attachedDocuments != null) {
									taskObj.put("document_attached", 1);
									Iterator<UploadedDocuments> itre = attachedDocuments.iterator();
									JSONArray docArray = new JSONArray();
									while (itre.hasNext()) {
										UploadedDocuments uploadedDocuments = (UploadedDocuments) itre.next();
										JSONObject docObj = new JSONObject();
										docObj.put("udoc_id", uploadedDocuments.getUdoc_id());
										docObj.put("udoc_original_file_name",
												uploadedDocuments.getUdoc_original_file_name());
										docArray.add(docObj);
									}
									taskObj.put("document_list", docArray);
								} else {
									taskObj.put("document_list", new JSONArray());
									taskObj.put("document_attached", 0);
								}
							} else {
								taskObj.put("document_list", new JSONArray());
								taskObj.put("document_attached", 0);
							}
							taskObj.put("task_type", "Main");
							tasksList.add(taskObj);
						} // complied

						/**
						 * if close
						 */

						else {
							if (submittedDate.after(legalDueDate) && CompletedDate.after(legalDueDate)) {
								delayed++;
								JSONObject taskObj = new JSONObject();
								taskObj.put("date", sdfOut.format(legalDueDate));
								taskObj.put("status", "delayed");
								taskObj.put("orga_id", object[9]);
								taskObj.put("orga_name", object[10]);
								taskObj.put("loca_id", object[11]);
								taskObj.put("loca_name", object[12]);
								taskObj.put("dept_id", object[13]);
								taskObj.put("dept_name", object[14]);
								taskObj.put("tsk_impact", object[22]);
								// Task Details
								taskObj.put("ttrn_client_task_id", object[1]);
								taskObj.put("ttrn_legal_due_date",
										sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
								taskObj.put("task_legi_name", object[15]);
								taskObj.put("task_rule_name", object[16]);
								taskObj.put("task_reference", object[17]);
								taskObj.put("task_who", object[18]);
								taskObj.put("task_when", object[19]);
								taskObj.put("task_activity", object[20]);
								taskObj.put("task_executor", object[25].toString() + " " + object[26].toString());
								taskObj.put("ttrn_pr_due_date",
										sdfOutDisplay.format(sdfIn.parse(object[3].toString())));
								taskObj.put("ttrn_rw_due_date",
										sdfOutDisplay.format(sdfIn.parse(object[4].toString())));
								taskObj.put("ttrn_fh_due_date",
										sdfOutDisplay.format(sdfIn.parse(object[5].toString())));
								taskObj.put("ttrn_uh_due_date",
										sdfOutDisplay.format(sdfIn.parse(object[6].toString())));
								taskObj.put("task_cat_law", object[28].toString());
								taskObj.put("ttrn_frequency_for_operation", object[23].toString());
								taskObj.put("ttrn_id", object[0]);
								taskObj.put("task_implication", object[31].toString());
								taskObj.put("task_evaluator", object[32].toString() + " " + object[33].toString());
								taskObj.put("task_fun_head", object[34].toString() + " " + object[35].toString());

								taskObj.put("ttrn_legal_task_status", "Delayed");
								taskObj.put("ttrn_legal_task_style", "Delayed");

								/*
								 * taskObj.put("auditDate", object[36] != null ? object[36].toString() : "NA");
								 * taskObj.put("reOpenDateWindow", object[37] != null ? object[37].toString() :
								 * "NA");
								 */

								if (object[29] != null)
									taskObj.put("comments", object[29].toString());
								else
									taskObj.put("comments", " ");

								if (object[30] != null)
									taskObj.put("reasonForNonCompliance", object[30].toString());
								else
									taskObj.put("reasonForNonCompliance", " ");

								if (object[0] != null) {
									List<UploadedDocuments> attachedDocuments = uploadedDocumentsDao
											.getAllDocumentByTtrnId(Integer.parseInt(object[0].toString()));

									if (attachedDocuments != null) {
										taskObj.put("document_attached", 1);

										Iterator<UploadedDocuments> itre = attachedDocuments.iterator();
										JSONArray docArray = new JSONArray();

										while (itre.hasNext()) {
											UploadedDocuments uploadedDocuments = (UploadedDocuments) itre.next();
											JSONObject docObj = new JSONObject();
											docObj.put("udoc_id", uploadedDocuments.getUdoc_id());
											docObj.put("udoc_original_file_name",
													uploadedDocuments.getUdoc_original_file_name());
											docArray.add(docObj);
										}
										taskObj.put("document_list", docArray);
									} else {
										taskObj.put("document_list", new JSONArray());
										taskObj.put("document_attached", 0);
									}

								} else {
									taskObj.put("document_list", new JSONArray());
									taskObj.put("document_attached", 0);
								}

								taskObj.put("task_type", "Main");
								tasksList.add(taskObj);

							}
							if (submittedDate.after(legalDueDate)
									&& (CompletedDate.before(legalDueDate) || CompletedDate.equals(legalDueDate))) {

								delayed_reported++;
								JSONObject taskObj = new JSONObject();
								taskObj.put("date", sdfOut.format(legalDueDate));
								taskObj.put("status", "delayed-reported");
								taskObj.put("orga_id", object[9]);
								taskObj.put("orga_name", object[10]);
								taskObj.put("loca_id", object[11]);
								taskObj.put("loca_name", object[12]);
								taskObj.put("dept_id", object[13]);
								taskObj.put("dept_name", object[14]);
								taskObj.put("tsk_impact", object[22]);
								// Task Details
								taskObj.put("ttrn_client_task_id", object[1]);
								taskObj.put("ttrn_legal_due_date",
										sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
								taskObj.put("task_legi_name", object[15]);
								taskObj.put("task_rule_name", object[16]);
								taskObj.put("task_reference", object[17]);
								taskObj.put("task_who", object[18]);
								taskObj.put("task_when", object[19]);
								taskObj.put("task_activity", object[20]);
								taskObj.put("task_executor", object[25].toString() + " " + object[26].toString());
								taskObj.put("ttrn_pr_due_date",
										sdfOutDisplay.format(sdfIn.parse(object[3].toString())));
								taskObj.put("ttrn_rw_due_date",
										sdfOutDisplay.format(sdfIn.parse(object[4].toString())));
								taskObj.put("ttrn_fh_due_date",
										sdfOutDisplay.format(sdfIn.parse(object[5].toString())));
								taskObj.put("ttrn_uh_due_date",
										sdfOutDisplay.format(sdfIn.parse(object[6].toString())));
								taskObj.put("task_cat_law", object[28].toString());
								taskObj.put("ttrn_frequency_for_operation", object[23].toString());
								taskObj.put("ttrn_id", object[0]);
								taskObj.put("task_implication", object[31].toString());
								taskObj.put("task_evaluator", object[32].toString() + " " + object[33].toString());
								taskObj.put("task_fun_head", object[34].toString() + " " + object[35].toString());

								taskObj.put("ttrn_legal_task_status", "Delayed");
								taskObj.put("ttrn_legal_task_style", "Delayed");

								/*
								 * taskObj.put("auditDate", object[36] != null ? object[36].toString() : "NA");
								 * taskObj.put("reOpenDateWindow", object[37] != null ? object[37].toString() :
								 * "NA");
								 */

								if (object[29] != null)
									taskObj.put("comments", object[29].toString());
								else
									taskObj.put("comments", " ");

								if (object[30] != null)
									taskObj.put("reasonForNonCompliance", object[30].toString());
								else
									taskObj.put("reasonForNonCompliance", " ");

								if (object[0] != null) {
									List<UploadedDocuments> attachedDocuments = uploadedDocumentsDao
											.getAllDocumentByTtrnId(Integer.parseInt(object[0].toString()));

									if (attachedDocuments != null) {
										taskObj.put("document_attached", 1);

										Iterator<UploadedDocuments> itre = attachedDocuments.iterator();
										JSONArray docArray = new JSONArray();

										while (itre.hasNext()) {
											UploadedDocuments uploadedDocuments = (UploadedDocuments) itre.next();
											JSONObject docObj = new JSONObject();
											docObj.put("udoc_id", uploadedDocuments.getUdoc_id());
											docObj.put("udoc_original_file_name",
													uploadedDocuments.getUdoc_original_file_name());
											docArray.add(docObj);
										}
										taskObj.put("document_list", docArray);
									} else {
										taskObj.put("document_list", new JSONArray());
										taskObj.put("document_attached", 0);
									}

								} else {
									taskObj.put("document_list", new JSONArray());
									taskObj.put("document_attached", 0);
								}

								taskObj.put("task_type", "Main");
								tasksList.add(taskObj);

							}

							// delayed tasks if ends here
						}

					} else {

						if (object[2].toString().equals("Active")) {
							totalActiveTasks++;
							legalDueDate = sdfIn.parse(object[7].toString());
							Date prdueDate = sdfIn.parse(object[3].toString());
							if (currentDate.after(legalDueDate)) {
								noncomplied++;
								JSONObject taskObj = new JSONObject();
								taskObj.put("date", sdfOut.format(legalDueDate));
								taskObj.put("status", "noncomplied");
								taskObj.put("orga_id", object[9]);
								taskObj.put("orga_name", object[10]);
								taskObj.put("loca_id", object[11]);
								taskObj.put("loca_name", object[12]);
								taskObj.put("dept_id", object[13]);
								taskObj.put("dept_name", object[14]);
								taskObj.put("tsk_impact", object[22]);
								taskObj.put("ttrn_legal_task_status", "Non Complied");
								taskObj.put("ttrn_legal_task_style", "Non-Complied");
								// Task Details
								taskObj.put("ttrn_client_task_id", object[1]);
								taskObj.put("ttrn_legal_due_date",
										sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
								taskObj.put("task_legi_name", object[15]);
								taskObj.put("task_rule_name", object[16]);
								taskObj.put("task_reference", object[17]);
								// System.out.println("object[17] task_reference : " + object[17].toString());
								taskObj.put("task_who", object[18]);
								taskObj.put("task_when", object[19]);
								taskObj.put("task_activity", object[20]);
								taskObj.put("task_executor", object[25].toString() + " " + object[26].toString());
								taskObj.put("task_type", "Main");
								taskObj.put("ttrn_pr_due_date",
										sdfOutDisplay.format(sdfIn.parse(object[3].toString())));
								taskObj.put("ttrn_rw_due_date",
										sdfOutDisplay.format(sdfIn.parse(object[4].toString())));
								taskObj.put("ttrn_fh_due_date",
										sdfOutDisplay.format(sdfIn.parse(object[5].toString())));
								taskObj.put("ttrn_uh_due_date", taskObj.put("task_cat_law", object[28].toString()));
								taskObj.put("ttrn_frequency_for_operation", object[23].toString());
								taskObj.put("ttrn_id", object[0]);
								taskObj.put("task_implication", object[31].toString());
								taskObj.put("task_evaluator", object[32].toString() + " " + object[33].toString());
								taskObj.put("task_fun_head", object[34].toString() + " " + object[35].toString());
								if (object[29] != null)
									taskObj.put("comments", object[29].toString());
								else
									taskObj.put("comments", " ");

								if (object[30] != null)
									taskObj.put("reasonForNonCompliance", object[30].toString());
								else
									taskObj.put("reasonForNonCompliance", " ");

								if (object[0] != null) {
									List<UploadedDocuments> attachedDocuments = uploadedDocumentsDao
											.getAllDocumentByTtrnId(Integer.parseInt(object[0].toString()));
									if (attachedDocuments != null) {
										taskObj.put("document_attached", 1);
										Iterator<UploadedDocuments> itre = attachedDocuments.iterator();
										JSONArray docArray = new JSONArray();
										while (itre.hasNext()) {
											UploadedDocuments uploadedDocuments = (UploadedDocuments) itre.next();
											JSONObject docObj = new JSONObject();
											docObj.put("udoc_id", uploadedDocuments.getUdoc_id());
											docObj.put("udoc_original_file_name",
													uploadedDocuments.getUdoc_original_file_name());
											docArray.add(docObj);
										}
										taskObj.put("document_list", docArray);
									} else {
										taskObj.put("document_list", new JSONArray());
										taskObj.put("document_attached", 0);
									}
								} else {
									taskObj.put("document_list", new JSONArray());
									taskObj.put("document_attached", 0);
								}
								taskObj.put("task_type", "Main");
								tasksList.add(taskObj);
							}
						}

						/**
						 * 
						 */
						if (object[2].toString().equals("Re_Opened")) {

							re_opened++;
							JSONObject taskObj = new JSONObject();
							taskObj.put("date", sdfOut.format(legalDueDate));
							taskObj.put("status", "reopen");
							taskObj.put("orga_id", object[9]);
							taskObj.put("orga_name", object[10]);
							taskObj.put("loca_id", object[11]);
							taskObj.put("loca_name", object[12]);
							taskObj.put("dept_id", object[13]);
							taskObj.put("dept_name", object[14]);
							taskObj.put("tsk_impact", object[22]);
							taskObj.put("ttrn_legal_task_status", "Non Complied");
							taskObj.put("ttrn_legal_task_style", "Non-Complied");
							// Task Details
							taskObj.put("ttrn_client_task_id", object[1]);
							taskObj.put("ttrn_legal_due_date", sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
							taskObj.put("task_legi_name", object[15]);
							taskObj.put("task_rule_name", object[16]);
							taskObj.put("task_reference", object[17]);
							// System.out.println("object[17] task_reference : " + object[17].toString());
							taskObj.put("task_who", object[18]);
							taskObj.put("task_when", object[19]);
							taskObj.put("task_activity", object[20]);
							taskObj.put("task_executor", object[25].toString() + " " + object[26].toString());
							taskObj.put("task_type", "Main");
							taskObj.put("ttrn_pr_due_date", sdfOutDisplay.format(sdfIn.parse(object[3].toString())));
							taskObj.put("ttrn_rw_due_date", sdfOutDisplay.format(sdfIn.parse(object[4].toString())));
							taskObj.put("ttrn_fh_due_date", sdfOutDisplay.format(sdfIn.parse(object[5].toString())));
							taskObj.put("ttrn_uh_due_date", taskObj.put("task_cat_law", object[28].toString()));
							taskObj.put("ttrn_frequency_for_operation", object[23].toString());
							taskObj.put("ttrn_id", object[0]);
							taskObj.put("task_implication", object[31].toString());
							taskObj.put("task_evaluator", object[32].toString() + " " + object[33].toString());
							taskObj.put("task_fun_head", object[34].toString() + " " + object[35].toString());
							if (object[29] != null)
								taskObj.put("comments", object[29].toString());
							else
								taskObj.put("comments", " ");

							if (object[30] != null)
								taskObj.put("reasonForNonCompliance", object[30].toString());
							else
								taskObj.put("reasonForNonCompliance", " ");

							if (object[0] != null) {
								List<UploadedDocuments> attachedDocuments = uploadedDocumentsDao
										.getAllDocumentByTtrnId(Integer.parseInt(object[0].toString()));
								if (attachedDocuments != null) {
									taskObj.put("document_attached", 1);
									Iterator<UploadedDocuments> itre = attachedDocuments.iterator();
									JSONArray docArray = new JSONArray();
									while (itre.hasNext()) {
										UploadedDocuments uploadedDocuments = (UploadedDocuments) itre.next();
										JSONObject docObj = new JSONObject();
										docObj.put("udoc_id", uploadedDocuments.getUdoc_id());
										docObj.put("udoc_original_file_name",
												uploadedDocuments.getUdoc_original_file_name());
										docArray.add(docObj);
									}
									taskObj.put("document_list", docArray);
								} else {
									taskObj.put("document_list", new JSONArray());
									taskObj.put("document_attached", 0);
								}
							} else {
								taskObj.put("document_list", new JSONArray());
								taskObj.put("document_attached", 0);
							}
							taskObj.put("task_type", "Main");
							tasksList.add(taskObj);

						}

					}

					/**
					 * Completed if ends here
					 */

				}
			}

			objForSend.put("Complied", complied);
			objForSend.put("NonComplied", noncomplied);
			// objForSend.put("PosingRisk", posingrisk);
			objForSend.put("Delayed", delayed);
			objForSend.put("totaltasksinloop", totalTasksInLoop);
			objForSend.put("totalactivetasks", totalActiveTasks);
			objForSend.put("totalcompletedtasks", totalCompletedTasks);
			// objForSend.put("Pending", pending);
			// objForSend.put("WaitingForApproval", partially_Completed);
			objForSend.put("ReOpened", re_opened);
			objForSend.put("delayed_reported", delayed_reported);
			// System.out.println("tasksList : " + tasksList.toString());
			objForSend.put("taskList", tasksList);
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("ErrorMessage", "Failed");
			return objForSend.toJSONString();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getHeadCountsByLocation(HttpSession session, HttpServletResponse res) {
		JSONArray dataForSend = new JSONArray();
		try {
			List<Object> headCountsByLocation = auditTasksDAO.getHeadCountsByLocation(session, res);
			if (headCountsByLocation.size() > 0) {
				Iterator<Object> iterator = headCountsByLocation.iterator();
				while (iterator.hasNext()) {
					JSONObject obj = new JSONObject();
					Object nxt[] = (Object[]) iterator.next();
					obj.put("Location", nxt[0].toString());
					obj.put("Male", nxt[1].toString());
					obj.put("Female", nxt[2].toString());
					obj.put("NotAssigned", nxt[3].toString());
					obj.put("Total", nxt[4].toString());
					dataForSend.add(obj);
				}
				return dataForSend.toJSONString();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String searchAuditRepository(String jsonString, HttpSession session, HttpServletResponse res) {
		try {
			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			int user_role_id = Integer.parseInt(session.getAttribute("sess_role_id").toString());

			List<Object> repo = auditTasksDAO.searchAuditRepository(jsonString, user_id, user_role_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String auditTaskDashboard(String jsonString, HttpSession session) {

		// System.out.println("inside getOverallComplianceGraph () ");
		JSONObject objForSend = new JSONObject();
		JSONArray tasksList = new JSONArray();
		int totalTasksInLoop = 0;
		int totalActiveTasks = 0;
		int totalCompletedTasks = 0;
		int complied = 0;
		int noncomplied = 0;
		int posingrisk = 0;
		int delayed = 0;
		int pending = 0;
		int partially_Completed = 0;
		int re_opened = 0;
		int delayed_reported = 0;

		try {
			List<Object> allTask = auditTasksDAO.auditTaskDashboard(
					Integer.parseInt(session.getAttribute("sess_user_id").toString()),
					Integer.parseInt(session.getAttribute("sess_role_id").toString()), jsonString);

			if (allTask.size() > 0 || allTask != null) {
				Iterator<Object> itr = allTask.iterator();

				while (itr.hasNext()) {
					Object[] object = (Object[]) itr.next();
					JSONObject taskObj = new JSONObject();
					taskObj.put("date", object[8] != null ? object[8].toString() : "NA");
					if (object[40].toString().equalsIgnoreCase("Complied")) {
						complied++;
						taskObj.put("status", "complied");
					} else if (object[40].toString().equalsIgnoreCase("NonComplied")) {
						noncomplied++;
						taskObj.put("status", "noncomplied");
					} else {
						re_opened++;
						taskObj.put("status", "reopen");
					}

					// taskObj.put("status", object[40].toString());
					taskObj.put("orga_id", object[9]);
					taskObj.put("orga_name", object[10]);
					taskObj.put("loca_id", object[11]);
					taskObj.put("loca_name", object[12]);
					taskObj.put("dept_id", object[13]);
					taskObj.put("dept_name", object[14]);
					taskObj.put("tsk_impact", object[22]);
					taskObj.put("ttrn_client_task_id", object[1]);
					taskObj.put("ttrn_legal_due_date", sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
					taskObj.put("task_legi_name", object[15]);
					taskObj.put("task_rule_name", object[16]);
					taskObj.put("task_reference", object[17]);
					taskObj.put("task_who", object[18]);
					taskObj.put("task_when", object[19]);
					taskObj.put("task_activity", object[20]);
					taskObj.put("task_executor", object[25].toString() + " " + object[26].toString());
					taskObj.put("ttrn_pr_due_date", sdfOutDisplay.format(sdfIn.parse(object[3].toString())));
					taskObj.put("ttrn_rw_due_date", sdfOutDisplay.format(sdfIn.parse(object[4].toString())));
					taskObj.put("ttrn_fh_due_date", sdfOutDisplay.format(sdfIn.parse(object[5].toString())));
					taskObj.put("ttrn_uh_due_date", sdfOutDisplay.format(sdfIn.parse(object[6].toString())));
					taskObj.put("task_cat_law", object[28].toString());
					taskObj.put("ttrn_frequency_for_operation", object[23].toString());
					taskObj.put("ttrn_id", object[0]);
					taskObj.put("task_implication", object[31].toString());
					taskObj.put("task_evaluator", object[32].toString() + " " + object[33].toString());
					taskObj.put("task_fun_head", object[34].toString() + " " + object[35].toString());

					taskObj.put("auditDate", object[36] != null ? object[36].toString() : "NA");
					taskObj.put("reOpenDateWindow", object[37] != null ? object[37].toString() : "NA");
					taskObj.put("auditorComments", object[38] != null ? object[38].toString() : "NA");
					taskObj.put("auditorAuditTime", object[39] != null ? object[39].toString() : "NA");
					taskObj.put("auditorStatus", object[40] != null ? object[40].toString() : "NA");
					taskObj.put("auditor_performer_by_id", object[41] != null ? object[41].toString() : "NA");
					taskObj.put("isAuditTasks", object[42] != null ? object[42].toString() : "NA");
					taskObj.put("isDocumentUpload", object[43] != null ? object[43].toString() : "NA");
					taskObj.put("ttrn_tasks_status", object[44] != null ? object[44].toString() : "NA");

					if (object[29] != null) {
						taskObj.put("comments", object[29].toString());
					} else {
						taskObj.put("comments", " ");
					}
					if (object[30] != null) {
						taskObj.put("reasonForNonCompliance", object[30].toString());
					} else {
						taskObj.put("reasonForNonCompliance", " ");
					}

					/*
					 * if (object[0] != null) { List<UploadedDocuments> attachedDocuments =
					 * uploadedDocumentsDao
					 * .getAllDocumentByTtrnId(Integer.parseInt(object[0].toString()));
					 * 
					 * if (attachedDocuments != null) { taskObj.put("document_attached", 1);
					 * 
					 * Iterator<UploadedDocuments> itre = attachedDocuments.iterator(); JSONArray
					 * docArray = new JSONArray();
					 * 
					 * while (itre.hasNext()) { UploadedDocuments uploadedDocuments =
					 * (UploadedDocuments) itre.next(); JSONObject docObj = new JSONObject();
					 * docObj.put("udoc_id", uploadedDocuments.getUdoc_id());
					 * docObj.put("udoc_original_file_name",
					 * uploadedDocuments.getUdoc_original_file_name()); docArray.add(docObj); }
					 * taskObj.put("document_list", docArray); } else { taskObj.put("document_list",
					 * new JSONArray()); taskObj.put("document_attached", 0); }
					 * 
					 * 
					 * 
					 * } else { taskObj.put("document_list", new JSONArray());
					 * taskObj.put("document_attached", 0); }
					 */
					taskObj.put("task_type", "Main");

					tasksList.add(taskObj);
				}

			} // if end

			objForSend.put("Complied", complied);
			objForSend.put("NonComplied", noncomplied);
			// objForSend.put("PosingRisk", posingrisk);
			// objForSend.put("Delayed", delayed);
			objForSend.put("totaltasksinloop", totalTasksInLoop);
			objForSend.put("totalactivetasks", totalActiveTasks);
			objForSend.put("totalcompletedtasks", totalCompletedTasks);
			objForSend.put("Pending", pending);
			// objForSend.put("WaitingForApproval", partially_Completed);
			objForSend.put("ReOpened", re_opened);
			// objForSend.put("delayed_reported", delayed_reported);
			objForSend.put("taskList", tasksList);
			return objForSend.toJSONString();
		} catch (

		Exception e) {
			e.printStackTrace();
			objForSend.put("ErrorMessage", "Failed");
			return objForSend.toJSONString();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public String approverCompliedTasksURL(String json, HttpSession session) {
		JSONObject objForSend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(json);
			String ttrn_id = jsonObj.get("ttrn_id").toString();

			String auditoComments = null;
			if (jsonObj.get("reopen_comment") != null) {
				auditoComments = jsonObj.get("reopen_comment").toString();
			} else {
				auditoComments = null;
			}

			String taskComplied = auditTasksDAO.approverCompliedTasksURL(session, auditoComments, ttrn_id);
			// System.out.println("taskComplied : " + taskComplied.toString());
			objForSend.put("responseMessage", "Success");
			return objForSend.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("ErrorMessage", "Failed");
			return objForSend.toJSONString();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String searhMonthlyComplianceStatus(String org_id, String loca_id, String dept_id, String date_from,
			String date_to, HttpSession session) {

		JSONObject objForSend = new JSONObject();
		JSONArray tasksList = new JSONArray();
		int totalTasksInLoop = 0;
		int totalActiveTasks = 0;
		int totalCompletedTasks = 0;
		int complied = 0;
		int noncomplied = 0;
		int re_opened = 0;
		try {

			// System.out.println("orga_id : " + org_id + "\t loca_id : " + loca_id + "\t
			// dept_id : " + dept_id
			// + "\t date_from : " + date_from + "\t date_to " + date_to);

			SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");

			Date dFrom = null;
			Date dTo = null;
			String fromDate = null;
			String toDate = null;

			LocalDate l;
			LocalDate l2;
			LocalDate pL1;
			LocalDate pL2;

			if (!date_from.equalsIgnoreCase("") || !date_from.isEmpty()) {
				dFrom = s.parse(date_from);
				dTo = s.parse(date_to);
				fromDate = sd.format(dFrom);
				toDate = sd.format(dTo);

				l = new LocalDate(dFrom);
				l2 = new LocalDate(dTo);
				pL1 = l.plusDays(1);
				pL2 = l2.plusDays(1);
			} else {
				fromDate = "0";
				toDate = "0";
				Date curDate = new Date();
				String cDate = sd.format(curDate);
				LocalDate plDays = new LocalDate(cDate);
				LocalDate minusDays = plDays.minusDays(30);
				LocalDate plusDays = plDays.plusDays(30);

				pL1 = minusDays;
				pL2 = plusDays;
			}
			// System.out.println("fromDate : " + fromDate + "\t toDate : " + toDate);

			// System.out.println("plusDays : " + pL2);

			List<Object> allTask = auditTasksDAO.searhMonthlyComplianceStatus(
					Integer.parseInt(session.getAttribute("sess_user_id").toString()),
					Integer.parseInt(session.getAttribute("sess_role_id").toString()), org_id, loca_id, dept_id, pL1,
					pL2);
			Iterator<Object> itr = allTask.iterator();
			objForSend.put("totalTasks", allTask.size());
			Date currentDate = sdfIn.parse(sdfOut.format(new Date()));

			Date CompletedDate = null;
			while (itr.hasNext()) {

				totalTasksInLoop++;
				Object[] object = (Object[]) itr.next();

				Date performerDueDate = sdfIn.parse(object[3].toString());
				Date legalDueDate = sdfIn.parse(object[7].toString());

				if (object[7] != null) {
					if (object[2].toString().equals("Completed")) {
						totalCompletedTasks++;
						legalDueDate = sdfIn.parse(object[7].toString());
						Date submittedDate = sdfIn.parse(object[8].toString());

						/**
						 * if opens
						 */

						if (legalDueDate.after(submittedDate) || legalDueDate.equals(submittedDate)) {
							complied++;
							JSONObject taskObj = new JSONObject();
							taskObj.put("date", sdfOut.format(submittedDate));
							taskObj.put("status", "complied");
							taskObj.put("orga_id", object[9]);
							taskObj.put("orga_name", object[10]);
							taskObj.put("loca_id", object[11]);
							taskObj.put("loca_name", object[12]);
							taskObj.put("dept_id", object[13]);
							taskObj.put("dept_name", object[14]);
							taskObj.put("tsk_impact", object[22]);
							// Task Details
							taskObj.put("ttrn_client_task_id", object[1]);
							taskObj.put("ttrn_legal_due_date", sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
							taskObj.put("task_legi_name", object[15]);
							taskObj.put("task_rule_name", object[16]);
							taskObj.put("task_reference", object[17]);
							taskObj.put("task_who", object[18]);
							taskObj.put("task_when", object[19]);
							taskObj.put("task_activity", object[20]);
							taskObj.put("task_executor", object[25].toString() + " " + object[26].toString());
							taskObj.put("ttrn_pr_due_date", sdfOutDisplay.format(sdfIn.parse(object[3].toString())));
							taskObj.put("ttrn_rw_due_date", sdfOutDisplay.format(sdfIn.parse(object[4].toString())));
							taskObj.put("ttrn_fh_due_date", sdfOutDisplay.format(sdfIn.parse(object[5].toString())));
							taskObj.put("ttrn_uh_due_date", sdfOutDisplay.format(sdfIn.parse(object[6].toString())));
							taskObj.put("task_cat_law", object[28].toString());
							taskObj.put("ttrn_frequency_for_operation", object[23].toString());
							taskObj.put("ttrn_id", object[0]);

							taskObj.put("ttrn_legal_task_status", "Complied");
							taskObj.put("ttrn_legal_task_style", "Complied");

							taskObj.put("task_implication", object[31].toString());
							taskObj.put("task_evaluator", object[32].toString() + " " + object[33].toString());
							taskObj.put("task_fun_head", object[34].toString() + " " + object[35].toString());

							String completedDate = "";
							if (object[35] != null) {
								completedDate = object[35].toString();
							} else {
								completedDate = "";
							}

							taskObj.put("performBy", object[36] != null ? object[36].toString() : "NA");
							taskObj.put("ttrn_performer_comments", object[37] != null ? object[37].toString() : "NA");

							taskObj.put("completedDate", completedDate);

							if (object[29] != null)
								taskObj.put("comments", object[29].toString());
							else
								taskObj.put("comments", " ");

							if (object[30] != null)
								taskObj.put("reasonForNonCompliance", object[30].toString());
							else
								taskObj.put("reasonForNonCompliance", " ");

							/*
							 * if (object[0] != null) { List<UploadedDocuments> attachedDocuments =
							 * uploadedDocumentsDao
							 * .getAllDocumentByTtrnId(Integer.parseInt(object[0].toString())); if
							 * (attachedDocuments != null) { taskObj.put("document_attached", 1);
							 * Iterator<UploadedDocuments> itre = attachedDocuments.iterator(); JSONArray
							 * docArray = new JSONArray(); while (itre.hasNext()) { UploadedDocuments
							 * uploadedDocuments = (UploadedDocuments) itre.next(); JSONObject docObj = new
							 * JSONObject(); docObj.put("udoc_id", uploadedDocuments.getUdoc_id());
							 * docObj.put("udoc_original_file_name",
							 * uploadedDocuments.getUdoc_original_file_name()); docArray.add(docObj); }
							 * taskObj.put("document_list", docArray); } else { taskObj.put("document_list",
							 * new JSONArray()); taskObj.put("document_attached", 0); } } else {
							 * taskObj.put("document_list", new JSONArray());
							 * taskObj.put("document_attached", 0); }
							 * 
							 */
							taskObj.put("task_type", "Main");
							tasksList.add(taskObj);
						}

						/**
						 * if close
						 */
					} else {

						if (object[2].toString().equals("Active")) {
							totalActiveTasks++;
							legalDueDate = sdfIn.parse(object[7].toString());
							Date prdueDate = sdfIn.parse(object[3].toString());
							if (currentDate.after(legalDueDate)) {
								noncomplied++;
								JSONObject taskObj = new JSONObject();
								taskObj.put("date", sdfOut.format(legalDueDate));
								taskObj.put("status", "noncomplied");
								taskObj.put("orga_id", object[9]);
								taskObj.put("orga_name", object[10]);
								taskObj.put("loca_id", object[11]);
								taskObj.put("loca_name", object[12]);
								taskObj.put("dept_id", object[13]);
								taskObj.put("dept_name", object[14]);
								taskObj.put("tsk_impact", object[22]);
								taskObj.put("ttrn_legal_task_status", "Non Complied");
								taskObj.put("ttrn_legal_task_style", "Non-Complied");
								// Task Details
								taskObj.put("ttrn_client_task_id", object[1]);
								taskObj.put("ttrn_legal_due_date",
										sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
								taskObj.put("task_legi_name", object[15]);
								taskObj.put("task_rule_name", object[16]);
								taskObj.put("task_reference", object[17]);
								taskObj.put("task_who", object[18]);
								taskObj.put("task_when", object[19]);
								taskObj.put("task_activity", object[20]);
								taskObj.put("task_executor", object[25].toString() + " " + object[26].toString());
								taskObj.put("task_type", "Main");
								taskObj.put("ttrn_pr_due_date",
										sdfOutDisplay.format(sdfIn.parse(object[3].toString())));
								taskObj.put("ttrn_rw_due_date",
										sdfOutDisplay.format(sdfIn.parse(object[4].toString())));
								taskObj.put("ttrn_fh_due_date",
										sdfOutDisplay.format(sdfIn.parse(object[5].toString())));
								taskObj.put("ttrn_uh_due_date", taskObj.put("task_cat_law", object[28].toString()));
								taskObj.put("ttrn_frequency_for_operation", object[23].toString());
								taskObj.put("ttrn_id", object[0]);
								taskObj.put("task_implication", object[31].toString());
								taskObj.put("task_evaluator", object[32].toString() + " " + object[33].toString());
								taskObj.put("task_fun_head", object[34].toString() + " " + object[35].toString());
								if (object[29] != null)
									taskObj.put("comments", object[29].toString());
								else
									taskObj.put("comments", " ");

								if (object[30] != null)
									taskObj.put("reasonForNonCompliance", object[30].toString());
								else
									taskObj.put("reasonForNonCompliance", " ");

								if (object[0] != null) {
									List<UploadedDocuments> attachedDocuments = uploadedDocumentsDao
											.getAllDocumentByTtrnId(Integer.parseInt(object[0].toString()));
									if (attachedDocuments != null) {
										taskObj.put("document_attached", 1);
										Iterator<UploadedDocuments> itre = attachedDocuments.iterator();
										JSONArray docArray = new JSONArray();
										while (itre.hasNext()) {
											UploadedDocuments uploadedDocuments = (UploadedDocuments) itre.next();
											JSONObject docObj = new JSONObject();
											docObj.put("udoc_id", uploadedDocuments.getUdoc_id());
											docObj.put("udoc_original_file_name",
													uploadedDocuments.getUdoc_original_file_name());
											docArray.add(docObj);
										}
										taskObj.put("document_list", docArray);
									} else {
										taskObj.put("document_list", new JSONArray());
										taskObj.put("document_attached", 0);
									}
								} else {
									taskObj.put("document_list", new JSONArray());
									taskObj.put("document_attached", 0);
								}
								taskObj.put("task_type", "Main");
								tasksList.add(taskObj);
							}
						}

					}
				}
			}

			objForSend.put("Complied", complied);
			objForSend.put("NonComplied", noncomplied);
			objForSend.put("totaltasksinloop", totalTasksInLoop);
			objForSend.put("totalactivetasks", totalActiveTasks);
			objForSend.put("totalcompletedtasks", totalCompletedTasks);
			objForSend.put("taskList", tasksList);
			return objForSend.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("ErrorMessage", "Failed");
			return objForSend.toJSONString();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String copyComplianceOwner(String jsonString, HttpSession session) {
		JSONObject objForAppend = new JSONObject();
		String email_body = "";
		String client_task_id = null;
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int tmap_orga_id = Integer.parseInt(jsonObj.get("orga_id").toString());
			int tmap_loca_id = Integer.parseInt(jsonObj.get("loca_id").toString());
			int tmap_dept_id = Integer.parseInt(jsonObj.get("dept_id").toString());
			int tmap_pr_user_id = Integer.parseInt(jsonObj.get("pr_user_id").toString());
			int tmap_rw_user_id = Integer.parseInt(jsonObj.get("rw_user_id").toString());
			int tmap_fh_user_id = Integer.parseInt(jsonObj.get("fh_user_id").toString());

			Organization org = entityDao.getOrganizationById(tmap_orga_id);
			Department dept = functionDao.getDepartmentById(tmap_dept_id);
			Location loca = unitDao.getLocationById(tmap_loca_id);
			User executor = userDao.getUserById(tmap_pr_user_id);
			User evaluator = userDao.getUserById(tmap_rw_user_id);
			User functionHead = userDao.getUserById(tmap_fh_user_id);

			String user_name = session.getAttribute("sess_user_full_name").toString();
			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			User user = userDao.getUserById(user_id);

			// String operation_to_perform = jsonObj.get("operation_to_perform").toString();
			JSONArray assinged_tasks = (JSONArray) jsonObj.get("tasks_list");
			// System.out.println("These many tasks are re assigned: " +
			// assinged_tasks.size());

			email_body = "<h2 style='font-size:18px;'>Dear Executor,</h2>";
			email_body += "<p style='text-align:justify;width:70%;'>Please note that the compliance owner details for the below task has been changed. </p>"
					+ "<p style='text-align:justify;width:70%;'>Requesting you to execute the task accordingly.</p>";
			email_body += "<table style='width:80%;' border='1'>" + "<thead>"
					+ "<tr style='background:#0B6EC3;color:#fff;'>" + "<th>Client Task ID</th>" + "<th>Entity</th>"
					+ "<th>Unit</th>" + "<th>Function </th>" + "<th>Executor</th>" + "<th>Evaluator</th>"
					+ "<th>Function Head</th>" + "</tr>" + "</thead>" + "<tbody>";
			int SN = 1;

			for (int i = 0; i < assinged_tasks.size(); i++) {

				JSONObject assinged_tasks_obj = (JSONObject) assinged_tasks.get(i);
				TaskUserMapping taskUserMappings = tasksUserMappingDao.getTmapForchangeComplianceOwner(
						Integer.parseInt(assinged_tasks_obj.get("tmap_id").toString()));

				TaskUserMapping taskUserMapping = new TaskUserMapping();

				// client_task_id = taskUserMapping.getTmap_client_tasks_id();
				/*
				 * String previous = "orga_id-" + taskUserMapping.getTmap_orga_id() +
				 * " loca_id-" + taskUserMapping.getTmap_loca_id() + " dept_id-" +
				 * taskUserMapping.getTmap_dept_id() + " performer_id-" +
				 * taskUserMapping.getTmap_pr_user_id() + " reviewer_id-" +
				 * taskUserMapping.getTmap_rw_user_id() + " function_head_id-" +
				 * taskUserMapping.getTmap_fh_user_id();
				 */
				taskUserMapping.setTmap_orga_id(tmap_orga_id);
				taskUserMapping.setTmap_loca_id(tmap_loca_id);
				taskUserMapping.setTmap_dept_id(tmap_dept_id);
				/*
				 * taskUserMapping.setTmap_pr_user_id(tmap_pr_user_id);
				 * taskUserMapping.setTmap_rw_user_id(tmap_rw_user_id);
				 * taskUserMapping.setTmap_fh_user_id(tmap_fh_user_id);
				 */
				if (tmap_pr_user_id == 0)
					tmap_pr_user_id = taskUserMapping.getTmap_pr_user_id();
				else
					taskUserMapping.setTmap_pr_user_id(tmap_pr_user_id);

				if (tmap_rw_user_id == 0)
					tmap_rw_user_id = taskUserMapping.getTmap_rw_user_id();
				else
					taskUserMapping.setTmap_rw_user_id(tmap_rw_user_id);

				if (tmap_fh_user_id == 0)
					tmap_fh_user_id = taskUserMapping.getTmap_fh_user_id();
				else
					taskUserMapping.setTmap_fh_user_id(tmap_fh_user_id);

				int last_generated_id = tasksUserMappingDao.getMaxLastGeneratedValue(tmap_loca_id, tmap_dept_id);

				// System.out.println("last_generated_id : " + last_generated_id);
				int plus = 3;
				int lstNo = last_generated_id + 1; // last_generated_id + plus;
				// System.out.println("lstNo : " + lstNo);
				String tmap_last_generated_value_for_client_task_id = String.format("%07d", (lstNo));
				String tmap_client_id = "D" + String.format("%02d", tmap_loca_id) + String.format("%02d", tmap_dept_id)
						+ tmap_last_generated_value_for_client_task_id;
				// System.out.println("tmap_client_id : " + tmap_client_id);

				taskUserMapping.setTmap_task_id(taskUserMappings.getTmap_task_id());
				taskUserMapping.setTmap_last_generated_value_for_client_task_id(
						Integer.parseInt(tmap_last_generated_value_for_client_task_id)); // + 1
				taskUserMapping.setTmap_client_tasks_id(tmap_client_id);
				taskUserMapping.setTmap_lexcare_task_id(taskUserMappings.getTmap_lexcare_task_id());
				taskUserMapping.setTmap_created_at(new Date());
				taskUserMapping.setTmap_enable_status("1");
				taskUserMapping.setTmap_approval_status("1");
				taskUserMapping.setTmap_added_by(user_id);
				auditTasksDAO.insertNewTaskUserMapping(taskUserMapping);

				String changed = "orga_id-" + taskUserMapping.getTmap_orga_id() + " loca_id-"
						+ taskUserMapping.getTmap_loca_id() + " dept_id-" + taskUserMapping.getTmap_dept_id()
						+ " performer_id-" + taskUserMapping.getTmap_pr_user_id() + " reviewer_id-"
						+ taskUserMapping.getTmap_rw_user_id() + " function_head_id-"
						+ taskUserMapping.getTmap_fh_user_id();

				/*
				 * utilitiesService.addChangeComplianceOwnerLog(
				 * Integer.parseInt(assinged_tasks_obj.get("tmap_id").toString()), previous,
				 * changed, user_id, user_name);
				 */
				// System.out.println("Tasks I : " +
				// assinged_tasks_obj.get("tmap_client_tasks_id") != null ?
				// assinged_tasks_obj.get("tmap_client_tasks_id").toString() : "0");
				// TaskTransactional clientTaskDetailById =
				// auditTasksDAO.getClientTaskDetailById("0");

				// TaskTransactional tsk = new TaskTransactional();
				// tsk.setTtrn_created_at(new Date());
				// tsk.setTtrn_activation_date(new Date());
				// tsk.setTtrn_added_by(user_id);
				// tsk.setTtrn_alert_days(0);
				// tsk.setTtrn_allow_approver_reopening("0");
				// tsk.setTtrn_allow_back_date_completion("0");
				// tsk.setTtrn_client_task_id(tmap_client_id);
				// tsk.setTtrn_document("0");
				// tsk.setTtrn_fh_due_date(clientTaskDetailById.getTtrn_fh_due_date());
				// tsk.setTtrn_first_alert(clientTaskDetailById.getTtrn_first_alert());
				// tsk.setTtrn_frequency_for_alerts(clientTaskDetailById.getTtrn_frequency_for_alerts());
				// tsk.setTtrn_frequency_for_operation(clientTaskDetailById.getTtrn_frequency_for_operation());
				// tsk.setTtrn_historical("0");
				// tsk.setTtrn_impact(clientTaskDetailById.getTtrn_impact());
				// tsk.setTtrn_impact_on_organization(clientTaskDetailById.getTtrn_impact_on_organization());
				// tsk.setTtrn_impact_on_unit(clientTaskDetailById.getTtrn_impact_on_unit());
				// tsk.setTtrn_legal_due_date(clientTaskDetailById.getTtrn_legal_due_date());
				// tsk.setTtrn_no_of_back_days_allowed(clientTaskDetailById.getTtrn_no_of_back_days_allowed());
				// tsk.setTtrn_performer_comments(clientTaskDetailById.getTtrn_performer_comments());
				// tsk.setAuditor_performer_by_id(clientTaskDetailById.getAuditor_performer_by_id());
				// tsk.setTtrn_pr_due_date(clientTaskDetailById.getTtrn_pr_due_date());
				// tsk.setTtrn_prior_days_buffer(clientTaskDetailById.getTtrn_prior_days_buffer());
				// tsk.setTtrn_reason_for_non_compliance(clientTaskDetailById.getTtrn_reason_for_non_compliance());
				// tsk.setTtrn_rw_due_date(clientTaskDetailById.getTtrn_rw_due_date());
				// tsk.setTtrn_second_alert(clientTaskDetailById.getTtrn_second_alert());
				// tsk.setTtrn_status(clientTaskDetailById.getTtrn_status());
				// tsk.setTtrn_submitted_date(clientTaskDetailById.getTtrn_submitted_date());
				// tsk.setTtrn_task_approved_by(clientTaskDetailById.getTtrn_task_approved_by());
				// tsk.setTtrn_task_approved_date(clientTaskDetailById.getTtrn_task_approved_date());
				// tsk.setTtrn_task_completed_by(clientTaskDetailById.getTtrn_task_completed_by());
				// tsk.setTtrn_third_alert(clientTaskDetailById.getTtrn_third_alert());
				// tsk.setTtrn_uh_due_date(clientTaskDetailById.getTtrn_uh_due_date());
				// auditTasksDAO.copyCompliance(tsk);

				/*-----Changing performer user id if task is not completed and legal due date has not crossed--------------------------*/
				List<Object> allTasks = tasksconfigurationdao.getLatestTtrnForChangeComplianceOwner(
						Integer.parseInt(assinged_tasks_obj.get("tmap_id").toString()));
				System.out
						.println("This is tmap id: " + Integer.parseInt(assinged_tasks_obj.get("tmap_id").toString()));
				if (allTasks.size() > 0) {
					Iterator<Object> iterator = allTasks.iterator();
					while (iterator.hasNext()) {
						Object[] taskTransactional = (Object[]) iterator.next();
						// System.out.println(taskTransactional[0]); // ttrn_id
						// System.out.println(taskTransactional[1]); // ttrn_legal_due_date
						String lglDueDate = taskTransactional[1].toString();
						Integer ttrnId = Integer.parseInt(taskTransactional[0].toString());

						SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd");
						Date curDate = new Date();

						Date legalDueDate = sDate.parse(lglDueDate);
						String formatCurDate = sDate.format(curDate);
						Date currentDate = sDate.parse(formatCurDate);
						if (legalDueDate.after(currentDate) || legalDueDate.equals(currentDate)) {
							// System.out.println("In change pr_user_id for transactional");

							List<Object> tasksForCompletionNativeQuery = tasksconfigurationdao
									.getTasksForCompletionNativeQuery(ttrnId);
							TaskTransactional taskTransactional1 = new TaskTransactional();
							if (tasksForCompletionNativeQuery.size() > 0) {
								Iterator iterator2 = tasksForCompletionNativeQuery.iterator();
								while (iterator2.hasNext()) {
									Object nextTtrnStatus = iterator2.next();
									// System.out.println("Inactive : " + nextTtrnStatus.equals("Inactive"));
									// System.out.println("Active : " + nextTtrnStatus.equals("Active"));
									// TaskTransactional taskTransactional1 =
									// tasksconfigurationdao.getTasksForCompletion(ttrnId);
									// if (nextTtrnStatus[0].equals("Active") ||
									// nextTtrnStatus[0].equals("Inactive")) {
									if (nextTtrnStatus.equals("Active") || nextTtrnStatus.equals("Inactive")) {
										taskTransactional1.setTtrn_performer_user_id(tmap_pr_user_id);
										// tasksconfigurationdao.updateTaskConfiguration(taskTransactional1, ttrnId);
										tasksconfigurationdao.updateTaskConfigurationNativeQuery(taskTransactional1,
												ttrnId);
									}
								}
							}

						}
						// System.out.println("parseLglDate : " + legalDueDate);
						// System.out.println("parseCurrentDate : " + currentDate);
					}
				}

				/*-----Changing performer user id if task is not completed and legal due date has not crossed ends here---------------*/
				email_body += "<tr>" + "<td>" + taskUserMapping.getTmap_client_tasks_id() + "</td>" + "<td>"
						+ org.getOrga_name() + "</td>" + "<td>" + loca.getLoca_name() + "</td>" + "<td>"
						+ dept.getDept_name() + "</td>" + "<td>" + executor.getUser_first_name() + " "
						+ executor.getUser_last_name() + "</td>" + "<td>" + evaluator.getUser_first_name() + " "
						+ evaluator.getUser_last_name() + "</td>" + "<td>" + functionHead.getUser_first_name() + " "
						+ functionHead.getUser_last_name() + "<td>" + "</tr>";

				SN++;
			}
			email_body += "</tbody>" + "</table>";
			email_body += "<p>This is a system generated mail. Please do not reply to this mail.<br/>"
					+ "In case of any doubt or difficulty, please call Helpdesk as per details given on the support page."
					+ "</p>" + "<h2 style='font-size:18px;font-weight:bold;'>Yours Sincerely</h2>"
					+ "<h2 style='font-size:19px;font-weight:bold;'>Team LexCare</h2>" + "</div>";

			try {
				/*----------------------Code to send mail Start here---------------*/
				Properties props = new Properties();
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.host", hostName);
				props.put("mail.smtp.port", portNo);
				Session session1 = Session.getInstance(props, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

				try {

					Message message = new MimeMessage(session1);
					message.setFrom(new InternetAddress(mailFrom));
					// message.setRecipients(Message.RecipientType.TO,address);
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(executor.getUser_email()));

					String address = evaluator.getUser_email() + "," + functionHead.getUser_email();
					InternetAddress[] iAdressArray = InternetAddress.parse(address);
					message.setRecipients(Message.RecipientType.CC, iAdressArray);
					message.setSubject("Change Compliance Owner Alert");

					message.setContent(email_body, "text/html; charset=utf-8");
					Transport.send(message);
					utilitiesService.addMailToLog(executor.getUser_email(), "Change Compliance Owner Alert",
							client_task_id, address); // Mail
					// log
				}

				catch (Exception e) {
					e.printStackTrace();
					// System.out.println("Error in transport send");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			objForAppend.put("responseMessage", "Success");
			return objForAppend.toJSONString();
		} catch (Exception e) {
			objForAppend.put("responseMessage", "Failed");
			e.printStackTrace();
			return objForAppend.toJSONString();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public String submitSimplyCompDocumentsURL(ArrayList<MultipartFile> ttrn_proof_of_compliance, String jsonString,
			HttpSession session) {
		JSONObject objForSend = new JSONObject();
		try {

			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String docName = jsonObj.get("docName").toString();
			String docDescription = jsonObj.get("docDescription").toString();
			String fromDate = jsonObj.get("date_from") != null ? jsonObj.get("date_from").toString() : null;
			String toDate = jsonObj.get("date_to") != null ? jsonObj.get("date_to").toString() : null;
			Integer tskState = Integer.parseInt(jsonObj.get("tskState").toString());

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date docReportFromDate = null;
			if (fromDate == null) {
				docReportFromDate = null;
			} else {
				docReportFromDate = sdf.parse(fromDate);
			}

			Date docReportToDate = null;
			if (toDate == null) {
				docReportToDate = null;
			} else {
				docReportToDate = sdf.parse(toDate);
			}
			String originalFileName = null;
			String generatedFileName = null;

			Date cur_date = new Date();

			Calendar cal = Calendar.getInstance();
			cal.setTime(cur_date);
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			int day = cal.get(Calendar.DAY_OF_MONTH);
			String dir_month_name = day + "-" + (month + 1) + "-" + year;

			Random rand = new Random();
			String randId = String.format("%04d", rand.nextInt(10000));

			for (int i1 = 0; i1 < ttrn_proof_of_compliance.size(); i1++) {
				MultipartFile file1 = ttrn_proof_of_compliance.get(i1);

				if (file1.getSize() > 0) {

					File dir = new File("C:" + File.separator + "CMS" + File.separator + "documents" + File.separator
							+ "proofOfCompliance" + File.separator + projectName + File.separator + dir_month_name);
					if (!dir.exists())
						dir.mkdirs();
					int lastGeneratedValue = 0;
					lastGeneratedValue++;
					originalFileName = file1.getOriginalFilename().split("\\.")[1];
					// System.out.println("originalFileName : " + originalFileName);
					generatedFileName = randId + file1.getOriginalFilename();
					File newFile = new File(dir.getPath() + File.separator + generatedFileName);
					if (!newFile.exists()) {
						newFile.createNewFile();
					}

					OutputStream outputStream = new FileOutputStream(newFile);

					outputStream.write(file1.getBytes());

					String algo = "DES/ECB/PKCS5Padding";
					utilitiesService.encrypt(algo, newFile.getPath());

					/*
					 * UploadedDocuments documents = new UploadedDocuments();
					 * documents.setUdoc_ttrn_id(ttrn_id);
					 * documents.setUdoc_original_file_name(originalFileName);
					 * documents.setUdoc_filename(dir + File.separator + generatedFileName);
					 * documents.setUdoc_last_generated_value_for_filename_for_ttrn_id(
					 * lastGeneratedValue); uploadedDocumentsDao.saveDocuments(documents);
					 */

					UploadedPODDocuments documents = new UploadedPODDocuments();
					documents.setIsDownload(0);
					documents.setDocDescription(docDescription);
					documents.setDocName(docName);
					documents.setDocOriginalName(randId + file1.getOriginalFilename());
					documents.setDocPath(dir + File.separator + generatedFileName);
					documents.setAddedBy(user_id);
					documents.setIsDeleted(0);
					documents.setDocReportFromDate(docReportFromDate);
					documents.setDocReportToDate(docReportToDate);
					documents.setStateId(tskState);
					auditTasksDAO.savePODDocuments(documents);

					outputStream.flush();
					outputStream.close();

					Path path = Paths.get(dir + File.separator + generatedFileName);
					try {
						Files.delete(path);
					} catch (IOException e) {
						// deleting file failed
						e.printStackTrace();
						// System.out.println(e.getMessage());
					}

				} else {
					objForSend.put("responseMessage", "Invalid File Type");
					return objForSend.toJSONString();
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

	@SuppressWarnings("unchecked")
	@Override
	public String listSimplyCompDocuments(String jsonString, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		JSONArray dataForAppend = new JSONArray();
		try {
			List<Object> listOfSimplyCompDocuments = auditTasksDAO.getListOfSimplyCompDocuments(jsonString, session);
			if (listOfSimplyCompDocuments.size() > 0 && listOfSimplyCompDocuments != null) {
				Iterator<Object> iterator = listOfSimplyCompDocuments.iterator();
				while (iterator.hasNext()) {
					Object next[] = (Object[]) iterator.next();
					JSONObject objForAppend = new JSONObject();
					objForAppend.put("ID", next[0].toString());
					objForAppend.put("ADDED_BY", next[1].toString());
					objForAppend.put("DOC_DESCRIPTION", next[2].toString());
					objForAppend.put("DOC_NAME", next[3].toString());
					objForAppend.put("DOC_ORIGINAL_NAME", next[4].toString());
					objForAppend.put("DOC_PATH", next[5].toString());
					objForAppend.put("REPORT_FROM_DATE", next[6] != null ? next[6].toString() : "NA");
					objForAppend.put("REPORT_TO_DATE", next[7] != null ? next[7].toString() : "NA");
					objForAppend.put("IS_DELETED", next[8].toString());
					objForAppend.put("STATE_ID", next[9] != null ? next[9].toString() : "NA");
					objForAppend.put("STATE_NAME", next[10] != null ? next[10].toString() : "NA");
					dataForAppend.add(objForAppend);
				}
			}
			dataForSend.put("docrepos", dataForAppend);
			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonString;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String downloadComplianceDocument(String jsonString, HttpServletResponse response) {

		JSONObject objForSend = new JSONObject();
		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);
			// System.out.println("udoc_id :" + jsonObject.get("udoc_id").toString());
			int udoc_id = Integer.parseInt(jsonObject.get("udoc_id").toString());
			if (auditTasksDAO.getProofFilePath(udoc_id) != null) {
				File file = new File(auditTasksDAO.getProofFilePath(udoc_id));

				String algo = "DES/ECB/PKCS5Padding";
				File decFile = new File(utilitiesService.decrypt(algo, file.getPath() + ".enc"));
				// System.out.println("return :"+utilitiesService.decrypt(algo,
				// file.getPath()+".enc"));

				InputStream is = new FileInputStream(decFile);
				// System.out.println("deccFile :"
				// +decFile.getPath().substring(0,decFile.getPath().lastIndexOf(".")));

				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", "attachment; filename=\"" + decFile.getName() + "\"");

				OutputStream os = response.getOutputStream();
				byte[] buffer = new byte[1024];
				int len;
				while ((len = is.read(buffer)) != -1) {
					os.write(buffer, 0, len);
				}

				os.flush();
				os.close();
				is.close();
				Path path = Paths.get(decFile.getPath());
				try {
					Files.delete(path);
				} catch (IOException e) {
					// deleting file failed
					e.printStackTrace();
					// System.out.println(e.getMessage());
				}
			} else {

			}
			objForSend.put("responeMessage", "Success");
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("responeMessage", "Failed");
			return objForSend.toJSONString();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public String makeNonCompliedTasks(String jsonString, HttpSession session) {
		try {

			JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);
			String apprvrComments = jsonObject.get("reopen_comment") != null
					? jsonObject.get("reopen_comment").toString()
					: "NA";
			int ttrn_id = Integer.parseInt(jsonObject.get("ttrn_id").toString());
			int roleId = Integer.parseInt(jsonObject.get("role_id").toString());

			Integer sesRoleId = Integer.parseInt(session.getAttribute("sess_role_id").toString());
			System.out.println("sesRoleId : " + sesRoleId);

			String auditor_performer_by_id = session.getAttribute("sess_user_id").toString();

			// System.out.println("udoc_id :" +
			// jsonObject.get("reopen_comment").toString());
			TaskTransactional taskTransactional = tasksconfigurationdao.getTasksForCompletion(ttrn_id);
//			taskTransactional.setTtrn_task_completed_by(0);
//			taskTransactional.setTtrn_completed_date(null);
//			taskTransactional.setTtrn_submitted_date(null);
			taskTransactional.setTtrn_status("Active");
//			taskTransactional.setTtrn_performer_comments(null);
//			taskTransactional.setTtrn_reason_for_non_compliance(null);
			taskTransactional.setTtrn_tasks_status("NonComplied");

			if (sesRoleId >= 6 || roleId >= 6) {
				System.out.println("inside if sesRoleId >= 6 || roleId >= 6 ");
				taskTransactional.setTtrn_tasks_status("NonComplied");
				taskTransactional.setAuditorStatus("NonComplied");
				taskTransactional.setAuditorAuditTime(new Date());
				taskTransactional.setAuditoComments(apprvrComments);
				taskTransactional.setAuditor_performer_by_id(auditor_performer_by_id);
				taskTransactional.setIsDocumentUpload(1);
				taskTransactional.setIsAuditTasks(1);
				taskTransactional.setTtrn_is_Task_Audited("Yes");
			}

			auditTasksDAO.makeNonCompliedTasks(taskTransactional);
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("responseMessage", "Success");
			return objForAppend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("responseMessage", "Failed");
			return objForAppend.toJSONString();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String verticalWiceReport(String jsonString, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		JSONArray arr = new JSONArray();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String fromDate = null;
			String toDate = null;
			if (jsonObj.get("fromDate") != null && jsonObj.get("toDate") != null) {
				fromDate = jsonObj.get("fromDate").toString();
				toDate = jsonObj.get("toDate").toString();
			}

			List<Object> verticalWiceReport = auditTasksDAO.verticalWiceReport(fromDate, toDate, session);
			if (verticalWiceReport.size() > 0 && verticalWiceReport != null) {
				Iterator<Object> iterator = verticalWiceReport.iterator();
				while (iterator.hasNext()) {
					JSONObject obj = new JSONObject();
					Object next[] = (Object[]) iterator.next();
					obj.put("orgaName", next[0].toString());
					obj.put("NonComplied", next[1].toString());
					obj.put("PosingRisk", next[2].toString());
					obj.put("Complied", next[3].toString());
					obj.put("Delayed", next[4].toString());
					obj.put("WaitingForApproval", next[5].toString());
					obj.put("ReOpened", next[6].toString());
					obj.put("DelayedReported", next[7].toString());
					arr.add(obj);
				}
			}

			dataForSend.put("statusReport", arr);
			return dataForSend.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String statusCompletionReport(String jsonString, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		JSONArray arr = new JSONArray();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String fromDate = null;
			String toDate = null;
			if (jsonObj.get("fromDate") != null && jsonObj.get("toDate") != null) {
				fromDate = jsonObj.get("fromDate").toString();
				toDate = jsonObj.get("toDate").toString();
			}
			List<Object> statusCompletionReport = auditTasksDAO.statusCompletionReport(fromDate, toDate, session);
			if (statusCompletionReport.size() > 0) {
				System.out.println("statusCompletionReport size : " + statusCompletionReport.size());
				Iterator<Object> iterator = statusCompletionReport.iterator();
				while (iterator.hasNext()) {
					JSONObject obj = new JSONObject();
					Object next[] = (Object[]) iterator.next();
					obj.put("vertical", next[0].toString());
					obj.put("unitName", next[1].toString());
					obj.put("function", next[2].toString());
					obj.put("executor", next[3].toString());
					obj.put("email", next[4].toString());
					obj.put("mobNo", next[5].toString());
					obj.put("docUploaded", next[6].toString());
					obj.put("docPending", next[7].toString());
					arr.add(obj);
				}
			}
			dataForSend.put("taskList", arr);
			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("responseMessage", "Failed");
			return objForAppend.toJSONString();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String searchExecutorList(String jsonString, HttpSession session) {
		JSONArray arr = new JSONArray();
		JSONObject dataForSend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);

			String orga_id = "0";
			String loca_id = "0";
			String dept_id = "0";

			if (jsonObj.get("orga_id") != null) {
				orga_id = jsonObj.get("orga_id").toString();
			} else {
				orga_id = "0";
			}

			if (jsonObj.get("loca_id") != null) {
				loca_id = jsonObj.get("loca_id").toString();
			} else {
				loca_id = "0";
			}

			if (jsonObj.get("dept_id") != null) {
				dept_id = jsonObj.get("dept_id").toString();
			} else {
				dept_id = "0";
			}

			List<Object> searchExecutorList = auditTasksDAO.searchExecutorList(session, orga_id, loca_id, dept_id);
			if (searchExecutorList.size() > 0) {
				Iterator<Object> iterator = searchExecutorList.iterator();
				while (iterator.hasNext()) {
					JSONObject obj = new JSONObject();
					Object next[] = (Object[]) iterator.next();
					obj.put("vertical", next[0].toString());
					obj.put("unitName", next[1].toString());
					obj.put("function", next[2].toString());
					obj.put("executor", next[3].toString());
					obj.put("email", next[4].toString());
					obj.put("mobNo", next[5].toString());
					arr.add(obj);
				}
			}
			dataForSend.put("executorLists", arr);
			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			dataForSend.put("responseMessage", "Failed");
			e.printStackTrace();
			return dataForSend.toJSONString();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String monthlyComplianceAuditChartURL(String jsonString, HttpSession session) {

		// System.out.println("inside getOverallComplianceGraph () ");
		JSONObject objForSend = new JSONObject();
		JSONArray tasksList = new JSONArray();
		int totalTasksInLoop = 0;
		int totalActiveTasks = 0;
		int totalCompletedTasks = 0;
		int complied = 0;
		int noncomplied = 0;
		int posingrisk = 0;
		int delayed = 0;
		int pending = 0;
		int partially_Completed = 0;
		int re_opened = 0;
		int delayed_reported = 0;

		try {
			List<Object> allTask = auditTasksDAO.searchMonthlyComplianceAuditChartURL(
					Integer.parseInt(session.getAttribute("sess_user_id").toString()),
					Integer.parseInt(session.getAttribute("sess_role_id").toString()), jsonString);

			if (allTask.size() > 0 || allTask != null) {
				Iterator<Object> itr = allTask.iterator();

				while (itr.hasNext()) {
					Object[] object = (Object[]) itr.next();
					JSONObject taskObj = new JSONObject();
					taskObj.put("date", object[8] != null ? object[8].toString() : "NA");

					if (object[40].toString().equalsIgnoreCase("Complied")) {
						complied++;
						taskObj.put("status", "complied");
					} else if (object[40].toString().equalsIgnoreCase("NonComplied")) {
						noncomplied++;
						taskObj.put("status", "noncomplied");
					} else {
						re_opened++;
						taskObj.put("status", "reopen");
					}

					// taskObj.put("status", object[40].toString());
					taskObj.put("orga_id", object[9]);
					taskObj.put("orga_name", object[10]);
					taskObj.put("loca_id", object[11]);
					taskObj.put("loca_name", object[12]);
					taskObj.put("dept_id", object[13]);
					taskObj.put("dept_name", object[14]);
					taskObj.put("tsk_impact", object[22]);
					taskObj.put("ttrn_client_task_id", object[1]);
					taskObj.put("ttrn_legal_due_date", sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
					taskObj.put("task_legi_name", object[15]);
					taskObj.put("task_rule_name", object[16]);
					taskObj.put("task_reference", object[17]);
					taskObj.put("task_who", object[18]);
					taskObj.put("task_when", object[19]);
					taskObj.put("task_activity", object[20]);
					taskObj.put("task_executor", object[25].toString() + " " + object[26].toString());
					taskObj.put("ttrn_pr_due_date", sdfOutDisplay.format(sdfIn.parse(object[3].toString())));
					taskObj.put("ttrn_rw_due_date", sdfOutDisplay.format(sdfIn.parse(object[4].toString())));
					taskObj.put("ttrn_fh_due_date", sdfOutDisplay.format(sdfIn.parse(object[5].toString())));
					taskObj.put("ttrn_uh_due_date", sdfOutDisplay.format(sdfIn.parse(object[6].toString())));
					taskObj.put("task_cat_law", object[28].toString());
					taskObj.put("ttrn_frequency_for_operation", object[23].toString());
					taskObj.put("ttrn_id", object[0]);
					taskObj.put("task_implication", object[31].toString());
					taskObj.put("task_evaluator", object[32].toString() + " " + object[33].toString());
					taskObj.put("task_fun_head", object[34].toString() + " " + object[35].toString());
					taskObj.put("auditDate", object[36] != null ? object[36].toString() : "NA");
					taskObj.put("reOpenDateWindow", object[37] != null ? object[37].toString() : "NA");

					taskObj.put("auditorComments", object[38] != null ? object[38].toString() : "NA");
					taskObj.put("auditorAuditTime", object[39] != null ? object[39].toString() : "NA");
					taskObj.put("auditorStatus", object[40] != null ? object[40].toString() : "NA");
					taskObj.put("auditor_performer_by_id", object[41] != null ? object[41].toString() : "NA");
					taskObj.put("isAuditTasks", object[42] != null ? object[42].toString() : "NA");
					taskObj.put("isDocumentUpload", object[43] != null ? object[43].toString() : "NA");
					taskObj.put("ttrn_tasks_status", object[44] != null ? object[44].toString() : "NA");

					if (object[29] != null) {
						taskObj.put("comments", object[29].toString());
					} else {
						taskObj.put("comments", " ");
					}
					if (object[30] != null) {
						taskObj.put("reasonForNonCompliance", object[30].toString());
					} else {
						taskObj.put("reasonForNonCompliance", " ");
					}

					// if (object[0] != null) {
					// List<UploadedDocuments> attachedDocuments = uploadedDocumentsDao
					// .getAllDocumentByTtrnId(Integer.parseInt(object[0].toString()));
					//
					// if (attachedDocuments != null) {
					// taskObj.put("document_attached", 1);
					//
					// Iterator<UploadedDocuments> itre = attachedDocuments.iterator();
					// JSONArray docArray = new JSONArray();
					//
					// while (itre.hasNext()) {
					// UploadedDocuments uploadedDocuments = (UploadedDocuments) itre.next();
					// JSONObject docObj = new JSONObject();
					// docObj.put("udoc_id", uploadedDocuments.getUdoc_id());
					// docObj.put("udoc_original_file_name",
					// uploadedDocuments.getUdoc_original_file_name());
					// docArray.add(docObj);
					// }
					// taskObj.put("document_list", docArray);
					// } else {
					// taskObj.put("document_list", new JSONArray());
					// taskObj.put("document_attached", 0);
					// }
					//
					// } else {
					// taskObj.put("document_list", new JSONArray());
					// taskObj.put("document_attached", 0);
					// }

					taskObj.put("task_type", "Main");

					tasksList.add(taskObj);
				}

			} // if end

			objForSend.put("Complied", complied);
			objForSend.put("NonComplied", noncomplied);
			// objForSend.put("PosingRisk", posingrisk);
			// objForSend.put("Delayed", delayed);
			objForSend.put("totaltasksinloop", totalTasksInLoop);
			objForSend.put("totalactivetasks", totalActiveTasks);
			objForSend.put("totalcompletedtasks", totalCompletedTasks);
			objForSend.put("Pending", pending);
			// objForSend.put("WaitingForApproval", partially_Completed);
			objForSend.put("ReOpened", re_opened);
			// objForSend.put("delayed_reported", delayed_reported);
			objForSend.put("taskList", tasksList);
			return objForSend.toJSONString();
		} catch (

		Exception e) {
			e.printStackTrace();
			objForSend.put("ErrorMessage", "Failed");
			return objForSend.toJSONString();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public String searchAuditDashboard(String jsonString, HttpSession session) {
		JSONObject objForSend = new JSONObject();
		JSONArray tasksList = new JSONArray();
		int totalTasksInLoop = 0;
		int totalActiveTasks = 0;
		int totalCompletedTasks = 0;
		int complied = 0;
		int noncomplied = 0;
		// int posingrisk = 0;
		// int delayed = 0;
		int pending = 0;
		// int partially_Completed = 0;
		int re_opened = 0;
		// int delayed_reported = 0;
		try {
			List<Object> allTask = auditTasksDAO.searchAuditDashboard(
					Integer.parseInt(session.getAttribute("sess_user_id").toString()),
					Integer.parseInt(session.getAttribute("sess_role_id").toString()), jsonString);

			if (allTask != null) {

				Iterator<Object> itr = allTask.iterator();
				objForSend.put("totalTasks", allTask.size());
				Date currentDate = sdfIn.parse(sdfOut.format(new Date()));
				Date CompletedDate = null;
				while (itr.hasNext()) {
					Object[] object = (Object[]) itr.next();

					Date submittedDate = null;
					Date legalDueDate = sdfIn.parse(object[7].toString());
					if (object[8] != null) {
						submittedDate = sdfIn.parse(object[8].toString());
					}

					if (object[27] != null) {
						CompletedDate = sdfIn.parse(object[27].toString());
					}

					if (object[40].toString().equalsIgnoreCase("Complied")) {
						complied++;
						JSONObject taskObj = new JSONObject();
						taskObj.put("date", sdfOut.format(submittedDate));
						taskObj.put("status", "complied");
						taskObj.put("orga_id", object[9]);
						taskObj.put("orga_name", object[10]);
						taskObj.put("loca_id", object[11]);
						taskObj.put("loca_name", object[12]);
						taskObj.put("dept_id", object[13]);
						taskObj.put("dept_name", object[14]);
						taskObj.put("tsk_impact", object[22]);
						// Task Details
						taskObj.put("ttrn_client_task_id", object[1]);
						taskObj.put("ttrn_legal_due_date", sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
						taskObj.put("task_legi_name", object[15]);
						taskObj.put("task_rule_name", object[16]);
						taskObj.put("task_reference", object[17]);
						taskObj.put("task_who", object[18]);
						taskObj.put("task_when", object[19]);
						taskObj.put("task_activity", object[20]);
						taskObj.put("task_executor", object[25].toString() + " " + object[26].toString());
						taskObj.put("ttrn_pr_due_date", sdfOutDisplay.format(sdfIn.parse(object[3].toString())));
						taskObj.put("ttrn_rw_due_date", sdfOutDisplay.format(sdfIn.parse(object[4].toString())));
						taskObj.put("ttrn_fh_due_date", sdfOutDisplay.format(sdfIn.parse(object[5].toString())));
						taskObj.put("ttrn_uh_due_date", sdfOutDisplay.format(sdfIn.parse(object[6].toString())));
						taskObj.put("task_cat_law", object[28].toString());
						taskObj.put("ttrn_frequency_for_operation", object[23].toString());
						taskObj.put("ttrn_id", object[0]);
						taskObj.put("task_implication", object[29].toString());
						taskObj.put("task_evaluator", object[30].toString() + " " + object[31].toString());
						taskObj.put("task_fun_head", object[32].toString() + " " + object[33].toString());

						taskObj.put("auditoComments", object[38].toString());
						if (object[35] != null) {
							taskObj.put("auditorAuditTime", object[39].toString());
						}
						taskObj.put("auditorStatus", object[40].toString());
						taskObj.put("auditDate", object[36] != null ? object[36].toString() : "NA");
						taskObj.put("reOpenDateWindow", object[37] != null ? object[37].toString() : "NA");

						/*
						 * if (object[0] != null) { List<UploadedDocuments> attachedDocuments =
						 * uploadedDocumentsDao
						 * .getAllDocumentByTtrnId(Integer.parseInt(object[0].toString()));
						 * 
						 * if (attachedDocuments != null) { taskObj.put("document_attached", 1);
						 * 
						 * Iterator<UploadedDocuments> itre = attachedDocuments.iterator(); JSONArray
						 * docArray = new JSONArray();
						 * 
						 * while (itre.hasNext()) { UploadedDocuments uploadedDocuments =
						 * (UploadedDocuments) itre.next(); JSONObject docObj = new JSONObject();
						 * docObj.put("udoc_id", uploadedDocuments.getUdoc_id());
						 * docObj.put("udoc_original_file_name",
						 * uploadedDocuments.getUdoc_original_file_name()); docArray.add(docObj); }
						 * taskObj.put("document_list", docArray); } else { taskObj.put("document_list",
						 * new JSONArray()); taskObj.put("document_attached", 0); }
						 * 
						 * } else { taskObj.put("document_list", new JSONArray());
						 * taskObj.put("document_attached", 0); }
						 * 
						 */

						taskObj.put("task_type", "Main");

						tasksList.add(taskObj);
					} // Complied tasks if end
					else if (object[40].toString().equalsIgnoreCase("NonComplied")) {

						noncomplied++;

						// System.out.println("inside noncomplied if ");

						JSONObject taskObj = new JSONObject();
						taskObj.put("date", sdfOut.format(legalDueDate));
						taskObj.put("status", "noncomplied");
						taskObj.put("orga_id", object[9]);
						taskObj.put("orga_name", object[10]);
						taskObj.put("loca_id", object[11]);
						taskObj.put("loca_name", object[12]);
						taskObj.put("dept_id", object[13]);
						taskObj.put("dept_name", object[14]);
						taskObj.put("tsk_impact", object[22]);
						// Task Details
						taskObj.put("ttrn_client_task_id", object[1]);
						taskObj.put("ttrn_legal_due_date", sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
						taskObj.put("task_legi_name", object[15]);
						taskObj.put("task_rule_name", object[16]);
						taskObj.put("task_reference", object[17]);
						taskObj.put("task_who", object[18]);
						taskObj.put("task_when", object[19]);
						taskObj.put("task_activity", object[20]);
						taskObj.put("task_executor", object[25].toString() + " " + object[26].toString());
						taskObj.put("task_type", "Main");
						taskObj.put("ttrn_pr_due_date", sdfOutDisplay.format(sdfIn.parse(object[3].toString())));
						taskObj.put("ttrn_rw_due_date", sdfOutDisplay.format(sdfIn.parse(object[4].toString())));
						taskObj.put("ttrn_fh_due_date", sdfOutDisplay.format(sdfIn.parse(object[5].toString())));
						taskObj.put("ttrn_uh_due_date", taskObj.put("task_cat_law", object[28].toString()));
						taskObj.put("ttrn_frequency_for_operation", object[23].toString());
						taskObj.put("ttrn_id", object[0]);
						taskObj.put("task_implication", object[29].toString());
						taskObj.put("task_evaluator", object[30].toString() + " " + object[31].toString());
						taskObj.put("task_fun_head", object[32].toString() + " " + object[33].toString());

						taskObj.put("auditDate", object[36] != null ? object[36].toString() : "NA");
						taskObj.put("reOpenDateWindow", object[37] != null ? object[37].toString() : "NA");
						taskObj.put("auditorComments", object[38] != null ? object[38].toString() : "NA");
						taskObj.put("auditorAuditTime", object[39] != null ? object[39].toString() : "NA");
						taskObj.put("auditorStatus", object[40] != null ? object[40].toString() : "NA");
						taskObj.put("auditor_performer_by_id", object[41] != null ? object[41].toString() : "NA");
						taskObj.put("isAuditTasks", object[42] != null ? object[42].toString() : "NA");
						taskObj.put("isDocumentUpload", object[43] != null ? object[43].toString() : "NA");
						taskObj.put("ttrn_tasks_status", object[44] != null ? object[44].toString() : "NA");

						/**
						 * if (object[0] != null) { List<UploadedDocuments> attachedDocuments =
						 * uploadedDocumentsDao
						 * .getAllDocumentByTtrnId(Integer.parseInt(object[0].toString()));
						 * 
						 * if (attachedDocuments != null) { taskObj.put("document_attached", 1);
						 * 
						 * Iterator<UploadedDocuments> itre = attachedDocuments.iterator(); JSONArray
						 * docArray = new JSONArray();
						 * 
						 * while (itre.hasNext()) { UploadedDocuments uploadedDocuments =
						 * (UploadedDocuments) itre.next(); JSONObject docObj = new JSONObject();
						 * docObj.put("udoc_id", uploadedDocuments.getUdoc_id());
						 * docObj.put("udoc_original_file_name",
						 * uploadedDocuments.getUdoc_original_file_name()); docArray.add(docObj); }
						 * taskObj.put("document_list", docArray); } else { taskObj.put("document_list",
						 * new JSONArray()); taskObj.put("document_attached", 0); }
						 * 
						 * } else { taskObj.put("document_list", new JSONArray());
						 * taskObj.put("document_attached", 0); }
						 */

						tasksList.add(taskObj);
					} // non-complied if else end
					else {

						// System.out.println("inside reopen if ");
						re_opened++;

						JSONObject taskObj = new JSONObject();
						taskObj.put("date", sdfOut.format(legalDueDate));
						taskObj.put("status", "reopen");
						taskObj.put("orga_id", object[9]);
						taskObj.put("orga_name", object[10]);
						taskObj.put("loca_id", object[11]);
						taskObj.put("loca_name", object[12]);
						taskObj.put("dept_id", object[13]);
						taskObj.put("dept_name", object[14]);
						taskObj.put("tsk_impact", object[22]);
						// Task Details
						taskObj.put("ttrn_client_task_id", object[1]);
						taskObj.put("ttrn_legal_due_date", sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
						taskObj.put("task_legi_name", object[15]);
						taskObj.put("task_rule_name", object[16]);
						taskObj.put("task_reference", object[17]);
						taskObj.put("task_who", object[18]);
						taskObj.put("task_when", object[19]);
						taskObj.put("task_activity", object[20]);
						taskObj.put("task_executor", object[25].toString() + " " + object[26].toString());
						taskObj.put("task_type", "Main");
						taskObj.put("ttrn_pr_due_date", sdfOutDisplay.format(sdfIn.parse(object[3].toString())));
						taskObj.put("ttrn_rw_due_date", sdfOutDisplay.format(sdfIn.parse(object[4].toString())));
						taskObj.put("ttrn_fh_due_date", sdfOutDisplay.format(sdfIn.parse(object[5].toString())));
						taskObj.put("ttrn_uh_due_date", taskObj.put("task_cat_law", object[28].toString()));
						taskObj.put("ttrn_frequency_for_operation", object[23].toString());
						taskObj.put("ttrn_id", object[0]);
						taskObj.put("task_implication", object[29].toString());
						taskObj.put("task_evaluator", object[30].toString() + " " + object[31].toString());
						taskObj.put("task_fun_head", object[32].toString() + " " + object[33].toString());

						taskObj.put("auditoComments", object[38].toString());
						if (object[39] != null) {
							taskObj.put("auditorAuditTime", object[39].toString());
						}
						taskObj.put("auditorStatus", object[40].toString());
						taskObj.put("auditDate", object[36] != null ? object[36].toString() : "NA");
						taskObj.put("reOpenDateWindow", object[37] != null ? object[37].toString() : "NA");

						/**
						 * if (object[0] != null) { List<UploadedDocuments> attachedDocuments =
						 * uploadedDocumentsDao
						 * .getAllDocumentByTtrnId(Integer.parseInt(object[0].toString()));
						 * 
						 * if (attachedDocuments != null) { taskObj.put("document_attached", 1);
						 * 
						 * Iterator<UploadedDocuments> itre = attachedDocuments.iterator(); JSONArray
						 * docArray = new JSONArray();
						 * 
						 * while (itre.hasNext()) { UploadedDocuments uploadedDocuments =
						 * (UploadedDocuments) itre.next(); JSONObject docObj = new JSONObject();
						 * docObj.put("udoc_id", uploadedDocuments.getUdoc_id());
						 * docObj.put("udoc_original_file_name",
						 * uploadedDocuments.getUdoc_original_file_name()); docArray.add(docObj); }
						 * taskObj.put("document_list", docArray); } else { taskObj.put("document_list",
						 * new JSONArray()); taskObj.put("document_attached", 0); }
						 * 
						 * } else { taskObj.put("document_list", new JSONArray());
						 * taskObj.put("document_attached", 0); }
						 * 
						 */

						tasksList.add(taskObj);
					}

				}
			}
			objForSend.put("Complied", complied);
			objForSend.put("NonComplied", noncomplied);
			// objForSend.put("PosingRisk", posingrisk);
			// objForSend.put("Delayed", delayed);
			objForSend.put("totaltasksinloop", totalTasksInLoop);
			objForSend.put("totalactivetasks", totalActiveTasks);
			objForSend.put("totalcompletedtasks", totalCompletedTasks);
			objForSend.put("Pending", pending);

			// objForSend.put("WaitingForApproval", partially_Completed);
			objForSend.put("ReOpened", re_opened);
			// objForSend.put("delayed_reported", delayed_reported);
			objForSend.put("taskList", tasksList);
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("ErrorMessage", "Failed");
			return objForSend.toJSONString();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getTaskActivityListURL(String jsonString, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		JSONArray dataForAppend = new JSONArray();
		try {
			List<Object> listOfSimplyCompDocuments = auditTasksDAO.getTaskActivityListURL(jsonString, session);
			if (listOfSimplyCompDocuments.size() > 0 && listOfSimplyCompDocuments != null) {
				Iterator<Object> iterator = listOfSimplyCompDocuments.iterator();
				while (iterator.hasNext()) {
					Object next[] = (Object[]) iterator.next();
					JSONObject objForAppend = new JSONObject();
//					objForAppend.put("TASK_ID", next[0].toString());
//					objForAppend.put("TASK_ACITIVITY", next[1].toString());
//					objForAppend.put("TASK_FORM_NO", next[2].toString());
					objForAppend.put("STATE_ID", next[0].toString());
					objForAppend.put("STATE_NAME", next[1].toString());
					dataForAppend.add(objForAppend);
				}
			}
			dataForSend.put("tasksRepo", dataForAppend);
			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonString;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String finalComplianceAuditReport(String jsonString, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		JSONArray dataForAppend = new JSONArray();
		try {

			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String date_from = jsonObj.get("date_from") != null ? jsonObj.get("date_from").toString() : "0";
			String date_to = jsonObj.get("date_to") != null ? jsonObj.get("date_to").toString() : "0";
			String orgaId = jsonObj.get("orgaId") != null ? jsonObj.get("orgaId").toString() : "0";
			String locaId = jsonObj.get("locaId") != null ? jsonObj.get("locaId").toString() : "0";
			String deptId = jsonObj.get("deptId") != null ? jsonObj.get("deptId").toString() : "0";

			List<Object> finalComplianceAuditReport = auditTasksDAO.finalComplianceAuditReport(orgaId, locaId, deptId,
					date_from, date_to, session);
			if (finalComplianceAuditReport != null && finalComplianceAuditReport.size() > 0) {
				Iterator<Object> iterator = finalComplianceAuditReport.iterator();
				while (iterator.hasNext()) {
					Object next[] = (Object[]) iterator.next();
					JSONObject objForAppend = new JSONObject();
					objForAppend.put("ttrn_id", next[0].toString());
					objForAppend.put("orga_id", next[1].toString());
					objForAppend.put("orga_name", next[2].toString());
					objForAppend.put("loca_id", next[3].toString());
					objForAppend.put("loca_name", next[4].toString());
					objForAppend.put("dept_id", next[5].toString());
					objForAppend.put("dept_name", next[6].toString());
					objForAppend.put("Complied", next[7].toString());
					objForAppend.put("NonComplied", next[8].toString());
//					objForAppend.put("Re_Opened", next[9].toString());
					objForAppend.put("Verti_Unit_Function", next[10].toString());

					Float complied = Float.parseFloat(next[7].toString());
					Float nonComplied = Float.parseFloat(next[8].toString());

					Float sum = complied + nonComplied;

					Float percentage = (complied / sum) * 100;
					String pCalculate = String.format("%.2f", percentage);
//					System.out.println("percentage : " + percentage);
					objForAppend.put("percentage", pCalculate);

					dataForAppend.add(objForAppend);
				}
			}
			dataForSend.put("finalAuditReport", dataForAppend);
			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getFunctionListByOrgaId(String jsonString, HttpSession session) {

		JSONArray dataForSend = new JSONArray();

		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String orgaId = jsonObj.get("orga_id") != null ? jsonObj.get("orga_id").toString() : "0";
			String locaId = jsonObj.get("loca_id") != null ? jsonObj.get("loca_id").toString() : "0";
			List<Object> listData = auditTasksDAO.getFunctionListByOrgaId(orgaId, locaId, session);

			Iterator<Object> itr = listData.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("orga_id", object[0]);
				objForAppend.put("orga_name", object[1].toString());
				objForAppend.put("loca_id", object[2]);
				objForAppend.put("loca_name", object[3].toString());
				objForAppend.put("dept_id", object[4]);
				objForAppend.put("dept_name", object[5].toString());
				dataForSend.add(objForAppend);
			}
			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("errorMessage", "Failed");
			dataForSend.add(objForAppend);
			return dataForSend.toJSONString();
		}

	}

}
