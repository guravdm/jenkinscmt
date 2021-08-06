package lexprd006.service.impl;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lexprd006.dao.impl.DashboardCDAO;
import lexprd006.service.DashboardCService;

@Service(value = "dcService")
public class DashboardCServiceImpl implements DashboardCService {

	@Autowired
	DashboardCDAO dcDAO;

	@SuppressWarnings("unchecked")
	@Override
	public String complianceDashboardCount(String jsonString, HttpSession session) {
		JSONObject objForSend = new JSONObject();
		JSONArray tasksList = new JSONArray();
		try {
			List<Object> complianceDashboardCount = dcDAO.complianceDashboardCount(
					Integer.parseInt(session.getAttribute("sess_user_id").toString()),
					Integer.parseInt(session.getAttribute("sess_role_id").toString()), jsonString);
			if (complianceDashboardCount.size() > 0 && complianceDashboardCount != null) {
				Iterator<Object> iterator = complianceDashboardCount.iterator();
				while (iterator.hasNext()) {
					JSONObject taskObj = new JSONObject();
					Object next[] = (Object[]) iterator.next();
					taskObj.put("PosingRisk", next[0]);
					taskObj.put("Complied", next[1]);
					taskObj.put("NonComplied", next[2]);
					taskObj.put("Delayed", next[3]);
					taskObj.put("DelayedReported", next[4]);
					taskObj.put("ReOpened", next[5]);
					taskObj.put("WaitingForApproval", next[6]);
					tasksList.add(taskObj);
				}
			}
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
	public String searchComplianceDashboardCount(String jsonString, HttpSession session) {
		JSONObject objForSend = new JSONObject();
		JSONArray tasksList = new JSONArray();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String fromDate = "";
			String toDate = "";

			if (jsonObj.get("date_from") != null && jsonObj.get("date_to") != null) {
				fromDate = jsonObj.get("date_from").toString();
				toDate = jsonObj.get("date_to").toString();
			}

			String orgaId = "0";
			if (jsonObj.get("orgaId") != null) {
				orgaId = jsonObj.get("orgaId").toString();
			}

			List<Object> tasksCount = dcDAO.searchComplianceDashboardCount(
					Integer.parseInt(session.getAttribute("sess_user_id").toString()),
					Integer.parseInt(session.getAttribute("sess_role_id").toString()), fromDate, toDate, orgaId);

			if (tasksCount.size() > 0 && tasksCount != null) {
				Iterator<Object> iterator = tasksCount.iterator();
				while (iterator.hasNext()) {
					JSONObject taskObj = new JSONObject();
					Object next[] = (Object[]) iterator.next();
					taskObj.put("PosingRisk", next[0]);
					taskObj.put("Complied", next[1]);
					taskObj.put("NonComplied", next[2]);
					taskObj.put("Delayed", next[3]);
					taskObj.put("DelayedReported", next[4]);
					taskObj.put("ReOpened", next[5]);
					taskObj.put("WaitingForApproval", next[6]);
					tasksList.add(taskObj);
				}
			}
			objForSend.put("taskList", tasksList);
			return objForSend.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("ErrorMessage", "Failed");
			return objForSend.toJSONString();
		}
	}

}
