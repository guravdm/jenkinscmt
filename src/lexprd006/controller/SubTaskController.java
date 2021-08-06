package lexprd006.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lexprd006.service.SubTaskService;

@RequestMapping("/*")
@Controller
public class SubTaskController {

	@Autowired
	SubTaskService subtaskservice;
	
	//Method Created By: Harshad Padole
	//Method Purpose: get user defined task 
	@RequestMapping(value="/getUserDefinedTask", method = RequestMethod.POST)
	public @ResponseBody String getUserDefinedTask(@RequestBody String json, HttpSession session){
		try {
			return subtaskservice.getUserDefinedTask(session);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//Method Created By: Harshad Padole
	//Method Purpose: import sub task against client task id
	@RequestMapping(value="/importsubtask", method = RequestMethod.POST)
	public @ResponseBody String importsubtask(@RequestParam("json") String json,@RequestParam("subTaskFile") MultipartFile file, HttpSession session){
		try {
			return subtaskservice.uploadSubTask(json, file, session);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//Method Created By: Harshad Padole
	//Method Purpose: get imported sub task
	@RequestMapping(value="/getimportedsubtask", method = RequestMethod.POST)
	public @ResponseBody String getimportedsubtask(@RequestBody String json, HttpSession session){
		try {
			return subtaskservice.importedSubTask(json, session);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Method Written By: Harshad Padole
	//Method Purpose: get Sub Tasks for Configuration 
	@RequestMapping(value = "/getsubtasksforconfiguration" , method = RequestMethod.POST) 
	public @ResponseBody String getsubtasksforconfiguration(@RequestBody String json , HttpSession session){
		try {
			return subtaskservice.getTaskForConfiguration(json, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//Method Written By: Harshad Padole
	//Method Purpose: Save Sub Tasks Configuration 
	@RequestMapping(value = "/savesubtasksconfiguration" , method = RequestMethod.POST) 
	public @ResponseBody String savesubtasksconfiguration(@RequestBody String json , HttpSession session){
		try {
			return subtaskservice.saveSubtTaskConfiguration(json, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//Method Written By: Harshad Padole
	//Method Purpose: Get configured task
	@RequestMapping(value = "/getconfiguredsubtask" , method = RequestMethod.POST) 
	public @ResponseBody String getconfiguredsubtask(@RequestBody String json , HttpSession session){
		try {
			return subtaskservice.getConfiguredTask(json, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//Method Written By: Harshad Padole
	//Method Purpose: Activate or deactivate task
	@RequestMapping(value = "/updateStatus" , method = RequestMethod.POST) 
	public @ResponseBody String updateStatus(@RequestBody String json , HttpSession session){
		try {
			return subtaskservice.activateDeactivateSubTask(json, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/updateSubTasksConfigurationDates", method = RequestMethod.POST)
	public @ResponseBody String updateSubTasksConfigurationDates(@RequestBody String jsonString, HttpSession session) {
		try {
			return subtaskservice.updateSubTasksConfigurationDates(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/approveSubTask", method = RequestMethod.POST)
	public @ResponseBody String approveSubTask(@RequestBody String json, HttpSession session) {
		try {
			System.out.println("json:" +json);
			return subtaskservice.approveSubTask(json, session);
		} catch (Exception e) {
			return null;
		}
		
	}
	
	@RequestMapping(value = "/reopenSubTask", method = RequestMethod.POST)
	public @ResponseBody String reopenSubTask(@RequestBody String json, HttpSession session) {
		try {
			return subtaskservice.reOpenSubTask(json, session);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/updateSubTasksConfiguration", method = RequestMethod.POST)
	public @ResponseBody String updatetasksconfiguration(@RequestBody String jsonString, HttpSession session) {
		try {
			return subtaskservice.updateSubTasksConfiguration(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/downloadSubtaskDocument", method = RequestMethod.GET)
	public void downloadSubtaskDocument(int udoc_sub_id, HttpServletResponse response) throws Throwable {
		try {
			JSONObject jsonString = new JSONObject();
			jsonString.put("udoc_sub_id", udoc_sub_id);
			subtaskservice.downloadSubtaskDocument(jsonString.toJSONString(), response);
		} catch (Exception e) {
			e.printStackTrace();

		}

	}
	
	@RequestMapping(value = "/deleteSubTaskDocument", method = RequestMethod.POST)
	public @ResponseBody String deleteSubTaskDocument(@RequestBody String json, HttpSession session) {
		try {
			return subtaskservice.deleteSubTaskDocument(json, session);
		} catch (Exception e) {
			return null;
		}
	}
	
	@RequestMapping(value = "/deleteSubTaskHistory", method = RequestMethod.POST)
	public @ResponseBody String deleteSubTaskHistory(@RequestBody String json, HttpSession session) {
		try {
			return subtaskservice.deleteSubTaskHistory(json, session);
		} catch (Exception e) {
			return null;
		}
	}
	
}
