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
 * Created Date : 10/01/2017
 * Updated By :
 * Updated Date : 
 *  
 * */


@Entity(name = "cfg_task_user_mapping")
@Table(name = "cfg_task_user_mapping")
// , indexes = @Index(name = "idx_tmap", columnList = "tmap_id,tmap_loca_id,tmap_client_tasks_id,tmap_orga_id,tmap_pr_user_id")
public class TaskUserMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int tmap_id;
	private String tmap_client_tasks_id;
	private int tmap_dept_id;
	private int tmap_last_generated_value_for_client_task_id;
	private String tmap_lexcare_task_id;
	private int tmap_loca_id;
	private int tmap_orga_id;
	private int tmap_pr_user_id;
	private int tmap_rw_user_id;
	private int tmap_fh_user_id;
	private String tmap_enable_status;
	private String tmap_approval_status;
	private int tmap_task_id;
	private int tmap_added_by;
	private Date tmap_created_at;
	
	public int getTmap_id() {
		return tmap_id;
	}
	public void setTmap_id(int tmap_id) {
		this.tmap_id = tmap_id;
	}
	public String getTmap_client_tasks_id() {
		return tmap_client_tasks_id;
	}
	public void setTmap_client_tasks_id(String tmap_client_tasks_id) {
		this.tmap_client_tasks_id = tmap_client_tasks_id;
	}
	public int getTmap_dept_id() {
		return tmap_dept_id;
	}
	public void setTmap_dept_id(int tmap_dept_id) {
		this.tmap_dept_id = tmap_dept_id;
	}
	public int getTmap_last_generated_value_for_client_task_id() {
		return tmap_last_generated_value_for_client_task_id;
	}
	public void setTmap_last_generated_value_for_client_task_id(int tmap_last_generated_value_for_client_task_id) {
		this.tmap_last_generated_value_for_client_task_id = tmap_last_generated_value_for_client_task_id;
	}
	public String getTmap_lexcare_task_id() {
		return tmap_lexcare_task_id;
	}
	public void setTmap_lexcare_task_id(String tmap_lexcare_task_id) {
		this.tmap_lexcare_task_id = tmap_lexcare_task_id;
	}
	public int getTmap_loca_id() {
		return tmap_loca_id;
	}
	public void setTmap_loca_id(int tmap_loca_id) {
		this.tmap_loca_id = tmap_loca_id;
	}
	public int getTmap_orga_id() {
		return tmap_orga_id;
	}
	public void setTmap_orga_id(int tmap_orga_id) {
		this.tmap_orga_id = tmap_orga_id;
	}
	public int getTmap_pr_user_id() {
		return tmap_pr_user_id;
	}
	public void setTmap_pr_user_id(int tmap_pr_user_id) {
		this.tmap_pr_user_id = tmap_pr_user_id;
	}
	public int getTmap_rw_user_id() {
		return tmap_rw_user_id;
	}
	public void setTmap_rw_user_id(int tmap_rw_user_id) {
		this.tmap_rw_user_id = tmap_rw_user_id;
	}
	public String getTmap_enable_status() {
		return tmap_enable_status;
	}
	public void setTmap_enable_status(String tmap_enable_status) {
		this.tmap_enable_status = tmap_enable_status;
	}
	public String getTmap_approval_status() {
		return tmap_approval_status;
	}
	public void setTmap_approval_status(String tmap_approval_status) {
		this.tmap_approval_status = tmap_approval_status;
	}
	public int getTmap_task_id() {
		return tmap_task_id;
	}
	public void setTmap_task_id(int tmap_task_id) {
		this.tmap_task_id = tmap_task_id;
	}
	public int getTmap_added_by() {
		return tmap_added_by;
	}
	public void setTmap_added_by(int tmap_added_by) {
		this.tmap_added_by = tmap_added_by;
	}
	public Date getTmap_created_at() {
		return tmap_created_at;
	}
	public void setTmap_created_at(Date tmap_created_at) {
		this.tmap_created_at = tmap_created_at;
	}
	public int getTmap_fh_user_id() {
		return tmap_fh_user_id;
	}
	public void setTmap_fh_user_id(int tmap_fh_user_id) {
		this.tmap_fh_user_id = tmap_fh_user_id;
	}
	
	
	
	
	
}
