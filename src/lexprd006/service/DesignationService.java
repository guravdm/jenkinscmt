package lexprd006.service;

public interface DesignationService {

	public String listDesignations();

	public String saveDesignation(String jsonString);

	public String editDesignation(String jsonString);

	public String updateDesignation(String jsonString);

	public String deleteDesignation(String jsonString);

	public String isDesignationNameExist(String jsonString);
}
