package lexprd006.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/*
 * Author : Rahul Shinde
 * Created Date : 19/02/2016
 * Updated By :
 * Updated Date : 
 *  
 * */

@Entity(name = "mst_user")
@Table(name = "mst_user")
// , indexes = @Index(name = "idx_userId_userName", columnList = "user_id,user_username")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int user_id;
	private String user_username;
	private String user_userpassword;
	private String user_employee_id;
	private String user_first_name;
	private String user_last_name;
	private String user_mobile;
	private String user_email;
	private String user_address;
	private int user_role_id;
	private int user_organization_id;
	private int user_location_id;
	private int user_department_id;
	private int user_designation_id;
	private int user_report_to;
	private String user_enable_status;
	private String user_approval_status;
	private int user_added_by;
	private Date user_created_at;
	private String profile_pic;
	private String user_default_password_changed;

	private Date loginTime;
	private Date logOutTime;
	private String isOnline;

	@Column(name = "login_attempts", columnDefinition = "int default 0")
	private Integer login_attempts;
	private Date account_locked_at;
	@Column(name = "account_locked_status", columnDefinition = "int default 0")
	private Integer account_locked_status;
	@Column(name = "login_attempts_left", columnDefinition = "int default 3")
	private Integer login_attempts_left;

	private String gender;

	public User() {
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Date getLogOutTime() {
		return logOutTime;
	}

	public void setLogOutTime(Date logOutTime) {
		this.logOutTime = logOutTime;
	}

	public String getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUser_username() {
		return user_username;
	}

	public void setUser_username(String user_username) {
		this.user_username = user_username;
	}

	public String getUser_userpassword() {
		return user_userpassword;
	}

	public void setUser_userpassword(String user_userpassword) {
		this.user_userpassword = user_userpassword;
	}

	public String getUser_employee_id() {
		return user_employee_id;
	}

	public void setUser_employee_id(String user_employee_id) {
		this.user_employee_id = user_employee_id;
	}

	public String getUser_first_name() {
		return user_first_name;
	}

	public void setUser_first_name(String user_first_name) {
		this.user_first_name = user_first_name;
	}

	public String getUser_last_name() {
		return user_last_name;
	}

	public void setUser_last_name(String user_last_name) {
		this.user_last_name = user_last_name;
	}

	public String getUser_mobile() {
		return user_mobile;
	}

	public void setUser_mobile(String user_mobile) {
		this.user_mobile = user_mobile;
	}

	public String getUser_email() {
		return user_email;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}

	public String getUser_address() {
		return user_address;
	}

	public void setUser_address(String user_address) {
		this.user_address = user_address;
	}

	public int getUser_role_id() {
		return user_role_id;
	}

	public void setUser_role_id(int user_role_id) {
		this.user_role_id = user_role_id;
	}

	public int getUser_organization_id() {
		return user_organization_id;
	}

	public void setUser_organization_id(int user_organization_id) {
		this.user_organization_id = user_organization_id;
	}

	public int getUser_location_id() {
		return user_location_id;
	}

	public void setUser_location_id(int user_loaction_id) {
		this.user_location_id = user_loaction_id;
	}

	public int getUser_department_id() {
		return user_department_id;
	}

	public void setUser_department_id(int user_department_id) {
		this.user_department_id = user_department_id;
	}

	public int getUser_designation_id() {
		return user_designation_id;
	}

	public void setUser_designation_id(int user_designation_id) {
		this.user_designation_id = user_designation_id;
	}

	public int getUser_report_to() {
		return user_report_to;
	}

	public void setUser_report_to(int user_report_to) {
		this.user_report_to = user_report_to;
	}

	public String getUser_enable_status() {
		return user_enable_status;
	}

	public void setUser_enable_status(String user_enable_status) {
		this.user_enable_status = user_enable_status;
	}

	public String getUser_approval_status() {
		return user_approval_status;
	}

	public void setUser_approval_status(String user_approval_status) {
		this.user_approval_status = user_approval_status;
	}

	public int getUser_added_by() {
		return user_added_by;
	}

	public void setUser_added_by(int user_added_by) {
		this.user_added_by = user_added_by;
	}

	public Date getUser_created_at() {
		return user_created_at;
	}

	public void setUser_created_at(Date user_created_at) {
		this.user_created_at = user_created_at;
	}

	public String getProfile_pic() {
		return profile_pic;
	}

	public void setProfile_pic(String profile_pic) {
		this.profile_pic = profile_pic;
	}

	public String getUser_default_password_changed() {
		return user_default_password_changed;
	}

	public void setUser_default_password_changed(String user_default_password_changed) {
		this.user_default_password_changed = user_default_password_changed;
	}

	public Integer getLogin_attempts() {
		return login_attempts;
	}

	public void setLogin_attempts(Integer login_attempts) {
		this.login_attempts = login_attempts;
	}

	public Date getAccount_locked_at() {
		return account_locked_at;
	}

	public void setAccount_locked_at(Date account_locked_at) {
		this.account_locked_at = account_locked_at;
	}

	public Integer getAccount_locked_status() {
		return account_locked_status;
	}

	public void setAccount_locked_status(Integer account_locked_status) {
		this.account_locked_status = account_locked_status;
	}

	public Integer getLogin_attempts_left() {
		return login_attempts_left;
	}

	public void setLogin_attempts_left(Integer login_attempts_left) {
		this.login_attempts_left = login_attempts_left;
	}

	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", user_username=" + user_username + ", user_userpassword="
				+ user_userpassword + ", user_employee_id=" + user_employee_id + ", user_first_name=" + user_first_name
				+ ", user_last_name=" + user_last_name + ", user_mobile=" + user_mobile + ", user_email=" + user_email
				+ ", user_address=" + user_address + ", user_role_id=" + user_role_id + ", user_organization_id="
				+ user_organization_id + ", user_location_id=" + user_location_id + ", user_department_id="
				+ user_department_id + ", user_designation_id=" + user_designation_id + ", user_report_to="
				+ user_report_to + ", user_enable_status=" + user_enable_status + ", user_approval_status="
				+ user_approval_status + ", user_added_by=" + user_added_by + ", user_created_at=" + user_created_at
				+ ", profile_pic=" + profile_pic + ", user_default_password_changed=" + user_default_password_changed
				+ ", loginTime=" + loginTime + ", logOutTime=" + logOutTime + ", isOnline=" + isOnline
				+ ", login_attempts=" + login_attempts + ", account_locked_at=" + account_locked_at
				+ ", account_locked_status=" + account_locked_status + ", login_attempts_left=" + login_attempts_left
				+ ", gender=" + gender + "]";
	}

}
