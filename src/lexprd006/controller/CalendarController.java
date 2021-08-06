package lexprd006.controller;

import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import lexprd006.domain.CalendarDomain;
import lexprd006.service.CalendarService;

/*
 * Author: Mahesh Kharote
 * Date: 02/03/2016
 * Updated By: 
 * Updated Date: 
 * 
 * */

@Controller
@RequestMapping("/*")
public class CalendarController {

	@Autowired
	CalendarService calendarService;

	// Method Created By: Mahesh Kharote
	// Method Purpose: Get User Id set in session
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/showComplianceCalendar", method = RequestMethod.POST)
	public @ResponseBody String showComplianceCalendar(@RequestBody String jsonString, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		try {
			String resJson = calendarService.getAllTasksAssignedToUserForCalendarDepartmentWise(session);
			session.setAttribute("backFromTaskDetails", "./showComplianceCalendar");
			return resJson;
		} catch (Exception e) {
			e.printStackTrace();
			dataForSend.put("errorMessage", e.getMessage());
		}
		return null;

	}

	// Method Created By: Mahesh Kharote
	// Method Purpose: Get User Id set in session
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getTasksForPerformerCalendar", method = RequestMethod.GET)
	public @ResponseBody String getTasksForPerformer(HttpSession session) throws ParseException {
		try {
			JSONArray jsonArray = new JSONArray();
			List<CalendarDomain> listOfAssignedTasks = calendarService
					.getAllTasksAssignedToUserForCalendarPerformerWise(
							Integer.parseInt(session.getAttribute("sess_user_id").toString()));
			Iterator<CalendarDomain> itr = listOfAssignedTasks.iterator();

			while (itr.hasNext()) {
				JSONObject jsonObject = new JSONObject();
				CalendarDomain calendarDomain = (CalendarDomain) itr.next();
				jsonObject.put("client_task_id", calendarDomain.getClient_task_id());
				jsonObject.put("performer_due_date", calendarDomain.getPerformer_due_date());
				jsonObject.put("task_status", calendarDomain.getTask_status());
				jsonObject.put("task_which_entity_wise", "Performer Wise");// Just to show some color, work has be done
																			// on this

				jsonArray.add(jsonObject);
			}

			return jsonArray.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";

	}

	// Method created : Harshad Padole on 14-04-2016
	// Method purpose : search task for calendar
	@RequestMapping(value = "/searchTasksForCalendar", method = RequestMethod.POST)
	public @ResponseBody String searchTasksForCalendar(@RequestBody String json, HttpSession session)
			throws ParseException {
		try {

			String taskList = calendarService.searchTaskForCalendar(json, session);

			return taskList;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";

	}

	// Method Created By: Harshad padole(5/7/2016)
	// Method Purpose: Get configured task Frequency from task_transactional
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getFrequencyFromTtrn", method = RequestMethod.POST)
	public @ResponseBody String getAssignedFrequency(HttpSession session) {
		try {
			List<String> result = calendarService.getDistinctFrequencyForUser(session);
			JSONArray sendFrequency = new JSONArray();
			Iterator<String> frequency = result.iterator();
			while (frequency.hasNext()) {
				String objects = frequency.next();

				JSONObject jsonObject = new JSONObject();

				jsonObject.put("frequency", objects.toString());
				sendFrequency.add(jsonObject);
			}
			return sendFrequency.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

//Search Code for Calendar Page
	@RequestMapping(value = "/getentity", method = RequestMethod.GET)
	public @ResponseBody String getentity(HttpSession session) {
		try {
			return calendarService.getEntityList(session);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/getunit", method = RequestMethod.GET)
	public @ResponseBody String getunit(@RequestParam("entity_id") String entity_id, HttpSession session) {
		try {
			System.out.println("I am in getunit");
			return calendarService.getUnitList(entity_id, session);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/getFunction", method = RequestMethod.GET)
	public @ResponseBody String getFunction(@RequestParam("unit_id") String unit_id,
			@RequestParam("orga_id") String orga_id, HttpSession session) {
		try {
			return calendarService.getFunctionList(unit_id, orga_id, session);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/getExeEvalFuncHeadList", method = RequestMethod.POST)
	public @ResponseBody String getExeEvalFuncHeadList(@RequestBody String jsonString, HttpSession session) {
		try {
			return calendarService.getExeEvalFuncHeadList(jsonString, session);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/getTaskHistoryListForCalender", method = RequestMethod.POST)
	public @ResponseBody String getTaskHistoryListForCalender(@RequestBody String jsonString, HttpSession session) {
		try {
			return calendarService.getTaskHistoryByClientTaskId(jsonString, session);
		} catch (Exception e) {
			return null;
		}
	}
}
