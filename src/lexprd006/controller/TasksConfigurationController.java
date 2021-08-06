package lexprd006.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lexprd006.service.TasksConfigurationService;

@Controller
@RequestMapping(value = "/*")
public class TasksConfigurationController {

	@Autowired
	TasksConfigurationService tasksconfigurationservice;

	// Method Written By: Mahesh Kharote(11/01/2017)
	// Method Purpose: Save Tasks Configuration rest Call
	@RequestMapping(value = "/savetasksconfiguration", method = RequestMethod.POST)
	public @ResponseBody String savetasksconfiguration(@RequestBody String jsonString, HttpSession session) {
		try {
			System.out.println("jsonString : " + jsonString.toString());
			return tasksconfigurationservice.savetasksconfiguration(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Written By: Mahesh Kharote(11/01/2017)
	// Method Purpose: Save Tasks Configuration rest Call
	@RequestMapping(value = "/updatetasksconfiguration", method = RequestMethod.POST)
	public @ResponseBody String updatetasksconfiguration(@RequestBody String jsonString, HttpSession session) {
		try {
			return tasksconfigurationservice.updateTasksConfiguration(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Written By: Mahesh Kharote(11/01/2017)
	// Method Purpose: Activate Tasks rest Call
	@RequestMapping(value = "/activationoftasks", method = RequestMethod.POST)
	public @ResponseBody String activationOfTasks(@RequestBody String jsonString, HttpSession session) {
		try {
			return tasksconfigurationservice.activationOfTasks(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Written By: Mahesh Kharote(11/01/2017)
	// Method Purpose: Activate Tasks rest Call
	/*
	 * @RequestMapping(value = "/getallconfiguredtaskforactivationpage", method =
	 * RequestMethod.POST) public @ResponseBody String
	 * getAllConfiguredTaskForActivationPage(@RequestBody String jsonString,
	 * HttpSession session) { try { return
	 * tasksconfigurationservice.getAllConfiguredTaskForActivationPage(jsonString,
	 * session); } catch (Exception e) { e.printStackTrace(); return null; } }
	 */

	// Method Written By: Mahesh Kharote(11/01/2017)
	// Method Purpose: Get All Mapped Tasks For Configuration Page
	@RequestMapping(value = "/getallmappedtasksforconfigurationpage", method = RequestMethod.POST)
	public @ResponseBody String getAllMappedTasksForConfigurationPage(@RequestBody String jsonString,
			HttpSession session) {
		try {
			return tasksconfigurationservice.searchTaskForConfiguration(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Written By: Mahesh Kharote(03/01/2016)
	// Method Purpose: Save task completion
	@RequestMapping(value = "/savetaskcompletion", method = RequestMethod.POST)
	public @ResponseBody String savetaskcompletion(
			@RequestParam("ttrn_proof_of_compliance") ArrayList<MultipartFile> ttrn_proof_of_compliance,
			@RequestParam("jsonString") String jsonString, HttpSession session) {
		System.out.println("No of documents attached: " + ttrn_proof_of_compliance.size());
		System.out.println("savetaskcompletion json : " + jsonString);
		try {
			return tasksconfigurationservice.saveTaskCompletion(ttrn_proof_of_compliance, jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

// Method Created By: Harshad padole
	// Method Purpose: search task for default configuration
	@RequestMapping(value = "/searchTaskForDefaultConfiguration", method = RequestMethod.POST)
	public @ResponseBody String searchClinicalTaskForConfiguration(@RequestBody String json) {
		try {
			return tasksconfigurationservice.searchTaskForConfiguration(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Harshad padole(06/04/2017)
	// Method Purpose : save default task for configuration
	@RequestMapping(value = "/saveDefaultTaskConfiguration", method = RequestMethod.POST)
	public @ResponseBody String saveClinicalConfiguration(@RequestBody String jsonString, HttpSession session) {
		try {
			String result = tasksconfigurationservice.savedefaulttaskconfiguration(jsonString, session);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "notSuccess";
	}

	// Method Created By: Harshad padole
	// Method Purpose: initiate default task
	@RequestMapping(value = "/initiateDefaultConfiguredTask", method = RequestMethod.POST)
	public @ResponseBody String initiateDefaultConfiguredTask(@RequestBody String json, HttpSession session) {
		try {
			return tasksconfigurationservice.initiateDefaultConfiguredTask(json, session);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Harshad Padole(06/04/2017)
	// Method Purpose: Approve task
	@RequestMapping(value = "/approveTask", method = RequestMethod.POST)
	public @ResponseBody String approveTask(@RequestBody String json, HttpSession session) {
		try {
			return tasksconfigurationservice.approveTask(json, session);
		} catch (Exception e) {
			return null;
		}
	}

	// Method Created By: Harshad Padole(06/04/2017)
	// Method Purpose: Approve task
	@RequestMapping(value = "/reopenTask", method = RequestMethod.POST)
	public @ResponseBody String reopenTask(@RequestBody String json, HttpSession session) {
		try {
			System.out.println("reopenTask json : " + json.toString());
			return tasksconfigurationservice.reOpenTask(json, session);
		} catch (Exception e) {
			return null;
		}
	}

	// Method Created By: Harshad Padole
	// Method Purpose: Delete Task History
	@RequestMapping(value = "/deleteTaskHistory", method = RequestMethod.POST)
	public @ResponseBody String deleteTaskHistory(@RequestBody String json, HttpSession session) {
		try {
			return tasksconfigurationservice.deleteTaskHistory(json, session);
		} catch (Exception e) {
			return null;
		}
	}

	// Method Created By: Harshad Padole
	// Method Purpose: Delete Task Mapping
	@RequestMapping(value = "/deleteTaskMapping", method = RequestMethod.POST)
	public @ResponseBody String deleteTaskMapping(@RequestBody String json, HttpSession session) {
		try {
			return tasksconfigurationservice.deleteTaskMapping(json, session);
		} catch (Exception e) {
			return null;
		}
	}

	// Method Created By: Harshad Padole
	// Method Purpose: get default task configurations
	@RequestMapping(value = "/getDefaultTaskList", method = RequestMethod.POST)
	public @ResponseBody String getDefaultTaskList(@RequestBody String json, HttpSession session) {
		try {
			return tasksconfigurationservice.getDefaultTaskConfiguration(json);
		} catch (Exception e) {
			return null;
		}
	}

	// Method Created By: Harshad Padole
	// Method Purpose:Update default task configurations
	@RequestMapping(value = "/updateDefaultTaskConfiguration", method = RequestMethod.POST)
	public @ResponseBody String updateDefaultTaskConfiguration(@RequestBody String json, HttpSession session) {
		try {
			return tasksconfigurationservice.updateDefaultTaskConfiguration(json, session);
		} catch (Exception e) {
			return null;
		}
	}

	// Method Written By: Harshad Padole
	// Method Purpose: update task reason for non compliance
	@RequestMapping(value = "/updateTaskReasonForNonCompliance", method = RequestMethod.POST)
	public @ResponseBody String updateTaskReasonForNonCompliance(@RequestBody String jsonString, HttpSession session) {
		try {
			return tasksconfigurationservice.updateResoneOfNonCompliance(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Written By: Harshad Padole
	// Method Purpose: save sub task completion
	@RequestMapping(value = "/savesubtaskcompletion", method = RequestMethod.POST)
	public @ResponseBody String savesubtaskcompletion(
			@RequestParam("ttrn_proof_of_compliance") ArrayList<MultipartFile> ttrn_proof_of_compliance,
			@RequestParam("jsonString") String jsonString, HttpSession session) {
		try {
			return tasksconfigurationservice.saveSubTaskCompletion(ttrn_proof_of_compliance, jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Created By: Calis Lopes
	// Method Purpose: Delete uploaded document
	@RequestMapping(value = "/deleteTaskDocument", method = RequestMethod.POST)
	public @ResponseBody String deleteTaskDocument(@RequestBody String json, HttpSession session) {
		try {
			return tasksconfigurationservice.deleteTaskDocument(json, session);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/searchConfiguartionPage", method = RequestMethod.POST)
	public @ResponseBody String searchConfiguartionPage(@RequestBody String jsonString, HttpSession session) {
		try {
			return tasksconfigurationservice.searchTaskForConfigurationPage(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/getTaskListForDefaultTaskConfiguration", method = RequestMethod.POST)
	public @ResponseBody String getTaskListForDefaultTaskConfiguration(@RequestBody String jsonString) {
		try {
			return tasksconfigurationservice.getTaskListForDefaultTaskConfiguration(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
