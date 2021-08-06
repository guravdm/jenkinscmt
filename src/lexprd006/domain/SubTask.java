package lexprd006.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity(name = "mst_sub_task")
@Table(name = "mst_sub_task")
// , indexes =@Index(name = "sub_id",columnList = "sub_id")
public class SubTask {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int sub_id;
	@Size(max = 100)
	private String sub_task_id;
	@Size(max = 1000)
	private String sub_client_task_id;
	
	private String sub_equipment_type;
	@Size(max = 100)
	private String sub_equipment_number;
	@Size(max = 5000)
	private String sub_equipment_description;
	@Size(max = 1000)
	private String sub_equipment_location;
	private String sub_last_examination_date;
	@Size(max = 100)
	private String sub_frequency;
	
	private int sub_last_generated_id;
	
	
	public int getSub_id() {
		return sub_id;
	}
	public void setSub_id(int sub_id) {
		this.sub_id = sub_id;
	}
	public int getSub_last_generated_id() {
		return sub_last_generated_id;
	}
	public void setSub_last_generated_id(int sub_last_generated_id) {
		this.sub_last_generated_id = sub_last_generated_id;
	}
	public String getSub_task_id() {
		return sub_task_id;
	}
	public void setSub_task_id(String sub_task_id) {
		this.sub_task_id = sub_task_id;
	}
	public String getSub_client_task_id() {
		return sub_client_task_id;
	}
	public void setSub_client_task_id(String sub_client_task_id) {
		this.sub_client_task_id = sub_client_task_id;
	}
	public String getSub_equipment_type() {
		return sub_equipment_type;
	}
	public void setSub_equipment_type(String sub_equipment_type) {
		this.sub_equipment_type = sub_equipment_type;
	}
	public String getSub_equipment_number() {
		return sub_equipment_number;
	}
	public void setSub_equipment_number(String sub_equipment_number) {
		this.sub_equipment_number = sub_equipment_number;
	}
	public String getSub_equipment_description() {
		return sub_equipment_description;
	}
	public void setSub_equipment_description(String sub_equipment_description) {
		this.sub_equipment_description = sub_equipment_description;
	}
	public String getSub_equipment_location() {
		return sub_equipment_location;
	}
	public void setSub_equipment_location(String sub_equipment_location) {
		this.sub_equipment_location = sub_equipment_location;
	}
	public String getSub_last_examination_date() {
		return sub_last_examination_date;
	}
	public void setSub_last_examination_date(String sub_last_examination_date) {
		this.sub_last_examination_date = sub_last_examination_date;
	}
	public String getSub_frequency() {
		return sub_frequency;
	}
	public void setSub_frequency(String sub_frequency) {
		this.sub_frequency = sub_frequency;
	}
	
}
