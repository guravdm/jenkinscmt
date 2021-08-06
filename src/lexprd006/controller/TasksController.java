
package lexprd006.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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

import lexprd006.service.TasksConfigurationService;
import lexprd006.service.TasksRepositoryService;
import lexprd006.service.TasksService;

@Controller
@RequestMapping("/*")
public class TasksController {

	@Autowired
	TasksService tasksService;
	@Autowired
	TasksRepositoryService tasksRepositoryService;

	@Autowired
	TasksConfigurationService tasksconfigurationservice;

	// Method Written By: Mahesh Kharote(03/01/2016)
	// Method Purpose: Get all Tasks rest Call
	@RequestMapping(value = "/getimportedtasks", method = RequestMethod.GET)
	public @ResponseBody String getAllImportedTasks() {
		try {
			return tasksService.getAllImportedTasks();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Written By: Mahesh Kharote(03/01/2016)
	// Method Purpose: Import Legal Updates
	@RequestMapping(value = "/importlegalupdatesfromfile", method = RequestMethod.POST)
	public @ResponseBody String importLegalUpdates(
			@RequestParam("legal_update_activity_list") MultipartFile ttrn_proof_of_compliance,
			@RequestParam("jsonString") String jsonString, HttpSession session) {
		try {
			System.out.println("No of documents attached:" + ttrn_proof_of_compliance.getOriginalFilename());
			return tasksService.addupdateTaskLegalUpdate(ttrn_proof_of_compliance, jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Created By: Mahesh Kharote(25/02/2016)
	// Method Purpose: Save task completion page for completing task
	@RequestMapping(value = "/gethistoryfortask", method = RequestMethod.POST)
	public @ResponseBody String getHistoryForTask(@RequestBody String jsonString, HttpSession session) {
		try {
			System.out.println("TasksController gethistoryfortask : " + jsonString);
			return tasksService.getTaskHistoryByClientTaskId(jsonString, session);
		} catch (Exception e) {
			return null;
		}
	}

	// Method Created By: Mahesh Kharote(25/02/2016)
	// Method Purpose: Save task completion page for completing task
	@RequestMapping(value = "/getdetailsfortask", method = RequestMethod.POST)
	public @ResponseBody String getDetailsForTask(@RequestBody String jsonString, HttpSession session) {
		try {
			return tasksService.getTaskDetailsByClientTaskId(jsonString, session);
		} catch (Exception e) {
			return null;
		}
	}

	// Method Created By: Mahesh Kharote(25/03/2016)
	// Method Purpose: Download Proof of Compliance
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/downloadProofOfCompliance", method = RequestMethod.GET)
	public void downloadProofOfCompliance(int udoc_id, HttpServletResponse response) throws Throwable {
		try {
			JSONObject jsonString = new JSONObject();
			jsonString.put("udoc_id", udoc_id);
			tasksconfigurationservice.downloadProofOfCompliance(jsonString.toJSONString(), response);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	// Method Created By: Mahesh Kharote(25/02/2016)
	// Method Purpose: Save task completion page for completing task
	@Bean(name = "multipartResolver")
	public MultipartResolver multipartResolver() {
		System.out.println("In multipart resolver");
		MultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		return commonsMultipartResolver;
	}

	// Method Created By: Harshad Padole(06/04/2017)
	// Method Purpose: search task for initiation
	@RequestMapping(value = "/getdefaulttaskforinitiation", method = RequestMethod.POST)
	public @ResponseBody String getdefaulttaskforinitiation(@RequestBody String jsonString, HttpSession session) {
		try {
			return tasksRepositoryService.getClientTaskIdFromDefaultConfiguartion(jsonString);
		} catch (Exception e) {
			return null;
		}
	}

	// Method Created By: Harshad Padole
	// Method Purpose: getTask for export
	@RequestMapping(value = "/getalltasksforexport", method = RequestMethod.POST)
	public @ResponseBody String getAllTasksForExport(@RequestBody String jsonString, HttpSession session) {
		try {
			return tasksRepositoryService.getTaskForExport(jsonString, session);
		} catch (Exception e) {
			return null;
		}
	}

	// Method created : Harshad Padole
	// Method Purpose : get multiple task for completion where same task assigned to
	// multiple location with same performer/executor
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gettaskformultiplecompletion", method = RequestMethod.POST)
	public @ResponseBody String getTaskForMultipleCompletion(@RequestBody String jsonString, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		try {
			return tasksService.getMultipleTaskForCompletion(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("responce", "failed");
			return jsonString;
		}

	}

	// Method Created By: Harshad Padole
	// Method Purpose: get all documents for download
	@RequestMapping(value = "/getalldocuments", method = RequestMethod.POST)
	public @ResponseBody String getAllDocumentsForRepository(@RequestBody String jsonString, HttpSession session) {
		try {
			return tasksRepositoryService.getAllDocumentForRepository(jsonString, session);
		} catch (Exception e) {
			return null;
		}
	}

	// Method Created By: Sharad Rindhe(16/05/2019)
	// Method Purpose: upcomming todo
	@RequestMapping(value = "/listofupcommingtask", method = RequestMethod.GET)
	public @ResponseBody String listOfUpcommingTask(HttpSession session) {
		try {
			return tasksRepositoryService.listOfUpcommingTask(session);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/waitingforapprovaltasks", method = RequestMethod.POST)
	public @ResponseBody String listWaitingForApprovalTasksTask(@RequestBody String jsonString, HttpSession session) {
		try {
			return tasksRepositoryService.listWaitingForApprovalTasksTask(jsonString, session);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/reopenedtasks", method = RequestMethod.POST)
	public @ResponseBody String reopenedtasks(@RequestBody String jsonString, HttpSession session) {
		try {
			return tasksRepositoryService.reopenedTask(jsonString, session);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/searchrepository", method = RequestMethod.POST)
	public @ResponseBody String searchRepository(@RequestParam("data") String jsonString, HttpSession session) {
		try {
			System.out.println("jsonString : " + jsonString);
			return tasksRepositoryService.searchRepository(jsonString, session);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/getalltasksforrepository", method = RequestMethod.POST)
	public @ResponseBody String getAllTasksForRepository(@RequestBody String jsonString, HttpSession session) {
		try {
			return tasksRepositoryService.getAllTasksForRepository(jsonString, session);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/scrollrepository", method = RequestMethod.POST)
	public @ResponseBody String getRepositoryOnScroll(@RequestParam("data") String jsonString, HttpSession session) {
		try {
			System.out.println("jsonString scrollrepository : " + jsonString);
			return tasksRepositoryService.searchRepository(jsonString, session);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/paginationEx/get", method = RequestMethod.GET)
	public @ResponseBody String paginationExample(@RequestParam("data") String jsonString, HttpSession session) {
		try {
			System.out.println("URL paginationEx/get called");
			System.out.println("jsonString : " + jsonString);
			return tasksRepositoryService.searchRepository(jsonString, session);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/getalltasks", method = RequestMethod.POST)
	public @ResponseBody String getalltasks(@RequestBody String jsonString, HttpSession session) {
		try {
			System.out.println("URL getalltasks : " + jsonString);
			return tasksRepositoryService.getalltasks(jsonString, session);
		} catch (Exception e) {
			return null;
		}
	}

	// New Repositriy Page Code

	@RequestMapping(value = "/getCategory", method = RequestMethod.GET)
	public @ResponseBody String getCategory(HttpSession session) {
		try {
			return tasksRepositoryService.getCategoryList(session);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/getTypeOfTask", method = RequestMethod.GET)
	public @ResponseBody String getTypeOfTask(HttpSession session) {
		try {
			return tasksRepositoryService.getTypeOfTask(session);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/getFrequencyList", method = RequestMethod.GET)
	public @ResponseBody String getFrequencyList(HttpSession session) {
		try {
			return tasksRepositoryService.getFrequencyList(session);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/checkDocumentBeforeDownload", method = RequestMethod.POST)
	public @ResponseBody String checkDocumentBeforeDownload(@RequestBody String jsonString,
			HttpServletResponse response) throws Throwable {
		try {
			/*
			 * JSONObject jsonString = new JSONObject(); jsonString.put("udoc_id", udoc_id);
			 */
			System.out.println("jsonString:" + jsonString);
			return tasksconfigurationservice.checkDocumentBeforeDownload(jsonString, response);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
}
