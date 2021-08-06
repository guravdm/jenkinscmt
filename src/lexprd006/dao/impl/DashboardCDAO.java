package lexprd006.dao.impl;

import java.util.List;

public interface DashboardCDAO {

	<T> List<T> complianceDashboardCount(int user_id, int user_role_id, String jsonString);

	<T> List<T> searchComplianceDashboardCount(int user_id, int user_role_id, String fromDate, String toDate,
			String orgaId);

}
