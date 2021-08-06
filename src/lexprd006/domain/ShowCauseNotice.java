package lexprd006.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/*
 * Author: Harshad Padole
 * Date: 09/05/2017
 * Purpose: Domain for Show cause notice
 * 
 * 
 * 
 * */

@Entity(name = "mst_showcausenotice")
@Table(name = "mst_showcausenotice")
// , indexes = @Index(columnList = "scau_id", name = "idx_scau_id")
public class ShowCauseNotice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int scau_id;
	private int scau_orga_id;
	private int scau_loca_id;
	private int scau_dept_id;
	private String scau_ralated_to;
	private Date scau_notice_date;
	private Date scau_received_date;
	private Date scau_deadline_date;
	@Size(max = 5000)
	private String scau_comments;
	private String scau_action_taken;
	private String scau_next_action_item;
	private int scau_responsible_person;
	private int scau_reporting_person;
	private Date scau_reminder_date;
	private int scau_added_by;
	private Date scau_created_at;
	private Date scau_updated_at;
	private String scau_status;
	public int getScau_id() {
		return scau_id;
	}
	public void setScau_id(int scau_id) {
		this.scau_id = scau_id;
	}
	public int getScau_orga_id() {
		return scau_orga_id;
	}
	public void setScau_orga_id(int scau_orga_id) {
		this.scau_orga_id = scau_orga_id;
	}
	public int getScau_loca_id() {
		return scau_loca_id;
	}
	public void setScau_loca_id(int scau_loca_id) {
		this.scau_loca_id = scau_loca_id;
	}
	public int getScau_dept_id() {
		return scau_dept_id;
	}
	public void setScau_dept_id(int scau_dept_id) {
		this.scau_dept_id = scau_dept_id;
	}
	public String getScau_ralated_to() {
		return scau_ralated_to;
	}
	public void setScau_ralated_to(String scau_ralated_to) {
		this.scau_ralated_to = scau_ralated_to;
	}
	public Date getScau_notice_date() {
		return scau_notice_date;
	}
	public void setScau_notice_date(Date scau_notice_date) {
		this.scau_notice_date = scau_notice_date;
	}
	public Date getScau_received_date() {
		return scau_received_date;
	}
	public void setScau_received_date(Date scau_received_date) {
		this.scau_received_date = scau_received_date;
	}
	public Date getScau_deadline_date() {
		return scau_deadline_date;
	}
	public void setScau_deadline_date(Date scau_deadline_date) {
		this.scau_deadline_date = scau_deadline_date;
	}
	public String getScau_comments() {
		return scau_comments;
	}
	public void setScau_comments(String scau_comments) {
		this.scau_comments = scau_comments;
	}
	public String getScau_action_taken() {
		return scau_action_taken;
	}
	public void setScau_action_taken(String scau_action_taken) {
		this.scau_action_taken = scau_action_taken;
	}
	public String getScau_next_action_item() {
		return scau_next_action_item;
	}
	public void setScau_next_action_item(String scau_next_action_item) {
		this.scau_next_action_item = scau_next_action_item;
	}
	public int getScau_responsible_person() {
		return scau_responsible_person;
	}
	public void setScau_responsible_person(int scau_responsible_person) {
		this.scau_responsible_person = scau_responsible_person;
	}
	public int getScau_reporting_person() {
		return scau_reporting_person;
	}
	public void setScau_reporting_person(int scau_reporting_person) {
		this.scau_reporting_person = scau_reporting_person;
	}
	
	public int getScau_added_by() {
		return scau_added_by;
	}
	public void setScau_added_by(int scau_added_by) {
		this.scau_added_by = scau_added_by;
	}
	public Date getScau_created_at() {
		return scau_created_at;
	}
	public void setScau_created_at(Date scau_created_at) {
		this.scau_created_at = scau_created_at;
	}
	public Date getScau_updated_at() {
		return scau_updated_at;
	}
	public void setScau_updated_at(Date scau_updated_at) {
		this.scau_updated_at = scau_updated_at;
	}
	public String getScau_status() {
		return scau_status;
	}
	public void setScau_status(String scau_status) {
		this.scau_status = scau_status;
	}
	public Date getScau_reminder_date() {
		return scau_reminder_date;
	}
	public void setScau_reminder_date(Date scau_reminder_date) {
		this.scau_reminder_date = scau_reminder_date;
	}
	
	
}
