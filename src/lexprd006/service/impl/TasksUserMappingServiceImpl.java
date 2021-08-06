package lexprd006.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lexprd006.dao.CommonLogsDao;
import lexprd006.dao.EntityDao;
import lexprd006.dao.EntityMappingDao;
import lexprd006.dao.FunctionDao;
import lexprd006.dao.TasksConfigurationDao;
import lexprd006.dao.TasksRepositoryDao;
import lexprd006.dao.TasksUserMappingDao;
import lexprd006.dao.UnitDao;
import lexprd006.dao.UserEntityMappingDao;
import lexprd006.dao.UsersDao;
import lexprd006.domain.Department;
import lexprd006.domain.Location;
import lexprd006.domain.Organization;
import lexprd006.domain.TaskAssignLogs;
import lexprd006.domain.TaskChangeComplianeAssignLogs;
import lexprd006.domain.TaskTransactional;
import lexprd006.domain.TaskUserMapping;
import lexprd006.domain.User;
import lexprd006.service.TasksUserMappingService;
import lexprd006.service.UtilitiesService;

/*
 * Author: Mahesh Kharote
 * Date: 25/10/2016
 * Purpose: Service Impl for Task User Mapping
 * 
 * 
 * 
 * */

@Service(value = "tasksUserMappingService")
public class TasksUserMappingServiceImpl implements TasksUserMappingService {

	public final SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd");
	public final SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	TasksUserMappingDao tasksUserMappingDao;

	@Autowired
	UserEntityMappingDao userEntityMappingDao;

	@Autowired
	TasksConfigurationDao tasksconfigurationdao;

	@Autowired
	EntityMappingDao entityMappingDao;

	@Autowired
	UtilitiesService utilitiesService;

	@Autowired
	CommonLogsDao cLogsDao;
	
	@Autowired
	UsersDao userDao;

	@Autowired
	EntityDao entityDao;

	@Autowired
	UnitDao unitDao;

	@Autowired
	FunctionDao functionDao;
	
	@Autowired
	TasksRepositoryDao repositoryDao;
	
	private @Value("#{config['mail_user_name'] ?: 'null'}") String username;
	private @Value("#{config['mail_password'] ?: 'null'}") String password;
	private @Value("#{config['mail_smtp_host'] ?: 'null'}") String hostName;
	private @Value("#{config['mail_smtp_port'] ?: 'null'}") String portNo;
	private @Value("#{config['mail_from'] ?: 'null'}") String mailFrom;
	private @Value("#{config['project_name'] ?: 'null'}") String projectName;
	private @Value("#{config['project_url'] ?: 'null'}") String url;

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Save Tasks User Mapping rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String saveTasksUserMapping(String jsonString, HttpSession session) {
		JSONArray dataForSend = new JSONArray();

		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int tmap_dept_id = Integer.parseInt(jsonObj.get("dept_id").toString());
			// int tmap_loca_id = Integer.parseInt(jsonObj.get("tmap_loca_id").toString());
			int tmap_orga_id = Integer.parseInt(jsonObj.get("orga_id").toString());
			int tmap_pr_user_id = Integer.parseInt(jsonObj.get("pr_user_id").toString());
			int tmap_rw_user_id = Integer.parseInt(jsonObj.get("rw_user_id").toString());
			int tmap_fh_user_id = Integer.parseInt(jsonObj.get("fh_user_id").toString());
			String user_name = session.getAttribute("sess_user_full_name").toString();
			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());

			JSONArray tasks_list = (JSONArray) jsonObj.get("tasks_list");
			JSONArray dataForAppend = new JSONArray();
			JSONArray unit_list = (JSONArray) jsonObj.get("loca_list");

			ArrayList<Integer> unitIds = new ArrayList<>();

			for (int j = 0; j < unit_list.size(); j++) {
				JSONObject loca_list = (JSONObject) unit_list.get(j);
				int loca_id = Integer.parseInt(loca_list.get("tmap_loca_id").toString());
				int last_generated_id = tasksUserMappingDao.getMaxLastGeneratedValue(loca_id, tmap_dept_id);
				for (int i = 0; i < tasks_list.size(); i++) {
					JSONObject task = (JSONObject) tasks_list.get(i);
					int tmap_task_id = Integer.parseInt(task.get("tmap_task_id").toString());
					String tmap_lexcare_task_id = task.get("tmap_lexcare_task_id").toString();
					List<Object> tmapList = tasksUserMappingDao.getAssignedRecord(jsonString, tmap_task_id,
							tmap_lexcare_task_id, loca_id);

					if (tmapList.size() == 0) {
						List<Object> entityMappingList = tasksUserMappingDao.checkIfMappingExist(tmap_orga_id, loca_id,
								tmap_dept_id);
						if (entityMappingList.size() != 0) {
							TaskUserMapping mapping = new TaskUserMapping();
							mapping.setTmap_added_by(user_id);
							mapping.setTmap_approval_status("1");
							last_generated_id++;
							String tmap_last_generated_value_for_client_task_id = String.format("%07d",
									(last_generated_id));
							String tmap_client_id = "D" + String.format("%02d", loca_id)
									+ String.format("%02d", tmap_dept_id)
									+ tmap_last_generated_value_for_client_task_id;

							mapping.setTmap_client_tasks_id(tmap_client_id);
							mapping.setTmap_created_at(new Date());
							mapping.setTmap_dept_id(tmap_dept_id);
							mapping.setTmap_enable_status("1");
							mapping.setTmap_fh_user_id(tmap_fh_user_id);
							mapping.setTmap_last_generated_value_for_client_task_id(
									Integer.parseInt(tmap_last_generated_value_for_client_task_id));
							mapping.setTmap_lexcare_task_id(tmap_lexcare_task_id);
							mapping.setTmap_loca_id(loca_id);
							mapping.setTmap_orga_id(tmap_orga_id);
							mapping.setTmap_pr_user_id(tmap_pr_user_id);
							mapping.setTmap_rw_user_id(tmap_rw_user_id);
							mapping.setTmap_task_id(tmap_task_id);

							tasksUserMappingDao.persist(mapping);

							TaskAssignLogs assignLogs = new TaskAssignLogs();
							assignLogs.setAssignTime(new Date());
							assignLogs.setEvaluatorId(Integer.toString(tmap_fh_user_id));
							assignLogs.setExeutorId(Integer.toString(tmap_pr_user_id));
							assignLogs.setFunctionId(Integer.toString(tmap_rw_user_id));
							assignLogs.setUserId(user_id);
							assignLogs.setTasksId(tmap_client_id);
							assignLogs.setLexTasksId(tmap_lexcare_task_id);
							assignLogs.setEntityId(Integer.toString(tmap_orga_id));
							assignLogs.setUnitId(Integer.toString(loca_id));
							cLogsDao.persist(assignLogs);

							String assigned = "Lexcare_id-" + tmap_lexcare_task_id + " Client Task Id-" + tmap_client_id
									+ " orga_id-" + mapping.getTmap_orga_id() + " loca_id-" + mapping.getTmap_loca_id()
									+ " dept_id-" + mapping.getTmap_dept_id() + " performer_id-"
									+ mapping.getTmap_pr_user_id() + " reviewer_id-" + mapping.getTmap_rw_user_id()
									+ " function_head_id-" + mapping.getTmap_fh_user_id();
							utilitiesService.addTaskAssignedLog(assigned, user_id, user_name);
							JSONObject objForAppend = new JSONObject();
							objForAppend.put("Client_tasks_id", tmap_client_id);
							dataForSend.add(objForAppend);
						}
					}
				}
			}
			return dataForSend.toJSONString();
		} catch (

		Exception e) {
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("errorMessage", "Failed");
			e.printStackTrace();
			return objForAppend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(13/02/2017)
	// Method Purpose: Save Tasks User Mapping rest Call
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String SearchTaskFromMst_task(String jsonString) {
		JSONObject dataForSend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			JSONArray dataForAppend = new JSONArray();
			JSONArray dataForAssign = new JSONArray();
			if (jsonObj.get("dataRequiredFor").equals("taskMapping")) {
				List<Object> taskList = tasksUserMappingDao.searchTaskFromMstTaskForAssign(jsonString);

				System.out.println("task list:" + taskList.size());
				dataForSend.put("totalNoOfTasks", taskList.size());
				Iterator<Object> itr = taskList.iterator();
				while (itr.hasNext()) {
					Object[] object = (Object[]) itr.next();
					JSONObject objForAppend = new JSONObject();
					objForAppend.put("task_id", object[0].toString());
					objForAppend.put("lexcare_task_id", object[1].toString());
					objForAppend.put("task_legi_name", object[2].toString());
					objForAppend.put("task_who", object[3].toString());
					objForAppend.put("task_when", object[4].toString());
					objForAppend.put("task_activity", object[5].toString());
					objForAppend.put("task_procedure", object[6].toString());
					objForAppend.put("task_impact", object[7].toString());
					objForAppend.put("task_frequency", object[8].toString());
					objForAppend.put("task_reference", object[9].toString());
					objForAppend.put("task_rule_name", object[10].toString());

					dataForAppend.add(objForAppend);

				}

				List<Object> assignList = userEntityMappingDao.getDataForFilterUsingAccessTable();
				itr = null;
				itr = assignList.iterator();
				JSONObject filtersObj = new JSONObject();

				JSONArray entityarray = new JSONArray();
				JSONArray unitarray = new JSONArray();
				JSONArray funcarray = new JSONArray();
				JSONArray execarray = new JSONArray();
				JSONArray evalarray = new JSONArray();
				JSONArray funcheadarray = new JSONArray();

				List<Integer> checkEntiList = new ArrayList<>();
				List<List> checUnitList = new ArrayList<>();
				List<List> checFuncList = new ArrayList<>();
				List<List> checUserList = new ArrayList<>();

				while (itr.hasNext()) {
					Object[] object = (Object[]) itr.next();

					if (!checkEntiList.contains(object[0])) {
						JSONObject dataForAppendEntiArray = new JSONObject();
						dataForAppendEntiArray.put("orga_id", object[0]);
						dataForAppendEntiArray.put("orga_name", object[1]);

						entityarray.add(dataForAppendEntiArray);
						checkEntiList.add(Integer.parseInt(object[0].toString()));
					}

					List<Integer> checkUnitForAdding = new ArrayList<>();

					checkUnitForAdding.add(Integer.parseInt(object[0].toString()));
					checkUnitForAdding.add(Integer.parseInt(object[2].toString()));

					if (!checUnitList.contains(checkUnitForAdding)) {
						JSONObject dataForAppendUnitArray = new JSONObject();
						dataForAppendUnitArray.put("loca_id", object[2]);
						dataForAppendUnitArray.put("loca_name", object[3]);
						dataForAppendUnitArray.put("orga_id", object[0]);

						unitarray.add(dataForAppendUnitArray);
						checUnitList.add(checkUnitForAdding);
					}
					List<Integer> checkFuncForAdding = new ArrayList<>();

					checkFuncForAdding.add(Integer.parseInt(object[0].toString()));
					checkFuncForAdding.add(Integer.parseInt(object[2].toString()));
					checkFuncForAdding.add(Integer.parseInt(object[4].toString()));

					if (!checFuncList.contains(checkFuncForAdding)) {
						JSONObject dataForAppendFuncArray = new JSONObject();
						dataForAppendFuncArray.put("dept_id", object[4]);
						dataForAppendFuncArray.put("dept_name", object[5]);
						dataForAppendFuncArray.put("orga_id", object[0]);
						dataForAppendFuncArray.put("loca_id", object[2]);

						funcarray.add(dataForAppendFuncArray);
						checFuncList.add(checkFuncForAdding);
					}

					List<Integer> checkUserForAdding = new ArrayList<>();

					checkUserForAdding.add(Integer.parseInt(object[0].toString()));
					checkUserForAdding.add(Integer.parseInt(object[2].toString()));
					checkUserForAdding.add(Integer.parseInt(object[4].toString()));
					checkUserForAdding.add(Integer.parseInt(object[6].toString()));

					if (!checUserList.contains(checkUserForAdding)) {
						JSONObject dataForAppendUserArray = new JSONObject();
						dataForAppendUserArray.put("user_id", object[6]);
						dataForAppendUserArray.put("user_name", object[7].toString() + " " + object[8].toString());
						dataForAppendUserArray.put("orga_id", object[0]);
						dataForAppendUserArray.put("loca_id", object[2]);
						dataForAppendUserArray.put("dept_id", object[4]);

						if (Integer.parseInt(object[9].toString()) == 1) {
							if (!execarray.contains(dataForAppendUserArray)) {
								execarray.add(dataForAppendUserArray);
							}

						}
						if (Integer.parseInt(object[9].toString()) > 1) {
							evalarray.add(dataForAppendUserArray);
							if (!execarray.contains(dataForAppendUserArray)) {
								execarray.add(dataForAppendUserArray);

							}

						}
						if (Integer.parseInt(object[9].toString()) > 2) {
							if (!execarray.contains(dataForAppendUserArray)) {
								execarray.add(dataForAppendUserArray);
								evalarray.add(dataForAppendUserArray);
							}

							funcheadarray.add(dataForAppendUserArray);
						}
						checUserList.add(checkUserForAdding);
					}

				}
				filtersObj.put("Entity", entityarray);
				filtersObj.put("Unit", unitarray);
				filtersObj.put("Function", funcarray);
				filtersObj.put("Executor", execarray);
				filtersObj.put("Evaluator", evalarray);
				filtersObj.put("Function_Head", funcheadarray);

				dataForAssign.add(filtersObj);

				dataForSend.put("assignDropDowns", dataForAssign);
				dataForSend.put("allTasks", dataForAppend);
			} else {
				if (jsonObj.get("dataRequiredFor").equals("taskEnabling")) {
					List<Object> assignList = userEntityMappingDao.getDataForFilterUsingAccessTable();
					Iterator<Object> itr = assignList.iterator();

					JSONObject filtersObj = new JSONObject();

					JSONArray entityarray = new JSONArray();
					JSONArray unitarray = new JSONArray();
					JSONArray funcarray = new JSONArray();
					JSONArray execarray = new JSONArray();
					JSONArray evalarray = new JSONArray();
					JSONArray funcheadarray = new JSONArray();

					List<Integer> checkEntiList = new ArrayList<>();
					List<List> checUnitList = new ArrayList<>();
					List<List> checFuncList = new ArrayList<>();
					List<List> checUserList = new ArrayList<>();

					while (itr.hasNext()) {
						Object[] object = (Object[]) itr.next();

						if (!checkEntiList.contains(object[0])) {
							JSONObject dataForAppendEntiArray = new JSONObject();
							dataForAppendEntiArray.put("orga_id", object[0]);
							dataForAppendEntiArray.put("orga_name", object[1]);

							entityarray.add(dataForAppendEntiArray);
							checkEntiList.add(Integer.parseInt(object[0].toString()));
						}

						List<Integer> checkUnitForAdding = new ArrayList<>();

						checkUnitForAdding.add(Integer.parseInt(object[0].toString()));
						checkUnitForAdding.add(Integer.parseInt(object[2].toString()));

						if (!checUnitList.contains(checkUnitForAdding)) {
							JSONObject dataForAppendUnitArray = new JSONObject();
							dataForAppendUnitArray.put("loca_id", object[2]);
							dataForAppendUnitArray.put("loca_name", object[3]);
							dataForAppendUnitArray.put("orga_id", object[0]);

							unitarray.add(dataForAppendUnitArray);
							checUnitList.add(checkUnitForAdding);
						}
						List<Integer> checkFuncForAdding = new ArrayList<>();

						checkFuncForAdding.add(Integer.parseInt(object[0].toString()));
						checkFuncForAdding.add(Integer.parseInt(object[2].toString()));
						checkFuncForAdding.add(Integer.parseInt(object[4].toString()));

						if (!checFuncList.contains(checkFuncForAdding)) {
							JSONObject dataForAppendFuncArray = new JSONObject();
							dataForAppendFuncArray.put("dept_id", object[4]);
							dataForAppendFuncArray.put("dept_name", object[5]);
							dataForAppendFuncArray.put("orga_id", object[0]);
							dataForAppendFuncArray.put("loca_id", object[2]);

							funcarray.add(dataForAppendFuncArray);
							checFuncList.add(checkFuncForAdding);
						}

						List<Integer> checkUserForAdding = new ArrayList<>();

						checkUserForAdding.add(Integer.parseInt(object[0].toString()));
						checkUserForAdding.add(Integer.parseInt(object[2].toString()));
						checkUserForAdding.add(Integer.parseInt(object[4].toString()));
						checkUserForAdding.add(Integer.parseInt(object[6].toString()));

						if (!checUserList.contains(checkUserForAdding)) {
							JSONObject dataForAppendUserArray = new JSONObject();
							dataForAppendUserArray.put("user_id", object[6]);
							dataForAppendUserArray.put("user_name", object[7].toString() + " " + object[8].toString());
							dataForAppendUserArray.put("orga_id", object[0]);
							dataForAppendUserArray.put("loca_id", object[2]);
							dataForAppendUserArray.put("dept_id", object[4]);

							if (Integer.parseInt(object[9].toString()) == 1) {
								if (!execarray.contains(dataForAppendUserArray)) {
									execarray.add(dataForAppendUserArray);
								}

							}
							if (Integer.parseInt(object[9].toString()) > 1) {
								evalarray.add(dataForAppendUserArray);
								if (!execarray.contains(dataForAppendUserArray)) {
									execarray.add(dataForAppendUserArray);

								}

							}
							if (Integer.parseInt(object[9].toString()) > 2) {
								if (!execarray.contains(dataForAppendUserArray)) {
									execarray.add(dataForAppendUserArray);
									evalarray.add(dataForAppendUserArray);
								}

								funcheadarray.add(dataForAppendUserArray);
							}
							checUserList.add(checkUserForAdding);
						}

					}
					filtersObj.put("Entity", entityarray);
					filtersObj.put("Unit", unitarray);
					filtersObj.put("Function", funcarray);
					filtersObj.put("Executor", execarray);
					filtersObj.put("Evaluator", evalarray);
					filtersObj.put("Function_Head", funcheadarray);

					dataForAssign.add(filtersObj);

					dataForSend.put("assignDropDowns", dataForAssign);
				}
			}

			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			dataForSend.put("errorMessage", "Failed");
			return dataForSend.toJSONString();
		}

	}

	// Method Written By: Mahesh Kharote(13/02/2017)
	// Method Purpose: Save Tasks User Mapping rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String getDistinctCountries() {
		JSONArray dataForSend = new JSONArray();
		try {

			List<Object> countries_list = tasksUserMappingDao.getDistinctCountries();

			Iterator<Object> iterator = countries_list.iterator();

			while (iterator.hasNext()) {
				Object[] object = (Object[]) iterator.next();
				JSONObject dataForAppend = new JSONObject();
				dataForAppend.put("country_id", object[0]);
				dataForAppend.put("country_name", object[1].toString());

				dataForSend.add(dataForAppend);
			}

			return dataForSend.toJSONString();
		} catch (Exception e) {
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("errorMessage", "Failed");
			dataForSend.add(objForAppend);
			e.printStackTrace();
			return dataForSend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(13/02/2017)
	// Method Purpose: Save Tasks User Mapping rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String getAllStateForCountry(String jsonString) {
		JSONArray dataForSend = new JSONArray();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int task_country_id = Integer.parseInt(jsonObj.get("country_id").toString());

			List<Object> state_list = tasksUserMappingDao.getAllStateForCountry(task_country_id);

			Iterator<Object> iterator = state_list.iterator();

			while (iterator.hasNext()) {
				Object[] object = (Object[]) iterator.next();
				JSONObject dataForAppend = new JSONObject();
				dataForAppend.put("state_id", object[0].toString());
				dataForAppend.put("state_name", object[1].toString());

				dataForSend.add(dataForAppend);
			}

			return dataForSend.toJSONString();
		} catch (Exception e) {
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("errorMessage", "Failed");
			dataForSend.add(objForAppend);
			e.printStackTrace();
			return dataForSend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(13/02/2017)
	// Method Purpose: Save Tasks User Mapping rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String getAllCat_lawFromMst_task(String jsonString) {
		JSONArray dataForSend = new JSONArray();
		try {

			List<Object> cat_law_list = tasksUserMappingDao.getAllCat_lawFromMst_task(jsonString);

			Iterator<Object> iterator = cat_law_list.iterator();

			while (iterator.hasNext()) {
				Object[] object = (Object[]) iterator.next();
				JSONObject dataForAppend = new JSONObject();
				dataForAppend.put("cat_law_id", object[0].toString());
				dataForAppend.put("cat_law_name", object[1].toString());

				dataForSend.add(dataForAppend);
			}

			return dataForSend.toJSONString();
		} catch (Exception e) {
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("errorMessage", "Failed");
			dataForSend.add(objForAppend);
			e.printStackTrace();
			return dataForSend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(13/02/2017)
	// Method Purpose: Save Tasks User Mapping rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String getlegislationFromMst_task(String jsonString) {
		JSONArray dataForSend = new JSONArray();
		try {

			List<Object> legi_list = tasksUserMappingDao.getlegislationFromMst_task(jsonString);

			Iterator<Object> iterator = legi_list.iterator();

			while (iterator.hasNext()) {
				Object[] object = (Object[]) iterator.next();
				JSONObject dataForAppend = new JSONObject();
				dataForAppend.put("legi_id", object[0].toString());
				dataForAppend.put("legi_name", object[1].toString());

				dataForSend.add(dataForAppend);
			}

			return dataForSend.toJSONString();
		} catch (Exception e) {
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("errorMessage", "Failed");
			dataForSend.add(objForAppend);
			e.printStackTrace();
			return dataForSend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(13/02/2017)
	// Method Purpose: Save Tasks User Mapping rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String getRuleFromMst_task(String jsonString) {
		JSONArray dataForSend = new JSONArray();
		try {

			List<Object> rule_list = tasksUserMappingDao.getRuleFromMst_task(jsonString);

			Iterator<Object> iterator = rule_list.iterator();

			while (iterator.hasNext()) {
				Object[] object = (Object[]) iterator.next();
				JSONObject dataForAppend = new JSONObject();
				dataForAppend.put("rule_id", object[0].toString());
				dataForAppend.put("rule_name", object[1].toString());

				dataForSend.add(dataForAppend);
			}

			return dataForSend.toJSONString();
		} catch (Exception e) {
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("errorMessage", "Failed");
			dataForSend.add(objForAppend);
			e.printStackTrace();
			return dataForSend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(13/02/2017)
	// Method Purpose: Save Tasks User Mapping rest Call
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String getAllMappedTasksForEnablingPage(String jsonString, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		try {
			List<Object> allTasks = tasksUserMappingDao.getAllMappedTasks();
			Iterator<Object> itr = allTasks.iterator();
			JSONArray dataForAppend = new JSONArray();
			JSONArray filters = new JSONArray();
			JSONObject filtersObj = new JSONObject();
			JSONArray Entity = new JSONArray();

			JSONArray Unit = new JSONArray();
			JSONArray Function = new JSONArray();
			JSONArray Users = new JSONArray();
			JSONArray FHUsers = new JSONArray();
			JSONArray Legislations = new JSONArray();
			JSONArray Rules = new JSONArray();

			List<Integer> CheckEntityForAddingToFilter = new ArrayList<>();
			ArrayList<List> checkUntiForAddingToFilter = new ArrayList<>();
			ArrayList<List> checkFuncForAddingToFilter = new ArrayList<>();
			ArrayList<List> checkExecEvalForAddingToFilter = new ArrayList<>();
			ArrayList<List> checkFunHeadForAddingToFilter = new ArrayList<>();
			ArrayList<List> checkLegiForAddingToFilter = new ArrayList<>();
			ArrayList<List> checkRuleForAddingToFilter = new ArrayList<>();

			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();

				JSONObject objForAppend = new JSONObject();

				objForAppend.put("tmap_id", object[8]);
				objForAppend.put("tmap_client_tasks_id", object[10]);
				objForAppend.put("task_legi_name", object[1]);
				objForAppend.put("task_rule_name", object[2]);
				objForAppend.put("task_reference", object[3]);
				objForAppend.put("task_activity_who", object[4]);
				objForAppend.put("task_activity_when", object[5]);
				objForAppend.put("task_activity", object[6]);
				objForAppend.put("task_procedure", object[7]);
				objForAppend.put("tmap_enable_status", object[9]);
				objForAppend.put("executor_id", object[11]);
				objForAppend.put("executor_name", object[12].toString() + " " + object[13].toString());
				objForAppend.put("evaluator_id", object[14]);
				objForAppend.put("orga_id", object[17]);
				objForAppend.put("loca_id", object[19]);
				objForAppend.put("dept_id", object[21]);
				objForAppend.put("task_lexcare_id", object[23]);
				objForAppend.put("function_head_id", object[26]);
				objForAppend.put("task_legi_id", object[24]);
				objForAppend.put("task_rule_id", object[25]);

				dataForAppend.add(objForAppend);
				/*
				 * -----------------------------------Code for adding filters list to
				 * array---------------------------
				 */

				if (!CheckEntityForAddingToFilter.contains(object[17])) {
					JSONObject dataForEntityFilterObj = new JSONObject();
					dataForEntityFilterObj.put("orga_id", object[17]);
					dataForEntityFilterObj.put("orga_name", object[18]);

					Entity.add(dataForEntityFilterObj);
					CheckEntityForAddingToFilter.add(Integer.parseInt(object[17].toString()));
				}

				// Creating new list to add orga id and loca id
				List<Integer> checkentiunitforadding = new ArrayList<>();

				// Adding current orga Id and loca id to the for checking
				checkentiunitforadding.add(Integer.parseInt(object[17].toString()));
				checkentiunitforadding.add(Integer.parseInt(object[19].toString()));

				// Checking if current orga id and loca id are already added to the main filter
				// list
				if (!checkUntiForAddingToFilter.contains(checkentiunitforadding)) {
					JSONObject dataForUnitFilterObj = new JSONObject();
					dataForUnitFilterObj.put("loca_id", object[19]);
					dataForUnitFilterObj.put("loca_name", object[20]);
					dataForUnitFilterObj.put("orga_id", object[17]);

					Unit.add(dataForUnitFilterObj);

					checkUntiForAddingToFilter.add(checkentiunitforadding);
				}

				// Creating new list to add orga id and loca id and dept id
				List<Integer> checkentiunitdeptforadding = new ArrayList<>();

				// Adding current orga Id and loca id and dept id to the for checking
				checkentiunitdeptforadding.add(Integer.parseInt(object[17].toString()));
				checkentiunitdeptforadding.add(Integer.parseInt(object[19].toString()));
				checkentiunitdeptforadding.add(Integer.parseInt(object[21].toString()));

				// Checking if current orga id and loca id and dept id are already added to the
				// main filter list
				if (!checkFuncForAddingToFilter.contains(checkentiunitdeptforadding)) {
					JSONObject dataForFuncFilterObj = new JSONObject();
					dataForFuncFilterObj.put("dept_id", object[21]);
					dataForFuncFilterObj.put("dept_name", object[22]);
					dataForFuncFilterObj.put("loca_id", object[19]);
					dataForFuncFilterObj.put("orga_id", object[17]);

					Function.add(dataForFuncFilterObj);

					checkFuncForAddingToFilter.add(checkentiunitdeptforadding);
				}

				// Creating new list to add orga id and loca id and dept id and execeval
				List<Integer> checkentiunitdeptexecevalforadding = new ArrayList<>();

				// Adding current orga Id and loca id and dept id and exec and eval to the for
				// checking

				checkentiunitdeptexecevalforadding.add(Integer.parseInt(object[17].toString()));
				checkentiunitdeptexecevalforadding.add(Integer.parseInt(object[19].toString()));
				checkentiunitdeptexecevalforadding.add(Integer.parseInt(object[21].toString()));
				checkentiunitdeptexecevalforadding.add(Integer.parseInt(object[11].toString()));
				checkentiunitdeptexecevalforadding.add(Integer.parseInt(object[14].toString()));

				// Checking if current orga id and loca id and dept id and exec id and eval id
				// are already added to the main filter list
				if (!checkExecEvalForAddingToFilter.contains(checkentiunitdeptexecevalforadding)) {
					JSONObject dataForExecEvalFilterObj = new JSONObject();

					dataForExecEvalFilterObj.put("dept_id", object[21]);
					dataForExecEvalFilterObj.put("loca_id", object[19]);
					dataForExecEvalFilterObj.put("orga_id", object[17]);
					dataForExecEvalFilterObj.put("exec_id", object[11]);
					dataForExecEvalFilterObj.put("exec_name", object[12].toString() + " " + object[13].toString());
					dataForExecEvalFilterObj.put("eval_id", object[14]);
					dataForExecEvalFilterObj.put("eval_name", object[15].toString() + " " + object[16].toString());

					Users.add(dataForExecEvalFilterObj);

					checkExecEvalForAddingToFilter.add(checkentiunitdeptexecevalforadding);
				}

				List<Integer> chkEntiUnitDeptLegiForAdding = new ArrayList<>();

				chkEntiUnitDeptLegiForAdding.add(Integer.parseInt(object[17].toString()));
				chkEntiUnitDeptLegiForAdding.add(Integer.parseInt(object[19].toString()));
				chkEntiUnitDeptLegiForAdding.add(Integer.parseInt(object[21].toString()));
				chkEntiUnitDeptLegiForAdding.add(Integer.parseInt(object[24].toString()));
				// chkEntiUnitDeptLegiForAdding.add(Integer.parseInt(object[25].toString()));

				if (!checkLegiForAddingToFilter.contains(chkEntiUnitDeptLegiForAdding)) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("orga_id", object[17]);
					jsonObject.put("loca_id", object[19]);
					jsonObject.put("dept_id", object[21]);
					jsonObject.put("task_legi_id", object[24]);
					// jsonObject.put("rule_id", object[25]);
					jsonObject.put("task_legi_name", object[1].toString());
					// jsonObject.put("rule_name", object[2].toString());

					Legislations.add(jsonObject);
					checkLegiForAddingToFilter.add(chkEntiUnitDeptLegiForAdding);
				}

				List<Integer> checkRuleForAdding = new ArrayList<>();

				checkRuleForAdding.add(Integer.parseInt(object[24].toString()));
				checkRuleForAdding.add(Integer.parseInt(object[25].toString()));

				if (!checkRuleForAddingToFilter.contains(checkRuleForAdding)) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("task_legi_id", object[24]);
					jsonObject.put("task_rule_id", object[25]);
					jsonObject.put("task_rule_name", object[2].toString());
					Rules.add(jsonObject);
					checkRuleForAddingToFilter.add(checkRuleForAdding);
				}

				List<Integer> checkFunHeadForAdding = new ArrayList<>();

				checkFunHeadForAdding.add(Integer.parseInt(object[17].toString()));
				checkFunHeadForAdding.add(Integer.parseInt(object[19].toString()));
				checkFunHeadForAdding.add(Integer.parseInt(object[21].toString()));
				checkFunHeadForAdding.add(Integer.parseInt(object[26].toString()));

				if (!checkFunHeadForAddingToFilter.contains(checkFunHeadForAdding)) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("orga_id", object[17]);
					jsonObject.put("loca_id", object[19]);
					jsonObject.put("dept_id", object[21]);
					jsonObject.put("fh_id", object[26]);
					jsonObject.put("fh_name", object[27].toString() + " " + object[28].toString());

					FHUsers.add(jsonObject);
					checkFunHeadForAddingToFilter.add(checkFunHeadForAdding);
				}

				/*-----------------------------------Code for adding filters list to array---------------------------*/

			}
			filtersObj.put("Entity", Entity);
			filtersObj.put("Unit", Unit);
			filtersObj.put("Function", Function);
			filtersObj.put("Users", Users);
			filtersObj.put("FHUsers", FHUsers);
			filtersObj.put("Legislations", Legislations);
			filtersObj.put("Rules", Rules);

			filters.add(filtersObj);

			dataForSend.put("All_Tasks", dataForAppend);
			dataForSend.put("Filters", filters);

			return dataForSend.toJSONString();
		} catch (Exception e) {
			dataForSend.put("responseMessage", "Failed");
			e.printStackTrace();
			return dataForSend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(13/02/2017)
	// Method Purpose: Save Tasks User Mapping rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String enablingOfTasks(String jsonString, HttpSession session) {
		JSONObject objForAppend = new JSONObject();
		try {
			ArrayList<Integer> tmap_ids = new ArrayList<>();
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String operation_to_perform = jsonObj.get("operation_to_perform").toString();

			String user_name = session.getAttribute("sess_user_full_name").toString();
			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());

			JSONArray configured_tasks = (JSONArray) jsonObj.get("tasks_list");
			for (int i = 0; i < configured_tasks.size(); i++) {
				JSONObject configured_tasks_obj = (JSONObject) configured_tasks.get(i);
				int tmap_id = Integer.parseInt(configured_tasks_obj.get("tmap_id").toString());
				tmap_ids.add(tmap_id);
			}
			String responseString = "";
			if (operation_to_perform.equals("enable")) {
				responseString = tasksUserMappingDao.enableTasks(tmap_ids);
			} else {
				responseString = tasksUserMappingDao.disableTasks(tmap_ids);

				ArrayList<Integer> ttrn_ids = new ArrayList<>();
				for (int i = 0; i < tmap_ids.size(); i++) {
					int id = tmap_ids.get(i);

					List<Object> res = tasksUserMappingDao.getDisabledTaskDetails(id);
					Iterator<Object> itr_res = res.iterator();
					while (itr_res.hasNext()) {
						Object[] objects = (Object[]) itr_res.next();
						if (objects[1] != null && !objects[1].toString().equals("Completed")
								&& !objects[1].toString().equals("Event_Not_Occured")
								&& !objects[1].toString().equals("Inactive")) {
							int ttrn_id = Integer.parseInt(objects[0].toString());
							ttrn_ids.add(ttrn_id);
						}
					}

					tasksconfigurationdao.deactivateTasks(ttrn_ids);
				}
				if (ttrn_ids.size() > 0)
					utilitiesService.addTaskActivationLog(ttrn_ids, "Deactivated", user_id, user_name);

			}

			if (responseString.equals("Success")) {
				objForAppend.put("responseMessage", "Success");
				utilitiesService.addTaskEnableLog(tmap_ids, operation_to_perform, user_id, user_name);

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

	// Method Written By: Mahesh Kharote(13/02/2017)
	// Method Purpose: Save Tasks User Mapping rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String changeComplianceOwner(String jsonString, HttpSession session) {
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
			System.out.println("These many tasks are re assigned: " + assinged_tasks.size());
			
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
				TaskUserMapping taskUserMapping = tasksUserMappingDao.getTmapForchangeComplianceOwner(
						Integer.parseInt(assinged_tasks_obj.get("tmap_id").toString()));

				client_task_id = taskUserMapping.getTmap_client_tasks_id();
				String previous = "orga_id-" + taskUserMapping.getTmap_orga_id() + " loca_id-"
						+ taskUserMapping.getTmap_loca_id() + " dept_id-" + taskUserMapping.getTmap_dept_id()
						+ " performer_id-" + taskUserMapping.getTmap_pr_user_id() + " reviewer_id-"
						+ taskUserMapping.getTmap_rw_user_id() + " function_head_id-"
						+ taskUserMapping.getTmap_fh_user_id();
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

				tasksUserMappingDao.updateTaskUserMapping(taskUserMapping);

				String changed = "orga_id-" + taskUserMapping.getTmap_orga_id() + " loca_id-"
						+ taskUserMapping.getTmap_loca_id() + " dept_id-" + taskUserMapping.getTmap_dept_id()
						+ " performer_id-" + taskUserMapping.getTmap_pr_user_id() + " reviewer_id-"
						+ taskUserMapping.getTmap_rw_user_id() + " function_head_id-"
						+ taskUserMapping.getTmap_fh_user_id();

				utilitiesService.addChangeComplianceOwnerLog(
						Integer.parseInt(assinged_tasks_obj.get("tmap_id").toString()), previous, changed, user_id,
						user_name);

				/*-----Changing performer user id if task is not completed and legal due date has not crossed--------------------------*/
				List<Object> allTasks = tasksconfigurationdao.getLatestTtrnForChangeComplianceOwner(
						Integer.parseInt(assinged_tasks_obj.get("tmap_id").toString()));
				System.out.println("This is tmap id: " + Integer.parseInt(assinged_tasks_obj.get("tmap_id").toString()));
				if (allTasks.size() > 0) {
					Iterator<Object> iterator = allTasks.iterator();
					while (iterator.hasNext()) {
						Object[] taskTransactional = (Object[]) iterator.next();
						System.out.println(taskTransactional[0]); // ttrn_id
						System.out.println(taskTransactional[1]); // ttrn_legal_due_date
						String lglDueDate = taskTransactional[1].toString();
						Integer ttrnId = Integer.parseInt(taskTransactional[0].toString());

						SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd");
						Date curDate = new Date();

						Date legalDueDate = sDate.parse(lglDueDate);
						String formatCurDate = sDate.format(curDate);
						Date currentDate = sDate.parse(formatCurDate);
						if (legalDueDate.after(currentDate) || legalDueDate.equals(currentDate)) {
							System.out.println("In change pr_user_id for transactional");

							List<Object> tasksForCompletionNativeQuery = tasksconfigurationdao
									.getTasksForCompletionNativeQuery(ttrnId);
							TaskTransactional taskTransactional1 = new TaskTransactional();
							if (tasksForCompletionNativeQuery.size() > 0) {
								Iterator iterator2 = tasksForCompletionNativeQuery.iterator();
								while (iterator2.hasNext()) {
									Object nextTtrnStatus = iterator2.next();
									System.out.println("Inactive : " + nextTtrnStatus.equals("Inactive"));
									System.out.println("Active : " + nextTtrnStatus.equals("Active"));
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
						System.out.println("parseLglDate : " + legalDueDate);
						System.out.println("parseCurrentDate : " + currentDate);
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
				utilitiesService.addMailToLog(executor.getUser_email(), "Change Compliance Owner Alert",client_task_id, address); //Mail
				// log
			}

			catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error in transport send");
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

	@Override
	public String deleteTasksUserMapping(String jsonString, HttpSession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTaskForChangeComplianceOwnerPage(String jsonString, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		try {
			List<Object> allTasks = tasksUserMappingDao.getAllMappedTasks();
			Iterator<Object> itr = allTasks.iterator();
			JSONArray dataForAppend = new JSONArray();
			JSONArray filters = new JSONArray();
			JSONObject filtersObj = new JSONObject();

			JSONArray Legislations = new JSONArray();
			JSONArray Rules = new JSONArray();

			ArrayList<List> checkLegiForAddingToFilter = new ArrayList<>();
			ArrayList<List> checkRuleForAddingToFilter = new ArrayList<>();

			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();

				JSONObject objForAppend = new JSONObject();

				List<Integer> chkEntiUnitDeptLegiForAdding = new ArrayList<>();

				chkEntiUnitDeptLegiForAdding.add(Integer.parseInt(object[17].toString()));
				chkEntiUnitDeptLegiForAdding.add(Integer.parseInt(object[19].toString()));
				chkEntiUnitDeptLegiForAdding.add(Integer.parseInt(object[21].toString()));
				chkEntiUnitDeptLegiForAdding.add(Integer.parseInt(object[24].toString()));
				// chkEntiUnitDeptLegiForAdding.add(Integer.parseInt(object[25].toString()));

				if (!checkLegiForAddingToFilter.contains(chkEntiUnitDeptLegiForAdding)) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("orga_id", object[17]);
					jsonObject.put("loca_id", object[19]);
					jsonObject.put("dept_id", object[21]);
					jsonObject.put("task_legi_id", object[24]);
					// jsonObject.put("rule_id", object[25]);
					jsonObject.put("task_legi_name", object[1].toString());
					// jsonObject.put("rule_name", object[2].toString());

					Legislations.add(jsonObject);
					checkLegiForAddingToFilter.add(chkEntiUnitDeptLegiForAdding);
				}

				List<Integer> checkRuleForAdding = new ArrayList<>();

				checkRuleForAdding.add(Integer.parseInt(object[24].toString()));
				checkRuleForAdding.add(Integer.parseInt(object[25].toString()));

				if (!checkRuleForAddingToFilter.contains(checkRuleForAdding)) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("task_legi_id", object[24]);
					jsonObject.put("task_rule_id", object[25]);
					jsonObject.put("task_rule_name", object[2].toString());
					Rules.add(jsonObject);
					checkRuleForAddingToFilter.add(checkRuleForAdding);
				}

			}

			filtersObj.put("Legislations", Legislations);
			filtersObj.put("Rules", Rules);

			filters.add(filtersObj);

			// dataForSend.put("All_Tasks", dataForAppend);
			dataForSend.put("Filters", filters);

			return dataForSend.toJSONString();
		} catch (Exception e) {
			dataForSend.put("responseMessage", "Failed");
			e.printStackTrace();
			return dataForSend.toJSONString();
		}
	}

}
