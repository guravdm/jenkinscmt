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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lexprd006.service.DashboardService;

@Controller
@RequestMapping("/*")
public class DashboardController {

	private @Value("#{config['project_name'] ?: 'null'}") String projectName;

	@Autowired
	DashboardService dashboardService;

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all Functions rest Call
	@RequestMapping(value = "/getoverallcompliancestatus", method = RequestMethod.POST)
	public @ResponseBody String getOverallComplianceStatus(@RequestBody String jsonString, HttpSession session) {
		try {
			System.out.println("controller 1");
			return dashboardService.getOverallComplianceGraph(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/searchgetoverallcompliancestatus", method = RequestMethod.POST)
	public @ResponseBody String searchOverallComplianceStatus(@RequestBody String jsonString, HttpSession session) {
		try {
			System.out.println(" jsonString : " + jsonString);
			return dashboardService.searchGetOverallComplianceGraph(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all Functions rest Call
	@RequestMapping(value = "/getentitywiseComplianceStatus", method = RequestMethod.POST)
	public @ResponseBody String getEntityWiseComplianceStatus(@RequestBody String jsonString, HttpSession session) {
		try {
			return dashboardService.getEntityComplianceGraph(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all Functions rest Call
	@RequestMapping(value = "/getunitwiseComplianceStatus", method = RequestMethod.POST)
	public @ResponseBody String getUnitWiseComplianceStatus(@RequestBody String jsonString, HttpSession session) {
		try {
			return dashboardService.getUnitComplianceGraph(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all Functions rest Call
	@RequestMapping(value = "/getfunctionwiseComplianceStatus", method = RequestMethod.POST)
	public @ResponseBody String getFunctionWiseComplianceStatus(@RequestBody String jsonString, HttpSession session) {
		try {
			return dashboardService.getFunctionComplianceGraph(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all Functions rest Call
	@RequestMapping(value = "/getgraphdrilldown", method = RequestMethod.POST)
	public @ResponseBody String getGraphDrillDown(@RequestBody String jsonString, HttpSession session) {
		try {
			System.out.println("controller 2");
			return dashboardService.getGraphDrillDown(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	

	@RequestMapping(value = "/searchGraph", method = RequestMethod.POST)
	public @ResponseBody String searchGraph(@RequestBody String jsonString, HttpSession session) {
		try {
			return dashboardService.searchGraph(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * @RequestMapping(value = "/getExportDrillReport", method = RequestMethod.GET)
	 * public void getExportDrillReport(String status, HttpSession session ) { try {
	 * System.out.println("controller - export" + status);
	 * dashboardService.getExportDrillReport(status, session); } catch (Exception e)
	 * { e.printStackTrace();
	 * 
	 * } }
	 */

	@RequestMapping(value = "/getExportDrillReport", method = RequestMethod.POST)
	public @ResponseBody String getExportDrillReport(@RequestBody String jsonString, HttpSession session) {
		try {

			// String status = jsonString;
			System.out.println(jsonString);
			return dashboardService.getExportDrillReport(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/downloadExportDrillReport", method = RequestMethod.GET)
	public void downloadExportDrillReport(HttpServletResponse response) {
		try {
			String path = "C:" + File.separator + "CMS" + File.separator + projectName + File.separator + "Report";

			File file = new File(getLatestFilefromDir(path).toString());

			InputStream is = new FileInputStream(file);

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

	@RequestMapping(value = "/approveAllTask", method = RequestMethod.POST)
	private @ResponseBody String approveAllTask(@RequestBody String jsonString, HttpSession session) {
		return dashboardService.approveAllTask(jsonString, session);
	}

	@RequestMapping(value = "/getSubTaskHistoryList", method = RequestMethod.POST)
	public @ResponseBody String getHistoryForTask(@RequestBody String jsonString, HttpSession session) {
		try {
			return dashboardService.getSubTaskHistoryList(jsonString, session);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/importTaskToComplete", method = RequestMethod.POST)
	public @ResponseBody String importTaskToComplete(@RequestParam("task_list") MultipartFile task_list,
			@RequestParam("jsonString") String jsonString, HttpSession session) {
		try {
			System.out.println("No of documents attached:" + task_list.getOriginalFilename());
			return dashboardService.importTaskToComplete(task_list, jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
