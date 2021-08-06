package lexprd006.service;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpSession;

import lexprd006.domain.CalendarDomain;



public interface CalendarService {
	public List<CalendarDomain> getAllTasksAssignedToUserForCalendarPerformerWise(int pr_id) throws ParseException;
	public <T> List<T> getAllTasksAssignedToUserForCalendarReviewerWise(int rw_id) throws ParseException;
	public List<CalendarDomain> getAllTasksAssignedToUserForCalendarBothWise(int user_id) throws ParseException;
	public <T> List<T> getAllTasksAssignedToUserForCalendarOrganizationWise(int tmap_performer_user_id) throws ParseException;
	public <T> List<T> getAllTasksAssignedToUserForCalendarLocationWise(int tmap_performer_user_id) throws ParseException;
	public String getAllTasksAssignedToUserForCalendarDepartmentWise(HttpSession session) throws ParseException;
	
	public String searchTaskForCalendar(String json,HttpSession session);
	public <T> List<T> getDistinctFrequencyForUser(HttpSession session);
	public List<CalendarDomain> getAllSubTasksAssignedToUserForCalendarDepartmentWise(HttpSession session);
	public String getExeEvalFuncHeadList(String jsonString, HttpSession session);
	public String getEntityList(HttpSession session);
	public String getUnitList(String entity_id, HttpSession session);
	public String getFunctionList(String unit_id, String orga_id, HttpSession session);
	public String getTaskHistoryByClientTaskId(String jsonString, HttpSession session);
}
