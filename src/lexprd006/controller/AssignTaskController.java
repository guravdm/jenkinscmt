package lexprd006.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lexprd006.service.AssignTaskService;

@Controller
@RequestMapping("/*")
public class AssignTaskController {

	@Autowired
	AssignTaskService assignTaskService;

	@RequestMapping(value = "/getentitylist", method = RequestMethod.GET)
	public @ResponseBody String getentity(HttpSession session) {
		try {
			return assignTaskService.getEntityList(session);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/getunitlist", method = RequestMethod.GET)
	public @ResponseBody String getunit(@RequestParam("entity_id") String entity_id, HttpSession session) {
		try {
			return assignTaskService.getUnitList(entity_id, session);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/getFunctionlist", method = RequestMethod.POST)
	public @ResponseBody String getFunction(@RequestBody String jsonString, HttpSession session) {
		try {

			return assignTaskService.getFunctionList(jsonString, session);
		} catch (Exception e) {
			return null;
		}

	}

	@RequestMapping(value = "/getExecutorList", method = RequestMethod.POST)
	public @ResponseBody String getExecutorList(@RequestBody String jsonString) {
		try {
			return assignTaskService.getExecutorList(jsonString);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/getEvaluatorList", method = RequestMethod.POST)
	public @ResponseBody String getEvaluatorList(@RequestBody String json) {
		try {
			return assignTaskService.getEvaluatorList(json);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/getFunHeadList", method = RequestMethod.POST)
	public @ResponseBody String getFunHeadList(@RequestBody String json) {
		try {
			return assignTaskService.getFunHeadList(json);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/getTaskListToAssign", method = RequestMethod.POST)
	public @ResponseBody String getTaskListToAssign(@RequestBody String jsonString, HttpSession session) {
		try {
			return assignTaskService.getTaskListToAssign(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/getExeListForChangeOwner", method = RequestMethod.POST)
	public @ResponseBody String getExeListForChangeOwner(@RequestBody String jsonString) {
		try {
			System.out.println("jsonString : " + jsonString.toString());
			return assignTaskService.getExeListForChangeOwner(jsonString);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/getEvalListForChangeOwner", method = RequestMethod.POST)
	public @ResponseBody String getEvalListForChangeOwner(@RequestBody String jsonString) {
		try {
			return assignTaskService.getEvalListForChangeOwner(jsonString);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/getFunHeadListForChangeOwner", method = RequestMethod.POST)
	public @ResponseBody String getFunHeadListForChangeOwner(@RequestBody String json) {
		try {
			return assignTaskService.getFunHeadListForChangeOwner(json);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/searchcomplianceownerpage", method = RequestMethod.POST)
	public @ResponseBody String searchcomplianceownerpage(@RequestParam("data") String jsonString,
			HttpSession session) {
		try {
			System.out.println("jsonString : " + jsonString);
			return assignTaskService.searchcomplianceownerpage(jsonString, session);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/uploadAssignTaskList", method = RequestMethod.POST)
	public @ResponseBody String uploadAssignTaskList(@RequestParam("assign_task_list") MultipartFile assign_task_list,
			@RequestParam("jsonString") String jsonString, HttpSession session) {
		try {
			System.out.println("No of documents attached:" + assign_task_list.getOriginalFilename());
			return assignTaskService.uploadAssignTaskList(assign_task_list, jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/searchActivationPage", method = RequestMethod.POST)
	public @ResponseBody String searchActivationPage(@RequestBody String jsonString, HttpSession session) {
		try {
			System.out.println("jsonString : " + jsonString);
			return assignTaskService.searchActivationPage(jsonString, session);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/getExeListForActivationPage", method = RequestMethod.POST)
	public @ResponseBody String getExeListForActivationPage(@RequestBody String jsonString) {
		try {
			return assignTaskService.getExeListForActivationPage(jsonString);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/getEvalListForActivationPage", method = RequestMethod.POST)
	public @ResponseBody String getEvalListForActivationPageURL(@RequestBody String jsonString) {
		try {
			return assignTaskService.getEvalListForActivationPageURL(jsonString);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/searchEnableDisablePage", method = RequestMethod.POST)
	public @ResponseBody String searchEnableDisablePage(@RequestBody String jsonString, HttpSession session) {
		try {
			System.out.println("jsonString : " + jsonString);
			return assignTaskService.searchEnableDisablePage(jsonString, session);
		} catch (Exception e) {
			return null;
		}
	}
}
