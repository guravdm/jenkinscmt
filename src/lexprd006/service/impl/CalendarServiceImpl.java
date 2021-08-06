package lexprd006.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lexprd006.dao.CalendarDao;
import lexprd006.dao.TasksDao;
import lexprd006.dao.UploadedDocumentsDao;
import lexprd006.dao.UsersDao;
import lexprd006.domain.CalendarDomain;
import lexprd006.domain.SubTask;
import lexprd006.domain.SubTaskTranscational;
import lexprd006.domain.UploadedDocuments;
import lexprd006.domain.UploadedSubTaskDocuments;
import lexprd006.domain.User;
import lexprd006.service.CalendarService;
import lexprd006.service.UtilitiesService;

/*
 * Author: Mahesh Kharote
 * Date: 02/03/2016
 * Updated By:
 * Updated Date: 
 * 
 * */

@Service(value = "calendarService")
public class CalendarServiceImpl implements CalendarService {

	public final SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd");
	public final SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd");
	public final SimpleDateFormat sdfOutForDisplay = new SimpleDateFormat("dd-MM-yyyy");

	@Autowired
	CalendarDao calendarDao;
	@Autowired
	UtilitiesService utilitiesService;
	@Autowired
	TasksDao tasksDao;
	@Autowired
	UsersDao usersDao;

	@Autowired
	UploadedDocumentsDao uploadedDocumentsDao;

	// Method Created By: Mahesh Kharote
	// Method Purpose: Get User Id set in session
	@Override
	public List<CalendarDomain> getAllTasksAssignedToUserForCalendarPerformerWise(int pr_id) throws ParseException {
		try {
			List<CalendarDomain> allTasksToSend = new ArrayList<>();
			List<Object> asBoth = calendarDao.getAllTasksAssignedToUserForCalendarWhereUserIsPerformer(pr_id);

			Iterator<Object> itr = asBoth.iterator();
			while (itr.hasNext()) {
				CalendarDomain cal = new CalendarDomain();
				Object[] object = (Object[]) itr.next();
				// adding the ttrn_id to a list for further use

				cal.setTtrn_id(object[0].toString());
				cal.setClient_task_id(object[1].toString());
				cal.setPerformer_due_date(object[2].toString());
				cal.setTask_status(object[3].toString());

				allTasksToSend.add(cal);

			}

			return allTasksToSend;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Mahesh Kharote
	// Method Purpose: Get User Id set in session

	@Override
	public <T> List<T> getAllTasksAssignedToUserForCalendarReviewerWise(int rw_id) throws ParseException {
		try {
			return calendarDao.getAllTasksAssignedToUserForCalendarWhereUserIsReviwer(rw_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Mahesh Kharote
	// Method Purpose: Get User Id set in session
	@SuppressWarnings("unused")
	@Override
	public List<CalendarDomain> getAllTasksAssignedToUserForCalendarBothWise(int user_id) throws ParseException {
		try {
			SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd");
			List<CalendarDomain> allTasksToSend = new ArrayList<>();
			List<Object> asBoth = calendarDao.getAllTasksAssignedToUserForCalendarWhereUserIsPerformer(user_id);

			List<Integer> existingTasksList = new ArrayList<Integer>();
			Iterator<Object> itr = asBoth.iterator();
			while (itr.hasNext()) {
				CalendarDomain cal = new CalendarDomain();
				Object[] object = (Object[]) itr.next();
				// adding the ttrn_id to a list for further use
				existingTasksList.add(Integer.parseInt(object[0].toString()));
				cal.setTtrn_id(object[0].toString());
				cal.setClient_task_id(object[1].toString());
				cal.setPerformer_due_date(object[2].toString());
				cal.setTask_status(object[3].toString());

				allTasksToSend.add(cal);

			}

			List<Object> asReviewer = calendarDao.getAllTasksAssignedToUserForCalendarWhereUserIsReviwer(user_id);
			Iterator<Object> itrasReviewer = asReviewer.iterator();
			while (itrasReviewer.hasNext()) {
				Object[] rw = (Object[]) itrasReviewer.next();
				// Checking the ttrn_id is already present in the list, if it is present i.e. if
				// the same user is performer
				// as well as reviewer for the same task dont add the same task again
				if (existingTasksList.contains(rw[0])) {//

				} else {
					CalendarDomain cal = new CalendarDomain();
					Object[] object = (Object[]) itr.next();

					cal.setTtrn_id(object[0].toString());
					cal.setClient_task_id(object[1].toString());
					cal.setPerformer_due_date(object[2].toString());
					cal.setTask_status(object[3].toString());

					allTasksToSend.add(cal);
				}
			}

			return allTasksToSend;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// Method Created By: Mahesh Kharote
	// Method Purpose: Get User Id set in session
	@Override
	public <T> List<T> getAllTasksAssignedToUserForCalendarOrganizationWise(int tmap_performer_user_id)
			throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	// Method Created By: Mahesh Kharote
	// Method Purpose: Get User Id set in session
	@Override
	public <T> List<T> getAllTasksAssignedToUserForCalendarLocationWise(int tmap_performer_user_id)
			throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	// Method Created By: Mahesh Kharote
	// Method Purpose: Get User Id set in session
	@SuppressWarnings({ "unused", "unchecked" })
	@Override
	public String getAllTasksAssignedToUserForCalendarDepartmentWise(HttpSession session) throws ParseException {
		JSONObject dataForSend = new JSONObject();
		try {
			SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdfOut = new SimpleDateFormat("dd-MM-yyyy");
			List<CalendarDomain> allTasksToSend = new ArrayList<>();

			JSONArray sendArray = new JSONArray();
			Date completedOn = null;
			Date submittedDate = null;
			int user_role = Integer.parseInt(session.getAttribute("sess_role_id").toString());
			// Get main task
			List<Object> asBoth = calendarDao.getAllTasksOrgaLocaDeptWise(
					Integer.parseInt(session.getAttribute("sess_user_id").toString()),
					Integer.parseInt(session.getAttribute("sess_role_id").toString()));

			if (asBoth != null) {
				Iterator<Object> itr = asBoth.iterator();
				while (itr.hasNext()) {
					JSONObject jsonData = new JSONObject();
					Object[] object = (Object[]) itr.next();
					// adding the ttrn_id to a list for further use

					if (object[2] != null && object[3] != null && object[8] != null && object[9] != null
							&& object[10] != null) {
						int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
						// int user_id = 8;
						int per_id = Integer.parseInt(object[7].toString());
						int rev_id = Integer.parseInt(object[11].toString());
						Date performerDate = sdfIn.parse(object[2].toString());
						Date legalDueDate = sdfIn.parse(object[3].toString());
						Date rwDueDate = sdfIn.parse(object[8].toString());
						Date fhDueDate = sdfIn.parse(object[9].toString());
						Date uhDueDate = sdfIn.parse(object[10].toString());

						DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						DateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

						String dd = formatter1.format(formatter.parse(object[2].toString())); // Change format for
																								// performer due date
						Date per_due_date = formatter1.parse(dd);
						dd = formatter1.format(utilitiesService.getCurrentDate()); // Change format for current date
						Date cur_date = formatter1.parse(dd);

						jsonData.put("client_task_id", object[1].toString());
						jsonData.put("performer_due_date", object[2].toString());
						jsonData.put("task_which_entity_wise", "Performer Wise");// Just to show some color, work has be
																					// done on this
						jsonData.put("task_activity", object[6].toString().replaceAll("\'", "").replaceAll("\"", "")
								.replaceAll(":", "").replaceAll("-", "").replaceAll(";", ""));// Because single quotes
																								// and double quotes
																								// will disturb the
																								// format of json

						jsonData.put("task_frequency", object[12].toString());
						jsonData.put("task_performer_id", object[7]);
						jsonData.put("task_reviewer_id", object[11]);
						jsonData.put("task_orga_id", object[13]);
						jsonData.put("task_loca_id", object[14]);
						jsonData.put("task_dept_id", object[15]);
						jsonData.put("task_function_head_id", object[17]);

						if (user_role == 1) {
							jsonData.put("calender_date", object[2].toString());
						}
						if (user_role == 2) {
							if (per_id == user_id) {
								if (rev_id == user_id && !cur_date.after(rwDueDate) && !cur_date.equals(per_due_date)) { // if
																															// reviewer
																															// and
																															// current
																															// date
																															// not
																															// after
																															// reviewer
																															// date
									jsonData.put("calender_date", object[8].toString());
								} else {
									if (cur_date.after(per_due_date) && !cur_date.equals(per_due_date)) {
										jsonData.put("calender_date", object[8].toString());
									} else {
										jsonData.put("calender_date", object[2].toString());
									}
								}
							} else {
								jsonData.put("calender_date", object[8].toString());
							}
						}
						if (user_role == 3) {
							if (per_id == user_id) {
								if (rev_id == user_id && !cur_date.after(rwDueDate) && !cur_date.equals(per_due_date)) { // if
																															// reviewer
																															// and
																															// current
																															// date
																															// not
																															// after
																															// reviewer
																															// date
									jsonData.put("calender_date", object[8].toString());
								} else {
									if (cur_date.after(per_due_date) && !cur_date.equals(per_due_date)) {
										jsonData.put("calender_date", object[9].toString());
									} else {
										jsonData.put("calender_date", object[2].toString());
									}
								}
							} else {
								if (rev_id == user_id && !cur_date.after(rwDueDate)) { // if reviewer and current date
																						// not after reviewer date
									jsonData.put("calender_date", object[8].toString());
								} else {
									jsonData.put("calender_date", object[9].toString());
								}
							}
						}
						if (user_role == 4) {
							if (per_id == user_id) {
								if (rev_id == user_id && !cur_date.after(rwDueDate) && !cur_date.equals(per_due_date)) { // if
																															// reviewer
																															// and
																															// current
																															// date
																															// not
																															// after
																															// reviewer
																															// date
									jsonData.put("calender_date", object[8].toString());
								} else {
									if (cur_date.after(per_due_date) && !cur_date.equals(per_due_date)) {
										jsonData.put("calender_date", object[10].toString());
									} else {
										jsonData.put("calender_date", object[2].toString());
									}
								}

							} else {
								if (rev_id == user_id && !cur_date.after(rwDueDate)) { // if reviewer and current date
																						// not after reviewer date
									jsonData.put("calender_date", object[8].toString());
								} else {
									jsonData.put("calender_date", object[10].toString());
								}
							}
						}
						if (user_role > 4) {
							if (per_id == user_id) {
								if (rev_id == user_id && !cur_date.after(rwDueDate) && !cur_date.equals(per_due_date)) { // if
																															// reviewer
																															// and
																															// current
																															// date
																															// not
																															// after
																															// reviewer
																															// date
									jsonData.put("calender_date", object[8].toString());
								} else {
									if (cur_date.after(per_due_date) && !cur_date.equals(per_due_date)) {
										jsonData.put("calender_date", object[3].toString());
									} else {
										jsonData.put("calender_date", object[2].toString());
									}
								}
							} else {
								if (rev_id == user_id && !cur_date.after(rwDueDate) || cur_date.equals(rwDueDate)) { // if
																														// reviewer
																														// and
																														// current
																														// date
																														// before
																														// reviewer
																														// date
									jsonData.put("calender_date", object[8].toString());
								} else {
									jsonData.put("calender_date", object[3].toString());
								}
							}
						}

						Date completedDate = null;

						if (object[4] != null) {
							completedDate = sdfIn.parse(object[4].toString());
						}

						// cal.setTask_status(object[5].toString());

						if (object[5].toString().equals("Completed")) {
							if (object[4] != null) {
								submittedDate = sdfIn.parse(object[4].toString());
							}
							if (object[16] != null) {
								completedOn = sdfIn.parse(object[16].toString());
							}
							/*
							 * if(submittedDate.after(legalDueDate) && (completedDate.before(legalDueDate)
							 * || completedDate.equals(legalDueDate) )){ jsonData.put("task_status",
							 * "Delayed_Reported"); }else if(submittedDate.after(legalDueDate) &&
							 * completedDate.after(legalDueDate)){ jsonData.put("task_status", "Delayed"); }
							 */

							if (submittedDate.before(legalDueDate) || submittedDate.equals(legalDueDate)) {
								jsonData.put("task_status", "Complied");
							} else {
								if (submittedDate.after(legalDueDate) && completedOn.after(legalDueDate)) {
									jsonData.put("task_status", "Delayed");
								}
								if (submittedDate.after(legalDueDate)
										&& (completedOn.before(legalDueDate) || completedOn.equals(legalDueDate))) {
									jsonData.put("task_status", "Delayed_Reported");
								}

							}

							/*
							 * if(completedDate.after(legalDueDate) ){ jsonData.put("task_status",
							 * "Delayed"); } else{ jsonData.put("task_status", "Complied"); }
							 */
						}
						if (object[5].toString().equals("Active")) {
							if (utilitiesService.getCurrentDate().after(legalDueDate)) {
								jsonData.put("task_status", "Non Complied");
							} else {
								if (utilitiesService.getCurrentDate().after(performerDate)) {
									jsonData.put("task_status", "Posing Risk");
								} else {
									jsonData.put("task_status", "Pending");
								}

							}
						}
						if (object[5].toString().equals("Partially_Completed")) {
							jsonData.put("task_status", "WaitingForApproval");
						}
						if (object[5].toString().equals("Re_Opened")) {
							jsonData.put("task_status", "Reopned");
						}
						jsonData.put("type", "MainTask");
						sendArray.add(jsonData);
					}
				}
			}
			// END Get main task

			// Get Sub task
			List<Object> subTask = calendarDao.getAllSubTasksOrgaLocaDeptWise(
					Integer.parseInt(session.getAttribute("sess_user_id").toString()),
					Integer.parseInt(session.getAttribute("sess_role_id").toString()));

			if (subTask != null) {
				Iterator<Object> itr = subTask.iterator();
				while (itr.hasNext()) {
					JSONObject jsonData = new JSONObject();
					Object[] object = (Object[]) itr.next();
					// adding the ttrn_id to a list for further use

					if (object[2] != null && object[3] != null && object[8] != null && object[9] != null
							&& object[10] != null) {
						int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
						// int user_id = 8;
						int per_id = Integer.parseInt(object[7].toString());
						int rev_id = Integer.parseInt(object[11].toString());
						Date performerDate = sdfIn.parse(object[2].toString());
						Date legalDueDate = sdfIn.parse(object[3].toString());
						Date rwDueDate = sdfIn.parse(object[8].toString());
						Date fhDueDate = sdfIn.parse(object[9].toString());
						Date uhDueDate = sdfIn.parse(object[10].toString());

						DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						DateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

						String dd = formatter1.format(formatter.parse(object[2].toString())); // Change format for
																								// performer due date
						Date per_due_date = formatter1.parse(dd);
						dd = formatter1.format(utilitiesService.getCurrentDate()); // Change format for current date
						Date cur_date = formatter1.parse(dd);

						jsonData.put("client_task_id", object[1].toString());
						jsonData.put("performer_due_date", object[2].toString());
						jsonData.put("task_which_entity_wise", "Performer Wise");// Just to show some color, work has be
																					// done on this
						jsonData.put("task_activity", object[6].toString().replaceAll("\'", "").replaceAll("\"", "")
								.replaceAll(":", "").replaceAll("-", "").replaceAll(";", ""));// Because single quotes
																								// and double quotes
																								// will disturb the
																								// format of json

						jsonData.put("task_frequency", object[12].toString());
						jsonData.put("task_performer_id", object[7]);
						jsonData.put("task_reviewer_id", object[11]);
						jsonData.put("task_orga_id", object[13]);
						jsonData.put("task_loca_id", object[14]);
						jsonData.put("task_dept_id", object[15]);
						jsonData.put("task_sub_task_id", object[16]);

						if (user_role == 1) {
							jsonData.put("calender_date", object[2].toString());
						}
						if (user_role == 2) {
							if (per_id == user_id) {
								if (rev_id == user_id && !cur_date.after(rwDueDate) && !cur_date.equals(per_due_date)) { // if
																															// reviewer
																															// and
																															// current
																															// date
																															// not
																															// after
																															// reviewer
																															// date
									jsonData.put("calender_date", object[8].toString());
								} else {
									if (cur_date.after(per_due_date) && !cur_date.equals(per_due_date)) {
										jsonData.put("calender_date", object[8].toString());
									} else {
										jsonData.put("calender_date", object[2].toString());
									}
								}
							} else {
								jsonData.put("calender_date", object[8].toString());
							}
						}
						if (user_role == 3) {
							if (per_id == user_id) {
								if (rev_id == user_id && !cur_date.after(rwDueDate) && !cur_date.equals(per_due_date)) { // if
																															// reviewer
																															// and
																															// current
																															// date
																															// not
																															// after
																															// reviewer
																															// date
									jsonData.put("calender_date", object[8].toString());
								} else {
									if (cur_date.after(per_due_date) && !cur_date.equals(per_due_date)) {
										jsonData.put("calender_date", object[9].toString());
									} else {
										jsonData.put("calender_date", object[2].toString());
									}
								}
							} else {
								if (rev_id == user_id && !cur_date.after(rwDueDate)) { // if reviewer and current date
																						// not after reviewer date
									jsonData.put("calender_date", object[8].toString());
								} else {
									jsonData.put("calender_date", object[9].toString());
								}
							}
						}
						if (user_role == 4) {
							if (per_id == user_id) {
								if (rev_id == user_id && !cur_date.after(rwDueDate) && !cur_date.equals(per_due_date)) { // if
																															// reviewer
																															// and
																															// current
																															// date
																															// not
																															// after
																															// reviewer
																															// date
									jsonData.put("calender_date", object[8].toString());
								} else {
									if (cur_date.after(per_due_date) && !cur_date.equals(per_due_date)) {
										jsonData.put("calender_date", object[10].toString());
									} else {
										jsonData.put("calender_date", object[2].toString());
									}
								}

							} else {
								if (rev_id == user_id && !cur_date.after(rwDueDate)) { // if reviewer and current date
																						// not after reviewer date
									jsonData.put("calender_date", object[8].toString());
								} else {
									jsonData.put("calender_date", object[10].toString());
								}
							}
						}
						if (user_role > 4) {

							if (per_id == user_id) {
								if (rev_id == user_id && !cur_date.after(rwDueDate) && !cur_date.equals(per_due_date)) { // if
																															// reviewer
																															// and
																															// current
																															// date
																															// not
																															// after
																															// reviewer
																															// date
									jsonData.put("calender_date", object[8].toString());
								} else {
									if (cur_date.after(per_due_date) && !cur_date.equals(per_due_date)) {
										jsonData.put("calender_date", object[3].toString());
									} else {
										jsonData.put("calender_date", object[2].toString());
									}
								}
							} else {
								if (rev_id == user_id && !cur_date.after(rwDueDate) || cur_date.equals(rwDueDate)) { // if
																														// reviewer
																														// and
																														// current
																														// date
																														// before
																														// reviewer
																														// date
									jsonData.put("calender_date", object[8].toString());
								} else {
									jsonData.put("calender_date", object[3].toString());
								}
							}
						}

						Date completedDate = null;

						if (object[4] != null) {
							completedDate = sdfIn.parse(object[4].toString());
						}

						// cal.setTask_status(object[5].toString());

						if (object[5].toString().equals("Completed")) {
							if (completedDate.after(legalDueDate)) {
								jsonData.put("task_status", "Delayed");
							} else {
								jsonData.put("task_status", "Complied");
							}
						}
						if (object[5].toString().equals("Active")) {
							if (utilitiesService.getCurrentDate().after(legalDueDate)) {
								jsonData.put("task_status", "Non Complied");
							} else {
								if (utilitiesService.getCurrentDate().after(performerDate)) {
									jsonData.put("task_status", "Posing Risk");
								} else {
									jsonData.put("task_status", "Pending");
								}

							}
						}
						if (object[5].toString().equals("Inactive")) {
							jsonData.put("task_status", "Pending");
						}

						if (object[5].toString().equals("Partially_Completed")) {
							jsonData.put("task_status", "WaitingForApproval");
						}
						if (object[5].toString().equals("Re_Opened")) {
							jsonData.put("task_status", "Reopned");
						}
						jsonData.put("type", "SubTask");
						sendArray.add(jsonData);
					}
				}
			}
			// END Get sub task
			dataForSend.put("task_assigned_to_user", sendArray);

			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method created : Harshad Padole on 14-04-2016
	// Method purpose : search task for calendar

	@Override
	public String searchTaskForCalendar(String json, HttpSession session) {
		try {

			String result = calendarDao.searchTaskForCalender(json,
					Integer.parseInt(session.getAttribute("sess_user_id").toString()),
					Integer.parseInt(session.getAttribute("sess_role_id").toString()));
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Created By: Harshad padole(5/7/2016)
	// Method Purpose: Get configured task Frequency from task_transactional
	@Override
	public <T> List<T> getDistinctFrequencyForUser(HttpSession session) {
		try {
			return calendarDao.getDistinctFrequencyForUser(session);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<CalendarDomain> getAllSubTasksAssignedToUserForCalendarDepartmentWise(HttpSession session) {

		try {

			SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<CalendarDomain> allTasksToSend = new ArrayList<>();

			int user_role = Integer.parseInt(session.getAttribute("sess_role_id").toString());
			// int user_role = 8;

			List<Object> asBoth = calendarDao.getAllSubTasksOrgaLocaDeptWise(
					Integer.parseInt(session.getAttribute("sess_user_id").toString()),
					Integer.parseInt(session.getAttribute("sess_role_id").toString()));
			// List<Object> asBoth = calendarDao.getAllSubTasksOrgaLocaDeptWise(8,5);

			if (asBoth != null) {
				Iterator<Object> itr = asBoth.iterator();
				while (itr.hasNext()) {

					CalendarDomain cal = new CalendarDomain();
					Object[] object = (Object[]) itr.next();
					// adding the ttrn_id to a list for further use

					if (object[2] != null && object[3] != null && object[8] != null && object[9] != null
							&& object[10] != null) {
						// int user_id =
						// Integer.parseInt(session.getAttribute("sess_user_id").toString());
						int user_id = 8;
						int per_id = Integer.parseInt(object[7].toString());
						int rev_id = Integer.parseInt(object[11].toString());
						Date performerDate = sdfIn.parse(object[2].toString());
						Date legalDueDate = sdfIn.parse(object[3].toString());
						Date rwDueDate = sdfIn.parse(object[8].toString());

						DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						DateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

						String dd = formatter1.format(formatter.parse(object[2].toString())); // Change format for
																								// performer due date
						Date per_due_date = formatter1.parse(dd);
						dd = formatter1.format(utilitiesService.getCurrentDate()); // Change format for current date
						Date cur_date = formatter1.parse(dd);

						cal.setTtrn_id(object[0].toString());
						cal.setClient_task_id(object[1].toString());
						cal.setPerformer_due_date(object[2].toString());
						cal.setTask_activity(object[6].toString());
						cal.setPer_id(object[7].toString());
						cal.setReviewer_due_date(object[8].toString());
						cal.setFh_due_date(object[9].toString());
						cal.setUh_due_date(object[10].toString());
						cal.setSubtask_id(object[12].toString());

						if (user_role == 1) {
							cal.setCalender_date(object[2].toString()); // Set performer date
						}
						if (user_role == 2) {
							if (per_id == user_id) {
								if (rev_id == user_id && !cur_date.after(rwDueDate) && !cur_date.equals(per_due_date)) { // if
																															// reviewer
																															// and
																															// current
																															// date
																															// not
																															// after
																															// reviewer
																															// date
									cal.setCalender_date(object[8].toString()); // Set reviewer date
								} else {
									if (cur_date.after(per_due_date) && !cur_date.equals(per_due_date)) {
										cal.setCalender_date(object[8].toString()); // Set reviewer date
									} else {
										cal.setCalender_date(object[2].toString()); // Set performer date
									}
								}
							} else {
								cal.setCalender_date(object[8].toString()); // Set reviewer date
							}
						}
						if (user_role == 3) {
							if (per_id == user_id) {
								if (rev_id == user_id && !cur_date.after(rwDueDate) && !cur_date.equals(per_due_date)) { // if
																															// reviewer
																															// and
																															// current
																															// date
																															// not
																															// after
																															// reviewer
																															// date
									cal.setCalender_date(object[8].toString()); // Set reviewer date
								} else {
									if (cur_date.after(per_due_date) && !cur_date.equals(per_due_date)) {
										cal.setCalender_date(object[9].toString()); // Set FH date
									} else {
										cal.setCalender_date(object[2].toString()); // Set performer date
									}
								}
							} else {
								if (rev_id == user_id && !cur_date.after(rwDueDate)) { // if reviewer and current date
																						// not after reviewer date
									cal.setCalender_date(object[8].toString()); // Set reviewer date
								} else {
									cal.setCalender_date(object[9].toString()); // Set FH date
								}
							}
						}
						if (user_role == 4) {
							if (per_id == user_id) {
								if (rev_id == user_id && !cur_date.after(rwDueDate) && !cur_date.equals(per_due_date)) { // if
																															// reviewer
																															// and
																															// current
																															// date
																															// not
																															// after
																															// reviewer
																															// date
									cal.setCalender_date(object[8].toString()); // Set reviewer date
								} else {
									if (cur_date.after(per_due_date) && !cur_date.equals(per_due_date)) {
										cal.setCalender_date(object[10].toString()); // Set UH date
									} else {
										cal.setCalender_date(object[2].toString()); // Set performer date
									}
								}

							} else {
								if (rev_id == user_id && !cur_date.after(rwDueDate)) { // if reviewer and current date
																						// not after reviewer date
									cal.setCalender_date(object[8].toString()); // Set reviewer date
								} else {
									cal.setCalender_date(object[10].toString()); // Set UH date
								}
							}
						}
						if (user_role > 4) {
							if (per_id == user_id) {
								if (rev_id == user_id && !cur_date.after(rwDueDate) && !cur_date.equals(per_due_date)) { // if
																															// reviewer
																															// and
																															// current
																															// date
																															// not
																															// after
																															// reviewer
																															// date
									cal.setCalender_date(object[8].toString()); // Set reviewer date
								} else {
									if (cur_date.after(per_due_date) && !cur_date.equals(per_due_date)) {
										cal.setCalender_date(object[3].toString()); // Set legal date
									} else {
										cal.setCalender_date(object[2].toString()); // Set performer date
									}
								}
							} else {
								if (rev_id == user_id && !cur_date.after(rwDueDate) || cur_date.equals(rwDueDate)) { // if
																														// reviewer
																														// and
																														// current
																														// date
																														// before
																														// reviewer
																														// date
									cal.setCalender_date(object[8].toString()); // Set reviewer date
								} else {
									cal.setCalender_date(object[3].toString()); // Set legal date
								}
							}
						}

						Date completedDate = null;
						/* Code to add days to date */
						// Calendar c = Calendar.getInstance();
						// c.setTime(legalDueDate);
						// c.add(Calendar.DATE, 1);
						// completedDate = c.getTime();
						/* Code to add days to date ends here */
						if (object[4] != null) {
							completedDate = sdfIn.parse(object[4].toString());
						}

						// cal.setTask_status(object[5].toString());

						if (object[5].toString().equals("Completed")) {
							if (completedDate.after(legalDueDate)) {
								cal.setTask_status("Delayed");
							} else {
								cal.setTask_status("Complied");
							}
						}
						if (object[5].toString().equals("Active")) {
							if (utilitiesService.getCurrentDate().after(legalDueDate)) {
								cal.setTask_status("Non Complied");
							} else {
								if (utilitiesService.getCurrentDate().after(performerDate)) {
									cal.setTask_status("Posing Risk");
								} else {
									cal.setTask_status("Pending");
								}

							}
						}
						if (object[5].toString().equals("Inactive")) {
							cal.setTask_status("Pending");
						}
						allTasksToSend.add(cal);
					}
				}
			}
			return allTasksToSend;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getEntityList(HttpSession session) {

		JSONArray dataForSend = new JSONArray();
		try {
			List<Object> orga = calendarDao.getEntityList(session);

			Iterator<Object> itr = orga.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("orga_id", object[1]);
				objForAppend.put("orga_name", object[0]);
				dataForSend.add(objForAppend);
			}
			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("errorMessage", "Failed");
			dataForSend.add(objForAppend);
			return dataForSend.toJSONString();
		}

	}

	@Override
	public String getUnitList(String entity_id, HttpSession session) {

		JSONArray dataForSend = new JSONArray();
		try {
			List<Object> orga = calendarDao.getUnitList(entity_id, session);

			Iterator<Object> itr = orga.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("orga_name", object[0]);
				objForAppend.put("orga_id", object[1]);
				objForAppend.put("loca_id", object[2]);
				objForAppend.put("loca_name", object[3]);
				dataForSend.add(objForAppend);
			}
			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("errorMessage", "Failed");
			dataForSend.add(objForAppend);
			return dataForSend.toJSONString();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public String getFunctionList(String unit_id, String orga_id, HttpSession session) {
		JSONArray dataForSend = new JSONArray();
		try {
			List<Object> orga = calendarDao.getFunctionList(unit_id, orga_id, session);

			Iterator<Object> itr = orga.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("orga_name", object[0]);
				objForAppend.put("orga_id", object[1]);
				objForAppend.put("loca_id", object[2]);
				objForAppend.put("loca_name", object[3]);
				objForAppend.put("dept_id", object[4]);
				objForAppend.put("dept_name", object[5]);

				dataForSend.add(objForAppend);
			}
			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("errorMessage", "Failed");
			dataForSend.add(objForAppend);
			return dataForSend.toJSONString();
		}
	}

	@Override
	public String getExeEvalFuncHeadList(String jsonString, HttpSession session) {

		JSONObject dataForSend = new JSONObject();
		JsonNode rootNode = null;
		final ObjectMapper mapper = new ObjectMapper();
		try {
			rootNode = mapper.readTree(jsonString);
			int orga_id = rootNode.path("orga_id").asInt();
			int loca_id = rootNode.path("loca_id").asInt();
			int dept_id = rootNode.path("dept_id").asInt();
			System.out.println("orga_id:" + orga_id + "loca_id:" + loca_id + "dept_id:" + dept_id);
			List<Object> allTasks = calendarDao.getExeEvalFuncHeadList(
					Integer.parseInt(session.getAttribute("sess_user_id").toString()),
					Integer.parseInt(session.getAttribute("sess_role_id").toString()), orga_id, loca_id, dept_id);
			Iterator<Object> itr = allTasks.iterator();
			JSONArray dataForAppend = new JSONArray();
			JSONArray dataForOrganagram = new JSONArray();
			JSONArray dataForTaskFilter = new JSONArray();

			JSONObject filtersObj = new JSONObject();
			JSONObject filtersObjTask = new JSONObject();

			JSONArray execarray = new JSONArray();
			JSONArray evalarray = new JSONArray();
			JSONArray funcHeadarray = new JSONArray();

			List<List> checExecList = new ArrayList<>();
			List<List> checEvalList = new ArrayList<>();
			List<List> checFuncHeadList = new ArrayList<>();

			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();

				JSONObject objForAppend = new JSONObject();

				/*-----------------------------------Code for adding filters list to array---------------------------*/

				if (orga_id != 0 && loca_id == 0) {
					List<Integer> checkExecForAdding = new ArrayList<>();

					checkExecForAdding.add(Integer.parseInt(object[8].toString()));
					checkExecForAdding.add(Integer.parseInt(object[7].toString()));

					if (!checExecList.contains(checkExecForAdding)) {
						JSONObject dataForAppendExecArray = new JSONObject();
						dataForAppendExecArray.put("user_id", object[7]);
						dataForAppendExecArray.put("user_name", object[12].toString() + " " + object[13].toString());
						dataForAppendExecArray.put("orga_id", object[8]);

						execarray.add(dataForAppendExecArray);
						checExecList.add(checkExecForAdding);
					}

					List<Integer> checkEvalForAdding = new ArrayList<>();

					checkEvalForAdding.add(Integer.parseInt(object[8].toString()));
					checkEvalForAdding.add(Integer.parseInt(object[11].toString()));

					if (!checEvalList.contains(checkEvalForAdding)) {
						JSONObject dataForAppendEvalArray = new JSONObject();
						dataForAppendEvalArray.put("user_id", object[11]);
						dataForAppendEvalArray.put("user_name", object[14].toString() + " " + object[15].toString());
						dataForAppendEvalArray.put("orga_id", object[8]);

						evalarray.add(dataForAppendEvalArray);
						checEvalList.add(checkEvalForAdding);
					}

					List<Integer> checkFuncHeadForAdding = new ArrayList<>();

					checkFuncHeadForAdding.add(Integer.parseInt(object[8].toString()));
					checkFuncHeadForAdding.add(Integer.parseInt(object[17].toString()));

					if (!checFuncHeadList.contains(checkFuncHeadForAdding)) {
						JSONObject dataForAppendFuncHeadArray = new JSONObject();
						dataForAppendFuncHeadArray.put("user_id", object[17]);
						dataForAppendFuncHeadArray.put("user_name",
								object[18].toString() + " " + object[19].toString());
						dataForAppendFuncHeadArray.put("orga_id", object[8]);

						funcHeadarray.add(dataForAppendFuncHeadArray);
						checFuncHeadList.add(checkFuncHeadForAdding);
					}
				}

				if (loca_id != 0 && dept_id == 0) {
					List<Integer> checkExecForAdding = new ArrayList<>();

					checkExecForAdding.add(Integer.parseInt(object[8].toString()));
					checkExecForAdding.add(Integer.parseInt(object[9].toString()));
					checkExecForAdding.add(Integer.parseInt(object[7].toString()));

					if (!checExecList.contains(checkExecForAdding)) {
						JSONObject dataForAppendExecArray = new JSONObject();
						dataForAppendExecArray.put("user_id", object[7]);
						dataForAppendExecArray.put("user_name", object[12].toString() + " " + object[13].toString());
						dataForAppendExecArray.put("orga_id", object[8]);
						dataForAppendExecArray.put("loca_id", object[9]);

						execarray.add(dataForAppendExecArray);
						checExecList.add(checkExecForAdding);
					}

					List<Integer> checkEvalForAdding = new ArrayList<>();

					checkEvalForAdding.add(Integer.parseInt(object[8].toString()));
					checkEvalForAdding.add(Integer.parseInt(object[9].toString()));
					checkEvalForAdding.add(Integer.parseInt(object[11].toString()));

					if (!checEvalList.contains(checkEvalForAdding)) {
						JSONObject dataForAppendEvalArray = new JSONObject();
						dataForAppendEvalArray.put("user_id", object[11]);
						dataForAppendEvalArray.put("user_name", object[14].toString() + " " + object[15].toString());
						dataForAppendEvalArray.put("orga_id", object[8]);
						dataForAppendEvalArray.put("loca_id", object[9]);

						evalarray.add(dataForAppendEvalArray);
						checEvalList.add(checkEvalForAdding);
					}

					List<Integer> checkFuncHeadForAdding = new ArrayList<>();

					checkFuncHeadForAdding.add(Integer.parseInt(object[8].toString()));
					checkFuncHeadForAdding.add(Integer.parseInt(object[9].toString()));
					checkFuncHeadForAdding.add(Integer.parseInt(object[17].toString()));

					if (!checFuncHeadList.contains(checkFuncHeadForAdding)) {
						JSONObject dataForAppendFuncHeadArray = new JSONObject();
						dataForAppendFuncHeadArray.put("user_id", object[17]);
						dataForAppendFuncHeadArray.put("user_name",
								object[18].toString() + " " + object[19].toString());
						dataForAppendFuncHeadArray.put("orga_id", object[8]);
						dataForAppendFuncHeadArray.put("loca_id", object[9]);

						funcHeadarray.add(dataForAppendFuncHeadArray);
						checFuncHeadList.add(checkFuncHeadForAdding);
					}

				}
				if (dept_id != 0) {
					List<Integer> checkExecForAdding = new ArrayList<>();

					checkExecForAdding.add(Integer.parseInt(object[8].toString()));
					checkExecForAdding.add(Integer.parseInt(object[9].toString()));
					checkExecForAdding.add(Integer.parseInt(object[10].toString()));
					checkExecForAdding.add(Integer.parseInt(object[7].toString()));

					if (!checExecList.contains(checkExecForAdding)) {
						JSONObject dataForAppendExecArray = new JSONObject();
						dataForAppendExecArray.put("user_id", object[7]);
						dataForAppendExecArray.put("user_name", object[12].toString() + " " + object[13].toString());
						dataForAppendExecArray.put("orga_id", object[8]);
						dataForAppendExecArray.put("loca_id", object[9]);
						dataForAppendExecArray.put("dept_id", object[10]);

						execarray.add(dataForAppendExecArray);
						checExecList.add(checkExecForAdding);
					}

					List<Integer> checkEvalForAdding = new ArrayList<>();

					checkEvalForAdding.add(Integer.parseInt(object[8].toString()));
					checkEvalForAdding.add(Integer.parseInt(object[9].toString()));
					checkEvalForAdding.add(Integer.parseInt(object[10].toString()));
					checkEvalForAdding.add(Integer.parseInt(object[11].toString()));

					if (!checEvalList.contains(checkEvalForAdding)) {
						JSONObject dataForAppendEvalArray = new JSONObject();
						dataForAppendEvalArray.put("user_id", object[11]);
						dataForAppendEvalArray.put("user_name", object[14].toString() + " " + object[15].toString());
						dataForAppendEvalArray.put("orga_id", object[8]);
						dataForAppendEvalArray.put("loca_id", object[9]);
						dataForAppendEvalArray.put("dept_id", object[10]);

						evalarray.add(dataForAppendEvalArray);
						checEvalList.add(checkEvalForAdding);
					}

					List<Integer> checkFuncHeadForAdding = new ArrayList<>();

					checkFuncHeadForAdding.add(Integer.parseInt(object[8].toString()));
					checkFuncHeadForAdding.add(Integer.parseInt(object[9].toString()));
					checkFuncHeadForAdding.add(Integer.parseInt(object[10].toString()));
					checkFuncHeadForAdding.add(Integer.parseInt(object[17].toString()));

					if (!checFuncHeadList.contains(checkFuncHeadForAdding)) {
						JSONObject dataForAppendFuncHeadArray = new JSONObject();
						dataForAppendFuncHeadArray.put("user_id", object[17]);
						dataForAppendFuncHeadArray.put("user_name",
								object[18].toString() + " " + object[19].toString());
						dataForAppendFuncHeadArray.put("orga_id", object[8]);
						dataForAppendFuncHeadArray.put("loca_id", object[9]);
						dataForAppendFuncHeadArray.put("dept_id", object[10]);

						funcHeadarray.add(dataForAppendFuncHeadArray);
						checFuncHeadList.add(checkFuncHeadForAdding);
					}

				}

				/*-----------------------------------Code for adding filters list to array---------------------------*/

			}

			filtersObj.put("Executor", execarray);
			filtersObj.put("Evaluator", evalarray);
			filtersObj.put("FunctionHead", funcHeadarray);

			dataForOrganagram.add(filtersObj);
			dataForSend.put("OrganogramFilter", dataForOrganagram);

			return dataForSend.toJSONString();
		} catch (Exception e) {
			dataForSend.put("responseMessage", "Failed");
			e.printStackTrace();
			return dataForSend.toJSONString();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public String getTaskHistoryByClientTaskId(String jsonString, HttpSession session) {
		JSONObject objForSend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			Date currentDate = sdfIn.parse(sdfOut.format(new Date()));

			JSONArray dataForAppend = new JSONArray();
			String tmap_client_task_id = jsonObj.get("tmap_client_task_id").toString();

			List<Object> taskHistoryList = tasksDao.getTaskHistoryByClientTaskId(tmap_client_task_id);
			Iterator<Object> itr = taskHistoryList.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				Date performerDueDate = sdfIn.parse(object[3].toString());
				Date legalDueDate = sdfIn.parse(object[7].toString());
				String legalTaskStatus = "";
				String legalClass = "";
				String taskStatus = object[10].toString();

				if (taskStatus.equals("Completed")) {
					Date submittedDate = sdfIn.parse(object[12].toString());
					Date completedDate = sdfIn.parse(object[8].toString());
					if (submittedDate.before(legalDueDate) || submittedDate.equals(legalDueDate)) {
						legalTaskStatus = "Complied";
						legalClass = "Complied";
					} else {
						/*
						 * if(submittedDate.after(legalDueDate)) legalTaskStatus = "Delayed"; legalClass
						 * = "Delayed";
						 */
						if (submittedDate.after(legalDueDate) && completedDate.after(legalDueDate)) {
							legalTaskStatus = "Delayed";
							legalClass = "Delayed";
						}
						if (submittedDate.after(legalDueDate)
								&& (completedDate.before(legalDueDate) || completedDate.equals(legalDueDate))) {
							legalTaskStatus = "Delayed Reported";
							legalClass = "Delayed_Reported";
						}
					}
				} else {
					if (taskStatus.equals("Active")) {
						if (legalDueDate.after(currentDate) || legalDueDate.equals(currentDate)) {
							if (currentDate.after(performerDueDate)) {
								legalTaskStatus = "Posing Risk";
								legalClass = "Posing-Risk";
							} else {
								legalTaskStatus = "Pending";
								legalClass = "";
							}
						} else {
							if (currentDate.after(legalDueDate))
								legalTaskStatus = "Non Complied";
							legalClass = "Non-Complied";
						}

					} else {
						if (taskStatus.equals("Inactive"))
							legalTaskStatus = "Inactive";
						legalClass = "";

						if (taskStatus.equals("Partially_Completed")) {
							legalTaskStatus = "Waiting for Approval";
							legalClass = "Partially_Completed";
						}

						if (taskStatus.equals("Re_Opened")) {
							legalTaskStatus = "Re_Opened";
							legalClass = "ReOpened";
						}
					}
				}

				objForAppend.put("ttrn_id", object[0]);
				objForAppend.put("ttrn_performer_name", object[1].toString() + " " + object[2].toString());
				objForAppend.put("ttrn_pr_due_date", sdfOutForDisplay.format(performerDueDate));
				objForAppend.put("ttrn_rw_due_date", sdfOutForDisplay.format(sdfIn.parse(object[4].toString())));
				objForAppend.put("ttrn_fh_due_date", sdfOutForDisplay.format(sdfIn.parse(object[5].toString())));
				objForAppend.put("ttrn_uh_due_date", sdfOutForDisplay.format(sdfIn.parse(object[6].toString())));
				objForAppend.put("ttrn_legal_due_date", sdfOutForDisplay.format(legalDueDate));
				if (object[8] != null)
					objForAppend.put("ttrn_completed_date", sdfOutForDisplay.format(sdfIn.parse(object[8].toString())));
				else
					objForAppend.put("ttrn_completed_date", "");
				if (object[9] != null)
					objForAppend.put("ttrn_performer_comments", object[9]);
				else
					objForAppend.put("ttrn_performer_comments", "");

				objForAppend.put("ttrn_task_status", taskStatus);
				objForAppend.put("ttrn_legal_task_status", legalTaskStatus);
				objForAppend.put("ttrn_legal_task_style", legalClass);
				objForAppend.put("ttrn_reason_for_non_compliance", object[14]);

				/*-----------Getting Completed by first name last name------------------------------------------------------*/
				int user_id = Integer.parseInt(object[11].toString());
				if (user_id > 0) {
					User user = usersDao.getUserById(user_id);
					objForAppend.put("ttrn_task_completed_by",
							user.getUser_first_name() + " " + user.getUser_last_name());
				} else
					objForAppend.put("ttrn_task_completed_by", "");
				/*-----------Getting Completed by first name last name ends here--------------------------------------------*/
				objForAppend.put("ttrn_document", object[13]);
				
				
				/**
				 * auditor condition
				 */
				
				
				objForAppend.put("auditoComments", object[19] != null ? object[19].toString() : "NA");

				String auditorAuditTime = "";
				if (object[20] != null) {
					auditorAuditTime = sdfOutForDisplay.format(sdfIn.parse(object[20].toString()));
				} else {
					auditorAuditTime = "";
				}

				objForAppend.put("auditorAuditTime", auditorAuditTime);
				objForAppend.put("auditorStatus", object[21] != null ? object[21].toString() : "NA");
				objForAppend.put("auditor_performer_by_id", object[22] != null ? object[22].toString() : "0");

				objForAppend.put("auditDate", object[23] != null ? object[23].toString() : "0");
				System.out.println("object[24] : " + object[24]);

				if (object[24] != null) {
					objForAppend.put("reOpenDateWindow", "Yes");
				} else {
					objForAppend.put("reOpenDateWindow", "No");
				}
				
				
				
				SimpleDateFormat sdfo = new SimpleDateFormat("yyyy-MM-dd");
				Date cuDate = new Date();
				String currentDateD2 = sdfo.format(cuDate);
				Date d1 = null;
				Date d2 = null;
				if (object[25] != null) {
					d1 = sdfo.parse(object[23].toString()); // Audit Date
					d2 = sdfo.parse(currentDateD2); // Current Date

					String adDate = sdfo.format(d1); // Audit Date
					String cDate = sdfo.format(d2); // Current Date

					LocalDate currentDates = LocalDate.parse(cDate);
					LocalDate auditDate = LocalDate.parse(adDate);

					System.out.println("AudiDate : " + object[23].toString());

//					if (d1.before(d2) || d1.equals(d2)) {
					if ((auditDate.isAfter(currentDates) || auditDate.equals(currentDates)) && auditDate != null) {
						System.out.println("before and equals currentDates : " + currentDates + "\t d2 : " + auditDate);
						objForAppend.put("auditConditionTwo", "Yes");
					} else {
						System.out.println("before and equals else part currentDates : " + currentDates
								+ "\t auditDate : " + auditDate);
						objForAppend.put("auditConditionTwo", "No");
					}
				} else {
					objForAppend.put("auditConditionTwo", "Yes");
				}
				/**
				 * end code here
				 */
				

				/*
				 * Below is the code for show and hide complete task button as per the frequency
				 * 
				 * Complete task button will be available before 2 month from Executor date
				 */

				SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
				Date parseDate = ss.parse(object[3].toString());
				String formatDate = ss.format(parseDate);

				LocalDate locDate = LocalDate.now();
				LocalDate prDueDate = LocalDate.parse(formatDate);
				// System.out.println("current Date : " + locDate + "\t prDueDate : " +
				// prDueDate);

				LocalDate minusDays = prDueDate.minusDays(60);
				// System.out.println("minusDays : " + minusDays);

				if (minusDays.isBefore(locDate) || locDate.isEqual(minusDays)) {
					// System.out.println("inside if condition and allow YES : " + locDate + "\t pr
					// : " + minusDays);
					objForAppend.put("ttrn_allow_completion", "yes");
				} else {
					objForAppend.put("ttrn_allow_completion", "no");
				}
				
				

				// Completed Hide and show complete task button

				List<UploadedDocuments> attachedDocuments = uploadedDocumentsDao
						.getAllDocumentByTtrnId(Integer.parseInt(object[0].toString()));

				if (attachedDocuments != null) {
					Iterator<UploadedDocuments> itre = attachedDocuments.iterator();
					JSONArray docArray = new JSONArray();

					while (itre.hasNext()) {
						UploadedDocuments uploadedDocuments = (UploadedDocuments) itre.next();
						JSONObject docObj = new JSONObject();
						docObj.put("udoc_id", uploadedDocuments.getUdoc_id());
						docObj.put("udoc_original_file_name", uploadedDocuments.getUdoc_original_file_name());
						docArray.add(docObj);
					}

					objForAppend.put("document_list", docArray);
				} else {
					objForAppend.put("document_list", new JSONArray());
				}

				objForAppend.put("task_frequency",
						tasksDao.getOriginalFrequency(Integer.parseInt(object[0].toString())));
				objForAppend.put("client_task_id", tmap_client_task_id);
				dataForAppend.add(objForAppend);
			}

			objForSend.put("task_history", dataForAppend);

			JSONArray subTaskdataForAppend = new JSONArray();
			String ttrn_sub_task_id = null;
			/*
			 * if(jsonObj.get("ttrn_sub_task_id").toString() == null) { ttrn_sub_task_id =
			 * jsonObj.get("ttrn_sub_task_id").toString(); }else { ttrn_sub_task_id =
			 * jsonObj.get("ttrn_sub_task_id").toString(); }
			 */

			if (jsonObj.get("ttrn_sub_task_id") != null)
				ttrn_sub_task_id = jsonObj.get("ttrn_sub_task_id").toString();
			List<SubTaskTranscational> subTaskList = calendarDao.getSubTaskHitoryByclientTaskID(ttrn_sub_task_id);
			if (subTaskList != null) {

				Iterator<SubTaskTranscational> iterator = subTaskList.iterator();

				while (iterator.hasNext()) {

					SubTaskTranscational subTaskTranscational = iterator.next();

					JSONObject objForAppend = new JSONObject();
					Date performerDueDate = sdfIn.parse(subTaskTranscational.getTtrn_sub_task_pr_due_date().toString());
					Date legalDueDate = sdfIn.parse(subTaskTranscational.getTtrn_sub_task_ENT_due_date().toString());
					String legalTaskStatus = "";
					String legalClass = "";
					String taskStatus = subTaskTranscational.getTtrn_sub_task_status();

					if (taskStatus.equals("Completed")) {
						Date submittedDate = sdfIn
								.parse(subTaskTranscational.getTtrn_sub_task_submition_date().toString());
						if (submittedDate.before(legalDueDate) || submittedDate.equals(legalDueDate)) {
							legalTaskStatus = "Complied";
							legalClass = "Complied";
						} else {
							if (submittedDate.after(legalDueDate))
								legalTaskStatus = "Delayed";
							legalClass = "Delayed";
						}
					} else {
						if (taskStatus.equals("Active")) {
							if (legalDueDate.after(currentDate) || legalDueDate.equals(currentDate)) {
								if (currentDate.after(performerDueDate)) {
									legalTaskStatus = "Posing Risk";
									legalClass = "Posing-Risk";
								} else {
									legalTaskStatus = "Pending";
									legalClass = "";
								}
							} else {
								if (currentDate.after(legalDueDate))
									legalTaskStatus = "Non Complied";
								legalClass = "Non-Complied";
							}

						} else {
							if (taskStatus.equals("Inactive"))
								legalTaskStatus = "Inactive";
							legalClass = "";

							if (taskStatus.equals("Partially_Completed")) {
								legalTaskStatus = "Waiting for Approval";
								legalClass = "Partially_Completed";
							}

							if (taskStatus.equals("Re_Opened")) {
								legalTaskStatus = "Re_Opened";
								legalClass = "ReOpened";
							}
						}
					}

					objForAppend.put("ttrn_id", subTaskTranscational.getTtrn_sub_id());
					// objForAppend.put("ttrn_performer_name", object[1].toString() +" "+
					// object[2].toString());
					objForAppend.put("ttrn_pr_due_date", sdfOutForDisplay.format(performerDueDate));
					objForAppend.put("ttrn_rw_due_date", sdfOutForDisplay
							.format(sdfIn.parse(subTaskTranscational.getTtrn_sub_task_rw_date().toString())));
					objForAppend.put("ttrn_fh_due_date", sdfOutForDisplay
							.format(sdfIn.parse(subTaskTranscational.getTtrn_sub_task_FH_due_date().toString())));
					objForAppend.put("ttrn_uh_due_date", sdfOutForDisplay
							.format(sdfIn.parse(subTaskTranscational.getTtrn_sub_task_UH_due_date().toString())));
					objForAppend.put("ttrn_legal_due_date", sdfOutForDisplay.format(legalDueDate));
					if (subTaskTranscational.getTtrn_sub_task_compl_date() != null)
						objForAppend.put("ttrn_completed_date", sdfOutForDisplay
								.format(sdfIn.parse(subTaskTranscational.getTtrn_sub_task_compl_date().toString())));
					else
						objForAppend.put("ttrn_completed_date", "");
					if (subTaskTranscational.getTtrn_sub_task_comment() != null)
						objForAppend.put("ttrn_performer_comments", subTaskTranscational.getTtrn_sub_task_comment());
					else
						objForAppend.put("ttrn_performer_comments", "");

					objForAppend.put("ttrn_task_status", taskStatus);
					objForAppend.put("ttrn_legal_task_status", legalTaskStatus);
					objForAppend.put("ttrn_legal_task_style", legalClass);
					objForAppend.put("ttrn_reason_for_non_compliance",
							subTaskTranscational.getTtrn_sub_task_reason_for_non_compliance());
					// objForAppend.put("ttrn_next_examination_date",
					// sdfOutForDisplay.format(sdfIn.parse(subTaskTranscational.getTttn_sub_task_next_examination_date().toString())));

					/*-----------Getting Completed by first name last name------------------------------------------------------*/
					int user_id = 0;
					if (subTaskTranscational.getTtrn_sub_task_completed_by() != null) {
						user_id = Integer.parseInt(subTaskTranscational.getTtrn_sub_task_completed_by().toString());
					}
					if (user_id > 0) {
						User user = usersDao.getUserById(user_id);
						objForAppend.put("ttrn_task_completed_by",
								user.getUser_first_name() + " " + user.getUser_last_name());
					} else
						objForAppend.put("ttrn_task_completed_by", "");

					/*-----------Getting Completed by first name last name ends here--------------------------------------------*/
					objForAppend.put("ttrn_document", subTaskTranscational.getTtrn_sub_task_document());
					objForAppend.put("ttrn_sub_id", subTaskTranscational.getTtrn_sub_task_id());
					objForAppend.put("ttrn_sub_task_id", subTaskTranscational.getTtrn_sub_id());

					SubTask subTask = tasksDao
							.getSubTaskDetailsBysub_task_id(subTaskTranscational.getTtrn_sub_task_id());

					objForAppend.put("ttrn_sub_equip_number", subTask.getSub_equipment_number());
					objForAppend.put("ttrn_sub_equip_type", subTask.getSub_equipment_type());
					objForAppend.put("ttrn_sub_equip_loca", subTask.getSub_equipment_location());
					objForAppend.put("ttrn_sub_equip_desc", subTask.getSub_equipment_description());
					objForAppend.put("ttrn_sub_equip_frequency", subTask.getSub_frequency());

					List<UploadedSubTaskDocuments> attachedDocuments = uploadedDocumentsDao
							.getAllDocumentBySubTtrnId(subTaskTranscational.getTtrn_sub_id());

					if (attachedDocuments != null) {
						Iterator<UploadedSubTaskDocuments> itre = attachedDocuments.iterator();
						JSONArray docArray = new JSONArray();

						while (itre.hasNext()) {
							UploadedSubTaskDocuments uploadedDocuments = (UploadedSubTaskDocuments) itre.next();
							JSONObject docObj = new JSONObject();
							docObj.put("udoc_id", uploadedDocuments.getUdoc_sub_task_id());
							docObj.put("udoc_original_file_name",
									uploadedDocuments.getUdoc_sub_task_original_file_name());
							docArray.add(docObj);
						}

						objForAppend.put("document_list", docArray);
					} else {
						objForAppend.put("document_list", new JSONArray());
					}

					// objForAppend.put("task_frequency",
					// tasksDao.getOriginalFrequency(Integer.parseInt(object[0].toString())));
					objForAppend.put("client_task_id", tmap_client_task_id);
					subTaskdataForAppend.add(objForAppend);

				}
			}

			objForSend.put("subTaskHistory", subTaskdataForAppend);

			return objForSend.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("repsonseMessage", "Failed");
			return objForSend.toJSONString();
		}
	}

}
