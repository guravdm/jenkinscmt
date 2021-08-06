package lexprd006.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lexprd006.dao.FunctionDao;
import lexprd006.domain.Department;

/*
 * Author: Mahesh Kharote
 * Date: 07/11/2016
 * Purpose: DAO Impl for Functions
 * 
 * 
 * 
 * */

@Transactional
@Repository(value = "functionDao")
public class FuntionDaoImpl implements FunctionDao {

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
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<Object> getAllFunction(HttpSession session) {
		try {

			if ((int) session.getAttribute("sess_role_id") == 7) {
				String getFunctionQuery = "SELECT dept_added_by, dept_id, dept_name FROM mst_department";
				List<Object> resultList = em.createQuery(getFunctionQuery).getResultList();

				return resultList;
			}
			// TypedQuery<T> query = em.createQuery(" from " + clazz.getName(), clazz);

			int userId = (int) session.getAttribute("userId");
			String getDepartmentQuery = "select distinct concat(usr.user_first_name, ' ', usr.user_last_name) as user_name, umap. umap_dept_id, dept. dept_name FROM cfg_user_entity_mapping umap, mst_department dept, mst_user usr WHERE umap.umap_dept_id  = dept. dept_id AND usr.user_id = umap.umap_user_id AND usr.user_id ="
					+ userId + "";
			List<Object> resultList = em.createQuery(getDepartmentQuery).getResultList();

			return resultList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Get Department by id for edit from Database
	@SuppressWarnings("rawtypes")
	@Override
	public Department getDepartmentById(int dept_id) {
		try {
			TypedQuery query = em.createQuery(" from " + Department.class.getName() + " where dept_id = :dept_id",
					Department.class);
			query.setParameter("dept_id", dept_id);
			return (Department) query.getResultList().get(0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Update particular department in Database
	@Override
	public void updateDepartment(Department department) {
		try {
			em.merge(department);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Method Created By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Check if Department name already exists
	@Override
	public int isDeptNameExist(int dept_id, String dept_name) {
		try {
			String sql = "select count(*) as deptcount from mst_department where dept_name='" + dept_name + "' " + " ";
			if (dept_id != 0) {
				sql += " AND dept_id !=" + dept_id;
			}
			Query query = em.createQuery(sql);
			return Integer.parseInt(query.getResultList().get(0).toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// Method Written By: Mahesh Kharote(21/12/2016)
	// Method Purpose: Get Mapped Departments for Entity Mapping
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getMappedDepartments(int enti_orga_id, int enti_loca_id) {
		try {
			String sql = "SELECT dpt.dept_id, dpt.dept_name FROM mst_department dpt "
					+ "JOIN cfg_entity_mapping ent ON ent.enti_dept_id = dpt.dept_id " + "WHERE ent.enti_orga_id = '"
					+ enti_orga_id + "' AND ent.enti_loca_id = '" + enti_loca_id + "'";
			Query query = em.createNativeQuery(sql);
//			query.setParameter("enti_orga_id", enti_orga_id);
//			query.setParameter("enti_loca_id", enti_loca_id);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Mahesh Kharote(21/12/2016)
	// Method Purpose: Get Unmapped Departments for Entity Mapping
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getUnMappedDepartments(int enti_orga_id, int enti_loca_id) {
		try {
			String sql = "SELECT dep.dept_id, dep.dept_name FROM mst_department dep WHERE dep.dept_id NOT IN "
					+ "(SELECT dpt.dept_id FROM mst_department dpt "
					+ "JOIN cfg_entity_mapping ent ON ent.enti_dept_id = dpt.dept_id " + "WHERE ent.enti_orga_id = '"
					+ enti_orga_id + "' AND ent.enti_loca_id = '" + enti_loca_id + "')";
			Query query = em.createNativeQuery(sql);
//			query.setParameter("enti_orga_id", enti_orga_id);
//			query.setParameter("enti_loca_id", enti_loca_id);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Department> checkNameIfExist(String dept_name) {
		try {
			Query query = null;
			String sql = null;

			if (dept_name.contains("'")) {
				dept_name = dept_name.replace("'", "''");
				sql = "select dept_id, dept_name, dept_parent_id from mst_department where dept_name = " + "'"
						+ dept_name + "'";
			} else {
				sql = "select dept_id, dept_name, dept_parent_id from mst_department where dept_name = " + "'"
						+ dept_name + "'";
			}
			System.out.println("QUERY : " + sql);
			query = em.createNativeQuery(sql);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Object> getDepartmentNameById(String function_name) {
		try {
			/*
			 * String sql = "from " + Department.class.getName() + " where dept_name = " +
			 * "'" + function_name + "'"; Query query = em.createQuery(sql);
			 * System.out.println("sql in dept:" +sql); return (Department)
			 * query.getResultList().get(0);
			 */

			String sql = "select dept_id, dept_enable_status, dept_name from mst_department where dept_name LIKE "
					+ "\"" + function_name + "\"";
			Query query = em.createNativeQuery(sql);
			System.out.println("sql : " + sql);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
