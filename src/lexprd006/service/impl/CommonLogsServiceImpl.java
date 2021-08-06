package lexprd006.service.impl;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lexprd006.dao.CommonLogsDao;
import lexprd006.service.CommonLogsService;

@Service
public class CommonLogsServiceImpl implements CommonLogsService {

	@Autowired
	CommonLogsDao commonLogsDao;

	@SuppressWarnings("unchecked")
	@Override
	public String getLoginLogs(HttpSession session) {
		JSONArray dataForSend = new JSONArray();
		List<Object> listData = commonLogsDao.getLoginLogsData(session);
		try {
			Iterator<Object> itr = listData.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("user_nam", object[0].toString() + " " + object[1].toString());
				objForAppend.put("USER_ID", object[2].toString());
				objForAppend.put("isOnline", object[3].toString());
				objForAppend.put("logOutTime", object[4] != null ? object[4].toString() : "-");
				objForAppend.put("loginTime", object[5].toString());
				objForAppend.put("user_email", object[6].toString());
				objForAppend.put("TimeDiff", object[7] != null ? object[7].toString() : "-");
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
	@Override
	public String getAssignLogs(HttpSession session) {
		JSONArray dataForSend = new JSONArray();
		List<Object> listData = commonLogsDao.getAssignLogsData(session);
		try {
			Iterator<Object> itr = listData.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("entity", object[0].toString());
				objForAppend.put("deptName", object[1].toString());
				objForAppend.put("locaName", object[2].toString());
				objForAppend.put("executorName", object[3].toString());
				objForAppend.put("evaluatorName", object[4].toString());
				objForAppend.put("functionHead", object[5].toString());
				objForAppend.put("tasksId", object[6].toString());
				objForAppend.put("lexTasksId", object[7].toString());
				objForAppend.put("createdTime", object[8].toString());
				objForAppend.put("addedBy", object[9].toString());
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
	@Override
	public String changeComplianceOwnerLogs(HttpSession session) {

		JSONArray dataForSend = new JSONArray();
		List<Object> listData = commonLogsDao.changeComplianceOwnerLogsData(session);
		try {
			Iterator<Object> itr = listData.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("entity", object[0].toString());
				objForAppend.put("deptName", object[1].toString());
				objForAppend.put("locaName", object[2].toString());
				objForAppend.put("executorName", object[3].toString());
				objForAppend.put("evaluatorNam", object[4].toString());
				objForAppend.put("functionHead", object[5].toString());
				objForAppend.put("tasksId", object[6].toString());
				objForAppend.put("lexcareTasksId", object[7].toString());
				objForAppend.put("createdTime", object[8].toString());
				objForAppend.put("addedBy", object[9].toString());
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
	@Override
	public String tasksConfigLogs(HttpSession session) {

		JSONArray dataForSend = new JSONArray();
		List<Object> listData = commonLogsDao.tasksConfigLogsData(session);
		try {
			Iterator<Object> itr = listData.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("createdTime", object[0].toString());
				objForAppend.put("bufferDays", object[1].toString());
				objForAppend.put("evaluatorDueDate", object[2].toString());
				objForAppend.put("executorDueDate", object[3].toString());
				objForAppend.put("frequency", object[4] != null ? object[4].toString() : "NA");
				objForAppend.put("functionHeadDueDate", object[5].toString());
				objForAppend.put("legalDueDate", object[6].toString());
				objForAppend.put("lexTasksId", object[7].toString());
				objForAppend.put("priorDays", object[8].toString());
				objForAppend.put("tasksId", object[9].toString());
				objForAppend.put("unitHeadDueDate", object[10].toString());
				objForAppend.put("addedBy", object[11].toString());
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
	@Override
	public String complianceReportLogs(HttpSession session) {

		JSONArray dataForSend = new JSONArray();
		List<Object> listData = commonLogsDao.complianceReportLogsData(session);
		try {
			Iterator<Object> itr = listData.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("fromDate", object[0].toString());
				objForAppend.put("toDate", object[1].toString());
				objForAppend.put("legalStatus", object[2].toString());
				objForAppend.put("tasksImpact", object[3].toString());
				objForAppend.put("userId", object[4].toString());
				objForAppend.put("orgaName", object[5].toString());
				objForAppend.put("locaName", object[6].toString());
				objForAppend.put("addedBy", object[7].toString());
				objForAppend.put("createdTime", object[8].toString());
				objForAppend.put("functionName", object[9].toString());
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
	@Override
	public String emailLogs(HttpSession session) {

		JSONArray dataForSend = new JSONArray();
		List<Object> listData = commonLogsDao.emailLogsData(session);
		try {
			Iterator<Object> itr = listData.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("emailStatus", object[0].toString());
				objForAppend.put("tasksId", object[1].toString());
				objForAppend.put("mailSentTo", object[2].toString());
				objForAppend.put("time", object[3].toString());

				/*if (object[4] != null) {
					objForAppend.put("createdBy", object[4].toString());
				}*/

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
	@Override
	public String activateDeActivateLogs(HttpSession session) {
		JSONArray dataForSend = new JSONArray();
		List<Object> listData = commonLogsDao.activateDeActivateLogsData(session);
		try {
			Iterator<Object> itr = listData.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("ttrnClientTasksId", object[0].toString());
				objForAppend.put("ttrnId", object[1].toString());
				objForAppend.put("tasksStatus", object[2].toString());
				objForAppend.put("timeLine", object[3].toString());
				objForAppend.put("addedBy", object[4].toString());
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
	@Override
	public String reactivateTasks(HttpSession session) {

		JSONArray dataForSend = new JSONArray();
		List<Object> listData = commonLogsDao.getReactivateTasksData(session);
		try {
			Iterator<Object> itr = listData.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("frequency", object[0].toString());
				objForAppend.put("createdTime", object[1].toString());
				objForAppend.put("status", object[2].toString());
				objForAppend.put("tasksId", object[3].toString());
				objForAppend.put("addedBy", object[4].toString());
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
	public String getqueryBuilder(String jsonString, HttpSession session) {
		JSONObject objForAppend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String tasksIds = jsonObj.get("clientTasksId").toString();
			// System.out.println("tasksIds : " + tasksIds);

			String newString = "";
			String client_task_ids = tasksIds;
			System.out.println(client_task_ids);
			String[] split = client_task_ids.split(",");
			System.out.println("split : " + split);

			// System.out.println(newString);

			for (int i = 0; i < split.length; i++) {
				if (i == split.length - 1) {
					newString += "'" + split[i] + "'";
					System.out.println("if newString : " + newString.toString());
				} else {
					newString += "'" + split[i] + "',";
					System.out.println("else newString : " + newString.toString());
				}
			}
			System.out.println("Normal for loop: " + newString);

			commonLogsDao.tasksDeletionQuery(newString);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String queryDeActivation(String jsonString, HttpSession session) {
		JSONObject objForAppend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String tasksIds = jsonObj.get("tasksId").toString();
			// System.out.println("tasksIds : " + tasksIds);

			String newString = "";
			String client_task_ids = tasksIds;
			System.out.println(client_task_ids);
			String[] split = client_task_ids.split(",");
			System.out.println("split : " + split);

			// System.out.println(newString);

			for (int i = 0; i < split.length; i++) {
				if (i == split.length - 1) {
					newString += "'" + split[i] + "'";
					System.out.println("if newString : " + newString.toString());
				} else {
					newString += "'" + split[i] + "',";
					System.out.println("else newString : " + newString.toString());
				}
			}
			System.out.println("Normal for loop: " + newString);

			commonLogsDao.queryDeActivation(newString);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String queryDisableTasks(String jsonString, HttpSession session) {
		JSONObject objForAppend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String tasksIds = jsonObj.get("cTasksId").toString();
			// System.out.println("tasksIds : " + tasksIds);

			String newString = "";
			String client_task_ids = tasksIds;
			System.out.println(client_task_ids);
			String[] split = client_task_ids.split(",");
			System.out.println("split : " + split);

			// System.out.println(newString);

			for (int i = 0; i < split.length; i++) {
				if (i == split.length - 1) {
					newString += "'" + split[i] + "'";
					System.out.println("if newString : " + newString.toString());
				} else {
					newString += "'" + split[i] + "',";
					System.out.println("else newString : " + newString.toString());
				}
			}
			System.out.println("Normal for loop: " + newString);

			commonLogsDao.queryDisableTasks(newString);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
