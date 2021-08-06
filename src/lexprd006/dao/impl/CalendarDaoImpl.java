package lexprd006.dao.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lexprd006.dao.CalendarDao;
import lexprd006.domain.SubTaskTranscational;
import lexprd006.service.UtilitiesService;

/*
 * Author: Mahesh Kharote
 * Date: 02/03/2016
 * Updated By:
 * Updated Date: 
 * 
 * */

@Repository(value = "calendarDao")
@Transactional
public class CalendarDaoImpl implements CalendarDao {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	UtilitiesService utilitiesService;

	// Method Created By: Mahesh Kharote
	// Method Purpose: Get User Id set in session
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getAllTasksAssignedToUserForCalendarWhereUserIsPerformer(int pr_id) {
		try {
			String sql = "SELECT DISTINCT trn_tt.ttrn_id,"
					+ "cfg_tum.tmap_client_tasks_id, trn_tt.ttrn_pr_due_date,trn_tt.ttrn_legal_due_date,trn_tt.ttrn_completed_date ,"
					+ "trn_tt.ttrn_status FROM trn_task_transactional as trn_tt JOIN cfg_task_user_mapping cfg_tum "
					+ "ON cfg_tum.tmap_client_tasks_id = trn_tt.ttrn_client_task_id "
					+ "WHERE (cfg_tum.tmap_pr_user_id = ? OR cfg_tum.tmap_rw_user_id = ?) AND trn_tt.ttrn_status != 'Inactive'";
			Query query = em.createQuery(sql);
			query.setParameter(1, pr_id);
			query.setParameter(2, pr_id);
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

	// Method Created By: Mahesh Kharote
	// Method Purpose: Get User Id set in session
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getAllTasksAssignedToUserForCalendarWhereUserIsReviwer(int rw_id) {
		try {
			String sql = "SELECT tt_tra.ttrn_id,cfg_tum.tmap_client_tasks_id, "
					+ "tt_tra.ttrn_rw_due_date, tt_tra.ttrn_status FROM trn_task_transactional as tt_tra "
					+ "JOIN cfg_task_user_mapping as cfg_tum ON cfg_tum.tmap_client_tasks_id = tt_tra.ttrn_client_task_id "
					+ "WHERE cfg_tum.tmap_rw_user_id = ?";
			Query query = em.createQuery(sql);
			query.setParameter(1, rw_id);
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

	// Method Created By: Mahesh Kharote
	// Method Purpose: Get User Id set in session
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getAllTasksOrgaLocaDeptWise(int user_id, int role_id) {
		System.out.println(" user_id : " + user_id + "\t role_id : " + role_id);
		try {
			if (role_id == 1) {
				String sqlPerformerAccess = "SELECT DISTINCT " + "trn_tt.ttrn_id," + "cfg_tum.tmap_client_tasks_id, "
						+ "trn_tt.ttrn_pr_due_date, " + "trn_tt.ttrn_legal_due_date," + "trn_tt.ttrn_submitted_date ,"
						+ "trn_tt.ttrn_status, " + "m_task.task_activity," + "cfg_tum.tmap_pr_user_id,"
						+ "trn_tt.ttrn_rw_due_date," + "trn_tt.ttrn_fh_due_date," + "trn_tt.ttrn_uh_due_date,"
						+ "cfg_tum.tmap_rw_user_id," + "trn_tt.ttrn_frequency_for_operation," + "cfg_tum.tmap_orga_id,"
						+ "cfg_tum.tmap_loca_id," + "cfg_tum.tmap_dept_id," + "trn_tt.ttrn_completed_date, "
						+ "cfg_tum.tmap_fh_user_id "
						+ "FROM trn_task_transactional as trn_tt join cfg_task_user_mapping as cfg_tum "
						+ "on cfg_tum.tmap_client_tasks_id = trn_tt.ttrn_client_task_id "
						+ "join mst_task as m_task on m_task.task_id = cfg_tum.tmap_task_id "
						+ "WHERE cfg_tum.tmap_pr_user_id = ? AND trn_tt.ttrn_status != 'Inactive' AND cfg_tum.tmap_enable_status != 0 ";
				Query queryPerformerAccess = em.createNativeQuery(sqlPerformerAccess);
				queryPerformerAccess.setParameter(1, user_id);
				return queryPerformerAccess.getResultList();
			}
			if (role_id == 2) {
				String sqlReviewerAccess = "SELECT DISTINCT " + "trn_tt.ttrn_id," + "cfg_tum.tmap_client_tasks_id, "
						+ "trn_tt.ttrn_pr_due_date," + "trn_tt.ttrn_legal_due_date," + "trn_tt.ttrn_submitted_date ,"
						+ "trn_tt.ttrn_status, " + "m_task.task_activity," + "cfg_tum.tmap_pr_user_id,"
						+ "trn_tt.ttrn_rw_due_date," + "trn_tt.ttrn_fh_due_date," + "trn_tt.ttrn_uh_due_date,"
						+ "cfg_tum.tmap_rw_user_id," + "trn_tt.ttrn_frequency_for_operation," + "cfg_tum.tmap_orga_id,"
						+ "cfg_tum.tmap_loca_id," + "cfg_tum.tmap_dept_id, " + "trn_tt.ttrn_completed_date, "
						+ "cfg_tum.tmap_fh_user_id " + "FROM trn_task_transactional as trn_tt "
						+ "JOIN cfg_task_user_mapping as cfg_tum "
						+ "ON cfg_tum.tmap_client_tasks_id = trn_tt.ttrn_client_task_id "
						+ "JOIN mst_task as m_task ON m_task.task_id = cfg_tum.tmap_task_id "
						+ "WHERE (cfg_tum.tmap_pr_user_id = ? OR cfg_tum.tmap_rw_user_id = ? )  AND "
						+ "trn_tt.ttrn_status != 'Inactive' AND cfg_tum.tmap_enable_status != 0 ";
				Query queryReviwerAccess = em.createNativeQuery(sqlReviewerAccess);
				queryReviwerAccess.setParameter(1, user_id);
				queryReviwerAccess.setParameter(2, user_id);
				return queryReviwerAccess.getResultList();
			}
			if (role_id == 3) {
				String sqlReviewerAccess = "SELECT DISTINCT " + "trn_tt.ttrn_id," + "cfg_tum.tmap_client_tasks_id, "
						+ "trn_tt.ttrn_pr_due_date," + "trn_tt.ttrn_legal_due_date," + "trn_tt.ttrn_submitted_date ,"
						+ "trn_tt.ttrn_status, " + "m_task.task_activity," + "cfg_tum.tmap_pr_user_id,"
						+ "trn_tt.ttrn_rw_due_date," + "trn_tt.ttrn_fh_due_date," + "trn_tt.ttrn_uh_due_date,"
						+ "cfg_tum.tmap_rw_user_id," + "trn_tt.ttrn_frequency_for_operation," + "cfg_tum.tmap_orga_id,"
						+ "cfg_tum.tmap_loca_id," + "cfg_tum.tmap_dept_id, " + "trn_tt.ttrn_completed_date, "
						+ "cfg_tum.tmap_fh_user_id " + "FROM trn_task_transactional as trn_tt "
						+ "JOIN cfg_task_user_mapping as cfg_tum "
						+ "ON cfg_tum.tmap_client_tasks_id = trn_tt.ttrn_client_task_id "
						+ "JOIN mst_task as m_task ON m_task.task_id = cfg_tum.tmap_task_id "
						+ "WHERE cfg_tum.tmap_pr_user_id = ? OR " + "cfg_tum.tmap_rw_user_id = ?  OR "
						+ "cfg_tum.tmap_fh_user_id = ?  AND "
						+ "trn_tt.ttrn_status != 'Inactive' AND cfg_tum.tmap_enable_status != 0 ";
				// System.out.println(sqlReviewerAccess);
				Query queryReviwerAccess = em.createNativeQuery(sqlReviewerAccess);
				queryReviwerAccess.setParameter(1, user_id);
				queryReviwerAccess.setParameter(2, user_id);
				queryReviwerAccess.setParameter(3, user_id);
				return queryReviwerAccess.getResultList();
			}
			if (role_id > 3 && role_id <= 6) {
				String sqlAccessQuery = "SELECT DISTINCT " + "trn_tt.ttrn_id," + "cfg_tum.tmap_client_tasks_id, "
						+ "trn_tt.ttrn_pr_due_date," + "trn_tt.ttrn_legal_due_date," + "trn_tt.ttrn_submitted_date ,"
						+ "trn_tt.ttrn_status, " + "m_task.task_activity," + "cfg_tum.tmap_pr_user_id,"
						+ "trn_tt.ttrn_rw_due_date," + "trn_tt.ttrn_fh_due_date," + "trn_tt.ttrn_uh_due_date,"
						+ "cfg_tum.tmap_rw_user_id," + "trn_tt.ttrn_frequency_for_operation," + "cfg_tum.tmap_orga_id,"
						+ "cfg_tum.tmap_loca_id," + "cfg_tum.tmap_dept_id," + "trn_tt.ttrn_completed_date, "
						+ "cfg_tum.tmap_fh_user_id "
						+ "FROM trn_task_transactional as trn_tt JOIN cfg_task_user_mapping as cfg_tum "
						+ "ON cfg_tum.tmap_client_tasks_id = trn_tt.ttrn_client_task_id "
						+ "JOIN mst_task as m_task ON m_task.task_id = cfg_tum.tmap_task_id "
						+ "JOIN cfg_user_entity_mapping as cfg_uem ON cfg_uem.umap_orga_id = cfg_tum.tmap_orga_id "
						+ "AND cfg_uem.umap_loca_id = cfg_tum.tmap_loca_id AND cfg_uem.umap_dept_id = cfg_tum.tmap_dept_id "
						+ "WHERE cfg_uem.umap_user_id = ? AND trn_tt.ttrn_status != 'Inactive' AND cfg_tum.tmap_enable_status != 0 ";
				Query queryForAccess = em.createNativeQuery(sqlAccessQuery);
				queryForAccess.setParameter(1, user_id);
				return queryForAccess.getResultList();
			}

			if (role_id == 7) {

				String sqlAccessQuery = "SELECT DISTINCT trn_tt.ttrn_id, cfg_tum.tmap_client_tasks_id, "
						+ "trn_tt.ttrn_pr_due_date, trn_tt.ttrn_legal_due_date," + "trn_tt.ttrn_submitted_date ,"
						+ "trn_tt.ttrn_status, m_task.task_activity," + "cfg_tum.tmap_pr_user_id,"
						+ "trn_tt.ttrn_rw_due_date, trn_tt.ttrn_fh_due_date, trn_tt.ttrn_uh_due_date,"
						+ "cfg_tum.tmap_rw_user_id, trn_tt.ttrn_frequency_for_operation," + "cfg_tum.tmap_orga_id,"
						+ "cfg_tum.tmap_loca_id, cfg_tum.tmap_dept_id," + "trn_tt.ttrn_completed_date, "
						+ "cfg_tum.tmap_fh_user_id "
						+ "FROM trn_task_transactional as trn_tt JOIN cfg_task_user_mapping as cfg_tum "
						+ "ON cfg_tum.tmap_client_tasks_id = trn_tt.ttrn_client_task_id "
						+ "JOIN mst_task as m_task ON m_task.task_id = cfg_tum.tmap_task_id "
						+ "JOIN cfg_user_entity_mapping as cfg_uem ON cfg_uem.umap_orga_id = cfg_tum.tmap_orga_id "
						+ "AND cfg_uem.umap_loca_id = cfg_tum.tmap_loca_id AND cfg_uem.umap_dept_id = cfg_tum.tmap_dept_id "
						+ "WHERE trn_tt.ttrn_status != 'Inactive' AND cfg_tum.tmap_enable_status != 0 ";
				Query queryForAccess = em.createNativeQuery(sqlAccessQuery);
				// queryForAccess.setParameter(1, user_id);
				System.out.println("Superadmin Calender : " + sqlAccessQuery);
				return queryForAccess.getResultList();

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

	// Method created : Harshad Padole on 14-04-2016
	// Method purpose : search task for calendar
	@SuppressWarnings("unchecked")
	@Override
	public String searchTaskForCalender(String json, int user_id, int user_role) {
		try {
			SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			@SuppressWarnings("unused")
			SimpleDateFormat sdfOut = new SimpleDateFormat("dd-MM-yyyy");

			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			DateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

			List<Integer> exist = new ArrayList<Integer>();
			JSONArray ListToSend = new JSONArray();
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);
			int orga_id = Integer.parseInt(jsonObject.get("org-a_id").toString());
			int loca_id = Integer.parseInt(jsonObject.get("loca_id").toString());
			int dept_id = Integer.parseInt(jsonObject.get("dept_id").toString());
			String freq = jsonObject.get("frequency").toString();

			String sql = " ";

			if (user_role == 1) {
				sql = "SELECT DISTINCT trn_tt.ttrn_id,cfg_tum.tmap_client_tasks_id, trn_tt.ttrn_pr_due_date, trn_tt.ttrn_legal_due_date,"
						+ "trn_tt.ttrn_completed_date ,trn_tt.ttrn_status,m_task.task_activity,cfg_tum.tmap_pr_user_id,trn_tt.ttrn_rw_due_date,"
						+ "trn_tt.ttrn_fh_due_date,trn_tt.ttrn_uh_due_date,cfg_tum.tmap_rw_user_id FROM trn_task_transactional as trn_tt "
						+ "JOIN cfg_task_user_mapping as cfg_tum ON cfg_tum.tmap_client_tasks_id = trn_tt.ttrn_client_task_id "
						+ "JOIN mst_task as m_task ON m_task.task_id = cfg_tum.tmap_task_id WHERE cfg_tum.tmap_orga_id ="
						+ orga_id;

				if (loca_id != 0) {
					sql += " AND cfg_tum.tmap_loca_id =" + loca_id;
				}
				if (dept_id != 0) {
					sql += " AND cfg_tum.tmap_dept_id =" + dept_id;
				}

				if (!freq.equals("NA")) {
					sql += " AND trn_tt.ttrn_frequency_for_operaion ='" + freq + "' ";
				}
				sql += " AND cfg_tum.tmap_pr_user_id =" + user_id;
			}
			if (user_role == 2) {
				sql = "SELECT DISTINCT trn_tt.ttrn_id,cfg_tum.tmap_client_tasks_id, trn_tt.ttrn_pr_due_date, trn_tt.ttrn_legal_due_date,"
						+ "trn_tt.ttrn_completed_date ,trn_tt.ttrn_status,m_task.task_activity,cfg_tum.tmap_pr_user_id,trn_tt.ttrn_rw_due_date,"
						+ "trn_tt.ttrn_fh_due_date,trn_tt.ttrn_uh_due_date,cfg_tum.tmap_rw_user_id FROM trn_task_transactional as trn_tt JOIN "
						+ "cfg_task_user_mapping as cfg_tum ON cfg_tum.tmap_client_tasks_id = trn_tt.ttrn_client_task_id JOIN mst_task as m_task ON "
						+ "m_task.task_id = cfg_tum.tmap_task_id WHERE cfg_tum.tmap_orga_id =" + orga_id;
				if (loca_id != 0) {
					sql += " AND cfg_tum.tmap_loca_id =" + loca_id;
				}
				if (dept_id != 0) {
					sql += " AND cfg_tum.tmap_dept_id =" + dept_id;
				}

				if (!freq.equals("NA")) {
					sql += " AND trn_tt.ttrn_frequency_for_operaion ='" + freq + "' ";
				}
				sql += " AND cfg_tum.tmap_pr_user_id =" + user_id + " OR cfg_tum.tmap_rw_user_id =" + user_id;
			}
			if (user_role == 3) {
				sql = "SELECT DISTINCT trn_tt.ttrn_id,cfg_tum.tmap_client_tasks_id, trn_tt.ttrn_pr_due_date, trn_tt.ttrn_legal_due_date,"
						+ "trn_tt.ttrn_completed_date ,trn_tt.ttrn_status,m_task.task_activity,cfg_tum.tmap_pr_user_id,trn_tt.ttrn_rw_due_date,"
						+ "trn_tt.ttrn_fh_due_date,trn_tt.ttrn_uh_due_date,cfg_tum.tmap_rw_user_id FROM trn_task_transactional as trn_tt JOIN "
						+ "cfg_task_user_mapping as cfg_tum ON cfg_tum.tmap_client_tasks_id = trn_tt.ttrn_client_task_id JOIN mst_task as m_task ON "
						+ "m_task.task_id = cfg_tum.tmap_task_id WHERE cfg_tum.tmap_orga_id =" + orga_id;
				if (loca_id != 0) {
					sql += " AND cfg_tum.tmap_loca_id =" + loca_id;
				}
				if (dept_id != 0) {
					sql += " AND cfg_tum.tmap_dept_id =" + dept_id;
				}

				if (!freq.equals("NA")) {
					sql += " AND trn_tt.ttrn_frequency_for_operaion ='" + freq + "' ";
				}
				sql += " AND cfg_tum.tmap_pr_user_id =" + user_id + " OR cfg_tum.tmap_rw_user_id =" + user_id
						+ " OR cfg_tum.tmap_fh_user_id =" + user_id;
			}
			if (user_role > 3) {
				sql = "SELECT DISTINCT trn_tt.ttrn_id,cfg_tum.tmap_client_tasks_id, "
						+ "trn_tt.ttrn_pr_due_date, trn_tt.ttrn_legal_due_date,"
						+ "trn_tt.ttrn_completed_date ,trn_tt.ttrn_status,m_task.task_activity,"
						+ "cfg_tum.tmap_pr_user_id,trn_tt.ttrn_rw_due_date,"
						+ "trn_tt.ttrn_fh_due_date,trn_tt.ttrn_uh_due_date,cfg_tum.tmap_rw_user_id "
						+ "FROM trn_task_transactional as trn_tt JOIN cfg_task_user_mapping as cfg_tum ON "
						+ "cfg_tum.tmap_client_tasks_id = trn_tt.ttrn_client_task_id JOIN "
						+ "cfg_user_entity_mapping as cfg_uem on cfg_uem.umap_orga_id = cfg_tum.tmap_orga_id "
						+ "AND cfg_uem.umap_loca_id = cfg_tum.tmap_loca_id AND "
						+ "cfg_uem.umap_dept_id = cfg_tum.tmap_dept_id AND " + "cfg_uem.umap_user_id =" + user_id
						+ " JOIN mst_task as m_task ON m_task.task_id = cfg_tum.tmap_task_id "
						+ "WHERE cfg_tum.tmap_orga_id =" + orga_id;
				if (loca_id != 0) {
					sql += " AND cfg_tum.tmap_loca_id =" + loca_id;
				}
				if (dept_id != 0) {
					sql += " AND cfg_tum.tmap_dept_id =" + dept_id;
				}

				if (!freq.equals("NA")) {
					sql += " AND trn_tt.ttrn_frequency_for_operaion ='" + freq + "' ";
				}
			}
			sql += " AND trn_tt.ttrn_status != 'Inactive'";
			// System.out.println("Calender search "+sql);
			Query query = em.createQuery(sql);
			List<Object> asPerUserAssignTask = query.getResultList();
			Iterator<Object> taskList = asPerUserAssignTask.iterator();
			while (taskList.hasNext()) {
				Object[] objects = (Object[]) taskList.next();
				JSONObject asPerTask = new JSONObject();

				if (exist.contains(Integer.parseInt(objects[0].toString()))) {
					// Do nothing
				} else {
					Date performerDate = sdfIn.parse(objects[2].toString());
					Date legalDueDate = sdfIn.parse(objects[3].toString());
					Date completedDate = null;
					Date rwDueDate = sdfIn.parse(objects[8].toString());

					int per_id_res = Integer.parseInt(objects[7].toString());
					int rev_id_res = Integer.parseInt(objects[11].toString());

					String dd = formatter1.format(formatter.parse(objects[2].toString())); // Change format for
					// performer due date
					Date per_due_date = formatter1.parse(dd);
					dd = formatter1.format(utilitiesService.getCurrentDate()); // Change format for current date
					Date cur_date = formatter1.parse(dd);

					if (user_role == 1) {
						asPerTask.put("calender_date", objects[2].toString()); // Set performer date

					}
					if (user_role == 2) {
						if (per_id_res == user_id) {
							if (rev_id_res == user_id && !cur_date.after(rwDueDate) && !cur_date.equals(per_due_date)) { // if
								// reviewer
								// and
								// current
								// date
								// not
								// after
								// reviewer
								// date
								asPerTask.put("calender_date", objects[8].toString()); // Set reviewer date
							} else {
								if (cur_date.after(per_due_date) && !cur_date.equals(per_due_date)) {
									asPerTask.put("calender_date", objects[8].toString()); // Set reviewer date
								} else {
									asPerTask.put("calender_date", objects[2].toString()); // Set performer date
								}
							}
						} else {
							asPerTask.put("calender_date", objects[8].toString()); // Set reviewer date
						}
					}
					if (user_role == 3) {
						if (per_id_res == user_id) {
							if (rev_id_res == user_id && !cur_date.after(rwDueDate) && !cur_date.equals(per_due_date)) { // if
								// reviewer
								// and
								// current
								// date
								// not
								// after
								// reviewer
								// date
								asPerTask.put("calender_date", objects[8].toString()); // Set reviewer date
							} else {
								if (cur_date.after(per_due_date) && !cur_date.equals(per_due_date)) {
									asPerTask.put("calender_date", objects[9].toString()); // Set FH date
								} else {
									asPerTask.put("calender_date", objects[2].toString()); // Set performer date
								}
							}
						} else {
							if (rev_id_res == user_id && !cur_date.after(rwDueDate)) { // if reviewer and current date
								// not after reviewer date
								asPerTask.put("calender_date", objects[8].toString()); // Set reviewer date
							} else {
								asPerTask.put("calender_date", objects[9].toString()); // Set FH date
							}
						}
					}
					if (user_role == 4) {
						if (per_id_res == user_id) {
							if (rev_id_res == user_id && !cur_date.after(rwDueDate) && !cur_date.equals(per_due_date)) { // if
								// reviewer
								// and
								// current
								// date
								// not
								// after
								// reviewer
								// date
								asPerTask.put("calender_date", objects[8].toString()); // Set reviewer date
							} else {
								if (cur_date.after(per_due_date) && !cur_date.equals(per_due_date)) {
									asPerTask.put("calender_date", objects[10].toString()); // Set UH date
								} else {
									asPerTask.put("calender_date", objects[2].toString()); // Set performer date
								}
							}

						} else {
							if (rev_id_res == user_id && !cur_date.after(rwDueDate)) { // if reviewer and current date
								// not after reviewer date
								asPerTask.put("calender_date", objects[8].toString()); // Set reviewer date
							} else {
								asPerTask.put("calender_date", objects[10].toString()); // Set UH date
							}
						}
					}
					if (user_role == 5) {
						if (per_id_res == user_id) {
							if (rev_id_res == user_id && !cur_date.after(rwDueDate) && !cur_date.equals(per_due_date)) { // if
								// reviewer
								// and
								// current
								// date
								// not
								// after
								// reviewer
								// date
								asPerTask.put("calender_date", objects[8].toString()); // Set reviewer date
							} else {
								if (cur_date.after(per_due_date) && !cur_date.equals(per_due_date)) {
									asPerTask.put("calender_date", objects[3].toString()); // Set legal date
								} else {
									asPerTask.put("calender_date", objects[2].toString()); // Set performer date
								}
							}
						} else {
							if (rev_id_res == user_id && !cur_date.after(rwDueDate) || cur_date.equals(rwDueDate)) { // if
								// reviewer
								// and
								// current
								// date
								// before
								// reviewer
								// date
								asPerTask.put("calender_date", objects[8].toString()); // Set reviewer date
							} else {
								asPerTask.put("calender_date", objects[3].toString()); // Set legal date
							}
						}
					}

					if (objects[4] != null) {
						completedDate = sdfIn.parse(objects[4].toString());
					}

					if (objects[5].toString().equals("Completed")) {
						if (completedDate.after(legalDueDate)) {
							asPerTask.put("ttrn_status", "Delayed");
						} else {
							asPerTask.put("ttrn_status", "Complied");
						}

					}
					if (objects[5].toString().equals("Active")) {
						if (utilitiesService.getCurrentDate().after(legalDueDate)) {
							asPerTask.put("ttrn_status", "Non Complied");
						} else {
							if (utilitiesService.getCurrentDate().after(performerDate)) {
								asPerTask.put("ttrn_status", "Posing Risk");
							} else {
								asPerTask.put("ttrn_status", "Pending");
							}

						}

					}
					if (objects[5].toString().equals("Inactive")) {
						asPerTask.put("ttrn_status", "Pending");

					}

					asPerTask.put("ttrn_id", objects[0]);
					asPerTask.put("tmap_client_tasks_id", objects[1]);
					asPerTask.put("ttrn_pr_due_date", objects[2].toString());
					// asPerTask.put("ttrn_legal_due_date", legalDueDate);
					if (completedDate != null) {
						// asPerTask.put("ttrn_completed_date", completedDate);
					} else {
						// asPerTask.put("ttrn_completed_date", "");
					}
					asPerTask.put("task_which_entity_wise", "Performer Wise");
					// asPerTask.put("ttrn_status", objects[5]);
					asPerTask.put("task_activity", objects[6].toString().replaceAll("\'", "").replaceAll("\"", ""));
					exist.add(Integer.parseInt(objects[0].toString()));
					ListToSend.add(asPerTask);
				}

			}

			/*
			 * String access =
			 * "SELECT DISTINCT ttrn_id,tmap_client_tasks_id, ttrn_pr_due_date, ttrn_legal_due_date,ttrn_completed_date ,ttrn_status,task_activity,tmap_pr_user_id,ttrn_rw_due_date,ttrn_fh_due_date,ttrn_uh_due_date,tmap_rw_user_id FROM trn_task_transactional JOIN cfg_task_user_mapping ON tmap_client_tasks_id = ttrn_client_task_id JOIN cfg_user_entity_mapping ON cfg_user_entity_mapping.umap_orga_id = cfg_task_user_mapping.tmap_orga_id AND cfg_user_entity_mapping.umap_loca_id = cfg_task_user_mapping.tmap_loca_id AND cfg_user_entity_mapping.umap_dept_id = cfg_task_user_mapping.tmap_dept_id AND cfg_user_entity_mapping.umap_user_id ="
			 * +user_id+" JOIN mst_task ON task_id = tmap_task_id WHERE cfg_task_user_mapping.tmap_orga_id ="
			 * +orga_id; if(loca_id != 0){ access +=
			 * " AND cfg_task_user_mapping.tmap_loca_id ="+loca_id; } if(dept_id != 0){
			 * access += " AND cfg_task_user_mapping.tmap_dept_id ="+dept_id ; } if(per_id
			 * != 0){ access += " AND tmap_pr_user_id ="+per_id ; } if(rev_id != 0){ access
			 * += " AND tmap_rw_user_id ="+rev_id ; } if(!freq.equals("NA")){ access +=
			 * " AND ttrn_frequency_for_operaion ='"+freq+"' " ; }
			 * 
			 * Query query2 = em.createNativeQuery(access); List<Object> asPerUserAccess =
			 * query2.getResultList(); Iterator<Object> AccesstaskList =
			 * asPerUserAccess.iterator(); while(AccesstaskList.hasNext()){ Object[] objects
			 * = (Object[]) AccesstaskList.next(); JSONObject jsonObject2 = new
			 * JSONObject(); if(exist.contains(Integer.parseInt(objects[0].toString()))){
			 * //Do nothing }else{
			 * 
			 * Date performerDate = sdfIn.parse(objects[2].toString()); Date legalDueDate =
			 * sdfIn.parse(objects[3].toString()); Date completedDate = null; Date rwDueDate
			 * = sdfIn.parse(objects[8].toString()); int per_id_res =
			 * Integer.parseInt(objects[7].toString()); int rev_id_res =
			 * Integer.parseInt(objects[11].toString()); String dd =
			 * formatter1.format(formatter.parse(objects[2].toString())); //Change format
			 * for performer due date Date per_due_date = formatter1.parse(dd); dd =
			 * formatter1.format(utilitiesService.getCurrentDate()); //Change format for
			 * current date Date cur_date = formatter1.parse(dd);
			 * 
			 * if(user_role==3){ if(per_id_res==user_id){ if(rev_id_res == user_id &&
			 * !cur_date.after(rwDueDate) && !cur_date.equals(per_due_date)){ //if reviewer
			 * and current date not after reviewer date jsonObject2.put("calender_date",
			 * objects[8].toString()); //Set reviewer date }else{
			 * if(cur_date.after(per_due_date) && !cur_date.equals(per_due_date)){
			 * jsonObject2.put("calender_date",objects[9].toString()); //Set FH date }else{
			 * jsonObject2.put("calender_date",objects[2].toString()); //Set performer date
			 * } } }else{ if(rev_id_res == user_id && !cur_date.after(rwDueDate)){ //if
			 * reviewer and current date not after reviewer date
			 * jsonObject2.put("calender_date", objects[8].toString()); //Set reviewer date
			 * }else{ jsonObject2.put("calender_date",objects[9].toString()); //Set FH date
			 * } } } if(user_role==4){ if(per_id_res==user_id){ if(rev_id_res == user_id &&
			 * !cur_date.after(rwDueDate) && !cur_date.equals(per_due_date)){ //if reviewer
			 * and current date not after reviewer date jsonObject2.put("calender_date",
			 * objects[8].toString()); //Set reviewer date }else{
			 * if(cur_date.after(per_due_date) && !cur_date.equals(per_due_date)){
			 * jsonObject2.put("calender_date",objects[10].toString()); //Set UH date }
			 * else{ jsonObject2.put("calender_date",objects[2].toString()); //Set performer
			 * date } }
			 * 
			 * }else{ if(rev_id_res == user_id && !cur_date.after(rwDueDate)){ //if reviewer
			 * and current date not after reviewer date jsonObject2.put("calender_date",
			 * objects[8].toString()); //Set reviewer date }else{
			 * jsonObject2.put("calender_date",objects[10].toString()); //Set UH date } } }
			 * if(user_role==5){ if(per_id_res==user_id){ if(rev_id_res == user_id &&
			 * !cur_date.after(rwDueDate) && !cur_date.equals(per_due_date)){ //if reviewer
			 * and current date not after reviewer date jsonObject2.put("calender_date",
			 * objects[8].toString()); //Set reviewer date }else{
			 * if(cur_date.after(per_due_date) && !cur_date.equals(per_due_date)){
			 * jsonObject2.put("calender_date",objects[3].toString()); //Set legal date
			 * }else{ jsonObject2.put("calender_date",objects[2].toString()); //Set
			 * performer date } } }else{ if(rev_id_res == user_id &&
			 * !cur_date.after(rwDueDate) || cur_date.equals(rwDueDate)){ //if reviewer and
			 * current date before reviewer date jsonObject2.put("calender_date",
			 * objects[8].toString()); //Set reviewer date }else{
			 * jsonObject2.put("calender_date",objects[3].toString()); //Set legal date } }
			 * }
			 * 
			 * if(objects[4] != null){ completedDate = sdfIn.parse(objects[4].toString()); }
			 * 
			 * if(objects[5].toString().equals("Completed")){
			 * if(completedDate.after(legalDueDate) ){ jsonObject2.put("ttrn_status",
			 * "Delayed"); } else{ jsonObject2.put("ttrn_status", "Complied"); }
			 * 
			 * } if(objects[5].toString().equals("Active")){
			 * if(utilitiesService.getCurrentDate().after(legalDueDate)){
			 * jsonObject2.put("ttrn_status", "Non Complied"); } else{
			 * if(utilitiesService.getCurrentDate().after(performerDate)){
			 * jsonObject2.put("ttrn_status", "Posing Risk"); } else{
			 * jsonObject2.put("ttrn_status", "Pending"); }
			 * 
			 * }
			 * 
			 * } if(objects[5].toString().equals("Inactive")){
			 * jsonObject2.put("ttrn_status", "Pending");
			 * 
			 * }
			 * 
			 * jsonObject2.put("ttrn_id", objects[0]);
			 * jsonObject2.put("tmap_client_tasks_id", objects[1]);
			 * jsonObject2.put("ttrn_pr_due_date", objects[2].toString());
			 * //jsonObject2.put("ttrn_legal_due_date", objects[3].toString());
			 * if(completedDate !=null){ //jsonObject2.put("ttrn_completed_date",
			 * completedDate); }else{ //jsonObject2.put("ttrn_completed_date", ""); }
			 * jsonObject2.put("task_which_entity_wise", "Performer Wise");
			 * jsonObject2.put("ttrn_id", objects[0]);
			 * jsonObject2.put("tmap_client_tasks_id", objects[1]);
			 * jsonObject2.put("ttrn_pr_due_date", objects[2].toString());
			 * jsonObject2.put("ttrn_legal_due_date", objects[3].toString());
			 * jsonObject2.put("ttrn_completed_date", objects[4].toString());
			 * jsonObject2.put("ttrn_status", objects[5]); jsonObject2.put("task_activity",
			 * objects[6].toString().replaceAll("\'", "").replaceAll("\"", ""));
			 * exist.add(Integer.parseInt(objects[0].toString()));
			 * ListToSend.add(jsonObject2); } }
			 */

			return ListToSend.toString();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return null;
	}

	// Method Created By: Harshad padole(5/7/2016)
	// Method Purpose: Get configured task Frequency from task_transactional
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getDistinctFrequencyForUser(HttpSession session) {
		try {
			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			int user_role = Integer.parseInt(session.getAttribute("sess_user_role").toString());
			String sql = "";
			if (user_role == 1) {
				sql = "SELECT DISTINCT trn_tt.ttrn_frequency_for_operaion FROM trn_task_transactional as trn_tt "
						+ "WHERE trn_tt.ttrn_client_task_id "
						+ "IN (SELECT cfg_tum.tmap_client_tasks_id FROM cfg_task_user_mapping as cfg_tum "
						+ "WHERE cfg_tum.tmap_pr_user_id = " + user_id + ")";
			}
			if (user_role == 2) {
				sql = "SELECT DISTINCT trn_tt.ttrn_frequency_for_operaion "
						+ "FROM trn_task_transactional as trn_tt WHERE "
						+ "trn_tt.ttrn_client_task_id IN(SELECT cfg_tum.tmap_client_tasks_id "
						+ "FROM cfg_task_user_mapping as cfg_tum WHERE cfg_tum.tmap_pr_user_id = " + user_id + " "
						+ "OR cfg_tum.tmap_rw_user_id =" + user_id + ")";
			}
			if (user_role > 2) {
				sql = "SELECT DISTINCT trn_tt.ttrn_frequency_for_operaion "
						+ "FROM trn_task_transactional as trn_tt WHERE trn_tt.ttrn_client_task_id "
						+ "IN(SELECT cfg_tum.tmap_client_tasks_id "
						+ "FROM cfg_task_user_mapping as cfg_tum JOIN cfg_user_entity_mapping cfg_uem "
						+ "on cfg_uem.umap_orga_id = cfg_tum.tmap_orga_id AND cfg_uem.umap_loca_id = cfg_tum.tmap_loca_id "
						+ "AND cfg_uem.umap_dept_id = cfg_tum.tmap_dept_id AND cfg_uem.umap_user_id =" + user_id + ")";
			}
			// System.out.println(sql);
			Query query = em.createQuery(sql);
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
	public <T> List<T> getAllSubTasksOrgaLocaDeptWise(int userId, int user_role) {
		try {
			if (user_role == 1) {
				String sqlPerformerAccess = "SELECT DISTINCT trn_stt.ttrn_sub_id, cfg_tum.tmap_client_tasks_id, "
						+ "trn_stt.ttrn_sub_task_pr_due_date, trn_stt.ttrn_sub_task_ENT_due_date, "
						+ "trn_stt.ttrn_sub_task_compl_date, trn_stt.ttrn_sub_task_status, m_task.task_activity, "
						+ "cfg_tum.tmap_pr_user_id, trn_stt.ttrn_sub_task_rw_date, trn_stt.ttrn_sub_task_FH_due_date, "
						+ "trn_stt.ttrn_sub_task_UH_due_date, cfg_tum.tmap_rw_user_id, m_task.task_frequency, cfg_tum.tmap_orga_id , cfg_tum.tmap_loca_id, cfg_tum.tmap_dept_id,trn_stt.ttrn_sub_task_id FROM "
						+ "trn_sub_task_transactional as trn_stt JOIN trn_task_transactional as trn_tt "
						+ "ON trn_stt.ttrn_sub_client_task_id = trn_tt.ttrn_client_task_id "
						+ "JOIN cfg_task_user_mapping as cfg_tum ON cfg_tum.tmap_client_tasks_id = trn_tt.ttrn_client_task_id "
						+ "JOIN mst_task as m_task ON m_task.task_id = cfg_tum.tmap_task_id WHERE cfg_tum.tmap_pr_user_id =:user_id "
						+ "AND trn_stt.ttrn_sub_task_status != 'Inactive'";

				Query queryPerformerAccess = em.createNativeQuery(sqlPerformerAccess);
				queryPerformerAccess.setParameter("user_id", userId);
				return queryPerformerAccess.getResultList();
			}
			if (user_role == 2) {
				String sqlReviewerAccess = "SELECT DISTINCT trn_stt.ttrn_sub_id, cfg_tum.tmap_client_tasks_id, "
						+ "trn_stt.ttrn_sub_task_pr_due_date, trn_stt.ttrn_sub_task_ENT_due_date, "
						+ "trn_stt.ttrn_sub_task_compl_date, trn_stt.ttrn_sub_task_status, m_task.task_activity, "
						+ "cfg_tum.tmap_pr_user_id, trn_stt.ttrn_sub_task_rw_date, trn_stt.ttrn_sub_task_FH_due_date, "
						+ "trn_stt.ttrn_sub_task_UH_due_date, cfg_tum.tmap_rw_user_id, m_task.task_frequency, cfg_tum.tmap_orga_id , cfg_tum.tmap_loca_id, cfg_tum.tmap_dept_id,trn_stt.ttrn_sub_task_id FROM "
						+ "trn_sub_task_transactional as trn_stt JOIN trn_task_transactional as trn_tt "
						+ "ON trn_stt.ttrn_sub_client_task_id = trn_tt.ttrn_client_task_id "
						+ "JOIN cfg_task_user_mapping as cfg_tum ON cfg_tum.tmap_client_tasks_id = trn_tt.ttrn_client_task_id "
						+ "JOIN mst_task as m_task ON m_task.task_id = cfg_tum.tmap_task_id WHERE (cfg_tum.tmap_pr_user_id =:user_id "
						+ "OR cfg_tum.tmap_rw_user_id =:user_id) AND trn_stt.ttrn_sub_task_status != 'Inactive'";

				Query queryReviwerAccess = em.createNativeQuery(sqlReviewerAccess);
				queryReviwerAccess.setParameter("user_id", userId);
				return queryReviwerAccess.getResultList();
			}
			if (user_role > 2) {
				String sqlAccessQuery = "SELECT DISTINCT trn_stt.ttrn_sub_id, cfg_tum.tmap_client_tasks_id, "
						+ "trn_stt.ttrn_sub_task_pr_due_date, trn_stt.ttrn_sub_task_ENT_due_date, "
						+ "trn_stt.ttrn_sub_task_compl_date, trn_stt.ttrn_sub_task_status, m_task.task_activity, "
						+ "cfg_tum.tmap_pr_user_id, trn_stt.ttrn_sub_task_rw_date, trn_stt.ttrn_sub_task_FH_due_date, "
						+ "trn_stt.ttrn_sub_task_UH_due_date, cfg_tum.tmap_rw_user_id, m_task.task_frequency, cfg_tum.tmap_orga_id , cfg_tum.tmap_loca_id, cfg_tum.tmap_dept_id,trn_stt.ttrn_sub_task_id FROM "
						+ "trn_sub_task_transactional as trn_stt JOIN trn_task_transactional as trn_tt "
						+ "ON trn_stt.ttrn_sub_client_task_id = trn_tt.ttrn_client_task_id "
						+ "JOIN cfg_task_user_mapping as cfg_tum ON cfg_tum.tmap_client_tasks_id = trn_tt.ttrn_client_task_id "
						+ "JOIN cfg_user_entity_mapping umap ON umap.umap_user_id =:user_id AND umap.umap_orga_id = cfg_tum.tmap_orga_id AND umap_loca_id = cfg_tum.tmap_loca_id AND umap_dept_id = cfg_tum.tmap_dept_id "
						+ "JOIN mst_task as m_task ON m_task.task_id = cfg_tum.tmap_task_id WHERE trn_stt.ttrn_sub_task_status != 'Inactive'";
				Query queryForAccess = em.createNativeQuery(sqlAccessQuery);
				queryForAccess.setParameter("user_id", userId);
				return queryForAccess.getResultList();
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
	public List<Object> getEntityList(HttpSession session) {

		int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
		int user_roleSuper = Integer.parseInt(session.getAttribute("sess_role_id").toString());
		// System.out.println("USER ROLE in getEntityList: " + user_roleSuper);

		try {
			if (user_roleSuper == 7) {
				String sql = "SELECT orga.orga_name, orga.orga_id, COUNT(umap.umap_orga_id) AS NumberOfOrders FROM cfg_user_entity_mapping umap "
						+ "LEFT JOIN mst_organization orga ON umap.umap_orga_id = orga.orga_id "
						+ "LEFT JOIN mst_user usr ON umap.umap_user_id = usr.user_id "
						+ "GROUP BY orga.orga_name,orga.orga_id";
				Query query = em.createNativeQuery(sql);
				return query.getResultList();
			}
			String sql = "SELECT orga.orga_name, orga.orga_id, COUNT(umap.umap_orga_id) AS NumberOfOrders FROM cfg_user_entity_mapping umap "
					+ "LEFT JOIN mst_organization orga ON umap.umap_orga_id = orga.orga_id "
					+ "LEFT JOIN mst_user usr ON umap.umap_user_id = usr.user_id " + "WHERE usr.user_id = " + user_id
					+ " GROUP BY orga.orga_name,orga.orga_id";
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
	public List<Object> getUnitList(String entity_id, HttpSession session) {
		String sql = "";

		try {
			// Superadmin users
			if (Integer.parseInt(session.getAttribute("sess_role_id").toString()) == 7) {
				// System.out.println("entity_id : " + entity_id);
				sql = "SELECT Distinct orga.orga_name, orga.orga_id,loca.loca_id, loca.loca_name FROM cfg_user_entity_mapping umap "
						+ "LEFT JOIN mst_organization orga ON umap.umap_orga_id = orga.orga_id "
						+ "LEFT JOIN mst_location loca ON umap.umap_loca_id = loca.loca_id "
						+ "LEFT JOIN mst_user usr ON umap.umap_user_id = usr.user_id " + "WHERE orga.orga_id = "
						+ entity_id + " and loca.loca_id IS NOT NULL and loca.loca_name IS NOT NULL";
			} else {
				int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());

				// System.out.println("user_id : " + user_id + "entity_id : " + entity_id);
				sql = "SELECT Distinct orga.orga_name, orga.orga_id,loca.loca_id, loca.loca_name FROM cfg_user_entity_mapping umap "
						+ "LEFT JOIN mst_organization orga ON umap.umap_orga_id = orga.orga_id "
						+ "LEFT JOIN mst_location loca ON umap.umap_loca_id = loca.loca_id "
						+ "LEFT JOIN mst_user usr ON umap.umap_user_id = usr.user_id " + "WHERE usr.user_id =" + user_id
						+ " and orga.orga_id = " + entity_id
						+ " and loca.loca_id IS NOT NULL and loca.loca_name IS NOT NULL ";
			}
			// System.out.println("Calender DaoIMPL Unit : " + sql);
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
	public List<Object> getFunctionList(String unit_id, String orga_id, HttpSession session) {
		int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
		try {

			if ((int) session.getAttribute("sess_role_id") == 7) {
				String sql = "SELECT distinct orga.orga_name, orga.orga_id,loca.loca_id, loca.loca_name,dept.dept_id,dept.dept_name  "
						+ "FROM cfg_user_entity_mapping umap "
						+ "LEFT JOIN mst_organization orga ON umap.umap_orga_id = orga.orga_id "
						+ "LEFT JOIN mst_location loca ON umap.umap_loca_id = loca.loca_id "
						+ "LEFT JOIN mst_department dept ON umap.umap_dept_id = dept.dept_id WHERE loca.loca_id = "
						+ unit_id + " and orga.orga_id = " + orga_id;
				Query query = em.createNativeQuery(sql);
				System.out.println("FOR SUPERADMIN : ---> " + sql);
				return query.getResultList();
			} else {
				String sql = "SELECT distinct orga.orga_name, orga.orga_id, loca.loca_id, loca.loca_name,dept.dept_id,dept.dept_name  "
						+ "FROM cfg_user_entity_mapping umap "
						+ "LEFT JOIN mst_organization orga ON umap.umap_orga_id = orga.orga_id "
						+ "LEFT JOIN mst_location loca ON umap.umap_loca_id = loca.loca_id "
						+ "LEFT JOIN mst_department dept ON umap.umap_dept_id = dept.dept_id "
						+ "WHERE umap.umap_user_id = " + user_id + " and loca.loca_id = " + unit_id
						+ " and orga.orga_id = " + orga_id;
				Query query = em.createNativeQuery(sql);
				System.out.println("normal usr sql : " + sql);
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getExeEvalFuncHeadList(int user_id, int user_role_id, int orga_id, int loca_id, int dept_id) {

		System.out.println("user_id : " + user_id + "\t user_role_id : " + user_role_id);
		try {
			String sql = "";

			if (user_role_id > 3) {
				sql = "SELECT tsk.task_legi_name, tsk.task_rule_name, tsk.task_legi_id, tsk.task_rule_id, tsk.task_event,"
						+ "tsk.task_sub_event, tmapp.tmap_client_tasks_id, tmapp.tmap_pr_user_id as prUserIds, orga.orga_id,loca.loca_id,dept.dept_id,"
						+ "tmapp.tmap_rw_user_id as prUsrId, usr.user_first_name as usFNames, usr.user_last_name as uLastNames, "
						+ "usrw.user_first_name as rwFiNames, usrw.user_last_name as rwLNames, tsk.task_cat_law_id, "
						+ "usrfh.user_id as fhUsrId, usrfh.user_first_name as fhFNamess, usrfh.user_last_name as fLastNames "
						+ " FROM cfg_task_user_mapping tmapp "
						+ "JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id JOIN mst_user usr "
						+ "ON usr.user_id = tmapp.tmap_pr_user_id "
						+ "JOIN mst_user usrw ON usrw.user_id = tmapp.tmap_rw_user_id "
						+ "JOIN mst_user usrfh ON usrfh.user_id = tmapp.tmap_fh_user_id "
						+ "JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id " + "JOIN mst_location loca "
						+ "ON loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id "
						+ "WHERE tmapp.tmap_enable_status = 1 ";

			} else if (user_role_id == 3) {
				sql = "SELECT tsk.task_legi_name, tsk.task_rule_name, tsk.task_legi_id, tsk.task_rule_id, tsk.task_event,"
						+ "tsk.task_sub_event, tmapp.tmap_client_tasks_id, tmapp.tmap_pr_user_id, orga.orga_id,loca.loca_id,dept.dept_id,"
						+ "tmapp.tmap_rw_user_id as rwUserId, usr.user_first_name as usrFNames, usr.user_last_name as usrLastNames, "
						+ "usrw.user_first_name as rwFNamess, usrw.user_last_name as rwLastNames, tsk.task_cat_law_id, "
						+ "usrfh.user_id as fhUSerId, usrfh.user_first_name as fhFNamess, usrfh.user_last_name as fhLAstnames "
						+ " FROM cfg_task_user_mapping tmapp JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id "
						+ "JOIN mst_user usr ON usr.user_id = tmapp.tmap_pr_user_id "
						+ "JOIN mst_user usrw ON usrw.user_id = tmapp.tmap_rw_user_id "
						+ "JOIN mst_user usrfh ON usrfh.user_id = tmapp.tmap_fh_user_id "
						+ "JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id "
						+ "JOIN mst_location loca ON loca.loca_id = tmapp.tmap_loca_id "
						+ "JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id "
						+ "WHERE tmapp.tmap_enable_status = 1" + "AND (tmapp.tmap_pr_user_id = '" + user_id
						+ "' OR tmapp.tmap_rw_user_id = '" + user_id + "' OR tmapp.tmap_fh_user_id = '" + user_id
						+ "')";

			} else if (user_role_id == 2) {
				sql = "SELECT tsk.task_legi_name, tsk.task_rule_name, tsk.task_legi_id, tsk.task_rule_id, tsk.task_event,"
						+ "tsk.task_sub_event, tmapp.tmap_client_tasks_id, tmapp.tmap_pr_user_id, orga.orga_id,loca.loca_id,dept.dept_id,"
						+ "tmapp.tmap_rw_user_id as rwUserId, usr.user_first_name as fNames, usr.user_last_name as usLastNames, "
						+ "usrw.user_first_name as wFNames, usrw.user_last_name as rwLastNames, tsk.task_cat_law_id, "
						+ "usrfh.user_id as fhUserId, usrfh.user_first_name as fhFirstNamess, usrfh.user_last_name as fhLastNames "
						+ " FROM cfg_task_user_mapping tmapp "
						+ "JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id "
						+ "JOIN mst_user usr  ON usr.user_id = tmapp.tmap_pr_user_id "
						+ "JOIN mst_user usrw ON usrw.user_id = tmapp.tmap_rw_user_id "
						+ "JOIN mst_user usrfh ON usrfh.user_id = tmapp.tmap_fh_user_id "
						+ "JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id "
						+ "JOIN mst_location loca ON loca.loca_id = tmapp.tmap_loca_id "
						+ "JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id "
						+ "WHERE tmapp.tmap_enable_status = 1 " + "AND (tmapp.tmap_pr_user_id = '" + user_id
						+ "' OR tmapp.tmap_rw_user_id = '" + user_id + "')";

			} else if (user_role_id == 1) {

				sql = "SELECT tsk.task_legi_name, tsk.task_rule_name, tsk.task_legi_id, tsk.task_rule_id, tsk.task_event,"
						+ "tsk.task_sub_event, tmapp.tmap_client_tasks_id, tmapp.tmap_pr_user_id, orga.orga_id,loca.loca_id,dept.dept_id,"
						+ "tmapp.tmap_rw_user_id as rwuserId, usr.user_first_name as usfIaNames, usr.user_last_name as usrLastNames, "
						+ "usrw.user_first_name as rwFNames, usrw.user_last_name as rwLastNamess, tsk.task_cat_law_id, "
						+ "usrfh.user_id as fhUserIds, usrfh.user_first_name as fhFFNames, usrfh.user_last_name as fhLastNames "
						+ " FROM cfg_task_user_mapping tmapp "
						+ "JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id "
						+ "JOIN mst_user usr ON usr.user_id = tmapp.tmap_pr_user_id "
						+ "JOIN mst_user usrw ON usrw.user_id = tmapp.tmap_rw_user_id "
						+ "JOIN mst_user usrfh ON usrfh.user_id = tmapp.tmap_fh_user_id "
						+ "JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id "
						+ "JOIN mst_location loca ON loca.loca_id = tmapp.tmap_loca_id "
						+ "JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id WHERE tmapp.tmap_enable_status = 1 "
						+ "AND (tmapp.tmap_pr_user_id = '" + user_id + "' )";
			}

			System.out.println("In list all for repository:" + sql);
			if (orga_id != 0) {
				sql = sql + " and orga.orga_id = " + orga_id;
			}
			if (loca_id != 0) {
				sql = sql + " and loca.loca_id = " + loca_id;
			}
			if (dept_id != 0) {
				sql = sql + " and dept.dept_id = " + dept_id;
			}
			Query query = em.createNativeQuery(sql);
			// if (user_role_id <= 3) {
			// query.setParameter("user_id", user_id);
			// }

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
	public List<SubTaskTranscational> getSubTaskHitoryByclientTaskID(String ttrn_sub_task_id) {
		try {
			TypedQuery query = em.createQuery(
					"FROM " + SubTaskTranscational.class.getName()
							+ " WHERE ttrn_sub_task_id=:ttrn_sub_task_id ORDER BY ttrn_sub_task_ENT_due_date DESC ",
					SubTaskTranscational.class);
			query.setParameter("ttrn_sub_task_id", ttrn_sub_task_id);
			if (!query.getResultList().isEmpty())
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
