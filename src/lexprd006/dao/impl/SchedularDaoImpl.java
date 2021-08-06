package lexprd006.dao.impl;

import java.text.SimpleDateFormat;
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

import lexprd006.dao.SchedularDao;
import lexprd006.domain.MainTaskEmailLog;
import lexprd006.domain.SubTaskEmailLog;
import lexprd006.domain.SubTaskTranscational;
import lexprd006.domain.TaskTransactional;
import lexprd006.domain.User;

@Repository(value = "schedularDao")
@Transactional
public class SchedularDaoImpl implements SchedularDao {

	public final SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd");
	public final SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

	@PersistenceContext
	private EntityManager em;

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all upcoming Tasks rest Call
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getUpcomingTasks() {
		try {

			String sql = "SELECT tmap.tmap_pr_user_id,tmap.tmap_client_tasks_id,orga.orga_name,loca.loca_name,dept.dept_name,"
					+ "tsk.task_legi_name, tsk.task_rule_name, tsk.task_activity_when, tsk.task_activity as tsk1, "
					+ "ttrn.ttrn_frequency_for_operation, tsk.task_impact, ttrn.ttrn_pr_due_date, ttrn.ttrn_rw_due_date,ttrn.ttrn_fh_due_date, "
					+ "ttrn.ttrn_uh_due_date, ttrn.ttrn_legal_due_date,ttrn.ttrn_alert_days, "
					+ "ttrn.ttrn_id, ttrn.ttrn_client_task_id, tmap.tmap_rw_user_id,tmap.tmap_lexcare_task_id,tmap.tmap_fh_user_id as fhUserId "
					+ "FROM trn_task_transactional ttrn "
					+ "JOIN cfg_task_user_mapping tmap on tmap.tmap_client_tasks_id = ttrn.ttrn_client_task_id "
					+ "JOIN mst_user usr on usr.user_id = tmap.tmap_pr_user_id "
					+ "JOIN mst_organization orga on orga.orga_id = tmap.tmap_orga_id "
					+ "JOIN mst_location loca on loca.loca_id = tmap.tmap_loca_id "
					+ "JOIN mst_department dept on dept.dept_id = tmap_dept_id "
					+ "JOIN mst_task tsk on tsk.task_lexcare_task_id = tmap.tmap_lexcare_task_id "
					+ "WHERE (date_day_sub(ttrn.ttrn_pr_due_date,ttrn.ttrn_alert_days) = " + "'"
					+ sdfOut.format(new Date()) + "'" + " OR ttrn_first_alert = " + "'" + sdfOut.format(new Date())
					+ "'" + " OR ttrn_second_alert = " + "'" + sdfOut.format(new Date()) + "'"
					+ " OR ttrn_third_alert = " + "'" + sdfOut.format(new Date()) + "'"
					+ ") AND ttrn.ttrn_status = 'Active' "
					+ "AND tmap.tmap_enable_status != 0 order by tmap.tmap_pr_user_id asc ";

			// DATEADD(DAY, -(ttrn.ttrn_alert_days), ttrn.ttrn_pr_due_date) = GETDATE()
			Query query = em.createNativeQuery(sql);
			System.out.println("getUpcomingTasks sql : " + sql);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all escalation Tasks rest Call to evaluator
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getEscalationsToEvaluator() {
		try {

			String sql = "SELECT tmap.tmap_rw_user_id,tmap.tmap_client_tasks_id,orga.orga_name,loca.loca_name,dept.dept_name,tsk.task_legi_name,tsk.task_rule_name,tsk.task_activity_when,tsk.task_activity,ttrn.ttrn_frequency_for_operation,tsk.task_impact,ttrn.ttrn_pr_due_date, ttrn.ttrn_rw_due_date,ttrn.ttrn_fh_due_date,ttrn.ttrn_uh_due_date, ttrn.ttrn_legal_due_date,ttrn.ttrn_alert_days, ttrn.ttrn_id, ttrn.ttrn_client_task_id,tmap.tmap_pr_user_id,tmap.tmap_lexcare_task_id,tmap.tmap_fh_user_id "
					+ "FROM trn_task_transactional ttrn "
					+ "JOIN cfg_task_user_mapping tmap on tmap.tmap_client_tasks_id = ttrn.ttrn_client_task_id "
					+ "JOIN mst_user usr on usr.user_id = tmap.tmap_rw_user_id "
					+ "JOIN mst_organization orga on orga.orga_id = tmap.tmap_orga_id "
					+ "JOIN mst_location loca on loca.loca_id = tmap.tmap_loca_id "
					+ "JOIN mst_department dept on dept.dept_id = tmap_dept_id "
					+ "JOIN mst_task tsk on tsk.task_lexcare_task_id = tmap.tmap_lexcare_task_id "
					+ "WHERE date_day_add(ttrn.ttrn_pr_due_date, 1) = " + "'" + sdfOut.format(new Date()) + "'"
					+ " AND ttrn.ttrn_status = 'Active' "
					+ "AND tmap.tmap_enable_status != 0 order by tmap.tmap_rw_user_id asc ";

			Query query = em.createNativeQuery(sql);
			System.out.println("getEscalationsToEvaluator Sql : " + sql);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all escalation Tasks rest Call to Function Head
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getEscalationsToFunctionHead() {
		try {

			String sql = "SELECT tmap.tmap_fh_user_id,tmap.tmap_client_tasks_id,orga.orga_name,loca.loca_name,dept.dept_name,tsk.task_legi_name,tsk.task_rule_name,tsk.task_activity_when,tsk.task_activity,ttrn.ttrn_frequency_for_operation,tsk.task_impact,ttrn.ttrn_pr_due_date, ttrn.ttrn_rw_due_date,ttrn.ttrn_fh_due_date,ttrn.ttrn_uh_due_date, ttrn.ttrn_legal_due_date,ttrn.ttrn_alert_days, ttrn.ttrn_id, ttrn.ttrn_client_task_id,tmap.tmap_pr_user_id,tmap.tmap_lexcare_task_id ,tmap.tmap_rw_user_id "
					+ "FROM trn_task_transactional ttrn "
					+ "JOIN cfg_task_user_mapping tmap on tmap.tmap_client_tasks_id = ttrn.ttrn_client_task_id "
					+ "JOIN mst_user usr on usr.user_id = tmap.tmap_fh_user_id "
					+ "JOIN mst_organization orga on orga.orga_id = tmap.tmap_orga_id "
					+ "JOIN mst_location loca on loca.loca_id = tmap.tmap_loca_id "
					+ "JOIN mst_department dept on dept.dept_id = tmap_dept_id "
					+ "JOIN mst_task tsk on tsk.task_lexcare_task_id = tmap.tmap_lexcare_task_id "
					+ "WHERE date_day_add(ttrn.ttrn_rw_due_date, 1) = " + "'" + sdfOut.format(new Date()) + "'"
					+ " AND ttrn.ttrn_status = 'Active' "
					+ "AND tmap.tmap_enable_status != 0 order by tmap.tmap_fh_user_id asc ";

			Query query = em.createNativeQuery(sql);
			System.out.println("getEscalationsToFunctionHead sql: " + sql);
			if (query.getResultList().size() > 0 && query.getResultList() != null) {
				return query.getResultList();
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	// Method Written By: Harshad Padole(14/04/2016)
	// Method Purpose: Get all escalation Tasks to send Unit Head
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getEscalationsToUnitHead(int user_id) {
		try {

			String sql = "SELECT usr.user_id,tmap.tmap_client_tasks_id,orga.orga_name,loca.loca_name,dept.dept_name,tsk.task_legi_name,tsk.task_rule_name,tsk.task_activity_when,tsk.task_activity,ttrn.ttrn_frequency_for_operation,tsk.task_impact,ttrn.ttrn_pr_due_date, ttrn.ttrn_rw_due_date,ttrn.ttrn_fh_due_date,ttrn.ttrn_uh_due_date, ttrn.ttrn_legal_due_date,ttrn.ttrn_alert_days, ttrn.ttrn_id, ttrn.ttrn_client_task_id,tmap.tmap_pr_user_id,tmap.tmap_lexcare_task_id,tmap.tmap_pr_user_id ,tmap.tmap_rw_user_id,tmap.tmap_fh_user_id "
					+ "FROM trn_task_transactional ttrn "
					+ "JOIN cfg_task_user_mapping tmap on tmap.tmap_client_tasks_id = ttrn.ttrn_client_task_id "
					+ "JOIN cfg_user_entity_mapping umap ON umap.umap_orga_id = tmap.tmap_orga_id "
					+ "AND  umap.umap_loca_id = tmap.tmap_loca_id " + "AND umap.umap_dept_id = tmap.tmap_dept_id "
					+ "JOIN mst_user usr on usr.user_id = umap.umap_user_id "
					+ "JOIN mst_organization orga on orga.orga_id = tmap.tmap_orga_id "
					+ "JOIN mst_location loca on loca.loca_id = tmap.tmap_loca_id "
					+ "JOIN mst_department dept on dept.dept_id = tmap.tmap_dept_id "
					+ "JOIN mst_task tsk on tsk.task_lexcare_task_id = tmap.tmap_lexcare_task_id "
					+ "WHERE date_day_add(ttrn.ttrn_fh_due_date, 1) = " + "'" + sdfOut.format(new Date()) + "'"
					+ " AND ttrn.ttrn_status = 'Active' AND tmap.tmap_enable_status != 0 "
					+ "AND usr.user_role_id = 4 AND usr.user_id =:user_id AND umap.umap_user_id =:user_id "
					+ "order by usr.user_id asc ";

			Query query = em.createNativeQuery(sql);
			query.setParameter("user_id", user_id);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Harshad Padole
	// Method Purpose: Get task details to send while task completion and reopen
	// task
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getTaskDetailsToSend(int ttrn_id, String task_type) {// Task type for Main task and Sub Task
		try {
			String sql = "";
			if (task_type.equals("MainTask"))
				sql = "SELECT ttrn.ttrn_id,ttrn.ttrn_client_task_id,orga.orga_name,loca.loca_name,dept.dept_name,tsk.task_legi_name, "
						+ "tsk.task_rule_name,tsk.task_activity,tsk.task_impact,tsk.task_activity_when,ttrn.ttrn_frequency_for_operation, "
						+ "ttrn.ttrn_pr_due_date,ttrn.ttrn_rw_due_date,ttrn.ttrn_fh_due_date,ttrn.ttrn_uh_due_date,ttrn.ttrn_legal_due_date, "
						+ "ttrn.ttrn_completed_date,ttrn.ttrn_task_completed_by,tmap.tmap_pr_user_id,tmap.tmap_rw_user_id "
						+ "FROM trn_task_transactional ttrn JOIN cfg_task_user_mapping tmap ON tmap.tmap_client_tasks_id = ttrn.ttrn_client_task_id "
						+ "JOIN mst_organization orga ON orga.orga_id = tmap.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmap.tmap_loca_id "
						+ "JOIN mst_department dept ON dept.dept_id = tmap.tmap_dept_id JOIN mst_task tsk ON tsk.task_id =tmap.tmap_task_id "
						+ "WHERE (ttrn.ttrn_status ='Completed' OR ttrn.ttrn_status ='Partially_Completed') AND ttrn.ttrn_id = '"
						+ ttrn_id + "' ";

			if (task_type.equals("ReopenMainTask"))
				sql = "SELECT ttrn.ttrn_id,ttrn.ttrn_client_task_id,orga.orga_name,loca.loca_name,dept.dept_name,tsk.task_legi_name,tsk.task_rule_name,"
						+ "tsk.task_activity,tsk.task_impact,tsk.task_activity_when,ttrn.ttrn_frequency_for_operation,ttrn.ttrn_pr_due_date, "
						+ "ttrn.ttrn_rw_due_date,ttrn.ttrn_fh_due_date,ttrn.ttrn_uh_due_date,ttrn.ttrn_legal_due_date,ttrn.ttrn_completed_date, "
						+ "ttrn.ttrn_task_completed_by,tmap.tmap_pr_user_id,tmap.tmap_rw_user_id FROM trn_task_transactional ttrn "
						+ "JOIN cfg_task_user_mapping tmap ON tmap.tmap_client_tasks_id = ttrn.ttrn_client_task_id "
						+ "JOIN mst_organization orga ON orga.orga_id = tmap.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmap.tmap_loca_id "
						+ "JOIN mst_department dept ON dept.dept_id = tmap.tmap_dept_id JOIN mst_task tsk ON tsk.task_id =tmap.tmap_task_id "
						+ "WHERE ttrn.ttrn_id = '" + ttrn_id + "' ";

			if (task_type.equals("SubTask")) {
				sql = "SELECT ttrn.ttrn_sub_id,ttrn.ttrn_sub_client_task_id,orga.orga_name,loca.loca_name,dept.dept_name,tsk.task_legi_name, "
						+ "tsk.task_rule_name,tsk.task_activity,tsk.task_impact,tsk.task_activity_when, "
						+ "sub_tsk.sub_frequency,ttrn.ttrn_sub_task_pr_due_date,ttrn.ttrn_sub_task_rw_date,ttrn.ttrn_sub_task_FH_due_date, "
						+ "ttrn.ttrn_sub_task_UH_due_date,ttrn.ttrn_sub_task_ENT_due_date,ttrn.ttrn_sub_task_compl_date, "
						+ "ttrn.ttrn_sub_task_completed_by,tmap.tmap_pr_user_id,tmap.tmap_rw_user_id FROM trn_sub_task_transactional ttrn "
						+ "JOIN cfg_task_user_mapping tmap ON tmap.tmap_client_tasks_id = ttrn.ttrn_sub_client_task_id "
						+ "JOIN mst_organization orga ON orga.orga_id = tmap.tmap_orga_id "
						+ "JOIN mst_location loca ON loca.loca_id = tmap.tmap_loca_id "
						+ "JOIN mst_department dept ON dept.dept_id = tmap.tmap_dept_id "
						+ "JOIN mst_task tsk ON tsk.task_id =tmap.tmap_task_id "
						+ "JOIN mst_sub_task sub_tsk ON sub_tsk.sub_task_id = ttrn.ttrn_sub_task_id"
						+ " WHERE ttrn.ttrn_sub_id = '" + ttrn_id + "' AND tmap.tmap_enable_status != 0";
			}

			Query query = em.createNativeQuery(sql);
//			query.setParameter("ttrn_id", ttrn_id);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Harshad Padole
	// Method Purpose: Get user list role wise
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUserList(int role_id) {
		try {
			@SuppressWarnings("rawtypes")
			TypedQuery query = em.createQuery(" from " + User.class.getName() + " where user_role_id = :role_id",
					User.class);
			query.setParameter("role_id", role_id);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Harshad Padole
	// Method Purpose: Get task details to send entity head
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getEscalationsToEntityHead(int user_id) {
		try {
			String sql = "SELECT usr.user_id,tmap.tmap_client_tasks_id,orga.orga_name,loca.loca_name,dept.dept_name,tsk.task_legi_name, "
					+ "tsk.task_rule_name,tsk.task_activity_when,tsk.task_activity,ttrn.ttrn_frequency_for_operation,tsk.task_impact, "
					+ "ttrn.ttrn_pr_due_date, ttrn.ttrn_rw_due_date,ttrn.ttrn_fh_due_date,ttrn.ttrn_uh_due_date, ttrn.ttrn_legal_due_date, "
					+ "ttrn.ttrn_alert_days, ttrn.ttrn_id, ttrn.ttrn_client_task_id, "
					+ "tmap.tmap_pr_user_id as p1, tmap.tmap_lexcare_task_id, tmap.tmap_pr_user_id as p2, "
					+ "tmap.tmap_rw_user_id as rwusrId, tmap.tmap_fh_user_id as fUsrId "
					+ "FROM trn_task_transactional ttrn "
					+ "JOIN cfg_task_user_mapping tmap on tmap.tmap_client_tasks_id = ttrn.ttrn_client_task_id "
					+ "JOIN cfg_user_entity_mapping umap ON umap.umap_orga_id = tmap.tmap_orga_id "
					+ "AND  umap.umap_loca_id = tmap.tmap_loca_id " + "AND umap.umap_dept_id = tmap.tmap_dept_id "
					+ "JOIN mst_user usr on usr.user_id = umap.umap_user_id "
					+ "JOIN mst_organization orga on orga.orga_id = tmap.tmap_orga_id "
					+ "JOIN mst_location loca on loca.loca_id = tmap.tmap_loca_id "
					+ "JOIN mst_department dept on dept.dept_id = tmap.tmap_dept_id "
					+ "JOIN mst_task tsk on tsk.task_lexcare_task_id = tmap.tmap_lexcare_task_id "
					+ "WHERE date_day_add(ttrn.ttrn_uh_due_date, 1) = " + "'" + sdfOut.format(new Date()) + "'"
					+ " AND ttrn.ttrn_status = 'Active' AND tmap.tmap_enable_status != 0 "
					+ "AND usr.user_role_id = 5 AND usr.user_id = '" + user_id + "' AND umap.umap_user_id = '" + user_id
					+ "' " + "order by usr.user_id asc ";

			Query query = em.createNativeQuery(sql);
//			query.setParameter("user_id", user_id);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Harshad Padole
	// Method Purpose: Get task details to auto activate task
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getTaskForAutoActivate() {
		try {
			String sql = "SELECT tttrn.ttrn_client_task_id,tttrn.ttrn_status,tmapp.tmap_pr_user_id,tttrn.ttrn_frequency_for_operation, "
					+ "tttrn.ttrn_frequency_for_alerts,tttrn.ttrn_impact,tttrn.ttrn_impact_on_organization,tttrn.ttrn_impact_on_unit, "
					+ "tttrn.ttrn_allow_approver_reopening,tttrn.ttrn_allow_back_date_completion,tttrn.ttrn_no_of_back_days_allowed, "
					+ "tttrn.ttrn_alert_days,tttrn.ttrn_document,tttrn.ttrn_historical, "
					+ "tttrn.ttrn_prior_days_buffer,tttrn.ttrn_pr_due_date,tttrn.ttrn_rw_due_date,tttrn.ttrn_fh_due_date, "
					+ "tttrn.ttrn_uh_due_date,tttrn.ttrn_legal_due_date,tttrn.ttrn_first_alert,tttrn.ttrn_second_alert, "
					+ "tttrn.ttrn_third_alert,tttrn.ttrn_added_by,tttrn.ttrn_created_at, tttrn.auditDate  "
					+ "FROM cfg_task_user_mapping tmapp "
					+ "JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
					+ "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id AND (tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) "
					+ "WHERE tmapp.tmap_enable_status = 1 AND tttrn.ttrn_legal_due_date < current_date() AND (tttrn.ttrn_status='Active' OR tttrn.ttrn_status='Completed' OR tttrn.ttrn_status='Event_Not_Occured' OR tttrn.ttrn_status='Partially_Completed' OR tttrn.ttrn_status='Re_Opened') AND tttrn.ttrn_frequency_for_operation !='Event_Based' AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND tttrn.ttrn_frequency_for_operation !='One_Time' "
					+ "AND ttttrn.ttrn_id IS NULL";
			Query query = em.createNativeQuery(sql);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Harshad Padole
	// Method Purpose : get details to send
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> showCauseNoticeReminder() {
		try {
			String sql = "SELECT  scau.scau_id, usr.user_email, "
					+ "usr.user_first_name, usr.user_last_name,scau.scau_ralated_to,scau.scau_notice_date,scau.scau_received_date, "
					+ "scau.scau_deadline_date,scau.scau_next_action_item from mst_showcausenotice scau "
					+ "JOIN mst_user usr ON usr.user_id = scau.scau_responsible_person "
					+ "WHERE scau.scau_reminder_date =" + "'" + sdfOut.format(new Date()) + "'";
			Query query = em.createNativeQuery(sql);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Harshad Padole
	// Method Purpose : get details to to send one day before deadline
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> showCauseNoticeOneDayBeforeDeadline() {
		try {
			String sql = "SELECT  scau.scau_id, usr.user_email, usr.user_first_name, usr.user_last_name,scau.scau_ralated_to,scau.scau_notice_date,scau.scau_received_date,scau.scau_deadline_date,scau.scau_next_action_item from mst_showcausenotice scau JOIN mst_user usr ON usr.user_id = scau.scau_responsible_person "
					+ "WHERE date_day_sub(scau.scau_deadline_date, 1) = '" + sdfOut.format(new Date()) + "' ";
			Query query = em.createNativeQuery(sql);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Harshad Padole
	// Method Purpose : Get action item details
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> actionItemReminder() {
		try {

			String sql = "SELECT  scau.scau_id, usr.user_email as respo_email, usr.user_first_name as respo_fname , usr.user_last_name as respo_lname,user.user_email as repor_email, user.user_first_name as repor_fname , user.user_last_name as repor_lname, tsca.tscn_action_taken,tsca.tscn_next_action_item,tsca.tscn_next_due_date, scau.scau_ralated_to from mst_showcausenotice scau JOIN trn_show_cause_notice tsca ON scau.scau_id = tsca.tcau_scau_id JOIN mst_user usr ON usr.user_id = scau.scau_responsible_person JOIN mst_user user ON user.user_id = scau.scau_reporting_person "
					+ "WHERE tsca.tscn_reminder_date = '" + sdfOut.format(new Date()) + "' ";
			Query query = em.createNativeQuery(sql);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getFunctionHead(String type) {
		try {

			String sql = "";
			if (type.equals("Main"))
				sql = "SELECT DISTINCT umap.umap_user_id FROM cfg_user_entity_mapping umap JOIN cfg_task_user_mapping tmap ON tmap.tmap_orga_id = umap.umap_orga_id AND tmap.tmap_loca_id = umap.umap_loca_id AND tmap.tmap_dept_id = umap.umap_dept_id JOIN trn_task_transactional ttrn ON ttrn.ttrn_client_task_id = tmap.tmap_client_tasks_id JOIN mst_user usr ON usr.user_id = umap.umap_user_id AND usr.user_role_id = 3 where ttrn.ttrn_pr_due_date< '"
						+ sdfOut.format(new Date()) + "' AND ttrn.ttrn_status = 'Active' AND ttrn.ttrn_rw_due_date <= '"
						+ sdfOut.format(new Date()) + "'  AND ttrn.ttrn_fh_due_date >= '" + sdfOut.format(new Date())
						+ "'  AND date_day_add(ttrn.ttrn_rw_due_date,1)= '" + sdfOut.format(new Date()) + "'  ";
			else
				sql += "SELECT DISTINCT umap.umap_user_id FROM cfg_user_entity_mapping umap "
						+ "JOIN cfg_task_user_mapping tmap ON tmap.tmap_orga_id = umap.umap_orga_id AND tmap.tmap_loca_id = umap.umap_loca_id AND tmap.tmap_dept_id = umap.umap_dept_id JOIN trn_sub_task_transactional ttrnsub ON ttrnsub.ttrn_sub_client_task_id = tmap.tmap_client_tasks_id JOIN mst_user usr ON usr.user_id = umap.umap_user_id AND usr.user_role_id = 3 where ttrnsub.ttrn_sub_task_pr_due_date< '"
						+ sdfOut.format(new Date())
						+ "'  AND ttrnsub.ttrn_sub_task_status = 'Active' AND ttrnsub.ttrn_sub_task_rw_date < '"
						+ sdfOut.format(new Date()) + "'  AND ttrnsub.ttrn_sub_task_FH_due_date >= '"
						+ sdfOut.format(new Date()) + "'  AND date_day_add(ttrnsub.ttrn_sub_task_rw_date,1)= '"
						+ sdfOut.format(new Date()) + "' ";

			Query query = em.createNativeQuery(sql);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getDetailsToSendFH(int user_id, String type) {
		try {
			String sql = "";
			if (type.equals("Main"))
				sql = "SELECT ttrn.ttrn_id,ttrn.ttrn_client_task_id,loca.loca_name,tsk.task_legi_name,tsk.task_rule_name,tsk.task_activity, "
						+ "tsk.task_impact,ttrn.ttrn_legal_due_date,ttrn.ttrn_fh_due_date,ttrn.ttrn_rw_due_date,ttrn.ttrn_pr_due_date, "
						+ "tmap.tmap_pr_user_id,tmap.tmap_rw_user_id,tmap.tmap_orga_id,tmap.tmap_loca_id,tmap.tmap_dept_id, "
						+ "usr1.user_email,prEmail.user_email as pr_email,rwEmail.user_email as rw_email,dept.dept_name,tsk.task_activity_when, "
						+ "ttrn.ttrn_frequency_for_operation,orga.orga_name,prEmail.user_first_name,prEmail.user_last_name "
						+ "from cfg_task_user_mapping tmap JOIN cfg_user_entity_mapping umap ON tmap.tmap_orga_id = umap.umap_orga_id AND "
						+ "tmap.tmap_loca_id = umap.umap_loca_id AND tmap.tmap_dept_id = umap.umap_dept_id JOIN trn_task_transactional ttrn "
						+ "ON ttrn.ttrn_client_task_id = tmap.tmap_client_tasks_id JOIN mst_organization orga ON orga.orga_id = tmap.tmap_orga_id "
						+ "JOIN mst_location loca ON loca.loca_id = tmap.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmap.tmap_dept_id "
						+ "JOIN mst_task tsk ON tsk.task_id = tmap.tmap_task_id JOIN mst_user usr1 ON usr1.user_id = umap.umap_user_id "
						+ "LEFT JOIN mst_user prEmail ON prEmail.user_id = tmap.tmap_pr_user_id "
						+ "LEFT JOIN mst_user rwEmail ON rwEmail.user_id = tmap.tmap_rw_user_id where umap.umap_user_id = '"
						+ user_id + "' " + "AND ttrn.ttrn_pr_due_date< '" + sdfOut.format(new Date())
						+ "' AND ttrn.ttrn_status = 'Active' AND ttrn.ttrn_rw_due_date <= '" + sdfOut.format(new Date())
						+ "' AND ttrn.ttrn_fh_due_date >= '" + sdfOut.format(new Date())
						+ "' AND date_day_add(ttrn.ttrn_rw_due_date,1)= '" + sdfOut.format(new Date()) + "' ";
			else
				sql = "SELECT ttrn.ttrn_sub_id,ttrn.ttrn_sub_client_task_id,loca.loca_name,tsk.task_legi_name,tsk.task_rule_name,tsk.task_activity, "
						+ "tsk.task_impact,ttrn.ttrn_sub_task_ENT_due_date,ttrn.ttrn_sub_task_FH_due_date,ttrn.ttrn_sub_task_rw_date, "
						+ "ttrn.ttrn_sub_task_pr_due_date,tmap.tmap_pr_user_id,tmap.tmap_rw_user_id,tmap.tmap_orga_id,tmap.tmap_loca_id, "
						+ "tmap.tmap_dept_id,usr1.user_email,prEmail.user_email as pr_email,rwEmail.user_email as rw_email, "
						+ "dept.dept_name,tsk.task_activity_when,stsk.sub_frequency,orga.orga_name, "
						+ "prEmail.user_first_name,prEmail.user_last_name from cfg_task_user_mapping tmap "
						+ "JOIN cfg_user_entity_mapping umap ON tmap.tmap_orga_id = umap.umap_orga_id AND "
						+ "tmap.tmap_loca_id = umap.umap_loca_id AND tmap.tmap_dept_id = umap.umap_dept_id "
						+ "JOIN trn_sub_task_transactional ttrn ON ttrn.ttrn_sub_client_task_id = tmap.tmap_client_tasks_id "
						+ "JOIN mst_sub_task stsk ON stsk.sub_task_id = ttrn.ttrn_sub_task_id JOIN mst_organization orga ON "
						+ "orga.orga_id = tmap.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmap.tmap_loca_id "
						+ "JOIN mst_department dept ON dept.dept_id = tmap.tmap_dept_id JOIN mst_task tsk ON tsk.task_id = tmap.tmap_task_id "
						+ "JOIN mst_user usr1 ON usr1.user_id = umap.umap_user_id LEFT JOIN mst_user prEmail ON prEmail.user_id = tmap.tmap_pr_user_id "
						+ "LEFT JOIN mst_user rwEmail ON rwEmail.user_id = tmap.tmap_rw_user_id where umap.umap_user_id = '"
						+ user_id + "' AND " + "ttrn.ttrn_sub_task_pr_due_date< '" + sdfOut.format(new Date())
						+ "' AND ttrn.ttrn_sub_task_status = 'Active' AND ttrn.ttrn_sub_task_rw_date <= '"
						+ sdfOut.format(new Date()) + "' AND ttrn.ttrn_sub_task_FH_due_date >= '"
						+ sdfOut.format(new Date()) + "' AND date_day_add(ttrn.ttrn_sub_task_rw_date,1)= '"
						+ sdfOut.format(new Date()) + "'";

			Query query = em.createNativeQuery(sql);
//			query.setParameter("user_id", user_id);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Harshad Padole
	// Method Purpose : Get upcoming sub task
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getUpcomingSubTasks() {
		try {
			String sql = "SELECT tmap.tmap_pr_user_id,tmap.tmap_client_tasks_id,orga.orga_name,loca.loca_name,dept.dept_name,tsk.task_legi_name, "
					+ "tsk.task_rule_name,tsk.task_activity_when,tsk.task_activity,stsk.sub_frequency,tsk.task_impact,ttrn.ttrn_sub_task_pr_due_date, "
					+ "ttrn.ttrn_sub_task_rw_date,ttrn.ttrn_sub_task_FH_due_date,ttrn.ttrn_sub_task_UH_due_date, ttrn.ttrn_sub_task_ENT_due_date, "
					+ "ttrn.ttrn_sub_task_alert_prior_day, ttrn.ttrn_sub_id, ttrn.ttrn_sub_task_id,tmap.tmap_rw_user_id, "
					+ "tmap.tmap_lexcare_task_id,tmap.tmap_fh_user_id, ttrn.ttrn_sub_client_task_id, stsk.sub_equipment_description, "
					+ "stsk.sub_equipment_location, stsk.sub_equipment_number, stsk.sub_equipment_type "
					+ "FROM trn_sub_task_transactional ttrn "
					+ "JOIN mst_sub_task stsk ON stsk.sub_task_id = ttrn.ttrn_sub_task_id "
					+ "JOIN cfg_task_user_mapping tmap on tmap.tmap_client_tasks_id = ttrn.ttrn_sub_client_task_id "
					+ "JOIN mst_user usr on usr.user_id = tmap.tmap_pr_user_id "
					+ "JOIN mst_organization orga on orga.orga_id = tmap.tmap_orga_id "
					+ "JOIN mst_location loca on loca.loca_id = tmap.tmap_loca_id "
					+ "JOIN mst_department dept on dept.dept_id = tmap_dept_id "
					+ "JOIN mst_task tsk on tsk.task_lexcare_task_id = tmap.tmap_lexcare_task_id "
					+ "WHERE date_day_sub(ttrn.ttrn_sub_task_pr_due_date,ttrn.ttrn_sub_task_alert_prior_day) = " + "'"
					+ sdfOut.format(new Date()) + "'"
					+ " AND ttrn.ttrn_sub_task_status = 'Active' AND tmap.tmap_enable_status != 0 "
					+ "order by tmap.tmap_pr_user_id asc ";
			Query query = em.createNativeQuery(sql);
			System.out.println("Subtask Query:" + sql);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Harshad Padole
	// Method Purpose : Get escalation sub task to Evaluator
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getEscalationsToEvaluatorSubTasks() {
		try {
			String sql = "SELECT tmap.tmap_rw_user_id,tmap.tmap_client_tasks_id,orga.orga_name,loca.loca_name,dept.dept_name,tsk.task_legi_name,tsk.task_rule_name,tsk.task_activity_when,tsk.task_activity,stsk.sub_frequency,tsk.task_impact,ttrn.ttrn_sub_task_pr_due_date, ttrn.ttrn_sub_task_rw_date,ttrn.ttrn_sub_task_FH_due_date,ttrn.ttrn_sub_task_UH_due_date, ttrn.ttrn_sub_task_ENT_due_date,ttrn.ttrn_sub_task_alert_prior_day, ttrn.ttrn_sub_id, ttrn.ttrn_sub_task_id,tmap.tmap_pr_user_id,tmap.tmap_lexcare_task_id, tmap.tmap_fh_user_id, stsk.sub_equipment_description, stsk.sub_equipment_location, stsk.sub_equipment_number, stsk.sub_equipment_type "
					+ "FROM trn_sub_task_transactional ttrn "
					+ "JOIN mst_sub_task stsk ON stsk.sub_task_id = ttrn.ttrn_sub_task_id "
					+ "JOIN cfg_task_user_mapping tmap on tmap.tmap_client_tasks_id = ttrn.ttrn_sub_client_task_id "
					+ "JOIN mst_user usr on usr.user_id = tmap.tmap_pr_user_id "
					+ "JOIN mst_organization orga on orga.orga_id = tmap.tmap_orga_id "
					+ "JOIN mst_location loca on loca.loca_id = tmap.tmap_loca_id "
					+ "JOIN mst_department dept on dept.dept_id = tmap_dept_id "
					+ "JOIN mst_task tsk on tsk.task_lexcare_task_id = tmap.tmap_lexcare_task_id "
					+ "WHERE date_day_add(ttrn.ttrn_sub_task_pr_due_date,1) = " + "'" + sdfOut.format(new Date()) + "'"
					+ " AND ttrn.ttrn_sub_task_status = 'Active' "
					+ "AND tmap.tmap_enable_status != 0 order by tmap.tmap_rw_user_id asc ";
			Query query = em.createNativeQuery(sql);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Harshad Padole
	// Method Purpose : Get escalation sub task to FH
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getEscalationsToFuntionHeadSubTasks() {
		try {
			String sql = "SELECT tmap.tmap_fh_user_id,tmap.tmap_client_tasks_id,orga.orga_name,loca.loca_name,dept.dept_name, "
					+ "tsk.task_legi_name,tsk.task_rule_name,tsk.task_activity_when,tsk.task_activity,stsk.sub_frequency, "
					+ "tsk.task_impact,ttrn.ttrn_sub_task_pr_due_date, ttrn.ttrn_sub_task_rw_date,ttrn.ttrn_sub_task_FH_due_date, "
					+ "ttrn.ttrn_sub_task_UH_due_date, ttrn.ttrn_sub_task_ENT_due_date,ttrn.ttrn_sub_task_alert_prior_day, ttrn.ttrn_sub_id, "
					+ "ttrn.ttrn_sub_task_id,tmap.tmap_pr_user_id,tmap.tmap_lexcare_task_id,tmap.tmap_rw_user_id, tmap.tmap_fh_user_id, "
					+ "stsk.sub_equipment_description, stsk.sub_equipment_location, stsk.sub_equipment_number, stsk.sub_equipment_type "
					+ "FROM trn_sub_task_transactional ttrn "
					+ "JOIN mst_sub_task stsk ON stsk.sub_task_id = ttrn.ttrn_sub_task_id "
					+ "JOIN cfg_task_user_mapping tmap on tmap.tmap_client_tasks_id = ttrn.ttrn_sub_client_task_id "
					+ "JOIN mst_user usr on usr.user_id = tmap.tmap_pr_user_id "
					+ "JOIN mst_organization orga on orga.orga_id = tmap.tmap_orga_id "
					+ "JOIN mst_location loca on loca.loca_id = tmap.tmap_loca_id "
					+ "JOIN mst_department dept on dept.dept_id = tmap_dept_id "
					+ "JOIN mst_task tsk on tsk.task_lexcare_task_id = tmap.tmap_lexcare_task_id "
					+ "WHERE date_day_add(ttrn.ttrn_sub_task_rw_date,1) = " + "'" + sdfOut.format(new Date()) + "'"
					+ " AND ttrn.ttrn_sub_task_status = 'Active' "
					+ "AND tmap.tmap_enable_status != 0 order by tmap.tmap_fh_user_id asc ";
			Query query = em.createNativeQuery(sql);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Harshad Padole
	// Method Purpose : Get escalation sub task to UH

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getEscalationsToUnitHeadSubTasks(int user_id) {
		try {
			String sql = "SELECT usr.user_id,tmap.tmap_client_tasks_id,orga.orga_name,loca.loca_name,dept.dept_name,tsk.task_legi_name, "
					+ "tsk.task_rule_name,tsk.task_activity_when,tsk.task_activity,stsk.sub_frequency,tsk.task_impact,ttrn.ttrn_sub_task_pr_due_date, "
					+ "ttrn.ttrn_sub_task_rw_date,ttrn.ttrn_sub_task_FH_due_date,ttrn.ttrn_sub_task_UH_due_date, "
					+ "ttrn.ttrn_sub_task_ENT_due_date,ttrn.ttrn_sub_task_alert_prior_day, ttrn.ttrn_sub_id, ttrn.ttrn_sub_client_task_id, "
					+ "tmap.tmap_pr_user_id,tmap.tmap_lexcare_task_id,tmap.tmap_rw_user_id,tmap.tmap_fh_user_id,ttrn.ttrn_sub_task_id, "
					+ "stsk.sub_equipment_description, stsk.sub_equipment_location, stsk.sub_equipment_number, stsk.sub_equipment_type "
					+ "FROM trn_sub_task_transactional ttrn "
					+ "JOIN mst_sub_task stsk ON stsk.sub_task_id = ttrn.ttrn_sub_task_id "
					+ "JOIN cfg_task_user_mapping tmap on tmap.tmap_client_tasks_id = ttrn.ttrn_sub_client_task_id "
					+ "JOIN cfg_user_entity_mapping umap ON umap.umap_orga_id = tmap.tmap_orga_id "
					+ "AND  umap.umap_loca_id = tmap.tmap_loca_id "
					+ "AND umap.umap_dept_id = tmap.tmap_dept_id AND umap.umap_user_id = '" + user_id + "' "
					+ "JOIN mst_user usr on usr.user_id = umap.umap_user_id "
					+ "JOIN mst_organization orga on orga.orga_id = tmap.tmap_orga_id "
					+ "JOIN mst_location loca on loca.loca_id = tmap.tmap_loca_id "
					+ "JOIN mst_department dept on dept.dept_id = tmap.tmap_dept_id "
					+ "JOIN mst_task tsk on tsk.task_lexcare_task_id = tmap.tmap_lexcare_task_id "
					+ "WHERE date_day_add(ttrn.ttrn_sub_task_FH_due_date, 1) = " + "'" + sdfOut.format(new Date()) + "'"
					+ " AND ttrn.ttrn_sub_task_status = 'Active' AND tmap.tmap_enable_status != 0 "
					+ "AND usr.user_role_id = 4 AND usr.user_id = '" + user_id + "' ";
			Query query = em.createNativeQuery(sql);
//			query.setParameter("user_id", user_id);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Harshad Padole
	// Method Purpose : Get escalation sub task to EH
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getEscalationsToEntityHeadSubTasks(int user_id) {
		try {
			String sql = "SELECT usr.user_id,tmap.tmap_client_tasks_id,orga.orga_name,loca.loca_name,dept.dept_name, "
					+ "tsk.task_legi_name,tsk.task_rule_name,tsk.task_activity_when,tsk.task_activity,stsk.sub_frequency,tsk.task_impact, "
					+ "ttrn.ttrn_sub_task_pr_due_date, ttrn.ttrn_sub_task_rw_date,ttrn.ttrn_sub_task_FH_due_date,ttrn.ttrn_sub_task_UH_due_date, "
					+ "ttrn.ttrn_sub_task_ENT_due_date,ttrn.ttrn_sub_task_alert_prior_day, ttrn.ttrn_sub_id, ttrn.ttrn_sub_client_task_id, "
					+ "tmap.tmap_pr_user_id,tmap.tmap_lexcare_task_id,tmap.tmap_rw_user_id,tmap.tmap_fh_user_id, ttrn.ttrn_sub_task_id, "
					+ "stsk.sub_equipment_description, stsk.sub_equipment_location, stsk.sub_equipment_number, stsk.sub_equipment_type "
					+ "FROM trn_sub_task_transactional ttrn "
					+ "JOIN mst_sub_task stsk ON stsk.sub_task_id = ttrn.ttrn_sub_task_id "
					+ "JOIN cfg_task_user_mapping tmap on tmap.tmap_client_tasks_id = ttrn.ttrn_sub_client_task_id "
					+ "JOIN cfg_user_entity_mapping umap ON umap.umap_orga_id = tmap.tmap_orga_id "
					+ "AND  umap.umap_loca_id = tmap.tmap_loca_id "
					+ "AND umap.umap_dept_id = tmap.tmap_dept_id AND umap.umap_user_id = '" + user_id + "' "
					+ "JOIN mst_user usr on usr.user_id = umap.umap_user_id "
					+ "JOIN mst_organization orga on orga.orga_id = tmap.tmap_orga_id "
					+ "JOIN mst_location loca on loca.loca_id = tmap.tmap_loca_id "
					+ "JOIN mst_department dept on dept.dept_id = tmap.tmap_dept_id "
					+ "JOIN mst_task tsk on tsk.task_lexcare_task_id = tmap.tmap_lexcare_task_id "
					+ "WHERE date_day_add(ttrn.ttrn_sub_task_UH_due_date, 1) = " + "'" + sdfOut.format(new Date()) + "'"
					+ " AND ttrn.ttrn_sub_task_status = 'Active' AND tmap.tmap_enable_status != 0 "
					+ "AND usr.user_role_id = 5 AND usr.user_id = '" + user_id + "' ";
			Query query = em.createNativeQuery(sql);
//			query.setParameter("user_id", user_id);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void saveEmailLog(MainTaskEmailLog log) {
		try {
			em.persist(log);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Object> getTodaysMainTask(String subject, String curr_date) {

		try {
			String sql = "Select log.log_id,log.activity,log.client_task_id,log.created_at,log.email_subject,log.email_to,"
					+ "log.entity_name,log.eval_id,log.evaluator_date,log.exec_id,log.executor_date,log.frequency,log.func_head_date,"
					+ "log.function_name, log.impact,log.legal_due_date,log.log_when,log.name_of_legislation,log.name_of_rule,log.unit_head_date,log.unit_name,log.fh_id "
					+ "from maintask_email_log log " + "where log.email_subject = " + "'" + subject + "'"
					+ " AND log.created_at = " + "'" + curr_date + "'";
			System.out.println("SQL:" + sql);
			Query query = em.createNativeQuery(sql);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void saveEmailLog(SubTaskEmailLog log) {
		try {
			em.persist(log);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<Object> getTodaysSubTask(String subject, String curr_date) {
		try {
			String sql = "Select log.log_id,log.activity,log.client_task_id,log.created_at,log.email_subject,log.email_to,"
					+ "log.entity_name,log.eval_id,log.evaluator_date,log.exec_id,log.executor_date,log.frequency,log.func_head_date,"
					+ "log.function_name, log.impact,log.legal_due_date,log.log_when,log.name_of_legislation,log.name_of_rule,log.unit_head_date,log.unit_name,log.sub_client_task_id, log.fh_id, log.compliance_activity, log.sub_task_unit, log.name_of_contractor, log.compliance_title "
					+ "from subtask_email_log log " + "where log.email_subject = " + "'" + subject + "'"
					+ " AND log.created_at = " + "'" + curr_date + "'";
			System.out.println("getTodaysSubTask SQL: " + sql);
			Query query = em.createNativeQuery(sql);

			if (query.getResultList().size() > 0) {
				return query.getResultList();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int updateEmailLog(String client_task_id, String subject) {
		try {
			System.out.println("client_task_id:" + client_task_id);
			String[] task_id = client_task_id.split(",");
			String newString = "";
			String created_at = sdfOut.format(new Date());
			for (int i = 0; i < task_id.length; i++) {
				if (i == task_id.length - 1) {
					newString += "'" + task_id[i] + "'";
				} else {
					newString += "'" + task_id[i] + "',";
				}
			}

			System.out.println("newString:" + newString);
			Query query = null;
			String date = sdfOut.format(new Date());
			System.out.println("date:" + date);

			String sql = "Update maintask_email_log set email_status = 1, email_sent_at = " + "'" + date + "'"
					+ " where email_subject = " + "'" + subject + "'" + " AND created_at = " + "'" + created_at + "'"
					+ " AND client_task_id IN (" + newString + ")";
			System.out.println("SQL UPDATE QUERY:" + sql);
			query = em.createQuery(sql);

			return query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;

	}

	@Override
	public int updateSubTaskEmailLog(String client_task_id, String subject) {
		try {
			System.out.println("client_task_id:" + client_task_id);
			String[] task_id = client_task_id.split(",");
			String newString = "";
			String created_at = sdfOut.format(new Date());
			for (int i = 0; i < task_id.length; i++) {
				if (i == task_id.length - 1) {
					newString += "'" + task_id[i] + "'";
				} else {
					newString += "'" + task_id[i] + "',";
				}
			}

			System.out.println("newString:" + newString);
			Query query = null;
			String date = sdfOut.format(new Date());

			String sql = "Update subtask_email_log set email_status = 1, email_sent_at = " + "'" + date + "'"
					+ " where email_subject = " + "'" + subject + "'" + " AND created_at = " + "'" + created_at + "'"
					+ " AND sub_client_task_id IN (" + newString + ")";
			System.out.println("SQL UPDATE QUERY:" + sql);
			query = em.createNativeQuery(sql);

			return query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;

	}

	@Override
	public List<Object> getNonDeliveredMails(String subject, String curr_date) {

		try {
			String sql = "Select log.log_id,log.activity,log.client_task_id,log.created_at,log.email_subject,log.email_to,"
					+ "log.entity_name,log.eval_id,log.evaluator_date,log.exec_id,log.executor_date,log.frequency,log.func_head_date,"
					+ "log.function_name, log.impact,log.legal_due_date,log.log_when,log.name_of_legislation,log.name_of_rule,log.unit_head_date,log.unit_name,log.fh_id "
					+ "from maintask_email_log log " + "where log.email_subject = " + "'" + subject + "'"
					+ " AND log.created_at = " + "'" + curr_date + "' AND log.email_status = 0 ";
			System.out.println("SQL:" + sql);
			Query query = em.createNativeQuery(sql);
			if (query.getResultList().size() > 0) {
				return query.getResultList();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Object> getSubTaskForAutoActivate() {
		try {
			String sql = "SELECT ttrn.ttrn_sub_client_task_id,ttrn.ttrn_sub_task_status,tsk.sub_frequency,ttrn.ttrn_sub_task_allow_approver_reopening,"
					+ "ttrn.ttrn_sub_task_back_date_allowed,ttrn.ttrn_sub_task_alert_prior_day,ttrn.ttrn_sub_task_document,ttrn.ttrn_sub_task_historical,"
					+ "ttrn.ttrn_sub_task_buffer_days,ttrn.ttrn_sub_task_pr_due_date,ttrn.ttrn_sub_task_rw_date,ttrn.ttrn_sub_task_FH_due_date,"
					+ "ttrn.ttrn_sub_task_UH_due_date,ttrn.ttrn_sub_task_ENT_due_date,ttrn.ttrn_sub_task_first_alert,ttrn.ttrn_sub_task_second_alert,"
					+ "ttrn.ttrn_sub_task_third_alert,ttrn.ttrn_sub_task_id,ttrn.ttrn_sub_task_updated_at "
					+ "FROM trn_sub_task_transactional ttrn JOIN mst_sub_task tsk ON ttrn.ttrn_sub_task_id = tsk.sub_task_id "
					+ "where ttrn.ttrn_sub_task_ENT_due_date < current_date() AND (ttrn.ttrn_sub_task_status='Active' "
					+ "OR ttrn.ttrn_sub_task_status='Completed' OR ttrn.ttrn_sub_task_status='Partially_Completed' OR ttrn.ttrn_sub_task_status='Re_Opened') "
					+ "AND tsk.sub_frequency !='Event_Based' AND tsk.sub_frequency !='User_Defined' AND tsk.sub_frequency !='One_Time' ";
			System.out.println("sql:" + sql);
			Query query = em.createNativeQuery(sql);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<SubTaskTranscational> getSubtaskIfExist(String sub_task_id, Date leagl_date) {
		try {
			TypedQuery<SubTaskTranscational> query = em.createQuery("from " + SubTaskTranscational.class.getName()
					+ " where ttrn_sub_task_id = :sub_task_id AND ttrn_sub_task_ENT_due_date = :ttrn_sub_task_ENT_due_date ",
					SubTaskTranscational.class);
			query.setParameter("sub_task_id", sub_task_id);
			query.setParameter("ttrn_sub_task_ENT_due_date", leagl_date);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Object> getNonDeliveredMailsOfSubtask(String subject, String curr_date) {

		try {
			String sql = "Select log.log_id,log.activity,log.client_task_id,log.created_at,log.email_subject,log.email_to,"
					+ "log.entity_name,log.eval_id,log.evaluator_date,log.exec_id,log.executor_date,log.frequency,log.func_head_date,"
					+ "log.function_name, log.impact,log.legal_due_date,log.log_when,log.name_of_legislation,log.name_of_rule,log.unit_head_date,log.unit_name, log.sub_client_task_id,log.fh_id "
					+ "from subtask_email_log log " + "where log.email_subject = " + "'" + subject + "'"
					+ " AND log.created_at = " + "'" + curr_date + "' AND log.email_status = 0 ";
			System.out.println("SQL:" + sql);
			Query query = em.createNativeQuery(sql);

			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Object> autoComplianceReport(String jsonString, int orga_id) {

		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);

			int user_id = Integer.parseInt(jsonObject.get("user_id").toString());
			String from_date = jsonObject.get("from_date").toString();
			String to_date = jsonObject.get("to_date").toString();
			System.out.println(" from date-- " + from_date + " user id " + user_id);
			System.out.println(" to date-- " + to_date);
			String sql = "SELECT tmap.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name, "
					+ "tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, ttrn.ttrn_impact, "
					+ "ttrn.ttrn_frequency_for_operation,ttrn.ttrn_status, ttrn.ttrn_performer_comments, "
					+ "ttrn.ttrn_reason_for_non_compliance, tmap.tmap_pr_user_id, prusr.user_first_name, "
					+ "prusr.user_last_name,ttrn.ttrn_legal_due_date, ttrn.ttrn_submitted_date, "
					+ "ttrn.ttrn_completed_date, tsk.task_reference , orga.orga_name , loca.loca_name , "
					+ "dept.dept_name, "
					+ "tmap.tmap_rw_user_id as twusrId, tmap.tmap_fh_user_id as fhUsrId, rwusr.user_first_name as rwFName, "
					+ "rwusr.user_last_name as rwLNames, fhusr.user_first_name as fNames, fhusr.user_last_name as fgLasNames "
					+ "FROM trn_task_transactional ttrn "
					+ " LEFT JOIN cfg_task_user_mapping tmap on tmap.tmap_client_tasks_id = ttrn.ttrn_client_task_id "
					+ " JOIN mst_task tsk on tmap.tmap_task_id = tsk.task_id "
					+ " JOIN mst_user prusr on prusr.user_id = tmap.tmap_pr_user_id "
					+ "JOIN mst_user rwusr on rwusr.user_id = tmap.tmap_rw_user_id "
					+ "JOIN mst_user fhusr on fhusr.user_id = tmap.tmap_fh_user_id "
					+ "JOIN mst_organization orga on orga.orga_id = tmap.tmap_orga_id "
					+ "JOIN mst_location loca on loca.loca_id = tmap.tmap_loca_id "
					+ "JOIN mst_department dept on dept.dept_id = tmap_dept_id  " + "WHERE (tmap.tmap_pr_user_id = "
					+ user_id + " OR tmap.tmap_rw_user_id = " + user_id + " OR tmap.tmap_fh_user_id = " + user_id + ") "
					+ "AND ttrn.ttrn_status != 'Inactive' AND tmap.tmap_enable_status != 0 "
					+ "AND ttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal'"
					+ " and ttrn.ttrn_legal_due_date BETWEEN " + "'" + from_date + "'" + "and " + "'" + to_date + "'"
					+ " AND orga.orga_id = " + orga_id;

			System.out.println("This is report query: " + sql);
			Query query = em.createNativeQuery(sql);

			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public List<Object> getEvaluatorList(String jsonString, int orga_id) {

		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);

			int user_id = Integer.parseInt(jsonObject.get("user_id").toString());
			String from_date = jsonObject.get("from_date").toString();
			String to_date = jsonObject.get("to_date").toString();
			System.out.println(" from date-- " + from_date + " user id " + user_id);
			System.out.println(" to date-- " + to_date);
			String sql = "SELECT tmap.tmap_rw_user_id, COUNT(tmap.tmap_rw_user_id) FROM trn_task_transactional ttrn "
					+ " LEFT JOIN cfg_task_user_mapping tmap on tmap.tmap_client_tasks_id = ttrn.ttrn_client_task_id "
					+ " JOIN mst_task tsk on tmap.tmap_task_id = tsk.task_id "
					+ "JOIN mst_user prusr on prusr.user_id = tmap.tmap_pr_user_id "
					+ "JOIN mst_user rwusr on rwusr.user_id = tmap.tmap_rw_user_id "
					+ "JOIN mst_user fhusr on fhusr.user_id = tmap.tmap_fh_user_id "
					+ "JOIN mst_organization orga on orga.orga_id = tmap.tmap_orga_id "
					+ "JOIN mst_location loca on loca.loca_id = tmap.tmap_loca_id "
					+ "JOIN mst_department dept on dept.dept_id = tmap_dept_id " + "WHERE (tmap.tmap_pr_user_id = "
					+ user_id + " OR tmap.tmap_rw_user_id = " + user_id + " OR tmap.tmap_fh_user_id = " + user_id + ") "
					+ "AND ttrn.ttrn_status = 'Active' AND tmap.tmap_enable_status != 0 "
					+ "AND ttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal'"
					+ " AND ttrn.ttrn_legal_due_date < curdate() " + " and ttrn.ttrn_legal_due_date BETWEEN " + "'"
					+ from_date + "'" + "and " + "'" + to_date + "'" + " AND orga.orga_id = " + orga_id;

			System.out.println("This is report query: " + sql);
			Query query = em.createNativeQuery(sql);

			if (query.getResultList().size() > 0) {
				return query.getResultList();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public void updateIsDocumentDownloadStatus(TaskTransactional tsk) {
		try {
			em.merge(tsk);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
