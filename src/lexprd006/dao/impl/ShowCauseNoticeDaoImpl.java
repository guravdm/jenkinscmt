package lexprd006.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lexprd006.dao.ShowCauseNoticeDao;
import lexprd006.domain.ShowCauseDocuments;
import lexprd006.domain.ShowCauseNotice;
import lexprd006.domain.ShowCauseNoticeActionItem;

/*
 * Author: Harshad Padole
 * Date: 09/05/2017
 * 
 * */

@Repository("showCauseNoticeDao")
@Transactional
public class ShowCauseNoticeDaoImpl implements ShowCauseNoticeDao {

	@PersistenceContext
	private EntityManager em;

	// Method Written By: Harshad Padole(09-05-2017)
	// Method Purpose:Save show cause notice details
	@Override
	public int saveShowCauseNotice(ShowCauseNotice causeNotice) {
		try {
			em.persist(causeNotice);
			em.flush();
			return causeNotice.getScau_id();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return 0;
	}

	// Method Written By: Harshad Padole(09-05-2017)
	// Method Purpose: Get User access wise organization,location and department
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getFiltersForShowCauseNotice(HttpSession session) {
		String sql = "";
		int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
		int user_role = Integer.parseInt(session.getAttribute("sess_role_id").toString());
		try {

			if (user_role == 7) {
				sql = "SELECT orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, "
						+ "usr.user_id, usr.user_first_name, usr.user_last_name, usr.user_role_id "
						+ "FROM cfg_user_entity_mapping umap "
						+ "JOIN mst_organization orga on umap.umap_orga_id = orga.orga_id "
						+ "JOIN mst_location loca on umap.umap_loca_id = loca.loca_id "
						+ "JOIN mst_department dept on umap.umap_dept_id = dept.dept_id "
						+ "JOIN mst_user usr on umap.umap_user_id = usr.user_id";
			} else {
				sql = "SELECT orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, "
						+ "usr.user_id, usr.user_first_name, usr.user_last_name, usr.user_role_id "
						+ "FROM cfg_user_entity_mapping umap "
						+ "JOIN mst_organization orga on umap.umap_orga_id = orga.orga_id "
						+ "JOIN mst_location loca on umap.umap_loca_id = loca.loca_id "
						+ "JOIN mst_department dept on umap.umap_dept_id = dept.dept_id "
						+ "JOIN mst_user usr on umap.umap_user_id = usr.user_id  WHERE umap.umap_user_id = '" + user_id
						+ "' ";
			}

			System.out.println("Create Show Cause Notice Filters : " + sql);

			Query query = em.createNativeQuery(sql);
//			if (user_role != 7) {
//				query.setParameter("user_id", user_id);
//			}

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

	// Method Written By: Harshad Padole(09-05-2017)
	// Method Purpose: get last generated value from document table
	@Override
	public int getLastGeneratedValueForShowCauseDocument(int related_id, String related_to) {
		try {
			String sql = "select MAX(scnd_last_generated_value_for_filename_for_related_id) FROM "
					+ ShowCauseDocuments.class.getName()
					+ " WHERE scnd_related_id =:related_id AND scnd_related_type =:related_to";
			Query query = em.createQuery(sql);
			query.setParameter("related_id", related_id);
			query.setParameter("related_to", related_to);
			if (!query.getResultList().isEmpty()) {
				if (query.getResultList().get(0) != null) {
					return Integer.parseInt(query.getResultList().get(0).toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return 0;
	}

	// Method Written By: Harshad Padole(09-05-2017)
	// Method Purpose: Save Document
	@Override
	public void saveDocument(Object object) {
		try {
			em.persist(object);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	// Method Written By: Harshad Padole(09-05-2017)
	// Method Purpose:Get all show cause notice by role id and responsible person
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getShowCauseNoticeList(HttpSession session) {
		try {
			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			int user_role = Integer.parseInt(session.getAttribute("sess_role_id").toString());
			String sql = "";
			if (user_role >= 5) {
				sql = "SELECT scau.scau_id,orga.orga_id,orga.orga_name,loca.loca_id,loca.loca_name,dept.dept_id,dept.dept_name, "
						+ "scau.scau_ralated_to,scau.scau_notice_date,scau.scau_received_date,scau.scau_deadline_date, "
						+ "scau.scau_action_taken,scau.scau_reminder_date,scau.scau_status, "
						+ "res_usr.user_id as resUserId, res_usr.user_first_name as resFName, res_usr.user_last_name as resLastName, "
						+ "rep_usr.user_id as repUserId, rep_usr.user_first_name as repFName, rep_usr.user_last_name as repLName, "
						+ "scau.scau_comments FROM mst_showcausenotice scau "
						+ "JOIN mst_organization orga ON orga.orga_id = scau.scau_orga_id "
						+ "JOIN mst_location loca ON loca.loca_id = scau.scau_loca_id "
						+ "JOIN mst_department dept ON dept.dept_id = scau.scau_dept_id "
						+ "JOIN mst_user res_usr ON res_usr.user_id = scau.scau_responsible_person "
						+ "JOIN mst_user rep_usr ON rep_usr.user_id = scau.scau_reporting_person";

			} else {
				sql = "SELECT scau.scau_id,orga.orga_id,orga.orga_name,loca.loca_id,loca.loca_name,dept.dept_id,dept.dept_name,scau.scau_ralated_to, "
						+ "scau.scau_notice_date,scau.scau_received_date,scau.scau_deadline_date, "
						+ "scau.scau_action_taken,scau.scau_reminder_date,scau.scau_status, "
						+ "res_usr.user_id as rsUsR, res_usr.user_first_name as resFName, res_usr.user_last_name as rsLastName, "
						+ "rep_usr.user_id as repUsrId, rep_usr.user_first_name as repFName, rep_usr.user_last_name as repLastName, "
						+ "scau.scau_comments FROM mst_showcausenotice scau "
						+ "JOIN mst_organization orga ON orga.orga_id = scau.scau_orga_id "
						+ "JOIN mst_location loca ON loca.loca_id = scau.scau_loca_id "
						+ "JOIN mst_department dept ON dept.dept_id = scau.scau_dept_id "
						+ "JOIN mst_user res_usr ON res_usr.user_id = scau.scau_responsible_person "
						+ "JOIN mst_user rep_usr ON rep_usr.user_id = scau.scau_reporting_person "
						+ "WHERE (scau.scau_responsible_person = '" + user_id + "' OR scau.scau_reporting_person = '"
						+ user_id + "' OR scau.scau_added_by= '" + user_id + "' ) ";
			}
			Query query = em.createNativeQuery(sql);
//			if (user_role < 5) {
//				query.setParameter("responsible_per", user_id);
//				query.setParameter("reporting_per", user_id);
//				query.setParameter("added_by", user_id);
//			}
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

	// Method Written By: Harshad Padole
	// Method Purpose: Save action item
	@Override
	public int saveActionItem(ShowCauseNoticeActionItem actionItem) {
		try {
			em.persist(actionItem);
			em.flush();
			return actionItem.getTscn_id();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return 0;
	}

	// Method Written By: Harshad Padole
	// Method Purpose: get notice details to update
	@SuppressWarnings("rawtypes")
	@Override
	public ShowCauseNotice getNoticeDetailsById(int scau_id) {
		try {
			TypedQuery query = em.createQuery(" from " + ShowCauseNotice.class.getName() + " where scau_id = :scau_id",
					ShowCauseNotice.class);
			query.setParameter("scau_id", scau_id);
			return (ShowCauseNotice) query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return null;
	}

	// Method Written By: Harshad Padole
	// Method Purpose: update sent object to DB
	@Override
	public void merge(Object object) {
		try {
			em.merge(object);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	// Method Written By: Harshad Padole
	// Method Purpose: Get Show cause notice details
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getShowCauseNoticeDetailsById(int scau_id) {
		try {

			String sql = "SELECT scau.scau_id,orga.orga_id,orga.orga_name,loca.loca_id,loca.loca_name,dept.dept_id,dept.dept_name,scau.scau_ralated_to,scau.scau_notice_date,scau.scau_received_date,scau.scau_deadline_date, "
					+ "scau.scau_action_taken,scau.scau_reminder_date,scau.scau_status, "
					+ "res_usr.user_id as resUserId, res_usr.user_first_name as resFName, res_usr.user_last_name as resLastNames, "
					+ "rep_usr.user_id as repUserId, rep_usr.user_first_name as repFNames, rep_usr.user_last_name as repLNames, "
					+ "scau.scau_comments,scau.scau_next_action_item FROM mst_showcausenotice scau "
					+ "JOIN mst_organization orga ON orga.orga_id = scau.scau_orga_id "
					+ "JOIN mst_location loca ON loca.loca_id = scau.scau_loca_id "
					+ "JOIN mst_department dept ON dept.dept_id = scau.scau_dept_id "
					+ "JOIN mst_user res_usr ON res_usr.user_id = scau.scau_responsible_person "
					+ "JOIN mst_user rep_usr ON rep_usr.user_id = scau.scau_reporting_person "
					+ "WHERE scau.scau_id = '" + scau_id + "' ";
			Query query = em.createNativeQuery(sql);
//			query.setParameter("scau_id", scau_id);
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

	// Method Written By: Harshad Padole
	// Method Purpose: get list of show cause notice action items
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<ShowCauseNoticeActionItem> getActionItemDetailsById(int scau_id) {
		try {
			TypedQuery query = em.createQuery(
					" from " + ShowCauseNoticeActionItem.class.getName() + " where tcau_scau_id = :scau_id",
					ShowCauseNoticeActionItem.class);
			query.setParameter("scau_id", scau_id);
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

	// Method Written By: Harshad Padole
	// Method Purpose: get list of documents
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<ShowCauseDocuments> getDocuments(int id, String type) {
		try {
			TypedQuery query = em.createQuery(" FROM " + ShowCauseDocuments.class.getName()
					+ " Where scnd_related_id =:id AND scnd_related_type=:type", ShowCauseDocuments.class);
			query.setParameter("id", id);
			query.setParameter("type", type);
			return (List<ShowCauseDocuments>) query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return null;
	}

	// Method Written By: Harshad Padole
	// Method Purpose: get documents details by id
	@SuppressWarnings("rawtypes")
	@Override
	public ShowCauseDocuments getDocDetails(int doc_id) {
		try {
			TypedQuery query = em.createQuery(
					" FROM " + ShowCauseDocuments.class.getName() + " WHERE scnd_id =:scnd_id",
					ShowCauseDocuments.class);
			query.setParameter("scnd_id", doc_id);
			return (ShowCauseDocuments) query.getResultList().get(0);
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
