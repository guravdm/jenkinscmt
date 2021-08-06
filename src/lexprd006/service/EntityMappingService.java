package lexprd006.service;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

/*
 * Author: Mahesh Kharote
 * Date: 19/12/2016
 * Purpose: Service Interface for Entity Mapping
 * 
 * 
 * 
 * */

public interface EntityMappingService {

	public String listEntityMappings();
	public String saveEntityMapping(String jsonString);
	public String editEntityMapping(String jsonString);
	public String updateEntityMapping(String jsonString);
	public String deleteEntityMapping(String jsonString);
	public String getAllMappings(String jsonString);
	public String getMappedUnitForEntity(String jsonString);
	public String getMappedDepartments(String jsonString);
	public String getAllMappingsReport(String jsonString, HttpSession session);
	public String importEntityMapping(MultipartFile entity_mapping_list, String jsonString, HttpSession session);
	
}
