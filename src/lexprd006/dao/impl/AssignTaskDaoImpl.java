package lexprd006.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lexprd006.dao.AssignTaskDao;
import lexprd006.domain.Task;
import lexprd006.domain.TaskUserMapping;

@Repository(value = "assignTaskDao")
@Transactional
public class AssignTaskDaoImpl implements AssignTaskDao {

	@PersistenceContext
	private EntityManager em;

	public List<Object> getEntityList(HttpSession session) {

		int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
		try {
			if ((int) session.getAttribute("sess_role_id") == 7) {

				String sql = "SELECT orga.orga_name, orga.orga_id " + "FROM cfg_entity_mapping cemap "
						+ "LEFT JOIN mst_organization orga ON cemap.enti_orga_id = orga.orga_id "
						+ "GROUP BY orga.orga_name, orga.orga_id";

				Query query = em.createNativeQuery(sql);
				return query.getResultList();
			} else {
				String sql = "SELECT orga.orga_name, orga.orga_id, COUNT(umap.umap_orga_id) AS NumberOfOrders FROM cfg_user_entity_mapping umap "
						+ "LEFT JOIN mst_organization orga ON umap.umap_orga_id = orga.orga_id "
						+ "LEFT JOIN mst_user usr ON umap.umap_user_id = usr.user_id " + "WHERE usr.user_id = "
						+ user_id + " GROUP BY orga.orga_name,orga.orga_id";

				Query query = em.createNativeQuery(sql);
				return query.getResultList();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return null;

	}

	public List<Object> getUnitList(String entity_id, HttpSession session) {
		// int user_id =
		// Integer.parseInt(session.getAttribute("sess_user_id").toString());
		try {

			// Super admin can see all the units under Entity's
			if ((int) session.getAttribute("sess_role_id") == 7) {

				String sql = "SELECT DISTINCT orga.orga_name, orga.orga_id, loca.loca_id, loca.loca_name "
						+ "FROM cfg_entity_mapping cemap "
						+ "LEFT JOIN mst_organization orga ON cemap.enti_orga_id = orga.orga_id "
						+ "LEFT JOIN mst_location loca ON cemap.enti_loca_id = loca.loca_id " + "WHERE orga.orga_id = "
						+ entity_id + " " + " and loca.loca_id IS NOT NULL and loca.loca_name IS NOT NULL";

				System.out.println("Unit for SuperAdmin : ==> " + sql);
				Query query = em.createNativeQuery(sql);
				return query.getResultList();
			} else {

				// Admin will see the Units as per he's and Her sccess
				int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());

				String sql = "SELECT DISTINCT orga.orga_name, orga.orga_id,loca.loca_id, loca.loca_name FROM cfg_user_entity_mapping umap "
						+ "LEFT JOIN mst_organization orga ON umap.umap_orga_id = orga.orga_id "
						+ "LEFT JOIN mst_location loca ON umap.umap_loca_id = loca.loca_id "
						+ "LEFT JOIN mst_user usr ON umap.umap_user_id = usr.user_id " + "WHERE usr.user_id =" + user_id
						+ " and orga.orga_id = " + entity_id
						+ " and loca.loca_id IS NOT NULL and loca.loca_name IS NOT NULL";
				Query query = em.createNativeQuery(sql);
				return query.getResultList();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return null;
	}

	public List<Object> getFunctionList(int unit_id, int entity_id, HttpSession session) {
		int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
		try {
			if ((int) session.getAttribute("sess_role_id") == 7) {

				String sql = "SELECT DISTINCT orga.orga_name,orga.orga_id,loca.loca_id,loca.loca_name,dept.dept_id,dept.dept_name "
						+ "FROM cfg_entity_mapping cemap "
						+ "LEFT JOIN mst_organization orga ON cemap.enti_orga_id = orga.orga_id "
						+ "LEFT JOIN mst_location loca ON cemap.enti_loca_id = loca.loca_id "
						+ "LEFT JOIN mst_department dept ON cemap.enti_dept_id = dept.dept_id " + "WHERE loca.loca_id ="
						+ unit_id + " AND orga.orga_id =" + entity_id;

				Query query = em.createNativeQuery(sql);

				System.out.println("Function For Super Admin:" + sql);

				return query.getResultList();
			} else {
				String sql = "SELECT DISTINCT orga.orga_name, orga.orga_id,loca.loca_id, loca.loca_name,dept.dept_id,dept.dept_name FROM cfg_user_entity_mapping umap "
						+ "LEFT JOIN mst_organization orga ON umap.umap_orga_id = orga.orga_id "
						+ "LEFT JOIN mst_location loca ON umap.umap_loca_id = loca.loca_id "
						+ "LEFT JOIN mst_department dept ON umap.umap_dept_id = dept.dept_id "
						+ "WHERE umap.umap_user_id = " + user_id + " and loca.loca_id = " + unit_id
						+ " and orga.orga_id = " + entity_id;
				Query query = em.createNativeQuery(sql);
				System.out.println("sql:" + sql);
				return query.getResultList();
			}

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
	public List<Object> getExecutorList(int orga_id, int loca_id, int dept_id) {
		try {
			String sql = "SELECT Distinct usr.user_id,usr.user_first_name,usr.user_last_name,usr.user_role_id, orga.orga_name, orga.orga_id,"
					+ "loca.loca_id, loca.loca_name,dept.dept_id,dept.dept_name FROM cfg_user_entity_mapping umap LEFT JOIN mst_organization orga "
					+ "ON umap.umap_orga_id = orga.orga_id LEFT JOIN mst_location loca ON umap.umap_loca_id = loca.loca_id LEFT JOIN "
					+ "mst_department dept ON umap.umap_dept_id = dept.dept_id LEFT JOIN mst_user usr ON usr.user_id = umap.umap_user_id"
					+ " WHERE orga.orga_id = " + orga_id + " and usr.user_role_id >= 1 ";

			if (loca_id != 0) {
				sql += " AND loca.loca_id = " + loca_id;
			}

			if (dept_id != 0) {
				sql += " AND dept.dept_id = " + dept_id;
			}

			Query query = em.createNativeQuery(sql);
			System.out.println("getExecutorList sql: " + sql);
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

	public List<Object> getEvaluatorList(int orga_id, int loca_id, int dept_id) {
		try {
			String sql = "SELECT Distinct usr.user_id,usr.user_first_name,usr.user_last_name,usr.user_role_id, orga.orga_name, orga.orga_id,"
					+ "loca.loca_id, loca.loca_name,dept.dept_id,dept.dept_name FROM cfg_user_entity_mapping umap LEFT JOIN mst_organization orga "
					+ "ON umap.umap_orga_id = orga.orga_id LEFT JOIN mst_location loca ON umap.umap_loca_id = loca.loca_id LEFT JOIN "
					+ "mst_department dept ON umap.umap_dept_id = dept.dept_id LEFT JOIN mst_user usr ON usr.user_id = umap.umap_user_id"
					+ " WHERE loca.loca_id = " + loca_id + " and orga.orga_id = " + orga_id + " and dept.dept_id = "
					+ dept_id + " and usr.user_role_id >= 2 ";
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

	public List<Object> getFunHeadList(int orga_id, int loca_id, int dept_id) {
		try {
			String sql = "SELECT Distinct usr.user_id,usr.user_first_name,usr.user_last_name,usr.user_role_id, orga.orga_name, orga.orga_id,"
					+ "loca.loca_id, loca.loca_name,dept.dept_id,dept.dept_name FROM cfg_user_entity_mapping umap LEFT JOIN mst_organization orga "
					+ "ON umap.umap_orga_id = orga.orga_id LEFT JOIN mst_location loca ON umap.umap_loca_id = loca.loca_id LEFT JOIN "
					+ "mst_department dept ON umap.umap_dept_id = dept.dept_id LEFT JOIN mst_user usr ON usr.user_id = umap.umap_user_id"
					+ " WHERE loca.loca_id = " + loca_id + " and orga.orga_id = " + orga_id + " and dept.dept_id = "
					+ dept_id + " and usr.user_role_id >= 3 ";
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

	@Override
	public List<Object> searchTaskFromMstTaskForAssign(String jsonString) {

		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);
			int country_id = Integer.parseInt(jsonObject.get("country_id").toString());
			int state_id = Integer.parseInt(jsonObject.get("state_id").toString());
			int cat_id = Integer.parseInt(jsonObject.get("cat_id").toString());
			int legi_id = Integer.parseInt(jsonObject.get("legi_id").toString());
			int rule_id = Integer.parseInt(jsonObject.get("rule_id").toString());
			int orga_id = Integer.parseInt(jsonObject.get("orga_id").toString());
			int loca_id = Integer.parseInt(jsonObject.get("loca_id").toString());
			String sql = "Select task.task_id,task.task_lexcare_task_id,task.task_legi_name, task.task_activity_who,task.task_activity_when, "
					+ "task.task_activity,task.task_procedure,task.task_impact,task.task_frequency, task.task_reference, task.task_rule_name "
					+ "FROM mst_task task WHERE task.task_country_id = :country_id "
					+ "AND task.task_id NOT IN (Select tmapp.tmap_task_id FROM cfg_task_user_mapping tmapp WHERE tmapp.tmap_orga_id = :orga_id AND tmapp.tmap_loca_id = :loca_id)";

			// Checking if following are present while searching
			if (state_id != 0) {
				sql += " AND task.task_state_id = :state_id";
			}
			if (cat_id != 0) {
				sql += " AND task.task_cat_law_id = :cat_id";
			}
			if (legi_id != 0) {
				sql += " AND task.task_legi_id = :legi_id";
			}
			if (rule_id != 0) {
				sql += " AND task.task_rule_id= :rule_id";
			}
			System.out.println("searchTaskFromMstTaskForAssign sql :" + sql);
			Query query = em.createNativeQuery(sql);
			query.setParameter("country_id", country_id);
			query.setParameter("loca_id", loca_id);
			query.setParameter("orga_id", orga_id);

			// Setting Parameters if following are present
			if (state_id != 0) {
				query.setParameter("state_id", state_id);
			}
			if (cat_id != 0) {
				query.setParameter("cat_id", cat_id);
			}
			if (legi_id != 0) {
				query.setParameter("legi_id", legi_id);
			}
			if (rule_id != 0) {
				query.setParameter("rule_id", rule_id);
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
	public List<Object> checkIfExist(int orgaId, int unitId, int deptId, String lexcare_task_id, int pr_user_id,
			int rw_user_id, int fh_user_id, int task_id) {
		try {
			String sql = null;
			sql = "from " + TaskUserMapping.class.getName() + " where tmap_lexcare_task_id = " + "'" + lexcare_task_id
					+ "'" + " and tmap_orga_id = " + orgaId + " and tmap_loca_id = " + unitId + " and tmap_dept_id = "
					+ deptId + " and tmap_pr_user_id = " + pr_user_id + " and tmap_rw_user_id = " + rw_user_id
					+ " and tmap_fh_user_id = " + fh_user_id + " and tmap_task_id = " + task_id;
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

	@Override
	public Task getTaskIdByLexcareTaskId(String lexcare_task_id) {
		System.out.println("lexcare_task_id: " + lexcare_task_id);
		try {
			String sql = "from " + Task.class.getName() + " where task_lexcare_task_id = " + "'" + lexcare_task_id
					+ "'";
			Query query = em.createQuery(sql);
			return (Task) query.getResultList().get(0);

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
	public List<Object> getExeListForActivationPage(int orga_id, int loca_id, int dept_id) {
		try {
			String sql = "SELECT DISTINCT b.task_id, b.task_legi_name ,b.task_rule_name,b.task_reference,b.task_activity_who,b.task_activity_when,"
					+ "b.task_activity,b.task_procedure ,a.tmap_client_tasks_id, a.tmap_pr_user_id, c.ttrn_id, c.ttrn_status, "
					+ "c.ttrn_frequency_for_operation, c.ttrn_legal_due_date, c.ttrn_pr_due_date , c.ttrn_created_at, d.user_first_name, "
					+ "d.user_last_name , c.ttrn_status, e.orga_id , e.orga_name , f.loca_id, f.loca_name, g.dept_id, g.dept_name, h.user_first_name,"
					+ " h.user_last_name , d.user_id , h.user_id , c.ttrn_rw_due_date , c.ttrn_fh_due_date , c.ttrn_uh_due_date, c.ttrn_frequency_for_alerts, "
					+ "c.ttrn_impact, c.ttrn_impact_on_organization, c.ttrn_impact_on_unit, c.ttrn_document, c.ttrn_historical, c.ttrn_prior_days_buffer, "
					+ "c.ttrn_alert_days, c.ttrn_first_alert, c.ttrn_second_alert, c.ttrn_third_alert, c.ttrn_no_of_back_days_allowed, c.ttrn_allow_approver_reopening,"
					+ " c.ttrn_allow_back_date_completion, c.ttrn_task_approved_by , c.ttrn_task_approved_date,b.task_lexcare_task_id "
					+ "FROM cfg_task_user_mapping a, mst_task b, trn_task_transactional c , mst_user d , mst_organization e, mst_location f, mst_department g, mst_user h "
					+ "WHERE a.tmap_task_id = b.task_id " + "AND a.tmap_orga_id = e.orga_id "
					+ "AND a.tmap_loca_id = f.loca_id " + "AND a.tmap_dept_id = g.dept_id "
					+ "AND a.tmap_pr_user_id = d.user_id " + "AND a.tmap_rw_user_id = h.user_id "
					+ "AND a.tmap_client_tasks_id = c.ttrn_client_task_id  "
					+ "AND c.ttrn_status != 'Completed' AND c.ttrn_status != 'Event_Not_Occured' ";

			if (orga_id != 0) {
				sql += " AND e.orga_id = " + orga_id;
			}
			if (loca_id != 0) {
				sql += " AND f.loca_id = " + loca_id;
			}
			if (dept_id != 0) {
				sql += " AND g.dept_id = " + dept_id;
			}
			Query query = em.createQuery(sql);
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

	@Override
	public List<Object> getEvalListForActivationPage(int orga_id, int loca_id, int dept_id) {
		try {
			String sql = "SELECT DISTINCT b.task_id, b.task_legi_name ,b.task_rule_name,b.task_reference,b.task_activity_who,b.task_activity_when,"
					+ "b.task_activity,b.task_procedure ,a.tmap_client_tasks_id, a.tmap_pr_user_id, c.ttrn_id, c.ttrn_status, "
					+ "c.ttrn_frequency_for_operation, c.ttrn_legal_due_date, c.ttrn_pr_due_date , c.ttrn_created_at, d.user_first_name, "
					+ "d.user_last_name , c.ttrn_status, e.orga_id , e.orga_name , f.loca_id, f.loca_name, g.dept_id, g.dept_name, h.user_first_name,"
					+ " h.user_last_name , d.user_id , h.user_id , c.ttrn_rw_due_date , c.ttrn_fh_due_date , c.ttrn_uh_due_date, c.ttrn_frequency_for_alerts, "
					+ "c.ttrn_impact, c.ttrn_impact_on_organization, c.ttrn_impact_on_unit, c.ttrn_document, c.ttrn_historical, c.ttrn_prior_days_buffer, "
					+ "c.ttrn_alert_days, c.ttrn_first_alert, c.ttrn_second_alert, c.ttrn_third_alert, c.ttrn_no_of_back_days_allowed, c.ttrn_allow_approver_reopening,"
					+ " c.ttrn_allow_back_date_completion, c.ttrn_task_approved_by , c.ttrn_task_approved_date,b.task_lexcare_task_id "
					+ "FROM cfg_task_user_mapping a, mst_task b, trn_task_transactional c , mst_user d , mst_organization e, mst_location f, mst_department g, mst_user h "
					+ "WHERE a.tmap_task_id = b.task_id " + "AND a.tmap_orga_id = e.orga_id "
					+ "AND a.tmap_loca_id = f.loca_id " + "AND a.tmap_dept_id = g.dept_id "
					+ "AND a.tmap_pr_user_id = d.user_id " + "AND a.tmap_rw_user_id = h.user_id "
					+ "AND a.tmap_client_tasks_id = c.ttrn_client_task_id  "
					+ "AND c.ttrn_status != 'Completed' AND c.ttrn_status != 'Event_Not_Occured' ";

			if (orga_id != 0) {
				sql += " AND e.orga_id = " + orga_id;
			}
			if (loca_id != 0) {
				sql += " AND f.loca_id = " + loca_id;
			}
			if (dept_id != 0) {
				sql += " AND g.dept_id = " + dept_id;
			}
			Query query = em.createQuery(sql);
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> searchEnableDisablePage(String jsonString) {
		JsonNode rootNode = null;
		final ObjectMapper mapper = new ObjectMapper();
		try {
			rootNode = mapper.readTree(jsonString);
			Integer orga_id = rootNode.path("orga_id").asInt();
			Integer loca_id = rootNode.path("loca_id").asInt();
			Integer dept_id = rootNode.path("dept_id").asInt();
			Integer exe_id = rootNode.path("pr_user_id").asInt();
			Integer eval_id = rootNode.path("rw_user_id").asInt();
			Integer legi_id = rootNode.path("legi_id").asInt();
			Integer rule_id = rootNode.path("rule_id").asInt();

			String sql = "SELECT tsk.task_id, tsk.task_legi_name ,tsk.task_rule_name,tsk.task_reference,tsk.task_activity_who, "
					+ "tsk.task_activity_when,tsk.task_activity,tsk.task_procedure ,tmapp.tmap_id,tmapp.tmap_enable_status, "
					+ "tmapp.tmap_client_tasks_id, "
					+ "tmapp.tmap_pr_user_id as prUserId, usr.user_first_name as usFNames, usr.user_last_name as usLNamess, "
					+ "tmapp.tmap_rw_user_id as rwuserId, usrw.user_first_name as rwFNames, usrw.user_last_name as rwLASTNames, "
					+ "orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, "
					+ "tsk.task_lexcare_task_id,tsk.task_legi_id,tsk.task_rule_id, "
					+ "tmapp.tmap_fh_user_id as fhUsrId, usrfh.user_first_name as fhFNames, usrfh.user_last_name as fhULNames "
					+ "FROM cfg_task_user_mapping tmapp " + "JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
					+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
					+ "JOIN mst_user usrw on usrw.user_id = tmapp.tmap_rw_user_id "
					+ "JOIN mst_user usrfh on usrfh.user_id = tmapp.tmap_fh_user_id "
					+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
					+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
					+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id ";

			if (orga_id != 0) {
				sql += " Where orga.orga_id = " + orga_id;
			}
			if (loca_id != 0) {
				sql += " AND loca.loca_id = " + loca_id;
			}
			if (dept_id != 0) {
				sql += " AND dept.dept_id = " + dept_id;
			}
			if (exe_id != 0) {
				sql += " AND usr.user_id = " + exe_id;
			}
			if (eval_id != 0) {
				sql += " AND usrw.user_id = " + eval_id;
			}

			if (legi_id != 0) {
				sql += " AND tsk.task_legi_id = " + legi_id;
			}
			if (rule_id != 0) {
				sql += " AND tsk.task_rule_id = " + rule_id;
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
