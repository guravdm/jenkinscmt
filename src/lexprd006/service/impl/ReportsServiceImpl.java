package lexprd006.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

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
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lexprd006.dao.CommonLogsDao;
import lexprd006.dao.ReportsDao;
import lexprd006.dao.UploadedDocumentsDao;
import lexprd006.dao.UsersDao;
import lexprd006.domain.ComplianceReportLogs;
import lexprd006.domain.User;
import lexprd006.service.ReportsService;
import lexprd006.service.UtilitiesService;

@Service(value = "reportsService")
public class ReportsServiceImpl implements ReportsService {
	private @Value("#{config['mail_user_name'] ?: 'null'}") String username;
	private @Value("#{config['mail_password'] ?: 'null'}") String password;
	private @Value("#{config['mail_smtp_host'] ?: 'null'}") String hostName;
	private @Value("#{config['mail_smtp_port'] ?: 'null'}") String portNo;
	private @Value("#{config['mail_from'] ?: 'null'}") String mailFrom;
	private @Value("#{config['project_name'] ?: 'null'}") String projectName;
	private @Value("#{config['project_url'] ?: 'null'}") String url;
	// private @Value("#{config['monthly_report_to'] ?: 'null'}") String
	// monthly_report_to;
	// private @Value("#{config['monthly_report_cc'] ?: 'null'}") String
	// monthly_report_cc;
	public final SimpleDateFormat sdfIn = new SimpleDateFormat("dd-MM-yyyy");
	public final SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd");
	public final SimpleDateFormat sdfOutnew = new SimpleDateFormat("dd-MM-yyyy");

	@Autowired
	ReportsDao reportsDao;

	@Autowired
	UsersDao usersDao;

	@Autowired
	UtilitiesService utilitiesService;

	@Autowired
	UploadedDocumentsDao uploadedDocumentsDao;

	@Autowired
	CommonLogsDao cLogsDao;

	// Method Written By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Fetch reports access wise and various filters wise
	@SuppressWarnings("unchecked")
	@Override
	public String generateReports(String jsonString, HttpSession session) {
		JSONObject objForSend = new JSONObject();
		try {

			System.out.println("generateReports jsonString : " + jsonString.toString());

			JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);
			String legal_status = jsonObject.get("legal_status").toString();
			JSONArray dataForAppend = new JSONArray();
			Date currentDate = sdfIn.parse(sdfOutnew.format(new Date()));
			List<Object> task_list = reportsDao.generateReports(jsonString, session);

			int entity_id = Integer.parseInt(jsonObject.get("entity_id").toString());
			int unit_id = Integer.parseInt(jsonObject.get("unit_id").toString());
			int func_id = Integer.parseInt(jsonObject.get("func_id").toString());
			int exec_id = Integer.parseInt(jsonObject.get("exec_id").toString());
			int eval_id = Integer.parseInt(jsonObject.get("eval_id").toString());

			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			int role_id = Integer.parseInt(session.getAttribute("sess_role_id").toString());
			Date from_date = sdfIn.parse(jsonObject.get("from_date").toString());
			Date to_date = sdfIn.parse(jsonObject.get("to_date").toString());
			String tasksImpact = jsonObject.get("task_impact").toString();

			ComplianceReportLogs logs = new ComplianceReportLogs();
			logs.setTasksImpact(tasksImpact);
			logs.setFromDate(from_date);
			logs.setToDate(to_date);
			logs.setEntityId(Integer.toString(entity_id));
			logs.setUnitId(Integer.toString(unit_id));
			logs.setFunctionId(Integer.toString(func_id));
			logs.setUserId(user_id);
			logs.setCreatedTime(new Date());
			logs.setLegalStatus(legal_status);
			cLogsDao.persistComplianceReportLogs(logs);

			if (!legal_status.equals("NA")) {
				if (legal_status.equals("Complied")) {
					Iterator<Object> itr = task_list.iterator();

					while (itr.hasNext()) {
						Object[] object = (Object[]) itr.next();
						JSONObject objForAppend = new JSONObject();
						if (object[14] != null) {
							String taskStatus = object[8].toString();
							Date legalDueDate = sdfOut.parse(object[14].toString());
							if (taskStatus.equals("Completed")) {
								Date submittedDate = sdfOut.parse(object[15].toString());
								if (submittedDate.before(legalDueDate) || submittedDate.equals(legalDueDate)) {
									objForAppend.put("task_status", "Complied");
									objForAppend.put("ttrn_submitted_date", sdfIn.format(submittedDate));
									if (object[16] != null) {
										objForAppend.put("ttrn_completed_date",
												sdfIn.format(sdfOut.parse(object[16].toString())));
									}
									objForAppend.put("tmap_client_task_id", object[0]);
									objForAppend.put("task_legi_name", object[1]);
									objForAppend.put("task_rule_name", object[2]);
									objForAppend.put("task_who", object[3]);
									objForAppend.put("task_when", object[4]);
									objForAppend.put("task_activity", object[5]);
									objForAppend.put("task_impact", object[6]);
									objForAppend.put("task_frequency", object[7]);
									objForAppend.put("ttrn_performer_comments", object[9]);
									objForAppend.put("ttrn_reason_for_non_compliance", object[10]);
									objForAppend.put("executor_name",
											object[12].toString() + " " + object[13].toString());
									objForAppend.put("ttrn_legal_due_date", sdfIn.format(legalDueDate));
									objForAppend.put("entity_name", object[18]);
									objForAppend.put("unit_name", object[19]);
									objForAppend.put("function_name", object[20]);
									objForAppend.put("task_reference", object[17]);
									objForAppend.put("task_implication", object[24]);
									objForAppend.put("evaluator_name",
											object[26].toString() + " " + object[27].toString());
									objForAppend.put("fun_head_name",
											object[29].toString() + " " + object[30].toString());
									
									
									/*if (object[21] != null) {
										List<UploadedDocuments> attachedDocuments = uploadedDocumentsDao
												.getAllDocumentByTtrnId(Integer.parseInt(object[21].toString()));
										if (attachedDocuments != null) {
											objForAppend.put("document_attached", 1);

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
											objForAppend.put("document_list", docArray);
										} else {
											objForAppend.put("document_list", new JSONArray());
											objForAppend.put("document_attached", 0);
										}
									} else {
										objForAppend.put("document_list", new JSONArray());
										objForAppend.put("document_attached", 0);
									}*/
									
									dataForAppend.add(objForAppend);
								}
							}
						}
					}
					objForSend.put("reportList", dataForAppend);
				} else {
					if (legal_status.equals("nonComplied")) {
						Iterator<Object> itr = task_list.iterator();

						while (itr.hasNext()) {
							Object[] object = (Object[]) itr.next();
							JSONObject objForAppend = new JSONObject();
							String taskStatus = object[8].toString();
							// System.out.println("Task status:" + taskStatus);
							Date legalDueDate = sdfOut.parse(object[14].toString());
							if (object[14] != null) {
								if (taskStatus.equals("Active")) {
									if (currentDate.after(legalDueDate)) {
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
										objForAppend.put("ttrn_performer_comments", object[9]);
										objForAppend.put("ttrn_reason_for_non_compliance", object[10]);
										objForAppend.put("executor_name",
												object[12].toString() + " " + object[13].toString());
										objForAppend.put("ttrn_legal_due_date", sdfIn.format(legalDueDate));
										objForAppend.put("entity_name", object[18]);
										objForAppend.put("unit_name", object[19]);
										objForAppend.put("function_name", object[20]);
										objForAppend.put("task_reference", object[17]);
										objForAppend.put("task_implication", object[24]);
										objForAppend.put("evaluator_name",
												object[26].toString() + " " + object[27].toString());
										objForAppend.put("fun_head_name",
												object[29].toString() + " " + object[30].toString());

										/*if (object[21] != null) {
											List<UploadedDocuments> attachedDocuments = uploadedDocumentsDao
													.getAllDocumentByTtrnId(Integer.parseInt(object[21].toString()));

											if (attachedDocuments != null) {
												objForAppend.put("document_attached", 1);

												Iterator<UploadedDocuments> itre = attachedDocuments.iterator();
												JSONArray docArray = new JSONArray();

												while (itre.hasNext()) {
													UploadedDocuments uploadedDocuments = (UploadedDocuments) itre
															.next();
													JSONObject docObj = new JSONObject();
													docObj.put("udoc_id", uploadedDocuments.getUdoc_id());
													docObj.put("udoc_original_file_name",
															uploadedDocuments.getUdoc_original_file_name());
													docArray.add(docObj);
												}
												objForAppend.put("document_list", docArray);
											} else {
												objForAppend.put("document_list", new JSONArray());
												objForAppend.put("document_attached", 0);
											}

										} else {
											objForAppend.put("document_list", new JSONArray());
											objForAppend.put("document_attached", 0);
										}*/

										dataForAppend.add(objForAppend);
									}
								}
							}
						}
						objForSend.put("reportList", dataForAppend);
					} else {
						if (legal_status.equals("Delayed")) {
							Iterator<Object> itr = task_list.iterator();

							while (itr.hasNext()) {
								Object[] object = (Object[]) itr.next();
								JSONObject objForAppend = new JSONObject();
								String taskStatus = object[8].toString();
								// System.out.println("Task status:" + taskStatus);
								Date legalDueDate = sdfOut.parse(object[14].toString());
								if (object[14] != null) {
									if (taskStatus.equals("Completed")) {
										Date submittedDate = sdfOut.parse(object[15].toString());
										if (submittedDate.after(legalDueDate)) {
											objForAppend.put("task_status", "Delayed");
											objForAppend.put("ttrn_submitted_date", sdfIn.format(submittedDate));
											if (object[16] != null) {
												objForAppend.put("ttrn_completed_date",
														sdfIn.format(sdfOut.parse(object[16].toString())));
											}
											objForAppend.put("tmap_client_task_id", object[0]);
											objForAppend.put("task_legi_name", object[1]);
											objForAppend.put("task_rule_name", object[2]);
											objForAppend.put("task_who", object[3]);
											objForAppend.put("task_when", object[4]);
											objForAppend.put("task_activity", object[5]);
											objForAppend.put("task_impact", object[6]);
											objForAppend.put("task_frequency", object[7]);
											objForAppend.put("ttrn_performer_comments", object[9]);
											objForAppend.put("ttrn_reason_for_non_compliance", object[10]);
											objForAppend.put("executor_name",
													object[12].toString() + " " + object[13].toString());
											objForAppend.put("ttrn_legal_due_date", sdfIn.format(legalDueDate));
											objForAppend.put("entity_name", object[18]);
											objForAppend.put("unit_name", object[19]);
											objForAppend.put("function_name", object[20]);
											objForAppend.put("task_reference", object[17]);
											objForAppend.put("task_implication", object[24]);
											objForAppend.put("evaluator_name",
													object[26].toString() + " " + object[27].toString());
											objForAppend.put("fun_head_name",
													object[29].toString() + " " + object[30].toString());

											/*if (object[21] != null) {
												List<UploadedDocuments> attachedDocuments = uploadedDocumentsDao
														.getAllDocumentByTtrnId(
																Integer.parseInt(object[21].toString()));

												if (attachedDocuments != null) {
													objForAppend.put("document_attached", 1);

													Iterator<UploadedDocuments> itre = attachedDocuments.iterator();
													JSONArray docArray = new JSONArray();

													while (itre.hasNext()) {
														UploadedDocuments uploadedDocuments = (UploadedDocuments) itre
																.next();
														JSONObject docObj = new JSONObject();
														docObj.put("udoc_id", uploadedDocuments.getUdoc_id());
														docObj.put("udoc_original_file_name",
																uploadedDocuments.getUdoc_original_file_name());
														docArray.add(docObj);
													}
													objForAppend.put("document_list", docArray);
												} else {
													objForAppend.put("document_list", new JSONArray());
													objForAppend.put("document_attached", 0);
												}

											} else {
												objForAppend.put("document_list", new JSONArray());
												objForAppend.put("document_attached", 0);
											}
											*/
											dataForAppend.add(objForAppend);
										}
									}
								}
							}
							objForSend.put("reportList", dataForAppend);
						}
					}
				}
			} else {
				Iterator<Object> itr = task_list.iterator();

				while (itr.hasNext()) {
					Object[] object = (Object[]) itr.next();
					JSONObject objForAppend = new JSONObject();
					if (object[14] != null) {
						String taskStatus = object[8].toString();
						Date legalDueDate = sdfOut.parse(object[14].toString());

						if (taskStatus.equals("Completed")) {

							Date submittedDate = sdfOut.parse(object[15].toString());
							Date completedDate = sdfOut.parse(object[16].toString());
							if ((completedDate.before(legalDueDate) || completedDate.equals(legalDueDate))
									&& submittedDate.after(legalDueDate)) {
								objForAppend.put("task_status", "Delayed_Reported");
							} else if (submittedDate.after(legalDueDate)) {
								objForAppend.put("task_status", "Delayed");
							} else {
								objForAppend.put("task_status", "Complied");
							}

							/*
							 * if (submittedDate.after(legalDueDate)) { objForAppend.put("task_status",
							 * "Delayed"); } else { objForAppend.put("task_status", "Complied"); }
							 */
							objForAppend.put("ttrn_submitted_date", sdfIn.format(submittedDate));
							if (object[16] != null) {
								objForAppend.put("ttrn_completed_date",
										sdfIn.format(sdfOut.parse(object[16].toString())));
							}
							objForAppend.put("tmap_client_task_id", object[0]);
							objForAppend.put("task_legi_name", object[1]);
							objForAppend.put("task_rule_name", object[2]);
							objForAppend.put("task_who", object[3]);
							objForAppend.put("task_when", object[4]);
							objForAppend.put("task_activity", object[5]);
							objForAppend.put("task_impact", object[6]);
							objForAppend.put("task_frequency", object[7]);
							objForAppend.put("ttrn_performer_comments", object[9]);
							objForAppend.put("ttrn_reason_for_non_compliance", object[10]);
							objForAppend.put("executor_name", object[12].toString() + " " + object[13].toString());
							objForAppend.put("ttrn_legal_due_date", sdfIn.format(legalDueDate));
							objForAppend.put("entity_name", object[18]);
							objForAppend.put("unit_name", object[19]);
							objForAppend.put("function_name", object[20]);
							objForAppend.put("task_reference", object[17]);
							objForAppend.put("task_implication", object[24]);
							objForAppend.put("evaluator_name", object[26].toString() + " " + object[27].toString());
							objForAppend.put("fun_head_name", object[29].toString() + " " + object[30].toString());

							
							/*if (object[21] != null) {
								List<UploadedDocuments> attachedDocuments = uploadedDocumentsDao
										.getAllDocumentByTtrnId(Integer.parseInt(object[21].toString()));

								if (attachedDocuments != null) {
									objForAppend.put("document_attached", 1);

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
									objForAppend.put("document_list", docArray);
								} else {
									objForAppend.put("document_list", new JSONArray());
									objForAppend.put("document_attached", 0);
								}

							} else {
								objForAppend.put("document_list", new JSONArray());
								objForAppend.put("document_attached", 0);
							}*/
							dataForAppend.add(objForAppend);
							// System.out.println("Legal Date: "+legalDueDate);
						} else {
							if (taskStatus.equals("Active")) {
								if (currentDate.after(legalDueDate)) {
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
									objForAppend.put("ttrn_performer_comments", object[9]);
									objForAppend.put("ttrn_reason_for_non_compliance", object[10]);

									if (object[12] != null && object[13] != null) {
										objForAppend.put("executor_name",
												object[12].toString() + " " + object[13].toString());
									}
									objForAppend.put("ttrn_legal_due_date", sdfIn.format(legalDueDate));
									objForAppend.put("entity_name", object[18]);
									objForAppend.put("unit_name", object[19]);
									objForAppend.put("function_name", object[20]);
									objForAppend.put("task_reference", object[17]);
									objForAppend.put("task_implication", object[24]);

									if (object[26] != null && object[27] != null) {
										objForAppend.put("evaluator_name",
												object[26].toString() + " " + object[27].toString());
									}
									if (object[29] != null && object[30] != null) {
										objForAppend.put("fun_head_name",
												object[29].toString() + " " + object[30].toString());
									}

									/**
									 * if (object[21] != null) { List<UploadedDocuments> attachedDocuments =
									 * uploadedDocumentsDao
									 * .getAllDocumentByTtrnId(Integer.parseInt(object[21].toString()));
									 * 
									 * if (attachedDocuments != null) { objForAppend.put("document_attached", 1);
									 * 
									 * Iterator<UploadedDocuments> itre = attachedDocuments.iterator(); JSONArray
									 * docArray = new JSONArray();
									 * 
									 * while (itre.hasNext()) { UploadedDocuments uploadedDocuments =
									 * (UploadedDocuments) itre.next(); JSONObject docObj = new JSONObject();
									 * docObj.put("udoc_id", uploadedDocuments.getUdoc_id());
									 * docObj.put("udoc_original_file_name",
									 * uploadedDocuments.getUdoc_original_file_name()); docArray.add(docObj); }
									 * objForAppend.put("document_list", docArray); } else {
									 * objForAppend.put("document_list", new JSONArray());
									 * objForAppend.put("document_attached", 0); }
									 * 
									 * } else { objForAppend.put("document_list", new JSONArray());
									 * objForAppend.put("document_attached", 0); }
									 */
									dataForAppend.add(objForAppend);
									// System.out.println("Legal Date: "+legalDueDate);
								}
							}

							/**
							 * Partially Completed
							 * 
							 */
							else if (taskStatus.equals("Partially_Completed")) {

								System.out.println("Partially_Completed");

								Date submittedDate = sdfOut.parse(object[15].toString());

								objForAppend.put("task_status", "Waiting For Approval");

								objForAppend.put("ttrn_submitted_date", sdfIn.format(submittedDate));
								if (object[16] != null) {
									objForAppend.put("ttrn_completed_date",
											sdfIn.format(sdfOut.parse(object[16].toString())));
								}
								objForAppend.put("tmap_client_task_id", object[0]);
								objForAppend.put("task_legi_name", object[1]);
								objForAppend.put("task_rule_name", object[2]);
								objForAppend.put("task_who", object[3]);
								objForAppend.put("task_when", object[4]);
								objForAppend.put("task_activity", object[5]);
								objForAppend.put("task_impact", object[6]);
								objForAppend.put("task_frequency", object[7]);
								objForAppend.put("ttrn_performer_comments", object[9]);
								objForAppend.put("ttrn_reason_for_non_compliance", object[10]);
								objForAppend.put("executor_name", object[12].toString() + " " + object[13].toString());
								objForAppend.put("ttrn_legal_due_date", sdfIn.format(legalDueDate));
								objForAppend.put("entity_name", object[18]);
								objForAppend.put("unit_name", object[19]);
								objForAppend.put("function_name", object[20]);
								objForAppend.put("task_reference", object[17]);
								objForAppend.put("task_implication", object[24]);
								objForAppend.put("evaluator_name", object[26].toString() + " " + object[27].toString());
								objForAppend.put("fun_head_name", object[29].toString() + " " + object[30].toString());

								/*if (object[21] != null) {
									List<UploadedDocuments> attachedDocuments = uploadedDocumentsDao
											.getAllDocumentByTtrnId(Integer.parseInt(object[21].toString()));

									if (attachedDocuments != null) {
										objForAppend.put("document_attached", 1);

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
										objForAppend.put("document_list", docArray);
									} else {
										objForAppend.put("document_list", new JSONArray());
										objForAppend.put("document_attached", 0);
									}

								} else {
									objForAppend.put("document_list", new JSONArray());
									objForAppend.put("document_attached", 0);
								}*/
								dataForAppend.add(objForAppend);
								// System.out.println("Legal Date: "+legalDueDate);

							} // END PC

							else if (taskStatus.equalsIgnoreCase("Re_Opened")) {

								objForAppend.put("task_status", "Re_Opened");
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
								objForAppend.put("ttrn_performer_comments", object[9]);
								objForAppend.put("ttrn_reason_for_non_compliance", object[10]);

								if (object[12] != null && object[13] != null) {
									objForAppend.put("executor_name",
											object[12].toString() + " " + object[13].toString());
								}
								objForAppend.put("ttrn_legal_due_date", sdfIn.format(legalDueDate));
								objForAppend.put("entity_name", object[18]);
								objForAppend.put("unit_name", object[19]);
								objForAppend.put("function_name", object[20]);
								objForAppend.put("task_reference", object[17]);
								objForAppend.put("task_implication", object[24]);

								if (object[26] != null && object[27] != null) {
									objForAppend.put("evaluator_name",
											object[26].toString() + " " + object[27].toString());
								}
								if (object[29] != null && object[30] != null) {
									objForAppend.put("fun_head_name",
											object[29].toString() + " " + object[30].toString());
								}
								dataForAppend.add(objForAppend);

							}

						}
					}

				}
				objForSend.put("reportList", dataForAppend);
			}
			// Get sub task list
			JSONArray subTaskList = new JSONArray();
			List<Object> sub_task_list = reportsDao.generateSubTaskReports(jsonString, session);

			if (legal_status.equals("NA")) {
				Iterator<Object> iterator = sub_task_list.iterator();
				while (iterator.hasNext()) {
					Object[] object = (Object[]) iterator.next();
					JSONObject objForAppend = new JSONObject();
					if (object[14] != null) {
						String taskStatus = object[8].toString();
						Date legalDueDate = sdfOut.parse(object[14].toString());
						if (taskStatus.equals("Completed")) {
							Date submittedDate = sdfOut.parse(object[15].toString());
							if (submittedDate.after(legalDueDate)) {
								objForAppend.put("task_status", "Delayed");
							} else {
								objForAppend.put("task_status", "Complied");
							}
							objForAppend.put("ttrn_submitted_date", sdfIn.format(submittedDate));
							objForAppend.put("ttrn_completed_date", sdfIn.format(sdfOut.parse(object[16].toString())));
							objForAppend.put("tmap_client_task_id", object[0]);
							objForAppend.put("task_legi_name", object[1]);
							objForAppend.put("task_rule_name", object[2]);
							objForAppend.put("task_who", object[3]);
							objForAppend.put("task_when", object[4]);
							objForAppend.put("task_activity", object[5]);
							objForAppend.put("task_impact", object[6]);
							objForAppend.put("task_frequency", object[7]);
							objForAppend.put("ttrn_performer_comments", object[9]);
							objForAppend.put("ttrn_reason_for_non_compliance", object[10]);
							objForAppend.put("executor_name", object[12].toString() + " " + object[13].toString());
							objForAppend.put("ttrn_legal_due_date", sdfIn.format(legalDueDate));
							objForAppend.put("entity_name", object[18]);
							objForAppend.put("unit_name", object[19]);
							objForAppend.put("function_name", object[20]);
							objForAppend.put("task_reference", object[17]);
							objForAppend.put("sub_task_id", object[21]);
							objForAppend.put("equi_number", object[22]);
							objForAppend.put("equi_location", object[23]);
							objForAppend.put("equi_description", object[24]);

							subTaskList.add(objForAppend);
						} else {
							if (taskStatus.equals("Active")) {
								if (currentDate.after(legalDueDate)) {
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
									objForAppend.put("ttrn_performer_comments", object[9]);
									objForAppend.put("ttrn_reason_for_non_compliance", object[10]);
									objForAppend.put("executor_name",
											object[12].toString() + " " + object[13].toString());
									objForAppend.put("ttrn_legal_due_date", sdfIn.format(legalDueDate));
									objForAppend.put("entity_name", object[18]);
									objForAppend.put("unit_name", object[19]);
									objForAppend.put("function_name", object[20]);
									objForAppend.put("task_reference", object[17]);
									objForAppend.put("sub_task_id", object[21]);
									objForAppend.put("equi_number", object[22]);
									objForAppend.put("equi_location", object[23]);
									objForAppend.put("equi_description", object[24]);
									subTaskList.add(objForAppend);
								}

							}
						}
					}

				}

			} else {

				if (legal_status.equals("Complied")) {
					Iterator<Object> iterator = sub_task_list.iterator();
					while (iterator.hasNext()) {
						Object[] object = (Object[]) iterator.next();
						JSONObject objForAppend = new JSONObject();
						if (object[14] != null) {
							String taskStatus = object[8].toString();
							Date legalDueDate = sdfOut.parse(object[14].toString());
							if (taskStatus.equals("Completed")) {
								Date submittedDate = sdfOut.parse(object[15].toString());
								if (submittedDate.before(legalDueDate) || submittedDate.equals(legalDueDate)) {
									objForAppend.put("task_status", "Complied");
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
									objForAppend.put("ttrn_reason_for_non_compliance", object[10]);
									objForAppend.put("executor_name",
											object[12].toString() + " " + object[13].toString());
									objForAppend.put("ttrn_legal_due_date", sdfIn.format(legalDueDate));
									objForAppend.put("entity_name", object[18]);
									objForAppend.put("unit_name", object[19]);
									objForAppend.put("function_name", object[20]);
									objForAppend.put("task_reference", object[17]);
									objForAppend.put("sub_task_id", object[21]);
									objForAppend.put("equi_number", object[22]);
									objForAppend.put("equi_location", object[23]);
									objForAppend.put("equi_description", object[24]);
									// System.out.println("Legal Date: "+legalDueDate);

									subTaskList.add(objForAppend);
								}

							}
						}

					}
					// objForSend.put("reportList", dataForAppend);
				} else {
					if (legal_status.equals("nonComplied")) {
						Iterator<Object> iterator = sub_task_list.iterator();
						while (iterator.hasNext()) {
							Object[] object = (Object[]) iterator.next();
							JSONObject objForAppend = new JSONObject();
							String taskStatus = object[8].toString();
							Date legalDueDate = sdfOut.parse(object[14].toString());
							if (object[14] != null) {
								if (taskStatus.equals("Active")) {
									if (currentDate.after(legalDueDate)) {
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
										objForAppend.put("ttrn_performer_comments", object[9]);
										objForAppend.put("ttrn_reason_for_non_compliance", object[10]);
										objForAppend.put("executor_name",
												object[12].toString() + " " + object[13].toString());
										objForAppend.put("ttrn_legal_due_date", sdfIn.format(legalDueDate));
										objForAppend.put("entity_name", object[18]);
										objForAppend.put("unit_name", object[19]);
										objForAppend.put("function_name", object[20]);
										objForAppend.put("task_reference", object[17]);
										objForAppend.put("sub_task_id", object[21]);
										objForAppend.put("equi_number", object[22]);
										objForAppend.put("equi_location", object[23]);
										objForAppend.put("equi_description", object[24]);
										// System.out.println("Legal Date: "+legalDueDate);

										subTaskList.add(objForAppend);
									}

								}
							}
						}
					} else {
						if (legal_status.equals("Delayed")) {
							Iterator<Object> iterator = sub_task_list.iterator();
							while (iterator.hasNext()) {
								Object[] object = (Object[]) iterator.next();
								JSONObject objForAppend = new JSONObject();
								String taskStatus = object[8].toString();
								Date legalDueDate = sdfOut.parse(object[14].toString());
								if (object[14] != null) {
									if (taskStatus.equals("Completed")) {
										Date submittedDate = sdfOut.parse(object[15].toString());
										if (submittedDate.after(legalDueDate)) {
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
											objForAppend.put("ttrn_reason_for_non_compliance", object[10]);
											objForAppend.put("executor_name",
													object[12].toString() + " " + object[13].toString());
											objForAppend.put("ttrn_legal_due_date", sdfIn.format(legalDueDate));
											objForAppend.put("entity_name", object[18]);
											objForAppend.put("unit_name", object[19]);
											objForAppend.put("function_name", object[20]);
											objForAppend.put("task_reference", object[17]);
											objForAppend.put("sub_task_id", object[21]);
											objForAppend.put("equi_number", object[22]);
											objForAppend.put("equi_location", object[23]);
											objForAppend.put("equi_description", object[24]);
											// System.out.println("Legal Date: "+legalDueDate);
											subTaskList.add(objForAppend);
										}

									}
								}

							}

						}
					}
				}

			}
			objForSend.put("reportSubTaskList", subTaskList);
			objForSend.put("responseMessage", "Success");
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("responseMessage", "Success");
			return objForSend.toJSONString();
		}
	}

	// Method Written By: Sharad Rindhe(06/02/2018)
	// Method Purpose: Fetch monthly wise report
	@SuppressWarnings("unchecked")

	@Override
	public String automatedMonthlyReports(String jsonString, int user_role) {

		JSONObject objForSend = new JSONObject();
		try {
			JSONObject objForFetch = new JSONObject();
			Date currentDate = sdfIn.parse(sdfOutnew.format(new Date()));
			Calendar aCalendar = Calendar.getInstance();
			// add -1 month to current month
			aCalendar.add(Calendar.MONTH, -1);
			// set DATE to 1, so first date of previous month
			aCalendar.set(Calendar.DATE, 1);

			String previousMonthYear = new SimpleDateFormat("MMM-yyyy").format(aCalendar.getTime());

			Date firstDateOfPreviousMonth = aCalendar.getTime();

			// set actual maximum date of previous month
			aCalendar.set(Calendar.DATE, aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));

			// Monthly automated report for Function Head
			int count = 0;

			List<User> fh_users = null;
			fh_users = usersDao.getUsersByRole(user_role);

			Iterator<User> usr_itr = fh_users.iterator();
			while (usr_itr.hasNext()) {
				User user = (User) usr_itr.next();
				System.out.println("This is Function Head user list:" + user.getUser_first_name() + " "
						+ user.getUser_last_name());
				// System.out.println("User Id :"+user.getUser_id());

				// read it
				Date lastDateOfPreviousMonth = aCalendar.getTime();

				objForFetch.put("task_impact", "NA");
				objForFetch.put("user_id", user.getUser_id());
				objForFetch.put("from_date", sdfIn.format(firstDateOfPreviousMonth));
				objForFetch.put("to_date", sdfIn.format(lastDateOfPreviousMonth));

				String format = "";
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet spreadsheet = workbook.createSheet("LastWeekNonCompliedTask");

				format = "" + "Monthly_report" + "-" + sdfOut.format(new Date()) + "";
				XSSFRow row = spreadsheet.createRow(0);

				row.createCell(0).setCellValue("Task Id");
				row.createCell(1).setCellValue("Entity");
				row.createCell(2).setCellValue("Unit");
				row.createCell(3).setCellValue("Function");
				row.createCell(4).setCellValue("Executor Name");
				row.createCell(5).setCellValue("Reference");
				row.createCell(6).setCellValue("Who");
				row.createCell(7).setCellValue("When");
				row.createCell(8).setCellValue("Compliance Activity");
				row.createCell(9).setCellValue("Impact");
				row.createCell(10).setCellValue("Frequency");
				row.createCell(11).setCellValue("Comments");
				row.createCell(12).setCellValue("Reason for non compliance");
				row.createCell(13).setCellValue("Legal due date");
				row.createCell(14).setCellValue("Completed date");
				row.createCell(15).setCellValue("Submitted date");
				row.createCell(16).setCellValue("Status");
				List<Object> task_list = reportsDao.automatedMonthlyReports(objForFetch.toJSONString());

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
							if (submittedDate.after(legalDueDate)) {
								objForAppend.put("task_status", "Non Complied");
							} else {
								objForAppend.put("task_status", "Complied");
							}
							objForAppend.put("ttrn_submitted_date", sdfIn.format(submittedDate));
							objForAppend.put("ttrn_completed_date", sdfIn.format(sdfOut.parse(object[16].toString())));
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
							objForAppend.put("executor_name", object[12].toString() + " " + object[13].toString());
							objForAppend.put("ttrn_legal_due_date", sdfIn.format(legalDueDate));
							objForAppend.put("entity_name", object[18]);
							objForAppend.put("unit_name", object[19]);
							objForAppend.put("function_name", object[20]);
							objForAppend.put("task_reference", object[17]);
							// dataForAppend.add(objForAppend);
							// System.out.println("Legal Date: "+legalDueDate);
						} else {
							if (taskStatus.equals("Active")) {
								if (currentDate.after(legalDueDate)) {
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
									objForAppend.put("ttrn_legal_due_date", sdfIn.format(legalDueDate));
									objForAppend.put("entity_name", object[18]);
									objForAppend.put("unit_name", object[19]);
									objForAppend.put("function_name", object[20]);
									objForAppend.put("task_reference", object[17]);

									if (object[10] != null) {
										objForAppend.put("ttrn_reason_for_non_compliance", object[10]);
									} else {
										objForAppend.put("ttrn_reason_for_non_compliance", "");
									}

									// dataForAppend.add(objForAppend);
									// System.out.println("Legal Date: "+legalDueDate);
								}

							}
						}
					}

					row = spreadsheet.createRow(i);
					row.createCell(0).setCellValue(objForAppend.get("tmap_client_task_id").toString());
					row.createCell(1).setCellValue(objForAppend.get("entity_name").toString());
					row.createCell(2).setCellValue(objForAppend.get("unit_name").toString());
					row.createCell(3).setCellValue(objForAppend.get("function_name").toString());
					row.createCell(4).setCellValue(objForAppend.get("executor_name").toString());
					row.createCell(5).setCellValue(objForAppend.get("task_reference").toString());
					row.createCell(6).setCellValue(objForAppend.get("task_who").toString());
					row.createCell(7).setCellValue(objForAppend.get("task_when").toString());
					row.createCell(8).setCellValue(objForAppend.get("task_activity").toString());
					row.createCell(9).setCellValue(objForAppend.get("task_impact").toString());
					row.createCell(10).setCellValue(objForAppend.get("task_frequency").toString());
					row.createCell(11).setCellValue(objForAppend.get("ttrn_performer_comments").toString());
					row.createCell(12).setCellValue(objForAppend.get("ttrn_reason_for_non_compliance").toString());
					row.createCell(13).setCellValue(objForAppend.get("ttrn_legal_due_date").toString());
					row.createCell(14).setCellValue(objForAppend.get("ttrn_completed_date").toString());
					row.createCell(15).setCellValue(objForAppend.get("ttrn_submitted_date").toString());
					row.createCell(16).setCellValue(objForAppend.get("task_status").toString());
					i++;
				} // End of task while loop

				if (task_list.size() > 0) {
					String FILE_NAME = "C:/CMS/documents/Automated_Monthly_report-" + format + ".xlsx";
					System.out.println(" Excel Create");
					FileOutputStream outputStream;
					outputStream = new FileOutputStream(FILE_NAME);
					workbook.write(outputStream);

					File file = new File(FILE_NAME);

					Properties props = new Properties();

					props.put("mail.smtp.auth", "true");
					props.put("mail.smtp.starttls.enable", "true");
					props.put("mail.smtp.host", hostName);
					props.put("mail.smtp.port", portNo);
					Session session = Session.getInstance(props, new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(username, password);
						}
					});

					String email_body = "<div style='margin:0 auto;width:100%;height:auto;padding:16px;'>";

					email_body += "<div style='margin:0 auto;width:100%;height:auto;padding:20px;'>";
					email_body += "<h2 style='font-size:18px;'>Dear " + user.getUser_first_name() + ",</h2>";
					email_body += "<p style='text-align:justify;width:70%;'>Kindly find attached compliance report for "
							+ previousMonthYear + ".</p>";
					email_body += "<p >This is System Generated Mail.</p>"
							+ "<p style='font-size:14px;'>Thanks and Regards.</p>"
							+ "<p style='font-size:16px;'>Lexcare Team.</p>" + "</div>";

					// Create a default MimeMessage object.
					Message message = new MimeMessage(session);

					// Set From: header field of the header.
					message.setFrom(new InternetAddress(mailFrom));
					message.setSentDate(new Date());
					String to = user.getUser_email();
					// String cc = monthly_report_cc;

					message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

					// message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));

					message.setSubject("Monthly Compliance Report");
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

					// utilitiesService.addMailToLog(entityObj[0].toString(), "Weekly Excel Sheet",
					// "");
					workbook.removeSheetAt(0);
					// workbook.close();
					// workbook.removeSheetAt(1);
					System.out.println("Send monthly report mail with Sheet---");
					file.delete();
					// utilitiesService.addMonthlyReportToLog(to + "," + cc, "Monthly Compliance
					// Report");
					// objForSend.put("reportList", dataForAppend);
					/*------------------Code for generating and mailing excel ends here------------------------------*/
				}
			} // End of user while loop

			objForSend.put("responseMessage", "Success");
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("responseMessage", "Failed");
			return objForSend.toJSONString();
		}

	}

	@Override
	public String getExportReportById(String jsonString, HttpSession session) {
		JSONObject objForSend = new JSONObject();
		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);
			String legal_status = jsonObject.get("legal_status").toString();
			JSONArray dataForAppend = new JSONArray();
			Date currentDate = sdfIn.parse(sdfOutnew.format(new Date()));
			List<Object> task_list = reportsDao.generateReports(jsonString, session);

			HSSFWorkbook workbook = new HSSFWorkbook();
			System.out.println("size: " + task_list.size());
			int i = 1;
			HSSFSheet sheet = workbook.createSheet("dump");
			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell((short) 0).setCellValue("Client Task ID");
			rowhead.createCell((short) 1).setCellValue("Entity");
			rowhead.createCell((short) 2).setCellValue("Unit");
			rowhead.createCell((short) 3).setCellValue("Function");
			rowhead.createCell((short) 4).setCellValue("Legislation");
			rowhead.createCell((short) 5).setCellValue("Rule");
			rowhead.createCell((short) 6).setCellValue("Who");
			rowhead.createCell((short) 7).setCellValue("When");
			rowhead.createCell((short) 8).setCellValue("Activity");
			rowhead.createCell((short) 9).setCellValue("Reference");
			rowhead.createCell((short) 10).setCellValue("Impact");
			rowhead.createCell((short) 11).setCellValue("Category Of Law");
			rowhead.createCell((short) 12).setCellValue("Frequency");
			rowhead.createCell((short) 13).setCellValue("Executor");
			rowhead.createCell((short) 14).setCellValue("Legal Due Date");
			rowhead.createCell((short) 15).setCellValue("Completed Date");
			rowhead.createCell((short) 16).setCellValue("Submitted Date");
			rowhead.createCell((short) 17).setCellValue("Status");
			rowhead.createCell((short) 18).setCellValue("Comments");
			rowhead.createCell((short) 19).setCellValue("Reason For Compliance");

			if (!legal_status.equals("NA")) {
				if (legal_status.equals("Complied")) {
					Iterator<Object> itr = task_list.iterator();
					Date submittedDate = null;
					HSSFRow row = sheet.createRow((short) i);
					while (itr.hasNext()) {
						Object[] object = (Object[]) itr.next();
						JSONObject objForAppend = new JSONObject();
						if (object[14] != null) {
							String taskStatus = object[8].toString();
							Date legalDueDate = sdfOut.parse(object[14].toString());
							if (taskStatus.equals("Completed")) {
								if (object[15] != null) {
									submittedDate = sdfOut.parse(object[15].toString());
								}

								if (submittedDate.before(legalDueDate) || submittedDate.equals(legalDueDate)) {
									row.createCell((short) 0).setCellValue(object[0].toString());
									row.createCell((short) 1).setCellValue(object[18].toString());
									row.createCell((short) 2).setCellValue(object[19].toString());
									row.createCell((short) 3).setCellValue(object[20].toString());
									row.createCell((short) 4).setCellValue(object[1].toString());
									row.createCell((short) 5).setCellValue(object[2].toString());
									row.createCell((short) 6).setCellValue(object[3].toString());
									row.createCell((short) 7).setCellValue(object[4].toString());
									row.createCell((short) 8).setCellValue(object[5].toString());
									row.createCell((short) 9).setCellValue(object[17].toString());
									row.createCell((short) 10).setCellValue(object[6].toString());
									row.createCell((short) 11).setCellValue(object[23].toString());
									row.createCell((short) 12).setCellValue(object[7].toString());
									row.createCell((short) 13)
											.setCellValue(object[12].toString() + " " + object[13].toString());
									if (object[14] != null) {
										row.createCell((short) 14).setCellValue(sdfIn.format(legalDueDate));
									}

									// row.createCell((short) 13).setCellValue(sdfIn.format(legalDueDate));
									if (object[16] != null) {
										row.createCell((short) 15)
												.setCellValue(sdfIn.format(sdfOut.parse(object[16].toString())));
									}

									row.createCell((short) 16).setCellValue(sdfIn.format(submittedDate));
									row.createCell((short) 17).setCellValue("Complied");
									if (object[9] != null) {
										row.createCell((short) 18).setCellValue(object[9].toString());
									}
									if (object[10] != null) {
										row.createCell((short) 19).setCellValue(object[10].toString());
									}
									i++;

								}
							}
						}
					}

				} else {
					if (legal_status.equals("nonComplied")) {
						Iterator<Object> itr = task_list.iterator();

						while (itr.hasNext()) {
							Object[] object = (Object[]) itr.next();
							JSONObject objForAppend = new JSONObject();
							HSSFRow row = sheet.createRow((short) i);
							String taskStatus = object[8].toString();

							Date legalDueDate = null;
							if (object[14] != null) {
								legalDueDate = sdfOut.parse(object[14].toString());
							}

							Date submittedDate = null;
							Date completedDate = null;
							if (object[14] != null) {
								if (taskStatus.equals("Active")) {
									if (object[15] != null) {
										submittedDate = sdfOut.parse(object[15].toString());
									}

									if (currentDate.after(legalDueDate)) {
										row.createCell((short) 0).setCellValue(object[0].toString());
										row.createCell((short) 1).setCellValue(object[18].toString());
										row.createCell((short) 2).setCellValue(object[19].toString());
										row.createCell((short) 3).setCellValue(object[20].toString());
										row.createCell((short) 4).setCellValue(object[1].toString());
										row.createCell((short) 5).setCellValue(object[2].toString());
										row.createCell((short) 6).setCellValue(object[3].toString());
										row.createCell((short) 7).setCellValue(object[4].toString());
										row.createCell((short) 8).setCellValue(object[5].toString());
										row.createCell((short) 9).setCellValue(object[17].toString());
										row.createCell((short) 10).setCellValue(object[6].toString());
										row.createCell((short) 11).setCellValue(object[23].toString());
										row.createCell((short) 12).setCellValue(object[7].toString());

										row.createCell((short) 13)
												.setCellValue(object[12].toString() + " " + object[13].toString());
										row.createCell((short) 14).setCellValue(sdfIn.format(legalDueDate));
										if (object[16] != null) {
											row.createCell((short) 15)
													.setCellValue(sdfIn.format(sdfOut.parse(object[16].toString())));
										}
										if (object[15] != null) {
											row.createCell((short) 16).setCellValue(sdfIn.format(submittedDate));
										}

										row.createCell((short) 17).setCellValue("Non Complied");
										if (object[9] != null) {
											row.createCell((short) 18).setCellValue(object[9].toString());
										}
										if (object[10] != null) {
											row.createCell((short) 19).setCellValue(object[10].toString());
										}
										i++;
									}
								}
							}
						}

					} else {
						if (legal_status.equals("Delayed")) {
							Iterator<Object> itr = task_list.iterator();

							while (itr.hasNext()) {
								Object[] object = (Object[]) itr.next();
								JSONObject objForAppend = new JSONObject();
								String taskStatus = object[8].toString();
								HSSFRow row = sheet.createRow((short) i);
								Date submittedDate = null;
								// System.out.println("Task status:" + taskStatus);
								Date legalDueDate = null;
								if (object[14] != null) {
									legalDueDate = sdfOut.parse(object[14].toString());
								}
								if (object[14] != null) {
									if (object[15] != null) {
										submittedDate = sdfOut.parse(object[15].toString());
									}

									if (submittedDate.after(legalDueDate)) {
										row.createCell((short) 0).setCellValue(object[0].toString());
										row.createCell((short) 1).setCellValue(object[18].toString());
										row.createCell((short) 2).setCellValue(object[19].toString());
										row.createCell((short) 3).setCellValue(object[20].toString());
										row.createCell((short) 4).setCellValue(object[1].toString());
										row.createCell((short) 5).setCellValue(object[2].toString());
										row.createCell((short) 6).setCellValue(object[3].toString());
										row.createCell((short) 7).setCellValue(object[4].toString());
										row.createCell((short) 8).setCellValue(object[5].toString());
										row.createCell((short) 9).setCellValue(object[17].toString());
										row.createCell((short) 10).setCellValue(object[6].toString());
										row.createCell((short) 11).setCellValue(object[23].toString());
										row.createCell((short) 12).setCellValue(object[7].toString());
										row.createCell((short) 13)
												.setCellValue(object[12].toString() + " " + object[13].toString());
										row.createCell((short) 14).setCellValue(sdfIn.format(legalDueDate));
										if (object[16] != null) {
											row.createCell((short) 15)
													.setCellValue(sdfIn.format(sdfOut.parse(object[16].toString())));
										}
										if (object[15] != null) {
											row.createCell((short) 16).setCellValue(sdfIn.format(submittedDate));
										}

										row.createCell((short) 17).setCellValue("Non Complied");
										if (object[9] != null) {
											row.createCell((short) 18).setCellValue(object[9].toString());
										}
										if (object[10] != null) {
											row.createCell((short) 19).setCellValue(object[10].toString());
										}
										i++;
									}
								}
							}

						}
					}
				}
			} else {
				Iterator<Object> itr = task_list.iterator();

				while (itr.hasNext()) {
					Object[] object = (Object[]) itr.next();
					HSSFRow row = sheet.createRow((short) i);
					JSONObject objForAppend = new JSONObject();
					if (object[14] != null) {
						String taskStatus = object[8].toString();
						Date legalDueDate = null;
						Date submittedDate = null;
						if (object[14] != null) {
							legalDueDate = sdfOut.parse(object[14].toString());
						}
						if (taskStatus.equals("Completed")) {
							if (object[15] != null) {
								submittedDate = sdfOut.parse(object[15].toString());
							}
							if (submittedDate.after(legalDueDate)) {
								row.createCell((short) 17).setCellValue("Delayed");
							} else {
								row.createCell((short) 17).setCellValue("Complied");
							}
							row.createCell((short) 0).setCellValue(object[0].toString());
							row.createCell((short) 1).setCellValue(object[18].toString());
							row.createCell((short) 2).setCellValue(object[19].toString());
							row.createCell((short) 3).setCellValue(object[20].toString());
							row.createCell((short) 4).setCellValue(object[1].toString());
							row.createCell((short) 5).setCellValue(object[2].toString());
							row.createCell((short) 6).setCellValue(object[3].toString());
							row.createCell((short) 7).setCellValue(object[4].toString());
							row.createCell((short) 8).setCellValue(object[5].toString());
							row.createCell((short) 9).setCellValue(object[17].toString());
							row.createCell((short) 10).setCellValue(object[6].toString());
							row.createCell((short) 11).setCellValue(object[23].toString());
							row.createCell((short) 12).setCellValue(object[7].toString());
							row.createCell((short) 13)
									.setCellValue(object[12].toString() + " " + object[13].toString());
							row.createCell((short) 14).setCellValue(sdfIn.format(legalDueDate));
							if (object[16] != null) {
								row.createCell((short) 15)
										.setCellValue(sdfIn.format(sdfOut.parse(object[16].toString())));
							}
							if (object[15] != null) {
								row.createCell((short) 16).setCellValue(sdfIn.format(submittedDate));
							}

							if (object[9] != null) {
								row.createCell((short) 18).setCellValue(object[9].toString());
							}
							if (object[10] != null) {
								row.createCell((short) 19).setCellValue(object[10].toString());
							}
							i++;
						} else {
							if (taskStatus.equals("Active")) {
								if (currentDate.after(legalDueDate)) {
									row.createCell((short) 0).setCellValue(object[0].toString());
									row.createCell((short) 1).setCellValue(object[18].toString());
									row.createCell((short) 2).setCellValue(object[19].toString());
									row.createCell((short) 3).setCellValue(object[20].toString());
									row.createCell((short) 4).setCellValue(object[1].toString());
									row.createCell((short) 5).setCellValue(object[2].toString());
									row.createCell((short) 6).setCellValue(object[3].toString());
									row.createCell((short) 7).setCellValue(object[4].toString());
									row.createCell((short) 8).setCellValue(object[5].toString());
									row.createCell((short) 9).setCellValue(object[17].toString());
									row.createCell((short) 10).setCellValue(object[6].toString());
									row.createCell((short) 11).setCellValue(object[23].toString());
									row.createCell((short) 12).setCellValue(object[7].toString());
									row.createCell((short) 13)
											.setCellValue(object[12].toString() + " " + object[13].toString());
									row.createCell((short) 14).setCellValue(sdfIn.format(legalDueDate));
									if (object[16] != null) {
										row.createCell((short) 15)
												.setCellValue(sdfIn.format(sdfOut.parse(object[16].toString())));
									}
									if (object[15] != null) {
										row.createCell((short) 16).setCellValue(sdfIn.format(submittedDate));
									}
									row.createCell((short) 17).setCellValue("Non-Complied");

									if (object[9] != null) {
										row.createCell((short) 18).setCellValue(object[9].toString());
									}
									if (object[10] != null) {
										row.createCell((short) 19).setCellValue(object[10].toString());
									}
									i++;
								}

							}
						}
					}

				}

			}
			// Get sub task list
			JSONArray subTaskList = new JSONArray();
			List<Object> sub_task_list = reportsDao.generateSubTaskReports(jsonString, session);

			if (legal_status.equals("NA")) {
				Iterator<Object> iterator = sub_task_list.iterator();
				while (iterator.hasNext()) {
					Object[] object = (Object[]) iterator.next();
					HSSFRow row = sheet.createRow((short) i);
					JSONObject objForAppend = new JSONObject();
					if (object[14] != null) {
						String taskStatus = object[8].toString();
						Date legalDueDate = null;
						Date submittedDate = null;
						if (object[14] != null) {
							legalDueDate = sdfOut.parse(object[14].toString());
						}
						if (taskStatus.equals("Completed")) {
							if (object[14] != null) {
								submittedDate = sdfOut.parse(object[15].toString());
							}
							if (submittedDate.after(legalDueDate)) {
								row.createCell((short) 17).setCellValue("Delayed");
							} else {
								row.createCell((short) 17).setCellValue("Complied");
							}
							row.createCell((short) 0).setCellValue(object[0].toString());
							row.createCell((short) 1).setCellValue(object[18].toString());
							row.createCell((short) 2).setCellValue(object[19].toString());
							row.createCell((short) 3).setCellValue(object[20].toString());
							row.createCell((short) 4).setCellValue(object[1].toString());
							row.createCell((short) 5).setCellValue(object[2].toString());
							row.createCell((short) 6).setCellValue(object[3].toString());
							row.createCell((short) 7).setCellValue(object[4].toString());
							row.createCell((short) 8).setCellValue(object[5].toString());
							row.createCell((short) 9).setCellValue(object[17].toString());
							row.createCell((short) 10).setCellValue(object[6].toString());
							row.createCell((short) 11).setCellValue(object[23].toString());
							row.createCell((short) 12).setCellValue(object[7].toString());
							row.createCell((short) 13)
									.setCellValue(object[12].toString() + " " + object[13].toString());
							row.createCell((short) 14).setCellValue(sdfIn.format(legalDueDate));
							if (object[16] != null) {
								row.createCell((short) 15)
										.setCellValue(sdfIn.format(sdfOut.parse(object[16].toString())));
							}
							if (object[15] != null) {
								row.createCell((short) 16).setCellValue(sdfIn.format(submittedDate));
							}

							if (object[9] != null) {
								row.createCell((short) 18).setCellValue(object[9].toString());
							}
							if (object[10] != null) {
								row.createCell((short) 19).setCellValue(object[10].toString());
							}
							i++;
						} else {
							if (taskStatus.equals("Active")) {
								if (currentDate.after(legalDueDate)) {
									row.createCell((short) 0).setCellValue(object[0].toString());
									row.createCell((short) 1).setCellValue(object[18].toString());
									row.createCell((short) 2).setCellValue(object[19].toString());
									row.createCell((short) 3).setCellValue(object[20].toString());
									row.createCell((short) 4).setCellValue(object[1].toString());
									row.createCell((short) 5).setCellValue(object[2].toString());
									row.createCell((short) 6).setCellValue(object[3].toString());
									row.createCell((short) 7).setCellValue(object[4].toString());
									row.createCell((short) 8).setCellValue(object[5].toString());
									row.createCell((short) 9).setCellValue(object[17].toString());
									row.createCell((short) 10).setCellValue(object[6].toString());
									row.createCell((short) 11).setCellValue(object[23].toString());
									row.createCell((short) 12).setCellValue(object[7].toString());
									row.createCell((short) 13)
											.setCellValue(object[12].toString() + " " + object[13].toString());
									row.createCell((short) 14).setCellValue(sdfIn.format(legalDueDate));
									if (object[16] != null) {
										row.createCell((short) 15)
												.setCellValue(sdfIn.format(sdfOut.parse(object[16].toString())));
									}
									if (object[15] != null) {
										row.createCell((short) 16).setCellValue(sdfIn.format(submittedDate));
									}

									row.createCell((short) 17).setCellValue("Non Complied");
									if (object[9] != null) {
										row.createCell((short) 18).setCellValue(object[9].toString());
									}
									if (object[10] != null) {
										row.createCell((short) 19).setCellValue(object[10].toString());
									}
									i++;
								}

							}
						}
					}

				}

			} else {

				if (legal_status.equals("Complied")) {
					Iterator<Object> iterator = sub_task_list.iterator();
					while (iterator.hasNext()) {
						Object[] object = (Object[]) iterator.next();
						JSONObject objForAppend = new JSONObject();
						HSSFRow row = sheet.createRow((short) i);
						Date submittedDate = null;
						Date legalDueDate = null;
						if (object[14] != null) {
							String taskStatus = object[8].toString();
							if (object[14] != null) {
								legalDueDate = sdfOut.parse(object[14].toString());
							}
							if (taskStatus.equals("Completed")) {
								if (object[15] != null) {
									submittedDate = sdfOut.parse(object[15].toString());
								}
								if (submittedDate.before(legalDueDate) || submittedDate.equals(legalDueDate)) {
									row.createCell((short) 0).setCellValue(object[0].toString());
									row.createCell((short) 1).setCellValue(object[18].toString());
									row.createCell((short) 2).setCellValue(object[19].toString());
									row.createCell((short) 3).setCellValue(object[20].toString());
									row.createCell((short) 4).setCellValue(object[1].toString());
									row.createCell((short) 5).setCellValue(object[2].toString());
									row.createCell((short) 6).setCellValue(object[3].toString());
									row.createCell((short) 7).setCellValue(object[4].toString());
									row.createCell((short) 8).setCellValue(object[5].toString());
									row.createCell((short) 9).setCellValue(object[17].toString());
									row.createCell((short) 10).setCellValue(object[6].toString());
									row.createCell((short) 11).setCellValue(object[23].toString());
									row.createCell((short) 12).setCellValue(object[7].toString());
									row.createCell((short) 13)
											.setCellValue(object[12].toString() + " " + object[13].toString());
									row.createCell((short) 14).setCellValue(sdfIn.format(legalDueDate));
									if (object[16] != null) {
										row.createCell((short) 15)
												.setCellValue(sdfIn.format(sdfOut.parse(object[16].toString())));
									}
									if (object[15] != null) {
										row.createCell((short) 16).setCellValue(sdfIn.format(submittedDate));
									}

									row.createCell((short) 17).setCellValue("Complied");
									if (object[9] != null) {
										row.createCell((short) 18).setCellValue(object[9].toString());
									}
									if (object[10] != null) {
										row.createCell((short) 19).setCellValue(object[10].toString());
									}
									i++;
								}

							}
						}

					}
					// objForSend.put("reportList", dataForAppend);
				} else {
					if (legal_status.equals("nonComplied")) {
						Iterator<Object> iterator = sub_task_list.iterator();
						while (iterator.hasNext()) {
							Object[] object = (Object[]) iterator.next();
							JSONObject objForAppend = new JSONObject();
							HSSFRow row = sheet.createRow((short) i);
							String taskStatus = object[8].toString();
							Date legalDueDate = null;
							Date submittedDate = null;
							if (object[14] != null) {
								legalDueDate = sdfOut.parse(object[14].toString());
							}

							if (object[15] != null) {
								submittedDate = sdfOut.parse(object[15].toString());
							}
							if (object[14] != null) {
								if (taskStatus.equals("Active")) {
									if (currentDate.after(legalDueDate)) {
										row.createCell((short) 0).setCellValue(object[0].toString());
										row.createCell((short) 1).setCellValue(object[18].toString());
										row.createCell((short) 2).setCellValue(object[19].toString());
										row.createCell((short) 3).setCellValue(object[20].toString());
										row.createCell((short) 4).setCellValue(object[1].toString());
										row.createCell((short) 5).setCellValue(object[2].toString());
										row.createCell((short) 6).setCellValue(object[3].toString());
										row.createCell((short) 7).setCellValue(object[4].toString());
										row.createCell((short) 8).setCellValue(object[5].toString());
										row.createCell((short) 9).setCellValue(object[17].toString());
										row.createCell((short) 10).setCellValue(object[6].toString());
										row.createCell((short) 11).setCellValue(object[23].toString());
										row.createCell((short) 12).setCellValue(object[7].toString());
										row.createCell((short) 13)
												.setCellValue(object[12].toString() + " " + object[13].toString());
										row.createCell((short) 14).setCellValue(sdfIn.format(legalDueDate));
										if (object[16] != null) {
											row.createCell((short) 15)
													.setCellValue(sdfIn.format(sdfOut.parse(object[16].toString())));
										}
										if (object[15] != null) {
											row.createCell((short) 16).setCellValue(sdfIn.format(submittedDate));
										}

										row.createCell((short) 17).setCellValue("Non Complied");
										if (object[9] != null) {
											row.createCell((short) 18).setCellValue(object[9].toString());
										}
										if (object[10] != null) {
											row.createCell((short) 19).setCellValue(object[10].toString());
										}
										i++;
									}

								}
							}
						}
					} else {
						if (legal_status.equals("Delayed")) {
							Iterator<Object> iterator = sub_task_list.iterator();
							while (iterator.hasNext()) {
								Object[] object = (Object[]) iterator.next();
								JSONObject objForAppend = new JSONObject();
								String taskStatus = object[8].toString();
								HSSFRow row = sheet.createRow((short) i);
								Date legalDueDate = null;
								Date submittedDate = null;
								if (object[14] != null) {
									legalDueDate = sdfOut.parse(object[14].toString());
								}

								if (object[14] != null) {
									if (taskStatus.equals("Completed")) {
										if (object[15] != null) {
											submittedDate = sdfOut.parse(object[15].toString());
										}
										if (submittedDate.after(legalDueDate)) {
											row.createCell((short) 0).setCellValue(object[0].toString());
											row.createCell((short) 1).setCellValue(object[18].toString());
											row.createCell((short) 2).setCellValue(object[19].toString());
											row.createCell((short) 3).setCellValue(object[20].toString());
											row.createCell((short) 4).setCellValue(object[1].toString());
											row.createCell((short) 5).setCellValue(object[2].toString());
											row.createCell((short) 6).setCellValue(object[3].toString());
											row.createCell((short) 7).setCellValue(object[4].toString());
											row.createCell((short) 8).setCellValue(object[5].toString());
											row.createCell((short) 9).setCellValue(object[17].toString());
											row.createCell((short) 10).setCellValue(object[6].toString());
											row.createCell((short) 11).setCellValue(object[23].toString());
											row.createCell((short) 12).setCellValue(object[7].toString());
											row.createCell((short) 13)
													.setCellValue(object[12].toString() + " " + object[13].toString());
											row.createCell((short) 14).setCellValue(sdfIn.format(legalDueDate));

											if (object[16] != null) {
												row.createCell((short) 15).setCellValue(
														sdfIn.format(sdfOut.parse(object[16].toString())));
											}
											if (object[15] != null) {
												row.createCell((short) 16).setCellValue(sdfIn.format(submittedDate));
											}

											row.createCell((short) 17).setCellValue("Delayed");
											if (object[9] != null) {
												row.createCell((short) 18).setCellValue(object[9].toString());
											}
											if (object[10] != null) {
												row.createCell((short) 19).setCellValue(object[10].toString());
											}
											i++;
										}

									}
								}

							}

						}
					}
				}

			}
			File dir1 = new File("C:" + File.separator + "CMS" + File.separator + projectName + File.separator
					+ "Compliance_Report");

			if (!dir1.exists())
				dir1.mkdirs();

			DateFormat df = new SimpleDateFormat("dd-MM-yy_HH-mm-ss");
			Date dateobj = new Date();
			String test = df.format(dateobj);

			Object generatedExcelFile = "Compliance_Report" + test + ".csv";
			File newExcelFile = new File(dir1.getPath() + File.separator + generatedExcelFile);

			if (!newExcelFile.exists()) {
				newExcelFile.createNewFile();
			}

			FileOutputStream fileOut = new FileOutputStream(newExcelFile);
			workbook.write(fileOut);
			fileOut.close();

			objForSend.put("responseMessage", "Success");
			return objForSend.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("responseMessage", "Success");
			return objForSend.toJSONString();
		}
	}

}
