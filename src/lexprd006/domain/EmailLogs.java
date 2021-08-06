package lexprd006.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EMAIL_LOGS")
public class EmailLogs {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;

	@Column(name = "TASKS_ID")
	private String tasksId;

	@Column(name = "LEX_TASKS_ID")
	private String lexTasksId;

	@Column(name = "ENTITY_ID")
	private String entityId;

	@Column(name = "UNIT_ID")
	private String unitId;

	@Column(name = "DEPT_ID")
	private String deptId;

	@Column(name = "EMAIL_STATUS")
	private String emailStatus;

	@Column(name = "EXECUTOR_ID")
	private String executorId;

	@Column(name = "EVALUATOR_ID")
	private String evaluatorId;

	@Column(name = "FUNCTION_HEAD_ID")
	private String functionHeadId;

	@Column(name = "UNIT_HEAD_ID")
	private String unitHeadId;

	@Column(name = "CREATED_TIME")
	private Date createdTime;

	@Column(name = "MAIL_SENT_TO")
	private String mailSentTo;
	
	@Column(name="CREATED_BY")
	private String createdBy;

	public EmailLogs() {
	}

	@Override
	public String toString() {
		return "EmailLogs [id=" + id + ", tasksId=" + tasksId + ", lexTasksId=" + lexTasksId + ", entityId=" + entityId
				+ ", unitId=" + unitId + ", deptId=" + deptId + ", emailStatus=" + emailStatus + ", executorId="
				+ executorId + ", evaluatorId=" + evaluatorId + ", functionHeadId=" + functionHeadId + ", unitHeadId="
				+ unitHeadId + ", createdTime=" + createdTime + ", mailSentTo=" + mailSentTo + ", createdBy="
				+ createdBy + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(String emailStatus) {
		this.emailStatus = emailStatus;
	}

	public String getExecutorId() {
		return executorId;
	}

	public void setExecutorId(String executorId) {
		this.executorId = executorId;
	}

	public String getEvaluatorId() {
		return evaluatorId;
	}

	public void setEvaluatorId(String evaluatorId) {
		this.evaluatorId = evaluatorId;
	}

	public String getFunctionHeadId() {
		return functionHeadId;
	}

	public void setFunctionHeadId(String functionHeadId) {
		this.functionHeadId = functionHeadId;
	}

	public String getUnitHeadId() {
		return unitHeadId;
	}

	public void setUnitHeadId(String unitHeadId) {
		this.unitHeadId = unitHeadId;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getMailSentTo() {
		return mailSentTo;
	}

	public void setMailSentTo(String mailSentTo) {
		this.mailSentTo = mailSentTo;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	

}
