package lexprd006.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import lexprd006.service.DesignationService;

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
public class DesignationController {

	@Autowired
	DesignationService designationService;

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all Functions rest Call
	@RequestMapping(value = "/listdesignations", method = RequestMethod.GET)
	public @ResponseBody String listDesignations() {
		try {
			return designationService.listDesignations();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/savedesignations", method = RequestMethod.POST)
	public @ResponseBody String savedesignations(@RequestBody String jsonString) {
		try {
			return designationService.saveDesignation(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/editdesignations", method = RequestMethod.POST)
	public @ResponseBody String editdesignations(@RequestBody String jsonString) {
		try {
			return designationService.editDesignation(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/updatedesignation", method = RequestMethod.POST)
	public @ResponseBody String updateFunction(@RequestBody String jsonString) {
		try {
			return designationService.updateDesignation(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/deletedesignation", method = RequestMethod.POST)
	public @ResponseBody String deletedesignation(@RequestBody String jsonString) {
		try {
			return designationService.deleteDesignation(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/isDesiNameExist", method = RequestMethod.POST)
	public @ResponseBody String isDesiNameExist(@RequestBody String jsonString) {
		try {
			System.out.println("In is Dept Name Exist");
			return designationService.isDesignationNameExist(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
