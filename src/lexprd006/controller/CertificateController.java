package lexprd006.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import lexprd006.service.CertificateService;

/*
 * Author: Harshad Padole
 * Date: 22/05/2017
 * Purpose: Controller for certificate
 * 
 * */

@Controller
@RequestMapping("/*")
public class CertificateController {
	@Autowired
	CertificateService certificateService;

	// Method Created By: Harshad Padole
	// Method Purpose: get certificate details
	@RequestMapping(value = "/generateCertificate", method = RequestMethod.POST)
	public @ResponseBody String genereateCertificate(@RequestBody String json, HttpSession session) {
		try {
			return certificateService.getQuerterlyCertificateDetails(json, session);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Swapnali Kumbhar
	// Method Purpose: get certificate details
	@RequestMapping(value = "/downloadCertificateDetails", method = RequestMethod.GET)
	public @ResponseBody String downloadCertificate(HttpSession session) {
		try {
			return certificateService.downloadCertificateDetails(session);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/download", method = RequestMethod.POST)
	public @ResponseBody String downloadCertificate(@RequestBody String certificate, HttpSession session,
			HttpServletResponse response) {
		try {
			return certificateService.downloadCertificate(certificate, session, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/downloadDocuments", method = RequestMethod.GET)
	public void downloadProofOfCompliance(String certificate, HttpServletResponse response) throws Throwable {
		try {
			System.out.println("certificate : " + certificate);
			System.out.println("downloadDocuments : API called");
			JSONObject jsonString = new JSONObject();
			jsonString.put("certificate", certificate);
			certificateService.downloadDocuments(jsonString.toJSONString(), response);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

}
