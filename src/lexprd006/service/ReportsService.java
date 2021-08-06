package lexprd006.service;

import javax.servlet.http.HttpSession;

public interface ReportsService {

	public String generateReports(String jsonString , HttpSession session);
	public String automatedMonthlyReports(String jsonString, int role_id);
	public String getExportReportById(String jsonString, HttpSession session);
}
