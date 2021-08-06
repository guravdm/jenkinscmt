package lexprd006.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.csvreader.CsvReader;

import lexprd006.dao.DashboardDao;
import lexprd006.dao.TasksConfigurationDao;
import lexprd006.dao.TasksDao;
import lexprd006.dao.UploadedDocumentsDao;
import lexprd006.dao.UserEntityMappingDao;
import lexprd006.dao.UsersDao;
import lexprd006.domain.SubTask;
import lexprd006.domain.SubTaskTranscational;
import lexprd006.domain.TaskTransactional;
import lexprd006.domain.UploadedSubTaskDocuments;
import lexprd006.domain.User;
import lexprd006.service.DashboardService;
import lexprd006.service.UtilitiesService;

@Service(value = "dashboardService")
public class DashboardServiceImpl implements DashboardService {
	public final SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd");
	public final SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd");
	public final SimpleDateFormat sdfOutDisplay = new SimpleDateFormat("dd-MM-yyyy");

	private @Value("#{config['project_name'] ?: 'null'}") String projectName;

	@Autowired
	DashboardDao dashboardDao;
	@Autowired
	UserEntityMappingDao userEntityMappingDao;
	@Autowired
	UploadedDocumentsDao uploadedDocumentsDao;
	@Autowired
	TasksConfigurationDao tasksconfigurationdao;
	@Autowired
	UtilitiesService utilitiesService;

	@Autowired
	TasksConfigurationDao taskConfigurationDao;
	@Autowired
	UsersDao usersDao;
	@Autowired
	TasksDao tasksDao;

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all Tasks rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String getOverallComplianceGraph(String jsonString, HttpSession session) {
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

		LocalDate lc = LocalDate.now();

		try {
			List<Object> allTask = dashboardDao.getOverallComplianceGraph(
					Integer.parseInt(session.getAttribute("sess_user_id").toString()),
					Integer.parseInt(session.getAttribute("sess_role_id").toString()), jsonString);
			System.out.println("allTask : " + allTask);
			long startTime = System.currentTimeMillis();
			System.out.println("startTime : " + startTime);
			// List<Object> allTask = dashboardDao.getOverallComplianceGraph(8,5);
			Iterator<Object> itr = allTask.iterator();
			objForSend.put("totalTasks", allTask.size());
			Date currentDate = sdfIn.parse(sdfOut.format(new Date()));
			Date CompletedDate = null;
			while (itr.hasNext()) {

				totalTasksInLoop++;
				Object[] object = (Object[]) itr.next();
				if (object[7] != null) {
					if (object[2].toString().equals("Completed")) {

						totalCompletedTasks++;
						Date legalDueDate = null;
						if (object[7] != null || !object[7].toString().isEmpty() || object[7].toString() != "") {
							legalDueDate = sdfIn.parse(object[7].toString());
						}
						Date submittedDate = null;
						if (object[8] != null) {
							submittedDate = sdfIn.parse(object[8].toString());
						}

						if (object[27] != null) {
							CompletedDate = sdfIn.parse(object[27].toString());
						}
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
							if (object[3] != null) {
								taskObj.put("ttrn_pr_due_date",
										sdfOutDisplay.format(sdfIn.parse(object[3].toString())));
							}

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
						} else {
							/*
							 * if(submittedDate.after(legalDueDate)){ delayed++; JSONObject taskObj = new
							 * JSONObject(); taskObj.put("date", sdfOut.format(legalDueDate));
							 * taskObj.put("status", "delayed"); taskObj.put("orga_id", object[9]);
							 * taskObj.put("orga_name", object[10]); taskObj.put("loca_id", object[11]);
							 * taskObj.put("loca_name", object[12]); taskObj.put("dept_id", object[13]);
							 * taskObj.put("dept_name", object[14]); taskObj.put("tsk_impact", object[22]);
							 * //Task Details taskObj.put("ttrn_client_task_id", object[1]);
							 * taskObj.put("ttrn_legal_due_date",
							 * sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
							 * taskObj.put("task_legi_name", object[15]); taskObj.put("task_rule_name",
							 * object[16]); taskObj.put("task_reference", object[17]);
							 * taskObj.put("task_who", object[18]); taskObj.put("task_when", object[19]);
							 * taskObj.put("task_activity", object[20]); taskObj.put("task_executor",
							 * object[25].toString()+" "+object[26].toString()); taskObj.put("task_type",
							 * "Main"); tasksList.add(taskObj); }
							 */

							if (submittedDate != null)
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

									taskObj.put("auditDate", object[36] != null ? object[36].toString() : "NA");
									taskObj.put("reOpenDateWindow", object[37] != null ? object[37].toString() : "NA");

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

								}
							// Delayed reported task
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

								if (object[3] != null || object[3].toString() != "") {
									taskObj.put("ttrn_pr_due_date",
											sdfOutDisplay.format(sdfIn.parse(object[3].toString())));
								}

								if (object[4] != null || object[4].toString() != "") {
									taskObj.put("ttrn_rw_due_date",
											sdfOutDisplay.format(sdfIn.parse(object[4].toString())));
								}

								if (object[5] != null || object[5].toString() != "") {
									taskObj.put("ttrn_fh_due_date",
											sdfOutDisplay.format(sdfIn.parse(object[5].toString())));
								}
								if (object[6] != null || object[6].toString() != ""
										|| !object[6].toString().isEmpty()) {
									taskObj.put("ttrn_uh_due_date",
											sdfOutDisplay.format(sdfIn.parse(object[6].toString())));
								}

								taskObj.put("task_cat_law", object[28].toString());
								taskObj.put("ttrn_frequency_for_operation", object[23].toString());
								taskObj.put("ttrn_id", object[0]);
								taskObj.put("task_implication", object[31].toString());
								taskObj.put("task_evaluator", object[32].toString() + " " + object[33].toString());
								taskObj.put("task_fun_head", object[34].toString() + " " + object[35].toString());

								taskObj.put("auditDate", object[36] != null ? object[36].toString() : "NA");
								taskObj.put("reOpenDateWindow", object[37] != null ? object[37].toString() : "NA");

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

								taskObj.put("task_type", "Main");
								tasksList.add(taskObj);
							}

						}

						// status completed else here
					} else {
						if (object[2].toString().equals("Active")) {
							totalActiveTasks++;
							Date legalDueDate = sdfIn.parse(object[7].toString());
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

								taskObj.put("auditDate", object[36] != null ? object[36].toString() : "NA");
								taskObj.put("reOpenDateWindow", object[37] != null ? object[37].toString() : "NA");

								if (object[29] != null)
									taskObj.put("comments", object[29].toString());
								else
									taskObj.put("comments", " ");

								if (object[30] != null)
									taskObj.put("reasonForNonCompliance", object[30].toString());
								else
									taskObj.put("reasonForNonCompliance", " ");

								tasksList.add(taskObj);
							} else {
								if (currentDate.after(prdueDate)) {
									if (currentDate.before(legalDueDate) || currentDate.equals(legalDueDate)) {
										posingrisk++;

										JSONObject taskObj = new JSONObject();

										taskObj.put("date", sdfOut.format(legalDueDate));
										taskObj.put("status", "posingrisk");
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
										taskObj.put("task_executor",
												object[25].toString() + " " + object[26].toString());
										taskObj.put("task_type", "Main");
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
										taskObj.put("task_evaluator",
												object[32].toString() + " " + object[33].toString());
										taskObj.put("task_fun_head",
												object[34].toString() + " " + object[35].toString());
										if (object[29] != null)
											taskObj.put("comments", object[29].toString());
										else
											taskObj.put("comments", " ");
										if (object[30] != null)
											taskObj.put("reasonForNonCompliance", object[30].toString());
										else
											taskObj.put("reasonForNonCompliance", " ");

										taskObj.put("auditDate", object[36] != null ? object[36].toString() : "NA");
										taskObj.put("reOpenDateWindow",
												object[37] != null ? object[37].toString() : "NA");

										taskObj.put("task_type", "Main");
										tasksList.add(taskObj);

									}
								} else {
									if (prdueDate.after(currentDate) || prdueDate.equals(currentDate))
										pending++;
								}
							}

						} // Check Active END

						if (object[2].toString().equals("Partially_Completed")) {
							partially_Completed++;
							Date legalDueDate = sdfIn.parse(object[7].toString());
							JSONObject taskObj = new JSONObject();
							taskObj.put("orga_id", object[9]);
							taskObj.put("orga_name", object[10]);
							taskObj.put("loca_id", object[11]);
							taskObj.put("loca_name", object[12]);
							taskObj.put("dept_id", object[13]);
							taskObj.put("dept_name", object[14]);
							taskObj.put("tsk_impact", object[22]);
							taskObj.put("status", "waitingforapproval");

							taskObj.put("auditDate", object[36] != null ? object[36].toString() : "NA");
							taskObj.put("reOpenDateWindow", object[37] != null ? object[37].toString() : "NA");

							taskObj.put("date", sdfOut.format(legalDueDate));
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
							taskObj.put("ttrn_uh_due_date", sdfOutDisplay.format(sdfIn.parse(object[6].toString())));
							taskObj.put("task_cat_law", object[28].toString());
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

						}

						if (object[2].toString().equals("Re_Opened")) {
							re_opened++;
							Date legalDueDate = sdfIn.parse(object[7].toString());
							JSONObject taskObj = new JSONObject();
							taskObj.put("orga_id", object[9]);
							taskObj.put("orga_name", object[10]);
							taskObj.put("loca_id", object[11]);
							taskObj.put("loca_name", object[12]);
							taskObj.put("dept_id", object[13]);
							taskObj.put("dept_name", object[14]);
							taskObj.put("tsk_impact", object[22]);
							taskObj.put("status", "reopen");

							taskObj.put("auditDate", object[36] != null ? object[36].toString() : "NA");
							taskObj.put("reOpenDateWindow", object[37] != null ? object[37].toString() : "NA");
							taskObj.put("date", sdfOut.format(legalDueDate));
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
							taskObj.put("ttrn_uh_due_date", sdfOutDisplay.format(sdfIn.parse(object[6].toString())));
							taskObj.put("task_cat_law", object[28].toString());
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

							taskObj.put("task_type", "Main");
							tasksList.add(taskObj);
						}

					}
				}

			}

			int eSubtasks = 0;

			if (eSubtasks == 1) {

				// Sub Task code start
				List<Object> allSubTask = dashboardDao.getOverallComplianceGraphSubTask(
						Integer.parseInt(session.getAttribute("sess_user_id").toString()),
						Integer.parseInt(session.getAttribute("sess_role_id").toString()));

				Iterator<Object> itrSub = allSubTask.iterator();
				objForSend.put("totalTasks", allSubTask.size());
				System.out.println("subtask Size:" + allSubTask.size());
				// Date legalDueDate = null;
				// Date currentDate = sdfIn.parse(sdfOut.format(new Date()));
				while (itrSub.hasNext()) {

					totalTasksInLoop++;
					Object[] object = (Object[]) itrSub.next();
					if (object[7] != null) {
						if (object[2].toString().equals("Completed")) {

							totalCompletedTasks++;
							Date legalDueDate = sdfIn.parse(object[7].toString());
							Date submittedDate = sdfIn.parse(object[8].toString());
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
								taskObj.put("ttrn_legal_due_date",
										sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
								taskObj.put("task_legi_name", object[15]);
								taskObj.put("task_rule_name", object[16]);
								taskObj.put("task_reference", object[17]);
								taskObj.put("task_who", object[18]);
								taskObj.put("task_when", object[19]);
								taskObj.put("task_activity", object[20]);
								taskObj.put("task_executor", object[25].toString() + " " + object[26].toString());
								taskObj.put("task_type", "Sub");
								taskObj.put("sub_frequency", object[23].toString());
								taskObj.put("equipment_number", object[28].toString());
								taskObj.put("equipment_type", object[29].toString());
								taskObj.put("task_sub_id", object[27].toString());

								tasksList.add(taskObj);
							} else {
								if (submittedDate.after(legalDueDate)) {
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
									taskObj.put("sub_frequency", object[23].toString());
									taskObj.put("equipment_number", object[28].toString());
									taskObj.put("equipment_type", object[29].toString());
									taskObj.put("task_sub_id", object[27].toString());
									taskObj.put("task_type", "Sub");

									tasksList.add(taskObj);
								}
							}

						} else {
							if (object[2].toString().equals("Active")) {
								totalActiveTasks++;
								Date legalDueDate = sdfIn.parse(object[7].toString());
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
									taskObj.put("task_sub_id", object[27].toString());
									taskObj.put("sub_frequency", object[23].toString());
									taskObj.put("equipment_number", object[28].toString());
									taskObj.put("equipment_type", object[29].toString());
									taskObj.put("task_type", "Sub");

									tasksList.add(taskObj);
								} else {
									if (currentDate.after(prdueDate)) {
										if (currentDate.before(legalDueDate) || currentDate.equals(legalDueDate)) {
											posingrisk++;

											JSONObject taskObj = new JSONObject();

											taskObj.put("date", sdfOut.format(legalDueDate));
											taskObj.put("status", "posingrisk");
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
											taskObj.put("task_executor",
													object[25].toString() + " " + object[26].toString());
											taskObj.put("task_sub_id", object[27].toString());
											taskObj.put("sub_frequency", object[23].toString());
											taskObj.put("equipment_number", object[28].toString());
											taskObj.put("equipment_type", object[29].toString());
											taskObj.put("task_type", "Sub");

											tasksList.add(taskObj);

										}
									} else {
										if (prdueDate.after(currentDate) || prdueDate.equals(currentDate))
											pending++;
									}
								}

							} // Check Active END

							if (object[2].toString().equals("Partially_Completed")) {
								partially_Completed++;
								Date legalDueDate = sdfIn.parse(object[7].toString());
								JSONObject taskObj = new JSONObject();
								taskObj.put("date", sdfOut.format(legalDueDate));
								taskObj.put("status", "waitingforapproval");
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
								taskObj.put("task_sub_id", object[27].toString());
								taskObj.put("sub_frequency", object[23].toString());
								taskObj.put("equipment_number", object[28].toString());
								taskObj.put("equipment_type", object[29].toString());
								taskObj.put("task_type", "Sub");

								tasksList.add(taskObj);

							}

							if (object[2].toString().equals("Re_Opened")) {
								Date legalDueDate = sdfIn.parse(object[7].toString());
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
								taskObj.put("ttrn_legal_due_date",
										sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
								taskObj.put("task_legi_name", object[15]);
								taskObj.put("task_rule_name", object[16]);
								taskObj.put("task_reference", object[17]);
								taskObj.put("task_who", object[18]);
								taskObj.put("task_when", object[19]);
								taskObj.put("task_activity", object[20]);
								taskObj.put("task_executor", object[25].toString() + " " + object[26].toString());
								taskObj.put("task_sub_id", object[27].toString());
								taskObj.put("sub_frequency", object[23].toString());
								taskObj.put("equipment_number", object[28].toString());
								taskObj.put("equipment_type", object[29].toString());
								taskObj.put("task_type", "Sub");

								tasksList.add(taskObj);
							}

						}
					}
				}
			} // if condition closed

			// subtasks temporary removed as there is no data in the tool

			objForSend.put("Complied", complied);
			objForSend.put("NonComplied", noncomplied);
			objForSend.put("PosingRisk", posingrisk);
			objForSend.put("Delayed", delayed);
			objForSend.put("totaltasksinloop", totalTasksInLoop);
			objForSend.put("totalactivetasks", totalActiveTasks);
			objForSend.put("totalcompletedtasks", totalCompletedTasks);
			objForSend.put("Pending", pending);
			objForSend.put("WaitingForApproval", partially_Completed);
			objForSend.put("ReOpened", re_opened);
			objForSend.put("delayed_reported", delayed_reported);
			objForSend.put("taskList", tasksList);
			long stopTime = System.currentTimeMillis();
			long elapsedTime = stopTime - startTime;
			System.out.println("Time Consumed by while loop : " + elapsedTime);
			return objForSend.toJSONString();
		} catch (

		Exception e) {
			e.printStackTrace();
			objForSend.put("ErrorMessage", "Failed");
			return objForSend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all tasks entity wise
	@SuppressWarnings("unchecked")
	@Override
	public String getEntityComplianceGraph(String jsonString, HttpSession session) {
		JSONObject objForSend = new JSONObject();
		try {
			Date currentDate = sdfIn.parse(sdfOut.format(new Date()));
			int complied = 0;
			int noncomplied = 0;
			int posingrisk = 0;
			int delayed = 0;
			int pending = 0;
			int partially_Completed = 0;
			int re_opened = 0;
			JSONArray entityArray = new JSONArray();
			JSONArray compliedArray = new JSONArray();
			JSONArray nonCompliedArray = new JSONArray();
			JSONArray posingArray = new JSONArray();
			JSONArray delayedArray = new JSONArray();
			JSONArray partially_CompletedArray = new JSONArray();
			JSONArray re_openedArray = new JSONArray();

			List<Object> allTask = dashboardDao.getOverallComplianceGraph(
					Integer.parseInt(session.getAttribute("sess_user_id").toString()),
					Integer.parseInt(session.getAttribute("sess_role_id").toString()), jsonString);
			List<Object> allAccessOrganization = userEntityMappingDao
					.getDistinctOrgaById(Integer.parseInt(session.getAttribute("sess_user_id").toString()));// Get all
																											// distinct
																											// orga from
																											// user_entity_mapping_where
																											// user_id =
																											// 23
			Iterator<Object> itrOrga = allAccessOrganization.iterator();
			objForSend.put("orga_count", allAccessOrganization.size());
			while (itrOrga.hasNext()) {
				JSONObject orgaObject = new JSONObject();
				JSONArray orgaArray = new JSONArray();
				Object[] object1 = (Object[]) itrOrga.next();
				entityArray.add(object1[1]);
				Iterator<Object> itr = allTask.iterator();
				while (itr.hasNext()) {
					Object[] object = (Object[]) itr.next();
					if (object[7] != null) {
						if (Integer.parseInt(object1[0].toString()) == Integer.parseInt(object[9].toString())) {
							if (object[2].toString().equals("Completed")) {
								Date legalDueDate = sdfIn.parse(object[7].toString());
								Date submittedDate = sdfIn.parse(object[8].toString());
								if (legalDueDate.after(submittedDate) || legalDueDate.equals(submittedDate))
									complied++;
								else {
									if (submittedDate.after(legalDueDate))
										delayed++;
								}
							} else {
								if (object[2].toString().equals("Active")) {
									Date legalDueDate = sdfIn.parse(object[7].toString());
									Date prdueDate = sdfIn.parse(object[3].toString());
									if (currentDate.after(legalDueDate))
										noncomplied++;
									else {
										if (currentDate.after(prdueDate)) {
											if (currentDate.before(legalDueDate) || currentDate.equals(legalDueDate))
												posingrisk++;
										} else {
											if (prdueDate.after(currentDate) || prdueDate.equals(currentDate))
												pending++;
										}
									}
								}

								if (object[2].toString().equals("Partially_Completed")) {
									partially_Completed++;

								}

								if (object[2].toString().equals("Re_Opened")) {
									re_opened++;

								}

							} // End ELSe
						}
					}
				}
				// This is code kept for checking data correctness
				orgaObject.put("Complied", complied);
				orgaObject.put("NonComplied", noncomplied);
				orgaObject.put("PosingRisk", posingrisk);
				orgaObject.put("Delayed", delayed);
				orgaObject.put("Pending", pending);
				orgaObject.put("WaitingForApproval", partially_Completed);
				orgaObject.put("ReOpened", re_opened);
				compliedArray.add(complied);
				nonCompliedArray.add(noncomplied);
				posingArray.add(posingrisk);
				delayedArray.add(delayed);
				partially_CompletedArray.add(partially_Completed);
				re_openedArray.add(re_opened);
				orgaArray.add(orgaObject);
				objForSend.put(object1[1], orgaArray);
				complied = 0;
				noncomplied = 0;
				delayed = 0;
				posingrisk = 0;
				partially_Completed = 0;
				re_opened = 0;
			}
			objForSend.put("Entity", entityArray);
			objForSend.put("Complied", compliedArray);
			objForSend.put("NonComplied", nonCompliedArray);
			objForSend.put("PosingRisk", posingArray);
			objForSend.put("Delayed", delayedArray);
			objForSend.put("WaitingForApproval", partially_CompletedArray);
			objForSend.put("ReOpened", re_openedArray);
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("ErrorMessage", "Failed");
			return objForSend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all tasks unit wise
	@SuppressWarnings("unchecked")
	@Override
	public String getUnitComplianceGraph(String jsonString, HttpSession session) {
		JSONObject objForSend = new JSONObject();
		try {
			Date currentDate = sdfIn.parse(sdfOut.format(new Date()));
			int complied = 0;
			int noncomplied = 0;
			int posingrisk = 0;
			int delayed = 0;
			int pending = 0;
			int partially_Completed = 0;
			int re_opened = 0;
			JSONArray unitArray = new JSONArray();
			JSONArray compliedArray = new JSONArray();
			JSONArray nonCompliedArray = new JSONArray();
			JSONArray posingArray = new JSONArray();
			JSONArray delayedArray = new JSONArray();
			JSONArray partially_CompletedArray = new JSONArray();
			JSONArray re_openedArray = new JSONArray();
			List<Object> allTask = dashboardDao.getOverallComplianceGraph(
					Integer.parseInt(session.getAttribute("sess_user_id").toString()),
					Integer.parseInt(session.getAttribute("sess_role_id").toString()), jsonString);
			List<Object> allAccessOrganization = userEntityMappingDao
					.getDistinctLocaById(Integer.parseInt(session.getAttribute("sess_user_id").toString()));// Get all
																											// distinct
																											// orga from
																											// user_entity_mapping_where
																											// user_id =
																											// 23
			Iterator<Object> itrOrga = allAccessOrganization.iterator();
			objForSend.put("orga_count", allAccessOrganization.size());
			while (itrOrga.hasNext()) {
				JSONObject orgaObject = new JSONObject();
				JSONArray orgaArray = new JSONArray();
				Object[] object1 = (Object[]) itrOrga.next();
				unitArray.add(object1[1]);
				Iterator<Object> itr = allTask.iterator();
				while (itr.hasNext()) {
					Object[] object = (Object[]) itr.next();
					if (object[7] != null) {
						if (Integer.parseInt(object1[0].toString()) == Integer.parseInt(object[11].toString())) {
							if (object[2].toString().equals("Completed")) {
								Date legalDueDate = sdfIn.parse(object[7].toString());
								Date submittedDate = sdfIn.parse(object[8].toString());
								if (legalDueDate.after(submittedDate) || legalDueDate.equals(submittedDate))
									complied++;
								else {
									if (submittedDate.after(legalDueDate))
										delayed++;
								}
							} else {
								if (object[2].toString().equals("Active")) {
									Date legalDueDate = sdfIn.parse(object[7].toString());
									Date prdueDate = sdfIn.parse(object[3].toString());
									if (currentDate.after(legalDueDate))
										noncomplied++;
									else {
										if (currentDate.after(prdueDate)) {
											if (currentDate.before(legalDueDate) || currentDate.equals(legalDueDate))
												posingrisk++;
										} else {
											if (prdueDate.after(currentDate) || prdueDate.equals(currentDate))
												pending++;
										}
									}
								}

								if (object[2].toString().equals("Partially_Completed")) {
									partially_Completed++;

								}

								if (object[2].toString().equals("Re_Opened")) {
									re_opened++;

								}

							} // End Else
						}
					}
				}
				orgaObject.put("Complied", complied);
				orgaObject.put("NonComplied", noncomplied);
				orgaObject.put("PosingRisk", posingrisk);
				orgaObject.put("Delayed", delayed);
				orgaObject.put("Pending", pending);
				orgaObject.put("WaitingForApproval", partially_Completed);
				orgaObject.put("ReOpened", re_opened);

				compliedArray.add(complied);
				nonCompliedArray.add(noncomplied);
				posingArray.add(posingrisk);
				delayedArray.add(delayed);
				partially_CompletedArray.add(partially_Completed);
				re_openedArray.add(re_opened);

				orgaArray.add(orgaObject);
				objForSend.put(object1[1], orgaArray);
				complied = 0;
				noncomplied = 0;
				delayed = 0;
				posingrisk = 0;
				partially_Completed = 0;
				re_opened = 0;
			}
			objForSend.put("Unit", unitArray);
			objForSend.put("Complied", compliedArray);
			objForSend.put("NonComplied", nonCompliedArray);
			objForSend.put("PosingRisk", posingArray);
			objForSend.put("Delayed", delayedArray);
			objForSend.put("WaitingForApproval", partially_CompletedArray);
			objForSend.put("ReOpened", re_openedArray);
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("ErrorMessage", "Failed");
			return objForSend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all tasks function wise
	@SuppressWarnings("unchecked")
	@Override
	public String getFunctionComplianceGraph(String jsonString, HttpSession session) {
		JSONObject objForSend = new JSONObject();
		try {
			Date currentDate = sdfIn.parse(sdfOut.format(new Date()));
			int complied = 0;
			int noncomplied = 0;
			int posingrisk = 0;
			int delayed = 0;
			int pending = 0;
			int partially_Completed = 0;
			int re_opened = 0;
			JSONArray unitArray = new JSONArray();
			JSONArray compliedArray = new JSONArray();
			JSONArray nonCompliedArray = new JSONArray();
			JSONArray posingArray = new JSONArray();
			JSONArray delayedArray = new JSONArray();
			JSONArray partially_CompletedArray = new JSONArray();
			JSONArray re_openedArray = new JSONArray();
			List<Object> allTask = dashboardDao.getOverallComplianceGraph(
					Integer.parseInt(session.getAttribute("sess_user_id").toString()),
					Integer.parseInt(session.getAttribute("sess_role_id").toString()), jsonString);
			List<Object> allAccessOrganization = userEntityMappingDao
					.getDistinctDeptById(Integer.parseInt(session.getAttribute("sess_user_id").toString()));// Get all
																											// distinct
																											// orga from
																											// user_entity_mapping_where
																											// user_id =
																											// 23
			Iterator<Object> itrOrga = allAccessOrganization.iterator();
			objForSend.put("orga_count", allAccessOrganization.size());
			while (itrOrga.hasNext()) {
				JSONObject orgaObject = new JSONObject();
				JSONArray orgaArray = new JSONArray();
				Object[] object1 = (Object[]) itrOrga.next();
				unitArray.add(object1[1]);
				Iterator<Object> itr = allTask.iterator();
				while (itr.hasNext()) {
					Object[] object = (Object[]) itr.next();
					if (object[7] != null) {
						if (Integer.parseInt(object1[0].toString()) == Integer.parseInt(object[13].toString())) {
							if (object[2].toString().equals("Completed")) {
								Date legalDueDate = sdfIn.parse(object[7].toString());
								Date submittedDate = sdfIn.parse(object[8].toString());
								if (legalDueDate.after(submittedDate) || legalDueDate.equals(submittedDate))
									complied++;
								else {
									if (submittedDate.after(legalDueDate))
										delayed++;
								}
							} else {
								if (object[2].toString().equals("Active")) {
									Date legalDueDate = sdfIn.parse(object[7].toString());
									Date prdueDate = sdfIn.parse(object[3].toString());
									if (currentDate.after(legalDueDate))
										noncomplied++;
									else {
										if (currentDate.after(prdueDate)) {
											if (currentDate.before(legalDueDate) || currentDate.equals(legalDueDate))
												posingrisk++;
										} else {
											if (prdueDate.after(currentDate) || prdueDate.equals(currentDate))
												pending++;
										}
									}
								}

								if (object[2].toString().equals("Partially_Completed")) {
									partially_Completed++;

								}

								if (object[2].toString().equals("Re_Opened")) {
									re_opened++;

								}

							}
						}
					}
				}
				orgaObject.put("Complied", complied);
				orgaObject.put("NonComplied", noncomplied);
				orgaObject.put("PosingRisk", posingrisk);
				orgaObject.put("Delayed", delayed);
				orgaObject.put("Pending", pending);
				orgaObject.put("WaitingForApproval", partially_Completed);
				orgaObject.put("ReOpened", re_opened);

				compliedArray.add(complied);
				nonCompliedArray.add(noncomplied);
				posingArray.add(posingrisk);
				delayedArray.add(delayed);
				partially_CompletedArray.add(partially_Completed);
				re_openedArray.add(re_opened);
				orgaArray.add(orgaObject);
				// objForSend.put(object1[1], orgaArray);
				complied = 0;
				noncomplied = 0;
				delayed = 0;
				posingrisk = 0;
				partially_Completed = 0;
				re_opened = 0;
			}
			objForSend.put("Function", unitArray);
			objForSend.put("Complied", compliedArray);
			objForSend.put("NonComplied", nonCompliedArray);
			objForSend.put("PosingRisk", posingArray);
			objForSend.put("Delayed", delayedArray);
			objForSend.put("WaitingForApproval", partially_CompletedArray); // Added By Harshad Padole
			objForSend.put("ReOpened", re_openedArray); // Added By Harshad Padole
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("ErrorMessage", "Failed");
			return objForSend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all tasks function wise
	@SuppressWarnings("unchecked")
	@Override
	public String getGraphDrillDown(String jsonString, HttpSession session) {
		JSONObject objForSend = new JSONObject();
		JSONArray tasksList = new JSONArray();
		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);
			String chartName = jsonObject.get("chart_name").toString();
			String status = jsonObject.get("status").toString();
			String entity = jsonObject.get("entity").toString();
			String unit = jsonObject.get("unit").toString();
			String department = jsonObject.get("department").toString();
			if (chartName.equals("overall")) {// Overall graph
				int complied = 0;
				int noncomplied = 0;
				int posingrisk = 0;
				int delayed = 0;
				int partiallyCompleted = 0;
				int reOpened = 0;
				List<Object> allTask = dashboardDao.getOverallComplianceGraph(
						Integer.parseInt(session.getAttribute("sess_user_id").toString()),
						Integer.parseInt(session.getAttribute("sess_role_id").toString()), jsonString);
				// List<Object> allTask = dashboardDao.getOverallComplianceGraph(8,5);
				Iterator<Object> itr = allTask.iterator();
				objForSend.put("totalTasks", allTask.size());
				Date currentDate = sdfIn.parse(sdfOut.format(new Date()));
				if (status.equals("Complied")) {// Complied Task
					while (itr.hasNext()) {
						Object[] object = (Object[]) itr.next();
						if (object[7] != null) {
							if (object[2].toString().equals("Completed")) {
								Date legalDueDate = sdfIn.parse(object[7].toString());
								Date submittedDate = sdfIn.parse(object[8].toString());
								if (legalDueDate.after(submittedDate) || legalDueDate.equals(submittedDate)) {
									// Here will be complied tasks
									complied++;
									JSONObject taskListObj = new JSONObject();
									taskListObj.put("ttrn_client_task_id", object[1]);
									taskListObj.put("ttrn_legal_due_date",
											sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
									taskListObj.put("task_legi_name", object[15]);
									taskListObj.put("task_rule_name", object[16]);
									taskListObj.put("task_reference", object[17]);
									taskListObj.put("task_who", object[18]);
									taskListObj.put("task_when", object[19]);
									taskListObj.put("task_activity", object[20]);
									taskListObj.put("task_executor",
											object[25].toString() + " " + object[26].toString());
									tasksList.add(taskListObj);
								}
							}
						}
					}
				} else {
					if (status.equals("Delayed")) {
						while (itr.hasNext()) {
							Object[] object = (Object[]) itr.next();
							if (object[7] != null) {
								if (object[2].toString().equals("Completed")) {
									Date legalDueDate = sdfIn.parse(object[7].toString());
									Date submittedDate = sdfIn.parse(object[8].toString());
									if (submittedDate.after(legalDueDate)) {
										// Here will be Delayed tasks
										delayed++;
										JSONObject taskListObj = new JSONObject();
										taskListObj.put("ttrn_client_task_id", object[1]);
										taskListObj.put("ttrn_legal_due_date",
												sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
										taskListObj.put("task_legi_name", object[15]);
										taskListObj.put("task_rule_name", object[16]);
										taskListObj.put("task_reference", object[17]);
										taskListObj.put("task_who", object[18]);
										taskListObj.put("task_when", object[19]);
										taskListObj.put("task_activity", object[20]);
										taskListObj.put("task_executor",
												object[25].toString() + " " + object[26].toString());
										tasksList.add(taskListObj);
									}
								}
							}
						}
					} else {
						if (status.equals("NonComplied")) {
							while (itr.hasNext()) {
								Object[] object = (Object[]) itr.next();
								if (object[7] != null) {
									if (object[2].toString().equals("Active")) {
										Date legalDueDate = sdfIn.parse(object[7].toString());
										if (currentDate.after(legalDueDate)) {
											// Here will be non-complied tasks
											noncomplied++;
											JSONObject taskListObj = new JSONObject();
											taskListObj.put("ttrn_client_task_id", object[1]);
											taskListObj.put("ttrn_legal_due_date",
													sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
											taskListObj.put("task_legi_name", object[15]);
											taskListObj.put("task_rule_name", object[16]);
											taskListObj.put("task_reference", object[17]);
											taskListObj.put("task_who", object[18]);
											taskListObj.put("task_when", object[19]);
											taskListObj.put("task_activity", object[20]);
											taskListObj.put("task_executor",
													object[25].toString() + " " + object[26].toString());
											tasksList.add(taskListObj);
										}
									}
								}
							}
						} else {
							if (status.equals("PosingRisk")) {
								while (itr.hasNext()) {
									Object[] object = (Object[]) itr.next();
									if (object[7] != null) {
										if (object[2].toString().equals("Active")) {
											Date legalDueDate = sdfIn.parse(object[7].toString());
											Date prdueDate = sdfIn.parse(object[3].toString());
											if (currentDate.after(prdueDate)) {
												if (currentDate.before(legalDueDate)
														|| currentDate.equals(legalDueDate)) {
													// Here will be posing risk tasks
													posingrisk++;
													JSONObject taskListObj = new JSONObject();
													taskListObj.put("ttrn_client_task_id", object[1]);
													taskListObj.put("ttrn_legal_due_date",
															sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
													taskListObj.put("task_legi_name", object[15]);
													taskListObj.put("task_rule_name", object[16]);
													taskListObj.put("task_reference", object[17]);
													taskListObj.put("task_who", object[18]);
													taskListObj.put("task_when", object[19]);
													taskListObj.put("task_activity", object[20]);
													taskListObj.put("task_executor",
															object[25].toString() + " " + object[26].toString());
													tasksList.add(taskListObj);
												}
											}
										}
									}
								}
							}
						}
					}
					// Added By Harshad Padole
					if (status.equals("Waiting For Approval")) {
						while (itr.hasNext()) {
							Object[] object = (Object[]) itr.next();
							if (object[7] != null) {
								if (object[2].toString().equals("Partially_Completed")) {
									// Here will be Partially_Completed tasks
									partiallyCompleted++;
									JSONObject taskListObj = new JSONObject();
									taskListObj.put("ttrn_client_task_id", object[1]);
									taskListObj.put("ttrn_legal_due_date",
											sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
									taskListObj.put("task_legi_name", object[15]);
									taskListObj.put("task_rule_name", object[16]);
									taskListObj.put("task_reference", object[17]);
									taskListObj.put("task_who", object[18]);
									taskListObj.put("task_when", object[19]);
									taskListObj.put("task_activity", object[20]);
									taskListObj.put("task_executor",
											object[25].toString() + " " + object[26].toString());
									tasksList.add(taskListObj);
								}
							}
						}
					}
					// Added By Harshad Padole
					if (status.equals("Re-Opened")) {
						while (itr.hasNext()) {
							Object[] object = (Object[]) itr.next();
							if (object[7] != null) {
								if (object[2].toString().equals("Re_Opened")) {
									// Here will reopened tasks
									partiallyCompleted++;
									JSONObject taskListObj = new JSONObject();
									taskListObj.put("ttrn_client_task_id", object[1]);
									taskListObj.put("ttrn_legal_due_date",
											sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
									taskListObj.put("task_legi_name", object[15]);
									taskListObj.put("task_rule_name", object[16]);
									taskListObj.put("task_reference", object[17]);
									taskListObj.put("task_who", object[18]);
									taskListObj.put("task_when", object[19]);
									taskListObj.put("task_activity", object[20]);
									taskListObj.put("task_executor",
											object[25].toString() + " " + object[26].toString());
									tasksList.add(taskListObj);
								}
							}
						}
					}

				} // End Maim Else
				objForSend.put("Complied", complied);
				objForSend.put("NonComplied", noncomplied);
				objForSend.put("PosingRisk", posingrisk);
				objForSend.put("Delayed", delayed);
				objForSend.put("WaitingForApproval", partiallyCompleted); // Added By Harshad Padole
				objForSend.put("ReOpened", reOpened); // Added By Harshad Padole
				objForSend.put("TaskList", tasksList);
				return objForSend.toJSONString();
			} else {
				if (chartName.equals("entityLevel")) {
					Date currentDate = sdfIn.parse(sdfOut.format(new Date()));
					int complied = 0;
					int noncomplied = 0;
					int posingrisk = 0;
					int delayed = 0;
					int partiallyCompleted = 0;
					int reOpened = 0;
					List<Object> allTask = dashboardDao.getOverallComplianceGraph(
							Integer.parseInt(session.getAttribute("sess_user_id").toString()),
							Integer.parseInt(session.getAttribute("sess_role_id").toString()), jsonString);
					Iterator<Object> itr = allTask.iterator();
					if (status.equals("Complied")) {
						while (itr.hasNext()) {
							Object[] object = (Object[]) itr.next();
							if (object[7] != null) {
								if (object[10].toString().equals(entity)) {
									if (object[2].toString().equals("Completed")) {
										Date legalDueDate = sdfIn.parse(object[7].toString());
										Date submittedDate = sdfIn.parse(object[8].toString());
										if (legalDueDate.after(submittedDate) || legalDueDate.equals(submittedDate)) {
											complied++;
											JSONObject taskListObj = new JSONObject();
											taskListObj.put("ttrn_client_task_id", object[1]);
											taskListObj.put("ttrn_legal_due_date",
													sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
											taskListObj.put("task_legi_name", object[15]);
											taskListObj.put("task_rule_name", object[16]);
											taskListObj.put("task_reference", object[17]);
											taskListObj.put("task_who", object[18]);
											taskListObj.put("task_when", object[19]);
											taskListObj.put("task_activity", object[20]);
											taskListObj.put("task_executor",
													object[25].toString() + " " + object[26].toString());
											tasksList.add(taskListObj);
										}
									}
								}
							}
						}
					} else {
						if (status.equals("Delayed")) {
							while (itr.hasNext()) {
								Object[] object = (Object[]) itr.next();
								if (object[7] != null) {
									if (object[10].toString().equals(entity)) {
										if (object[2].toString().equals("Completed")) {
											Date legalDueDate = sdfIn.parse(object[7].toString());
											Date submittedDate = sdfIn.parse(object[8].toString());
											if (submittedDate.after(legalDueDate)) {
												delayed++;
												JSONObject taskListObj = new JSONObject();
												taskListObj.put("ttrn_client_task_id", object[1]);
												taskListObj.put("ttrn_legal_due_date",
														sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
												taskListObj.put("task_legi_name", object[15]);
												taskListObj.put("task_rule_name", object[16]);
												taskListObj.put("task_reference", object[17]);
												taskListObj.put("task_who", object[18]);
												taskListObj.put("task_when", object[19]);
												taskListObj.put("task_activity", object[20]);
												taskListObj.put("task_executor",
														object[25].toString() + " " + object[26].toString());
												tasksList.add(taskListObj);
											}
										}
									}
								}
							}
						} else {
							if (status.equals("NonComplied")) {
								while (itr.hasNext()) {
									Object[] object = (Object[]) itr.next();
									if (object[7] != null) {
										if (object[10].toString().equals(entity)) {
											if (object[2].toString().equals("Active")) {
												Date legalDueDate = sdfIn.parse(object[7].toString());
												if (currentDate.after(legalDueDate)) {
													// Here will be non-complied tasks
													noncomplied++;
													JSONObject taskListObj = new JSONObject();
													taskListObj.put("ttrn_client_task_id", object[1]);
													taskListObj.put("ttrn_legal_due_date",
															sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
													taskListObj.put("task_legi_name", object[15]);
													taskListObj.put("task_rule_name", object[16]);
													taskListObj.put("task_reference", object[17]);
													taskListObj.put("task_who", object[18]);
													taskListObj.put("task_when", object[19]);
													taskListObj.put("task_activity", object[20]);
													taskListObj.put("task_executor",
															object[25].toString() + " " + object[26].toString());
													tasksList.add(taskListObj);
												}
											}
										}
									}
								}
							} else {
								if (status.equals("PosingRisk")) {
									while (itr.hasNext()) {
										Object[] object = (Object[]) itr.next();
										if (object[7] != null) {
											if (object[10].toString().equals(entity)) {
												if (object[2].toString().equals("Active")) {
													Date legalDueDate = sdfIn.parse(object[7].toString());
													Date prdueDate = sdfIn.parse(object[3].toString());
													if (currentDate.after(prdueDate)) {
														if (currentDate.before(legalDueDate)
																|| currentDate.equals(legalDueDate)) {
															// Here will be posing risk tasks
															posingrisk++;
															JSONObject taskListObj = new JSONObject();
															taskListObj.put("ttrn_client_task_id", object[1]);
															taskListObj.put("ttrn_legal_due_date", sdfOutDisplay
																	.format(sdfIn.parse(object[7].toString())));
															taskListObj.put("task_legi_name", object[15]);
															taskListObj.put("task_rule_name", object[16]);
															taskListObj.put("task_reference", object[17]);
															taskListObj.put("task_who", object[18]);
															taskListObj.put("task_when", object[19]);
															taskListObj.put("task_activity", object[20]);
															taskListObj.put("task_executor", object[25].toString() + " "
																	+ object[26].toString());
															tasksList.add(taskListObj);
														}
													}
												}
											}
										}
									}
								}
							}
						}

						// Added By Harshad Padole
						if (status.equals("Waiting For Approval")) {
							while (itr.hasNext()) {
								Object[] object = (Object[]) itr.next();
								if (object[7] != null) {
									if (object[10].toString().equals(entity)) {
										if (object[2].toString().equals("Partially_Completed")) {

											partiallyCompleted++;
											JSONObject taskListObj = new JSONObject();
											taskListObj.put("ttrn_client_task_id", object[1]);
											taskListObj.put("ttrn_legal_due_date",
													sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
											taskListObj.put("task_legi_name", object[15]);
											taskListObj.put("task_rule_name", object[16]);
											taskListObj.put("task_reference", object[17]);
											taskListObj.put("task_who", object[18]);
											taskListObj.put("task_when", object[19]);
											taskListObj.put("task_activity", object[20]);
											taskListObj.put("task_executor",
													object[25].toString() + " " + object[26].toString());
											tasksList.add(taskListObj);

										}
									}
								}
							}
						}
						// Added By Harshad Padole
						if (status.equals("Re-Opened")) {
							while (itr.hasNext()) {
								Object[] object = (Object[]) itr.next();
								if (object[7] != null) {
									if (object[10].toString().equals(entity)) {
										if (object[2].toString().equals("Re_Opened")) {

											reOpened++;
											JSONObject taskListObj = new JSONObject();
											taskListObj.put("ttrn_client_task_id", object[1]);
											taskListObj.put("ttrn_legal_due_date",
													sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
											taskListObj.put("task_legi_name", object[15]);
											taskListObj.put("task_rule_name", object[16]);
											taskListObj.put("task_reference", object[17]);
											taskListObj.put("task_who", object[18]);
											taskListObj.put("task_when", object[19]);
											taskListObj.put("task_activity", object[20]);
											taskListObj.put("task_executor",
													object[25].toString() + " " + object[26].toString());
											tasksList.add(taskListObj);

										}
									}
								}
							}
						}

					} // End else
					objForSend.put("Complied", complied);
					objForSend.put("NonComplied", noncomplied);
					objForSend.put("PosingRisk", posingrisk);
					objForSend.put("Delayed", delayed);
					objForSend.put("WaitingForApproval", partiallyCompleted); // Added By Harshad Padole
					objForSend.put("ReOpened", reOpened); // Added By Harshad Padole
					objForSend.put("TaskList", tasksList);
					return objForSend.toJSONString();
				} // Entity Level drill down IF END
				else {
					if (chartName.equals("unitLevel")) {
						Date currentDate = sdfIn.parse(sdfOut.format(new Date()));
						int complied = 0;
						int noncomplied = 0;
						int posingrisk = 0;
						int delayed = 0;
						int partiallyCompleted = 0;
						int reOpened = 0;
						List<Object> allTask = dashboardDao.getOverallComplianceGraph(
								Integer.parseInt(session.getAttribute("sess_user_id").toString()),
								Integer.parseInt(session.getAttribute("sess_role_id").toString()), jsonString);
						Iterator<Object> itr = allTask.iterator();
						if (status.equals("Complied")) {
							while (itr.hasNext()) {
								Object[] object = (Object[]) itr.next();
								if (object[7] != null) {
									if (object[12].toString().equals(unit)) {
										if (object[2].toString().equals("Completed")) {
											Date legalDueDate = sdfIn.parse(object[7].toString());
											Date submittedDate = sdfIn.parse(object[8].toString());
											if (legalDueDate.after(submittedDate)
													|| legalDueDate.equals(submittedDate)) {
												complied++;
												JSONObject taskListObj = new JSONObject();
												taskListObj.put("ttrn_client_task_id", object[1]);
												taskListObj.put("ttrn_legal_due_date",
														sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
												taskListObj.put("task_legi_name", object[15]);
												taskListObj.put("task_rule_name", object[16]);
												taskListObj.put("task_reference", object[17]);
												taskListObj.put("task_who", object[18]);
												taskListObj.put("task_when", object[19]);
												taskListObj.put("task_activity", object[20]);
												taskListObj.put("task_executor",
														object[25].toString() + " " + object[26].toString());
												tasksList.add(taskListObj);
											}
										}
									}
								}
							}
						} else {
							if (status.equals("Delayed")) {
								while (itr.hasNext()) {
									Object[] object = (Object[]) itr.next();
									if (object[7] != null) {
										if (object[12].toString().equals(unit)) {
											if (object[2].toString().equals("Completed")) {
												Date legalDueDate = sdfIn.parse(object[7].toString());
												Date submittedDate = sdfIn.parse(object[8].toString());
												if (submittedDate.after(legalDueDate)) {
													delayed++;
													JSONObject taskListObj = new JSONObject();
													taskListObj.put("ttrn_client_task_id", object[1]);
													taskListObj.put("ttrn_legal_due_date",
															sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
													taskListObj.put("task_legi_name", object[15]);
													taskListObj.put("task_rule_name", object[16]);
													taskListObj.put("task_reference", object[17]);
													taskListObj.put("task_who", object[18]);
													taskListObj.put("task_when", object[19]);
													taskListObj.put("task_activity", object[20]);
													taskListObj.put("task_executor",
															object[25].toString() + " " + object[26].toString());
													tasksList.add(taskListObj);
												}
											}
										}
									}
								}
							} else {
								if (status.equals("NonComplied")) {
									while (itr.hasNext()) {
										Object[] object = (Object[]) itr.next();
										if (object[7] != null) {
											if (object[12].toString().equals(unit)) {
												if (object[2].toString().equals("Active")) {
													Date legalDueDate = sdfIn.parse(object[7].toString());
													if (currentDate.after(legalDueDate)) {
														// Here will be non-complied tasks
														noncomplied++;
														JSONObject taskListObj = new JSONObject();
														taskListObj.put("ttrn_client_task_id", object[1]);
														taskListObj.put("ttrn_legal_due_date", sdfOutDisplay
																.format(sdfIn.parse(object[7].toString())));
														taskListObj.put("task_legi_name", object[15]);
														taskListObj.put("task_rule_name", object[16]);
														taskListObj.put("task_reference", object[17]);
														taskListObj.put("task_who", object[18]);
														taskListObj.put("task_when", object[19]);
														taskListObj.put("task_activity", object[20]);
														taskListObj.put("task_executor",
																object[25].toString() + " " + object[26].toString());
														tasksList.add(taskListObj);
													}
												}
											}
										}
									}
								} else {
									if (status.equals("PosingRisk")) {
										while (itr.hasNext()) {
											Object[] object = (Object[]) itr.next();
											if (object[7] != null) {
												if (object[12].toString().equals(unit)) {
													if (object[2].toString().equals("Active")) {
														Date legalDueDate = sdfIn.parse(object[7].toString());
														Date prdueDate = sdfIn.parse(object[3].toString());
														if (currentDate.after(prdueDate)) {
															if (currentDate.before(legalDueDate)
																	|| currentDate.equals(legalDueDate)) {
																// Here will be posing risk tasks
																posingrisk++;
																JSONObject taskListObj = new JSONObject();
																taskListObj.put("ttrn_client_task_id", object[1]);
																taskListObj.put("ttrn_legal_due_date", sdfOutDisplay
																		.format(sdfIn.parse(object[7].toString())));
																taskListObj.put("task_legi_name", object[15]);
																taskListObj.put("task_rule_name", object[16]);
																taskListObj.put("task_reference", object[17]);
																taskListObj.put("task_who", object[18]);
																taskListObj.put("task_when", object[19]);
																taskListObj.put("task_activity", object[20]);
																taskListObj.put("task_executor", object[25].toString()
																		+ " " + object[26].toString());
																tasksList.add(taskListObj);
															}
														}
													}
												}
											}
										}
									}
								}
							}

							// Added By Harshad Padole
							if (status.equals("Waiting For Approval")) {
								while (itr.hasNext()) {
									Object[] object = (Object[]) itr.next();
									if (object[7] != null) {
										if (object[12].toString().equals(unit)) {
											if (object[2].toString().equals("Partially_Completed")) {

												partiallyCompleted++;
												JSONObject taskListObj = new JSONObject();
												taskListObj.put("ttrn_client_task_id", object[1]);
												taskListObj.put("ttrn_legal_due_date",
														sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
												taskListObj.put("task_legi_name", object[15]);
												taskListObj.put("task_rule_name", object[16]);
												taskListObj.put("task_reference", object[17]);
												taskListObj.put("task_who", object[18]);
												taskListObj.put("task_when", object[19]);
												taskListObj.put("task_activity", object[20]);
												taskListObj.put("task_executor",
														object[25].toString() + " " + object[26].toString());
												tasksList.add(taskListObj);

											}
										}
									}
								}
							}
							// Added By Harshad Padole
							if (status.equals("Re-Opened")) {
								while (itr.hasNext()) {
									Object[] object = (Object[]) itr.next();
									if (object[7] != null) {
										if (object[12].toString().equals(unit)) {
											if (object[2].toString().equals("Re_Opened")) {

												reOpened++;
												JSONObject taskListObj = new JSONObject();
												taskListObj.put("ttrn_client_task_id", object[1]);
												taskListObj.put("ttrn_legal_due_date",
														sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
												taskListObj.put("task_legi_name", object[15]);
												taskListObj.put("task_rule_name", object[16]);
												taskListObj.put("task_reference", object[17]);
												taskListObj.put("task_who", object[18]);
												taskListObj.put("task_when", object[19]);
												taskListObj.put("task_activity", object[20]);
												taskListObj.put("task_executor",
														object[25].toString() + " " + object[26].toString());
												tasksList.add(taskListObj);

											}
										}
									}
								}
							}

						}
						objForSend.put("Complied", complied);
						objForSend.put("NonComplied", noncomplied);
						objForSend.put("PosingRisk", posingrisk);
						objForSend.put("Delayed", delayed);
						objForSend.put("WaitingForApproval", partiallyCompleted); // Added By Harshad Padole
						objForSend.put("ReOpened", reOpened); // Added By Harshad Padole
						objForSend.put("TaskList", tasksList);
						return objForSend.toJSONString();
					} // End UnitLevel IF
					else {
						if (chartName.equals("departmentLevel")) {
							Date currentDate = sdfIn.parse(sdfOut.format(new Date()));
							int complied = 0;
							int noncomplied = 0;
							int posingrisk = 0;
							int delayed = 0;
							int partiallyCompleted = 0;
							int reOpened = 0;
							List<Object> allTask = dashboardDao.getOverallComplianceGraph(
									Integer.parseInt(session.getAttribute("sess_user_id").toString()),
									Integer.parseInt(session.getAttribute("sess_role_id").toString()), jsonString);
							Iterator<Object> itr = allTask.iterator();
							if (status.equals("Complied")) {
								while (itr.hasNext()) {
									Object[] object = (Object[]) itr.next();
									if (object[7] != null) {
										if (object[14].toString().equals(department)) {
											if (object[2].toString().equals("Completed")) {
												Date legalDueDate = sdfIn.parse(object[7].toString());
												Date submittedDate = sdfIn.parse(object[8].toString());
												if (legalDueDate.after(submittedDate)
														|| legalDueDate.equals(submittedDate)) {
													complied++;
													JSONObject taskListObj = new JSONObject();
													taskListObj.put("ttrn_client_task_id", object[1]);
													taskListObj.put("ttrn_legal_due_date",
															sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
													taskListObj.put("task_legi_name", object[15]);
													taskListObj.put("task_rule_name", object[16]);
													taskListObj.put("task_reference", object[17]);
													taskListObj.put("task_who", object[18]);
													taskListObj.put("task_when", object[19]);
													taskListObj.put("task_activity", object[20]);
													taskListObj.put("task_executor",
															object[25].toString() + " " + object[26].toString());
													tasksList.add(taskListObj);
												}
											}
										}
									}
								}
							} else {
								if (status.equals("Delayed")) {
									while (itr.hasNext()) {
										Object[] object = (Object[]) itr.next();
										if (object[7] != null) {
											if (object[14].toString().equals(department)) {
												if (object[2].toString().equals("Completed")) {
													Date legalDueDate = sdfIn.parse(object[7].toString());
													Date submittedDate = sdfIn.parse(object[8].toString());
													if (submittedDate.after(legalDueDate)) {
														delayed++;
														JSONObject taskListObj = new JSONObject();
														taskListObj.put("ttrn_client_task_id", object[1]);
														taskListObj.put("ttrn_legal_due_date", sdfOutDisplay
																.format(sdfIn.parse(object[7].toString())));
														taskListObj.put("task_legi_name", object[15]);
														taskListObj.put("task_rule_name", object[16]);
														taskListObj.put("task_reference", object[17]);
														taskListObj.put("task_who", object[18]);
														taskListObj.put("task_when", object[19]);
														taskListObj.put("task_activity", object[20]);
														taskListObj.put("task_executor",
																object[25].toString() + " " + object[26].toString());
														tasksList.add(taskListObj);
													}
												}
											}
										}
									}
								} else {
									if (status.equals("NonComplied")) {
										while (itr.hasNext()) {
											Object[] object = (Object[]) itr.next();
											if (object[7] != null) {
												if (object[14].toString().equals(department)) {
													if (object[2].toString().equals("Active")) {
														Date legalDueDate = sdfIn.parse(object[7].toString());
														if (currentDate.after(legalDueDate)) {
															// Here will be non-complied tasks
															noncomplied++;
															JSONObject taskListObj = new JSONObject();
															taskListObj.put("ttrn_client_task_id", object[1]);
															taskListObj.put("ttrn_legal_due_date", sdfOutDisplay
																	.format(sdfIn.parse(object[7].toString())));
															taskListObj.put("task_legi_name", object[15]);
															taskListObj.put("task_rule_name", object[16]);
															taskListObj.put("task_reference", object[17]);
															taskListObj.put("task_who", object[18]);
															taskListObj.put("task_when", object[19]);
															taskListObj.put("task_activity", object[20]);
															taskListObj.put("task_executor", object[25].toString() + " "
																	+ object[26].toString());
															tasksList.add(taskListObj);
														}
													}
												}
											}
										}
									} else {
										if (status.equals("PosingRisk")) {
											while (itr.hasNext()) {
												Object[] object = (Object[]) itr.next();
												if (object[7] != null) {
													if (object[14].toString().equals(department)) {
														if (object[2].toString().equals("Active")) {
															Date legalDueDate = sdfIn.parse(object[7].toString());
															Date prdueDate = sdfIn.parse(object[3].toString());
															if (currentDate.after(prdueDate)) {
																if (currentDate.before(legalDueDate)
																		|| currentDate.equals(legalDueDate)) {
																	// Here will be posing risk tasks
																	posingrisk++;
																	JSONObject taskListObj = new JSONObject();
																	taskListObj.put("ttrn_client_task_id", object[1]);
																	taskListObj.put("ttrn_legal_due_date", sdfOutDisplay
																			.format(sdfIn.parse(object[7].toString())));
																	taskListObj.put("task_legi_name", object[15]);
																	taskListObj.put("task_rule_name", object[16]);
																	taskListObj.put("task_reference", object[17]);
																	taskListObj.put("task_who", object[18]);
																	taskListObj.put("task_when", object[19]);
																	taskListObj.put("task_activity", object[20]);
																	taskListObj.put("task_executor",
																			object[25].toString() + " "
																					+ object[26].toString());
																	tasksList.add(taskListObj);
																}
															}
														}
													}
												}
											}
										}
									}
								}

								if (status.equals("Waiting For Approval")) {
									while (itr.hasNext()) {
										Object[] object = (Object[]) itr.next();
										if (object[7] != null) {
											if (object[14].toString().equals(department)) {
												if (object[2].toString().equals("Partially_Completed")) {
													// Here will be Waiting For Approval task tasks
													partiallyCompleted++;
													JSONObject taskListObj = new JSONObject();
													taskListObj.put("ttrn_client_task_id", object[1]);
													taskListObj.put("ttrn_legal_due_date",
															sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
													taskListObj.put("task_legi_name", object[15]);
													taskListObj.put("task_rule_name", object[16]);
													taskListObj.put("task_reference", object[17]);
													taskListObj.put("task_who", object[18]);
													taskListObj.put("task_when", object[19]);
													taskListObj.put("task_activity", object[20]);
													taskListObj.put("task_executor",
															object[25].toString() + " " + object[26].toString());
													tasksList.add(taskListObj);
												}
											}
										}
									}
								}

								if (status.equals("Re-Opened")) {
									while (itr.hasNext()) {
										Object[] object = (Object[]) itr.next();
										if (object[7] != null) {
											if (object[14].toString().equals(department)) {
												if (object[2].toString().equals("Re_Opened")) {
													// Here will be reopened task tasks
													reOpened++;
													JSONObject taskListObj = new JSONObject();
													taskListObj.put("ttrn_client_task_id", object[1]);
													taskListObj.put("ttrn_legal_due_date",
															sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
													taskListObj.put("task_legi_name", object[15]);
													taskListObj.put("task_rule_name", object[16]);
													taskListObj.put("task_reference", object[17]);
													taskListObj.put("task_who", object[18]);
													taskListObj.put("task_when", object[19]);
													taskListObj.put("task_activity", object[20]);
													taskListObj.put("task_executor",
															object[25].toString() + " " + object[26].toString());
													tasksList.add(taskListObj);
												}
											}
										}
									}
								}

							} // End Main Else
							objForSend.put("Complied", complied);
							objForSend.put("NonComplied", noncomplied);
							objForSend.put("PosingRisk", posingrisk);
							objForSend.put("Delayed", delayed);
							objForSend.put("WaitingForApproval", partiallyCompleted); // Added By Harshad Padole
							objForSend.put("ReOpened", reOpened); // Added By Harshad Padole
							objForSend.put("TaskList", tasksList);
							return objForSend.toJSONString();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("ErrorMessage", "Failed");
			return objForSend.toJSONString();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String searchGetOverallComplianceGraph(String jsonString, HttpSession session) {

		JSONObject objForSend = new JSONObject();
		JSONArray tasksList = new JSONArray();
		int totalTasksInLoop = 0;
		int totalActiveTasks = 0;
		int totalCompletedTasks = 0;
		int complied = 0;
		int noncomplied = 0;
		int delayedReported = 0;
		int posingrisk = 0;
		int delayed = 0;
		int pending = 0;
		int partially_Completed = 0;
		int re_opened = 0;

		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String fromDate = "";
			String toDate = "";

			Calendar c = Calendar.getInstance();
			int month = c.get(Calendar.MONTH);

			if (jsonObj.get("date_from") != null && jsonObj.get("date_to") != null) {
				fromDate = jsonObj.get("date_from").toString();
				toDate = jsonObj.get("date_to").toString();
				// System.out.println("\nfromDate : " + fromDate);
				// System.out.println("toDate : " + toDate);
			} else if (month >= Calendar.JANUARY && month <= Calendar.MARCH) {

			} else if (month >= Calendar.APRIL && month <= Calendar.JUNE) {

			} else if (month >= Calendar.JULY && month <= Calendar.SEPTEMBER) {

			} else if (month >= Calendar.OCTOBER && month <= Calendar.DECEMBER) {

			}
			String orgaId = "0";
			if (jsonObj.get("orgaId") != null) {
				orgaId = jsonObj.get("orgaId").toString();
			}

			// String quarterDate = jsonObj.get("quarter").toString();

			List<Object> allTask = dashboardDao.searchGetOverallComplianceGraph(
					Integer.parseInt(session.getAttribute("sess_user_id").toString()),
					Integer.parseInt(session.getAttribute("sess_role_id").toString()), fromDate, toDate, orgaId);
			Iterator<Object> itr = allTask.iterator();
			objForSend.put("totalTasks", allTask.size());
			Date currentDate = sdfIn.parse(sdfOut.format(new Date()));
			Date completedDate = null;
			while (itr.hasNext()) {
				totalTasksInLoop++;
				Object[] object = (Object[]) itr.next();

				if (object[7] != null) {
					if (object[2].toString().equals("Completed")) {
						totalCompletedTasks++;
						Date legalDueDate = sdfIn.parse(object[7].toString());
						Date submittedDate = sdfIn.parse(object[8].toString());
						// System.out.println("Completed Date : " + object[34].toString());
						if (object[34] != null) {
							completedDate = sdfIn.parse(object[34].toString());
						}

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
							taskObj.put("ttrn_submitted_date", sdfOutDisplay.format(sdfIn.parse(object[8].toString())));
							taskObj.put("task_cat_law", object[27].toString());
							taskObj.put("task_type", "Main");
							taskObj.put("pr_due_date", sdfOutDisplay.format(sdfIn.parse(object[3].toString())));
							taskObj.put("rw_due_date", sdfOutDisplay.format(sdfIn.parse(object[4].toString())));
							taskObj.put("fh_due_date", sdfOutDisplay.format(sdfIn.parse(object[5].toString())));
							taskObj.put("task_evaluator", object[29].toString() + " " + object[30].toString());
							taskObj.put("task_fun_head", object[32].toString() + " " + object[33].toString());
							taskObj.put("ttrn_id", object[0]);
							taskObj.put("task_implication", object[37].toString());
							if (object[35] != null)
								taskObj.put("comments", object[35].toString());
							else
								taskObj.put("comments", " ");

							if (object[36] != null)
								taskObj.put("reasonForNonCompliance", object[36].toString());
							else
								taskObj.put("reasonForNonCompliance", " ");
							tasksList.add(taskObj);
						} else {
							if (submittedDate.after(legalDueDate) && completedDate.after(legalDueDate)) {
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
								taskObj.put("ttrn_submitted_date",
										sdfOutDisplay.format(sdfIn.parse(object[8].toString())));
								taskObj.put("task_cat_law", object[27].toString());
								taskObj.put("task_type", "Main");
								taskObj.put("pr_due_date", sdfOutDisplay.format(sdfIn.parse(object[3].toString())));
								taskObj.put("rw_due_date", sdfOutDisplay.format(sdfIn.parse(object[4].toString())));
								taskObj.put("fh_due_date", sdfOutDisplay.format(sdfIn.parse(object[5].toString())));
								taskObj.put("task_evaluator", object[29].toString() + " " + object[30].toString());
								taskObj.put("task_fun_head", object[32].toString() + " " + object[33].toString());
								taskObj.put("ttrn_id", object[0]);
								taskObj.put("task_implication", object[37].toString());
								if (object[35] != null)
									taskObj.put("comments", object[35].toString());
								else
									taskObj.put("comments", " ");

								if (object[36] != null)
									taskObj.put("reasonForNonCompliance", object[36].toString());
								else
									taskObj.put("reasonForNonCompliance", " ");
								tasksList.add(taskObj);
							}
							if (submittedDate.after(legalDueDate)
									&& (completedDate.before(legalDueDate) || completedDate.equals(legalDueDate))) {
								delayedReported++;
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
								taskObj.put("ttrn_submitted_date",
										sdfOutDisplay.format(sdfIn.parse(object[8].toString())));
								taskObj.put("task_cat_law", object[27].toString());
								taskObj.put("task_type", "Main");
								taskObj.put("pr_due_date", sdfOutDisplay.format(sdfIn.parse(object[3].toString())));
								taskObj.put("rw_due_date", sdfOutDisplay.format(sdfIn.parse(object[4].toString())));
								taskObj.put("fh_due_date", sdfOutDisplay.format(sdfIn.parse(object[5].toString())));
								taskObj.put("task_evaluator", object[29].toString() + " " + object[30].toString());
								taskObj.put("task_fun_head", object[32].toString() + " " + object[33].toString());
								taskObj.put("ttrn_id", object[0]);
								taskObj.put("task_implication", object[37].toString());
								if (object[35] != null)
									taskObj.put("comments", object[35].toString());
								else
									taskObj.put("comments", " ");

								if (object[36] != null)
									taskObj.put("reasonForNonCompliance", object[36].toString());
								else
									taskObj.put("reasonForNonCompliance", " ");
								tasksList.add(taskObj);

							}
						}
					} else {
						if (object[2].toString().equals("Active")) {
							totalActiveTasks++;
							Date legalDueDate = sdfIn.parse(object[7].toString());
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
								taskObj.put("ttrn_submitted_date", "NA");
								if (object[27] != null) {
									taskObj.put("task_cat_law", object[27].toString());
								} else {
									taskObj.put("task_cat_law", "NA");
								}
								taskObj.put("task_type", "Main");
								taskObj.put("pr_due_date", sdfOutDisplay.format(sdfIn.parse(object[3].toString())));
								taskObj.put("rw_due_date", sdfOutDisplay.format(sdfIn.parse(object[4].toString())));
								taskObj.put("fh_due_date", sdfOutDisplay.format(sdfIn.parse(object[5].toString())));

								taskObj.put("task_evaluator", object[29].toString() + " " + object[30].toString());
								taskObj.put("task_fun_head", object[32].toString() + " " + object[33].toString());
								taskObj.put("ttrn_id", object[0]);
								taskObj.put("task_implication", object[37].toString());
								if (object[35] != null)
									taskObj.put("comments", object[35].toString());
								else
									taskObj.put("comments", " ");

								if (object[36] != null)
									taskObj.put("reasonForNonCompliance", object[36].toString());
								else
									taskObj.put("reasonForNonCompliance", " ");
								tasksList.add(taskObj);
							} else {
								if (currentDate.after(prdueDate)) {
									if (currentDate.before(legalDueDate) || currentDate.equals(legalDueDate)) {
										posingrisk++;
										JSONObject taskObj = new JSONObject();
										taskObj.put("date", sdfOut.format(legalDueDate));
										taskObj.put("status", "posingrisk");
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
										taskObj.put("task_executor",
												object[25].toString() + " " + object[26].toString());
										taskObj.put("ttrn_submitted_date", "NA");
										taskObj.put("task_cat_law", object[27].toString());
										taskObj.put("task_type", "Main");
										taskObj.put("pr_due_date",
												sdfOutDisplay.format(sdfIn.parse(object[3].toString())));
										taskObj.put("rw_due_date",
												sdfOutDisplay.format(sdfIn.parse(object[4].toString())));
										taskObj.put("fh_due_date",
												sdfOutDisplay.format(sdfIn.parse(object[5].toString())));
										taskObj.put("task_evaluator",
												object[29].toString() + " " + object[30].toString());
										taskObj.put("task_fun_head",
												object[32].toString() + " " + object[33].toString());
										taskObj.put("ttrn_id", object[0]);
										taskObj.put("task_implication", object[37].toString());
										if (object[35] != null)
											taskObj.put("comments", object[35].toString());
										else
											taskObj.put("comments", " ");

										if (object[36] != null)
											taskObj.put("reasonForNonCompliance", object[36].toString());
										else
											taskObj.put("reasonForNonCompliance", " ");
										tasksList.add(taskObj);
									}
								} else {
									if (prdueDate.after(currentDate) || prdueDate.equals(currentDate))
										pending++;
								}
							}
						} // Check Active END

						if (object[2].toString().equals("Partially_Completed")) {
							partially_Completed++;
							Date legalDueDate = sdfIn.parse(object[7].toString());
							JSONObject taskObj = new JSONObject();
							taskObj.put("orga_id", object[9]);
							taskObj.put("orga_name", object[10]);
							taskObj.put("loca_id", object[11]);
							taskObj.put("loca_name", object[12]);
							taskObj.put("dept_id", object[13]);
							taskObj.put("dept_name", object[14]);
							taskObj.put("tsk_impact", object[22]);
							taskObj.put("status", "waitingforapproval");
							taskObj.put("date", sdfOut.format(legalDueDate));
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
							taskObj.put("ttrn_submitted_date", sdfOutDisplay.format(sdfIn.parse(object[8].toString())));
							taskObj.put("task_cat_law", object[27].toString());
							taskObj.put("task_type", "Main");
							taskObj.put("pr_due_date", sdfOutDisplay.format(sdfIn.parse(object[3].toString())));
							taskObj.put("rw_due_date", sdfOutDisplay.format(sdfIn.parse(object[4].toString())));
							taskObj.put("fh_due_date", sdfOutDisplay.format(sdfIn.parse(object[5].toString())));
							taskObj.put("task_evaluator", object[29].toString() + " " + object[30].toString());
							taskObj.put("task_fun_head", object[32].toString() + " " + object[33].toString());
							taskObj.put("ttrn_id", object[0]);
							taskObj.put("task_implication", object[37].toString());
							if (object[35] != null)
								taskObj.put("comments", object[35].toString());
							else
								taskObj.put("comments", " ");

							if (object[36] != null)
								taskObj.put("reasonForNonCompliance", object[36].toString());
							else
								taskObj.put("reasonForNonCompliance", " ");
							tasksList.add(taskObj);
						}

						if (object[2].toString().equals("Re_Opened")) {
							re_opened++;
							Date legalDueDate = sdfIn.parse(object[7].toString());
							JSONObject taskObj = new JSONObject();
							taskObj.put("orga_id", object[9]);
							taskObj.put("orga_name", object[10]);
							taskObj.put("loca_id", object[11]);
							taskObj.put("loca_name", object[12]);
							taskObj.put("dept_id", object[13]);
							taskObj.put("dept_name", object[14]);
							taskObj.put("tsk_impact", object[22]);
							taskObj.put("status", "reopen");
							taskObj.put("date", sdfOut.format(legalDueDate));
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
							taskObj.put("ttrn_submitted_date", "NA");
							taskObj.put("task_cat_law", object[27].toString());
							taskObj.put("task_type", "Main");
							taskObj.put("pr_due_date", sdfOutDisplay.format(sdfIn.parse(object[3].toString())));
							taskObj.put("rw_due_date", sdfOutDisplay.format(sdfIn.parse(object[4].toString())));
							taskObj.put("fh_due_date", sdfOutDisplay.format(sdfIn.parse(object[5].toString())));
							taskObj.put("task_evaluator", object[29].toString() + " " + object[30].toString());
							taskObj.put("task_fun_head", object[32].toString() + " " + object[33].toString());
							taskObj.put("ttrn_id", object[0]);
							taskObj.put("task_implication", object[37].toString());
							if (object[35] != null)
								taskObj.put("comments", object[35].toString());
							else
								taskObj.put("comments", " ");

							if (object[36] != null)
								taskObj.put("reasonForNonCompliance", object[36].toString());
							else
								taskObj.put("reasonForNonCompliance", " ");
							tasksList.add(taskObj);
						}
					}
				}
			}

			int eSubtasks = 0;

			if (eSubtasks == 1) {

				// Sub Task code start
				List<Object> allSubTask = dashboardDao.getSearchComplianceGraphSubTask(
						Integer.parseInt(session.getAttribute("sess_user_id").toString()),
						Integer.parseInt(session.getAttribute("sess_role_id").toString()), fromDate, toDate, orgaId);

				Iterator<Object> itrSub = allSubTask.iterator();
				objForSend.put("totalTasks", allSubTask.size());
				System.out.println("subtask Size:" + allSubTask.size());
				// Date legalDueDate = null;
				// Date currentDate = sdfIn.parse(sdfOut.format(new Date()));
				while (itrSub.hasNext()) {

					totalTasksInLoop++;
					Object[] object = (Object[]) itrSub.next();
					if (object[7] != null) {
						if (object[2].toString().equals("Completed")) {

							totalCompletedTasks++;
							Date legalDueDate = sdfIn.parse(object[7].toString());
							Date submittedDate = sdfIn.parse(object[8].toString());
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
								taskObj.put("ttrn_legal_due_date",
										sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
								taskObj.put("task_legi_name", object[15]);
								taskObj.put("task_rule_name", object[16]);
								taskObj.put("task_reference", object[17]);
								taskObj.put("task_who", object[18]);
								taskObj.put("task_when", object[19]);
								taskObj.put("task_activity", object[20]);
								taskObj.put("task_executor", object[25].toString() + " " + object[26].toString());
								taskObj.put("task_type", "Sub");
								taskObj.put("sub_frequency", object[23].toString());
								taskObj.put("equipment_number", object[28].toString());
								taskObj.put("equipment_type", object[29].toString());
								taskObj.put("task_sub_id", object[27].toString());

								tasksList.add(taskObj);
							} else {
								if (submittedDate.after(legalDueDate)) {
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
									taskObj.put("sub_frequency", object[23].toString());
									taskObj.put("equipment_number", object[28].toString());
									taskObj.put("equipment_type", object[29].toString());
									taskObj.put("task_sub_id", object[27].toString());
									taskObj.put("task_type", "Sub");

									tasksList.add(taskObj);
								}
							}

						} else {
							if (object[2].toString().equals("Active")) {
								totalActiveTasks++;
								Date legalDueDate = sdfIn.parse(object[7].toString());
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
									taskObj.put("task_sub_id", object[27].toString());
									taskObj.put("sub_frequency", object[23].toString());
									taskObj.put("equipment_number", object[28].toString());
									taskObj.put("equipment_type", object[29].toString());
									taskObj.put("task_type", "Sub");

									tasksList.add(taskObj);
								} else {
									if (currentDate.after(prdueDate)) {
										if (currentDate.before(legalDueDate) || currentDate.equals(legalDueDate)) {
											posingrisk++;

											JSONObject taskObj = new JSONObject();

											taskObj.put("date", sdfOut.format(legalDueDate));
											taskObj.put("status", "posingrisk");
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
											taskObj.put("task_executor",
													object[25].toString() + " " + object[26].toString());
											taskObj.put("task_sub_id", object[27].toString());
											taskObj.put("sub_frequency", object[23].toString());
											taskObj.put("equipment_number", object[28].toString());
											taskObj.put("equipment_type", object[29].toString());
											taskObj.put("task_type", "Sub");

											tasksList.add(taskObj);

										}
									} else {
										if (prdueDate.after(currentDate) || prdueDate.equals(currentDate))
											pending++;
									}
								}

							} // Check Active END

							if (object[2].toString().equals("Partially_Completed")) {
								partially_Completed++;
								Date legalDueDate = sdfIn.parse(object[7].toString());
								JSONObject taskObj = new JSONObject();
								taskObj.put("date", sdfOut.format(legalDueDate));
								taskObj.put("status", "waitingforapproval");
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
								taskObj.put("task_sub_id", object[27].toString());
								taskObj.put("sub_frequency", object[23].toString());
								taskObj.put("equipment_number", object[28].toString());
								taskObj.put("equipment_type", object[29].toString());
								taskObj.put("task_type", "Sub");

								tasksList.add(taskObj);

							}

							if (object[2].toString().equals("Re_Opened")) {
								re_opened++;
								Date legalDueDate = sdfIn.parse(object[7].toString());
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
								taskObj.put("ttrn_legal_due_date",
										sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
								taskObj.put("task_legi_name", object[15]);
								taskObj.put("task_rule_name", object[16]);
								taskObj.put("task_reference", object[17]);
								taskObj.put("task_who", object[18]);
								taskObj.put("task_when", object[19]);
								taskObj.put("task_activity", object[20]);
								taskObj.put("task_executor", object[25].toString() + " " + object[26].toString());
								taskObj.put("task_sub_id", object[27].toString());
								taskObj.put("sub_frequency", object[23].toString());
								taskObj.put("equipment_number", object[28].toString());
								taskObj.put("equipment_type", object[29].toString());
								taskObj.put("task_type", "Sub");

								tasksList.add(taskObj);
							}

						}
					}
				}
			} // end of if condition of sub tasks search

			objForSend.put("Complied", complied);
			objForSend.put("NonComplied", noncomplied);
			objForSend.put("PosingRisk", posingrisk);
			objForSend.put("delayed_reported", delayedReported);
			objForSend.put("Delayed", delayed);
			objForSend.put("totaltasksinloop", totalTasksInLoop);
			objForSend.put("totalactivetasks", totalActiveTasks);
			objForSend.put("totalcompletedtasks", totalCompletedTasks);
			objForSend.put("Pending", pending);
			objForSend.put("WaitingForApproval", partially_Completed);
			objForSend.put("ReOpened", re_opened);
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
	public String searchGraph(String jsonString, HttpSession session) {
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
			List<Object> allTask = dashboardDao.searchGraph(jsonString,
					Integer.parseInt(session.getAttribute("sess_user_id").toString()),
					Integer.parseInt(session.getAttribute("sess_role_id").toString()));
			// List<Object> allTask = dashboardDao.getOverallComplianceGraph(8,5);
			Iterator<Object> itr = allTask.iterator();
			objForSend.put("totalTasks", allTask.size());
			Date currentDate = sdfIn.parse(sdfOut.format(new Date()));
			while (itr.hasNext()) {

				totalTasksInLoop++;
				Object[] object = (Object[]) itr.next();
				if (object[7] != null) {
					if (object[2].toString().equals("Completed")) {

						totalCompletedTasks++;
						Date legalDueDate = sdfIn.parse(object[7].toString());
						Date submittedDate = sdfIn.parse(object[8].toString());
						Date CompletedDate = sdfIn.parse(object[28].toString());
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
							taskObj.put("loca_type", object[27]);
							taskObj.put("task_loca_category", object[27]);
							taskObj.put("ttrn_frequency_for_operation", object[23].toString());
							/*
							 * if(object[29]!=null) taskObj.put("comments", object[29].toString()); else
							 * taskObj.put("comments", "-");
							 * 
							 * if(object[0]!=null){ List<UploadedDocuments> attachedDocuments =
							 * uploadedDocumentsDao.getAllDocumentByTtrnId(Integer.parseInt(object[0].
							 * toString()));
							 * 
							 * if(attachedDocuments != null){ taskObj.put("document_attached", 1);
							 * 
							 * Iterator<UploadedDocuments> itre = attachedDocuments.iterator(); JSONArray
							 * docArray = new JSONArray();
							 * 
							 * while (itre.hasNext()) { UploadedDocuments uploadedDocuments =
							 * (UploadedDocuments) itre.next(); JSONObject docObj = new JSONObject();
							 * docObj.put("udoc_id", uploadedDocuments.getUdoc_id());
							 * docObj.put("udoc_original_file_name",
							 * uploadedDocuments.getUdoc_original_file_name()); docArray.add(docObj); }
							 * taskObj.put("document_list", docArray); } else{ taskObj.put("document_list",
							 * new JSONArray()); taskObj.put("document_attached", 0); }
							 * 
							 * }else{ taskObj.put("document_list", new JSONArray());
							 * taskObj.put("document_attached", 0); }
							 */

							taskObj.put("task_type", "Main");

							tasksList.add(taskObj);
						} else {
							/*
							 * if(submittedDate.after(legalDueDate)){ delayed++; JSONObject taskObj = new
							 * JSONObject(); taskObj.put("date", sdfOut.format(legalDueDate));
							 * taskObj.put("status", "delayed"); taskObj.put("orga_id", object[9]);
							 * taskObj.put("orga_name", object[10]); taskObj.put("loca_id", object[11]);
							 * taskObj.put("loca_name", object[12]); taskObj.put("dept_id", object[13]);
							 * taskObj.put("dept_name", object[14]); taskObj.put("tsk_impact", object[22]);
							 * //Task Details taskObj.put("ttrn_client_task_id", object[1]);
							 * taskObj.put("ttrn_legal_due_date",
							 * sdfOutDisplay.format(sdfIn.parse(object[7].toString())));
							 * taskObj.put("task_legi_name", object[15]); taskObj.put("task_rule_name",
							 * object[16]); taskObj.put("task_reference", object[17]);
							 * taskObj.put("task_who", object[18]); taskObj.put("task_when", object[19]);
							 * taskObj.put("task_activity", object[20]); taskObj.put("task_executor",
							 * object[25].toString()+" "+object[26].toString()); taskObj.put("task_type",
							 * "Main"); tasksList.add(taskObj); }
							 */

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
								taskObj.put("loca_type", object[27]);
								taskObj.put("task_loca_category", object[27]);
								taskObj.put("ttrn_frequency_for_operation", object[23].toString());

								/*
								 * if(object[29]!=null) taskObj.put("comments", object[29].toString()); else
								 * taskObj.put("comments", "-");
								 * 
								 * if(object[0]!=null){ List<UploadedDocuments> attachedDocuments =
								 * uploadedDocumentsDao.getAllDocumentByTtrnId(Integer.parseInt(object[0].
								 * toString()));
								 * 
								 * if(attachedDocuments != null){ taskObj.put("document_attached", 1);
								 * 
								 * Iterator<UploadedDocuments> itre = attachedDocuments.iterator(); JSONArray
								 * docArray = new JSONArray();
								 * 
								 * while (itre.hasNext()) { UploadedDocuments uploadedDocuments =
								 * (UploadedDocuments) itre.next(); JSONObject docObj = new JSONObject();
								 * docObj.put("udoc_id", uploadedDocuments.getUdoc_id());
								 * docObj.put("udoc_original_file_name",
								 * uploadedDocuments.getUdoc_original_file_name()); docArray.add(docObj); }
								 * taskObj.put("document_list", docArray); } else{ taskObj.put("document_list",
								 * new JSONArray()); taskObj.put("document_attached", 0); }
								 * 
								 * }else{ taskObj.put("document_list", new JSONArray());
								 * taskObj.put("document_attached", 0); }
								 */
								taskObj.put("task_type", "Main");
								tasksList.add(taskObj);

							}
							// Delayed reported task
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
								taskObj.put("loca_type", object[27]);
								taskObj.put("task_loca_category", object[27]);
								taskObj.put("task_type", "Main");
								taskObj.put("ttrn_frequency_for_operation", object[23].toString());
								/*
								 * if(object[29]!=null) taskObj.put("comments", object[29].toString()); else
								 * taskObj.put("comments", "-");
								 * 
								 * if(object[0]!=null){ List<UploadedDocuments> attachedDocuments =
								 * uploadedDocumentsDao.getAllDocumentByTtrnId(Integer.parseInt(object[0].
								 * toString()));
								 * 
								 * if(attachedDocuments != null){ taskObj.put("document_attached", 1);
								 * 
								 * Iterator<UploadedDocuments> itre = attachedDocuments.iterator(); JSONArray
								 * docArray = new JSONArray();
								 * 
								 * while (itre.hasNext()) { UploadedDocuments uploadedDocuments =
								 * (UploadedDocuments) itre.next(); JSONObject docObj = new JSONObject();
								 * docObj.put("udoc_id", uploadedDocuments.getUdoc_id());
								 * docObj.put("udoc_original_file_name",
								 * uploadedDocuments.getUdoc_original_file_name()); docArray.add(docObj); }
								 * taskObj.put("document_list", docArray); } else{ taskObj.put("document_list",
								 * new JSONArray()); taskObj.put("document_attached", 0); }
								 * 
								 * }else{ taskObj.put("document_list", new JSONArray());
								 * taskObj.put("document_attached", 0); }
								 */

								tasksList.add(taskObj);
							}

						}

					} else {
						if (object[2].toString().equals("Active")) {
							totalActiveTasks++;
							Date legalDueDate = sdfIn.parse(object[7].toString());
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
								taskObj.put("ttrn_uh_due_date",
										sdfOutDisplay.format(sdfIn.parse(object[6].toString())));
								taskObj.put("loca_type", object[27]);
								taskObj.put("task_loca_category", object[27]);
								taskObj.put("ttrn_frequency_for_operation", object[23].toString());
								/*
								 * if(object[29]!=null) taskObj.put("comments", object[29].toString()); else
								 * taskObj.put("comments", "-");
								 */
								tasksList.add(taskObj);
							} else {
								if (currentDate.after(prdueDate)) {
									if (currentDate.before(legalDueDate) || currentDate.equals(legalDueDate)) {
										posingrisk++;

										JSONObject taskObj = new JSONObject();

										taskObj.put("date", sdfOut.format(legalDueDate));
										taskObj.put("status", "posingrisk");
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
										taskObj.put("task_executor",
												object[25].toString() + " " + object[26].toString());
										taskObj.put("task_type", "Main");
										taskObj.put("ttrn_pr_due_date",
												sdfOutDisplay.format(sdfIn.parse(object[3].toString())));
										taskObj.put("ttrn_rw_due_date",
												sdfOutDisplay.format(sdfIn.parse(object[4].toString())));
										taskObj.put("ttrn_fh_due_date",
												sdfOutDisplay.format(sdfIn.parse(object[5].toString())));
										taskObj.put("ttrn_uh_due_date",
												sdfOutDisplay.format(sdfIn.parse(object[6].toString())));
										taskObj.put("loca_type", object[27]);
										taskObj.put("task_loca_category", object[27]);
										taskObj.put("ttrn_frequency_for_operation", object[23].toString());
										/*
										 * if(object[29]!=null) taskObj.put("comments", object[29].toString()); else
										 * taskObj.put("comments", "-");
										 */
										tasksList.add(taskObj);

									}
								} else {
									if (prdueDate.after(currentDate) || prdueDate.equals(currentDate))
										pending++;
								}
							}

						} // Check Active END

						if (object[2].toString().equals("Partially_Completed")) {
							partially_Completed++;
							Date legalDueDate = sdfIn.parse(object[7].toString());
							JSONObject taskObj = new JSONObject();
							taskObj.put("orga_id", object[9]);
							taskObj.put("orga_name", object[10]);
							taskObj.put("loca_id", object[11]);
							taskObj.put("loca_name", object[12]);
							taskObj.put("dept_id", object[13]);
							taskObj.put("dept_name", object[14]);
							taskObj.put("tsk_impact", object[22]);
							taskObj.put("status", "watingforapproval");
							taskObj.put("date", sdfOut.format(legalDueDate));
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
							taskObj.put("ttrn_uh_due_date", sdfOutDisplay.format(sdfIn.parse(object[6].toString())));
							taskObj.put("loca_type", object[27]);
							taskObj.put("task_loca_category", object[27]);
							taskObj.put("ttrn_frequency_for_operation", object[23].toString());
							/*
							 * if(object[29]!=null) taskObj.put("comments", object[29].toString()); else
							 * taskObj.put("comments", "-");
							 * 
							 * 
							 * if(object[0]!=null){ List<UploadedDocuments> attachedDocuments =
							 * uploadedDocumentsDao.getAllDocumentByTtrnId(Integer.parseInt(object[0].
							 * toString()));
							 * 
							 * if(attachedDocuments != null){ taskObj.put("document_attached", 1);
							 * 
							 * Iterator<UploadedDocuments> itre = attachedDocuments.iterator(); JSONArray
							 * docArray = new JSONArray();
							 * 
							 * while (itre.hasNext()) { UploadedDocuments uploadedDocuments =
							 * (UploadedDocuments) itre.next(); JSONObject docObj = new JSONObject();
							 * docObj.put("udoc_id", uploadedDocuments.getUdoc_id());
							 * docObj.put("udoc_original_file_name",
							 * uploadedDocuments.getUdoc_original_file_name()); docArray.add(docObj); }
							 * taskObj.put("document_list", docArray); } else{ taskObj.put("document_list",
							 * new JSONArray()); taskObj.put("document_attached", 0); }
							 * 
							 * }else{ taskObj.put("document_list", new JSONArray());
							 * taskObj.put("document_attached", 0); }
							 */
							tasksList.add(taskObj);

						}

						if (object[2].toString().equals("Re_Opened")) {
							re_opened++;
							Date legalDueDate = sdfIn.parse(object[7].toString());
							JSONObject taskObj = new JSONObject();
							taskObj.put("orga_id", object[9]);
							taskObj.put("orga_name", object[10]);
							taskObj.put("loca_id", object[11]);
							taskObj.put("loca_name", object[12]);
							taskObj.put("dept_id", object[13]);
							taskObj.put("dept_name", object[14]);
							taskObj.put("tsk_impact", object[22]);
							taskObj.put("status", "reopen");
							taskObj.put("date", sdfOut.format(legalDueDate));
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
							taskObj.put("ttrn_uh_due_date", sdfOutDisplay.format(sdfIn.parse(object[6].toString())));
							taskObj.put("loca_type", object[27]);
							taskObj.put("task_loca_category", object[27]);
							taskObj.put("ttrn_frequency_for_operation", object[23].toString());
							/*
							 * if(object[29]!=null) taskObj.put("comments", object[29].toString()); else
							 * taskObj.put("comments", "-");
							 */
							tasksList.add(taskObj);
						}

					}
				}

			}

			// Sub Task code start
			List<Object> allSubTask = dashboardDao.getOverallComplianceGraph(
					Integer.parseInt(session.getAttribute("sess_user_id").toString()),
					Integer.parseInt(session.getAttribute("sess_role_id").toString()), jsonString);

			Iterator<Object> itrSub = allSubTask.iterator();
			objForSend.put("totalTasks", allSubTask.size());
			// Date currentDate = sdfIn.parse(sdfOut.format(new Date()));
			while (itrSub.hasNext()) {

				totalTasksInLoop++;
				Object[] object = (Object[]) itrSub.next();
				if (object[7] != null) {
					if (object[2].toString().equals("Completed")) {

						totalCompletedTasks++;
						Date legalDueDate = sdfIn.parse(object[7].toString());
						Date submittedDate = sdfIn.parse(object[8].toString());
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
							taskObj.put("task_type", "Sub");

							taskObj.put("task_sub_id", object[28].toString());

							tasksList.add(taskObj);
						} else {
							if (submittedDate.after(legalDueDate)) {
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
								taskObj.put("task_sub_id", object[28].toString());
								taskObj.put("task_type", "Sub");

								tasksList.add(taskObj);
							}
						}

					} else {
						if (object[2].toString().equals("Active")) {
							totalActiveTasks++;
							Date legalDueDate = sdfIn.parse(object[7].toString());
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
								taskObj.put("task_sub_id", object[28].toString());
								taskObj.put("task_type", "Sub");

								tasksList.add(taskObj);
							} else {
								if (currentDate.after(prdueDate)) {
									if (currentDate.before(legalDueDate) || currentDate.equals(legalDueDate)) {
										posingrisk++;

										JSONObject taskObj = new JSONObject();

										taskObj.put("date", sdfOut.format(legalDueDate));
										taskObj.put("status", "posingrisk");
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
										taskObj.put("task_executor",
												object[25].toString() + " " + object[26].toString());
										taskObj.put("task_sub_id", object[28].toString());
										taskObj.put("task_type", "Sub");

										tasksList.add(taskObj);

									}
								} else {
									if (prdueDate.after(currentDate) || prdueDate.equals(currentDate))
										pending++;
								}
							}

						} // Check Active END

					}
				}

			}

			objForSend.put("Complied", complied);
			objForSend.put("NonComplied", noncomplied);
			objForSend.put("PosingRisk", posingrisk);
			objForSend.put("Delayed", delayed);
			objForSend.put("totaltasksinloop", totalTasksInLoop);
			objForSend.put("totalactivetasks", totalActiveTasks);
			objForSend.put("totalcompletedtasks", totalCompletedTasks);
			objForSend.put("Pending", pending);
			objForSend.put("WaitingForApproval", partially_Completed);
			objForSend.put("ReOpened", re_opened);
			objForSend.put("delayed_reported", delayed_reported);
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
	public String getExportDrillReport(String jsonString, HttpSession session) {
		JSONArray dataForSend = new JSONArray();
		JSONObject sendList = new JSONObject();
		try {

			SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdfOut = new SimpleDateFormat("dd-MM-yyyy");

			File dir1 = new File(
					"C:" + File.separator + "CMS" + File.separator + projectName + File.separator + "Report");

			if (!dir1.exists())
				dir1.mkdirs();

			DateFormat df = new SimpleDateFormat("dd-MM-yy_HH-mm-ss");
			Date dateobj = new Date();
			String test = df.format(dateobj);

			Object generatedExcelFile = "Drill_Report" + test + ".csv";
			File newExcelFile = new File(dir1.getPath() + File.separator + generatedExcelFile);

			if (!newExcelFile.exists()) {
				newExcelFile.createNewFile();
			}

			int rownum = 1;

			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Litigation List");
			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell(0).setCellValue("Client Task ID");
			rowhead.createCell(1).setCellValue("Entity");
			rowhead.createCell(2).setCellValue("Unit");
			rowhead.createCell(3).setCellValue("Function");
			rowhead.createCell(4).setCellValue("Legislation");
			rowhead.createCell(5).setCellValue("Rule");
			rowhead.createCell(6).setCellValue("Reference");
			rowhead.createCell(7).setCellValue("Who");
			rowhead.createCell(8).setCellValue("When");
			rowhead.createCell(9).setCellValue("Activity");
			rowhead.createCell(10).setCellValue("Impact");
			rowhead.createCell(11).setCellValue("Executor");
			rowhead.createCell(12).setCellValue("Legal Due Date");

			List<Object> allTasks = dashboardDao.getOverallComplianceGrapheExportReport(
					Integer.parseInt(session.getAttribute("sess_user_id").toString()),
					Integer.parseInt(session.getAttribute("sess_role_id").toString()), jsonString);
			Iterator<Object> iterator = allTasks.iterator();
			FileOutputStream fileOut = null;
			while (iterator.hasNext()) {
				Object[] object = (Object[]) iterator.next();
				HSSFRow row = sheet.createRow(rownum);
				row.createCell(0).setCellValue(object[1].toString());
				row.createCell(1).setCellValue(object[10].toString());
				row.createCell(2).setCellValue(object[12].toString());
				row.createCell(3).setCellValue(object[14].toString());
				row.createCell(4).setCellValue(object[15].toString());
				row.createCell(5).setCellValue(object[16].toString());
				row.createCell(6).setCellValue(object[17].toString());
				row.createCell(7).setCellValue(object[18].toString());
				row.createCell(8).setCellValue(object[19].toString());
				row.createCell(9).setCellValue(object[20].toString());
				row.createCell(10).setCellValue(object[22].toString());
				row.createCell(11).setCellValue(object[25].toString() + " " + object[26].toString());
				row.createCell(12).setCellValue(sdfOutDisplay.format(sdfIn.parse(object[7].toString())));

				fileOut = new FileOutputStream(newExcelFile);
				workbook.write(fileOut);
				rownum++;
				JSONObject objForAppend = new JSONObject();
				dataForSend.add(fileOut);
				dataForSend.add(objForAppend);
			}

			sendList.put("message", "success");
			return sendList.toJSONString();
			// return dataForSend.toJSONString();

		} catch (Exception e) {
			JSONObject objForAppend = new JSONObject();
			dataForSend.add(objForAppend);
			e.printStackTrace();
			// return dataForSend.toJSONString();
			sendList.put("message", "error");
			return sendList.toJSONString();
		}

	}

	@Override
	public String approveAllTask(String jsonString, HttpSession session) {
		try {

			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			JSONArray configured_tasks = (JSONArray) jsonObj.get("tasks_list");
			System.out.println("configured_tasks.size():" + configured_tasks.size());
			for (int i = 0; i < configured_tasks.size(); i++) {
				JSONObject configured_tasks_obj = (JSONObject) configured_tasks.get(i);
				int ttrn_id = Integer.parseInt(configured_tasks_obj.get("ttrn_id").toString());
				TaskTransactional taskTransactional = taskConfigurationDao.getTasksForCompletion(ttrn_id);

				int noOfBackDaysAllowed = taskTransactional.getTtrn_no_of_back_days_allowed();

				if (noOfBackDaysAllowed > 0) {
					Date currentDate = new Date();
					if (currentDate.after(taskTransactional.getTtrn_legal_due_date())) {
						long diff = currentDate.getTime() - taskTransactional.getTtrn_legal_due_date().getTime();
						int differenceDays = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
						if ((noOfBackDaysAllowed - differenceDays) >= 0) {
							taskTransactional.setTtrn_submitted_date(taskTransactional.getTtrn_submitted_date());
						} else {
							taskTransactional.setTtrn_submitted_date(new Date());
						}
					}

				} else {
					taskTransactional.setTtrn_submitted_date(new Date());
				}
				taskTransactional.setTtrn_status("Completed");
				taskTransactional.setTtrn_task_approved_date(new Date());
				taskTransactional
						.setTtrn_task_approved_by(Integer.parseInt(session.getAttribute("sess_user_id").toString()));

				taskConfigurationDao.updateTaskConfiguration(taskTransactional);
			}
			JSONObject objForSend = new JSONObject();
			objForSend.put("responseMessage", "Success");
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject objForSend = new JSONObject();
			objForSend.put("responseMessage", "Failed");

			return objForSend.toJSONString();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getSubTaskHistoryList(String jsonString, HttpSession session) {
		JSONObject objForSend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			Date currentDate = sdfIn.parse(sdfOut.format(new Date()));

			JSONArray dataForAppend = new JSONArray();
			String ttrn_sub_task_id = jsonObj.get("ttrn_sub_task_id").toString();
			JSONArray subTaskdataForAppend = new JSONArray();
			List<SubTaskTranscational> subTaskList = dashboardDao.getSubTaskHitoryByclientTaskID(ttrn_sub_task_id);
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
					objForAppend.put("ttrn_pr_due_date", sdfOutDisplay.format(performerDueDate));
					objForAppend.put("ttrn_rw_due_date", sdfOutDisplay
							.format(sdfIn.parse(subTaskTranscational.getTtrn_sub_task_rw_date().toString())));
					objForAppend.put("ttrn_fh_due_date", sdfOutDisplay
							.format(sdfIn.parse(subTaskTranscational.getTtrn_sub_task_FH_due_date().toString())));
					objForAppend.put("ttrn_uh_due_date", sdfOutDisplay
							.format(sdfIn.parse(subTaskTranscational.getTtrn_sub_task_UH_due_date().toString())));
					objForAppend.put("ttrn_legal_due_date", sdfOutDisplay.format(legalDueDate));
					if (subTaskTranscational.getTtrn_sub_task_compl_date() != null)
						objForAppend.put("ttrn_completed_date", sdfOutDisplay
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
					objForAppend.put("client_task_id", ttrn_sub_task_id);
					subTaskdataForAppend.add(objForAppend);

				}
			}

			objForSend.put("subTaskHistory", subTaskdataForAppend);

			return objForSend.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("repsonseMessage", "Failed");
			return objForSend.toJSONString();
		}
	}

	@Override
	public String importTaskToComplete(MultipartFile task_list, String jsonString, HttpSession session) {
		String user_name = session.getAttribute("sess_user_full_name").toString();
		JSONObject objForSend = new JSONObject();

		int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			ArrayList<TaskTransactional> taskTransactionals = new ArrayList<>();
			Date currentDate = new Date();
			if (!task_list.isEmpty()) {
				JSONArray neglectedTask = new JSONArray();
				JSONArray addedTask = new JSONArray();

				String name = jsonObj.get("name").toString();
				byte[] bytes = task_list.getBytes();

				File temp = File.createTempFile(task_list.getName(), ".csv");

				int i = 0;
				String absolutePath = temp.getAbsolutePath();
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(absolutePath));
				stream.write(bytes);
				stream.close();
				CsvReader taskUpdates = new CsvReader(absolutePath);
				taskUpdates.readHeaders();

				while (taskUpdates.readRecord()) {
					i++;
					// List<Department> deptList =
					// functionDao.checkNameIfExist(deptUpdates.get("dept_name"));
					// System.out.println("ttrnID = " +taskUpdates.get("Ttrn ID"));

					if (!taskUpdates.get("Comments").equals("")) {
						TaskTransactional taskTransactional = tasksconfigurationdao
								.getTasksForCompletion(Integer.parseInt(taskUpdates.get("Ttrn ID")));
						// Date ttrn_completed_date = sdfOutDisplay.parse(taskUpdates.get("Completion
						// Date").toString());
						if (taskTransactional.getTtrn_status().equals("Active")
								|| taskTransactional.getTtrn_status().equals("Re_Opened"))

							taskTransactional.setTtrn_completed_date(new Date());
						taskTransactional.setTtrn_performer_comments(taskUpdates.get("Comments"));

						if (!taskUpdates.get("Reason For Non-Compliance").equals("")) {
							taskTransactional
									.setTtrn_reason_for_non_compliance(taskUpdates.get("Reason For Non-Compliance"));
						} else {
							taskTransactional.setTtrn_reason_for_non_compliance(" ");
						}

						int noOfBackDaysAllowed = taskTransactional.getTtrn_no_of_back_days_allowed();
						if (noOfBackDaysAllowed > 0) {
							// Date currentDate = new Date();
							if (currentDate.after(taskTransactional.getTtrn_legal_due_date())) {
								long diff = currentDate.getTime()
										- taskTransactional.getTtrn_legal_due_date().getTime();
								int differenceDays = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

								if ((noOfBackDaysAllowed - differenceDays) >= 0) {
									taskTransactional.setTtrn_submitted_date(new Date());
								} else {
									taskTransactional.setTtrn_submitted_date(new Date());
								}

							} else {
								taskTransactional.setTtrn_submitted_date(new Date());
							}

						} else {
							taskTransactional.setTtrn_submitted_date(new Date());
						}

						if (taskUpdates.get("Event_Not_Occured").equals("Yes")) {
							taskTransactional.setTtrn_status("Event_Not_Occured");
							// taskTransactional.setTtrn_completed_date(ttrn_completed_date);
							taskTransactional.setTtrn_performer_comments("");
							taskTransactional.setTtrn_reason_for_non_compliance("");

						} else {

							if (taskTransactional.getTtrn_allow_approver_reopening() != null
									&& Integer.parseInt(taskTransactional.getTtrn_allow_approver_reopening()) == 1) {
								taskTransactional.setTtrn_status("Partially_Completed");
							} else {
								taskTransactional.setTtrn_status("Completed");
							}

						}

						// taskTransactional.setTtrn_status("Completed");
						taskTransactional.setTtrn_task_completed_by(user_id);// Change this to session user id
						taskTransactionals.add(taskTransactional);
					}
				}

				tasksconfigurationdao.saveTaskCompletion(taskTransactionals);
				objForSend.put("responseMessage", "Success");
				return objForSend.toJSONString();

			}
		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("responseMessage", "Failed");
			return objForSend.toJSONString();
		}
		return null;

	}

	/*
	 * public String updateTasksCompletion(ArrayList<MultipartFile>
	 * ttrn_proof_of_compliance, String jsonString, HttpSession session) {
	 * 
	 * JSONObject objForSend = new JSONObject(); String
	 * ttrn_reason_for_non_compliance = ""; try {
	 * 
	 * JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString); String
	 * ttrn_performer_comments = jsonObj.get("ttrn_performer_comments").toString();
	 * Date ttrn_completed_date =
	 * sdfOutDisplay.parse(jsonObj.get("ttrn_completed_date").toString());
	 * 
	 * if (jsonObj.get("ttrn_reason_for_non_compliance") == null) {
	 * ttrn_reason_for_non_compliance = ""; } else { ttrn_reason_for_non_compliance
	 * = jsonObj.get("ttrn_reason_for_non_compliance").toString(); } String
	 * ttrn_event_not_occure = jsonObj.get("ttrn_event_not_occure").toString(); int
	 * user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
	 * int user_role =
	 * Integer.parseInt(session.getAttribute("sess_role_id").toString()); int
	 * mail_send_flag = 0;
	 * 
	 * ArrayList<TaskTransactional> taskTransactionals = new ArrayList<>();
	 * 
	 * JSONArray arrayToIterate = (JSONArray) jsonObj.get("ttrn_ids"); for (int i =
	 * 0; i < arrayToIterate.size(); i++) {
	 * 
	 * JSONObject configured_tasks_obj = (JSONObject) arrayToIterate.get(i); int
	 * ttrn_id = Integer.parseInt(configured_tasks_obj.get("ttrn_id").toString());
	 * 
	 * String originalFileName = null; String generatedFileName = null; // int
	 * lastGeneratedValue = //
	 * uploadedDocumentsDao.getLastGeneratedValueByTtrnId(ttrn_id);
	 * UploadedDocuments documents = uploadedDocumentsDao.getDocumentById(ttrn_id);
	 * 
	 * for (int i1 = 0; i1 < ttrn_proof_of_compliance.size(); i1++) { MultipartFile
	 * file1 = ttrn_proof_of_compliance.get(i1); if (file1.getSize() > 0) {
	 * 
	 * Date cur_date = new Date();
	 * 
	 * Calendar cal = Calendar.getInstance(); cal.setTime(cur_date); int year =
	 * cal.get(Calendar.YEAR); int month = cal.get(Calendar.MONTH); int day =
	 * cal.get(Calendar.DAY_OF_MONTH); String dir_month_name = day + "-" + (month +
	 * 1) + "-" + year;
	 * 
	 * File dir = new File("C:" + File.separator + "CMS" + File.separator +
	 * "documents" + File.separator + "proofOfCompliance" + File.separator +
	 * projectName + File.separator + dir_month_name); if (!dir.exists())
	 * dir.mkdirs();
	 * 
	 * // lastGeneratedValue++; originalFileName = file1.getOriginalFilename();
	 * generatedFileName = "uploadedProof" + ttrn_id + "_" +
	 * file1.getOriginalFilename().split("\\.")[1]; File newFile = new
	 * File(dir.getPath() + File.separator + generatedFileName); if
	 * (!newFile.exists()) { newFile.createNewFile(); }
	 * 
	 * OutputStream outputStream = new FileOutputStream(newFile);
	 * 
	 * outputStream.write(file1.getBytes());
	 * 
	 * String algo = "DES/ECB/PKCS5Padding"; utilitiesService.encrypt(algo,
	 * newFile.getPath());
	 * 
	 * // UploadedDocuments documents = new UploadedDocuments();
	 * documents.setUdoc_ttrn_id(ttrn_id);
	 * documents.setUdoc_original_file_name(originalFileName);
	 * documents.setUdoc_filename(dir + File.separator + generatedFileName);
	 * documents.setUdoc_last_generated_value_for_filename_for_ttrn_id(
	 * documents.getUdoc_last_generated_value_for_filename_for_ttrn_id());
	 * uploadedDocumentsDao.updateDocuments(documents);
	 * 
	 * outputStream.flush(); outputStream.close();
	 * 
	 * Path path = Paths.get(dir + File.separator + generatedFileName); try {
	 * Files.delete(path); } catch (IOException e) { // deleting file failed
	 * e.printStackTrace(); System.out.println(e.getMessage()); } }
	 * 
	 * }
	 * 
	 * TaskTransactional taskTransactional =
	 * taskConfigurationDao.getTasksForCompletion(ttrn_id);
	 * 
	 * if (taskTransactional.getTtrn_status().equals("Active") ||
	 * taskTransactional.getTtrn_status().equals("Re_Opened")) mail_send_flag = 1;
	 * 
	 * taskTransactional.setTtrn_completed_date(ttrn_completed_date);
	 * taskTransactional.setTtrn_performer_comments(ttrn_performer_comments); if
	 * (!ttrn_reason_for_non_compliance.equals("")) {
	 * taskTransactional.setTtrn_reason_for_non_compliance(
	 * ttrn_reason_for_non_compliance); } int noOfBackDaysAllowed =
	 * taskTransactional.getTtrn_no_of_back_days_allowed(); if (noOfBackDaysAllowed
	 * > 0) { Date currentDate = new Date(); if
	 * (currentDate.after(taskTransactional.getTtrn_legal_due_date())) { long diff =
	 * currentDate.getTime() - taskTransactional.getTtrn_legal_due_date().getTime();
	 * int differenceDays = (int) TimeUnit.DAYS.convert(diff,
	 * TimeUnit.MILLISECONDS);
	 * 
	 * if ((noOfBackDaysAllowed - differenceDays) >= 0) {
	 * taskTransactional.setTtrn_submitted_date(ttrn_completed_date); } else {
	 * taskTransactional.setTtrn_submitted_date(new Date()); }
	 * 
	 * } else { taskTransactional.setTtrn_submitted_date(new Date()); }
	 * 
	 * } else { taskTransactional.setTtrn_submitted_date(new Date()); }
	 * 
	 * if (ttrn_event_not_occure.equals("Yes")) {
	 * taskTransactional.setTtrn_status("Event_Not_Occured"); //
	 * taskTransactional.setTtrn_completed_date(ttrn_completed_date);
	 * taskTransactional.setTtrn_performer_comments("");
	 * taskTransactional.setTtrn_reason_for_non_compliance("");
	 * 
	 * } else { System.out.println(" ROLE " + user_role + " Aprover or not " +
	 * taskTransactional.getTtrn_allow_approver_reopening()); if
	 * (taskTransactional.getTtrn_allow_approver_reopening() != null &&
	 * Integer.parseInt(taskTransactional.getTtrn_allow_approver_reopening()) == 1)
	 * { taskTransactional.setTtrn_status("Partially_Completed"); } else {
	 * taskTransactional.setTtrn_status("Completed"); }
	 * 
	 * }
	 * 
	 * // taskTransactional.setTtrn_status("Completed");
	 * taskTransactional.setTtrn_task_completed_by(user_id);// Change this to
	 * session user id
	 * 
	 * taskTransactionals.add(taskTransactional);
	 * 
	 * } taskConfigurationDao.saveTaskCompletion(taskTransactionals); if
	 * (mail_send_flag == 1) // IF status is active then only mail send while
	 * updating task mail mail should // not be sent
	 * utilitiesService.sendTaskCompletionMailToEvaluator(taskTransactionals,
	 * "MainTask");// Task completion // mail to evaluator
	 * 
	 * objForSend.put("responseMessage", "Success"); return
	 * objForSend.toJSONString(); } catch (Exception e) { e.printStackTrace();
	 * objForSend.put("responseMessage", "Failed"); return
	 * objForSend.toJSONString(); } }
	 */

}
