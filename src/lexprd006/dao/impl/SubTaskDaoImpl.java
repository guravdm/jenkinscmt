package lexprd006.dao.impl;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lexprd006.dao.SubTaskDao;
import lexprd006.domain.SubTask;
import lexprd006.domain.SubTaskTranscational;
import lexprd006.domain.UploadedSubTaskDocuments;

@Repository(value = "subtaskdao")
@Transactional
public class SubTaskDaoImpl implements SubTaskDao {

	@PersistenceContext
	private EntityManager em;

	// Method Created By: Harshad Padole
	// Method Purpose: get user defined task
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getUserDefinedTask(HttpSession session) {
		try {
			String sql = "SELECT tmap.tmap_client_tasks_id, tsk.task_legi_id,tsk.task_legi_name,tsk.task_rule_id, tsk.task_rule_name, tsk.task_activity_who, tsk.task_activity_when, tsk.task_reference, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tsk.task_frequency, ttrn.ttrn_frequency_for_operation,orga.orga_id,orga.orga_name,loca.loca_id,loca.loca_name,dept.dept_id,dept.dept_name,prusr.user_id,prusr.user_first_name,prusr.user_last_name,rwusr.user_id,rwusr.user_first_name,rwusr.user_last_name "
					+ "FROM mst_task tsk JOIN cfg_task_user_mapping tmap ON tsk.task_id = tmap.tmap_task_id "
					+ "JOIN trn_task_transactional ttrn ON tmap.tmap_client_tasks_id = ttrn.ttrn_client_task_id "
					+ "JOIN mst_organization orga ON orga.orga_id = tmap.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmap.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmap.tmap_dept_id "
					+ "JOIN mst_user prusr ON prusr.user_id = tmap.tmap_pr_user_id "
					+ "JOIN mst_user rwusr ON rwusr.user_id = tmap.tmap_rw_user_id "
					+ "WHERE ttrn.ttrn_frequency_for_operation = 'User_Defined' ";
			Query query = em.createQuery(sql);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Harshad Padole
	// Method Purpose: Check whether task exist in table or not
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> List<T> checkTaskExist(String client_task_id, String equipment_number) {
		try {
			// String sql = "select * from mst_sub_task sub_task where
			// sub_task.sub_client_task_id =:client_task_id AND
			// sub_task.sub_equipment_number =:equipment_number";
			TypedQuery query = em.createQuery(
					" FROM " + SubTask.class.getName()
							+ " Where sub_client_task_id =:client_task_id AND sub_equipment_number=:equipment_number",
					SubTask.class);
			// Query query = em.createQuery(sql);
			query.setParameter("client_task_id", client_task_id);
			query.setParameter("equipment_number", equipment_number);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Harshad Padole
	// Method Purpose: Get last generated value for client task id
	@Override
	public int getLastGeneratedValue(String client_task_id) {
		try {
			String sql = "select max(sub_task.sub_last_generated_id) from mst_sub_task sub_task where sub_task.sub_client_task_id =:client_task_id ";
			Query query = em.createQuery(sql);
			query.setParameter("client_task_id", client_task_id);
			if (!query.getResultList().isEmpty()) {
				if (query.getResultList().get(0) != null) {
					return Integer.parseInt(query.getResultList().get(0).toString());
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void saveObject(Object object) {
		try {
			em.persist(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Method Created By: Harshad Padole
	// Method Purpose: Get all imported sub task from master table
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getImportedTask(HttpSession session) {
		try {
			String sql = "SELECT tmap.tmap_client_tasks_id, tsk.task_legi_id,tsk.task_legi_name,tsk.task_rule_id, tsk.task_rule_name, tsk.task_activity_who, tsk.task_activity_when, tsk.task_reference, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tsk.task_frequency,orga.orga_id,orga.orga_name,loca.loca_id,loca.loca_name,dept.dept_id,dept.dept_name,prusr.user_id,prusr.user_first_name,prusr.user_last_name,rwusr.user_id,rwusr.user_first_name,rwusr.user_last_name,sub_task.sub_equipment_type,sub_task.sub_equipment_number,sub_task.sub_equipment_location,sub_task.sub_equipment_description,sub_task.sub_frequency,sub_task.sub_last_examination_date,sub_task.sub_task_id "
					+ "FROM mst_task tsk JOIN cfg_task_user_mapping tmap ON tsk.task_id = tmap.tmap_task_id "
					+ "JOIN mst_organization orga ON orga.orga_id = tmap.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmap.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmap.tmap_dept_id "
					+ "JOIN mst_user prusr ON prusr.user_id = tmap.tmap_pr_user_id "
					+ "JOIN mst_user rwusr ON rwusr.user_id = tmap.tmap_rw_user_id "
					+ "JOIN mst_sub_task sub_task ON sub_task.sub_client_task_id = tmap.tmap_client_tasks_id ";
			Query query = em.createQuery(sql);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Harshad Padole
	// Method Purpose: Get task for configuration
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getTaskForConfiguration() {
		try {
			String sql = "SELECT orga.orga_id,orga.orga_name,loca.loca_id,loca.loca_name,dept.dept_id,dept.dept_name,prusr.user_id,prusr.user_first_name,prusr.user_last_name,rwusr.user_id,rwusr.user_first_name,rwusr.user_last_name,sub_task.sub_task_id,sub_task.sub_client_task_id,sub_task.sub_equipment_type,sub_task.sub_equipment_number,sub_task.sub_equipment_location,sub_task.sub_equipment_description,sub_task.sub_frequency,sub_task.sub_last_examination_date,tsk.task_legi_id,tsk.task_legi_name,tsk.task_rule_id,tsk.task_rule_name "
					+ "FROM mst_sub_task sub_task JOIN cfg_task_user_mapping tmap ON sub_task.sub_client_task_id = tmap.tmap_client_tasks_id "
					+ "JOIN mst_organization orga ON orga.orga_id = tmap.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmap.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmap.tmap_dept_id "
					+ "JOIN mst_user prusr ON prusr.user_id = tmap.tmap_pr_user_id "
					+ "JOIN mst_user rwusr ON rwusr.user_id = tmap.tmap_rw_user_id "
					+ "JOIN mst_task tsk ON tsk.task_id = tmap.tmap_task_id "
					+ "WHERE sub_task.sub_task_id NOT IN(SELECT DISTINCT ttrn_sub.ttrn_sub_task_id FROM trn_sub_task_transactional ttrn_sub) ";
			Query query = em.createQuery(sql);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Harshad Padole
	// Method Purpose: Get configured task
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getConfiguredTask(HttpSession session) {
		try {
			String sql = "SELECT orga.orga_id,orga.orga_name,loca.loca_id,loca.loca_name,dept.dept_id,dept.dept_name,prusr.user_id,prusr.user_first_name,prusr.user_last_name,rwusr.user_id,rwusr.user_first_name,rwusr.user_last_name,sub_task.sub_task_id,sub_task.sub_client_task_id,sub_task.sub_equipment_type,sub_task.sub_equipment_number,sub_task.sub_equipment_location,sub_task.sub_equipment_description,sub_task.sub_frequency,sub_task.sub_last_examination_date,tsk.task_legi_id,tsk.task_legi_name,tsk.task_rule_id,tsk.task_rule_name,ttrn_sub.ttrn_sub_id,ttrn_sub.ttrn_sub_task_status,ttrn_sub.tttn_sub_task_next_examination_date, ttrn_sub.ttrn_sub_task_alert_prior_day,ttrn_sub.ttrn_sub_task_buffer_days,ttrn_sub.ttrn_sub_task_ENT_due_date,ttrn_sub.ttrn_sub_task_FH_due_date,ttrn_sub.ttrn_sub_task_UH_due_date,ttrn_sub.ttrn_sub_task_rw_date,ttrn_sub.ttrn_sub_task_pr_due_date,ttrn_sub.ttrn_sub_task_allow_approver_reopening,ttrn_sub.ttrn_sub_task_historical,ttrn_sub.ttrn_sub_task_document,ttrn_sub.ttrn_sub_task_back_date_allowed,ttrn_sub.ttrn_sub_task_first_alert, ttrn_sub.ttrn_sub_task_second_alert,ttrn_sub.ttrn_sub_task_third_alert "
					+ "FROM mst_sub_task sub_task JOIN cfg_task_user_mapping tmap ON sub_task.sub_client_task_id = tmap.tmap_client_tasks_id "
					+ "JOIN mst_organization orga ON orga.orga_id = tmap.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmap.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmap.tmap_dept_id "
					+ "JOIN mst_user prusr ON prusr.user_id = tmap.tmap_pr_user_id "
					+ "JOIN mst_user rwusr ON rwusr.user_id = tmap.tmap_rw_user_id "
					+ "JOIN mst_task tsk ON tsk.task_id = tmap.tmap_task_id "
					+ "JOIN trn_sub_task_transactional ttrn_sub ON ttrn_sub.ttrn_sub_task_id = sub_task.sub_task_id AND ttrn_sub.ttrn_sub_task_status !='Completed'";
			Query query = em.createQuery(sql);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public SubTaskTranscational getTaskConfigurationById(int sub_id) {
		try {
			TypedQuery query = em.createQuery(
					"FROM " + SubTaskTranscational.class.getName() + " WHERE ttrn_sub_id=:sub_id",
					SubTaskTranscational.class);
			query.setParameter("sub_id", sub_id);
			return (SubTaskTranscational) query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void updateObject(Object object) {
		try {
			em.merge(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public SubTaskTranscational getSubTaskForCompletion(int ttrn_id) {
		try {
			TypedQuery<SubTaskTranscational> query = em.createQuery(
					" from " + SubTaskTranscational.class.getName() + " where ttrn_sub_id = :ttrn_id",
					SubTaskTranscational.class);
			query.setParameter("ttrn_id", ttrn_id);

			return (SubTaskTranscational) query.getResultList().get(0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void updateTaskConfiguration(SubTaskTranscational taskTransactional) {
		try {
			em.merge(taskTransactional);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public SubTask getTaskToChangeTheFrequency(String ttrn_sub_task_id) {
		try {
			TypedQuery<SubTask> query = em.createQuery(
					" from " + SubTask.class.getName() + " where sub_task_id = :ttrn_sub_task_id", SubTask.class);
			query.setParameter("ttrn_sub_task_id", ttrn_sub_task_id);
			return (SubTask) query.getResultList().get(0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void saveConfiguration(SubTask task) {
		try {
			em.merge(task);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getProofFilePath(int udoc_sub_id) {
		try {

			// set staus 1 if document is downloaded
			String updateDownloadStatus = "Update " + UploadedSubTaskDocuments.class.getName()
					+ " set download_status = 1 where udoc_sub_task_id =" + udoc_sub_id + "";

			// To download the document
			String sql = "SELECT udoc_sub_task_filename FROM " + UploadedSubTaskDocuments.class.getName()
					+ " where udoc_sub_task_id = :udoc_sub_id";
			Query query = em.createQuery(sql);

			int executeUpdate = em.createQuery(updateDownloadStatus).executeUpdate();

			System.out.println("Download document Status  : " + executeUpdate);

			query.setParameter("udoc_sub_id", udoc_sub_id);
			if (!query.getResultList().isEmpty()) {
				if (query.getResultList().get(0) != null) {
					return query.getResultList().get(0).toString();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void deleteTaskDocument(int udoc_sub_task_id) {
		try {
			UploadedSubTaskDocuments uploadedDocuments = em.find(UploadedSubTaskDocuments.class, udoc_sub_task_id);
			em.remove(uploadedDocuments);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteTaskHistory(int ttrn_sub_task_id) {
		try {
			SubTaskTranscational taskTransactional = em.find(SubTaskTranscational.class, ttrn_sub_task_id);
			em.remove(taskTransactional);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<UploadedSubTaskDocuments> getAllDocumentByTtrnSubId(int ttrn_id) {
		try {
			TypedQuery query = em.createQuery(
					" from " + UploadedSubTaskDocuments.class.getName() + " where udoc_sub_task_ttrn_id = :ttrn_id",
					UploadedSubTaskDocuments.class);
			query.setParameter("ttrn_id", ttrn_id);
			if (!query.getResultList().isEmpty()) {
				return query.getResultList();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void deleteDocument(int udoc_sub_task_id) {
		try {
			UploadedSubTaskDocuments d = em.find(UploadedSubTaskDocuments.class, udoc_sub_task_id);
			em.remove(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public SubTaskTranscational getTaskForCompletion(int ttrn_sub_id) {
		try {
			TypedQuery<SubTaskTranscational> query = em.createQuery(
					" from " + SubTaskTranscational.class.getName() + " where ttrn_sub_id = :ttrn_sub_id",
					SubTaskTranscational.class);
			query.setParameter("ttrn_sub_id", ttrn_sub_id);
			return (SubTaskTranscational) query.getResultList().get(0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void updateSubTaskConfiguration(SubTaskTranscational subTasktransactional) {
		try {
			em.merge(subTasktransactional);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getDocumentDownloadStatus(String ttrn_id) {

		// TODO Auto-generated method stub
		String status = "Failed";
		String sql = "SELECT COUNT(udoc_sub_task_ttrn_id), Sum(download_status) from trn_sub_task_uploadeddocuments where udoc_sub_task_ttrn_id ="
				+ ttrn_id + "";
		Query query = em.createNativeQuery(sql);
		List resultList = query.getResultList();

		Iterator iterator = resultList.iterator();
		while (iterator.hasNext()) {
			Object object[] = (Object[]) iterator.next();
			System.out.println("Sum of ttrn ID : " + object[0] + " Sum of Document : " + object[1]);

			if (Integer.parseInt(object[0].toString()) == 0
					|| object[0] == null && Integer.parseInt(object[1].toString()) == 0) {
				status = "Approved";
			} else if (Integer.parseInt(object[0].toString()) > 0 && Integer.parseInt(object[1].toString()) == 0) {
				status = "Failed";
			} else if (Integer.parseInt(object[0].toString()) == Integer.parseInt(object[1].toString())) {
				status = "Approved";
			}
		}

		return status;

	}

}
