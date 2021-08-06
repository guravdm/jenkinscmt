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


@Entity(name="mst_designation")
@Table(name="mst_designation")
//, indexes=@Index(name="idx_desi_id_Name", columnList="desi_id,desi_name")
public class Designation {
   
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int desi_id;
	private String desi_name;
	private int desi_parent_id;
	private String desi_approval_status;
	private String desi_enable_status;
	private int desi_added_by;
	private Date desi_created_at;
	
	public int getDesi_id() {
		return desi_id;
	}
	public void setDesi_id(int desi_id) {
		this.desi_id = desi_id;
	}
	public String getDesi_name() {
		return desi_name;
	}
	public void setDesi_name(String desi_name) {
		this.desi_name = desi_name;
	}
	public int getDesi_parent_id() {
		return desi_parent_id;
	}
	public void setDesi_parent_id(int desi_parent_id) {
		this.desi_parent_id = desi_parent_id;
	}
	public String getDesi_approval_status() {
		return desi_approval_status;
	}
	public void setDesi_approval_status(String desi_approval_status) {
		this.desi_approval_status = desi_approval_status;
	}
	public String getDesi_enable_status() {
		return desi_enable_status;
	}
	public void setDesi_enable_status(String desi_enable_status) {
		this.desi_enable_status = desi_enable_status;
	}
	public int getDesi_added_by() {
		return desi_added_by;
	}
	public void setDesi_added_by(int desi_added_by) {
		this.desi_added_by = desi_added_by;
	}
	public Date getDesi_created_at() {
		return desi_created_at;
	}
	public void setDesi_created_at(Date desi_created_at) {
		this.desi_created_at = desi_created_at;
	}
	
	
}
