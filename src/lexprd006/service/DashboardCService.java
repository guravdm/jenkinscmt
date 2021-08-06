package lexprd006.service;

import javax.servlet.http.HttpSession;

public interface DashboardCService {

	String complianceDashboardCount(String jsonString, HttpSession session);

	String searchComplianceDashboardCount(String jsonString, HttpSession session);

}
