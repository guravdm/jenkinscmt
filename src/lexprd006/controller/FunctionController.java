package lexprd006.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.multipart.MultipartFile;

import lexprd006.service.FunctionService;

/*
 * Author: Mahesh Kharote
 * Date: 25/10/2016
 * Purpose: Controller for Functions
 * 
 * 
 * 
 * */


@Controller
@RequestMapping("/*")
public class FunctionController {

	@Autowired
	FunctionService functionService;

	@Autowired
	HttpServletRequest request;

	//Method Written By: Mahesh Kharote(25/10/2016)
	//Method Purpose: Get all Functions rest Call
	@RequestMapping(value = "/listfunctions" , method = RequestMethod.GET)
	public @ResponseBody String listFunctions(HttpSession session, HttpServletResponse res){
		try {
			Integer sesRoleId = Integer.parseInt(session.getAttribute("sess_role_id").toString());
			System.out.println("sesRoleId : " + sesRoleId);
			JSONObject objForSend = new JSONObject();
			JSONArray dataForSend = new JSONArray();
			if (sesRoleId < 6) {
				int userId = 0;

				if (session.getAttribute("sess_user_id") != null) {
					userId = Integer.parseInt(session.getAttribute("sess_user_id").toString());
					// usersService.logOutEntry(userId);
					res.setHeader("Cache-Control", "no-cache");
					res.setHeader("Cache-Control", "no-store");
					res.setDateHeader("Expires", 0);
					res.setHeader("Pragma", "no-cache");
					session.invalidate();
					objForSend.put("responseMessage", "Failed");
					dataForSend.add(objForSend);
					return dataForSend.toJSONString();
				}
			}
			return functionService.listFunctions();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	//Method Written By: Mahesh Kharote(25/10/2016)
	//Method Purpose: Save Function rest Call
	@RequestMapping(value = "/savefunction" , method = RequestMethod.POST)
	public @ResponseBody String saveFunction(@RequestBody String jsonString,HttpSession session, HttpServletResponse res){
		try {
			System.out.println("USER ID FROM SESSION : " + session.getAttribute("userId"));
			String requestURI = request.getRequestURI();
			System.out.println("Called list entity  : " + requestURI);
			Integer sesRoleId = Integer.parseInt(session.getAttribute("sess_role_id").toString());
			System.out.println("sesRoleId : " + sesRoleId);
			JSONObject objForSend = new JSONObject();
			JSONArray dataForSend = new JSONArray();
			if (sesRoleId < 6) {
				int userId = 0;

				if (session.getAttribute("sess_user_id") != null) {
					userId = Integer.parseInt(session.getAttribute("sess_user_id").toString());
					// usersService.logOutEntry(userId);
					res.setHeader("Cache-Control", "no-cache");
					res.setHeader("Cache-Control", "no-store");
					res.setDateHeader("Expires", 0);
					res.setHeader("Pragma", "no-cache");
					session.invalidate();
					objForSend.put("responseMessage", "Failed");
					// dataForSend.add(objForSend);
					return objForSend.toJSONString();
				}
			}
			return functionService.saveFunction(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//Method Written By: Mahesh Kharote(25/10/2016)
	//Method Purpose: Get Function by Id for Edit Function rest Call
	@RequestMapping(value = "/editfunction" , method = RequestMethod.POST)
	public @ResponseBody String editFunction(@RequestBody String jsonString){
		try {
			return functionService.editFunction(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	//Method Written By: Mahesh Kharote(25/10/2016)
	//Method Purpose: Update Function rest Call
	@RequestMapping(value = "/updatefunction" , method = RequestMethod.POST)
	public @ResponseBody String updateFunction(@RequestBody String jsonString,HttpServletResponse res, HttpSession session){
		try {
			System.out.println("USER ID FROM SESSION : " + session.getAttribute("userId"));
			String requestURI = request.getRequestURI();
			System.out.println("Called list entity  : " + requestURI);
			Integer sesRoleId = Integer.parseInt(session.getAttribute("sess_role_id").toString());
			System.out.println("sesRoleId : " + sesRoleId);
			JSONObject objForSend = new JSONObject();
			JSONArray dataForSend = new JSONArray();
			if (sesRoleId < 6) {
				int userId = 0;

				if (session.getAttribute("sess_user_id") != null) {
					userId = Integer.parseInt(session.getAttribute("sess_user_id").toString());
					// usersService.logOutEntry(userId);
					res.setHeader("Cache-Control", "no-cache");
					res.setHeader("Cache-Control", "no-store");
					res.setDateHeader("Expires", 0);
					res.setHeader("Pragma", "no-cache");
					session.invalidate();
					objForSend.put("responseMessage", "Failed");
					// dataForSend.add(objForSend);
					return objForSend.toJSONString();
				}
			}
			return functionService.updateFunction(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	//Method Written By: Mahesh Kharote(25/10/2016)
	//Method Purpose: Delete Function rest Call
	@RequestMapping(value = "/deletefunction" , method = RequestMethod.POST)
	public @ResponseBody String deleteFunction(@RequestBody String jsonString){
		try {
			return functionService.deleteFunction(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//Method Created : Mahesh Kharote(12/11/2016)
	//Method Purpose : Verify if Department Name is exist or not
	@RequestMapping(value="/isDeptNameExist" , method = RequestMethod.POST)
	public @ResponseBody String isDeptNameExist(@RequestBody String jsonString) {
		try {
			return functionService.isFunctionNameExist(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	@RequestMapping(value = "/importfunction", method = RequestMethod.POST)
	public @ResponseBody String importLegalUpdates(@RequestParam("dept_update_list") MultipartFile dept_update_list,
			@RequestParam("jsonString") String jsonString, HttpSession session) {
		try {
			System.out.println("No of documents attached:" + dept_update_list.getOriginalFilename());
			return functionService.importFunctionList(dept_update_list, jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
