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

import lexprd006.service.EntityService;

@Controller
@RequestMapping("/*")
public class EntityController {

	@Autowired
	EntityService entityService;

	@Autowired
	HttpServletRequest request;

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all Functions rest Call
	@RequestMapping(value = "/listentities", method = RequestMethod.GET)
	public @ResponseBody String listEntities(HttpSession session, HttpServletResponse res) {
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
					dataForSend.add(objForSend);
					return dataForSend.toJSONString();
				}
			}
			return entityService.listEntities(session, null);
			// return entityService.listEntities((int) session.getAttribute("userId"),
			// null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// List all entity from the database

	@RequestMapping(value = "/listallentities", method = RequestMethod.GET)
	public @ResponseBody String listAllEntities() {
		try {
			// System.out.println("USER ID FROM SESSION : " +
			// session.getAttribute("userId"));
			String requestURI = request.getRequestURI();

			System.out.println("Called list entity  : " + requestURI);

			String requestFromMethod = "listAllEntities";
			return entityService.listEntities(null, requestFromMethod);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all Entities rest Call
	@RequestMapping(value = "/listAllForAddingEntity", method = RequestMethod.GET)
	public @ResponseBody String listAllForAddingEntity() {
		try {
			return entityService.listAllForAddingEntity();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Save Entity rest Call
	@RequestMapping(value = "/saveentity", method = RequestMethod.POST)
	public @ResponseBody String saveEntity(@RequestBody String jsonString, HttpSession session, HttpServletResponse res) {
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
			return entityService.saveEntity(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Update Entity rest Call
	@RequestMapping(value = "/updateentity", method = RequestMethod.POST)
	public @ResponseBody String updateEntity(@RequestBody String jsonString, HttpSession session,
			HttpServletResponse res) {
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
			return entityService.updateEntity(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Created : Mahesh Kharote(12/11/2016)
	// Method Purpose : Verify if Department Name is exist or not
	@RequestMapping(value = "/isOrgaNameExist", method = RequestMethod.POST)
	public @ResponseBody String isOrgaNameExist(@RequestBody String jsonString) {
		try {
			return entityService.isEntityNameExist(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(value = "/importentity", method = RequestMethod.POST)
	public @ResponseBody String importLegalUpdates(@RequestParam("entity_update_list") MultipartFile entity_update_list,
			@RequestParam("jsonString") String jsonString, HttpSession session) {
		try {
			System.out.println("No of documents attached:" + entity_update_list.getOriginalFilename());
			return entityService.importEntityList(entity_update_list, jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
