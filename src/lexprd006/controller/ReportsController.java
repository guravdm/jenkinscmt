package lexprd006.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import lexprd006.service.ReportsService;

@Controller
@RequestMapping("/*")
public class ReportsController {

	private @Value("#{config['project_name'] ?: 'null'}") String projectName;

	@Autowired
	ReportsService reportsService;

	// Method Created By: Mahesh Kharote(25/02/2016)
	// Method Purpose: Save task completion page for completing task
	@RequestMapping(value = "/fetchreports", method = RequestMethod.POST)
	public @ResponseBody String fetchReports(@RequestBody String jsonString, HttpSession session) {
		try {
			return reportsService.generateReports(jsonString, session);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/getExportReportById", method = RequestMethod.POST)
	public @ResponseBody String getExportReportById(@RequestBody String jsonString, HttpSession session) {
		try {
			return reportsService.getExportReportById(jsonString, session);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/getExportReport", method = RequestMethod.GET)
	public String getExportData(String jsonString, HttpSession session, HttpServletResponse response) {
		try {
			String path = "C:" + File.separator + "CMS" + File.separator + projectName + File.separator
					+ "Compliance_Report";

			File file = new File(getLatestFilefromDir(path).toString());
			InputStream is = new FileInputStream(file);
			System.out.println(file.getName());
			response.setContentType("application/octet-stream");

			response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

			OutputStream os = response.getOutputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
			os.flush();
			os.close();
			is.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private File getLatestFilefromDir(String dirPath) {
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files == null || files.length == 0) {
			return null;
		}

		File lastModifiedFile = files[0];
		for (int i = 1; i < files.length; i++) {
			if (lastModifiedFile.lastModified() < files[i].lastModified()) {
				lastModifiedFile = files[i];
			}
		}
		return lastModifiedFile;
	}
}
