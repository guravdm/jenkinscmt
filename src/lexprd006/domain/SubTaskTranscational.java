package lexprd006.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity(name = "trn_sub_task_transactional")
public class SubTaskTranscational {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ttrn_sub_id;
	@Size(max=1000)
	private String ttrn_sub_task_id;
	private String ttrn_sub_client_task_id;
	private int ttrn_sub_task_alert_prior_day;
	private String ttrn_sub_task_status;
	private Date ttrn_sub_task_compl_date;
	private Date ttrn_sub_task_submition_date;
	private Date tttn_sub_task_next_examination_date;
	private Date ttrn_sub_task_activation_date;
	private int ttrn_sub_task_buffer_days;
	private Date ttrn_sub_task_pr_due_date;
	private Date ttrn_sub_task_rw_date;
	private Date ttrn_sub_task_FH_due_date;
	private Date ttrn_sub_task_UH_due_date;
	private Date ttrn_sub_task_ENT_due_date;
	private Date ttrn_sub_task_updated_at;
	private String ttrn_sub_task_comment;
	private String ttrn_sub_task_completed_by;
	private String ttrn_sub_task_proof_document;
	private Date ttrn_sub_task_created_at;
	private Date ttrn_sub_task_first_alert;
	private Date ttrn_sub_task_second_alert;
	private Date ttrn_sub_task_third_alert;
	private int ttrn_sub_task_back_date_allowed;
	private int ttrn_sub_task_document;
	private int ttrn_sub_task_historical;
	private String ttrn_sub_task_reason_for_non_compliance;
	private String ttrn_sub_task_allow_approver_reopening;
	private Date ttrn_sub_task_approved_date;
	private int ttrn_sub_task_approved_by;
	
	public int getTtrn_sub_id() {
		return ttrn_sub_id;
	}
	public void setTtrn_sub_id(int ttrn_sub_id) {
		this.ttrn_sub_id = ttrn_sub_id;
	}
	public String getTtrn_sub_task_id() {
		return ttrn_sub_task_id;
	}
	public void setTtrn_sub_task_id(String ttrn_sub_task_id) {
		this.ttrn_sub_task_id = ttrn_sub_task_id;
	}
	public String getTtrn_sub_client_task_id() {
		return ttrn_sub_client_task_id;
	}
	public void setTtrn_sub_client_task_id(String ttrn_sub_client_task_id) {
		this.ttrn_sub_client_task_id = ttrn_sub_client_task_id;
	}
	public int getTtrn_sub_task_alert_prior_day() {
		return ttrn_sub_task_alert_prior_day;
	}
	public void setTtrn_sub_task_alert_prior_day(int ttrn_sub_task_alert_prior_day) {
		this.ttrn_sub_task_alert_prior_day = ttrn_sub_task_alert_prior_day;
	}
	public String getTtrn_sub_task_status() {
		return ttrn_sub_task_status;
	}
	public void setTtrn_sub_task_status(String ttrn_sub_task_status) {
		this.ttrn_sub_task_status = ttrn_sub_task_status;
	}
	public Date getTtrn_sub_task_compl_date() {
		return ttrn_sub_task_compl_date;
	}
	public void setTtrn_sub_task_compl_date(Date ttrn_sub_task_compl_date) {
		this.ttrn_sub_task_compl_date = ttrn_sub_task_compl_date;
	}
	public Date getTtrn_sub_task_submition_date() {
		return ttrn_sub_task_submition_date;
	}
	public void setTtrn_sub_task_submition_date(Date ttrn_sub_task_submition_date) {
		this.ttrn_sub_task_submition_date = ttrn_sub_task_submition_date;
	}
	public Date getTttn_sub_task_next_examination_date() {
		return tttn_sub_task_next_examination_date;
	}
	public void setTttn_sub_task_next_examination_date(Date tttn_sub_task_next_examination_date) {
		this.tttn_sub_task_next_examination_date = tttn_sub_task_next_examination_date;
	}
	public Date getTtrn_sub_task_activation_date() {
		return ttrn_sub_task_activation_date;
	}
	public void setTtrn_sub_task_activation_date(Date ttrn_sub_task_activation_date) {
		this.ttrn_sub_task_activation_date = ttrn_sub_task_activation_date;
	}
	public int getTtrn_sub_task_buffer_days() {
		return ttrn_sub_task_buffer_days;
	}
	public void setTtrn_sub_task_buffer_days(int ttrn_sub_task_buffer_days) {
		this.ttrn_sub_task_buffer_days = ttrn_sub_task_buffer_days;
	}
	public Date getTtrn_sub_task_pr_due_date() {
		return ttrn_sub_task_pr_due_date;
	}
	public void setTtrn_sub_task_pr_due_date(Date ttrn_sub_task_pr_due_date) {
		this.ttrn_sub_task_pr_due_date = ttrn_sub_task_pr_due_date;
	}
	public Date getTtrn_sub_task_rw_date() {
		return ttrn_sub_task_rw_date;
	}
	public void setTtrn_sub_task_rw_date(Date ttrn_sub_task_rw_date) {
		this.ttrn_sub_task_rw_date = ttrn_sub_task_rw_date;
	}
	public Date getTtrn_sub_task_FH_due_date() {
		return ttrn_sub_task_FH_due_date;
	}
	public void setTtrn_sub_task_FH_due_date(Date ttrn_sub_task_FH_due_date) {
		this.ttrn_sub_task_FH_due_date = ttrn_sub_task_FH_due_date;
	}
	public Date getTtrn_sub_task_UH_due_date() {
		return ttrn_sub_task_UH_due_date;
	}
	public void setTtrn_sub_task_UH_due_date(Date ttrn_sub_task_UH_due_date) {
		this.ttrn_sub_task_UH_due_date = ttrn_sub_task_UH_due_date;
	}
	public Date getTtrn_sub_task_ENT_due_date() {
		return ttrn_sub_task_ENT_due_date;
	}
	public void setTtrn_sub_task_ENT_due_date(Date ttrn_sub_task_ENT_due_date) {
		this.ttrn_sub_task_ENT_due_date = ttrn_sub_task_ENT_due_date;
	}
	public Date getTtrn_sub_task_updated_at() {
		return ttrn_sub_task_updated_at;
	}
	public void setTtrn_sub_task_updated_at(Date ttrn_sub_task_updated_at) {
		this.ttrn_sub_task_updated_at = ttrn_sub_task_updated_at;
	}
	public String getTtrn_sub_task_comment() {
		return ttrn_sub_task_comment;
	}
	public void setTtrn_sub_task_comment(String ttrn_sub_task_comment) {
		this.ttrn_sub_task_comment = ttrn_sub_task_comment;
	}
	public String getTtrn_sub_task_completed_by() {
		return ttrn_sub_task_completed_by;
	}
	public void setTtrn_sub_task_completed_by(String ttrn_sub_task_completed_by) {
		this.ttrn_sub_task_completed_by = ttrn_sub_task_completed_by;
	}
	public String getTtrn_sub_task_proof_document() {
		return ttrn_sub_task_proof_document;
	}
	public void setTtrn_sub_task_proof_document(String ttrn_sub_task_proof_document) {
		this.ttrn_sub_task_proof_document = ttrn_sub_task_proof_document;
	}
	public Date getTtrn_sub_task_created_at() {
		return ttrn_sub_task_created_at;
	}
	public void setTtrn_sub_task_created_at(Date ttrn_sub_task_created_at) {
		this.ttrn_sub_task_created_at = ttrn_sub_task_created_at;
	}
	public Date getTtrn_sub_task_first_alert() {
		return ttrn_sub_task_first_alert;
	}
	public void setTtrn_sub_task_first_alert(Date ttrn_sub_task_first_alert) {
		this.ttrn_sub_task_first_alert = ttrn_sub_task_first_alert;
	}
	public Date getTtrn_sub_task_second_alert() {
		return ttrn_sub_task_second_alert;
	}
	public void setTtrn_sub_task_second_alert(Date ttrn_sub_task_second_alert) {
		this.ttrn_sub_task_second_alert = ttrn_sub_task_second_alert;
	}
	public Date getTtrn_sub_task_third_alert() {
		return ttrn_sub_task_third_alert;
	}
	public void setTtrn_sub_task_third_alert(Date ttrn_sub_task_third_alert) {
		this.ttrn_sub_task_third_alert = ttrn_sub_task_third_alert;
	}
	public int getTtrn_sub_task_back_date_allowed() {
		return ttrn_sub_task_back_date_allowed;
	}
	public void setTtrn_sub_task_back_date_allowed(int ttrn_sub_task_back_date_allowed) {
		this.ttrn_sub_task_back_date_allowed = ttrn_sub_task_back_date_allowed;
	}
	public int getTtrn_sub_task_document() {
		return ttrn_sub_task_document;
	}
	public void setTtrn_sub_task_document(int ttrn_sub_task_document) {
		this.ttrn_sub_task_document = ttrn_sub_task_document;
	}
	public int getTtrn_sub_task_historical() {
		return ttrn_sub_task_historical;
	}
	public void setTtrn_sub_task_historical(int ttrn_sub_task_historical) {
		this.ttrn_sub_task_historical = ttrn_sub_task_historical;
	}
	public String getTtrn_sub_task_reason_for_non_compliance() {
		return ttrn_sub_task_reason_for_non_compliance;
	}
	public void setTtrn_sub_task_reason_for_non_compliance(String ttrn_sub_task_reason_for_non_compliance) {
		this.ttrn_sub_task_reason_for_non_compliance = ttrn_sub_task_reason_for_non_compliance;
	}
	public String getTtrn_sub_task_allow_approver_reopening() {
		return ttrn_sub_task_allow_approver_reopening;
	}
	public void setTtrn_sub_task_allow_approver_reopening(String ttrn_sub_task_allow_approver_reopening) {
		this.ttrn_sub_task_allow_approver_reopening = ttrn_sub_task_allow_approver_reopening;
	}
	public Date getTtrn_sub_task_approved_date() {
		return ttrn_sub_task_approved_date;
	}
	public void setTtrn_sub_task_approved_date(Date ttrn_sub_task_approved_date) {
		this.ttrn_sub_task_approved_date = ttrn_sub_task_approved_date;
	}
	public int getTtrn_sub_task_approved_by() {
		return ttrn_sub_task_approved_by;
	}
	public void setTtrn_sub_task_approved_by(int ttrn_sub_task_approved_by) {
		this.ttrn_sub_task_approved_by = ttrn_sub_task_approved_by;
	}
	
	
	
}
