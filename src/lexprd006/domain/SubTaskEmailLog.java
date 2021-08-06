package lexprd006.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity(name = "subtask_email_log")
@Table(name = "subtask_email_log")
public class SubTaskEmailLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int log_id;
	private String client_task_id;
	private String sub_client_task_id;
	private String email_subject;
	private String email_to;
	private String created_at;
	private String entity_name;
	private String unit_name;
	private String function_name;
	@Size(max = 5000)
	private String name_of_legislation;
	@Size(max = 5000)
	private String name_of_rule;
	@Size(max = 5000)
	private String log_when;
	@Size(max = 5000)
	private String activity;
	private String frequency;
	private String impact;
	private String executor_date;
	private String evaluator_date;
	private String func_head_date;
	private String unit_head_date;
	private String legal_due_date;
	private int exec_id;
	private int eval_id;
	private String email_status;
	private String email_sent_at;
	private int fh_id;
	private String compliance_activity;
	private String sub_task_unit;
	private String name_of_contractor;
	private String compliance_title;

	public int getLog_id() {
		return log_id;
	}

	public void setLog_id(int log_id) {
		this.log_id = log_id;
	}

	public String getClient_task_id() {
		return client_task_id;
	}

	public void setClient_task_id(String client_task_id) {
		this.client_task_id = client_task_id;
	}

	public String getSub_client_task_id() {
		return sub_client_task_id;
	}

	public void setSub_client_task_id(String sub_client_task_id) {
		this.sub_client_task_id = sub_client_task_id;
	}

	public String getEmail_subject() {
		return email_subject;
	}

	public void setEmail_subject(String email_subject) {
		this.email_subject = email_subject;
	}

	public String getEmail_to() {
		return email_to;
	}

	public void setEmail_to(String email_to) {
		this.email_to = email_to;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getEntity_name() {
		return entity_name;
	}

	public void setEntity_name(String entity_name) {
		this.entity_name = entity_name;
	}

	public String getUnit_name() {
		return unit_name;
	}

	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}

	public String getFunction_name() {
		return function_name;
	}

	public void setFunction_name(String function_name) {
		this.function_name = function_name;
	}

	public String getName_of_legislation() {
		return name_of_legislation;
	}

	public void setName_of_legislation(String name_of_legislation) {
		this.name_of_legislation = name_of_legislation;
	}

	public String getName_of_rule() {
		return name_of_rule;
	}

	public void setName_of_rule(String name_of_rule) {
		this.name_of_rule = name_of_rule;
	}

	public String getLog_when() {
		return log_when;
	}

	public void setLog_when(String log_when) {
		this.log_when = log_when;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getImpact() {
		return impact;
	}

	public void setImpact(String impact) {
		this.impact = impact;
	}

	public String getExecutor_date() {
		return executor_date;
	}

	public void setExecutor_date(String executor_date) {
		this.executor_date = executor_date;
	}

	public String getEvaluator_date() {
		return evaluator_date;
	}

	public void setEvaluator_date(String evaluator_date) {
		this.evaluator_date = evaluator_date;
	}

	public String getFunc_head_date() {
		return func_head_date;
	}

	public void setFunc_head_date(String func_head_date) {
		this.func_head_date = func_head_date;
	}

	public String getUnit_head_date() {
		return unit_head_date;
	}

	public void setUnit_head_date(String unit_head_date) {
		this.unit_head_date = unit_head_date;
	}

	public String getLegal_due_date() {
		return legal_due_date;
	}

	public void setLegal_due_date(String legal_due_date) {
		this.legal_due_date = legal_due_date;
	}

	public int getExec_id() {
		return exec_id;
	}

	public void setExec_id(int exec_id) {
		this.exec_id = exec_id;
	}

	public int getEval_id() {
		return eval_id;
	}

	public void setEval_id(int eval_id) {
		this.eval_id = eval_id;
	}

	public String getEmail_status() {
		return email_status;
	}

	public void setEmail_status(String email_status) {
		this.email_status = email_status;
	}

	public String getEmail_sent_at() {
		return email_sent_at;
	}

	public void setEmail_sent_at(String email_sent_at) {
		this.email_sent_at = email_sent_at;
	}
	
	public int getFh_id() {
		return fh_id;
	}

	public void setFh_id(int fh_id) {
		this.fh_id = fh_id;
	}

	
	public String getCompliance_activity() {
		return compliance_activity;
	}

	public void setCompliance_activity(String compliance_activity) {
		this.compliance_activity = compliance_activity;
	}

	public String getSub_task_unit() {
		return sub_task_unit;
	}

	public void setSub_task_unit(String sub_task_unit) {
		this.sub_task_unit = sub_task_unit;
	}

	public String getName_of_contractor() {
		return name_of_contractor;
	}

	public void setName_of_contractor(String name_of_contractor) {
		this.name_of_contractor = name_of_contractor;
	}

	public String getCompliance_title() {
		return compliance_title;
	}

	public void setCompliance_title(String compliance_title) {
		this.compliance_title = compliance_title;
	}

	@Override
	public String toString() {
		return "SubTaskEmailLog [log_id=" + log_id + ", client_task_id=" + client_task_id + ", sub_client_task_id="
				+ sub_client_task_id + ", email_subject=" + email_subject + ", email_to=" + email_to + ", "
				+ ", created_at=" + created_at + ", entity_name=" + entity_name + ", unit_name=" + unit_name
				+ ", function_name=" + function_name + ", name_of_legislation=" + name_of_legislation
				+ ", name_of_rule=" + name_of_rule + ", log_when=" + log_when + ", activity=" + activity
				+ ", frequency=" + frequency + ", impact=" + impact + ", executor_date=" + executor_date
				+ ", evaluator_date=" + evaluator_date + ", func_head_date=" + func_head_date + ", unit_head_date="
				+ unit_head_date + ", legal_due_date=" + legal_due_date + ", exec_id=" + exec_id + ", eval_id="
				+ eval_id + ", email_status=" + email_status + ", email_sent_at=" + email_sent_at + ", fh_id=" + fh_id
				+ "]";
	}

	
}
