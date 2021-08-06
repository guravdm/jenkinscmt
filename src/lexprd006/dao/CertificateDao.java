package lexprd006.dao;

import java.util.List;

import javax.servlet.http.HttpSession;

import lexprd006.domain.QuerterlyGeneratedCertificate;

public interface CertificateDao {
	public <T>List<T> getQuerterlyCertificateDetails(String json,HttpSession session);
	public <T>List<T> getQuerterlyShowCauseDetails(String json,HttpSession session);
	public List<Object> getQrtrLegRecords(String json, HttpSession session);
	public void saveQuerterlyCertificate(QuerterlyGeneratedCertificate sc);
	public List<Object> downloadCertificate(HttpSession session);
	public String getCertificatePath(String certificate);

}
