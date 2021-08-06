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

@Entity(name = "mst_location")
@Table(name = "mst_location")
// , indexes = @Index(name = "idx_location_id_name", columnList = "loca_id,loca_name")
public class Location {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int loca_id;
	private String loca_name;
	private int loca_parent_id;
	private String loca_approval_status;
	private String loca_enable_status;
	private int loca_added_by;
	private Date loca_created_at;

	public int getLoca_id() {
		return loca_id;
	}

	public void setLoca_id(int loca_id) {
		this.loca_id = loca_id;
	}

	public String getLoca_name() {
		return loca_name;
	}

	public void setLoca_name(String loca_name) {
		this.loca_name = loca_name;
	}

	public int getLoca_parent_id() {
		return loca_parent_id;
	}

	public void setLoca_parent_id(int loca_parent_id) {
		this.loca_parent_id = loca_parent_id;
	}

	public String getLoca_approval_status() {
		return loca_approval_status;
	}

	public void setLoca_approval_status(String loca_approval_status) {
		this.loca_approval_status = loca_approval_status;
	}

	public String getLoca_enable_status() {
		return loca_enable_status;
	}

	public void setLoca_enable_status(String loca_enable_status) {
		this.loca_enable_status = loca_enable_status;
	}

	public int getLoca_added_by() {
		return loca_added_by;
	}

	public void setLoca_added_by(int loca_added_by) {
		this.loca_added_by = loca_added_by;
	}

	public Date getLoca_created_at() {
		return loca_created_at;
	}

	public void setLoca_created_at(Date loca_created_at) {
		this.loca_created_at = loca_created_at;
	}

}
