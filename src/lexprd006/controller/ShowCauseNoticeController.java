package lexprd006.controller;

import java.util.ArrayList;

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

import lexprd006.service.ShowCauseNoticeService;
import lexprd006.service.UserEntityMappingService;

/*
 * Author: Harshad Padole
 * Date: 09/05/2017
 * 
 * */
@Controller
@RequestMapping("/*")
public class ShowCauseNoticeController {

	@Autowired
	ShowCauseNoticeService showCauseNoticeService;

	@Autowired
	UserEntityMappingService userEntityMappingService;

	// Method Written By: Harshad Padole(09-05-2017)
	// Method Purpose: Save show cause Notice
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveShowCauseNotice", method = RequestMethod.POST)
	public @ResponseBody String saveShowCauseNotice(@RequestParam("jsonString") String json,
			@RequestParam("show_cause_doc") ArrayList<MultipartFile> show_cause_doc, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		try {
			String res = showCauseNoticeService.saveShowCauseNotice(json, show_cause_doc, session);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			dataForSend.put("responseMessage", e.getMessage());
			return dataForSend.toJSONString();
		}

	}

	// Method Written By: Harshad Padole(09-05-2017)
	// Method Purpose: Get User access wise organization,location and department
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getAccessWiseOrgaLocaDept", method = RequestMethod.POST)
	public @ResponseBody String getAccessWiseOrgaLocaDept(@RequestBody String json, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		try {
			return showCauseNoticeService.accessWiseDataFilters(json, session);
		} catch (Exception e) {
			e.printStackTrace();
			dataForSend.put("responseMessage", e.getMessage());
			return dataForSend.toJSONString();
		}

	}

	// Method Written By: Harshad Padole(09-05-2017)
	// Method Purpose: Get User access wise organization,location and department
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getAllShowCauseNotice", method = RequestMethod.POST)
	public @ResponseBody String getAllShowCauseNotice(@RequestBody String json, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		try {
			return showCauseNoticeService.getAllShowCauseNotice(json, session);
		} catch (Exception e) {
			e.printStackTrace();
			dataForSend.put("responseMessage", e.getMessage());
			return dataForSend.toJSONString();
		}

	}

	// Method Written By: Harshad Padole(10-05-2017)
	// Method Purpose: Save show cause Notice - action item
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveActionItem", method = RequestMethod.POST)
	public @ResponseBody String saveActionItem(@RequestParam("jsonString") String json,
			@RequestParam("action_document") ArrayList<MultipartFile> action_document, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		try {
			String res = showCauseNoticeService.saveActionItem(json, action_document, session);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			dataForSend.put("responseMessage", e.getMessage());
			return dataForSend.toJSONString();
		}

	}

	// Method Written By: Harshad Padole(10-05-2017)
	// Method Purpose: Save show cause Notice - action item
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getAllActionItem", method = RequestMethod.POST)
	public @ResponseBody String getAllActionItem(@RequestBody String json, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		try {
			String res = showCauseNoticeService.getAllActionItem(json, session);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			dataForSend.put("responseMessage", e.getMessage());
			return dataForSend.toJSONString();
		}

	}

	// Method Written By: Harshad Padole(10-05-2017)
	// Method Purpose: Get show cause notice details by id
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getShowCauseNoticeDetails", method = RequestMethod.POST)
	public @ResponseBody String getShowCauseNoticeDetails(@RequestBody String json, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		try {
			String res = showCauseNoticeService.getShowCauseNoticeDetails(json, session);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			dataForSend.put("responseMessage", e.getMessage());
			return dataForSend.toJSONString();
		}

	}

	// Method Created By: Harshad Padole
	// Method Purpose: Download document
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/downloadShowCauseDocument", method = RequestMethod.GET)
	public void downloadProofOfCompliance(int doc_id, HttpServletResponse response) throws Throwable {
		try {
			JSONObject jsonString = new JSONObject();
			jsonString.put("doc_id", doc_id);
			showCauseNoticeService.downloadShowCauseDocument(jsonString.toJSONString(), response);
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	// Method Written By: Harshad Padole(09-05-2017)
	// Method Purpose: Update show cause Notice
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateShowCauseNotice", method = RequestMethod.POST)
	public @ResponseBody String updateShowCauseNotice(@RequestParam("jsonString") String json,
			@RequestParam("show_cause_doc") ArrayList<MultipartFile> show_cause_doc, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		try {
			String res = showCauseNoticeService.updateShowCauseNotice(json, show_cause_doc, session);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			dataForSend.put("responseMessage", e.getMessage());
			return dataForSend.toJSONString();
		}

	}

}
