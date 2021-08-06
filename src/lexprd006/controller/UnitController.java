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

import lexprd006.service.UnitService;

/*
 * Author: Mahesh Kharote
 * Date: 25/10/2016
 * Purpose: Controller for Functions 
 * */



@Controller
@RequestMapping("/*")
public class UnitController {

	@Autowired
	UnitService unitService;

	@Autowired
	HttpServletRequest request;
	//Method Written By: Mahesh Kharote(25/10/2016)
	//Method Purpose: Get Function by Id for Edit Function rest Call
	@RequestMapping(value = "/listunits" , method = RequestMethod.GET)
	public @ResponseBody String listUnits(HttpSession session, HttpServletResponse res){
		try {
			
			String requestFromMethod = "listUnits";
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
			return unitService.listUnits(requestFromMethod);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//Method Purpose: Get Function by Id for Edit Function rest Call
		@RequestMapping(value = "/listallunits" , method = RequestMethod.GET)
		public @ResponseBody String listAllUnits(){
			try {
				String requestFromMethod = "listAllUnits";
				return unitService.listUnits(requestFromMethod);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

	//Method Written By: Mahesh Kharote(25/10/2016)
	//Method Purpose: Get Function by Id for Edit Function rest Call
	@RequestMapping(value = "/saveunit" , method = RequestMethod.POST)
	public @ResponseBody String saveUnit(@RequestBody String jsonString,HttpSession session, HttpServletResponse res){
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
			return unitService.saveUnit(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}



	//Method Written By: Mahesh Kharote(25/10/2016)
	//Method Purpose: Get Function by Id for Edit Function rest Call
	@RequestMapping(value = "/updateunit" , method = RequestMethod.POST)
	public @ResponseBody String updateUnit(@RequestBody String jsonString, HttpSession session,HttpServletResponse res){
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
			return unitService.updateUnit(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//Method Purpose : Verify if Location Name is exist or not
		@RequestMapping(value="/isLocaNameExist" , method = RequestMethod.POST)
		public @ResponseBody String isLocaNameExist(@RequestBody String jsonString) {
			try {
				System.out.println("In is Loca Name Exist");
				return unitService.isUnitNameExist(jsonString);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}
		
		@RequestMapping(value = "/importunit", method = RequestMethod.POST)
		public @ResponseBody String importLegalUpdates(@RequestParam("unit_update_list") MultipartFile unit_update_list,
				@RequestParam("jsonString") String jsonString, HttpSession session) {
			try {
				System.out.println("No of documents attached:" + unit_update_list.getOriginalFilename());
				return unitService.importUnitList(unit_update_list, jsonString, session);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

}
