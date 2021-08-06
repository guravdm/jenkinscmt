package lexprd006.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lexprd006.dao.EntityDao;
import lexprd006.domain.Organization;

/*
 * Author: Mahesh Kharote
 * Date: 11/11/2016
 * Purpose: DAO Impl for Enities
 * 
 * 
 * 
 * */

@Repository(value = "entityDao")
@Transactional
public class EntityDaoImpl implements EntityDao {

	@PersistenceContext
	private EntityManager em;

	// Method Written By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Save Entity To DB
	@Override
	public void persist(Organization obj) {
		try {
			em.persist(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Method Written By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Fetch List of all Organization from database Hide NA from
	// organization list list
	@Override
	public <T> List<T> getAll(Class<T> clazz) {

		try {
			TypedQuery<T> query = em.createQuery(" from " + clazz.getName() + " where orga_id > 1 ", clazz);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Mahesh Kharte
	// Method Purpose: Adding entity requires NA
	@Override
	public <T> List<T> getAllForAddingEntity(Class<T> clazz) {
		try {
			TypedQuery<T> query = em.createQuery(" from " + clazz.getName(), clazz);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Get Organization with parent Name from Database
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getJoinedAll(HttpSession session, String requestFromMethod) {
		try {

			// OLD QUERY

			/*
			 * if((int) session.getAttribute("sess_role_id")==7) {
			 * System.out.println("Request from Super-Admin user \n"); String sqlQuery =
			 * "SELECT org.orga_id AS child_orga_id, " +
			 * "org.orga_name AS child_orga_name , org1.orga_id AS parent_orga_id , org1.orga_name AS parent_orga_name, "
			 * +
			 * "org.orga_approval_status,org.orga_enable_status FROM mst_organization org, "
			 * + "mst_organization org1 WHERE org.orga_parent_id = org1.orga_id";
			 * 
			 * Query query = em.createQuery(sqlQuery); //System.out.println("sqlQuery : " +
			 * sqlQuery); return query.getResultList(); }
			 */

			if (session == null && requestFromMethod.equals("listAllEntities")
					|| (int) session.getAttribute("sess_role_id") == 7) {

				System.out.println("Request coming from Entity Mapping page \n");
				String sqlQuery = "SELECT org.orga_id AS child_orga_id, "
						+ "org.orga_name AS child_orga_name , org1.orga_id AS parent_orga_id , org1.orga_name AS parent_orga_name, "
						+ "org.orga_approval_status,org.orga_enable_status FROM mst_organization org, "
						+ "mst_organization org1 WHERE org.orga_parent_id = org1.orga_id";

				Query query = em.createQuery(sqlQuery);
				// System.out.println("sqlQuery : " + sqlQuery);
				return query.getResultList();
			}

			/* New Query Below query returns list of entity where users has access */
			else {

				int userId = (int) session.getAttribute("userId");

				String sqlQuery1 = "SELECT org.orga_id AS child_orga_id, "
						+ "org.orga_name AS child_orga_name , org1.orga_id AS parent_orga_id , org1.orga_name AS parent_orga_name, "
						+ "org.orga_approval_status,org.orga_enable_status FROM mst_organization org, "
						+ "mst_organization org1, mst_user usr, cfg_user_entity_mapping umap WHERE org.orga_parent_id = org1.orga_id AND "
						+ "umap.umap_user_id = usr.user_id AND " + "org.orga_id = umap.umap_orga_id AND "
						+ "usr.user_id =" + userId + " group by org.orga_name";

				/* System.out.println("QUERY : " + sqlQuery1); */
				Query query = em.createQuery(sqlQuery1);
				System.out.println("sqlQuery1 : " + sqlQuery1);
				return query.getResultList();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Get Organization by id for edit from Database
	@SuppressWarnings("rawtypes")
	@Override
	public Organization getOrganizationById(int orga_id) {
		try {
			TypedQuery query = em.createQuery(" from " + Organization.class.getName() + " where orga_id = :orga_id",
					Organization.class);
			query.setParameter("orga_id", orga_id);
			return (Organization) query.getResultList().get(0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Update particular department in Database
	@Override
	public void updateOrganization(Organization organization) {
		try {
			em.merge(organization);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int approveDisapproveOrg(int org_id, int orga_status) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int enableDisableOrg(int org_id, int orga_status) {
		// TODO Auto-generated method stub
		return 0;
	}

	// Method Created By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Check for existing Entity Name
	@Override
	public int isOrgaNameExist(int orga_id, String orga_name) {
		try {
			String sql = "select count(*) as orgacount from mst_organization where orga_name='" + orga_name + "' "
					+ " ";
			if (orga_id != 0) {
				sql += " AND orga_id !=" + orga_id;
			}
			Query query = em.createQuery(sql);
			String count = query.getResultList().get(0).toString();

			return Integer.parseInt(count);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int getOrganizationIdByName(String eId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Organization> checkDuplicateName(String orga_name) {
		try {
			Query query = null;
			String sql = null;

			if (orga_name.contains("'")) {
				orga_name = orga_name.replace("'", "''");
				sql = "select orga_id, orga_name, orga_parent_id from mst_organization where orga_name = " + "'"
						+ orga_name + "'";
			} else {
				sql = "select orga_id, orga_name, orga_parent_id from mst_organization where orga_name = " + "'"
						+ orga_name + "'";
			}
			System.out.println("QUERY : " + sql);
			query = em.createQuery(sql);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Object> getOrganizationIdByOrgaName(String entity_name) {
		try {
			/*
			 * String sql = "from " + Organization.class.getName() + " where orga_name = " +
			 * "'" + orga_name + "'"; Query query = em.createQuery(sql); return
			 * (Organization) query.getResultList().get(0);
			 */

			String sql = "select orga_id, orga_enable_status, orga_name from mst_organization where orga_name LIKE "
					+ "\"" + entity_name + "\"";
			Query query = em.createNativeQuery(sql);
			System.out.println("sql : " + sql);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
