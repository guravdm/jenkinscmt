package lexprd006.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "upload_pod_docs")
public class UploadedPODDocuments {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "DOC_NAME")
	private String docName;

	@Column(name = "DOC_PATH")
	private String docPath;

	@Column(name = "DOC_ORIGINAL_NAME")
	private String docOriginalName;

	@Column(name = "DOC_DESCRIPTION")
	private String docDescription;

	@Column(name = "REPORT_FROM_DATE")
	private Date docReportFromDate;

	@Column(name = "REPORT_TO_DATE")
	private Date docReportToDate;

	@Column(name = "ADDED_BY")
	private int addedBy = 0;

	@Column(name = "IS_DELETED")
	private int isDeleted = 0;

	@Column(name = "DOWNLOAD_STATUS")
	private int isDownload = 0;

	@Column(name = "TASK_ID")
	private int tskId = 0;

	@Column(name = "STATE_ID")
	private int stateId = 0;

	public UploadedPODDocuments() {
	}

	@Override
	public String toString() {
		return "UploadedPODDocuments [id=" + id + ", docName=" + docName + ", docPath=" + docPath + ", docOriginalName="
				+ docOriginalName + ", docDescription=" + docDescription + ", docReportFromDate=" + docReportFromDate
				+ ", docReportToDate=" + docReportToDate + ", addedBy=" + addedBy + ", isDeleted=" + isDeleted
				+ ", isDownload=" + isDownload + ", tskId=" + tskId + ", stateId=" + stateId + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getDocPath() {
		return docPath;
	}

	public void setDocPath(String docPath) {
		this.docPath = docPath;
	}

	public String getDocOriginalName() {
		return docOriginalName;
	}

	public void setDocOriginalName(String docOriginalName) {
		this.docOriginalName = docOriginalName;
	}

	public String getDocDescription() {
		return docDescription;
	}

	public void setDocDescription(String docDescription) {
		this.docDescription = docDescription;
	}

	public Date getDocReportFromDate() {
		return docReportFromDate;
	}

	public void setDocReportFromDate(Date docReportFromDate) {
		this.docReportFromDate = docReportFromDate;
	}

	public Date getDocReportToDate() {
		return docReportToDate;
	}

	public void setDocReportToDate(Date docReportToDate) {
		this.docReportToDate = docReportToDate;
	}

	public int getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(int addedBy) {
		this.addedBy = addedBy;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getIsDownload() {
		return isDownload;
	}

	public void setIsDownload(int isDownload) {
		this.isDownload = isDownload;
	}

	public int getTskId() {
		return tskId;
	}

	public void setTskId(int tskId) {
		this.tskId = tskId;
	}

	public int getStateId() {
		return stateId;
	}

	public void setStateId(int stateId) {
		this.stateId = stateId;
	}

}
