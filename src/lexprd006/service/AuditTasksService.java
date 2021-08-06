package lexprd006.service;

import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

public interface AuditTasksService {

	String getMonthlyComplianceStatus(String jsonString, HttpSession session);

	String getHeadCountsByLocation(HttpSession session, HttpServletResponse res);

	String searchAuditRepository(String jsonString, HttpSession session, HttpServletResponse res);

	String auditTaskDashboard(String jsonString, HttpSession session);

	String approverCompliedTasksURL(String json, HttpSession session);

	String searhMonthlyComplianceStatus(String org_id, String loca_id, String dept_id, String date_from, String date_to, HttpSession session);

	String copyComplianceOwner(String jsonString, HttpSession session);

	String submitSimplyCompDocumentsURL(ArrayList<MultipartFile> ttrn_proof_of_compliance, String jsonString, HttpSession session);

	String listSimplyCompDocuments(String jsonString, HttpSession session);

	String downloadComplianceDocument(String jsonString, HttpServletResponse response);

	String makeNonCompliedTasks(String jsonString, HttpSession session);

	String verticalWiceReport(String jsonString, HttpSession session);

	String statusCompletionReport(String jsonString, HttpSession session);

	String searchExecutorList(String jsonString, HttpSession session);

	String monthlyComplianceAuditChartURL(String jsonString, HttpSession session);

	String searchAuditDashboard(String jsonString, HttpSession session);

	String getTaskActivityListURL(String jsonString, HttpSession session);

	String finalComplianceAuditReport(String jsonString, HttpSession session);

	String getFunctionListByOrgaId(String jsonString, HttpSession session);

}
