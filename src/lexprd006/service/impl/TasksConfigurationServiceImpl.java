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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lexprd006.dao.CommonLogsDao;
import lexprd006.dao.TasksConfigurationDao;
import lexprd006.dao.TasksDao;
import lexprd006.dao.UploadedDocumentsDao;
import lexprd006.dao.UsersDao;
import lexprd006.domain.DefaultTaskConfiguration;
import lexprd006.domain.LogActivateDeActivateTasks;
import lexprd006.domain.LogTasksConfiguration;
import lexprd006.domain.SubTaskTranscational;
import lexprd006.domain.Task;
import lexprd006.domain.TaskTransactional;
import lexprd006.domain.UploadedDocuments;
import lexprd006.domain.UploadedSubTaskDocuments;
import lexprd006.domain.User;
import lexprd006.service.SchedularService;
import lexprd006.service.TasksConfigurationService;
import lexprd006.service.UtilitiesService;

@Service(value = "tasksconfigurationservice")
public class TasksConfigurationServiceImpl implements TasksConfigurationService {

	public final SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public final SimpleDateFormat sdfOut = new SimpleDateFormat("dd-MM-yyyy");

	@Autowired
	TasksConfigurationDao tasksconfigurationdao;

	@Autowired
	UploadedDocumentsDao uploadedDocumentsDao;

	@Autowired
	UtilitiesService utilitiesService;

	@Autowired
	TasksDao tasksDao;

	@Autowired
	SchedularService schedularService;

	@Autowired
	CommonLogsDao cLogsDao;

	@Autowired
	UsersDao userDao;

	private @Value("#{config['mail_user_name'] ?: 'null'}") String username;
	private @Value("#{config['mail_password'] ?: 'null'}") String password;
	private @Value("#{config['mail_smtp_host'] ?: 'null'}") String hostName;
	private @Value("#{config['mail_smtp_port'] ?: 'null'}") String portNo;
	private @Value("#{config['mail_from'] ?: 'null'}") String mailFrom;
	private @Value("#{config['project_name'] ?: 'null'}") String projectName;
	private @Value("#{config['project_url'] ?: 'null'}") String url;

	// Method Created By: Mahesh Kharote(10/01/2016)
	// Method Purpose: Save Tasks User Mapping
	@SuppressWarnings("unchecked")
	@Override
	public String savetasksconfiguration(String jsonString, HttpSession session) {
		JSONArray dataForSend = new JSONArray();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			Date ttrn_legal_due_date = sdfOut.parse(jsonObj.get("ttrn_legal_due_date").toString());
			Date ttrn_fh_due_date = sdfOut.parse(jsonObj.get("ttrn_fh_due_date").toString());

			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			// Parsing to date from
			// specified input date
			// format which is
			// dd-MM-yyyy and
			// parsing it to Date
			// type of java
			Date ttrn_pr_due_date = sdfOut.parse(jsonObj.get("ttrn_pr_due_date").toString());
			Date ttrn_rw_due_date = sdfOut.parse(jsonObj.get("ttrn_rw_due_date").toString());
			Date ttrn_uh_due_date = sdfOut.parse(jsonObj.get("ttrn_uh_due_date").toString());

			Date auditDate = null;
			Integer isAuditTasks = null;

			System.out.println("auditDate : " + jsonObj.get("ttrn_audit_date"));
			if (!jsonObj.get("ttrn_audit_date").toString().equals("")) {
				auditDate = sdfOut.parse(jsonObj.get("ttrn_audit_date").toString());
				isAuditTasks = 1;
			} else {
				isAuditTasks = 0;
				auditDate = null;
			}

			String ttrn_document = jsonObj.get("ttrn_document").toString();
			String ttrn_historical = jsonObj.get("ttrn_historical").toString();

			String ttrn_frequency_for_alerts = jsonObj.get("ttrn_frequency_for_alerts").toString();
			String ttrn_frequency_for_operation = jsonObj.get("ttrn_frequency_for_operation").toString();
			int ttrn_prior_days_buffer = Integer.parseInt(jsonObj.get("ttrn_prior_days_buffer").toString());
			int ttrn_alert_days = Integer.parseInt(jsonObj.get("ttrn_alert_days").toString());
			String ttrn_allow_back_date_completion = jsonObj.get("ttrn_allow_back_date_completion").toString();
			String ttrn_first_alert_check = jsonObj.get("ttrn_first_alert").toString();
			String ttrn_second_alert_check = jsonObj.get("ttrn_second_alert").toString();
			String ttrn_third_alert_check = jsonObj.get("ttrn_third_alert").toString();
			Date ttrn_first_alert = null;
			Date ttrn_second_alert = null;
			Date ttrn_third_alert = null;
			if (!ttrn_first_alert_check.equals(""))
				ttrn_first_alert = sdfOut.parse(ttrn_first_alert_check);
			if (!ttrn_second_alert_check.equals(""))
				ttrn_second_alert = sdfOut.parse(ttrn_second_alert_check);
			if (!ttrn_third_alert_check.equals(""))
				ttrn_third_alert = sdfOut.parse(ttrn_third_alert_check);

			int ttrn_no_of_back_days_allowed = Integer.parseInt(jsonObj.get("ttrn_no_of_back_days_allowed").toString());
			String ttrn_allow_approver_reopening = jsonObj.get("ttrn_allow_approver_reopening").toString();

			JSONArray configured_tasks = (JSONArray) jsonObj.get("tasks_list");
			for (int i = 0; i < configured_tasks.size(); i++) {
				JSONObject configured_tasks_obj = (JSONObject) configured_tasks.get(i);
				String ttrn_client_task_id = configured_tasks_obj.get("ttrn_client_task_id").toString();
				int ttrn_performer_user_id = Integer
						.parseInt(configured_tasks_obj.get("ttrn_performer_user_id").toString());

				String ttrn_impact = jsonObj.get("ttrn_impact").toString();
				String ttrn_impact_on_unit = jsonObj.get("ttrn_impact_on_unit").toString();
				String ttrn_impact_on_organization = jsonObj.get("ttrn_impact_on_organization").toString();
				Task task = tasksDao.getTaskUsingClientTaskID(ttrn_client_task_id);
				if (ttrn_impact.equals("")) {
					ttrn_impact = task.getTask_impact();
				}
				if (ttrn_impact_on_unit.equals("")) {
					ttrn_impact_on_unit = task.getTask_impact_on_unit();
				}
				if (ttrn_impact_on_organization.equals("")) {
					ttrn_impact_on_organization = task.getTask_impact_on_organization();
				}

				TaskTransactional taskTransactional = new TaskTransactional();

				taskTransactional.setTtrn_activation_date(new Date());
				taskTransactional.setTtrn_added_by(1);
				taskTransactional.setTtrn_alert_days(ttrn_alert_days);
				taskTransactional.setTtrn_allow_approver_reopening(ttrn_allow_approver_reopening);
				taskTransactional.setTtrn_allow_back_date_completion(ttrn_allow_back_date_completion);
				taskTransactional.setTtrn_client_task_id(ttrn_client_task_id);
				taskTransactional.setTtrn_created_at(new Date());
				taskTransactional.setTtrn_document(ttrn_document);
				taskTransactional.setTtrn_fh_due_date(ttrn_fh_due_date);
				taskTransactional.setTtrn_first_alert(ttrn_first_alert);
				taskTransactional.setTtrn_frequency_for_alerts(ttrn_frequency_for_alerts);
				taskTransactional.setTtrn_frequency_for_operation(ttrn_frequency_for_operation);
				taskTransactional.setTtrn_historical(ttrn_historical);
				taskTransactional.setTtrn_impact(ttrn_impact);
				taskTransactional.setTtrn_impact_on_organization(ttrn_impact_on_organization);
				taskTransactional.setTtrn_impact_on_unit(ttrn_impact_on_unit);
				taskTransactional.setTtrn_legal_due_date(ttrn_legal_due_date);
				taskTransactional.setTtrn_no_of_back_days_allowed(ttrn_no_of_back_days_allowed);
				taskTransactional.setTtrn_performer_user_id(ttrn_performer_user_id);
				taskTransactional.setTtrn_pr_due_date(ttrn_pr_due_date);
				taskTransactional.setTtrn_prior_days_buffer(ttrn_prior_days_buffer);
				taskTransactional.setTtrn_rw_due_date(ttrn_rw_due_date);
				taskTransactional.setTtrn_second_alert(ttrn_second_alert);
				taskTransactional.setTtrn_status("Inactive");
				taskTransactional.setTtrn_third_alert(ttrn_third_alert);
				taskTransactional.setTtrn_uh_due_date(ttrn_uh_due_date);
				taskTransactional.setTtrn_task_approved_by(0);
				taskTransactional.setTtrn_task_approved_date(new Date());
				taskTransactional.setAuditDate(auditDate);
				taskTransactional.setIsAuditTasks(isAuditTasks);
				taskTransactional.setTtrn_is_Task_Audited("No");
				taskTransactional.setIsDocumentUpload(0);
				tasksconfigurationdao.persist(taskTransactional);

				LogTasksConfiguration configLogs = new LogTasksConfiguration();
				configLogs.setAssignTime(new Date());
				configLogs.setUserId(user_id);
				configLogs.setTasksId(ttrn_client_task_id);
				configLogs.setLexTasksId(task.getTask_lexcare_task_id());
				configLogs.setAuditDate(auditDate);

				configLogs.setLegalDueDate(ttrn_legal_due_date);
				configLogs.setUnitHeadDueDate(ttrn_uh_due_date);
				configLogs.setExecutorHeadDueDate(ttrn_pr_due_date);
				configLogs.setEvaluatorDueDate(ttrn_rw_due_date);
				configLogs.setFuntionHeadDueDate(ttrn_fh_due_date);
				configLogs.setFrequency(ttrn_frequency_for_operation);

				configLogs.setPriodDays(Integer.toString(ttrn_alert_days));
				configLogs.setBufferDays(Integer.toString(ttrn_prior_days_buffer));
				cLogsDao.persistLogTasksConfiguration(configLogs);

				JSONObject objForAppend = new JSONObject();
				objForAppend.put("successClientTasksId", ttrn_client_task_id);
				dataForSend.add(objForAppend);

			}

			return dataForSend.toJSONString();
		} catch (Exception e) {
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("responseMessage", "Failed");
			e.printStackTrace();
			return objForAppend.toJSONString();
		}
	}

	// Method Created By: Mahesh Kharote(10/01/2016)
	// Method Purpose: Save Tasks User Mapping
	@SuppressWarnings("unchecked")
	@Override
	public String activationOfTasks(String jsonString, HttpSession session) {
		JSONObject objForAppend = new JSONObject();

		System.out.println("jsonString : " + jsonString.toString());

		try {
			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());

			ArrayList<Integer> ttrn_ids = new ArrayList<>();
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String operation_to_perform = jsonObj.get("operation_to_perform").toString();
			JSONArray configured_tasks = (JSONArray) jsonObj.get("tasks_list");
			for (int i = 0; i < configured_tasks.size(); i++) {
				JSONObject configured_tasks_obj = (JSONObject) configured_tasks.get(i);
				int ttrn_id = Integer.parseInt(configured_tasks_obj.get("ttrn_id").toString());
				ttrn_ids.add(ttrn_id);
				LogActivateDeActivateTasks aTasks = new LogActivateDeActivateTasks();
				aTasks.setActivateDeActTime(new Date());
				aTasks.setUserId(user_id);
				aTasks.setTasksStatus(operation_to_perform);
				aTasks.setTtrnId(Integer.toString(ttrn_id));
				cLogsDao.prsistActivateDeactivateLog(aTasks);
			}
			String responseString = "";
			if (operation_to_perform.equals("activate"))
				responseString = tasksconfigurationdao.activateTasks(ttrn_ids);
			else
				responseString = tasksconfigurationdao.deactivateTasks(ttrn_ids);

			String user_name = session.getAttribute("sess_user_full_name").toString();

			if (responseString.equals("Success")) {
				objForAppend.put("responseMessage", "Success");
				utilitiesService.addTaskActivationLog(ttrn_ids, operation_to_perform, user_id, user_name);
			} else {
				objForAppend.put("responseMessage", "Failed");
			}

			return objForAppend.toJSONString();
		} catch (Exception e) {

			objForAppend.put("responseMessage", "Failed");
			e.printStackTrace();
			return objForAppend.toJSONString();
		}
	}

	// Method Created By: Mahesh Kharote(10/01/2016)
	// Method Purpose: Save Tasks User Mapping
	/*
	 * @SuppressWarnings({ "unchecked", "rawtypes" })
	 * 
	 * @Override public String getAllConfiguredTaskForActivationPage(String
	 * jsonString, HttpSession session) { JSONObject dataForSend = new JSONObject();
	 * try { List<Object> allTasks =
	 * tasksconfigurationdao.getAllConfiguredTaskForActivationPage(jsonString);
	 * Iterator<Object> itr = allTasks.iterator(); JSONArray dataForAppend = new
	 * JSONArray(); JSONArray filters = new JSONArray(); JSONObject filtersObj = new
	 * JSONObject(); JSONArray Entity = new JSONArray();
	 * 
	 * JSONArray Unit = new JSONArray(); JSONArray Function = new JSONArray();
	 * JSONArray Users = new JSONArray(); List<Integer> CheckEntityForAddingToFilter
	 * = new ArrayList<>(); ArrayList<List> checkUntiForAddingToFilter = new
	 * ArrayList<>(); ArrayList<List> checkFuncForAddingToFilter = new
	 * ArrayList<>(); ArrayList<List> checkExecEvalForAddingToFilter = new
	 * ArrayList<>();
	 * 
	 * while (itr.hasNext()) { Object[] object = (Object[]) itr.next();
	 * -----------------------------------Code for adding tasks list of
	 * array--------------------------- JSONObject objForAppend = new JSONObject();
	 * 
	 * objForAppend.put("ttrn_id", object[10]);
	 * objForAppend.put("tmap_client_tasks_id", object[8]);
	 * objForAppend.put("task_legi_name", object[1]);
	 * objForAppend.put("task_rule_name", object[2]);
	 * objForAppend.put("task_reference", object[3]);
	 * objForAppend.put("task_activity_who", object[4]);
	 * objForAppend.put("task_activity_when", object[5]);
	 * objForAppend.put("task_activity", object[6]);
	 * objForAppend.put("task_procedure", object[7]);
	 * objForAppend.put("ttrn_frequency_for_operation", object[12]);
	 * objForAppend.put("executor_name", object[16].toString() + " " +
	 * object[17].toString()); objForAppend.put("ttrn_status", object[18]);
	 * objForAppend.put("orga_id", object[19]); objForAppend.put("loca_id",
	 * object[21]); objForAppend.put("dept_id", object[23]);
	 * objForAppend.put("exec_id", object[27]); objForAppend.put("eval_id",
	 * object[28]); objForAppend.put("ttrn_pr_due_date",
	 * sdfOut.format(sdfIn.parse(object[14].toString())));
	 * objForAppend.put("ttrn_rw_due_date",
	 * sdfOut.format(sdfIn.parse(object[29].toString())));
	 * objForAppend.put("ttrn_fh_due_date",
	 * sdfOut.format(sdfIn.parse(object[30].toString())));
	 * objForAppend.put("ttrn_uh_due_date",
	 * sdfOut.format(sdfIn.parse(object[31].toString())));
	 * objForAppend.put("ttrn_legal_due_date",
	 * sdfOut.format(sdfIn.parse(object[13].toString())));
	 * objForAppend.put("ttrn_frequency_for_alerts", object[32]);
	 * objForAppend.put("ttrn_impact", object[33]);
	 * objForAppend.put("ttrn_impact_on_organization", object[34]);
	 * objForAppend.put("ttrn_impact_on_unit", object[35]);
	 * objForAppend.put("ttrn_document", object[36]);
	 * objForAppend.put("ttrn_historical", object[37]);
	 * objForAppend.put("ttrn_prior_days_buffer", object[38]);
	 * objForAppend.put("ttrn_alert_days", object[39]);
	 * objForAppend.put("ttrn_task_approved_by", object[46]);
	 * objForAppend.put("task_lexcare_id", object[48]);
	 * 
	 * if (object[47] != null) objForAppend.put("ttrn_task_approved_date",
	 * sdfOut.format(sdfIn.parse(object[47].toString()))); else
	 * objForAppend.put("ttrn_task_approved_date", "");
	 * 
	 * if (object[40] != null) objForAppend.put("ttrn_first_alert",
	 * sdfOut.format(sdfIn.parse(object[40].toString()))); else
	 * objForAppend.put("ttrn_first_alert", "");
	 * 
	 * if (object[41] != null) objForAppend.put("ttrn_second_alert",
	 * sdfOut.format(sdfIn.parse(object[41].toString()))); else
	 * objForAppend.put("ttrn_second_alert", "");
	 * 
	 * if (object[42] != null) objForAppend.put("ttrn_third_alert",
	 * sdfOut.format(sdfIn.parse(object[42].toString()))); else
	 * objForAppend.put("ttrn_third_alert", "");
	 * 
	 * objForAppend.put("ttrn_no_of_back_days_allowed", object[43]);
	 * objForAppend.put("ttrn_allow_approver_reopening", object[44]);
	 * objForAppend.put("ttrn_allow_back_date_completion", object[45]);
	 * 
	 * dataForAppend.add(objForAppend); -----------------------------------Code for
	 * adding tasks list of array ends here-------------------
	 * 
	 * -----------------------------------Code for adding filters list to
	 * array---------------------------
	 * 
	 * if (!CheckEntityForAddingToFilter.contains(object[19])) { JSONObject
	 * dataForEntityFilterObj = new JSONObject();
	 * dataForEntityFilterObj.put("orga_id", object[19]);
	 * dataForEntityFilterObj.put("orga_name", object[20]);
	 * 
	 * Entity.add(dataForEntityFilterObj);
	 * CheckEntityForAddingToFilter.add(Integer.parseInt(object[19].toString())); }
	 * 
	 * // Creating new list to add orga id and loca id List<Integer>
	 * checkentiunitforadding = new ArrayList<>();
	 * 
	 * // Adding current orga Id and loca id to the for checking
	 * checkentiunitforadding.add(Integer.parseInt(object[19].toString()));
	 * checkentiunitforadding.add(Integer.parseInt(object[21].toString()));
	 * 
	 * // Checking if current orga id and loca id are already added to the main
	 * filter // list if
	 * (!checkUntiForAddingToFilter.contains(checkentiunitforadding)) { JSONObject
	 * dataForUnitFilterObj = new JSONObject(); dataForUnitFilterObj.put("loca_id",
	 * object[21]); dataForUnitFilterObj.put("loca_name", object[22]);
	 * dataForUnitFilterObj.put("orga_id", object[19]);
	 * 
	 * Unit.add(dataForUnitFilterObj);
	 * 
	 * checkUntiForAddingToFilter.add(checkentiunitforadding); }
	 * 
	 * // Creating new list to add orga id and loca id and dept id List<Integer>
	 * checkentiunitdeptforadding = new ArrayList<>();
	 * 
	 * // Adding current orga Id and loca id and dept id to the for checking
	 * checkentiunitdeptforadding.add(Integer.parseInt(object[19].toString()));
	 * checkentiunitdeptforadding.add(Integer.parseInt(object[21].toString()));
	 * checkentiunitdeptforadding.add(Integer.parseInt(object[23].toString()));
	 * 
	 * // Checking if current orga id and loca id and dept id are already added to
	 * the // main filter list if
	 * (!checkFuncForAddingToFilter.contains(checkentiunitdeptforadding)) {
	 * JSONObject dataForFuncFilterObj = new JSONObject();
	 * dataForFuncFilterObj.put("dept_id", object[23]);
	 * dataForFuncFilterObj.put("dept_name", object[24]);
	 * dataForFuncFilterObj.put("loca_id", object[21]);
	 * dataForFuncFilterObj.put("orga_id", object[19]);
	 * 
	 * Function.add(dataForFuncFilterObj);
	 * 
	 * checkFuncForAddingToFilter.add(checkentiunitdeptforadding); }
	 * 
	 * // Creating new list to add orga id and loca id and dept id and execeval
	 * List<Integer> checkentiunitdeptexecevalforadding = new ArrayList<>();
	 * 
	 * // Adding current orga Id and loca id and dept id and exec and eval to the
	 * for // checking
	 * 
	 * checkentiunitdeptexecevalforadding.add(Integer.parseInt(object[19].toString()
	 * ));
	 * checkentiunitdeptexecevalforadding.add(Integer.parseInt(object[21].toString()
	 * ));
	 * checkentiunitdeptexecevalforadding.add(Integer.parseInt(object[23].toString()
	 * ));
	 * checkentiunitdeptexecevalforadding.add(Integer.parseInt(object[27].toString()
	 * ));
	 * checkentiunitdeptexecevalforadding.add(Integer.parseInt(object[28].toString()
	 * ));
	 * 
	 * // Checking if current orga id and loca id and dept id and exec id and eval
	 * id // are already added to the main filter list if
	 * (!checkExecEvalForAddingToFilter.contains(checkentiunitdeptexecevalforadding)
	 * ) { JSONObject dataForExecEvalFilterObj = new JSONObject();
	 * 
	 * dataForExecEvalFilterObj.put("dept_id", object[23]);
	 * dataForExecEvalFilterObj.put("loca_id", object[21]);
	 * dataForExecEvalFilterObj.put("orga_id", object[19]);
	 * dataForExecEvalFilterObj.put("exec_id", object[27]);
	 * dataForExecEvalFilterObj.put("exec_name", object[16].toString() + " " +
	 * object[17].toString()); dataForExecEvalFilterObj.put("eval_id", object[28]);
	 * dataForExecEvalFilterObj.put("eval_name", object[25].toString() + " " +
	 * object[26].toString());
	 * 
	 * Users.add(dataForExecEvalFilterObj);
	 * 
	 * checkExecEvalForAddingToFilter.add(checkentiunitdeptexecevalforadding); }
	 * 
	 * -----------------------------------Code for adding filters list to
	 * array---------------------------
	 * 
	 * } filtersObj.put("Entity", Entity); filtersObj.put("Unit", Unit);
	 * filtersObj.put("Function", Function); filtersObj.put("Users", Users);
	 * filters.add(filtersObj);
	 * 
	 * dataForSend.put("All_Tasks", dataForAppend); dataForSend.put("Filters",
	 * filters);
	 * 
	 * return dataForSend.toJSONString(); } catch (Exception e) {
	 * dataForSend.put("responseMessage", "Failed"); e.printStackTrace(); return
	 * dataForSend.toJSONString(); } }
	 */
	// Method Created By: Mahesh Kharote(10/01/2016)
	// Method Purpose: Save Tasks User Mapping
	@SuppressWarnings("unchecked")
	@Override
	public String searchTaskForConfiguration(String json) {
		JSONArray searchList = new JSONArray();
		try {

			List<Object> result = tasksconfigurationdao.searchTasksForConfigurationPage(json);
			Iterator<Object> iterator = result.iterator();
			while (iterator.hasNext()) {
				Object[] objects = (Object[]) iterator.next();
				JSONObject jsonObject2 = new JSONObject();
				jsonObject2.put("tmap_client_task_id", objects[0]);
				jsonObject2.put("task_legi_name", objects[1]);
				jsonObject2.put("task_rule_name", objects[2]);
				jsonObject2.put("task_activity_who", objects[3]);
				jsonObject2.put("task_activity_when", objects[4]);
				jsonObject2.put("task_activity", objects[5]);
				jsonObject2.put("task_frequency_for_operation", objects[6]);
				jsonObject2.put("task_impact", objects[7]);
				jsonObject2.put("task_impact_on_unit", objects[8]);
				jsonObject2.put("task_impact_on_orga", objects[9]);
				jsonObject2.put("exec_id", objects[11]);
				jsonObject2.put("task_reference", objects[12]);
				jsonObject2.put("task_lexcare_id", objects[14]);
				searchList.add(jsonObject2);

			}

			return searchList.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
			JSONObject jsonObject2 = new JSONObject();
			jsonObject2.put("errorMessage", "Failed");
			searchList.add(jsonObject2);
			return searchList.toJSONString();
		}

	}

	// Method Created By: Mahesh Kharote(10/01/2016)
	// Method Purpose: Save Tasks User Mapping
	@SuppressWarnings("unchecked")
	@Override
	public String saveTaskCompletion(ArrayList<MultipartFile> ttrn_proof_of_compliance, String jsonString,
			HttpSession session) {
		JSONObject objForSend = new JSONObject();
		String ttrn_reason_for_non_compliance = "";
		try {

			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String ttrn_performer_comments = jsonObj.get("ttrn_performer_comments").toString();
			System.out.println("completed date:" + jsonObj.get("ttrn_completed_date").toString());
			Date ttrn_completed_date = sdfOut.parse(jsonObj.get("ttrn_completed_date").toString());

			if (jsonObj.get("ttrn_reason_for_non_compliance") == null) {
				ttrn_reason_for_non_compliance = "";
			} else {
				ttrn_reason_for_non_compliance = jsonObj.get("ttrn_reason_for_non_compliance").toString();
			}
			String ttrn_event_not_occure = jsonObj.get("ttrn_event_not_occure").toString();
			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			int user_role = Integer.parseInt(session.getAttribute("sess_role_id").toString());
			int mail_send_flag = 0;

			ArrayList<TaskTransactional> taskTransactionals = new ArrayList<>();

			JSONArray arrayToIterate = (JSONArray) jsonObj.get("ttrn_ids");
			for (int i = 0; i < arrayToIterate.size(); i++) {

				JSONObject configured_tasks_obj = (JSONObject) arrayToIterate.get(i);
				int ttrn_id = Integer.parseInt(configured_tasks_obj.get("ttrn_id").toString());
				/*------------Code for uploading files---------------------------------------------------------------------*/
				String originalFileName = null;
				String generatedFileName = null;
				System.out.println("ttrn_id : " + ttrn_id);
				int lastGeneratedValue = uploadedDocumentsDao.getLastGeneratedValueByTtrnId(ttrn_id);

				TaskTransactional taskTransactional = tasksconfigurationdao.getTasksForCompletion(ttrn_id);

				List<Object> saveStatusByCompletion = tasksconfigurationdao.saveStatusByCompletion(ttrn_id);
				if (saveStatusByCompletion != null) {
					Iterator<Object> iterator = saveStatusByCompletion.iterator();
					while (iterator.hasNext()) {
						System.out.println("Inside saveStatusByCompletion " + ttrn_id + "\t size : "
								+ saveStatusByCompletion.size());

						JSONObject obj = new JSONObject();
						Object next[] = (Object[]) iterator.next();
						obj.put("PosingRisk", next[0].toString());
						obj.put("Complied", next[1].toString());
						obj.put("NonComplied", next[2].toString());
						obj.put("Delayed", next[3].toString());
						obj.put("DelayedReported", next[4].toString());
						obj.put("ReOpened", next[5].toString());
						obj.put("WaitingForApproval", next[6].toString());

						if (next[0].toString().equalsIgnoreCase("1")) {
							taskTransactional.setTtrn_tasks_status("PosingRisk");
						}

						if (next[1].toString().equalsIgnoreCase("1")) {
							taskTransactional.setTtrn_tasks_status("Complied");
						}
						if (next[2].toString().equalsIgnoreCase("1")) {
							taskTransactional.setTtrn_tasks_status("NonComplied");
						}
						if (next[3].toString().equalsIgnoreCase("1")) {
							taskTransactional.setTtrn_tasks_status("PosingRisk");
						}
						if (next[4].toString().equalsIgnoreCase("1")) {
							taskTransactional.setTtrn_tasks_status("Delayed");
						}

						if (next[5].toString().equalsIgnoreCase("1")) {
							taskTransactional.setTtrn_tasks_status("DelayedReported");
						}

						if (next[6].toString().equalsIgnoreCase("1")) {
							taskTransactional.setTtrn_tasks_status("WaitingForApproval");
						}

					}
				}

				if (ttrn_proof_of_compliance.size() > 0) {
					taskTransactional.setIsDocumentUpload(1);
					System.out.println("inside docu upload 1 " + ttrn_proof_of_compliance.size());
				} else {
					taskTransactional.setIsDocumentUpload(1);
					System.out.println("inside docu upload 0 " + ttrn_proof_of_compliance.size());
				}

				/*------------------Getting name of project-----------------------*/
				/*------------------Getting name of project ends here-----------------------*/
				for (int i1 = 0; i1 < ttrn_proof_of_compliance.size(); i1++) {
					MultipartFile file1 = ttrn_proof_of_compliance.get(i1);
					String fileExtension = FilenameUtils.getExtension(file1.getOriginalFilename());
					if (fileExtension.equalsIgnoreCase("csv") || fileExtension.equalsIgnoreCase("xls")
							|| fileExtension.equalsIgnoreCase("xlsx") || fileExtension.equalsIgnoreCase("txt")
							|| fileExtension.equalsIgnoreCase("pdf") || fileExtension.equalsIgnoreCase("zip")
							|| fileExtension.equalsIgnoreCase("jpeg") || fileExtension.equalsIgnoreCase("docx")
							|| fileExtension.equalsIgnoreCase("pptx") || fileExtension.equalsIgnoreCase("rar")
							|| fileExtension.equalsIgnoreCase("jpg") || fileExtension.equalsIgnoreCase("png")
							|| fileExtension.equalsIgnoreCase("msg")) {
						if (file1.getSize() > 0) {

							Date cur_date = new Date();

							Calendar cal = Calendar.getInstance();
							cal.setTime(cur_date);
							int year = cal.get(Calendar.YEAR);
							int month = cal.get(Calendar.MONTH);
							int day = cal.get(Calendar.DAY_OF_MONTH);
							String dir_month_name = day + "-" + (month + 1) + "-" + year;

							File dir = new File("C:" + File.separator + "CMS" + File.separator + "documents"
									+ File.separator + "proofOfCompliance" + File.separator + projectName
									+ File.separator + dir_month_name);
							if (!dir.exists())
								dir.mkdirs();

							lastGeneratedValue++;
							originalFileName = file1.getOriginalFilename();
							generatedFileName = "uploadedProof" + ttrn_id + "_" + lastGeneratedValue + "."
									+ file1.getOriginalFilename().split("\\.")[1];
							File newFile = new File(dir.getPath() + File.separator + generatedFileName);
							if (!newFile.exists()) {
								newFile.createNewFile();
							}

							OutputStream outputStream = new FileOutputStream(newFile);

							outputStream.write(file1.getBytes());

							String algo = "DES/ECB/PKCS5Padding";
							utilitiesService.encrypt(algo, newFile.getPath());

							UploadedDocuments documents = new UploadedDocuments();
							documents.setUdoc_ttrn_id(ttrn_id);
							documents.setUdoc_original_file_name(originalFileName);
							documents.setUdoc_filename(dir + File.separator + generatedFileName);
							documents.setUdoc_last_generated_value_for_filename_for_ttrn_id(lastGeneratedValue);
							uploadedDocumentsDao.saveDocuments(documents);

							outputStream.flush();
							outputStream.close();

							Path path = Paths.get(dir + File.separator + generatedFileName);
							try {
								Files.delete(path);
							} catch (IOException e) {
								// deleting file failed
								e.printStackTrace();
								System.out.println(e.getMessage());
							}
						}
					} else {
						objForSend.put("responseMessage", "Invalid File Type");
						return objForSend.toJSONString();
					}

					/*---------------Code for uploading files ends here---------------------------------------------------------*/
				}

				if (taskTransactional.getTtrn_status().equals("Active")
						|| taskTransactional.getTtrn_status().equals("Re_Opened"))
					mail_send_flag = 1;

				taskTransactional.setTtrn_completed_date(ttrn_completed_date);
				taskTransactional.setTtrn_performer_comments(ttrn_performer_comments);
				if (!ttrn_reason_for_non_compliance.equals("")) {
					taskTransactional.setTtrn_reason_for_non_compliance(ttrn_reason_for_non_compliance);
				}
				int noOfBackDaysAllowed = taskTransactional.getTtrn_no_of_back_days_allowed();
				if (noOfBackDaysAllowed > 0) {
					Date currentDate = new Date();
					if (currentDate.after(taskTransactional.getTtrn_legal_due_date())) {
						long diff = currentDate.getTime() - taskTransactional.getTtrn_legal_due_date().getTime();
						int differenceDays = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

						if ((noOfBackDaysAllowed - differenceDays) >= 0) {
							taskTransactional.setTtrn_submitted_date(ttrn_completed_date);
						} else {
							taskTransactional.setTtrn_submitted_date(new Date());
						}

					} else {
						taskTransactional.setTtrn_submitted_date(new Date());
					}

				} else {
					taskTransactional.setTtrn_submitted_date(new Date());
				}

				/*
				 * Calendar cal = Calendar.getInstance();
				 * cal.setTime(taskTransactional.getTtrn_legal_due_date()); //int year =
				 * cal.get(Calendar.YEAR); //int month = cal.get(Calendar.MONTH); int day =
				 * cal.get(Calendar.DAY_OF_WEEK);
				 * 
				 * if(day ==5 || day==6){
				 * taskTransactional.setTtrn_submitted_date(taskTransactional.
				 * getTtrn_legal_due_date()); }
				 */

				if (ttrn_event_not_occure.equals("Yes")) {
					taskTransactional.setTtrn_status("Event_Not_Occured");
					// taskTransactional.setTtrn_completed_date(ttrn_completed_date);
					taskTransactional.setTtrn_performer_comments("");
					taskTransactional.setTtrn_reason_for_non_compliance("");

				} else {
					System.out.println(" ROLE " + user_role + " Aprover or not "
							+ taskTransactional.getTtrn_allow_approver_reopening());
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
			tasksconfigurationdao.saveTaskCompletion(taskTransactionals);
			if (mail_send_flag == 1) // IF status is active then only mail send while updating task mail mail should
										// not be sent
				utilitiesService.sendTaskCompletionMailToEvaluator(taskTransactionals, "MainTask");// Task completion
																									// mail to evaluator

			objForSend.put("responseMessage", "Success");
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("responseMessage", "Failed");
			return objForSend.toJSONString();
		}
	}

	// Method Created By: Mahesh Kharote(10/01/2016)
	// Method Purpose: Save Tasks User Mapping
	@Override
	public String updateTasksConfiguration(String jsonString, HttpSession session) {
		JSONObject objForSend = new JSONObject();
		String email_body = null;
		try {
			System.out.println("jsonString:" + jsonString);
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int ttrn_id = Integer.parseInt(jsonObj.get("ttrn_id").toString());
			String user_name = session.getAttribute("sess_user_full_name").toString();
			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			System.out.println("user_id , user_name:" + user_id + " " + user_name);

			User user = userDao.getUserById(user_id);
			// System.out.println("user :" + user);
			String user_mail_id = user.getUser_email();
			// System.out.println("user_mail_id:" + user_mail_id);

			List<Object> next = tasksconfigurationdao.getTasksForCompletionWithNativeQuery(ttrn_id);
			// System.out.println("next size : " + next.size());
			Iterator<Object> iterator = next.iterator();
			while (iterator.hasNext()) {
				Object[] taskTransactional1 = (Object[]) iterator.next();
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				/*
				 * String get_prev_pr_due_date = taskTransactional1[22].toString(); //
				 * .getTtrn_pr_due_date() String get_prev_rw_due_date =
				 * taskTransactional1[25].toString(); // .getTtrn_rw_due_date() String
				 * get_prev_fh_due_date = taskTransactional1[10].toString(); //
				 * .getTtrn_fh_due_date() String get_prev_uh_due_date =
				 * taskTransactional1[33].toString(); // .getTtrn_uh_due_date() String
				 * get_prev_legal_due_date = taskTransactional1[18].toString(); //
				 * .getTtrn_legal_due_date()
				 */
				String frequency_for_operation = taskTransactional1[13].toString(); // .getTtrn_frequency_for_operation()
				User prusr = userDao.getUserById(Integer.parseInt(taskTransactional1[21].toString()));
				String ttrn_impact = null;
				String ttrn_impact_on_unit = null;
				String ttrn_impact_on_organization = null;
				String ttrn_document = null;
				String ttrn_historical = null;

				String ttrn_frequency_for_alerts = null;
				String ttrn_frequency_for_operation = null;
				int ttrn_prior_days_buffer = 0;
				int ttrn_alert_days = 0;

				int ttrn_no_of_back_days_allowed = 0;
				String ttrn_allow_approver_reopening = null;
				Date ttrn_activation_date = null;

				String prev_pr_due_date = dateFormat.format(taskTransactional1[22]);
				String prev_rw_due_date = dateFormat.format(taskTransactional1[25]);
				String prev_fh_due_date = dateFormat.format(taskTransactional1[10]);
				String prev_uh_due_date = dateFormat.format(taskTransactional1[33]);
				String prev_legal_due_date = dateFormat.format(taskTransactional1[18]);

				TaskTransactional taskTransactional = new TaskTransactional();
				System.out.println("frequency_for_operation:" + frequency_for_operation);

				// jsonObj.get("ttrn_frequency_for_operation"
				if (jsonObj.get("ttrn_frequency_for_operation") != null || jsonObj.get("ttrn_impact_on_unit") != null
						|| jsonObj.get("ttrn_impact") != null || jsonObj.get("ttrn_impact_on_organization") != null
						|| jsonObj.get("ttrn_document") != null || jsonObj.get("ttrn_historical") != null
						|| jsonObj.get("ttrn_prior_days_buffer") != null || jsonObj.get("ttrn_alert_days") != null
						|| jsonObj.get("ttrn_no_of_back_days_allowed") != null
						|| jsonObj.get("ttrn_allow_approver_reopening") != null) {
					// System.out.println("in if");
					System.out.println("in if : " + taskTransactional1[6].toString());
					Date ttrn_pr_due_date = sdfOut.parse(jsonObj.get("ttrn_pr_due_date").toString());
					Date ttrn_rw_due_date = sdfOut.parse(jsonObj.get("ttrn_rw_due_date").toString());
					Date ttrn_fh_due_date = sdfOut.parse(jsonObj.get("ttrn_fh_due_date").toString());
					Date ttrn_uh_due_date = sdfOut.parse(jsonObj.get("ttrn_uh_due_date").toString());
					Date ttrn_legal_due_date = sdfOut.parse(jsonObj.get("ttrn_legal_due_date").toString());
					ttrn_activation_date = sdfOut.parse(taskTransactional1[1].toString());
					if (jsonObj.get("ttrn_impact") != null) {
						ttrn_impact = jsonObj.get("ttrn_impact").toString();
					} else {
						ttrn_impact = taskTransactional1[15].toString();
					}

					if (jsonObj.get("ttrn_impact_on_unit") != null) {
						ttrn_impact_on_unit = jsonObj.get("ttrn_impact_on_unit").toString();
					} else {
						ttrn_impact_on_unit = taskTransactional1[17].toString();
					}

					if (jsonObj.get("ttrn_impact_on_organization") != null) {
						ttrn_impact_on_organization = jsonObj.get("ttrn_impact_on_organization").toString();
					} else {
						ttrn_impact_on_organization = taskTransactional1[16].toString();
					}

					if (jsonObj.get("ttrn_document") != null) {
						ttrn_document = jsonObj.get("ttrn_document").toString();
					} else {
						ttrn_document = taskTransactional1[9].toString();
					}

					if (jsonObj.get("ttrn_historical") != null) {
						ttrn_historical = jsonObj.get("ttrn_historical").toString();
					} else {
						ttrn_historical = taskTransactional1[14].toString();
					}

					if (jsonObj.get("ttrn_frequency_for_alerts") != null) {
						ttrn_frequency_for_alerts = jsonObj.get("ttrn_frequency_for_alerts").toString();
					} else {
						ttrn_frequency_for_alerts = taskTransactional1[12].toString();
					}

					if (jsonObj.get("ttrn_frequency_for_operation") != null) {
						ttrn_frequency_for_operation = jsonObj.get("ttrn_frequency_for_operation").toString();
					} else {
						ttrn_frequency_for_operation = taskTransactional1[13].toString();
					}

					if (jsonObj.get("ttrn_prior_days_buffer") != null) {
						ttrn_prior_days_buffer = Integer.parseInt(jsonObj.get("ttrn_prior_days_buffer").toString());
					} else {
						ttrn_prior_days_buffer = Integer.parseInt(taskTransactional1[23].toString());
					}

					if (jsonObj.get("ttrn_alert_days") != null) {
						ttrn_alert_days = Integer.parseInt(jsonObj.get("ttrn_alert_days").toString());
					} else {
						ttrn_alert_days = Integer.parseInt(taskTransactional1[3].toString());
					}
					// String ttrn_first_alert_check = taskTransactional1[11].toString();
					// String ttrn_second_alert_check = taskTransactional1[26].toString();
					// String ttrn_third_alert_check = taskTransactional1[32].toString();
					Date ttrn_first_alert = null;
					Date ttrn_second_alert = null;
					Date ttrn_third_alert = null;

					/*
					 * if (jsonObj.get("ttrn_first_alert").toString().equals("")) { ttrn_first_alert
					 * = sdfOut.parse(taskTransactional1[11].toString()); }else { ttrn_first_alert =
					 * sdfOut.parse(jsonObj.get("ttrn_first_alert").toString()); }
					 */
					/*
					 * if (!taskTransactional1[26].toString().equals(null)) ttrn_second_alert =
					 * sdfOut.parse(taskTransactional1[26].toString()); if
					 * (!taskTransactional1[32].toString().equals(null)) ttrn_third_alert =
					 * sdfOut.parse(taskTransactional1[32].toString());
					 */

					if (jsonObj.get("ttrn_no_of_back_days_allowed") != null) {
						ttrn_no_of_back_days_allowed = Integer
								.parseInt(jsonObj.get("ttrn_no_of_back_days_allowed").toString());
					} else {
						ttrn_no_of_back_days_allowed = Integer.parseInt(taskTransactional1[19].toString());
					}
					if (jsonObj.get("ttrn_allow_approver_reopening") != null) {
						ttrn_allow_approver_reopening = jsonObj.get("ttrn_allow_approver_reopening").toString();
					} else {
						ttrn_allow_approver_reopening = taskTransactional1[4].toString();
					}

					taskTransactional.setTtrn_alert_days(ttrn_alert_days);
					taskTransactional.setTtrn_allow_approver_reopening(ttrn_allow_approver_reopening);
					taskTransactional.setTtrn_document(ttrn_document);
					taskTransactional.setTtrn_fh_due_date(ttrn_fh_due_date);
					// taskTransactional.setTtrn_first_alert(null);
					taskTransactional.setTtrn_frequency_for_alerts(ttrn_frequency_for_alerts);
					taskTransactional.setTtrn_frequency_for_operation(ttrn_frequency_for_operation);
					taskTransactional.setTtrn_historical(ttrn_historical);
					taskTransactional.setTtrn_impact(ttrn_impact);
					taskTransactional.setTtrn_impact_on_organization(ttrn_impact_on_organization);
					taskTransactional.setTtrn_impact_on_unit(ttrn_impact_on_unit);
					taskTransactional.setTtrn_legal_due_date(ttrn_legal_due_date);
					taskTransactional.setTtrn_no_of_back_days_allowed(ttrn_no_of_back_days_allowed);
					taskTransactional.setTtrn_pr_due_date(ttrn_pr_due_date);
					taskTransactional.setTtrn_prior_days_buffer(ttrn_prior_days_buffer);
					taskTransactional.setTtrn_rw_due_date(ttrn_rw_due_date);
					// taskTransactional.setTtrn_second_alert(ttrn_second_alert);
					// taskTransactional.setTtrn_third_alert(ttrn_third_alert);
					taskTransactional.setTtrn_uh_due_date(ttrn_uh_due_date);
					taskTransactional.setTtrn_activation_date(ttrn_activation_date);

					tasksconfigurationdao.updateTaskConfigurationWithNativeQuery(taskTransactional, ttrn_id);

					String curr_pr_due_date = dateFormat.format(ttrn_pr_due_date);
					String curr_rw_due_date = dateFormat.format(ttrn_rw_due_date);
					String curr_fh_due_date = dateFormat.format(ttrn_fh_due_date);
					String curr_uh_due_date = dateFormat.format(ttrn_uh_due_date);
					String curr_legal_due_date = dateFormat.format(ttrn_legal_due_date);

					email_body = "<h2 style='font-size:18px;'>Dear Executor,</h2>";
					email_body += "<p style='text-align:justify;width:70%;'>Please note that the due date for the below task has been changed. </p>"
							+ "<p style='text-align:justify;width:70%;'>Requesting you to execute the task accordingly.</p>"
							+ "<h2 style='font-size:19px;font-weight:bold;'> Client Task ID: "
							+ taskTransactional1[6].toString() + "</h2>";
					email_body += "<table style='width:80%;' border='1'>" + "<thead>"
							+ "<tr style='background:#0B6EC3;color:#fff;'>" + "<th>#</th>"
							+ "<th>Executor Due Date</th>" + "<th>Evaluator Due Date</th>"
							+ "<th>Function Head Due Date</th>" + "<th>Unit Head Due Date</th>"
							+ "<th>Legal Due Date</th>" + "</tr>" + "</thead>" + "<tbody>";

					email_body += "<tr>" + "<td>" + "Previous Due Date" + "</td>" + "<td>" + prev_pr_due_date + "</td>"
							+ "<td>" + prev_rw_due_date + "</td>" + "<td>" + prev_fh_due_date + "</td>" + "<td>"
							+ prev_uh_due_date + "</td>" + "<td>" + prev_legal_due_date + "</td>" + "</tr>" + "<tr>"
							+ "<td>" + "Current Due Date" + "</td>" + "<td>" + curr_pr_due_date + "</td>" + "<td>"
							+ curr_rw_due_date + "</td>" + "<td>" + curr_fh_due_date + "</td>" + "<td>"
							+ curr_uh_due_date + "</td>" + "<td>" + curr_legal_due_date + "</td>" + "</tr>";

					email_body += "</tbody>" + "</table>";
					email_body += "<p>This is a system generated mail. Please do not reply to this mail.<br/>"
							+ "In case of any doubt or difficulty, please call Helpdesk as per details given on the support page."
							+ "</p>" + "<h2 style='font-size:18px;font-weight:bold;'>Yours Sincerely</h2>"
							+ "<h2 style='font-size:19px;font-weight:bold;'>Team LexCare</h2>" + "</div>";

					try {
						/*----------------------Code to send mail Start here---------------*/
						Properties props = new Properties();
						props.put("mail.smtp.auth", "true");
						// props.put("mail.smtp.starttls.enable", "true");
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
							message.addRecipient(Message.RecipientType.TO, new InternetAddress(prusr.getUser_email()));

							message.setSubject("Due Date Change Alert");
							// }
							message.setContent(email_body, "text/html; charset=utf-8");
							Transport.send(message);
							utilitiesService.addMailToLog(prusr.getUser_email(), "Due Date Change Alert",
									taskTransactional1[6].toString(), user.getUser_email()); // Mail
							// log
						}

						catch (Exception e) {
							e.printStackTrace();
							System.out.println("Error in transport send");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {

					System.out.println("IN ELSE LOOP");
					Date ttrn_pr_due_date = sdfOut.parse(jsonObj.get("ttrn_pr_due_date").toString());
					Date ttrn_rw_due_date = sdfOut.parse(jsonObj.get("ttrn_rw_due_date").toString());
					Date ttrn_fh_due_date = sdfOut.parse(jsonObj.get("ttrn_fh_due_date").toString());
					Date ttrn_uh_due_date = sdfOut.parse(jsonObj.get("ttrn_uh_due_date").toString());
					Date ttrn_legal_due_date = sdfOut.parse(jsonObj.get("ttrn_legal_due_date").toString());

					ttrn_activation_date = sdfOut.parse(taskTransactional1[1].toString());
					ttrn_impact = taskTransactional1[15].toString();
					ttrn_impact_on_unit = taskTransactional1[17].toString();
					ttrn_impact_on_organization = taskTransactional1[16].toString();
					ttrn_document = taskTransactional1[9].toString();
					ttrn_historical = taskTransactional1[14].toString();

					ttrn_frequency_for_alerts = taskTransactional1[13].toString();
					ttrn_frequency_for_operation = taskTransactional1[13].toString();
					ttrn_prior_days_buffer = Integer.parseInt(taskTransactional1[23].toString());
					ttrn_alert_days = Integer.parseInt(taskTransactional1[3].toString());

					Date ttrn_first_alert = null;
					Date ttrn_second_alert = null;
					Date ttrn_third_alert = null;

					ttrn_no_of_back_days_allowed = Integer.parseInt(taskTransactional1[19].toString());
					ttrn_allow_approver_reopening = taskTransactional1[4].toString();

					taskTransactional.setTtrn_pr_due_date(ttrn_pr_due_date);
					taskTransactional.setTtrn_rw_due_date(ttrn_rw_due_date);
					taskTransactional.setTtrn_fh_due_date(ttrn_fh_due_date);
					taskTransactional.setTtrn_uh_due_date(ttrn_uh_due_date);
					taskTransactional.setTtrn_legal_due_date(ttrn_legal_due_date);

					taskTransactional.setTtrn_alert_days(ttrn_alert_days);
					taskTransactional.setTtrn_allow_approver_reopening(ttrn_allow_approver_reopening);
					taskTransactional.setTtrn_document(ttrn_document);
					taskTransactional.setTtrn_fh_due_date(ttrn_fh_due_date);
					// taskTransactional.setTtrn_first_alert(null);
					taskTransactional.setTtrn_frequency_for_alerts(ttrn_frequency_for_alerts);
					taskTransactional.setTtrn_frequency_for_operation(ttrn_frequency_for_operation);
					taskTransactional.setTtrn_historical(ttrn_historical);
					taskTransactional.setTtrn_impact(ttrn_impact);
					taskTransactional.setTtrn_impact_on_organization(ttrn_impact_on_organization);
					taskTransactional.setTtrn_impact_on_unit(ttrn_impact_on_unit);
					taskTransactional.setTtrn_no_of_back_days_allowed(ttrn_no_of_back_days_allowed);

					taskTransactional.setTtrn_prior_days_buffer(ttrn_prior_days_buffer);

					taskTransactional.setTtrn_activation_date(ttrn_activation_date);

					String curr_pr_due_date = dateFormat.format(ttrn_pr_due_date);
					String curr_rw_due_date = dateFormat.format(ttrn_rw_due_date);
					String curr_fh_due_date = dateFormat.format(ttrn_fh_due_date);
					String curr_uh_due_date = dateFormat.format(ttrn_uh_due_date);
					String curr_legal_due_date = dateFormat.format(ttrn_legal_due_date);

					tasksconfigurationdao.updateTaskConfigurationWithNativeQuery(taskTransactional, ttrn_id);

					System.out.println("due date change alert");
					email_body = "<h2 style='font-size:18px;'>Dear Executor,</h2>";
					email_body += "<p style='text-align:justify;width:70%;'>Please note that the due date for the below task has been changed. </p>"
							+ "<p style='text-align:justify;width:70%;'>Requesting you to execute the task accordingly.</p>"
							+ "<h2 style='font-size:19px;font-weight:bold;'> Client Task ID: "
							+ taskTransactional1[6].toString() + "</h2>";
					email_body += "<table style='width:80%;' border='1'>" + "<thead>"
							+ "<tr style='background:#0B6EC3;color:#fff;'>" + "<th>#</th>"
							+ "<th>Executor Due Date</th>" + "<th>Evaluator Due Date</th>"
							+ "<th>Function Head Due Date</th>" + "<th>Unit Head Due Date</th>"
							+ "<th>Legal Due Date</th>" + "</tr>" + "</thead>" + "<tbody>";

					email_body += "<tr>" + "<td>" + "Previous Due Date" + "</td>" + "<td>" + prev_pr_due_date + "</td>"
							+ "<td>" + prev_rw_due_date + "</td>" + "<td>" + prev_fh_due_date + "</td>" + "<td>"
							+ prev_uh_due_date + "</td>" + "<td>" + prev_legal_due_date + "</td>" + "</tr>" + "<tr>"
							+ "<td>" + "Current Due Date" + "</td>" + "<td>" + curr_pr_due_date + "</td>" + "<td>"
							+ curr_rw_due_date + "</td>" + "<td>" + curr_fh_due_date + "</td>" + "<td>"
							+ curr_uh_due_date + "</td>" + "<td>" + curr_legal_due_date + "</td>" + "</tr>";

					email_body += "</tbody>" + "</table>";
					email_body += "<p>This is a system generated mail. Please do not reply to this mail.<br/>"
							+ "In case of any doubt or difficulty, please call Helpdesk as per details given on the support page."
							+ "</p>" + "<h2 style='font-size:18px;font-weight:bold;'>Yours Sincerely</h2>"
							+ "<h2 style='font-size:19px;font-weight:bold;'>Team LexCare</h2>" + "</div>";
					try {
						/*----------------------Code to send mail Start here---------------*/
						Properties props = new Properties();
						props.put("mail.smtp.auth", "true");
						// props.put("mail.smtp.starttls.enable", "true");
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
							message.addRecipient(Message.RecipientType.TO, new InternetAddress(prusr.getUser_email()));

							message.setSubject("Due Date Change Alert");
							// }
							message.setContent(email_body, "text/html; charset=utf-8");
							Transport.send(message);
							// addMailToLog(user_email, subject,client_tasks_id_for_email_appending); //Mail
							// log
						}

						catch (Exception e) {
							e.printStackTrace();
							System.out.println("Error in transport send");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				utilitiesService.addTaskCofigurationLog(taskTransactional.getTtrn_client_task_id(), user_id, user_name,
						"MainTask", "Update");

				return objForSend.toJSONString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return objForSend.toJSONString();
		}
		return email_body;
	}

	// Method Created By: Mahesh Kharote(10/01/2016)
	@SuppressWarnings("unchecked")
	// Method Purpose: Save Tasks User Mapping
	@Override
	public String savedefaulttaskconfiguration(String jsonString, HttpSession session) {
		JSONArray dataForSend = new JSONArray();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String user_name = session.getAttribute("sess_user_full_name").toString();
			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());

			String dtco_after_before = jsonObj.get("dtco_after_before").toString();
			String dtco_event = jsonObj.get("dtco_event").toString();
			String dtco_sub_event = jsonObj.get("dtco_sub_event").toString();
			int dtco_pr_days;
			int dtco_rw_days;
			int dtco_fh_days;
			int dtco_uh_days;
			int dtco_legal_days;
			/*
			 * String dtco_client_task_id; String dtco_default_frequency; String
			 * dtco_lexcare_task_id; int dtco_mst_task_id; Date dtco_created_at; int
			 * dtco_added_by; String dtco_status; String dtco_comments;
			 */

			if (dtco_after_before.equals("Same")) {
				dtco_legal_days = 0;
				dtco_uh_days = 0;
				dtco_fh_days = 0;
				dtco_rw_days = 0;
				dtco_pr_days = 0;
			} else {
				dtco_legal_days = Integer.parseInt(jsonObj.get("dtco_legal_days").toString());
				dtco_uh_days = Integer.parseInt(jsonObj.get("dtco_uh_days").toString());
				dtco_fh_days = Integer.parseInt(jsonObj.get("dtco_fh_days").toString());
				dtco_rw_days = Integer.parseInt(jsonObj.get("dtco_rw_days").toString());
				dtco_pr_days = Integer.parseInt(jsonObj.get("dtco_pr_days").toString());
			}

			JSONArray configured_tasks = (JSONArray) jsonObj.get("tasks_list");
			for (int i = 0; i < configured_tasks.size(); i++) {
				JSONObject configured_tasks_obj = (JSONObject) configured_tasks.get(i);
				String ttrn_client_task_id = configured_tasks_obj.get("dtco_client_task_id").toString();

				DefaultTaskConfiguration configuration = new DefaultTaskConfiguration();
				configuration.setDtco_client_task_id(ttrn_client_task_id);
				configuration.setDtco_default_frequency("Event_Based");
				configuration.setDtco_event(dtco_event);
				configuration.setDtco_sub_event(dtco_sub_event);
				configuration.setDtco_after_before(dtco_after_before);
				configuration.setDtco_legal_days(dtco_legal_days);
				configuration.setDtco_uh_days(dtco_uh_days);
				configuration.setDtco_fh_days(dtco_fh_days);
				configuration.setDtco_rw_days(dtco_rw_days);
				configuration.setDtco_pr_days(dtco_pr_days);
				configuration.setDtco_status("Active");
				configuration.setDtco_added_by(Integer.parseInt(session.getAttribute("sess_user_id").toString()));
				configuration.setDtco_created_at(utilitiesService.getCurrentDate());
				tasksconfigurationdao.persist(configuration);

				utilitiesService.addTaskCofigurationLog(ttrn_client_task_id, user_id, user_name, "DefaultTask", "Add");
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("successClientTasksId", ttrn_client_task_id);
				dataForSend.add(objForAppend);
			}

			return dataForSend.toJSONString();
		} catch (Exception e) {
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("responseMessage", "Failed");
			e.printStackTrace();
			return objForAppend.toJSONString();
		}
	}

	// Method Created By: Harshad Padole
	// Method Purpose: Initiate Task
	@SuppressWarnings("unchecked")
	@Override
	public String initiateDefaultConfiguredTask(String json, HttpSession session) {
		JSONArray dataForSend = new JSONArray();
		try {

			JSONObject jsonObj = (JSONObject) new JSONParser().parse(json);
			String date_of_initiation = jsonObj.get("date_of_initiation").toString();
			String comments = jsonObj.get("comments").toString();

			SimpleDateFormat sdfIn = new SimpleDateFormat("dd-MM-yyyy");

			Date init_date = sdfIn.parse(date_of_initiation);

			JSONArray configured_tasks = (JSONArray) jsonObj.get("tasks_list");
			for (int i = 0; i < configured_tasks.size(); i++) {
				JSONObject configured_tasks_obj = (JSONObject) configured_tasks.get(i);
				int dtco_id = Integer.parseInt(configured_tasks_obj.get("dtco_id").toString());
				// System.out.println("TASK ID "+dtco_id);

				// System.out.println("After "+after_before);
				List<Object> res = tasksconfigurationdao.getTaskDetailsToConfigure(dtco_id);
				Iterator<Object> iterator = res.iterator();
				while (iterator.hasNext()) {
					Object[] object = (Object[]) iterator.next();

					Date legalDate = null;
					Date unitDate = null;
					Date functionDate = null;
					Date evaluatorDate = null;
					Date executorDate = null;

					Calendar lega_cal = Calendar.getInstance();
					lega_cal.setTime(init_date);
					Calendar unit_cal = Calendar.getInstance();
					unit_cal.setTime(init_date);
					Calendar fun_cal = Calendar.getInstance();
					fun_cal.setTime(init_date);
					Calendar eva_cal = Calendar.getInstance();
					eva_cal.setTime(init_date);
					Calendar exe_cal = Calendar.getInstance();
					exe_cal.setTime(init_date);

					// Date dateBefore30Days = cal.getTime();

					if (object[9].toString().equals("After")) {

						lega_cal.add(Calendar.DATE, Integer.parseInt(object[4].toString()));
						legalDate = lega_cal.getTime(); // new Date(init_date.getTime() -
														// Integer.parseInt(object[4].toString()) * 24 * 3600 * 1000 );

						unit_cal.add(Calendar.DATE, Integer.parseInt(object[5].toString()));
						unitDate = unit_cal.getTime(); // new Date(init_date.getTime() -
														// Integer.parseInt(object[5].toString()) * 24 * 3600 * 1000 );

						fun_cal.add(Calendar.DATE, Integer.parseInt(object[6].toString()));
						functionDate = fun_cal.getTime();// new Date(init_date.getTime() -
															// Integer.parseInt(object[6].toString()) * 24 * 3600 * 1000
															// );

						eva_cal.add(Calendar.DATE, Integer.parseInt(object[8].toString()));
						evaluatorDate = eva_cal.getTime(); // new Date(init_date.getTime() -
															// Integer.parseInt(object[7].toString()) * 24 * 3600 * 1000
															// );

						exe_cal.add(Calendar.DATE, Integer.parseInt(object[7].toString()));
						executorDate = exe_cal.getTime(); // new Date(init_date.getTime() -
															// Integer.parseInt(object[8].toString()) * 24 * 3600 * 1000
															// );
					}
					if (object[9].toString().equals("Before")) {

						lega_cal.add(Calendar.DATE, -Integer.parseInt(object[4].toString()));
						legalDate = lega_cal.getTime(); // new Date(init_date.getTime() -
														// Integer.parseInt(object[4].toString()) * 24 * 3600 * 1000 );

						unit_cal.add(Calendar.DATE, -Integer.parseInt(object[5].toString()));
						unitDate = unit_cal.getTime();
						// new Date(init_date.getTime() -
						// Integer.parseInt(object[5].toString()) * 24 * 3600 * 1000 );

						fun_cal.add(Calendar.DATE, -Integer.parseInt(object[6].toString()));
						functionDate = fun_cal.getTime();// new Date(init_date.getTime() -
															// Integer.parseInt(object[6].toString()) * 24 * 3600 * 1000
															// );

						eva_cal.add(Calendar.DATE, -Integer.parseInt(object[8].toString()));
						evaluatorDate = eva_cal.getTime(); // new Date(init_date.getTime() -
															// Integer.parseInt(object[7].toString()) * 24 * 3600 * 1000
															// );

						exe_cal.add(Calendar.DATE, -Integer.parseInt(object[7].toString()));
						executorDate = exe_cal.getTime(); // new Date(init_date.getTime() -
															// Integer.parseInt(object[8].toString()) * 24 * 3600 * 1000
															// );
					}

					if (object[9].toString().equals("Before") || object[9].toString().equals("After")) {

						TaskTransactional taskConfig = new TaskTransactional();

						if (object[10] != null)
							taskConfig.setTtrn_impact(object[10].toString());
						else
							taskConfig.setTtrn_impact(null);

						if (object[11] != null)
							taskConfig.setTtrn_impact_on_unit(object[11].toString());
						else
							taskConfig.setTtrn_impact_on_unit(null);

						if (object[12] != null)
							taskConfig.setTtrn_impact_on_organization(object[12].toString());
						else
							taskConfig.setTtrn_impact_on_organization(null);

						taskConfig.setTtrn_document("0");
						taskConfig.setTtrn_historical("0");
						taskConfig.setTtrn_created_at(new Date());
						taskConfig.setTtrn_allow_back_date_completion("0");
						taskConfig.setTtrn_legal_due_date(legalDate);
						taskConfig.setTtrn_uh_due_date(unitDate);
						taskConfig.setTtrn_fh_due_date(functionDate);
						taskConfig.setTtrn_rw_due_date(evaluatorDate);
						taskConfig.setTtrn_pr_due_date(executorDate);
						taskConfig.setTtrn_first_alert(null);
						taskConfig.setTtrn_second_alert(null);
						taskConfig.setTtrn_third_alert(null);
						taskConfig.setTtrn_performer_user_id(Integer.parseInt(object[13].toString()));
						taskConfig.setTtrn_client_task_id(object[0].toString());
						taskConfig.setTtrn_frequency_for_alerts(object[1].toString());
						taskConfig.setTtrn_frequency_for_operation(object[1].toString());
						taskConfig.setTtrn_added_by(Integer.parseInt(session.getAttribute("sess_user_id").toString()));
						taskConfig.setTtrn_status("Active");
						taskConfig.setTtrn_prior_days_buffer(0);
						taskConfig.setTtrn_alert_days(2);
						taskConfig.setTtrn_activation_date(new Date());
						taskConfig.setTtrn_task_approved_by(0);
						taskConfig.setTtrn_task_approved_date(new Date());
						tasksconfigurationdao.persist(taskConfig);
					}

					if (object[9].toString().equals("Same")) {
						// dtco_id = Integer.parseInt(object[8].toString());
						TaskTransactional taskConfig = new TaskTransactional();
						if (object[10] != null)
							taskConfig.setTtrn_impact(object[10].toString());
						else
							taskConfig.setTtrn_impact(null);

						if (object[11] != null)
							taskConfig.setTtrn_impact_on_unit(object[11].toString());
						else
							taskConfig.setTtrn_impact_on_unit(null);

						if (object[12] != null)
							taskConfig.setTtrn_impact_on_organization(object[12].toString());
						else
							taskConfig.setTtrn_impact_on_organization(null);

						taskConfig.setTtrn_document("0");
						taskConfig.setTtrn_historical("0");
						taskConfig.setTtrn_created_at(utilitiesService.getCurrentDate());
						taskConfig.setTtrn_allow_back_date_completion("0");
						taskConfig.setTtrn_legal_due_date(init_date);
						taskConfig.setTtrn_uh_due_date(init_date);
						taskConfig.setTtrn_fh_due_date(init_date);
						taskConfig.setTtrn_rw_due_date(init_date);
						taskConfig.setTtrn_pr_due_date(init_date);
						taskConfig.setTtrn_first_alert(null);
						taskConfig.setTtrn_second_alert(null);
						taskConfig.setTtrn_third_alert(null);
						taskConfig.setTtrn_performer_user_id(Integer.parseInt(object[13].toString()));
						taskConfig.setTtrn_client_task_id(object[0].toString());
						taskConfig.setTtrn_frequency_for_alerts(object[1].toString());
						taskConfig.setTtrn_frequency_for_operation(object[1].toString());
						taskConfig.setTtrn_added_by(Integer.parseInt(session.getAttribute("sess_user_id").toString()));
						taskConfig.setTtrn_status("Active");
						taskConfig.setTtrn_prior_days_buffer(0);
						taskConfig.setTtrn_alert_days(2);
						taskConfig.setTtrn_activation_date(init_date);
						taskConfig.setTtrn_task_approved_by(0);
						taskConfig.setTtrn_task_approved_date(new Date());
						tasksconfigurationdao.persist(taskConfig);

					}

					// Update Comments

					DefaultTaskConfiguration defaultConfigrutaion = tasksconfigurationdao
							.getDetails(Integer.parseInt(object[14].toString()));
					if (comments.equals("") || comments.equals(null)) {
						System.out.println("No changes in comment");

					} else {
						defaultConfigrutaion.setDtco_comments(comments);
						System.out.println("changes in comment " + comments);
						tasksconfigurationdao.updateDefaultConfiguration(defaultConfigrutaion);

					}
					JSONObject objForAppend = new JSONObject();
					objForAppend.put("successDtcoId", dtco_id);
					dataForSend.add(objForAppend);
				}

			}
			return dataForSend.toJSONString();
		} catch (Exception e) {
			JSONObject objForAppend = new JSONObject();
			// objForAppend.put("responseMessage", "Failed");
			e.printStackTrace();
			return objForAppend.toJSONString();
		}
	}

	// Method Created By: Harshad Padole
	// Method Purpose: Approve Task
	@SuppressWarnings("unchecked")
	@Override
	public String approveTask(String json, HttpSession session) {
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(json);
			// Document download status
			String documentDownloadStatus = tasksconfigurationdao
					.getDocumentDownloadStatus(jsonObj.get("ttrn_id").toString());

			if (documentDownloadStatus == "Approved") {

				System.out.println("Attached are downloaded");

				int ttrn_id = Integer.parseInt(jsonObj.get("ttrn_id").toString());
				TaskTransactional taskTransactional = tasksconfigurationdao.getTasksForCompletion(ttrn_id);

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

				tasksconfigurationdao.updateTaskConfiguration(taskTransactional);
				JSONObject objForSend = new JSONObject();
				objForSend.put("responseMessage", "Success");
				return objForSend.toJSONString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject objForSend = new JSONObject();
			objForSend.put("responseMessage", "Failed");

			return objForSend.toJSONString();
		}
		JSONObject objForSend = new JSONObject();
		// System.out.println("Please go through all the document..");
		objForSend.put("responseMessage", "Please download all the attachment in order to approve the task !");

		return objForSend.toJSONString();
	}

	// Method Created By: Harshad Padole
	// Method Purpose: Reopen Task
	@SuppressWarnings("unchecked")
	@Override
	public String reOpenTask(String json, HttpSession session) {
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(json);

			String auditor_performer_by_id = session.getAttribute("sess_user_id").toString();

			Integer sesUserRole = Integer.parseInt(session.getAttribute("sess_role_id").toString());

			int ttrn_id = Integer.parseInt(jsonObj.get("ttrn_id").toString());
			String comment = jsonObj.get("reopen_comment").toString();

			List<UploadedDocuments> attachedDocuments = uploadedDocumentsDao.getAllDocumentByTtrnId(ttrn_id);

			if (attachedDocuments != null) {
				Iterator<UploadedDocuments> itre = attachedDocuments.iterator();

				while (itre.hasNext()) {
					UploadedDocuments uploadedDocuments = (UploadedDocuments) itre.next();

					File deleteFile = new File(uploadedDocuments.getUdoc_filename());
					deleteFile.delete();
					// uploadedDocumentsDao.deleteDocument(uploadedDocuments.getUdoc_id());
					// System.out.println("Delete DOC");
				}

			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			Date rDate = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(rDate);
			cal.add(Calendar.DATE, 5);
			String format = sdf.format(cal.getTime());
			System.out.println("format  : " + format);
			Date reOpenDateWindow = sdf1.parse(format);
			System.out.println("reOpenDateWindow  : " + reOpenDateWindow);
			System.out.println("auitPag : " + jsonObj.get("auditor"));

			TaskTransactional taskTransactional = tasksconfigurationdao.getTasksForCompletion(ttrn_id);
			taskTransactional.setTtrn_task_completed_by(0);
//			taskTransactional.setTtrn_completed_date(null);
//			if (jsonObj.get("auditor") != null) {
			if (sesUserRole >= 6) {
				System.out.println("inside audit if ");
				taskTransactional.setReOpenDateWindow(reOpenDateWindow);
				taskTransactional.setAuditDate(reOpenDateWindow);
				taskTransactional.setAuditorAuditTime(new Date());
				taskTransactional.setTtrn_tasks_status("Re_Opened");
				taskTransactional.setIsDocumentUpload(1);
				taskTransactional.setIsAuditTasks(1);
				taskTransactional.setTtrn_is_Task_Audited("Yes");
				taskTransactional.setAuditor_performer_by_id(auditor_performer_by_id);
			}

			/**
			 * As per audit req commented below lines
			 */

//			taskTransactional.setTtrn_submitted_date(null);
			taskTransactional.setTtrn_status("Re_Opened");
//			taskTransactional.setTtrn_performer_comments(null);
			taskTransactional.setAuditorStatus("Re_Opened");
			taskTransactional.setTtrn_tasks_status("Re_Opened");
			taskTransactional.setAuditoComments(comment);

			tasksconfigurationdao.updateTaskConfiguration(taskTransactional); // Reopen Task

			String res = utilitiesService.sendTaskReopenMailToExecutor(ttrn_id, comment, "ReopenMainTask");// Send mail
			if (res.equals("Success")) {
				String user_name = session.getAttribute("sess_user_full_name").toString();
				String ids = String.valueOf(ttrn_id);
				utilitiesService.addMailToLog("Task Reopen by- " + user_name, " Reopen Task ", "ttrn_id - " + ids);
			}
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

	// Method Created By: Harshad Padole
	@SuppressWarnings("unchecked")
	// Method Purpose: Delete task details
	@Override
	public String deleteTaskHistory(String json, HttpSession session) {
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(json);
			int ttrn_id = Integer.parseInt(jsonObj.get("ttrn_id").toString());
			String ttrn_client_task_id = jsonObj.get("ttrn_client_task_id").toString();
			tasksconfigurationdao.deleteTaskHistory(ttrn_id);
			String user_name = session.getAttribute("sess_user_full_name").toString();
			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			String ids = String.valueOf(ttrn_id);
			// utilitiesService.addMailToLog("Delete Task History- User Id-"+user_id+"
			// UserName - "+user_name, " Delete Task ", "ttrn_id - "+ids );
			utilitiesService.addTaskDeletionLog(ids, "Task History", user_id, user_name, ttrn_client_task_id, "");
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("responseMessage", "Success");
			return objForAppend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("responseMessage", "Fail");
			return objForAppend.toJSONString();
		}

	}

	// Method Created By: Harshad Padole
	// Method Purpose: Delete task mapping
	@SuppressWarnings("unchecked")
	@Override
	public String deleteTaskMapping(String json, HttpSession session) {
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(json);
			int tmap_id = Integer.parseInt(jsonObj.get("tmap_id").toString());
			String clientTaskId = jsonObj.get("ttrn_client_task_id").toString();
			String lexcare_task_id = jsonObj.get("lexcare_task_id").toString();

			List<TaskTransactional> Res = tasksconfigurationdao.getTaskHistoryByClientTaskId(clientTaskId);
			if (Res.size() == 0) {
				tasksconfigurationdao.deleteTaskMapping(tmap_id);
				String user_name = session.getAttribute("sess_user_full_name").toString();
				int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
				String ids = String.valueOf(tmap_id);
				// utilitiesService.addMailToLog("Delete Task Mapping- User Id-"+user_id+"
				// UserName - "+user_name, " Delete mapping ", "tmap_id - "+ids );
				utilitiesService.addTaskDeletionLog(ids, "Task Mapping", user_id, user_name, clientTaskId,
						lexcare_task_id);
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("responseMessage", "Success");
				return objForAppend.toJSONString();
			} else {
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("responseMessage", "CheckTaskHistory");
				return objForAppend.toJSONString();
			}

		} catch (Exception e) {
			e.printStackTrace();
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("responseMessage", "Fail");
			return objForAppend.toJSONString();
		}
	}

	// Method Created By: Harshad Padole
	// Method Purpose: get all default task configuration details
	@SuppressWarnings("unchecked")
	@Override
	public String getDefaultTaskConfiguration(String json) {
		JSONArray sendList = new JSONArray();
		try {
			List<Object> res = tasksconfigurationdao.getDefaultTask();
			if (res.size() != 0) {
				Iterator<Object> iterator = res.iterator();
				while (iterator.hasNext()) {
					Object[] objects = (Object[]) iterator.next();
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("dtco_id", objects[0]);
					jsonObj.put("dtco_client_task_id", objects[1]);
					jsonObj.put("dtco_after_before", objects[2]);
					jsonObj.put("dtco_pr_days", objects[3]);
					jsonObj.put("dtco_rw_days", objects[4]);
					jsonObj.put("dtco_fh_days", objects[5]);
					jsonObj.put("dtco_uh_days", objects[6]);
					jsonObj.put("dtco_legal_days", objects[7]);
					jsonObj.put("dtco_frequency", objects[8]);
					jsonObj.put("dtco_event", objects[9]);
					jsonObj.put("dtco_sub_event", objects[10]);
					jsonObj.put("tmap_orga_id", objects[11]);
					jsonObj.put("tmap_loca_id", objects[12]);
					jsonObj.put("tmap_dept_id", objects[13]);
					jsonObj.put("tmap_pr_id", objects[14]);
					jsonObj.put("tmap_rw_id", objects[15]);
					jsonObj.put("lexcare_task_id", objects[16]);

					sendList.add(jsonObj);
				}
			}

			return sendList.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			return sendList.toJSONString();
		}
	}

	// Method Created By: Harshad Padole
	// Method Purpose: update default task configuration details
	@SuppressWarnings("unchecked")
	@Override
	public String updateDefaultTaskConfiguration(String json, HttpSession session) {
		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);
			String user_name = session.getAttribute("sess_user_full_name").toString();
			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());

			int dtco_id = Integer.parseInt(jsonObject.get("dtco_id").toString());
			String dtco_client_task_id = jsonObject.get("dtco_client_task_id").toString();
			String dtco_after_before = jsonObject.get("dtco_after_before").toString();
			int dtco_pr_days = Integer.parseInt(jsonObject.get("dtco_pr_days").toString());
			int dtco_rw_days = Integer.parseInt(jsonObject.get("dtco_rw_days").toString());
			int dtco_fh_days = Integer.parseInt(jsonObject.get("dtco_fh_days").toString());
			int dtco_uh_days = Integer.parseInt(jsonObject.get("dtco_uh_days").toString());
			int dtco_legal_days = Integer.parseInt(jsonObject.get("dtco_legal_days").toString());

			DefaultTaskConfiguration defaultConfigrutaion = tasksconfigurationdao.getDetails(dtco_id);

			defaultConfigrutaion.setDtco_after_before(dtco_after_before);

			if (!dtco_after_before.equals("Same")) {
				defaultConfigrutaion.setDtco_pr_days(dtco_pr_days);
				defaultConfigrutaion.setDtco_rw_days(dtco_rw_days);
				defaultConfigrutaion.setDtco_fh_days(dtco_fh_days);
				defaultConfigrutaion.setDtco_uh_days(dtco_uh_days);
				defaultConfigrutaion.setDtco_legal_days(dtco_legal_days);
			} else {
				defaultConfigrutaion.setDtco_pr_days(0);
				defaultConfigrutaion.setDtco_rw_days(0);
				defaultConfigrutaion.setDtco_fh_days(0);
				defaultConfigrutaion.setDtco_uh_days(0);
				defaultConfigrutaion.setDtco_legal_days(0);
			}

			tasksconfigurationdao.updateDefaultConfiguration(defaultConfigrutaion);
			utilitiesService.addTaskCofigurationLog(dtco_client_task_id, user_id, user_name, "DefaultTask", "Update");
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("responseMessage", "Success");
			return objForAppend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("responseMessage", "Fail");
			return objForAppend.toJSONString();
		}
	}

	// Method Written By: Harshad Padole
	@SuppressWarnings("unchecked")
	// Method Purpose: update task reason for non compliance
	@Override
	public String updateResoneOfNonCompliance(String jsonString, HttpSession session) {
		JSONObject dataToSend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int ttrn_id = Integer.parseInt(jsonObj.get("ttrn_id").toString());
			String ttrn_reason_for_non_compliance = jsonObj.get("ttrn_reason_for_non_compliance").toString();
			TaskTransactional taskTransactional = tasksconfigurationdao.getTasksForCompletion(ttrn_id);
			taskTransactional.setTtrn_reason_for_non_compliance(ttrn_reason_for_non_compliance);
			tasksconfigurationdao.updateTaskConfiguration(taskTransactional);
			dataToSend.put("responseMessage", "Success");
			return dataToSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			dataToSend.put("responseMessage", "Failed");
			return dataToSend.toJSONString();
		}
	}

	// Method Written By: Harshad Padole
	// Method Purpose: Save sub task completion
	@SuppressWarnings("unchecked")
	@Override
	public String saveSubTaskCompletion(ArrayList<MultipartFile> ttrn_proof_of_compliance, String json,
			HttpSession session) {
		JSONObject dataToSend = new JSONObject();
		try {

			JSONObject jsonObj = (JSONObject) new JSONParser().parse(json);
			String ttrn_performer_comments = jsonObj.get("ttrn_performer_comments").toString();
			Date ttrn_completed_date = sdfOut.parse(jsonObj.get("ttrn_completed_date").toString());
			String ttrn_reason_for_non_compliance = jsonObj.get("ttrn_reason_for_non_compliance").toString();
			// Date ttrn_next_examination_date =
			// sdfOut.parse(jsonObj.get("ttrn_next_examination_date").toString());
			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			// int user_role =
			// Integer.parseInt(session.getAttribute("sess_role_id").toString());

			JSONArray arrayToIterate = (JSONArray) jsonObj.get("ttrn_sub_ids");
			for (int i = 0; i < arrayToIterate.size(); i++) {

				JSONObject configured_tasks_obj = (JSONObject) arrayToIterate.get(i);
				int ttrn_sub_id = Integer.parseInt(configured_tasks_obj.get("ttrn_sub_id").toString());

				String originalFileName = null;
				String generatedFileName = null;
				int lastGeneratedValue = uploadedDocumentsDao.getLastGeneratedValueByTtrnSubId(ttrn_sub_id);

				/*------------------Getting name of project-----------------------*/
				/*------------------Getting name of project ends here-----------------------*/
				for (int i1 = 0; i1 < ttrn_proof_of_compliance.size(); i1++) {
					MultipartFile file1 = ttrn_proof_of_compliance.get(i1);
					if (file1.getSize() > 0) {
						File dir = new File("C:" + File.separator + "CMS" + File.separator + projectName
								+ File.separator + "documents" + File.separator + "SubTask_proofOfCompliance");
						if (!dir.exists())
							dir.mkdirs();

						lastGeneratedValue++;
						originalFileName = file1.getOriginalFilename();
						generatedFileName = "uploadedProof" + ttrn_sub_id + "_" + lastGeneratedValue + "."
								+ file1.getOriginalFilename().split("\\.")[1];
						File newFile = new File(dir.getPath() + File.separator + generatedFileName);
						if (!newFile.exists()) {
							newFile.createNewFile();
						}

						OutputStream outputStream = new FileOutputStream(newFile);

						outputStream.write(file1.getBytes());

						String algo = "DES/ECB/PKCS5Padding";
						utilitiesService.encrypt(algo, newFile.getPath());

						String file_path = "C:" + File.separator + "CMS" + File.separator + projectName + File.separator
								+ "documents" + File.separator + "SubTask_proofOfCompliance" + File.separator
								+ generatedFileName;

						UploadedSubTaskDocuments documents = new UploadedSubTaskDocuments();
						documents.setUdoc_sub_task_ttrn_id(ttrn_sub_id);
						documents.setUdoc_sub_task_original_file_name(originalFileName);
						documents.setUdoc_sub_task_filename(file_path);
						documents.setUdoc_last_generated_value_for_filename_for_sub_task_ttrn_id(lastGeneratedValue);
						uploadedDocumentsDao.saveDocuments(documents);

						outputStream.flush();
						outputStream.close();

						Path path = Paths.get(file_path);
						try {
							Files.delete(path);
						} catch (IOException e) {
							// deleting file failed
							e.printStackTrace();
							System.out.println(e.getMessage());
						}
					}

					/*---------------Code for uploading files ends here---------------------------------------------------------*/
				}

				int autoActivate_flag = 0;
				int mail_send_flag = 0;
				SubTaskTranscational subTaskTranscational = tasksconfigurationdao
						.getSubTaskTransactionalDetailsById(ttrn_sub_id);

				if (subTaskTranscational.getTtrn_sub_task_status().equals("Active"))
					mail_send_flag = 1;
				autoActivate_flag = 1;

				subTaskTranscational.setTtrn_sub_task_comment(ttrn_performer_comments);
				subTaskTranscational.setTtrn_sub_task_compl_date(ttrn_completed_date);
				subTaskTranscational.setTtrn_sub_task_completed_by(String.valueOf(user_id));

				if (!ttrn_reason_for_non_compliance.equals(""))
					subTaskTranscational.setTtrn_sub_task_reason_for_non_compliance(ttrn_reason_for_non_compliance);

				int noOfBackDaysAllowed = subTaskTranscational.getTtrn_sub_task_back_date_allowed();
				if (noOfBackDaysAllowed > 0) {
					Date currentDate = new Date();
					if (currentDate.after(subTaskTranscational.getTtrn_sub_task_ENT_due_date())) {
						long diff = currentDate.getTime()
								- subTaskTranscational.getTtrn_sub_task_ENT_due_date().getTime();
						int differenceDays = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

						if ((noOfBackDaysAllowed - differenceDays) >= 0) {
							subTaskTranscational.setTtrn_sub_task_submition_date(ttrn_completed_date);
						} else {
							subTaskTranscational.setTtrn_sub_task_submition_date(new Date());
						}

					} else {
						subTaskTranscational.setTtrn_sub_task_submition_date(new Date());
					}

				} else {
					subTaskTranscational.setTtrn_sub_task_submition_date(new Date());
				}

				if (subTaskTranscational.getTtrn_sub_task_allow_approver_reopening() != null
						&& Integer.parseInt(subTaskTranscational.getTtrn_sub_task_allow_approver_reopening()) == 1) {
					subTaskTranscational.setTtrn_sub_task_status("Partially_Completed");
				} else {
					subTaskTranscational.setTtrn_sub_task_status("Completed");
				}
				tasksconfigurationdao.merge(subTaskTranscational);

				// After completing sub task -reactivate task
				/*
				 * if (autoActivate_flag == 1)
				 * schedularService.subTaskAutoActivate(subTaskTranscational,
				 * ttrn_next_examination_date);
				 */

				// Send task completion mail send to evaluator
				if (mail_send_flag == 1)
					utilitiesService.sendSubTaskCompletionMailToEvaluator(subTaskTranscational);

			}
			dataToSend.put("responseMessage", "Success");
			return dataToSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			dataToSend.put("responseMessage", "Failed");
			return dataToSend.toJSONString();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String deleteTaskDocument(String json, HttpSession session) {

		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(json);
			int udoc_id = Integer.parseInt(jsonObj.get("udoc_id").toString());
			tasksconfigurationdao.deleteTaskDocument(udoc_id);

			JSONObject objForAppend = new JSONObject();
			objForAppend.put("responseMessage", "Success");
			return objForAppend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("responseMessage", "Fail");
			return objForAppend.toJSONString();
		}

	}

	@Override
	public String searchTaskForConfigurationPage(String json) {

		JSONArray searchList = new JSONArray();
		try {

			List<Object> result = tasksconfigurationdao.searchTasksForConfigurationPage(json);
			Iterator<Object> iterator = result.iterator();
			while (iterator.hasNext()) {
				Object[] objects = (Object[]) iterator.next();
				JSONObject jsonObject2 = new JSONObject();
				jsonObject2.put("tmap_client_task_id", objects[0]);
				jsonObject2.put("task_legi_name", objects[1]);
				jsonObject2.put("task_rule_name", objects[2]);
				jsonObject2.put("task_activity_who", objects[3]);
				jsonObject2.put("task_activity_when", objects[4]);
				jsonObject2.put("task_activity", objects[5]);
				jsonObject2.put("task_frequency_for_operation", objects[6]);
				jsonObject2.put("task_impact", objects[7]);
				jsonObject2.put("task_impact_on_unit", objects[8]);
				jsonObject2.put("task_impact_on_orga", objects[9]);
				jsonObject2.put("exec_id", objects[11]);
				jsonObject2.put("task_reference", objects[12]);
				jsonObject2.put("task_lexcare_id", objects[14]);
				searchList.add(jsonObject2);

			}

			return searchList.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
			JSONObject jsonObject2 = new JSONObject();
			jsonObject2.put("errorMessage", "Failed");
			searchList.add(jsonObject2);
			return searchList.toJSONString();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public String downloadProofOfCompliance(String jsonString, HttpServletResponse response) throws Throwable {
		JSONObject objForSend = new JSONObject();
		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);
			System.out.println("udoc_id :" + jsonObject.get("udoc_id").toString());
			int udoc_id = Integer.parseInt(jsonObject.get("udoc_id").toString());
			if (uploadedDocumentsDao.getProofFilePath(udoc_id) != null) {
				File file = new File(uploadedDocumentsDao.getProofFilePath(udoc_id));

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
					System.out.println(e.getMessage());
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
	public String getTaskListForDefaultTaskConfiguration(String jsonString) {
		JSONArray searchList = new JSONArray();
		try {

			List<Object> result = tasksconfigurationdao.searchTasksForDefaultConfigurationPage(jsonString);
			Iterator<Object> iterator = result.iterator();
			while (iterator.hasNext()) {
				Object[] objects = (Object[]) iterator.next();
				JSONObject jsonObject2 = new JSONObject();
				jsonObject2.put("tmap_client_task_id", objects[0]);
				jsonObject2.put("task_legi_name", objects[1]);
				jsonObject2.put("task_rule_name", objects[2]);
				jsonObject2.put("task_activity_who", objects[3]);
				jsonObject2.put("task_activity_when", objects[4]);
				jsonObject2.put("task_activity", objects[5]);
				jsonObject2.put("task_frequency_for_operation", objects[6]);
				jsonObject2.put("task_impact", objects[7]);
				jsonObject2.put("task_impact_on_unit", objects[8]);
				jsonObject2.put("task_impact_on_orga", objects[9]);
				jsonObject2.put("exec_id", objects[11]);
				jsonObject2.put("task_reference", objects[12]);
				jsonObject2.put("task_lexcare_id", objects[14]);
				searchList.add(jsonObject2);

			}

			return searchList.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
			JSONObject jsonObject2 = new JSONObject();
			jsonObject2.put("errorMessage", "Failed");
			searchList.add(jsonObject2);
			return searchList.toJSONString();
		}

	}

	@Override
	public String checkDocumentBeforeDownload(String jsonString, HttpServletResponse response) {
		JSONObject objForSend = new JSONObject();
		JSONArray dataForSend = new JSONArray();
		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);
			System.out.println("udoc_id :" + jsonObject.get("doc_id").toString());
			int udoc_id = Integer.parseInt(jsonObject.get("doc_id").toString());
			System.out.println("udoc_id:" + udoc_id);

			if (uploadedDocumentsDao.getProofFilePath(udoc_id) != null) {
				File file = new File(uploadedDocumentsDao.getProofFilePath(udoc_id));
				File file1 = new File(file.getPath() + ".enc");
				if (file1.exists()) {
					System.out.println("file exist");
					String algo = "DES/ECB/PKCS5Padding";
					File decFile = new File(utilitiesService.decrypt(algo, file.getPath() + ".enc"));
					// System.out.println("return :"+utilitiesService.decrypt(algo,
					// file.getPath()+".enc"));

					InputStream is = new FileInputStream(decFile);
					// System.out.println("deccFile :"
					// +decFile.getPath().substring(0,decFile.getPath().lastIndexOf(".")));
					String fileExtension = getFileExtension(decFile);
					if (fileExtension.equals("xlsx") || fileExtension.equals("xls") || fileExtension.equals("csv")) {
						System.out.println("in if loop");
						FileInputStream fis = new FileInputStream(decFile); // obtaining bytes from the file
						// creating Workbook instance that refers to .xlsx file
						XSSFWorkbook wb = new XSSFWorkbook(fis);
						XSSFCellStyle safeFormulaStyle = wb.createCellStyle();

						XSSFSheet sheet = wb.getSheetAt(0); // creating a Sheet object to retrieve object
						Iterator<Row> itr = sheet.iterator(); // iterating over excel file
						while (itr.hasNext()) {
							Row row = itr.next();
							Iterator<Cell> cellIterator = row.cellIterator(); // iterating over each column
							while (cellIterator.hasNext()) {
								Cell cell = cellIterator.next();
								switch (cell.getCellType()) {
								case Cell.CELL_TYPE_STRING: // field that represents string cell type
									String cellValue = cell.getStringCellValue();
									System.out.println("cell value:" + cellValue);
									if (cellValue != null && "=-+@[]?^#!<>".indexOf(cellValue.charAt(0)) >= 0) {
										System.out.println("cell value:" + cellValue);
										System.out.println("THROW CSV INJECTION EXCEPTION");
										cell.setCellStyle(safeFormulaStyle);
										/*
										 * objForSend.put("responseMessage", "Failed"); dataForSend.add(objForSend);
										 * return dataForSend.toJSONString();
										 */
									}
									break;
								case Cell.CELL_TYPE_NUMERIC: // field that represents number cell type
									String cellValue1 = cell.getStringCellValue();
									if (cellValue1 != null && "=-+@[]?^#!<>".indexOf(cellValue1.charAt(0)) >= 0) {
										System.out.println("cell value:" + cellValue1);
										System.out.println("THROW CSV INJECTION EXCEPTION");
										cell.setCellStyle(safeFormulaStyle);
										/*
										 * objForSend.put("responseMessage", "Failed"); dataForSend.add(objForSend);
										 * return dataForSend.toJSONString();
										 */
									}
									break;
								case Cell.CELL_TYPE_FORMULA:
									String cellValue2 = cell.getCellFormula();
									if (cellValue2 != null && "=-+@[]?^#!<>".indexOf(cellValue2.charAt(0)) >= 0) {
										System.out.println("cell value:" + cellValue2);
										System.out.println("THROW CSV INJECTION EXCEPTION");
										cell.setCellStyle(safeFormulaStyle);
										objForSend.put("responseMessage", "Failed");
										dataForSend.add(objForSend);
										return dataForSend.toJSONString();
									}
									break;

								default:

								}
							}
							System.out.println("");

						}
						objForSend.put("responseMessage", "Success");
						dataForSend.add(objForSend);
						return dataForSend.toJSONString();
					} else {
						System.out.println("IN ELSE LOOP");
						objForSend.put("responseMessage", "Success");
						dataForSend.add(objForSend);
						return dataForSend.toJSONString();
					}

				} else {
					System.out.println("The File was not found");
					objForSend.put("responseMessage", "File Not Found");
					dataForSend.add(objForSend);
					return dataForSend.toJSONString();

				}

				// objForSend.put("responseMessage", "Success");
				// return objForSend.toJSONString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("responseMessage", "Failed");
			dataForSend.add(objForSend);
			return dataForSend.toJSONString();
		}
		return null;
	}

	private static String getFileExtension(File file) {
		String fileName = file.getName();
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		else
			return "";
	}

}
