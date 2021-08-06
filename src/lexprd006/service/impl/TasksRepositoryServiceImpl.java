package lexprd006.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lexprd006.dao.DashboardDao;
import lexprd006.dao.TasksRepositoryDao;
import lexprd006.dao.UploadedDocumentsDao;
import lexprd006.domain.UploadedDocuments;
import lexprd006.service.TasksRepositoryService;

@Service(value = "tasksRepositoryService")
public class TasksRepositoryServiceImpl implements TasksRepositoryService {

	public final SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public final SimpleDateFormat sdfOut = new SimpleDateFormat("dd-MM-yyyy");
	public final SimpleDateFormat sdfIns = new SimpleDateFormat("yyyy-MM-dd");
	public final SimpleDateFormat sdfOutDisplay = new SimpleDateFormat("dd-MM-yyyy");

	@Autowired
	TasksRepositoryDao tasksRepositoryDao;

	@Autowired
	UploadedDocumentsDao uploadedDocumentsDao;

	@Autowired
	DashboardDao dashboardDao;

	// Method Created By: Mahesh Kharote(10/01/2016)
	// Method Purpose: Get all Tasks for repository
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String getAllTasksForRepository(String jsonString, HttpSession session) {
		JsonNode rootNode = null;
		String response = "";
		final ObjectMapper mapper = new ObjectMapper();
		ObjectNode objectNode = mapper.createObjectNode();
		System.out.println();
		int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
		int user_role_id = Integer.parseInt(session.getAttribute("sess_role_id").toString());
		ArrayNode repoArray = mapper.createArrayNode();
		try {
			List<Object> repo = tasksRepositoryDao.getAllTask(jsonString, user_id, user_role_id);

			System.out.println("repo size : " + repo.size());

			Iterator<Object> iterator = repo.iterator();
			while (iterator.hasNext()) {
				Object[] object = (Object[]) iterator.next();
				ObjectNode repoNode = mapper.createObjectNode();

				repoNode.put("tmap_client_task_id", object[0].toString());
				repoNode.put("task_legi_name", object[1].toString());
				repoNode.put("task_rule_name", object[2].toString());
				if (object[3] != null)
					repoNode.put("task_pr_due_date", object[3].toString());
				else
					repoNode.put("task_pr_due_date", "");

				if (object[4] != null)
					repoNode.put("task_legal_due_date", object[4].toString());
				else
					repoNode.put("task_legal_due_date", "");

				repoNode.put("task_activity_who", object[5].toString());
				repoNode.put("task_activity_when", object[6].toString());
				repoNode.put("task_activity", object[7].toString());
				repoNode.put("task_procedure", object[8].toString());
				repoNode.put("task_impact", object[9].toString());
				repoNode.put("task_frequency_for_operation", object[10].toString());
				repoNode.put("task_reference", object[12].toString());
				repoNode.put("task_cat_law", object[13].toString());
				repoNode.put("task_type_of_task", object[14].toString());
				repoNode.put("task_prohibitive", object[15].toString());
				repoNode.put("task_event", object[16].toString());
				repoNode.put("task_sub_event", object[17].toString());

				if (object[18] != null) {
					repoNode.put("ttrn_status", object[18].toString());
				} else {
					repoNode.put("ttrn_status", " ");
				}
				repoNode.put("orga_id", object[19].toString());
				repoNode.put("orga_name", object[20].toString());
				repoNode.put("loca_id", object[21].toString());
				repoNode.put("loca_name", object[22].toString());
				repoNode.put("dept_id", object[23].toString());
				repoNode.put("dept_name", object[24].toString());
				repoNode.put("performer_id", object[25].toString());
				repoNode.put("performer_name", object[26].toString() + " " + object[27].toString());
				repoNode.put("reviewer_id", object[28].toString());
				repoNode.put("reviewer_name", object[29].toString() + " " + object[30].toString());
				repoNode.put("task_cat_law_id", object[31].toString());
				repoNode.put("task_legi_id", object[32].toString());
				repoNode.put("task_rule_id", object[33].toString());
				repoNode.put("function_head_name", object[35].toString() + " " + object[36].toString());
				repoNode.put("dtco_id", object[38].toString());
				repoNode.put("dtco_client_task_id", object[39].toString());
				repoNode.put("dtco_after_before", object[40].toString());

				if (object[37] != null
						&& (!object[18].toString().equals("Active") || !object[18].toString().equals("Inactive"))) {
					List<UploadedDocuments> attachedDocuments = uploadedDocumentsDao
							.getAllDocumentByTtrnId(Integer.parseInt(object[37].toString()));

					if (attachedDocuments != null) {
						repoNode.put("document_attached", 1);
					} else {
						repoNode.put("document_attached", 0);
					}
				} else {
					repoNode.put("document_attached", 0);
				}

				repoArray.add(repoNode);
				// objectNode.putArray("subclasses").addAll(subclassArray);
				// tradeArray.addAll("subclasses")
				objectNode.putArray("repoData").addAll(repoArray);

			}
			return objectNode.toString();
		} catch (Exception e) {

			e.printStackTrace();
			return objectNode.toString();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getClientTaskIdFromDefaultConfiguartion(String jsonString) {
		JSONArray dataForSend = new JSONArray();
		try {

			List<Object> allTask = tasksRepositoryDao.getClientTaskIdFromDefaultConfiguartion(jsonString);
			Iterator<Object> itr = allTask.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("dtco_client_id", object[1]);
				objForAppend.put("dtco_id", object[0]);
				objForAppend.put("dtco_after_before", object[2]);
				dataForSend.add(objForAppend);

			}
			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("errorMessage", "Failed");

			return dataForSend.toJSONString();
		}
	}

	// Method Created By: Harshad Padole
	// Method Purpose: getTask for export
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String getTaskForExport(String json, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		try {
			List<Object> allTask = tasksRepositoryDao.gerTaskForExport(json, session);
			// List<Object> allTask = tasksRepositoryDao.getAllTask(8, 5);
			System.out.println("This is size:" + allTask.size());
			Iterator<Object> itr = allTask.iterator();
			JSONArray dataForAppend = new JSONArray();
			JSONArray dataForOrganagram = new JSONArray();
			JSONArray dataForTaskFilter = new JSONArray();

			JSONObject filtersObj = new JSONObject();
			JSONObject filtersObjTask = new JSONObject();

			JSONArray entityarray = new JSONArray();
			JSONArray unitarray = new JSONArray();
			JSONArray funcarray = new JSONArray();
			JSONArray execarray = new JSONArray();
			JSONArray evalarray = new JSONArray();
			JSONArray catlawarray = new JSONArray();
			JSONArray legiarray = new JSONArray();
			JSONArray rulearray = new JSONArray();
			JSONArray eventarray = new JSONArray();
			JSONArray subeventarray = new JSONArray();
			JSONArray frequencyarray = new JSONArray();
			JSONArray typeoftaskarray = new JSONArray();

			List<Integer> checkEntiList = new ArrayList<>();
			List<List> checUnitList = new ArrayList<>();
			List<List> checFuncList = new ArrayList<>();
			List<List> checExecList = new ArrayList<>();
			List<List> checEvalList = new ArrayList<>();
			List<Integer> checCatLaw = new ArrayList<>();
			List<List> checLegiList = new ArrayList<>();
			List<List> checRuleList = new ArrayList<>();
			List<List> checEventList = new ArrayList<>();
			List<List> checSubEventList = new ArrayList<>();
			List<String> checFrequencyList = new ArrayList<>();
			List<String> checTypeOfTaskList = new ArrayList<>();

			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("tmap_client_task_id", object[0]);
				objForAppend.put("task_legi_name", object[1]);
				objForAppend.put("task_rule_name", object[2]);
				if (object[3] != null)
					objForAppend.put("task_pr_due_date", sdfOut.format(sdfIn.parse(object[3].toString())));
				else
					objForAppend.put("task_pr_due_date", "");

				if (object[4] != null)
					objForAppend.put("task_legal_due_date", sdfOut.format(sdfIn.parse(object[4].toString())));
				else
					objForAppend.put("task_legal_due_date", "");

				objForAppend.put("task_activity_who", object[5]);
				objForAppend.put("task_activity_when", object[6]);
				objForAppend.put("task_activity", object[7]);
				objForAppend.put("task_procedure", object[8]);
				objForAppend.put("task_impact", object[9]);
				objForAppend.put("task_frequency_for_operation", object[10]);
				objForAppend.put("task_reference", object[12]);
				objForAppend.put("task_cat_law", object[13]);
				objForAppend.put("task_type_of_task", object[14]);
				objForAppend.put("task_prohibitive", object[15]);
				objForAppend.put("task_event", object[16]);
				objForAppend.put("task_sub_event", object[17]);

				if (object[18] != null)
					objForAppend.put("ttrn_status", object[18]);
				else
					objForAppend.put("ttrn_status", "");

				objForAppend.put("orga_id", object[19]);
				objForAppend.put("orga_name", object[20]);
				objForAppend.put("loca_id", object[21]);
				objForAppend.put("loca_name", object[22]);
				objForAppend.put("dept_id", object[23]);
				objForAppend.put("dept_name", object[24]);
				objForAppend.put("performer_id", object[25]);
				objForAppend.put("performer_name", object[26].toString() + " " + object[27].toString());
				objForAppend.put("reviewer_id", object[28]);
				objForAppend.put("reviewer_name", object[29].toString() + " " + object[30].toString());
				objForAppend.put("task_cat_law_id", object[31]);
				objForAppend.put("task_legi_id", object[32]);
				objForAppend.put("task_rule_id", object[33]);
				objForAppend.put("task_country", object[34]);
				objForAppend.put("task_state", object[35]);
				if (object[36] != null)
					objForAppend.put("task_effective_date", object[36]);
				else
					objForAppend.put("task_effective_date", "NA");
				objForAppend.put("task_excemption_criteria", object[37]);
				objForAppend.put("task_fine_amount", object[38]);
				objForAppend.put("task_form_no", object[39]);
				objForAppend.put("task_impact_organization", object[40]);
				objForAppend.put("task_impact_on_unit", object[41]);
				objForAppend.put("task_implications", object[42]);
				objForAppend.put("task_imprisonment_duration", object[43]);
				objForAppend.put("task_imprisonment_applies_to", object[44]);
				objForAppend.put("task_level", object[45]);
				objForAppend.put("task_linked_task_id", object[46]);
				objForAppend.put("task_more_info", object[47]);
				objForAppend.put("task_interlinkage", object[48]);
				if (object[49] != null)
					objForAppend.put("task_specific_due_date", object[49]);
				else
					objForAppend.put("task_specific_due_date", "NA");
				objForAppend.put("task_subsequent_amount_per_day", object[52]);
				objForAppend.put("task_weblinks", object[50]);
				objForAppend.put("task_statutory_authority", object[51]);

				if (object[57] != null)
					objForAppend.put("task_uh_due_date", sdfOut.format(sdfIn.parse(object[57].toString())));
				else
					objForAppend.put("task_uh_due_date", "");

				if (object[54] != null)
					objForAppend.put("task_fh_due_date", sdfOut.format(sdfIn.parse(object[54].toString())));
				else
					objForAppend.put("task_fh_due_date", "");

				objForAppend.put("functionHead_name", object[55].toString() + " " + object[56].toString());

				if (object[53] != null)
					objForAppend.put("task_rw_due_date", sdfOut.format(sdfIn.parse(object[53].toString())));
				else
					objForAppend.put("task_rw_due_date", "");

				dataForAppend.add(objForAppend);

				/*-----------------Code for generating filters---------------------------------------------------------------*/

				/*-----------------Code for generating organogram filters----------------------------------------------------*/

				if (!checkEntiList.contains(object[19])) {
					JSONObject dataForAppendEntiArray = new JSONObject();
					dataForAppendEntiArray.put("orga_id", object[19]);
					dataForAppendEntiArray.put("orga_name", object[20]);

					entityarray.add(dataForAppendEntiArray);
					checkEntiList.add(Integer.parseInt(object[19].toString()));
				}

				List<Integer> checkUnitForAdding = new ArrayList<>();

				checkUnitForAdding.add(Integer.parseInt(object[19].toString()));
				checkUnitForAdding.add(Integer.parseInt(object[21].toString()));

				if (!checUnitList.contains(checkUnitForAdding)) {
					JSONObject dataForAppendUnitArray = new JSONObject();
					dataForAppendUnitArray.put("loca_id", object[21]);
					dataForAppendUnitArray.put("loca_name", object[22]);
					dataForAppendUnitArray.put("orga_id", object[19]);

					unitarray.add(dataForAppendUnitArray);
					checUnitList.add(checkUnitForAdding);
				}
				List<Integer> checkFuncForAdding = new ArrayList<>();

				checkFuncForAdding.add(Integer.parseInt(object[19].toString()));
				checkFuncForAdding.add(Integer.parseInt(object[21].toString()));
				checkFuncForAdding.add(Integer.parseInt(object[23].toString()));

				if (!checFuncList.contains(checkFuncForAdding)) {
					JSONObject dataForAppendFuncArray = new JSONObject();
					dataForAppendFuncArray.put("dept_id", object[23]);
					dataForAppendFuncArray.put("dept_name", object[24]);
					dataForAppendFuncArray.put("orga_id", object[19]);
					dataForAppendFuncArray.put("loca_id", object[21]);

					funcarray.add(dataForAppendFuncArray);
					checFuncList.add(checkFuncForAdding);
				}

				List<Integer> checkExecForAdding = new ArrayList<>();

				checkExecForAdding.add(Integer.parseInt(object[19].toString()));
				checkExecForAdding.add(Integer.parseInt(object[21].toString()));
				checkExecForAdding.add(Integer.parseInt(object[23].toString()));
				checkExecForAdding.add(Integer.parseInt(object[25].toString()));

				if (!checExecList.contains(checkExecForAdding)) {
					JSONObject dataForAppendExecArray = new JSONObject();
					dataForAppendExecArray.put("user_id", object[25]);
					dataForAppendExecArray.put("user_name", object[26].toString() + " " + object[27].toString());
					dataForAppendExecArray.put("orga_id", object[19]);
					dataForAppendExecArray.put("loca_id", object[21]);
					dataForAppendExecArray.put("dept_id", object[23]);

					execarray.add(dataForAppendExecArray);
					checExecList.add(checkExecForAdding);
				}

				List<Integer> checkEvalForAdding = new ArrayList<>();

				checkEvalForAdding.add(Integer.parseInt(object[19].toString()));
				checkEvalForAdding.add(Integer.parseInt(object[21].toString()));
				checkEvalForAdding.add(Integer.parseInt(object[23].toString()));
				checkEvalForAdding.add(Integer.parseInt(object[28].toString()));

				if (!checEvalList.contains(checkEvalForAdding)) {
					JSONObject dataForAppendEvalArray = new JSONObject();
					dataForAppendEvalArray.put("user_id", object[28]);
					dataForAppendEvalArray.put("user_name", object[29].toString() + " " + object[30].toString());
					dataForAppendEvalArray.put("orga_id", object[19]);
					dataForAppendEvalArray.put("loca_id", object[21]);
					dataForAppendEvalArray.put("dept_id", object[23]);

					evalarray.add(dataForAppendEvalArray);
					checEvalList.add(checkEvalForAdding);
				}
				/*-----------------Code for generating organogram filters ends here------------------------------------------*/

				/*-----------------Code for generating task details filters ------------------------------------------------*/

				if (!checCatLaw.contains(object[31])) {
					JSONObject dataForAppendCatLawArray = new JSONObject();
					dataForAppendCatLawArray.put("task_cat_law_id", object[31]);
					dataForAppendCatLawArray.put("task_cat_law", object[13]);

					catlawarray.add(dataForAppendCatLawArray);
					checCatLaw.add(Integer.parseInt(object[31].toString()));

				}

				List<Integer> checkLegiForAdding = new ArrayList<>();

				checkLegiForAdding.add(Integer.parseInt(object[31].toString()));
				checkLegiForAdding.add(Integer.parseInt(object[32].toString()));

				if (!checLegiList.contains(checkLegiForAdding)) {
					JSONObject dataForAppendLegiArray = new JSONObject();
					dataForAppendLegiArray.put("task_legi_id", object[32]);
					dataForAppendLegiArray.put("task_legi_name", object[1]);
					dataForAppendLegiArray.put("task_cat_law_id", object[31]);

					legiarray.add(dataForAppendLegiArray);
					checLegiList.add(checkLegiForAdding);
				}

				List<Integer> checkRuleForAdding = new ArrayList<>();

				checkRuleForAdding.add(Integer.parseInt(object[31].toString()));
				checkRuleForAdding.add(Integer.parseInt(object[32].toString()));
				checkRuleForAdding.add(Integer.parseInt(object[33].toString()));

				if (!checRuleList.contains(checkRuleForAdding)) {
					JSONObject dataForAppendRuleArray = new JSONObject();
					dataForAppendRuleArray.put("task_rule_id", object[33]);
					dataForAppendRuleArray.put("task_rule_name", object[2]);
					dataForAppendRuleArray.put("task_legi_id", object[32]);
					dataForAppendRuleArray.put("task_cat_law_id", object[31]);

					rulearray.add(dataForAppendRuleArray);
					checRuleList.add(checkRuleForAdding);
				}

				List<String> checkEventForAdding = new ArrayList<>();

				checkEventForAdding.add(object[31].toString());
				checkEventForAdding.add(object[32].toString());
				checkEventForAdding.add(object[33].toString());
				checkEventForAdding.add(object[16].toString());

				if (!checEventList.contains(checkEventForAdding)) {
					JSONObject dataForAppenEventArray = new JSONObject();
					dataForAppenEventArray.put("task_rule_id", object[33]);
					dataForAppenEventArray.put("task_legi_id", object[32]);
					dataForAppenEventArray.put("task_cat_law_id", object[31]);
					dataForAppenEventArray.put("task_Event", object[16]);

					eventarray.add(dataForAppenEventArray);
					checEventList.add(checkEventForAdding);
				}

				List<String> checkSubEventForAdding = new ArrayList<>();

				checkSubEventForAdding.add(object[31].toString());
				checkSubEventForAdding.add(object[32].toString());
				checkSubEventForAdding.add(object[33].toString());
				checkSubEventForAdding.add(object[16].toString());
				checkSubEventForAdding.add(object[17].toString());

				if (!checSubEventList.contains(checkEventForAdding)) {
					JSONObject dataForAppenSubEventArray = new JSONObject();
					dataForAppenSubEventArray.put("task_rule_id", object[33]);
					dataForAppenSubEventArray.put("task_legi_id", object[32]);
					dataForAppenSubEventArray.put("task_cat_law_id", object[31]);
					dataForAppenSubEventArray.put("task_Event", object[16]);
					dataForAppenSubEventArray.put("task_Sub_Event", object[17]);

					subeventarray.add(dataForAppenSubEventArray);
					checSubEventList.add(checkEventForAdding);
				}

				if (!checFrequencyList.contains(object[10])) {
					JSONObject dataForAppendFrequencyArray = new JSONObject();
					dataForAppendFrequencyArray.put("task_frequency", object[10]);

					frequencyarray.add(dataForAppendFrequencyArray);
					checFrequencyList.add(object[10].toString());

				}

				if (!checTypeOfTaskList.contains(object[14])) {
					JSONObject dataForAppendTypeOfTaskArray = new JSONObject();
					dataForAppendTypeOfTaskArray.put("task_type_of_task", object[14]);

					typeoftaskarray.add(dataForAppendTypeOfTaskArray);
					checTypeOfTaskList.add(object[14].toString());

				}

				/*-----------------Code for generating task details filters ends here---------------------------------------*/

				/*-----------------Code for generating filters ends here-----------------------------------------------------*/

			}
			filtersObj.put("Entity", entityarray);
			filtersObj.put("Unit", unitarray);
			filtersObj.put("Function", funcarray);
			filtersObj.put("Executor", execarray);
			filtersObj.put("Evaluator", evalarray);

			dataForOrganagram.add(filtersObj);

			filtersObjTask.put("Cat_law", catlawarray);
			filtersObjTask.put("Legislation", legiarray);
			filtersObjTask.put("Rule", rulearray);
			filtersObjTask.put("Event", eventarray);
			filtersObjTask.put("Sub_Event", subeventarray);
			filtersObjTask.put("Frequency", frequencyarray);
			filtersObjTask.put("Type_Of_Task", typeoftaskarray);

			dataForTaskFilter.add(filtersObjTask);

			dataForSend.put("TotalNoOfTasks", allTask.size());
			dataForSend.put("AllTasks", dataForAppend);
			dataForSend.put("OrganogramFilter", dataForOrganagram);
			dataForSend.put("TasksFilter", dataForTaskFilter);

			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("errorMessage", "Failed");

			return dataForSend.toJSONString();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String getAllDocumentForRepository(String jsonString, HttpSession session) {

		JSONObject dataForSend = new JSONObject();
		try {
			List<Object> allTask = uploadedDocumentsDao.getDocumentRepository(
					Integer.parseInt(session.getAttribute("sess_user_id").toString()),
					Integer.parseInt(session.getAttribute("sess_role_id").toString()));
			// List<Object> allTask = tasksRepositoryDao.getAllTask(8, 5);
			// System.out.println("This is size:"+ allTask.size());
			Iterator<Object> itr = allTask.iterator();
			JSONArray dataForAppend = new JSONArray();
			JSONArray dataForOrganagram = new JSONArray();
			JSONArray dataForTaskFilter = new JSONArray();

			JSONObject filtersObj = new JSONObject();
			JSONObject filtersObjTask = new JSONObject();
			JSONArray entityarray = new JSONArray();
			JSONArray unitarray = new JSONArray();
			JSONArray funcarray = new JSONArray();
			JSONArray execarray = new JSONArray();
			JSONArray evalarray = new JSONArray();
			JSONArray catlawarray = new JSONArray();
			JSONArray legiarray = new JSONArray();
			JSONArray rulearray = new JSONArray();
			JSONArray eventarray = new JSONArray();
			JSONArray subeventarray = new JSONArray();
			JSONArray frequencyarray = new JSONArray();
			JSONArray typeoftaskarray = new JSONArray();

			List<Integer> checkEntiList = new ArrayList<>();
			List<List> checUnitList = new ArrayList<>();
			List<List> checFuncList = new ArrayList<>();
			List<List> checExecList = new ArrayList<>();
			List<List> checEvalList = new ArrayList<>();
			List<Integer> checCatLaw = new ArrayList<>();
			List<List> checLegiList = new ArrayList<>();
			List<List> checRuleList = new ArrayList<>();
			List<List> checEventList = new ArrayList<>();
			List<List> checSubEventList = new ArrayList<>();
			List<String> checFrequencyList = new ArrayList<>();
			List<String> checTypeOfTaskList = new ArrayList<>();

			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("tmap_client_task_id", object[0]);
				objForAppend.put("task_legi_name", object[1]);
				objForAppend.put("task_rule_name", object[2]);
				if (object[3] != null)
					objForAppend.put("task_pr_due_date", sdfOut.format(sdfIn.parse(object[3].toString())));
				else
					objForAppend.put("task_pr_due_date", "");

				if (object[4] != null)
					objForAppend.put("task_legal_due_date", sdfOut.format(sdfIn.parse(object[4].toString())));
				else
					objForAppend.put("task_legal_due_date", "");

				objForAppend.put("task_activity_who", object[5]);
				objForAppend.put("task_activity_when", object[6]);
				objForAppend.put("task_activity", object[7]);
				objForAppend.put("task_procedure", object[8]);
				objForAppend.put("task_impact", object[9]);
				objForAppend.put("task_frequency_for_operation", object[10]);
				objForAppend.put("task_reference", object[12]);
				objForAppend.put("task_cat_law", object[13]);
				objForAppend.put("task_type_of_task", object[14]);
				objForAppend.put("task_prohibitive", object[15]);
				objForAppend.put("task_event", object[16]);
				objForAppend.put("task_sub_event", object[17]);
				objForAppend.put("ttrn_status", object[18]);
				objForAppend.put("orga_id", object[19]);
				objForAppend.put("orga_name", object[20]);
				objForAppend.put("loca_id", object[21]);
				objForAppend.put("loca_name", object[22]);
				objForAppend.put("dept_id", object[23]);
				objForAppend.put("dept_name", object[24]);
				objForAppend.put("performer_id", object[25]);
				objForAppend.put("performer_name", object[26].toString() + " " + object[27].toString());
				objForAppend.put("reviewer_id", object[28]);
				objForAppend.put("reviewer_name", object[29].toString() + " " + object[30].toString());
				objForAppend.put("task_cat_law_id", object[31]);
				objForAppend.put("task_legi_id", object[32]);
				objForAppend.put("task_rule_id", object[33]);
				objForAppend.put("function_head_name", object[35].toString() + " " + object[36].toString());
				objForAppend.put("ttrn_id", object[37]);
				objForAppend.put("doc_id", object[38]);
				objForAppend.put("doc_name", object[39]);

				// System.out.println("This is task id:"+object[0]);

				dataForAppend.add(objForAppend);

				/*-----------------Code for generating filters---------------------------------------------------------------*/

				/*-----------------Code for generating organogram filters----------------------------------------------------*/

				if (!checkEntiList.contains(object[19])) {
					JSONObject dataForAppendEntiArray = new JSONObject();
					dataForAppendEntiArray.put("orga_id", object[19]);
					dataForAppendEntiArray.put("orga_name", object[20]);

					entityarray.add(dataForAppendEntiArray);
					checkEntiList.add(Integer.parseInt(object[19].toString()));
				}

				List<Integer> checkUnitForAdding = new ArrayList<>();

				checkUnitForAdding.add(Integer.parseInt(object[19].toString()));
				checkUnitForAdding.add(Integer.parseInt(object[21].toString()));

				if (!checUnitList.contains(checkUnitForAdding)) {
					JSONObject dataForAppendUnitArray = new JSONObject();
					dataForAppendUnitArray.put("loca_id", object[21]);
					dataForAppendUnitArray.put("loca_name", object[22]);
					dataForAppendUnitArray.put("orga_id", object[19]);

					unitarray.add(dataForAppendUnitArray);
					checUnitList.add(checkUnitForAdding);
				}
				List<Integer> checkFuncForAdding = new ArrayList<>();

				checkFuncForAdding.add(Integer.parseInt(object[19].toString()));
				checkFuncForAdding.add(Integer.parseInt(object[21].toString()));
				checkFuncForAdding.add(Integer.parseInt(object[23].toString()));

				if (!checFuncList.contains(checkFuncForAdding)) {
					JSONObject dataForAppendFuncArray = new JSONObject();
					dataForAppendFuncArray.put("dept_id", object[23]);
					dataForAppendFuncArray.put("dept_name", object[24]);
					dataForAppendFuncArray.put("orga_id", object[19]);
					dataForAppendFuncArray.put("loca_id", object[21]);

					funcarray.add(dataForAppendFuncArray);
					checFuncList.add(checkFuncForAdding);
				}

				List<Integer> checkExecForAdding = new ArrayList<>();

				checkExecForAdding.add(Integer.parseInt(object[19].toString()));
				checkExecForAdding.add(Integer.parseInt(object[21].toString()));
				checkExecForAdding.add(Integer.parseInt(object[23].toString()));
				checkExecForAdding.add(Integer.parseInt(object[25].toString()));

				if (!checExecList.contains(checkExecForAdding)) {
					JSONObject dataForAppendExecArray = new JSONObject();
					dataForAppendExecArray.put("user_id", object[25]);
					dataForAppendExecArray.put("user_name", object[26].toString() + " " + object[27].toString());
					dataForAppendExecArray.put("orga_id", object[19]);
					dataForAppendExecArray.put("loca_id", object[21]);
					dataForAppendExecArray.put("dept_id", object[23]);

					execarray.add(dataForAppendExecArray);
					checExecList.add(checkExecForAdding);
				}

				List<Integer> checkEvalForAdding = new ArrayList<>();

				checkEvalForAdding.add(Integer.parseInt(object[19].toString()));
				checkEvalForAdding.add(Integer.parseInt(object[21].toString()));
				checkEvalForAdding.add(Integer.parseInt(object[23].toString()));
				checkEvalForAdding.add(Integer.parseInt(object[28].toString()));

				if (!checEvalList.contains(checkEvalForAdding)) {
					JSONObject dataForAppendEvalArray = new JSONObject();
					dataForAppendEvalArray.put("user_id", object[28]);
					dataForAppendEvalArray.put("user_name", object[29].toString() + " " + object[30].toString());
					dataForAppendEvalArray.put("orga_id", object[19]);
					dataForAppendEvalArray.put("loca_id", object[21]);
					dataForAppendEvalArray.put("dept_id", object[23]);

					evalarray.add(dataForAppendEvalArray);
					checEvalList.add(checkEvalForAdding);
				}
				/*-----------------Code for generating organogram filters ends here------------------------------------------*/

				/*-----------------Code for generating task details filters ------------------------------------------------*/

				if (!checCatLaw.contains(object[31])) {
					JSONObject dataForAppendCatLawArray = new JSONObject();
					dataForAppendCatLawArray.put("task_cat_law_id", object[31]);
					dataForAppendCatLawArray.put("task_cat_law", object[13]);

					catlawarray.add(dataForAppendCatLawArray);
					checCatLaw.add(Integer.parseInt(object[31].toString()));

				}

				List<Integer> checkLegiForAdding = new ArrayList<>();

				checkLegiForAdding.add(Integer.parseInt(object[31].toString()));
				checkLegiForAdding.add(Integer.parseInt(object[32].toString()));
				checkLegiForAdding.add(Integer.parseInt(object[19].toString()));
				checkLegiForAdding.add(Integer.parseInt(object[21].toString()));
				checkLegiForAdding.add(Integer.parseInt(object[23].toString()));

				if (!checLegiList.contains(checkLegiForAdding)) {
					JSONObject dataForAppendLegiArray = new JSONObject();
					dataForAppendLegiArray.put("task_legi_id", object[32]);
					dataForAppendLegiArray.put("task_legi_name", object[1]);
					dataForAppendLegiArray.put("task_cat_law_id", object[31]);
					dataForAppendLegiArray.put("orga_id", object[19]);
					dataForAppendLegiArray.put("loca_id", object[21]);
					dataForAppendLegiArray.put("dept_id", object[23]);

					legiarray.add(dataForAppendLegiArray);
					checLegiList.add(checkLegiForAdding);
				}

				List<Integer> checkRuleForAdding = new ArrayList<>();

				checkRuleForAdding.add(Integer.parseInt(object[31].toString()));
				checkRuleForAdding.add(Integer.parseInt(object[32].toString()));
				checkRuleForAdding.add(Integer.parseInt(object[33].toString()));

				if (!checRuleList.contains(checkRuleForAdding)) {
					JSONObject dataForAppendRuleArray = new JSONObject();
					dataForAppendRuleArray.put("task_rule_id", object[33]);
					dataForAppendRuleArray.put("task_rule_name", object[2]);
					dataForAppendRuleArray.put("task_legi_id", object[32]);
					dataForAppendRuleArray.put("task_cat_law_id", object[31]);

					rulearray.add(dataForAppendRuleArray);
					checRuleList.add(checkRuleForAdding);
				}

				List<String> checkEventForAdding = new ArrayList<>();

				checkEventForAdding.add(object[31].toString());
				checkEventForAdding.add(object[32].toString());
				checkEventForAdding.add(object[33].toString());
				checkEventForAdding.add(object[16].toString());

				checkEventForAdding.add(object[19].toString());
				checkEventForAdding.add(object[21].toString());
				checkEventForAdding.add(object[23].toString());

				if (!checEventList.contains(checkEventForAdding)) {
					JSONObject dataForAppenEventArray = new JSONObject();
					dataForAppenEventArray.put("task_rule_id", object[33]);
					dataForAppenEventArray.put("task_legi_id", object[32]);
					dataForAppenEventArray.put("task_cat_law_id", object[31]);
					dataForAppenEventArray.put("task_Event", object[16]);

					dataForAppenEventArray.put("orga_id", object[19]);
					dataForAppenEventArray.put("loca_id", object[21]);
					dataForAppenEventArray.put("dept_id", object[23]);

					eventarray.add(dataForAppenEventArray);
					checEventList.add(checkEventForAdding);
				}

				List<String> checkSubEventForAdding = new ArrayList<>();

				checkSubEventForAdding.add(object[31].toString());
				checkSubEventForAdding.add(object[32].toString());
				checkSubEventForAdding.add(object[33].toString());
				checkSubEventForAdding.add(object[16].toString());
				checkSubEventForAdding.add(object[17].toString());

				checkSubEventForAdding.add(object[19].toString());
				checkSubEventForAdding.add(object[21].toString());
				checkSubEventForAdding.add(object[23].toString());

				if (!checSubEventList.contains(checkSubEventForAdding)) {
					JSONObject dataForAppenSubEventArray = new JSONObject();
					dataForAppenSubEventArray.put("task_rule_id", object[33]);
					dataForAppenSubEventArray.put("task_legi_id", object[32]);
					dataForAppenSubEventArray.put("task_cat_law_id", object[31]);
					dataForAppenSubEventArray.put("task_Event", object[16]);
					dataForAppenSubEventArray.put("task_Sub_Event", object[17]);

					dataForAppenSubEventArray.put("orga_id", object[19]);
					dataForAppenSubEventArray.put("loca_id", object[21]);
					dataForAppenSubEventArray.put("dept_id", object[23]);

					subeventarray.add(dataForAppenSubEventArray);
					checSubEventList.add(checkSubEventForAdding);
				}

				if (!checFrequencyList.contains(object[10])) {
					JSONObject dataForAppendFrequencyArray = new JSONObject();
					dataForAppendFrequencyArray.put("task_frequency", object[10]);

					frequencyarray.add(dataForAppendFrequencyArray);
					checFrequencyList.add(object[10].toString());

				}

				if (!checTypeOfTaskList.contains(object[14])) {
					JSONObject dataForAppendTypeOfTaskArray = new JSONObject();
					dataForAppendTypeOfTaskArray.put("task_type_of_task", object[14]);

					typeoftaskarray.add(dataForAppendTypeOfTaskArray);
					checTypeOfTaskList.add(object[14].toString());

				}

				/*-----------------Code for generating task details filters ends here---------------------------------------*/

				/*-----------------Code for generating filters ends here-----------------------------------------------------*/

			}
			filtersObj.put("Entity", entityarray);
			filtersObj.put("Unit", unitarray);
			filtersObj.put("Function", funcarray);
			filtersObj.put("Executor", execarray);
			filtersObj.put("Evaluator", evalarray);

			dataForOrganagram.add(filtersObj);

			filtersObjTask.put("Cat_law", catlawarray);
			filtersObjTask.put("Legislation", legiarray);
			filtersObjTask.put("Rule", rulearray);
			filtersObjTask.put("Event", eventarray);
			filtersObjTask.put("Sub_Event", subeventarray);
			filtersObjTask.put("Frequency", frequencyarray);
			filtersObjTask.put("Type_Of_Task", typeoftaskarray);

			dataForTaskFilter.add(filtersObjTask);

			dataForSend.put("TotalNoOfTasks", allTask.size());
			dataForSend.put("AllTasks", dataForAppend);
			dataForSend.put("OrganogramFilter", dataForOrganagram);
			dataForSend.put("TasksFilter", dataForTaskFilter);

			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("errorMessage", "Failed");

			return dataForSend.toJSONString();
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String listOfUpcommingTask(HttpSession session) {

		JSONObject dataForSend = new JSONObject();
		try {
			List<Object> allTask = tasksRepositoryDao.listOfUpcommingTask(
					Integer.parseInt(session.getAttribute("sess_user_id").toString()),
					Integer.parseInt(session.getAttribute("sess_role_id").toString()));
			// List<Object> allTask = tasksRepositoryDao.getAllTask(8, 5);

			Iterator<Object> itr = allTask.iterator();
			JSONArray dataForAppend = new JSONArray();
			JSONArray dataForOrganagram = new JSONArray();
			JSONArray dataForTaskFilter = new JSONArray();

			JSONObject filtersObj = new JSONObject();
			JSONObject filtersObjTask = new JSONObject();

			JSONArray entityarray = new JSONArray();
			JSONArray unitarray = new JSONArray();
			JSONArray funcarray = new JSONArray();
			JSONArray execarray = new JSONArray();
			JSONArray evalarray = new JSONArray();
			JSONArray catlawarray = new JSONArray();
			JSONArray legiarray = new JSONArray();
			JSONArray rulearray = new JSONArray();
			JSONArray eventarray = new JSONArray();
			JSONArray subeventarray = new JSONArray();
			JSONArray frequencyarray = new JSONArray();
			JSONArray typeoftaskarray = new JSONArray();

			List<Integer> checkEntiList = new ArrayList<>();
			List<List> checUnitList = new ArrayList<>();
			List<List> checFuncList = new ArrayList<>();
			List<List> checExecList = new ArrayList<>();
			List<List> checEvalList = new ArrayList<>();
			List<Integer> checCatLaw = new ArrayList<>();
			List<List> checLegiList = new ArrayList<>();
			List<List> checRuleList = new ArrayList<>();
			List<List> checEventList = new ArrayList<>();
			List<List> checSubEventList = new ArrayList<>();
			List<String> checFrequencyList = new ArrayList<>();
			List<String> checTypeOfTaskList = new ArrayList<>();

			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("tmap_client_task_id", object[0]);
				objForAppend.put("task_legi_name", object[1]);
				objForAppend.put("task_rule_name", object[2]);
				if (object[3] != null)
					objForAppend.put("task_pr_due_date", sdfOut.format(sdfIn.parse(object[3].toString())));
				else
					objForAppend.put("task_pr_due_date", "");

				if (object[4] != null)
					objForAppend.put("task_legal_due_date", sdfOut.format(sdfIn.parse(object[4].toString())));
				else
					objForAppend.put("task_legal_due_date", "");

				objForAppend.put("task_activity_who", object[5]);
				objForAppend.put("task_activity_when", object[6]);
				objForAppend.put("task_activity", object[7]);
				objForAppend.put("task_procedure", object[8]);
				objForAppend.put("task_impact", object[9]);
				objForAppend.put("task_frequency_for_operation", object[10]);
				objForAppend.put("task_reference", object[12]);
				objForAppend.put("task_cat_law", object[13]);
				objForAppend.put("task_type_of_task", object[14]);
				objForAppend.put("task_prohibitive", object[15]);
				objForAppend.put("task_event", object[16]);
				objForAppend.put("task_sub_event", object[17]);
				objForAppend.put("ttrn_status", object[18]);
				objForAppend.put("orga_id", object[19]);
				objForAppend.put("orga_name", object[20]);
				objForAppend.put("loca_id", object[21]);
				objForAppend.put("loca_name", object[22]);
				objForAppend.put("dept_id", object[23]);
				objForAppend.put("dept_name", object[24]);
				objForAppend.put("performer_id", object[25]);
				objForAppend.put("performer_name", object[26].toString() + " " + object[27].toString());
				objForAppend.put("reviewer_id", object[28]);
				objForAppend.put("reviewer_name", object[29].toString() + " " + object[30].toString());
				objForAppend.put("task_cat_law_id", object[31]);
				objForAppend.put("task_legi_id", object[32]);
				objForAppend.put("task_rule_id", object[33]);
				objForAppend.put("function_head_name", object[35].toString() + " " + object[36].toString());
				objForAppend.put("task_frequency", object[38]);

				if (object[37] != null
						&& (!object[18].toString().equals("Active") || !object[18].toString().equals("Inactive"))) {
					List<UploadedDocuments> attachedDocuments = uploadedDocumentsDao
							.getAllDocumentByTtrnId(Integer.parseInt(object[37].toString()));

					if (attachedDocuments != null) {
						objForAppend.put("document_attached", 1);
					} else {
						objForAppend.put("document_attached", 0);
					}
				} else {
					objForAppend.put("document_attached", 0);
				}

				dataForAppend.add(objForAppend);

				/*-----------------Code for generating filters---------------------------------------------------------------*/

				/*-----------------Code for generating organogram filters----------------------------------------------------*/

				if (!checkEntiList.contains(object[19])) {
					JSONObject dataForAppendEntiArray = new JSONObject();
					dataForAppendEntiArray.put("orga_id", object[19]);
					dataForAppendEntiArray.put("orga_name", object[20]);

					entityarray.add(dataForAppendEntiArray);
					checkEntiList.add(Integer.parseInt(object[19].toString()));
				}

				List<Integer> checkUnitForAdding = new ArrayList<>();

				checkUnitForAdding.add(Integer.parseInt(object[19].toString()));
				checkUnitForAdding.add(Integer.parseInt(object[21].toString()));

				if (!checUnitList.contains(checkUnitForAdding)) {
					JSONObject dataForAppendUnitArray = new JSONObject();
					dataForAppendUnitArray.put("loca_id", object[21]);
					dataForAppendUnitArray.put("loca_name", object[22]);
					dataForAppendUnitArray.put("orga_id", object[19]);

					unitarray.add(dataForAppendUnitArray);
					checUnitList.add(checkUnitForAdding);
				}
				List<Integer> checkFuncForAdding = new ArrayList<>();

				checkFuncForAdding.add(Integer.parseInt(object[19].toString()));
				checkFuncForAdding.add(Integer.parseInt(object[21].toString()));
				checkFuncForAdding.add(Integer.parseInt(object[23].toString()));

				if (!checFuncList.contains(checkFuncForAdding)) {
					JSONObject dataForAppendFuncArray = new JSONObject();
					dataForAppendFuncArray.put("dept_id", object[23]);
					dataForAppendFuncArray.put("dept_name", object[24]);
					dataForAppendFuncArray.put("orga_id", object[19]);
					dataForAppendFuncArray.put("loca_id", object[21]);

					funcarray.add(dataForAppendFuncArray);
					checFuncList.add(checkFuncForAdding);
				}

				List<Integer> checkExecForAdding = new ArrayList<>();

				checkExecForAdding.add(Integer.parseInt(object[19].toString()));
				checkExecForAdding.add(Integer.parseInt(object[21].toString()));
				checkExecForAdding.add(Integer.parseInt(object[23].toString()));
				checkExecForAdding.add(Integer.parseInt(object[25].toString()));

				if (!checExecList.contains(checkExecForAdding)) {
					JSONObject dataForAppendExecArray = new JSONObject();
					dataForAppendExecArray.put("user_id", object[25]);
					dataForAppendExecArray.put("user_name", object[26].toString() + " " + object[27].toString());
					dataForAppendExecArray.put("orga_id", object[19]);
					dataForAppendExecArray.put("loca_id", object[21]);
					dataForAppendExecArray.put("dept_id", object[23]);

					execarray.add(dataForAppendExecArray);
					checExecList.add(checkExecForAdding);
				}

				List<Integer> checkEvalForAdding = new ArrayList<>();

				checkEvalForAdding.add(Integer.parseInt(object[19].toString()));
				checkEvalForAdding.add(Integer.parseInt(object[21].toString()));
				checkEvalForAdding.add(Integer.parseInt(object[23].toString()));
				checkEvalForAdding.add(Integer.parseInt(object[28].toString()));

				if (!checEvalList.contains(checkEvalForAdding)) {
					JSONObject dataForAppendEvalArray = new JSONObject();
					dataForAppendEvalArray.put("user_id", object[28]);
					dataForAppendEvalArray.put("user_name", object[29].toString() + " " + object[30].toString());
					dataForAppendEvalArray.put("orga_id", object[19]);
					dataForAppendEvalArray.put("loca_id", object[21]);
					dataForAppendEvalArray.put("dept_id", object[23]);

					evalarray.add(dataForAppendEvalArray);
					checEvalList.add(checkEvalForAdding);
				}
				/*-----------------Code for generating organogram filters ends here------------------------------------------*/

				/*-----------------Code for generating task details filters ------------------------------------------------*/

				if (!checCatLaw.contains(object[31])) {
					JSONObject dataForAppendCatLawArray = new JSONObject();
					dataForAppendCatLawArray.put("task_cat_law_id", object[31]);
					dataForAppendCatLawArray.put("task_cat_law", object[13]);

					catlawarray.add(dataForAppendCatLawArray);
					checCatLaw.add(Integer.parseInt(object[31].toString()));

				}

				List<Integer> checkLegiForAdding = new ArrayList<>();

				checkLegiForAdding.add(Integer.parseInt(object[31].toString()));
				checkLegiForAdding.add(Integer.parseInt(object[32].toString()));
				checkLegiForAdding.add(Integer.parseInt(object[19].toString()));
				checkLegiForAdding.add(Integer.parseInt(object[21].toString()));
				checkLegiForAdding.add(Integer.parseInt(object[23].toString()));

				if (!checLegiList.contains(checkLegiForAdding)) {
					JSONObject dataForAppendLegiArray = new JSONObject();
					dataForAppendLegiArray.put("task_legi_id", object[32]);
					dataForAppendLegiArray.put("task_legi_name", object[1]);
					dataForAppendLegiArray.put("task_cat_law_id", object[31]);
					dataForAppendLegiArray.put("orga_id", object[19]);
					dataForAppendLegiArray.put("loca_id", object[21]);
					dataForAppendLegiArray.put("dept_id", object[23]);

					legiarray.add(dataForAppendLegiArray);
					checLegiList.add(checkLegiForAdding);
				}

				List<Integer> checkRuleForAdding = new ArrayList<>();

				checkRuleForAdding.add(Integer.parseInt(object[31].toString()));
				checkRuleForAdding.add(Integer.parseInt(object[32].toString()));
				checkRuleForAdding.add(Integer.parseInt(object[33].toString()));

				if (!checRuleList.contains(checkRuleForAdding)) {
					JSONObject dataForAppendRuleArray = new JSONObject();
					dataForAppendRuleArray.put("task_rule_id", object[33]);
					dataForAppendRuleArray.put("task_rule_name", object[2]);
					dataForAppendRuleArray.put("task_legi_id", object[32]);
					dataForAppendRuleArray.put("task_cat_law_id", object[31]);

					rulearray.add(dataForAppendRuleArray);
					checRuleList.add(checkRuleForAdding);
				}

				List<String> checkEventForAdding = new ArrayList<>();

				checkEventForAdding.add(object[31].toString());
				checkEventForAdding.add(object[32].toString());
				checkEventForAdding.add(object[33].toString());
				checkEventForAdding.add(object[16].toString());

				checkEventForAdding.add(object[19].toString());
				checkEventForAdding.add(object[21].toString());
				checkEventForAdding.add(object[23].toString());

				if (!checEventList.contains(checkEventForAdding)) {
					JSONObject dataForAppenEventArray = new JSONObject();
					dataForAppenEventArray.put("task_rule_id", object[33]);
					dataForAppenEventArray.put("task_legi_id", object[32]);
					dataForAppenEventArray.put("task_cat_law_id", object[31]);
					dataForAppenEventArray.put("task_Event", object[16]);

					dataForAppenEventArray.put("orga_id", object[19]);
					dataForAppenEventArray.put("loca_id", object[21]);
					dataForAppenEventArray.put("dept_id", object[23]);

					eventarray.add(dataForAppenEventArray);
					checEventList.add(checkEventForAdding);
				}

				List<String> checkSubEventForAdding = new ArrayList<>();

				checkSubEventForAdding.add(object[31].toString());
				checkSubEventForAdding.add(object[32].toString());
				checkSubEventForAdding.add(object[33].toString());
				checkSubEventForAdding.add(object[16].toString());
				checkSubEventForAdding.add(object[17].toString());

				checkSubEventForAdding.add(object[19].toString());
				checkSubEventForAdding.add(object[21].toString());
				checkSubEventForAdding.add(object[23].toString());

				if (!checSubEventList.contains(checkSubEventForAdding)) {
					JSONObject dataForAppenSubEventArray = new JSONObject();
					dataForAppenSubEventArray.put("task_rule_id", object[33]);
					dataForAppenSubEventArray.put("task_legi_id", object[32]);
					dataForAppenSubEventArray.put("task_cat_law_id", object[31]);
					dataForAppenSubEventArray.put("task_Event", object[16]);
					dataForAppenSubEventArray.put("task_Sub_Event", object[17]);

					dataForAppenSubEventArray.put("orga_id", object[19]);
					dataForAppenSubEventArray.put("loca_id", object[21]);
					dataForAppenSubEventArray.put("dept_id", object[23]);

					subeventarray.add(dataForAppenSubEventArray);
					checSubEventList.add(checkSubEventForAdding);
				}

				if (!checFrequencyList.contains(object[10])) {
					JSONObject dataForAppendFrequencyArray = new JSONObject();
					dataForAppendFrequencyArray.put("task_frequency", object[10]);

					frequencyarray.add(dataForAppendFrequencyArray);
					checFrequencyList.add(object[10].toString());

				}

				if (!checTypeOfTaskList.contains(object[14])) {
					JSONObject dataForAppendTypeOfTaskArray = new JSONObject();
					dataForAppendTypeOfTaskArray.put("task_type_of_task", object[14]);

					typeoftaskarray.add(dataForAppendTypeOfTaskArray);
					checTypeOfTaskList.add(object[14].toString());

				}

				/*-----------------Code for generating task details filters ends here---------------------------------------*/

				/*-----------------Code for generating filters ends here-----------------------------------------------------*/

			}
			filtersObj.put("Entity", entityarray);
			filtersObj.put("Unit", unitarray);
			filtersObj.put("Function", funcarray);
			filtersObj.put("Executor", execarray);
			filtersObj.put("Evaluator", evalarray);

			dataForOrganagram.add(filtersObj);

			filtersObjTask.put("Cat_law", catlawarray);
			filtersObjTask.put("Legislation", legiarray);
			filtersObjTask.put("Rule", rulearray);
			filtersObjTask.put("Event", eventarray);
			filtersObjTask.put("Sub_Event", subeventarray);
			filtersObjTask.put("Frequency", frequencyarray);
			filtersObjTask.put("Type_Of_Task", typeoftaskarray);

			dataForTaskFilter.add(filtersObjTask);

			dataForSend.put("TotalNoOfTasks", allTask.size());
			dataForSend.put("AllTasks", dataForAppend);
			dataForSend.put("OrganogramFilter", dataForOrganagram);
			dataForSend.put("TasksFilter", dataForTaskFilter);

			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("errorMessage", "Failed");

			return dataForSend.toJSONString();
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String listWaitingForApprovalTasksTask(String jsonString, HttpSession session) {

		JSONObject dataForSend = new JSONObject();
		try {
			List<Object> allTask = tasksRepositoryDao.listWaitingForApprovalTasksTask(
					Integer.parseInt(session.getAttribute("sess_user_id").toString()),
					Integer.parseInt(session.getAttribute("sess_role_id").toString()));
			// List<Object> allTask = tasksRepositoryDao.getAllTask(8, 5);
			System.out.println("This is size:" + allTask.size());
			Iterator<Object> itr = allTask.iterator();
			JSONArray dataForAppend = new JSONArray();
			JSONArray dataForOrganagram = new JSONArray();
			JSONArray dataForTaskFilter = new JSONArray();

			JSONObject filtersObj = new JSONObject();
			JSONObject filtersObjTask = new JSONObject();
			JSONArray entityarray = new JSONArray();
			JSONArray unitarray = new JSONArray();
			JSONArray funcarray = new JSONArray();
			JSONArray execarray = new JSONArray();
			JSONArray evalarray = new JSONArray();
			JSONArray catlawarray = new JSONArray();
			JSONArray legiarray = new JSONArray();
			JSONArray rulearray = new JSONArray();
			JSONArray eventarray = new JSONArray();
			JSONArray subeventarray = new JSONArray();
			JSONArray frequencyarray = new JSONArray();
			JSONArray typeoftaskarray = new JSONArray();

			List<Integer> checkEntiList = new ArrayList<>();
			List<List> checUnitList = new ArrayList<>();
			List<List> checFuncList = new ArrayList<>();
			List<List> checExecList = new ArrayList<>();
			List<List> checEvalList = new ArrayList<>();
			List<Integer> checCatLaw = new ArrayList<>();
			List<List> checLegiList = new ArrayList<>();
			List<List> checRuleList = new ArrayList<>();
			List<List> checEventList = new ArrayList<>();
			List<List> checSubEventList = new ArrayList<>();
			List<String> checFrequencyList = new ArrayList<>();
			List<String> checTypeOfTaskList = new ArrayList<>();

			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("tmap_client_task_id", object[0]);
				objForAppend.put("task_legi_name", object[1]);
				objForAppend.put("task_rule_name", object[2]);
				if (object[3] != null)
					objForAppend.put("task_pr_due_date", sdfOut.format(sdfIn.parse(object[3].toString())));
				else
					objForAppend.put("task_pr_due_date", "");

				if (object[4] != null)
					objForAppend.put("task_legal_due_date", sdfOut.format(sdfIn.parse(object[4].toString())));
				else
					objForAppend.put("task_legal_due_date", "");

				objForAppend.put("task_activity_who", object[5]);
				objForAppend.put("task_activity_when", object[6]);
				objForAppend.put("task_activity", object[7]);
				objForAppend.put("task_procedure", object[8]);
				objForAppend.put("task_impact", object[9]);
				objForAppend.put("task_frequency_for_operation", object[10]);
				objForAppend.put("task_reference", object[12]);
				objForAppend.put("task_cat_law", object[13]);
				objForAppend.put("task_type_of_task", object[14]);
				objForAppend.put("task_prohibitive", object[15]);
				objForAppend.put("task_event", object[16]);
				objForAppend.put("task_sub_event", object[17]);
				objForAppend.put("ttrn_status", object[18]);
				objForAppend.put("orga_id", object[19]);
				objForAppend.put("orga_name", object[20]);
				objForAppend.put("loca_id", object[21]);
				objForAppend.put("loca_name", object[22]);
				objForAppend.put("dept_id", object[23]);
				objForAppend.put("dept_name", object[24]);
				objForAppend.put("performer_id", object[25]);
				objForAppend.put("performer_name", object[26].toString() + " " + object[27].toString());
				objForAppend.put("reviewer_id", object[28]);
				objForAppend.put("reviewer_name", object[29].toString() + " " + object[30].toString());
				objForAppend.put("task_cat_law_id", object[31]);
				objForAppend.put("task_legi_id", object[32]);
				objForAppend.put("task_rule_id", object[33]);
				objForAppend.put("function_head_name", object[35].toString() + " " + object[36].toString());
				objForAppend.put("task_frequency", object[38]);

				if (object[37] != null
						&& (!object[18].toString().equals("Active") || !object[18].toString().equals("Inactive"))) {
					List<UploadedDocuments> attachedDocuments = uploadedDocumentsDao
							.getAllDocumentByTtrnId(Integer.parseInt(object[37].toString()));

					if (attachedDocuments != null) {
						objForAppend.put("document_attached", 1);
					} else {
						objForAppend.put("document_attached", 0);
					}
				} else {
					objForAppend.put("document_attached", 0);
				}

				// System.out.println("This is task id:" + object[0]);

				dataForAppend.add(objForAppend);

				/*-----------------Code for generating filters---------------------------------------------------------------*/

				/*-----------------Code for generating organogram filters----------------------------------------------------*/

				if (!checkEntiList.contains(object[19])) {
					JSONObject dataForAppendEntiArray = new JSONObject();
					dataForAppendEntiArray.put("orga_id", object[19]);
					dataForAppendEntiArray.put("orga_name", object[20]);
					entityarray.add(dataForAppendEntiArray);
					checkEntiList.add(Integer.parseInt(object[19].toString()));
				}

				List<Integer> checkUnitForAdding = new ArrayList<>();
				checkUnitForAdding.add(Integer.parseInt(object[19].toString()));
				checkUnitForAdding.add(Integer.parseInt(object[21].toString()));

				if (!checUnitList.contains(checkUnitForAdding)) {
					JSONObject dataForAppendUnitArray = new JSONObject();
					dataForAppendUnitArray.put("loca_id", object[21]);
					dataForAppendUnitArray.put("loca_name", object[22]);
					dataForAppendUnitArray.put("orga_id", object[19]);
					unitarray.add(dataForAppendUnitArray);
					checUnitList.add(checkUnitForAdding);
				}
				List<Integer> checkFuncForAdding = new ArrayList<>();
				checkFuncForAdding.add(Integer.parseInt(object[19].toString()));
				checkFuncForAdding.add(Integer.parseInt(object[21].toString()));
				checkFuncForAdding.add(Integer.parseInt(object[23].toString()));

				if (!checFuncList.contains(checkFuncForAdding)) {
					JSONObject dataForAppendFuncArray = new JSONObject();
					dataForAppendFuncArray.put("dept_id", object[23]);
					dataForAppendFuncArray.put("dept_name", object[24]);
					dataForAppendFuncArray.put("orga_id", object[19]);
					dataForAppendFuncArray.put("loca_id", object[21]);

					funcarray.add(dataForAppendFuncArray);
					checFuncList.add(checkFuncForAdding);
				}

				List<Integer> checkExecForAdding = new ArrayList<>();

				checkExecForAdding.add(Integer.parseInt(object[19].toString()));
				checkExecForAdding.add(Integer.parseInt(object[21].toString()));
				checkExecForAdding.add(Integer.parseInt(object[23].toString()));
				checkExecForAdding.add(Integer.parseInt(object[25].toString()));

				if (!checExecList.contains(checkExecForAdding)) {
					JSONObject dataForAppendExecArray = new JSONObject();
					dataForAppendExecArray.put("user_id", object[25]);
					dataForAppendExecArray.put("user_name", object[26].toString() + " " + object[27].toString());
					dataForAppendExecArray.put("orga_id", object[19]);
					dataForAppendExecArray.put("loca_id", object[21]);
					dataForAppendExecArray.put("dept_id", object[23]);

					execarray.add(dataForAppendExecArray);
					checExecList.add(checkExecForAdding);
				}

				List<Integer> checkEvalForAdding = new ArrayList<>();

				checkEvalForAdding.add(Integer.parseInt(object[19].toString()));
				checkEvalForAdding.add(Integer.parseInt(object[21].toString()));
				checkEvalForAdding.add(Integer.parseInt(object[23].toString()));
				checkEvalForAdding.add(Integer.parseInt(object[28].toString()));

				if (!checEvalList.contains(checkEvalForAdding)) {
					JSONObject dataForAppendEvalArray = new JSONObject();
					dataForAppendEvalArray.put("user_id", object[28]);
					dataForAppendEvalArray.put("user_name", object[29].toString() + " " + object[30].toString());
					dataForAppendEvalArray.put("orga_id", object[19]);
					dataForAppendEvalArray.put("loca_id", object[21]);
					dataForAppendEvalArray.put("dept_id", object[23]);

					evalarray.add(dataForAppendEvalArray);
					checEvalList.add(checkEvalForAdding);
				}
				/*-----------------Code for generating organogram filters ends here------------------------------------------*/

				/*-----------------Code for generating task details filters ------------------------------------------------*/

				if (!checCatLaw.contains(object[31])) {
					JSONObject dataForAppendCatLawArray = new JSONObject();
					dataForAppendCatLawArray.put("task_cat_law_id", object[31]);
					dataForAppendCatLawArray.put("task_cat_law", object[13]);
					catlawarray.add(dataForAppendCatLawArray);
					checCatLaw.add(Integer.parseInt(object[31].toString()));
				}

				List<Integer> checkLegiForAdding = new ArrayList<>();

				checkLegiForAdding.add(Integer.parseInt(object[31].toString()));
				checkLegiForAdding.add(Integer.parseInt(object[32].toString()));
				checkLegiForAdding.add(Integer.parseInt(object[19].toString()));
				checkLegiForAdding.add(Integer.parseInt(object[21].toString()));
				checkLegiForAdding.add(Integer.parseInt(object[23].toString()));

				if (!checLegiList.contains(checkLegiForAdding)) {
					JSONObject dataForAppendLegiArray = new JSONObject();
					dataForAppendLegiArray.put("task_legi_id", object[32]);
					dataForAppendLegiArray.put("task_legi_name", object[1]);
					dataForAppendLegiArray.put("task_cat_law_id", object[31]);
					dataForAppendLegiArray.put("orga_id", object[19]);
					dataForAppendLegiArray.put("loca_id", object[21]);
					dataForAppendLegiArray.put("dept_id", object[23]);

					legiarray.add(dataForAppendLegiArray);
					checLegiList.add(checkLegiForAdding);
				}

				List<Integer> checkRuleForAdding = new ArrayList<>();

				checkRuleForAdding.add(Integer.parseInt(object[31].toString()));
				checkRuleForAdding.add(Integer.parseInt(object[32].toString()));
				checkRuleForAdding.add(Integer.parseInt(object[33].toString()));

				if (!checRuleList.contains(checkRuleForAdding)) {
					JSONObject dataForAppendRuleArray = new JSONObject();
					dataForAppendRuleArray.put("task_rule_id", object[33]);
					dataForAppendRuleArray.put("task_rule_name", object[2]);
					dataForAppendRuleArray.put("task_legi_id", object[32]);
					dataForAppendRuleArray.put("task_cat_law_id", object[31]);

					rulearray.add(dataForAppendRuleArray);
					checRuleList.add(checkRuleForAdding);
				}

				List<String> checkEventForAdding = new ArrayList<>();
				checkEventForAdding.add(object[31].toString());
				checkEventForAdding.add(object[32].toString());
				checkEventForAdding.add(object[33].toString());
				checkEventForAdding.add(object[16].toString());
				checkEventForAdding.add(object[19].toString());
				checkEventForAdding.add(object[21].toString());
				checkEventForAdding.add(object[23].toString());

				if (!checEventList.contains(checkEventForAdding)) {
					JSONObject dataForAppenEventArray = new JSONObject();
					dataForAppenEventArray.put("task_rule_id", object[33]);
					dataForAppenEventArray.put("task_legi_id", object[32]);
					dataForAppenEventArray.put("task_cat_law_id", object[31]);
					dataForAppenEventArray.put("task_Event", object[16]);
					dataForAppenEventArray.put("orga_id", object[19]);
					dataForAppenEventArray.put("loca_id", object[21]);
					dataForAppenEventArray.put("dept_id", object[23]);
					eventarray.add(dataForAppenEventArray);
					checEventList.add(checkEventForAdding);
				}

				List<String> checkSubEventForAdding = new ArrayList<>();
				checkSubEventForAdding.add(object[31].toString());
				checkSubEventForAdding.add(object[32].toString());
				checkSubEventForAdding.add(object[33].toString());
				checkSubEventForAdding.add(object[16].toString());
				checkSubEventForAdding.add(object[17].toString());
				checkSubEventForAdding.add(object[19].toString());
				checkSubEventForAdding.add(object[21].toString());
				checkSubEventForAdding.add(object[23].toString());

				if (!checSubEventList.contains(checkSubEventForAdding)) {
					JSONObject dataForAppenSubEventArray = new JSONObject();
					dataForAppenSubEventArray.put("task_rule_id", object[33]);
					dataForAppenSubEventArray.put("task_legi_id", object[32]);
					dataForAppenSubEventArray.put("task_cat_law_id", object[31]);
					dataForAppenSubEventArray.put("task_Event", object[16]);
					dataForAppenSubEventArray.put("task_Sub_Event", object[17]);

					dataForAppenSubEventArray.put("orga_id", object[19]);
					dataForAppenSubEventArray.put("loca_id", object[21]);
					dataForAppenSubEventArray.put("dept_id", object[23]);
					subeventarray.add(dataForAppenSubEventArray);
					checSubEventList.add(checkSubEventForAdding);
				}

				if (!checFrequencyList.contains(object[10])) {
					JSONObject dataForAppendFrequencyArray = new JSONObject();
					dataForAppendFrequencyArray.put("task_frequency", object[10]);
					frequencyarray.add(dataForAppendFrequencyArray);
					checFrequencyList.add(object[10].toString());
				}

				if (!checTypeOfTaskList.contains(object[14])) {
					JSONObject dataForAppendTypeOfTaskArray = new JSONObject();
					dataForAppendTypeOfTaskArray.put("task_type_of_task", object[14]);
					typeoftaskarray.add(dataForAppendTypeOfTaskArray);
					checTypeOfTaskList.add(object[14].toString());
				}

				/*-----------------Code for generating task details filters ends here---------------------------------------*/

				/*-----------------Code for generating filters ends here-----------------------------------------------------*/

			}
			filtersObj.put("Entity", entityarray);
			filtersObj.put("Unit", unitarray);
			filtersObj.put("Function", funcarray);
			filtersObj.put("Executor", execarray);
			filtersObj.put("Evaluator", evalarray);

			dataForOrganagram.add(filtersObj);

			filtersObjTask.put("Cat_law", catlawarray);
			filtersObjTask.put("Legislation", legiarray);
			filtersObjTask.put("Rule", rulearray);
			filtersObjTask.put("Event", eventarray);
			filtersObjTask.put("Sub_Event", subeventarray);
			filtersObjTask.put("Frequency", frequencyarray);
			filtersObjTask.put("Type_Of_Task", typeoftaskarray);

			dataForTaskFilter.add(filtersObjTask);

			dataForSend.put("TotalNoOfTasks", allTask.size());
			dataForSend.put("AllTasks", dataForAppend);
			dataForSend.put("OrganogramFilter", dataForOrganagram);
			dataForSend.put("TasksFilter", dataForTaskFilter);

			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("errorMessage", "Failed");

			return dataForSend.toJSONString();
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String reopenedTask(String jsonString, HttpSession session) {

		JSONObject dataForSend = new JSONObject();
		try {
			List<Object> allTask = tasksRepositoryDao.listreopenedTask(
					Integer.parseInt(session.getAttribute("sess_user_id").toString()),
					Integer.parseInt(session.getAttribute("sess_role_id").toString()));
			// List<Object> allTask = tasksRepositoryDao.getAllTask(8, 5);
			System.out.println("This is size:" + allTask.size());
			Iterator<Object> itr = allTask.iterator();
			JSONArray dataForAppend = new JSONArray();
			JSONArray dataForOrganagram = new JSONArray();
			JSONArray dataForTaskFilter = new JSONArray();

			JSONObject filtersObj = new JSONObject();
			JSONObject filtersObjTask = new JSONObject();
			JSONArray entityarray = new JSONArray();
			JSONArray unitarray = new JSONArray();
			JSONArray funcarray = new JSONArray();
			JSONArray execarray = new JSONArray();
			JSONArray evalarray = new JSONArray();
			JSONArray catlawarray = new JSONArray();
			JSONArray legiarray = new JSONArray();
			JSONArray rulearray = new JSONArray();
			JSONArray eventarray = new JSONArray();
			JSONArray subeventarray = new JSONArray();
			JSONArray frequencyarray = new JSONArray();
			JSONArray typeoftaskarray = new JSONArray();

			List<Integer> checkEntiList = new ArrayList<>();
			List<List> checUnitList = new ArrayList<>();
			List<List> checFuncList = new ArrayList<>();
			List<List> checExecList = new ArrayList<>();
			List<List> checEvalList = new ArrayList<>();
			List<Integer> checCatLaw = new ArrayList<>();
			List<List> checLegiList = new ArrayList<>();
			List<List> checRuleList = new ArrayList<>();
			List<List> checEventList = new ArrayList<>();
			List<List> checSubEventList = new ArrayList<>();
			List<String> checFrequencyList = new ArrayList<>();
			List<String> checTypeOfTaskList = new ArrayList<>();

			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("tmap_client_task_id", object[0]);
				objForAppend.put("task_legi_name", object[1]);
				objForAppend.put("task_rule_name", object[2]);
				if (object[3] != null)
					objForAppend.put("task_pr_due_date", sdfOut.format(sdfIn.parse(object[3].toString())));
				else
					objForAppend.put("task_pr_due_date", "");

				if (object[4] != null)
					objForAppend.put("task_legal_due_date", sdfOut.format(sdfIn.parse(object[4].toString())));
				else
					objForAppend.put("task_legal_due_date", "");

				objForAppend.put("task_activity_who", object[5]);
				objForAppend.put("task_activity_when", object[6]);
				objForAppend.put("task_activity", object[7]);
				objForAppend.put("task_procedure", object[8]);
				objForAppend.put("task_impact", object[9]);
				objForAppend.put("task_frequency_for_operation", object[10]);
				objForAppend.put("task_reference", object[12]);
				objForAppend.put("task_cat_law", object[13]);
				objForAppend.put("task_type_of_task", object[14]);
				objForAppend.put("task_prohibitive", object[15]);
				objForAppend.put("task_event", object[16]);
				objForAppend.put("task_sub_event", object[17]);
				objForAppend.put("ttrn_status", object[18]);
				objForAppend.put("orga_id", object[19]);
				objForAppend.put("orga_name", object[20]);
				objForAppend.put("loca_id", object[21]);
				objForAppend.put("loca_name", object[22]);
				objForAppend.put("dept_id", object[23]);
				objForAppend.put("dept_name", object[24]);
				objForAppend.put("performer_id", object[25]);
				objForAppend.put("performer_name", object[26].toString() + " " + object[27].toString());
				objForAppend.put("reviewer_id", object[28]);
				objForAppend.put("reviewer_name", object[29].toString() + " " + object[30].toString());
				objForAppend.put("task_cat_law_id", object[31]);
				objForAppend.put("task_legi_id", object[32]);
				objForAppend.put("task_rule_id", object[33]);
				objForAppend.put("function_head_name", object[35].toString() + " " + object[36].toString());
				objForAppend.put("task_frequency", object[38]);

				if (object[37] != null
						&& (!object[18].toString().equals("Active") || !object[18].toString().equals("Inactive"))) {
					List<UploadedDocuments> attachedDocuments = uploadedDocumentsDao
							.getAllDocumentByTtrnId(Integer.parseInt(object[37].toString()));

					if (attachedDocuments != null) {
						objForAppend.put("document_attached", 1);
					} else {
						objForAppend.put("document_attached", 0);
					}
				} else {
					objForAppend.put("document_attached", 0);
				}

				// System.out.println("This is task id:" + object[0]);

				dataForAppend.add(objForAppend);

				/*-----------------Code for generating filters---------------------------------------------------------------*/

				/*-----------------Code for generating organogram filters----------------------------------------------------*/

				if (!checkEntiList.contains(object[19])) {
					JSONObject dataForAppendEntiArray = new JSONObject();
					dataForAppendEntiArray.put("orga_id", object[19]);
					dataForAppendEntiArray.put("orga_name", object[20]);
					entityarray.add(dataForAppendEntiArray);
					checkEntiList.add(Integer.parseInt(object[19].toString()));
				}

				List<Integer> checkUnitForAdding = new ArrayList<>();
				checkUnitForAdding.add(Integer.parseInt(object[19].toString()));
				checkUnitForAdding.add(Integer.parseInt(object[21].toString()));

				if (!checUnitList.contains(checkUnitForAdding)) {
					JSONObject dataForAppendUnitArray = new JSONObject();
					dataForAppendUnitArray.put("loca_id", object[21]);
					dataForAppendUnitArray.put("loca_name", object[22]);
					dataForAppendUnitArray.put("orga_id", object[19]);
					unitarray.add(dataForAppendUnitArray);
					checUnitList.add(checkUnitForAdding);
				}
				List<Integer> checkFuncForAdding = new ArrayList<>();
				checkFuncForAdding.add(Integer.parseInt(object[19].toString()));
				checkFuncForAdding.add(Integer.parseInt(object[21].toString()));
				checkFuncForAdding.add(Integer.parseInt(object[23].toString()));

				if (!checFuncList.contains(checkFuncForAdding)) {
					JSONObject dataForAppendFuncArray = new JSONObject();
					dataForAppendFuncArray.put("dept_id", object[23]);
					dataForAppendFuncArray.put("dept_name", object[24]);
					dataForAppendFuncArray.put("orga_id", object[19]);
					dataForAppendFuncArray.put("loca_id", object[21]);

					funcarray.add(dataForAppendFuncArray);
					checFuncList.add(checkFuncForAdding);
				}

				List<Integer> checkExecForAdding = new ArrayList<>();

				checkExecForAdding.add(Integer.parseInt(object[19].toString()));
				checkExecForAdding.add(Integer.parseInt(object[21].toString()));
				checkExecForAdding.add(Integer.parseInt(object[23].toString()));
				checkExecForAdding.add(Integer.parseInt(object[25].toString()));

				if (!checExecList.contains(checkExecForAdding)) {
					JSONObject dataForAppendExecArray = new JSONObject();
					dataForAppendExecArray.put("user_id", object[25]);
					dataForAppendExecArray.put("user_name", object[26].toString() + " " + object[27].toString());
					dataForAppendExecArray.put("orga_id", object[19]);
					dataForAppendExecArray.put("loca_id", object[21]);
					dataForAppendExecArray.put("dept_id", object[23]);

					execarray.add(dataForAppendExecArray);
					checExecList.add(checkExecForAdding);
				}

				List<Integer> checkEvalForAdding = new ArrayList<>();

				checkEvalForAdding.add(Integer.parseInt(object[19].toString()));
				checkEvalForAdding.add(Integer.parseInt(object[21].toString()));
				checkEvalForAdding.add(Integer.parseInt(object[23].toString()));
				checkEvalForAdding.add(Integer.parseInt(object[28].toString()));

				if (!checEvalList.contains(checkEvalForAdding)) {
					JSONObject dataForAppendEvalArray = new JSONObject();
					dataForAppendEvalArray.put("user_id", object[28]);
					dataForAppendEvalArray.put("user_name", object[29].toString() + " " + object[30].toString());
					dataForAppendEvalArray.put("orga_id", object[19]);
					dataForAppendEvalArray.put("loca_id", object[21]);
					dataForAppendEvalArray.put("dept_id", object[23]);

					evalarray.add(dataForAppendEvalArray);
					checEvalList.add(checkEvalForAdding);
				}
				/*-----------------Code for generating organogram filters ends here------------------------------------------*/

				/*-----------------Code for generating task details filters ------------------------------------------------*/

				if (!checCatLaw.contains(object[31])) {
					JSONObject dataForAppendCatLawArray = new JSONObject();
					dataForAppendCatLawArray.put("task_cat_law_id", object[31]);
					dataForAppendCatLawArray.put("task_cat_law", object[13]);
					catlawarray.add(dataForAppendCatLawArray);
					checCatLaw.add(Integer.parseInt(object[31].toString()));
				}

				List<Integer> checkLegiForAdding = new ArrayList<>();

				checkLegiForAdding.add(Integer.parseInt(object[31].toString()));
				checkLegiForAdding.add(Integer.parseInt(object[32].toString()));
				checkLegiForAdding.add(Integer.parseInt(object[19].toString()));
				checkLegiForAdding.add(Integer.parseInt(object[21].toString()));
				checkLegiForAdding.add(Integer.parseInt(object[23].toString()));

				if (!checLegiList.contains(checkLegiForAdding)) {
					JSONObject dataForAppendLegiArray = new JSONObject();
					dataForAppendLegiArray.put("task_legi_id", object[32]);
					dataForAppendLegiArray.put("task_legi_name", object[1]);
					dataForAppendLegiArray.put("task_cat_law_id", object[31]);
					dataForAppendLegiArray.put("orga_id", object[19]);
					dataForAppendLegiArray.put("loca_id", object[21]);
					dataForAppendLegiArray.put("dept_id", object[23]);

					legiarray.add(dataForAppendLegiArray);
					checLegiList.add(checkLegiForAdding);
				}

				List<Integer> checkRuleForAdding = new ArrayList<>();

				checkRuleForAdding.add(Integer.parseInt(object[31].toString()));
				checkRuleForAdding.add(Integer.parseInt(object[32].toString()));
				checkRuleForAdding.add(Integer.parseInt(object[33].toString()));

				if (!checRuleList.contains(checkRuleForAdding)) {
					JSONObject dataForAppendRuleArray = new JSONObject();
					dataForAppendRuleArray.put("task_rule_id", object[33]);
					dataForAppendRuleArray.put("task_rule_name", object[2]);
					dataForAppendRuleArray.put("task_legi_id", object[32]);
					dataForAppendRuleArray.put("task_cat_law_id", object[31]);

					rulearray.add(dataForAppendRuleArray);
					checRuleList.add(checkRuleForAdding);
				}

				List<String> checkEventForAdding = new ArrayList<>();
				checkEventForAdding.add(object[31].toString());
				checkEventForAdding.add(object[32].toString());
				checkEventForAdding.add(object[33].toString());
				checkEventForAdding.add(object[16].toString());
				checkEventForAdding.add(object[19].toString());
				checkEventForAdding.add(object[21].toString());
				checkEventForAdding.add(object[23].toString());

				if (!checEventList.contains(checkEventForAdding)) {
					JSONObject dataForAppenEventArray = new JSONObject();
					dataForAppenEventArray.put("task_rule_id", object[33]);
					dataForAppenEventArray.put("task_legi_id", object[32]);
					dataForAppenEventArray.put("task_cat_law_id", object[31]);
					dataForAppenEventArray.put("task_Event", object[16]);
					dataForAppenEventArray.put("orga_id", object[19]);
					dataForAppenEventArray.put("loca_id", object[21]);
					dataForAppenEventArray.put("dept_id", object[23]);
					eventarray.add(dataForAppenEventArray);
					checEventList.add(checkEventForAdding);
				}

				List<String> checkSubEventForAdding = new ArrayList<>();
				checkSubEventForAdding.add(object[31].toString());
				checkSubEventForAdding.add(object[32].toString());
				checkSubEventForAdding.add(object[33].toString());
				checkSubEventForAdding.add(object[16].toString());
				checkSubEventForAdding.add(object[17].toString());
				checkSubEventForAdding.add(object[19].toString());
				checkSubEventForAdding.add(object[21].toString());
				checkSubEventForAdding.add(object[23].toString());

				if (!checSubEventList.contains(checkSubEventForAdding)) {
					JSONObject dataForAppenSubEventArray = new JSONObject();
					dataForAppenSubEventArray.put("task_rule_id", object[33]);
					dataForAppenSubEventArray.put("task_legi_id", object[32]);
					dataForAppenSubEventArray.put("task_cat_law_id", object[31]);
					dataForAppenSubEventArray.put("task_Event", object[16]);
					dataForAppenSubEventArray.put("task_Sub_Event", object[17]);

					dataForAppenSubEventArray.put("orga_id", object[19]);
					dataForAppenSubEventArray.put("loca_id", object[21]);
					dataForAppenSubEventArray.put("dept_id", object[23]);
					subeventarray.add(dataForAppenSubEventArray);
					checSubEventList.add(checkSubEventForAdding);
				}

				if (!checFrequencyList.contains(object[10])) {
					JSONObject dataForAppendFrequencyArray = new JSONObject();
					dataForAppendFrequencyArray.put("task_frequency", object[10]);
					frequencyarray.add(dataForAppendFrequencyArray);
					checFrequencyList.add(object[10].toString());
				}

				if (!checTypeOfTaskList.contains(object[14])) {
					JSONObject dataForAppendTypeOfTaskArray = new JSONObject();
					dataForAppendTypeOfTaskArray.put("task_type_of_task", object[14]);
					typeoftaskarray.add(dataForAppendTypeOfTaskArray);
					checTypeOfTaskList.add(object[14].toString());
				}

				/*-----------------Code for generating task details filters ends here---------------------------------------*/

				/*-----------------Code for generating filters ends here-----------------------------------------------------*/

			}
			filtersObj.put("Entity", entityarray);
			filtersObj.put("Unit", unitarray);
			filtersObj.put("Function", funcarray);
			filtersObj.put("Executor", execarray);
			filtersObj.put("Evaluator", evalarray);

			dataForOrganagram.add(filtersObj);

			filtersObjTask.put("Cat_law", catlawarray);
			filtersObjTask.put("Legislation", legiarray);
			filtersObjTask.put("Rule", rulearray);
			filtersObjTask.put("Event", eventarray);
			filtersObjTask.put("Sub_Event", subeventarray);
			filtersObjTask.put("Frequency", frequencyarray);
			filtersObjTask.put("Type_Of_Task", typeoftaskarray);

			dataForTaskFilter.add(filtersObjTask);

			dataForSend.put("TotalNoOfTasks", allTask.size());
			dataForSend.put("AllTasks", dataForAppend);
			dataForSend.put("OrganogramFilter", dataForOrganagram);
			dataForSend.put("TasksFilter", dataForTaskFilter);

			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("errorMessage", "Failed");

			return dataForSend.toJSONString();
		}

	}

	@Override
	public String searchRepository(String jsonString, HttpSession session) {
		JsonNode rootNode = null;
		String response = "";
		final ObjectMapper mapper = new ObjectMapper();
		ObjectNode objectNode = mapper.createObjectNode();
		System.out.println();
		int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
		int user_role_id = Integer.parseInt(session.getAttribute("sess_role_id").toString());
		ArrayNode repoArray = mapper.createArrayNode();
		try {
			List<Object> repo = tasksRepositoryDao.searchForRepository(jsonString, user_id, user_role_id);

			System.out.println("repo size : " + repo.size());

			Iterator<Object> iterator = repo.iterator();
			while (iterator.hasNext()) {
				Object[] object = (Object[]) iterator.next();
				ObjectNode repoNode = mapper.createObjectNode();

				repoNode.put("tmap_client_task_id", object[0].toString());
				repoNode.put("task_legi_name", object[1].toString());
				repoNode.put("task_rule_name", object[2].toString());
				if (object[3] != null)
					repoNode.put("task_pr_due_date", object[3].toString());
				else
					repoNode.put("task_pr_due_date", "");

				if (object[4] != null)
					repoNode.put("task_legal_due_date", object[4].toString());
				else
					repoNode.put("task_legal_due_date", "");

				repoNode.put("task_activity_who", object[5].toString());
				repoNode.put("task_activity_when", object[6].toString());
				repoNode.put("task_activity", object[7].toString());
				repoNode.put("task_procedure", object[8].toString());
				repoNode.put("task_impact", object[9].toString());
				repoNode.put("task_frequency_for_operation", object[10].toString());
				repoNode.put("task_reference", object[12].toString());
				repoNode.put("task_cat_law", object[13].toString());
				repoNode.put("task_type_of_task", object[14].toString());
				repoNode.put("task_prohibitive", object[15].toString());
				repoNode.put("task_event", object[16].toString());
				repoNode.put("task_sub_event", object[17].toString());

				if (object[18] != null) {
					repoNode.put("ttrn_status", object[18].toString());
				} else {
					repoNode.put("ttrn_status", " ");
				}
				repoNode.put("orga_id", object[19].toString());
				repoNode.put("orga_name", object[20].toString());
				repoNode.put("loca_id", object[21].toString());
				repoNode.put("loca_name", object[22].toString());
				repoNode.put("dept_id", object[23].toString());
				repoNode.put("dept_name", object[24].toString());
				repoNode.put("performer_id", object[25].toString());
				repoNode.put("performer_name", object[26].toString() + " " + object[27].toString());
				repoNode.put("reviewer_id", object[28].toString());
				repoNode.put("reviewer_name", object[29].toString() + " " + object[30].toString());
				repoNode.put("task_cat_law_id", object[31].toString());
				repoNode.put("task_legi_id", object[32].toString());
				repoNode.put("task_rule_id", object[33].toString());
				repoNode.put("function_head_name", object[35].toString() + " " + object[36].toString());

				/*
				 * if (object[37] != null && (!object[18].toString().equals("Active") ||
				 * !object[18].toString().equals("Inactive"))) { List<UploadedDocuments>
				 * attachedDocuments = uploadedDocumentsDao
				 * .getAllDocumentByTtrnId(Integer.parseInt(object[37].toString()));
				 * 
				 * if (attachedDocuments != null) { repoNode.put("document_attached", 1); } else
				 * { repoNode.put("document_attached", 0); } } else {
				 * repoNode.put("document_attached", 0); }
				 */

				repoArray.add(repoNode);
				// objectNode.putArray("subclasses").addAll(subclassArray);
				// tradeArray.addAll("subclasses")
				objectNode.putArray("repoData").addAll(repoArray);

			}
			return objectNode.toString();
		} catch (Exception e) {

			e.printStackTrace();
			return objectNode.toString();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getalltasks(String jsonString, HttpSession session) {

		JSONObject dataForSend = new JSONObject();
		JsonNode rootNode = null;
		final ObjectMapper mapper = new ObjectMapper();
		try {
			rootNode = mapper.readTree(jsonString);
			int orga_id = rootNode.path("orga_id").asInt();
			int loca_id = rootNode.path("loca_id").asInt();
			int dept_id = rootNode.path("dept_id").asInt();
			System.out.println("orga_id:" + orga_id + "loca_id:" + loca_id + "dept_id:" + dept_id);
			List<Object> allTasks = tasksRepositoryDao.getAllTaskForRepository(
					Integer.parseInt(session.getAttribute("sess_user_id").toString()),
					Integer.parseInt(session.getAttribute("sess_role_id").toString()), orga_id, loca_id, dept_id);
			Iterator<Object> itr = allTasks.iterator();
			JSONArray dataForAppend = new JSONArray();
			JSONArray dataForOrganagram = new JSONArray();
			JSONArray dataForTaskFilter = new JSONArray();

			JSONObject filtersObj = new JSONObject();
			JSONObject filtersObjTask = new JSONObject();

			JSONArray entityarray = new JSONArray();
			JSONArray unitarray = new JSONArray();
			JSONArray funcarray = new JSONArray();
			JSONArray execarray = new JSONArray();
			JSONArray evalarray = new JSONArray();
			JSONArray funheadarray = new JSONArray();
			JSONArray catlawarray = new JSONArray();
			JSONArray legiarray = new JSONArray();
			JSONArray rulearray = new JSONArray();
			JSONArray eventarray = new JSONArray();
			JSONArray subeventarray = new JSONArray();
			JSONArray frequencyarray = new JSONArray();
			JSONArray typeoftaskarray = new JSONArray();

			List<Integer> checkEntiList = new ArrayList<>();
			List<List> checUnitList = new ArrayList<>();
			List<List> checFuncList = new ArrayList<>();
			List<List> checExecList = new ArrayList<>();
			List<List> checEvalList = new ArrayList<>();
			List<List> checFunHeadList = new ArrayList<>();
			List<Integer> checCatLaw = new ArrayList<>();
			List<List> checLegiList = new ArrayList<>();
			List<List> checRuleList = new ArrayList<>();
			List<List> checEventList = new ArrayList<>();
			List<List> checSubEventList = new ArrayList<>();
			List<String> checFrequencyList = new ArrayList<>();
			List<String> checTypeOfTaskList = new ArrayList<>();

			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();

				JSONObject objForAppend = new JSONObject();

				/*-----------------------------------Code for adding filters list to array---------------------------*/

				if (orga_id != 0 && loca_id == 0) {
					List<Integer> checkExecForAdding = new ArrayList<>();

					checkExecForAdding.add(Integer.parseInt(object[8].toString()));
					checkExecForAdding.add(Integer.parseInt(object[7].toString()));

					if (!checExecList.contains(checkExecForAdding)) {
						JSONObject dataForAppendExecArray = new JSONObject();
						dataForAppendExecArray.put("user_id", object[7]);
						dataForAppendExecArray.put("user_name", object[12].toString() + " " + object[13].toString());
						dataForAppendExecArray.put("orga_id", object[8]);

						execarray.add(dataForAppendExecArray);
						checExecList.add(checkExecForAdding);
					}

					List<Integer> checkEvalForAdding = new ArrayList<>();

					checkEvalForAdding.add(Integer.parseInt(object[8].toString()));
					checkEvalForAdding.add(Integer.parseInt(object[11].toString()));

					if (!checEvalList.contains(checkEvalForAdding)) {
						JSONObject dataForAppendEvalArray = new JSONObject();
						dataForAppendEvalArray.put("user_id", object[11]);
						dataForAppendEvalArray.put("user_name", object[14].toString() + " " + object[15].toString());
						dataForAppendEvalArray.put("orga_id", object[8]);

						evalarray.add(dataForAppendEvalArray);
						checEvalList.add(checkEvalForAdding);
					}

					List<Integer> checkFunHeadForAdding = new ArrayList<>();

					checkFunHeadForAdding.add(Integer.parseInt(object[8].toString()));
					checkFunHeadForAdding.add(Integer.parseInt(object[17].toString()));

					if (!checEvalList.contains(checkFunHeadForAdding)) {
						JSONObject dataForAppendFunHeadArray = new JSONObject();
						dataForAppendFunHeadArray.put("user_id", object[17]);
						dataForAppendFunHeadArray.put("user_name", object[18].toString() + " " + object[19].toString());
						dataForAppendFunHeadArray.put("orga_id", object[8]);

						funheadarray.add(dataForAppendFunHeadArray);
						checFunHeadList.add(checkFunHeadForAdding);
					}
				}

				if (loca_id != 0 && dept_id == 0) {
					List<Integer> checkExecForAdding = new ArrayList<>();

					checkExecForAdding.add(Integer.parseInt(object[8].toString()));
					checkExecForAdding.add(Integer.parseInt(object[9].toString()));
					checkExecForAdding.add(Integer.parseInt(object[7].toString()));

					if (!checExecList.contains(checkExecForAdding)) {
						JSONObject dataForAppendExecArray = new JSONObject();
						dataForAppendExecArray.put("user_id", object[7]);
						dataForAppendExecArray.put("user_name", object[12].toString() + " " + object[13].toString());
						dataForAppendExecArray.put("orga_id", object[8]);
						dataForAppendExecArray.put("loca_id", object[9]);

						execarray.add(dataForAppendExecArray);
						checExecList.add(checkExecForAdding);
					}

					List<Integer> checkEvalForAdding = new ArrayList<>();

					checkEvalForAdding.add(Integer.parseInt(object[8].toString()));
					checkEvalForAdding.add(Integer.parseInt(object[9].toString()));
					checkEvalForAdding.add(Integer.parseInt(object[11].toString()));

					if (!checEvalList.contains(checkEvalForAdding)) {
						JSONObject dataForAppendEvalArray = new JSONObject();
						dataForAppendEvalArray.put("user_id", object[11]);
						dataForAppendEvalArray.put("user_name", object[14].toString() + " " + object[15].toString());
						dataForAppendEvalArray.put("orga_id", object[8]);
						dataForAppendEvalArray.put("loca_id", object[9]);

						evalarray.add(dataForAppendEvalArray);
						checEvalList.add(checkEvalForAdding);
					}

					List<Integer> checkFunHeadForAdding = new ArrayList<>();

					checkFunHeadForAdding.add(Integer.parseInt(object[8].toString()));
					checkFunHeadForAdding.add(Integer.parseInt(object[9].toString()));
					checkFunHeadForAdding.add(Integer.parseInt(object[17].toString()));

					if (!checFunHeadList.contains(checkFunHeadForAdding)) {
						JSONObject dataForAppendFunHeadArray = new JSONObject();
						dataForAppendFunHeadArray.put("user_id", object[17]);
						dataForAppendFunHeadArray.put("user_name", object[18].toString() + " " + object[19].toString());
						dataForAppendFunHeadArray.put("orga_id", object[8]);
						dataForAppendFunHeadArray.put("loca_id", object[9]);

						funheadarray.add(dataForAppendFunHeadArray);
						checFunHeadList.add(checkFunHeadForAdding);
					}

				}
				if (dept_id != 0) {
					List<Integer> checkExecForAdding = new ArrayList<>();

					checkExecForAdding.add(Integer.parseInt(object[8].toString()));
					checkExecForAdding.add(Integer.parseInt(object[9].toString()));
					checkExecForAdding.add(Integer.parseInt(object[10].toString()));
					checkExecForAdding.add(Integer.parseInt(object[7].toString()));

					if (!checExecList.contains(checkExecForAdding)) {
						JSONObject dataForAppendExecArray = new JSONObject();
						dataForAppendExecArray.put("user_id", object[7]);
						dataForAppendExecArray.put("user_name", object[12].toString() + " " + object[13].toString());
						dataForAppendExecArray.put("orga_id", object[8]);
						dataForAppendExecArray.put("loca_id", object[9]);
						dataForAppendExecArray.put("dept_id", object[10]);

						execarray.add(dataForAppendExecArray);
						checExecList.add(checkExecForAdding);
					}

					List<Integer> checkEvalForAdding = new ArrayList<>();

					checkEvalForAdding.add(Integer.parseInt(object[8].toString()));
					checkEvalForAdding.add(Integer.parseInt(object[9].toString()));
					checkEvalForAdding.add(Integer.parseInt(object[10].toString()));
					checkEvalForAdding.add(Integer.parseInt(object[11].toString()));

					if (!checEvalList.contains(checkEvalForAdding)) {
						JSONObject dataForAppendEvalArray = new JSONObject();
						dataForAppendEvalArray.put("user_id", object[11]);
						dataForAppendEvalArray.put("user_name", object[14].toString() + " " + object[15].toString());
						dataForAppendEvalArray.put("orga_id", object[8]);
						dataForAppendEvalArray.put("loca_id", object[9]);
						dataForAppendEvalArray.put("dept_id", object[10]);

						evalarray.add(dataForAppendEvalArray);
						checEvalList.add(checkEvalForAdding);
					}

					List<Integer> checkFunHeadForAdding = new ArrayList<>();

					checkFunHeadForAdding.add(Integer.parseInt(object[8].toString()));
					checkFunHeadForAdding.add(Integer.parseInt(object[9].toString()));
					checkFunHeadForAdding.add(Integer.parseInt(object[10].toString()));
					checkFunHeadForAdding.add(Integer.parseInt(object[17].toString()));

					if (!checFunHeadList.contains(checkFunHeadForAdding)) {
						JSONObject dataForAppendFunHeadArray = new JSONObject();
						dataForAppendFunHeadArray.put("user_id", object[17]);
						dataForAppendFunHeadArray.put("user_name", object[18].toString() + " " + object[19].toString());
						dataForAppendFunHeadArray.put("orga_id", object[8]);
						dataForAppendFunHeadArray.put("loca_id", object[9]);
						dataForAppendFunHeadArray.put("dept_id", object[10]);

						funheadarray.add(dataForAppendFunHeadArray);
						checFunHeadList.add(checkFunHeadForAdding);
					}
				}

				List<Integer> chkEntiUnitDeptLegiForAdding = new ArrayList<>();

				chkEntiUnitDeptLegiForAdding.add(Integer.parseInt(object[8].toString()));
				chkEntiUnitDeptLegiForAdding.add(Integer.parseInt(object[9].toString()));
				chkEntiUnitDeptLegiForAdding.add(Integer.parseInt(object[10].toString()));
				chkEntiUnitDeptLegiForAdding.add(Integer.parseInt(object[2].toString()));
				// chkEntiUnitDeptLegiForAdding.add(Integer.parseInt(object[25].toString()));

				if (!checLegiList.contains(chkEntiUnitDeptLegiForAdding)) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("orga_id", object[8]);
					jsonObject.put("loca_id", object[9]);
					jsonObject.put("dept_id", object[10]);
					jsonObject.put("task_legi_id", object[2]);
					// jsonObject.put("rule_id", object[25]);
					jsonObject.put("task_legi_name", object[0].toString());
					// jsonObject.put("rule_name", object[2].toString());

					legiarray.add(jsonObject);
					checLegiList.add(chkEntiUnitDeptLegiForAdding);
				}

				List<Integer> checkRuleForAdding = new ArrayList<>();

				checkRuleForAdding.add(Integer.parseInt(object[2].toString()));
				checkRuleForAdding.add(Integer.parseInt(object[3].toString()));

				if (!checRuleList.contains(checkRuleForAdding)) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("task_legi_id", object[2]);
					jsonObject.put("task_rule_id", object[3]);
					jsonObject.put("task_rule_name", object[1].toString());
					rulearray.add(jsonObject);
					checRuleList.add(checkRuleForAdding);
				}

				List<String> checkEventForAdding = new ArrayList<>();

				checkEventForAdding.add(object[16].toString());
				checkEventForAdding.add(object[2].toString());
				checkEventForAdding.add(object[3].toString());
				checkEventForAdding.add(object[11].toString());

				checkEventForAdding.add(object[8].toString());
				checkEventForAdding.add(object[9].toString());
				checkEventForAdding.add(object[10].toString());

				if (!checEventList.contains(checkEventForAdding)) {
					JSONObject dataForAppenEventArray = new JSONObject();
					dataForAppenEventArray.put("task_rule_id", object[3]);
					dataForAppenEventArray.put("task_legi_id", object[2]);
					dataForAppenEventArray.put("task_cat_law_id", object[16]);
					dataForAppenEventArray.put("task_Event", object[4]);

					dataForAppenEventArray.put("orga_id", object[8]);
					dataForAppenEventArray.put("loca_id", object[9]);
					dataForAppenEventArray.put("dept_id", object[10]);

					eventarray.add(dataForAppenEventArray);
					checEventList.add(checkEventForAdding);
				}

				List<String> checkSubEventForAdding = new ArrayList<>();

				checkSubEventForAdding.add(object[16].toString());
				checkSubEventForAdding.add(object[2].toString());
				checkSubEventForAdding.add(object[3].toString());
				checkSubEventForAdding.add(object[4].toString());
				checkSubEventForAdding.add(object[5].toString());

				checkSubEventForAdding.add(object[8].toString());
				checkSubEventForAdding.add(object[9].toString());
				checkSubEventForAdding.add(object[10].toString());

				if (!checSubEventList.contains(checkSubEventForAdding)) {
					JSONObject dataForAppenSubEventArray = new JSONObject();
					dataForAppenSubEventArray.put("task_rule_id", object[3]);
					dataForAppenSubEventArray.put("task_legi_id", object[2]);
					dataForAppenSubEventArray.put("task_cat_law_id", object[16]);
					dataForAppenSubEventArray.put("task_Event", object[4]);
					dataForAppenSubEventArray.put("task_Sub_Event", object[5]);

					dataForAppenSubEventArray.put("orga_id", object[8]);
					dataForAppenSubEventArray.put("loca_id", object[9]);
					dataForAppenSubEventArray.put("dept_id", object[10]);

					subeventarray.add(dataForAppenSubEventArray);
					checSubEventList.add(checkSubEventForAdding);
				}

				/*-----------------------------------Code for adding filters list to array---------------------------*/

			}

			filtersObj.put("Executor", execarray);
			filtersObj.put("Evaluator", evalarray);
			filtersObj.put("FunctionHead", funheadarray);
			dataForOrganagram.add(filtersObj);

			filtersObjTask.put("Legislation", legiarray);
			filtersObjTask.put("Rule", rulearray);
			filtersObjTask.put("Event", eventarray);
			filtersObjTask.put("Sub_Event", subeventarray);

			dataForTaskFilter.add(filtersObjTask);

			dataForSend.put("OrganogramFilter", dataForOrganagram);
			dataForSend.put("TasksFilter", dataForTaskFilter);

			return dataForSend.toJSONString();
		} catch (Exception e) {
			dataForSend.put("responseMessage", "Failed");
			e.printStackTrace();
			return dataForSend.toJSONString();
		}

	}

	// New Code
	@Override
	public String getCategoryList(HttpSession session) {
		JSONArray dataForSend = new JSONArray();
		try {
			List<Object> orga = tasksRepositoryDao.getCategoryList(session);

			Iterator<Object> itr = orga.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("cat_law_id", object[0]);
				objForAppend.put("cat_law_name", object[1]);

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

	@Override
	public String getTypeOfTask(HttpSession session) {
		JSONArray dataForSend = new JSONArray();
		try {
			List<Object> orga = tasksRepositoryDao.getTypeOfTask(session);

			Iterator<Object> itr = orga.iterator();
			while (itr.hasNext()) {
				String str = (String) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("task_type_of_task", str);

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

	@Override
	public String getFrequencyList(HttpSession session) {
		JSONArray dataForSend = new JSONArray();
		try {
			List<Object> orga = tasksRepositoryDao.getFrequencyList(session);

			Iterator<Object> itr = orga.iterator();
			while (itr.hasNext()) {
				String str = (String) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("task_frequency", str);

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

	@Override
	public String getExeEvalListByEntity(String orga_id, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		List<List> checExecList = new ArrayList<>();
		List<List> checEvalList = new ArrayList<>();
		JSONArray execarray = new JSONArray();
		JSONArray evalarray = new JSONArray();

		JSONArray dataForOrganagram = new JSONArray();
		JSONObject filtersObj = new JSONObject();
		try {
			List<Object> allTasks = tasksRepositoryDao.getExeEvalListByEntity(
					Integer.parseInt(session.getAttribute("sess_user_id").toString()),
					Integer.parseInt(session.getAttribute("sess_role_id").toString()), orga_id);
			Iterator<Object> itr = allTasks.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();

				JSONObject objForAppend = new JSONObject();

				/*-----------------------------------Code for adding filters list to array---------------------------*/

				List<Integer> checkExecForAdding = new ArrayList<>();

				checkExecForAdding.add(Integer.parseInt(object[8].toString()));
				checkExecForAdding.add(Integer.parseInt(object[9].toString()));
				checkExecForAdding.add(Integer.parseInt(object[10].toString()));
				checkExecForAdding.add(Integer.parseInt(object[7].toString()));

				if (!checExecList.contains(checkExecForAdding)) {
					JSONObject dataForAppendExecArray = new JSONObject();
					dataForAppendExecArray.put("user_id", object[7]);
					dataForAppendExecArray.put("user_name", object[12].toString() + " " + object[13].toString());
					dataForAppendExecArray.put("orga_id", object[8]);
					dataForAppendExecArray.put("loca_id", object[9]);
					dataForAppendExecArray.put("dept_id", object[10]);

					execarray.add(dataForAppendExecArray);
					checExecList.add(checkExecForAdding);
				}

				List<Integer> checkEvalForAdding = new ArrayList<>();

				checkEvalForAdding.add(Integer.parseInt(object[8].toString()));
				checkEvalForAdding.add(Integer.parseInt(object[9].toString()));
				checkEvalForAdding.add(Integer.parseInt(object[10].toString()));
				checkEvalForAdding.add(Integer.parseInt(object[11].toString()));

				if (!checEvalList.contains(checkEvalForAdding)) {
					JSONObject dataForAppendEvalArray = new JSONObject();
					dataForAppendEvalArray.put("user_id", object[11]);
					dataForAppendEvalArray.put("user_name", object[14].toString() + " " + object[15].toString());
					dataForAppendEvalArray.put("orga_id", object[8]);
					dataForAppendEvalArray.put("loca_id", object[9]);
					dataForAppendEvalArray.put("dept_id", object[10]);

					evalarray.add(dataForAppendEvalArray);
					checEvalList.add(checkEvalForAdding);
				}
			}

			filtersObj.put("Executor", execarray);
			filtersObj.put("Evaluator", evalarray);

			dataForOrganagram.add(filtersObj);
			dataForSend.put("OrganogramFilter", dataForOrganagram);

			return dataForSend.toJSONString();
		} catch (Exception e) {
			dataForSend.put("responseMessage", "Failed");
			e.printStackTrace();
			return dataForSend.toJSONString();
		}

	}
}
