package lexprd006.dao;

import java.util.List;

import javax.servlet.http.HttpSession;

import lexprd006.domain.Department;
import lexprd006.domain.Location;
import lexprd006.domain.Organization;

public interface NewDashboardDAO {

	List<Object> getComplianceStatusData(String jsonString, HttpSession session, String fromDate, String toDate);

	List<Organization> getAllOrganization(HttpSession session);

	List<Object> getEntityChartRiskCount(int orga_id, int sess_user_id, HttpSession session, String fromDate,
			String toDate);

	List<Location> getAllLocations(HttpSession session);

	List<Department> getAllDepartments(HttpSession session);

	List<Object> getDepartmentChartRiskCount(int dept_id, int orgaId, int sess_user_id, HttpSession session,
			String fromDate, String toDate);

	List<Object> getLocationChartRiskCount(int loca_id, int sess_user_id, HttpSession session, int orgaId,
			String fromDate, String toDate);

	<T> List<T> getOverallDrilledData(String status, String entity, HttpSession session, String fromDate,
			String toDate);

	<T> List<T> getEntityDrilledData(String status, String entity, HttpSession session, String fromDate, String toDate);

	<T> List<T> getUnitLevelDrilledData(String status, String entity, Integer locaId, HttpSession session,
			String fromDate, String toDate);

	<T> List<T> getLocIdByUnitName(String unit);

	<T> List<T> getOrgaIdByDeptName(String depatmentName, HttpSession session);

	<T> List<T> getFunctionIdByName(String department);

	<T> List<T> getFunctionLevelDrilledData(String status, String orgaId, Integer unitId, HttpSession session,
			String fromDate, String toDate);

	<T> List<T> getAllUnitsByAccess(int userSesId, String orgaId, HttpSession session);

	<T> List<T> getFunctionDataByOrgaIdDeptId(String orgaId, HttpSession session);

	<T> List<T> funDataForGraph(String deptId, String orgaId, HttpSession session, String fromDate, String toDate);

	<T> List<T> filterDataByOrgaIdAndUnitIdURL(String deptId, String orgaId, Integer unitId, HttpSession session, String fromDate, String toDate);

	List<Department> getAllDepartmentsByOrgaId(HttpSession session, String orgaId);

}
