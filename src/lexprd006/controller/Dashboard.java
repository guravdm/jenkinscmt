package lexprd006.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import lexprd006.service.DashboardCService;

@Controller
@RequestMapping("/*")
public class Dashboard {

	@Autowired
	DashboardCService dcService;

	@RequestMapping(value = "/complianceDashboardCount", method = RequestMethod.POST)
	public @ResponseBody String complianceDashboardCount(@RequestBody String jsonString, HttpSession session) {
		try {
			System.out.println("complianceDashboardCount 1 " + jsonString);
			return dcService.complianceDashboardCount(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/searchComplianceDashboardCount", method = RequestMethod.POST)
	public @ResponseBody String searchComplianceDashboardCount(@RequestBody String jsonString, HttpSession session) {
		try {
			System.out.println(" searchComplianceDashboardCount : " + jsonString);
			return dcService.searchComplianceDashboardCount(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
