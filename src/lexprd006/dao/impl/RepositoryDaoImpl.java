package lexprd006.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lexprd006.dao.RepositoryDao;

@Transactional
@Repository(value = "repositoryDao")
public class RepositoryDaoImpl implements RepositoryDao {

	@PersistenceContext
	EntityManager em;

	@Override
	public List<Object> getAllTaskForRepository(int user_id, int user_role_id, String jsonString) {
		try {
			JsonNode rootNode = null;
			final ObjectMapper mapper = new ObjectMapper();
			String sql = "";
			if (user_role_id > 3 && user_role_id != 7) {
				
				/*
				sql = "SELECT Distinct tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id, prusr.user_first_name, prusr.user_last_name , rwusr.user_id , rwusr.user_first_name , rwusr.user_last_name, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id "
						+ ",tsk.task_country_name,tsk.task_state_name,tsk.task_effective_date,tsk.task_excemption_criteria,tsk.task_fine_amount,tsk.task_form_no,coalesce(tttrn.ttrn_impact_on_organization, tsk.task_impact_on_organization) as impact_on_organization,coalesce(tttrn.ttrn_impact_on_unit, tsk.task_impact_on_unit) as impact_on_unit, tsk.task_implication,tsk.task_imprisonment_duration,tsk.task_imprisonment_implies_to,tsk.task_level,tsk.task_linked_task_id,tsk.task_more_info,tsk.task_interlinkage,tsk.task_specific_due_date,tsk.task_weblinks,tsk.task_statutory_authority,tsk.task_subsequent_amount_per_day,tttrn.ttrn_rw_due_date,tttrn.ttrn_fh_due_date,fhusr.user_first_name, fhusr.user_last_name,tttrn.ttrn_uh_due_date, tttrn.ttrn_prior_days_buffer, tttrn.ttrn_alert_days "
						
					*/
				
				sql = "SELECT Distinct tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id AS Performer, prusr.user_first_name as prfn, prusr.user_last_name as prln , rwusr.user_id AS Reviewer , rwusr.user_first_name as rwfn, rwusr.user_last_name as rwln, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id "
						+ ",tsk.task_country_name,tsk.task_state_name,tsk.task_effective_date,tsk.task_excemption_criteria,tsk.task_fine_amount,tsk.task_form_no,coalesce(tttrn.ttrn_impact_on_organization, tsk.task_impact_on_organization) as impact_on_organization,coalesce(tttrn.ttrn_impact_on_unit, tsk.task_impact_on_unit) as impact_on_unit, tsk.task_implication,tsk.task_imprisonment_duration,tsk.task_imprisonment_implies_to,tsk.task_level,tsk.task_linked_task_id,tsk.task_more_info,tsk.task_interlinkage,tsk.task_specific_due_date,tsk.task_weblinks,tsk.task_statutory_authority,tsk.task_subsequent_amount_per_day,tttrn.ttrn_uh_due_date as uduedate,tttrn.ttrn_fh_due_date,fhusr.user_first_name, fhusr.user_last_name,tttrn.ttrn_uh_due_date, tttrn.ttrn_prior_days_buffer, tttrn.ttrn_alert_days "
						+ "FROM cfg_task_user_mapping tmapp "
						+ "LEFT JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
						+ "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id AND (tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) "
						+ "LEFT JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id "
						+ "JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = "
						+ user_id + " JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
						+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
						+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
						+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
						+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
						+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
						+ "WHERE tmapp.tmap_enable_status = 1 " + "AND ttttrn.ttrn_id IS NULL ";
			} else {
				if (user_role_id == 3) {
					
					/*
					sql = "SELECT Distinct tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id, prusr.user_first_name, prusr.user_last_name , rwusr.user_id , rwusr.user_first_name , rwusr.user_last_name, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id "
							+ ",tsk.task_country_name,tsk.task_state_name,tsk.task_effective_date,tsk.task_excemption_criteria,tsk.task_fine_amount,tsk.task_form_no,coalesce(tttrn.ttrn_impact_on_organization, tsk.task_impact_on_organization) as impact_on_organization,coalesce(tttrn.ttrn_impact_on_unit, tsk.task_impact_on_unit) as impact_on_unit, tsk.task_implication,tsk.task_imprisonment_duration,tsk.task_imprisonment_implies_to,tsk.task_level,tsk.task_linked_task_id,tsk.task_more_info,tsk.task_interlinkage,tsk.task_specific_due_date,tsk.task_weblinks,tsk.task_statutory_authority,tsk.task_subsequent_amount_per_day,tttrn.ttrn_uh_due_date,tttrn.ttrn_fh_due_date,fhusr.user_first_name, fhusr.user_last_name,tttrn.ttrn_uh_due_date, tttrn.ttrn_prior_days_buffer, tttrn.ttrn_alert_days "
						*/	
							
					sql = "SELECT Distinct tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id AS Performer, prusr.user_first_name as prfn, prusr.user_last_name as prln , rwusr.user_id AS Reviewer , rwusr.user_first_name as rwfn, rwusr.user_last_name as rwln, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id "
							+ ",tsk.task_country_name,tsk.task_state_name,tsk.task_effective_date,tsk.task_excemption_criteria,tsk.task_fine_amount,tsk.task_form_no,coalesce(tttrn.ttrn_impact_on_organization, tsk.task_impact_on_organization) as impact_on_organization,coalesce(tttrn.ttrn_impact_on_unit, tsk.task_impact_on_unit) as impact_on_unit, tsk.task_implication,tsk.task_imprisonment_duration,tsk.task_imprisonment_implies_to,tsk.task_level,tsk.task_linked_task_id,tsk.task_more_info,tsk.task_interlinkage,tsk.task_specific_due_date,tsk.task_weblinks,tsk.task_statutory_authority,tsk.task_subsequent_amount_per_day,tttrn.ttrn_uh_due_date as uduedate,tttrn.ttrn_fh_due_date,fhusr.user_first_name, fhusr.user_last_name,tttrn.ttrn_uh_due_date, tttrn.ttrn_prior_days_buffer, tttrn.ttrn_alert_days "
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
							+ "AND (tmapp.tmap_pr_user_id = " + user_id + " OR tmapp.tmap_rw_user_id = " + user_id
							+ "OR tmapp.tmap_fh_user_id = " + user_id + ")";

				} else {
					if (user_role_id == 2) {
						
						/*
						sql = "SELECT Distinct tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id, prusr.user_first_name, prusr.user_last_name , rwusr.user_id , rwusr.user_first_name , rwusr.user_last_name, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id "
								+ ",tsk.task_country_name,tsk.task_state_name,tsk.task_effective_date,tsk.task_excemption_criteria,tsk.task_fine_amount,tsk.task_form_no,coalesce(tttrn.ttrn_impact_on_organization, tsk.task_impact_on_organization) as impact_on_organization,coalesce(tttrn.ttrn_impact_on_unit, tsk.task_impact_on_unit) as impact_on_unit, tsk.task_implication,tsk.task_imprisonment_duration,tsk.task_imprisonment_implies_to,tsk.task_level,tsk.task_linked_task_id,tsk.task_more_info,tsk.task_interlinkage,tsk.task_specific_due_date,tsk.task_weblinks,tsk.task_statutory_authority,tsk.task_subsequent_amount_per_day,tttrn.ttrn_uh_due_date,tttrn.ttrn_fh_due_date,fhusr.user_first_name, fhusr.user_last_name,tttrn.ttrn_uh_due_date, tttrn.ttrn_prior_days_buffer, tttrn.ttrn_alert_days "
								
								*/
						sql = "SELECT Distinct tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id AS Performer, prusr.user_first_name as prfn, prusr.user_last_name as prln , rwusr.user_id AS Reviewer , rwusr.user_first_name as rwfn, rwusr.user_last_name as rwln, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id "
								+ ",tsk.task_country_name,tsk.task_state_name,tsk.task_effective_date,tsk.task_excemption_criteria,tsk.task_fine_amount,tsk.task_form_no,coalesce(tttrn.ttrn_impact_on_organization, tsk.task_impact_on_organization) as impact_on_organization,coalesce(tttrn.ttrn_impact_on_unit, tsk.task_impact_on_unit) as impact_on_unit, tsk.task_implication,tsk.task_imprisonment_duration,tsk.task_imprisonment_implies_to,tsk.task_level,tsk.task_linked_task_id,tsk.task_more_info,tsk.task_interlinkage,tsk.task_specific_due_date,tsk.task_weblinks,tsk.task_statutory_authority,tsk.task_subsequent_amount_per_day,tttrn.ttrn_uh_due_date as uduedate,tttrn.ttrn_fh_due_date,fhusr.user_first_name, fhusr.user_last_name,tttrn.ttrn_uh_due_date, tttrn.ttrn_prior_days_buffer, tttrn.ttrn_alert_days "
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
								+ "AND (tmapp.tmap_pr_user_id = " + user_id + " OR tmapp.tmap_rw_user_id = " + user_id
								+ ")";
					} else {
						if (user_role_id == 1) {
							/*sql = "SELECT Distinct tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id, prusr.user_first_name, prusr.user_last_name , rwusr.user_id , rwusr.user_first_name , rwusr.user_last_name, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id "
									+ ",tsk.task_country_name,tsk.task_state_name,tsk.task_effective_date,tsk.task_excemption_criteria,tsk.task_fine_amount,tsk.task_form_no,coalesce(tttrn.ttrn_impact_on_organization, tsk.task_impact_on_organization) as impact_on_organization,coalesce(tttrn.ttrn_impact_on_unit, tsk.task_impact_on_unit) as impact_on_unit, tsk.task_implication,tsk.task_imprisonment_duration,tsk.task_imprisonment_implies_to,tsk.task_level,tsk.task_linked_task_id,tsk.task_more_info,tsk.task_interlinkage,tsk.task_specific_due_date,tsk.task_weblinks,tsk.task_statutory_authority,tsk.task_subsequent_amount_per_day,tttrn.ttrn_uh_due_date,tttrn.ttrn_fh_due_date,fhusr.user_first_name, fhusr.user_last_name,tttrn.ttrn_uh_due_date, tttrn.ttrn_prior_days_buffer, tttrn.ttrn_alert_days "
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
									+ "AND (tmapp.tmap_pr_user_id = " + user_id + ")";*/
							
							sql = "SELECT Distinct tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id AS Performer, prusr.user_first_name as prfn, prusr.user_last_name as prln , rwusr.user_id AS Reviewer , rwusr.user_first_name as rwfn, rwusr.user_last_name as rwln, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id "
									+ ",tsk.task_country_name,tsk.task_state_name,tsk.task_effective_date,tsk.task_excemption_criteria,tsk.task_fine_amount,tsk.task_form_no,coalesce(tttrn.ttrn_impact_on_organization, tsk.task_impact_on_organization) as impact_on_organization,coalesce(tttrn.ttrn_impact_on_unit, tsk.task_impact_on_unit) as impact_on_unit, tsk.task_implication,tsk.task_imprisonment_duration,tsk.task_imprisonment_implies_to,tsk.task_level,tsk.task_linked_task_id,tsk.task_more_info,tsk.task_interlinkage,tsk.task_specific_due_date,tsk.task_weblinks,tsk.task_statutory_authority,tsk.task_subsequent_amount_per_day,tttrn.ttrn_uh_due_date as uduedate,tttrn.ttrn_fh_due_date,fhusr.user_first_name, fhusr.user_last_name,tttrn.ttrn_uh_due_date, tttrn.ttrn_prior_days_buffer, tttrn.ttrn_alert_days "
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
									+ "AND (tmapp.tmap_pr_user_id = " + user_id + ")";
									
						} else {
							if (user_role_id == 7) {
								
								/*
								sql = "SELECT DISTINCT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id, prusr.user_first_name, prusr.user_last_name , rwusr.user_id , rwusr.user_first_name , rwusr.user_last_name, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id "
										+ ",tsk.task_country_name,tsk.task_state_name,tsk.task_effective_date,tsk.task_excemption_criteria,tsk.task_fine_amount,tsk.task_form_no,coalesce(tttrn.ttrn_impact_on_organization, tsk.task_impact_on_organization) as impact_on_organization,coalesce(tttrn.ttrn_impact_on_unit, tsk.task_impact_on_unit) as impact_on_unit, tsk.task_implication,tsk.task_imprisonment_duration,tsk.task_imprisonment_implies_to,tsk.task_level,tsk.task_linked_task_id,tsk.task_more_info,tsk.task_interlinkage,tsk.task_specific_due_date,tsk.task_weblinks,tsk.task_statutory_authority,tsk.task_subsequent_amount_per_day,tttrn.ttrn_rw_due_date,tttrn.ttrn_fh_due_date,fhusr.user_first_name, fhusr.user_last_name,tttrn.ttrn_uh_due_date, tttrn.ttrn_prior_days_buffer, tttrn.ttrn_alert_days "
										
									*/	
								sql = "SELECT Distinct tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, prusr.user_id AS Performer, prusr.user_first_name as prfn, prusr.user_last_name as prln , rwusr.user_id AS Reviewer , rwusr.user_first_name as rwfn, rwusr.user_last_name as rwln, tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id "
										+ ",tsk.task_country_name,tsk.task_state_name,tsk.task_effective_date,tsk.task_excemption_criteria,tsk.task_fine_amount,tsk.task_form_no,coalesce(tttrn.ttrn_impact_on_organization, tsk.task_impact_on_organization) as impact_on_organization,coalesce(tttrn.ttrn_impact_on_unit, tsk.task_impact_on_unit) as impact_on_unit, tsk.task_implication,tsk.task_imprisonment_duration,tsk.task_imprisonment_implies_to,tsk.task_level,tsk.task_linked_task_id,tsk.task_more_info,tsk.task_interlinkage,tsk.task_specific_due_date,tsk.task_weblinks,tsk.task_statutory_authority,tsk.task_subsequent_amount_per_day,tttrn.ttrn_uh_due_date as uduedate,tttrn.ttrn_fh_due_date,fhusr.user_first_name, fhusr.user_last_name,tttrn.ttrn_uh_due_date, tttrn.ttrn_prior_days_buffer, tttrn.ttrn_alert_days "
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

			rootNode = mapper.readTree(jsonString);
			Integer entityId = rootNode.path("orga_id").asInt();
			System.out.println("entityName:" + entityId);
			Integer unitId = rootNode.path("loca_id").asInt();
			Integer functionId = rootNode.path("dept_id").asInt();
			Integer executorId = rootNode.path("executor_id").asInt();
			Integer evaluatorId = rootNode.path("evaluator_id").asInt();
			Integer functionHeadId = rootNode.path("fh_user_id").asInt();
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
			if (functionHeadId != 0) {
				sql = sql + " and  fhusr.user_id = " + functionHeadId;

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

			} else {
				sql = sql + " and tsk.task_impact =" + "'" + impact + "'";

			}

			if (proh_pres.equalsIgnoreCase("null")) {

			} else {
				sql = sql + " and tsk.task_prohibitive =" + "'" + proh_pres + "'";

			}

			if (type_of_task.equalsIgnoreCase("null")) {

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

			} else {
				sql = sql + " and tmapp.tmap_client_tasks_id =" + "'" + task_id + "'";
			}
			Query query = em.createNativeQuery(sql);
			System.out.println("sql:" + sql);
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
