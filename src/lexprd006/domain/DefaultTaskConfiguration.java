package lexprd006.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/*
 * Author : Mahesh Kharote
 * Created Date : 10/01/2017
 * Updated By : 
 * Updated Date : 
 *  
 * */

@Entity(name="cfg_default_task_configuration")
public class DefaultTaskConfiguration {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int dtco_id;
	private String dtco_after_before;
	private String dtco_event;
	private String dtco_sub_event;
	private int dtco_pr_days;
	private int dtco_rw_days;
	private int dtco_fh_days;
	private int dtco_uh_days;
	private int dtco_legal_days;
	private String dtco_client_task_id;
	private String dtco_default_frequency;
	private String dtco_lexcare_task_id;
	private int dtco_mst_task_id;
	private Date dtco_created_at;
	private int dtco_added_by;
	private String dtco_status;
	private String dtco_comments;
	public int getDtco_id() {
		return dtco_id;
	}
	public void setDtco_id(int dtco_id) {
		this.dtco_id = dtco_id;
	}
	public String getDtco_after_before() {
		return dtco_after_before;
	}
	public void setDtco_after_before(String dtco_after_before) {
		this.dtco_after_before = dtco_after_before;
	}
	public String getDtco_event() {
		return dtco_event;
	}
	public void setDtco_event(String dtco_event) {
		this.dtco_event = dtco_event;
	}
	public String getDtco_sub_event() {
		return dtco_sub_event;
	}
	public void setDtco_sub_event(String dtco_sub_event) {
		this.dtco_sub_event = dtco_sub_event;
	}
	public int getDtco_pr_days() {
		return dtco_pr_days;
	}
	public void setDtco_pr_days(int dtco_pr_days) {
		this.dtco_pr_days = dtco_pr_days;
	}
	public int getDtco_rw_days() {
		return dtco_rw_days;
	}
	public void setDtco_rw_days(int dtco_rw_days) {
		this.dtco_rw_days = dtco_rw_days;
	}
	public int getDtco_fh_days() {
		return dtco_fh_days;
	}
	public void setDtco_fh_days(int dtco_fh_days) {
		this.dtco_fh_days = dtco_fh_days;
	}
	public int getDtco_uh_days() {
		return dtco_uh_days;
	}
	public void setDtco_uh_days(int dtco_uh_days) {
		this.dtco_uh_days = dtco_uh_days;
	}
	public int getDtco_legal_days() {
		return dtco_legal_days;
	}
	public void setDtco_legal_days(int dtco_legal_days) {
		this.dtco_legal_days = dtco_legal_days;
	}
	public String getDtco_client_task_id() {
		return dtco_client_task_id;
	}
	public void setDtco_client_task_id(String dtco_client_task_id) {
		this.dtco_client_task_id = dtco_client_task_id;
	}
	public String getDtco_default_frequency() {
		return dtco_default_frequency;
	}
	public void setDtco_default_frequency(String dtco_default_frequency) {
		this.dtco_default_frequency = dtco_default_frequency;
	}
	public String getDtco_lexcare_task_id() {
		return dtco_lexcare_task_id;
	}
	public void setDtco_lexcare_task_id(String dtco_lexcare_task_id) {
		this.dtco_lexcare_task_id = dtco_lexcare_task_id;
	}
	public int getDtco_mst_task_id() {
		return dtco_mst_task_id;
	}
	public void setDtco_mst_task_id(int dtco_mst_task_id) {
		this.dtco_mst_task_id = dtco_mst_task_id;
	}
	public Date getDtco_created_at() {
		return dtco_created_at;
	}
	public void setDtco_created_at(Date dtco_created_at) {
		this.dtco_created_at = dtco_created_at;
	}
	public int getDtco_added_by() {
		return dtco_added_by;
	}
	public void setDtco_added_by(int dtco_added_by) {
		this.dtco_added_by = dtco_added_by;
	}
	public String getDtco_status() {
		return dtco_status;
	}
	public void setDtco_status(String dtco_status) {
		this.dtco_status = dtco_status;
	}
	public String getDtco_comments() {
		return dtco_comments;
	}
	public void setDtco_comments(String dtco_comments) {
		this.dtco_comments = dtco_comments;
	}
	
	
}
