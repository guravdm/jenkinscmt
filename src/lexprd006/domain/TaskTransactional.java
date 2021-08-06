
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
 * Author : Mahesh Kharote
 * Created Date : 10/01/2017
 * Updated By : 
 * Updated Date : 
 *  
 * */

@Entity(name = "trn_task_transactional")
@Table(name = "trn_task_transactional")
// , indexes = { @Index(name = "idx_ttrn_id", columnList = "ttrn_id"),
// @Index(name = "idx_tlglDate_status", columnList = "ttrn_legal_due_date, ttrn_status") }
public class TaskTransactional {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ttrn_id;
	@Size(max = 5000)
	private String ttrn_performer_comments;
	private Date ttrn_completed_date;
	private String ttrn_status;
	private String ttrn_tasks_status = "NA";
	private Date ttrn_submitted_date;
	private String ttrn_client_task_id;
	private Date ttrn_legal_due_date;
	private Date ttrn_fh_due_date;
	private Date ttrn_pr_due_date;
	private Date ttrn_rw_due_date;
	private String ttrn_frequency_for_alerts;
	private String ttrn_frequency_for_operation;
	private String ttrn_impact;
	private String ttrn_impact_on_unit;
	private String ttrn_impact_on_organization;
	private int ttrn_added_by;
	private Date ttrn_created_at;
	@Size(max = 5000)
	private String ttrn_reason_for_non_compliance;
	private String ttrn_document;
	private String ttrn_historical;
	private int ttrn_performer_user_id;
	private int ttrn_prior_days_buffer;
	private Date ttrn_activation_date;
	private int ttrn_alert_days;
	private int ttrn_task_completed_by;
	private String ttrn_allow_back_date_completion;
	private int ttrn_no_of_back_days_allowed;
	private Date ttrn_uh_due_date;
	private Date ttrn_first_alert;
	private Date ttrn_second_alert;
	private Date ttrn_third_alert;
	private String ttrn_allow_approver_reopening;
	private int ttrn_task_approved_by;
	private Date ttrn_task_approved_date;

	private String auditorStatus;
	private String auditoComments;
	private Date auditorAuditTime;
	private String auditor_performer_by_id = "0";

	private Date auditDate;
	private Integer isAuditTasks = 0;
	private Date reOpenDateWindow;

	private String ttrn_is_Task_Audited;
	private Integer isDocumentUpload = 0;

	public TaskTransactional() {
	}

	@Override
	public String toString() {
		return "TaskTransactional [ttrn_id=" + ttrn_id + ", ttrn_performer_comments=" + ttrn_performer_comments
				+ ", ttrn_completed_date=" + ttrn_completed_date + ", ttrn_status=" + ttrn_status
				+ ", ttrn_tasks_status=" + ttrn_tasks_status + ", ttrn_submitted_date=" + ttrn_submitted_date
				+ ", ttrn_client_task_id=" + ttrn_client_task_id + ", ttrn_legal_due_date=" + ttrn_legal_due_date
				+ ", ttrn_fh_due_date=" + ttrn_fh_due_date + ", ttrn_pr_due_date=" + ttrn_pr_due_date
				+ ", ttrn_rw_due_date=" + ttrn_rw_due_date + ", ttrn_frequency_for_alerts=" + ttrn_frequency_for_alerts
				+ ", ttrn_frequency_for_operation=" + ttrn_frequency_for_operation + ", ttrn_impact=" + ttrn_impact
				+ ", ttrn_impact_on_unit=" + ttrn_impact_on_unit + ", ttrn_impact_on_organization="
				+ ttrn_impact_on_organization + ", ttrn_added_by=" + ttrn_added_by + ", ttrn_created_at="
				+ ttrn_created_at + ", ttrn_reason_for_non_compliance=" + ttrn_reason_for_non_compliance
				+ ", ttrn_document=" + ttrn_document + ", ttrn_historical=" + ttrn_historical
				+ ", ttrn_performer_user_id=" + ttrn_performer_user_id + ", ttrn_prior_days_buffer="
				+ ttrn_prior_days_buffer + ", ttrn_activation_date=" + ttrn_activation_date + ", ttrn_alert_days="
				+ ttrn_alert_days + ", ttrn_task_completed_by=" + ttrn_task_completed_by
				+ ", ttrn_allow_back_date_completion=" + ttrn_allow_back_date_completion
				+ ", ttrn_no_of_back_days_allowed=" + ttrn_no_of_back_days_allowed + ", ttrn_uh_due_date="
				+ ttrn_uh_due_date + ", ttrn_first_alert=" + ttrn_first_alert + ", ttrn_second_alert="
				+ ttrn_second_alert + ", ttrn_third_alert=" + ttrn_third_alert + ", ttrn_allow_approver_reopening="
				+ ttrn_allow_approver_reopening + ", ttrn_task_approved_by=" + ttrn_task_approved_by
				+ ", ttrn_task_approved_date=" + ttrn_task_approved_date + ", auditorStatus=" + auditorStatus
				+ ", auditoComments=" + auditoComments + ", auditorAuditTime=" + auditorAuditTime
				+ ", auditor_performer_by_id=" + auditor_performer_by_id + ", auditDate=" + auditDate
				+ ", isAuditTasks=" + isAuditTasks + ", reOpenDateWindow=" + reOpenDateWindow
				+ ", ttrn_is_Task_Audited=" + ttrn_is_Task_Audited + ", isDocumentUpload=" + isDocumentUpload + "]";
	}

	public int getTtrn_id() {
		return ttrn_id;
	}

	public void setTtrn_id(int ttrn_id) {
		this.ttrn_id = ttrn_id;
	}

	public String getTtrn_performer_comments() {
		return ttrn_performer_comments;
	}

	public void setTtrn_performer_comments(String ttrn_performer_comments) {
		this.ttrn_performer_comments = ttrn_performer_comments;
	}

	public Date getTtrn_completed_date() {
		return ttrn_completed_date;
	}

	public void setTtrn_completed_date(Date ttrn_completed_date) {
		this.ttrn_completed_date = ttrn_completed_date;
	}

	public String getTtrn_status() {
		return ttrn_status;
	}

	public void setTtrn_status(String ttrn_status) {
		this.ttrn_status = ttrn_status;
	}

	public String getTtrn_tasks_status() {
		return ttrn_tasks_status;
	}

	public void setTtrn_tasks_status(String ttrn_tasks_status) {
		this.ttrn_tasks_status = ttrn_tasks_status;
	}

	public Date getTtrn_submitted_date() {
		return ttrn_submitted_date;
	}

	public void setTtrn_submitted_date(Date ttrn_submitted_date) {
		this.ttrn_submitted_date = ttrn_submitted_date;
	}

	public String getTtrn_client_task_id() {
		return ttrn_client_task_id;
	}

	public void setTtrn_client_task_id(String ttrn_client_task_id) {
		this.ttrn_client_task_id = ttrn_client_task_id;
	}

	public Date getTtrn_legal_due_date() {
		return ttrn_legal_due_date;
	}

	public void setTtrn_legal_due_date(Date ttrn_legal_due_date) {
		this.ttrn_legal_due_date = ttrn_legal_due_date;
	}

	public Date getTtrn_fh_due_date() {
		return ttrn_fh_due_date;
	}

	public void setTtrn_fh_due_date(Date ttrn_fh_due_date) {
		this.ttrn_fh_due_date = ttrn_fh_due_date;
	}

	public Date getTtrn_pr_due_date() {
		return ttrn_pr_due_date;
	}

	public void setTtrn_pr_due_date(Date ttrn_pr_due_date) {
		this.ttrn_pr_due_date = ttrn_pr_due_date;
	}

	public Date getTtrn_rw_due_date() {
		return ttrn_rw_due_date;
	}

	public void setTtrn_rw_due_date(Date ttrn_rw_due_date) {
		this.ttrn_rw_due_date = ttrn_rw_due_date;
	}

	public String getTtrn_frequency_for_alerts() {
		return ttrn_frequency_for_alerts;
	}

	public void setTtrn_frequency_for_alerts(String ttrn_frequency_for_alerts) {
		this.ttrn_frequency_for_alerts = ttrn_frequency_for_alerts;
	}

	public String getTtrn_frequency_for_operation() {
		return ttrn_frequency_for_operation;
	}

	public void setTtrn_frequency_for_operation(String ttrn_frequency_for_operation) {
		this.ttrn_frequency_for_operation = ttrn_frequency_for_operation;
	}

	public String getTtrn_impact() {
		return ttrn_impact;
	}

	public void setTtrn_impact(String ttrn_impact) {
		this.ttrn_impact = ttrn_impact;
	}

	public String getTtrn_impact_on_unit() {
		return ttrn_impact_on_unit;
	}

	public void setTtrn_impact_on_unit(String ttrn_impact_on_unit) {
		this.ttrn_impact_on_unit = ttrn_impact_on_unit;
	}

	public String getTtrn_impact_on_organization() {
		return ttrn_impact_on_organization;
	}

	public void setTtrn_impact_on_organization(String ttrn_impact_on_organization) {
		this.ttrn_impact_on_organization = ttrn_impact_on_organization;
	}

	public int getTtrn_added_by() {
		return ttrn_added_by;
	}

	public void setTtrn_added_by(int ttrn_added_by) {
		this.ttrn_added_by = ttrn_added_by;
	}

	public Date getTtrn_created_at() {
		return ttrn_created_at;
	}

	public void setTtrn_created_at(Date ttrn_created_at) {
		this.ttrn_created_at = ttrn_created_at;
	}

	public String getTtrn_reason_for_non_compliance() {
		return ttrn_reason_for_non_compliance;
	}

	public void setTtrn_reason_for_non_compliance(String ttrn_reason_for_non_compliance) {
		this.ttrn_reason_for_non_compliance = ttrn_reason_for_non_compliance;
	}

	public String getTtrn_document() {
		return ttrn_document;
	}

	public void setTtrn_document(String ttrn_document) {
		this.ttrn_document = ttrn_document;
	}

	public String getTtrn_historical() {
		return ttrn_historical;
	}

	public void setTtrn_historical(String ttrn_historical) {
		this.ttrn_historical = ttrn_historical;
	}

	public int getTtrn_performer_user_id() {
		return ttrn_performer_user_id;
	}

	public void setTtrn_performer_user_id(int ttrn_performer_user_id) {
		this.ttrn_performer_user_id = ttrn_performer_user_id;
	}

	public int getTtrn_prior_days_buffer() {
		return ttrn_prior_days_buffer;
	}

	public void setTtrn_prior_days_buffer(int ttrn_prior_days_buffer) {
		this.ttrn_prior_days_buffer = ttrn_prior_days_buffer;
	}

	public Date getTtrn_activation_date() {
		return ttrn_activation_date;
	}

	public void setTtrn_activation_date(Date ttrn_activation_date) {
		this.ttrn_activation_date = ttrn_activation_date;
	}

	public int getTtrn_alert_days() {
		return ttrn_alert_days;
	}

	public void setTtrn_alert_days(int ttrn_alert_days) {
		this.ttrn_alert_days = ttrn_alert_days;
	}

	public int getTtrn_task_completed_by() {
		return ttrn_task_completed_by;
	}

	public void setTtrn_task_completed_by(int ttrn_task_completed_by) {
		this.ttrn_task_completed_by = ttrn_task_completed_by;
	}

	public String getTtrn_allow_back_date_completion() {
		return ttrn_allow_back_date_completion;
	}

	public void setTtrn_allow_back_date_completion(String ttrn_allow_back_date_completion) {
		this.ttrn_allow_back_date_completion = ttrn_allow_back_date_completion;
	}

	public int getTtrn_no_of_back_days_allowed() {
		return ttrn_no_of_back_days_allowed;
	}

	public void setTtrn_no_of_back_days_allowed(int ttrn_no_of_back_days_allowed) {
		this.ttrn_no_of_back_days_allowed = ttrn_no_of_back_days_allowed;
	}

	public Date getTtrn_uh_due_date() {
		return ttrn_uh_due_date;
	}

	public void setTtrn_uh_due_date(Date ttrn_uh_due_date) {
		this.ttrn_uh_due_date = ttrn_uh_due_date;
	}

	public Date getTtrn_first_alert() {
		return ttrn_first_alert;
	}

	public void setTtrn_first_alert(Date ttrn_first_alert) {
		this.ttrn_first_alert = ttrn_first_alert;
	}

	public Date getTtrn_second_alert() {
		return ttrn_second_alert;
	}

	public void setTtrn_second_alert(Date ttrn_second_alert) {
		this.ttrn_second_alert = ttrn_second_alert;
	}

	public Date getTtrn_third_alert() {
		return ttrn_third_alert;
	}

	public void setTtrn_third_alert(Date ttrn_third_alert) {
		this.ttrn_third_alert = ttrn_third_alert;
	}

	public String getTtrn_allow_approver_reopening() {
		return ttrn_allow_approver_reopening;
	}

	public void setTtrn_allow_approver_reopening(String ttrn_allow_approver_reopening) {
		this.ttrn_allow_approver_reopening = ttrn_allow_approver_reopening;
	}

	public int getTtrn_task_approved_by() {
		return ttrn_task_approved_by;
	}

	public void setTtrn_task_approved_by(int ttrn_task_approved_by) {
		this.ttrn_task_approved_by = ttrn_task_approved_by;
	}

	public Date getTtrn_task_approved_date() {
		return ttrn_task_approved_date;
	}

	public void setTtrn_task_approved_date(Date ttrn_task_approved_date) {
		this.ttrn_task_approved_date = ttrn_task_approved_date;
	}

	public String getAuditorStatus() {
		return auditorStatus;
	}

	public void setAuditorStatus(String auditorStatus) {
		this.auditorStatus = auditorStatus;
	}

	public String getAuditoComments() {
		return auditoComments;
	}

	public void setAuditoComments(String auditoComments) {
		this.auditoComments = auditoComments;
	}

	public Date getAuditorAuditTime() {
		return auditorAuditTime;
	}

	public void setAuditorAuditTime(Date auditorAuditTime) {
		this.auditorAuditTime = auditorAuditTime;
	}

	public String getAuditor_performer_by_id() {
		return auditor_performer_by_id;
	}

	public void setAuditor_performer_by_id(String auditor_performer_by_id) {
		this.auditor_performer_by_id = auditor_performer_by_id;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public Integer getIsAuditTasks() {
		return isAuditTasks;
	}

	public void setIsAuditTasks(Integer isAuditTasks) {
		this.isAuditTasks = isAuditTasks;
	}

	public Date getReOpenDateWindow() {
		return reOpenDateWindow;
	}

	public void setReOpenDateWindow(Date reOpenDateWindow) {
		this.reOpenDateWindow = reOpenDateWindow;
	}

	public String getTtrn_is_Task_Audited() {
		return ttrn_is_Task_Audited;
	}

	public void setTtrn_is_Task_Audited(String ttrn_is_Task_Audited) {
		this.ttrn_is_Task_Audited = ttrn_is_Task_Audited;
	}

	public Integer getIsDocumentUpload() {
		return isDocumentUpload;
	}

	public void setIsDocumentUpload(Integer isDocumentUpload) {
		this.isDocumentUpload = isDocumentUpload;
	}

}
