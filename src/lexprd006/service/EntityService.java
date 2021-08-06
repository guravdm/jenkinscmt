package lexprd006.service;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

/*
 * Author: Mahesh Kharote
 * Date: 25/10/2016
 * Purpose: Service Interface for Entities
 * 
 * 
 * 
 * */

public interface EntityService {
	public String listEntities(HttpSession session, String requestFromMethod);
	public String listAllForAddingEntity();
	public String saveEntity(String jsonString);
	public String editEntity(String jsonString);
	public String updateEntity(String jsonString);
	public String deleteEntity(String jsonString);
	public String isEntityNameExist(String jsonString);
	public String importEntityList(MultipartFile entity_update_list, String jsonString, HttpSession session);
}
