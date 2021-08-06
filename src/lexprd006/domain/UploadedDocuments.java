package lexprd006.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/*
 * Author : Mahesh Kharote
 * Created Date : 18/04/2016
 * Updated By : 
 * Updated Date : 
 *  
 * */


@Entity(name = "trn_uploadedDocuments")
public class UploadedDocuments {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int udoc_id;
	private int udoc_ttrn_id;
	private String udoc_original_file_name;
	private String udoc_filename;
	private int udoc_last_generated_value_for_filename_for_ttrn_id;
	private int download_status;
	
	
	public int getDownload_status() {
		return download_status;
	}
	public void setDownload_status(int download_status) {
		this.download_status = download_status;
	}
	public int getUdoc_id() {
		return udoc_id;
	}
	public void setUdoc_id(int udoc_id) {
		this.udoc_id = udoc_id;
	}
	public int getUdoc_ttrn_id() {
		return udoc_ttrn_id;
	}
	public void setUdoc_ttrn_id(int udoc_ttrn_id) {
		this.udoc_ttrn_id = udoc_ttrn_id;
	}
	public String getUdoc_original_file_name() {
		return udoc_original_file_name;
	}
	public void setUdoc_original_file_name(String udoc_original_file_name) {
		this.udoc_original_file_name = udoc_original_file_name;
	}
	public String getUdoc_filename() {
		return udoc_filename;
	}
	public void setUdoc_filename(String udoc_filename) {
		this.udoc_filename = udoc_filename;
	}
	public int getUdoc_last_generated_value_for_filename_for_ttrn_id() {
		return udoc_last_generated_value_for_filename_for_ttrn_id;
	}
	public void setUdoc_last_generated_value_for_filename_for_ttrn_id(
			int udoc_last_generated_value_for_filename_for_ttrn_id) {
		this.udoc_last_generated_value_for_filename_for_ttrn_id = udoc_last_generated_value_for_filename_for_ttrn_id;
	}
	
	
	
	
}
