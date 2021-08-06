package lexprd006.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "ACTIVATE_DEACT_TASKS_LOG")
public class LogActivateDeActivateTasks {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;

	@Column(name = "USER_ID")
	private int userId;

	@Column(name = "TTRN_ID")
	private String ttrnId;

	@Column(name = "ACTIVATE_DEACT_TIME")
	private Date activateDeActTime;

	@Column(name = "TASKS_STATUS")
	private String tasksStatus;

	public LogActivateDeActivateTasks() {
	}

	@Override
	public String toString() {
		return "LogActivateDeActivateTasks [id=" + id + ", userId=" + userId + ", ttrnId=" + ttrnId
				+ ", activateDeActTime=" + activateDeActTime + ", tasksStatus=" + tasksStatus + "]";
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

	public String getTtrnId() {
		return ttrnId;
	}

	public void setTtrnId(String ttrnId) {
		this.ttrnId = ttrnId;
	}

	public Date getActivateDeActTime() {
		return activateDeActTime;
	}

	public void setActivateDeActTime(Date activateDeActTime) {
		this.activateDeActTime = activateDeActTime;
	}

	public String getTasksStatus() {
		return tasksStatus;
	}

	public void setTasksStatus(String tasksStatus) {
		this.tasksStatus = tasksStatus;
	}

}
