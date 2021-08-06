package lexprd006.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "COMPLIANCE_REPORT_LOG")
public class ComplianceReportLogs {

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

	@Column(name = "FUNCTION_ID")
	private String functionId;

	@Column(name = "FROM_DATE")
	private Date fromDate;

	@Column(name = "TO_DATE")
	private Date toDate;

	@Column(name = "LEGAL_STATUS")
	private String legalStatus;

	@Column(name = "TASKS_IMPACT")
	private String tasksImpact;

	@Column(name = "CREATED_TIME")
	private Date createdTime;

	public ComplianceReportLogs() {
	}

	@Override
	public String toString() {
		return "ComplianceReportLogs [id=" + id + ", userId=" + userId + ", entityId=" + entityId + ", unitId=" + unitId
				+ ", functionId=" + functionId + ", fromDate=" + fromDate + ", toDate=" + toDate + ", legalStatus="
				+ legalStatus + ", tasksImpact=" + tasksImpact + ", createdTime=" + createdTime + "]";
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

	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getLegalStatus() {
		return legalStatus;
	}

	public void setLegalStatus(String legalStatus) {
		this.legalStatus = legalStatus;
	}

	public String getTasksImpact() {
		return tasksImpact;
	}

	public void setTasksImpact(String tasksImpact) {
		this.tasksImpact = tasksImpact;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

}
