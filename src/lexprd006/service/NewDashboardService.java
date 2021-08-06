package lexprd006.service;

import javax.servlet.http.HttpSession;

public interface NewDashboardService {

	String getOverAllCount(String jsonString, HttpSession session);

	String getEntityRisksCount(String jsonString, HttpSession session);

	String getUnitRisksCount(String jsonString, HttpSession session);

	String getFunctionRisksCount(String jsonString, HttpSession session);

	String getFinancialRisksCount(String jsonString, HttpSession session);

	String getoveralldrilleddata(String jsonString, HttpSession session);

	String getEntityRisksDrilledData(String jsonString, HttpSession session);

	String getUnitRisksModalData(String jsonString, HttpSession session);

	String getFunctionRisksModalData(String jsonString, HttpSession session);

	String getLocationListByOrgaId(String jsonString, HttpSession session);

	String filterDataByOrgaIdAndUnitIdURL(String jsonString, HttpSession session);

	String approveAllTask(String jsonString, HttpSession session);

}
