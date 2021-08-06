package lexprd006.dao;

import java.util.List;

import javax.servlet.http.HttpSession;

import lexprd006.domain.SubTaskTranscational;

public interface CalendarDao {
	public <T> List<T> getAllTasksAssignedToUserForCalendarWhereUserIsPerformer(int pr_id);
	public <T> List<T> getAllTasksAssignedToUserForCalendarWhereUserIsReviwer(int rw_id);
	public <T> List<T> getAllTasksOrgaLocaDeptWise(int user_id,int role_id);
	
	public String searchTaskForCalender(String json,int user_id,int role_id);
	public <T> List<T> getDistinctFrequencyForUser(HttpSession session);
	public <T> List<T> getAllSubTasksOrgaLocaDeptWise(int parseInt, int user_role);
	
	public List<Object> getExeEvalFuncHeadList(int user_id, int user_role_id, int orga_id, int loca_id, int dept_id);
	List<Object> getUnitList(String entity_id, HttpSession session);
	List<Object> getFunctionList(String unit_id, String orga_id, HttpSession session);
	public List<Object> getEntityList(HttpSession session);
	public List<SubTaskTranscational> getSubTaskHitoryByclientTaskID(String ttrn_sub_task_id);

}
