package lexprd006.dao.impl;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lexprd006.dao.NewDashboardDAO;
import lexprd006.domain.Department;
import lexprd006.domain.Location;
import lexprd006.domain.Organization;

@Transactional
@Repository
public class NewDashboardDAOImpl implements NewDashboardDAO {

	@PersistenceContext
	EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getComplianceStatusData(String jsonString, HttpSession session, String fromDate,
			String toDate) {

		Integer userId = Integer.parseInt(session.getAttribute("sess_user_id").toString());
		Integer roleId = Integer.parseInt(session.getAttribute("sess_role_id").toString());

		String sql = "";

		LocalDate localDate = LocalDate.now();

		LocalDate minusDays;
		LocalDate plusDays;

		if (!toDate.equalsIgnoreCase("0")) {
			minusDays = LocalDate.parse(fromDate);
			plusDays = LocalDate.parse(toDate);
		} else {
			minusDays = localDate.minusDays(50);
			plusDays = localDate.plusDays(50);
		}

		/**
		 * roleBased Count
		 */

		if (roleId > 3) {
			sql = "SELECT SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'NonComplied', "
					+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', "
					+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Complied', "
					+ "SUM(IF(DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND  DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Delayed',  "
					+ "SUM(IF(ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval', "
					+ "SUM(IF(ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', "
					+ "SUM(IF(DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d')) AND ttrn_status = 'Completed', 1, 0)) AS 'DelayedReported' "
					+ "FROM cfg_task_user_mapping tmap JOIN trn_task_transactional ON tmap_client_tasks_id = ttrn_client_task_id AND "
					+ "ttrn_client_task_id = tmap_client_tasks_id JOIN cfg_user_entity_mapping umap ON umap.umap_orga_id = tmap.tmap_orga_id AND "
					+ "umap.umap_loca_id = tmap.tmap_loca_id AND umap.umap_dept_id = tmap.tmap_dept_id AND umap.umap_user_id = '"
					+ userId + "' AND ttrn_legal_due_date BETWEEN '" + minusDays + "' AND '" + plusDays + "' ";
		} else {
			if (roleId == 3) {
				sql = "SELECT SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'NonComplied', "
						+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', "
						+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Complied', "
						+ "SUM(IF(DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND  DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Delayed',  "
						+ "SUM(IF(ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval', "
						+ "SUM(IF(ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', "
						+ "SUM(IF(DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d')) AND ttrn_status = 'Completed', 1, 0)) AS 'DelayedReported' "
						+ "FROM cfg_task_user_mapping JOIN trn_task_transactional ON tmap_client_tasks_id = ttrn_client_task_id AND ttrn_client_task_id = tmap_client_tasks_id AND (tmap_pr_user_id = '"
						+ userId + "' OR tmap_rw_user_id = '" + userId + "' OR tmap_fh_user_id = '" + userId
						+ "' ) AND ttrn_legal_due_date BETWEEN '" + minusDays + "' AND '" + plusDays + "' ";
			} else {
				if (roleId == 2) {
					sql = "SELECT SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'NonComplied', "
							+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', "
							+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Complied', "
							+ "SUM(IF(DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND  DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Delayed',  "
							+ "SUM(IF(ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval', "
							+ "SUM(IF(ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', "
							+ "SUM(IF(DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d')) AND ttrn_status = 'Completed', 1, 0)) AS 'DelayedReported' "
							+ "FROM cfg_task_user_mapping JOIN trn_task_transactional ON tmap_client_tasks_id = ttrn_client_task_id AND ttrn_client_task_id = tmap_client_tasks_id AND (tmap_pr_user_id = '"
							+ userId + "' OR tmap_rw_user_id = '" + userId + "') AND ttrn_legal_due_date BETWEEN '"
							+ minusDays + "' AND '" + plusDays + "' ";
				} else {
					if (roleId == 1) {
						sql = "SELECT SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'NonComplied', "
								+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', "
								+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Complied', "
								+ "SUM(IF(DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND  DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Delayed',  "
								+ "SUM(IF(ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval', "
								+ "SUM(IF(ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', "
								+ "SUM(IF(DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d')) AND ttrn_status = 'Completed', 1, 0)) AS 'DelayedReported' "
								+ "FROM cfg_task_user_mapping JOIN trn_task_transactional ON tmap_client_tasks_id = ttrn_client_task_id AND ttrn_client_task_id = tmap_client_tasks_id AND tmap_pr_user_id = '"
								+ userId + "' AND ttrn_legal_due_date BETWEEN '" + minusDays + "' AND '" + plusDays
								+ "'";
					}
				}
			}
		}
		Query createNativeQuery = entityManager.createNativeQuery(sql);
		// System.out.println("overall sql : " + sql);
		return createNativeQuery.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Organization> getAllOrganization(HttpSession session) {
		String sess_user_id = session.getAttribute("sess_user_id").toString();
		String sql = "SELECT DISTINCT org.* FROM mst_organization org, cfg_user_entity_mapping umap, cfg_task_user_mapping tmap WHERE umap.umap_user_id = '"
				+ sess_user_id + "' AND "
				+ "umap.umap_orga_id = org.orga_id AND umap.umap_orga_id = tmap.tmap_orga_id AND umap.umap_loca_id = tmap.tmap_loca_id AND "
				+ "umap.umap_dept_id = tmap.tmap_dept_id GROUP BY tmap.tmap_client_tasks_id";
		Query query = entityManager.createNativeQuery(sql, Organization.class);
		// query.setParameter(1, session.getAttribute("sess_user_id"));
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getEntityChartRiskCount(int orga_id, int sess_user_id, HttpSession session, String fromDate,
			String toDate) {

		Integer userId = Integer.parseInt(session.getAttribute("sess_user_id").toString());
		Integer roleId = Integer.parseInt(session.getAttribute("sess_role_id").toString());

		String sql = "";

		LocalDate localDate = LocalDate.now();

		LocalDate minusDays;
		LocalDate plusDays;

		if (!toDate.equalsIgnoreCase("0")) {
			minusDays = LocalDate.parse(fromDate);
			plusDays = LocalDate.parse(toDate);
		} else {
			minusDays = localDate.minusDays(50);
			plusDays = localDate.plusDays(50);
		}

		/**
		 * roleBased Count
		 */

		if (roleId > 3) {
			sql = "SELECT SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'NonComplied', "
					+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', "
					+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Complied', "
					+ "SUM(IF(DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND  DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Delayed',  "
					+ "SUM(IF(ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval', "
					+ "SUM(IF(ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', "
					+ "SUM(IF(DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d')) AND ttrn_status = 'Completed', 1, 0)) AS 'DelayedReported' "
					+ "FROM cfg_task_user_mapping tmap JOIN trn_task_transactional ON tmap_client_tasks_id = ttrn_client_task_id AND "
					+ "ttrn_client_task_id = tmap_client_tasks_id JOIN cfg_user_entity_mapping umap ON umap.umap_orga_id = tmap.tmap_orga_id AND "
					+ "umap.umap_loca_id = tmap.tmap_loca_id AND umap.umap_dept_id = tmap.tmap_dept_id AND umap.umap_user_id = '"
					+ userId + "' AND tmap_orga_id = '" + orga_id + "' AND ttrn_legal_due_date BETWEEN '" + minusDays
					+ "' AND '" + plusDays + "' ";
			// System.out.println("roleId > 3 " + sql);
		} else {
			if (roleId == 3) {
				sql = "SELECT SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'NonComplied', "
						+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', "
						+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Complied', "
						+ "SUM(IF(DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND  DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Delayed',  "
						+ "SUM(IF(ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval', "
						+ "SUM(IF(ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', "
						+ "SUM(IF(DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d')) AND ttrn_status = 'Completed', 1, 0)) AS 'DelayedReported' "
						+ "FROM cfg_task_user_mapping JOIN trn_task_transactional ON tmap_client_tasks_id = ttrn_client_task_id AND ttrn_client_task_id = tmap_client_tasks_id AND (tmap_pr_user_id = '"
						+ userId + "' OR tmap_rw_user_id = '" + userId + "' OR tmap_fh_user_id = '" + userId
						+ "' ) AND tmap_orga_id = '" + orga_id + "' AND  ttrn_legal_due_date BETWEEN '" + minusDays
						+ "' AND '" + plusDays + "' ";
				// System.out.println("roleId == 3 " + sql);
			} else {
				if (roleId == 2) {
					sql = "SELECT SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'NonComplied', "
							+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', "
							+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Complied', "
							+ "SUM(IF(DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND  DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Delayed',  "
							+ "SUM(IF(ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval', "
							+ "SUM(IF(ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', "
							+ "SUM(IF(DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d')) AND ttrn_status = 'Completed', 1, 0)) AS 'DelayedReported' "
							+ "FROM cfg_task_user_mapping JOIN trn_task_transactional ON tmap_client_tasks_id = ttrn_client_task_id AND ttrn_client_task_id = tmap_client_tasks_id AND (tmap_pr_user_id = '"
							+ userId + "' OR tmap_rw_user_id = '" + userId + "') AND tmap_orga_id = '" + orga_id
							+ "' AND  ttrn_legal_due_date BETWEEN '" + minusDays + "' AND '" + plusDays + "' ";
					// System.out.println("roleId == 2 " + sql);
				} else {
					if (roleId == 1) {
						sql = "SELECT SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'NonComplied', "
								+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', "
								+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Complied', "
								+ "SUM(IF(DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND  DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Delayed',  "
								+ "SUM(IF(ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval', "
								+ "SUM(IF(ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', "
								+ "SUM(IF(DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d')) AND ttrn_status = 'Completed', 1, 0)) AS 'DelayedReported' "
								+ "FROM cfg_task_user_mapping JOIN trn_task_transactional ON tmap_client_tasks_id = ttrn_client_task_id AND ttrn_client_task_id = tmap_client_tasks_id AND tmap_pr_user_id = '"
								+ userId + "' AND tmap_orga_id = '" + orga_id + "' AND ttrn_legal_due_date BETWEEN '"
								+ minusDays + "' AND '" + plusDays + "'";
						// System.out.println("roleId == 1 " + sql);
					}
				}
			}
		}

		Query createNativeQuery = entityManager.createNativeQuery(sql);
		// System.out.println("getEntityChartRiskCount sql : " + sql);
		return createNativeQuery.getResultList();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Location> getAllLocations(HttpSession session) {
		Integer userId = Integer.parseInt(session.getAttribute("sess_user_id").toString());
		try {
			String sql = "SELECT DISTINCT loc.* FROM mst_location loc, cfg_user_entity_mapping umap, cfg_task_user_mapping tmap WHERE umap.umap_user_id = '"
					+ userId
					+ "' AND umap.umap_loca_id = loc.loca_id AND umap.umap_orga_id = tmap.tmap_orga_id AND umap.umap_loca_id = tmap.tmap_loca_id AND "
					+ "umap.umap_dept_id = tmap.tmap_dept_id GROUP BY tmap.tmap_client_tasks_id";
			Query query = entityManager.createNativeQuery(sql, Location.class);
			// System.out.println("getAllLocations : " + sql);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getLocationChartRiskCount(int locaId, int sess_user_id, HttpSession session, int orgId,
			String fromDate, String toDate) {

		Integer userId = Integer.parseInt(session.getAttribute("sess_user_id").toString());
		Integer roleId = Integer.parseInt(session.getAttribute("sess_role_id").toString());

		String sql = "";

		LocalDate localDate = LocalDate.now();

		LocalDate minusDays;
		LocalDate plusDays;

		if (!toDate.equalsIgnoreCase("0")) {
			minusDays = LocalDate.parse(fromDate);
			plusDays = LocalDate.parse(toDate);
		} else {
			minusDays = localDate.minusDays(50);
			plusDays = localDate.plusDays(50);
		}

		/**
		 * roleBased Count
		 */

		String sqlQuery = "SELECT DISTINCT org.* FROM mst_organization org, cfg_user_entity_mapping umap, cfg_task_user_mapping tmap WHERE umap.umap_user_id = '"
				+ sess_user_id + "' AND "
				+ "umap.umap_orga_id = org.orga_id AND umap.umap_orga_id = tmap.tmap_orga_id AND umap.umap_loca_id = tmap.tmap_loca_id AND "
				+ "umap.umap_dept_id = tmap.tmap_dept_id GROUP BY tmap.tmap_client_tasks_id";

//		List<Object> createQuery = (List<Object>) entityManager.createNativeQuery(sqlQuery).getResultList();
//		Iterator<Object> i = createQuery.iterator();
//		while (i.hasNext()) {
//			Object[] next = (Object[]) i.next();
//			System.out.println("======= orgaId obj : " + next[0].toString());
//			int orga_id = Integer.parseInt(next[0].toString());
		// }

		if (roleId > 3) {
			sql = "SELECT SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'NonComplied', "
					+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', "
					+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Complied', "
					+ "SUM(IF(DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND  DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Delayed',  "
					+ "SUM(IF(ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval', "
					+ "SUM(IF(ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', "
					+ "SUM(IF(DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d')) AND ttrn_status = 'Completed', 1, 0)) AS 'DelayedReported' "
					+ "FROM cfg_task_user_mapping tmap JOIN trn_task_transactional ON tmap_client_tasks_id = ttrn_client_task_id AND "
					+ "ttrn_client_task_id = tmap_client_tasks_id JOIN cfg_user_entity_mapping umap ON umap.umap_orga_id = tmap.tmap_orga_id AND "
					+ "umap.umap_loca_id = tmap.tmap_loca_id AND umap.umap_dept_id = tmap.tmap_dept_id AND umap.umap_user_id = '"
					+ userId + "' AND tmap_loca_id = '" + locaId + "' AND ttrn_legal_due_date BETWEEN '" + minusDays
					+ "' AND '" + plusDays + "' ";
			// System.out.println("getLocationChartRiskCount roleId > 3 " + sql);
		} else {
			if (roleId == 3) {
				sql = "SELECT SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'NonComplied', "
						+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', "
						+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Complied', "
						+ "SUM(IF(DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND  DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Delayed',  "
						+ "SUM(IF(ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval', "
						+ "SUM(IF(ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', "
						+ "SUM(IF(DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d')) AND ttrn_status = 'Completed', 1, 0)) AS 'DelayedReported' "
						+ "FROM cfg_task_user_mapping JOIN trn_task_transactional ON tmap_client_tasks_id = ttrn_client_task_id AND ttrn_client_task_id = tmap_client_tasks_id AND (tmap_pr_user_id = '"
						+ userId + "' OR tmap_rw_user_id = '" + userId + "' OR tmap_fh_user_id = '" + userId
						+ "' ) AND tmap_loca_id = '" + locaId + "' AND  ttrn_legal_due_date BETWEEN '" + minusDays
						+ "' AND '" + plusDays + "' ";
				// System.out.println("getLocationChartRiskCount roleId == 3 " + sql);
			} else {
				if (roleId == 2) {
					sql = "SELECT SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'NonComplied', "
							+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', "
							+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Complied', "
							+ "SUM(IF(DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND  DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Delayed',  "
							+ "SUM(IF(ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval', "
							+ "SUM(IF(ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', "
							+ "SUM(IF(DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d')) AND ttrn_status = 'Completed', 1, 0)) AS 'DelayedReported' "
							+ "FROM cfg_task_user_mapping JOIN trn_task_transactional ON tmap_client_tasks_id = ttrn_client_task_id AND ttrn_client_task_id = tmap_client_tasks_id AND (tmap_pr_user_id = '"
							+ userId + "' OR tmap_rw_user_id = '" + userId + "') AND  tmap_loca_id = '" + locaId
							+ "' AND ttrn_legal_due_date BETWEEN '" + minusDays + "' AND '" + plusDays + "' ";
					// System.out.println("getLocationChartRiskCount roleId == 2 " + sql);
				} else {
					if (roleId == 1) {
						sql = "SELECT SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'NonComplied', "
								+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', "
								+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Complied', "
								+ "SUM(IF(DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND  DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Delayed',  "
								+ "SUM(IF(ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval', "
								+ "SUM(IF(ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', "
								+ "SUM(IF(DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d')) AND ttrn_status = 'Completed', 1, 0)) AS 'DelayedReported' "
								+ "FROM cfg_task_user_mapping JOIN trn_task_transactional ON tmap_client_tasks_id = ttrn_client_task_id AND ttrn_client_task_id = tmap_client_tasks_id AND tmap_pr_user_id = '"
								+ userId + "' AND tmap_loca_id = '" + locaId + "' AND ttrn_legal_due_date BETWEEN '"
								+ minusDays + "' AND '" + plusDays + "'";
						// System.out.println("getLocationChartRiskCount roleId == 1 " + sql);
					}
				}
			}
		}

		Query createUnitQuery = entityManager.createNativeQuery(sql);
		return createUnitQuery.getResultList();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Department> getAllDepartments(HttpSession session) {
		String userId = session.getAttribute("sess_user_id").toString();

		try {
			String sql = "SELECT DISTINCT dept.* FROM mst_department dept, cfg_user_entity_mapping umap, cfg_task_user_mapping tmap WHERE umap.umap_user_id = '"
					+ userId
					+ "' AND umap.umap_dept_id = dept.dept_id AND umap.umap_orga_id = tmap.tmap_orga_id AND umap.umap_loca_id = tmap.tmap_loca_id AND "
					+ "umap.umap_dept_id = tmap.tmap_dept_id GROUP BY tmap.tmap_client_tasks_id ";
			Query query = entityManager.createNativeQuery(sql, Department.class);
			// query.setParameter(1, session.getAttribute("sess_user_id"));
			// System.out.println("all depts : " + sql);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getDepartmentChartRiskCount(int dept_id, int orga_id, int sess_user_id, HttpSession session,
			String fromDate, String toDate) {

		Integer userId = Integer.parseInt(session.getAttribute("sess_user_id").toString());
		Integer roleId = Integer.parseInt(session.getAttribute("sess_role_id").toString());

		String sql = "";

		LocalDate localDate = LocalDate.now();

		LocalDate minusDays;
		LocalDate plusDays;

		if (!toDate.equalsIgnoreCase("0")) {
			minusDays = LocalDate.parse(fromDate);
			plusDays = LocalDate.parse(toDate);
		} else {
			minusDays = localDate.minusDays(50);
			plusDays = localDate.plusDays(50);
		}

		/**
		 * roleBased Count
		 */

		if (roleId > 3) {
			sql = "SELECT SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'NonComplied', "
					+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', "
					+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Complied', "
					+ "SUM(IF(DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND  DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Delayed',  "
					+ "SUM(IF(ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval', "
					+ "SUM(IF(ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', "
					+ "SUM(IF(DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d')) AND ttrn_status = 'Completed', 1, 0)) AS 'DelayedReported' "
					+ "FROM cfg_task_user_mapping tmap JOIN trn_task_transactional ON tmap_client_tasks_id = ttrn_client_task_id AND "
					+ "ttrn_client_task_id = tmap_client_tasks_id JOIN cfg_user_entity_mapping umap ON umap.umap_orga_id = tmap.tmap_orga_id AND "
					+ "umap.umap_loca_id = tmap.tmap_loca_id AND umap.umap_dept_id = tmap.tmap_dept_id AND umap.umap_user_id = '"
					+ userId + "' AND tmap_dept_id = '" + dept_id + "' AND ttrn_legal_due_date BETWEEN '" + minusDays
					+ "' AND '" + plusDays + "' ";
			// System.out.println("getDepartmentChartRiskCount roleId > 3 " + sql);
		} else {
			if (roleId == 3) {
				sql = "SELECT SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'NonComplied', "
						+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', "
						+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Complied', "
						+ "SUM(IF(DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND  DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Delayed',  "
						+ "SUM(IF(ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval', "
						+ "SUM(IF(ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', "
						+ "SUM(IF(DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d')) AND ttrn_status = 'Completed', 1, 0)) AS 'DelayedReported' "
						+ "FROM cfg_task_user_mapping JOIN trn_task_transactional ON tmap_client_tasks_id = ttrn_client_task_id AND ttrn_client_task_id = tmap_client_tasks_id AND (tmap_pr_user_id = '"
						+ userId + "' OR tmap_rw_user_id = '" + userId + "' OR tmap_fh_user_id = '" + userId
						+ "' ) AND tmap_dept_id = '" + dept_id + "' AND  ttrn_legal_due_date BETWEEN '" + minusDays
						+ "' AND '" + plusDays + "' ";
				// System.out.println("getDepartmentChartRiskCount roleId == 3 " + sql);
			} else {
				if (roleId == 2) {
					sql = "SELECT SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'NonComplied', "
							+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', "
							+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Complied', "
							+ "SUM(IF(DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND  DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Delayed',  "
							+ "SUM(IF(ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval', "
							+ "SUM(IF(ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', "
							+ "SUM(IF(DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d')) AND ttrn_status = 'Completed', 1, 0)) AS 'DelayedReported' "
							+ "FROM cfg_task_user_mapping JOIN trn_task_transactional ON tmap_client_tasks_id = ttrn_client_task_id AND ttrn_client_task_id = tmap_client_tasks_id AND (tmap_pr_user_id = '"
							+ userId + "' OR tmap_rw_user_id = '" + userId + "') AND tmap_dept_id = '" + dept_id
							+ "' AND ttrn_legal_due_date BETWEEN '" + minusDays + "' AND '" + plusDays + "' ";
					// System.out.println("getDepartmentChartRiskCount roleId == 2 " + sql);
				} else {
					if (roleId == 1) {
						sql = "SELECT SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'NonComplied', "
								+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', "
								+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Complied', "
								+ "SUM(IF(DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND  DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Delayed',  "
								+ "SUM(IF(ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval', "
								+ "SUM(IF(ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', "
								+ "SUM(IF(DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d')) AND ttrn_status = 'Completed', 1, 0)) AS 'DelayedReported' "
								+ "FROM cfg_task_user_mapping JOIN trn_task_transactional ON tmap_client_tasks_id = ttrn_client_task_id AND ttrn_client_task_id = tmap_client_tasks_id AND tmap_pr_user_id = '"
								+ userId + "' AND tmap_dept_id = '" + dept_id + "' AND ttrn_legal_due_date BETWEEN '"
								+ minusDays + "' AND '" + plusDays + "'";
						// System.out.println("getDepartmentChartRiskCount roleId == 1 " + sql);
					}
				}
			}
		}

		Query createUnitQuery = entityManager.createNativeQuery(sql);
		return createUnitQuery.getResultList();
	}

	/**
	 * pending
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getOverallDrilledData(String status, String entity, HttpSession session, String fromDate,
			String toDate) {
		try {
			// System.out.println("fromDate : " + fromDate);
			LocalDate localDate = LocalDate.now();
			LocalDate minusDays;
			LocalDate plusDays;

			if (!toDate.equalsIgnoreCase("0")) {
				minusDays = LocalDate.parse(fromDate);
				plusDays = LocalDate.parse(toDate);
			} else {
				minusDays = localDate.minusDays(50);
				plusDays = localDate.plusDays(50);
			}

			Integer userId = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			Integer roleId = Integer.parseInt(session.getAttribute("sess_role_id").toString());
			String sql = "";

			String nonCompliedQuery = " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND tttrn.ttrn_status = 'Active' AND ttrn_legal_due_date BETWEEN '"
					+ minusDays + "' AND '" + plusDays + "'   ";

			String posingRiskQuery = " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active' AND ttrn_legal_due_date BETWEEN '"
					+ minusDays + "' AND '" + plusDays + "' ";

			String compliedQuery = " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
					+ minusDays + "' AND '" + plusDays + "' ";

			String delayedQuery = " AND DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
					+ minusDays + "' AND '" + plusDays + "'  ";

			String waitingForApprovalQuery = " AND ttrn_status = 'Partially_Completed' AND ttrn_legal_due_date BETWEEN '"
					+ minusDays + "' AND '" + plusDays + "'  ";

			String reOpenedQuery = " AND ttrn_status = 'Re_Opened' AND ttrn_legal_due_date BETWEEN '" + minusDays
					+ "' AND  '" + plusDays + "'  ";

			String delayedReportedQuery = " AND DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d')) AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
					+ minusDays + "' AND  '" + plusDays + "'  ";

			if (roleId > 3) {
				sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status, DATE_FORMAT(tttrn.ttrn_pr_due_date, '%d-%m-%Y'), DATE_FORMAT(tttrn.ttrn_rw_due_date, '%d-%m-%Y'), DATE_FORMAT(tttrn.ttrn_fh_due_date, '%d-%m-%Y'), DATE_FORMAT(tttrn.ttrn_uh_due_date, '%d-%m-%Y'), DATE_FORMAT(tttrn.ttrn_legal_due_date, '%d-%m-%Y'), IFNULL(DATE_FORMAT(tttrn.ttrn_submitted_date, '%d-%m-%Y'), ''), orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, tsk.task_legi_name, tsk.task_rule_name, tsk.task_reference, tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id, usr.user_first_name, usr.user_last_name, tttrn.ttrn_completed_date, tsk.task_cat_law_name, tttrn.ttrn_performer_comments FROM trn_task_transactional tttrn LEFT JOIN cfg_task_user_mapping tmapp ON tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id JOIN cfg_user_entity_mapping umapp ON umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = '"
						+ userId
						+ "' JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id JOIN mst_user usr ON usr.user_id = tmapp.tmap_pr_user_id ";
				if (status.equalsIgnoreCase("Complied")) {
					// System.out.println("inside Complied " + status);
					sql += compliedQuery;
				} else if (status.equalsIgnoreCase("PosingRisk")) {
					// System.out.println("inside PosingRisk " + status);
					sql += posingRiskQuery;
				} else if (status.equalsIgnoreCase("NonComplied")) {
					// System.out.println("inside NonComplied " + status);
					sql += nonCompliedQuery;
				} else if (status.equalsIgnoreCase("Delayed")) {
					// System.out.println("inside Delayed " + status);
					sql += delayedQuery;
				} else if (status.equalsIgnoreCase("Waiting For Approval")) {
					// System.out.println("inside WFA " + status);
					sql += waitingForApprovalQuery;
				} else if (status.equalsIgnoreCase("ReOpened")) {
					// System.out.println("inside ReOpened " + status);
					sql += reOpenedQuery;
				} else if (status.equalsIgnoreCase("DelayedReported")) {
					// System.out.println("inside DelayedReported " + status);
					sql += delayedReportedQuery;
				}
			} else {
				if (roleId == 3) {
					sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status, DATE_FORMAT(tttrn.ttrn_pr_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_rw_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_fh_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_uh_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y-%m-%d'), IFNULL(DATE_FORMAT(tttrn.ttrn_submitted_date, '%Y-%m-%d'), ''), orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, tsk.task_legi_name, tsk.task_rule_name, tsk.task_reference, tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id, usr.user_first_name, usr.user_last_name, IFNULL(DATE_FORMAT(tttrn.ttrn_completed_date, '%Y-%m-%d'), ''), tsk.task_cat_law_name, tttrn.ttrn_performer_comments FROM trn_task_transactional tttrn LEFT JOIN cfg_task_user_mapping tmapp ON tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id JOIN mst_user usr ON usr.user_id = tmapp.tmap_pr_user_id WHERE (tmapp.tmap_pr_user_id = '"
							+ userId + "' OR tmapp.tmap_rw_user_id = '" + userId + " ' OR  tmapp.tmap_fh_user_id = '"
							+ userId + "' ) ";
					if (status.equalsIgnoreCase("Complied")) {
						// System.out.println("inside Complied " + status);
						sql += compliedQuery;
					} else if (status.equalsIgnoreCase("PosingRisk")) {
						// System.out.println("inside PosingRisk " + status);
						sql += posingRiskQuery;
					} else if (status.equalsIgnoreCase("NonComplied")) {
						// System.out.println("inside NonComplied " + status);
						sql += nonCompliedQuery;
					} else if (status.equalsIgnoreCase("Delayed")) {
						// System.out.println("inside Delayed " + status);
						sql += delayedQuery;
					} else if (status.equalsIgnoreCase("Waiting For Approval")) {
						// System.out.println("inside WFA " + status);
						sql += waitingForApprovalQuery;
					} else if (status.equalsIgnoreCase("ReOpened")) {
						// System.out.println("inside ReOpened " + status);
						sql += reOpenedQuery;
					} else if (status.equalsIgnoreCase("DelayedReported")) {
						// System.out.println("inside DelayedReported " + status);
						sql += delayedReportedQuery;
					}
				} else {
					if (roleId == 2) {
						sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status, DATE_FORMAT(tttrn.ttrn_pr_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_rw_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_fh_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_uh_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y-%m-%d'), IFNULL(DATE_FORMAT(tttrn.ttrn_submitted_date, '%Y-%m-%d'), ''), orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, tsk.task_legi_name, tsk.task_rule_name, tsk.task_reference, tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id, usr.user_first_name, usr.user_last_name, IFNULL(DATE_FORMAT(tttrn.ttrn_completed_date, '%Y-%m-%d'), ''), tsk.task_cat_law_name, tttrn.ttrn_performer_comments FROM trn_task_transactional tttrn LEFT JOIN cfg_task_user_mapping tmapp ON tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id JOIN mst_user usr ON usr.user_id = tmapp.tmap_pr_user_id WHERE (tmapp.tmap_pr_user_id = '"
								+ userId + "' OR tmapp.tmap_rw_user_id = '" + userId + "' ) ";
						if (status.equalsIgnoreCase("Complied")) {
							// System.out.println("inside Complied " + status);
							sql += compliedQuery;
						} else if (status.equalsIgnoreCase("PosingRisk")) {
							// System.out.println("inside PosingRisk " + status);
							sql += posingRiskQuery;
						} else if (status.equalsIgnoreCase("NonComplied")) {
							// System.out.println("inside NonComplied " + status);
							sql += nonCompliedQuery;
						} else if (status.equalsIgnoreCase("Delayed")) {
							// System.out.println("inside Delayed " + status);
							sql += delayedQuery;
						} else if (status.equalsIgnoreCase("Waiting For Approval")) {
							// System.out.println("inside WFA " + status);
							sql += waitingForApprovalQuery;
						} else if (status.equalsIgnoreCase("ReOpened")) {
							// System.out.println("inside ReOpened " + status);
							sql += reOpenedQuery;
						} else if (status.equalsIgnoreCase("DelayedReported")) {
							// System.out.println("inside DelayedReported " + status);
							sql += delayedReportedQuery;
						}
					} else {
						if (roleId == 1) {
							sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status, DATE_FORMAT(tttrn.ttrn_pr_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_rw_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_fh_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_uh_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y-%m-%d'), IFNULL(DATE_FORMAT(tttrn.ttrn_submitted_date, '%Y-%m-%d'), ''), orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, tsk.task_legi_name, tsk.task_rule_name, tsk.task_reference, tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id, usr.user_first_name, usr.user_last_name, IFNULL(DATE_FORMAT(tttrn.ttrn_completed_date, '%Y-%m-%d'), ''), tsk.task_cat_law_name, tttrn.ttrn_performer_comments FROM trn_task_transactional tttrn LEFT JOIN cfg_task_user_mapping tmapp ON tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id JOIN mst_user usr ON usr.user_id = tmapp.tmap_pr_user_id WHERE (tmapp.tmap_pr_user_id = '"
									+ userId + "') ";
							if (status.equalsIgnoreCase("Complied")) {
								// System.out.println("inside Complied " + status);
								sql += compliedQuery;
							} else if (status.equalsIgnoreCase("PosingRisk")) {
								// System.out.println("inside PosingRisk " + status);
								sql += posingRiskQuery;
							} else if (status.equalsIgnoreCase("NonComplied")) {
								// System.out.println("inside NonComplied " + status);
								sql += nonCompliedQuery;
							} else if (status.equalsIgnoreCase("Delayed")) {
								/// System.out.println("inside Delayed " + status);
								sql += delayedQuery;
							} else if (status.equalsIgnoreCase("Waiting For Approval")) {
								// System.out.println("inside WFA " + status);
								sql += waitingForApprovalQuery;
							} else if (status.equalsIgnoreCase("ReOpened")) {
								// System.out.println("inside ReOpened " + status);
								sql += reOpenedQuery;
							} else if (status.equalsIgnoreCase("DelayedReported")) {
								// System.out.println("inside DelayedReported " + status);
								sql += delayedReportedQuery;
							}
						}
					}
				}
			}
			System.out.println("sql : " + sql);
			Query createNativeQuery = entityManager.createNativeQuery(sql);
			return createNativeQuery.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getEntityDrilledData(String status, String orgaName, HttpSession session, String fromDate,
			String toDate) {

		try {
			// System.out.println("status : " + status);
			// System.out.println("orgaName : " + orgaName);
			Integer orgaId = 0;

			LocalDate localDate = LocalDate.now();

			LocalDate minusDays;
			LocalDate plusDays;

			if (!toDate.equalsIgnoreCase("0")) {
				minusDays = LocalDate.parse(fromDate);
				plusDays = LocalDate.parse(toDate);
			} else {
				minusDays = localDate.minusDays(50);
				plusDays = localDate.plusDays(50);
			}

			Integer userId = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			Integer roleId = Integer.parseInt(session.getAttribute("sess_role_id").toString());
			String sql = "";

			String oSql = "from mst_organization  where orga_name = '" + orgaName + "' AND orga_name != 'NA'";
			Query entityQuery = entityManager.createQuery(oSql);
			List<Organization> resultList = entityQuery.getResultList();
			for (Organization org : resultList) {
				orgaId = org.getOrga_id();
				// System.out.println("orga id : " + org.getOrga_id());

				if (roleId > 3) {
					sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status, IFNULL(DATE_FORMAT(tttrn.ttrn_pr_due_date, '%Y-%m-%d'), ''), IFNULL(DATE_FORMAT(tttrn.ttrn_rw_due_date, '%Y-%m-%d'), ''), IFNULL(DATE_FORMAT(tttrn.ttrn_fh_due_date, '%Y-%m-%d'), ''), IFNULL(DATE_FORMAT(tttrn.ttrn_uh_due_date, '%Y-%m-%d'), ''), IFNULL(DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y-%m-%d'), ''), IFNULL(DATE_FORMAT(tttrn.ttrn_submitted_date, '%Y-%m-%d'), ''), orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, tsk.task_legi_name, tsk.task_rule_name, tsk.task_reference, tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id, usr.user_first_name, usr.user_last_name, tttrn.ttrn_completed_date, tsk.task_cat_law_name, tttrn.ttrn_performer_comments FROM trn_task_transactional tttrn LEFT JOIN cfg_task_user_mapping tmapp ON tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id JOIN cfg_user_entity_mapping umapp ON umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = '"
							+ userId
							+ "' JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id JOIN mst_user usr ON usr.user_id = tmapp.tmap_pr_user_id AND tmap_orga_id = '"
							+ orgaId + "' ";
					if (status.equalsIgnoreCase("Complied")) {
						// System.out.println("inside Complied " + status);
						sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
								+ minusDays + "' AND '" + plusDays + "' ";
					} else if (status.equalsIgnoreCase("PosingRisk")) {
						// System.out.println("inside PosingRisk " + status);
						sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d')  AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active' AND ttrn_legal_due_date BETWEEN '"
								+ minusDays + "' AND '" + plusDays + "' ";
					} else if (status.equalsIgnoreCase("NonComplied")) {
						// System.out.println("inside NonComplied " + status);
						sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND tttrn.ttrn_status = 'Active' AND ttrn_legal_due_date BETWEEN '"
								+ minusDays + "' AND '" + plusDays + "'   ";
					} else if (status.equalsIgnoreCase("Delayed")) {
						// System.out.println("inside Delayed " + status);
						sql += " AND DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
								+ minusDays + "' AND '" + plusDays + "'  ";
					} else if (status.equalsIgnoreCase("Waiting For Approval")) {
						// System.out.println("inside WFA " + status);
						sql += " AND ttrn_status = 'Partially_Completed' AND ttrn_legal_due_date BETWEEN '" + minusDays
								+ "' AND '" + plusDays + "'  ";
					} else if (status.equalsIgnoreCase("ReOpened") || status.equalsIgnoreCase("Re-Opened")) {
						// System.out.println("inside ReOpened " + status);
						sql += " AND ttrn_status = 'Re_Opened' AND ttrn_legal_due_date BETWEEN '" + minusDays
								+ "' AND  '" + plusDays + "'  ";
					} else if (status.equalsIgnoreCase("DelayedReported")
							|| status.equalsIgnoreCase("Delayed-Reported")) {
						// System.out.println("inside DelayedReported " + status);
						sql += " AND DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') < DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') OR DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') = DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d'))  AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
								+ minusDays + "' AND  '" + plusDays + "'  ";
					}
					// System.out.println("roleId > 3 : " + sql);
				} else {
					if (roleId == 3) {
						sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status, DATE_FORMAT(tttrn.ttrn_pr_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_rw_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_fh_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_uh_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y-%m-%d'), IFNULL(DATE_FORMAT(tttrn.ttrn_submitted_date, '%Y-%m-%d'), ''), orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, tsk.task_legi_name, tsk.task_rule_name, tsk.task_reference, tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id, usr.user_first_name, usr.user_last_name, IFNULL(DATE_FORMAT(tttrn.ttrn_completed_date, '%Y-%m-%d'), ''), tsk.task_cat_law_name, tttrn.ttrn_performer_comments FROM trn_task_transactional tttrn LEFT JOIN cfg_task_user_mapping tmapp ON tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id JOIN mst_user usr ON usr.user_id = tmapp.tmap_pr_user_id WHERE (tmapp.tmap_pr_user_id = '"
								+ userId + "' OR tmapp.tmap_rw_user_id = '" + userId
								+ " ' OR  tmapp.tmap_fh_user_id = '" + userId + "' ) AND tmap_orga_id = '" + orgaId
								+ "' ";
						if (status.equalsIgnoreCase("Complied")) {
							// System.out.println("inside Complied " + status);
							sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
									+ minusDays + "' AND '" + plusDays + "' ";
						} else if (status.equalsIgnoreCase("PosingRisk")) {
							// System.out.println("inside PosingRisk " + status);
							sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d')  AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active' AND ttrn_legal_due_date BETWEEN '"
									+ minusDays + "' AND '" + plusDays + "' ";
						} else if (status.equalsIgnoreCase("NonComplied")) {
							// System.out.println("inside NonComplied " + status);
							sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND tttrn.ttrn_status = 'Active' AND ttrn_legal_due_date BETWEEN '"
									+ minusDays + "' AND '" + plusDays + "'   ";
						} else if (status.equalsIgnoreCase("Delayed")) {
							// System.out.println("inside Delayed " + status);
							sql += " AND DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
									+ minusDays + "' AND '" + plusDays + "'  ";
						} else if (status.equalsIgnoreCase("Waiting For Approval")) {
							// System.out.println("inside WFA " + status);
							sql += " AND ttrn_status = 'Partially_Completed' AND ttrn_legal_due_date BETWEEN '"
									+ minusDays + "' AND '" + plusDays + "'  ";
						} else if (status.equalsIgnoreCase("ReOpened")) {
							// System.out.println("inside ReOpened " + status);
							sql += " AND ttrn_status = 'Re_Opened' AND ttrn_legal_due_date BETWEEN '" + minusDays
									+ "' AND  '" + plusDays + "'  ";
						} else if (status.equalsIgnoreCase("DelayedReported")) {
							// System.out.println("inside DelayedReported " + status);
							sql += " AND DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') < DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') OR DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') = DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d'))  AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
									+ minusDays + "' AND  '" + plusDays + "'  ";
						}
						// System.out.println("roleId == 3 : " + sql);
					} else {
						if (roleId == 2) {
							sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status, DATE_FORMAT(tttrn.ttrn_pr_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_rw_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_fh_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_uh_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y-%m-%d'), IFNULL(DATE_FORMAT(tttrn.ttrn_submitted_date, '%Y-%m-%d'), ''), orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, tsk.task_legi_name, tsk.task_rule_name, tsk.task_reference, tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id, usr.user_first_name, usr.user_last_name, IFNULL(DATE_FORMAT(tttrn.ttrn_completed_date, '%Y-%m-%d'), ''), tsk.task_cat_law_name, tttrn.ttrn_performer_comments FROM trn_task_transactional tttrn LEFT JOIN cfg_task_user_mapping tmapp ON tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id JOIN mst_user usr ON usr.user_id = tmapp.tmap_pr_user_id WHERE (tmapp.tmap_pr_user_id = '"
									+ userId + "' OR tmapp.tmap_rw_user_id = '" + userId + "' ) AND tmap_orga_id = '"
									+ orgaId + "' ";
							if (status.equalsIgnoreCase("Complied")) {
								// System.out.println("inside Complied " + status);
								sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
										+ minusDays + "' AND '" + plusDays + "' ";
							} else if (status.equalsIgnoreCase("PosingRisk")) {
								// System.out.println("inside PosingRisk " + status);
								sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d')  AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active' AND ttrn_legal_due_date BETWEEN '"
										+ minusDays + "' AND '" + plusDays + "' ";
							} else if (status.equalsIgnoreCase("NonComplied")) {
								// System.out.println("inside NonComplied " + status);
								sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND tttrn.ttrn_status = 'Active' AND ttrn_legal_due_date BETWEEN '"
										+ minusDays + "' AND '" + plusDays + "'   ";
							} else if (status.equalsIgnoreCase("Delayed")) {
								// System.out.println("inside Delayed " + status);
								sql += " AND DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
										+ minusDays + "' AND '" + plusDays + "'  ";
							} else if (status.equalsIgnoreCase("Waiting For Approval")) {
								// System.out.println("inside WFA " + status);
								sql += " AND ttrn_status = 'Partially_Completed' AND ttrn_legal_due_date BETWEEN '"
										+ minusDays + "' AND '" + plusDays + "'  ";
							} else if (status.equalsIgnoreCase("ReOpened")) {
								// System.out.println("inside ReOpened " + status);
								sql += " AND ttrn_status = 'Re_Opened' AND ttrn_legal_due_date BETWEEN '" + minusDays
										+ "' AND  '" + plusDays + "'  ";
							} else if (status.equalsIgnoreCase("DelayedReported")) {
								// System.out.println("inside DelayedReported " + status);
								sql += " AND DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') < DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') OR DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') = DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d'))  AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
										+ minusDays + "' AND  '" + plusDays + "'  ";
							}
							// System.out.println("roleId == 2 : " + sql);
						} else {
							if (roleId == 1) {

								sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status, DATE_FORMAT(tttrn.ttrn_pr_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_rw_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_fh_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_uh_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y-%m-%d'), IFNULL(DATE_FORMAT(tttrn.ttrn_submitted_date, '%Y-%m-%d'), ''), orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, tsk.task_legi_name, tsk.task_rule_name, tsk.task_reference, tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id, usr.user_first_name, usr.user_last_name, IFNULL(DATE_FORMAT(tttrn.ttrn_completed_date, '%Y-%m-%d'), ''), tsk.task_cat_law_name, tttrn.ttrn_performer_comments FROM trn_task_transactional tttrn LEFT JOIN cfg_task_user_mapping tmapp ON tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id JOIN mst_user usr ON usr.user_id = tmapp.tmap_pr_user_id WHERE (tmapp.tmap_pr_user_id = '"
										+ userId + "') AND tmap_orga_id = '" + orgaId + "' ";
								if (status.equalsIgnoreCase("Complied")) {
									// System.out.println("inside Complied " + status);
									sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
											+ minusDays + "' AND '" + plusDays + "' ";
								} else if (status.equalsIgnoreCase("PosingRisk")) {
									// System.out.println("inside PosingRisk " + status);
									sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d')  AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active' AND ttrn_legal_due_date BETWEEN '"
											+ minusDays + "' AND '" + plusDays + "' ";
								} else if (status.equalsIgnoreCase("NonComplied")) {
									// System.out.println("inside NonComplied " + status);
									sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND tttrn.ttrn_status = 'Active' AND ttrn_legal_due_date BETWEEN '"
											+ minusDays + "' AND '" + plusDays + "'   ";
								} else if (status.equalsIgnoreCase("Delayed")) {
									// System.out.println("inside Delayed " + status);
									sql += " AND DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
											+ minusDays + "' AND '" + plusDays + "'  ";
								} else if (status.equalsIgnoreCase("Waiting For Approval")) {
									// System.out.println("inside WFA " + status);
									sql += " AND ttrn_status = 'Partially_Completed' AND ttrn_legal_due_date BETWEEN '"
											+ minusDays + "' AND '" + plusDays + "'  ";
								} else if (status.equalsIgnoreCase("ReOpened")) {
									// System.out.println("inside ReOpened " + status);
									sql += " AND ttrn_status = 'Re_Opened' AND ttrn_legal_due_date BETWEEN '"
											+ minusDays + "' AND  '" + plusDays + "'  ";
								} else if (status.equalsIgnoreCase("DelayedReported")) {
									// System.out.println("inside DelayedReported " + status);
									sql += " AND DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') < DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') OR DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') = DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d'))  AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
											+ minusDays + "' AND  '" + plusDays + "'  ";
								}
								// System.out.println("roleId == 1 : " + sql);
							}
						}
					}
				}
			}
			System.out.println("sql : " + sql);
			Query createNativeQuery = entityManager.createNativeQuery(sql);
			return createNativeQuery.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getUnitLevelDrilledData(String status, String orgaId, Integer locaId, HttpSession session,
			String fromDate, String toDate) {
		try {
			// System.out.println("status : " + status);
			// System.out.println("orgaId : " + orgaId);

			LocalDate localDate = LocalDate.now();
			LocalDate minusDays;
			LocalDate plusDays;

			if (!toDate.equalsIgnoreCase("0")) {
				minusDays = LocalDate.parse(fromDate);
				plusDays = LocalDate.parse(toDate);
			} else {
				minusDays = localDate.minusDays(50);
				plusDays = localDate.plusDays(50);
			}

			Integer userId = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			Integer roleId = Integer.parseInt(session.getAttribute("sess_role_id").toString());
			String sql = "";

			if (roleId > 3) {
				sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status, IFNULL(DATE_FORMAT(tttrn.ttrn_pr_due_date, '%Y-%m-%d'), ''), IFNULL(DATE_FORMAT(tttrn.ttrn_rw_due_date, '%Y-%m-%d'), ''), IFNULL(DATE_FORMAT(tttrn.ttrn_fh_due_date, '%Y-%m-%d'), ''), IFNULL(DATE_FORMAT(tttrn.ttrn_uh_due_date, '%Y-%m-%d'), ''), IFNULL(DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y-%m-%d'), ''), IFNULL(DATE_FORMAT(tttrn.ttrn_submitted_date, '%Y-%m-%d'), ''), orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, tsk.task_legi_name, tsk.task_rule_name, tsk.task_reference, tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id, usr.user_first_name, usr.user_last_name, tttrn.ttrn_completed_date, tsk.task_cat_law_name, tttrn.ttrn_performer_comments FROM trn_task_transactional tttrn LEFT JOIN cfg_task_user_mapping tmapp ON tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id JOIN cfg_user_entity_mapping umapp ON umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = '"
						+ userId
						+ "' JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id JOIN mst_user usr ON usr.user_id = tmapp.tmap_pr_user_id AND tmap_orga_id = '"
						+ orgaId + "' AND tmap_loca_id = '" + locaId + "' ";
				if (status.equalsIgnoreCase("Complied")) {
					// System.out.println("inside Complied " + status);
					sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
							+ minusDays + "' AND '" + plusDays + "' ";
				} else if (status.equalsIgnoreCase("PosingRisk")) {
					// System.out.println("inside PosingRisk " + status);
					sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d')  AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active' AND ttrn_legal_due_date BETWEEN '"
							+ minusDays + "' AND '" + plusDays + "' ";
				} else if (status.equalsIgnoreCase("NonComplied")) {
					// System.out.println("inside NonComplied " + status);
					sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND tttrn.ttrn_status = 'Active' AND ttrn_legal_due_date BETWEEN '"
							+ minusDays + "' AND '" + plusDays + "'   ";
				} else if (status.equalsIgnoreCase("Delayed")) {
					// System.out.println("inside Delayed " + status);
					sql += " AND DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
							+ minusDays + "' AND '" + plusDays + "'  ";
				} else if (status.equalsIgnoreCase("Waiting For Approval")) {
					// System.out.println("inside WFA " + status);
					sql += " AND ttrn_status = 'Partially_Completed' AND ttrn_legal_due_date BETWEEN '" + minusDays
							+ "' AND '" + plusDays + "'  ";
				} else if (status.equalsIgnoreCase("ReOpened") || status.equalsIgnoreCase("Re-Opened")) {
					// System.out.println("inside ReOpened " + status);
					sql += " AND ttrn_status = 'Re_Opened' AND ttrn_legal_due_date BETWEEN '" + minusDays + "' AND  '"
							+ plusDays + "'  ";
				} else if (status.equalsIgnoreCase("DelayedReported") || status.equalsIgnoreCase("Delayed-Reported")) {
					// System.out.println("inside DelayedReported " + status);
					sql += " AND DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') < DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') OR DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') = DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d'))  AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
							+ minusDays + "' AND  '" + plusDays + "'  ";
				}
				// System.out.println("roleId > 3 : " + sql);
			} else {
				if (roleId == 3) {
					sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status, DATE_FORMAT(tttrn.ttrn_pr_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_rw_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_fh_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_uh_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y-%m-%d'), IFNULL(DATE_FORMAT(tttrn.ttrn_submitted_date, '%Y-%m-%d'), ''), orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, tsk.task_legi_name, tsk.task_rule_name, tsk.task_reference, tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id, usr.user_first_name, usr.user_last_name, IFNULL(DATE_FORMAT(tttrn.ttrn_completed_date, '%Y-%m-%d'), ''), tsk.task_cat_law_name, tttrn.ttrn_performer_comments FROM trn_task_transactional tttrn LEFT JOIN cfg_task_user_mapping tmapp ON tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id JOIN mst_user usr ON usr.user_id = tmapp.tmap_pr_user_id WHERE (tmapp.tmap_pr_user_id = '"
							+ userId + "' OR tmapp.tmap_rw_user_id = '" + userId + " ' OR  tmapp.tmap_fh_user_id = '"
							+ userId + "' ) AND tmap_orga_id = '" + orgaId + "' AND tmap_loca_id = '" + locaId + "' ";
					if (status.equalsIgnoreCase("Complied")) {
						// System.out.println("inside Complied " + status);
						sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
								+ minusDays + "' AND '" + plusDays + "' ";
					} else if (status.equalsIgnoreCase("PosingRisk")) {
						// System.out.println("inside PosingRisk " + status);
						sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d')  AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active' AND ttrn_legal_due_date BETWEEN '"
								+ minusDays + "' AND '" + plusDays + "' ";
					} else if (status.equalsIgnoreCase("NonComplied")) {
						// System.out.println("inside NonComplied " + status);
						sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND tttrn.ttrn_status = 'Active' AND ttrn_legal_due_date BETWEEN '"
								+ minusDays + "' AND '" + plusDays + "'   ";
					} else if (status.equalsIgnoreCase("Delayed")) {
						// System.out.println("inside Delayed " + status);
						sql += " AND DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
								+ minusDays + "' AND '" + plusDays + "'  ";
					} else if (status.equalsIgnoreCase("Waiting For Approval")) {
						// System.out.println("inside WFA " + status);
						sql += " AND ttrn_status = 'Partially_Completed' AND ttrn_legal_due_date BETWEEN '" + minusDays
								+ "' AND '" + plusDays + "'  ";
					} else if (status.equalsIgnoreCase("ReOpened")) {
						// System.out.println("inside ReOpened " + status);
						sql += " AND ttrn_status = 'Re_Opened' AND ttrn_legal_due_date BETWEEN '" + minusDays
								+ "' AND  '" + plusDays + "'  ";
					} else if (status.equalsIgnoreCase("DelayedReported")) {
						// System.out.println("inside DelayedReported " + status);
						sql += " AND DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') < DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') OR DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') = DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d'))  AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
								+ minusDays + "' AND  '" + plusDays + "'  ";
					}
					// System.out.println("roleId == 3 : " + sql);
				} else {
					if (roleId == 2) {
						sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status, DATE_FORMAT(tttrn.ttrn_pr_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_rw_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_fh_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_uh_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y-%m-%d'), IFNULL(DATE_FORMAT(tttrn.ttrn_submitted_date, '%Y-%m-%d'), ''), orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, tsk.task_legi_name, tsk.task_rule_name, tsk.task_reference, tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id, usr.user_first_name, usr.user_last_name, IFNULL(DATE_FORMAT(tttrn.ttrn_completed_date, '%Y-%m-%d'), ''), tsk.task_cat_law_name, tttrn.ttrn_performer_comments FROM trn_task_transactional tttrn LEFT JOIN cfg_task_user_mapping tmapp ON tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id JOIN mst_user usr ON usr.user_id = tmapp.tmap_pr_user_id WHERE (tmapp.tmap_pr_user_id = '"
								+ userId + "' OR tmapp.tmap_rw_user_id = '" + userId + "' ) AND tmap_orga_id = '"
								+ orgaId + "' AND tmap_loca_id = '" + locaId + "' ";
						if (status.equalsIgnoreCase("Complied")) {
							// System.out.println("inside Complied " + status);
							sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
									+ minusDays + "' AND '" + plusDays + "' ";
						} else if (status.equalsIgnoreCase("PosingRisk")) {
							// System.out.println("inside PosingRisk " + status);
							sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d')  AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active' AND ttrn_legal_due_date BETWEEN '"
									+ minusDays + "' AND '" + plusDays + "' ";
						} else if (status.equalsIgnoreCase("NonComplied")) {
							// System.out.println("inside NonComplied " + status);
							sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND tttrn.ttrn_status = 'Active' AND ttrn_legal_due_date BETWEEN '"
									+ minusDays + "' AND '" + plusDays + "'   ";
						} else if (status.equalsIgnoreCase("Delayed")) {
							// System.out.println("inside Delayed " + status);
							sql += " AND DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
									+ minusDays + "' AND '" + plusDays + "'  ";
						} else if (status.equalsIgnoreCase("Waiting For Approval")) {
							// System.out.println("inside WFA " + status);
							sql += " AND ttrn_status = 'Partially_Completed' AND ttrn_legal_due_date BETWEEN '"
									+ minusDays + "' AND '" + plusDays + "'  ";
						} else if (status.equalsIgnoreCase("ReOpened")) {
							// System.out.println("inside ReOpened " + status);
							sql += " AND ttrn_status = 'Re_Opened' AND ttrn_legal_due_date BETWEEN '" + minusDays
									+ "' AND  '" + plusDays + "'  ";
						} else if (status.equalsIgnoreCase("DelayedReported")) {
							// System.out.println("inside DelayedReported " + status);
							sql += " AND DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') < DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') OR DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') = DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d'))  AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
									+ minusDays + "' AND  '" + plusDays + "'  ";
						}
						// System.out.println("roleId == 2 : " + sql);
					} else {
						if (roleId == 1) {

							sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status, DATE_FORMAT(tttrn.ttrn_pr_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_rw_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_fh_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_uh_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y-%m-%d'), IFNULL(DATE_FORMAT(tttrn.ttrn_submitted_date, '%Y-%m-%d'), ''), orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, tsk.task_legi_name, tsk.task_rule_name, tsk.task_reference, tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id, usr.user_first_name, usr.user_last_name, IFNULL(DATE_FORMAT(tttrn.ttrn_completed_date, '%Y-%m-%d'), ''), tsk.task_cat_law_name, tttrn.ttrn_performer_comments FROM trn_task_transactional tttrn LEFT JOIN cfg_task_user_mapping tmapp ON tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id JOIN mst_user usr ON usr.user_id = tmapp.tmap_pr_user_id WHERE (tmapp.tmap_pr_user_id = '"
									+ userId + "') AND tmap_orga_id = '" + orgaId + "' AND tmap_loca_id = '" + locaId
									+ "' ";
							if (status.equalsIgnoreCase("Complied")) {
								// System.out.println("inside Complied " + status);
								sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
										+ minusDays + "' AND '" + plusDays + "' ";
							} else if (status.equalsIgnoreCase("PosingRisk")) {
								// System.out.println("inside PosingRisk " + status);
								sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d')  AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active' AND ttrn_legal_due_date BETWEEN '"
										+ minusDays + "' AND '" + plusDays + "' ";
							} else if (status.equalsIgnoreCase("NonComplied")) {
								// System.out.println("inside NonComplied " + status);
								sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND tttrn.ttrn_status = 'Active' AND ttrn_legal_due_date BETWEEN '"
										+ minusDays + "' AND '" + plusDays + "'   ";
							} else if (status.equalsIgnoreCase("Delayed")) {
								// System.out.println("inside Delayed " + status);
								sql += " AND DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
										+ minusDays + "' AND '" + plusDays + "'  ";
							} else if (status.equalsIgnoreCase("Waiting For Approval")) {
								// System.out.println("inside WFA " + status);
								sql += " AND ttrn_status = 'Partially_Completed' AND ttrn_legal_due_date BETWEEN '"
										+ minusDays + "' AND '" + plusDays + "'  ";
							} else if (status.equalsIgnoreCase("ReOpened")) {
								// System.out.println("inside ReOpened " + status);
								sql += " AND ttrn_status = 'Re_Opened' AND ttrn_legal_due_date BETWEEN '" + minusDays
										+ "' AND  '" + plusDays + "'  ";
							} else if (status.equalsIgnoreCase("DelayedReported")) {
								// System.out.println("inside DelayedReported " + status);
								sql += " AND DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') < DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') OR DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') = DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d'))  AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
										+ minusDays + "' AND  '" + plusDays + "'  ";
							}
							// System.out.println("roleId == 1 : " + sql);
						}
					}
				}
			}
			// System.out.println("sql : " + sql);
			Query createNativeQuery = entityManager.createNativeQuery(sql);
			return createNativeQuery.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getLocIdByUnitName(String unitName) {
		String sql = "select loc.loca_id, loc.loca_name, umap.umap_orga_id, umap.umap_user_id, umap.umap_loca_id from mst_location loc JOIN cfg_user_entity_mapping umap ON loc.loca_id = umap.umap_loca_id where loc.loca_name = '"
				+ unitName + "' AND umap.umap_enable_status = 1 group by loca_name";
		Query createNativeQuery = entityManager.createNativeQuery(sql);
		return createNativeQuery.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getOrgaIdByDeptName(String depatmentName, HttpSession session) {
		String sql = "select umap_orga_id, umap_dept_id, dept_name, orga_name from  cfg_user_entity_mapping umap JOIN mst_department dept ON dept.dept_id = umap.umap_dept_id JOIN mst_organization org ON org.orga_id = umap.umap_orga_id where  dept_name = '"
				+ depatmentName + "' group by dept_name";
		Query createNativeQuery = entityManager.createNativeQuery(sql);
		return createNativeQuery.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getFunctionIdByName(String department) {
		String sql = "select dept.dept_id, dept.dept_name, umap.umap_orga_id, umap.umap_user_id, umap.umap_dept_id from mst_department dept JOIN cfg_user_entity_mapping umap ON dept.dept_id = umap.umap_dept_id where dept.dept_name = '"
				+ department + "' AND umap.umap_enable_status = 1 group by dept_name";
		Query createNativeQuery = entityManager.createNativeQuery(sql);
		// System.out.println("getFunctionIdByName sql " + sql);
		return createNativeQuery.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getFunctionLevelDrilledData(String status, String orgaId, Integer deptId, HttpSession session,
			String fromDate, String toDate) {
		try {
			// System.out.println("getFunctionLevelDrilledData orgaId : " + orgaId);

			LocalDate localDate = LocalDate.now();
			LocalDate minusDays;
			LocalDate plusDays;

			if (!toDate.equalsIgnoreCase("0")) {
				minusDays = LocalDate.parse(fromDate);
				plusDays = LocalDate.parse(toDate);
			} else {
				minusDays = localDate.minusDays(50);
				plusDays = localDate.plusDays(50);
			}

			Integer userId = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			Integer roleId = Integer.parseInt(session.getAttribute("sess_role_id").toString());
			String sql = "";

			if (roleId > 3) {
				sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status, IFNULL(DATE_FORMAT(tttrn.ttrn_pr_due_date, '%Y-%m-%d'), ''), "
						+ "IFNULL(DATE_FORMAT(tttrn.ttrn_rw_due_date, '%Y-%m-%d'), ''), IFNULL(DATE_FORMAT(tttrn.ttrn_fh_due_date, '%Y-%m-%d'), ''), "
						+ "IFNULL(DATE_FORMAT(tttrn.ttrn_uh_due_date, '%Y-%m-%d'), ''), IFNULL(DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y-%m-%d'), ''), "
						+ "IFNULL(DATE_FORMAT(tttrn.ttrn_submitted_date, '%Y-%m-%d'), ''), orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, "
						+ "dept.dept_id, dept.dept_name, tsk.task_legi_name, tsk.task_rule_name, tsk.task_reference, tsk.task_activity_who, tsk.task_activity_when, "
						+ "tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id, usr.user_first_name, "
						+ "usr.user_last_name, tttrn.ttrn_completed_date, tsk.task_cat_law_name, tttrn.ttrn_performer_comments FROM trn_task_transactional tttrn "
						+ "LEFT JOIN cfg_task_user_mapping tmapp ON tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id JOIN cfg_user_entity_mapping umapp "
						+ "ON umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND umapp.umap_dept_id = tmapp.tmap_dept_id "
						+ "AND umapp.umap_user_id = '" + userId
						+ "' JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id "
						+ "JOIN mst_location loca ON loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id "
						+ "JOIN mst_user usr ON usr.user_id = tmapp.tmap_pr_user_id  ";

				if (!orgaId.equalsIgnoreCase("0")) {
					sql += " AND tmap_orga_id = '" + orgaId + "' ";
					// System.out.println("sql orgaid check : " + sql);
				}

				if (deptId != 0 && !orgaId.equalsIgnoreCase("0")) {
					sql += " AND tmap_dept_id = '" + deptId + "' AND tmap_orga_id = '" + orgaId + "'  ";
				}

				if (status.equalsIgnoreCase("Complied")) {
					// System.out.println("inside Complied " + status);
					sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
							+ minusDays + "' AND '" + plusDays + "' ";
				} else if (status.equalsIgnoreCase("PosingRisk")) {
					// System.out.println("inside PosingRisk " + status);
					sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d')  AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active' AND ttrn_legal_due_date BETWEEN '"
							+ minusDays + "' AND '" + plusDays + "' ";
				} else if (status.equalsIgnoreCase("NonComplied")) {
					// System.out.println("inside NonComplied " + status);
					sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND tttrn.ttrn_status = 'Active' AND ttrn_legal_due_date BETWEEN '"
							+ minusDays + "' AND '" + plusDays + "'   ";
				} else if (status.equalsIgnoreCase("Delayed")) {
					// System.out.println("inside Delayed " + status);
					sql += " AND DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
							+ minusDays + "' AND '" + plusDays + "'  ";
				} else if (status.equalsIgnoreCase("Waiting For Approval")) {
					// System.out.println("inside WFA " + status);
					sql += " AND ttrn_status = 'Partially_Completed' AND ttrn_legal_due_date BETWEEN '" + minusDays
							+ "' AND '" + plusDays + "'  ";
				} else if (status.equalsIgnoreCase("ReOpened") || status.equalsIgnoreCase("Re-Opened")) {
					// System.out.println("inside ReOpened " + status);
					sql += " AND ttrn_status = 'Re_Opened' AND ttrn_legal_due_date BETWEEN '" + minusDays + "' AND  '"
							+ plusDays + "'  ";
				} else if (status.equalsIgnoreCase("DelayedReported") || status.equalsIgnoreCase("Delayed-Reported")) {
					// System.out.println("inside DelayedReported " + status);
					sql += " AND DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') < DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') OR DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') = DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d'))  AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
							+ minusDays + "' AND  '" + plusDays + "'  ";
				}
				// System.out.println("getFunctionLevelDrilledData roleId > 3 : " + sql);
			} else {
				if (roleId == 3) {
					sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status, DATE_FORMAT(tttrn.ttrn_pr_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_rw_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_fh_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_uh_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y-%m-%d'), IFNULL(DATE_FORMAT(tttrn.ttrn_submitted_date, '%Y-%m-%d'), ''), orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, tsk.task_legi_name, tsk.task_rule_name, tsk.task_reference, tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id, usr.user_first_name, usr.user_last_name, IFNULL(DATE_FORMAT(tttrn.ttrn_completed_date, '%Y-%m-%d'), ''), tsk.task_cat_law_name, tttrn.ttrn_performer_comments FROM trn_task_transactional tttrn LEFT JOIN cfg_task_user_mapping tmapp ON tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id JOIN mst_user usr ON usr.user_id = tmapp.tmap_pr_user_id WHERE (tmapp.tmap_pr_user_id = '"
							+ userId + "' OR tmapp.tmap_rw_user_id = '" + userId + " ' OR  tmapp.tmap_fh_user_id = '"
							+ userId + "' )  ";

					if (!orgaId.equalsIgnoreCase("0")) {
						sql += " AND tmap_orga_id = '" + orgaId + "' ";
						// System.out.println("sql orgaid check : " + sql);
					}

					if (deptId != 0 && !orgaId.equalsIgnoreCase("0")) {
						sql += " AND tmap_dept_id = '" + deptId + "' AND tmap_orga_id = '" + orgaId + "'  ";
					}

					if (status.equalsIgnoreCase("Complied")) {
						// System.out.println("inside Complied " + status);
						sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
								+ minusDays + "' AND '" + plusDays + "' ";
					} else if (status.equalsIgnoreCase("PosingRisk")) {
						// System.out.println("inside PosingRisk " + status);
						sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d')  AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active' AND ttrn_legal_due_date BETWEEN '"
								+ minusDays + "' AND '" + plusDays + "' ";
					} else if (status.equalsIgnoreCase("NonComplied")) {
						// System.out.println("inside NonComplied " + status);
						sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND tttrn.ttrn_status = 'Active' AND ttrn_legal_due_date BETWEEN '"
								+ minusDays + "' AND '" + plusDays + "'   ";
					} else if (status.equalsIgnoreCase("Delayed")) {
						// System.out.println("inside Delayed " + status);
						sql += " AND DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
								+ minusDays + "' AND '" + plusDays + "'  ";
					} else if (status.equalsIgnoreCase("Waiting For Approval")) {
						// System.out.println("inside WFA " + status);
						sql += " AND ttrn_status = 'Partially_Completed' AND ttrn_legal_due_date BETWEEN '" + minusDays
								+ "' AND '" + plusDays + "'  ";
					} else if (status.equalsIgnoreCase("ReOpened")) {
						// System.out.println("inside ReOpened " + status);
						sql += " AND ttrn_status = 'Re_Opened' AND ttrn_legal_due_date BETWEEN '" + minusDays
								+ "' AND  '" + plusDays + "'  ";
					} else if (status.equalsIgnoreCase("DelayedReported")) {
						// System.out.println("inside DelayedReported " + status);
						sql += " AND DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') < DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') OR DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') = DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d'))  AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
								+ minusDays + "' AND  '" + plusDays + "'  ";
					}
					// System.out.println("getFunctionLevelDrilledData roleId == 3 : " + sql);
				} else {
					if (roleId == 2) {
						sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status, DATE_FORMAT(tttrn.ttrn_pr_due_date, '%Y-%m-%d'), "
								+ "DATE_FORMAT(tttrn.ttrn_rw_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_fh_due_date, '%Y-%m-%d'), "
								+ "DATE_FORMAT(tttrn.ttrn_uh_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y-%m-%d'), "
								+ "IFNULL(DATE_FORMAT(tttrn.ttrn_submitted_date, '%Y-%m-%d'), ''), orga.orga_id, orga.orga_name, loca.loca_id, "
								+ "loca.loca_name, dept.dept_id, dept.dept_name, tsk.task_legi_name, tsk.task_rule_name, tsk.task_reference, "
								+ "tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, "
								+ "tttrn.ttrn_frequency_for_operation, usr.user_id, usr.user_first_name, usr.user_last_name, "
								+ "IFNULL(DATE_FORMAT(tttrn.ttrn_completed_date, '%Y-%m-%d'), ''), tsk.task_cat_law_name, tttrn.ttrn_performer_comments "
								+ "FROM trn_task_transactional tttrn LEFT JOIN cfg_task_user_mapping tmapp ON tttrn.ttrn_client_task_id = "
								+ "tmapp.tmap_client_tasks_id JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id JOIN mst_organization orga ON "
								+ "orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept ON "
								+ "dept.dept_id = tmapp.tmap_dept_id JOIN mst_user usr ON usr.user_id = tmapp.tmap_pr_user_id WHERE (tmapp.tmap_pr_user_id = '"
								+ userId + "' OR tmapp.tmap_rw_user_id = '" + userId + "' )  ";

						if (!orgaId.equalsIgnoreCase("0")) {
							sql += " AND tmap_orga_id = '" + orgaId + "' ";
							// System.out.println("sql orgaid check : " + sql);
						}

						if (deptId != 0 && !orgaId.equalsIgnoreCase("0")) {
							sql += " AND tmap_dept_id = '" + deptId + "' AND tmap_orga_id = '" + orgaId + "'  ";
						}

						if (status.equalsIgnoreCase("Complied")) {
							// System.out.println("inside Complied " + status);
							sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
									+ minusDays + "' AND '" + plusDays + "' ";
						} else if (status.equalsIgnoreCase("PosingRisk")) {
							// System.out.println("inside PosingRisk " + status);
							sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d')  AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active' AND ttrn_legal_due_date BETWEEN '"
									+ minusDays + "' AND '" + plusDays + "' ";
						} else if (status.equalsIgnoreCase("NonComplied")) {
							// System.out.println("inside NonComplied " + status);
							sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND tttrn.ttrn_status = 'Active' AND ttrn_legal_due_date BETWEEN '"
									+ minusDays + "' AND '" + plusDays + "'   ";
						} else if (status.equalsIgnoreCase("Delayed")) {
							// System.out.println("inside Delayed " + status);
							sql += " AND DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
									+ minusDays + "' AND '" + plusDays + "'  ";
						} else if (status.equalsIgnoreCase("Waiting For Approval")) {
							// System.out.println("inside WFA " + status);
							sql += " AND ttrn_status = 'Partially_Completed' AND ttrn_legal_due_date BETWEEN '"
									+ minusDays + "' AND '" + plusDays + "'  ";
						} else if (status.equalsIgnoreCase("ReOpened")) {
							// System.out.println("inside ReOpened " + status);
							sql += " AND ttrn_status = 'Re_Opened' AND ttrn_legal_due_date BETWEEN '" + minusDays
									+ "' AND  '" + plusDays + "'  ";
						} else if (status.equalsIgnoreCase("DelayedReported")) {
							// System.out.println("inside DelayedReported " + status);
							sql += " AND DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') < DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') OR DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') = DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d'))  AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
									+ minusDays + "' AND  '" + plusDays + "'  ";
						}
						// System.out.println("getFunctionLevelDrilledData roleId == 2 : " + sql);
					} else {
						if (roleId == 1) {

							sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status, DATE_FORMAT(tttrn.ttrn_pr_due_date, '%Y-%m-%d'), "
									+ "DATE_FORMAT(tttrn.ttrn_rw_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_fh_due_date, '%Y-%m-%d'), "
									+ "DATE_FORMAT(tttrn.ttrn_uh_due_date, '%Y-%m-%d'), DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y-%m-%d'), "
									+ "IFNULL(DATE_FORMAT(tttrn.ttrn_submitted_date, '%Y-%m-%d'), ''), orga.orga_id, orga.orga_name, loca.loca_id, "
									+ "loca.loca_name, dept.dept_id, dept.dept_name, tsk.task_legi_name, tsk.task_rule_name, tsk.task_reference, "
									+ "tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, "
									+ "tttrn.ttrn_frequency_for_operation, usr.user_id, usr.user_first_name, usr.user_last_name, "
									+ "IFNULL(DATE_FORMAT(tttrn.ttrn_completed_date, '%Y-%m-%d'), ''), tsk.task_cat_law_name, tttrn.ttrn_performer_comments "
									+ "FROM trn_task_transactional tttrn LEFT JOIN cfg_task_user_mapping tmapp ON tttrn.ttrn_client_task_id = "
									+ "tmapp.tmap_client_tasks_id JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id JOIN mst_organization orga "
									+ "ON orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmapp.tmap_loca_id "
									+ "JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id JOIN mst_user usr ON usr.user_id = tmapp.tmap_pr_user_id "
									+ "WHERE (tmapp.tmap_pr_user_id = '" + userId + "') ";

							if (!orgaId.equalsIgnoreCase("0")) {
								sql += " AND tmap_orga_id = '" + orgaId + "' ";
								// System.out.println("sql orgaid check : " + sql);
							}

							if (deptId != 0 && !orgaId.equalsIgnoreCase("0")) {
								sql += " AND tmap_dept_id = '" + deptId + "' AND tmap_orga_id = '" + orgaId + "'  ";
							}

							if (status.equalsIgnoreCase("Complied")) {
								// System.out.println("inside Complied " + status);
								sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
										+ minusDays + "' AND '" + plusDays + "' ";
							} else if (status.equalsIgnoreCase("PosingRisk")) {
								// System.out.println("inside PosingRisk " + status);
								sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d')  AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active' AND ttrn_legal_due_date BETWEEN '"
										+ minusDays + "' AND '" + plusDays + "' ";
							} else if (status.equalsIgnoreCase("NonComplied")) {
								// System.out.println("inside NonComplied " + status);
								sql += " AND DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND tttrn.ttrn_status = 'Active' AND ttrn_legal_due_date BETWEEN '"
										+ minusDays + "' AND '" + plusDays + "'   ";
							} else if (status.equalsIgnoreCase("Delayed")) {
								// System.out.println("inside Delayed " + status);
								sql += " AND DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
										+ minusDays + "' AND '" + plusDays + "'  ";
							} else if (status.equalsIgnoreCase("Waiting For Approval")) {
								// System.out.println("inside WFA " + status);
								sql += " AND ttrn_status = 'Partially_Completed' AND ttrn_legal_due_date BETWEEN '"
										+ minusDays + "' AND '" + plusDays + "'  ";
							} else if (status.equalsIgnoreCase("ReOpened")) {
								// System.out.println("inside ReOpened " + status);
								sql += " AND ttrn_status = 'Re_Opened' AND ttrn_legal_due_date BETWEEN '" + minusDays
										+ "' AND  '" + plusDays + "'  ";
							} else if (status.equalsIgnoreCase("DelayedReported")) {
								// System.out.println("inside DelayedReported " + status);
								sql += " AND DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') < DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') OR DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') = DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d'))  AND ttrn_status = 'Completed' AND ttrn_legal_due_date BETWEEN '"
										+ minusDays + "' AND  '" + plusDays + "'  ";
							}
							// System.out.println("getFunctionLevelDrilledData roleId == 1 : " + sql);
						}
					}
				}
			}
			Query createNativeQuery = entityManager.createNativeQuery(sql);
			return createNativeQuery.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getAllUnitsByAccess(int userSesId, String orgaId, HttpSession session) {
		String sql = "select loc.loca_id, loc.loca_name from  mst_location loc JOIN cfg_user_entity_mapping umap ON loc.loca_id = umap.umap_loca_id AND "
				+ "umap.umap_user_id = '" + userSesId + "' AND umap.umap_orga_id = '" + orgaId
				+ "' group by loc.loca_name";
		// System.out.println("getAllUnitsByAccess sql : " + sql);
		Query createQuery = entityManager.createNativeQuery(sql);
		return createQuery.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getFunctionDataByOrgaIdDeptId(String orgaId, HttpSession session) {

		Integer userId = Integer.parseInt(session.getAttribute("sess_user_id").toString());
		Integer roleId = Integer.parseInt(session.getAttribute("sess_role_id").toString());

		try {
			String sql = "SELECT dept.dept_id, dept.dept_name FROM mst_department dept JOIN cfg_user_entity_mapping umap ON dept.dept_id = umap.umap_dept_id "
					+ "AND umap.umap_orga_id = '" + orgaId + "' AND umap.umap_user_id = '" + userId
					+ "' GROUP BY dept.dept_name";
			Query createQuery = entityManager.createNativeQuery(sql);
			// System.out.println("getFunctionDataByOrgaIdDeptId SQL : " + sql);
			return createQuery.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> funDataForGraph(String deptId, String orga_id, HttpSession session, String fromDate,
			String toDate) {
		Integer userId = Integer.parseInt(session.getAttribute("sess_user_id").toString());
		Integer roleId = Integer.parseInt(session.getAttribute("sess_role_id").toString());

		String sql = "";

		LocalDate localDate = LocalDate.now();

		LocalDate minusDays;
		LocalDate plusDays;

		if (!toDate.equalsIgnoreCase("0")) {
			minusDays = LocalDate.parse(fromDate);
			plusDays = LocalDate.parse(toDate);
			// System.out.println("minusDays " + minusDays + "\t plusDays " + plusDays);
			// System.out.println("fromDate " + fromDate + "\t toDate " + toDate);
		} else {
			minusDays = localDate.minusDays(50);
			plusDays = localDate.plusDays(50);
		}
		try {
			/**
			 * roleBased Count
			 */

			if (roleId > 3) {
				sql = "SELECT Distinct SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'NonComplied', "
						+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', "
						+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Complied', "
						+ "SUM(IF(DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND  DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Delayed',  "
						+ "SUM(IF(ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval', "
						+ "SUM(IF(ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', "
						+ "SUM(IF(DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d')) AND ttrn_status = 'Completed', 1, 0)) AS 'DelayedReported' "
						+ "FROM cfg_task_user_mapping tmap JOIN trn_task_transactional ON tmap_client_tasks_id = ttrn_client_task_id AND "
						+ "ttrn_client_task_id = tmap_client_tasks_id JOIN cfg_user_entity_mapping umap ON umap.umap_orga_id = tmap.tmap_orga_id AND "
						+ "umap.umap_loca_id = tmap.tmap_loca_id AND umap.umap_dept_id = tmap.tmap_dept_id AND umap.umap_user_id = '"
						+ userId + "' AND tmap_orga_id = '" + orga_id + "' AND tmap_dept_id = '" + deptId
						+ "' AND ttrn_legal_due_date BETWEEN '" + minusDays + "' AND '" + plusDays + "' ";
				// System.out.println("funDataForGraph roleId > 3 " + sql);
			} else {
				if (roleId == 3) {
					sql = "SELECT Distinct SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'NonComplied', "
							+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', "
							+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Complied', "
							+ "SUM(IF(DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND  DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Delayed',  "
							+ "SUM(IF(ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval', "
							+ "SUM(IF(ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', "
							+ "SUM(IF(DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d')) AND ttrn_status = 'Completed', 1, 0)) AS 'DelayedReported' "
							+ "FROM cfg_task_user_mapping JOIN trn_task_transactional ON tmap_client_tasks_id = ttrn_client_task_id AND ttrn_client_task_id = tmap_client_tasks_id AND (tmap_pr_user_id = '"
							+ userId + "' OR tmap_rw_user_id = '" + userId + "' OR tmap_fh_user_id = '" + userId
							+ "' ) AND tmap_orga_id = '" + orga_id + "'  AND tmap_dept_id = '" + deptId
							+ "' AND  ttrn_legal_due_date BETWEEN '" + minusDays + "' AND '" + plusDays + "' ";
					// System.out.println("funDataForGraph roleId == 3 " + sql);
				} else {
					if (roleId == 2) {
						sql = "SELECT Distinct SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'NonComplied', "
								+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', "
								+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Complied', "
								+ "SUM(IF(DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND  DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Delayed',  "
								+ "SUM(IF(ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval', "
								+ "SUM(IF(ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', "
								+ "SUM(IF(DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d')) AND ttrn_status = 'Completed', 1, 0)) AS 'DelayedReported' "
								+ "FROM cfg_task_user_mapping JOIN trn_task_transactional ON tmap_client_tasks_id = ttrn_client_task_id AND ttrn_client_task_id = tmap_client_tasks_id AND (tmap_pr_user_id = '"
								+ userId + "' OR tmap_rw_user_id = '" + userId + "') AND tmap_orga_id = '" + orga_id
								+ "' AND  tmap_dept_id = '" + deptId + "' AND ttrn_legal_due_date BETWEEN '" + minusDays
								+ "' AND '" + plusDays + "' ";
						// System.out.println("funDataForGraph roleId == 2 " + sql);
					} else {
						if (roleId == 1) {
							sql = "SELECT Distinct SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'NonComplied', "
									+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', "
									+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Complied', "
									+ "SUM(IF(DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND  DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Delayed',  "
									+ "SUM(IF(ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval', "
									+ "SUM(IF(ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', "
									+ "SUM(IF(DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d')) AND ttrn_status = 'Completed', 1, 0)) AS 'DelayedReported' "
									+ "FROM cfg_task_user_mapping JOIN trn_task_transactional ON tmap_client_tasks_id = ttrn_client_task_id AND ttrn_client_task_id = tmap_client_tasks_id AND tmap_pr_user_id = '"
									+ userId + "' AND tmap_orga_id = '" + orga_id + "'  AND tmap_dept_id = '" + deptId
									+ "' AND ttrn_legal_due_date BETWEEN '" + minusDays + "' AND '" + plusDays + "'";
							// System.out.println("funDataForGraph roleId == 1 " + sql);
						}
					}
				}
			}

			Query createUnitQuery = entityManager.createNativeQuery(sql);
			return createUnitQuery.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> filterDataByOrgaIdAndUnitIdURL(String deptId, String orga_id, Integer unitId,
			HttpSession session, String fromDate, String toDate) {
		System.out.println("inside orgaById and unitById fun() ");
		Integer userId = Integer.parseInt(session.getAttribute("sess_user_id").toString());
		Integer roleId = Integer.parseInt(session.getAttribute("sess_role_id").toString());

		String sql = "";

		LocalDate localDate = LocalDate.now();

		LocalDate minusDays;
		LocalDate plusDays;

		if (!toDate.equalsIgnoreCase("0")) {
			minusDays = LocalDate.parse(fromDate);
			plusDays = LocalDate.parse(toDate);
		} else {
			minusDays = localDate.minusDays(50);
			plusDays = localDate.plusDays(50);
		}
		try {
			/**
			 * roleBased Count
			 */

			if (roleId > 3) {
				sql = "SELECT Distinct SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'NonComplied', "
						+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', "
						+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Complied', "
						+ "SUM(IF(DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND  DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Delayed',  "
						+ "SUM(IF(ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval', "
						+ "SUM(IF(ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', "
						+ "SUM(IF(DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d')) AND ttrn_status = 'Completed', 1, 0)) AS 'DelayedReported' "
						+ "FROM cfg_task_user_mapping tmap JOIN trn_task_transactional ON tmap_client_tasks_id = ttrn_client_task_id AND "
						+ "ttrn_client_task_id = tmap_client_tasks_id JOIN cfg_user_entity_mapping umap ON umap.umap_orga_id = tmap.tmap_orga_id AND "
						+ "umap.umap_loca_id = tmap.tmap_loca_id AND umap.umap_dept_id = tmap.tmap_dept_id AND umap.umap_user_id = '"
						+ userId + "' AND tmap_loca_id = '" + unitId + "' AND tmap_orga_id = '" + orga_id
						+ "' AND tmap_dept_id = '" + deptId + "' AND ttrn_legal_due_date BETWEEN '" + minusDays
						+ "' AND '" + plusDays + "' ";
				// System.out.println("roleId > 3 " + sql);
			} else {
				if (roleId == 3) {
					sql = "SELECT Distinct SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'NonComplied', "
							+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', "
							+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Complied', "
							+ "SUM(IF(DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND  DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Delayed',  "
							+ "SUM(IF(ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval', "
							+ "SUM(IF(ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', "
							+ "SUM(IF(DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d')) AND ttrn_status = 'Completed', 1, 0)) AS 'DelayedReported' "
							+ "FROM cfg_task_user_mapping JOIN trn_task_transactional ON tmap_client_tasks_id = ttrn_client_task_id AND ttrn_client_task_id = tmap_client_tasks_id AND (tmap_pr_user_id = '"
							+ userId + "' OR tmap_rw_user_id = '" + userId + "' OR tmap_fh_user_id = '" + userId
							+ "' ) AND tmap_orga_id = '" + orga_id + "'  AND tmap_dept_id = '" + deptId
							+ "' AND tmap_loca_id = '" + unitId + "' AND  ttrn_legal_due_date BETWEEN '" + minusDays
							+ "' AND '" + plusDays + "' ";
					// System.out.println("roleId == 3 " + sql);
				} else {
					if (roleId == 2) {
						sql = "SELECT Distinct SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'NonComplied', "
								+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', "
								+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Complied', "
								+ "SUM(IF(DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND  DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Delayed',  "
								+ "SUM(IF(ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval', "
								+ "SUM(IF(ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', "
								+ "SUM(IF(DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d')) AND ttrn_status = 'Completed', 1, 0)) AS 'DelayedReported' "
								+ "FROM cfg_task_user_mapping JOIN trn_task_transactional ON tmap_client_tasks_id = ttrn_client_task_id AND ttrn_client_task_id = tmap_client_tasks_id AND (tmap_pr_user_id = '"
								+ userId + "' OR tmap_rw_user_id = '" + userId + "') AND tmap_orga_id = '" + orga_id
								+ "' AND tmap_loca_id = '" + unitId + "' AND  tmap_dept_id = '" + deptId
								+ "' AND ttrn_legal_due_date BETWEEN '" + minusDays + "' AND '" + plusDays + "' ";
						// System.out.println("roleId == 2 " + sql);
					} else {
						if (roleId == 1) {
							sql = "SELECT Distinct SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'NonComplied', "
									+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', "
									+ "SUM(IF(DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Complied', "
									+ "SUM(IF(DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND  DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND ttrn_status = 'Completed', 1, 0)) AS 'Delayed',  "
									+ "SUM(IF(ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval', "
									+ "SUM(IF(ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', "
									+ "SUM(IF(DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d')) AND ttrn_status = 'Completed', 1, 0)) AS 'DelayedReported' "
									+ "FROM cfg_task_user_mapping JOIN trn_task_transactional ON tmap_client_tasks_id = ttrn_client_task_id AND ttrn_client_task_id = tmap_client_tasks_id AND tmap_pr_user_id = '"
									+ userId + "' AND tmap_orga_id = '" + orga_id + "'  AND tmap_dept_id = '" + deptId
									+ "' AND tmap_loca_id = '" + unitId + "' AND ttrn_legal_due_date BETWEEN '"
									+ minusDays + "' AND '" + plusDays + "'";
							// System.out.println("roleId == 1 " + sql);
						}
					}
				}
			}

			Query createUnitQuery = entityManager.createNativeQuery(sql);
			return createUnitQuery.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Department> getAllDepartmentsByOrgaId(HttpSession session, String orgaId) {

		String userId = session.getAttribute("sess_user_id").toString();

		try {
			String sql = "SELECT DISTINCT dept.* FROM mst_department dept, cfg_user_entity_mapping umap, cfg_task_user_mapping tmap WHERE umap.umap_user_id = '"
					+ userId
					+ "' AND umap.umap_dept_id = dept.dept_id AND umap.umap_orga_id = tmap.tmap_orga_id AND umap.umap_loca_id = tmap.tmap_loca_id AND "
					+ "umap.umap_dept_id = tmap.tmap_dept_id AND tmap_orga_id = '" + orgaId
					+ "' GROUP BY tmap.tmap_client_tasks_id ";
			Query query = entityManager.createNativeQuery(sql, Department.class);
			// query.setParameter(1, session.getAttribute("sess_user_id"));
			// System.out.println("getAllDepartmentsByOrgaId : " + sql);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}
