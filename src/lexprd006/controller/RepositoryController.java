package lexprd006.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

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

import lexprd006.service.RepositoryService;

@Controller
@RequestMapping(value = "/*")
public class RepositoryController {
	private @Value("#{config['project_name'] ?: 'null'}") String projectName;
	
	@Autowired
	RepositoryService repositoryService;
	
	@RequestMapping(value = "/getExportDataById", method = RequestMethod.POST)
	public @ResponseBody String getExportDataById(@RequestBody String jsonString, HttpSession session,
			HttpServletResponse response) {
		try {
			System.out.println("controller - export");
			return repositoryService.getExportDataById(jsonString, session, response);
		} catch (Exception e) {
			return null;
		}
	}
	@RequestMapping(value = "/getExportData", method = RequestMethod.GET)
	public String getExportData(String jsonString, HttpSession session, HttpServletResponse response) {
		try {			
			String path ="C:" + File.separator + "CMS" + File.separator + projectName + File.separator + "Dump";

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

	@RequestMapping(value = "/updateTasksCompletion" , method = RequestMethod.POST)
	public @ResponseBody String updateTasksCompletion(@RequestParam("ttrn_proof_of_compliance") ArrayList<MultipartFile> ttrn_proof_of_compliance , @RequestParam("jsonString") String jsonString , HttpSession session){
		System.out.println("No of documents attached: "+ttrn_proof_of_compliance.size());
		System.out.println(jsonString);
		try {
			return repositoryService.updateTasksCompletion(ttrn_proof_of_compliance, jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
