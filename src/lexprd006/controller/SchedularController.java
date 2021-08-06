package lexprd006.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import lexprd006.service.SchedularService;

@Controller
@RequestMapping("/*")
public class SchedularController {

	@Autowired
	SchedularService schedularService;

	//Method Written By: Mahesh Kharote(25/10/2016)
	//Method Purpose: Get all Functions rest Call
	@RequestMapping(value = "/getupcomingtasks" , method = RequestMethod.POST)
	public @ResponseBody String getUpcomingTasks(@RequestBody String jsonString, HttpSession session){
		try {
			return schedularService.getUpcomingTasks(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//Method Written By: Mahesh Kharote(25/10/2016)
	//Method Purpose: Get all Functions rest Call
	@RequestMapping(value = "/getescalationtasksforevaluator" , method = RequestMethod.POST)
	public @ResponseBody String getEscalationTasksForEvaluator(@RequestBody String jsonString, HttpSession session){
		try {
			return schedularService.getEscalationsToEvaluator(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//Method Written By: Mahesh Kharote(25/10/2016)
	//Method Purpose: Get all Functions rest Call
	@RequestMapping(value = "/getescalationtasksforfunctionhead" , method = RequestMethod.POST)
	public @ResponseBody String getEscalationTasksForFunctionHead(@RequestBody String jsonString, HttpSession session){
		try {
			return schedularService.getEscalationsToFunctionHead(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
