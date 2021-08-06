package lexprd006.dao.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lexprd006.dao.AuditTasksDAO;
import lexprd006.domain.TaskTransactional;
import lexprd006.domain.TaskUserMapping;
import lexprd006.domain.UploadedPODDocuments;

@Repository
@Transactional
public class AuditTasksDAOImpl implements AuditTasksDAO {

	@PersistenceContext
	private EntityManager em;

	public final SimpleDateFormat sdfIn = new SimpleDateFormat("dd-MM-yyyy");
	public final SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd");

//	@Cacheable(value = "auditTasksComplianceGraph", key = "{#user_id, #user_role_id}")
	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public <T> List<T> getMonthlyComplianceStatus(int user_id, int user_role_id, String jsonString) {

		LocalDate localDate = LocalDate.now();
		// Previous 60 days data
		LocalDate minusDays;
		// Next 30 days data will be loading
		LocalDate plusDays;

		// //System.out.println("dashboard compliance user_role_id : " + user_role_id);

		try {

			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String orga_id = jsonObj.get("orga_id") != null ? jsonObj.get("orga_id").toString() : "0";
			String loca_id = jsonObj.get("loca_id") != null ? jsonObj.get("loca_id").toString() : "0";
			String dept_id = jsonObj.get("dept_id") != null ? jsonObj.get("dept_id").toString() : "0";
			String date_from = jsonObj.get("date_from").toString();
			String date_to = jsonObj.get("date_to").toString();

			// //System.out.println(date_from.length());

			if (!date_from.equalsIgnoreCase("''") && date_from != "''" && date_from.length() > 0 && date_from != null) {
				LocalDate frmDate = LocalDate.parse(date_from);
				LocalDate tDate = LocalDate.parse(date_to);
				plusDays = tDate;
				minusDays = frmDate;
			} else {
				minusDays = localDate.minusDays(30);
				plusDays = localDate.plusDays(30);
			}

			String sql = "";
			//// System.out.println(user_id + " id " + user_role_id);
			if (user_role_id > 3 && user_role_id <= 6 || user_role_id == 12) {
				/**
				 * #Auditor Role user_role_id == 12
				 */

				/**
				 * Only Complied count sql call
				 */

				sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, "
						+ "tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date,tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, "
						+ "tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, "
						+ "dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, "
						+ "tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id , "
						+ "usr.user_first_name, usr.user_last_name,tttrn.ttrn_completed_date as c1,tsk.task_cat_law_name,tttrn.ttrn_performer_comments as pc1, "
						+ "tttrn.ttrn_reason_for_non_compliance, tsk.task_implication, rwusr.user_first_name as rwFirstNames , rwusr.user_last_name as rwLastNames, "
						+ "fhusr.user_first_name as fhFirstNames, fhusr.user_last_name as fhLastNames, tttrn.ttrn_completed_date, tttrn.ttrn_performer_user_id, tttrn.ttrn_performer_comments "
						+ "FROM trn_task_transactional tttrn "
						+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
						+ " LEFT JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND "
						+ "umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = '" + user_id + "'  "
						+ " LEFT JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
						+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
						+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
						+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
						+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
						+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
						+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
						+ "WHERE tttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND "
						+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
						+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays
						+ "' AND (tttrn.ttrn_is_Task_Audited = 'No' OR (tttrn.ttrn_is_Task_Audited = 'Yes' and tttrn.auditorStatus = 'Re_Opened' OR tttrn.auditorStatus = 'NonComplied' )) ";
//						+ "AND tttrn.ttrn_is_Task_Audited = 'No'  ";

				if (!orga_id.equalsIgnoreCase("0")) {
					sql += " AND orga.orga_id = '" + orga_id + "' ";
				}

				if (!loca_id.equalsIgnoreCase("0")) {
					sql += " AND loca.loca_id = '" + loca_id + "' ";
				}

				if (!dept_id.equalsIgnoreCase("0")) {
					sql += " AND dept.dept_id = '" + dept_id + "' ";
				}

				// System.out.println("user_role_id > 3 && user_role_id <= 6 || user_role_id ==
				// 12 sql : " + sql);
			} else {
				if (user_role_id == 3) {

					sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, "
							+ "tttrn.ttrn_fh_due_date,tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, "
							+ "orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, "
							+ "tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, "
							+ "tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id , usr.user_first_name, usr.user_last_name, "
							+ "tttrn.ttrn_completed_date  as c1,tsk.task_cat_law_name,tttrn.ttrn_performer_comments as pc1, tttrn.ttrn_reason_for_non_compliance, "
							+ "tsk.task_implication, rwusr.user_first_name as rwFirstNames , rwusr.user_last_name as rwLastNames, fhusr.user_first_name as fhFirstNames, fhusr.user_last_name as fhLastNames, "
							+ " tttrn.ttrn_completed_date, tttrn.ttrn_performer_user_id, tttrn.ttrn_performer_comments "
							+ "FROM trn_task_transactional tttrn "
							+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
							+ " LEFT JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
							+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
							+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
							+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
							+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
							+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
							+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id " + "WHERE "
							+ "(tmapp.tmap_pr_user_id = '" + user_id + "' OR  tmapp.tmap_rw_user_id = '" + user_id
							+ "' OR  tmapp.tmap_fh_user_id = '" + user_id + "') " + "AND "
							+ "tttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND "
							+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
							+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays
							+ "' AND (tttrn.ttrn_is_Task_Audited = 'No' OR (tttrn.ttrn_is_Task_Audited = 'Yes' and tttrn.auditorStatus = 'Re_Opened'  OR tttrn.auditorStatus = 'NonComplied' )) ";
//							+ "AND  tttrn.ttrn_is_Task_Audited = 'No' ";

					if (!orga_id.equalsIgnoreCase("0")) {
						sql += " AND orga.orga_id = '" + orga_id + "' ";
					}

					if (!loca_id.equalsIgnoreCase("0")) {
						sql += " AND loca.loca_id = '" + loca_id + "' ";
					}

					if (!dept_id.equalsIgnoreCase("0")) {
						sql += " AND dept.dept_id = '" + dept_id + "' ";
					}

					// System.out.println("user_role_id == 3 : " + sql);
				} else {
					if (user_role_id == 2) {
						sql = "SELECT distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, "
								+ "tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date,tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, "
								+ "tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name , "
								+ "tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, "
								+ "tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id , "
								+ "usr.user_first_name, usr.user_last_name,tttrn.ttrn_completed_date  as c1,tsk.task_cat_law_name, "
								+ "tttrn.ttrn_performer_comments as pc1, tttrn.ttrn_reason_for_non_compliance,tsk.task_implication, rwusr.user_first_name as rwFirstNames , "
								+ "rwusr.user_last_name as rwLastNames, fhusr.user_first_name as fhFirstNames, fhusr.user_last_name as fhLastNames, "
								+ " tttrn.ttrn_completed_date, tttrn.ttrn_performer_user_id, tttrn.ttrn_performer_comments "
								+ "FROM trn_task_transactional tttrn "
								+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
								+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
								+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
								+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
								+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
								+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
								+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
								+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id " + "WHERE "
								+ "(tmapp.tmap_pr_user_id = '" + user_id + "' OR  tmapp.tmap_rw_user_id = '" + user_id
								+ "') " + "AND "
								+ "tttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND "
								+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
								+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays
								+ "' AND (tttrn.ttrn_is_Task_Audited = 'No' OR (tttrn.ttrn_is_Task_Audited = 'Yes' and tttrn.auditorStatus = 'Re_Opened'  OR tttrn.auditorStatus = 'NonComplied' )) ";
//								+ "AND tttrn.ttrn_is_Task_Audited = 'No' ";

						if (!orga_id.equalsIgnoreCase("0")) {
							sql += " AND orga.orga_id = '" + orga_id + "' ";
						}

						if (!loca_id.equalsIgnoreCase("0")) {
							sql += " AND loca.loca_id = '" + loca_id + "' ";
						}

						if (!dept_id.equalsIgnoreCase("0")) {
							sql += " AND dept.dept_id = '" + dept_id + "' ";
						}
						// System.out.println("user_role_id == 2 : " + sql);
					} else {
						if (user_role_id == 1) {
							sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, "
									+ "tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date,tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, "
									+ "tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, "
									+ "dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, "
									+ "tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, "
									+ "tttrn.ttrn_frequency_for_operation, usr.user_id , usr.user_first_name, usr.user_last_name, "
									+ "tttrn.ttrn_completed_date  as c1,tsk.task_cat_law_name,tttrn.ttrn_performer_comments as pc1 , "
									+ "tttrn.ttrn_reason_for_non_compliance,tsk.task_implication, rwusr.user_first_name as rwFirstNames , rwusr.user_last_name as rwLastNames,"
									+ " fhusr.user_first_name as fhFirstNames, fhusr.user_last_name as fhLastNames, tttrn.ttrn_completed_date, tttrn.ttrn_performer_user_id, tttrn.ttrn_performer_comments "
									+ "FROM trn_task_transactional tttrn "
									+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
									+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
									+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
									+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
									+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
									+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
									+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
									+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id " + "WHERE "
									+ "(tmapp.tmap_pr_user_id = '" + user_id + "') " + "AND "
									+ "tttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND "
									+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
									+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays
									+ "' AND (tttrn.ttrn_is_Task_Audited = 'No' OR (tttrn.ttrn_is_Task_Audited = 'Yes' and tttrn.auditorStatus = 'Re_Opened'  OR tttrn.auditorStatus = 'NonComplied' )) ";
//									+ "AND tttrn.ttrn_is_Task_Audited = 'No' ";

							if (!orga_id.equalsIgnoreCase("0")) {
								sql += " AND orga.orga_id = '" + orga_id + "' ";
							}

							if (!loca_id.equalsIgnoreCase("0")) {
								sql += " AND loca.loca_id = '" + loca_id + "' ";
							}

							if (!dept_id.equalsIgnoreCase("0")) {
								sql += " AND dept.dept_id = '" + dept_id + "' ";
							}

							// System.out.println("user_role_id == 1 : " + sql);
						} else {
							if (user_role_id == 7) {

								sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, "
										+ "tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date,tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, "
										+ "tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, "
										+ "dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, "
										+ "tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, "
										+ "tttrn.ttrn_frequency_for_operation, usr.user_id , usr.user_first_name, usr.user_last_name, "
										+ "tttrn.ttrn_completed_date  as c1,tsk.task_cat_law_name,tttrn.ttrn_performer_comments as pc1, "
										+ "tttrn.ttrn_reason_for_non_compliance, tsk.task_implication, rwusr.user_first_name as rwFirstNames , rwusr.user_last_name as rwLastNames, "
										+ "fhusr.user_first_name as fhFirstNames, fhusr.user_last_name as fhLastNames, tttrn.ttrn_completed_date, tttrn.ttrn_performer_user_id, tttrn.ttrn_performer_comments "
										+ "FROM trn_task_transactional tttrn "
										+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
										+ " JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND "
										+ "umapp.umap_loca_id = tmapp.tmap_loca_id AND umapp.umap_dept_id = tmapp.tmap_dept_id "
										+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
										+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
										+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
										+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
										+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
										+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
										+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
										+ "WHERE tttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND "
										+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
										+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays
										+ "' AND (tttrn.ttrn_is_Task_Audited = 'No' OR (tttrn.ttrn_is_Task_Audited = 'Yes' and tttrn.auditorStatus = 'Re_Opened'  OR tttrn.auditorStatus = 'NonComplied' )) ";
//										+ "AND tttrn.ttrn_is_Task_Audited = 'No' ";

								if (!orga_id.equalsIgnoreCase("0")) {
									sql += " AND orga.orga_id = '" + orga_id + "' ";
								}

								if (!loca_id.equalsIgnoreCase("0")) {
									sql += " AND loca.loca_id = '" + loca_id + "' ";
								}

								if (!dept_id.equalsIgnoreCase("0")) {
									sql += " AND dept.dept_id = '" + dept_id + "' ";
								}

								// System.out.println("user_role_id == 7 : " + sql);
								// System.out.println("Inside Superadmin access");
							}
						}
					}
				}
			}

			// System.out.println("Main Task Query: " + sql);
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
	public <T> List<T> getHeadCountsByLocation(HttpSession session, HttpServletResponse res) {
		List<T> resultList = null;
		try {
			String sql = "SELECT loca.loca_name, COUNT(CASE WHEN UPPER(Gender) = 'MALE' THEN 1 END) Male, COUNT(CASE WHEN UPPER(Gender) = 'FEMALE' THEN 1 END) Female, COUNT(CASE WHEN Gender IS NULL THEN 1 END) 'Not Assigned',  COUNT(user_id) AS 'Total Employee'  FROM mst_user usr JOIN cfg_user_entity_mapping umap ON usr.user_id = umap.umap_user_id JOIN  mst_location loca on loca.loca_id = umap.umap_loca_id group by loca_name ";
			Query createNativeQuery = em.createNativeQuery(sql);
			resultList = createNativeQuery.getResultList();
			if (resultList.size() > 0)
				return resultList;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> searchAuditRepository(String jsonString, int user_id, int user_role_id) {

		LocalDate localDate = LocalDate.now();
		// Previous 60 days data
		LocalDate minusDays = localDate.minusDays(30);
		// Next 30 days data will be loading
		LocalDate plusDays = localDate.plusDays(30);

		// System.out.println("dashboard compliance user_role_id : " + user_role_id);

		JsonNode rootNode = null;

		try {
			final ObjectMapper mapper = new ObjectMapper();
			rootNode = mapper.readTree(jsonString);
			Integer entityId = rootNode.path("orga_id").asInt();
			// System.out.println("entityName:" + entityId);
			Integer unitId = rootNode.path("loca_id").asInt();
			Integer functionId = rootNode.path("dept_id").asInt();
			String fromDate = rootNode.path("date_from").toString();
			String toDate = rootNode.path("date_to").toString();

			// System.out.println("fromDate : " + fromDate);

			String sql = "";
			// //System.out.println(user_id +" id " +user_role_id );
			if (user_role_id > 3 && user_role_id <= 6 || user_role_id == 12) {
				/**
				 * #Auditor Role user_role_id == 12
				 */

				sql = "SELECT DISTINCT tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status, tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date, tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, tsk.task_legi_name, tsk.task_rule_name, tsk.task_reference, tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id, usr.user_first_name, usr.user_last_name, tttrn.ttrn_completed_date  as c1, tsk.task_cat_law_name, tttrn.ttrn_performer_comments as pc1, tttrn.ttrn_reason_for_non_compliance, tsk.task_implication, rwusr.user_first_name as rwFirstNames , rwusr.user_last_name as rwLastNames, fhusr.user_first_name as fhFirstNames, fhusr.user_last_name as fhLastNames FROM trn_task_transactional tttrn LEFT JOIN cfg_task_user_mapping tmapp ON tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id JOIN cfg_user_entity_mapping umapp ON umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = '"
						+ user_id
						+ "' JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id JOIN mst_user usr ON usr.user_id = tmapp.tmap_pr_user_id JOIN mst_user rwusr ON rwusr.user_id = tmapp.tmap_rw_user_id JOIN mst_user fhusr ON fhusr.user_id = tmapp.tmap_fh_user_id WHERE tttrn.ttrn_status != 'Inactive' AND tttrn.ttrn_status = 'Completed' AND tttrn.ttrn_legal_due_date > tttrn.ttrn_submitted_date OR tttrn.ttrn_legal_due_date = tttrn.ttrn_submitted_date AND tmapp.tmap_enable_status != 0 AND tttrn.ttrn_frequency_for_operation != 'User_Defined' AND tsk.task_cat_law_name != 'Internal' AND tttrn.ttrn_legal_due_date BETWEEN '"
						+ minusDays + "' AND '" + plusDays + "' ";

				// System.out.println("sql : " + sql);
			} else {
				if (user_role_id == 3) {

					sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date,tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id , usr.user_first_name, usr.user_last_name,tttrn.ttrn_completed_date  as c1,tsk.task_cat_law_name,tttrn.ttrn_performer_comments as pc1, tttrn.ttrn_reason_for_non_compliance, tsk.task_implication, rwusr.user_first_name as rwFirstNames , rwusr.user_last_name as rwLastNames, fhusr.user_first_name as fhFirstNames, fhusr.user_last_name as fhLastNames "
							+ "FROM trn_task_transactional tttrn "
							+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
							+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
							+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
							+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
							+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
							+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
							+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
							+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id " + "WHERE "
							+ "(tmapp.tmap_pr_user_id = '" + user_id + "' OR  tmapp.tmap_rw_user_id = '" + user_id
							+ "' OR  tmapp.tmap_fh_user_id = '" + user_id + "') " + "AND "
							+ "tttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
							+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays + "'";

					// System.out.println("user_role_id == 3 : " + sql);
				} else {
					if (user_role_id == 2) {
						sql = "SELECT distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date,tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id , usr.user_first_name, usr.user_last_name,tttrn.ttrn_completed_date  as c1,tsk.task_cat_law_name,tttrn.ttrn_performer_comments as pc1, tttrn.ttrn_reason_for_non_compliance,tsk.task_implication, rwusr.user_first_name as rwFirstNames , rwusr.user_last_name as rwLastNames, fhusr.user_first_name as fhFirstNames, fhusr.user_last_name as fhLastNames "
								+ "FROM trn_task_transactional tttrn "
								+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
								+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
								+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
								+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
								+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
								+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
								+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
								+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id " + "WHERE "
								+ "(tmapp.tmap_pr_user_id = '" + user_id + "' OR  tmapp.tmap_rw_user_id = '" + user_id
								+ "') " + "AND "
								+ "tttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND "
								+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
								+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays + "'";
						// System.out.println("user_role_id == 2 : " + sql);
					} else {
						if (user_role_id == 1) {
							sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date,tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id , usr.user_first_name, usr.user_last_name,tttrn.ttrn_completed_date  as c1,tsk.task_cat_law_name,tttrn.ttrn_performer_comments as pc3, tttrn.ttrn_reason_for_non_compliance,tsk.task_implication, rwusr.user_first_name as rwFirstNames , rwusr.user_last_name as rwLastNames, fhusr.user_first_name as fhFirstNames, fhusr.user_last_name as fhLastNames "
									+ "FROM trn_task_transactional tttrn "
									+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
									+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
									+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
									+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
									+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
									+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
									+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
									+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id " + "WHERE "
									+ "(tmapp.tmap_pr_user_id = '" + user_id + "') " + "AND "
									+ "tttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND "
									+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
									+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays
									+ "'";
							// System.out.println("user_role_id == 1 : " + sql);
						} else {
							if (user_role_id == 7) {

								sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, "
										+ "tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date,tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id , usr.user_first_name, usr.user_last_name,tttrn.ttrn_completed_date,tsk.task_cat_law_name,tttrn.ttrn_performer_comments as pc4, tttrn.ttrn_reason_for_non_compliance, tsk.task_implication, rwusr.user_first_name as rwFirstNames , rwusr.user_last_name as rwLastNames, fhusr.user_first_name as fhFirstNames, fhusr.user_last_name as fhLastNames "
										+ "FROM trn_task_transactional tttrn "
										+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
										+ " JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND "
										+ "umapp.umap_loca_id = tmapp.tmap_loca_id AND umapp.umap_dept_id = tmapp.tmap_dept_id "
										+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
										+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
										+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
										+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
										+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
										+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
										+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
										+ "WHERE tttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND "
										+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
										+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays
										+ "'";

								// System.out.println("user_role_id == 7 : " + sql);
								// System.out.println("Inside Superadmin access");
							}
						}
					}
				}
			}

			// System.out.println("Main Task Query: " + sql);
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

	/**
	 * @Date 27-Feb-2021
	 * @author DnyaneshG
	 * @Function Get Auditor Data on Dashboard by status
	 */

//	@Cacheable(value = "auditTaskDashboard", key = "{#user_id, #user_role_id}")
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> auditTaskDashboard(int user_id, int user_role_id, String jsonString) {

		LocalDate localDate = LocalDate.now();
		// Previous 60 days data
		// LocalDate minusDays = localDate.minusDays(60);
		LocalDate minusDays = localDate.minusDays(20);
		// Next 30 days data will be loading -Removed from 30 day's+ and added 10 days
		LocalDate plusDays = localDate.plusDays(20);

		try {
			String sql = "";
			// System.out.println(user_id + " id " + user_role_id);
			if (user_role_id > 3 && user_role_id != 7) {
				sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date, "
						+ "tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, "
						+ "dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, "
						+ "tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, "
						+ "usr.user_id as usrId, usr.user_first_name as usrFirstName, usr.user_last_name as usrLastName, "
						+ "tttrn.ttrn_completed_date  as c1, tsk.task_cat_law_name, tsk.task_cat_law_name as ts1, "
						+ "tsk.task_cat_law_name as t2, tsk.task_implication, "
						+ "rwusr.user_first_name as rwFirstNames , rwusr.user_last_name as rawLastNames , "
						+ "fhusr.user_first_name as fahFirstNames , fhusr.user_last_name as fhLastNames , tttrn.auditDate, tttrn.reOpenDateWindow, "
						+ "tttrn.auditoComments, tttrn.auditorAuditTime, tttrn.auditorStatus, tttrn.auditor_performer_by_id, "
						+ "tttrn.isAuditTasks, tttrn.isDocumentUpload, tttrn.ttrn_tasks_status  FROM trn_task_transactional tttrn "
						+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
						+ " JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND "
						+ "umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = '" + user_id + "' "
						+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
						+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
						+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
						+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
						+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
						+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
						+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
						+ "WHERE  tttrn.ttrn_is_Task_Audited = 'Yes'   AND tttrn.ttrn_status != 'Inactive'  AND tmapp.tmap_enable_status != 0 AND "
						+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
						+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays + "'";

				// tttrn.auditorStatus IN ('Complied', 'NonComplied') AND
				// tttrn.auditor_performer_by_id != 0
				// System.out.println("auditTaskDashboard role > 3 & != 7 " + sql);

			} else {
				if (user_role_id == 3) {

					sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date, "
							+ "tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, "
							+ "dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, "
							+ "tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, "
							+ "usr.user_id as usrId, usr.user_first_name as usrFirstName, usr.user_last_name as usrLastName, "
							+ "tttrn.ttrn_completed_date  as c1, tsk.task_cat_law_name as t1, tsk.task_cat_law_name as tc2, "
							+ "tsk.task_cat_law_name, tsk.task_implication, "
							+ "rwusr.user_first_name as rwFirstNames , rwusr.user_last_name as rwLastNames , "
							+ "fhusr.user_first_name as fhFirstNames , fhusr.user_last_name as fhaLastNames "
							+ " , tttrn.auditDate, tttrn.reOpenDateWindow, tttrn.auditoComments, tttrn.auditorAuditTime, tttrn.auditorStatus, tttrn.auditor_performer_by_id, "
							+ "tttrn.isAuditTasks, tttrn.isDocumentUpload, tttrn.ttrn_tasks_status  FROM trn_task_transactional tttrn "
							+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
							+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
							+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
							+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
							+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
							+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
							+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
							+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id " + "WHERE "
							+ "(tmapp.tmap_pr_user_id = '" + user_id + "' OR  tmapp.tmap_rw_user_id = '" + user_id
							+ "' OR  tmapp.tmap_fh_user_id = '" + user_id + "') AND "
							+ "  tttrn.ttrn_is_Task_Audited = 'Yes'   AND tttrn.ttrn_status != 'Inactive'  AND tmapp.tmap_enable_status != 0 AND "
							+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
							+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays + "'";

					// tttrn.auditorStatus IN ('Complied', 'NonComplied') AND
					// tttrn.auditor_performer_by_id != 0 AND
					// System.out.println("auditTaskDashboard role 3 access : " + sql);

				} else {

					if (user_role_id == 12) {

						sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date, "
								+ "tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, "
								+ "dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, "
								+ "tsk.task_activity_when, tsk.task_activity, "
								+ "tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, "
								+ "usr.user_id as usrId, usr.user_first_name as usrFirstName, usr.user_last_name as usrLName, "
								+ "tttrn.ttrn_completed_date  as c1, tsk.task_cat_law_name as tac1, tsk.task_cat_law_name as tc2, "
								+ "tsk.task_cat_law_name, tsk.task_implication, "
								+ "rwusr.user_first_name as rwFirstNames , rwusr.user_last_name as rwLastNames , "
								+ "fhusr.user_first_name as fhFirstNames , fhusr.user_last_name as fahLastNames , tttrn.auditDate, tttrn.reOpenDateWindow, "
								+ "tttrn.auditoComments, tttrn.auditorAuditTime, tttrn.auditorStatus, tttrn.auditor_performer_by_id, "
								+ "tttrn.isAuditTasks, tttrn.isDocumentUpload, tttrn.ttrn_tasks_status  FROM trn_task_transactional tttrn "
								+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
								+ " JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND "
								+ "umapp.umap_dept_id = tmapp.tmap_dept_id AND usr.user_id = '" + user_id + "' "
								+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
								+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
								+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
								+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
								+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
								+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
								+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
								+ "WHERE  tttrn.ttrn_is_Task_Audited = 'Yes'   AND tttrn.ttrn_status != 'Inactive'  AND tmapp.tmap_enable_status != 0 AND  "
								+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
								+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays + "'";

						// tttrn.auditorStatus IN ('Complied', 'NonComplied') AND
						// tttrn.auditor_performer_by_id != 0 AND
						// System.out.println("auditTaskDashboard user_role_id == 12 access : " + sql);

					}

					if (user_role_id == 2) {
						sql = "SELECT distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date, "
								+ "tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, "
								+ "dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, "
								+ "tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, "
								+ "usr.user_id usrId, usr.user_first_name as usrFnMae, usr.user_last_name as usrLName, "
								+ "tttrn.ttrn_completed_date  as c1,tsk.task_cat_law_name as tlawname1, tsk.task_cat_law_name as tc1, "
								+ "tsk.task_cat_law_name, tsk.task_implication, "
								+ "rwusr.user_first_name as rwFirstNames, rwusr.user_last_name as rwLastNames , "
								+ "fhusr.user_first_name as fhFirstNames, fhusr.user_last_name as fhLastNames "
								+ " , tttrn.auditDate, tttrn.reOpenDateWindow, tttrn.auditoComments, tttrn.auditorAuditTime, tttrn.auditorStatus, tttrn.auditor_performer_by_id, "
								+ "tttrn.isAuditTasks, tttrn.isDocumentUpload, tttrn.ttrn_tasks_status  FROM trn_task_transactional tttrn "
								+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
								+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
								+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
								+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
								+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
								+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
								+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
								+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id " + "WHERE "
								+ "(tmapp.tmap_pr_user_id = '" + user_id + "' OR  tmapp.tmap_rw_user_id = '" + user_id
								+ "') " + "AND "
								+ "  tttrn.ttrn_is_Task_Audited = 'Yes'   AND tttrn.ttrn_status != 'Inactive'  AND tmapp.tmap_enable_status != 0 AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
								+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays + "'";

						// tttrn.auditorStatus IN ('Complied', 'NonComplied') AND
						// tttrn.auditor_performer_by_id != 0
						// System.out.println("auditTaskDashboard role 2 access : " + sql);
					} else {
						if (user_role_id == 1) {
							sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, "
									+ "tttrn.ttrn_fh_due_date,tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, "
									+ "loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , "
									+ "tsk.task_activity_who, "
									+ "tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, "
									+ "usr.user_id as usrId, usr.user_first_name as uFName, usr.user_last_name as uLName, tttrn.ttrn_completed_date  as c1,"
									+ "tsk.task_cat_law_name as tLawNames1, tsk.task_cat_law_name as ta2, "
									+ "tsk.task_cat_law_name,tsk.task_implication, "
									+ "rwusr.user_first_name as rwFirstNames , rwusr.user_last_name as rwLastNames , "
									+ "fhusr.user_first_name as fhFirstNames , fhusr.user_last_name as fhLastNames  "
									+ " , tttrn.auditDate, tttrn.reOpenDateWindow, tttrn.auditoComments, tttrn.auditorAuditTime, tttrn.auditorStatus, tttrn.auditor_performer_by_id, "
									+ "tttrn.isAuditTasks, tttrn.isDocumentUpload, tttrn.ttrn_tasks_status  FROM trn_task_transactional tttrn "
									+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
									+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
									+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
									+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
									+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
									+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
									+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
									+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id WHERE "
									+ "(tmapp.tmap_pr_user_id = '" + user_id + "') " + "AND "
									+ " tttrn.ttrn_is_Task_Audited = 'Yes'   AND tttrn.ttrn_status != 'Inactive'  AND tmapp.tmap_enable_status != 0 AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND "
									+ "tsk.task_cat_law_name != 'Internal' " + "and tttrn.ttrn_legal_due_date BETWEEN '"
									+ minusDays + "' and '" + plusDays + "'";

							// tttrn.auditorStatus IN ('Complied', 'NonComplied') AND
							// tttrn.auditor_performer_by_id != 0 AND
							// System.out.println("auditTaskDashboard role 1 Inside Superadmin access : " +
							// sql);

						} else {
							if (user_role_id == 7) {

								sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, "
										+ "tttrn.ttrn_fh_due_date,tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, "
										+ "loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, "
										+ "tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, "
										+ "tttrn.ttrn_frequency_for_operation, "
										+ "usr.user_id as usrId, usr.user_first_name as usFiraName, usr.user_last_name as usrLastName, "
										+ "tttrn.ttrn_completed_date  as c1,tsk.task_cat_law_name as ta4, tsk.task_cat_law_name, "
										+ "tsk.task_cat_law_name as tLanam1, tsk.task_implication, "
										+ "rwusr.user_first_name as rwFirstNames , rwusr.user_last_name as rwLastNames , fhusr.user_first_name as fahFirstNames , "
										+ "fhusr.user_last_name as fahLastNames, tttrn.auditDate, tttrn.reOpenDateWindow, tttrn.auditoComments, tttrn.auditorAuditTime, "
										+ "tttrn.auditorStatus, tttrn.auditor_performer_by_id, "
										+ "tttrn.isAuditTasks, tttrn.isDocumentUpload, tttrn.ttrn_tasks_status  FROM trn_task_transactional tttrn "
										+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
										+ " JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND "
										+ "umapp.umap_dept_id = tmapp.tmap_dept_id "
										+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
										+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
										+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
										+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
										+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
										+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
										+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
										+ "WHERE tttrn.ttrn_is_Task_Audited = 'Yes'   AND tttrn.ttrn_status != 'Inactive'   AND tmapp.tmap_enable_status != 0 AND "
										+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
										+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays
										+ "'";

								// tttrn.auditorStatus IN ('Complied', 'NonComplied') AND
								// tttrn.auditor_performer_by_id != 0
								// System.out.println("auditTaskDashboard role 7 Inside Superadmin access : " +
								// sql);
							}
						}
					}
				}
			}

			// System.out.println("auditTaskDashboard Main Task Query: " + sql);
			Query query = em.createNativeQuery(sql);
			/*
			 * if (user_role_id != 7) { query.setParameter("user_id", user_id); }
			 */
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

//	@CacheEvict(value = "auditTaskDashboard", allEntries = true)
	@Override
	public String approverCompliedTasksURL(HttpSession session, String auditoComments, String ttrn_id) {
		try {
			String usrId = session.getAttribute("sess_user_id").toString();
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
			String currentDate = sdf.format(date);

			String sql = "UPDATE trn_task_transactional SET ttrn_completed_date = ttrn_legal_due_date, ttrn_submitted_date = ttrn_legal_due_date, "
					+ "ttrn_performer_comments = 'Task has been completed', ttrn_task_completed_by = ttrn_performer_user_id, "
					+ "ttrn_status = 'Completed', ttrn_tasks_status = 'Complied', isAuditTasks = 1, isDocumentUpload = 1, "
					+ "ttrn_reason_for_non_compliance = 'Complied', auditoComments = '" + auditoComments
					+ "', auditorStatus = 'Complied', ttrn_is_Task_Audited = 'Yes', isAuditTasks = 1, auditorAuditTime = '"
					+ currentDate + "', auditor_performer_by_id = '" + usrId
					+ "', ttrn_tasks_status = 'Complied' WHERE ttrn_id = '" + ttrn_id + "' ";

			// System.out.println("Making tasks as complied SQL : " + sql);

			Query createNativeQuery = em.createNativeQuery(sql);
			int executeUpdate = createNativeQuery.executeUpdate();
			// //System.out.println("executeUpdate : " + executeUpdate);
			if (createNativeQuery.executeUpdate() > 1) {
				return "success";
			} else {
				return "Error";
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
	public <T> List<T> searhMonthlyComplianceStatus(int parseInt, int parseInt2, String orga_id, String loca_id,
			String dept_id, org.joda.time.LocalDate pL1, org.joda.time.LocalDate pL2) {
		try {

			String sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, "
					+ "tttrn.ttrn_fh_due_date,tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, "
					+ "loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference, "
					+ "tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, "
					+ "tttrn.ttrn_frequency_for_operation, usr.user_id , usr.user_first_name, usr.user_last_name,tttrn.ttrn_completed_date as completedDate, "
					+ "tsk.task_cat_law_name,tttrn.ttrn_performer_comments as pc2, tttrn.ttrn_reason_for_non_compliance, tsk.task_implication, "
					+ "rwusr.user_first_name as rwFirstNames  as rw_first_name, rwusr.user_last_name as rwLastNames as rw_last_name, fhusr.user_first_name as fhFirstNames as fh_first_name, fhusr.user_last_name as fhLastNames as fh_last_name, tttrn.ttrn_completed_date, "
					+ "tttrn.ttrn_performer_user_id, tttrn.ttrn_performer_comments as pfComments "
					+ "FROM trn_task_transactional tttrn LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id  "
					+ "JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND "
					+ "umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = '32'  "
					+ "JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
					+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
					+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
					+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
					+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
					+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
					+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
					+ "WHERE tttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND "
					+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' and "
					+ "tttrn.ttrn_legal_due_date BETWEEN '2021-02-09' and '2021-04-10' AND auditor_performer_by_id =  0 ";
			Query createNativeQuery = em.createNativeQuery(sql);
			List resultList = createNativeQuery.getResultList();
			if (resultList.size() > 0) {
				return resultList;
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
	public void insertNewTaskUserMapping(TaskUserMapping taskUserMapping) {
		try {
			// em.merge(taskUserMapping);
			em.persist(taskUserMapping);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public TaskTransactional getClientTaskDetailById(String ttrn_client_task_id) {
		try {
			TypedQuery query = em.createQuery(
					" from " + TaskTransactional.class.getName() + " where ttrn_client_task_id = :ttrn_client_task_id ",
					TaskTransactional.class);
			query.setParameter("ttrn_client_task_id", ttrn_client_task_id);
			return (TaskTransactional) query.getResultList().get(0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void copyCompliance(TaskTransactional tsk) {
		try {
			em.persist(tsk);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void savePODDocuments(UploadedPODDocuments documents) {
		try {
			em.persist(documents);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getListOfSimplyCompDocuments(String jsonString, HttpSession session) {
		try {
//			String sql = "SELECT ID, ADDED_BY, DOC_DESCRIPTION, DOC_NAME, DOC_ORIGINAL_NAME, DOC_PATH, REPORT_FROM_DATE, REPORT_TO_DATE, IS_DELETED FROM upload_pod_docs  WHERE IS_DELETED = 0  order by ID desc  ";
			String sql = "SELECT distinct ud.ID, ud.ADDED_BY, ud.DOC_DESCRIPTION, ud.DOC_NAME, ud.DOC_ORIGINAL_NAME, ud.DOC_PATH, ud.REPORT_FROM_DATE, ud.REPORT_TO_DATE, ud.IS_DELETED, tsk.task_state_id, tsk.task_state_name FROM upload_pod_docs ud JOIN mst_task tsk ON tsk.task_state_id = ud.STATE_ID WHERE ud.IS_DELETED = 0 ORDER BY ud.ID DESC ";
			Query createQuery = em.createNativeQuery(sql);
			// System.out.println("sql : " + sql);
			List resultList = createQuery.getResultList();
			if (resultList.size() > 0) {
				return resultList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getProofFilePath(int docId) {
		try {
			String uStatus = "UPDATE upload_pod_docs set DOWNLOAD_STATUS = 1 where ID = '" + docId + "' ";

			String q = "SELECT DOC_PATH FROM upload_pod_docs WHERE ID = '" + docId + "' ";
			// String updateDownloadStatus = "Update " +
			// UploadedPODDocuments.class.getName() + " set DOWNLOAD_STATUS = 1 where ID ="
			// + docId + "";
			// String sql = "SELECT DOC_PATH FROM " + UploadedPODDocuments.class.getName() +
			// " where id = :udoc_id";
			Query query = em.createNativeQuery(q);
			int executeUpdate = em.createNativeQuery(uStatus).executeUpdate();
			// System.out.println("Download document Status : " + executeUpdate);

			// query.setParameter("udoc_id", docId);

			if (!query.getResultList().isEmpty()) {
				if (query.getResultList().get(0) != null) {
					return query.getResultList().get(0).toString();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

		/*
		 * try { String sql = "SELECT DOC_PATH FROM upload_pod_docs WHERE ID = '" +
		 * docId + "' "; Query createNativeQuery = em.createNativeQuery(sql); List
		 * resultList = createNativeQuery.getResultList(); if (resultList.size() > 0) {
		 * return resultList.toString(); } } catch (Exception e) { e.printStackTrace();
		 * } return null;
		 */
	}

	@Override
	public String decrypt(String algo, String path) {
		// TODO Auto-generated method stub
		try {
			// generating same key
			byte k[] = "HignDlPs".getBytes();
			SecretKeySpec key = new SecretKeySpec(k, algo.split("/")[0]);
			// creating and initializing cipher and cipher streams
			Cipher decrypt = Cipher.getInstance(algo);
			decrypt.init(Cipher.DECRYPT_MODE, key);
			// opening streams
			FileInputStream fis = new FileInputStream(path);
			try (CipherInputStream cin = new CipherInputStream(fis, decrypt)) {
				try (FileOutputStream fos = new FileOutputStream(path.substring(0, path.lastIndexOf(".")))) {
					copy(cin, fos);
				} catch (Exception e) {
					// System.out.println(e.getMessage());
				}
			} catch (Exception e) {
				// System.out.println(e.getMessage());
			}
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return path.substring(0, path.lastIndexOf("."));
	}

	private void copy(InputStream is, OutputStream os) {

		// TODO Auto-generated method stub
		try {
			byte buf[] = new byte[4096]; // 4K buffer set
			int read = 0;
			while ((read = is.read(buf)) != -1) // reading
				os.write(buf, 0, read); // writing
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	@Override
	public void makeNonCompliedTasks(TaskTransactional taskTransactional) {
		em.merge(taskTransactional);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> List<T> verticalWiceReport(String fromDate, String toDate, HttpSession session) {
		try {

			String usrId = session.getAttribute("sess_user_id").toString();
			Integer userRole = (Integer) session.getAttribute("sess_role_id");

			String sql = null;

			if (userRole == 6 || userRole == 7) {
				sql = "SELECT org.orga_name, SUM(IF(DATE_FORMAT(ttrn.ttrn_legal_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn.ttrn_status = 'Active', 1, 0)) AS 'NonComplied', SUM(IF(DATE_FORMAT(ttrn.ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(NOW(), '%Y/%m/%d') AND DATE_FORMAT(ttrn.ttrn_pr_due_date, '%Y/%m/%d') < DATE_FORMAT(NOW(), '%Y/%m/%d') AND ttrn.ttrn_status = 'Active', 1, 0)) AS 'PosingRisk', SUM(IF(DATE_FORMAT(ttrn.ttrn_legal_due_date, '%Y/%m/%d') >= DATE_FORMAT(ttrn.ttrn_submitted_date, '%Y/%m/%d') AND ttrn.ttrn_status = 'Completed', 1, 0)) AS 'Complied', SUM(IF(DATE_FORMAT(ttrn.ttrn_completed_date, '%Y/%m/%d') > DATE_FORMAT(ttrn.ttrn_legal_due_date, '%Y/%m/%d') AND DATE_FORMAT(ttrn.ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn.ttrn_legal_due_date, '%Y/%m/%d') AND ttrn.ttrn_status = 'Completed', 1, 0)) AS 'Delayed', SUM(IF(ttrn.ttrn_status = 'Partially_Completed', 1, 0)) AS 'WaitingForApproval',  SUM(IF(ttrn.ttrn_status = 'Re_Opened', 1, 0)) AS 'ReOpened', SUM(IF(DATE_FORMAT(ttrn.ttrn_submitted_date, '%Y/%m/%d') > DATE_FORMAT(ttrn.ttrn_legal_due_date, '%Y/%m/%d') AND (DATE_FORMAT(ttrn.ttrn_completed_date, '%Y/%m/%d') <= DATE_FORMAT(ttrn.ttrn_legal_due_date, '%Y/%m/%d')) AND ttrn.ttrn_status = 'Completed', 1, 0)) AS 'DelayedReported' FROM trn_task_transactional ttrn JOIN cfg_task_user_mapping tmap ON tmap.tmap_client_tasks_id = ttrn.ttrn_client_task_id JOIN mst_organization org ON org.orga_id = tmap.tmap_orga_id group by org.orga_name  ";
				Query createNativeQuery = em.createNativeQuery(sql);
				List resultList = createNativeQuery.getResultList();
				if (resultList.size() > 0 && resultList != null) {
					return resultList;
				}
			} else {
				sql = "";
				Query createNativeQuery = em.createNativeQuery(sql);
				List resultList = createNativeQuery.getResultList();
				if (resultList.size() > 0 && resultList != null) {
					return resultList;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> statusCompletionReport(String fromDate, String toDate, HttpSession session) {

		/*
		 * try { String sql =
		 * "SELECT org.orga_name AS 'Vertical', loca.loca_name AS 'Unit Name', dept.dept_name AS 'Function', "
		 * +
		 * "CONCAT(usr.user_first_name, ' ', usr.user_last_name) AS 'Executor', usr.user_email AS 'Email', usr.user_mobile AS 'Mobile No', "
		 * +
		 * "COUNT(IF(ttrn.isDocumentUpload = 1, 1, NULL)) AS 'Document Uploaded', COUNT(IF(ttrn.isDocumentUpload = 0, 1, NULL)) AS 'Document Pending' "
		 * +
		 * "FROM trn_task_transactional ttrn LEFT JOIN trn_uploadeddocuments docs ON docs.udoc_ttrn_id = ttrn.ttrn_id "
		 * +
		 * "JOIN cfg_task_user_mapping tmap ON tmap.tmap_client_tasks_id = ttrn.ttrn_client_task_id "
		 * +
		 * "JOIN mst_organization org ON org.orga_id = tmap.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmap.tmap_loca_id "
		 * +
		 * "JOIN mst_department dept ON dept.dept_id = tmap.tmap_dept_id JOIN mst_user usr ON usr.user_id = tmap.tmap_pr_user_id AND "
		 * + "ttrn.ttrn_legal_due_date BETWEEN '" + fromDate + "' AND '" + toDate +
		 * "' AND ttrn.ttrn_status != 'Inactive' AND " +
		 * "tmap.tmap_enable_status != 0 AND ttrn.ttrn_frequency_for_operation != 'User_Defined' GROUP BY usr.user_username , "
		 * + "dept.dept_name , loca.loca_name "; Query createNativeQuery =
		 * em.createNativeQuery(sql);
		 * //System.out.println("statusCompletionReport SQL : " + sql); List resultList
		 * = createNativeQuery.getResultList(); ifl (resultList.size() > 0) { return
		 * resultList; } } catch (Exception e) { e.printStackTrace(); } return null;
		 */

		int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
		int user_role_id = Integer.parseInt(session.getAttribute("sess_role_id").toString());
		try {
			String sql = "";
			// System.out.println(user_id + " id " + user_role_id);
			if (user_role_id > 3 && user_role_id != 7) {

//				sql = "SELECT distinct orga.orga_name AS 'Vertical', loca.loca_name AS 'Unit Name', dept.dept_name AS 'Function', "
//						+ "CONCAT(usr.user_first_name, ' ', usr.user_last_name) AS 'Executor', usr.user_email AS 'Email', usr.user_mobile AS 'Mobile No', "
//						+ "COUNT(IF(tttrn.isDocumentUpload = 1, tttrn.ttrn_id, NULL)) AS 'Document Uploaded', "
//						+ "COUNT(IF(tttrn.isDocumentUpload = 0, tttrn.ttrn_id, NULL)) AS 'Document Pending' "
////						+ " COUNT(IF((tttrn.isDocumentUpload = 1 AND tttrn.ttrn_is_Task_Audited = 'No'), 1, NULL)) AS 'Uploaded', "
////						+ " COUNT(IF(tttrn.isDocumentUpload = 0, 1, NULL)) AS 'Document Pending', "
////						+ " COUNT(IF((tttrn.isDocumentUpload = 1 AND tttrn.ttrn_is_Task_Audited = 'Yes'), 1, NULL)) AS 'Audited Task Uploaded' "
//						+ " FROM trn_task_transactional tttrn "
//						+ "LEFT JOIN cfg_task_user_mapping tmapp ON tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
//						+ "JOIN cfg_user_entity_mapping umapp ON umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND "
//						+ "umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = '" + user_id + "' "
//						+ "JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id "
//						+ "JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmapp.tmap_loca_id "
//						+ "JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id JOIN mst_user usr ON usr.user_id = tmapp.tmap_pr_user_id "
//						+ "JOIN mst_user rwusr ON rwusr.user_id = tmapp.tmap_rw_user_id JOIN mst_user fhusr ON fhusr.user_id = tmapp.tmap_fh_user_id AND "
//						+ "tttrn.ttrn_legal_due_date BETWEEN '" + fromDate + "' AND '" + toDate
//						+ "' AND tttrn.ttrn_status NOT IN ('Inactive', 'Partially_completed', 'Event_Not_Occured') AND "
//						+ "tmapp.tmap_enable_status != 0 AND tttrn.ttrn_frequency_for_operation != 'User_Defined' AND loca.loca_id != 0 "
//						+ "GROUP BY orga.orga_name, loca.loca_name, dept.dept_name ";

				sql = "SELECT DISTINCT orga.orga_name, loca.loca_name, dept.dept_name, "
						+ "CONCAT(usr.user_first_name, ' ', usr.user_last_name) AS 'Executor', usr.user_email AS 'Email', usr.user_mobile AS 'Mobile No', "
						+ "COUNT(distinct IF(tttrn.isDocumentUpload = 1, tttrn.ttrn_id, NULL)) AS 'Document Uploaded', "
						+ "COUNT(distinct IF(tttrn.isDocumentUpload = 0, tttrn.ttrn_id, NULL)) AS 'Document Pending' "
						+ "FROM trn_task_transactional tttrn "
						+ "LEFT JOIN cfg_task_user_mapping tmapp ON tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
						+ "JOIN cfg_user_entity_mapping umapp ON umapp.umap_orga_id = tmapp.tmap_orga_id "
						+ "AND umapp.umap_loca_id = tmapp.tmap_loca_id AND umapp.umap_dept_id = tmapp.tmap_dept_id AND "
						+ "umapp.umap_user_id = '" + user_id + "' "
						+ "LEFT JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id "
						+ "JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id "
						+ "JOIN mst_location loca ON loca.loca_id = tmapp.tmap_loca_id "
						+ "JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id "
						+ "JOIN mst_user usr ON usr.user_id = tmapp.tmap_pr_user_id "
						+ "JOIN mst_user usrrw ON usrrw.user_id = tmapp.tmap_rw_user_id "
						+ "JOIN mst_user usrfh ON usrfh.user_id = tmapp.tmap_fh_user_id "
						+ "WHERE (tttrn.ttrn_status = 'Active' OR tttrn.ttrn_status = 'Completed' OR tttrn.ttrn_status = 'Re_Opened') AND "
						+ "tttrn.ttrn_frequency_for_operation != 'User_Defined' AND "
						+ "tmapp.tmap_enable_status != 0 AND "
						+ "((tttrn.ttrn_is_Task_Audited = 'No' AND isDocumentUpload = 1) OR isDocumentUpload = 0) and "
						+ "loca.loca_id != 0 AND tmapp.tmap_enable_status != 0 AND "
						+ "tttrn.ttrn_legal_due_date BETWEEN '" + fromDate + "' AND '" + toDate + "' "
						+ "group by usr.user_first_name, usr.user_last_name, orga.orga_name , loca.loca_name , dept.dept_name";

				// System.out.println("statusCompletionReport user_role_id > 3 && user_role_id
				// != 7 : " + sql);
			} else {
				if (user_role_id == 3) {
//					sql = "SELECT orga.orga_name AS 'Vertical', loca.loca_name AS 'Unit Name', dept.dept_name AS 'Function', "
//							+ "CONCAT(usr.user_first_name, ' ', usr.user_last_name) AS 'Executor', usr.user_email AS 'Email', usr.user_mobile AS 'Mobile No', "
//							+ "COUNT(IF(tttrn.isDocumentUpload = 1, 1, NULL)) AS 'Document Uploaded', COUNT(IF(tttrn.isDocumentUpload = 0, 1, NULL)) AS 'Document Pending' "
//							+ "FROM trn_task_transactional tttrn LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
//							+ "JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
//							+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
//							+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
//							+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id WHERE "
//							+ "(tmapp.tmap_pr_user_id = '" + user_id + "' OR  tmapp.tmap_rw_user_id = '" + user_id
//							+ "' OR  tmapp.tmap_fh_user_id = '" + user_id + "') "
//							+ "AND tttrn.ttrn_legal_due_date BETWEEN '" + fromDate + "' AND '" + toDate
//							+ "' AND tttrn.ttrn_status NOT IN ('Inactive' , 'Event_Not_Occured') AND "
//							+ "tmapp.tmap_enable_status != 0 AND tttrn.ttrn_frequency_for_operation != 'User_Defined' AND loca.loca_id != 0 "
//							+ " GROUP BY usr.user_first_name , usr.user_last_name , orga.orga_name , loca.loca_name , dept.dept_name ";

					sql = "SELECT DISTINCT orga.orga_name AS 'Vertical', loca.loca_name AS 'Unit Name', dept.dept_name AS 'Function', "
							+ "CONCAT(usr.user_first_name, ' ', usr.user_last_name) AS 'Executor', usr.user_email AS 'Email', "
							+ "usr.user_mobile AS 'Mobile No', "
							+ "COUNT(distinct IF(tttrn.isDocumentUpload = 1, tttrn.ttrn_id, NULL)) AS 'Document Uploaded', "
							+ "COUNT(distinct IF(tttrn.isDocumentUpload = 0, tttrn.ttrn_id, NULL)) AS 'Document Pending'"
//							+ "COUNT(IF(tttrn.isDocumentUpload = 1, 1, NULL)) AS 'Document Uploaded', "
//							+ "COUNT(IF(tttrn.isDocumentUpload = 0, 1, NULL)) AS 'Document Pending' "
//							+ " COUNT(IF((tttrn.isDocumentUpload = 1 AND tttrn.ttrn_is_Task_Audited = 'No'), 1, NULL)) AS 'Uploaded', "
//							+ " COUNT(IF(tttrn.isDocumentUpload = 0, 1, NULL)) AS 'Document Pending', "
//							+ " COUNT(IF((tttrn.isDocumentUpload = 1 AND tttrn.ttrn_is_Task_Audited = 'Yes'), 1, NULL)) AS 'Audited Task Uploaded' "
							+ " FROM trn_task_transactional tttrn "
							+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
							+ "JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
							+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
							+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
							+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
							+ "WHERE (tmapp.tmap_pr_user_id = '" + user_id + "' OR tmapp.tmap_rw_user_id = '" + user_id
							+ "' OR tmapp.tmap_fh_user_id = '" + user_id + "') AND "
							+ "tttrn.ttrn_legal_due_date BETWEEN '" + fromDate + "' AND '" + toDate
							+ "' AND tttrn.ttrn_status NOT IN ('Inactive', 'Partially_completed', 'Event_Not_Occured') AND "
							+ "tmapp.tmap_enable_status != 0 AND tttrn.ttrn_frequency_for_operation != 'User_Defined' "
							+ "AND ((tttrn.ttrn_is_Task_Audited = 'No' AND isDocumentUpload = 1) OR isDocumentUpload = 0) "
							+ "GROUP BY usr.user_username, dept.dept_name, loca.loca_name";

					// System.out.println("statusCompletionReport user_role_id == 3 : " + sql);
				} else {
					if (user_role_id == 2) {
						sql = "SELECT DISTINCT orga.orga_name AS 'Vertical', loca.loca_name AS 'Unit Name', dept.dept_name AS 'Function', "
								+ "CONCAT(usr.user_first_name, ' ', usr.user_last_name) AS 'Executor', usr.user_email AS 'Email', "
								+ "usr.user_mobile AS 'Mobile No', "
								+ "COUNT(distinct IF(tttrn.isDocumentUpload = 1, tttrn.ttrn_id, NULL)) AS 'Document Uploaded', "
								+ "COUNT(distinct IF(tttrn.isDocumentUpload = 0, tttrn.ttrn_id, NULL)) AS 'Document Pending' "
//								+ "COUNT(IF(tttrn.isDocumentUpload = 1, 1, NULL)) AS 'Document Uploaded', "
//								+ "COUNT(IF(tttrn.isDocumentUpload = 0, 1, NULL)) AS 'Document Pending' "
//								+ " COUNT(IF((tttrn.isDocumentUpload = 1 AND tttrn.ttrn_is_Task_Audited = 'No'), 1, NULL)) AS 'Uploaded', "
//								+ " COUNT(IF(tttrn.isDocumentUpload = 0, 1, NULL)) AS 'Document Pending', "
//								+ " COUNT(IF((tttrn.isDocumentUpload = 1 AND tttrn.ttrn_is_Task_Audited = 'Yes'), 1, NULL)) AS 'Audited Task Uploaded' "
								+ " FROM trn_task_transactional tttrn LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
								+ "JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
								+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
								+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id  "
								+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id  WHERE (tmapp.tmap_pr_user_id = '"
								+ user_id + "' OR  tmapp.tmap_rw_user_id = '" + user_id + "') "
								+ "AND tttrn.ttrn_legal_due_date BETWEEN '" + fromDate + "' AND '" + toDate
								+ "' AND tttrn.ttrn_status NOT IN ('Inactive', 'Partially_completed', 'Event_Not_Occured') AND tmapp.tmap_enable_status != 0 "
								+ "AND tttrn.ttrn_frequency_for_operation != 'User_Defined' AND loca.loca_id != 0 "
								+ "AND ((tttrn.ttrn_is_Task_Audited = 'No' AND isDocumentUpload = 1) OR isDocumentUpload = 0) "
								+ "GROUP BY usr.user_first_name , usr.user_last_name , orga.orga_name , loca.loca_name , dept.dept_name ";
						// System.out.println("statusCompletionReport user_role_id == 2 : " + sql);
					} else {
						if (user_role_id == 1) {
							sql = "SELECT DISTINCT orga.orga_name AS 'Vertical', loca.loca_name AS 'Unit Name', dept.dept_name AS 'Function', "
									+ "CONCAT(usr.user_first_name, ' ', usr.user_last_name) AS 'Executor', usr.user_email AS 'Email', "
									+ "usr.user_mobile AS 'Mobile No', "
									+ " COUNT(distinct IF(tttrn.isDocumentUpload = 1, tttrn.ttrn_id, NULL)) AS 'Document Uploaded', "
									+ "COUNT(distinct IF(tttrn.isDocumentUpload = 0, tttrn.ttrn_id, NULL)) AS 'Document Pending' "
//									+ "COUNT(IF(tttrn.isDocumentUpload = 1, 1, NULL)) AS 'Document Uploaded', "
//									+ "COUNT(IF(tttrn.isDocumentUpload = 0, 1, NULL)) AS 'Document Pending' "
//									+ " COUNT(IF((tttrn.isDocumentUpload = 1 AND tttrn.ttrn_is_Task_Audited = 'No'), 1, NULL)) AS 'Uploaded', "
//									+ " COUNT(IF(tttrn.isDocumentUpload = 0, 1, NULL)) AS 'Document Pending', "
//									+ " COUNT(IF((tttrn.isDocumentUpload = 1 AND tttrn.ttrn_is_Task_Audited = 'Yes'), 1, NULL)) AS 'Audited Task Uploaded' "
									+ " FROM trn_task_transactional tttrn "
									+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
									+ "JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
									+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
									+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
									+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id WHERE  (tmapp.tmap_pr_user_id = '"
									+ user_id + "') AND " + "tttrn.ttrn_legal_due_date BETWEEN '" + fromDate + "' AND '"
									+ toDate
									+ "' AND tttrn.ttrn_status NOT IN ('Inactive', 'Partially_completed', 'Event_Not_Occured') AND "
									+ "tmapp.tmap_enable_status != 0 AND tttrn.ttrn_frequency_for_operation != 'User_Defined' AND loca.loca_id != 0 AND "
									+ "((tttrn.ttrn_is_Task_Audited = 'No' AND isDocumentUpload = 1) OR isDocumentUpload = 0) "
									+ "GROUP BY usr.user_first_name , usr.user_last_name , orga.orga_name , loca.loca_name , dept.dept_name ";

							// System.out.println("statusCompletionReport user_role_id == 1 : " + sql);

						} else {
							if (user_role_id == 7) {
//								sql = "SELECT org.orga_name AS 'Vertical', loca.loca_name AS 'Unit Name', dept.dept_name AS 'Function', "
//										+ "CONCAT(usr.user_first_name, ' ', usr.user_last_name) AS 'Executor', usr.user_email AS 'Email', "
//										+ "usr.user_mobile AS 'Mobile No', COUNT(IF(ttrn.isDocumentUpload = 1, 1, NULL)) AS 'Document Uploaded', "
//										+ "COUNT(IF(ttrn.isDocumentUpload = 0, 1, NULL)) AS 'Document Pending' FROM trn_task_transactional ttrn "
//										+ "LEFT JOIN trn_uploadeddocuments docs ON docs.udoc_ttrn_id = ttrn.ttrn_id JOIN cfg_task_user_mapping tmap ON "
//										+ "tmap.tmap_client_tasks_id = ttrn.ttrn_client_task_id JOIN mst_organization org ON org.orga_id = tmap.tmap_orga_id "
//										+ "JOIN mst_location loca ON loca.loca_id = tmap.tmap_loca_id JOIN mst_department dept ON "
//										+ "dept.dept_id = tmap.tmap_dept_id JOIN mst_user usr ON usr.user_id = tmap.tmap_pr_user_id AND "
//										+ "ttrn.ttrn_legal_due_date BETWEEN '" + fromDate + "' AND '" + toDate
//										+ "' AND ttrn.ttrn_status != 'Inactive' AND "
//										+ "tmap.tmap_enable_status != 0 AND ttrn.ttrn_frequency_for_operation != 'User_Defined'  GROUP BY usr.user_username, "
//										+ "dept.dept_name , loca.loca_name";

								sql = "SELECT distinct orga.orga_name AS 'Vertical', loca.loca_name AS 'Unit Name', dept.dept_name AS 'Function', "
										+ "CONCAT(usr.user_first_name, ' ', usr.user_last_name) AS 'Executor', usr.user_email AS 'Email', usr.user_mobile AS 'Mobile No', "
										+ " COUNT(DISTINCT IF(tttrn.isDocumentUpload = 1, tttrn.ttrn_id, NULL)) AS 'Document Uploaded', "
										+ " COUNT(DISTINCT IF(tttrn.isDocumentUpload = 0, tttrn.ttrn_id, NULL)) AS 'Document Pending', "
//										+ "COUNT(IF(tttrn.isDocumentUpload = 1, 1, NULL)) AS 'Document Uploaded', "
//										+ "COUNT(IF(tttrn.isDocumentUpload = 0, 1, NULL)) AS 'Document Pending', "
//										+ " COUNT(IF((tttrn.isDocumentUpload = 1 AND tttrn.ttrn_is_Task_Audited = 'No'), 1, NULL)) AS 'Uploaded', "
//										+ " COUNT(IF(tttrn.isDocumentUpload = 0, 1, NULL)) AS 'Document Pending', "
//										+ " COUNT(IF((tttrn.isDocumentUpload = 1 AND tttrn.ttrn_is_Task_Audited = 'Yes'), 1, NULL)) AS 'Audited Task Uploaded', "
										+ "tttrn.ttrn_client_task_id "
										+ "FROM trn_task_transactional tttrn LEFT JOIN cfg_task_user_mapping tmapp ON tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
										+ "JOIN mst_task tsk ON tsk.task_id = tmapp.tmap_task_id JOIN mst_organization orga ON orga.orga_id = tmapp.tmap_orga_id "
										+ "JOIN mst_location loca ON loca.loca_id = tmapp.tmap_loca_id JOIN mst_department dept ON dept.dept_id = tmapp.tmap_dept_id "
										+ "JOIN mst_user usr ON usr.user_id = tmapp.tmap_pr_user_id "
										+ "JOIN mst_user rwusr ON rwusr.user_id = tmapp.tmap_rw_user_id "
										+ "JOIN mst_user fhusr ON fhusr.user_id = tmapp.tmap_fh_user_id "
										+ "WHERE tttrn.ttrn_legal_due_date BETWEEN '" + fromDate + "' AND '" + toDate
										+ "' AND "
										+ " tttrn.ttrn_status NOT IN ('Inactive', 'Partially_completed', 'Event_Not_Occured') AND tmapp.tmap_enable_status != 0 AND "
										+ "tttrn.ttrn_frequency_for_operation != 'User_Defined' AND loca.loca_id !=0 AND "
										+ "((tttrn.ttrn_is_Task_Audited = 'No' AND isDocumentUpload = 1) OR isDocumentUpload = 0)"
										+ "GROUP BY usr.user_first_name,usr.user_last_name, orga.orga_name, loca.loca_name, dept.dept_name ";

								// System.out.println("statusCompletionReport user_role_id == 7 : " + sql);

							}
						}
					}
				}
			}

//			//System.out.println("Progress Report Query " + sql);
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
	public <T> List<T> searchExecutorList(HttpSession session, String orga_id, String loca_id, String dept_id) {
		try {
			String sql = "SELECT distinct org.orga_name AS 'Vertical', loca.loca_name AS 'Unit Name', dept.dept_name AS 'Function', "
					+ "CONCAT(usr.user_first_name, ' ', usr.user_last_name) AS 'Executor', usr.user_email AS 'Email', usr.user_mobile AS 'Mobile No' "
					+ "FROM trn_task_transactional ttrn LEFT JOIN trn_uploadeddocuments docs ON docs.udoc_ttrn_id = ttrn.ttrn_id "
					+ "JOIN cfg_task_user_mapping tmap ON tmap.tmap_client_tasks_id = ttrn.ttrn_client_task_id "
					+ "JOIN mst_organization org ON org.orga_id = tmap.tmap_orga_id JOIN mst_location loca ON loca.loca_id = tmap.tmap_loca_id "
					+ "JOIN mst_department dept ON dept.dept_id = tmap.tmap_dept_id JOIN mst_user usr ON usr.user_id = tmap.tmap_pr_user_id AND "
					+ " ttrn.ttrn_status != 'Inactive' AND "
					+ "tmap.tmap_enable_status != 0 AND ttrn.ttrn_frequency_for_operation != 'User_Defined' ";

			if (orga_id != "0") {
				sql += " AND org.orga_id = '" + orga_id + "' ";
			}

			if (loca_id != "0") {
				sql += " AND loca.loca_id = '" + loca_id + "' ";
			}

			if (dept_id != "0") {
				sql += " AND dept.dept_id = '" + dept_id + "' ";
			}

			Query createNativeQuery = em.createNativeQuery(sql);
			// System.out.println("searchExecutorList SQL : " + sql);
			List resultList = createNativeQuery.getResultList();
			/* if (resultList.size() > 0) { */
			return resultList;
			/* } */
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return null;
	}

	@SuppressWarnings({ "null", "unchecked" })
	@Override
	public <T> List<T> searchMonthlyComplianceAuditChartURL(int user_id, int user_role_id, String jsonString) {
		try {

			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String date_from = jsonObj.get("date_from") != null ? jsonObj.get("date_from").toString() : "0";
			String date_to = jsonObj.get("date_to") != null ? jsonObj.get("date_to").toString() : "0";
			String orgaId = jsonObj.get("orgaId") != null ? jsonObj.get("orgaId").toString() : "0";
			String locaId = jsonObj.get("locaId") != null ? jsonObj.get("locaId").toString() : "0";
			String deptId = jsonObj.get("deptId") != null ? jsonObj.get("deptId").toString() : "0";

			String sql = "";
			// System.out.println(user_id + " id " + user_role_id);
			if (user_role_id > 3 && user_role_id != 7) {
				sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date, "
						+ "tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, "
						+ "dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, "
						+ "tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, "
						+ "usr.user_id as usIds, usr.user_first_name as usFirstsNames, usr.user_last_name as usLasstNames, "
						+ "tttrn.ttrn_completed_date, tsk.task_cat_law_name as laName, tsk.task_cat_law_name as lwName1, tsk.task_cat_law_name, tsk.task_implication, "
						+ "rwusr.user_first_name as rwFirstNames , rwusr.user_last_name as rwLastNames, "
						+ "fhusr.user_first_name as fhFirstNames, fhusr.user_last_name as fhLastNames, tttrn.auditDate, tttrn.reOpenDateWindow, "
						+ "tttrn.auditoComments, tttrn.auditorAuditTime, tttrn.auditorStatus, tttrn.auditor_performer_by_id, "
						+ "tttrn.isAuditTasks, tttrn.isDocumentUpload, tttrn.ttrn_tasks_status  FROM trn_task_transactional tttrn "
						+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
						+ " JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND "
						+ "umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = '" + user_id + "' "
						+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
						+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
						+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
						+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
						+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
						+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
						+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
						+ "WHERE  tttrn.ttrn_is_Task_Audited = 'Yes' AND tmapp.tmap_enable_status != 0  AND "
						+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' ";

				// tttrn.auditorStatus IN ('Complied', 'NonComplied') AND
				// tttrn.auditor_performer_by_id != 0
				if (!date_from.equalsIgnoreCase("0") && !date_to.equalsIgnoreCase("0")) {
					sql += " AND  tttrn.ttrn_legal_due_date BETWEEN '" + date_from + "' AND '" + date_to + "' ";
				}

				if (!deptId.equalsIgnoreCase("0")) {
					sql += " AND dept.dept_id = '" + deptId + "' ";
				}

				if (!locaId.equalsIgnoreCase("0")) {
					sql += " AND loca.loca_id = '" + locaId + "' ";
				}

				if (!orgaId.equalsIgnoreCase("0")) {
					sql += " AND orga.orga_id = '" + orgaId + "' ";
				}

				// System.out.println("searchMonthlyComplianceAuditChart role > 3 & != 7 " +
				// sql);

			} else {
				if (user_role_id == 3) {

					sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date, "
							+ "tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, "
							+ "dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, "
							+ "tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id , usr.user_first_name, usr.user_last_name, "
							+ "tttrn.ttrn_completed_date,tsk.task_cat_law_name as lNames , tsk.task_cat_law_name as lManes2, tsk.task_cat_law_name, tsk.task_implication, "
							+ "rwusr.user_first_name as rwFirstNames , rwusr.user_last_name as rwLastNames, fhusr.user_first_name as fhFirstNames, fhusr.user_last_name as fhLastNames "
							+ " , tttrn.auditDate, tttrn.reOpenDateWindow, tttrn.auditoComments, tttrn.auditorAuditTime, tttrn.auditorStatus, tttrn.auditor_performer_by_id, "
							+ "tttrn.isAuditTasks, tttrn.isDocumentUpload, tttrn.ttrn_tasks_status  FROM trn_task_transactional tttrn "
							+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
							+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
							+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
							+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
							+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
							+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
							+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
							+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id " + "WHERE "
							+ "(tmapp.tmap_pr_user_id = '" + user_id + "' OR  tmapp.tmap_rw_user_id = '" + user_id
							+ "' OR  tmapp.tmap_fh_user_id = '" + user_id + "') "
							+ "AND  tttrn.ttrn_is_Task_Audited = 'Yes'  AND tmapp.tmap_enable_status != 0 AND "
							+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' ";

					// tttrn.auditor_performer_by_id != 0 AND tttrn.auditorStatus IN ('Complied',
					// 'NonComplied')
					if (!date_from.equalsIgnoreCase("0") && !date_to.equalsIgnoreCase("0")) {
						sql += " AND  tttrn.ttrn_legal_due_date BETWEEN '" + date_from + "' AND '" + date_to + "' ";
					}

					if (!deptId.equalsIgnoreCase("0")) {
						sql += " AND dept.dept_id = '" + deptId + "' ";
					}

					if (!locaId.equalsIgnoreCase("0")) {
						sql += " AND loca.loca_id = '" + locaId + "' ";
					}

					if (!orgaId.equalsIgnoreCase("0")) {
						sql += " AND orga.orga_id = '" + orgaId + "' ";
					}

					// System.out.println("searchMonthlyComplianceAuditChart role 3 access : " +
					// sql);

				} else {

					if (user_role_id == 12) {

						sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date, "
								+ "tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, "
								+ "dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, "
								+ "tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id , usr.user_first_name, usr.user_last_name, "
								+ "tttrn.ttrn_completed_date, tsk.task_cat_law_name as lwName, tsk.task_cat_law_name as lanMaes, tsk.task_cat_law_name, tsk.task_implication, "
								+ "rwusr.user_first_name as rwFirstNames , rwusr.user_last_name as rwLastNames, "
								+ "fhusr.user_first_name as fhFirstNames, fhusr.user_last_name as fhLastNames, tttrn.auditDate, tttrn.reOpenDateWindow, "
								+ "tttrn.auditoComments, tttrn.auditorAuditTime, tttrn.auditorStatus, tttrn.auditor_performer_by_id, "
								+ "tttrn.isAuditTasks, tttrn.isDocumentUpload, tttrn.ttrn_tasks_status  FROM trn_task_transactional tttrn "
								+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
								+ " JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND "
								+ "umapp.umap_dept_id = tmapp.tmap_dept_id AND usr.user_id = '" + user_id + "' "
								+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
								+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
								+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
								+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
								+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
								+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
								+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
								+ "WHERE  tttrn.ttrn_is_Task_Audited = 'Yes' AND tmapp.tmap_enable_status != 0  AND "
								+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' ";

						// tttrn.auditorStatus IN ('Complied', 'NonComplied') AND
						// tttrn.auditor_performer_by_id != 0
						if (!date_from.equalsIgnoreCase("0") && !date_to.equalsIgnoreCase("0")) {
							sql += " AND  tttrn.ttrn_legal_due_date BETWEEN '" + date_from + "' AND '" + date_to + "' ";
						}

						if (!deptId.equalsIgnoreCase("0")) {
							sql += " AND dept.dept_id = '" + deptId + "' ";
						}

						if (!locaId.equalsIgnoreCase("0")) {
							sql += " AND loca.loca_id = '" + locaId + "' ";
						}

						if (!orgaId.equalsIgnoreCase("0")) {
							sql += " AND orga.orga_id = '" + orgaId + "' ";
						}

						// System.out.println("searchMonthlyComplianceAuditChart user_role_id == 12
						// access : " + sql);

					}

					if (user_role_id == 2) {
						sql = "SELECT distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date, "
								+ "tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, "
								+ "dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, "
								+ "tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id , usr.user_first_name, usr.user_last_name, "
								+ "tttrn.ttrn_completed_date,tsk.task_cat_law_name as lawNames, tsk.task_cat_law_name as lawNmaes2, tsk.task_cat_law_name, "
								+ "tsk.task_implication, rwusr.user_first_name as rwFirstNames , rwusr.user_last_name as rwLastNames, fhusr.user_first_name as fhFirstNames, fhusr.user_last_name as fhLastNames "
								+ " , tttrn.auditDate, tttrn.reOpenDateWindow, tttrn.auditoComments, tttrn.auditorAuditTime, tttrn.auditorStatus, tttrn.auditor_performer_by_id, "
								+ "tttrn.isAuditTasks, tttrn.isDocumentUpload, tttrn.ttrn_tasks_status  FROM trn_task_transactional tttrn "
								+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
								+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
								+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
								+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
								+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
								+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
								+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
								+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id " + "WHERE "
								+ "(tmapp.tmap_pr_user_id = '" + user_id + "' OR  tmapp.tmap_rw_user_id = '" + user_id
								+ "') " + "AND tttrn.ttrn_is_Task_Audited = 'Yes' AND "
								+ "tmapp.tmap_enable_status != 0 AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND "
								+ "tsk.task_cat_law_name != 'Internal'  ";

						// tttrn.auditorStatus IN ('Complied', 'NonComplied') AND
						// tttrn.auditor_performer_by_id != 0
						if (!date_from.equalsIgnoreCase("0") && !date_to.equalsIgnoreCase("0")) {
							sql += " AND  tttrn.ttrn_legal_due_date BETWEEN '" + date_from + "' AND '" + date_to + "' ";
						}

						if (!deptId.equalsIgnoreCase("0")) {
							sql += " AND dept.dept_id = '" + deptId + "' ";
						}

						if (!locaId.equalsIgnoreCase("0")) {
							sql += " AND loca.loca_id = '" + locaId + "' ";
						}

						if (!orgaId.equalsIgnoreCase("0")) {
							sql += " AND orga.orga_id = '" + orgaId + "' ";
						}

						// System.out.println("searchMonthlyComplianceAuditChart role 2 access : " +
						// sql);
					} else {
						if (user_role_id == 1) {
							sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, "
									+ "tttrn.ttrn_fh_due_date,tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, "
									+ "loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, "
									+ "tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id , "
									+ "usr.user_first_name, usr.user_last_name,tttrn.ttrn_completed_date,tsk.task_cat_law_name as lasNames, tsk.task_cat_law_name as lawsNmaes1 , "
									+ "tsk.task_cat_law_name,tsk.task_implication, rwusr.user_first_name as rwFirstNames , rwusr.user_last_name as rwLastNames, "
									+ "fhusr.user_first_name as fhFirstNames, fhusr.user_last_name as fhLastNames "
									+ " , tttrn.auditDate, tttrn.reOpenDateWindow, tttrn.auditoComments, tttrn.auditorAuditTime, tttrn.auditorStatus, tttrn.auditor_performer_by_id, "
									+ "tttrn.isAuditTasks, tttrn.isDocumentUpload, tttrn.ttrn_tasks_status  FROM trn_task_transactional tttrn "
									+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
									+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
									+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
									+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
									+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
									+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
									+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
									+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id WHERE "
									+ "(tmapp.tmap_pr_user_id = '" + user_id + "') " + "AND "
									+ "  tttrn.ttrn_is_Task_Audited = 'Yes' AND tmapp.tmap_enable_status != 0 AND "
									+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND "
									+ "tsk.task_cat_law_name != 'Internal' ";

							// tttrn.auditorStatus IN ('Complied', 'NonComplied') AND
							// tttrn.auditor_performer_by_id != 0
							if (!date_from.equalsIgnoreCase("0") && !date_to.equalsIgnoreCase("0")) {
								sql += " AND  tttrn.ttrn_legal_due_date BETWEEN '" + date_from + "' AND '" + date_to
										+ "' ";
							}

							if (!deptId.equalsIgnoreCase("0")) {
								sql += " AND dept.dept_id = '" + deptId + "' ";
							}

							if (!locaId.equalsIgnoreCase("0")) {
								sql += " AND loca.loca_id = '" + locaId + "' ";
							}

							if (!orgaId.equalsIgnoreCase("0")) {
								sql += " AND orga.orga_id = '" + orgaId + "' ";
							}

							// System.out.println("searchMonthlyComplianceAuditChart role 1 Inside
							// Superadmin access : " + sql);

						} else {
							if (user_role_id == 7) {

								sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, "
										+ "tttrn.ttrn_fh_due_date,tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, "
										+ "loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, "
										+ "tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id , "
										+ "usr.user_first_name, usr.user_last_name,tttrn.ttrn_completed_date,tsk.task_cat_law_name as lawNames, tsk.task_cat_law_name as lawNames2, "
										+ "tsk.task_cat_law_name, tsk.task_implication, rwusr.user_first_name as rwFirstNames , rwusr.user_last_name as rwLastNames, fhusr.user_first_name as fhFirstNames, "
										+ "fhusr.user_last_name as fhLastNames, tttrn.auditDate, tttrn.reOpenDateWindow, tttrn.auditoComments, tttrn.auditorAuditTime, "
										+ "tttrn.auditorStatus, tttrn.auditor_performer_by_id, "
										+ "tttrn.isAuditTasks, tttrn.isDocumentUpload, tttrn.ttrn_tasks_status  FROM trn_task_transactional tttrn "
										+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
										+ " JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND "
										+ "umapp.umap_dept_id = tmapp.tmap_dept_id "
										+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
										+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
										+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
										+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
										+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
										+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
										+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
										+ "WHERE  tttrn.ttrn_is_Task_Audited = 'Yes' AND tmapp.tmap_enable_status != 0 AND "
										+ "tttrn.ttrn_frequency_for_operation !='User_Defined'  AND "
										+ "tsk.task_cat_law_name != 'Internal' " + " ";

								// tttrn.auditorStatus IN ('Complied', 'NonComplied') AND
								// tttrn.auditor_performer_by_id != 0

								if (!date_from.equalsIgnoreCase("0") && !date_to.equalsIgnoreCase("0")) {
									sql += " AND  tttrn.ttrn_legal_due_date BETWEEN '" + date_from + "' AND '" + date_to
											+ "' ";
								}

								if (!deptId.equalsIgnoreCase("0")) {
									sql += " AND dept.dept_id = '" + deptId + "' ";
								}

								if (!locaId.equalsIgnoreCase("0")) {
									sql += " AND loca.loca_id = '" + locaId + "' ";
								}

								if (!orgaId.equalsIgnoreCase("0")) {
									sql += " AND orga.orga_id = '" + orgaId + "' ";
								}

								// System.out.println("searchMonthlyComplianceAuditChart role 7 Inside
								// Superadmin access : " + sql);
							}
						}
					}
				}
			}

			// System.out.println("searchMonthlyComplianceAuditChart Main Task Query: " +
			// sql);
			Query query = em.createNativeQuery(sql);
			/*
			 * if (user_role_id != 7) { query.setParameter("user_id", user_id); }
			 */
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

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> searchAuditDashboard(int user_id, int user_role_id, String jsonString) {

		try {

			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String minusDays = jsonObj.get("date_from") != null ? jsonObj.get("date_from").toString() : "0";
			String plusDays = jsonObj.get("date_to") != null ? jsonObj.get("date_to").toString() : "0";

			// System.out.println("searchAuditDashboard role & Ids : " + user_role_id + "\t
			// user_id : " + user_id);

			String sql = "";
			// System.out.println(user_id + " id " + user_role_id);
			if (user_role_id > 3 && user_role_id != 7) {
				sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date, "
						+ "tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, "
						+ "dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, "
						+ "tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, "
						+ "usr.user_id as usrId, usr.user_first_name as uFNames, usr.user_last_name as usLastNames, "
						+ "tttrn.ttrn_completed_date, tsk.task_cat_law_name as t1, tsk.task_cat_law_name as t2, tsk.task_cat_law_name as t3, tsk.task_implication, "
						+ "rwusr.user_first_name as rwFirstNames , rwusr.user_last_name as rwLastNames, "
						+ "fhusr.user_first_name as fhFirstNames, fhusr.user_last_name as fhLastNames, tttrn.auditDate, tttrn.reOpenDateWindow, "
						+ "tttrn.auditoComments, tttrn.auditorAuditTime, tttrn.auditorStatus, tttrn.auditor_performer_by_id, "
						+ "tttrn.isAuditTasks, tttrn.isDocumentUpload, tttrn.ttrn_tasks_status  FROM trn_task_transactional tttrn "
						+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
						+ " JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND "
						+ "umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = '" + user_id + "' "
						+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
						+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
						+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
						+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
						+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
						+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
						+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
						+ "WHERE  tttrn.ttrn_is_Task_Audited = 'Yes'  AND tttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND "
						+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
						+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays + "'";

				// tttrn.auditorStatus IN ('Complied', 'NonComplied', 'Re_Opened') AND
				// tttrn.auditor_performer_by_id != 0
				// System.out.println("searchAuditDashboard role > 3 & != 7 " + sql);

			} else {
				if (user_role_id == 3) {

					sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date, "
							+ "tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, "
							+ "dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, "
							+ "tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id , usr.user_first_name, usr.user_last_name, "
							+ "tttrn.ttrn_completed_date,tsk.task_cat_law_name as t1, tsk.task_cat_law_name as t2, tsk.task_cat_law_name, tsk.task_implication, "
							+ "rwusr.user_first_name as rwFirstNames , rwusr.user_last_name as rwLastNames, fhusr.user_first_name as fhFirstNames, fhusr.user_last_name as fhLastNames "
							+ " , tttrn.auditDate, tttrn.reOpenDateWindow, tttrn.auditoComments, tttrn.auditorAuditTime, tttrn.auditorStatus, tttrn.auditor_performer_by_id, "
							+ "tttrn.isAuditTasks, tttrn.isDocumentUpload, tttrn.ttrn_tasks_status  FROM trn_task_transactional tttrn "
							+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
							+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
							+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
							+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
							+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
							+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
							+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
							+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id " + "WHERE "
							+ "(tmapp.tmap_pr_user_id = '" + user_id + "' OR  tmapp.tmap_rw_user_id = '" + user_id
							+ "' OR  tmapp.tmap_fh_user_id = '" + user_id + "') AND  "
							+ "  tttrn.ttrn_is_Task_Audited = 'Yes'  AND tttrn.ttrn_status != 'Inactive'  AND tmapp.tmap_enable_status != 0 AND "
							+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
							+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays + "'";

					// tttrn.auditorStatus IN ('Complied', 'NonComplied', 'Re_Opened') AND
					// tttrn.auditor_performer_by_id != 0 AND
					// System.out.println("searchAuditDashboard role 3 access : " + sql);

				} else {

					if (user_role_id == 12) {

						sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date, "
								+ "tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, "
								+ "dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, "
								+ "tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id , usr.user_first_name, usr.user_last_name, "
								+ "tttrn.ttrn_completed_date, tsk.task_cat_law_name, tsk.task_cat_law_name as t1, tsk.task_cat_law_name as t2, tsk.task_implication, "
								+ "rwusr.user_first_name as rwFirstNames , rwusr.user_last_name as rwLastNames, "
								+ "fhusr.user_first_name as fhFirstNames, fhusr.user_last_name as fhLastNames, tttrn.auditDate, tttrn.reOpenDateWindow, "
								+ "tttrn.auditoComments, tttrn.auditorAuditTime, tttrn.auditorStatus, tttrn.auditor_performer_by_id, "
								+ "tttrn.isAuditTasks, tttrn.isDocumentUpload, tttrn.ttrn_tasks_status  FROM trn_task_transactional tttrn "
								+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
								+ " JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND "
								+ "umapp.umap_dept_id = tmapp.tmap_dept_id AND usr.user_id = '" + user_id + "' "
								+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
								+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
								+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
								+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
								+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
								+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
								+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
								+ "WHERE tttrn.ttrn_is_Task_Audited = 'Yes'   AND tttrn.ttrn_status != 'Inactive'  AND tmapp.tmap_enable_status != 0 AND "
								+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
								+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays + "'";

						// tttrn.auditorStatus IN ('Complied', 'NonComplied', 'Re_Opened') AND
						// tttrn.auditor_performer_by_id != 0 AND
						// System.out.println("searchAuditDashboard user_role_id == 12 access : " +
						// sql);

					}

					if (user_role_id == 2) {
						sql = "SELECT distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, tttrn.ttrn_fh_due_date, "
								+ "tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, "
								+ "dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, tsk.task_activity_when, tsk.task_activity, "
								+ "tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id , usr.user_first_name, usr.user_last_name, "
								+ "tttrn.ttrn_completed_date,tsk.task_cat_law_name as t1Cast, tsk.task_cat_law_name as t2, tsk.task_cat_law_name as r2, "
								+ "tsk.task_implication, rwusr.user_first_name as rwFirstNames , rwusr.user_last_name as rwLastNames, fhusr.user_first_name as fhFirstNames, fhusr.user_last_name as fhLastNames "
								+ " , tttrn.auditDate, tttrn.reOpenDateWindow, tttrn.auditoComments, tttrn.auditorAuditTime, tttrn.auditorStatus, tttrn.auditor_performer_by_id, "
								+ "tttrn.isAuditTasks, tttrn.isDocumentUpload, tttrn.ttrn_tasks_status  FROM trn_task_transactional tttrn "
								+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
								+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
								+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
								+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
								+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
								+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
								+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
								+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id " + "WHERE "
								+ "(tmapp.tmap_pr_user_id = '" + user_id + "' OR  tmapp.tmap_rw_user_id = '" + user_id
								+ "') " + "AND "
								+ "   tttrn.ttrn_is_Task_Audited = 'Yes'   AND tttrn.ttrn_status != 'Inactive'  AND tmapp.tmap_enable_status != 0 AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
								+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays + "'";

						// tttrn.auditorStatus IN ('Complied', 'NonComplied', 'Re_Opened') AND
						// tttrn.auditor_performer_by_id != 0
						// System.out.println("searchAuditDashboard role 2 access : " + sql);
					} else {
						if (user_role_id == 1) {
							sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, "
									+ "tttrn.ttrn_fh_due_date,tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, loca.loca_id, "
									+ "loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, "
									+ "tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id , "
									+ "usr.user_first_name, usr.user_last_name,tttrn.ttrn_completed_date,tsk.task_cat_law_name as t1,tsk.task_cat_law_name as t5, "
									+ "tsk.task_cat_law_name,tsk.task_implication, rwusr.user_first_name as rwFirstNames , rwusr.user_last_name as rwLastNames, "
									+ "fhusr.user_first_name as fhFirstNames, fhusr.user_last_name as fhLastNames "
									+ " , tttrn.auditDate, tttrn.reOpenDateWindow, tttrn.auditoComments, tttrn.auditorAuditTime, tttrn.auditorStatus, tttrn.auditor_performer_by_id, "
									+ "tttrn.isAuditTasks, tttrn.isDocumentUpload, tttrn.ttrn_tasks_status  FROM trn_task_transactional tttrn "
									+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
									+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
									+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
									+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
									+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
									+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
									+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
									+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id WHERE "
									+ "(tmapp.tmap_pr_user_id = '" + user_id + "') " + "AND "
									+ "  tttrn.ttrn_is_Task_Audited = 'Yes'   AND tttrn.ttrn_status != 'Inactive'  AND tmapp.tmap_enable_status != 0 AND "
									+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND "
									+ "tsk.task_cat_law_name != 'Internal' " + "and tttrn.ttrn_legal_due_date BETWEEN '"
									+ minusDays + "' and '" + plusDays + "'";

							// tttrn.auditorStatus IN ('Complied', 'NonComplied', 'Re_Opened')
							// System.out.println("searchAuditDashboard role 1 Inside Superadmin access : "
							// + sql);

						} else {
							if (user_role_id == 7) {

								sql = "SELECT Distinct tttrn.ttrn_id, tttrn.ttrn_client_task_id, tttrn.ttrn_status ,tttrn.ttrn_pr_due_date, tttrn.ttrn_rw_due_date, "
										+ "tttrn.ttrn_fh_due_date,tttrn.ttrn_uh_due_date, tttrn.ttrn_legal_due_date, tttrn.ttrn_submitted_date, orga.orga_id, orga.orga_name, "
										+ "loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name , tsk.task_legi_name , tsk.task_rule_name, tsk.task_reference , tsk.task_activity_who, "
										+ "tsk.task_activity_when, tsk.task_activity, tsk.task_procedure, tsk.task_impact, tttrn.ttrn_frequency_for_operation, usr.user_id , "
										+ "usr.user_first_name, usr.user_last_name,tttrn.ttrn_completed_date,tsk.task_cat_law_name as t2, tsk.task_cat_law_name as t3, "
										+ "tsk.task_cat_law_name, tsk.task_implication, rwusr.user_first_name as rwFirstNames , rwusr.user_last_name as rwLastNames, fhusr.user_first_name as fhFirstNames, "
										+ "fhusr.user_last_name as fhLastNames, tttrn.auditDate, tttrn.reOpenDateWindow, tttrn.auditoComments, tttrn.auditorAuditTime, "
										+ "tttrn.auditorStatus, tttrn.auditor_performer_by_id, "
										+ "tttrn.isAuditTasks, tttrn.isDocumentUpload, tttrn.ttrn_tasks_status  FROM trn_task_transactional tttrn "
										+ "LEFT JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
										+ " JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND "
										+ "umapp.umap_dept_id = tmapp.tmap_dept_id "
										+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
										+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
										+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
										+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
										+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
										+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
										+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
										+ "WHERE tttrn.ttrn_is_Task_Audited = 'Yes'   AND tttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND "
										+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
										+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays
										+ "'";

								// tttrn.auditorStatus IN ('Complied', 'NonComplied', 'Re_Opened')
								// System.out.println("searchAuditDashboard role 7 Inside Superadmin access : "
								// + sql);
							}
						}
					}
				}
			}

			// System.out.println("auditTaskDashboard Main Task Query: " + sql);
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
	public <T> List<T> getTaskActivityListURL(String jsonString, HttpSession session) {
		try {
//			String sql = "SELECT distinct task_id, task_activity, task_form_no FROM mst_task WHERE task_form_no != 'NA' AND task_enable_status = 1 ";
			String sql = "select distinct task_state_id, task_state_name from mst_task order by task_state_id asc";
			Query createQuery = em.createNativeQuery(sql);
			List resultList = createQuery.getResultList();
			if (resultList.size() > 0) {
				return resultList;
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
	public <T> List<T> finalComplianceAuditReport(String orgaId, String locaId, String deptId, String minusDays,
			String plusDays, HttpSession session) {

		try {

			Integer user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			Integer user_role_id = Integer.parseInt(session.getAttribute("sess_role_id").toString());

			// System.out.println("final audit report role & Ids : " + user_role_id + "\t
			// user_id : " + user_id);

			String sql = "";
			// System.out.println(user_id + " id " + user_role_id);
			if (user_role_id > 3 && user_role_id != 7) {
				sql = "SELECT "
						+ "DISTINCT tttrn.ttrn_id, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, "
						+ "COUNT(DISTINCT IF(tttrn.auditorStatus = 'Complied', tttrn.ttrn_id, null)) AS 'Complied', "
						+ "COUNT(DISTINCT IF(tttrn.auditorStatus = 'NonComplied', tttrn.ttrn_id, null)) AS 'NonComplied', "
						+ "COUNT(DISTINCT IF(tttrn.auditorStatus = 'Re_Opened', tttrn.ttrn_id, null)) AS 'Re_Opened',"
						+ " Concat(orga.orga_id,'_',loca.loca_id,'_',dept.dept_id) AS 'Verti_Unit_Function'  "
						+ " FROM trn_task_transactional tttrn "
						+ " JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
						+ " JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND "
						+ "umapp.umap_dept_id = tmapp.tmap_dept_id AND umapp.umap_user_id = '" + user_id + "' "
						+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
						+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
						+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
						+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
						+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
						+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
						+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
						+ "WHERE  tttrn.ttrn_is_Task_Audited = 'Yes' AND tttrn.auditorStatus != 'Re_Opened'  AND tttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND "
						+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
						+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays + "' ";

				if (!deptId.equalsIgnoreCase("0")) {
					sql += " AND dept.dept_id = '" + deptId + "' ";
				}

				if (!locaId.equalsIgnoreCase("0")) {
					sql += " AND loca.loca_id = '" + locaId + "' ";
				}

				if (!orgaId.equalsIgnoreCase("0")) {
					sql += " AND orga.orga_id = '" + orgaId + "' ";
				}

				String gOSql = " group by orga.orga_id, loca.loca_id, dept.dept_id order by orga.orga_id , loca.loca_id , dept.dept_id, tttrn.auditorStatus ";
				sql += gOSql;

				// System.out.println("final audit report role > 3 & != 7 " + sql);

			} else {
				if (user_role_id == 3) {

					sql = "SELECT "
							+ "DISTINCT tttrn.ttrn_id, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, "
							+ "COUNT(DISTINCT IF(tttrn.auditorStatus = 'Complied', tttrn.ttrn_id, null)) AS 'Complied', "
							+ "COUNT(DISTINCT IF(tttrn.auditorStatus = 'NonComplied', tttrn.ttrn_id, null)) AS 'NonComplied', "
							+ "COUNT(DISTINCT IF(tttrn.auditorStatus = 'Re_Opened', tttrn.ttrn_id, null)) AS 'Re_Opened',"
							+ " Concat(orga.orga_id,'_',loca.loca_id,'_',dept.dept_id) AS 'Verti_Unit_Function'  "
							+ " FROM trn_task_transactional tttrn "
							+ " JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
							+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
							+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
							+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
							+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
							+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
							+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
							+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id " + "WHERE "
							+ "(tmapp.tmap_pr_user_id = '" + user_id + "' OR  tmapp.tmap_rw_user_id = '" + user_id
							+ "' OR  tmapp.tmap_fh_user_id = '" + user_id + "') AND  "
							+ "  tttrn.ttrn_is_Task_Audited = 'Yes' AND tttrn.auditorStatus != 'Re_Opened'  AND tttrn.ttrn_status != 'Inactive'  AND tmapp.tmap_enable_status != 0 AND "
							+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
							+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays + "' ";

					if (!deptId.equalsIgnoreCase("0")) {
						sql += " AND dept.dept_id = '" + deptId + "' ";
					}

					if (!locaId.equalsIgnoreCase("0")) {
						sql += " AND loca.loca_id = '" + locaId + "' ";
					}

					if (!orgaId.equalsIgnoreCase("0")) {
						sql += " AND orga.orga_id = '" + orgaId + "' ";
					}

					String gOSql = " group by orga.orga_id, loca.loca_id, dept.dept_id order by orga.orga_id , loca.loca_id , dept.dept_id, tttrn.auditorStatus ";
					sql += gOSql;

					// System.out.println("final audit report" + sql);

				} else {

					if (user_role_id == 12) {

						sql = "SELECT "
								+ "DISTINCT tttrn.ttrn_id, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, "
								+ "COUNT(DISTINCT IF(tttrn.auditorStatus = 'Complied', tttrn.ttrn_id, null)) AS 'Complied', "
								+ "COUNT(DISTINCT IF(tttrn.auditorStatus = 'NonComplied', tttrn.ttrn_id, null)) AS 'NonComplied', "
								+ "COUNT(DISTINCT IF(tttrn.auditorStatus = 'Re_Opened', tttrn.ttrn_id, null)) AS 'Re_Opened',"
								+ " Concat(orga.orga_id,'_',loca.loca_id,'_',dept.dept_id) AS 'Verti_Unit_Function' "
								+ " FROM trn_task_transactional tttrn "
								+ " JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
								+ " JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND "
								+ "umapp.umap_dept_id = tmapp.tmap_dept_id AND usr.user_id = '" + user_id + "' "
								+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
								+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
								+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
								+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
								+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
								+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
								+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
								+ "WHERE tttrn.ttrn_is_Task_Audited = 'Yes'  AND tttrn.auditorStatus != 'Re_Opened' AND tttrn.ttrn_status != 'Inactive'  AND tmapp.tmap_enable_status != 0 AND "
								+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
								+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays + "' ";

						if (!deptId.equalsIgnoreCase("0")) {
							sql += " AND dept.dept_id = '" + deptId + "' ";
						}

						if (!locaId.equalsIgnoreCase("0")) {
							sql += " AND loca.loca_id = '" + locaId + "' ";
						}

						if (!orgaId.equalsIgnoreCase("0")) {
							sql += " AND orga.orga_id = '" + orgaId + "' ";
						}

						String gOSql = " group by orga.orga_id, loca.loca_id, dept.dept_id order by orga.orga_id , loca.loca_id , dept.dept_id, tttrn.auditorStatus ";
						sql += gOSql;
						// System.out.println("final audit report user_role_id == 12 access : " + sql);

					}

					if (user_role_id == 2) {
						sql = "SELECT  "
								+ "DISTINCT tttrn.ttrn_id, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, "
								+ "COUNT(DISTINCT IF(tttrn.auditorStatus = 'Complied', tttrn.ttrn_id, null)) AS 'Complied', "
								+ "COUNT(DISTINCT IF(tttrn.auditorStatus = 'NonComplied', tttrn.ttrn_id, null)) AS 'NonComplied', "
								+ "COUNT(DISTINCT IF(tttrn.auditorStatus = 'Re_Opened', tttrn.ttrn_id, null)) AS 'Re_Opened',"
								+ " Concat(orga.orga_id,'_',loca.loca_id,'_',dept.dept_id) AS 'Verti_Unit_Function'  "
								+ " FROM trn_task_transactional tttrn "
								+ " JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
								+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
								+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
								+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
								+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
								+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
								+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
								+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id " + "WHERE "
								+ "(tmapp.tmap_pr_user_id = '" + user_id + "' OR  tmapp.tmap_rw_user_id = '" + user_id
								+ "') " + "AND "
								+ "   tttrn.ttrn_is_Task_Audited = 'Yes'  AND tttrn.auditorStatus != 'Re_Opened' AND tttrn.ttrn_status != 'Inactive'  AND tmapp.tmap_enable_status != 0 "
								+ "AND tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
								+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays + "' ";

						if (!deptId.equalsIgnoreCase("0")) {
							sql += " AND dept.dept_id = '" + deptId + "' ";
						}

						if (!locaId.equalsIgnoreCase("0")) {
							sql += " AND loca.loca_id = '" + locaId + "' ";
						}

						if (!orgaId.equalsIgnoreCase("0")) {
							sql += " AND orga.orga_id = '" + orgaId + "' ";
						}

						String gOSql = " group by orga.orga_id, loca.loca_id, dept.dept_id order by orga.orga_id , loca.loca_id , dept.dept_id, tttrn.auditorStatus ";
						sql += gOSql;

						// System.out.println("final audit report role 2 access : " + sql);
					} else {
						if (user_role_id == 1) {
							sql = "SELECT "
									+ " DISTINCT tttrn.ttrn_id, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, "
									+ "COUNT(DISTINCT IF(tttrn.auditorStatus = 'Complied', tttrn.ttrn_id, null)) AS 'Complied', "
									+ "COUNT(DISTINCT IF(tttrn.auditorStatus = 'NonComplied', tttrn.ttrn_id, null)) AS 'NonComplied', "
									+ "COUNT(DISTINCT IF(tttrn.auditorStatus = 'Re_Opened', tttrn.ttrn_id, null)) AS 'Re_Opened', "
									+ "Concat(orga.orga_id,'_',loca.loca_id,'_',dept.dept_id) AS 'Verti_Unit_Function' "
									+ " FROM trn_task_transactional tttrn "
									+ " JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
									+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
									+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
									+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
									+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
									+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
									+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
									+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id WHERE "
									+ "(tmapp.tmap_pr_user_id = '" + user_id + "') " + "AND "
									+ "  tttrn.ttrn_is_Task_Audited = 'Yes'  AND tttrn.auditorStatus != 'Re_Opened' AND tttrn.ttrn_status != 'Inactive'  AND tmapp.tmap_enable_status != 0 AND "
									+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND "
									+ "tsk.task_cat_law_name != 'Internal' " + "and tttrn.ttrn_legal_due_date BETWEEN '"
									+ minusDays + "' and '" + plusDays + "' ";

							if (!deptId.equalsIgnoreCase("0")) {
								sql += " AND dept.dept_id = '" + deptId + "' ";
							}

							if (!locaId.equalsIgnoreCase("0")) {
								sql += " AND loca.loca_id = '" + locaId + "' ";
							}

							if (!orgaId.equalsIgnoreCase("0")) {
								sql += " AND orga.orga_id = '" + orgaId + "' ";
							}

							String gOSql = " group by orga.orga_id, loca.loca_id, dept.dept_id order by orga.orga_id , loca.loca_id , dept.dept_id, tttrn.auditorStatus ";
							sql += gOSql;

							// System.out.println("final audit report role 1 Inside Superadmin access : " +
							// sql);

						} else {
							if (user_role_id == 7) {

								sql = "SELECT "
										+ " DISTINCT tttrn.ttrn_id, orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, dept.dept_id, dept.dept_name, "
										+ "COUNT(DISTINCT IF(tttrn.auditorStatus = 'Complied', tttrn.ttrn_id, null)) AS 'Complied', "
										+ "COUNT(DISTINCT IF(tttrn.auditorStatus = 'NonComplied', tttrn.ttrn_id, null)) AS 'NonComplied', "
										+ "COUNT(DISTINCT IF(tttrn.auditorStatus = 'Re_Opened', tttrn.ttrn_id, null)) AS 'Re_Opened',"
										+ " Concat(orga.orga_id,'_',loca.loca_id,'_',dept.dept_id) AS 'Verti_Unit_Function' "
										+ " FROM trn_task_transactional tttrn "
										+ " JOIN cfg_task_user_mapping tmapp on tttrn.ttrn_client_task_id = tmapp.tmap_client_tasks_id "
										+ " JOIN cfg_user_entity_mapping umapp on umapp.umap_orga_id = tmapp.tmap_orga_id AND umapp.umap_loca_id = tmapp.tmap_loca_id AND "
										+ "umapp.umap_dept_id = tmapp.tmap_dept_id "
										+ " JOIN mst_task tsk on tsk.task_id = tmapp.tmap_task_id "
										+ "JOIN mst_organization orga on orga.orga_id = tmapp.tmap_orga_id "
										+ "JOIN mst_location loca on loca.loca_id = tmapp.tmap_loca_id "
										+ "JOIN mst_department dept on dept.dept_id = tmapp.tmap_dept_id "
										+ "JOIN mst_user usr on usr.user_id = tmapp.tmap_pr_user_id "
										+ "JOIN mst_user rwusr on rwusr.user_id = tmapp.tmap_rw_user_id "
										+ "JOIN mst_user fhusr on fhusr.user_id = tmapp.tmap_fh_user_id "
										+ "WHERE tttrn.ttrn_is_Task_Audited = 'Yes' AND tttrn.auditorStatus != 'Re_Opened'  AND tttrn.ttrn_status != 'Inactive' AND tmapp.tmap_enable_status != 0 AND "
										+ "tttrn.ttrn_frequency_for_operation !='User_Defined' AND tsk.task_cat_law_name != 'Internal' "
										+ "and tttrn.ttrn_legal_due_date BETWEEN '" + minusDays + "' and '" + plusDays
										+ "' ";

								if (!deptId.equalsIgnoreCase("0")) {
									sql += " AND dept.dept_id = '" + deptId + "' ";
								}

								if (!locaId.equalsIgnoreCase("0")) {
									sql += " AND loca.loca_id = '" + locaId + "' ";
								}

								if (!orgaId.equalsIgnoreCase("0")) {
									sql += " AND orga.orga_id = '" + orgaId + "' ";
								}

								String gOSql = " group by orga.orga_id, loca.loca_id, dept.dept_id order by orga.orga_id , loca.loca_id , dept.dept_id, tttrn.auditorStatus ";
								sql += gOSql;

								// System.out.println("final audit report role 7 Inside Superadmin access : " +
								// sql);
							}
						}
					}
				}
			}

			// System.out.println("final audit report : " + sql);
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
	public <T> List<T> getFunctionListByOrgaId(String orgaId, String locaId, HttpSession session) {
		try {
			String sql = "select distinct orga.orga_id, orga.orga_name, loca.loca_id, loca.loca_name, "
					+ "dept.dept_id, dept.dept_name from mst_organization orga "
					+ "JOIN cfg_entity_mapping emap ON emap.enti_orga_id = orga.orga_id "
					+ "JOIN mst_location loca ON emap.enti_loca_id = loca.loca_id "
					+ "JOIN mst_department dept ON emap.enti_dept_id = dept.dept_id where orga.orga_id = '" + orgaId
					+ "' ";

			if (!locaId.equalsIgnoreCase("0")) {
				sql += " AND loca.loca_id = '" + locaId + "' ";
			}

			// System.out.println("getFunctionListByOrgaId sql : " + sql);

			Query createNativeQuery = em.createNativeQuery(sql);
			List resultList = createNativeQuery.getResultList();
			if (resultList.size() > 0 || resultList != null) {
				return resultList;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
