package lexprd006.domain;

import java.math.BigDecimal;
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
 * Created Date : 02/01/2017
 * 
 *  
 * */

@Entity(name = "mst_task")
@Table(name = "mst_task")
// , indexes = @Index(name = "idx_task_id_name_legi_id", columnList = "task_id,task_legi_name,task_legi_id")
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int task_id;
	@Size(max = 5000)
	private String task_activity;
	@Size(max = 5000)
	private String task_activity_who;
	@Size(max = 5000)
	private String task_activity_when;
	private int task_cat_law_id;
	private String task_cat_law_name;
	private int task_country_id;
	private String task_country_name;
	private Date task_legal_due_date;
	@Size(max = 500)
	private String task_event;
	@Size(max = 5000)
	private String task_excemption_criteria;
	private BigDecimal task_fine_amount;
	private String task_form_no;
	private String task_frequency;
	private String task_impact;
	private String task_impact_on_organization;
	private String task_impact_on_unit;
	@Size(max = 2500)
	private String task_implication;
	private String task_imprisonment_duration;
	private String task_imprisonment_implies_to;
	@Size(max = 2500)
	private String task_interlinkage;
	private int task_legi_id;
	@Size(max = 1000)
	private String task_legi_name;
	private String task_level;
	private String task_lexcare_task_id;
	private String task_linked_task_id;
	@Size(max = 5000)
	private String task_more_info;
	@Size(max = 5000)
	private String task_procedure;
	private String task_prohibitive;
	private String task_document_reference;
	private String task_form_format;//It will contain boolean value whether a form is to be attached or not
	private String task_reference;
	private int task_rule_id;
	@Size(max = 2500)
	private String task_rule_name;
	private int task_sequence;
	private String due_date;
	private String task_specific_due_date;
	private int task_state_id;
	private String task_state_name;
	private String task_sub_event;
	private BigDecimal task_subsequent_amount_per_day;
	private String task_task_type_of_task;
	@Size(max = 2500)
	private String task_subtask;
	private Date task_effective_date;
	@Size(max = 500)
	private String task_weblinks;
	private String task_approval_status;
	private int task_added_by;
	private Date task_created_at;
	private String task_enable_status;
	@Size(max = 2500)
	private String task_statutory_authority;

	public String getTask_statutory_authority() {
		return task_statutory_authority;
	}
	public void setTask_statutory_authority(String task_statutory_authority) {
		this.task_statutory_authority = task_statutory_authority;
	}
	
	public String getTask_specific_due_date() {
		return task_specific_due_date;
	}
	public void setTask_specific_due_date(String task_specific_due_date) {
		this.task_specific_due_date = task_specific_due_date;
	}
	
	public String getDue_date() {
		return due_date;
	}
	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}
	
	public int getTask_id() {
		return task_id;
	}
	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}
	public String getTask_activity() {
		return task_activity;
	}
	public void setTask_activity(String task_activity) {
		this.task_activity = task_activity;
	}
	public String getTask_activity_who() {
		return task_activity_who;
	}
	public void setTask_activity_who(String task_activity_who) {
		this.task_activity_who = task_activity_who;
	}
	public String getTask_activity_when() {
		return task_activity_when;
	}
	public void setTask_activity_when(String task_activity_when) {
		this.task_activity_when = task_activity_when;
	}
	public int getTask_cat_law_id() {
		return task_cat_law_id;
	}
	public void setTask_cat_law_id(int task_cat_law_id) {
		this.task_cat_law_id = task_cat_law_id;
	}
	public String getTask_cat_law_name() {
		return task_cat_law_name;
	}
	public void setTask_cat_law_name(String task_cat_law_name) {
		this.task_cat_law_name = task_cat_law_name;
	}
	public int getTask_country_id() {
		return task_country_id;
	}
	public void setTask_country_id(int task_country_id) {
		this.task_country_id = task_country_id;
	}
	public String getTask_country_name() {
		return task_country_name;
	}
	public void setTask_country_name(String task_country_name) {
		this.task_country_name = task_country_name;
	}
	public Date getTask_legal_due_date() {
		return task_legal_due_date;
	}
	public void setTask_legal_due_date(Date task_legal_due_date) {
		this.task_legal_due_date = task_legal_due_date;
	}
	public String getTask_event() {
		return task_event;
	}
	public void setTask_event(String task_event) {
		this.task_event = task_event;
	}
	public String getTask_excemption_criteria() {
		return task_excemption_criteria;
	}
	public void setTask_excemption_criteria(String task_excemption_criteria) {
		this.task_excemption_criteria = task_excemption_criteria;
	}
	public BigDecimal getTask_fine_amount() {
		return task_fine_amount;
	}
	public void setTask_fine_amount(BigDecimal task_fine_amount) {
		this.task_fine_amount = task_fine_amount;
	}
	public String getTask_form_no() {
		return task_form_no;
	}
	public void setTask_form_no(String task_form_no) {
		this.task_form_no = task_form_no;
	}
	public String getTask_frequency() {
		return task_frequency;
	}
	public void setTask_frequency(String task_frequency) {
		this.task_frequency = task_frequency;
	}
	public String getTask_impact() {
		return task_impact;
	}
	public void setTask_impact(String task_impact) {
		this.task_impact = task_impact;
	}
	public String getTask_impact_on_organization() {
		return task_impact_on_organization;
	}
	public void setTask_impact_on_organization(String task_impact_on_organization) {
		this.task_impact_on_organization = task_impact_on_organization;
	}
	
	public String getTask_impact_on_unit() {
		return task_impact_on_unit;
	}
	public void setTask_impact_on_unit(String task_impact_on_unit) {
		this.task_impact_on_unit = task_impact_on_unit;
	}
	public String getTask_implication() {
		return task_implication;
	}
	public void setTask_implication(String task_implication) {
		this.task_implication = task_implication;
	}
	public String getTask_imprisonment_duration() {
		return task_imprisonment_duration;
	}
	public void setTask_imprisonment_duration(String task_imprisonment_duration) {
		this.task_imprisonment_duration = task_imprisonment_duration;
	}
	public String getTask_imprisonment_implies_to() {
		return task_imprisonment_implies_to;
	}
	public void setTask_imprisonment_implies_to(String task_imprisonment_implies_to) {
		this.task_imprisonment_implies_to = task_imprisonment_implies_to;
	}
	public String getTask_interlinkage() {
		return task_interlinkage;
	}
	public void setTask_interlinkage(String task_interlinkage) {
		this.task_interlinkage = task_interlinkage;
	}
	public int getTask_legi_id() {
		return task_legi_id;
	}
	public void setTask_legi_id(int task_legi_id) {
		this.task_legi_id = task_legi_id;
	}
	public String getTask_legi_name() {
		return task_legi_name;
	}
	public void setTask_legi_name(String task_legi_name) {
		this.task_legi_name = task_legi_name;
	}
	public String getTask_level() {
		return task_level;
	}
	public void setTask_level(String task_level) {
		this.task_level = task_level;
	}
	
	public String getTask_lexcare_task_id() {
		return task_lexcare_task_id;
	}
	public void setTask_lexcare_task_id(String task_lexcare_task_id) {
		this.task_lexcare_task_id = task_lexcare_task_id;
	}
	public String getTask_linked_task_id() {
		return task_linked_task_id;
	}
	public void setTask_linked_task_id(String task_linked_task_id) {
		this.task_linked_task_id = task_linked_task_id;
	}
	public String getTask_more_info() {
		return task_more_info;
	}
	public void setTask_more_info(String task_more_info) {
		this.task_more_info = task_more_info;
	}
	public String getTask_procedure() {
		return task_procedure;
	}
	public void setTask_procedure(String task_procedure) {
		this.task_procedure = task_procedure;
	}
	public String getTask_prohibitive() {
		return task_prohibitive;
	}
	public void setTask_prohibitive(String task_prohibitive) {
		this.task_prohibitive = task_prohibitive;
	}
	
	public String getTask_document_reference() {
		return task_document_reference;
	}
	public void setTask_document_reference(String task_document_reference) {
		this.task_document_reference = task_document_reference;
	}
	public String getTask_form_format() {
		return task_form_format;
	}
	public void setTask_form_format(String task_form_format) {
		this.task_form_format = task_form_format;
	}
	public String getTask_reference() {
		return task_reference;
	}
	public void setTask_reference(String task_reference) {
		this.task_reference = task_reference;
	}
	public int getTask_rule_id() {
		return task_rule_id;
	}
	public void setTask_rule_id(int task_rule_id) {
		this.task_rule_id = task_rule_id;
	}
	public String getTask_rule_name() {
		return task_rule_name;
	}
	public void setTask_rule_name(String task_rule_name) {
		this.task_rule_name = task_rule_name;
	}
	public int getTask_sequence() {
		return task_sequence;
	}
	public void setTask_sequence(int task_sequence) {
		this.task_sequence = task_sequence;
	}
	
	public int getTask_state_id() {
		return task_state_id;
	}
	public void setTask_state_id(int task_state_id) {
		this.task_state_id = task_state_id;
	}
	public String getTask_state_name() {
		return task_state_name;
	}
	public void setTask_state_name(String task_state_name) {
		this.task_state_name = task_state_name;
	}
	public String getTask_sub_event() {
		return task_sub_event;
	}
	public void setTask_sub_event(String task_sub_event) {
		this.task_sub_event = task_sub_event;
	}
	public BigDecimal getTask_subsequent_amount_per_day() {
		return task_subsequent_amount_per_day;
	}
	public void setTask_subsequent_amount_per_day(BigDecimal task_subsequent_amount_per_day) {
		this.task_subsequent_amount_per_day = task_subsequent_amount_per_day;
	}
	public String getTask_task_type_of_task() {
		return task_task_type_of_task;
	}
	public void setTask_task_type_of_task(String task_task_type_of_task) {
		this.task_task_type_of_task = task_task_type_of_task;
	}
	public String getTask_subtask() {
		return task_subtask;
	}
	public void setTask_subtask(String task_subtask) {
		this.task_subtask = task_subtask;
	}
	public Date getTask_effective_date() {
		return task_effective_date;
	}
	public void setTask_effective_date(Date task_effective_date) {
		this.task_effective_date = task_effective_date;
	}
	public String getTask_weblinks() {
		return task_weblinks;
	}
	public void setTask_weblinks(String task_weblinks) {
		this.task_weblinks = task_weblinks;
	}
	public String getTask_approval_status() {
		return task_approval_status;
	}
	public void setTask_approval_status(String task_approval_status) {
		this.task_approval_status = task_approval_status;
	}
	public int getTask_added_by() {
		return task_added_by;
	}
	public void setTask_added_by(int task_added_by) {
		this.task_added_by = task_added_by;
	}
	public Date getTask_created_at() {
		return task_created_at;
	}
	public void setTask_created_at(Date task_created_at) {
		this.task_created_at = task_created_at;
	}
	public String getTask_enable_status() {
		return task_enable_status;
	}
	public void setTask_enable_status(String task_enable_status) {
		this.task_enable_status = task_enable_status;
	}		
}
