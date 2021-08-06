package lexprd006.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Log_Reactivation")
public class LogReactivation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;

	@Column(name = "USER_ID")
	private int userId;

	@Column(name = "FREQUENCY")
	private String frequency;

	@Column(name = "EXECUTOR_ID")
	private String exeutorId;

	/*
	 * @Column(name = "EVALUATOR_ID") private String evaluatorId;
	 * 
	 * @Column(name = "FUNCTION_ID") private String functionId;
	 */

	@Column(name = "TASKS_ID")
	private String tasksId;

	@Column(name = "REACTIVATION_TIME")
	private Date reactivationTime;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "TASKS_STATUS")
	private String tasksStatus = "NA";

	public LogReactivation() {
	}

	@Override
	public String toString() {
		return "LogReactivation [id=" + id + ", userId=" + userId + ", frequency=" + frequency + ", exeutorId="
				+ exeutorId + ", tasksId=" + tasksId + ", reactivationTime=" + reactivationTime + ", status=" + status
				+ ", tasksStatus=" + tasksStatus + "]";
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

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getExeutorId() {
		return exeutorId;
	}

	public void setExeutorId(String exeutorId) {
		this.exeutorId = exeutorId;
	}

	public String getTasksId() {
		return tasksId;
	}

	public void setTasksId(String tasksId) {
		this.tasksId = tasksId;
	}

	public Date getReactivationTime() {
		return reactivationTime;
	}

	public void setReactivationTime(Date reactivationTime) {
		this.reactivationTime = reactivationTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTasksStatus() {
		return tasksStatus;
	}

	public void setTasksStatus(String tasksStatus) {
		this.tasksStatus = tasksStatus;
	}

}
