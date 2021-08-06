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

@Entity(name = "cfg_entity_mapping")
@Table(name="cfg_entity_mapping")
// , indexes=@Index(name="idx_enti_id", columnList="enti_id")
public class EntityMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int enti_id;
	private int enti_orga_id;
	private int enti_loca_id;
	private int enti_dept_id;
	private String enti_approval_status;
	private String enti_enable_status;
	private int enti_added_by;
	private Date enti_created_at;
	
	public int getEnti_id() {
		return enti_id;
	}
	public void setEnti_id(int enti_id) {
		this.enti_id = enti_id;
	}
	public int getEnti_orga_id() {
		return enti_orga_id;
	}
	public void setEnti_orga_id(int enti_orga_id) {
		this.enti_orga_id = enti_orga_id;
	}
	public int getEnti_loca_id() {
		return enti_loca_id;
	}
	public void setEnti_loca_id(int enti_loca_id) {
		this.enti_loca_id = enti_loca_id;
	}
	public int getEnti_dept_id() {
		return enti_dept_id;
	}
	public void setEnti_dept_id(int enti_dept_id) {
		this.enti_dept_id = enti_dept_id;
	}
	public String getEnti_approval_status() {
		return enti_approval_status;
	}
	public void setEnti_approval_status(String enti_approval_status) {
		this.enti_approval_status = enti_approval_status;
	}
	public String getEnti_enable_status() {
		return enti_enable_status;
	}
	public void setEnti_enable_status(String enti_enable_status) {
		this.enti_enable_status = enti_enable_status;
	}
	public int getEnti_added_by() {
		return enti_added_by;
	}
	public void setEnti_added_by(int enti_added_by) {
		this.enti_added_by = enti_added_by;
	}
	public Date getEnti_created_at() {
		return enti_created_at;
	}
	public void setEnti_created_at(Date enti_created_at) {
		this.enti_created_at = enti_created_at;
	}
	
}
