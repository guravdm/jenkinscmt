package lexprd006.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lexprd006.dao.TasksConfigurationDao;
import lexprd006.domain.DefaultTaskConfiguration;
import lexprd006.domain.SubTaskTranscational;
import lexprd006.domain.TaskTransactional;
import lexprd006.domain.TaskUserMapping;
import lexprd006.domain.UploadedDocuments;

/*
 * Author: Mahesh Kharote
 * Date: 11/11/2016
 * Purpose: DAO Impl for Functions
 * 
 * 
 * 
 * */

@Repository(value = "tasksconfigurationdao")
@Transactional
public class TasksConfigurationDaoImpl implements TasksConfigurationDao {

	@PersistenceContext
	private EntityManager em;

	// Method Created By: Mahesh Kharote(10/01/2016)
	// Method Purpose: Save Tasks Configuration
	@Override
	public void persist(Object obj) {
		try {
			em.persist(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Method Created By: Mahesh Kharote(10/01/2016)
	// Method Purpose: Update Tasks Configuration
	@Override
	public void updateTaskConfiguration(TaskTransactional taskTransactional) {
		try {
			em.merge(taskTransactional);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Method Created By: Mahesh Kharote(10/01/2016)
	// Method Purpose: Activate Tasks Configuration
	@Override
	public String activateTasks(ArrayList<Integer> ttrn_ids) {
		try {
			for (int i = 0; i < ttrn_ids.size(); i++) {
				Query query = em.createQuery("UPDATE " + TaskTransactional.class.getName()
						+ " SET ttrn_status = 'Active' WHERE ttrn_id = :ttrn_id");
				query.setParameter("ttrn_id", ttrn_ids.get(i));
				query.executeUpdate();
			}
			return "Success";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Created By: Mahesh Kharote(10/01/2016)
	// Method Purpose: De-Activate Tasks Configuration
	@Override
	public String deactivateTasks(ArrayList<Integer> ttrn_ids) {
		try {
			for (int i = 0; i < ttrn_ids.size(); i++) {
				Query query = em.createQuery("UPDATE " + TaskTransactional.class.getName()
						+ " SET ttrn_status = 'Inactive' WHERE ttrn_id = :ttrn_id");
				query.setParameter("ttrn_id", ttrn_ids.get(i));
				query.executeUpdate();
			}
			return "Success";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Created By: Mahesh Kharote(10/01/2016)
	// Method Purpose: Search Tasks Configuration
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> searchTaskForConfiguration(String json) {
		try {

			JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);

			int country_id = Integer.parseInt(jsonObject.get("country_id").toString());
			int state_id = Integer.parseInt(jsonObject.get("state_id").toString());
			int cat_id = Integer.parseInt(jsonObject.get("cat_id").toString());
			int legi_id = Integer.parseInt(jsonObject.get("legi_id").toString());
			int rule_id = Integer.parseInt(jsonObject.get("rule_id").toString());

			int orga_id = Integer.parseInt(jsonObject.get("orga_id").toString());
			int loca_id = Integer.parseInt(jsonObject.get("loca_id").toString());
			int dept_id = Integer.parseInt(jsonObject.get("dept_id").toString());
			int per_id = Integer.parseInt(jsonObject.get("executor").toString());
			int rev_id = Integer.parseInt(jsonObject.get("evaluator").toString());
			String freq = jsonObject.get("frequency").toString();
			String searching_for = jsonObject.get("searching_for").toString();

			String sql = "";
			if (searching_for.equals("tasksconfiguration")) {
				sql = "SELECT cfg_t_u_m.tmap_client_tasks_id,mst_t.task_legi_name,mst_t.task_rule_name,mst_t.task_activity_who,mst_t.task_activity_when,mst_t.task_activity,mst_t.task_frequency,mst_t.task_impact,mst_t.task_impact_on_unit,mst_t.task_impact_on_organization,mst_t.task_specific_due_date,cfg_t_u_m.tmap_pr_user_id, mst_t.task_reference, mst_t.task_procedure,mst_t.task_lexcare_task_id FROM cfg_task_user_mapping cfg_t_u_m, mst_task mst_t WHERE cfg_t_u_m.tmap_task_id = mst_t.task_id AND cfg_t_u_m.tmap_client_tasks_id NOT IN (SELECT trn_t_t.ttrn_client_task_id FROM trn_task_transactional trn_t_t)";

			}

			if (searching_for.equals("defaultconfiguration")) {
				sql = "SELECT cfg_t_u_m.tmap_client_tasks_id,mst_t.task_legi_name,mst_t.task_rule_name,mst_t.task_activity_who,mst_t.task_activity_when,mst_t.task_activity,mst_t.task_frequency,mst_t.task_impact,mst_t.task_impact_on_unit,mst_t.task_impact_on_organization,mst_t.task_specific_due_date,cfg_t_u_m.tmap_pr_user_id, mst_t.task_reference, mst_t.task_procedure,mst_t.task_lexcare_task_id FROM cfg_task_user_mapping cfg_t_u_m, mst_task mst_t WHERE cfg_t_u_m.tmap_task_id = mst_t.task_id AND cfg_t_u_m.tmap_client_tasks_id NOT IN (SELECT DISTINCT dtco_client_task_id FROM cfg_default_task_configuration)";
			}

			// String sql = "SELECT
			// cfg_t_u_m.tmap_client_tasks_id,mst_t.task_legi_name,mst_t.task_rule_name,mst_t.task_activity_who,mst_t.task_activity_when,mst_t.task_activity,mst_t.task_frequency,mst_t.task_impact,mst_t.task_impact_on_unit,mst_t.task_impact_on_organization,mst_t.task_specific_due_date,cfg_t_u_m.tmap_pr_user_id,
			// mst_t.task_reference, mst_t.task_procedure FROM cfg_task_user_mapping
			// cfg_t_u_m, mst_task mst_t WHERE cfg_t_u_m.tmap_task_id = mst_t.task_id AND
			// cfg_t_u_m.tmap_client_tasks_id NOT IN ("+sql_not_in+")";

			if (country_id != 0) {
				sql += " AND mst_t.task_country_id= :country_id";
			}
			if (state_id != 0) {
				sql += " AND mst_t.task_state_id= :state_id";
			}
			if (cat_id != 0) {
				sql += " AND mst_t.task_cat_law_id= :cat_id";
			}
			if (legi_id != 0) {
				sql += " AND mst_t.task_legi_id= :legi_id";
			}
			if (rule_id != 0) {
				sql += " AND mst_t.task_rule_id= :rule_id";
			}
			if (!freq.equals("NA")) {
				sql += " AND mst_t.task_frequency= :freq";
			}

			if (searching_for.equals("defaultconfiguration")) {
				String event = jsonObject.get("event").toString();
				String sub_event = jsonObject.get("sub_event").toString();
				if (!event.equals("NA")) {
					sql += " AND mst_t.task_event= :event";
				}
				if (!sub_event.equals("NA")) {
					sql += " AND mst_t.task_sub_event= :sub_event";
				}

			}

			if (orga_id != 0) {
				sql += " AND cfg_t_u_m.tmap_orga_id= :orga_id";
			}
			if (loca_id != 0) {
				sql += " AND cfg_t_u_m.tmap_loca_id= :loca_id";
			}
			if (dept_id != 0) {
				sql += " AND cfg_t_u_m.tmap_dept_id= :dept_id";
			}
			if (per_id != 0) {
				sql += " AND cfg_t_u_m.tmap_pr_user_id= :per_id";
			}
			if (rev_id != 0) {
				sql += " AND cfg_t_u_m.tmap_rw_user_id= :rev_id";
			}

			System.out.println("Query " + sql);
			Query query = em.createQuery(sql);

			if (country_id != 0) {
				query.setParameter("country_id", country_id);
			}
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
			if (!freq.equals("NA")) {
				query.setParameter("freq", freq);
			}

			if (searching_for.equals("defaultconfiguration")) {
				String event = jsonObject.get("event").toString();
				String sub_event = jsonObject.get("sub_event").toString();
				if (!event.equals("NA")) {
					query.setParameter("event", freq);
				}
				if (!sub_event.equals("NA")) {
					query.setParameter("sub_event", freq);
				}

			}

			if (orga_id != 0) {
				query.setParameter("orga_id", orga_id);
			}
			if (loca_id != 0) {
				query.setParameter("loca_id", loca_id);
			}
			if (dept_id != 0) {
				query.setParameter("dept_id", dept_id);
			}
			if (per_id != 0) {
				query.setParameter("per_id", per_id);
			}
			if (rev_id != 0) {
				query.setParameter("rev_id", rev_id);
			}

			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Mahesh Kharote(15/02/2016)
	// Method Purpose: Get tasks for activation page
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getAllConfiguredTaskForActivationPage(String jsonString) {
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
			String status = rootNode.path("status").asText();

			String sql = "SELECT DISTINCT b.task_id, b.task_legi_name ,b.task_rule_name,b.task_reference,b.task_activity_who,b.task_activity_when,b.task_activity,b.task_procedure ,a.tmap_client_tasks_id, a.tmap_pr_user_id, c.ttrn_id, c.ttrn_status, c.ttrn_frequency_for_operation, c.ttrn_legal_due_date, c.ttrn_pr_due_date , c.ttrn_created_at, d.user_first_name, d.user_last_name , c.ttrn_status, e.orga_id , e.orga_name , f.loca_id, f.loca_name, g.dept_id, g.dept_name, h.user_first_name, h.user_last_name , d.user_id , h.user_id , c.ttrn_rw_due_date , c.ttrn_fh_due_date , c.ttrn_uh_due_date, c.ttrn_frequency_for_alerts, c.ttrn_impact, c.ttrn_impact_on_organization, c.ttrn_impact_on_unit, c.ttrn_document, c.ttrn_historical, c.ttrn_prior_days_buffer, c.ttrn_alert_days, c.ttrn_first_alert, c.ttrn_second_alert, c.ttrn_third_alert, c.ttrn_no_of_back_days_allowed, c.ttrn_allow_approver_reopening, c.ttrn_allow_back_date_completion, c.ttrn_task_approved_by , c.ttrn_task_approved_date,b.task_lexcare_task_id "
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
			if (exe_id != 0) {
				sql += " AND d.user_id = " + exe_id;
			}
			if (eval_id != 0) {
				sql += " AND h.user_id = " + eval_id;
			}
			if (status.equalsIgnoreCase("null")) {

			} else {
				sql += " AND c.ttrn_status = " + "'" + status + "'";
			}
			if (legi_id != 0) {
				sql += " AND b.task_legi_id = " + legi_id;
			}
			if (rule_id != 0) {
				sql += " AND b.task_rule_id = " + rule_id;
			}
			Query query = em.createQuery(sql);
			System.out.println("sql:" + sql);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Mahesh Kharote(10/01/2016)
	// Method Purpose: Activate Tasks Configuration
	@Override
	public TaskTransactional getTasksForCompletion(int ttrn_id) {
		try {
			System.out.println("ttrn_id : " + ttrn_id);
			TypedQuery<TaskTransactional> query = em.createQuery(
					" from " + TaskTransactional.class.getName() + " where ttrn_id = :ttrn_id",
					TaskTransactional.class);
			query.setParameter("ttrn_id", ttrn_id);
			return (TaskTransactional) query.getResultList().get(0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Mahesh Kharote(10/01/2016)
	// Method Purpose: Activate Tasks Configuration

	// @CacheEvict(value = "saveTaskCompletion", allEntries = true)
	// @CachePut(value = "overallComplianceGraph")

	// @CachePut(value = "overallComplianceGraph", key =
	// "#taskTransactionals.ttrn_id")
	// @CachePut(value = "overallComplianceGraph", key = "{#user_id,
	// #user_role_id}")
//	@CacheEvict(value = "overallComplianceGraph", allEntries = true)
	@Override
	public void saveTaskCompletion(ArrayList<TaskTransactional> taskTransactionals) {
		try {
			Iterator<TaskTransactional> itr = taskTransactionals.iterator();
			while (itr.hasNext()) {
				TaskTransactional taskTransactional = (TaskTransactional) itr.next();
				em.merge(taskTransactional);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Method Created By: Mahesh Kharote(10/01/2016)

	// Method Purpose: Activate Tasks Configuration
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getLatestTtrnForChangeComplianceOwner(int tmap_id) {
		try {

			String sql = "SELECT c.ttrn_id, c.ttrn_legal_due_date FROM cfg_task_user_mapping a, trn_task_transactional c "
					+ "WHERE a.tmap_client_tasks_id = c.ttrn_client_task_id "
					+ "AND c.ttrn_status != 'Completed' AND c.ttrn_status != 'Event_Not_Occured' " + "AND a.tmap_id = '"
					+ tmap_id + "' "
					+ "AND c.ttrn_created_at = ( SELECT MAX(ttrn_created_at) FROM trn_task_transactional WHERE ttrn_client_task_id = a.tmap_client_tasks_id GROUP BY ttrn_client_task_id) ";

			Query query = em.createNativeQuery(sql);
			// Query query = em.createQuery(sql);
			// query.setParameter("tmap_id", tmap_id);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Mahesh Kharote(10/01/2016)
	// Method Purpose: Activate Tasks Configuration
	@Override
	public void saveDefaultTaskConfiguration(DefaultTaskConfiguration configuration) {
		try {
			em.persist(configuration);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Method Created By: Harshad Padole
	// Method Purpose: Get default task detail for configuration
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getTaskDetailsToConfigure(int dtco_id) {
		try {

			String sql = "SELECT dtco.dtco_client_task_id, dtco.dtco_default_frequency, dtco.dtco_event, dtco.dtco_sub_event, dtco.dtco_legal_days, dtco.dtco_uh_days, dtco.dtco_fh_days, dtco.dtco_pr_days, dtco.dtco_rw_days, dtco.dtco_after_before, tsk.task_impact, tsk.task_impact_on_organization, tsk.task_impact_on_unit, tmap.tmap_pr_user_id, dtco.dtco_id "
					+ "FROM cfg_default_task_configuration dtco "
					+ "JOIN cfg_task_user_mapping tmap on dtco.dtco_client_task_id = tmap.tmap_client_tasks_id  "
					+ "JOIN mst_task tsk on tsk.task_lexcare_task_id = tmap.tmap_lexcare_task_id "
					+ "WHERE dtco.dtco_id = :dtco_id ";

			Query query = em.createQuery(sql);
			query.setParameter("dtco_id", dtco_id);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Harshad Padole
	// Method Purpose: Get default task detail for configuration
	@Override
	public DefaultTaskConfiguration getDetails(int dtco_id) {
		try {
			TypedQuery<DefaultTaskConfiguration> query = em.createQuery(
					" from " + DefaultTaskConfiguration.class.getName() + " where dtco_id = :dtco_id",
					DefaultTaskConfiguration.class);
			query.setParameter("dtco_id", dtco_id);
			return (DefaultTaskConfiguration) query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Harshad Padole
	// Method Purpose: Update default task configuration
	@Override
	public void updateDefaultConfiguration(DefaultTaskConfiguration configuration) {
		try {
			em.merge(configuration);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Method Created By: Harshad Padole
	// Method Purpose: Delete task history
	@Override
	public void deleteTaskHistory(int ttrn_id) {
		try {
			TaskTransactional taskTransactional = em.find(TaskTransactional.class, ttrn_id);
			em.remove(taskTransactional);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Method Created By: Harshad Padole
	// Method Purpose: Get task history by client task ID
	@Override
	public List<TaskTransactional> getTaskHistoryByClientTaskId(String clientTaskId) {
		try {
			TypedQuery<TaskTransactional> query = em.createQuery(
					"from " + TaskTransactional.class.getName() + " where ttrn_client_task_id =:client_task_id",
					TaskTransactional.class);
			query.setParameter("client_task_id", clientTaskId);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Harshad Padole
	// Method Purpose: Delete task mapping from cfg_task_user_mapping table
	@Override
	public void deleteTaskMapping(int tmap_id) {
		try {
			TaskUserMapping mapping = em.find(TaskUserMapping.class, tmap_id);
			em.remove(mapping);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getDefaultTask() {
		try {
			String sql = "select dtco.dtco_id,dtco.dtco_client_task_id,dtco.dtco_after_before,dtco.dtco_pr_days,dtco.dtco_rw_days,dtco.dtco_fh_days,dtco.dtco_uh_days,dtco.dtco_legal_days,dtco.dtco_default_frequency,tsk.task_event,tsk.task_sub_event,tmapp.tmap_orga_id,tmapp.tmap_loca_id,tmapp.tmap_dept_id,tmapp.tmap_pr_user_id,tmapp.tmap_rw_user_id,tsk.task_lexcare_task_id from cfg_default_task_configuration dtco "
					+ "JOIN cfg_task_user_mapping tmapp " + "ON tmapp.tmap_client_tasks_id = dtco.dtco_client_task_id "
					+ "JOIN mst_task tsk ON tsk.task_lexcare_task_id = tmapp.tmap_lexcare_task_id";
			Query query = em.createQuery(sql);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Harshad Padole
	// Method Purpose: get Transactional details by sub id
	@SuppressWarnings("rawtypes")
	@Override
	public SubTaskTranscational getSubTaskTransactionalDetailsById(int sub_id) {
		try {
			TypedQuery query = em.createQuery(
					"FROM " + SubTaskTranscational.class.getName() + " WHERE ttrn_sub_id=:sub_id ",
					SubTaskTranscational.class);
			query.setParameter("sub_id", sub_id);
			if (!query.getResultList().isEmpty())
				return (SubTaskTranscational) query.getResultList().get(0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Harshad Padole
	// Method Purpose: Update data
	@Override
	public void merge(Object object) {
		try {
			em.merge(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteTaskDocument(int udoc_id) {
		try {
			UploadedDocuments uploadedDocuments = em.find(UploadedDocuments.class, udoc_id);
			em.remove(uploadedDocuments);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Object> searchTasksForConfigurationPage(String json) {

		try {

			JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);

			int country_id = Integer.parseInt(jsonObject.get("country_id").toString());
			int state_id = Integer.parseInt(jsonObject.get("state_id").toString());
			int cat_id = Integer.parseInt(jsonObject.get("cat_id").toString());
			int legi_id = Integer.parseInt(jsonObject.get("legi_id").toString());
			int rule_id = Integer.parseInt(jsonObject.get("rule_id").toString());

			int orga_id = Integer.parseInt(jsonObject.get("orga_id").toString());
			// int loca_id = Integer.parseInt(jsonObject.get("loca_id").toString());
			int dept_id = Integer.parseInt(jsonObject.get("dept_id").toString());
			int per_id = Integer.parseInt(jsonObject.get("executor").toString());
			int rev_id = Integer.parseInt(jsonObject.get("evaluator").toString());
			String freq = jsonObject.get("frequency").toString();
			String searching_for = jsonObject.get("searching_for").toString();
			Query query = null;
			String temp = null;
			String temp1 = null;
			String loca_id = "";
			JSONArray unit_list = (JSONArray) jsonObject.get("loca_list");
			if (unit_list.size() != 0) {
				for (int i = 0; i < unit_list.size(); i++) {
					JSONObject loca_list = (JSONObject) unit_list.get(i);
					// int loca_id = Integer.parseInt(loca_list.get("loca_id").toString());
					temp = loca_list.get("loca_id").toString();
					temp1 = temp;
					loca_id = loca_id + temp1 + ",";
				}
			}
			String sql = "";
			if (searching_for.equals("tasksconfiguration")) {
				sql = "SELECT cfg_t_u_m.tmap_client_tasks_id,mst_t.task_legi_name,mst_t.task_rule_name,mst_t.task_activity_who,mst_t.task_activity_when,mst_t.task_activity,mst_t.task_frequency,mst_t.task_impact,mst_t.task_impact_on_unit,mst_t.task_impact_on_organization,mst_t.task_specific_due_date,cfg_t_u_m.tmap_pr_user_id, mst_t.task_reference, mst_t.task_procedure,mst_t.task_lexcare_task_id FROM cfg_task_user_mapping cfg_t_u_m, mst_task mst_t WHERE cfg_t_u_m.tmap_task_id = mst_t.task_id AND cfg_t_u_m.tmap_client_tasks_id NOT IN (SELECT trn_t_t.ttrn_client_task_id FROM trn_task_transactional trn_t_t)";

			}

			if (searching_for.equals("defaultconfiguration")) {
				sql = "SELECT cfg_t_u_m.tmap_client_tasks_id,mst_t.task_legi_name,mst_t.task_rule_name,mst_t.task_activity_who,mst_t.task_activity_when,mst_t.task_activity,mst_t.task_frequency,mst_t.task_impact,mst_t.task_impact_on_unit,mst_t.task_impact_on_organization,mst_t.task_specific_due_date,cfg_t_u_m.tmap_pr_user_id, mst_t.task_reference, mst_t.task_procedure,mst_t.task_lexcare_task_id FROM cfg_task_user_mapping cfg_t_u_m, mst_task mst_t WHERE cfg_t_u_m.tmap_task_id = mst_t.task_id AND cfg_t_u_m.tmap_client_tasks_id NOT IN (SELECT DISTINCT dtco_client_task_id FROM cfg_default_task_configuration)";
			}

			if (country_id != 0) {
				sql += " AND mst_t.task_country_id= :country_id";
			}
			if (state_id != 0) {
				sql += " AND mst_t.task_state_id= :state_id";
			}
			if (cat_id != 0) {
				sql += " AND mst_t.task_cat_law_id= :cat_id";
			}
			if (legi_id != 0) {
				sql += " AND mst_t.task_legi_id= :legi_id";
			}
			if (rule_id != 0) {
				sql += " AND mst_t.task_rule_id= :rule_id";
			}
			if (!freq.equals("NA")) {
				sql += " AND mst_t.task_frequency= :freq";
			}

			if (orga_id != 0) {
				sql += " AND cfg_t_u_m.tmap_orga_id= :orga_id";
			}
			if (loca_id != "") {
				loca_id = loca_id.substring(0, loca_id.length() - 1);
				sql += " AND cfg_t_u_m.tmap_loca_id IN (" + loca_id + ")";
			}
			if (dept_id != 0) {
				sql += " AND cfg_t_u_m.tmap_dept_id= :dept_id";
			}
			if (per_id != 0) {
				sql += " AND cfg_t_u_m.tmap_pr_user_id= :per_id";
			}
			if (rev_id != 0) {
				sql += " AND cfg_t_u_m.tmap_rw_user_id= :rev_id";
			}

			System.out.println("Query " + sql);
			query = em.createQuery(sql);

			if (country_id != 0) {
				query.setParameter("country_id", country_id);
			}
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
			if (!freq.equals("NA")) {
				query.setParameter("freq", freq);
			}

			if (orga_id != 0) {
				query.setParameter("orga_id", orga_id);
			}
			/*
			 * if (loca_id != 0) { query.setParameter("loca_id", loca_id); }
			 */
			if (dept_id != 0) {
				query.setParameter("dept_id", dept_id);
			}
			if (per_id != 0) {
				query.setParameter("per_id", per_id);
			}
			if (rev_id != 0) {
				query.setParameter("rev_id", rev_id);
			}

			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public List<Object> searchTasksForDefaultConfigurationPage(String jsonString) {
		try {

			JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);
			int country_id = Integer.parseInt(jsonObject.get("country_id").toString());
			int state_id = Integer.parseInt(jsonObject.get("state_id").toString());
			int cat_id = Integer.parseInt(jsonObject.get("cat_id").toString());
			int legi_id = Integer.parseInt(jsonObject.get("legi_id").toString());
			int rule_id = Integer.parseInt(jsonObject.get("rule_id").toString());

			int orga_id = Integer.parseInt(jsonObject.get("orga_id").toString());
			int loca_id = Integer.parseInt(jsonObject.get("loca_id").toString());
			int dept_id = Integer.parseInt(jsonObject.get("dept_id").toString());
			int per_id = Integer.parseInt(jsonObject.get("executor").toString());
			int rev_id = Integer.parseInt(jsonObject.get("evaluator").toString());
			String freq = jsonObject.get("frequency").toString();
			String searching_for = jsonObject.get("searching_for").toString();
			Query query = null;

			String sql = "";
			if (searching_for.equals("tasksconfiguration")) {
				sql = "SELECT cfg_t_u_m.tmap_client_tasks_id,mst_t.task_legi_name,mst_t.task_rule_name,mst_t.task_activity_who,mst_t.task_activity_when,mst_t.task_activity,mst_t.task_frequency,mst_t.task_impact,mst_t.task_impact_on_unit,mst_t.task_impact_on_organization,mst_t.task_specific_due_date,cfg_t_u_m.tmap_pr_user_id, mst_t.task_reference, mst_t.task_procedure,mst_t.task_lexcare_task_id FROM cfg_task_user_mapping cfg_t_u_m, mst_task mst_t WHERE cfg_t_u_m.tmap_task_id = mst_t.task_id AND cfg_t_u_m.tmap_client_tasks_id NOT IN (SELECT trn_t_t.ttrn_client_task_id FROM trn_task_transactional trn_t_t)";

			}

			if (searching_for.equals("defaultconfiguration")) {
				sql = "SELECT cfg_t_u_m.tmap_client_tasks_id,mst_t.task_legi_name,mst_t.task_rule_name,mst_t.task_activity_who,mst_t.task_activity_when,mst_t.task_activity,mst_t.task_frequency,mst_t.task_impact,mst_t.task_impact_on_unit,mst_t.task_impact_on_organization,mst_t.task_specific_due_date,cfg_t_u_m.tmap_pr_user_id, mst_t.task_reference, mst_t.task_procedure,mst_t.task_lexcare_task_id FROM cfg_task_user_mapping cfg_t_u_m, mst_task mst_t WHERE cfg_t_u_m.tmap_task_id = mst_t.task_id AND cfg_t_u_m.tmap_client_tasks_id NOT IN (SELECT DISTINCT dtco_client_task_id FROM cfg_default_task_configuration)";
			}

			if (country_id != 0) {
				sql += " AND mst_t.task_country_id= :country_id";
			}
			if (state_id != 0) {
				sql += " AND mst_t.task_state_id= :state_id";
			}
			if (cat_id != 0) {
				sql += " AND mst_t.task_cat_law_id= :cat_id";
			}
			if (legi_id != 0) {
				sql += " AND mst_t.task_legi_id= :legi_id";
			}
			if (rule_id != 0) {
				sql += " AND mst_t.task_rule_id= :rule_id";
			}
			if (!freq.equals("NA")) {
				sql += " AND mst_t.task_frequency= :freq";
			}

			if (orga_id != 0) {
				sql += " AND cfg_t_u_m.tmap_orga_id= :orga_id";
			}
			if (loca_id != 0) {
				sql += " AND cfg_t_u_m.tmap_loca_id IN (" + loca_id + ")";
			}
			if (dept_id != 0) {
				sql += " AND cfg_t_u_m.tmap_dept_id= :dept_id";
			}
			if (per_id != 0) {
				sql += " AND cfg_t_u_m.tmap_pr_user_id= :per_id";
			}
			if (rev_id != 0) {
				sql += " AND cfg_t_u_m.tmap_rw_user_id= :rev_id";
			}

			System.out.println("Query " + sql);
			query = em.createQuery(sql);

			if (country_id != 0) {
				query.setParameter("country_id", country_id);
			}
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
			if (!freq.equals("NA")) {
				query.setParameter("freq", freq);
			}

			if (orga_id != 0) {
				query.setParameter("orga_id", orga_id);
			}
			/*
			 * if (loca_id != 0) { query.setParameter("loca_id", loca_id); }
			 */
			if (dept_id != 0) {
				query.setParameter("dept_id", dept_id);
			}
			if (per_id != 0) {
				query.setParameter("per_id", per_id);
			}
			if (rev_id != 0) {
				query.setParameter("rev_id", rev_id);
			}

			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public String getDocumentDownloadStatus(String ttrn_id) {
		// TODO Auto-generated method stub
		String status = "Failed";
		String sql = "SELECT COUNT(udoc_ttrn_id), Sum(download_status) from trn_uploadeddocuments where udoc_ttrn_id ="
				+ ttrn_id + "";
		Query query = em.createNativeQuery(sql);
		List resultList = query.getResultList();

		Iterator iterator = resultList.iterator();
		while (iterator.hasNext()) {
			Object object[] = (Object[]) iterator.next();
			System.out.println("Sum of ttrn ID : " + object[0] + " Sum of Document : " + object[1]);

			if (Integer.parseInt(object[0].toString()) == 0
					|| object[0] == null && Integer.parseInt(object[1].toString()) == 0) {
				status = "Approved";
			} else if (Integer.parseInt(object[0].toString()) > 0 && Integer.parseInt(object[1].toString()) == 0) {
				status = "Failed";
			} else if (Integer.parseInt(object[0].toString()) == Integer.parseInt(object[1].toString())) {
				status = "Approved";
			}
		}

		return status;
	}

	// Method Purpose: get data date
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getTasksForCompletionWithNativeQuery(int ttrn_id) {
		try {
			String sql = "select ttrn_id, ttrn_activation_date, ttrn_added_by, ttrn_alert_days, ttrn_allow_approver_reopening, "
					+ "ttrn_allow_back_date_completion, ttrn_client_task_id, ttrn_completed_date, ttrn_created_at, ttrn_document, ttrn_fh_due_date, "
					+ "ttrn_first_alert, ttrn_frequency_for_alerts, ttrn_frequency_for_operation, ttrn_historical, ttrn_impact, "
					+ "ttrn_impact_on_organization, ttrn_impact_on_unit, ttrn_legal_due_date, ttrn_no_of_back_days_allowed, ttrn_performer_comments, "
					+ "ttrn_performer_user_id, ttrn_pr_due_date, ttrn_prior_days_buffer, ttrn_reason_for_non_compliance, ttrn_rw_due_date, "
					+ "ttrn_second_alert, ttrn_status, ttrn_submitted_date, ttrn_task_approved_by, ttrn_task_approved_date, ttrn_task_completed_by, "
					+ "ttrn_third_alert, ttrn_uh_due_date from trn_task_transactional where ttrn_id = '" + ttrn_id
					+ "' ";

			Query query = em.createQuery(sql);
			System.out.println(" SQL : " + sql);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Purpose: update data date
	@SuppressWarnings("unchecked")
	@Override
	public void updateTaskConfigurationWithNativeQuery(TaskTransactional taskTransactional, int ttrn_id) {

		SimpleDateFormat ssd = new SimpleDateFormat("yyyy-MM-dd");
		Date prDueDate = taskTransactional.getTtrn_pr_due_date();
		String ttrn_pr_due_date = ssd.format(prDueDate);

		Date rdDate = taskTransactional.getTtrn_rw_due_date();
		String ttrn_rw_due_date = ssd.format(rdDate);

		Date lglDate = taskTransactional.getTtrn_legal_due_date();
		String ttrn_legal_due_date = ssd.format(lglDate);

		Date uhDate = taskTransactional.getTtrn_uh_due_date();
		String ttrn_uh_due_date = ssd.format(uhDate);

		int ttrn_alert_days = taskTransactional.getTtrn_alert_days();
		String ttrn_allow_approver_reopening = taskTransactional.getTtrn_allow_approver_reopening();
		String ttrn_document = taskTransactional.getTtrn_document();

		Date fhDate = taskTransactional.getTtrn_fh_due_date();
		String ttrn_fh_due_date = ssd.format(fhDate);

		String ttrn_first_alert = null;
		if (taskTransactional.getTtrn_first_alert() != null) {
			Date firstAlertDate = taskTransactional.getTtrn_first_alert();
			ttrn_first_alert = ssd.format(firstAlertDate);
		}

		String ttrn_second_alert = null;
		if (taskTransactional.getTtrn_second_alert() != null) {
			Date secAlert = taskTransactional.getTtrn_second_alert();
			ttrn_second_alert = ssd.format(secAlert);
		}

		String ttrn_third_alert = null;
		if (taskTransactional.getTtrn_third_alert() != null) {
			Date thrdAlert = taskTransactional.getTtrn_third_alert();
			ttrn_third_alert = ssd.format(thrdAlert);
		}

		String ttrn_frequency_for_alerts = taskTransactional.getTtrn_frequency_for_alerts();

		String ttrn_frequency_for_operation = taskTransactional.getTtrn_frequency_for_operation();

		String ttrn_historical = taskTransactional.getTtrn_historical();

		String ttrn_impact = taskTransactional.getTtrn_impact();

		String ttrn_impact_on_organization = taskTransactional.getTtrn_impact_on_organization();

		String ttrn_impact_on_unit = taskTransactional.getTtrn_impact_on_unit();

		int ttrn_no_of_back_days_allowed = taskTransactional.getTtrn_no_of_back_days_allowed();

		int ttrn_prior_days_buffer = taskTransactional.getTtrn_prior_days_buffer();

		String ttrn_status = taskTransactional.getTtrn_status();
		System.out.println("ttrn_status:" + ttrn_status);

		String sql = "update trn_task_transactional set ttrn_alert_days = '" + ttrn_alert_days
				+ "', ttrn_allow_approver_reopening = '" + ttrn_allow_approver_reopening + "', ttrn_document = '"
				+ ttrn_document + "', " + "ttrn_fh_due_date = '" + ttrn_fh_due_date + "', ttrn_frequency_for_alerts = '"
				+ ttrn_frequency_for_alerts + "', ttrn_frequency_for_operation = '" + ttrn_frequency_for_operation
				+ "', " + "ttrn_historical = '" + ttrn_historical + "', ttrn_impact = '" + ttrn_impact
				+ "', ttrn_impact_on_organization = '" + ttrn_impact_on_organization + "', ttrn_impact_on_unit = '"
				+ ttrn_impact_on_unit + "', " + "ttrn_legal_due_date = '" + ttrn_legal_due_date
				+ "', ttrn_no_of_back_days_allowed = '" + ttrn_no_of_back_days_allowed + "', ttrn_pr_due_date = '"
				+ ttrn_pr_due_date + "', ttrn_prior_days_buffer = '" + ttrn_prior_days_buffer + "', "
				+ "ttrn_rw_due_date = '" + ttrn_rw_due_date + "', ttrn_uh_due_date = '" + ttrn_uh_due_date + "' ";

		if (ttrn_first_alert != null) {
			sql += " ,ttrn_first_alert = '" + ttrn_first_alert + "' ";
		}

		if (ttrn_second_alert != null) {
			sql += " ,ttrn_second_alert = '" + ttrn_second_alert + "' ";
		}

		if (ttrn_third_alert != null) {
			sql += " ,ttrn_third_alert = '" + ttrn_third_alert + "' ";
		}

		if (ttrn_status != null) {
			sql += " ,ttrn_status = '" + ttrn_status + "' ";
			System.out.println("sql:" + sql);
		}

		sql += " WHERE  ttrn_id = '" + ttrn_id + "'";
		Query createNativeQuery = em.createNativeQuery(sql);
		System.out.println("update config : " + sql);
		int executeUpdate = createNativeQuery.executeUpdate();
		System.out.println("executeUpdate : " + executeUpdate);
	}

	// Method Purpose: Get data
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getTasksForCompletionNativeQuery(Integer ttrnId) {
		String sql = "select ttrn_status from trn_task_transactional where ttrn_id = '" + ttrnId + "' ";
		Query createNativeQuery = em.createNativeQuery(sql);
		System.out.println("getTasksForCompletionNativeQuery SQL : " + sql);
		List resultList = createNativeQuery.getResultList();
		return createNativeQuery.getResultList();
	}

	// Method Purpose: get data date
	@SuppressWarnings("unchecked")
	@Override
	public void updateTaskConfigurationNativeQuery(TaskTransactional taskTransactional1, Integer ttrnId) {
		String sql = "update trn_task_transactional set ttrn_performer_user_id = '"
				+ taskTransactional1.getTtrn_performer_user_id() + "' where ttrn_id = '" + ttrnId + "' ";
		Query createNativeQuery = em.createNativeQuery(sql);
		int executeUpdate = createNativeQuery.executeUpdate();
		System.out.println("executeUpdate status : " + executeUpdate);
	}

	@Override
	public <T> List<T> saveStatusByCompletion(int ttrn_id) {
		try {
			String sql = "SELECT distinct "
					+ "SUM(IF(DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND "
					+ "DATE_FORMAT(tttrn.ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND tttrn.ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', "
					+ "SUM(IF(DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(tttrn.ttrn_submitted_date, '%Y/%m/%d') AND tttrn.ttrn_status = 'Completed', 1, 0)) AS 'Complied', "
					+ "SUM(IF(DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND tttrn.ttrn_status = 'Active', 1, 0)) AS 'NonComplied', "
					+ "SUM(IF(DATE_FORMAT(tttrn.ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y/%m/%d') AND DATE_FORMAT(tttrn.ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y/%m/%d') AND tttrn.ttrn_status = 'Completed', 1, 0)) AS 'Delayed', "
					+ "SUM(IF(DATE_FORMAT(tttrn.ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(tttrn.ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(tttrn.ttrn_legal_due_date, '%Y/%m/%d')) AND tttrn.ttrn_status = 'Completed', 1, 0))  AS 'DelayedReported', "
					+ "SUM(IF(tttrn.ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', SUM(IF(tttrn.ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval' "
					+ "from trn_task_transactional tttrn where tttrn.ttrn_id = '" + ttrn_id + "' ";
			Query createNativeQuery = em.createNativeQuery(sql);
			List resultList = createNativeQuery.getResultList();
			return resultList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
