package lexprd006.dao;

import lexprd006.domain.Task;

public interface LegalUpdateDao {

	String uploadlegalUpdates(Task task);

	int getLexcareTaskExist(String lexcareId);

	String update_legalUpdates(Task task);

}
