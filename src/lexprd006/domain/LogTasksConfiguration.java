package lexprd006.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TASKS_CONFIGURATION_LOG")
public class LogTasksConfiguration {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;

	@Column(name = "USER_ID")
	private int userId;

	@Column(name = "ENTITY_ID")
	private String entityId;

	@Column(name = "UNIT_ID")
	private String unitId;

	@Column(name = "FREQUENCY")
	private String frequency;

	@Column(name = "BUFFER_DAYS")
	private String bufferDays;

	@Column(name = "PRIOD_DAYS")
	private String priodDays;

	@Column(name = "LEAGAL_DUE_DATE")
	private Date legalDueDate;

	@Column(name = "UNIT_HEAD_DUE_DATE")
	private Date unitHeadDueDate;

	@Column(name = "FUNCTION_HEAD_DUE_DATE")
	private Date funtionHeadDueDate;

	@Column(name = "EVALUATOR_DUE_DATE")
	private Date evaluatorDueDate;

	@Column(name = "EXECUTOR_DUE_DATE")
	private Date executorHeadDueDate;

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

	@Column(name = "ASSIGN_TIME")
	private Date assignTime;

	@Column(name = "AUDIT_DATE")
	private Date auditDate;

	public LogTasksConfiguration() {
	}

	@Override
	public String toString() {
		return "LogTasksConfiguration [id=" + id + ", userId=" + userId + ", entityId=" + entityId + ", unitId="
				+ unitId + ", frequency=" + frequency + ", bufferDays=" + bufferDays + ", priodDays=" + priodDays
				+ ", legalDueDate=" + legalDueDate + ", unitHeadDueDate=" + unitHeadDueDate + ", funtionHeadDueDate="
				+ funtionHeadDueDate + ", evaluatorDueDate=" + evaluatorDueDate + ", executorHeadDueDate="
				+ executorHeadDueDate + ", exeutorId=" + exeutorId + ", evaluatorId=" + evaluatorId + ", functionId="
				+ functionId + ", tasksId=" + tasksId + ", lexTasksId=" + lexTasksId + ", assignTime=" + assignTime
				+ ", auditDate=" + auditDate + "]";
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
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

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getBufferDays() {
		return bufferDays;
	}

	public void setBufferDays(String bufferDays) {
		this.bufferDays = bufferDays;
	}

	public String getPriodDays() {
		return priodDays;
	}

	public void setPriodDays(String priodDays) {
		this.priodDays = priodDays;
	}

	public Date getLegalDueDate() {
		return legalDueDate;
	}

	public void setLegalDueDate(Date legalDueDate) {
		this.legalDueDate = legalDueDate;
	}

	public Date getUnitHeadDueDate() {
		return unitHeadDueDate;
	}

	public void setUnitHeadDueDate(Date unitHeadDueDate) {
		this.unitHeadDueDate = unitHeadDueDate;
	}

	public Date getFuntionHeadDueDate() {
		return funtionHeadDueDate;
	}

	public void setFuntionHeadDueDate(Date funtionHeadDueDate) {
		this.funtionHeadDueDate = funtionHeadDueDate;
	}

	public Date getEvaluatorDueDate() {
		return evaluatorDueDate;
	}

	public void setEvaluatorDueDate(Date evaluatorDueDate) {
		this.evaluatorDueDate = evaluatorDueDate;
	}

	public Date getExecutorHeadDueDate() {
		return executorHeadDueDate;
	}

	public void setExecutorHeadDueDate(Date executorHeadDueDate) {
		this.executorHeadDueDate = executorHeadDueDate;
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

	public Date getAssignTime() {
		return assignTime;
	}

	public void setAssignTime(Date assignTime) {
		this.assignTime = assignTime;
	}

}
