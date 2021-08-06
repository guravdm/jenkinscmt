package lexprd006.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lexprd006.dao.UserEntityMappingDao;
import lexprd006.domain.UserEntityMapping;

@Repository(value = "userEntityMappingDao")
@Transactional
public class UserEntityMappingDaoImpl implements UserEntityMappingDao {

	@PersistenceContext
	private EntityManager em;

	// Method Written By: Mahesh Kharote(07/11/2016)
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

	// Method Written By: Mahesh Kharote(22/02/2017)
	// Method Purpose: Get Data For filter access wise

//	@Cacheable(value = "getDataForFilterUsingAccessTable")
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getDataForFilterUsingAccessTable() {
		try {

			String sql = "SELECT orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, "
					+ "usr.user_id, usr.user_first_name, usr.user_last_name, usr.user_role_id "
					+ "FROM cfg_user_entity_mapping umap "
					+ "JOIN mst_organization orga on umap.umap_orga_id = orga.orga_id "
					+ "JOIN mst_location loca on umap.umap_loca_id = loca.loca_id "
					+ "JOIN mst_department dept on umap.umap_dept_id = dept.dept_id "
					+ "JOIN mst_user usr on umap.umap_user_id = usr.user_id ";

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

	// Method Written By: Mahesh Kharote(22/02/2017)
	// Method Purpose: Get list of access user wise
//	@Cacheable(value = "getUserAccessById", key = "#user_id")
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getUserAccessById(int user_id) {
		try {

			String sql = "SELECT orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, "
					+ "usr.user_id, usr.user_first_name, usr.user_last_name, usr.user_role_id,umap.umap_id "
					+ "FROM cfg_user_entity_mapping umap "
					+ "JOIN mst_organization orga on umap.umap_orga_id = orga.orga_id "
					+ "JOIN mst_location loca on umap.umap_loca_id = loca.loca_id "
					+ "JOIN mst_department dept on umap.umap_dept_id = dept.dept_id "
					+ "JOIN mst_user usr on umap.umap_user_id = usr.user_id where umap_user_id = '" + user_id + "' ";

			Query query = em.createNativeQuery(sql);
//			query.setParameter("user_id", user_id);
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

	// Method Written By: Mahesh Kharote(22/02/2017)
	// Method Purpose: Get list of access user wise
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getDistinctOrgaById(int user_id) {

		try {

			String sql = "SELECT DISTINCT orga.orga_id, orga.orga_name FROM cfg_user_entity_mapping umap "
					+ "JOIN mst_organization orga on umap.umap_orga_id = orga.orga_id where umap_user_id = '" + user_id
					+ "' ";

			Query query = em.createNativeQuery(sql);
//			query.setParameter("user_id", user_id);
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

	// Method Written By: Mahesh Kharote(22/02/2017)
	// Method Purpose: Get Data For filter access wise
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getDistinctLocaById(int user_id) {
		try {

			String sql = "SELECT DISTINCT loca.loca_id, loca.loca_name " + "FROM cfg_user_entity_mapping umap "
					+ "JOIN mst_location loca on loca.loca_id = umap.umap_loca_id where umap_user_id = '" + user_id
					+ "' ";

			Query query = em.createNativeQuery(sql);
//			query.setParameter("user_id", user_id);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Mahesh Kharote(22/02/2017)
	// Method Purpose: Get Data For filter access wise
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getDistinctDeptById(int user_id) {
		try {

			String sql = "SELECT DISTINCT dept.dept_id, dept.dept_name " + "FROM cfg_user_entity_mapping umap "
					+ "JOIN mst_department dept on dept.dept_id = umap.umap_dept_id " + "where umap_user_id = '"
					+ user_id + "' ";

			Query query = em.createNativeQuery(sql);
//			query.setParameter("user_id", user_id);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void removeUserAccess(int umap_id) {
		try {
			UserEntityMapping entityMapping = em.find(UserEntityMapping.class, umap_id);
			em.remove(entityMapping);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Method Written By: Harshad Padole
	// Method Purpose: Check task exist before remove user access
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> checkTaskExist(int umap_id, int user_id) {
		try {
			String sql = "SELECT uemap.umap_id,tmapp.tmap_orga_id,tmapp.tmap_loca_id,tmapp.tmap_dept_id,tmapp.tmap_client_tasks_id "
					+ "FROM cfg_task_user_mapping tmapp "
					+ "JOIN cfg_user_entity_mapping uemap ON tmapp.tmap_orga_id = uemap.umap_orga_id "
					+ "AND tmapp.tmap_loca_id = uemap.umap_loca_id AND tmapp.tmap_dept_id = uemap.umap_dept_id "
					+ "WHERE uemap.umap_id = '" + umap_id + "' AND uemap.umap_user_id = '" + user_id + "' AND "
					+ "(tmapp.tmap_pr_user_id = '" + user_id + "' OR tmapp.tmap_rw_user_id = '" + user_id + "' )";
			Query query = em.createNativeQuery(sql);
//			query.setParameter("umap_id", umap_id);
//			query.setParameter("user_id", user_id);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Harshad Padole
	// Method Purpose: Get orgonogran
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getOrgonogram() {
		try {
			String sql = "SELECT enti.enti_orga_id,enti.enti_loca_id,enti.enti_dept_id,orga.orga_name,loca.loca_name,dept.dept_name "
					+ "FROM cfg_entity_mapping enti JOIN mst_organization orga ON orga.orga_id = enti.enti_orga_id "
					+ "JOIN mst_location loca ON loca.loca_id = enti.enti_loca_id "
					+ "JOIN mst_department dept ON dept.dept_id = enti.enti_dept_id ORDER BY orga_id, loca_id,dept_id";
			Query query = em.createNativeQuery(sql);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Harshad Padole
	// Method Purpose: User mapping list
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getUserForMappingList(int orga_id, int loca_id, int dept_id) {
		try {
			String sql = "Select usr.user_id,usr.user_first_name,usr.user_last_name,usr.user_role_id "
					+ "FROM mst_user usr JOIN cfg_user_entity_mapping umap ON umap.umap_user_id = usr.user_id "
					+ "WHERE umap.umap_orga_id = '" + orga_id + "' AND umap.umap_loca_id = '" + loca_id
					+ "' AND umap.umap_dept_id = '" + dept_id + "' ";
			Query query = em.createNativeQuery(sql);
//			query.setParameter("orga_id", orga_id);
//			query.setParameter("loca_id", loca_id);
//			query.setParameter("dept_id", dept_id);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Mahesh Kharote(05/10/2017)
	// Method Purpose: Get User access wise
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getUserWithAccessForCommonEmail() {
		try {
			String sql = "SELECT orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, "
					+ "usr.user_id, usr.user_first_name , usr.user_last_name, usr.user_email " + "FROM mst_user usr "
					+ "JOIN cfg_user_entity_mapping umap on usr.user_id = umap.umap_user_id "
					+ "JOIN mst_organization orga on orga.orga_id = umap.umap_orga_id "
					+ "JOIN mst_location loca on loca.loca_id = umap.umap_loca_id "
					+ "JOIN mst_department dept on dept.dept_id = umap.umap_dept_id "
					+ "WHERE usr.user_enable_status = 1 AND usr.user_role_id < 8";
			Query query = em.createNativeQuery(sql);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
