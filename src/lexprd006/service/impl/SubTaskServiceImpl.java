package lexprd006.service.impl;

import java.io.BufferedOutputStream;
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
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.csvreader.CsvReader;

import lexprd006.dao.SubTaskDao;
import lexprd006.domain.SubTask;
import lexprd006.domain.SubTaskTranscational;
import lexprd006.domain.UploadedSubTaskDocuments;
import lexprd006.service.SubTaskService;
import lexprd006.service.UtilitiesService;

@Service(value = "subtaskservice")
public class SubTaskServiceImpl implements SubTaskService {

	public final SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public final SimpleDateFormat sdfOut = new SimpleDateFormat("dd-MM-yyyy");

	@Autowired
	SubTaskDao subTaskDao;

	@Autowired
	UtilitiesService utilitiesService;

	// Method Created By: Harshad Padole
	// Method Purpose: get user defined task
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String getUserDefinedTask(HttpSession session) {
		JSONObject dataToSend = new JSONObject();
		try {
			List<Object> res = subTaskDao.getUserDefinedTask(session);
			JSONArray subTaskArray = new JSONArray();

			JSONArray entityarray = new JSONArray();
			JSONArray unitarray = new JSONArray();
			JSONArray funcarray = new JSONArray();
			JSONArray execarray = new JSONArray();
			JSONArray evalarray = new JSONArray();
			JSONArray legiarray = new JSONArray();
			JSONArray rulearray = new JSONArray();

			if (res != null) {

				List<Integer> checkEntiList = new ArrayList<>();
				List<List> checUnitList = new ArrayList<>();
				List<List> checFuncList = new ArrayList<>();
				List<List> checExecList = new ArrayList<>();
				List<List> checEvalList = new ArrayList<>();
				List<List> checLegiList = new ArrayList<>();
				List<List> checRuleList = new ArrayList<>();

				Iterator<Object> itr = res.iterator();
				while (itr.hasNext()) {
					Object[] object = (Object[]) itr.next();
					JSONObject subTask = new JSONObject();

					subTask.put("clientTaskId", object[0]);
					subTask.put("task_legi_name", object[2]);
					subTask.put("task_rule_name", object[4]);
					subTask.put("task_who", object[5]);
					subTask.put("task_when", object[6]);
					;
					subTask.put("task_reference", object[7]);
					subTask.put("task_activity", object[8]);
					subTask.put("task_procedure", object[9]);
					subTask.put("task_impact", object[10]);
					subTask.put("task_frequency", object[11]);
					subTask.put("task_configured_frequency", object[12]);
					subTask.put("task_legi_id", object[1]);
					subTask.put("task_rule_id", object[3]);
					subTask.put("orga_id", object[13]);
					subTask.put("loca_id", object[15]);
					subTask.put("dept_id", object[17]);
					subTask.put("exec_id", object[19]);
					subTask.put("eval_id", object[22]);

					subTaskArray.add(subTask);

					/* Start Filter Data */
					if (!checkEntiList.contains(object[13])) {
						JSONObject dataForAppendEntiArray = new JSONObject();
						dataForAppendEntiArray.put("orga_id", object[13]);
						dataForAppendEntiArray.put("orga_name", object[14]);

						entityarray.add(dataForAppendEntiArray);
						checkEntiList.add(Integer.parseInt(object[13].toString()));
					}

					List<Integer> checkUnitForAdding = new ArrayList<>();
					checkUnitForAdding.add(Integer.parseInt(object[13].toString()));
					checkUnitForAdding.add(Integer.parseInt(object[15].toString()));

					if (!checUnitList.contains(checkUnitForAdding)) {
						JSONObject dataForAppendUnitArray = new JSONObject();
						dataForAppendUnitArray.put("loca_id", object[15]);
						dataForAppendUnitArray.put("loca_name", object[16]);
						dataForAppendUnitArray.put("orga_id", object[13]);

						unitarray.add(dataForAppendUnitArray);
						checUnitList.add(checkUnitForAdding);
					}

					List<Integer> checkFuncForAdding = new ArrayList<>();

					checkFuncForAdding.add(Integer.parseInt(object[13].toString()));
					checkFuncForAdding.add(Integer.parseInt(object[15].toString()));
					checkFuncForAdding.add(Integer.parseInt(object[17].toString()));

					if (!checFuncList.contains(checkFuncForAdding)) {
						JSONObject dataForAppendFuncArray = new JSONObject();
						dataForAppendFuncArray.put("dept_id", object[17]);
						dataForAppendFuncArray.put("dept_name", object[18]);
						dataForAppendFuncArray.put("orga_id", object[13]);
						dataForAppendFuncArray.put("loca_id", object[15]);

						funcarray.add(dataForAppendFuncArray);
						checFuncList.add(checkFuncForAdding);
					}

					List<Integer> checkExecForAdding = new ArrayList<>();

					checkExecForAdding.add(Integer.parseInt(object[19].toString()));
					checkExecForAdding.add(Integer.parseInt(object[13].toString()));
					checkExecForAdding.add(Integer.parseInt(object[15].toString()));
					checkExecForAdding.add(Integer.parseInt(object[17].toString()));

					if (!checExecList.contains(checkExecForAdding)) {
						JSONObject dataForAppendExecArray = new JSONObject();
						dataForAppendExecArray.put("user_id", object[19]);
						dataForAppendExecArray.put("user_name", object[20].toString() + " " + object[21].toString());
						dataForAppendExecArray.put("orga_id", object[13]);
						dataForAppendExecArray.put("loca_id", object[15]);
						dataForAppendExecArray.put("dept_id", object[17]);

						execarray.add(dataForAppendExecArray);
						checExecList.add(checkExecForAdding);
					}

					List<Integer> checkEvalForAdding = new ArrayList<>();

					checkEvalForAdding.add(Integer.parseInt(object[13].toString()));
					checkEvalForAdding.add(Integer.parseInt(object[15].toString()));
					checkEvalForAdding.add(Integer.parseInt(object[17].toString()));
					checkEvalForAdding.add(Integer.parseInt(object[22].toString()));

					if (!checEvalList.contains(checkEvalForAdding)) {
						JSONObject dataForAppendEvalArray = new JSONObject();
						dataForAppendEvalArray.put("user_id", object[22]);
						dataForAppendEvalArray.put("user_name", object[23].toString() + " " + object[24].toString());
						dataForAppendEvalArray.put("orga_id", object[13]);
						dataForAppendEvalArray.put("loca_id", object[15]);
						dataForAppendEvalArray.put("dept_id", object[17]);

						evalarray.add(dataForAppendEvalArray);
						checEvalList.add(checkEvalForAdding);
					}

					List<Integer> checkLegiForAdding = new ArrayList<>();

					checkLegiForAdding.add(Integer.parseInt(object[1].toString()));

					if (!checLegiList.contains(checkLegiForAdding)) {
						JSONObject dataForAppendLegiArray = new JSONObject();
						dataForAppendLegiArray.put("task_legi_id", object[1]);
						dataForAppendLegiArray.put("task_legi_name", object[2]);

						legiarray.add(dataForAppendLegiArray);
						checLegiList.add(checkLegiForAdding);
					}

					List<Integer> checkRuleForAdding = new ArrayList<>();

					checkRuleForAdding.add(Integer.parseInt(object[1].toString()));
					checkRuleForAdding.add(Integer.parseInt(object[3].toString()));

					if (!checRuleList.contains(checkRuleForAdding)) {
						JSONObject dataForAppendRuleArray = new JSONObject();
						dataForAppendRuleArray.put("task_rule_id", object[3]);
						dataForAppendRuleArray.put("task_rule_name", object[4]);
						dataForAppendRuleArray.put("task_legi_id", object[1]);

						rulearray.add(dataForAppendRuleArray);
						checRuleList.add(checkRuleForAdding);
					}

				}

			}
			dataToSend.put("SUBTASK", subTaskArray);
			dataToSend.put("Entity", entityarray);
			dataToSend.put("Unit", unitarray);
			dataToSend.put("Function", funcarray);
			dataToSend.put("Executor", execarray);
			dataToSend.put("Evaluator", evalarray);
			dataToSend.put("Legislation", legiarray);
			dataToSend.put("Rule", rulearray);
			return dataToSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			dataToSend.put("responseMessage", "Failed");
			return dataToSend.toJSONString();
		}
	}

	// Method Created By: Harshad Padole
	// Method Purpose: Upload sub task against client task id
	@SuppressWarnings("unchecked")
	@Override
	public String uploadSubTask(String json, MultipartFile file, HttpSession session) {
		JSONObject dataToSend = new JSONObject();
		try {

			JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);

			String clientTaskID = jsonObject.get("clientTaskId").toString();
			if (!file.isEmpty()) {

				JSONArray neglectedTask = new JSONArray();
				JSONArray emptyFields = new JSONArray();
				JSONArray addedTask = new JSONArray();

				byte[] bytes = file.getBytes();

				// Create Temp File
				File temp = File.createTempFile(file.getName(), ".csv");

				String absolutePath = temp.getAbsolutePath();
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(absolutePath));
				stream.write(bytes);
				stream.close();
				CsvReader task = new CsvReader(absolutePath);

				task.readHeaders();

				final String[] frequency = new String[] { "One_Time", "Ongoing", "Weekly", "Fortnightly", "Monthly",
						"Two_Monthly", "Quarterly", "Four_Monthly", "Five_Monthly", "Half_Yearly", "Seven_Monthly",
						"Eight_Monthly", "Nine_Monthly", "Ten_Monthly", "Yearly", "Fourteen_Monthly",
						"Eighteen_Monthly", "Two_Yearly", "Three_Yearly", "Four_Yearly", "Five_Yearly", "Six_Yearly",
						"Event_Based", "Seven_Yearly", "Eight_Yearly", "Nine_Yearly", "Ten_Yearly", "Twenty_Yearly" };
				int i = 2;
				while (task.readRecord()) {

					/*
					 * String equipment_number = task.get("Equipment Number"); String equipment_type
					 * = task.get("Equipment Type"); String equipment_decs =
					 * task.get("Equipment Description"); String equipment_loca =
					 * task.get("Equipment Location"); String examination_date =
					 * task.get("Last Examination Date"); String equipment_frequency =
					 * task.get("Frequency");
					 */

					String equipment_number = task.get("Name Of Contractor");
					String equipment_type = task.get("Compliance Title");
					String equipment_decs = task.get("Compliance Activity");
					String equipment_loca = task.get("Unit");
					// String examination_date = task.get("Due Date");
					String equipment_frequency = task.get("Frequency");

					JSONObject objForAppend = new JSONObject();
					if (equipment_number != null && !equipment_number.isEmpty() && equipment_type != null
							&& !equipment_type.isEmpty() && equipment_loca != null && !equipment_loca.isEmpty()) {

						if (Arrays.asList(frequency).contains(task.get("Frequency"))) {

							List<Object> res = subTaskDao.checkTaskExist(clientTaskID, equipment_number);

							if (res.size() == 0) {

								int last_genrated_id = subTaskDao.getLastGeneratedValue(clientTaskID);
								last_genrated_id++;
								SubTask subTask = new SubTask();
								subTask.setSub_client_task_id(clientTaskID);
								subTask.setSub_equipment_number(equipment_number);
								subTask.setSub_equipment_type(equipment_type);
								subTask.setSub_equipment_description(equipment_decs);
								subTask.setSub_equipment_location(equipment_loca);
								// subTask.setSub_last_examination_date(examination_date);
								subTask.setSub_frequency(equipment_frequency);
								subTask.setSub_last_generated_id(last_genrated_id);
								subTask.setSub_task_id(clientTaskID + "_" + last_genrated_id);

								subTaskDao.saveObject(subTask);

								objForAppend.put("Imported Task", task.get("Name Of Contractor"));
								addedTask.add(objForAppend);
							} else {

								objForAppend.put("Reason_for_negligence", "Already Exist.");
								objForAppend.put("Neglected_Equipment_Number", task.get("Name Of Contractor"));
								neglectedTask.add(objForAppend);
							}

						} else {
							objForAppend.put("Reason_for_negligence", "Frequency incorrect");
							objForAppend.put("Neglected_Equipment_Number", task.get("Name Of Contractor"));
							neglectedTask.add(objForAppend);
						}
					} else {
						objForAppend.put("Reason_for_negligence", "Empty fields are not allowed.");
						objForAppend.put("Row_No", i);
						emptyFields.add(objForAppend);
					}
					i++;
				}
				dataToSend.put("Neglected_Task", neglectedTask);
				dataToSend.put("Added_Task", addedTask);
				dataToSend.put("Empty_Fields", emptyFields);
			}

			return dataToSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			dataToSend.put("responseMessage", "Failed");
			return dataToSend.toJSONString();
		}
	}

	// Method Created By: Harshad Padole
	// Method Purpose: Get all imported sub task from master
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String importedSubTask(String json, HttpSession session) {
		JSONObject dataToSend = new JSONObject();
		try {
			List<Object> res = subTaskDao.getImportedTask(session);
			JSONArray subTaskArray = new JSONArray();

			JSONArray entityarray = new JSONArray();
			JSONArray unitarray = new JSONArray();
			JSONArray funcarray = new JSONArray();
			JSONArray execarray = new JSONArray();
			JSONArray evalarray = new JSONArray();
			JSONArray legiarray = new JSONArray();
			JSONArray rulearray = new JSONArray();
			JSONArray equipTypearray = new JSONArray();
			JSONArray equipNumbarray = new JSONArray();
			JSONArray equipLocaarray = new JSONArray();
			JSONArray equipFreqarray = new JSONArray();

			if (res != null) {

				List<Integer> checkEntiList = new ArrayList<>();
				List<List> checUnitList = new ArrayList<>();
				List<List> checFuncList = new ArrayList<>();
				List<List> checExecList = new ArrayList<>();
				List<List> checEvalList = new ArrayList<>();
				List<List> checLegiList = new ArrayList<>();
				List<List> checRuleList = new ArrayList<>();
				List<List> checEquipType = new ArrayList<>();
				List<List> checEquipNumb = new ArrayList<>();
				List<List> checEquipLoca = new ArrayList<>();
				List<List> checEquipFreq = new ArrayList<>();

				Iterator<Object> itr = res.iterator();
				while (itr.hasNext()) {
					Object[] object = (Object[]) itr.next();
					JSONObject subTask = new JSONObject();

					subTask.put("clientTaskId", object[0]);
					subTask.put("task_legi_name", object[2]);
					subTask.put("task_rule_name", object[4]);
					subTask.put("task_who", object[5]);
					subTask.put("task_when", object[6]);
					;
					subTask.put("task_reference", object[7]);
					subTask.put("task_activity", object[8]);
					subTask.put("task_procedure", object[9]);
					subTask.put("task_impact", object[10]);
					subTask.put("task_frequency", object[11]);
					subTask.put("task_legi_id", object[1]);
					subTask.put("task_rule_id", object[3]);
					subTask.put("orga_id", object[12]);
					subTask.put("loca_id", object[14]);
					subTask.put("dept_id", object[16]);
					subTask.put("exec_id", object[18]);
					subTask.put("eval_id", object[21]);
					subTask.put("equi_type", object[24]);
					subTask.put("equi_number", object[25]);
					subTask.put("equi_loca", object[26]);
					subTask.put("equi_desc", object[27]);
					subTask.put("equi_freq", object[28]);
					subTask.put("equi_last_examination_date", object[29]);
					subTask.put("equi_sub_task_id", object[30]);
					subTaskArray.add(subTask);

					/* Start Filter Data */
					if (!checkEntiList.contains(object[12])) {
						JSONObject dataForAppendEntiArray = new JSONObject();
						dataForAppendEntiArray.put("orga_id", object[12]);
						dataForAppendEntiArray.put("orga_name", object[13]);

						entityarray.add(dataForAppendEntiArray);
						checkEntiList.add(Integer.parseInt(object[12].toString()));
					}

					List<Integer> checkUnitForAdding = new ArrayList<>();
					checkUnitForAdding.add(Integer.parseInt(object[12].toString()));
					checkUnitForAdding.add(Integer.parseInt(object[14].toString()));

					if (!checUnitList.contains(checkUnitForAdding)) {
						JSONObject dataForAppendUnitArray = new JSONObject();
						dataForAppendUnitArray.put("loca_id", object[14]);
						dataForAppendUnitArray.put("loca_name", object[15]);
						dataForAppendUnitArray.put("orga_id", object[12]);

						unitarray.add(dataForAppendUnitArray);
						checUnitList.add(checkUnitForAdding);
					}

					List<Integer> checkFuncForAdding = new ArrayList<>();

					checkFuncForAdding.add(Integer.parseInt(object[12].toString()));
					checkFuncForAdding.add(Integer.parseInt(object[14].toString()));
					checkFuncForAdding.add(Integer.parseInt(object[16].toString()));

					if (!checFuncList.contains(checkFuncForAdding)) {
						JSONObject dataForAppendFuncArray = new JSONObject();
						dataForAppendFuncArray.put("dept_id", object[16]);
						dataForAppendFuncArray.put("dept_name", object[17]);
						dataForAppendFuncArray.put("orga_id", object[12]);
						dataForAppendFuncArray.put("loca_id", object[14]);

						funcarray.add(dataForAppendFuncArray);
						checFuncList.add(checkFuncForAdding);
					}

					List<Integer> checkExecForAdding = new ArrayList<>();

					checkExecForAdding.add(Integer.parseInt(object[18].toString()));
					checkExecForAdding.add(Integer.parseInt(object[12].toString()));
					checkExecForAdding.add(Integer.parseInt(object[14].toString()));
					checkExecForAdding.add(Integer.parseInt(object[16].toString()));

					if (!checExecList.contains(checkExecForAdding)) {
						JSONObject dataForAppendExecArray = new JSONObject();
						dataForAppendExecArray.put("user_id", object[18]);
						dataForAppendExecArray.put("user_name", object[19].toString() + " " + object[20].toString());
						dataForAppendExecArray.put("orga_id", object[12]);
						dataForAppendExecArray.put("loca_id", object[14]);
						dataForAppendExecArray.put("dept_id", object[16]);

						execarray.add(dataForAppendExecArray);
						checExecList.add(checkExecForAdding);
					}

					List<Integer> checkEvalForAdding = new ArrayList<>();

					checkEvalForAdding.add(Integer.parseInt(object[12].toString()));
					checkEvalForAdding.add(Integer.parseInt(object[14].toString()));
					checkEvalForAdding.add(Integer.parseInt(object[16].toString()));
					checkEvalForAdding.add(Integer.parseInt(object[21].toString()));

					if (!checEvalList.contains(checkEvalForAdding)) {
						JSONObject dataForAppendEvalArray = new JSONObject();
						dataForAppendEvalArray.put("user_id", object[21]);
						dataForAppendEvalArray.put("user_name", object[22].toString() + " " + object[23].toString());
						dataForAppendEvalArray.put("orga_id", object[12]);
						dataForAppendEvalArray.put("loca_id", object[14]);
						dataForAppendEvalArray.put("dept_id", object[16]);

						evalarray.add(dataForAppendEvalArray);
						checEvalList.add(checkEvalForAdding);
					}

					List<Integer> checkLegiForAdding = new ArrayList<>();

					checkLegiForAdding.add(Integer.parseInt(object[1].toString()));

					if (!checLegiList.contains(checkLegiForAdding)) {
						JSONObject dataForAppendLegiArray = new JSONObject();
						dataForAppendLegiArray.put("task_legi_id", object[1]);
						dataForAppendLegiArray.put("task_legi_name", object[2]);

						legiarray.add(dataForAppendLegiArray);
						checLegiList.add(checkLegiForAdding);
					}

					List<Integer> checkRuleForAdding = new ArrayList<>();

					checkRuleForAdding.add(Integer.parseInt(object[1].toString()));
					checkRuleForAdding.add(Integer.parseInt(object[3].toString()));

					if (!checRuleList.contains(checkRuleForAdding)) {
						JSONObject dataForAppendRuleArray = new JSONObject();
						dataForAppendRuleArray.put("task_rule_id", object[3]);
						dataForAppendRuleArray.put("task_rule_name", object[4]);
						dataForAppendRuleArray.put("task_legi_id", object[1]);

						rulearray.add(dataForAppendRuleArray);
						checRuleList.add(checkRuleForAdding);
					}

					List<String> checkEquipType = new ArrayList<String>();
					checkEquipType.add(object[24].toString());
					if (!checEquipType.contains(checkEquipType)) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("equip_type", object[24]);
						equipTypearray.add(jsonObject);
						checEquipType.add(checkEquipType);
					}

					List<String> checkEquipNumber = new ArrayList<String>();
					checkEquipNumber.add(object[24].toString());
					checkEquipNumber.add(object[25].toString());
					if (!checEquipNumb.contains(checkEquipNumber)) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("equip_type", object[24]);
						jsonObject.put("equip_number", object[25]);
						equipNumbarray.add(jsonObject);
						checEquipNumb.add(checkEquipNumber);
					}

					List<String> checkEquipLoca = new ArrayList<String>();
					checkEquipLoca.add(object[24].toString());
					checkEquipLoca.add(object[25].toString());
					checkEquipLoca.add(object[26].toString());
					if (!checEquipLoca.contains(checkEquipLoca)) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("equip_type", object[24]);
						jsonObject.put("equip_number", object[25]);
						jsonObject.put("equip_loca", object[26]);
						equipLocaarray.add(jsonObject);
						checEquipLoca.add(checkEquipLoca);
					}

					List<String> checkEquipFreq = new ArrayList<String>();
					checkEquipFreq.add(object[24].toString());
					checkEquipFreq.add(object[25].toString());
					checkEquipFreq.add(object[26].toString());
					checkEquipFreq.add(object[28].toString());
					if (!checEquipFreq.contains(checkEquipFreq)) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("equip_type", object[24]);
						jsonObject.put("equip_number", object[25]);
						jsonObject.put("equip_loca", object[26]);
						jsonObject.put("equip_Freq", object[28]);
						equipFreqarray.add(jsonObject);
						checEquipFreq.add(checkEquipFreq);
					}
				}

			}
			dataToSend.put("ImportedSubTask", subTaskArray);
			dataToSend.put("Entity", entityarray);
			dataToSend.put("Unit", unitarray);
			dataToSend.put("Function", funcarray);
			dataToSend.put("Executor", execarray);
			dataToSend.put("Evaluator", evalarray);
			dataToSend.put("Legislation", legiarray);
			dataToSend.put("Rule", rulearray);
			dataToSend.put("Equip_Type", equipTypearray);
			dataToSend.put("Equip_Loca", equipLocaarray);
			dataToSend.put("Equip_Number", equipNumbarray);
			dataToSend.put("Equip_Freq", equipFreqarray);
			return dataToSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			dataToSend.put("responseMessage", "Failed");
			return dataToSend.toJSONString();
		}
	}

	// Method Created By: Harshad Padole
	// Method Purpose: Save sub task configuration
	@SuppressWarnings("unchecked")
	@Override
	public String saveSubtTaskConfiguration(String json, HttpSession session) {
		JSONObject dataToSend = new JSONObject();
		try {

			JSONObject jsonObj = (JSONObject) new JSONParser().parse(json);

			JSONArray dataForSend = new JSONArray();

			// Date ttrn_next_examination_date =
			// sdfOut.parse(jsonObj.get("ttrn_next_examination_date").toString());
			Date ttrn_legal_due_date = sdfOut.parse(jsonObj.get("ttrn_legal_due_date").toString());
			Date ttrn_fh_due_date = sdfOut.parse(jsonObj.get("ttrn_fh_due_date").toString());
			Date ttrn_pr_due_date = sdfOut.parse(jsonObj.get("ttrn_pr_due_date").toString());
			Date ttrn_rw_due_date = sdfOut.parse(jsonObj.get("ttrn_rw_due_date").toString());
			Date ttrn_uh_due_date = sdfOut.parse(jsonObj.get("ttrn_uh_due_date").toString());
			int ttrn_document = Integer.parseInt(jsonObj.get("ttrn_document").toString());
			int ttrn_historical = Integer.parseInt(jsonObj.get("ttrn_historical").toString());

			int ttrn_prior_days_buffer = Integer.parseInt(jsonObj.get("ttrn_prior_days_buffer").toString());
			int ttrn_alert_days = Integer.parseInt(jsonObj.get("ttrn_alert_days").toString());
			// String ttrn_allow_back_date_completion =
			// jsonObj.get("ttrn_allow_back_date_completion").toString();
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
				JSONObject objForAppend = new JSONObject();
				String ttrn_client_task_id = configured_tasks_obj.get("ttrn_client_task_id").toString();
				String ttrn_sub_task_id = configured_tasks_obj.get("ttrn_sub_task_id").toString();
				// int ttrn_performer_user_id =
				// Integer.parseInt(configured_tasks_obj.get("ttrn_performer_user_id").toString());

				SubTaskTranscational taskTranscational = new SubTaskTranscational();

				taskTranscational.setTtrn_sub_task_updated_at(new Date());
				taskTranscational.setTtrn_sub_task_activation_date(null);
				taskTranscational.setTtrn_sub_task_alert_prior_day(ttrn_alert_days);
				// taskTranscational.setTtrn_sub_task_buffer_days();
				taskTranscational.setTtrn_sub_task_back_date_allowed(ttrn_no_of_back_days_allowed);
				taskTranscational.setTtrn_sub_client_task_id(ttrn_client_task_id);
				taskTranscational.setTtrn_sub_task_created_at(new Date());
				taskTranscational.setTtrn_sub_task_document(ttrn_document);
				taskTranscational.setTtrn_sub_task_historical(ttrn_historical);
				taskTranscational.setTtrn_sub_task_id(ttrn_sub_task_id);
				// taskTranscational.setTttn_sub_task_next_examination_date(ttrn_next_examination_date);
				taskTranscational.setTtrn_sub_task_ENT_due_date(ttrn_legal_due_date);
				taskTranscational.setTtrn_sub_task_UH_due_date(ttrn_uh_due_date);
				taskTranscational.setTtrn_sub_task_FH_due_date(ttrn_fh_due_date);
				taskTranscational.setTtrn_sub_task_rw_date(ttrn_rw_due_date);
				taskTranscational.setTtrn_sub_task_pr_due_date(ttrn_pr_due_date);
				taskTranscational.setTtrn_sub_task_first_alert(ttrn_first_alert);
				taskTranscational.setTtrn_sub_task_second_alert(ttrn_second_alert);
				taskTranscational.setTtrn_sub_task_third_alert(ttrn_third_alert);
				taskTranscational.setTtrn_sub_task_status("Inactive");
				taskTranscational.setTtrn_sub_task_buffer_days(ttrn_prior_days_buffer);
				taskTranscational.setTtrn_sub_task_allow_approver_reopening(ttrn_allow_approver_reopening);
				subTaskDao.saveObject(taskTranscational);
				objForAppend.put("successClientTasksId", ttrn_sub_task_id);
				dataForSend.add(objForAppend);

			}
			dataToSend.put("SuccessIds", dataForSend);

			return dataToSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			dataToSend.put("responseMessage", "Failed");
			return dataToSend.toJSONString();
		}

	}

	// Method Created By: Harshad Padole
	// Method Purpose: get task for configuration
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String getTaskForConfiguration(String json, HttpSession session) {
		JSONObject dataToSend = new JSONObject();
		try {
			JSONArray TaskForConfi = new JSONArray();

			JSONArray entityarray = new JSONArray();
			JSONArray unitarray = new JSONArray();
			JSONArray funcarray = new JSONArray();
			JSONArray execarray = new JSONArray();
			JSONArray evalarray = new JSONArray();
			JSONArray legiarray = new JSONArray();
			JSONArray rulearray = new JSONArray();
			JSONArray equipTypearray = new JSONArray();
			JSONArray equipNumbarray = new JSONArray();
			JSONArray equipLocaarray = new JSONArray();
			JSONArray equipFreqarray = new JSONArray();

			List<Object> res = subTaskDao.getTaskForConfiguration();

			if (res != null) {

				List<Integer> checkEntiList = new ArrayList<>();
				List<List> checUnitList = new ArrayList<>();
				List<List> checFuncList = new ArrayList<>();
				List<List> checExecList = new ArrayList<>();
				List<List> checEvalList = new ArrayList<>();
				List<List> checLegiList = new ArrayList<>();
				List<List> checRuleList = new ArrayList<>();
				List<List> checEquipType = new ArrayList<>();
				List<List> checEquipNumb = new ArrayList<>();
				List<List> checEquipLoca = new ArrayList<>();
				List<List> checEquipFreq = new ArrayList<>();
				Iterator<Object> itr = res.iterator();
				while (itr.hasNext()) {
					Object[] objects = (Object[]) itr.next();
					JSONObject JsonObj = new JSONObject();
					JsonObj.put("orga_id", objects[0]);
					JsonObj.put("orga_name", objects[1]);
					JsonObj.put("loca_id", objects[2]);
					JsonObj.put("loca_name", objects[3]);
					JsonObj.put("dept_id", objects[4]);
					JsonObj.put("dept_name", objects[5]);
					JsonObj.put("exec_id", objects[6]);
					JsonObj.put("eval_id", objects[9]);
					JsonObj.put("sub_task_id", objects[12]);
					JsonObj.put("sub_client_task_id", objects[13]);
					JsonObj.put("sub_equip_type", objects[14]);
					JsonObj.put("sub_equip_number", objects[15]);
					JsonObj.put("sub_equip_location", objects[16]);
					JsonObj.put("sub_equip_descri", objects[17]);
					JsonObj.put("sub_equip_frequency", objects[18]);
					JsonObj.put("sub_equip_last_examination_date", objects[19]);
					JsonObj.put("sub_legi_id", objects[20]);
					JsonObj.put("sub_rule_id", objects[22]);

					TaskForConfi.add(JsonObj);

					/* Start Filter Data */
					if (!checkEntiList.contains(objects[0])) {
						JSONObject dataForAppendEntiArray = new JSONObject();
						dataForAppendEntiArray.put("orga_id", objects[0]);
						dataForAppendEntiArray.put("orga_name", objects[1]);

						entityarray.add(dataForAppendEntiArray);
						checkEntiList.add(Integer.parseInt(objects[0].toString()));
					}

					List<Integer> checkUnitForAdding = new ArrayList<>();
					checkUnitForAdding.add(Integer.parseInt(objects[0].toString()));
					checkUnitForAdding.add(Integer.parseInt(objects[2].toString()));

					if (!checUnitList.contains(checkUnitForAdding)) {
						JSONObject dataForAppendUnitArray = new JSONObject();
						dataForAppendUnitArray.put("loca_id", objects[2]);
						dataForAppendUnitArray.put("loca_name", objects[3]);
						dataForAppendUnitArray.put("orga_id", objects[0]);

						unitarray.add(dataForAppendUnitArray);
						checUnitList.add(checkUnitForAdding);
					}

					List<Integer> checkFuncForAdding = new ArrayList<>();

					checkFuncForAdding.add(Integer.parseInt(objects[0].toString()));
					checkFuncForAdding.add(Integer.parseInt(objects[2].toString()));
					checkFuncForAdding.add(Integer.parseInt(objects[4].toString()));

					if (!checFuncList.contains(checkFuncForAdding)) {
						JSONObject dataForAppendFuncArray = new JSONObject();
						dataForAppendFuncArray.put("dept_id", objects[4]);
						dataForAppendFuncArray.put("dept_name", objects[5]);
						dataForAppendFuncArray.put("orga_id", objects[0]);
						dataForAppendFuncArray.put("loca_id", objects[2]);

						funcarray.add(dataForAppendFuncArray);
						checFuncList.add(checkFuncForAdding);
					}

					List<Integer> checkExecForAdding = new ArrayList<>();

					checkExecForAdding.add(Integer.parseInt(objects[6].toString()));
					checkExecForAdding.add(Integer.parseInt(objects[0].toString()));
					checkExecForAdding.add(Integer.parseInt(objects[2].toString()));
					checkExecForAdding.add(Integer.parseInt(objects[4].toString()));

					if (!checExecList.contains(checkExecForAdding)) {
						JSONObject dataForAppendExecArray = new JSONObject();
						dataForAppendExecArray.put("user_id", objects[6]);
						dataForAppendExecArray.put("user_name", objects[7].toString() + " " + objects[8].toString());
						dataForAppendExecArray.put("orga_id", objects[0]);
						dataForAppendExecArray.put("loca_id", objects[2]);
						dataForAppendExecArray.put("dept_id", objects[4]);

						execarray.add(dataForAppendExecArray);
						checExecList.add(checkExecForAdding);
					}

					List<Integer> checkEvalForAdding = new ArrayList<>();

					checkEvalForAdding.add(Integer.parseInt(objects[0].toString()));
					checkEvalForAdding.add(Integer.parseInt(objects[2].toString()));
					checkEvalForAdding.add(Integer.parseInt(objects[4].toString()));
					checkEvalForAdding.add(Integer.parseInt(objects[9].toString()));

					if (!checEvalList.contains(checkEvalForAdding)) {
						JSONObject dataForAppendEvalArray = new JSONObject();
						dataForAppendEvalArray.put("user_id", objects[9]);
						dataForAppendEvalArray.put("user_name", objects[10].toString() + " " + objects[11].toString());
						dataForAppendEvalArray.put("orga_id", objects[0]);
						dataForAppendEvalArray.put("loca_id", objects[2]);
						dataForAppendEvalArray.put("dept_id", objects[4]);

						evalarray.add(dataForAppendEvalArray);
						checEvalList.add(checkEvalForAdding);
					}

					List<Integer> checkLegiForAdding = new ArrayList<>();

					checkLegiForAdding.add(Integer.parseInt(objects[20].toString()));

					if (!checLegiList.contains(checkLegiForAdding)) {
						JSONObject dataForAppendLegiArray = new JSONObject();
						dataForAppendLegiArray.put("task_legi_id", objects[20]);
						dataForAppendLegiArray.put("task_legi_name", objects[21]);

						legiarray.add(dataForAppendLegiArray);
						checLegiList.add(checkLegiForAdding);
					}

					List<Integer> checkRuleForAdding = new ArrayList<>();

					checkRuleForAdding.add(Integer.parseInt(objects[20].toString()));
					checkRuleForAdding.add(Integer.parseInt(objects[22].toString()));

					if (!checRuleList.contains(checkRuleForAdding)) {
						JSONObject dataForAppendRuleArray = new JSONObject();
						dataForAppendRuleArray.put("task_rule_id", objects[22]);
						dataForAppendRuleArray.put("task_rule_name", objects[23]);
						dataForAppendRuleArray.put("task_legi_id", objects[20]);

						rulearray.add(dataForAppendRuleArray);
						checRuleList.add(checkRuleForAdding);
					}

					List<String> checkEquipType = new ArrayList<String>();
					checkEquipType.add(objects[14].toString());
					if (!checEquipType.contains(checkEquipType)) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("equip_type", objects[14]);
						equipTypearray.add(jsonObject);
						checEquipType.add(checkEquipType);
					}

					List<String> checkEquipNumber = new ArrayList<String>();
					checkEquipNumber.add(objects[14].toString());
					checkEquipNumber.add(objects[15].toString());
					if (!checEquipNumb.contains(checkEquipNumber)) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("equip_type", objects[14]);
						jsonObject.put("equip_number", objects[15]);
						equipNumbarray.add(jsonObject);
						checEquipNumb.add(checkEquipNumber);
					}

					List<String> checkEquipLoca = new ArrayList<String>();
					checkEquipLoca.add(objects[14].toString());
					checkEquipLoca.add(objects[15].toString());
					checkEquipLoca.add(objects[16].toString());
					if (!checEquipLoca.contains(checkEquipLoca)) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("equip_type", objects[14]);
						jsonObject.put("equip_number", objects[15]);
						jsonObject.put("equip_loca", objects[16]);
						equipLocaarray.add(jsonObject);
						checEquipLoca.add(checkEquipLoca);
					}

					List<String> checkEquipFreq = new ArrayList<String>();
					checkEquipFreq.add(objects[14].toString());
					checkEquipFreq.add(objects[15].toString());
					checkEquipFreq.add(objects[16].toString());
					checkEquipFreq.add(objects[18].toString());
					if (!checEquipFreq.contains(checkEquipFreq)) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("equip_type", objects[14]);
						jsonObject.put("equip_number", objects[15]);
						jsonObject.put("equip_loca", objects[16]);
						jsonObject.put("equip_Freq", objects[18]);
						equipFreqarray.add(jsonObject);
						checEquipFreq.add(checkEquipFreq);
					}

				}
			}
			dataToSend.put("Task", TaskForConfi);
			dataToSend.put("Entity", entityarray);
			dataToSend.put("Unit", unitarray);
			dataToSend.put("Function", funcarray);
			dataToSend.put("Executor", execarray);
			dataToSend.put("Evaluator", evalarray);
			dataToSend.put("Legislations", legiarray);
			dataToSend.put("Rules", rulearray);
			dataToSend.put("Equip_Type", equipTypearray);
			dataToSend.put("Equip_Loca", equipLocaarray);
			dataToSend.put("Equip_Number", equipNumbarray);
			dataToSend.put("Equip_Freq", equipFreqarray);
			return dataToSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			dataToSend.put("responseMessage", "Failed");
			return dataToSend.toJSONString();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String getConfiguredTask(String json, HttpSession session) {
		JSONObject dataToSend = new JSONObject();
		try {

			JSONArray TaskForConfi = new JSONArray();

			JSONArray entityarray = new JSONArray();
			JSONArray unitarray = new JSONArray();
			JSONArray funcarray = new JSONArray();
			JSONArray execarray = new JSONArray();
			JSONArray evalarray = new JSONArray();
			JSONArray legiarray = new JSONArray();
			JSONArray rulearray = new JSONArray();
			JSONArray equipTypearray = new JSONArray();
			JSONArray equipNumbarray = new JSONArray();
			JSONArray equipLocaarray = new JSONArray();
			JSONArray equipFreqarray = new JSONArray();

			List<Object> res = subTaskDao.getConfiguredTask(session);

			if (res != null) {

				List<Integer> checkEntiList = new ArrayList<>();
				List<List> checUnitList = new ArrayList<>();
				List<List> checFuncList = new ArrayList<>();
				List<List> checExecList = new ArrayList<>();
				List<List> checEvalList = new ArrayList<>();
				List<List> checLegiList = new ArrayList<>();
				List<List> checRuleList = new ArrayList<>();
				List<List> checEquipType = new ArrayList<>();
				List<List> checEquipNumb = new ArrayList<>();
				List<List> checEquipLoca = new ArrayList<>();
				List<List> checEquipFreq = new ArrayList<>();

				Iterator<Object> itr = res.iterator();
				while (itr.hasNext()) {
					Object[] objects = (Object[]) itr.next();
					JSONObject JsonObj = new JSONObject();
					JsonObj.put("orga_id", objects[0]);
					JsonObj.put("orga_name", objects[1]);
					JsonObj.put("loca_id", objects[2]);
					JsonObj.put("loca_name", objects[3]);
					JsonObj.put("dept_id", objects[4]);
					JsonObj.put("dept_name", objects[5]);
					JsonObj.put("exec_id", objects[6]);
					JsonObj.put("eval_id", objects[9]);
					JsonObj.put("sub_task_id", objects[12]);
					JsonObj.put("sub_client_task_id", objects[13]);
					JsonObj.put("sub_equip_type", objects[14]);
					JsonObj.put("sub_equip_number", objects[15]);
					JsonObj.put("sub_equip_location", objects[16]);
					JsonObj.put("sub_equip_descri", objects[17]);
					JsonObj.put("sub_equip_frequency", objects[18]);
					// JsonObj.put("sub_equip_last_examination_date", objects[19]);
					JsonObj.put("sub_legi_id", objects[20]);
					JsonObj.put("sub_rule_id", objects[22]);
					JsonObj.put("sub_id", objects[24]);
					JsonObj.put("sub_status", objects[25]);
					JsonObj.put("sub_ttrn_alert_prior_day", objects[27]);
					JsonObj.put("sub_ttrn_buffer_days", objects[28]);

					if (objects[29] != null) {
						JsonObj.put("sub_task_legal_due_date", sdfOut.format(sdfIn.parse(objects[29].toString())));
					}
					if (objects[30] != null) {
						JsonObj.put("sub_task_fh_due_date", sdfOut.format(sdfIn.parse(objects[30].toString())));
					}
					if (objects[31] != null) {
						JsonObj.put("sub_task_uh_due_date", sdfOut.format(sdfIn.parse(objects[31].toString())));
					}
					if (objects[32] != null) {
						JsonObj.put("sub_task_rw_due_date", sdfOut.format(sdfIn.parse(objects[32].toString())));
					}
					if (objects[33] != null) {
						JsonObj.put("sub_task_pr_due_date", sdfOut.format(sdfIn.parse(objects[33].toString())));
					}

					JsonObj.put("ttrn_sub_task_allow_approver_reopening", objects[34]);
					JsonObj.put("ttrn_sub_task_historical", objects[35]);
					JsonObj.put("ttrn_sub_task_document", objects[36]);
					JsonObj.put("ttrn_sub_task_back_date_allowed", objects[37]);

					if (objects[38] != null)
						JsonObj.put("ttrn_sub_task_first_alert", sdfOut.format(sdfIn.parse(objects[38].toString())));
					else
						JsonObj.put("ttrn_sub_task_first_alert", "");

					if (objects[39] != null)
						JsonObj.put("ttrn_sub_task_second_alert", sdfOut.format(sdfIn.parse(objects[39].toString())));
					else
						JsonObj.put("ttrn_sub_task_second_alert", "");

					if (objects[40] != null)
						JsonObj.put("ttrn_sub_task_third_alert", sdfOut.format(sdfIn.parse(objects[40].toString())));
					else
						JsonObj.put("ttrn_sub_task_third_alert", "");

					/*
					 * if (objects[26] != null) JsonObj.put("sub_task_next_examination_date",
					 * sdfOut.format(sdfIn.parse(objects[26].toString()))); else
					 * JsonObj.put("sub_task_next_examination_date", "NA");
					 */

					TaskForConfi.add(JsonObj);

					/* Start Filter Data */
					if (!checkEntiList.contains(objects[0])) {
						JSONObject dataForAppendEntiArray = new JSONObject();
						dataForAppendEntiArray.put("orga_id", objects[0]);
						dataForAppendEntiArray.put("orga_name", objects[1]);

						entityarray.add(dataForAppendEntiArray);
						checkEntiList.add(Integer.parseInt(objects[0].toString()));
					}

					List<Integer> checkUnitForAdding = new ArrayList<>();
					checkUnitForAdding.add(Integer.parseInt(objects[0].toString()));
					checkUnitForAdding.add(Integer.parseInt(objects[2].toString()));

					if (!checUnitList.contains(checkUnitForAdding)) {
						JSONObject dataForAppendUnitArray = new JSONObject();
						dataForAppendUnitArray.put("loca_id", objects[2]);
						dataForAppendUnitArray.put("loca_name", objects[3]);
						dataForAppendUnitArray.put("orga_id", objects[0]);

						unitarray.add(dataForAppendUnitArray);
						checUnitList.add(checkUnitForAdding);
					}

					List<Integer> checkFuncForAdding = new ArrayList<>();

					checkFuncForAdding.add(Integer.parseInt(objects[0].toString()));
					checkFuncForAdding.add(Integer.parseInt(objects[2].toString()));
					checkFuncForAdding.add(Integer.parseInt(objects[4].toString()));

					if (!checFuncList.contains(checkFuncForAdding)) {
						JSONObject dataForAppendFuncArray = new JSONObject();
						dataForAppendFuncArray.put("dept_id", objects[4]);
						dataForAppendFuncArray.put("dept_name", objects[5]);
						dataForAppendFuncArray.put("orga_id", objects[0]);
						dataForAppendFuncArray.put("loca_id", objects[2]);

						funcarray.add(dataForAppendFuncArray);
						checFuncList.add(checkFuncForAdding);
					}

					List<Integer> checkExecForAdding = new ArrayList<>();

					checkExecForAdding.add(Integer.parseInt(objects[6].toString()));
					checkExecForAdding.add(Integer.parseInt(objects[0].toString()));
					checkExecForAdding.add(Integer.parseInt(objects[2].toString()));
					checkExecForAdding.add(Integer.parseInt(objects[4].toString()));

					if (!checExecList.contains(checkExecForAdding)) {
						JSONObject dataForAppendExecArray = new JSONObject();
						dataForAppendExecArray.put("user_id", objects[6]);
						dataForAppendExecArray.put("user_name", objects[7].toString() + " " + objects[8].toString());
						dataForAppendExecArray.put("orga_id", objects[0]);
						dataForAppendExecArray.put("loca_id", objects[2]);
						dataForAppendExecArray.put("dept_id", objects[4]);

						execarray.add(dataForAppendExecArray);
						checExecList.add(checkExecForAdding);
					}

					List<Integer> checkEvalForAdding = new ArrayList<>();

					checkEvalForAdding.add(Integer.parseInt(objects[0].toString()));
					checkEvalForAdding.add(Integer.parseInt(objects[2].toString()));
					checkEvalForAdding.add(Integer.parseInt(objects[4].toString()));
					checkEvalForAdding.add(Integer.parseInt(objects[9].toString()));

					if (!checEvalList.contains(checkEvalForAdding)) {
						JSONObject dataForAppendEvalArray = new JSONObject();
						dataForAppendEvalArray.put("user_id", objects[9]);
						dataForAppendEvalArray.put("user_name", objects[10].toString() + " " + objects[11].toString());
						dataForAppendEvalArray.put("orga_id", objects[0]);
						dataForAppendEvalArray.put("loca_id", objects[2]);
						dataForAppendEvalArray.put("dept_id", objects[4]);

						evalarray.add(dataForAppendEvalArray);
						checEvalList.add(checkEvalForAdding);
					}

					List<Integer> checkLegiForAdding = new ArrayList<>();

					checkLegiForAdding.add(Integer.parseInt(objects[20].toString()));

					if (!checLegiList.contains(checkLegiForAdding)) {
						JSONObject dataForAppendLegiArray = new JSONObject();
						dataForAppendLegiArray.put("task_legi_id", objects[20]);
						dataForAppendLegiArray.put("task_legi_name", objects[21]);

						legiarray.add(dataForAppendLegiArray);
						checLegiList.add(checkLegiForAdding);
					}

					List<Integer> checkRuleForAdding = new ArrayList<>();

					checkRuleForAdding.add(Integer.parseInt(objects[20].toString()));
					checkRuleForAdding.add(Integer.parseInt(objects[22].toString()));

					if (!checRuleList.contains(checkRuleForAdding)) {
						JSONObject dataForAppendRuleArray = new JSONObject();
						dataForAppendRuleArray.put("task_rule_id", objects[22]);
						dataForAppendRuleArray.put("task_rule_name", objects[23]);
						dataForAppendRuleArray.put("task_legi_id", objects[20]);

						rulearray.add(dataForAppendRuleArray);
						checRuleList.add(checkRuleForAdding);
					}

					List<String> checkEquipType = new ArrayList<String>();
					checkEquipType.add(objects[14].toString());
					if (!checEquipType.contains(checkEquipType)) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("equip_type", objects[14]);
						equipTypearray.add(jsonObject);
						checEquipType.add(checkEquipType);
					}

					List<String> checkEquipNumber = new ArrayList<String>();
					checkEquipNumber.add(objects[14].toString());
					checkEquipNumber.add(objects[15].toString());
					if (!checEquipNumb.contains(checkEquipNumber)) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("equip_type", objects[14]);
						jsonObject.put("equip_number", objects[15]);
						equipNumbarray.add(jsonObject);
						checEquipNumb.add(checkEquipNumber);
					}

					List<String> checkEquipLoca = new ArrayList<String>();
					checkEquipLoca.add(objects[14].toString());
					checkEquipLoca.add(objects[15].toString());
					checkEquipLoca.add(objects[16].toString());
					if (!checEquipLoca.contains(checkEquipLoca)) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("equip_type", objects[14]);
						jsonObject.put("equip_number", objects[15]);
						jsonObject.put("equip_loca", objects[16]);
						equipLocaarray.add(jsonObject);
						checEquipLoca.add(checkEquipLoca);
					}

					List<String> checkEquipFreq = new ArrayList<String>();
					checkEquipFreq.add(objects[14].toString());
					checkEquipFreq.add(objects[15].toString());
					checkEquipFreq.add(objects[16].toString());
					checkEquipFreq.add(objects[18].toString());
					if (!checEquipFreq.contains(checkEquipFreq)) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("equip_type", objects[14]);
						jsonObject.put("equip_number", objects[15]);
						jsonObject.put("equip_loca", objects[16]);
						jsonObject.put("equip_Freq", objects[18]);
						equipFreqarray.add(jsonObject);
						checEquipFreq.add(checkEquipFreq);
					}

				}
			}
			dataToSend.put("Task", TaskForConfi);
			dataToSend.put("Entity", entityarray);
			dataToSend.put("Unit", unitarray);
			dataToSend.put("Function", funcarray);
			dataToSend.put("Executor", execarray);
			dataToSend.put("Evaluator", evalarray);
			dataToSend.put("Legislations", legiarray);
			dataToSend.put("Rules", rulearray);
			dataToSend.put("Equip_Type", equipTypearray);
			dataToSend.put("Equip_Loca", equipLocaarray);
			dataToSend.put("Equip_Number", equipNumbarray);
			dataToSend.put("Equip_Freq", equipFreqarray);
			return dataToSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			dataToSend.put("responseMessage", "Failed");
			return dataToSend.toJSONString();
		}
	}

	// Method Written By: Harshad Padole
	// Method Purpose: Activate or deactivate task
	@SuppressWarnings("unchecked")
	@Override
	public String activateDeactivateSubTask(String json, HttpSession session) {
		JSONObject dataToSend = new JSONObject();
		try {
			JSONArray successIDs = new JSONArray();
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);
			String status = jsonObject.get("status").toString();
			JSONArray configured_tasks = (JSONArray) jsonObject.get("tasks_list");
			for (int i = 0; i < configured_tasks.size(); i++) {
				JSONObject configured_tasks_obj = (JSONObject) configured_tasks.get(i);
				JSONObject objForAppend = new JSONObject();
				int ttrn_sub_id = Integer.parseInt(configured_tasks_obj.get("ttrn_sub_id").toString());

				SubTaskTranscational subTaskTranscational = subTaskDao.getTaskConfigurationById(ttrn_sub_id);
				subTaskTranscational.setTtrn_sub_task_status(status);
				if (status.equals("Active"))
					subTaskTranscational.setTtrn_sub_task_activation_date(new Date());

				subTaskTranscational.setTtrn_sub_task_updated_at(new Date());
				subTaskDao.updateObject(subTaskTranscational);

				objForAppend.put("id", subTaskTranscational.getTtrn_sub_id());
				successIDs.add(objForAppend);
			}
			dataToSend.put("successIDs", successIDs);
			return dataToSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			dataToSend.put("responseMessage", "Failed");
			return dataToSend.toJSONString();
		}
	}

	@Override
	public String updateSubTasksConfigurationDates(String jsonString, HttpSession session) {
		JSONObject objForSend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int sub_id = Integer.parseInt(jsonObj.get("sub_id").toString());
			System.out.println("sub_id:" + sub_id);
			String user_name = session.getAttribute("sess_user_full_name").toString();
			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());

			SubTaskTranscational taskTransactional = subTaskDao.getSubTaskForCompletion(sub_id);

			if (jsonObj.get("ttrn_frequency_for_operation") != null) {
				Date ttrn_pr_due_date = sdfOut.parse(jsonObj.get("ttrn_pr_due_date").toString());
				Date ttrn_rw_due_date = sdfOut.parse(jsonObj.get("ttrn_rw_due_date").toString());
				Date ttrn_fh_due_date = sdfOut.parse(jsonObj.get("ttrn_fh_due_date").toString());
				Date ttrn_uh_due_date = sdfOut.parse(jsonObj.get("ttrn_uh_due_date").toString());
				Date ttrn_legal_due_date = sdfOut.parse(jsonObj.get("ttrn_legal_due_date").toString());

				String ttrn_document = jsonObj.get("ttrn_document").toString();
				String ttrn_historical = jsonObj.get("ttrn_historical").toString();

				String ttrn_frequency_for_alerts = jsonObj.get("ttrn_frequency_for_alerts").toString();
				String ttrn_frequency_for_operation = jsonObj.get("ttrn_frequency_for_operation").toString();
				int ttrn_prior_days_buffer = Integer.parseInt(jsonObj.get("ttrn_prior_days_buffer").toString());
				int ttrn_alert_days = Integer.parseInt(jsonObj.get("ttrn_alert_days").toString());
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

				int ttrn_no_of_back_days_allowed = Integer
						.parseInt(jsonObj.get("ttrn_no_of_back_days_allowed").toString());
				String ttrn_allow_approver_reopening = jsonObj.get("ttrn_allow_approver_reopening").toString();

				taskTransactional.setTtrn_sub_task_alert_prior_day(ttrn_alert_days);
				taskTransactional.setTtrn_sub_task_allow_approver_reopening(ttrn_allow_approver_reopening);
				taskTransactional.setTtrn_sub_task_document(Integer.parseInt(ttrn_document));
				taskTransactional.setTtrn_sub_task_FH_due_date(ttrn_fh_due_date);
				taskTransactional.setTtrn_sub_task_first_alert(ttrn_first_alert);

				taskTransactional.setTtrn_sub_task_historical(Integer.parseInt(ttrn_historical));

				taskTransactional.setTtrn_sub_task_ENT_due_date(ttrn_legal_due_date);
				taskTransactional.setTtrn_sub_task_back_date_allowed(ttrn_no_of_back_days_allowed);
				taskTransactional.setTtrn_sub_task_pr_due_date(ttrn_pr_due_date);
				taskTransactional.setTtrn_sub_task_buffer_days(ttrn_prior_days_buffer);
				taskTransactional.setTtrn_sub_task_rw_date(ttrn_rw_due_date);
				taskTransactional.setTtrn_sub_task_second_alert(ttrn_second_alert);
				taskTransactional.setTtrn_sub_task_third_alert(ttrn_third_alert);
				taskTransactional.setTtrn_sub_task_UH_due_date(ttrn_uh_due_date);
				taskTransactional.setTtrn_sub_task_updated_at(new Date());

				subTaskDao.updateTaskConfiguration(taskTransactional);

				SubTask task = subTaskDao.getTaskToChangeTheFrequency(taskTransactional.getTtrn_sub_task_id());

				task.setSub_frequency(ttrn_frequency_for_operation);
				task.setSub_client_task_id(task.getSub_client_task_id());
				task.setSub_equipment_description(task.getSub_equipment_description());
				task.setSub_equipment_location(task.getSub_equipment_location());
				task.setSub_equipment_number(task.getSub_equipment_number());
				task.setSub_equipment_type(task.getSub_equipment_type());
				task.setSub_task_id(task.getSub_task_id());
				task.setSub_last_generated_id(task.getSub_last_generated_id());

				subTaskDao.saveConfiguration(task);

			} else {
				Date ttrn_pr_due_date = sdfOut.parse(jsonObj.get("ttrn_pr_due_date").toString());
				Date ttrn_rw_due_date = sdfOut.parse(jsonObj.get("ttrn_rw_due_date").toString());
				Date ttrn_fh_due_date = sdfOut.parse(jsonObj.get("ttrn_fh_due_date").toString());
				Date ttrn_uh_due_date = sdfOut.parse(jsonObj.get("ttrn_uh_due_date").toString());
				Date ttrn_legal_due_date = sdfOut.parse(jsonObj.get("ttrn_legal_due_date").toString());

				taskTransactional.setTtrn_sub_task_pr_due_date(ttrn_pr_due_date);
				taskTransactional.setTtrn_sub_task_rw_date(ttrn_rw_due_date);
				taskTransactional.setTtrn_sub_task_FH_due_date(ttrn_fh_due_date);
				taskTransactional.setTtrn_sub_task_UH_due_date(ttrn_uh_due_date);
				taskTransactional.setTtrn_sub_task_ENT_due_date(ttrn_legal_due_date);

				subTaskDao.updateTaskConfiguration(taskTransactional);
			}

			utilitiesService.addTaskCofigurationLog(taskTransactional.getTtrn_sub_task_id(), user_id, user_name,
					"MainTask", "Update");

			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			return objForSend.toJSONString();
		}
	}

	@Override
	public String approveSubTask(String json, HttpSession session) {

		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(json);

			String documentDownloadStatus = subTaskDao.getDocumentDownloadStatus(jsonObj.get("ttrn_id").toString());

			if (documentDownloadStatus == "Approved") {
				System.out.println("Attached are downloaded");

				int ttrn_sub_id = Integer.parseInt(jsonObj.get("ttrn_id").toString());
				// TaskTransactional taskTransactional =
				// tasksconfigurationdao.getTasksForCompletion(ttrn_id);
				SubTaskTranscational subTasktransactional = subTaskDao.getTaskForCompletion(ttrn_sub_id);
				int noOfBackDaysAllowed = subTasktransactional.getTtrn_sub_task_back_date_allowed();

				if (noOfBackDaysAllowed > 0) {
					Date currentDate = new Date();
					if (currentDate.after(subTasktransactional.getTtrn_sub_task_ENT_due_date())) {
						long diff = currentDate.getTime()
								- subTasktransactional.getTtrn_sub_task_ENT_due_date().getTime();
						int differenceDays = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
						if ((noOfBackDaysAllowed - differenceDays) >= 0) {
							subTasktransactional.setTtrn_sub_task_submition_date(
									subTasktransactional.getTtrn_sub_task_submition_date());
						} else {
							subTasktransactional.setTtrn_sub_task_submition_date(new Date());
						}
					}

				} else {
					subTasktransactional.setTtrn_sub_task_submition_date(new Date());
				}
				subTasktransactional.setTtrn_sub_task_status("Completed");
				subTasktransactional.setTtrn_sub_task_approved_date(new Date());
				subTasktransactional.setTtrn_sub_task_approved_by(
						Integer.parseInt(session.getAttribute("sess_user_id").toString()));

				subTaskDao.updateSubTaskConfiguration(subTasktransactional);
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
		System.out.println("Please go through all the document..");
		objForSend.put("responseMessage", "Please download all the attachment in order to approve the task !");

		return objForSend.toJSONString();
	}

	@Override
	public String reOpenSubTask(String json, HttpSession session) {
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(json);

			int ttrn_id = Integer.parseInt(jsonObj.get("ttrn_id").toString());
			System.out.println("ttrn_id:" + ttrn_id);
			String comment = jsonObj.get("reopen_comment").toString();
			List<UploadedSubTaskDocuments> attachedDocuments = subTaskDao.getAllDocumentByTtrnSubId(ttrn_id);

			if (attachedDocuments != null) {
				Iterator<UploadedSubTaskDocuments> itre = attachedDocuments.iterator();

				while (itre.hasNext()) {
					UploadedSubTaskDocuments uploadedDocuments = (UploadedSubTaskDocuments) itre.next();

					File deleteFile = new File(uploadedDocuments.getUdoc_sub_task_filename());
					deleteFile.delete();
					subTaskDao.deleteDocument(uploadedDocuments.getUdoc_sub_task_id());
					System.out.println("Delete DOC");
				}

			}

			SubTaskTranscational taskTransactional = subTaskDao.getSubTaskForCompletion(ttrn_id);
			taskTransactional.setTtrn_sub_task_completed_by("0");
			taskTransactional.setTtrn_sub_task_compl_date(null);
			taskTransactional.setTtrn_sub_task_submition_date(null);
			taskTransactional.setTtrn_sub_task_status("Re_Opened");
			taskTransactional.setTtrn_sub_task_comment("");
			subTaskDao.updateTaskConfiguration(taskTransactional); // Reopen Task

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

	@Override
	public String downloadSubtaskDocument(String jsonString, HttpServletResponse response) throws Throwable {
		JSONObject objForSend = new JSONObject();
		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);
			System.out.println("udoc_sub_id :" + jsonObject.get("udoc_sub_id").toString());
			int udoc_sub_id = Integer.parseInt(jsonObject.get("udoc_sub_id").toString());
			if (subTaskDao.getProofFilePath(udoc_sub_id) != null) {
				File file = new File(subTaskDao.getProofFilePath(udoc_sub_id));

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

	@Override
	public String deleteSubTaskDocument(String json, HttpSession session) {

		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(json);
			int udoc_sub_task_id = Integer.parseInt(jsonObj.get("udoc_sub_task_id").toString());
			subTaskDao.deleteTaskDocument(udoc_sub_task_id);

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
	public String deleteSubTaskHistory(String json, HttpSession session) {
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(json);
			int ttrn_sub_task_id = Integer.parseInt(jsonObj.get("ttrn_sub_task_id").toString());
			String ttrn_client_task_id = jsonObj.get("ttrn_client_task_id").toString();
			subTaskDao.deleteTaskHistory(ttrn_sub_task_id);
			String user_name = session.getAttribute("sess_user_full_name").toString();
			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			String ids = String.valueOf(ttrn_sub_task_id);
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

	@Override
	public String updateSubTasksConfiguration(String jsonString, HttpSession session) {
		JSONObject objForSend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int ttrn_sub_id = Integer.parseInt(jsonObj.get("ttrn_sub_id").toString());
			String user_name = session.getAttribute("sess_user_full_name").toString();
			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());

			SubTaskTranscational taskTransactional = subTaskDao.getSubTaskForCompletion(ttrn_sub_id);

			if (jsonObj.get("ttrn_frequency_for_operation") != null) {
				Date ttrn_pr_due_date = sdfOut.parse(jsonObj.get("ttrn_pr_due_date").toString());
				Date ttrn_rw_due_date = sdfOut.parse(jsonObj.get("ttrn_rw_due_date").toString());
				Date ttrn_fh_due_date = sdfOut.parse(jsonObj.get("ttrn_fh_due_date").toString());
				Date ttrn_uh_due_date = sdfOut.parse(jsonObj.get("ttrn_uh_due_date").toString());
				Date ttrn_legal_due_date = sdfOut.parse(jsonObj.get("ttrn_legal_due_date").toString());
				// String ttrn_impact = jsonObj.get("ttrn_impact").toString();
				// String ttrn_impact_on_unit = jsonObj.get("ttrn_impact_on_unit").toString();
				// String ttrn_impact_on_organization =
				// jsonObj.get("ttrn_impact_on_organization").toString();
				String ttrn_document = jsonObj.get("ttrn_document").toString();
				String ttrn_historical = jsonObj.get("ttrn_historical").toString();

				// String ttrn_frequency_for_alerts =
				// jsonObj.get("ttrn_frequency_for_alerts").toString();
				// String ttrn_frequency_for_operation =
				// jsonObj.get("ttrn_frequency_for_operation").toString();
				int ttrn_prior_days_buffer = Integer.parseInt(jsonObj.get("ttrn_prior_days_buffer").toString());
				int ttrn_alert_days = Integer.parseInt(jsonObj.get("ttrn_alert_days").toString());
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

				int ttrn_no_of_back_days_allowed = Integer
						.parseInt(jsonObj.get("ttrn_no_of_back_days_allowed").toString());
				String ttrn_allow_approver_reopening = jsonObj.get("ttrn_allow_approver_reopening").toString();

				taskTransactional.setTtrn_sub_task_alert_prior_day(ttrn_alert_days);
				taskTransactional.setTtrn_sub_task_allow_approver_reopening(ttrn_allow_approver_reopening);
				taskTransactional.setTtrn_sub_task_document(Integer.parseInt(ttrn_document));
				taskTransactional.setTtrn_sub_task_FH_due_date(ttrn_fh_due_date);
				taskTransactional.setTtrn_sub_task_first_alert(ttrn_first_alert);

				taskTransactional.setTtrn_sub_task_historical(Integer.parseInt(ttrn_historical));

				taskTransactional.setTtrn_sub_task_ENT_due_date(ttrn_legal_due_date);
				taskTransactional.setTtrn_sub_task_back_date_allowed(ttrn_no_of_back_days_allowed);
				taskTransactional.setTtrn_sub_task_pr_due_date(ttrn_pr_due_date);
				taskTransactional.setTtrn_sub_task_buffer_days(ttrn_prior_days_buffer);
				taskTransactional.setTtrn_sub_task_rw_date(ttrn_rw_due_date);
				taskTransactional.setTtrn_sub_task_second_alert(ttrn_second_alert);
				taskTransactional.setTtrn_sub_task_third_alert(ttrn_third_alert);
				taskTransactional.setTtrn_sub_task_UH_due_date(ttrn_uh_due_date);

				subTaskDao.updateTaskConfiguration(taskTransactional);

			} else {
				Date ttrn_pr_due_date = sdfOut.parse(jsonObj.get("ttrn_pr_due_date").toString());
				Date ttrn_rw_due_date = sdfOut.parse(jsonObj.get("ttrn_rw_due_date").toString());
				Date ttrn_fh_due_date = sdfOut.parse(jsonObj.get("ttrn_fh_due_date").toString());
				Date ttrn_uh_due_date = sdfOut.parse(jsonObj.get("ttrn_uh_due_date").toString());
				Date ttrn_legal_due_date = sdfOut.parse(jsonObj.get("ttrn_legal_due_date").toString());

				taskTransactional.setTtrn_sub_task_pr_due_date(ttrn_pr_due_date);
				taskTransactional.setTtrn_sub_task_rw_date(ttrn_rw_due_date);
				taskTransactional.setTtrn_sub_task_FH_due_date(ttrn_fh_due_date);
				taskTransactional.setTtrn_sub_task_UH_due_date(ttrn_uh_due_date);
				taskTransactional.setTtrn_sub_task_ENT_due_date(ttrn_legal_due_date);

				subTaskDao.updateTaskConfiguration(taskTransactional);
			}

			utilitiesService.addTaskCofigurationLog(taskTransactional.getTtrn_sub_client_task_id(), user_id, user_name,
					"MainTask", "Update");

			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			return objForSend.toJSONString();
		}
	}

}
