package lexprd006.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lexprd006.dao.TasksDao;
import lexprd006.domain.SubTask;
import lexprd006.domain.SubTaskTranscational;
import lexprd006.domain.Task;
import lexprd006.domain.TaskUserMapping;

/*
 * Author: Mahesh Kharote
 * Date: 11/11/2016
 * Purpose: DAO Impl for Functions
 * 
 * 
 * 
 * */

@Transactional
@Repository(value = "tasksDao")
public class TasksDaoImpl implements TasksDao {

	@PersistenceContext
	private EntityManager em;

	// Method Created By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Fetch List of all departments from database
	@Override
	public <T> List<T> getAll(Class<T> clazz) {
		try {
			TypedQuery<T> query = em.createQuery(" from " + clazz.getName(), clazz);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Update particular department in Database
	@Override
	public void updateTaskLegalUpdate(Task task) {
		try {
			em.merge(task);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Method Created By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Update particular department in Database
	@SuppressWarnings("rawtypes")
	@Override
	public Task getTasksForLegalUpdate(String lexcare_task_id) {
		try {
			TypedQuery query = em.createQuery(
					" from " + Task.class.getName() + " where task_lexcare_task_id = :task_lexcare_task_id",
					Task.class);
			query.setParameter("task_lexcare_task_id", lexcare_task_id);
			if (query.getResultList().size() > 0) {
				return (Task) query.getResultList().get(0);
			} // If task is not found by id exception will be caught and
				// null is returned. If null new task else update task found
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Update particular department in Database
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getTaskHistoryByClientTaskId(String tmap_client_task_id) {
		try {

			String sql = "SELECT "
					+ "ttrn.ttrn_id, pruser.user_first_name as prFirstName, pruser.user_last_name as prLastNames, ttrn.ttrn_pr_due_date, ttrn.ttrn_rw_due_date, ttrn.ttrn_fh_due_date, "
					+ "ttrn.ttrn_uh_due_date, "
					+ "ttrn.ttrn_legal_due_date, ttrn.ttrn_completed_date, ttrn.ttrn_performer_comments, ttrn.ttrn_status , ttrn.ttrn_task_completed_by , "
					+ "ttrn.ttrn_submitted_date , "
					+ "ttrn.ttrn_document , ttrn.ttrn_reason_for_non_compliance, tmap.tmap_pr_user_id, tmap.tmap_rw_user_id, tmap.tmap_fh_user_id, "
					+ "pruser.user_email, "
					+ "ttrn.auditoComments, ttrn.auditorAuditTime as auditorAuditTime, ttrn.auditorStatus, ttrn.auditor_performer_by_id, "
					+ "ttrn.auditDate, ttrn.reOpenDateWindow, ttrn.auditDate <= current_date() as auditCondition, "
					+ "ifnull(ttrn.reOpenDateWindow <= current_date(), 0) AS reOpenDateWindowTwo, ttrn.isAuditTasks FROM cfg_task_user_mapping tmap "
					+ "JOIN mst_organization orga on orga.orga_id = tmap.tmap_orga_id "
					+ "JOIN mst_location loca on loca.loca_id = tmap.tmap_loca_id "
					+ "JOIN mst_department dept on dept.dept_id = tmap.tmap_dept_id "
					+ "JOIN mst_user pruser on pruser.user_id = tmap.tmap_pr_user_id "
					+ "JOIN mst_user rwuser on rwuser.user_id = tmap.tmap_rw_user_id "
					+ "JOIN mst_user fhuser on fhuser.user_id = tmap.tmap_fh_user_id "
					+ "JOIN mst_task tsk on tsk.task_id = tmap.tmap_task_id "
					+ "LEFT JOIN trn_task_transactional ttrn on ttrn.ttrn_client_task_id = tmap.tmap_client_tasks_id "
					+ "WHERE ttrn.ttrn_client_task_id = '" + tmap_client_task_id
					+ "' AND ttrn.ttrn_frequency_for_operation !='User_Defined' "
					+ "ORDER BY ttrn.ttrn_legal_due_date DESC ";

			Query query = em.createNativeQuery(sql);
			System.out.println("audit calendar getTaskHistoryByClientTaskId : " + sql);
			// query.setParameter("tmap_client_task_id", tmap_client_task_id);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	// Method Created By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Fetch List of all departments from database
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getTaskDetailsByClientTaskid(String tmap_client_task_id) {
		try {

			String sql = "SELECT tmap.tmap_client_tasks_id, orga.orga_name, loca.loca_name, dept.dept_name, "
					+ "pruser.user_first_name as prFirstName, " + "pruser.user_last_name as prLastNames, "
					+ "rwuser.user_first_name as rwFNames, rwuser.user_last_name as rwLastNames , fhuser.user_first_name as fhFNames, "
					+ "fhuser.user_last_name as fhLastNames , "
					+ "tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference, tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, "
					+ "tsk.task_procedure, tsk.task_more_info, tsk.task_prohibitive, "
					+ "coalesce(ttrn.ttrn_frequency_for_operation , tsk.task_frequency) as task_frequency , tsk.task_form_no, tsk.task_specific_due_date, "
					+ "tsk.task_task_type_of_task, tsk.task_level, tsk.task_excemption_criteria , tsk.task_event, tsk.task_sub_event, "
					+ "tsk.task_implication, tsk.task_imprisonment_duration, tsk.task_imprisonment_implies_to, tsk.task_fine_amount, "
					+ "tsk.task_subsequent_amount_per_day, tsk.task_impact, tsk.task_impact_on_organization, tsk.task_impact_on_unit, "
					+ "tsk.task_interlinkage, tsk.task_linked_task_id, tsk.task_weblinks , tsk.due_date, tmap.tmap_lexcare_task_id,"
					+ "ttrn.auditoComments, ttrn.auditorAuditTime as auditorAuditTime, ttrn.auditorStatus, ttrn.auditor_performer_by_id, "
					+ "ttrn.auditDate, ttrn.reOpenDateWindow, ttrn.auditDate <= CURRENT_DATE() AS auditCondition, "
					+ "ifnull(ttrn.reOpenDateWindow <= CURRENT_DATE(), 0) AS reOpenDateWindowTwo, ttrn.isAuditTasks "
					+ "FROM cfg_task_user_mapping tmap "
					+ "LEFT JOIN trn_task_transactional ttrn ON tmap.tmap_client_tasks_id = ttrn.ttrn_client_task_id "
					+ "LEFT JOIN trn_task_transactional tttrn ON (tmap.tmap_client_tasks_id = tttrn.ttrn_client_task_id AND (ttrn.ttrn_created_at < tttrn.ttrn_created_at)) "
					+ "LEFT JOIN mst_task tsk on tmap.tmap_task_id = tsk.task_id "
					+ "JOIN mst_organization orga on orga.orga_id = tmap.tmap_orga_id "
					+ "JOIN mst_location loca on loca.loca_id = tmap.tmap_loca_id "
					+ "JOIN mst_department dept on dept.dept_id = tmap.tmap_dept_id "
					+ "JOIN mst_user pruser on pruser.user_id = tmap.tmap_pr_user_id "
					+ "JOIN mst_user rwuser on rwuser.user_id = tmap.tmap_rw_user_id "
					+ "JOIN mst_user fhuser on fhuser.user_id = tmap.tmap_fh_user_id "
					+ "WHERE tmap.tmap_client_tasks_id = '" + tmap_client_task_id + "' " + "AND tttrn.ttrn_id IS NULL ";

			Query query = em.createNativeQuery(sql);
			System.out.println("getTaskDetailsByClientTaskid Task Details: " + sql);
			// query.setParameter("tmap_client_task_id", tmap_client_task_id);
			if (query.getResultList().size() > 0) {
				return query.getResultList();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getOriginalFrequency(int ttrn_id) {
		try {
			String sql = "SELECT tsk.task_frequency FROM trn_task_transactional ttrn "
					+ "JOIN cfg_task_user_mapping tmap on tmap.tmap_client_tasks_id = ttrn.ttrn_client_task_id "
					+ "JOIN mst_task tsk on tsk.task_id = tmap.tmap_task_id where ttrn.ttrn_id = '" + ttrn_id + "' ";
			Query query = em.createNativeQuery(sql);
//			query.setParameter("ttrn_id", ttrn_id);
			if (!query.getResultList().isEmpty()) {

				return query.getResultList().get(0).toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Fetch List of all departments from database
	@SuppressWarnings("rawtypes")
	@Override
	public Task getTaskUsingClientTaskID(String tmap_client_task_id) {
		try {
			TypedQuery mappingquery = em.createQuery(
					" from " + TaskUserMapping.class.getName() + " where tmap_client_tasks_id = :tmap_client_task_id",
					TaskUserMapping.class);
			mappingquery.setParameter("tmap_client_task_id", tmap_client_task_id);
			TaskUserMapping taskUserMapping = (TaskUserMapping) mappingquery.getResultList().get(0);

			TypedQuery query = em.createQuery(" from " + Task.class.getName() + " where task_id = :task_id",
					Task.class);
			query.setParameter("task_id", taskUserMapping.getTmap_task_id());
			return (Task) query.getResultList().get(0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Harshad Padole
	// Method Purpose: Get sub task transactional details
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<SubTaskTranscational> getSubTaskHitoryByclientTaskID(String tmap_client_task_id) {
		try {
			TypedQuery query = em.createQuery("FROM " + SubTaskTranscational.class.getName()
					+ " WHERE ttrn_sub_client_task_id=:tmap_client_task_id ORDER BY ttrn_sub_task_ENT_due_date DESC ",
					SubTaskTranscational.class);
			query.setParameter("tmap_client_task_id", tmap_client_task_id);
			if (!query.getResultList().isEmpty())
				return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Harshad Padole
	// Method Purpose: Get sub task details
	@SuppressWarnings("rawtypes")
	@Override
	public SubTask getSubTaskDetailsBysub_task_id(String sub_task_id) {
		try {
			try {
				TypedQuery query = em.createQuery(
						"FROM " + SubTask.class.getName() + " WHERE sub_task_id=:sub_task_id ", SubTask.class);
				query.setParameter("sub_task_id", sub_task_id);
				if (!query.getResultList().isEmpty())
					return (SubTask) query.getResultList().get(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getTaskForMultipleCompletion(String lexcare_id, int user_id, int role_id, int ttrn_id) {
		try {
			String sql = "SELECT ttrn.ttrn_id, tmap.tmap_client_tasks_id, orga.orga_name, loca.loca_name, dept.dept_name, "
					+ "pruser.user_first_name as prFirstName, pruser.user_last_name as prLastNames, "
					+ "rwuser.user_first_name as rwFNames , rwuser.user_last_name as rwLastNames , "
					+ "fhuser.user_first_name as fhFNames, fhuser.user_last_name as fhLastNames, "
					+ "tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference, tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, "
					+ "tsk.task_procedure, tsk.task_more_info,tsk.task_lexcare_task_id, "
					+ "tsk.task_interlinkage,ttrn.ttrn_status,ttrn.ttrn_pr_due_date, ttrn.ttrn_legal_due_date,ttrn.ttrn_frequency_for_operation "
					+ "FROM cfg_task_user_mapping tmap "
					+ "JOIN mst_organization orga on orga.orga_id = tmap.tmap_orga_id "
					+ "JOIN mst_location loca on loca.loca_id = tmap.tmap_loca_id "
					+ "JOIN mst_department dept on dept.dept_id = tmap.tmap_dept_id "
					+ "JOIN mst_user pruser on pruser.user_id = tmap.tmap_pr_user_id "
					+ "JOIN mst_user rwuser on rwuser.user_id = tmap.tmap_rw_user_id "
					+ "JOIN mst_user fhuser on fhuser.user_id = tmap.tmap_fh_user_id "
					+ "JOIN mst_task tsk on tsk.task_id = tmap.tmap_task_id "
					+ "JOIN trn_task_transactional ttrn on ttrn.ttrn_client_task_id = tmap.tmap_client_tasks_id "
					+ "WHERE tmap.tmap_enable_status = 1 " + "AND tmap.tmap_pr_user_id = '" + user_id + "' "
					+ "AND ttrn.ttrn_id != '" + ttrn_id + "' " + "AND (tsk.task_lexcare_task_id = '" + lexcare_id
					+ "' ) " + "AND ttrn.ttrn_legal_due_date >= current_date() " + "AND ttrn.ttrn_status = 'Active'";
			Query query = em.createNativeQuery(sql);
//			query.setParameter("user_id", user_id);
//			query.setParameter("lexcare_id", lexcare_id);
//			query.setParameter("ttrn_id", ttrn_id);
			System.out.println("sql:" + sql);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
