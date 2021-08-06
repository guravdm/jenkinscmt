package lexprd006.dao.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lexprd006.dao.DashboardDao;
import lexprd006.domain.SubTaskTranscational;

@Repository(value = "dashboardDao")
@Transactional
public class DashboardDaoImpl implements DashboardDao {

	@PersistenceContext
	private EntityManager em;

	public final SimpleDateFormat sdfIn = new SimpleDateFormat("dd-MM-yyyy");
	public final SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd");

	// Method Written By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Get all tasks from Dashboard To DB

	// @Cacheable(value = "overallComplianceGraph", key = "{#user_id,
	// #user_role_id}")
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getOverallComplianceGraph(int user_id, int user_role_id, String jsonString) {

		LocalDate localDate = LocalDate.now();
		// Previous 60 days data
		// LocalDate minusDays = localDate.minusDays(60);
		LocalDate minusDays = localDate.minusDays(20);
		// Next 30 days data will be loading -Removed from 30 day's+ and added 10 days
		LocalDate plusDays = localDate.plusDays(20);

		try {
			String sql = "";
			System.out.println(user_id + " id " + user_role_id);
			if (user_role_id > 3 && user_role_id != 7) {
				sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date, "
						+ "tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, "
						+ "dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, "
						+ "tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, "
						+ "usr.user_id as usrId, usr.user_first_name as usrFirstName, usr.user_last_name as usrLastName, "
						+ "tttrn.ttrn_completed_date, tsk.task_cat_law_name as lawName, tsk.task_cat_law_name, tsk.task_cat_law_name as tLawName, tsk.task_implication, "
						+ "rwusr.user_first_name as rwFirstName, rwusr.user_last_name as rwLastName, "
						+ "fhusr.user_first_name asfhFirstName, fhusr.user_last_name as fhLastName, tttrn.auditDate, tttrn.reOpenDateWindow FROM trn_task_transactional tttrn "
						+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
						+ " JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND "
						+ "umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = '" + user_id + "' "
						+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
						+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
						+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
						+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
						+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
						+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
						+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
						+ "WHERE tttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND "
						+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
						+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays
						+ "' AND ((tttrn.ttrn_is_Task_Audited = 'No' AND isDocumentUpload = 1) OR isDocumentUpload = 0) AND loca.loca_id != 0 ";

				System.out.println("role > 3 & != 7 access : " + sql);

			} else {
				if (user_role_id == 3) {

					sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date, "
							+ "tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, "
							+ "dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, "
							+ "tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, "
							+ "usr.user_id as usrUserId, usr.user_first_name as usrFirstName, usr.user_last_name as usrLastName, "
							+ "tttrn.ttrn_completed_date,tsk.task_cat_law_name  as lawName, tsk.task_cat_law_name as lLname, tsk.task_cat_law_name, tsk.task_implication, "
							+ "rwusr.user_first_name as rwFirstName, rwusr.user_last_name as rwLastName, fhusr.user_first_name as fhFirstName, fhusr.user_last_name as fhLastName "
							+ " , tttrn.auditDate, tttrn.reOpenDateWindow FROM trn_task_transactional tttrn "
							+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
							+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
							+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
							+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
							+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
							+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
							+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
							+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id " + "WHERE "
							+ "(tmapp.tmap_pr_user_id = '" + user_id + "' OR  tmapp.tmap_rw_user_id = '" + user_id
							+ "' OR  tmapp.tmap_fh_user_id = '" + user_id + "') " + "AND "
							+ "tttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
							+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays
							+ "'  AND ((tttrn.ttrn_is_Task_Audited = 'No' AND isDocumentUpload = 1) OR isDocumentUpload = 0) AND loca.loca_id != 0 ";

					System.out.println("role 3 access : " + sql);

				} else {
					if (user_role_id == 2) {
						sql = "SELECT distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date, "
								+ "tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, "
								+ "loca.loca_id, loca.loca_name, dept.dept_id, "
								+ "dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, "
								+ "tsk.task_activity_when, tsk.task_activity, "
								+ "tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, "
								+ "usr.user_id as usrId, usr.user_first_name as usrFirstName, usr.user_last_name as usrLastName, "
								+ "tttrn.ttrn_completed_date,tsk.task_cat_law_name  as lawName, tsk.task_cat_law_name as tLawsNames, tsk.task_cat_law_name, "
								+ "tsk.task_implication, rwusr.user_first_name as rwFirstName, rwusr.user_last_name as rwLastName, fhusr.user_first_name as fhFirstName, fhusr.user_last_name as fhLastName "
								+ " , tttrn.auditDate, tttrn.reOpenDateWindow FROM trn_task_transactional tttrn "
								+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
								+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
								+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
								+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
								+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
								+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
								+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
								+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id " + "WHERE "
								+ "(tmapp.tmap_pr_user_id = '" + user_id + "' OR  tmapp.tmap_rw_user_id = '" + user_id
								+ "') " + "AND "
								+ "tttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
								+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays
								+ "'  AND ((tttrn.ttrn_is_Task_Audited = 'No' AND isDocumentUpload = 1) OR isDocumentUpload = 0) AND loca.loca_id != 0 ";

						System.out.println("role 2 access : " + sql);
					} else {
						if (user_role_id == 1) {
							sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, "
									+ "tttrn.ttrn_fh_due_date,tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, "
									+ "loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, "
									+ "tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, "
									+ "usr.user_id as usrId, "
									+ "usr.user_first_name as usrFirstName, usr.user_last_name as usrLastName,  tttrn.ttrn_completed_date,tsk.task_cat_law_name  as lawName, tsk.task_cat_law_name as ttLawsName, "
									+ "tsk.task_cat_law_name,tsk.task_implication, rwusr.user_first_name as rwFirstName, rwusr.user_last_name as rwLastName, "
									+ "fhusr.user_first_name as fhFirstName, fhusr.user_last_name as fhLastName "
									+ " , tttrn.auditDate, tttrn.reOpenDateWindow FROM trn_task_transactional tttrn "
									+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
									+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
									+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
									+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
									+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
									+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
									+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
									+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id WHERE "
									+ "(tmapp.tmap_pr_user_id = '" + user_id + "') " + "AND "
									+ "tttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND "
									+ "tsk.task_cat_law_name != 'Internal' " + "and tttrn.ttrn_legal_due_date BETWEEN '"
									+ minusDays + "' and '" + plusDays
									+ "'  AND ((tttrn.ttrn_is_Task_Audited = 'No' AND isDocumentUpload = 1) OR isDocumentUpload = 0) AND loca.loca_id != 0 ";

							System.out.println("role 1 Inside exevutor access : " + sql);

						} else {
							if (user_role_id == 7) {

								sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, "
										+ "tttrn.ttrn_fh_due_date,tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, "
										+ "loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, "
										+ "tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, "
										+ "usr.user_id as usrId, "
										+ "usr.user_first_name as useFirstName, usr.user_last_name as usrFirstName, tttrn.ttrn_completed_date,tsk.task_cat_law_name  as lawName, tsk.task_cat_law_name as ltLawName, "
										+ "tsk.task_cat_law_name, tsk.task_implication, rwusr.user_first_name as rwFirstName, rwusr.user_last_name as rwLastName, "
										+ "fhusr.user_first_name as fhFirstName, "
										+ "fhusr.user_last_name as fhLastName "
										+ " , tttrn.auditDate, tttrn.reOpenDateWindow FROM trn_task_transactional tttrn "
										+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
										+ " JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND "
										+ "umapp.umap_dept_id = tmapp.tmap_dept_id "
										+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
										+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
										+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
										+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
										+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
										+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
										+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
										+ "WHERE tttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND "
										+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
										+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays
										+ "' AND loca.loca_id != 0  ";
//										+ "AND ((tttrn.ttrn_is_Task_Audited = 'No' AND isDocumentUpload = 1) OR isDocumentUpload = 0) ";

								System.out.println("role 7 Inside Superadmin access : " + sql);
							}
						}
					}
				}
			}

//			System.out.println("Main Task Query: " + sql);
			Query query = em.createNativeQuery(sql);
			/*
			 * if (user_role_id != 7) { query.setParameter("user_id", user_id); }
			 */
			// query.setParameter("dept_id", dept_id);
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

//	@Cacheable(value = "overallComplianceGraphSubTask", key = "{#user_id, #user_role_id}")
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getOverallComplianceGraphSubTask(int user_id, int user_role_id) {
		try {
			LocalDate localDate = LocalDate.now();
			// Previous 60 days data
			LocalDate minusDays = localDate.minusDays(60);
			// Next 30 days data will be loading
			LocalDate plusDays = localDate.plusDays(30);

			String sql = "";

			if (user_role_id > 3) {
				sql = "SELECT sttrn.ttrn_sub_id, sttrn.ttrn_sub_client_task_id, sttrn.ttrn_sub_task_status ,sttrn.ttrn_sub_task_pr_due_date, "
						+ "sttrn.ttrn_sub_task_rw_date, sttrn.ttrn_sub_task_FH_due_date,sttrn.ttrn_sub_task_UH_due_date, sttrn.ttrn_sub_task_ENT_due_date, "
						+ "sttrn.ttrn_sub_task_submition_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name , "
						+ "tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, "
						+ "tsk.task_procedure, tsk.task_impact, stsk.sub_frequency, "
						+ "usr.user_id , usr.user_first_name, usr.user_last_name, "
						+ "sttrn.ttrn_sub_task_id,stsk.sub_equipment_number,stsk.sub_equipment_type "
						+ "FROM trn_sub_task_transactional sttrn "
						+ "LEFT JOIN cfg_task_user_mapping tmapp on sttrn.ttrn_sub_client_task_id = tmapp.tmap_client_tasks_id "
						+ " JOIN cfg_user_entity_mapping umapp on umapp.umap_user_id =:user_id "
						+ " JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
						+ " JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
						+ " JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
						+ " JOIN mst_task tsk on tsk.task_lexcare_task_id = tmapp.tmap_lexcare_task_id "
						+ " JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
						+ " JOIN mst_sub_task stsk ON stsk.sub_task_id = sttrn.ttrn_sub_task_id "
						+ "WHERE sttrn.ttrn_sub_task_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND stsk.sub_frequency !='User_Defined'"
						+ "and sttrn.ttrn_sub_task_ENT_due_date BETWEEN " + "'" + minusDays + "'" + " and " + "'"
						+ plusDays + "' group by sttrn.ttrn_sub_task_id ";
			} else {
				if (user_role_id == 3) {
					sql = "SELECT sttrn.ttrn_sub_id, sttrn.ttrn_sub_client_task_id, sttrn.ttrn_sub_task_status ,sttrn.ttrn_sub_task_pr_due_date, sttrn.ttrn_sub_task_rw_date, sttrn.ttrn_sub_task_FH_due_date,sttrn.ttrn_sub_task_UH_due_date, sttrn.ttrn_sub_task_ENT_due_date, sttrn.ttrn_sub_task_submition_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, stsk.sub_frequency, usr.user_id , usr.user_first_name, usr.user_last_name,sttrn.ttrn_sub_task_id,stsk.sub_equipment_number,stsk.sub_equipment_type "
							+ "FROM trn_sub_task_transactional sttrn "
							+ "LEFT JOIN cfg_task_user_mapping tmapp on sttrn.ttrn_sub_client_task_id = tmapp.tmap_client_tasks_id "
							+ " JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
							+ " JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
							+ " JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
							+ " JOIN mst_task tsk on tsk.task_lexcare_task_id = tmapp.tmap_lexcare_task_id "
							+ " JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
							+ " JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
							+ " JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
							+ " JOIN mst_sub_task stsk ON stsk.sub_task_id = sttrn.ttrn_sub_task_id WHERE "
							+ "(tmapp.tmap_pr_user_id = :user_id OR  tmapp.tmap_rw_user_id = :user_id OR  tmapp.tmap_fh_user_id = :user_id) "
							+ "AND "
							+ "sttrn.ttrn_sub_task_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND stsk.sub_frequency !='User_Defined' "
							+ "and sttrn.ttrn_sub_task_ENT_due_date BETWEEN " + "'" + minusDays + "'" + " and " + "'"
							+ plusDays + "' group by sttrn.ttrn_sub_task_id";
				} else {

					if (user_role_id == 2) {
						sql = "SELECT sttrn.ttrn_sub_id, sttrn.ttrn_sub_client_task_id, sttrn.ttrn_sub_task_status ,sttrn.ttrn_sub_task_pr_due_date, sttrn.ttrn_sub_task_rw_date, sttrn.ttrn_sub_task_FH_due_date,sttrn.ttrn_sub_task_UH_due_date, sttrn.ttrn_sub_task_ENT_due_date, sttrn.ttrn_sub_task_submition_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, stsk.sub_frequency, usr.user_id , usr.user_first_name, usr.user_last_name,sttrn.ttrn_sub_task_id,stsk.sub_equipment_number,stsk.sub_equipment_type "
								+ "FROM trn_sub_task_transactional sttrn "
								+ "LEFT JOIN cfg_task_user_mapping tmapp on sttrn.ttrn_sub_client_task_id = tmapp.tmap_client_tasks_id "
								+ " JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
								+ " JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
								+ " JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
								+ " JOIN mst_task tsk on tsk.task_lexcare_task_id = tmapp.tmap_lexcare_task_id "
								+ " JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
								+ " JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
								+ " JOIN mst_sub_task stsk ON stsk.sub_task_id = sttrn.ttrn_sub_task_id WHERE "
								+ "(tmapp.tmap_pr_user_id = :user_id OR  tmapp.tmap_rw_user_id = :user_id) " + "AND "
								+ "sttrn.ttrn_sub_task_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND stsk.sub_frequency !='User_Defined' "
								+ "and sttrn.ttrn_sub_task_ENT_due_date BETWEEN " + "'" + minusDays + "'" + " and "
								+ "'" + plusDays + "' group by sttrn.ttrn_sub_task_id";
					} else {
						if (user_role_id == 1) {
							sql = "SELECT sttrn.ttrn_sub_id, sttrn.ttrn_sub_client_task_id, sttrn.ttrn_sub_task_status ,sttrn.ttrn_sub_task_pr_due_date, sttrn.ttrn_sub_task_rw_date, sttrn.ttrn_sub_task_FH_due_date,sttrn.ttrn_sub_task_UH_due_date, sttrn.ttrn_sub_task_ENT_due_date, sttrn.ttrn_sub_task_submition_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, stsk.sub_frequency, usr.user_id , usr.user_first_name, usr.user_last_name,sttrn.ttrn_sub_task_id,stsk.sub_equipment_number,stsk.sub_equipment_type "
									+ "FROM trn_sub_task_transactional sttrn "
									+ "LEFT JOIN cfg_task_user_mapping tmapp on sttrn.ttrn_sub_client_task_id = tmapp.tmap_client_tasks_id "
									+ " JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
									+ " JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
									+ " JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
									+ " JOIN mst_task tsk on tsk.task_lexcare_task_id = tmapp.tmap_lexcare_task_id "
									+ " JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
									+ " JOIN mst_sub_task stsk ON stsk.sub_task_id = sttrn.ttrn_sub_task_id WHERE "
									+ "(tmapp.tmap_pr_user_id = :user_id) " + "AND "
									+ "sttrn.ttrn_sub_task_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND stsk.sub_frequency !='User_Defined' "
									+ "and sttrn.ttrn_sub_task_ENT_due_date BETWEEN " + "'" + minusDays + "'" + " and "
									+ "'" + plusDays + "' group by sttrn.ttrn_sub_task_id";
						}
					}
				}
			}
			Query query = em.createNativeQuery(sql);
			System.out.println("Subtask Query:" + sql);
			query.setParameter("user_id", user_id);
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
	public <T> List<T> searchGetOverallComplianceGraph(int user_id, int user_role_id, String fromDate, String toDate,
			String orgaId) {
		System.out.println("orgaId check if null is coming :  " + orgaId);
		try {
			String sql = "";

			if (user_role_id > 3 && user_role_id <= 6) {
				sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, "
						+ "tttrn.ttrn_fh_due_date,tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, "
						+ "loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , "
						+ "tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, "
						+ "tttrn.ttrn_frequency_for_operation, "
						+ "usr.user_id as usrId, usr.user_first_name as usrFirstName, usr.user_last_name as usrLastName,  tsk.task_cat_law_name,"
						+ "usrrw.user_id as rwUSerId, "
						+ "usrrw.user_first_name as rwFirstName, usrrw.user_last_name as rwLastName, usrfh.user_id as fhUserId, usrfh.user_first_name as fhLastNames, "
						+ "usrfh.user_last_name as fhLastName, tttrn.ttrn_completed_date, tsk.task_cat_law_name as r1LawName, tsk.task_cat_law_name as laNames, tsk.task_implication  "
						+ "FROM trn_task_transactional tttrn "
						+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
						+ " JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = '"
						+ user_id + "' " + " LEFT JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
						+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
						+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
						+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
						+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
						+ "JOIN mst_user usrrw on usrrw.user_id = tmapp.tmap_rw_user_id "
						+ "JOIN mst_user usrfh on usrfh.user_id = tmapp.tmap_fh_user_id "
						+ "WHERE (tttrn.ttrn_status = 'Active' OR tttrn.ttrn_status = 'Completed' OR tttrn.ttrn_status='Partially_Completed' OR "
						+ "tttrn.ttrn_status='Re_Opened') AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND "
						+ "tmapp.tmap_enable_status != 0 AND ((tttrn.ttrn_is_Task_Audited = 'No' AND isDocumentUpload = 1) OR isDocumentUpload = 0) AND tmapp.tmap_enable_status != 0 ";
				if (!fromDate.equals("0") && !toDate.equals("0")) {
					sql += " AND tttrn.ttrn_legal_due_date BETWEEN '" + fromDate + "' AND '" + toDate + "' ";
				}
				if (!orgaId.equals("0") && !orgaId.equals("null")) {
					sql += " AND orga.orga_id = '" + orgaId + "'";
				}
				// System.out.println("sql user_role_id > 3 : " + sql);

			} else if (user_role_id == 3) {

				sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date, "
						+ "tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, "
						+ "dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, "
						+ "tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id as userId, usr.user_first_name as useFirstName , usr.user_last_name as usrLastName, "
						+ "tsk.task_cat_law_name as tLaswName, usrrw.user_id as rwUsrId , "
						+ "usrrw.user_first_name as rwFirstName , usrrw.user_last_name as rswLastName, "
						+ "usrfh.user_id as fhUserId , usrfh.user_first_name as fhFirstNamea, usrfh.user_last_name as fshLastName, tttrn.ttrn_completed_date, "
						+ "tsk.task_cat_law_name, tsk.task_cat_law_name as rLawsNammes, tsk.task_implication  "
						+ "FROM trn_task_transactional tttrn "
						+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
						+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
						+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
						+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
						+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
						+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
						+ "JOIN mst_user usrrw on usrrw.user_id = tmapp.tmap_rw_user_id "
						+ "JOIN mst_user usrfh on usrfh.user_id = tmapp.tmap_fh_user_id " + "WHERE "
						+ "(tmapp.tmap_pr_user_id = '" + user_id + "' OR  tmapp.tmap_rw_user_id = '" + user_id
						+ "' OR  tmapp.tmap_fh_user_id = '" + user_id + "') " + "AND "
						+ "(tttrn.ttrn_status = 'Active' OR tttrn.ttrn_status = 'Completed' OR tttrn.ttrn_status='Partially_Completed' OR "
						+ "tttrn.ttrn_status='Re_Opened') AND tttrn.ttrn_frequency_for_operation !='User_Defined' "
						+ "AND tmapp.tmap_enable_status != 0  AND ((tttrn.ttrn_is_Task_Audited = 'No' AND isDocumentUpload = 1) OR isDocumentUpload = 0)  ";
				if (!fromDate.equals("0") && !toDate.equals("0")) {
					sql += " AND tttrn.ttrn_legal_due_date BETWEEN '" + fromDate + "' AND '" + toDate + "'";
				}
				if (!orgaId.equals("0") && !orgaId.equals("null")) {
					sql += " AND orga.orga_id = '" + orgaId + "'";
				}
				// + "AND tttrn.ttrn_created_at BETWEEN '" + fromDate + "' AND '" + toDate +
				// "'";
				// System.out.println("sql user_role_id == 3 : " + sql);
			}

			else {
				if (user_role_id == 2) {
					sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date, "
							+ "tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, "
							+ "dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, "
							+ "tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, "
							+ "usr.user_id as userId , usr.user_first_name as useFirstName, usr.user_last_name as usrLastName, tsk.task_cat_law_name as tName, "
							+ "usrrw.user_id as rwUsrId , usrrw.user_first_name as rwFirstName , usrrw.user_last_name as rswLastName, "
							+ "usrfh.user_id as fhUserId, usrfh.user_first_name as fhFirstName, usrfh.user_last_name as fahLastName, tttrn.ttrn_completed_date, "
							+ "tsk.task_cat_law_name, tsk.task_cat_law_name as tasLawsNames, tsk.task_implication  "
							+ "FROM trn_task_transactional tttrn "
							+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
							+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
							+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
							+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
							+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
							+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
							+ "JOIN mst_user usrrw on usrrw.user_id = tmapp.tmap_rw_user_id "
							+ "JOIN mst_user usrfh on usrfh.user_id = tmapp.tmap_fh_user_id " + "WHERE "
							+ "(tmapp.tmap_pr_user_id = '" + user_id + "' OR  tmapp.tmap_rw_user_id = '" + user_id
							+ "') " + "AND "
							+ "(tttrn.ttrn_status = 'Active' OR tttrn.ttrn_status = 'Completed' OR tttrn.ttrn_status='Partially_Completed' OR "
							+ "tttrn.ttrn_status='Re_Opened') AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND "
							+ "tmapp.tmap_enable_status != 0 AND ((tttrn.ttrn_is_Task_Audited = 'No' AND isDocumentUpload = 1) OR isDocumentUpload = 0) ";
					if (!fromDate.equals("0") && !toDate.equals("0")) {
						sql += " AND tttrn.ttrn_legal_due_date BETWEEN '" + fromDate + "' AND '" + toDate + "'";
					}
					if (!orgaId.equals("0") && !orgaId.equals("null")) {
						sql += " AND orga.orga_id = '" + orgaId + "'";
					}
					// + "AND tttrn.ttrn_created_at BETWEEN '" + fromDate + "' AND '" + toDate +
					// "'";
					System.out.println("sql user_role_id == 2 : " + sql);
				} else {
					if (user_role_id == 1) {
						sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date, "
								+ "tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, "
								+ "dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, "
								+ "tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, "
								+ "usr.user_id as userId , usr.user_first_name as useFirstName, usr.user_last_name as usrLastName, "
								+ "tsk.task_cat_law_name,"
								+ "usrrw.user_id as rwUsrId, usrrw.user_first_name as rwFirstName, usrrw.user_last_name as rwLastName, "
								+ "usrfh.user_id as fhUserId , usrfh.user_first_name as fhFirstName, usrfh.user_last_name as fhLastName, "
								+ "tttrn.ttrn_completed_date, tsk.task_cat_law_name as tLawNames, tsk.task_cat_law_name as LawNames, tsk.task_implication  "
								+ "FROM trn_task_transactional tttrn "
								+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
								+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
								+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
								+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
								+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
								+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
								+ "JOIN mst_user usrrw on usrrw.user_id = tmapp.tmap_rw_user_id "
								+ "JOIN mst_user usrfh on usrfh.user_id = tmapp.tmap_fh_user_id " + "WHERE "
								+ "(tmapp.tmap_pr_user_id = '" + user_id + "') " + "AND "
								+ "(tttrn.ttrn_status = 'Active' OR tttrn.ttrn_status = 'Completed' OR tttrn.ttrn_status='Partially_Completed' "
								+ "OR tttrn.ttrn_status='Re_Opened') AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND "
								+ "tmapp.tmap_enable_status != 0  AND ((tttrn.ttrn_is_Task_Audited = 'No' AND isDocumentUpload = 1) OR isDocumentUpload = 0) ";
						if (!fromDate.equals("0") && !toDate.equals("0")) {
							sql += " AND tttrn.ttrn_legal_due_date BETWEEN '" + fromDate + "' AND '" + toDate + "'";
						}
						if (!orgaId.equals("0") && !orgaId.equals("null")) {
							sql += " AND orga.orga_id = '" + orgaId + "'";
						}

						// System.out.println("sql user_role_id == 1 : " + sql);
					} else {
						if (user_role_id == 7) {
							sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, "
									+ "tttrn.ttrn_fh_due_date,tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, "
									+ "loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , "
									+ "tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, "
									+ "tttrn.ttrn_frequency_for_operation, "
									+ "usr.user_id as userId , usr.user_first_name as useFirstName, usr.user_last_name as usrLastName, "
									+ "tsk.task_cat_law_name, "
									+ "usrrw.user_id as rwUsrId, usrrw.user_first_name as rwFirstName, usrrw.user_last_name as rwLastName, "
									+ "usrfh.user_id as fhUserId , usrfh.user_first_name as fhFirstName, usrfh.user_last_name as fhLastName, "
									+ "tttrn.ttrn_completed_date, "
									+ "tsk.task_cat_law_name as tLaNames, tsk.task_cat_law_name as tLawNames, tsk.task_implication  "
									+ "FROM trn_task_transactional tttrn "
									+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
									+ " JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id "
									+ "AND umapp.umap_dept_id = tmapp.tmap_dept_id "
									+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
									+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
									+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
									+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
									+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
									+ "JOIN mst_user usrrw on usrrw.user_id = tmapp.tmap_rw_user_id "
									+ "JOIN mst_user usrfh on usrfh.user_id = tmapp.tmap_fh_user_id "
									+ "WHERE (tttrn.ttrn_status = 'Active' OR tttrn.ttrn_status = 'Completed' OR tttrn.ttrn_status='Partially_Completed' OR "
									+ "tttrn.ttrn_status='Re_Opened') AND tttrn.ttrn_frequency_for_operation !='User_Defined' "
									+ "AND tmapp.tmap_enable_status != 0 ";
//									+ "AND ((tttrn.ttrn_is_Task_Audited = 'No' AND isDocumentUpload = 1) OR isDocumentUpload = 0) ";

							if (!fromDate.equals("0") && !toDate.equals("0")) {
								sql += " AND tttrn.ttrn_legal_due_date BETWEEN '" + fromDate + "' AND '" + toDate + "'";
							}
							if (!orgaId.equals("0") && !orgaId.equals("null")) {
								sql += " AND orga.orga_id = '" + orgaId + "'";
							}
							System.out.println("sql user_role_id for SUPERADMIN : " + sql);

						}
					}
				}
			}
			Query query = em.createNativeQuery(sql);
//			System.out.println("sql search dashboard :  " + sql);
			// query.setParameter("user_id", user_id);
//			System.out.println("\n user_id : " + user_id + "\t user_role : " + user_role_id);
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
	public List<Object> searchGraph(String jsonString, int user_id, int user_role_id) {
		try {
			String sql = "";
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);
			Date from_date = sdfIn.parse(jsonObject.get("date_from").toString());
			Date to_date = sdfIn.parse(jsonObject.get("date_to").toString());

			if (user_role_id > 2) {
				sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date, "
						+ "tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, "
						+ "loca.loca_name, "
						+ "dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, "
						+ "tsk.task_activity_when, "
						+ "tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, "
						+ "usr.user_id , usr.user_first_name, usr.user_last_name, "
						+ "loca.loca_category, tttrn.ttrn_completed_date FROM trn_task_transactional tttrn "
						+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
						+ "JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = :user_id "
						+ "JOIN mst_task tsk on tsk.task_lexcare_task_id = tmapp.tmap_lexcare_task_id "
						+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
						+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
						+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
						+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
						+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
						+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
						+ "WHERE (tttrn.ttrn_status = 'Active' OR tttrn.ttrn_status = 'Completed' OR tttrn.ttrn_status='Partially_Completed' OR tttrn.ttrn_status='Re_Opened') AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND "
						+ "((tttrn.ttrn_legal_due_date BETWEEN :from_date AND :to_date))";
			} else {
				if (user_role_id == 2) {
					sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date,tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id , usr.user_first_name, usr.user_last_name, loca.loca_category, tttrn.ttrn_completed_date "
							+ "FROM trn_task_transactional tttrn "
							+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
							+ "JOIN mst_task tsk on tsk.task_lexcare_task_id = tmapp.tmap_lexcare_task_id "
							+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
							+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
							+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
							+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
							+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
							+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id " + "WHERE "
							+ "(tmapp.tmap_pr_user_id = :user_id OR  tmapp.tmap_rw_user_id = :user_id) " + "AND "
							+ "(tttrn.ttrn_status = 'Active' OR tttrn.ttrn_status = 'Completed' OR tttrn.ttrn_status='Partially_Completed' OR tttrn.ttrn_status='Re_Opened') AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND"
							+ "((tttrn.ttrn_legal_due_date BETWEEN :from_date AND :to_date)) ";
				} else {
					if (user_role_id == 1) {
						sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date,tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id , usr.user_first_name, usr.user_last_name, loca.loca_category, tttrn.ttrn_completed_date "
								+ "FROM trn_task_transactional tttrn "
								+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
								+ "JOIN mst_task tsk on tsk.task_lexcare_task_id = tmapp.tmap_lexcare_task_id "
								+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
								+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
								+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
								+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
								+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
								+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id " + "WHERE "
								+ "(tmapp.tmap_pr_user_id = :user_id) " + "AND "
								+ "(tttrn.ttrn_status = 'Active' OR tttrn.ttrn_status = 'Completed' OR tttrn.ttrn_status='Partially_Completed' OR tttrn.ttrn_status='Re_Opened') AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND "
								+ "((tttrn.ttrn_legal_due_date BETWEEN :from_date AND :to_date))";
					}
				}
			}
			Query query = em.createNativeQuery(sql);
			query.setParameter("user_id", user_id);
			query.setParameter("from_date", from_date);
			query.setParameter("to_date", to_date);
			// query.setParameter("dept_id", dept_id);
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
	public <T> List<T> getOverallComplianceGrapheExportReport(int user_id, int user_role_id, String jsonString) {
		try {
			try {
				JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);

				LocalDate localDate = LocalDate.now();
				String fromDate = null;
				String toDate = null;
				// System.out.println("TO DATE "+jsonObject.get("toDate"));

				/*
				 * if (jsonObject.get("toDate") != null && jsonObject.get("fromDate")!= null) {
				 * fromDate = jsonObject.get("fromDate").toString(); toDate =
				 * jsonObject.get("toDate").toString(); }
				 */
				// Previous 60 days data
				LocalDate minusDays = localDate.minusDays(60);
				// Next 30 days data will be loading
				LocalDate plusDays = localDate.plusDays(30);

				String status = jsonObject.get("status").toString();
				String entity_name = jsonObject.get("entity_name").toString();
				String sql = "";
				System.out.println(status);
				if (user_role_id > 3 && user_role_id <= 6) {
					sql = "SELECT tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date,tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id , usr.user_first_name, usr.user_last_name,tttrn.ttrn_completed_date,tsk.task_cat_law_name,tttrn.ttrn_performer_comments "
							+ "FROM trn_task_transactional tttrn "
							+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
							+ "JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = :user_id "
							+ "JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
							+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
							+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
							+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
							+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
							+ "WHERE tttrn.ttrn_status != 'Inactive' AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' ";
				} else {
					if (user_role_id == 3) {
						sql = "SELECT tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date,tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id , usr.user_first_name, usr.user_last_name,tttrn.ttrn_completed_date,tsk.task_cat_law_name,tttrn.ttrn_performer_comments "
								+ "FROM trn_task_transactional tttrn "
								+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
								+ "JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
								+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
								+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
								+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
								+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
								+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_rw_user_id "
								+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_fh_user_id " + "WHERE "
								+ "(tmapp.tmap_pr_user_id = :user_id OR  tmapp.tmap_rw_user_id = :user_id OR  tmapp.tmap_fh_user_id = :user_id) "
								+ "AND "
								+ "tttrn.ttrn_status != 'Inactive' AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' ";
					} else {
						if (user_role_id == 2) {
							sql = "SELECT tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date,tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id , usr.user_first_name, usr.user_last_name,tttrn.ttrn_completed_date,tsk.task_cat_law_name,tttrn.ttrn_performer_comments "
									+ "FROM trn_task_transactional tttrn "
									+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
									+ "JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
									+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
									+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
									+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
									+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id " + "WHERE "
									+ "(tmapp.tmap_pr_user_id = :user_id OR  tmapp.tmap_rw_user_id = :user_id) "
									+ "AND "
									+ "tttrn.ttrn_status != 'Inactive' AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' ";
						} else {
							if (user_role_id == 1) {
								sql = "SELECT tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date,tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id , usr.user_first_name, usr.user_last_name,tttrn.ttrn_completed_date,tsk.task_cat_law_name,tttrn.ttrn_performer_comments "
										+ "FROM trn_task_transactional tttrn "
										+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
										+ "JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
										+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
										+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
										+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
										+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id " + "WHERE "
										+ "(tmapp.tmap_pr_user_id = :user_id) " + "AND "
										+ "tttrn.ttrn_status != 'Inactive' AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' ";
							} else {
								if (user_role_id == 7) {
									sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, "
											+ "tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date,tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, "
											+ "tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, "
											+ "dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, "
											+ "tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, "
											+ "tttrn.ttrn_frequency_for_operation, "
											+ "usr.user_id , usr.user_first_name, usr.user_last_name, "
											+ "tttrn.ttrn_completed_date,tsk.task_cat_law_name,tttrn.ttrn_performer_comments "
											+ "FROM trn_task_transactional tttrn "
											+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
											+ "JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND umapp.umap_dept_id = tmapp.tmap_dept_id "
											+ "JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
											+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
											+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
											+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
											+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
											+ "WHERE tttrn.ttrn_status != 'Inactive' AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' ";
								}
							}
						}
					}
				}
				System.out.println(jsonObject.get("toDate"));
				if (jsonObject.get("toDate") != null && jsonObject.get("fromDate") != null) {
					fromDate = jsonObject.get("fromDate").toString();
					toDate = jsonObject.get("toDate").toString();
					sql += "and tttrn.ttrn_legal_due_date BETWEEN '" + fromDate + "' and '" + toDate + "'";

				} else {

					sql += "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays + "'";

				}

				if (status.equalsIgnoreCase("Complied")) {

					sql += " and  tttrn.ttrn_status ='Completed' and DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d')";

					if (!entity_name.equalsIgnoreCase("0")) {
						sql += " AND orga.orga_name = '" + entity_name + "'";
					}

				}
				if (status.equalsIgnoreCase("Delayed")) {
					sql += " and  tttrn.ttrn_status ='Completed' and tttrn.ttrn_legal_due_date < tttrn.ttrn_submitted_date and tttrn.ttrn_legal_due_date < tttrn.ttrn_completed_date ";

					if (!entity_name.equalsIgnoreCase("0")) {
						sql += " AND orga.orga_name = '" + entity_name + "'";
					}
				}
				if (status.equalsIgnoreCase("DelayedReported")) {
					sql += " and  tttrn.ttrn_status ='Completed' and  DATE_FORMAT(ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(ttrn_legal_due_date, '%Y/%m/%d')) ";

					if (!entity_name.equalsIgnoreCase("0")) {
						sql += " AND orga.orga_name = '" + entity_name + "'";
					}
				}
				if (status.equalsIgnoreCase("PosingRisk")) {
					sql += " and  tttrn.ttrn_status ='Active' and  tttrn.ttrn_pr_due_date < curdate() and (tttrn.ttrn_legal_due_date > curdate() or tttrn.ttrn_legal_due_date = curdate()) ";
					if (!entity_name.equalsIgnoreCase("0")) {
						sql += " AND orga.orga_name = '" + entity_name + "'";
					}
				}
				if (status.equalsIgnoreCase("NonComplied")) {
					sql += " and  tttrn.ttrn_status ='Active' and  tttrn.ttrn_legal_due_date < curdate() ";
					if (!entity_name.equalsIgnoreCase("0")) {
						sql += " AND orga.orga_name = '" + entity_name + "'";
					}
				}
				if (status.equalsIgnoreCase("Waiting For Approval")) {

					sql += "and  tttrn.ttrn_status ='Partially_Completed'";
					if (!entity_name.equalsIgnoreCase("0")) {
						sql += " AND orga.orga_name = '" + entity_name + "'";
					}
				}
				if (status.equalsIgnoreCase("Re-Opened")) {
					sql += "and  tttrn.ttrn_status ='Re_Opened'";
					if (!entity_name.equalsIgnoreCase("0")) {
						sql += " AND orga.orga_name = '" + entity_name + "'";
					}
				}

				System.out.println(sql);
				Query query = em.createNativeQuery(sql);
				if (user_role_id != 7) {
					query.setParameter("user_id", user_id);
				}
				// query.setParameter("dept_id", dept_id);
				return query.getResultList();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (em != null) {
					em.close();
				}
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return null;
	}

	@Override
	public List<SubTaskTranscational> getSubTaskHitoryByclientTaskID(String ttrn_sub_task_id) {
		try {
			TypedQuery query = em.createQuery(
					"FROM " + SubTaskTranscational.class.getName()
							+ " WHERE ttrn_sub_task_id=:ttrn_sub_task_id ORDER BY ttrn_sub_task_ENT_due_date DESC ",
					SubTaskTranscational.class);
			query.setParameter("ttrn_sub_task_id", ttrn_sub_task_id);
			if (!query.getResultList().isEmpty())
				return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Object> getSearchComplianceGraphSubTask(int user_id, int user_role_id, String fromDate, String toDate,
			String orgaId) {
		try {

			String sql = "";

			if (user_role_id > 3) {
				sql = "SELECT sttrn.ttrn_sub_id, sttrn.ttrn_sub_client_task_id, sttrn.ttrn_sub_task_status ,sttrn.ttrn_sub_task_pr_due_date, "
						+ "sttrn.ttrn_sub_task_rw_date, sttrn.ttrn_sub_task_FH_due_date,sttrn.ttrn_sub_task_UH_due_date, sttrn.ttrn_sub_task_ENT_due_date, "
						+ "sttrn.ttrn_sub_task_submition_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name , "
						+ "tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, "
						+ "tsk.task_activity, tsk.task_procedure, tsk.task_impact, stsk.sub_frequency, "
						+ "usr.user_id , usr.user_first_name, usr.user_last_name, "
						+ "sttrn.ttrn_sub_task_id,stsk.sub_equipment_number,stsk.sub_equipment_type "
						+ "FROM trn_sub_task_transactional sttrn "
						+ "LEFT JOIN cfg_task_user_mapping tmapp on sttrn.ttrn_sub_client_task_id = tmapp.tmap_client_tasks_id "
						+ "JOIN cfg_user_entity_mapping umapp on umapp.umap_user_id =:user_id "
						+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
						+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
						+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
						+ "JOIN mst_task tsk on tsk.task_lexcare_task_id = tmapp.tmap_lexcare_task_id "
						+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
						+ "JOIN mst_sub_task stsk ON stsk.sub_task_id = sttrn.ttrn_sub_task_id "
						+ "WHERE sttrn.ttrn_sub_task_status != 'Inactive' AND stsk.sub_frequency !='User_Defined' ";

				if (!fromDate.equals("0") && !toDate.equals("0")) {
					sql += " AND sttrn.ttrn_sub_task_ENT_due_date BETWEEN '" + fromDate + "' AND '" + toDate
							+ "' group by sttrn.ttrn_sub_task_id";
				}
				if (!orgaId.equals("0") && !orgaId.equals("null")) {
					sql += " AND orga.orga_id = '" + orgaId + "'";
				}
			} else {
				if (user_role_id == 3) {
					sql = "SELECT sttrn.ttrn_sub_id, sttrn.ttrn_sub_client_task_id, sttrn.ttrn_sub_task_status ,sttrn.ttrn_sub_task_pr_due_date, sttrn.ttrn_sub_task_rw_date, sttrn.ttrn_sub_task_FH_due_date,sttrn.ttrn_sub_task_UH_due_date, sttrn.ttrn_sub_task_ENT_due_date, sttrn.ttrn_sub_task_submition_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, stsk.sub_frequency, usr.user_id , usr.user_first_name, usr.user_last_name,sttrn.ttrn_sub_task_id,stsk.sub_equipment_number,stsk.sub_equipment_type "
							+ "FROM trn_sub_task_transactional sttrn "
							+ "LEFT JOIN cfg_task_user_mapping tmapp on sttrn.ttrn_sub_client_task_id = tmapp.tmap_client_tasks_id "
							+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
							+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
							+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
							+ "JOIN mst_task tsk on tsk.task_lexcare_task_id = tmapp.tmap_lexcare_task_id "
							+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
							+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
							+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
							+ "JOIN mst_sub_task stsk ON stsk.sub_task_id = sttrn.ttrn_sub_task_id WHERE "
							+ "(tmapp.tmap_pr_user_id = :user_id OR  tmapp.tmap_rw_user_id = :user_id OR  tmapp.tmap_fh_user_id = :user_id) "
							+ "AND "
							+ "sttrn.ttrn_sub_task_status != 'Inactive' AND stsk.sub_frequency !='User_Defined' ";
					if (!fromDate.equals("0") && !toDate.equals("0")) {
						sql += " AND sttrn.ttrn_sub_task_ENT_due_date BETWEEN '" + fromDate + "' AND '" + toDate
								+ "' group by sttrn.ttrn_sub_task_id";
					}
					if (!orgaId.equals("0") && !orgaId.equals("null")) {
						sql += " AND orga.orga_id = '" + orgaId + "'";
					}
				} else {

					if (user_role_id == 2) {
						sql = "SELECT sttrn.ttrn_sub_id, sttrn.ttrn_sub_client_task_id, sttrn.ttrn_sub_task_status ,sttrn.ttrn_sub_task_pr_due_date, sttrn.ttrn_sub_task_rw_date, sttrn.ttrn_sub_task_FH_due_date,sttrn.ttrn_sub_task_UH_due_date, sttrn.ttrn_sub_task_ENT_due_date, sttrn.ttrn_sub_task_submition_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, stsk.sub_frequency, usr.user_id , usr.user_first_name, usr.user_last_name,sttrn.ttrn_sub_task_id,stsk.sub_equipment_number,stsk.sub_equipment_type "
								+ "FROM trn_sub_task_transactional sttrn "
								+ "LEFT JOIN cfg_task_user_mapping tmapp on sttrn.ttrn_sub_client_task_id = tmapp.tmap_client_tasks_id "
								+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
								+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
								+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
								+ "JOIN mst_task tsk on tsk.task_lexcare_task_id = tmapp.tmap_lexcare_task_id "
								+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
								+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
								+ "JOIN mst_sub_task stsk ON stsk.sub_task_id = sttrn.ttrn_sub_task_id WHERE "
								+ "(tmapp.tmap_pr_user_id = :user_id OR  tmapp.tmap_rw_user_id = :user_id) " + "AND "
								+ "sttrn.ttrn_sub_task_status != 'Inactive' AND stsk.sub_frequency !='User_Defined' ";
						if (!fromDate.equals("0") && !toDate.equals("0")) {
							sql += " AND sttrn.ttrn_sub_task_ENT_due_date BETWEEN '" + fromDate + "' AND '" + toDate
									+ "' group by sttrn.ttrn_sub_task_id ";
						}
						if (!orgaId.equals("0") && !orgaId.equals("null")) {
							sql += " AND orga.orga_id = '" + orgaId + "'";
						}
					} else {
						if (user_role_id == 1) {
							sql = "SELECT sttrn.ttrn_sub_id, sttrn.ttrn_sub_client_task_id, sttrn.ttrn_sub_task_status ,sttrn.ttrn_sub_task_pr_due_date, sttrn.ttrn_sub_task_rw_date, sttrn.ttrn_sub_task_FH_due_date,sttrn.ttrn_sub_task_UH_due_date, sttrn.ttrn_sub_task_ENT_due_date, sttrn.ttrn_sub_task_submition_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, stsk.sub_frequency, usr.user_id , usr.user_first_name, usr.user_last_name,sttrn.ttrn_sub_task_id,stsk.sub_equipment_number,stsk.sub_equipment_type "
									+ "FROM trn_sub_task_transactional sttrn "
									+ "LEFT JOIN cfg_task_user_mapping tmapp on sttrn.ttrn_sub_client_task_id = tmapp.tmap_client_tasks_id "
									+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
									+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
									+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
									+ "JOIN mst_task tsk on tsk.task_lexcare_task_id = tmapp.tmap_lexcare_task_id "
									+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
									+ "JOIN mst_sub_task stsk ON stsk.sub_task_id = sttrn.ttrn_sub_task_id WHERE "
									+ "(tmapp.tmap_pr_user_id = :user_id) " + "AND "
									+ "sttrn.ttrn_sub_task_status != 'Inactive' AND stsk.sub_frequency !='User_Defined' ";
							if (!fromDate.equals("0") && !toDate.equals("0")) {
								sql += " AND sttrn.ttrn_sub_task_ENT_due_date BETWEEN '" + fromDate + "' AND '" + toDate
										+ "' group by sttrn.ttrn_sub_task_id ";
							}
							if (!orgaId.equals("0") && !orgaId.equals("null")) {
								sql += " AND orga.orga_id = '" + orgaId + "'";
							}
						}
					}
				}
			}
			Query query = em.createNativeQuery(sql);
			System.out.println("Subtask Query:" + sql);
			query.setParameter("user_id", user_id);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
