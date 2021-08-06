package lexprd006.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lexprd006.dao.DesignationDao;
import lexprd006.domain.Designation;

/*
 * Author: Mahesh Kharote
 * Date: 11/11/2016
 * Purpose: DAO Impl for Designations
 * 
 * 
 * 
 * */

@Repository(value = "designationDao")
@Transactional
public class DesignationDaoImpl implements DesignationDao {

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
	@Override
	public <T> List<T> getAll(Class<T> clazz) {
		try {
			TypedQuery<T> query = em.createQuery(" from " + clazz.getName(), clazz);
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
	// Method Purpose: Fetch List of all departments from database
	@SuppressWarnings("rawtypes")
	@Override
	public Designation getDesignationById(int desi_id) {
		try {
			TypedQuery query = em.createQuery(" from " + Designation.class.getName() + " where desi_id = :desi_id",
					Designation.class);
			query.setParameter("desi_id", desi_id);
			return (Designation) query.getResultList().get(0);

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
	// Method Purpose: Fetch List of all departments from database
	@Override
	public void updateDesignation(Designation designation) {
		try {
			em.merge(designation);
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
	@Override
	public int isDesiNameExist(int desi_id, String desi_name) {
		try {
			String sql = "select count(*) as locacount from mst_designation where desi_name='" + desi_name + "' " + " ";
			if (desi_id != 0) {
				sql += " AND desi_id !=" + desi_id;
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
}
