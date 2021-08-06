package lexprd006.dao;

import java.util.List;

import lexprd006.domain.UploadedDocuments;
import lexprd006.domain.UploadedSubTaskDocuments;

public interface UploadedDocumentsDao {

	public void saveDocuments(Object obj);
	public int getLastGeneratedValueByTtrnId(int ttrn_id);
	public List<UploadedDocuments> getAllDocumentByTtrnId(int ttrn_id);
	public String getProofFilePath(int udoc_id);
	public void deleteDocuments(int udoc_ttrn_id);
	public void deleteDocument(int udoc_id);
	public int getLastGeneratedValueByTtrnSubId(int ttrn_sub_id);
	public List<UploadedSubTaskDocuments> getAllDocumentBySubTtrnId(int ttrn_sub_id);
	public UploadedDocuments getDocById(int udoc_id);
	public <T>List<T> getDocumentRepository(int user_id, int user_role_id);
	public UploadedDocuments getDocumentById(int ttrn_id);
	public void updateDocuments(UploadedDocuments documents);

}
