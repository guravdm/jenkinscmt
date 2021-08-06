package lexprd006.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Repository;

import lexprd006.dao.LegalUpdateDao;
import lexprd006.domain.Task;

@Repository("legalUpdateDao")
@Transactional
public class LegalUpdateDaoImpl implements LegalUpdateDao {

	@PersistenceContext
	private EntityManager em;

	// Method Created By: Rahul Shinde(11/11/2016)
	// Method Purpose: Upload Legal Updates
	@Override
	public String uploadlegalUpdates(Task task) {
		try {
			em.persist(task); // persist Object
			return "Success";
		} catch (Exception e) {
			e.printStackTrace();
			return "Fail";
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	@Override
	public int getLexcareTaskExist(String lexcareId) {
		try {
			String sql = "SELECT task_id FROM mst_task where task_lexcare_task_id = ?";
			Query query = em.createQuery(sql);
			query.setParameter(1, lexcareId);
			return (int) query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	@Override
	public String update_legalUpdates(Task task) {
		try {
			em.merge(task);
			return "Success";
		} catch (Exception e) {
			e.printStackTrace();
			return "Fail";
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

}
