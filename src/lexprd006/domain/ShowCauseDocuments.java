package lexprd006.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity(name="trn_show_cause_notice_documents")
@Table(name = "trn_show_cause_notice_documents")
// , indexes = @Index(name="idx_scnd_id",columnList = "scnd_id")
public class ShowCauseDocuments {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int scnd_id;
	private int scnd_related_id;
	private String scnd_related_type;
	private String scnd_generated_file_path;
	private int scnd_last_generated_value_for_filename_for_related_id;
	private String scnd_original_file_name;
	private Date scnd_created_at;
	private int scnd_added_by;
	public int getScnd_id() {
		return scnd_id;
	}
	public void setScnd_id(int scnd_id) {
		this.scnd_id = scnd_id;
	}
	public int getScnd_related_id() {
		return scnd_related_id;
	}
	public void setScnd_related_id(int scnd_related_id) {
		this.scnd_related_id = scnd_related_id;
	}
	public String getScnd_related_type() {
		return scnd_related_type;
	}
	public void setScnd_related_type(String scnd_related_type) {
		this.scnd_related_type = scnd_related_type;
	}
	public String getScnd_generated_file_path() {
		return scnd_generated_file_path;
	}
	public void setScnd_generated_file_path(String scnd_generated_file_path) {
		this.scnd_generated_file_path = scnd_generated_file_path;
	}
	public int getScnd_last_generated_value_for_filename_for_related_id() {
		return scnd_last_generated_value_for_filename_for_related_id;
	}
	public void setScnd_last_generated_value_for_filename_for_related_id(
			int scnd_last_generated_value_for_filename_for_related_id) {
		this.scnd_last_generated_value_for_filename_for_related_id = scnd_last_generated_value_for_filename_for_related_id;
	}
	public String getScnd_original_file_name() {
		return scnd_original_file_name;
	}
	public void setScnd_original_file_name(String scnd_original_file_name) {
		this.scnd_original_file_name = scnd_original_file_name;
	}
	public Date getScnd_created_at() {
		return scnd_created_at;
	}
	public void setScnd_created_at(Date scnd_created_at) {
		this.scnd_created_at = scnd_created_at;
	}
	public int getScnd_added_by() {
		return scnd_added_by;
	}
	public void setScnd_added_by(int scnd_added_by) {
		this.scnd_added_by = scnd_added_by;
	}
	
	
}
