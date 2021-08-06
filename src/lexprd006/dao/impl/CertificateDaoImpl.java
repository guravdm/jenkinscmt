package lexprd006.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lexprd006.dao.CertificateDao;
import lexprd006.domain.QuerterlyGeneratedCertificate;

/*
 * Author: Harshad Padole
 * Date: 22/05/2017
 * Purpose: Controller for certificate
 * 
 * 
 * 
 * */

@Repository(value = "certificateDao")
@Transactional
public class CertificateDaoImpl implements CertificateDao {

	@PersistenceContext
	private EntityManager em;

	public final SimpleDateFormat sdfIn = new SimpleDateFormat("dd-MM-yyyy");
	public final SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd");

	// Method Created By: Harshad Padole
	// Method Purpose: get non complied details
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getQuerterlyCertificateDetails(String json, HttpSession session) {
		try {
			String sql = "";
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);

			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");

			String from_date = jsonObject.get("date_from").toString();
			Date date = formatter.parse(from_date);

			String to_date = jsonObject.get("date_to").toString();
			Date date2 = formatter.parse(to_date);

			String fromDate = formatter1.format(date);
			String toDate = formatter1.format(date2);

			// Date from_date = sdfIn.parse(jsonObject.get("date_from").toString());
			// Date to_date = sdfIn.parse(jsonObject.get("date_to").toString());
			// int monthFrom = Integer.parseInt(jsonObject.get("from").toString());
			// int monthMiddel = monthFrom +1;
			// int monthTo = Integer.parseInt(jsonObject.get("to").toString());
			// int year = Integer.parseInt(jsonObject.get("year").toString());
			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			int role_id = Integer.parseInt(session.getAttribute("sess_role_id").toString());
			System.out.println(role_id);

			if (role_id == 3) {
				sql = "SELECT distinct ttrn.ttrn_id,ttrn.ttrn_client_task_id, tsk.task_legi_name, tsk.task_rule_name,  tsk.task_activity, "
						+ "tsk.task_impact, tsk.task_frequency,tsk.task_activity_who,tsk.task_activity_when, ttrn.ttrn_legal_due_date , "
						+ "ttrn.ttrn_status, ttrn.ttrn_performer_comments , ttrn.ttrn_reason_for_non_compliance, ttrn.ttrn_submitted_date, "
						+ "usrpr.user_first_name as prFirstNames , usrpr.user_last_name as rUsLastName, "
						+ "usrrw.user_first_name as rwFNames, usrrw.user_last_name as rwLastNames, usrfh.user_first_name as fhFirstsNames, usrfh.user_last_name as fhULSastNames "
						+ "FROM mst_task tsk JOIN cfg_task_user_mapping tmapp on tsk.task_id = tmapp.tmap_task_id "
						+ "JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND "
						+ "umapp.umap_loca_id = tmapp.tmap_loca_id AND umapp.umap_dept_id = tmapp.tmap_dept_id AND (tmapp.tmap_fh_user_id = '"
						+ user_id + "' ) "
						+ "JOIN trn_task_transactional ttrn on ttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
						+ "JOIN mst_user usrpr ON usrpr.user_id = ttrn.ttrn_performer_user_id "
						+ "JOIN mst_user usrrw ON usrrw.user_id = tmapp.tmap_rw_user_id "
						+ "JOIN mst_user usrfh ON usrfh.user_id = tmapp.tmap_fh_user_id "

						+ "WHERE (ttrn.ttrn_legal_due_date BETWEEN '" + fromDate + "' AND '" + toDate
						+ "') AND ttrn.ttrn_status = 'Active' ";

			} else if (role_id > 3) {
				sql = "SELECT distinct ttrn.ttrn_id,ttrn.ttrn_client_task_id, tsk.task_legi_name, tsk.task_rule_name,  "
						+ "tsk.task_activity, tsk.task_impact, tsk.task_frequency,tsk.task_activity_who,tsk.task_activity_when, "
						+ "ttrn.ttrn_legal_due_date , ttrn.ttrn_status, ttrn.ttrn_performer_comments , ttrn.ttrn_reason_for_non_compliance,"
						+ " ttrn.ttrn_submitted_date as subDate, usrpr.user_first_name as prFirstNames, usrpr.user_last_name as prLastNamess, "
						+ "usrrw.user_first_name as rwFNames, usrrw.user_last_name as rwLastsNam, "
						+ "usrfh.user_first_name as firstNamesFg, usrfh.user_last_name as fhLasNames "
						+ "FROM mst_task tsk JOIN cfg_task_user_mapping tmapp on tsk.task_id = tmapp.tmap_task_id "
						+ "JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id "
						+ "AND umapp.umap_loca_id = tmapp.tmap_loca_id AND umapp.umap_dept_id = tmapp.tmap_dept_id "
						+ "AND umapp.umap_user_id = '" + user_id + "' "
						+ "JOIN trn_task_transactional ttrn on ttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
						+ "JOIN mst_user usrpr ON usrpr.user_id = ttrn.ttrn_performer_user_id "
						+ "JOIN mst_user usrrw ON usrrw.user_id = tmapp.tmap_rw_user_id "
						+ "JOIN mst_user usrfh ON usrfh.user_id = tmapp.tmap_fh_user_id "
						+ "WHERE (ttrn.ttrn_legal_due_date BETWEEN '" + fromDate + "' AND '" + toDate
						+ "') AND ttrn.ttrn_status = 'Active' ";
			}

			// System.out.println("getQuerterlyCertificateDetails : " + sql);
			Query query = em.createNativeQuery(sql);
//			query.setParameter("user_id", user_id);
			System.out.println("Test Certificate : " + sql);
			// query.setParameter("from_date", from_date);
			// query.setParameter("to_date", to_date);
			// query.setParameter("year", year);
			// query.setParameter("monthFrom", monthFrom);
			// query.setParameter("monthMiddel", monthMiddel);
			// query.setParameter("monthTo", monthTo);

			List<T> certificateRecordList = query.getResultList();
			if (certificateRecordList != null) {
				return certificateRecordList;
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
	public <T> List<T> getQuerterlyShowCauseDetails(String json, HttpSession session) {
		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);

			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");

			String from_date = jsonObject.get("date_from").toString();
			Date date = formatter.parse(from_date);

			String to_date = jsonObject.get("date_to").toString();
			Date date2 = formatter.parse(to_date);

			String fromDate = formatter1.format(date);
			String toDate = formatter1.format(date2);
			// Date from_date = sdfIn.parse(jsonObject.get("date_from").toString());
			// Date to_date = sdfIn.parse(jsonObject.get("date_to").toString());
			/*
			 * int monthFrom = Integer.parseInt(jsonObject.get("from").toString()); int
			 * monthMiddel = monthFrom +1; int monthTo =
			 * Integer.parseInt(jsonObject.get("to").toString()); int year =
			 * Integer.parseInt(jsonObject.get("year").toString());
			 */
			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			String sql = "SELECT scau.scau_id,orga.orga_name,loca.loca_name,dept.dept_name,scau.scau_ralated_to, scau.scau_notice_date, "
					+ "scau.scau_received_date, scau.scau_deadline_date, scau.scau_next_action_item, scau.scau_status, usr1.user_first_name as fiNames, "
					+ "usr1.user_last_name u1, usr2.user_first_name u1FirstName, usr2.user_last_name as u2LastNames, "
					+ "scau.scau_status as statusS "
					+ "FROM mst_showcausenotice scau JOIN cfg_user_entity_mapping umapp ON scau.scau_orga_id = umapp.umap_orga_id AND "
					+ "scau.scau_loca_id = umapp.umap_loca_id AND scau.scau_dept_id = umapp.umap_dept_id AND umapp.umap_user_id = '"
					+ user_id + "' " + "JOIN mst_organization orga ON orga.orga_id = scau.scau_orga_id "
					+ "JOIN mst_location loca ON loca.loca_id = scau.scau_loca_id "
					+ "JOIN mst_department dept ON dept.dept_id = scau.scau_dept_id "
					+ "JOIN mst_user usr1 ON usr1.user_id = scau.scau_responsible_person "
					+ "JOIN mst_user usr2 ON usr2.user_id = scau.scau_reporting_person "
					// + "WHERE YEAR(scau_notice_date) =:year AND (MONTH(scau.scau_notice_date)
					// =:monthFrom OR MONTH(scau.scau_notice_date) =:monthMiddel OR
					// MONTH(scau.scau_notice_date) =:monthTo)"
					+ "WHERE (scau_notice_date BETWEEN '" + fromDate + "' AND '" + toDate + "') ";

			Query query = em.createNativeQuery(sql);
//			query.setParameter("user_id", user_id);
			// query.setParameter("from_date", from_date);
			// query.setParameter("to_date", to_date);
			/*
			 * query.setParameter("user_id", user_id); query.setParameter("year", year);
			 * query.setParameter("monthFrom", monthFrom); query.setParameter("monthMiddel",
			 * monthMiddel); query.setParameter("monthTo", monthTo);
			 */
			if (query.getResultList().size() > 0) {
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
	public List<Object> getQrtrLegRecords(String json, HttpSession session) {

		try {

			String sql = "";
			int role_id = Integer.parseInt(session.getAttribute("sess_role_id").toString());
			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());

			if (role_id == 3) {
				sql = "SELECT tsk.task_rule_name, tsk.task_legi_name "
						+ "FROM mst_task tsk JOIN cfg_task_user_mapping tmapp on tsk.task_id = tmapp.tmap_task_id "
						+ "JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND "
						+ "umapp.umap_dept_id = tmapp.tmap_dept_id AND (tmapp.tmap_fh_user_id = '" + user_id + "') "
						+ "JOIN trn_task_transactional ttrn on ttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
						+ "JOIN mst_user usrpr ON usrpr.user_id = ttrn.ttrn_performer_user_id "
						+ "WHERE ttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND task_state_name = 'Central' group by tsk.task_legi_name";

			} else if (role_id > 3) {

				sql = "SELECT tsk.task_rule_name, tsk.task_legi_name "
						+ "FROM mst_task tsk JOIN cfg_task_user_mapping tmapp on tsk.task_id = tmapp.tmap_task_id "
						+ "JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND "
						+ "umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = '" + user_id + "' "
						+ "JOIN trn_task_transactional ttrn on ttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
						+ "JOIN mst_user usrpr ON usrpr.user_id = ttrn.ttrn_performer_user_id "
						+ "WHERE ttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND task_state_name = 'Central' group by tsk.task_legi_name";

			}
			System.out.println("Get Legislation : " + sql);
			Query query = em.createNativeQuery(sql);
// 
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
	public void saveQuerterlyCertificate(QuerterlyGeneratedCertificate sc) {
		try {
			em.persist(sc);
			/* em.flush(); */
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> downloadCertificate(HttpSession session) {
		int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
		String sql = " select c.Querterly_certificate_certificatePath,  DATE_FORMAT(c.Querterly_certificate_created_at, '%d-%m-%Y') as Querterly_certificate_created_at, DATE_FORMAT(c.Querterly_certificate_from, '%d-%m-%Y') as Querterly_certificate_from, "
				+ "DATE_FORMAT(c.Querterly_certificate_to, '%d-%m-%Y') as Querterly_certificate_to, CONCAT(u.user_first_name, '  ', u.user_last_name) AS fNAME,  "
				+ "c.Querterly_certificate_orignalName from mst_querterlycertificate   c , mst_user u "
				+ "where c.Querterly_certificate_added_by = u.user_id AND c.Querterly_certificate_added_by = "
				+ user_id;
		// Query query = em.createQuery(" select c.Querterly_certificate_certificatePath
		// , c.Querterly_certificate_created_at, c.Querterly_certificate_year, CASE WHEN
		// c.Querterly_certificate_querter =1 THEN 'First' WHEN
		// c.Querterly_certificate_querter =4 THEN 'Second' WHEN
		// c.Querterly_certificate_querter =7 THEN 'Third' ELSE 'Fourth' END ,
		// CONCAT(u.user_first_name, ' ', u.user_last_name) AS fNAME from
		// "+QuerterlyGeneratedCertificate.class+" c , "+User.class+" u where
		// c.Querterly_certificate_added_by=u.user_id ");
		Query query = em.createNativeQuery(sql);
		// System.out.println("downloadCertificate sql: " + sql);
		return query.getResultList();
	}

	@Override
	public String getCertificatePath(String certificate) {
		// System.out.println("certificateName:" + certificate);
		String filepath = null;
		String sql = "select Querterly_certificate_certificatePath from mst_QuerterlyCertificate where Querterly_certificate_orignalName = "
				+ "'" + certificate + "'" + " limit 1";
		Query q = em.createNativeQuery(sql);
		// q.setParameter("certificateName", certificate);
		// System.out.println("getCertificatePath query : " + sql);
		filepath = q.getSingleResult().toString();
		return filepath;
	}

}
