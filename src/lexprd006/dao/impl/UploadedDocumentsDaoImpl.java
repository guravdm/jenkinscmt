package lexprd006.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lexprd006.dao.UploadedDocumentsDao;
import lexprd006.domain.UploadedDocuments;
import lexprd006.domain.UploadedSubTaskDocuments;

@Repository(value = "uploadedDocumentsDao")
@Transactional
public class UploadedDocumentsDaoImpl implements UploadedDocumentsDao {

	@PersistenceContext
	private EntityManager em;

	// Method Written By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Save Function To DB
	@Override
	public void saveDocuments(Object obj) {
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

	// Method Written By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Save Function To DB

//	@Cacheable(value = "getLastGeneratedValueByTtrnId", key = "#ttrn_id")
	@Override
	public int getLastGeneratedValueByTtrnId(int ttrn_id) {
		try {
			String sql = "SELECT MAX(udoc_last_generated_value_for_filename_for_ttrn_id) FROM "
					+ UploadedDocuments.class.getName() + " WHERE udoc_ttrn_id = :ttrn_id";
			Query query = em.createQuery(sql);
			query.setParameter("ttrn_id", ttrn_id);
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

	// Method Written By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Save Function To DB

	// @Cacheable(value = "documentByTtrnId", key = "#ttrn_id")
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<UploadedDocuments> getAllDocumentByTtrnId(int ttrn_id) {
		try {
			TypedQuery query = em.createQuery(
					" from " + UploadedDocuments.class.getName() + " where udoc_ttrn_id = :ttrn_id",
					UploadedDocuments.class);
			query.setParameter("ttrn_id", ttrn_id);
			if (!query.getResultList().isEmpty()) {
				return query.getResultList();
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

	// Method Written By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Save Function To DB
	@Override
	public String getProofFilePath(int udoc_id) {
		try {

			String updateDownloadStatus = "Update " + UploadedDocuments.class.getName()
					+ " set download_status = 1 where udoc_id =" + udoc_id + "";
			String sql = "SELECT udoc_filename FROM " + UploadedDocuments.class.getName() + " where udoc_id = :udoc_id";
			Query query = em.createQuery(sql);
			int executeUpdate = em.createQuery(updateDownloadStatus).executeUpdate();
			System.out.println("Download document Status  : " + executeUpdate);

			query.setParameter("udoc_id", udoc_id);

			if (!query.getResultList().isEmpty()) {
				if (query.getResultList().get(0) != null) {
					return query.getResultList().get(0).toString();
				}
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

	// Method Written By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Save Function To DB

//	@CacheEvict(value = "documentByTtrnId", key = "#ttrn_id")
	@Override
	public void deleteDocuments(int ttrn_id) {
		// TODO Auto-generated method stub
		try {
			String sql = "DELETE FROM trn_uploadeddocuments udoc WHERE udoc.udoc_ttrn_id =" + ttrn_id;
			Query query = em.createNativeQuery(sql);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

	}

	// Method Written By: Harshad Padole(17/04/2017)
	// Method Purpose: Delete single document bu udoc id
	@Override
	public void deleteDocument(int udoc_id) {
		try {
			UploadedDocuments d = em.find(UploadedDocuments.class, udoc_id);
			em.remove(d);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

	}

	@Override
	public int getLastGeneratedValueByTtrnSubId(int ttrn_sub_id) {

		try {
			String sql = "SELECT MAX(udoc_last_generated_value_for_filename_for_sub_task_ttrn_id) FROM "
					+ UploadedSubTaskDocuments.class.getName() + " WHERE udoc_sub_task_ttrn_id =:ttrn_sub_id";
			Query query = em.createQuery(sql);
			query.setParameter("ttrn_sub_id", ttrn_sub_id);
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

	// Method Written By: Harshad Padole
	// Method Purpose: Get sub task document list
	@Override
	public List<UploadedSubTaskDocuments> getAllDocumentBySubTtrnId(int sub_task_id) {
		try {
			TypedQuery<UploadedSubTaskDocuments> query = em.createQuery(
					"FROM " + UploadedSubTaskDocuments.class.getName() + " WHERE udoc_sub_task_ttrn_id=:sub_task_id",
					UploadedSubTaskDocuments.class);
			query.setParameter("sub_task_id", sub_task_id);
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
	public UploadedDocuments getDocById(int udoc_id) {
		try {
			@SuppressWarnings("rawtypes")
			TypedQuery query = (TypedQuery) em.createQuery(
					"FROM " + UploadedDocuments.class.getName() + " where udoc_id=:udoc_id ", UploadedDocuments.class);
			query.setParameter("udoc_id", udoc_id);
			return (UploadedDocuments) query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return null;
	}

//	@Cacheable(value = "getDocumentRepository", key = "{#user_id, #user_role_id}")
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getDocumentRepository(int user_id, int user_role_id) {
		try {
			String sql = "";

			if (user_role_id == 7) {
				sql = "SELECT DISTINCT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, "
						+ "tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, "
						+ "tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, "
						+ "tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive, "
						+ "tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, "
						+ "loca.loca_name, dept.dept_id , dept.dept_name, "
						+ "prusr.user_id as prUserId, prusr.user_first_name as prFNames, prusr.user_last_name as prLNames, "
						+ "rwusr.user_id as rwUserId, rwusr.user_first_name as rwFNames, rwusr.user_last_name as rwLastNames, "
						+ "tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, "
						+ "fhusr.user_id as fhUserId, fhusr.user_first_name as fhFNames, fhusr.user_last_name as fhLastNames, "
						+ "tttrn.ttrn_id,doc.udoc_id,doc.udoc_original_file_name " + "FROM cfg_task_user_mapping tmapp "
						+ "JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
						+ "JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id "
						+ "JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND "
						+ "umapp.umap_dept_id = tmapp.tmap_dept_id "
						+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
						+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
						+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
						+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
						+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
						+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
						+ "JOIN trn_uploadedDocuments doc ON tttrn.ttrn_id = doc.udoc_ttrn_id "
						+ "WHERE tmapp.tmap_enable_status = 1 " + "AND tttrn.ttrn_status = 'Completed' ";
			} else if (user_role_id > 2 && user_role_id <= 6) {
				sql = "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , "
						+ "tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, "
						+ "coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, tttrn.ttrn_created_at, "
						+ "tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive, "
						+ "tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , l"
						+ "oca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, "
						+ "prusr.user_id as prUserId, prusr.user_first_name as prFNames, prusr.user_last_name as prLastNames, "
						+ "rwusr.user_id as rwUserId, rwusr.user_first_name as rwFNames, rwusr.user_last_name as rwLNames, "
						+ "tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, "
						+ "fhusr.user_id as fhUserId, fhusr.user_first_name as fhFNames, fhusr.user_last_name as fhLastNames, "
						+ "tttrn.ttrn_id,doc.udoc_id,doc.udoc_original_file_name " + "FROM cfg_task_user_mapping tmapp "
						+ "JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
						+ "JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id "
						+ "JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND "
						+ "umapp.umap_loca_id = tmapp.tmap_loca_id AND umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = '"
						+ user_id + "' " + "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
						+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
						+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
						+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
						+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
						+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
						+ "JOIN trn_uploadedDocuments doc ON tttrn.ttrn_id = doc.udoc_ttrn_id "
						+ "WHERE tmapp.tmap_enable_status = 1 " + "AND tttrn.ttrn_status = 'Completed' ";
			} else {
				if (user_role_id == 2) {
					sql = "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, tttrn.ttrn_legal_due_date , "
							+ "tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, "
							+ "coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, "
							+ "tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task, "
							+ "tsk.task_prohibitive,tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , "
							+ "loca.loca_id, loca.loca_name, dept.dept_id , dept.dept_name, "
							+ "prusr.user_id as prUserId, prusr.user_first_name as prFNames, prusr.user_last_name as prLastNames, "
							+ "rwusr.user_id as rwUserId, rwusr.user_first_name as rwFNames, rwusr.user_last_name as rwLastNames, "
							+ "tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, "
							+ "fhusr.user_id as fhUserId, fhusr.user_first_name as fhFNames, fhusr.user_last_name as fhLastNames, "
							+ "tttrn.ttrn_id,doc.udoc_id,doc.udoc_original_file_name "
							+ "FROM cfg_task_user_mapping tmapp "
							+ "JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
							+ "JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id "
							+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
							+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
							+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
							+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
							+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
							+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
							+ "JOIN trn_uploadedDocuments doc ON tttrn.ttrn_id = doc.udoc_ttrn_id "
							+ "WHERE tmapp.tmap_enable_status = 1 " + "AND tttrn.ttrn_status = 'Completed' "
							+ "AND (tmapp.tmap_pr_user_id = '" + user_id + "' OR tmapp.tmap_rw_user_id = '" + user_id
							+ "')";
				} else {
					if (user_role_id == 1) {
						sql = "SELECT tmapp.tmap_client_tasks_id, tsk.task_legi_name, tsk.task_rule_name ,tttrn.ttrn_pr_due_date, "
								+ "tttrn.ttrn_legal_due_date , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, "
								+ "tsk.task_impact, coalesce(tttrn.ttrn_frequency_for_operation,tsk.task_frequency) as ttrn_frequency_for_operation, "
								+ "tttrn.ttrn_created_at, tsk.task_reference , tsk.task_cat_law_name , tsk.task_task_type_of_task,tsk.task_prohibitive, "
								+ "tsk.task_event,tsk.task_sub_event,tttrn.ttrn_status ,orga.orga_id, orga.orga_name , loca.loca_id, loca.loca_name, "
								+ "dept.dept_id , dept.dept_name, "
								+ "prusr.user_id as prUserId, prusr.user_first_name as prFNames, prusr.user_last_name as prLNames, "
								+ "rwusr.user_id as rwUserId, rwusr.user_first_name as rwFNames, rwusr.user_last_name as rwLNames, "
								+ "tsk.task_cat_law_id, tsk.task_legi_id, tsk.task_rule_id, "
								+ "fhusr.user_id as fhUserId, fhusr.user_first_name as fhFNames, fhusr.user_last_name as fhLastNames, "
								+ "tttrn.ttrn_id,doc.udoc_id,doc.udoc_original_file_name "
								+ "FROM cfg_task_user_mapping tmapp "
								+ "JOIN trn_task_transactional tttrn ON tmapp.tmap_client_tasks_id = tttrn.ttrn_client_task_id "
								+ "JOIN mst_task tsk on tmapp.tmap_task_id = tsk.task_id "
								+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
								+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
								+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
								+ "JOIN mst_user prusr on prusr.user_id = tmapp.tmap_pr_user_id "
								+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
								+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
								+ "JOIN trn_uploadedDocuments doc ON tttrn.ttrn_id = doc.udoc_ttrn_id "
								+ "WHERE tmapp.tmap_enable_status = 1 AND tttrn.ttrn_status = 'Completed' "
								+ "AND (tmapp.tmap_pr_user_id = '" + user_id + "' )";
					}
				}
			}

			System.out.println("In list all for document repository");
			Query query = em.createNativeQuery(sql);
//			if (user_role_id != 7) {
//				query.setParameter("user_id", user_id);
//				query.setParameter("user_id", user_id);
//			}

			System.out.println("SQL Query : " + sql);
			System.out.println("user ID : " + user_id);
			// query.setParameter("dept_id", dept_id);
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

//	@Cacheable(value = "documentByTtrnId", key = "#ttrn_id")
	@Override
	public UploadedDocuments getDocumentById(int ttrn_id) {
		try {
			// Query query = em.createQuery("select
			// udoc_ttrn_id,udoc_original_file_name,udoc_filename,udoc_last_generated_value_for_filename_for_ttrn_id
			// from trn_uploadedDocuments where udoc_ttrn_id= " +ttrn_id);
			TypedQuery query = (TypedQuery) em.createQuery(
					"FROM " + UploadedDocuments.class.getName() + " where udoc_ttrn_id=:udoc_ttrn_id ",
					UploadedDocuments.class);
			query.setParameter("udoc_ttrn_id", ttrn_id);
			// System.out.println("getDocumentById : " + ttrn_id);
			if (query.getResultList() != null) {
				return (UploadedDocuments) query.getResultList().get(0);
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

	// @CacheEvict(value = "documentByTtrnId", key = "documents.udoc_ttrn_id")
//	@CachePut(value = "documentByTtrnId", key = "documents.udoc_ttrn_id")
	@Override
	public void updateDocuments(UploadedDocuments documents) {
		try {
			em.merge(documents);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

}
