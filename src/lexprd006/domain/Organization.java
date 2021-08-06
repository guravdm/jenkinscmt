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
 * Created Date : 26/12/2017
 * Updated By : 
 * Updated Date : 
 *  
 * */

@Entity(name="mst_organization")
@Table(name="mst_organization")
// , indexes=@Index(name="idx_orga_id_name", columnList="orga_id,orga_name")
public class Organization {
   
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int orga_id;
	private String orga_name;
	private int orga_parent_id;
	private String orga_approval_status;
	private String orga_enable_status;
	private int orga_added_by;
	private Date orga_created_at;
	
	public int getOrga_id() {
		return orga_id;
	}
	public void setOrga_id(int orga_id) {
		this.orga_id = orga_id;
	}
	public String getOrga_name() {
		return orga_name;
	}
	public void setOrga_name(String orga_name) {
		this.orga_name = orga_name;
	}
	public int getOrga_parent_id() {
		return orga_parent_id;
	}
	public void setOrga_parent_id(int orga_parent_id) {
		this.orga_parent_id = orga_parent_id;
	}
	public String getOrga_approval_status() {
		return orga_approval_status;
	}
	public void setOrga_approval_status(String orga_approval_status) {
		this.orga_approval_status = orga_approval_status;
	}
	public String getOrga_enable_status() {
		return orga_enable_status;
	}
	public void setOrga_enable_status(String orga_enable_status) {
		this.orga_enable_status = orga_enable_status;
	}
	public int getOrga_added_by() {
		return orga_added_by;
	}
	public void setOrga_added_by(int orga_added_by) {
		this.orga_added_by = orga_added_by;
	}
	public Date getOrga_created_at() {
		return orga_created_at;
	}
	public void setOrga_created_at(Date orga_created_at) {
		this.orga_created_at = orga_created_at;
	}
	
	
	
}
