package lexprd006.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lexprd006.dao.EntityMappingDao;
import lexprd006.domain.EntityMapping;

/*
 * Author: Mahesh Kharote
 * Date: 19/12/2016
 * Purpose: DAO Impl for Entity Mapping
 * 
 * 
 * 
 * */

@Transactional
@Repository(value = "entityMappingDao")
public class EntityMappingDaoImpl implements EntityMappingDao {

	@PersistenceContext
	private EntityManager em;

	// Method Written By: Mahesh Kharote(19/12/2016)
	// Method Purpose: Save Function To DB
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

	// Method Written By: Mahesh Kharote(19/12/2016)
	// Method Purpose: Get List of all Entities Mapping Function To DB
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getJoinedAll(HttpSession session) {
		try {

			if ((int) session.getAttribute("sess_role_id") == 7) {
				String sql = "SELECT ent.enti_id, org.orga_name, loc.loca_name, dep.dept_name,ent.enti_approval_status,ent.enti_enable_status "
						+ "FROM cfg_entity_mapping ent "
						+ "JOIN mst_organization org on ent.enti_orga_id = org.orga_id "
						+ "JOIN mst_location loc on ent.enti_loca_id = loc.loca_id "
						+ "JOIN mst_department dep on ent.enti_dept_id = dep.dept_id";

				// System.out.println("In joined all for entities mapping : - " + sql);
				Query query = em.createNativeQuery(sql);
				List<T> resultList = query.getResultList();
				// System.out.println("List Size of Entity Mapping : " + resultList.size());

				return resultList;
			}
			/*
			 * Query to get all the Entity Mapping
			 */
			/*
			 * String sql =
			 * "SELECT ent.enti_id, org.orga_name, loc.loca_name, dep.dept_name,ent.enti_approval_status,ent.enti_enable_status "
			 * + "FROM cfg_entity_mapping ent " +
			 * "JOIN mst_organization org on ent.enti_orga_id = org.orga_id " +
			 * "JOIN mst_location loc on ent.enti_loca_id = loc.loca_id " +
			 * "JOIN mst_department dep on ent.enti_dept_id = dep.dept_id";
			 */

			/*
			 * Query to get Entity Mapping as per the user access level.
			 */

			int userId = (int) session.getAttribute("userId");
			// System.out.println("User Id is : "+userId);
			String sqlQuery = "SELECT ent.enti_id, org.orga_name, loc.loca_name, dep.dept_name,ent.enti_approval_status,ent.enti_enable_status "
					+ "FROM cfg_entity_mapping ent " + "JOIN mst_organization org on ent.enti_orga_id = org.orga_id "
					+ "JOIN mst_location loc on ent.enti_loca_id = loc.loca_id "
					+ "JOIN mst_department dep on ent.enti_dept_id = dep.dept_id "
					+ "JOIN cfg_user_entity_mapping umap ON ent.enti_orga_id = umap.umap_orga_id "
					+ "AND ent.enti_loca_id = umap.umap_loca_id " + "AND ent.enti_dept_id = umap.umap_dept_id "
					+ "JOIN mst_user usr ON umap.umap_user_id = usr.user_id AND usr.user_id = " + userId + "";

			// System.out.println("In joined all for entities mapping : - " + sqlQuery);
			Query query = em.createNativeQuery(sqlQuery);
			List<T> resultList = query.getResultList();
			// System.out.println("List Size of Entity Mapping : " + resultList.size());

			return resultList;
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
	public <T> List<T> getMappedUnits(int orga_id) {
		try {
			String sql = "SELECT DISTINCT loca.loca_id, loca.loca_name FROM mst_location loca "
					+ "JOIN cfg_entity_mapping ent ON ent.enti_loca_id = loca.loca_id WHERE ent.enti_orga_id = '"
					+ orga_id + "' ";
			Query query = em.createNativeQuery(sql);
//			query.setParameter("enti_orga_id", orga_id);
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

	// Method Created By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Fetch List of all location mapped to entity from database
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getAllMapping() {
		try {
			String sql = "SELECT ent.enti_id, org.orga_id, org.orga_name, loc.loca_id, loc.loca_name, dep.dept_id, dep.dept_name, "
					+ "ent.enti_approval_status,ent.enti_enable_status " + "FROM cfg_entity_mapping ent "
					+ "JOIN mst_organization org on ent.enti_orga_id = org.orga_id "
					+ "JOIN mst_location loc on ent.enti_loca_id = loc.loca_id "
					+ "JOIN mst_department dep on ent.enti_dept_id = dep.dept_id";
			System.out.println("In joined all for entities mapping");
			System.out.println("getAllMapping SQL " + sql);
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

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getAllMappingsReport(int user_id) {
		try {

			String sql = "SELECT umap.umap_orga_id, org.orga_id, org.orga_name, loc.loca_id, loc.loca_name, dep.dept_id, "
					+ "dep.dept_name,umap.umap_enable_status " + "FROM cfg_user_entity_mapping umap "
					+ "JOIN mst_organization org on umap.umap_orga_id = org.orga_id "
					+ "JOIN mst_location loc on umap.umap_loca_id = loc.loca_id "
					+ "JOIN mst_department dep on umap.umap_dept_id = dep.dept_id where umap.umap_user_id =" + user_id
					+ " ";
			System.out.println("In joined all for entities mapping");
			System.out.println(sql);
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
	public <T> List<T> getAll(Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityMapping getTasksForUserUpdate(int orgaId, int unitId, int deptId) {
		try {
			TypedQuery query = em.createQuery(" from " + EntityMapping.class.getName()
					+ " where enti_orga_id = :entity_id and enti_loca_id = :unit_id and enti_dept_id = :function_id",
					EntityMapping.class);
			query.setParameter("entity_id", orgaId);
			query.setParameter("unit_id", unitId);
			query.setParameter("function_id", deptId);
			if (query.getResultList().size() > 0) {
				return (EntityMapping) query.getResultList().get(0);
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

	@Override
	public void updateEntityMapping(EntityMapping entityMapping) {
		try {
			em.merge(entityMapping);
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
	public <T> List<T> getAllByID(int orga, int loca, int dept) {
		try {
			String sql = "SELECT enti_orga_id, enti_loca_id, enti_dept_id from cfg_entity_mapping where enti_orga_id="
					+ orga + " AND enti_loca_id=" + loca + " AND enti_dept_id=" + dept;
			System.out.println("getAllByID :" + sql);
			// System.out.println(sql);
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
