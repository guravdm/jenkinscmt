package lexprd006.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lexprd006.dao.UnitDao;
import lexprd006.domain.Location;
import lexprd006.domain.Organization;

@Repository(value = "unitDao")
@Transactional
public class UnitDaoImpl implements UnitDao {

	@PersistenceContext
	private EntityManager em;

	// Method Created By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Fetch List of all departments from database
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

	// Method Created By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Fetch List of all departments from database

	// Return all the unit list (Behalf of this method, written (getAllUnit) method
	// below )
	@Override
	public <T> List<T> getAll(Class<T> clazz) {
		try {
			TypedQuery<T> query = em.createQuery(" from " + Location.class.getName(), clazz);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/*
	 * Get all the unit list as per the access level of the admin.
	 */

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<Object> getAllUnit(HttpSession session) {

		String getUnitQuery = null;
		try {
			// TypedQuery<T> query = em.createQuery(" from " + Location.class.getName(),
			// clazz);

			String requestFromMethod = session.getAttribute("requestFromMethod").toString();
			int userId = (int) session.getAttribute("userId");

			if (requestFromMethod.equals("listAllUnits")||(int) session.getAttribute("sess_role_id")==7) {
				System.out.println("Called");
				getUnitQuery = "SELECT loca_added_by, loca_id, loca_name FROM mst_location";
				System.out.println(getUnitQuery);
			} else {
				getUnitQuery = "SELECT distinct "
						+ "concat(usr.user_first_name, ' ', usr.user_last_name) as user_name, "
						+ "umap.umap_loca_id, loc.loca_name FROM "
						+ "cfg_user_entity_mapping umap, mst_location loc, mst_user usr " + "WHERE "
						+ "umap.umap_loca_id = loc.loca_id AND " + "usr.user_id = umap.umap_user_id AND usr.user_id = "
						+ userId + "";
			}

			// System.out.println(getUnitQuery);
			List<Object> resultList = em.createQuery(getUnitQuery).getResultList();

			return resultList;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	// Method Created By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Fetch List of all departments from database
	@SuppressWarnings("rawtypes")
	@Override
	public Location getLocationById(int loca_id) {
		try {
			TypedQuery query = em.createQuery(" from " + Location.class.getName() + " where loca_id = :loca_id",
					Location.class);
			query.setParameter("loca_id", loca_id);
			return (Location) query.getResultList().get(0);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	// Method Created By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Fetch List of all departments from database
	@Override
	public void updateLocation(Location location) {
		try {
			em.merge(location);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (em != null) {
				em.close();
			}
		}

	}

	// Method Created By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Fetch List of all departments from database
	@Override
	public int isLocaNameExist(int loca_id, String loca_name) {
		try {
			String sql = "select count(*) as locacount from mst_location where loca_name='" + loca_name + "' " + " ";
			if (loca_id != 0) {
				sql += " AND loca_id !=" + loca_id;
			}
			Query query = em.createQuery(sql);
			String count = query.getResultList().get(0).toString();

			return Integer.parseInt(count);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return 0;
	}

	@Override
	public List<Location> checkNameIfExist(String loca_name) {
		try {
			Query query = null;
			String sql = null;

			if (loca_name.contains("'")) {
				loca_name = loca_name.replace("'", "''");
				sql = "select loca_id, loca_name, loca_parent_id from mst_location where loca_name = " + "'" + loca_name
						+ "'";
			} else {
				sql = "select loca_id, loca_name, loca_parent_id from mst_location where loca_name = " + "'" + loca_name
						+ "'";
			}
			System.out.println("QUERY : " + sql);
			query = em.createQuery(sql);
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
	public List<Object> getLocationIdByName(String unit_name) {
		try {

			String sql = "select loca_id, loca_enable_status, loca_name from mst_location where loca_name LIKE " + "\""
					+ unit_name + "\"";
			Query query = em.createNativeQuery(sql);
			System.out.println("sql : " + sql);
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
