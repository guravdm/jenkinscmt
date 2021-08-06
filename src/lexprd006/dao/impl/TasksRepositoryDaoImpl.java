package lexprd006.dao.impl;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lexprd006.dao.TasksRepositoryDao;

/*
 * Author: Mahesh Kharote
 * Date: 11/11/2016
 * Purpose: DAO Impl for Functions
 * 
 * 
 * 
 * */

@Repository(value = "tasksRepositoryDao")
@Transactional
public class TasksRepositoryDaoImpl implements TasksRepositoryDao {

	@PersistenceContext
	private EntityManager em;

	// Method Written By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Get all tasks from repository To DB
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getAllTask(int user_id, int user_role_id) {
		return null;
		/*
		 * try { String sql = "";
		 * 
		 * if(user_role_id > 3){ sql =
		 * "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id, prusr.user_first_name, prusr.user_last_name , rwusr.user_id , rwusr.user_first_name , rwusr.user_last_name, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, fhusr.user_id , fhusr.user_first_name , fhusr.user_last_name,tttrn.ttrn_id , tsk.task_frequency "
		 * + "FROM cfg_task_user_mapping tmapp " +
		 * "LEFT JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
		 * +
		 * "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id AND (tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) "
		 * + "LEFT JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id " +
		 * "JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = :user_id "
		 * + "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id " +
		 * "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id " +
		 * "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id " +
		 * "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id " +
		 * "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id " +
		 * "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id " +
		 * "WHERE tmapp.tmap_enable_status = 1 " + "AND ttttrn.ttrn_id IS NULL "; }
		 * else{ if(user_role_id == 3){ sql =
		 * "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id, prusr.user_first_name, prusr.user_last_name , rwusr.user_id , rwusr.user_first_name , rwusr.user_last_name, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, fhusr.user_id , fhusr.user_first_name , fhusr.user_last_name,tttrn.ttrn_id , tsk.task_frequency "
		 * + "FROM cfg_task_user_mapping tmapp " +
		 * "LEFT JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
		 * +
		 * "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id AND (tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) "
		 * + "LEFT JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id " +
		 * "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id " +
		 * "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id " +
		 * "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id " +
		 * "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id " +
		 * "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id " +
		 * "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id " +
		 * "WHERE tmapp.tmap_enable_status = 1 " + "AND ttttrn.ttrn_id IS NULL " +
		 * "AND (tmapp.tmap_pr_user_id = :user_id OR tmapp.tmap_rw_user_id = :user_id OR tmapp.tmap_fh_user_id = :user_id)"
		 * ; } else{ if(user_role_id == 2){ sql =
		 * "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id, prusr.user_first_name, prusr.user_last_name , rwusr.user_id , rwusr.user_first_name , rwusr.user_last_name, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, fhusr.user_id , fhusr.user_first_name , fhusr.user_last_name,tttrn.ttrn_id , tsk.task_frequency "
		 * + "FROM cfg_task_user_mapping tmapp " +
		 * "LEFT JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
		 * +
		 * "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id AND (tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) "
		 * + "LEFT JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id " +
		 * "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id " +
		 * "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id " +
		 * "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id " +
		 * "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id " +
		 * "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id " +
		 * "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id " +
		 * "WHERE tmapp.tmap_enable_status = 1 " + "AND ttttrn.ttrn_id IS NULL " +
		 * "AND (tmapp.tmap_pr_user_id = :user_id OR tmapp.tmap_rw_user_id = :user_id)";
		 * } else{ if(user_role_id == 1){ sql =
		 * "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id, prusr.user_first_name, prusr.user_last_name , rwusr.user_id , rwusr.user_first_name , rwusr.user_last_name, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, fhusr.user_id , fhusr.user_first_name , fhusr.user_last_name,tttrn.ttrn_id , tsk.task_frequency "
		 * + "FROM cfg_task_user_mapping tmapp " +
		 * "LEFT JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
		 * +
		 * "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id AND (tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) "
		 * + "LEFT JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id " +
		 * "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id " +
		 * "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id " +
		 * "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id " +
		 * "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id " +
		 * "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id " +
		 * "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id " +
		 * "WHERE tmapp.tmap_enable_status = 1 " + "AND ttttrn.ttrn_id IS NULL " +
		 * "AND (tmapp.tmap_pr_user_id = :user_id)"; } } } }
		 * 
		 * 
		 * System.out.println("In list all for repository"); Query query =
		 * em.createQuery(sql); query.setParameter("user_id", user_id);
		 * //query.setParameter("dept_id", dept_id); return query.getResultList(); }
		 * catch (Exception e) { e.printStackTrace(); } return null;
		 */}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getClientTaskIdFromDefaultConfiguartion(String jsonString) {
		try {
			String sql = "SELECT dtco_id,dtco_client_task_id,dtco_after_before FROM cfg_default_task_configuration";
			Query query = em.createQuery(sql);
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

	// Method Created By: Harshad Padole
	// Method Purpose: getTask for export
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> gerTaskForExport(String json, HttpSession session) {
		try {
			// JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);

			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			int user_role_id = Integer.parseInt(session.getAttribute("sess_role_id").toString());

			String sql = "";

			if (user_role_id > 3 && user_role_id <= 6) {
				sql = "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id, prusr.user_first_name, prusr.user_last_name , rwusr.user_id , rwusr.user_first_name , rwusr.user_last_name, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id "
						+ ",tsk.task_country_name,tsk.task_state_name,tsk.task_effective_date,tsk.task_excemption_criteria,tsk.task_fine_amount,tsk.task_form_no,coalesce(tttrn.ttrn_impact_on_organization, tsk.task_impact_on_organization) as impact_on_organization,coalesce(tttrn.ttrn_impact_on_unit, tsk.task_impact_on_unit) as impact_on_unit, tsk.task_implication,tsk.task_imprisonment_duration,tsk.task_imprisonment_implies_to,tsk.task_level,tsk.task_linked_task_id,tsk.task_more_info,tsk.task_interlinkage,tsk.task_specific_due_date,tsk.task_weblinks,tsk.task_statutory_authority,tsk.task_subsequent_amount_per_day,tttrn.ttrn_rw_due_date,tttrn.ttrn_fh_due_date,fhusr.user_first_name, fhusr.user_last_name,tttrn.ttrn_uh_due_date "
						+ "FROM cfg_task_user_mapping tmapp "
						+ "LEFT JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
						+ "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id AND (tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) "
						+ "LEFT JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id "
						+ "JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = :user_id "
						+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
						+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
						+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
						+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
						+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
						+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
						+ "WHERE tmapp.tmap_enable_status = 1 " + "AND ttttrn.ttrn_id IS NULL ";

			} else {
				if (user_role_id == 3) {
					sql = "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id, prusr.user_first_name, prusr.user_last_name , rwusr.user_id , rwusr.user_first_name , rwusr.user_last_name, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id "
							+ ",tsk.task_country_name,tsk.task_state_name,tsk.task_effective_date,tsk.task_excemption_criteria,tsk.task_fine_amount,tsk.task_form_no,coalesce(tttrn.ttrn_impact_on_organization, tsk.task_impact_on_organization) as impact_on_organization,coalesce(tttrn.ttrn_impact_on_unit, tsk.task_impact_on_unit) as impact_on_unit, tsk.task_implication,tsk.task_imprisonment_duration,tsk.task_imprisonment_implies_to,tsk.task_level,tsk.task_linked_task_id,tsk.task_more_info,tsk.task_interlinkage,tsk.task_specific_due_date,tsk.task_weblinks,tsk.task_statutory_authority,tsk.task_subsequent_amount_per_day,tttrn.ttrn_uh_due_date,tttrn.ttrn_fh_due_date,fhusr.user_first_name, fhusr.user_last_name,tttrn.ttrn_uh_due_date "
							+ "FROM cfg_task_user_mapping tmapp "
							+ "LEFT JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
							+ "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id AND (tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) "
							+ "LEFT JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id "
							+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
							+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
							+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
							+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
							+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
							+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
							+ "WHERE tmapp.tmap_enable_status = 1 " + "AND ttttrn.ttrn_id IS NULL "
							+ "AND (tmapp.tmap_pr_user_id = :user_id OR tmapp.tmap_rw_user_id = :user_id OR tmapp.tmap_fh_user_id = :user_id )";
				} else {
					if (user_role_id == 2) {
						sql = "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id, prusr.user_first_name, prusr.user_last_name , rwusr.user_id , rwusr.user_first_name , rwusr.user_last_name, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id "
								+ ",tsk.task_country_name,tsk.task_state_name,tsk.task_effective_date,tsk.task_excemption_criteria,tsk.task_fine_amount,tsk.task_form_no,coalesce(tttrn.ttrn_impact_on_organization, tsk.task_impact_on_organization) as impact_on_organization,coalesce(tttrn.ttrn_impact_on_unit, tsk.task_impact_on_unit) as impact_on_unit, tsk.task_implication,tsk.task_imprisonment_duration,tsk.task_imprisonment_implies_to,tsk.task_level,tsk.task_linked_task_id,tsk.task_more_info,tsk.task_interlinkage,tsk.task_specific_due_date,tsk.task_weblinks,tsk.task_statutory_authority,tsk.task_subsequent_amount_per_day,tttrn.ttrn_uh_due_date,tttrn.ttrn_fh_due_date,fhusr.user_first_name, fhusr.user_last_name,tttrn.ttrn_uh_due_date "
								+ "FROM cfg_task_user_mapping tmapp "
								+ "LEFT JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
								+ "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id AND (tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) "
								+ "LEFT JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id "
								+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
								+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
								+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
								+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
								+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
								+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
								+ "WHERE tmapp.tmap_enable_status = 1 " + "AND ttttrn.ttrn_id IS NULL "
								+ "AND (tmapp.tmap_pr_user_id = :user_id OR tmapp.tmap_rw_user_id = :user_id)";

					} else {
						if (user_role_id == 1) {
							sql = "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id, prusr.user_first_name, prusr.user_last_name , rwusr.user_id , rwusr.user_first_name , rwusr.user_last_name, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id "
									+ ",tsk.task_country_name,tsk.task_state_name,tsk.task_effective_date,tsk.task_excemption_criteria,tsk.task_fine_amount,tsk.task_form_no,coalesce(tttrn.ttrn_impact_on_organization, tsk.task_impact_on_organization) as impact_on_organization,coalesce(tttrn.ttrn_impact_on_unit, tsk.task_impact_on_unit) as impact_on_unit, tsk.task_implication,tsk.task_imprisonment_duration,tsk.task_imprisonment_implies_to,tsk.task_level,tsk.task_linked_task_id,tsk.task_more_info,tsk.task_interlinkage,tsk.task_specific_due_date,tsk.task_weblinks,tsk.task_statutory_authority,tsk.task_subsequent_amount_per_day,tttrn.ttrn_uh_due_date,tttrn.ttrn_fh_due_date,fhusr.user_first_name, fhusr.user_last_name,tttrn.ttrn_uh_due_date "
									+ "FROM cfg_task_user_mapping tmapp "
									+ "LEFT JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
									+ "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id AND (tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) "
									+ "LEFT JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id "
									+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
									+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
									+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
									+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
									+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
									+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
									+ "WHERE tmapp.tmap_enable_status = 1 " + "AND ttttrn.ttrn_id IS NULL "
									+ "AND (tmapp.tmap_pr_user_id = :user_id)";
						} else {
							if (user_role_id == 7) {
								sql = "SELECT DISTINCT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id, prusr.user_first_name, prusr.user_last_name , rwusr.user_id , rwusr.user_first_name , rwusr.user_last_name, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id "
										+ ",tsk.task_country_name,tsk.task_state_name,tsk.task_effective_date,tsk.task_excemption_criteria,tsk.task_fine_amount,tsk.task_form_no,coalesce(tttrn.ttrn_impact_on_organization, tsk.task_impact_on_organization) as impact_on_organization,coalesce(tttrn.ttrn_impact_on_unit, tsk.task_impact_on_unit) as impact_on_unit, tsk.task_implication,tsk.task_imprisonment_duration,tsk.task_imprisonment_implies_to,tsk.task_level,tsk.task_linked_task_id,tsk.task_more_info,tsk.task_interlinkage,tsk.task_specific_due_date,tsk.task_weblinks,tsk.task_statutory_authority,tsk.task_subsequent_amount_per_day,tttrn.ttrn_rw_due_date,tttrn.ttrn_fh_due_date,fhusr.user_first_name, fhusr.user_last_name,tttrn.ttrn_uh_due_date "
										+ "FROM cfg_task_user_mapping tmapp "
										+ "LEFT JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
										+ "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id AND (tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) "
										+ "LEFT JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id "
										+ "JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND umapp.umap_dept_id = tmapp.tmap_dept_id "
										+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
										+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
										+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
										+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
										+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
										+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
										+ "WHERE tmapp.tmap_enable_status = 1 " + "AND ttttrn.ttrn_id IS NULL ";
							}
						}
					}
				}
			}

			Query query = em.createQuery(sql);
			if (user_role_id != 7) {
				query.setParameter("user_id", user_id);
			}
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
	public <T> List<T> listOfUpcommingTask(int user_id, int user_role_id) {
		LocalDate currentDate = LocalDate.now();
		LocalDate plus90Days = LocalDate.now().plusDays(90);
		try {
			String sql = "";

			if (user_role_id > 3) {
				sql = "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id, prusr.user_first_name, prusr.user_last_name , rwusr.user_id , rwusr.user_first_name , rwusr.user_last_name, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, fhusr.user_id , fhusr.user_first_name , fhusr.user_last_name,tttrn.ttrn_id , tsk.task_frequency "
						+ "FROM cfg_task_user_mapping tmapp "
						+ "LEFT JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
						+ "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id AND (tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) "
						+ "LEFT JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id "
						+ "JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND "
						+ "umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = '" + user_id + "' "
						+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
						+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
						+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
						+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
						+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
						+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
						+ "WHERE tmapp.tmap_enable_status = 1 AND ttttrn.ttrn_id IS NULL ";
				sql += "AND tttrn.ttrn_legal_due_date BETWEEN '" + currentDate + "' AND '" + plus90Days + "'";
				System.out.println("user_role_id > 2 SQL : " + sql);
			} else {
				if (user_role_id == 3) {
					sql = "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id, prusr.user_first_name, prusr.user_last_name , rwusr.user_id , rwusr.user_first_name , rwusr.user_last_name, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, fhusr.user_id , fhusr.user_first_name , fhusr.user_last_name,tttrn.ttrn_id , tsk.task_frequency "
							+ "FROM cfg_task_user_mapping tmapp "
							+ "LEFT JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id 	AND  date_format(tttrn.ttrn_legal_due_date, '%Y-%m-%d') > date_format(CURDATE(), '%Y-%m-%d') "
							+ "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id AND (tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) "
							+ "LEFT JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id "
							+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
							+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
							+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
							+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
							+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
							+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
							+ "WHERE tmapp.tmap_enable_status = 1 " + "AND ttttrn.ttrn_id IS NULL "
							+ "AND (tmapp.tmap_pr_user_id = '" + user_id + "' OR tmapp.tmap_rw_user_id = '" + user_id
							+ "' OR tmapp.tmap_fh_user_id = '" + user_id + "')";
					sql += "AND tttrn.ttrn_legal_due_date BETWEEN '" + currentDate + "' AND '" + plus90Days + "'";
					System.out.println("user_role_id == 2 SQL : " + sql);
				} else {
					if (user_role_id == 2) {
						sql = "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id, prusr.user_first_name, prusr.user_last_name , rwusr.user_id , rwusr.user_first_name , rwusr.user_last_name, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, fhusr.user_id , fhusr.user_first_name , fhusr.user_last_name,tttrn.ttrn_id , tsk.task_frequency "
								+ "FROM cfg_task_user_mapping tmapp "
								+ "LEFT JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id 	AND  date_format(tttrn.ttrn_legal_due_date, '%Y-%m-%d') > date_format(CURDATE(), '%Y-%m-%d') "
								+ "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id AND (tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) "
								+ "LEFT JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id "
								+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
								+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
								+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
								+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
								+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
								+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
								+ "WHERE tmapp.tmap_enable_status = 1 " + "AND ttttrn.ttrn_id IS NULL "
								+ "AND (tmapp.tmap_pr_user_id = '" + user_id + "' OR tmapp.tmap_rw_user_id = '"
								+ user_id + "')";
						sql += "AND tttrn.ttrn_legal_due_date BETWEEN '" + currentDate + "' AND '" + plus90Days + "'";
						System.out.println("user_role_id == 2 SQL : " + sql);
					} else {
						if (user_role_id == 1) {
							sql = "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id, prusr.user_first_name, prusr.user_last_name , rwusr.user_id , rwusr.user_first_name , rwusr.user_last_name, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, fhusr.user_id , fhusr.user_first_name , fhusr.user_last_name,tttrn.ttrn_id , tsk.task_frequency "
									+ "FROM cfg_task_user_mapping tmapp "
									+ "LEFT JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id 	AND  date_format(tttrn.ttrn_legal_due_date, '%Y-%m-%d') > date_format(CURDATE(), '%Y-%m-%d') "
									+ "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id AND (tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) "
									+ "LEFT JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id "
									+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
									+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
									+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
									+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
									+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
									+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
									+ "WHERE tmapp.tmap_enable_status = 1 " + "AND ttttrn.ttrn_id IS NULL "
									+ "AND (tmapp.tmap_pr_user_id = '" + user_id + "')";
							sql += "AND tttrn.ttrn_legal_due_date BETWEEN '" + currentDate + "' AND '" + plus90Days
									+ "'";
							System.out.println("user_role_id == 1 SQL : " + sql);

						}
					}
				}
			}

			Query query = em.createQuery(sql);
			// query.setParameter("user_id", user_id);
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
	public <T> List<T> listWaitingForApprovalTasksTask(int user_id, int user_role_id) {
		try {
			String sql = "";

			if (user_role_id > 3) {
				sql = "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name, tttrn.ttrn_pr_due_date,"
						+ " tttrn.ttrn_legal_due_date, tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, "
						+ "tsk.task_procedure, tsk.task_impact, COALESCE(tttrn.ttrn_frequency_for_operation, tsk.task_frequency)"
						+ " AS ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference,tsk.task_cat_law_name, "
						+ "tsk.task_task_type_of_task, tsk.task_prohibitive, tsk.task_event, tsk.task_sub_event, tttrn.ttrn_status, "
						+ "orga.orga_id,orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, usr.user_id,"
						+ " usr.user_first_name, usr.user_last_name, rw.user_id, rw.user_first_name, rw.user_last_name, tsk.task_cat_law_id,"
						+ "tsk.task_legi_id, tsk.task_rule_id, fh.user_id,fh.user_first_name, fh.user_last_name,tttrn.ttrn_id, "
						+ "tsk.task_frequency " + " FROM trn_task_transactional tttrn"
						+ " LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
						+ " JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id "
						+ " AND umapp.umap_loca_id = tmapp.tmap_loca_id AND umapp.umap_dept_id = tmapp.tmap_dept_id "
						+ " AND umapp.umap_user_id =:user_id JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
						+ " JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
						+ " JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id"
						+ " JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id JOIN mst_user rw ON rw.user_id = tmapp.tmap_rw_user_id "
						+ " JOIN mst_user fh ON fh.user_id = tmapp.tmap_fh_user_id WHERE tttrn.ttrn_status != 'Partially_Completed' "
						+ "	AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
						+ "	AND tttrn.ttrn_status= 'Partially_Completed'";
				System.out.println("user_role_id > 3 : " + sql);

			} else {
				if (user_role_id == 3) {
					sql = "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id, prusr.user_first_name, prusr.user_last_name , rwusr.user_id , rwusr.user_first_name , rwusr.user_last_name, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, fhusr.user_id , fhusr.user_first_name , fhusr.user_last_name,tttrn.ttrn_id , tsk.task_frequency "
							+ " FROM trn_task_transactional tttrn"
							+ " LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
							+ " JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id "
							+ " AND umapp.umap_loca_id = tmapp.tmap_loca_id AND umapp.umap_dept_id = tmapp.tmap_dept_id "
							+ " AND umapp.umap_user_id = 2 JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
							+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
							+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
							+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
							+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
							+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
							+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
							+ "WHERE tmapp.tmap_enable_status = 1 " + " AND tttrn.ttrn_status = 'Partially_Completed' "
							+ "	AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
							+ "AND (tmapp.tmap_pr_user_id = '" + user_id + "' OR tmapp.tmap_rw_user_id = '" + user_id
							+ "' OR tmapp.tmap_fh_user_id = '" + user_id + "')";
					System.out.println("user_role_id == 3 : " + sql);
				} else {
					if (user_role_id == 2) {
						sql = "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id, prusr.user_first_name, prusr.user_last_name , rwusr.user_id , rwusr.user_first_name , rwusr.user_last_name, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, fhusr.user_id , fhusr.user_first_name , fhusr.user_last_name,tttrn.ttrn_id , tsk.task_frequency "
								+ " FROM trn_task_transactional tttrn"
								+ " LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
								+ " JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id "
								+ " AND umapp.umap_loca_id = tmapp.tmap_loca_id AND umapp.umap_dept_id = tmapp.tmap_dept_id "
								+ " AND umapp.umap_user_id = 2 JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
								+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
								+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
								+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
								+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
								+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
								+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
								+ "WHERE tmapp.tmap_enable_status = 1 "
								+ " AND tttrn.ttrn_status = 'Partially_Completed' "
								+ "	AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
								+ "AND (tmapp.tmap_pr_user_id = '" + user_id + "' OR tmapp.tmap_rw_user_id = '"
								+ user_id + "')";
						System.out.println("user_role_id == 2 : " + sql);
					} else {
						if (user_role_id == 1) {
							sql = "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id, prusr.user_first_name, prusr.user_last_name , rwusr.user_id , rwusr.user_first_name , rwusr.user_last_name, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, fhusr.user_id , fhusr.user_first_name , fhusr.user_last_name,tttrn.ttrn_id , tsk.task_frequency "
									+ " FROM trn_task_transactional tttrn"
									+ " LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
									+ " JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id "
									+ " AND umapp.umap_loca_id = tmapp.tmap_loca_id AND umapp.umap_dept_id = tmapp.tmap_dept_id "
									+ " AND umapp.umap_user_id = 2 JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
									+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
									+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
									+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
									+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
									+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
									+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
									+ "WHERE tmapp.tmap_enable_status = 1 "
									+ " AND tttrn.ttrn_status = 'Partially_Completed' "
									+ "	AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
									+ "AND tmapp.tmap_pr_user_id = '" + user_id + "' ";
							System.out.println("user_role_id == 1 : " + sql);
						}
					}
				}
			}

			System.out.println("In list all for waiting for approval");
			Query query = em.createQuery(sql);
			// query.setParameter("user_id", user_id);
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
	public <T> List<T> listreopenedTask(int user_id, int user_role_id) {
		try {
			String sql = "";

			if (user_role_id > 3) {
				sql = "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name, tttrn.ttrn_pr_due_date, "
						+ "tttrn.ttrn_legal_due_date, tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, "
						+ "tsk.task_procedure, tsk.task_impact, COALESCE(tttrn.ttrn_frequency_for_operation, tsk.task_frequency) AS "
						+ "ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference,tsk.task_cat_law_name, "
						+ "tsk.task_task_type_of_task, tsk.task_prohibitive, tsk.task_event, tsk.task_sub_event, tttrn.ttrn_status, "
						+ "orga.orga_id,orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, usr.user_id, "
						+ "usr.user_first_name, usr.user_last_name, rw.user_id, rw.user_first_name, rw.user_last_name, tsk.task_cat_law_id, "
						+ "tsk.task_legi_id, tsk.task_rule_id, fh.user_id,fh.user_first_name, fh.user_last_name,tttrn.ttrn_id, "
						+ "tsk.task_frequency "
						+ "FROM trn_task_transactional tttrn LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
						+ "JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id "
						+ "AND umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = 2 JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
						+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
						+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
						+ "JOIN mst_user rw ON rw.user_id = tmapp.tmap_rw_user_id JOIN mst_user fh ON fh.user_id = tmapp.tmap_fh_user_id "
						+ "WHERE tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal'"
						+ " AND tttrn.ttrn_status= 'Re_Opened' ";
				System.out.println("user_role_id > 3 : " + sql);
			} else {
				if (user_role_id == 3) {
					sql = "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id, prusr.user_first_name, prusr.user_last_name , rwusr.user_id , rwusr.user_first_name , rwusr.user_last_name, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, fhusr.user_id , fhusr.user_first_name , fhusr.user_last_name,tttrn.ttrn_id , tsk.task_frequency "
							+ "FROM trn_task_transactional tttrn LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
							+ "JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id "
							+ "AND umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = 2 JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
							+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
							+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
							+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
							+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
							+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
							+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
							+ "WHERE tttrn.ttrn_status = 'Re_Opened' AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal'"
							+ "AND (tmapp.tmap_pr_user_id = '" + user_id + "' OR tmapp.tmap_rw_user_id = '" + user_id
							+ "' OR tmapp.tmap_fh_user_id = '" + user_id + "')";
					System.out.println("user_role_id == 3 : " + sql);
				} else {
					if (user_role_id == 2) {
						sql = "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id, prusr.user_first_name, prusr.user_last_name , rwusr.user_id , rwusr.user_first_name , rwusr.user_last_name, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, fhusr.user_id , fhusr.user_first_name , fhusr.user_last_name,tttrn.ttrn_id , tsk.task_frequency "
								+ "FROM trn_task_transactional tttrn LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
								+ "JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id "
								+ "AND umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = 2 JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
								+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
								+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
								+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
								+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
								+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
								+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
								+ "WHERE tttrn.ttrn_status = 'Re_Opened' AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal'"
								+ "AND (tmapp.tmap_pr_user_id = '" + user_id + "' OR tmapp.tmap_rw_user_id = '"
								+ user_id + "')";
						System.out.println("user_role_id == 2 : " + sql);

					} else {
						if (user_role_id == 1) {
							sql = "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id, prusr.user_first_name, prusr.user_last_name , rwusr.user_id , rwusr.user_first_name , rwusr.user_last_name, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, fhusr.user_id , fhusr.user_first_name , fhusr.user_last_name,tttrn.ttrn_id , tsk.task_frequency "
									+ "FROM trn_task_transactional tttrn LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
									+ "JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id "
									+ "AND umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = 2 JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
									+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
									+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
									+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
									+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
									+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
									+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
									+ "WHERE tttrn.ttrn_status = 'Re_Opened' AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal'"
									+ "AND (tmapp.tmap_pr_user_id = '" + user_id + "')";
							System.out.println("user_role_id == 1 : " + sql);

						}
					}
				}
			}

			System.out.println("In list all for listreopenedTask");
			Query query = em.createQuery(sql);
			// query.setParameter("user_id", user_id);
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
	public List<Object> searchForRepository(String jsonString, int user_id, int user_role_id) {
		String sql = "";
		System.out.println("searchForRepository : " + jsonString);
		System.out.println("user_id : " + user_id + "\t user_role : " + user_role_id);
		JsonNode rootNode = null;
		final ObjectMapper mapper = new ObjectMapper();
		if (user_role_id > 3 && user_role_id != 7) {
			sql = "SELECT Distinct tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name, "
					+ "date_format(tttrn.ttrn_pr_due_date, '%d-%m-%Y')  as ttrn_pr_due_date, "
					+ "date_format(tttrn.ttrn_legal_due_date, '%d-%m-%Y') as ttrn_legal_due_date , "
					+ "tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, "
					+ "coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, "
					+ "tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event, "
					+ "tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , "
					+ "dept.dept_name, prusr.user_id as prUsIds, prusr.user_first_name as prFnamess, prusr.user_last_name as prlastNames, "
					+ "rwusr.user_id as rwsUsId, rwusr.user_first_name as rwFNamees, "
					+ "rwusr.user_last_name as rwsaLNames, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, "
					+ "fhusr.user_id as fsUsrId, fhusr.user_first_name as fhsFNames, "
					+ "fhusr.user_last_name as fhLNames,tttrn.ttrn_id  FROM cfg_task_user_mapping tmapp "
					+ "LEFT JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
					+ "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id AND "
					+ "(tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) "
					+ "LEFT JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id "
					+ "JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id "
					+ "AND umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = '"
					+ user_id + "' JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
					+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
					+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
					+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
					+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
					+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
					+ "WHERE tmapp.tmap_enable_status = 1 AND ttttrn.ttrn_id IS NULL ";
			System.out.println("user_role_id > 3 && user_role_id != 7 " + sql);

		} else if (user_role_id == 3) {

			sql = "SELECT Distinct tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name, date_format(tttrn.ttrn_pr_due_date, '%d-%m-%Y') as ttrn_pr_due_date, date_format(tttrn.ttrn_legal_due_date, '%d-%m-%Y') as ttrn_legal_due_date, "
					+ "tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, "
					+ "coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, "
					+ "tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event, "
					+ "tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , "
					+ "dept.dept_name, prusr.user_id as prUserId, prusr.user_first_name as prFNames, prusr.user_last_name as prL, "
					+ "rwusr.user_id as rwUsid, rwusr.user_first_name as rwFname, "
					+ "rwusr.user_last_name as rwULastNames, "
					+ "tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, fhusr.user_id as fhUserId, fhusr.user_first_name as fhFNames, "
					+ "fhusr.user_last_name as fhUsrLastNmaes,tttrn.ttrn_id FROM cfg_task_user_mapping tmapp "
					+ "LEFT JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
					+ "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id "
					+ "AND (tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) "
					+ "LEFT JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id "
					+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
					+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
					+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
					+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
					+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
					+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
					+ "WHERE tmapp.tmap_enable_status = 1 " + "AND ttttrn.ttrn_id IS NULL "
					+ "AND (tmapp.tmap_pr_user_id = '" + user_id + "' OR tmapp.tmap_rw_user_id = '" + user_id
					+ "' OR tmapp.tmap_fh_user_id = '" + user_id + "')";

			System.out.println("User role is 3 " + sql);
		}

		else {
			if (user_role_id == 2) {
				sql = "SELECT Distinct tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name, date_format(tttrn.ttrn_pr_due_date, '%d-%m-%Y') as ttrn_pr_due_date, date_format(tttrn.ttrn_legal_due_date, '%d-%m-%Y') as ttrn_legal_due_date, "
						+ "tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, "
						+ "coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, "
						+ "tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event, "
						+ "tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , "
						+ "dept.dept_name, "
						+ "prusr.user_id as prUserId, prusr.user_first_name as prUsrFNames, prusr.user_last_name as prLastsNames, "
						+ "rwusr.user_id as rwUsrId, rwusr.user_first_name as rwFirstsNames, "
						+ "rwusr.user_last_name, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, "
						+ "fhusr.user_id as fhUserId, fhusr.user_first_name as fhFnames, "
						+ "fhusr.user_last_name as fhLasNames, tttrn.ttrn_id FROM cfg_task_user_mapping tmapp "
						+ "LEFT JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
						+ "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id AND "
						+ "(tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) "
						+ "LEFT JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id "
						+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
						+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
						+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
						+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
						+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
						+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
						+ "WHERE tmapp.tmap_enable_status = 1 " + "AND ttttrn.ttrn_id IS NULL "
						+ "AND (tmapp.tmap_pr_user_id = '" + user_id + "' OR tmapp.tmap_rw_user_id = '" + user_id
						+ "') ";

				System.out.println("User role is 2 " + sql);
			} else {
				if (user_role_id == 1) {
					sql = "SELECT Distinct tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,date_format(tttrn.ttrn_pr_due_date, '%d-%m-%Y') as ttrn_pr_due_date, date_format(tttrn.ttrn_legal_due_date, '%d-%m-%Y') as ttrn_legal_due_date , "
							+ "tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, "
							+ "coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, "
							+ "tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,"
							+ "tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, "
							+ "dept.dept_id , dept.dept_name, "
							+ "prusr.user_id as prUs, prusr.user_first_name as prFnames, prusr.user_last_name as prLastNam, "
							+ "rwusr.user_id as rwUsrId, rwusr.user_first_name as rwFiNames, rwusr.user_last_name as rWLasNmaes, "
							+ "tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, "
							+ "fhusr.user_id as fhUserId, fhusr.user_first_name as fhFnames, fhusr.user_last_name as fhLastNames, tttrn.ttrn_id "
							+ "FROM cfg_task_user_mapping tmapp "
							+ "LEFT JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
							+ "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id AND "
							+ "(tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) "
							+ "LEFT JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id "
							+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
							+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
							+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
							+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
							+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
							+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
							+ "WHERE tmapp.tmap_enable_status = 1 " + "AND ttttrn.ttrn_id IS NULL "
							+ "AND (tmapp.tmap_pr_user_id = '" + user_id + "')";

					System.out.println("User role is 1 " + sql);
				} else {
					if (user_role_id == 3) {
						sql = "SELECT Distinct tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,date_format(tttrn.ttrn_pr_due_date, '%d-%m-%Y') as ttrn_pr_due_date, date_format(tttrn.ttrn_legal_due_date, '%d-%m-%Y') as ttrn_legal_due_date , "
								+ "tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, "
								+ "coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, "
								+ "tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,"
								+ "tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, "
								+ "dept.dept_id , dept.dept_name, "
								+ "prusr.user_id as prUsrsId, prusr.user_first_name as prFinames, prusr.user_last_name as prLastsnames, "
								+ "rwusr.user_id as rwUsrsID, rwusr.user_first_name as rwFNMaes, rwusr.user_last_name as rwLastNames, "
								+ "tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, "
								+ "fhusr.user_id as fhUsrId, fhusr.user_first_name as fhFnames, fhusr.user_last_name as fhlanames, tttrn.ttrn_id "
								+ "FROM cfg_task_user_mapping tmapp "
								+ "LEFT JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
								+ "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id AND "
								+ "(tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) "
								+ "LEFT JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id "
								+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
								+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
								+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
								+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
								+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
								+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
								+ "WHERE tmapp.tmap_enable_status = 1 " + "AND ttttrn.ttrn_id IS NULL "
								+ "AND (tmapp.tmap_pr_user_id = '" + user_id + "' OR tmapp.tmap_rw_user_id = '"
								+ user_id + "' OR tmapp.tmap_fh_user_id = '" + user_id + "')";
						System.out.println("user_role_id == 3 : " + sql);
					} else {
						if (user_role_id == 7) {

							sql = "SELECT DISTINCT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name, date_format(tttrn.ttrn_pr_due_date, '%d-%m-%Y')  as ttrn_pr_due_date, date_format(tttrn.ttrn_legal_due_date, '%d-%m-%Y') as ttrn_legal_due_date , "
									+ "tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, "
									+ "coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, "
									+ "tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event, "
									+ "tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , "
									+ "dept.dept_name, "
									+ "prusr.user_id as prUsriD, prusr.user_first_name as prFNames, prusr.user_last_name as prLasNames, "
									+ "rwusr.user_id as rwUsrId, rwusr.user_first_name as rwFirNames, rwusr.user_last_name as rwLastsNames, "
									+ "tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, "
									+ "fhusr.user_id as fhUserId, fhusr.user_first_name as fhFNames, fhusr.user_last_name as fhLastNames, "
									+ "tttrn.ttrn_id  FROM cfg_task_user_mapping tmapp "
									+ "LEFT JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
									+ "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id AND "
									+ "(tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) "
									+ "LEFT JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id "
									+ "JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND umapp.umap_dept_id = tmapp.tmap_dept_id "
									+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
									+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
									+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
									+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
									+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
									+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
									+ "WHERE tmapp.tmap_enable_status = 1 AND ttttrn.ttrn_id IS NULL ";

							System.out.println("User role is : 7 " + sql);	

						}
					}
				}
			}
		}
		try {
			rootNode = mapper.readTree(jsonString);
			Integer entityId = rootNode.path("orga_id").asInt();
			System.out.println("entityName:" + entityId);
			Integer unitId = rootNode.path("loca_id").asInt();
			Integer functionId = rootNode.path("dept_id").asInt();
			Integer executorId = rootNode.path("executor_id").asInt();
			Integer evaluatorId = rootNode.path("evaluator_id").asInt();
			Integer categoryOfLawId = rootNode.path("cat_law_id").asInt();
			String impact = rootNode.path("impact").asText();
			String proh_pres = rootNode.path("proh_pres").asText();
			String type_of_task = rootNode.path("type_of_task").asText();
			String frequency = rootNode.path("frequency").asText();
			String task_status = rootNode.path("task_status").asText();
			String event = rootNode.path("event").asText();
			String sub_event = rootNode.path("sub_event").asText();
			Integer legi_id = rootNode.path("legi_id").asInt();
			Integer rule_id = rootNode.path("rule_id").asInt();
			String task_id = rootNode.path("task_id").asText();

			Integer maxCount = rootNode.path("nextSearch").asInt();
			Integer maxResult = 0;
			System.out.println("maxResult : " + maxCount);

			Integer itemsPerPage = rootNode.path("itemsPerPage").asInt();
			Integer pageno = rootNode.path("pageno").asInt();

			if (maxCount != null) {
				maxCount = maxCount;
			} else {
				maxResult = 200;
			}

			if (entityId != 0) {
				sql = sql + " and orga_id = " + entityId;
			}
			if (unitId != 0) {
				sql = sql + " and loca_id = " + unitId;
			}
			if (functionId != 0) {
				sql = sql + " and dept_Id = " + functionId;
				// System.out.println("hql:" +hql);
			}
			if (executorId != 0) {
				sql = sql + " and  prusr.user_id = " + executorId;

			}
			if (evaluatorId != 0) {
				sql = sql + " and  rwusr.user_id = " + evaluatorId;

			}
			if (categoryOfLawId != 0) {
				sql = sql + " and  tsk.task_cat_law_id =" + categoryOfLawId;

			}
			if (legi_id != 0) {
				sql = sql + " and  tsk.task_legi_id =" + legi_id;
			}

			if (rule_id != 0) {
				sql = sql + " and  tsk.task_rule_id =" + rule_id;
			}
			if (impact.equalsIgnoreCase("null")) {
				// System.out.println("swap");
			} else {
				sql = sql + " and tsk.task_impact =" + "'" + impact + "'";

			}

			if (proh_pres.equalsIgnoreCase("null")) {
				/// System.out.println("swap");
			} else {
				sql = sql + " and tsk.task_prohibitive =" + "'" + proh_pres + "'";

			}

			if (type_of_task.equalsIgnoreCase("null")) {
				// System.out.println("swap");
			} else {
				sql = sql + " and  tsk.task_task_type_of_task =" + "'" + type_of_task + "'";

			}

			if (frequency.equalsIgnoreCase("null")) {
				// System.out.println("swap");
			} else {
				sql = sql + " and tttrn.ttrn_frequency_for_operation =" + "'" + frequency + "'";

			}

			if (task_status.equalsIgnoreCase("null")) {
				// System.out.println("swap");
			} else {
				sql = sql + " and tttrn.ttrn_status =" + "'" + task_status + "'";
			}

			if (event.equalsIgnoreCase("null")) {
				// System.out.println("swap");
			} else {
				sql = sql + " and tsk.task_event =" + "'" + event + "'";
			}

			if (sub_event.equalsIgnoreCase("null")) {
				// System.out.println("swap");
			} else {
				sql = sql + " and tsk.task_sub_event =" + "'" + sub_event + "'";
			}

			if (task_id.equalsIgnoreCase("null")) {
				System.out.println("swap");
			} else {
				sql = sql + " and tmapp.tmap_client_tasks_id =" + "'" + task_id + "'";
			}

			// sql += " limit 400";

			System.out.println("In list all for repository " + sql);
			Query query = em.createNativeQuery(sql);
			// query.setMaxResults(itemsPerPage);
			System.out.println("sql : " + sql);
			// query.setParameter("user_id", user_id);
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
	public List<Object> getAllTaskForRepository(int user_id, int user_role_id, int orga_id, int loca_id, int dept_id) {

		System.out.println("user_id:" + user_id);
		try {
			String sql = "";

			if (user_role_id > 3) {
				sql = "SELECT tsk.task_legi_name, tsk.task_rule_name, tsk.task_legi_id, tsk.task_rule_id, tsk.task_event,"
						+ "tsk.task_sub_event, tmapp.tmap_client_tasks_id, tmapp.tmap_pr_user_id, orga.orga_id,loca.loca_id,dept.dept_id,"
						+ "tmapp.tmap_rw_user_id as rwusrId, usr.user_first_name as rFN, usr.user_last_name as usrLN, "
						+ "usrw.user_first_name as rwFNam, usrw.user_last_name as rwLNamess, tsk.task_cat_law_id, "
						+ "usrfh.user_id as fhUsId, usrfh.user_first_name as fhFirstName, usrfh.user_last_name as fhLLastNames "
						+ " FROM cfg_task_user_mapping tmapp JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id JOIN mst_user usr "
						+ "ON usr.user_id = tmapp.tmap_pr_user_id JOIN mst_user usrw ON usrw.user_id = tmapp.tmap_rw_user_id JOIN mst_user usrfh ON usrfh.user_id = tmapp.tmap_fh_user_id JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca "
						+ "ON loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id WHERE tmapp.tmap_enable_status = 1 ";

			} else if (user_role_id == 3) {
				sql = "SELECT tsk.task_legi_name, tsk.task_rule_name, tsk.task_legi_id, tsk.task_rule_id, tsk.task_event,"
						+ "tsk.task_sub_event, tmapp.tmap_client_tasks_id, "
						+ "tmapp.tmap_pr_user_id as prUId, orga.orga_id,loca.loca_id,dept.dept_id,"
						+ "tmapp.tmap_rw_user_id as rwUserID,  usr.user_first_name as urFNames, "
						+ "usr.user_last_name as uLasNames, usrw.user_first_name as rwFNammes, usrw.user_last_name as rwLLnames, tsk.task_cat_law_id, usrfh.user_id, usrfh.user_first_name, usrfh.user_last_name "
						+ " FROM cfg_task_user_mapping tmapp JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id JOIN mst_user usr "
						+ "ON usr.user_id = tmapp.tmap_pr_user_id JOIN mst_user usrw ON usrw.user_id = tmapp.tmap_rw_user_id JOIN mst_user usrfh ON usrfh.user_id = tmapp.tmap_fh_user_id JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca "
						+ "ON loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id WHERE tmapp.tmap_enable_status = 1"
						+ "AND (tmapp.tmap_pr_user_id = :user_id OR tmapp.tmap_rw_user_id = :user_id OR tmapp.tmap_fh_user_id = :user_id)";

			} else if (user_role_id == 2) {
				sql = "SELECT tsk.task_legi_name, tsk.task_rule_name, tsk.task_legi_id, tsk.task_rule_id, tsk.task_event,"
						+ "tsk.task_sub_event, tmapp.tmap_client_tasks_id, tmapp.tmap_pr_user_id, orga.orga_id,loca.loca_id,dept.dept_id,"
						+ "tmapp.tmap_rw_user_id as rwUserID,  usr.user_first_name as usrFiName, usr.user_last_name as usrLasmNames, "
						+ "usrw.user_first_name as rwwFNames, usrw.user_last_name as rwwLastNAmes, tsk.task_cat_law_id, "
						+ "usrfh.user_id as fhUSID, usrfh.user_first_name as fhFNAmes, usrfh.user_last_name as fhLLNAmes "
						+ " FROM cfg_task_user_mapping tmapp JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id JOIN mst_user usr  "
						+ "ON usr.user_id = tmapp.tmap_pr_user_id JOIN mst_user usrw ON usrw.user_id = tmapp.tmap_rw_user_id JOIN mst_user usrfh ON usrfh.user_id = tmapp.tmap_fh_user_id JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca "
						+ "ON loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id WHERE tmapp.tmap_enable_status = 1 "
						+ "AND (tmapp.tmap_pr_user_id = :user_id OR tmapp.tmap_rw_user_id = :user_id)";

			} else if (user_role_id == 1) {

				sql = "SELECT tsk.task_legi_name, tsk.task_rule_name, tsk.task_legi_id, tsk.task_rule_id, tsk.task_event,"
						+ "tsk.task_sub_event, tmapp.tmap_client_tasks_id, tmapp.tmap_pr_user_id, orga.orga_id,loca.loca_id,dept.dept_id,"
						+ "tmapp.tmap_rw_user_id as rwUserID,  usr.user_first_name as urFNames, usr.user_last_name as usrLastNames, "
						+ "usrw.user_first_name as rwFNames, usrw.user_last_name as rwLasNames, "
						+ "tsk.task_cat_law_id, usrfh.user_id as fhUsrId, usrfh.user_first_name as fhFNamess, usrfh.user_last_name as fhLastNAmes "
						+ " FROM cfg_task_user_mapping tmapp JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id JOIN mst_user usr "
						+ "ON usr.user_id = tmapp.tmap_pr_user_id JOIN mst_user usrw ON usrw.user_id = tmapp.tmap_rw_user_id JOIN mst_user usrfh ON usrfh.user_id = tmapp.tmap_fh_user_id JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca "
						+ "ON loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id WHERE tmapp.tmap_enable_status = 1 "
						+ "AND (tmapp.tmap_pr_user_id = :user_id )";
			}

			System.out.println("In list all for repository:" + sql);
			if (orga_id != 0) {
				sql = sql + " and orga.orga_id = " + orga_id;
			}
			if (loca_id != 0) {
				sql = sql + " and loca.loca_id = " + loca_id;
			}
			if (dept_id != 0) {
				sql = sql + " and dept.dept_id = " + dept_id;
			}
			Query query = em.createNativeQuery(sql);
			if (user_role_id <= 3) {
				query.setParameter("user_id", user_id);
			}

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

	// New Code
	@Override
	public List<Object> getCategoryList(HttpSession session) {

		try {
			String sql = "select tsk.task_cat_law_id,tsk.task_cat_law_name from mst_task tsk group by tsk.task_cat_law_id,tsk.task_cat_law_name";
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

	@Override
	public List<Object> getTypeOfTask(HttpSession session) {

		try {
			String sql = "select tsk.task_task_type_of_task from mst_task tsk group by tsk.task_task_type_of_task";
			Query query = em.createNativeQuery(sql);
			// System.out.println("sql:" +sql);
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

	@Override
	public List<Object> getFrequencyList(HttpSession session) {
		try {
			String sql = "select tsk.task_frequency from mst_task tsk group by tsk.task_frequency";
			Query query = em.createNativeQuery(sql);
			// System.out.println("sql:" +sql);
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

	@Override
	public List<Object> getExeEvalListByEntity(int user_id, int user_role_id, String orga_id) {

		System.out.println("user_id:" + user_id);
		try {
			String sql = "";

			if (user_role_id > 3) {
				sql = "SELECT tsk.task_legi_name, tsk.task_rule_name, tsk.task_legi_id, tsk.task_rule_id, tsk.task_event,"
						+ "tsk.task_sub_event, tmapp.tmap_client_tasks_id, tmapp.tmap_pr_user_id, orga.orga_id,loca.loca_id,dept.dept_id,"
						+ "tmapp.tmap_rw_user_id as rwUserID,  usr.user_first_name as usFNames, usr.user_last_name as usLAstnames, "
						+ "usrw.user_first_name as rwUsFiratNames, usrw.user_last_name as rwULastNames, tsk.task_cat_law_id "
						+ " FROM cfg_task_user_mapping tmapp JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id JOIN mst_user usr "
						+ "ON usr.user_id = tmapp.tmap_pr_user_id JOIN mst_user usrw ON usrw.user_id = tmapp.tmap_rw_user_id JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca "
						+ "ON loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id WHERE tmapp.tmap_enable_status = 1 and orga.orga_id = "
						+ orga_id;

			} else if (user_role_id == 3) {
				sql = "SELECT tsk.task_legi_name, tsk.task_rule_name, tsk.task_legi_id, tsk.task_rule_id, tsk.task_event,"
						+ "tsk.task_sub_event, tmapp.tmap_client_tasks_id, tmapp.tmap_pr_user_id, orga.orga_id,loca.loca_id,dept.dept_id,"
						+ "tmapp.tmap_rw_user_id as rwUserID,  usr.user_first_name as usFNames, usr.user_last_name as rsLastNames, "
						+ "usrw.user_first_name as twFirsNames, usrw.user_last_name as rwLastNames, tsk.task_cat_law_id "
						+ " FROM cfg_task_user_mapping tmapp JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id JOIN mst_user usr "
						+ "ON usr.user_id = tmapp.tmap_pr_user_id JOIN mst_user usrw ON usrw.user_id = tmapp.tmap_rw_user_id JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca "
						+ "ON loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id WHERE tmapp.tmap_enable_status = 1"
						+ "AND (tmapp.tmap_pr_user_id = :user_id OR tmapp.tmap_rw_user_id = :user_id OR tmapp.tmap_fh_user_id = :user_id) AND orga.orga_id = "
						+ orga_id;

			} else if (user_role_id == 2) {
				sql = "SELECT tsk.task_legi_name, tsk.task_rule_name, tsk.task_legi_id, tsk.task_rule_id, tsk.task_event,"
						+ "tsk.task_sub_event, tmapp.tmap_client_tasks_id, tmapp.tmap_pr_user_id as prUsrID, orga.orga_id,loca.loca_id,dept.dept_id,"
						+ "tmapp.tmap_rw_user_id as rwUserID, usr.user_first_name as usFNames, usr.user_last_name as uLASNAM, "
						+ "usrw.user_first_name as rwFNaames, usrw.user_last_name as rwLAnsmes, tsk.task_cat_law_id "
						+ " FROM cfg_task_user_mapping tmapp JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id JOIN mst_user usr  "
						+ "ON usr.user_id = tmapp.tmap_pr_user_id JOIN mst_user usrw ON usrw.user_id = tmapp.tmap_rw_user_id JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca "
						+ "ON loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id WHERE tmapp.tmap_enable_status = 1 "
						+ "AND (tmapp.tmap_pr_user_id = :user_id OR tmapp.tmap_rw_user_id = :user_id) AND orga.orga_id = "
						+ orga_id;

			} else if (user_role_id == 1) {

				sql = "SELECT tsk.task_legi_name, tsk.task_rule_name, tsk.task_legi_id, tsk.task_rule_id, tsk.task_event,"
						+ "tsk.task_sub_event, tmapp.tmap_client_tasks_id, tmapp.tmap_pr_user_id as prUserId, "
						+ "orga.orga_id,loca.loca_id,dept.dept_id,"
						+ "tmapp.tmap_rw_user_id as rwUserID,  usr.user_first_name as frstNames, usr.user_last_name as lasnmaes, "
						+ "usrw.user_first_name as rwFNames, usrw.user_last_name as rwLastNames, tsk.task_cat_law_id "
						+ " FROM cfg_task_user_mapping tmapp JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id JOIN mst_user usr "
						+ "ON usr.user_id = tmapp.tmap_pr_user_id JOIN mst_user usrw ON usrw.user_id = tmapp.tmap_rw_user_id JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca "
						+ "ON loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id WHERE tmapp.tmap_enable_status = 1 "
						+ "AND (tmapp.tmap_pr_user_id = :user_id ) AND orga.orga_id = " + orga_id;
			}

			System.out.println("In list all for repository:" + sql);
			Query query = em.createNativeQuery(sql);
			if (user_role_id <= 3) {
				query.setParameter("user_id", user_id);
			}

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

	@Override
	public List<Object> getAllTask(String jsonString, int user_id, int user_role_id) {
		String sql = "";
		JsonNode rootNode = null;
		final ObjectMapper mapper = new ObjectMapper();
		if (user_role_id > 3 && user_role_id <= 6) {
			sql = "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name, date_format(tttrn.ttrn_pr_due_date, '%d-%m-%Y')  as ttrn_pr_due_date, date_format(tttrn.ttrn_legal_due_date, '%d-%m-%Y') as ttrn_legal_due_date , "
					+ "tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, "
					+ "coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, "
					+ "tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event, "
					+ "tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , "
					+ "dept.dept_name, prusr.user_id as prUserId, prusr.user_first_name as prFirstName, prusr.user_last_name as prLastNames, "
					+ "rwusr.user_id as rwuserId, rwusr.user_first_name as rwFirstNames, "
					+ "rwusr.user_last_name as rwLastNames, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, "
					+ "fhusr.user_id as fhUserId, fhusr.user_first_name as fhFNames, "
					+ "fhusr.user_last_name,tttrn.ttrn_id, d_task.dtco_id, d_task.dtco_client_task_id, d_task.dtco_after_before FROM cfg_task_user_mapping tmapp "
					+ "LEFT JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
					+ "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id AND "
					+ "(tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) "
					+ "LEFT JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id "
					+ "JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = '"
					+ user_id + "' JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
					+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
					+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
					+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
					+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
					+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
					+ "JOIN cfg_default_task_configuration d_task ON d_task.dtco_client_task_id = tmapp.tmap_client_tasks_id "
					+ "WHERE tmapp.tmap_enable_status = 1 AND ttttrn.ttrn_id IS NULL and tttrn.ttrn_pr_due_date is null";
			System.out.println("User role is > 3");

		} else if (user_role_id == 3) {

			System.out.println("User role is 3");

			sql = "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name, date_format(tttrn.ttrn_pr_due_date, '%d-%m-%Y') as ttrn_pr_due_date, date_format(tttrn.ttrn_legal_due_date, '%d-%m-%Y') as ttrn_legal_due_date, "
					+ "tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, "
					+ "coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, "
					+ "tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event, "
					+ "tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , "
					+ "dept.dept_name, prusr.user_id as prUserId, prusr.user_first_name as prFNames, prusr.user_last_name as prLasNames, "
					+ "rwusr.user_id as rwUsrId, rwusr.user_first_name as rwFNames, "
					+ "rwusr.user_last_name as rwLNames, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, "
					+ "fhusr.user_id as fhUserId, fhusr.user_first_name as fhFNames, "
					+ "fhusr.user_last_name as fhLasnes, tttrn.ttrn_id,d_task.dtco_id, d_task.dtco_client_task_id, d_task.dtco_after_before FROM cfg_task_user_mapping tmapp "
					+ "LEFT JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
					+ "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id AND (tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) "
					+ "LEFT JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id "
					+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
					+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
					+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
					+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
					+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
					+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
					+ "JOIN cfg_default_task_configuration d_task ON d_task.dtco_client_task_id = tmapp.tmap_client_tasks_id "
					+ "WHERE tmapp.tmap_enable_status = 1 " + "AND ttttrn.ttrn_id IS NULL "
					+ "AND (tmapp.tmap_pr_user_id = '" + user_id + "' OR tmapp.tmap_rw_user_id = '" + user_id
					+ "' OR tmapp.tmap_fh_user_id = '" + user_id + "') and tttrn.ttrn_pr_due_date is null";
		}

		else {
			if (user_role_id == 2) {
				sql = "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name, date_format(tttrn.ttrn_pr_due_date, '%d-%m-%Y') as ttrn_pr_due_date, date_format(tttrn.ttrn_legal_due_date, '%d-%m-%Y') as ttrn_legal_due_date, "
						+ "tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, "
						+ "coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, "
						+ "tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event, "
						+ "tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , "
						+ "dept.dept_name, "
						+ "prusr.user_id as prUserId, prusr.user_first_name as prFNames, prusr.user_last_name as prLastsNames, "
						+ "rwusr.user_id as rwUsrId, rwusr.user_first_name as rwFNames, "
						+ "rwusr.user_last_name as rwLastNames, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, "
						+ "fhusr.user_id as fhUserId, fhusr.user_first_name as fhFirstNames, "
						+ "fhusr.user_last_name as fhLastNames, tttrn.ttrn_id,d_task.dtco_id, d_task.dtco_client_task_id, d_task.dtco_after_before FROM cfg_task_user_mapping tmapp "
						+ "LEFT JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
						+ "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id AND (tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) "
						+ "LEFT JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id "
						+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
						+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
						+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
						+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
						+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
						+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
						+ "JOIN cfg_default_task_configuration d_task ON d_task.dtco_client_task_id = tmapp.tmap_client_tasks_id "
						+ "WHERE tmapp.tmap_enable_status = 1 " + "AND ttttrn.ttrn_id IS NULL "
						+ "AND (tmapp.tmap_pr_user_id = '" + user_id + "' OR tmapp.tmap_rw_user_id = '" + user_id
						+ "') and tttrn.ttrn_pr_due_date is null";
			} else {
				if (user_role_id == 1) {
					sql = "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,date_format(tttrn.ttrn_pr_due_date, '%d-%m-%Y') as ttrn_pr_due_date, date_format(tttrn.ttrn_legal_due_date, '%d-%m-%Y') as ttrn_legal_due_date , "
							+ "tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, "
							+ "coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, "
							+ "tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,"
							+ "tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, "
							+ "dept.dept_id , dept.dept_name, "
							+ "prusr.user_id as prUsrrId, prusr.user_first_name as prFNames, prusr.user_last_name as prLastNames, "
							+ "rwusr.user_id as rwUserId, rwusr.user_first_name as rwFirstNames, rwusr.user_last_name as rwLNames, tsk.task_cat_law_id, "
							+ "tsk.task_legi_id, tsk.task_rule_id, fhusr.user_id as fHUserId, "
							+ "fhusr.user_first_name as fhUserName, fhusr.user_last_name as fhLastName, "
							+ "tttrn.ttrn_id, d_task.dtco_id, d_task.dtco_client_task_id, d_task.dtco_after_before "
							+ "FROM cfg_task_user_mapping tmapp "
							+ "LEFT JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
							+ "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id AND "
							+ "(tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) "
							+ "LEFT JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id "
							+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
							+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
							+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
							+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
							+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
							+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
							+ "JOIN cfg_default_task_configuration d_task ON d_task.dtco_client_task_id = tmapp.tmap_client_tasks_id "
							+ "WHERE tmapp.tmap_enable_status = 1 " + "AND ttttrn.ttrn_id IS NULL "
							+ "AND (tmapp.tmap_pr_user_id = '" + user_id + "') and tttrn.ttrn_pr_due_date is null";
				} else {
					if (user_role_id == 3) {
						sql = "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,date_format(tttrn.ttrn_pr_due_date, '%d-%m-%Y') as ttrn_pr_due_date, date_format(tttrn.ttrn_legal_due_date, '%d-%m-%Y') as ttrn_legal_due_date , "
								+ "tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, "
								+ "coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, "
								+ "tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,"
								+ "tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, "
								+ "dept.dept_id , dept.dept_name, "
								+ "prusr.user_id as prUserId, prusr.user_first_name as prFNames, prusr.user_last_name as prLastNames, "
								+ "rwusr.user_id as rwUserId, rwusr.user_first_name as rwFName, rwusr.user_last_name as rwLastNames, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, "
								+ "fhusr.user_id as fhUserId, fhusr.user_first_name as fhFNames, fhusr.user_last_name as fhLastNames, tttrn.ttrn_id, d_task.dtco_id, d_task.dtco_client_task_id, d_task.dtco_after_before "
								+ "FROM cfg_task_user_mapping tmapp "
								+ "LEFT JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
								+ "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id AND "
								+ "(tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) "
								+ "LEFT JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id "
								+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
								+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
								+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
								+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
								+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
								+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
								+ "JOIN cfg_default_task_configuration d_task ON d_task.dtco_client_task_id = tmapp.tmap_client_tasks_id "
								+ "WHERE tmapp.tmap_enable_status = 1 " + "AND ttttrn.ttrn_id IS NULL "
								+ "AND (tmapp.tmap_pr_user_id = '" + user_id + "' OR tmapp.tmap_rw_user_id = '"
								+ user_id + "' OR tmapp.tmap_fh_user_id = '" + user_id
								+ "') and tttrn.ttrn_pr_due_date is null";
						System.out.println("user_role_id == 3 : " + sql);
					} else {
						if (user_role_id == 7) {

							sql = "SELECT DISTINCT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name, date_format(tttrn.ttrn_pr_due_date, '%d-%m-%Y')  as ttrn_pr_due_date, date_format(tttrn.ttrn_legal_due_date, '%d-%m-%Y') as ttrn_legal_due_date , "
									+ "tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, "
									+ "coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, "
									+ "tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event, "
									+ "tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , "
									+ "dept.dept_name, "
									+ "prusr.user_id as prUserId, prusr.user_first_name as prFNames, prusr.user_last_name as prLastNames, "
									+ "rwusr.user_id as rwUsrId, rwusr.user_first_name as rwFNames, "
									+ "rwusr.user_last_name as LastNames, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, "
									+ "fhusr.user_id as fhUsrId, fhusr.user_first_name as fhFirstNames, "
									+ "fhusr.user_last_name as fhLastNames, tttrn.ttrn_id, d_task.dtco_id, d_task.dtco_client_task_id, d_task.dtco_after_before FROM cfg_task_user_mapping tmapp "
									+ "LEFT JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
									+ "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id AND "
									+ "(tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) "
									+ "LEFT JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id "
									+ "JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND umapp.umap_dept_id = tmapp.tmap_dept_id "
									+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
									+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
									+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
									+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
									+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
									+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
									+ "JOIN cfg_default_task_configuration d_task ON d_task.dtco_client_task_id = tmapp.tmap_client_tasks_id "
									+ "WHERE tmapp.tmap_enable_status = 1 AND ttttrn.ttrn_id IS NULL and tttrn.ttrn_pr_due_date is null";

							System.out.println("User role is : 7");

						}
					}
				}
			}
		}
		try {
			rootNode = mapper.readTree(jsonString);
			Integer entityId = rootNode.path("orga_id").asInt();
			System.out.println("entityName:" + entityId);
			Integer unitId = rootNode.path("loca_id").asInt();
			Integer functionId = rootNode.path("dept_id").asInt();
			Integer executorId = rootNode.path("executor_id").asInt();
			Integer evaluatorId = rootNode.path("evaluator_id").asInt();
			Integer categoryOfLawId = rootNode.path("cat_law_id").asInt();
			String impact = rootNode.path("impact").asText();
			String proh_pres = rootNode.path("proh_pres").asText();
			String type_of_task = rootNode.path("type_of_task").asText();
			String frequency = rootNode.path("frequency").asText();
			String task_status = rootNode.path("task_status").asText();
			String event = rootNode.path("event").asText();
			String sub_event = rootNode.path("sub_event").asText();
			Integer legi_id = rootNode.path("legi_id").asInt();
			Integer rule_id = rootNode.path("rule_id").asInt();
			String task_id = rootNode.path("task_id").asText();

			Integer maxCount = rootNode.path("nextSearch").asInt();
			Integer maxResult = 0;
			System.out.println("maxResult : " + maxCount);

			Integer itemsPerPage = rootNode.path("itemsPerPage").asInt();
			Integer pageno = rootNode.path("pageno").asInt();

			if (maxCount != null) {
				maxCount = maxCount;
			} else {
				maxResult = 200;
			}

			if (entityId != 0) {
				sql = sql + " and orga_id = " + entityId;
			}
			if (unitId != 0) {
				sql = sql + " and loca_id = " + unitId;
			}
			if (functionId != 0) {
				sql = sql + " and dept_Id = " + functionId;
				// System.out.println("hql:" +hql);
			}
			if (executorId != 0) {
				sql = sql + " and  prusr.user_id = " + executorId;

			}
			if (evaluatorId != 0) {
				sql = sql + " and  rwusr.user_id = " + evaluatorId;

			}
			if (categoryOfLawId != 0) {
				sql = sql + " and  tsk.task_cat_law_id =" + categoryOfLawId;

			}
			if (legi_id != 0) {
				sql = sql + " and  tsk.task_legi_id =" + legi_id;
			}

			if (rule_id != 0) {
				sql = sql + " and  tsk.task_rule_id =" + rule_id;
			}
			if (impact.equalsIgnoreCase("null")) {
				// System.out.println("swap");
			} else {
				sql = sql + " and tsk.task_impact =" + "'" + impact + "'";

			}

			if (proh_pres.equalsIgnoreCase("null")) {
				/// System.out.println("swap");
			} else {
				sql = sql + " and tsk.task_prohibitive =" + "'" + proh_pres + "'";

			}

			if (type_of_task.equalsIgnoreCase("null")) {
				// System.out.println("swap");
			} else {
				sql = sql + " and  tsk.task_task_type_of_task =" + "'" + type_of_task + "'";

			}

			/*
			 * if (frequency.equalsIgnoreCase("null")) { // System.out.println("swap"); }
			 * else { sql = sql + " and tttrn.ttrn_frequency_for_operation =" + "'" +
			 * frequency + "'";
			 * 
			 * }
			 */
			if (task_status.equalsIgnoreCase("null")) {
				// System.out.println("swap");
			} else {
				sql = sql + " and tttrn.ttrn_status =" + "'" + task_status + "'";
			}

			if (event.equalsIgnoreCase("null")) {
				// System.out.println("swap");
			} else {
				sql = sql + " and tsk.task_event =" + "'" + event + "'";
			}

			if (sub_event.equalsIgnoreCase("null")) {
				// System.out.println("swap");
			} else {
				sql = sql + " and tsk.task_sub_event =" + "'" + sub_event + "'";
			}

			if (task_id.equalsIgnoreCase("null")) {
				System.out.println("swap");
			} else {
				sql = sql + " and tmapp.tmap_client_tasks_id =" + "'" + task_id + "'";
			}

			// sql += " limit 400";

			System.out.println("In list all for repository");
			Query query = em.createQuery(sql);
			// query.setMaxResults(itemsPerPage);
			System.out.println("sql : " + sql);
			// query.setParameter("user_id", user_id);
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

}
