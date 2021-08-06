package lexprd006.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lexprd006.service.CommonLogsService;

@RequestMapping("/*")
@RestController
public class CommonLogsController {

	@Autowired
	CommonLogsService commonLogsService;

	// , method = RequestMethod.POST
	@RequestMapping(value = "/queryBuilder", method = RequestMethod.POST)
	public @ResponseBody String queryBuilder(@RequestBody String jsonString, HttpSession session) {
		System.out.println("jsonString : " + jsonString);
		return commonLogsService.getqueryBuilder(jsonString, session);
	}

	@RequestMapping(value = "/queryDeActivation", method = RequestMethod.POST)
	public @ResponseBody String queryDeActivation(@RequestBody String jsonString, HttpSession session) {
		System.out.println("queryDeActivation jsonString : " + jsonString);
		return commonLogsService.queryDeActivation(jsonString, session);
	}

	@RequestMapping(value = "/queryDisableTasks", method = RequestMethod.POST)
	public @ResponseBody String queryDisableTasks(@RequestBody String jsonString, HttpSession session) {
		System.out.println("queryDisableTasks jsonString : " + jsonString);
		return commonLogsService.queryDisableTasks(jsonString, session);
	}

	/**
	 * nd
	 * 
	 * @param session
	 * @return
	 */

	@RequestMapping(value = "/loginLogs")
	public @ResponseBody String loginLogs(HttpSession session) {
		return commonLogsService.getLoginLogs(session);
	}

	@RequestMapping(value = "/assignLogs")
	public @ResponseBody String assignLogs(HttpSession session) {
		return commonLogsService.getAssignLogs(session);
	}

	@RequestMapping(value = "/changeComplianceOwnerLogs")
	public @ResponseBody String changeComplianceOwnerLogs(HttpSession session) {
		return commonLogsService.changeComplianceOwnerLogs(session);
	}

	@RequestMapping(value = "/tasksConfigLogs")
	public @ResponseBody String tasksConfigLogs(HttpSession session) {
		return commonLogsService.tasksConfigLogs(session);
	}

	@RequestMapping(value = "/complianceReportLogs")
	public @ResponseBody String complianceReportLogs(HttpSession session) {
		return commonLogsService.complianceReportLogs(session);
	}

	@RequestMapping(value = "/emailLogs")
	public @ResponseBody String emailLogs(HttpSession session) {
		return commonLogsService.emailLogs(session);
	}

	@RequestMapping(value = "/activateDeActivateLogs")
	public @ResponseBody String activateDeActivateLogs(HttpSession session) {
		return commonLogsService.activateDeActivateLogs(session);
	}

	@RequestMapping(value = "/reactivateTasks")
	public @ResponseBody String reactivateTasks(HttpSession session) {
		return commonLogsService.reactivateTasks(session);
	}

}
