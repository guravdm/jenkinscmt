package lexprd006.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "mst_querterlycertificate")
public class QuerterlyGeneratedCertificate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int Querterly_certificate_id;
	private int Querterly_certificate_added_by;
	private String Querterly_certificate_certificatePath;
	private String Querterly_certificate_orignalName;
	private String Querterly_certificate_querter;
	private int Querterly_certificate_year;
	private Date Querterly_certificate_created_at;
	private Date Querterly_certificate_from;
	private Date Querterly_certificate_to;

	public Date getQuerterly_certificate_from() {
		return Querterly_certificate_from;
	}

	public void setQuerterly_certificate_from(Date querterly_certificate_from) {
		Querterly_certificate_from = querterly_certificate_from;
	}

	public Date getQuerterly_certificate_to() {
		return Querterly_certificate_to;
	}

	public void setQuerterly_certificate_to(Date querterly_certificate_to) {
		Querterly_certificate_to = querterly_certificate_to;
	}

	public QuerterlyGeneratedCertificate() {
	}

	public String getQuerterly_certificate_querter() {
		return Querterly_certificate_querter;
	}

	public void setQuerterly_certificate_querter(String querterly_certificate_querter) {
		Querterly_certificate_querter = querterly_certificate_querter;
	}

	public String getQuerterly_certificate_orignalName() {
		return Querterly_certificate_orignalName;
	}

	public void setQuerterly_certificate_orignalName(String Querterly_certificate_orignalName) {
		this.Querterly_certificate_orignalName = Querterly_certificate_orignalName;
	}

	public int getQuerterly_certificate_id() {
		return Querterly_certificate_id;
	}

	public void setQuerterly_certificate_id(int Querterly_certificate_id) {
		this.Querterly_certificate_id = Querterly_certificate_id;
	}

	public int getQuerterly_certificate_added_by() {
		return Querterly_certificate_added_by;
	}

	public void setQuerterly_certificate_added_by(int Querterly_certificate_added_by) {
		this.Querterly_certificate_added_by = Querterly_certificate_added_by;
	}

	public String getQuerterly_certificate_certificatePath() {
		return Querterly_certificate_certificatePath;
	}

	public void setQuerterly_certificate_certificatePath(String Querterly_certificate_certificatePath) {
		this.Querterly_certificate_certificatePath = Querterly_certificate_certificatePath;
	}

	public int getQuerterly_certificate_year() {
		return Querterly_certificate_year;
	}

	public void setQuerterly_certificate_year(int Querterly_certificate_year) {
		this.Querterly_certificate_year = Querterly_certificate_year;
	}

	public Date getQuerterly_certificate_created_at() {
		return Querterly_certificate_created_at;
	}

	public void setQuerterly_certificate_created_at(Date Querterly_certificate_created_at) {
		this.Querterly_certificate_created_at = Querterly_certificate_created_at;
	}

}
