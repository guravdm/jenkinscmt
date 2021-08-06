package lexprd006.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lexprd006.dao.TasksUserMappingDao;
import lexprd006.domain.TaskUserMapping;

/*
 * Author: Mahesh Kharote
 * Date: 01/01/2017
 * Updated By:
 * Updated Date: 
 * 
 * */

@Repository(value = "tasksUserMappingDao")
@Transactional
public class TasksUserMappingDaoImpl implements TasksUserMappingDao {

	@PersistenceContext
	private EntityManager em;

	// Method Created By: Mahesh Kharote(10/01/2017)
	// Method Purpose: Save Tasks User Mapping
	@Override
	public void persist(Object obj) {
		try {
			em.persist(obj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

	}

	// Method Created By: Mahesh Kharote(10/01/2017)
	// Method Purpose: Get Max last generated value to client task id Tasks User
	// Mapping
	@Override
	public int getMaxLastGeneratedValue(int loca_id, int dept_id) {
		try {
			Query query = em.createQuery(
					"Select MAX(tmap_last_generated_value_for_client_task_id) FROM " + TaskUserMapping.class.getName());
			// query.setParameter("loca_id", loca_id);
			// query.setParameter("dept_id", dept_id);
			return Integer.parseInt(query.getResultList().get(0).toString());
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	// Method Created By: Mahesh Kharote(13/02/2017)
	// Method Purpose: Save Tasks User Mapping

	// @Cacheable(value = "searchTasksFromMstTasks")
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> searchTaskFromMstTaskForAssign(String jsonString) {
		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);
			int country_id = Integer.parseInt(jsonObject.get("country_id").toString());
			int state_id = Integer.parseInt(jsonObject.get("state_id").toString());
			int cat_id = Integer.parseInt(jsonObject.get("cat_id").toString());
			int legi_id = Integer.parseInt(jsonObject.get("legi_id").toString());
			int rule_id = Integer.parseInt(jsonObject.get("rule_id").toString());
			int orga_id = Integer.parseInt(jsonObject.get("orga_id").toString());
			int loca_id = Integer.parseInt(jsonObject.get("loca_id").toString());
			String sql = "Select task.task_id,task.task_lexcare_task_id,task.task_legi_name,task.task_activity_who,task.task_activity_when,task.task_activity,task.task_procedure,task.task_impact,task.task_frequency, task.task_reference, task.task_rule_name "
					+ "FROM mst_task task " + "WHERE task.task_country_id = :country_id "
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
			System.out.println("sql :" + sql);
			Query query = em.createQuery(sql);
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

	// Method Created By: Mahesh Kharote(13/02/2017)
	// Method Purpose: Save Tasks User Mapping
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getDistinctCountries() {
		try {

			String sql = "Select DISTINCT task_country_id, task_country_name from mst_task";
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

	// Method Created By: Mahesh Kharote(13/02/2017)
	// Method Purpose: Get States for Country
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getAllStateForCountry(int task_country_id) {
		try {
			String sql = "SELECT DISTINCT stat.task_state_id,stat.task_state_name " + "FROM mst_task stat "
					+ "WHERE stat.task_state_id !=2 " + "AND stat.task_country_id = :task_country_id";

			Query query = em.createQuery(sql);
			query.setParameter("task_country_id", task_country_id);
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

	// Method Created By: Mahesh Kharote(13/02/2017)
	// Method Purpose: Save Tasks User Mapping
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getAllCat_lawFromMst_task(String json) {
		try {

			JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);
			int country_id = Integer.parseInt(jsonObject.get("country_id").toString());
			int state_id = Integer.parseInt(jsonObject.get("state_id").toString());
			String cond = jsonObject.get("searching_for").toString();
			String sql = "";
			if (cond.equals("tasksmapping")) {
				sql = "Select DISTINCT mst_t.task_cat_law_id,mst_t.task_cat_law_name FROM mst_task mst_t ";

				if (country_id != 0) {
					sql += " WHERE mst_t.task_country_id = :country_id";
				}

				if (state_id != 0 && country_id != 0) {
					sql += " AND mst_t.task_state_id = :state_id";
				} else if (state_id != 0) {

					sql += " WHERE mst_t.task_state_id = :state_id";
				}

			} else {
				if (cond.equals("tasksconfiguration")) {
					sql = "Select DISTINCT mst_t.task_cat_law_id,mst_t.task_cat_law_name FROM cfg_task_user_mapping cfg_t_u_m, mst_task mst_t WHERE cfg_t_u_m.tmap_task_id = mst_t.task_id";
					if (country_id != 0) {
						sql += " AND mst_t.task_country_id = :country_id";
					}
					if (state_id != 0) {
						sql += " AND mst_t.task_state_id = :state_id";
					}
				}

			}

			Query query = em.createQuery(sql);
			if (country_id != 0) {
				query.setParameter("country_id", country_id);
			}
			if (state_id != 0) {
				query.setParameter("state_id", state_id);
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

	// Method Created By: Mahesh Kharote(13/02/2017)
	// Method Purpose: Save Tasks User Mapping
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getlegislationFromMst_task(String json) {
		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);
			int country_id = Integer.parseInt(jsonObject.get("country_id").toString());
			int state_id = Integer.parseInt(jsonObject.get("state_id").toString());
			int cat_id = Integer.parseInt(jsonObject.get("cat_id").toString());
			String Cond = jsonObject.get("searching_for").toString();

			String sql = "";

			if (Cond.equals("tasksmapping")) {
				sql = "SELECT DISTINCT mst_t.task_legi_id,mst_t.task_legi_name FROM mst_task mst_t  ";
				if (country_id != 0) {
					sql += " WHERE mst_t.task_country_id = :country_id";
				}

				if (state_id != 0 && country_id != 0) {
					sql += " AND mst_t.task_state_id = :state_id";
				} else if (state_id != 0) {

					sql += " WHERE mst_t.task_state_id = state_id";
				}
			} else {
				if (Cond.equals("tasksconfiguration")) {
					sql = "SELECT DISTINCT mst_t.task_legi_id,mst_t.task_legi_name FROM cfg_task_user_mapping cfg_t_u_m, mst_task mst_t WHERE cfg_t_u_m.tmap_task_id = mst_t.task_id ";

					if (country_id != 0) {
						sql += " AND mst_t.task_country_id = :country_id";
					}
					if (state_id != 0) {
						sql += " AND mst_t.task_state_id = :state_id";
					}
				}
			}

			if (cat_id != 0) {
				sql += " AND mst_t.task_cat_law_id = :cat_id";
			}

			Query query = em.createQuery(sql);
			if (country_id != 0)
				query.setParameter("country_id", country_id);
			if (state_id != 0)
				query.setParameter("state_id", state_id);
			if (cat_id != 0)
				query.setParameter("cat_id", cat_id);

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

	// Method Created By: Mahesh Kharote(13/02/2017)
	@SuppressWarnings("unchecked")
	// Method Purpose: Save Tasks User Mapping
	@Override
	public <T> List<T> getRuleFromMst_task(String json) {
		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);
			int country_id = Integer.parseInt(jsonObject.get("country_id").toString());
			int state_id = Integer.parseInt(jsonObject.get("state_id").toString());
			int cat_id = Integer.parseInt(jsonObject.get("cat_id").toString());
			int legi_id = Integer.parseInt(jsonObject.get("legi_id").toString());
			String sql = "SELECT DISTINCT mst_t.task_rule_id,mst_t.task_rule_name FROM mst_task mst_t "
					+ " WHERE mst_t.task_country_id = :country_id";

			if (state_id != 0 && country_id != 0) {
				sql += " AND mst_t.task_state_id = :state_id";
			} else if (state_id != 0 && country_id == 0) {
				sql += " WHERE mst_t.task_state_id = :state_id";
			}
			if (cat_id != 0) {
				sql += " AND mst_t.task_cat_law_id = :cat_id";
			}
			if (legi_id != 0 && state_id != 0 || country_id != 0) {
				sql += " AND mst_t.task_legi_id = :legi_id";
			} else if (legi_id != 0) {
				sql += " WHERE mst_t.task_legi_id = :legi_id";
			}

			Query query = em.createQuery(sql);

			if (country_id != 0)
				query.setParameter("country_id", country_id);
			if (state_id != 0)
				query.setParameter("state_id", state_id);
			if (cat_id != 0)
				query.setParameter("cat_id", cat_id);
			if (legi_id != 0)
				query.setParameter("legi_id", legi_id);

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

	// Method Created By: Mahesh Kharote(10/01/2017)
	// Method Purpose: Save Tasks User Mapping
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getAllMappedTasksForEnablingPage(String jsonString) {
		try {
			JsonNode rootNode = null;
			final ObjectMapper mapper = new ObjectMapper();
			String sql = "SELECT tsk.task_id, tsk.task_legi_name ,tsk.task_rule_name,tsk.task_reference,tsk.task_activity_who,tsk.task_activity_when, "
					+ "tsk.task_activity,tsk.task_procedure ,tmapp.tmap_id,tmapp.tmap_enable_status,tmapp.tmap_client_tasks_id, "
					+ "tmapp.tmap_pr_user_id as prUserId, usr.user_first_name as usFnames, usr.user_last_name as usrLnames, "
					+ "tmapp.tmap_rw_user_id as rwUserId, usrw.user_first_name as rwFNames, usrw.user_last_name as rwLastNames, "
					+ "orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, "
					+ "tsk.task_lexcare_task_id,tsk.task_legi_id,tsk.task_rule_id, "
					+ "tmapp.tmap_fh_user_id as fhUserId, usrfh.user_first_name as fhFbanes, usrfh.user_last_name as fhLastNames "
					+ "FROM cfg_task_user_mapping tmapp " + "JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
					+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
					+ "JOIN mst_user usrw on usrw.user_id = tmapp.tmap_rw_user_id "
					+ "JOIN mst_user usrfh on usrfh.user_id = tmapp.tmap_fh_user_id "
					+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
					+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
					+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id ";

			rootNode = mapper.readTree(jsonString);
			Integer entityId = rootNode.path("orga_id").asInt();
			System.out.println("entityName:" + entityId);
			Integer unitId = rootNode.path("loca_id").asInt();
			Integer functionId = rootNode.path("dept_id").asInt();
			Integer executorId = rootNode.path("pr_user_id").asInt();
			Integer evaluatorId = rootNode.path("rw_user_id").asInt();
			Integer funHead_id = rootNode.path("fh_user_id").asInt();
			Integer legi_id = rootNode.path("legi_id").asInt();
			Integer rule_id = rootNode.path("rule_id").asInt();

			if (entityId != 0) {
				sql = sql + " where orga.orga_id = " + entityId;
			}
			if (unitId != 0) {
				sql = sql + " and loca.loca_id = " + unitId;
			}
			if (functionId != 0) {
				sql = sql + " and dept.dept_id = " + functionId;
				// System.out.println("hql:" +hql);
			}
			if (executorId != 0) {
				sql = sql + " and usr.user_id = " + executorId;
			}
			if (evaluatorId != 0) {
				sql = sql + " and usrw.user_id = " + evaluatorId;
			}
			if (funHead_id != 0) {
				sql = sql + " and usrfh.user_id = " + funHead_id;
			}
			if (legi_id != 0) {
				sql = sql + " and  tsk.task_legi_id =" + legi_id;
			}

			if (rule_id != 0) {
				sql = sql + " and  tsk.task_rule_id =" + rule_id;
			}

			System.out.println("query foe changeComplianceowner:" + sql);
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

	// Method Created By: Mahesh Kharote(10/01/2017)
	// Method Purpose: Enable Tasks User Mapping
	@Override
	public String enableTasks(ArrayList<Integer> tmap_ids) {
		try {
			for (int i = 0; i < tmap_ids.size(); i++) {
				Query query = em.createQuery("UPDATE " + TaskUserMapping.class.getName()
						+ " SET tmap_enable_status = 1 WHERE tmap_id = :tmap_id");
				query.setParameter("tmap_id", tmap_ids.get(i));
				query.executeUpdate();
			}
			return "Success";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	// Method Created By: Mahesh Kharote(10/01/2017)
	// Method Purpose: Disable Tasks User Mapping
	@Override
	public String disableTasks(ArrayList<Integer> tmap_ids) {
		try {
			for (int i = 0; i < tmap_ids.size(); i++) {
				Query query = em.createQuery("UPDATE " + TaskUserMapping.class.getName()
						+ " SET tmap_enable_status = 0 WHERE tmap_id = :tmap_id");
				query.setParameter("tmap_id", tmap_ids.get(i));
				query.executeUpdate();
			}
			return "Success";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	// Method Created By: Mahesh Kharote(10/01/2017)
	// Method Purpose: Save Tasks User Mapping
	@SuppressWarnings("rawtypes")
	@Override
	public TaskUserMapping getTmapForchangeComplianceOwner(int tmap_id) {
		try {
			TypedQuery query = em.createQuery(" from " + TaskUserMapping.class.getName() + " where tmap_id = :tmap_id",
					TaskUserMapping.class);
			query.setParameter("tmap_id", tmap_id);
			return (TaskUserMapping) query.getResultList().get(0);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return null;
	}

	// Method Created By: Mahesh Kharote(10/01/2017)
	// Method Purpose: Save Tasks User Mapping
	@Override
	public void updateTaskUserMapping(TaskUserMapping taskUserMapping) {
		try {
			em.merge(taskUserMapping);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

	}

	// Method Created By: Mahesh Kharote(10/01/2017)
	// Method Purpose: Save Tasks User Mapping
	@Override
	public void deleteTaskUserMapping(TaskUserMapping taskUserMapping) {
		try {
			em.remove(em.merge(taskUserMapping));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getDisabledTaskDetails(int tmap_id) {
		try {
			String sql = "SELECT tttrn.ttrn_id,tttrn.ttrn_status,tmapp.tmap_id,tmapp.tmap_client_tasks_id "
					+ "FROM cfg_task_user_mapping tmapp "
					+ "LEFT JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
					+ "LEFT JOIN trn_task_transactional ttttrn ON (tmapp.tmap_client_tasks_id = ttttrn.ttrn_client_task_id AND "
					+ "(tttrn.ttrn_created_at < ttttrn.ttrn_created_at)) " + "WHERE tmapp.tmap_enable_status = 0 "
					+ "AND tmapp.tmap_id = '" + tmap_id + "' " + "AND ttttrn.ttrn_id IS NULL";
			Query query = em.createNativeQuery(sql);
//			query.setParameter("tmap_id", tmap_id);
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
	public List<Object> getAssignedRecord(String jsonString, int tmap_task_id, String tmap_lexcare_task_id,
			int loca_id) {
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int tmap_dept_id = Integer.parseInt(jsonObj.get("dept_id").toString());
			// int tmap_loca_id = Integer.parseInt(jsonObj.get("tmap_loca_id").toString());
			int tmap_orga_id = Integer.parseInt(jsonObj.get("orga_id").toString());
			int tmap_pr_user_id = Integer.parseInt(jsonObj.get("pr_user_id").toString());
			int tmap_rw_user_id = Integer.parseInt(jsonObj.get("rw_user_id").toString());
			int tmap_fh_user_id = Integer.parseInt(jsonObj.get("fh_user_id").toString());

			String sql = "SELECT tmap.tmap_id,tmap.tmap_client_tasks_id,tmap.tmap_dept_id, "
					+ "tmap.tmap_fh_user_id as fhUserId, tmap.tmap_last_generated_value_for_client_task_id, "
					+ " tmap.tmap_lexcare_task_id,tmap.tmap_loca_id, tmap.tmap_orga_id, "
					+ "tmap.tmap_pr_user_id as prUserId, tmap.tmap_rw_user_id as rwUserId, tmap.tmap_task_id "
					+ " FROM cfg_task_user_mapping tmap where tmap.tmap_dept_id = " + tmap_dept_id
					+ " and tmap.tmap_fh_user_id = " + tmap_fh_user_id + " and tmap.tmap_lexcare_task_id = " + "'"
					+ tmap_lexcare_task_id + "'" + " and tmap.tmap_loca_id = " + loca_id + " and tmap.tmap_orga_id = "
					+ tmap_orga_id + "  and tmap.tmap_pr_user_id = " + tmap_pr_user_id + " and tmap.tmap_rw_user_id = "
					+ tmap_rw_user_id + " and tmap.tmap_task_id = " + tmap_task_id;
			Query query = em.createNativeQuery(sql);
			System.out.println("getAssignedRecord sql : " + sql);
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
	public <T> List<T> getAllMappedTasks() {
		try {

			String sql = "SELECT tsk.task_id, tsk.task_legi_name ,tsk.task_rule_name,tsk.task_reference,tsk.task_activity_who, "
					+ "tsk.task_activity_when,tsk.task_activity,tsk.task_procedure ,tmapp.tmap_id,tmapp.tmap_enable_status, "
					+ "tmapp.tmap_client_tasks_id, "
					+ "tmapp.tmap_pr_user_id as tPrUserId, usr.user_first_name as usrFNames, usr.user_last_name as usrLNames, "
					+ "tmapp.tmap_rw_user_id as rwUsrId, usrw.user_first_name as rwFNames, usrw.user_last_name as rwLNames, "
					+ "orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, "
					+ "tsk.task_lexcare_task_id,tsk.task_legi_id,tsk.task_rule_id, "
					+ "tmapp.tmap_fh_user_id as fhUserId, usrfh.user_first_name as fhFNames, usrfh.user_last_name as fhLNames "
					+ "FROM cfg_task_user_mapping tmapp JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
					+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
					+ "JOIN mst_user usrw on usrw.user_id = tmapp.tmap_rw_user_id "
					+ "JOIN mst_user usrfh on usrfh.user_id = tmapp.tmap_fh_user_id "
					+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
					+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
					+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id ";

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
	public List<Object> checkIfMappingExist(int tmap_orga_id, int loca_id, int tmap_dept_id) {
		try {
			String sql = "select enti_id,enti_orga_id,enti_loca_id,enti_dept_id from cfg_entity_mapping where enti_orga_id = "
					+ tmap_orga_id + " and enti_loca_id = " + loca_id + " and enti_dept_id = " + tmap_dept_id;
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
