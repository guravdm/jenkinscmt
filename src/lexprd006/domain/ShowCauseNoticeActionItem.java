package lexprd006.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/*
 * Author: Harshad Padole
 * Date: 12/05/2017
 * Purpose: Domain for Show cause notice action items
 * 
 * 
 * 
 * */
@Entity(name= "trn_show_cause_notice")
@Table(name = "trn_show_cause_notice")
// , indexes =@Index(name ="idx_tscn_id", columnList = "tscn_id")
public class ShowCauseNoticeActionItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int tscn_id;
	private String tscn_comment;
	private String tscn_action_taken;
	private Date tscn_next_due_date;
	private Date tscn_reminder_date;
	private String tscn_next_action_item;
	private int tcau_scau_id;
	private String tcau_status;
	private String tcau_closing_comment;
	private Date tscn_created_at;
	private Date tcau_updated_at;
	private int tcau_added_by;
	
	public int getTcau_added_by() {
		return tcau_added_by;
	}
	public void setTcau_added_by(int tcau_added_by) {
		this.tcau_added_by = tcau_added_by;
	}
	public int getTscn_id() {
		return tscn_id;
	}
	public void setTscn_id(int tscn_id) {
		this.tscn_id = tscn_id;
	}
	public String getTscn_comment() {
		return tscn_comment;
	}
	public void setTscn_comment(String tscn_comment) {
		this.tscn_comment = tscn_comment;
	}
	public String getTscn_action_taken() {
		return tscn_action_taken;
	}
	public void setTscn_action_taken(String tscn_action_taken) {
		this.tscn_action_taken = tscn_action_taken;
	}
	public Date getTscn_next_due_date() {
		return tscn_next_due_date;
	}
	public void setTscn_next_due_date(Date tscn_next_due_date) {
		this.tscn_next_due_date = tscn_next_due_date;
	}
	public Date getTscn_reminder_date() {
		return tscn_reminder_date;
	}
	public void setTscn_reminder_date(Date tscn_reminder_date) {
		this.tscn_reminder_date = tscn_reminder_date;
	}
	public String getTscn_next_action_item() {
		return tscn_next_action_item;
	}
	public void setTscn_next_action_item(String tscn_next_action_item) {
		this.tscn_next_action_item = tscn_next_action_item;
	}
	public int getTcau_scau_id() {
		return tcau_scau_id;
	}
	public void setTcau_scau_id(int tcau_scau_id) {
		this.tcau_scau_id = tcau_scau_id;
	}
	public String getTcau_status() {
		return tcau_status;
	}
	public void setTcau_status(String tcau_status) {
		this.tcau_status = tcau_status;
	}
	public String getTcau_closing_comment() {
		return tcau_closing_comment;
	}
	public void setTcau_closing_comment(String tcau_closing_comment) {
		this.tcau_closing_comment = tcau_closing_comment;
	}
	public Date getTscn_created_at() {
		return tscn_created_at;
	}
	public void setTscn_created_at(Date tscn_created_at) {
		this.tscn_created_at = tscn_created_at;
	}
	public Date getTcau_updated_at() {
		return tcau_updated_at;
	}
	public void setTcau_updated_at(Date tcau_updated_at) {
		this.tcau_updated_at = tcau_updated_at;
	}
	
	
	
	
	
	
}
