package lexprd006.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lexprd006.dao.ReportsDao;

@Repository(value = "reportsDao")
@Transactional
public class ReportsDaoImpl implements ReportsDao {

	@PersistenceContext
	private EntityManager em;

	public final SimpleDateFormat sdfIn = new SimpleDateFormat("dd-MM-yyyy");
	public final SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd");

	// Method Written By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Get all tasks from repository To DB
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> generateReports(String jsonString, HttpSession session) {
		try {

			String sql = "";

			JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);
			int entity_id = Integer.parseInt(jsonObject.get("entity_id").toString());
			int unit_id = Integer.parseInt(jsonObject.get("unit_id").toString());
			int func_id = Integer.parseInt(jsonObject.get("func_id").toString());
			int exec_id = Integer.parseInt(jsonObject.get("exec_id").toString());
			int eval_id = Integer.parseInt(jsonObject.get("eval_id").toString());

			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			int role_id = Integer.parseInt(session.getAttribute("sess_role_id").toString());
			Date frmDate = sdfIn.parse(jsonObject.get("from_date").toString());
			Date toDate = sdfIn.parse(jsonObject.get("to_date").toString());

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String from_date = sdf.format(frmDate);
			String to_date = sdf.format(toDate);

			String impact = jsonObject.get("task_impact").toString();

			System.out.println("role_id : " + role_id + "\t user_id : " + user_id);

			if (role_id == 1 || role_id == 2 || role_id == 3) {
				sql = "SELECT tmap.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name, tsk.task_activity_who, tsk.task_activity_when, "
						+ "tsk.task_activity, ttrn.ttrn_impact, ttrn.ttrn_frequency_for_operation,ttrn.ttrn_status,ttrn.ttrn_performer_comments, "
						+ "ttrn.ttrn_reason_for_non_compliance, "
						+ "ttrn.ttrn_performer_user_id as prfUserId, usr.user_first_name as ufFirstName, usr.user_last_name as usLastNames, "
						+ "ttrn.ttrn_legal_due_date,ttrn.ttrn_submitted_date,ttrn.ttrn_completed_date,tsk.task_reference , "
						+ "orga.orga_name , loca.loca_name , dept.dept_name, ttrn.ttrn_id, tsk.task_frequency, tsk.task_cat_law_name, "
						+ "tsk.task_implication, "
						+ "rwusr.user_id as rwUserId, rwusr.user_first_name as rwUFirstNames, rwusr.user_last_name as rwLastsNames, "
						+ "fhusr.user_id as fhUsersId, fhusr.user_first_name as fhFIrstNames, fhusr.user_last_name as fhULastNames "
						+ "FROM trn_task_transactional ttrn "
						+ "LEFT JOIN cfg_task_user_mapping tmap ON ttrn.ttrn_client_task_id = tmap.tmap_client_tasks_id "
						+ "LEFT JOIN mst_task tsk ON tsk.task_id = tmap.tmap_task_id "
						+ "LEFT JOIN mst_organization orga ON orga.orga_id = tmap.tmap_orga_id "
						+ "LEFT JOIN mst_location loca ON loca.loca_id = tmap.tmap_loca_id "
						+ "LEFT JOIN mst_department dept ON dept.dept_id = tmap.tmap_dept_id "
						+ "LEFT JOIN mst_user usr ON usr.user_id = tmap.tmap_pr_user_id "
						+ "LEFT JOIN mst_user rwusr ON rwusr.user_id = tmap.tmap_rw_user_id "
						+ "LEFT JOIN mst_user fhusr ON fhusr.user_id = tmap.tmap_fh_user_id "
						+ "WHERE tmap.tmap_enable_status = 1 and (tmap.tmap_pr_user_id = '" + user_id
						+ "' or tmap.tmap_rw_user_id = '" + user_id + "' or " + "tmap.tmap_fh_user_id = '" + user_id
						+ "') and ( " + "(ttrn.ttrn_status = 'Completed' AND ttrn.ttrn_legal_due_date BETWEEN '"
						+ from_date + "' AND '" + to_date + "') "
						+ "OR (ttrn.ttrn_status = 'Active' AND ttrn.ttrn_legal_due_date BETWEEN '" + from_date
						+ "' AND '" + to_date
						+ "' ) OR (ttrn.ttrn_status = 'Re_Opened' AND ttrn.ttrn_legal_due_date BETWEEN '" + from_date
						+ "' AND '" + to_date + "' )"
						+ " OR (ttrn.ttrn_status = 'Partially_Completed' AND ttrn.ttrn_legal_due_date BETWEEN '"
						+ from_date + "' AND '" + to_date + "') " + ") ";

				// Checking if following are present while searching
				if (entity_id != 0) {
					sql += " AND tmap.tmap_orga_id = '" + entity_id + "' ";
				}
				if (unit_id != 0) {
					sql += " AND tmap.tmap_loca_id = '" + unit_id + "' ";
				}
				if (func_id != 0) {
					sql += " AND tmap.tmap_dept_id = '" + func_id + "' ";
				}
				if (exec_id != 0) {
					sql += " AND tmap.tmap_pr_user_id = '" + exec_id + "' ";
				}
				if (eval_id != 0) {
					sql += " AND tmap.tmap_rw_user_id = '" + eval_id + "' ";
				}
				if (!impact.equals("NA")) {
					sql += " AND ttrn.ttrn_impact = '" + impact + "' ";
				}
				System.out.println("This is report query: " + sql);
			} else {
				sql = "SELECT tmap.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name, tsk.task_activity_who, tsk.task_activity_when, "
						+ "tsk.task_activity, ttrn.ttrn_impact, ttrn.ttrn_frequency_for_operation,ttrn.ttrn_status, "
						+ "ttrn.ttrn_performer_comments,ttrn.ttrn_reason_for_non_compliance, "
						+ "tmap.tmap_pr_user_id as prUserId, prusr.user_first_name as pFirstNames, prusr.user_last_name as prULastNames, "
						+ "ttrn.ttrn_legal_due_date,ttrn.ttrn_submitted_date,ttrn.ttrn_completed_date,tsk.task_reference , orga.orga_name , "
						+ "loca.loca_name , dept.dept_name, ttrn.ttrn_id, tsk.task_frequency, tsk.task_cat_law_name as r1,  "
						+ "tsk.task_implication, "
						+ "rwusr.user_id as rwUsrIs, rwusr.user_first_name as rwFirstNames, rwusr.user_last_name as rwLastNamess, "
						+ "fhusr.user_id as fhUserIS, fhusr.user_first_name as fhUFirstNames, fhusr.user_last_name as fhULastNames "
						+ "FROM trn_task_transactional ttrn "
						+ "JOIN cfg_task_user_mapping tmap on tmap.tmap_client_tasks_id = ttrn.ttrn_client_task_id "
						+ "LEFT JOIN mst_task tsk on tmap.tmap_task_id = tsk.task_id "
						+ "LEFT JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmap.tmap_orga_id AND "
						+ "umapp.umap_loca_id = tmap.tmap_loca_id AND umapp.umap_dept_id = tmap.tmap_dept_id AND umapp.umap_user_id = '"
						+ user_id + "' " + "LEFT JOIN mst_user prusr on prusr.user_id = tmap.tmap_pr_user_id "
						+ "LEFT JOIN mst_user rwusr ON rwusr.user_id = tmap.tmap_rw_user_id "
						+ "LEFT JOIN mst_user fhusr ON fhusr.user_id = tmap.tmap_fh_user_id "
						+ "LEFT JOIN mst_organization orga on orga.orga_id = tmap.tmap_orga_id "
						+ "LEFT JOIN mst_location loca on loca.loca_id = tmap.tmap_loca_id "
						+ "LEFT JOIN mst_department dept on dept.dept_id = tmap_dept_id WHERE ( "
						+ "(ttrn.ttrn_status = 'Completed' AND ttrn.ttrn_legal_due_date BETWEEN '" + from_date
						+ "' AND '" + to_date + "') "
						+ "OR (ttrn.ttrn_status = 'Active' AND ttrn.ttrn_legal_due_date BETWEEN '" + from_date
						+ "' AND '" + to_date
						+ "' OR (ttrn.ttrn_status = 'Re_Opened' AND ttrn.ttrn_legal_due_date BETWEEN '" + from_date
						+ "' AND '" + to_date + "') ) "
						+ "OR (ttrn.ttrn_status = 'Partially_Completed' AND ttrn.ttrn_legal_due_date BETWEEN '"
						+ from_date + "' AND '" + to_date + "') " + ")  AND tmap.tmap_enable_status != 0 ";

				// Checking if following are present while searching
				if (entity_id != 0) {
					sql += " AND tmap.tmap_orga_id = '" + entity_id + "' ";
				}
				if (unit_id != 0) {
					sql += " AND tmap.tmap_loca_id = '" + unit_id + "' ";
				}
				if (func_id != 0) {
					sql += " AND tmap.tmap_dept_id = '" + func_id + "' ";
				}
				if (exec_id != 0) {
					sql += " AND tmap.tmap_pr_user_id = '" + exec_id + "' ";
				}
				if (eval_id != 0) {
					sql += " AND tmap.tmap_rw_user_id = '" + eval_id + "' ";
				}
				if (!impact.equals("NA")) {
					sql += " AND ttrn.ttrn_impact = '" + impact + "' ";
				}
				System.out.println("impact : " + impact);
				System.out.println("This is report query: " + sql);
			}

			Query query = em.createNativeQuery(sql);
//			query.setParameter("user_id", user_id);
//			query.setParameter("from_date", from_date);
//			query.setParameter("to_date", to_date);

			// Setting Parameters if following are present
//			if (entity_id != 0) {
//				query.setParameter("entity_id", entity_id);
//			}
//			if (unit_id != 0) {
//				query.setParameter("unit_id", unit_id);
//			}
//			if (func_id != 0) {
//				query.setParameter("func_id", func_id);
//			}
//			if (exec_id != 0) {
//				query.setParameter("exec_id", exec_id);
//			}
//			if (eval_id != 0) {
//				query.setParameter("eval_id", eval_id);
//			}
//			if (!impact.equals("NA")) {
//				query.setParameter("impact", impact);
//			}

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

	// Method Written By: Harshad Padole(04/07/2017)
	// Method Purpose: Get task for report
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> generateSubTaskReports(String jsonString, HttpSession session) {
		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);
			int entity_id = Integer.parseInt(jsonObject.get("entity_id").toString());
			int unit_id = Integer.parseInt(jsonObject.get("unit_id").toString());
			int func_id = Integer.parseInt(jsonObject.get("func_id").toString());
			int exec_id = Integer.parseInt(jsonObject.get("exec_id").toString());
			int eval_id = Integer.parseInt(jsonObject.get("eval_id").toString());

			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			Date frmDate = sdfIn.parse(jsonObject.get("from_date").toString());
			Date toDate = sdfIn.parse(jsonObject.get("to_date").toString());

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String from_date = sdf.format(frmDate);
			String to_date = sdf.format(toDate);

			String impact = jsonObject.get("task_impact").toString();
			String sql = "SELECT sttrn.ttrn_sub_client_task_id, tsk.task_legi_name, tsk.task_rule_name, tsk.task_activity_who, "
					+ "tsk.task_activity_when, tsk.task_activity, tsk.task_impact,stsk.sub_frequency, sttrn.ttrn_sub_task_status , "
					+ "sttrn.ttrn_sub_task_comment, sttrn.ttrn_sub_task_reason_for_non_compliance, "
					+ "usr.user_id as usIsd, usr.user_first_name as usFirstNames, usr.user_last_name as usLastsNmaes, "
					+ "sttrn.ttrn_sub_task_ENT_due_date, sttrn.ttrn_sub_task_submition_date,sttrn.ttrn_sub_task_compl_date,tsk.task_reference, "
					+ "orga.orga_name, loca.loca_name, dept.dept_name ,sttrn.ttrn_sub_task_id,stsk.sub_equipment_number, "
					+ "stsk.sub_equipment_location,stsk.sub_equipment_description "
					+ "FROM trn_sub_task_transactional sttrn "
					+ "LEFT JOIN cfg_task_user_mapping tmapp on sttrn.ttrn_sub_client_task_id = tmapp.tmap_client_tasks_id "
					+ "JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id "
					+ "AND umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = '" + user_id + "' "
					+ "JOIN mst_task tsk on tsk.task_lexcare_task_id = tmapp.tmap_lexcare_task_id "
					+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
					+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
					+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
					+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
					+ "JOIN mst_sub_task stsk ON stsk.sub_task_id = sttrn.ttrn_sub_task_id "
					+ "WHERE sttrn.ttrn_sub_task_status != 'Inactive' "
					+ "AND ((sttrn.ttrn_sub_task_status = 'Completed' AND sttrn.ttrn_sub_task_submition_date between '"
					+ from_date + "' AND '" + to_date + "'  ) OR "
					+ "(sttrn.ttrn_sub_task_status = 'Active' AND  sttrn.ttrn_sub_task_ENT_due_date between '"
					+ from_date + "' AND '" + to_date + "' )) ";

			if (entity_id != 0) {
				sql += " AND tmapp.tmap_orga_id = '" + entity_id + "' ";
			}
			if (unit_id != 0) {
				sql += " AND tmapp.tmap_loca_id = '" + unit_id + "' ";
			}
			if (func_id != 0) {
				sql += " AND tmapp.tmap_dept_id = '" + func_id + "' ";
			}
			if (exec_id != 0) {
				sql += " AND tmapp.tmap_pr_user_id = '" + exec_id + "' ";
			}
			if (eval_id != 0) {
				sql += " AND tmapp.tmap_rw_user_id = '" + eval_id + "' ";
			}
			if (!impact.equals("NA")) {
				sql += " AND sttrn.ttrn_impact = '" + impact + "' ";
			}
			System.out.println("This is sub task report query: " + sql);
			Query query = em.createNativeQuery(sql);
//			query.setParameter("user_id", user_id);
//			query.setParameter("from_date", from_date);
//			query.setParameter("to_date", to_date);

			// Setting Parameters if following are present
//			if (entity_id != 0) {
//				query.setParameter("entity_id", entity_id);
//			}
//			if (unit_id != 0) {
//				query.setParameter("unit_id", unit_id);
//			}
//			if (func_id != 0) {
//				query.setParameter("func_id", func_id);
//			}
//			if (exec_id != 0) {
//				query.setParameter("exec_id", exec_id);
//			}
//			if (eval_id != 0) {
//				query.setParameter("eval_id", eval_id);
//			}
//			if (!impact.equals("NA")) {
//				query.setParameter("impact", impact);
//			}

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
	public <T> List<T> automatedMonthlyReports(String jsonString) {

		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);

			int user_id = Integer.parseInt(jsonObject.get("user_id").toString());
			Date frmDate = sdfIn.parse(jsonObject.get("from_date").toString());
			Date toDate = sdfIn.parse(jsonObject.get("to_date").toString());

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String from_date = sdf.format(frmDate);
			String to_date = sdf.format(toDate);

			System.out.println(" from date-- " + from_date + " user id " + user_id);
			// System.out.println(" to date-- " + to_date);
			String sql = "SELECT tmap.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name, tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, "
					+ "ttrn.ttrn_impact, ttrn.ttrn_frequency_for_operation,ttrn.ttrn_status,ttrn.ttrn_performer_comments,ttrn.ttrn_reason_for_non_compliance,tmap. "
					+ "tmap_pr_user_id,prusr.user_first_name, prusr.user_last_name, "
					+ "ttrn.ttrn_legal_due_date,ttrn.ttrn_submitted_date,ttrn.ttrn_completed_date,tsk.task_reference , orga.orga_name , loca.loca_name , dept.dept_name "
					+ "FROM trn_task_transactional ttrn "
					+ "JOIN cfg_task_user_mapping tmap on tmap.tmap_client_tasks_id = ttrn.ttrn_client_task_id "
					+ "LEFT JOIN mst_task tsk on tmap.tmap_task_id = tsk.task_id "
					+ "JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmap.tmap_orga_id AND umapp.umap_loca_id = tmap.tmap_loca_id AND "
					+ "umapp.umap_dept_id = tmap.tmap_dept_id AND umapp.umap_user_id = '" + user_id + "' "
					+ "JOIN mst_user prusr on prusr.user_id = tmap.tmap_pr_user_id "
					+ "JOIN mst_organization orga on orga.orga_id = tmap.tmap_orga_id "
					+ "JOIN mst_location loca on loca.loca_id = tmap.tmap_loca_id "
					+ "JOIN mst_department dept on dept.dept_id = tmap_dept_id " + "WHERE ( "
					+ "(ttrn.ttrn_status = 'Completed' AND ttrn.ttrn_submitted_date BETWEEN '" + from_date + "' AND '"
					+ to_date + "' ) " + "OR (ttrn.ttrn_status = 'Active' AND ttrn.ttrn_legal_due_date BETWEEN '"
					+ from_date + "' AND '" + to_date + "') " + ") AND tmap.tmap_enable_status != 0 ";

			System.out.println("This is monthly  report query: " + sql);
			Query query = em.createQuery(sql);
//			query.setParameter("user_id", user_id);
//			query.setParameter("from_date", from_date);
//			query.setParameter("to_date", to_date);

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
