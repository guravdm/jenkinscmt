package lexprd006.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lexprd006.dao.NewDashboardDAO;
import lexprd006.dao.TasksConfigurationDao;
import lexprd006.dao.UploadedDocumentsDao;
import lexprd006.domain.Department;
import lexprd006.domain.Location;
import lexprd006.domain.Organization;
import lexprd006.domain.TaskTransactional;
import lexprd006.domain.UploadedDocuments;
import lexprd006.service.NewDashboardService;

@Service
public class NewDashboardServiceImpl implements NewDashboardService {

	@Autowired
	NewDashboardDAO newDashboardDAO;

	@Autowired
	UploadedDocumentsDao uploadedDocumentsDao;

	@Autowired
	TasksConfigurationDao tasksConfigurationDao;

	@SuppressWarnings("unchecked")
	@Override
	public String getOverAllCount(String jsonString, HttpSession session) {
		try {

			JSONObject objForSend = new JSONObject();
			JSONArray tasksList = new JSONArray();
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);

			String fromDate = jsonObj.get("fromDate").toString() != null ? jsonObj.get("fromDate").toString() : "0";
			String toDate = jsonObj.get("toDate").toString() != null ? jsonObj.get("toDate").toString() : "0";

			System.out.println("fromDate : " + fromDate);
			System.out.println("toDate : " + toDate);

			List<Object> complianceStatusData = newDashboardDAO.getComplianceStatusData(jsonString, session, fromDate,
					toDate);
			System.out.println("complianceStatusData : " + complianceStatusData.size());
			if (complianceStatusData.size() > 0 && complianceStatusData != null) {
				Iterator<Object> itr = complianceStatusData.iterator();
				objForSend.put("totalTasks", complianceStatusData.size());
				while (itr.hasNext()) {
					JSONObject taskObj = new JSONObject();
					Object[] next = (Object[]) itr.next();
					taskObj.put("NonComplied", next[0].toString());
					taskObj.put("PosingRisk", next[1].toString());
					taskObj.put("Complied", next[2].toString());
					taskObj.put("Delayed", next[3].toString());
					taskObj.put("WaitingForApproval", next[4].toString());
					taskObj.put("ReOpened", next[5].toString());
					taskObj.put("DelayedReported", next[6].toString());
					tasksList.add(taskObj);
				}
			}
			objForSend.put("taskList", tasksList);
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonString;
	}

	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	@Override
	public String getEntityRisksCount(String jsonString, HttpSession session) {
		try {

			System.currentTimeMillis();
			JSONObject objForSend = new JSONObject();
			JSONArray tasksList = new JSONArray();
			JSONArray entityList = new JSONArray();

			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String fromDate = jsonObj.get("fromDate").toString() != null ? jsonObj.get("fromDate").toString() : "0";
			String toDate = jsonObj.get("toDate").toString() != null ? jsonObj.get("toDate").toString() : "0";

			Enumeration keys = session.getAttributeNames();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				System.out.println(key + ": " + session.getValue(key) + "<br>");
			}
			try {
				List<Organization> allOrganization = newDashboardDAO.getAllOrganization(session);
				Iterator<Organization> org = allOrganization.iterator();
				while (org.hasNext()) {
					JSONObject entObj = new JSONObject();
					Organization organization = org.next();
					String EntityName = organization.getOrga_name();
					entObj.put("entityId", organization.getOrga_id());
					entObj.put("EntityName", EntityName);
					entityList.add(entObj);

					List<Object> EntData = newDashboardDAO.getEntityChartRiskCount(organization.getOrga_id(),
							Integer.parseInt(session.getAttribute("sess_user_id").toString()), session, fromDate,
							toDate);
					Iterator<Object> itr = EntData.iterator();
					while (itr.hasNext()) {
						Object[] next = (Object[]) itr.next();
						if (next[0] == null && next[2] == null && next[1] == null && next[3] == null && next[4] == null
								&& next[5] == null) {
							JSONObject taskObj = new JSONObject();
							taskObj.put("EntityName", EntityName);
							taskObj.put("NonComplied", 0);
							taskObj.put("PosingRisk", 0);
							taskObj.put("Complied", 0);
							taskObj.put("Delayed", 0);
							taskObj.put("WaitingForApproval", 0);
							taskObj.put("ReOpened", 0);
							taskObj.put("DelayedReported", 0);
							tasksList.add(taskObj);
						} else {
							JSONObject taskObj = new JSONObject();
							taskObj.put("EntityName", EntityName);
							taskObj.put("NonComplied", next[0].toString());
							taskObj.put("PosingRisk", next[1].toString());
							taskObj.put("Complied", next[2].toString());
							taskObj.put("Delayed", next[3].toString());
							taskObj.put("WaitingForApproval", next[4].toString());
							taskObj.put("ReOpened", next[5].toString());
							taskObj.put("DelayedReported", next[6].toString());
							tasksList.add(taskObj);
						}
					}
				}
				objForSend.put("taskList", tasksList);
				objForSend.put("entityList", entityList);
				return objForSend.toJSONString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonString;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getUnitRisksCount(String jsonString, HttpSession session) {
		try {
			JSONObject objForSend = new JSONObject();
			JSONArray tasksList = new JSONArray();
			JSONArray locaList = new JSONArray();

			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);

			String fromDate = jsonObj.get("fromDate").toString() != null ? jsonObj.get("fromDate").toString() : "0";
			String toDate = jsonObj.get("toDate").toString() != null ? jsonObj.get("toDate").toString() : "0";

			System.out.println("fromDate : " + fromDate);
			System.out.println("toDate : " + toDate);

			List<Organization> allOrganization = newDashboardDAO.getAllOrganization(session);
//			if (allOrganization.size() > 0) {
//				for (Organization org : allOrganization) {
//					System.out.println("org : " + org.getOrga_id());

			Map<String, Object> totalLocationConut = new HashMap<String, Object>();
			JSONObject json = new JSONObject();
			String locationName = "";
			List<Location> allLocations = newDashboardDAO.getAllLocations(session);
			Iterator<Location> loc = allLocations.iterator();
			while (loc.hasNext()) {
				// Initialize value for count
				int NonComp = 0;
				int Compl = 0;
				int PosingRisk = 0;
				int delay = 0;
				int WaitingApproval = 0;
				int ReOpened = 0;
				int delayedReported = 0;

				ArrayList<Integer> locationCount = new ArrayList<>();
				Location location = loc.next();
				locationName = location.getLoca_name();

				JSONObject locaObj = new JSONObject();
				locaObj.put("locaId", location.getLoca_id());
				locaObj.put("locaName", locationName);
				locaList.add(locaObj);
				int orgaId = 0;
				List<Object> LocData = newDashboardDAO.getLocationChartRiskCount(location.getLoca_id(),
						Integer.parseInt(session.getAttribute("sess_user_id").toString()), session, orgaId, fromDate,
						toDate);
				Iterator<Object> itr = LocData.iterator();

				while (itr.hasNext()) {
					Object[] object = (Object[]) itr.next();
					if (object[0] == null && object[2] == null && object[1] == null && object[3] == null
							&& object[4] == null && object[5] == null) {

						JSONObject taskObj = new JSONObject();
						taskObj.put("locaName", locationName);
						taskObj.put("NonComplied", 0);
						taskObj.put("PosingRisk", 0);
						taskObj.put("Complied", 0);
						taskObj.put("Delayed", 0);
						taskObj.put("WaitingForApproval", 0);
						taskObj.put("ReOpened", 0);
						taskObj.put("DelayedReported", 0);

						NonComp += 0;
						Compl += 0;
						PosingRisk += 0;
						delay += 0;
						WaitingApproval += 0;
						ReOpened += 0;
						delayedReported += 0;
						tasksList.add(taskObj);
					} else {
						NonComp += Integer.parseInt(object[0].toString());
						Compl += Integer.parseInt(object[2].toString());
						PosingRisk += Integer.parseInt(object[1].toString());
						delay += Integer.parseInt(object[3].toString());
						WaitingApproval += Integer.parseInt(object[4].toString());
						ReOpened += Integer.parseInt(object[5].toString());
						delayedReported += Integer.parseInt(object[6].toString());

						JSONObject taskObj = new JSONObject();
						taskObj.put("locaName", locationName);
						taskObj.put("NonComplied", object[0].toString());
						taskObj.put("PosingRisk", object[1].toString());
						taskObj.put("Complied", object[2].toString());
						taskObj.put("Delayed", object[3].toString());
						taskObj.put("WaitingForApproval", object[4].toString());
						taskObj.put("ReOpened", object[5].toString());
						taskObj.put("DelayedReported", object[6].toString());
						tasksList.add(taskObj);
					}
				}
				locationCount.add(Compl);
				locationCount.add(PosingRisk);
				locationCount.add(NonComp);
				locationCount.add(delay);
				locationCount.add(WaitingApproval);
				locationCount.add(ReOpened);
				locationCount.add(delayedReported);
				totalLocationConut.put(locationName, locationCount);
			}
//				}
//			}
			objForSend.put("taskList", tasksList);
			objForSend.put("locaList", locaList);
			return objForSend.toJSONString();

			// Create Map Collection
			// Map data = new HashMap<>();
			// data.put("locData", totalLocationConut);
			// json.putAll(data);
			// return json;
			// End Location Graph

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getFunctionRisksCount(String jsonString, HttpSession session) {
		try {
			Integer orgaId = 0;
			System.currentTimeMillis();
			JSONObject objForSend = new JSONObject();
			JSONArray tasksList = new JSONArray();
			JSONArray deptList = new JSONArray();
			JSONArray orgaList = new JSONArray();
			JSONArray unitList = new JSONArray();
			Map<String, Object> totalDepartmentConut = new HashMap<String, Object>();

			Integer userSesId = Integer.parseInt(session.getAttribute("sess_user_id").toString());

			// List<Object> allUnitsByAccess =
			// newDashboardDAO.getAllUnitsByAccess(userSesId, session);
			// if (allUnitsByAccess.size() > 0) {
			// Iterator<Object> unitItr = allUnitsByAccess.iterator();
			// while (unitItr.hasNext()) {
			// Object[] loc = (Object[]) unitItr.next();
			// JSONObject unitObj = new JSONObject();
			// unitObj.put("unitId", loc[0].toString());
			// unitObj.put("unitName", loc[1].toString());
			// unitList.add(unitObj);
			// }
			// }

			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);

			String fromDate = jsonObj.get("fromDate").toString() != null ? jsonObj.get("fromDate").toString() : "0";
			String toDate = jsonObj.get("toDate").toString() != null ? jsonObj.get("toDate").toString() : "0";

			List<Organization> allOrganization = newDashboardDAO.getAllOrganization(session);
			if (allOrganization.size() > 0) {
				Iterator<Organization> itrOrg = allOrganization.iterator();
				while (itrOrg.hasNext()) {
					Organization orgObj = itrOrg.next();
					orgaId = orgObj.getOrga_id();

					JSONObject orgaObj = new JSONObject();
					orgaObj.put("orgaId", orgObj.getOrga_id());
					orgaObj.put("orgaName", orgObj.getOrga_name());
					orgaList.add(orgaObj);
				}
			}
			List<Department> allDepartments = newDashboardDAO.getAllDepartments(session);
			Iterator<Department> dept = allDepartments.iterator();
			while (dept.hasNext()) {

				// Initialize value for count
				int NonComp = 0;
				int Compl = 0;
				int PosingRisk = 0;
				int delay = 0;
				int WaitingApproval = 0;
				int ReOpened = 0;

				ArrayList<Integer> departmentCount = new ArrayList<>();

				Department department = dept.next();
				String depatmentName = department.getDept_name();

				JSONObject deptObj = new JSONObject();
				deptObj.put("deptId", department.getDept_id());
				deptObj.put("deptName", depatmentName);
				deptList.add(deptObj);

				List<Object> DeptData = newDashboardDAO.getDepartmentChartRiskCount(department.getDept_id(), orgaId,
						Integer.parseInt(session.getAttribute("sess_user_id").toString()), session, fromDate, toDate);
				Iterator<Object> itr = DeptData.iterator();

				while (itr.hasNext()) {
					Object[] object = (Object[]) itr.next();
					if (object[0] == null && object[2] == null && object[1] == null && object[3] == null
							&& object[4] == null && object[5] == null) {
						NonComp += 0;
						Compl += 0;
						PosingRisk += 0;
						delay += 0;
						WaitingApproval += 0;
						ReOpened += 0;
						JSONObject taskObj = new JSONObject();
						taskObj.put("deptName", depatmentName);
						taskObj.put("NonComplied", 0);
						taskObj.put("PosingRisk", 0);
						taskObj.put("Complied", 0);
						taskObj.put("Delayed", 0);
						taskObj.put("WaitingForApproval", 0);
						taskObj.put("ReOpened", 0);
						taskObj.put("DelayedReported", 0);
						tasksList.add(taskObj);
					} else {
						JSONObject taskObj = new JSONObject();
						taskObj.put("deptName", depatmentName);
						taskObj.put("NonComplied", object[0].toString());
						taskObj.put("PosingRisk", object[1].toString());
						taskObj.put("Complied", object[2].toString());
						taskObj.put("Delayed", object[3].toString());
						taskObj.put("WaitingForApproval", object[4].toString());
						taskObj.put("ReOpened", object[5].toString());
						taskObj.put("DelayedReported", object[6].toString());
						tasksList.add(taskObj);
						NonComp += Integer.parseInt(object[0].toString());
						Compl += Integer.parseInt(object[2].toString());
						PosingRisk += Integer.parseInt(object[1].toString());
						delay += Integer.parseInt(object[3].toString());
						WaitingApproval += Integer.parseInt(object[4].toString());
						ReOpened += Integer.parseInt(object[5].toString());
					}
				}
			}

			objForSend.put("taskList", tasksList);
			System.out.println("tasksList : " + tasksList.toString());
			objForSend.put("orgaList", orgaList);
			// objForSend.put("unitList", unitList);
			objForSend.put("deptList", deptList);

			// End Location Graph
			// Create JSON Object
			JSONObject json = new JSONObject();
			// Create Map Collection
			HashMap<Object, Object> data = new HashMap<>();
			data.put("deptData", totalDepartmentConut);
			json.putAll(data);

			return objForSend.toJSONString();
			// End Department Graph

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getFinancialRisksCount(String jsonString, HttpSession session) {

		try {
			System.currentTimeMillis();
			JSONObject objForSend = new JSONObject();
			JSONArray tasksList = new JSONArray();
			JSONArray deptList = new JSONArray();
			Map<String, Object> totalDepartmentConut = new HashMap<String, Object>();
			List<Department> allDepartments = newDashboardDAO.getAllDepartments(session);
			Iterator<Department> dept = allDepartments.iterator();
			while (dept.hasNext()) {

				// Initialize value for count
				int NonComp = 0;
				int Compl = 0;
				int PosingRisk = 0;
				int delay = 0;
				int WaitingApproval = 0;
				int ReOpened = 0;

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getoveralldrilleddata(String jsonString, HttpSession session) {

		JSONObject objForSend = new JSONObject();
		JSONArray tasksList = new JSONArray();

		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String chartName = jsonObj.get("chart_name").toString();

			String fromDate = jsonObj.get("fromDate").toString() != null ? jsonObj.get("fromDate").toString() : "0";
			String toDate = jsonObj.get("toDate").toString() != null ? jsonObj.get("toDate").toString() : "0";

			String status = jsonObj.get("status").toString();
			String entity = jsonObj.get("entity").toString();
			String unit = jsonObj.get("unit").toString();
			String department = jsonObj.get("department").toString();

			List<Object> overallDrilledData = newDashboardDAO.getOverallDrilledData(status, entity, session, fromDate,
					toDate);
			if (overallDrilledData.size() > 0) {
				Iterator<Object> iterator = overallDrilledData.iterator();
				while (iterator.hasNext()) {
					Object[] next = (Object[]) iterator.next();
					JSONObject obj = new JSONObject();
					obj.put("ttrn_id", next[0].toString());
					obj.put("ttrn_client_task_id", next[1].toString());
					obj.put("ttrn_status", next[2].toString());
					obj.put("ttrn_pr_due_date", next[3].toString());
					obj.put("ttrn_rw_due_date", next[4].toString());
					obj.put("ttrn_fh_due_date", next[5].toString());
					obj.put("ttrn_uh_due_date", next[6].toString());
					obj.put("ttrn_legal_due_date", next[7].toString());
					obj.put("ttrn_submitted_date", next[8].toString());
					obj.put("orga_id", next[9].toString());
					obj.put("orga_name", next[10].toString());
					obj.put("loca_id", next[11].toString());
					obj.put("loca_name", next[12].toString());
					obj.put("dept_id", next[13].toString());
					obj.put("dept_name", next[14].toString());
					obj.put("task_legi_name", next[15].toString());
					obj.put("task_rule_name", next[16].toString());
					obj.put("task_reference", next[17].toString());
					obj.put("task_who", next[18].toString());
					obj.put("task_when", next[19].toString());
					obj.put("task_activity", next[20].toString());
					obj.put("task_procedure", next[21].toString());
					obj.put("tsk_impact", next[22].toString());
					obj.put("ttrn_frequency_for_operation", next[23].toString());
					obj.put("user_id", next[24].toString());
					obj.put("user_first_name", next[25].toString());
					obj.put("user_last_name", next[26].toString());
					obj.put("task_executor", next[25].toString() + " " + next[26].toString());
					if(next[27] != null) {
						obj.put("ttrn_completed_date", next[27].toString());
					}
					obj.put("task_cat_law_name", next[28].toString());
					if (next[29] == null) {
						obj.put("ttrn_performer_comments", "");
					} else {
						obj.put("ttrn_performer_comments", next[29].toString());
					}

					if (next[0] != null) {
						List<UploadedDocuments> attachedDocuments = uploadedDocumentsDao
								.getAllDocumentByTtrnId(Integer.parseInt(next[0].toString()));

						if (attachedDocuments != null) {
							obj.put("document_attached", 1);

							Iterator<UploadedDocuments> itre = attachedDocuments.iterator();
							JSONArray docArray = new JSONArray();

							while (itre.hasNext()) {
								UploadedDocuments uploadedDocuments = (UploadedDocuments) itre.next();
								JSONObject docObj = new JSONObject();
								docObj.put("udoc_id", uploadedDocuments.getUdoc_id());
								docObj.put("udoc_original_file_name", uploadedDocuments.getUdoc_original_file_name());
								docArray.add(docObj);
							}
							obj.put("document_list", docArray);
						} else {
							obj.put("document_list", new JSONArray());
							obj.put("document_attached", 0);
						}

					} else {
						obj.put("document_list", new JSONArray());
						obj.put("document_attached", 0);
					}

					obj.put("task_type", "Main");

					tasksList.add(obj);
				}
			}
			objForSend.put("reportList", tasksList);
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @author DnyaneshG
	 * @Date 11-12-2019
	 * @method return the entity drilled report data on modal
	 */

	@SuppressWarnings("unchecked")
	@Override
	public String getEntityRisksDrilledData(String jsonString, HttpSession session) {

		JSONObject objForSend = new JSONObject();
		JSONArray tasksList = new JSONArray();

		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String chartName = jsonObj.get("chart_name").toString();
			String status = jsonObj.get("status").toString();
			String entity = jsonObj.get("entity").toString();
			String unit = jsonObj.get("unit").toString();
			String department = jsonObj.get("department").toString();

			String fromDate = jsonObj.get("fromDate").toString() != null ? jsonObj.get("fromDate").toString() : "0";
			String toDate = jsonObj.get("toDate").toString() != null ? jsonObj.get("toDate").toString() : "0";

			List<Object> entityDrilledData = newDashboardDAO.getEntityDrilledData(status, entity, session, fromDate,
					toDate);
			System.out.println("entityDrilledData : " + entityDrilledData.size());
			if (entityDrilledData.size() > 0) {
				Iterator<Object> iterator = entityDrilledData.iterator();
				while (iterator.hasNext()) {
					Object[] next = (Object[]) iterator.next();
					JSONObject obj = new JSONObject();
					obj.put("ttrn_id", next[0].toString());
					obj.put("ttrn_client_task_id", next[1].toString());
					obj.put("ttrn_status", next[2].toString());
					obj.put("ttrn_pr_due_date", next[3].toString());
					obj.put("ttrn_rw_due_date", next[4].toString());
					obj.put("ttrn_fh_due_date", next[5].toString());
					obj.put("ttrn_uh_due_date", next[6].toString());
					obj.put("ttrn_legal_due_date", next[7].toString());
					obj.put("ttrn_submitted_date", next[8].toString());
					obj.put("orga_id", next[9].toString());
					obj.put("orga_name", next[10].toString());
					obj.put("loca_id", next[11].toString());
					obj.put("loca_name", next[12].toString());
					obj.put("dept_id", next[13].toString());
					obj.put("dept_name", next[14].toString());
					obj.put("task_legi_name", next[15].toString());
					obj.put("task_rule_name", next[16].toString());
					obj.put("task_reference", next[17].toString());
					obj.put("task_who", next[18].toString());
					obj.put("task_when", next[19].toString());
					obj.put("task_activity", next[20].toString());
					obj.put("task_procedure", next[21].toString());
					obj.put("tsk_impact", next[22].toString());
					obj.put("ttrn_frequency_for_operation", next[23].toString());
					obj.put("user_id", next[24].toString());
					obj.put("user_first_name", next[25].toString());
					obj.put("user_last_name", next[26].toString());
					obj.put("task_executor", next[25].toString() + " " + next[26].toString());
					if (next[27] == null) {
						obj.put("ttrn_completed_date", "");
					} else {
						obj.put("ttrn_completed_date", next[27].toString());
					}

					if (next[28] == null) {
						obj.put("task_cat_law_name", "");
					} else {
						obj.put("task_cat_law_name", next[28].toString());
					}

					if (next[29] == null) {
						obj.put("ttrn_performer_comments", "");
					} else {
						obj.put("ttrn_performer_comments", next[29].toString());
					}
					tasksList.add(obj);
				}
			}
			objForSend.put("reportList", tasksList);
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getUnitRisksModalData(String jsonString, HttpSession session) {

		JSONObject objForSend = new JSONObject();
		JSONArray tasksList = new JSONArray();

		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String chartName = jsonObj.get("chart_name").toString();
			String status = jsonObj.get("status").toString();
			String entity = jsonObj.get("entity").toString();
			String unit = jsonObj.get("unit").toString();
			System.out.println("unit : " + unit);
			String department = jsonObj.get("department").toString();
			System.out.println("department : " + department);
			Integer locaId = 0;

			String fromDate = jsonObj.get("fromDate").toString() != "" ? jsonObj.get("fromDate").toString() : "0";
			String toDate = jsonObj.get("toDate").toString() != "" ? jsonObj.get("toDate").toString() : "0";

			List<Object> locIdByUnitName = newDashboardDAO.getLocIdByUnitName(unit);
			if (locIdByUnitName.size() > 0) {
				Iterator<Object> itera = locIdByUnitName.iterator();
				while (itera.hasNext()) {
					Object[] uObj = (Object[]) itera.next();
					Integer unitId = Integer.parseInt(uObj[0].toString());
					String orgaId = uObj[2].toString();

					List<Object> unitDrilledData = newDashboardDAO.getUnitLevelDrilledData(status, orgaId, unitId,
							session, fromDate, toDate);
					System.out.println("unitDrilledData : " + unitDrilledData.size());
					if (unitDrilledData.size() > 0) {
						Iterator<Object> iterator = unitDrilledData.iterator();
						while (iterator.hasNext()) {
							Object[] next = (Object[]) iterator.next();
							JSONObject obj = new JSONObject();
							obj.put("ttrn_id", next[0].toString());
							obj.put("ttrn_client_task_id", next[1].toString());
							obj.put("ttrn_status", next[2].toString());
							obj.put("ttrn_pr_due_date", next[3].toString());
							obj.put("ttrn_rw_due_date", next[4].toString());
							obj.put("ttrn_fh_due_date", next[5].toString());
							obj.put("ttrn_uh_due_date", next[6].toString());
							obj.put("ttrn_legal_due_date", next[7].toString());
							obj.put("ttrn_submitted_date", next[8].toString());
							obj.put("orga_id", next[9].toString());
							obj.put("orga_name", next[10].toString());
							obj.put("loca_id", next[11].toString());
							obj.put("loca_name", next[12].toString());
							obj.put("dept_id", next[13].toString());
							obj.put("dept_name", next[14].toString());
							obj.put("task_legi_name", next[15].toString());
							obj.put("task_rule_name", next[16].toString());
							obj.put("task_reference", next[17].toString());
							obj.put("task_who", next[18].toString());
							obj.put("task_when", next[19].toString());
							obj.put("task_activity", next[20].toString());
							obj.put("task_procedure", next[21].toString());
							obj.put("tsk_impact", next[22].toString());
							obj.put("ttrn_frequency_for_operation", next[23].toString());
							obj.put("user_id", next[24].toString());
							obj.put("user_first_name", next[25].toString());
							obj.put("user_last_name", next[26].toString());
							obj.put("task_executor", next[25].toString() + " " + next[26].toString());
							if (next[27] == null) {
								obj.put("ttrn_completed_date", "");
							} else {
								obj.put("ttrn_completed_date", next[27].toString());
							}

							if (next[28] == null) {
								obj.put("task_cat_law_name", "");
							} else {
								obj.put("task_cat_law_name", next[28].toString());
							}

							if (next[29] == null) {
								obj.put("ttrn_performer_comments", "");
							} else {
								obj.put("ttrn_performer_comments", next[29].toString());
							}
							tasksList.add(obj);
						}
					}
				}
			}
			objForSend.put("reportList", tasksList);
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @author DnyaneshG
	 * @Date 12-12-2019
	 * @method to get function wise count
	 */

	@SuppressWarnings("unchecked")
	@Override
	public String getFunctionRisksModalData(String jsonString, HttpSession session) {

		JSONObject objForSend = new JSONObject();
		JSONArray tasksList = new JSONArray();

		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String chartName = jsonObj.get("chart_name").toString();
			String status = jsonObj.get("status").toString();
			String entityId = jsonObj.get("entity").toString();
			Integer unitId = 0;
			if (jsonObj.get("unit") != "") {
				unitId = 0;
			} else {
				unitId = Integer.parseInt(jsonObj.get("unit").toString());
			}

			String department = jsonObj.get("department").toString();
			System.out.println("department : " + department);

			String fromDate = jsonObj.get("fromDate").toString() != "" ? jsonObj.get("fromDate").toString() : "0";
			String toDate = jsonObj.get("toDate").toString() != "" ? jsonObj.get("toDate").toString() : "0";

			List<Object> funIdByFunName = newDashboardDAO.getFunctionIdByName(department);
			if (funIdByFunName.size() > 0) {
				Iterator<Object> itera = funIdByFunName.iterator();
				while (itera.hasNext()) {
					Object[] uObj = (Object[]) itera.next();
					Integer functionId = Integer.parseInt(uObj[0].toString());
					String orgaId = uObj[2].toString();

					List<Object> functionDrilledData = newDashboardDAO.getFunctionLevelDrilledData(status, entityId,
							unitId, session, fromDate, toDate);
					System.out.println("functionDrilledData : " + functionDrilledData.size());
					if (functionDrilledData.size() > 0) {
						Iterator<Object> iterator = functionDrilledData.iterator();
						while (iterator.hasNext()) {
							Object[] next = (Object[]) iterator.next();
							JSONObject obj = new JSONObject();
							obj.put("ttrn_id", next[0].toString());
							obj.put("ttrn_client_task_id", next[1].toString());
							obj.put("ttrn_status", next[2].toString());
							obj.put("ttrn_pr_due_date", next[3].toString());
							obj.put("ttrn_rw_due_date", next[4].toString());
							obj.put("ttrn_fh_due_date", next[5].toString());
							obj.put("ttrn_uh_due_date", next[6].toString());
							obj.put("ttrn_legal_due_date", next[7].toString());
							obj.put("ttrn_submitted_date", next[8].toString());
							obj.put("orga_id", next[9].toString());
							obj.put("orga_name", next[10].toString());
							obj.put("loca_id", next[11].toString());
							obj.put("loca_name", next[12].toString());
							obj.put("dept_id", next[13].toString());
							obj.put("dept_name", next[14].toString());
							obj.put("task_legi_name", next[15].toString());
							obj.put("task_rule_name", next[16].toString());
							obj.put("task_reference", next[17].toString());
							obj.put("task_who", next[18].toString());
							obj.put("task_when", next[19].toString());
							obj.put("task_activity", next[20].toString());
							obj.put("task_procedure", next[21].toString());
							obj.put("tsk_impact", next[22].toString());
							obj.put("ttrn_frequency_for_operation", next[23].toString());
							obj.put("user_id", next[24].toString());
							obj.put("user_first_name", next[25].toString());
							obj.put("user_last_name", next[26].toString());
							obj.put("task_executor", next[25].toString() + " " + next[26].toString());
							if (next[27] == null) {
								obj.put("ttrn_completed_date", "");
							} else {
								obj.put("ttrn_completed_date", next[27].toString());
							}

							if (next[28] == null) {
								obj.put("task_cat_law_name", "");
							} else {
								obj.put("task_cat_law_name", next[28].toString());
							}

							if (next[29] == null) {
								obj.put("ttrn_performer_comments", "");
							} else {
								obj.put("ttrn_performer_comments", next[29].toString());
							}
							tasksList.add(obj);
						}
					}
				}
			}
			objForSend.put("reportList", tasksList);
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getLocationListByOrgaId(String jsonString, HttpSession session) {
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String orgaId = "";
			if (jsonObj.get("orgaId") != null) {
				orgaId = jsonObj.get("orgaId").toString();
			}

			String locaId = "";
			int userSesId = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			JSONObject jsonObject = new JSONObject();
			JSONArray unitList = new JSONArray();
			JSONArray orgaList = new JSONArray();
			Integer organId = 0;
			List<Organization> allOrganization = newDashboardDAO.getAllOrganization(session);
			if (allOrganization.size() > 0) {
				Iterator<Organization> itrOrg = allOrganization.iterator();
				while (itrOrg.hasNext()) {
					Organization orgObj = itrOrg.next();
					organId = orgObj.getOrga_id();
					JSONObject orgaObj = new JSONObject();
					orgaObj.put("orgaId", orgObj.getOrga_id());
					orgaObj.put("orgaName", orgObj.getOrga_name());
					orgaList.add(orgaObj);
				}
			}

			List<Object> allUnitsByAccess = newDashboardDAO.getAllUnitsByAccess(userSesId, orgaId, session);
			if (allUnitsByAccess.size() > 0) {
				Iterator<Object> unitItr = allUnitsByAccess.iterator();
				while (unitItr.hasNext()) {
					Object[] loc = (Object[]) unitItr.next();
					JSONObject unitObj = new JSONObject();
					unitObj.put("unitId", loc[0].toString());
					unitObj.put("unitName", loc[1].toString());
					unitList.add(unitObj);
					locaId = loc[0].toString();
				}
			}

			JSONArray deptList = new JSONArray();
			List<Department> allDepartments = newDashboardDAO.getAllDepartmentsByOrgaId(session, orgaId);
			Iterator<Department> dept = allDepartments.iterator();
			while (dept.hasNext()) {

				// Initialize value for count
				int NonComp = 0;
				int Compl = 0;
				int PosingRisk = 0;
				int delay = 0;
				int WaitingApproval = 0;
				int ReOpened = 0;

				ArrayList<Integer> departmentCount = new ArrayList<>();

				Department department = dept.next();
				String depatmentName = department.getDept_name();

				JSONObject deptObj = new JSONObject();
				deptObj.put("deptId", department.getDept_id());
				deptObj.put("deptName", depatmentName);
				deptList.add(deptObj);
				System.out.println("deptObj : " + deptObj.toString());
			}

			/**
			 * @author DnyaneshG
			 * @Date 18-12-2019 Filter date by orga id and display in graph
			 */

			JSONArray tasksList = new JSONArray();
			JSONArray entityList = new JSONArray();

			JSONArray functionList = new JSONArray();

			List<Object> functionDataByOrgaIdDeptId = newDashboardDAO.getFunctionDataByOrgaIdDeptId(orgaId, session);
			if (functionDataByOrgaIdDeptId.size() > 0) {
				Iterator<Object> itr = functionDataByOrgaIdDeptId.iterator();
				while (itr.hasNext()) {
					Object[] obj = (Object[]) itr.next();
					JSONObject funObj = new JSONObject();
					funObj.put("deptId", obj[0].toString());
					funObj.put("deptName", obj[1].toString());
					functionList.add(funObj);

					String deptId = obj[0].toString();

					String fromDate = jsonObj.get("fromDate").toString() != "" ? jsonObj.get("fromDate").toString()
							: "0";
					String toDate = jsonObj.get("toDate").toString() != "" ? jsonObj.get("toDate").toString() : "0";

					List<Object> funDataForGraph = newDashboardDAO.funDataForGraph(deptId, orgaId, session, fromDate,
							toDate);
					if (funDataForGraph.size() > 0) {
						Iterator<Object> iterator = funDataForGraph.iterator();
						while (iterator.hasNext()) {
							Object[] object = (Object[]) iterator.next();
							if (object[0] == null && object[2] == null && object[1] == null && object[3] == null
									&& object[4] == null && object[5] == null) {

								JSONObject taskObj = new JSONObject();
								taskObj.put("deptName", obj[1].toString());
								taskObj.put("NonComplied", 0);
								taskObj.put("PosingRisk", 0);
								taskObj.put("Complied", 0);
								taskObj.put("Delayed", 0);
								taskObj.put("WaitingForApproval", 0);
								taskObj.put("ReOpened", 0);
								taskObj.put("DelayedReported", 0);
								tasksList.add(taskObj);
							} else {
								JSONObject taskObj = new JSONObject();
								taskObj.put("deptName", obj[1].toString());
								taskObj.put("NonComplied", object[0].toString());
								taskObj.put("PosingRisk", object[1].toString());
								taskObj.put("Complied", object[2].toString());
								taskObj.put("Delayed", object[3].toString());
								taskObj.put("WaitingForApproval", object[4].toString());
								taskObj.put("ReOpened", object[5].toString());
								taskObj.put("DelayedReported", object[6].toString());
								tasksList.add(taskObj);
							}
						}
					}
				}
			}

			jsonObject.put("orgaList", orgaList);
			jsonObject.put("unitList", unitList);
			jsonObject.put("taskList", tasksList);
			jsonObject.put("deptList", deptList);
			return jsonObject.toJSONString();
		} catch (

		Exception e) {
			e.printStackTrace();
		}
		return jsonString;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String filterDataByOrgaIdAndUnitIdURL(String jsonString, HttpSession session) {
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String orgaId = "";
			if (jsonObj.get("orgaId") != null) {
				orgaId = jsonObj.get("orgaId").toString();
			}
			Integer unitId = 0;
			if (jsonObj.get("unitId") != null) {
				unitId = Integer.parseInt(jsonObj.get("unitId").toString());
			}

			String locaId = "";
			int userSesId = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			JSONObject jsonObject = new JSONObject();
			JSONArray unitList = new JSONArray();
			JSONArray orgaList = new JSONArray();
			Integer organId = 0;
			List<Organization> allOrganization = newDashboardDAO.getAllOrganization(session);
			if (allOrganization.size() > 0) {
				Iterator<Organization> itrOrg = allOrganization.iterator();
				while (itrOrg.hasNext()) {
					Organization orgObj = itrOrg.next();
					organId = orgObj.getOrga_id();
					JSONObject orgaObj = new JSONObject();
					orgaObj.put("orgaId", orgObj.getOrga_id());
					orgaObj.put("orgaName", orgObj.getOrga_name());
					orgaList.add(orgaObj);

				}
			}

			List<Object> allUnitsByAccess = newDashboardDAO.getAllUnitsByAccess(userSesId, orgaId, session);
			if (allUnitsByAccess.size() > 0) {
				Iterator<Object> unitItr = allUnitsByAccess.iterator();
				while (unitItr.hasNext()) {
					Object[] loc = (Object[]) unitItr.next();
					JSONObject unitObj = new JSONObject();
					unitObj.put("unitId", loc[0].toString());
					unitObj.put("unitName", loc[1].toString());
					unitList.add(unitObj);
					locaId = loc[0].toString();
				}
			}

			JSONArray deptList = new JSONArray();
			List<Department> allDepartments = newDashboardDAO.getAllDepartmentsByOrgaId(session, orgaId);
			Iterator<Department> dept = allDepartments.iterator();
			while (dept.hasNext()) {

				// Initialize value for count
				int NonComp = 0;
				int Compl = 0;
				int PosingRisk = 0;
				int delay = 0;
				int WaitingApproval = 0;
				int ReOpened = 0;

				ArrayList<Integer> departmentCount = new ArrayList<>();

				Department department = dept.next();
				String depatmentName = department.getDept_name();

				JSONObject deptObj = new JSONObject();
				deptObj.put("deptId", department.getDept_id());
				deptObj.put("deptName", depatmentName);
				deptList.add(deptObj);
			}

			/**
			 * @author DnyaneshG
			 * @Date 18-12-2019 Filter date by orga id and display in graph
			 */

			JSONArray tasksList = new JSONArray();
			JSONArray entityList = new JSONArray();

			JSONArray functionList = new JSONArray();

			List<Object> functionDataByOrgaIdDeptId = newDashboardDAO.getFunctionDataByOrgaIdDeptId(orgaId, session);
			if (functionDataByOrgaIdDeptId.size() > 0) {
				Iterator<Object> itr = functionDataByOrgaIdDeptId.iterator();
				while (itr.hasNext()) {
					Object[] obj = (Object[]) itr.next();
					JSONObject funObj = new JSONObject();
					funObj.put("deptId", obj[0].toString());
					funObj.put("deptName", obj[1].toString());
					functionList.add(funObj);

					String deptId = obj[0].toString();

					String fromDate = jsonObj.get("fromDate").toString() != null ? jsonObj.get("fromDate").toString()
							: "0";
					String toDate = jsonObj.get("toDate").toString() != null ? jsonObj.get("toDate").toString() : "0";

					System.out.println("data by orgaid and unit id : ");
					List<Object> funDataForGraph = newDashboardDAO.filterDataByOrgaIdAndUnitIdURL(deptId, orgaId,
							unitId, session, fromDate, toDate);
					if (funDataForGraph.size() > 0 || funDataForGraph != null) {
						Iterator<Object> iterator = funDataForGraph.iterator();
						while (iterator.hasNext()) {
							Object[] object = (Object[]) iterator.next();
							if (object[0] == null && object[2] == null && object[1] == null && object[3] == null
									&& object[4] == null && object[5] == null) {

								JSONObject taskObj = new JSONObject();
								taskObj.put("deptName", obj[1].toString());
								taskObj.put("NonComplied", 0);
								taskObj.put("PosingRisk", 0);
								taskObj.put("Complied", 0);
								taskObj.put("Delayed", 0);
								taskObj.put("WaitingForApproval", 0);
								taskObj.put("ReOpened", 0);
								taskObj.put("DelayedReported", 0);
								tasksList.add(taskObj);
							} else {
								JSONObject taskObj = new JSONObject();
								taskObj.put("deptName", obj[1].toString());
								taskObj.put("NonComplied", object[0].toString());
								taskObj.put("PosingRisk", object[1].toString());
								taskObj.put("Complied", object[2].toString());
								taskObj.put("Delayed", object[3].toString());
								taskObj.put("WaitingForApproval", object[4].toString());
								taskObj.put("ReOpened", object[5].toString());
								taskObj.put("DelayedReported", object[6].toString());
								tasksList.add(taskObj);
							}
						}
					}
				}
			}

			jsonObject.put("orgaList", orgaList);
			jsonObject.put("unitList", unitList);
			jsonObject.put("taskList", tasksList);
			jsonObject.put("deptList", deptList);

			return jsonObject.toJSONString();
		} catch (

		Exception e) {
			e.printStackTrace();
		}
		return jsonString;
	}

	@Override
	public String approveAllTask(String jsonString, HttpSession session) {
		try {

			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			JSONArray configured_tasks = (JSONArray) jsonObj.get("tasks_list");
			for (int i = 0; i < configured_tasks.size(); i++) {
				JSONObject configured_tasks_obj = (JSONObject) configured_tasks.get(i);
				int ttrn_id = Integer.parseInt(configured_tasks_obj.get("ttrn_id").toString());
				TaskTransactional taskTransactional = tasksConfigurationDao.getTasksForCompletion(ttrn_id);

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

				tasksConfigurationDao.updateTaskConfiguration(taskTransactional);
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

}
