package lexprd006.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "LOGIN_LOGOUT_LOG")
public class LogoutTimeLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;

	@Column(name = "USER_ID")
	private int userId;

	@Column(name = "SESSION_ID")
	private String sId;

	@Column(name = "LOGIN_TIME")
	private Date loginTime;

	@Column(name = "LOGOUT_TIME")
	private Date loginOutTime;

	public LogoutTimeLog() {
	}

	@Override
	public String toString() {
		return "LogoutTimeLog [id=" + id + ", userId=" + userId + ", sId=" + sId + ", loginTime=" + loginTime
				+ ", loginOutTime=" + loginOutTime + "]";
	}

	public void setLoginTime(Date currentDate) {
		this.loginTime = currentDate;
	}

	public void setLoginOutTime(Date loginOutTime) {
		this.loginOutTime = loginOutTime;
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

	public String getsId() {
		return sId;
	}

	public void setsId(String sId) {
		this.sId = sId;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public Date getLoginOutTime() {
		return loginOutTime;
	}

}
