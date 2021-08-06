package lexprd006.service;


import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public interface CertificateService {
//	public String getQuerterlyCertificateDetails(String json, HttpSession session);
//
//	public String downloadCertificateDetails(HttpSession session);
//
//	public String downloadCertificate(String certificate, HttpSession session, HttpServletResponse response);
//
//	public String downloadDocuments(String jsonString, HttpServletResponse response) throws Throwable;

/*	public String getMonthlyCertificateDetails(String json, HttpSession session);

	public String getQuerterlyCertificateDetails(String json, HttpSession session);
*/
	public String downloadCertificateDetails(HttpSession session);

	public String getQuerterlyCertificateDetails(String json, HttpSession session);

	public String downloadCertificate(String certificate, HttpSession session, HttpServletResponse response);

	public String downloadDocuments(String jsonString, HttpServletResponse response)throws Throwable;

}
