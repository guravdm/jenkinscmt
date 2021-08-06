package lexprd006.service;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

public interface DashboardService {
	
	public String getOverallComplianceGraph(String jsonString, HttpSession session);
	public String getEntityComplianceGraph(String jsonString , HttpSession session);
	public String getUnitComplianceGraph(String jsonString , HttpSession session);
	public String getFunctionComplianceGraph(String jsonString , HttpSession session);
	public String getGraphDrillDown(String jsonString, HttpSession session);
	public String searchGetOverallComplianceGraph(String jsonString, HttpSession session);
	public String searchGraph(String jsonString, HttpSession session);
	public String getExportDrillReport(String status, HttpSession session);
	public String approveAllTask(String jsonString, HttpSession session);
	public String getSubTaskHistoryList(String jsonString, HttpSession session);
	public String importTaskToComplete(MultipartFile task_list, String jsonString, HttpSession session);

}
