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
 * Created Date : 30/12/2016
 * Updated By :
 * Updated Date : 
 *  
 * */

@Entity(name = "cfg_user_entity_mapping")
@Table(name = "cfg_user_entity_mapping")
// , indexes = @Index(name = "idx_UserEntityMapping", columnList = "umap_orga_id,umap_loca_id,umap_dept_id,umap_user_id")
public class UserEntityMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int umap_id;
	private int umap_orga_id;
	private int umap_loca_id;
	private int umap_dept_id;
	private int umap_user_id;
	private String umap_approval_status;
	private int umap_added_by;
	private Date umap_created_at;
	private String umap_enable_status;
	public int getUmap_id() {
		return umap_id;
	}
	public void setUmap_id(int umap_id) {
		this.umap_id = umap_id;
	}
	public int getUmap_orga_id() {
		return umap_orga_id;
	}
	public void setUmap_orga_id(int umap_orga_id) {
		this.umap_orga_id = umap_orga_id;
	}
	public int getUmap_loca_id() {
		return umap_loca_id;
	}
	public void setUmap_loca_id(int umap_loca_id) {
		this.umap_loca_id = umap_loca_id;
	}
	public int getUmap_dept_id() {
		return umap_dept_id;
	}
	public void setUmap_dept_id(int umap_dept_id) {
		this.umap_dept_id = umap_dept_id;
	}
	public int getUmap_user_id() {
		return umap_user_id;
	}
	public void setUmap_user_id(int umap_user_id) {
		this.umap_user_id = umap_user_id;
	}
	public String getUmap_approval_status() {
		return umap_approval_status;
	}
	public void setUmap_approval_status(String umap_approval_status) {
		this.umap_approval_status = umap_approval_status;
	}
	public int getUmap_added_by() {
		return umap_added_by;
	}
	public void setUmap_added_by(int umap_added_by) {
		this.umap_added_by = umap_added_by;
	}
	public Date getUmap_created_at() {
		return umap_created_at;
	}
	public void setUmap_created_at(Date umap_created_at) {
		this.umap_created_at = umap_created_at;
	}
	public String getUmap_enable_status() {
		return umap_enable_status;
	}
	public void setUmap_enable_status(String umap_enable_status) {
		this.umap_enable_status = umap_enable_status;
	}
	
}
