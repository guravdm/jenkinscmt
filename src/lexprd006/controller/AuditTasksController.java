package lexprd006.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import lexprd006.service.AuditTasksService;

@Controller
@RequestMapping("/*")
public class AuditTasksController {

	private @Value("#{config['project_name'] ?: 'null'}") String projectName;

	@Autowired
	AuditTasksService auditTasksService;

	@RequestMapping(value = "/getMonthlyComplianceStatus", method = RequestMethod.POST)
	public @ResponseBody String getMonthlyComplianceStatus(@RequestBody String jsonString, HttpSession session) {
		try {
			System.out.println("getMonthlyComplianceStatus : " + jsonString.toString());
			return auditTasksService.getMonthlyComplianceStatus(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Search Audit Dashboard By dates
	 * 
	 * @param orga_id
	 * @param loca_id
	 * @param dept_id
	 * @param date_from
	 * @param date_to
	 * @param session
	 * @return
	 */

	@RequestMapping(value = "/searchAuditDashboard", method = RequestMethod.POST)
	public @ResponseBody String searchAuditDashboard(@RequestBody String jsonString, HttpSession session) {
		try {
			System.out.println("searchAuditDashboard : " + jsonString.toString());
			return auditTasksService.searchAuditDashboard(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/searhMonthlyComplianceStatus", method = RequestMethod.GET)
	public @ResponseBody String searhMonthlyComplianceStatus(@RequestParam("orga_id") String orga_id,
			@RequestParam("loca_id") String loca_id, @RequestParam("dept_id") String dept_id,
			@RequestParam("date_from") String date_from, @RequestParam("date_to") String date_to, HttpSession session) {
		try {
			System.out.println("searhMonthlyComplianceStatus : " + orga_id.toString());
			return auditTasksService.searhMonthlyComplianceStatus(orga_id, loca_id, dept_id, date_from, date_to,
					session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param jsonString
	 * @param session
	 * @return
	 */

	@RequestMapping(value = "/auditTaskDashboard", method = RequestMethod.POST)
	public @ResponseBody String auditTaskDashboard(@RequestBody String jsonString, HttpSession session) {
		try {
			System.out.println("auditTaskDashboard jsonString : " + jsonString.toString());
			return auditTasksService.auditTaskDashboard(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @author DnyaneshG
	 * @param jsonString
	 * @param session
	 * @return search postAuditReport by date wise function wise and by other fields
	 */

	@RequestMapping(value = "/searchMonthlyComplianceAuditChart", method = RequestMethod.POST)
	public @ResponseBody String monthlyComplianceAuditChartURL(@RequestBody String jsonString, HttpSession session) {
		try {
			System.out.println("searchMonthlyComplianceAuditChart jsonString : " + jsonString.toString());
			return auditTasksService.monthlyComplianceAuditChartURL(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/getHeadCountsByLocation", method = RequestMethod.GET)
	public @ResponseBody String getHeadCountsByLocation(HttpSession session, HttpServletResponse res) {
		try {
			System.out.println("getHeadCountsByLocation");
			return auditTasksService.getHeadCountsByLocation(session, res);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/searchAuditRepository", method = RequestMethod.POST)
	public @ResponseBody String searchAuditRepository(@RequestParam("data") String jsonString, HttpSession session,
			HttpServletResponse res) {
		try {
			System.out.println("jsonString : " + jsonString);
			return auditTasksService.searchAuditRepository(jsonString, session, res);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/copyComplianceOwner", method = RequestMethod.POST)
	public @ResponseBody String copyComplianceOwner(@RequestBody String jsonString, HttpSession session) {
		try {
			System.out.println("copyComplianceOwner jsonString : " + jsonString.toString());
			return auditTasksService.copyComplianceOwner(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/submitSimplyCompDocumentsURL", method = RequestMethod.POST)
	public @ResponseBody String submitSimplyCompDocumentsURL(
			@RequestParam("ttrn_proof_of_compliance") ArrayList<MultipartFile> ttrn_proof_of_compliance,
			@RequestParam("jsonString") String jsonString, HttpSession session) {
		try {
			System.out.println("submitSimplyCompDocumentsURL jsonString : " + jsonString.toString());
			return auditTasksService.submitSimplyCompDocumentsURL(ttrn_proof_of_compliance, jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Bean(name = "multipartResolver")
	public MultipartResolver multipartResolver() {
		System.out.println("In multipart resolver");
		MultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		return commonsMultipartResolver;
	}

	/**
	 * @auth Dnyanesh
	 * @param jsonString
	 * @param session
	 * @return Library Function
	 */

	@RequestMapping(value = "/listSimplyCompDocuments", method = RequestMethod.GET)
	public @ResponseBody String listSimplyCompDocuments(String jsonString, HttpSession session) {
		try {
			// System.out.println("listSimplyCompDocuments jsonString : " +
			// jsonString.toString());
			return auditTasksService.listSimplyCompDocuments(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/getTaskActivityListURL", method = RequestMethod.GET)
	public @ResponseBody String getTaskActivityListURL(String jsonString, HttpSession session) {
		try {
			return auditTasksService.getTaskActivityListURL(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/downloadComplianceDocument", method = RequestMethod.GET)
	public String downloadComplianceDocument(int docId, HttpServletResponse response) throws Throwable {
		try {
			System.out.println("docId : " + docId);
			JSONObject jsonString = new JSONObject();
			jsonString.put("udoc_id", docId);
			return auditTasksService.downloadComplianceDocument(jsonString.toJSONString(), response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @author DnyaneshG
	 * @param jsonString
	 * @param session
	 * @return Auditor Actions for non-complied
	 */

	@RequestMapping(value = "/makeNonCompliedTasks", method = RequestMethod.POST)
	public @ResponseBody String makeNonCompliedTasks(@RequestBody String jsonString, HttpSession session) {
		try {
			System.out.println("makeNonCompliedTasks URL jsonString : " + jsonString.toString());
			return auditTasksService.makeNonCompliedTasks(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/approverCompliedTasksURL", method = RequestMethod.POST)
	public @ResponseBody String approverCompliedTasksURL(@RequestBody String json, HttpSession session) {
		try {
			System.out.println("approverCompliedTasksURL json :" + json);
			return auditTasksService.approverCompliedTasksURL(json, session);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/verticalWiceReport", method = RequestMethod.POST)
	public @ResponseBody String verticalWiceReport(@RequestBody String jsonString, HttpSession session) {
		return auditTasksService.verticalWiceReport(jsonString, session);
	}

	/**
	 * 
	 * @param jsonString
	 * @param session
	 * @return Progress Report
	 */

	@RequestMapping(value = "/statusCompletionReport", method = RequestMethod.POST)
	public @ResponseBody String statusCompletionReport(@RequestBody String jsonString, HttpSession session) {
		System.out.println("jsonString : " + jsonString.toString());
		return auditTasksService.statusCompletionReport(jsonString, session);
	}

	@RequestMapping(value = "/searchExecutorList", method = RequestMethod.POST)
	public @ResponseBody String searchExecutorList(@RequestParam("data") String jsonString, HttpSession session) {
		try {
			System.out.println("searchExecutorList jsonString : " + jsonString);
			return auditTasksService.searchExecutorList(jsonString, session);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Final Audit Report API
	 * 
	 * @author DnyaneshG
	 * @Date 04-07-2021
	 */

	@RequestMapping(value = "/finalComplianceAuditReport", method = RequestMethod.POST)
	public @ResponseBody String finalComplianceAuditReport(@RequestBody String jsonString, HttpSession session) {
		try {
			System.out.println("finalComplianceAuditReport jsonString : " + jsonString);
			return auditTasksService.finalComplianceAuditReport(jsonString, session);
		} catch (Exception e) {
			return null;
		}
	}
	
	@RequestMapping(value = "/getFunctionListByOrgaId", method = RequestMethod.POST)
	public @ResponseBody String getFunctionListByOrgaIdURL(@RequestBody String jsonString, HttpSession session) {
		try {
			System.out.println("getFunctionListByOrgaIdURL : " + jsonString.toString());
			return auditTasksService.getFunctionListByOrgaId(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
