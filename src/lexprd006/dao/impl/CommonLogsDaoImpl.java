package lexprd006.dao.impl;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lexprd006.dao.CommonLogsDao;
import lexprd006.domain.ComplianceReportLogs;
import lexprd006.domain.EmailLogs;
import lexprd006.domain.LogActivateDeActivateTasks;
import lexprd006.domain.LogReactivation;
import lexprd006.domain.LogTasksConfiguration;
import lexprd006.domain.TaskAssignLogs;
import lexprd006.domain.TaskChangeComplianeAssignLogs;

@Repository
@Transactional
public class CommonLogsDaoImpl implements CommonLogsDao {

	@PersistenceContext
	EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getLoginLogsData(HttpSession session) {

		String sql = "SELECT user_first_name, user_last_name, USER_ID, isOnline, logOutTime, loginTime, user_email, "
				+ "CONCAT(FLOOR(HOUR(TIMEDIFF(logOutTime, loginTime)) / 24), ' days, ', "
				+ "MOD(HOUR(TIMEDIFF(logOutTime, loginTime)), 24), ' hours, ', "
				+ "MINUTE(TIMEDIFF(logOutTime, loginTime)), ' minutes, ', "
				+ "SECOND(TIMEDIFF(logOutTime, loginTime)), ' seconds') AS TimeDiff from mst_user "
				+ "where loginTime IS NOT NULL";
		Query createNativeQuery = em.createNativeQuery(sql);
		System.out.println("getLoginLogsData  SQL : " + sql);
		List resultList = createNativeQuery.getResultList();
		if (resultList.size() > 0 && resultList != null) {
			return resultList;
		}
		return resultList;
	}

	@Override
	public void persist(TaskAssignLogs assignLogs) {
		em.persist(assignLogs);
	}

	@Override
	public void persistChangeCompliane(TaskChangeComplianeAssignLogs assignLogs) {
		em.persist(assignLogs);
	}

	@Override
	public void persistLogTasksConfiguration(LogTasksConfiguration configLogs) {
		em.persist(configLogs);
	}

	@Override
	public void prsistActivateDeactivateLog(LogActivateDeActivateTasks aTasks) {
		em.persist(aTasks);
	}

	@Override
	public void saveLogReActivation(LogReactivation log) {
		em.persist(log);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getAssignLogsData(HttpSession session) {
		String sql = "SELECT org.orga_name, dept.dept_name, loca.loca_name, concat(pruser.user_first_name, ' ', pruser.user_last_name) as "
				+ "ExecutorName,  concat(rwuser.user_first_name, ' ', rwuser.user_last_name) as EvaluatorName, "
				+ "concat(fhuser.user_first_name, ' ', fhuser.user_last_name) as FunctionHead, asLog.TASKS_ID, asLog.LEX_TASKS_ID, "
				+ "asLog.ASSIGN_TIME as createdTime, concat(usr.user_first_name, ' ' ,usr.user_last_name) as addedBy FROM task_assign_log asLog JOIN cfg_task_user_mapping tmap ON "
				+ "tmap.tmap_lexcare_task_id = asLog.LEX_TASKS_ID AND asLog.TASKS_ID = tmap.tmap_client_tasks_id "
				+ "JOIN mst_organization org ON org.orga_id = asLog.ENTITY_ID "
				+ "JOIN mst_user pruser on pruser.user_id = tmap.tmap_pr_user_id "
				+ "JOIN mst_user rwuser on rwuser.user_id = tmap.tmap_rw_user_id "
				+ "JOIN mst_user fhuser on fhuser.user_id = tmap.tmap_fh_user_id "
				+ "JOIN mst_location loca on loca.loca_id = tmap.tmap_loca_id "
				+ "JOIN mst_department dept on dept.dept_id = tmap.tmap_dept_id "
				+ "JOIN mst_user usr ON usr.user_id = asLog.USER_ID ";
		Query createNativeQuery = em.createNativeQuery(sql);
		// System.out.println("getAssignLogsData SQL : " + sql);
		List resultList = createNativeQuery.getResultList();
		if (resultList.size() > 0 && resultList != null) {
			return resultList;
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> changeComplianceOwnerLogsData(HttpSession session) {
		String sql = "SELECT org.orga_name, dept.dept_name, loca.loca_name, concat(pruser.user_first_name, ' ', pruser.user_last_name) as "
				+ "ExecutorName,  concat(rwuser.user_first_name, ' ', rwuser.user_last_name) as EvaluatorName, "
				+ "concat(fhuser.user_first_name, ' ', fhuser.user_last_name) as FunctionHead, asLog.TASKS_ID, asLog.LEX_TASKS_ID, "
				+ "asLog.ASSIGN_TIME as createdTime, concat(usr.user_first_name, ' ' ,usr.user_last_name) as addedBy FROM task_change_compliance_owner_log asLog JOIN cfg_task_user_mapping tmap ON "
				+ "tmap.tmap_lexcare_task_id = asLog.LEX_TASKS_ID AND asLog.TASKS_ID = tmap.tmap_client_tasks_id "
				+ "JOIN mst_organization org ON org.orga_id = asLog.ENTITY_ID "
				+ "JOIN mst_user pruser on pruser.user_id = tmap.tmap_pr_user_id "
				+ "JOIN mst_user rwuser on rwuser.user_id = tmap.tmap_rw_user_id "
				+ "JOIN mst_user fhuser on fhuser.user_id = tmap.tmap_fh_user_id "
				+ "JOIN mst_location loca on loca.loca_id = tmap.tmap_loca_id "
				+ "JOIN mst_department dept on dept.dept_id = tmap.tmap_dept_id "
				+ "JOIN mst_user usr ON usr.user_id = asLog.USER_ID";
		Query createNativeQuery = em.createNativeQuery(sql);
		List resultList = createNativeQuery.getResultList();
		if (resultList.size() > 0 && resultList != null) {
			return resultList;
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> tasksConfigLogsData(HttpSession session) {
		String sql = "SELECT config.ASSIGN_TIME, config.BUFFER_DAYS, DATE_FORMAT(config.EVALUATOR_DUE_DATE,'%d-%m-%Y') as EVALUATOR_DUE_DATE, "
				+ "DATE_FORMAT(config.EXECUTOR_DUE_DATE, '%d-%m-%Y') as EXECUTOR_DUE_DATE, config.FREQUENCY, "
				+ "DATE_FORMAT(config.FUNCTION_HEAD_DUE_DATE, '%d-%m-%Y') as FUNCTION_HEAD_DUE_DATE, "
				+ "DATE_FORMAT(config.LEAGAL_DUE_DATE, '%d-%m-%Y') as LEAGAL_DUE_DATE, config.LEX_TASKS_ID, "
				+ "config.PRIOD_DAYS, config.TASKS_ID, DATE_FORMAT(config.UNIT_HEAD_DUE_DATE, '%d-%m-%Y') as UNIT_HEAD_DUE_DATE, "
				+ "CONCAT(usr.user_first_name,' ', usr.user_last_name) AS addedBy "
				+ "FROM tasks_configuration_log config JOIN mst_user usr ON config.user_id = usr.user_id ";
		Query createNativeQuery = em.createNativeQuery(sql);
		List resultList = createNativeQuery.getResultList();
		if (resultList.size() > 0 && resultList != null) {
			return resultList;
		}
		return resultList;
	}

	@Override
	public void persistComplianceReportLogs(ComplianceReportLogs logs) {
		em.persist(logs);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> complianceReportLogsData(HttpSession session) {
		String sql = "SELECT date_format(log.FROM_DATE, '%d-%m-%Y') as fromDate, date_format(log.TO_DATE, '%d-%m-%Y') as toDate, "
				+ "log.LEGAL_STATUS, log.TASKS_IMPACT, log.USER_ID, org.orga_name, loca.loca_name, concat(usr.user_first_name, ' ', usr.user_last_name) as "
				+ "name, date_format(log.CREATED_TIME, '%d-%m-%Y %h:%m:%s') as createdTime, ifnull(dept.dept_name, 'NA') as functionName "
				+ "FROM compliance_report_log log " + "JOIN mst_organization org ON org.orga_id = log.ENTITY_ID "
				+ "JOIN mst_location loca ON loca.loca_id = log.UNIT_ID "
				+ "JOIN mst_user usr ON usr.user_id = log.USER_ID "
				+ "LEFT JOIN mst_department dept ON dept.dept_id = log.FUNCTION_ID";
		Query createNativeQuery = em.createNativeQuery(sql);
		List resultList = createNativeQuery.getResultList();
		if (resultList.size() > 0 && resultList != null) {
			return resultList;
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> activateDeActivateLogsData(HttpSession session) {

		String sql = "select TTRN_ID from activate_deact_tasks_log";
		Query createNativeQuery = em.createNativeQuery(sql);
		List resultList = createNativeQuery.getResultList();
		List resultList2 = null;
		System.out.println("sql at : " + sql);
		String sql1 = "";
		Iterator iterator = resultList.iterator();
		while (iterator.hasNext()) {
			Object next = (Object) iterator.next();
			System.out.println("next : " + next.toString());
			// sql1 = "select ttrn.ttrn_client_task_id, log.TTRN_ID, log.TASKS_STATUS,
			// log.ACTIVATE_DEACT_TIME, concat(usr.user_first_name, ' ', usr.user_last_name)
			// as addedBy from trn_task_transactional ttrn JOIN activate_deact_tasks_log log
			// ON log.TTRN_ID = ttrn.ttrn_id JOIN mst_user usr ON usr.user_id = log.USER_ID
			// where ttrn.ttrn_id = '" + next + "' order by log.ACTIVATE_DEACT_TIME DESC
			// LIMIT 0, 100 ";
			sql1 = "select ttrn.ttrn_client_task_id, log.TTRN_ID, log.TASKS_STATUS, log.ACTIVATE_DEACT_TIME, concat(usr.user_first_name, ' ', usr.user_last_name) as addedBy from trn_task_transactional ttrn JOIN activate_deact_tasks_log log ON log.TTRN_ID = ttrn.ttrn_id JOIN mst_user usr ON usr.user_id = log.USER_ID order by log.ACTIVATE_DEACT_TIME DESC LIMIT 0, 100 ";
			Query qry = em.createNativeQuery(sql1);
			System.out.println("activateDeActivateLogsData Sql : " + sql1);
			resultList2 = qry.getResultList();
			if (resultList2.size() > 0 && resultList2 != null) {
				return resultList2;
			}
		}
		return resultList2;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getReactivateTasksData(HttpSession session) {
		String sql = "SELECT log.FREQUENCY, log.REACTIVATION_TIME, log.STATUS, log.TASKS_ID, "
				+ "concat(usr.user_first_name, ' ', usr.user_last_name) as addedBy FROM mahindra.log_reactivation log, "
				+ "mst_user usr where usr.user_id = log.USER_ID order by log.REACTIVATION_TIME desc limit 0, 500 ";
		// + "JOIN mst_user usr ON usr.user_id = log.USER_ID LIMIT 0, 500";
		Query createNativeQuery = em.createNativeQuery(sql);
		List resultList = createNativeQuery.getResultList();
		if (resultList.size() > 0 && resultList != null) {
			return resultList;
		}
		return resultList;
	}

	@Override
	public void saveUpcomingMail(EmailLogs logs) {
		em.persist(logs);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> emailLogsData(HttpSession session) {
		String sql = "select EMAIL_STATUS, TASKS_ID, MAIL_SENT_TO, CREATED_TIME from email_logs LIMIT 0, 200";
		Query createNativeQuery = em.createNativeQuery(sql);
		List resultList = createNativeQuery.getResultList();
		if (resultList.size() > 0 && resultList != null) {
			return resultList;
		}
		return resultList;
	}

	@Override
	public void tasksDeletionQuery(String newString) {
		try {
			String sql = "DELETE from cfg_task_user_mapping where tmap_client_tasks_id IN (" + newString + ")";
			Query createNativeQuery = em.createNativeQuery(sql);
			System.out.println("sql : " + sql);
			int executeUpdate = createNativeQuery.executeUpdate();
			System.out.println("executeUpdate : " + executeUpdate);

			String sql1 = "DELETE from trn_task_transactional where ttrn_client_task_id IN (" + newString + ") ";
			Query createNativeQuery1 = em.createNativeQuery(sql1);
			System.out.println("sql : " + sql1);
			int executeUpdate1 = createNativeQuery1.executeUpdate();
			System.out.println("executeUpdate1 : " + executeUpdate1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void queryDeActivation(String newString) {
		String sql = "Update trn_task_transactional JOIN cfg_task_user_mapping ON tmap_client_tasks_id = ttrn_client_task_id "
				+ "JOIN mst_task ON tmap_task_id = task_id SET ttrn_status = 'Inactive' where ttrn_client_task_id IN ("
				+ newString + ") ";
		Query createNativeQuery1 = em.createNativeQuery(sql);
		System.out.println("queryDeActivation sql : " + sql);
		int executeUpdate1 = createNativeQuery1.executeUpdate();
		System.out.println("queryDeActivation up : " + executeUpdate1);
	}

	@Override
	public void queryDisableTasks(String newString) {
		String sql = "update cfg_task_user_mapping JOIN mst_task ON tmap_task_id = task_id SET tmap_enable_diasble_task = 0 where tmap_client_tasks_id IN ("
				+ newString + ") ";
		Query createNativeQuery1 = em.createNativeQuery(sql);
		System.out.println("queryDeActivation sql : " + sql);
		int executeUpdate1 = createNativeQuery1.executeUpdate();
		System.out.println("queryDeActivation up : " + executeUpdate1);
	}

}
