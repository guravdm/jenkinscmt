package lexprd006.dao.impl;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "dcDAO")
@Transactional
public class DashboardCDAOImpl implements DashboardCDAO {

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> complianceDashboardCount(int user_id, int user_role_id, String jsonString) {

		LocalDate localDate = LocalDate.now();
		LocalDate minusDays = localDate.minusDays(15);
		LocalDate plusDays = localDate.plusDays(5);

		try {
			String sql = "SELECT "
					+ "SUM(IF(DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(tttrn.ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND tttrn.ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', "
					+ "SUM(IF(DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(tttrn.ttrn_submitted_date, '%Y/%m/%d') AND tttrn.ttrn_status = 'Completed', 1, 0)) AS 'Complied', "
					+ "SUM(IF(DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND tttrn.ttrn_status = 'Active', 1, 0)) AS 'NonComplied', "
					+ "SUM(IF(DATE_FORMAT(tttrn.ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y/%m/%d') AND DATE_FORMAT(tttrn.ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y/%m/%d') AND tttrn.ttrn_status = 'Completed', 1, 0)) AS 'Delayed', "
					+ "SUM(IF(DATE_FORMAT(tttrn.ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(tttrn.ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y/%m/%d')) AND tttrn.ttrn_status = 'Completed', 1, 0)) AS 'DelayedReported', "
					+ "SUM(IF(tttrn.ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', SUM(IF(tttrn.ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval' "
					+ "FROM trn_task_transactional tttrn LEFT JOIN cfg_task_user_mapping tmapp ON tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
					+ "JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id "
					+ "JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmapp.tmap_loca_id "
					+ "JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id JOIN mst_user usr ON usr.user_id = tmapp.tmap_pr_user_id "
					+ "JOIN mst_user rwusr ON rwusr.user_id = tmapp.tmap_rw_user_id JOIN mst_user fhusr ON fhusr.user_id = tmapp.tmap_fh_user_id ";

			System.out.println(user_id + " id " + user_role_id);
			if (user_role_id > 3 && user_role_id != 7) {
				sql += " WHERE tttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND "
						+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' and "
						+ "tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays + "' ";
			} else {
				if (user_role_id == 3) {
					sql += " WHERE (tmapp.tmap_pr_user_id = '" + user_id + "' OR  tmapp.tmap_rw_user_id = '" + user_id
							+ "' OR  tmapp.tmap_fh_user_id = '" + user_id + "') AND "
							+ "tttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND "
							+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' and "
							+ "tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays + "'";
				} else {
					if (user_role_id == 2) {
						sql += " WHERE (tmapp.tmap_pr_user_id = '" + user_id + "' OR tmapp.tmap_rw_user_id = '"
								+ user_id + "') AND "
								+ "tttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND "
								+ "tttrn.ttrn_frequency_for_operation != 'User_Defined' AND "
								+ "tsk.task_cat_law_name != 'Internal' AND tttrn.ttrn_legal_due_date BETWEEN '"
								+ minusDays + "' AND '" + plusDays + "' ";
					} else {
						if (user_role_id == 7) {
							sql += " WHERE tttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND "
									+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' and "
									+ "tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays + "' ";
						}
					}
				}
			}

			Query query = em.createNativeQuery(sql);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> searchComplianceDashboardCount(int user_id, int user_role_id, String fromDate, String toDate,
			String orgaId) {

		try {
			String sql = "SELECT "
					+ "SUM(IF(DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(tttrn.ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND tttrn.ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', "
					+ "SUM(IF(DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(tttrn.ttrn_submitted_date, '%Y/%m/%d') AND tttrn.ttrn_status = 'Completed', 1, 0)) AS 'Complied', "
					+ "SUM(IF(DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND tttrn.ttrn_status = 'Active', 1, 0)) AS 'NonComplied', "
					+ "SUM(IF(DATE_FORMAT(tttrn.ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y/%m/%d') AND DATE_FORMAT(tttrn.ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y/%m/%d') AND tttrn.ttrn_status = 'Completed', 1, 0)) AS 'Delayed', "
					+ "SUM(IF(DATE_FORMAT(tttrn.ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(tttrn.ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y/%m/%d')) AND tttrn.ttrn_status = 'Completed', 1, 0)) AS 'DelayedReported', "
					+ "SUM(IF(tttrn.ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', SUM(IF(tttrn.ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval' "
					+ "FROM trn_task_transactional tttrn LEFT JOIN cfg_task_user_mapping tmapp ON tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
					+ "JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id "
					+ "JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmapp.tmap_loca_id "
					+ "JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id JOIN mst_user usr ON usr.user_id = tmapp.tmap_pr_user_id "
					+ "JOIN mst_user rwusr ON rwusr.user_id = tmapp.tmap_rw_user_id JOIN mst_user fhusr ON fhusr.user_id = tmapp.tmap_fh_user_id ";

			System.out.println(user_id + " id " + user_role_id);
			if (user_role_id > 3 && user_role_id != 7) {
				sql += " WHERE tttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND "
						+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' ";

				if (!fromDate.equals("0") && !toDate.equals("0")) {
					sql += " AND tttrn.ttrn_legal_due_date BETWEEN '" + fromDate + "' AND '" + toDate + "'";
				}
				if (!orgaId.equals("0") && !orgaId.equals("null")) {
					sql += " AND orga.orga_id = '" + orgaId + "'";
				}

			} else {
				if (user_role_id == 3) {
					sql += " WHERE (tmapp.tmap_pr_user_id = '" + user_id + "' OR  tmapp.tmap_rw_user_id = '" + user_id
							+ "' OR  tmapp.tmap_fh_user_id = '" + user_id + "') AND "
							+ "tttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND "
							+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' ";

					if (!fromDate.equals("0") && !toDate.equals("0")) {
						sql += " AND tttrn.ttrn_legal_due_date BETWEEN '" + fromDate + "' AND '" + toDate + "'";
					}
					if (!orgaId.equals("0") && !orgaId.equals("null")) {
						sql += " AND orga.orga_id = '" + orgaId + "'";
					}

				} else {
					if (user_role_id == 2) {
						sql += " WHERE (tmapp.tmap_pr_user_id = '" + user_id + "' OR tmapp.tmap_rw_user_id = '"
								+ user_id + "') AND "
								+ "tttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND "
								+ "tttrn.ttrn_frequency_for_operation != 'User_Defined' AND "
								+ "tsk.task_cat_law_name != 'Internal' ";

						if (!fromDate.equals("0") && !toDate.equals("0")) {
							sql += " AND tttrn.ttrn_legal_due_date BETWEEN '" + fromDate + "' AND '" + toDate + "'";
						}
						if (!orgaId.equals("0") && !orgaId.equals("null")) {
							sql += " AND orga.orga_id = '" + orgaId + "'";
						}
					} else {
						if (user_role_id == 7) {
							sql += " WHERE tttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND "
									+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' ";

							if (!fromDate.equals("0") && !toDate.equals("0")) {
								sql += " AND tttrn.ttrn_legal_due_date BETWEEN '" + fromDate + "' AND '" + toDate + "'";
							}
							if (!orgaId.equals("0") && !orgaId.equals("null")) {
								sql += " AND orga.orga_id = '" + orgaId + "'";
							}

						}
					}
				}
			}

			Query query = em.createNativeQuery(sql);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return null;
	}

}
