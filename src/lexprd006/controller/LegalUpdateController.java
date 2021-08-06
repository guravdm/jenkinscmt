package lexprd006.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import lexprd006.service.LegalUpdateService;

@Controller
@RequestMapping("/*")
public class LegalUpdateController {

	@Autowired
	LegalUpdateService legalUpdateService;

	@RequestMapping(value = "/importlegalupdates", method = RequestMethod.POST)
	public @ResponseBody String importLegalUpdates(@RequestBody String jsonString) {
		try {
			return legalUpdateService.uploadlegalUpdates(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
