package lexprd006.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/*
 * Author : Mahesh Kharote
 * Created Date : 25/10/2016
 * Updated By : 
 * Updated Date : 
 *  
 * */
@Entity(name = "mst_department")
@Table(name = "mst_department")
// , indexes = @Index(name = "idx_dept_id_Name", columnList = "dept_id,dept_name")
public class Department {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int dept_id;
	private String dept_name;
	private int dept_parent_id;
	private String dept_approval_status;
	private String dept_enable_status;
	private int dept_added_by;
	private Date dept_created_at;

	public int getDept_id() {
		return dept_id;
	}

	public void setDept_id(int dept_id) {
		this.dept_id = dept_id;
	}

	public String getDept_name() {
		return dept_name;
	}

	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}

	public int getDept_parent_id() {
		return dept_parent_id;
	}

	public void setDept_parent_id(int dept_parent_id) {
		this.dept_parent_id = dept_parent_id;
	}

	public String getDept_approval_status() {
		return dept_approval_status;
	}

	public void setDept_approval_status(String dept_approval_status) {
		this.dept_approval_status = dept_approval_status;
	}

	public String getDept_enable_status() {
		return dept_enable_status;
	}

	public void setDept_enable_status(String dept_enable_status) {
		this.dept_enable_status = dept_enable_status;
	}

	public int getDept_added_by() {
		return dept_added_by;
	}

	public void setDept_added_by(int dept_added_by) {
		this.dept_added_by = dept_added_by;
	}

	public Date getDept_created_at() {
		return dept_created_at;
	}

	public void setDept_created_at(Date dept_created_at) {
		this.dept_created_at = dept_created_at;
	}

}
