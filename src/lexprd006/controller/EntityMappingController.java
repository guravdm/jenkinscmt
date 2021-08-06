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

import lexprd006.service.EntityMappingService;
/*
 * Author: Mahesh Kharote
 * Date: 19/12/2016
 * Purpose: Controller for Entity Mapping
 * 
 * 
 * 
 * */

@Controller
@RequestMapping("/*")
public class EntityMappingController {

	@Autowired
	EntityMappingService entityMappingService;
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	HttpSession session;

	//Method Written By: Mahesh Kharote(19/12/2016)
	//Method Purpose: Get all Functions rest Call
	@RequestMapping(value = "/listentitiesmapping", method = RequestMethod.GET)
	public @ResponseBody String listEntitiesMapping(HttpSession session,HttpServletResponse res){
		try {
			String URL = request.getRequestURI();
			//System.out.println("URL FROM : "+URL);
			session.setAttribute("url", URL);
			
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
			
			return entityMappingService.listEntityMappings();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}


	//Method Written By: Mahesh Kharote(19/12/2016)
	//Method Purpose: Get all Functions rest Call
	@RequestMapping(value = "/saveentitiesmapping" , method = RequestMethod.POST)
	public @ResponseBody String saveEntitiesMapping(@RequestBody String jsonString, HttpServletResponse res){
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
			return entityMappingService.saveEntityMapping(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//Method Created : Mahesh Kharote
	//Method purpose : Fetch list of departments which are mapped with organization and location 
	@RequestMapping(value = "/getMappedDepartments" , method = RequestMethod.POST)
	public @ResponseBody String getAllDepartments(@RequestBody String jsonString){
		try {
			return entityMappingService.getMappedDepartments(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	//Method Created : Mahesh Kharote
	//Method purpose : Fetch list of departments which are mapped with organization and location 
	@RequestMapping(value = "/getAllMapping" , method = RequestMethod.POST)
	public @ResponseBody String getAllMapping(@RequestBody String jsonString){
		try {
			return entityMappingService.getAllMappings(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Method purpose : Fetch list of departments , organization and location on report page
		@RequestMapping(value = "/getAllMappingReport" , method = RequestMethod.POST)
		public @ResponseBody String getAllMappingReport(@RequestBody String jsonString,HttpSession session){
			try {
				return entityMappingService.getAllMappingsReport(jsonString , session);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@RequestMapping(value = "/importentitymappingfromfile" , method = RequestMethod.POST)
		public @ResponseBody String importEntityMapping(@RequestParam("entity_mapping_list") MultipartFile entity_mapping_list, @RequestParam("jsonString") String jsonString, HttpSession session){
			try {
				System.out.println("No of documents attached:"+entity_mapping_list.getOriginalFilename());
				return entityMappingService.importEntityMapping(entity_mapping_list,jsonString,session);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
}
