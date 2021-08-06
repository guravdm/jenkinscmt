package lexprd006.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import lexprd006.service.NewDashboardService;

@Controller
@RequestMapping(value = "/*")
public class NewDashboardController {

	@Autowired
	NewDashboardService newDashboardService;

	@RequestMapping(value = "/getoverallCountForDashboard", method = RequestMethod.POST)
	private @ResponseBody String getOverallCount(@RequestBody String jsonString, HttpSession session) {
		System.out.println("jsonString : " + jsonString.toString());
		return newDashboardService.getOverAllCount(jsonString, session);
	}

	/**
	 * @author DnyaneshG
	 * @Date 09-12-2019
	 * 
	 */
	@RequestMapping(value = "/getoveralldrilleddata", method = RequestMethod.GET)
	private @ResponseBody String getoveralldrilleddata(String jsonString, HttpSession session) {
		System.out.println("jsonString : " + jsonString.toString());
		return newDashboardService.getoveralldrilleddata(jsonString, session);
	}

	/**
	 * 
	 * @param jsonString
	 * @param session
	 * @return Entity Graph count for pie chart &amp; modal data
	 */

	@RequestMapping(value = "/getEntityRisksCount", method = RequestMethod.POST)
	private @ResponseBody String getEntityRisksCount(@RequestBody String jsonString, HttpSession session) {
		System.out.println("entity risk count : " + jsonString);
		return newDashboardService.getEntityRisksCount(jsonString, session);
	}

	@RequestMapping(value = "/getEntityRisksModalData", method = RequestMethod.GET)
	private @ResponseBody String getEntityRisksDataOnModal(String jsonString, HttpSession session) {
		System.out.println("entity risk modal : " + jsonString);
		return newDashboardService.getEntityRisksDrilledData(jsonString, session);
	}

	/**
	 * End Entity Graph
	 */

	/**
	 * @author DnyaneshG
	 * @param jsonString
	 * @param session
	 * @return unit graph data
	 */

	@RequestMapping(value = "/getUnitRisksCount", method = RequestMethod.POST)
	private @ResponseBody String getUnitRisksCount(@RequestBody String jsonString, HttpSession session) {
		System.out.println("jsonString : " + jsonString);
		return newDashboardService.getUnitRisksCount(jsonString, session);
	}

	/**
	 * @author DnyaneshG
	 * @Date 12-12-2019
	 * @param jsonString
	 * @param session
	 * @return modal data for unit graph
	 */
	@RequestMapping(value = "/getUnitRisksModalData", method = RequestMethod.GET)
	private @ResponseBody String getUnitRisksModalData(String jsonString, HttpSession session) {
		System.out.println("getUnitRisksModalData : " + jsonString.toString());
		return newDashboardService.getUnitRisksModalData(jsonString, session);
	}

	/**
	 * @author DnyaneshG
	 * @Date 12-12-2019
	 * @param jsonString
	 * @param session
	 * @return
	 */

	@RequestMapping(value = "/getFunctionRisksCount", method = RequestMethod.POST)
	private @ResponseBody String getFunctionRisksCount(@RequestBody String jsonString, HttpSession session) {
		System.out.println("getFunctionRisksCount : " + jsonString);
		return newDashboardService.getFunctionRisksCount(jsonString, session);
	}

	@RequestMapping(value = "/getFunctionRisksModalData", method = RequestMethod.GET)
	private @ResponseBody String getFunctionRisksModalData(String jsonString, HttpSession session) {
		System.out.println("getFunctionRisksModalData : " + jsonString);
		return newDashboardService.getFunctionRisksModalData(jsonString, session);
	}

	/**
	 * @author DnyaneshG
	 * @Date 30-12-2019
	 * @param jsonString
	 * @param session
	 * @return onChange byEntity display graph with it's status. getUnit function
	 *         called from front end
	 */

	@RequestMapping(value = "/getLocationListByOrgaId", method = RequestMethod.POST)
	private @ResponseBody String getLocationListByOrgaId(@RequestBody String jsonString, HttpSession session) {
		System.out.println("getLocationListByOrgaId : " + jsonString.toString());
		return newDashboardService.getLocationListByOrgaId(jsonString, session);
	}

	@RequestMapping(value = "/getFinancialRisksCount", method = RequestMethod.POST)
	private @ResponseBody String getFinancialRisksCount(@RequestBody String jsonString, HttpSession session) {
		return newDashboardService.getFinancialRisksCount(jsonString, session);
	}

	/**
	 * @author DnyaneshG
	 * @Date 06-01-2019
	 */

	@RequestMapping(value = "/filterDataByOrgaIdAndUnitIdURL", method = RequestMethod.POST)
	private @ResponseBody String filterDataByOrgaIdAndUnitIdURL(@RequestBody String jsonString, HttpSession session) {
		System.out.println("filterDataByOrgaIdAndUnitIdURL : " + jsonString.toString());
		return newDashboardService.filterDataByOrgaIdAndUnitIdURL(jsonString, session);
	}

	/**
	 * @author Swapnali
	 * @Date 13-01-2020
	 */

	/*@RequestMapping(value = "/approveAllTask", method = RequestMethod.POST)
	private @ResponseBody String approveAllTask(@RequestBody String jsonString, HttpSession session) {
		return newDashboardService.approveAllTask(jsonString, session);
	}*/
	
}
