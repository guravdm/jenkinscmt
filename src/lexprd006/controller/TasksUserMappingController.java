package lexprd006.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import lexprd006.service.TasksUserMappingService;

@Controller
@RequestMapping(value = "/*")
public class TasksUserMappingController {

	@Autowired
	TasksUserMappingService tasksUserMappingService;

	// Method Written By: Mahesh Kharote(10/01/2017)
	// Method Purpose: Save Tasks User Mapping rest Call
	@RequestMapping(value = "/savetasksusermapping", method = RequestMethod.POST)
	public @ResponseBody String savetasksusermapping(@RequestBody String jsonString, HttpSession session) {
		try {
			return tasksUserMappingService.saveTasksUserMapping(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Written By: Mahesh Kharote(13/02/2017)
	// Method Purpose: Save Tasks User Mapping rest Call
	@RequestMapping(value = "/getdistinctcountries", method = RequestMethod.GET)
	public @ResponseBody String getDistinctCountries() {
		try {
			return tasksUserMappingService.getDistinctCountries();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Written By: Mahesh Kharote(13/02/2017)
	// Method Purpose: Save Tasks User Mapping rest Call
	@RequestMapping(value = "/getallstateforcountry", method = RequestMethod.POST)
	public @ResponseBody String getStatesForCountry(@RequestBody String jsonString) {
		try {
			return tasksUserMappingService.getAllStateForCountry(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Written By: Mahesh Kharote(13/02/2017)
	// Method Purpose: Save Tasks User Mapping rest Call
	@RequestMapping(value = "/getallcatlaw", method = RequestMethod.POST)
	public @ResponseBody String getAllCatLawFromMst_task(@RequestBody String jsonString) {
		try {
			return tasksUserMappingService.getAllCat_lawFromMst_task(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Written By: Mahesh Kharote(13/02/2017)
	// Method Purpose: Save Tasks User Mapping rest Call
	@RequestMapping(value = "/getalllegi", method = RequestMethod.POST)
	public @ResponseBody String getAllLegiFromMst_task(@RequestBody String jsonString) {
		try {
			return tasksUserMappingService.getlegislationFromMst_task(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Written By: Mahesh Kharote(13/02/2017)
	// Method Purpose: Save Tasks User Mapping rest Call
	@RequestMapping(value = "/getalllegirule", method = RequestMethod.POST)
	public @ResponseBody String getAllLegiruleFromMst_task(@RequestBody String jsonString) {
		try {
			return tasksUserMappingService.getRuleFromMst_task(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Written By: Mahesh Kharote(13/02/2017)
	// Method Purpose: Save Tasks User Mapping rest Call
	@RequestMapping(value = "/searchtasksforusermapping", method = RequestMethod.POST)
	public @ResponseBody String searchTasksForTaskUserMapping(@RequestBody String jsonString) {
		try {
			return tasksUserMappingService.SearchTaskFromMst_task(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Written By: Mahesh Kharote(11/01/2017)
	// Method Purpose: Activate Tasks rest Call
	@RequestMapping(value = "/getallmappedtasksforenablingpage", method = RequestMethod.POST)
	public @ResponseBody String getAllMappedTasksForEnablingPage(@RequestBody String jsonString, HttpSession session) {
		try {
			return tasksUserMappingService.getAllMappedTasksForEnablingPage(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Written By: Mahesh Kharote(11/01/2017)
	// Method Purpose: Enable Tasks rest Call
	@RequestMapping(value = "/enablingoftasks", method = RequestMethod.POST)
	public @ResponseBody String enablingOfTasks(@RequestBody String jsonString, HttpSession session) {
		try {
			return tasksUserMappingService.enablingOfTasks(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Written By: Mahesh Kharote(11/01/2017)
	// Method Purpose: Change Compliance rest Call
	@RequestMapping(value = "/changecomplianceowner", method = RequestMethod.POST)
	public @ResponseBody String changeComplianceOwner(@RequestBody String jsonString, HttpSession session) {
		try {
			return tasksUserMappingService.changeComplianceOwner(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/getTaskForChangeComplianceOwnerPage", method = RequestMethod.POST)
	public @ResponseBody String getTaskForChangeComplianceOwnerPage(@RequestBody String jsonString,
			HttpSession session) {
		try {
			return tasksUserMappingService.getTaskForChangeComplianceOwnerPage(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
