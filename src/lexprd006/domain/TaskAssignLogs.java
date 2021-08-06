package lexprd006.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "TASK_ASSIGN_LOG")
public class TaskAssignLogs {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;

	@Column(name = "USER_ID")
	private int userId;

	@Column(name = "EXECUTOR_ID")
	private String exeutorId;

	@Column(name = "EVALUATOR_ID")
	private String evaluatorId;

	@Column(name = "FUNCTION_ID")
	private String functionId;

	@Column(name = "TASKS_ID")
	private String tasksId;

	@Column(name = "LEX_TASKS_ID")
	private String lexTasksId;

	@Column(name = "ENTITY_ID")
	private String entityId;

	@Column(name = "UNIT_ID")
	private String unitId;

	@Column(name = "ASSIGN_TIME")
	private Date assignTime;

	public TaskAssignLogs() {
	}

	@Override
	public String toString() {
		return "TaskAssignLogs [id=" + id + ", userId=" + userId + ", exeutorId=" + exeutorId + ", evaluatorId="
				+ evaluatorId + ", functionId=" + functionId + ", tasksId=" + tasksId + ", lexTasksId=" + lexTasksId
				+ ", entityId=" + entityId + ", unitId=" + unitId + ", assignTime=" + assignTime + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getExeutorId() {
		return exeutorId;
	}

	public void setExeutorId(String exeutorId) {
		this.exeutorId = exeutorId;
	}

	public String getEvaluatorId() {
		return evaluatorId;
	}

	public void setEvaluatorId(String evaluatorId) {
		this.evaluatorId = evaluatorId;
	}

	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	public String getTasksId() {
		return tasksId;
	}

	public void setTasksId(String tasksId) {
		this.tasksId = tasksId;
	}

	public String getLexTasksId() {
		return lexTasksId;
	}

	public void setLexTasksId(String lexTasksId) {
		this.lexTasksId = lexTasksId;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public Date getAssignTime() {
		return assignTime;
	}

	public void setAssignTime(Date assignTime) {
		this.assignTime = assignTime;
	}

}
