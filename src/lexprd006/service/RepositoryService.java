package lexprd006.service;

import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

public interface RepositoryService {

	public String getExportDataById(String jsonString, HttpSession session, HttpServletResponse response);

	public String updateTasksCompletion(ArrayList<MultipartFile> ttrn_proof_of_compliance, String jsonString,
			HttpSession session);

}
