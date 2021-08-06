package lexprd006.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.csvreader.CsvReader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lexprd006.dao.AssignTaskDao;
import lexprd006.dao.EntityDao;
import lexprd006.dao.FunctionDao;
import lexprd006.dao.TasksConfigurationDao;
import lexprd006.dao.TasksUserMappingDao;
import lexprd006.dao.UnitDao;
import lexprd006.dao.UsersDao;
import lexprd006.domain.Task;
import lexprd006.domain.TaskUserMapping;
import lexprd006.domain.User;
import lexprd006.service.AssignTaskService;
import lexprd006.service.UtilitiesService;

@Service(value = "assignTaskService")
public class AssignTaskServiceImpl implements AssignTaskService {

	@Autowired
	AssignTaskDao assignTaskDao;

	@Autowired
	UtilitiesService utilitiesService;

	@Autowired
	TasksUserMappingDao taskUserMappingDao;

	@Autowired
	EntityDao entityDao;

	@Autowired
	UnitDao unitDao;

	@Autowired
	FunctionDao functionDao;

	@Autowired
	UsersDao userDao;

	@Autowired
	TasksUserMappingDao tasksUserMappingDao;

	@Autowired
	TasksConfigurationDao taskConfigurationDao;

	public final SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public final SimpleDateFormat sdfOut = new SimpleDateFormat("dd-MM-yyyy");

	@SuppressWarnings("unchecked")
	public String getEntityList(HttpSession session) {

		JSONArray dataForSend = new JSONArray();
		try {
			List<Object> orga = assignTaskDao.getEntityList(session);

			Iterator<Object> itr = orga.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("orga_id", object[1]);
				objForAppend.put("orga_name", object[0]);
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

	@SuppressWarnings("unchecked")
	public String getUnitList(String entity_id, HttpSession session) {

		JSONArray dataForSend = new JSONArray();
		try {
			List<Object> orga = assignTaskDao.getUnitList(entity_id, session);

			Iterator<Object> itr = orga.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("orga_name", object[0]);
				objForAppend.put("orga_id", object[1]);
				objForAppend.put("loca_id", object[2]);
				objForAppend.put("loca_name", object[3]);
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

	@SuppressWarnings("unchecked")
	public String getFunctionList(String jsonString, HttpSession session) {
		JSONArray dataForSend = new JSONArray();
		JSONObject objForSend = new JSONObject();
		int temp_dept_id = 0;
		try {

			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int orga_id = Integer.parseInt(jsonObj.get("orga_id").toString());
			JSONArray unit_list = (JSONArray) jsonObj.get("loca_list");
			for (int i = 0; i < unit_list.size(); i++) {
				JSONObject loca_list = (JSONObject) unit_list.get(i);
				int loca_id = Integer.parseInt(loca_list.get("tmap_loca_id").toString());
				List<Object> orga = assignTaskDao.getFunctionList(loca_id, orga_id, session);

				Iterator<Object> itr = orga.iterator();
				while (itr.hasNext()) {
					Object[] object = (Object[]) itr.next();
					JSONObject objForAppend = new JSONObject();
					objForAppend.put("orga_name", object[0]);
					objForAppend.put("orga_id", object[1]);
					objForAppend.put("loca_id", object[2]);
					objForAppend.put("loca_name", object[3]);
					objForAppend.put("dept_id", object[4]);
					objForAppend.put("dept_name", object[5]);

					dataForSend.add(objForAppend);

					objForAppend.put("responseMessage", "Success");
				}
			}

			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("responseMessage", "Failed");
			dataForSend.add(objForAppend);
			return dataForSend.toJSONString();
		}
	}

	@SuppressWarnings("unchecked")
	public String getExecutorList(String jsonString) {
		JSONObject objForSend = new JSONObject();
		try {
			JSONArray dataForAppend = new JSONArray();
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int orga_id = Integer.parseInt(jsonObj.get("orga_id").toString());
			// int loca_id = Integer.parseInt(jsonObj.get("tmap_loca_id").toString());
			int dept_id = 0;
			if (jsonObj.get("dept_id") != null) {
				dept_id = Integer.parseInt(jsonObj.get("dept_id").toString());
			}

			JSONArray unit_list = (JSONArray) jsonObj.get("loca_list");
			for (int i = 0; i < unit_list.size(); i++) {
				JSONObject loca_list = (JSONObject) unit_list.get(i);
				int loca_id = Integer.parseInt(loca_list.get("tmap_loca_id").toString());
				List<Object> exeList = assignTaskDao.getExecutorList(orga_id, loca_id, dept_id);
				Iterator<Object> itr = exeList.iterator();
				while (itr.hasNext()) {
					Object[] object = (Object[]) itr.next();
					JSONObject objForAppend = new JSONObject();
					objForAppend.put("user_id", object[0].toString());
					objForAppend.put("user_name", object[1].toString() + " " + object[2].toString());

					dataForAppend.add(objForAppend);

				}
			}

			objForSend.put("Executor", dataForAppend);
			objForSend.put("responseMessage", "Success");
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("responseMessage", "Failed");
			return objForSend.toJSONString();
		}

	}

	public String getEvaluatorList(String json) {

		JSONObject objForSend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(json);
			JSONArray dataForAppend = new JSONArray();
			int orga_id = Integer.parseInt(jsonObj.get("orga_id").toString());
			// int loca_id = Integer.parseInt(jsonObj.get("tmap_loca_id").toString());
			int dept_id = 0;
			if (jsonObj.get("dept_id") != null) {
				dept_id = Integer.parseInt(jsonObj.get("dept_id").toString());
			}

			JSONArray unit_list = (JSONArray) jsonObj.get("loca_list");
			for (int i = 0; i < unit_list.size(); i++) {
				JSONObject loca_list = (JSONObject) unit_list.get(i);
				int loca_id = Integer.parseInt(loca_list.get("tmap_loca_id").toString());
				List<Object> evalList = assignTaskDao.getEvaluatorList(orga_id, loca_id, dept_id);
				Iterator<Object> itr = evalList.iterator();
				while (itr.hasNext()) {
					Object[] object = (Object[]) itr.next();
					JSONObject objForAppend = new JSONObject();
					objForAppend.put("user_id", object[0].toString());
					objForAppend.put("user_name", object[1].toString() + " " + object[2].toString());

					dataForAppend.add(objForAppend);

				}
			}

			objForSend.put("Evaluator", dataForAppend);
			objForSend.put("responseMessage", "Success");
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("responseMessage", "Failed");
			return objForSend.toJSONString();
		}

	}

	@SuppressWarnings("unchecked")
	public String getFunHeadList(String json) {

		JSONObject objForSend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(json);
			int orga_id = Integer.parseInt(jsonObj.get("orga_id").toString());
			// int loca_id = Integer.parseInt(jsonObj.get("tmap_loca_id").toString());
			int dept_id = Integer.parseInt(jsonObj.get("dept_id").toString());
			JSONArray dataForAppend = new JSONArray();
			JSONArray unit_list = (JSONArray) jsonObj.get("loca_list");
			for (int i = 0; i < unit_list.size(); i++) {
				JSONObject loca_list = (JSONObject) unit_list.get(i);
				int loca_id = Integer.parseInt(loca_list.get("tmap_loca_id").toString());
				List<Object> funHeadList = assignTaskDao.getFunHeadList(orga_id, loca_id, dept_id);
				Iterator<Object> itr = funHeadList.iterator();
				while (itr.hasNext()) {
					Object[] object = (Object[]) itr.next();
					JSONObject objForAppend = new JSONObject();
					objForAppend.put("user_id", object[0].toString());
					objForAppend.put("user_name", object[1].toString() + " " + object[2].toString());

					dataForAppend.add(objForAppend);

				}
			}

			objForSend.put("FunctionHead", dataForAppend);
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
	public String getTaskListToAssign(String jsonString, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			JSONArray dataForAppend = new JSONArray();
			JSONArray dataForAssign = new JSONArray();

			System.out.println("jsonString : " + jsonString.toString());

			List<Object> taskList = assignTaskDao.searchTaskFromMstTaskForAssign(jsonString);

			System.out.println("task list:" + taskList.size());
			dataForSend.put("totalNoOfTasks", taskList.size());
			Iterator<Object> itr = taskList.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();

				// System.out.println("3 : " + object[3] + "\t 4 " + object[4] + "\t 5 " +
				// object[5]);

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

			dataForSend.put("assignDropDowns", dataForAssign);
			dataForSend.put("allTasks", dataForAppend);

			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			dataForSend.put("errorMessage", "Failed");
			return dataForSend.toJSONString();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public String getExeListForChangeOwner(String jsonString) {
		JSONObject objForSend = new JSONObject();
		try {
			JSONArray dataForAppend = new JSONArray();
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int orga_id = Integer.parseInt(jsonObj.get("orga_id").toString());
			int loca_id = 0;
			int dept_id = 0;
			if (jsonObj.get("loca_id") != null) {
				loca_id = Integer.parseInt(jsonObj.get("loca_id").toString());
			}
			if (jsonObj.get("dept_id") != null) {
				dept_id = Integer.parseInt(jsonObj.get("dept_id").toString());
			}

			List<Object> exeList = assignTaskDao.getExecutorList(orga_id, loca_id, dept_id);
			Iterator<Object> itr = exeList.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("user_id", object[0].toString());
				objForAppend.put("user_name", object[1].toString() + " " + object[2].toString());
				dataForAppend.add(objForAppend);
			}

			objForSend.put("Executor", dataForAppend);
			objForSend.put("responseMessage", "Success");
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("responseMessage", "Failed");
			return objForSend.toJSONString();
		}

	}

	@Override
	public String getEvalListForChangeOwner(String jsonString) {

		JSONObject objForSend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			JSONArray dataForAppend = new JSONArray();
			int orga_id = Integer.parseInt(jsonObj.get("orga_id").toString());
			int loca_id = Integer.parseInt(jsonObj.get("loca_id").toString());
			int dept_id = Integer.parseInt(jsonObj.get("dept_id").toString());

			List<Object> evalList = assignTaskDao.getEvaluatorList(orga_id, loca_id, dept_id);
			Iterator<Object> itr = evalList.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("user_id", object[0].toString());
				objForAppend.put("user_name", object[1].toString() + " " + object[2].toString());

				dataForAppend.add(objForAppend);

			}

			objForSend.put("Evaluator", dataForAppend);
			objForSend.put("responseMessage", "Success");
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("responseMessage", "Failed");
			return objForSend.toJSONString();
		}

	}

	@Override
	public String getFunHeadListForChangeOwner(String json) {

		JSONObject objForSend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(json);
			int orga_id = Integer.parseInt(jsonObj.get("orga_id").toString());
			int loca_id = Integer.parseInt(jsonObj.get("loca_id").toString());
			int dept_id = Integer.parseInt(jsonObj.get("dept_id").toString());
			JSONArray dataForAppend = new JSONArray();

			List<Object> funHeadList = assignTaskDao.getFunHeadList(orga_id, loca_id, dept_id);
			Iterator<Object> itr = funHeadList.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("user_id", object[0].toString());
				objForAppend.put("user_name", object[1].toString() + " " + object[2].toString());

				dataForAppend.add(objForAppend);

			}

			objForSend.put("FunctionHead", dataForAppend);
			objForSend.put("responseMessage", "Success");
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("responseMessage", "Failed");
			return objForSend.toJSONString();
		}

	}

	@Override
	public String searchcomplianceownerpage(String jsonString, HttpSession session) {
		JsonNode rootNode = null;
		String response = "";
		final ObjectMapper mapper = new ObjectMapper();
		ObjectNode objectNode = mapper.createObjectNode();
		System.out.println();
		int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
		int user_role_id = Integer.parseInt(session.getAttribute("sess_role_id").toString());
		ArrayNode repoArray = mapper.createArrayNode();
		try {
			List<Object> repo = taskUserMappingDao.getAllMappedTasksForEnablingPage(jsonString);

			System.out.println("repo size : " + repo.size());

			Iterator<Object> iterator = repo.iterator();
			while (iterator.hasNext()) {
				Object[] object = (Object[]) iterator.next();
				ObjectNode repoNode = mapper.createObjectNode();

				repoNode.put("tmap_id", Integer.parseInt(object[8].toString()));
				repoNode.put("tmap_client_tasks_id", object[10].toString());
				repoNode.put("task_legi_name", object[1].toString());
				repoNode.put("task_rule_name", object[2].toString());
				repoNode.put("task_reference", object[3].toString());
				repoNode.put("task_activity_who", object[4].toString());
				repoNode.put("task_activity_when", object[5].toString());
				repoNode.put("task_activity", object[6].toString());
				repoNode.put("task_procedure", object[7].toString());
				repoNode.put("tmap_enable_status", object[9].toString());
				repoNode.put("executor_id", object[11].toString());
				repoNode.put("executor_name", object[12].toString() + " " + object[13].toString());
				repoNode.put("evaluator_id", object[14].toString());
				repoNode.put("orga_id", object[17].toString());
				repoNode.put("loca_id", object[19].toString());
				repoNode.put("dept_id", object[21].toString());
				repoNode.put("task_lexcare_id", object[23].toString());
				repoNode.put("function_head_id", object[26].toString());
				repoNode.put("task_legi_id", object[24].toString());
				repoNode.put("task_rule_id", object[25].toString());

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

	@Override
	public String uploadAssignTaskList(MultipartFile assign_task_list, String jsonString, HttpSession session) {
		String user_name = session.getAttribute("sess_user_full_name").toString();
		int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
		JSONObject objForAppend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			if (!assign_task_list.isEmpty()) {
				JSONArray neglectedTask = new JSONArray();
				JSONArray addedTask = new JSONArray();

				String name = jsonObj.get("name").toString();
				byte[] bytes = assign_task_list.getBytes();
				String fileExtension = FilenameUtils.getExtension(assign_task_list.getOriginalFilename());
				if (fileExtension.equalsIgnoreCase("csv")) {

					File temp = File.createTempFile(assign_task_list.getName(), ".csv");

					int i = 0;
					String absolutePath = temp.getAbsolutePath();
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(absolutePath));
					stream.write(bytes);
					stream.close();
					CsvReader assignUpdates = new CsvReader(absolutePath);
					assignUpdates.readHeaders();

					while (assignUpdates.readRecord()) {
						i++;
						String lexcare_task_id = assignUpdates.get("Task_id");
						List<Object> orga = entityDao.getOrganizationIdByOrgaName(assignUpdates.get("Entity_ID"));
						int orgaId = 0;
						if (orga.size() > 0 || orga != null) {
							Iterator<Object> iterator = orga.iterator();
							while (iterator.hasNext()) {
								Object[] next = (Object[]) iterator.next();
								System.out.println("obj 1 " + next[0]);
								orgaId = Integer.parseInt(next[0].toString());
							}
						}

						List<Object> loca = unitDao.getLocationIdByName(assignUpdates.get("Unit_ID"));
						System.out.println("loca : " + loca.size());
						int unitId = 0;
						if (loca.size() > 0 || loca != null) {
							Iterator<Object> iterator = loca.iterator();
							while (iterator.hasNext()) {
								Object[] next = (Object[]) iterator.next();
								System.out.println("obj 1 " + next[0]);
								unitId = Integer.parseInt(next[0].toString());
							}
						}

						List<Object> dept = functionDao.getDepartmentNameById(assignUpdates.get("Function_ID"));
						int deptId = 0;
						if (dept.size() > 0 || dept != null) {
							Iterator<Object> iterator = dept.iterator();
							while (iterator.hasNext()) {
								Object[] next = (Object[]) iterator.next();
								System.out.println("obj 1 " + next[0]);
								deptId = Integer.parseInt(next[0].toString());
							}
						}

						User pr_user = userDao.getUserIdByName(assignUpdates.get("Executor"));
						int pr_user_id = pr_user.getUser_id();
						User rw_user = userDao.getUserIdByName(assignUpdates.get("Evaluator"));
						int rw_user_id = rw_user.getUser_id();
						User fh_user = userDao.getUserIdByName(assignUpdates.get("Function Head"));
						int fh_user_id = fh_user.getUser_id();

						Task task = assignTaskDao.getTaskIdByLexcareTaskId(lexcare_task_id);

						List<Object> taskList = assignTaskDao.checkIfExist(orgaId, unitId, deptId, lexcare_task_id,
								pr_user_id, rw_user_id, fh_user_id, task.getTask_id());
						int last_generated_id = tasksUserMappingDao.getMaxLastGeneratedValue(unitId, deptId);
						System.out.println("size : " + orga.size());
						if (taskList.size() == 0) {
							List<Object> entityMappingList = tasksUserMappingDao.checkIfMappingExist(orgaId, unitId,
									deptId);
							if (entityMappingList.size() != 0) {
								TaskUserMapping mapping = new TaskUserMapping();
								mapping.setTmap_added_by(user_id);
								mapping.setTmap_approval_status("1");

								last_generated_id++;
								String tmap_last_generated_value_for_client_task_id = String.format("%07d",
										(last_generated_id));
								String tmap_client_id = "D" + String.format("%02d", unitId)
										+ String.format("%02d", deptId) + tmap_last_generated_value_for_client_task_id;

								mapping.setTmap_client_tasks_id(tmap_client_id);
								mapping.setTmap_created_at(new Date());
								mapping.setTmap_dept_id(deptId);
								mapping.setTmap_enable_status("1");
								mapping.setTmap_fh_user_id(fh_user_id);
								mapping.setTmap_last_generated_value_for_client_task_id(
										Integer.parseInt(tmap_last_generated_value_for_client_task_id));
								mapping.setTmap_lexcare_task_id(lexcare_task_id);
								mapping.setTmap_loca_id(unitId);
								mapping.setTmap_orga_id(orgaId);
								mapping.setTmap_pr_user_id(pr_user_id);
								mapping.setTmap_rw_user_id(rw_user_id);
								mapping.setTmap_task_id(task.getTask_id());

								tasksUserMappingDao.persist(mapping);
							}
						}
					}
					objForAppend.put("responseMessage", "Success");
					return objForAppend.toJSONString();
				} else {

					objForAppend.put("responseMessage", "File type mismatch");
					return objForAppend.toJSONString();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			objForAppend.put("responseMessage", "Failed");
			return objForAppend.toJSONString();
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	@Override
	public String getExeListForActivationPage(String jsonString) {
		JSONObject objForSend = new JSONObject();
		try {
			JSONArray dataForAppend = new JSONArray();
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int orga_id = Integer.parseInt(jsonObj.get("orga_id").toString());
			int loca_id = Integer.parseInt(jsonObj.get("loca_id").toString());
			int dept_id = Integer.parseInt(jsonObj.get("dept_id").toString());

			List<Object> exeList = assignTaskDao.getExeListForActivationPage(orga_id, loca_id, dept_id);
			Iterator<Object> itr = exeList.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("user_id", object[27].toString());
				objForAppend.put("user_name", object[16].toString() + " " + object[17].toString());
				dataForAppend.add(objForAppend);

			}

			objForSend.put("Executor", dataForAppend);
			objForSend.put("responseMessage", "Success");
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("responseMessage", "Failed");
			return objForSend.toJSONString();
		}
	}

	@Override
	public String getEvalListForActivationPageURL(String jsonString) {
		JSONObject objForSend = new JSONObject();
		try {
			JSONArray dataForAppend = new JSONArray();
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int orga_id = Integer.parseInt(jsonObj.get("orga_id").toString());
			int loca_id = Integer.parseInt(jsonObj.get("loca_id").toString());
			int dept_id = Integer.parseInt(jsonObj.get("dept_id").toString());

			List<Object> exeList = assignTaskDao.getEvalListForActivationPage(orga_id, loca_id, dept_id);
			Iterator<Object> itr = exeList.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("user_id", object[28].toString());
				objForAppend.put("user_name", object[25].toString() + " " + object[26].toString());

				dataForAppend.add(objForAppend);

			}

			objForSend.put("Evaluator", dataForAppend);
			objForSend.put("responseMessage", "Success");
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("responseMessage", "Failed");
			return objForSend.toJSONString();
		}
	}

	@Override
	public String searchActivationPage(String jsonString, HttpSession session) {
		JSONArray dataForAppend = new JSONArray();
		JSONObject dataForSend = new JSONObject();
		try {
			List<Object> taskList = taskConfigurationDao.getAllConfiguredTaskForActivationPage(jsonString);

			Iterator<Object> itr = taskList.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				/*-----------------------------------Code for adding tasks list of array---------------------------*/
				JSONObject objForAppend = new JSONObject();

				objForAppend.put("ttrn_id", object[10]);
				objForAppend.put("tmap_client_tasks_id", object[8]);
				objForAppend.put("task_legi_name", object[1]);
				objForAppend.put("task_rule_name", object[2]);
				objForAppend.put("task_reference", object[3]);
				objForAppend.put("task_activity_who", object[4]);
				objForAppend.put("task_activity_when", object[5]);
				objForAppend.put("task_activity", object[6]);
				objForAppend.put("task_procedure", object[7]);
				objForAppend.put("ttrn_frequency_for_operation", object[12]);
				objForAppend.put("executor_name", object[16].toString() + " " + object[17].toString());
				objForAppend.put("ttrn_status", object[18]);
				objForAppend.put("orga_id", object[19]);
				objForAppend.put("loca_id", object[21]);
				objForAppend.put("dept_id", object[23]);
				objForAppend.put("exec_id", object[27]);
				objForAppend.put("eval_id", object[28]);
				objForAppend.put("ttrn_pr_due_date", sdfOut.format(sdfIn.parse(object[14].toString())));
				objForAppend.put("ttrn_rw_due_date", sdfOut.format(sdfIn.parse(object[29].toString())));
				objForAppend.put("ttrn_fh_due_date", sdfOut.format(sdfIn.parse(object[30].toString())));
				objForAppend.put("ttrn_uh_due_date", sdfOut.format(sdfIn.parse(object[31].toString())));
				objForAppend.put("ttrn_legal_due_date", sdfOut.format(sdfIn.parse(object[13].toString())));
				objForAppend.put("ttrn_frequency_for_alerts", object[32]);
				objForAppend.put("ttrn_impact", object[33]);
				objForAppend.put("ttrn_impact_on_organization", object[34]);
				objForAppend.put("ttrn_impact_on_unit", object[35]);
				objForAppend.put("ttrn_document", object[36]);
				objForAppend.put("ttrn_historical", object[37]);
				objForAppend.put("ttrn_prior_days_buffer", object[38]);
				objForAppend.put("ttrn_alert_days", object[39]);
				objForAppend.put("ttrn_task_approved_by", object[46]);
				objForAppend.put("task_lexcare_id", object[48]);

				if (object[47] != null)
					objForAppend.put("ttrn_task_approved_date", sdfOut.format(sdfIn.parse(object[47].toString())));
				else
					objForAppend.put("ttrn_task_approved_date", "");

				if (object[40] != null)
					objForAppend.put("ttrn_first_alert", sdfOut.format(sdfIn.parse(object[40].toString())));
				else
					objForAppend.put("ttrn_first_alert", "");

				if (object[41] != null)
					objForAppend.put("ttrn_second_alert", sdfOut.format(sdfIn.parse(object[41].toString())));
				else
					objForAppend.put("ttrn_second_alert", "");

				if (object[42] != null)
					objForAppend.put("ttrn_third_alert", sdfOut.format(sdfIn.parse(object[42].toString())));
				else
					objForAppend.put("ttrn_third_alert", "");

				objForAppend.put("ttrn_no_of_back_days_allowed", object[43]);
				objForAppend.put("ttrn_allow_approver_reopening", object[44]);
				objForAppend.put("ttrn_allow_back_date_completion", object[45]);

				dataForAppend.add(objForAppend);

			}
			dataForSend.put("All_Tasks", dataForAppend);
			dataForSend.put("responseMessage", "Success");
			return dataForSend.toString();
		} catch (Exception e) {
			e.printStackTrace();
			dataForSend.put("responseMessage", "Failed");
			return dataForSend.toString();

		}
	}

	@Override
	public String searchEnableDisablePage(String jsonString, HttpSession session) {
		JSONArray dataForAppend = new JSONArray();
		JSONObject dataForSend = new JSONObject();
		try {
			List<Object> taskList = assignTaskDao.searchEnableDisablePage(jsonString);

			Iterator<Object> itr = taskList.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				/*-----------------------------------Code for adding tasks list of array---------------------------*/
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

			}
			dataForSend.put("All_Tasks", dataForAppend);
			dataForSend.put("responseMessage", "Success");
			return dataForSend.toString();
		} catch (Exception e) {
			e.printStackTrace();
			dataForSend.put("responseMessage", "Failed");
			return dataForSend.toString();

		}
	}

}
