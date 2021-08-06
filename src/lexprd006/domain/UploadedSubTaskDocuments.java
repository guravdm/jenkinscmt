package lexprd006.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "trn_sub_task_uploadedDocuments")
public class UploadedSubTaskDocuments {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int udoc_sub_task_id;
	private int udoc_sub_task_ttrn_id;
	private String udoc_sub_task_original_file_name;
	private String udoc_sub_task_filename;
	private int udoc_last_generated_value_for_filename_for_sub_task_ttrn_id;
	private int download_status;
	
	public int getDownload_status() {
		return download_status;
	}
	public void setDownload_status(int download_status) {
		this.download_status = download_status;
	}
	public int getUdoc_sub_task_id() {
		return udoc_sub_task_id;
	}
	public void setUdoc_sub_task_id(int udoc_sub_task_id) {
		this.udoc_sub_task_id = udoc_sub_task_id;
	}
	public int getUdoc_sub_task_ttrn_id() {
		return udoc_sub_task_ttrn_id;
	}
	public void setUdoc_sub_task_ttrn_id(int udoc_sub_task_ttrn_id) {
		this.udoc_sub_task_ttrn_id = udoc_sub_task_ttrn_id;
	}
	public String getUdoc_sub_task_original_file_name() {
		return udoc_sub_task_original_file_name;
	}
	public void setUdoc_sub_task_original_file_name(String udoc_sub_task_original_file_name) {
		this.udoc_sub_task_original_file_name = udoc_sub_task_original_file_name;
	}
	public String getUdoc_sub_task_filename() {
		return udoc_sub_task_filename;
	}
	public void setUdoc_sub_task_filename(String udoc_sub_task_filename) {
		this.udoc_sub_task_filename = udoc_sub_task_filename;
	}
	public int getUdoc_last_generated_value_for_filename_for_sub_task_ttrn_id() {
		return udoc_last_generated_value_for_filename_for_sub_task_ttrn_id;
	}
	public void setUdoc_last_generated_value_for_filename_for_sub_task_ttrn_id(
			int udoc_last_generated_value_for_filename_for_sub_task_ttrn_id) {
		this.udoc_last_generated_value_for_filename_for_sub_task_ttrn_id = udoc_last_generated_value_for_filename_for_sub_task_ttrn_id;
	} 
	
	
	
}
