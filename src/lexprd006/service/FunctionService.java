package lexprd006.service;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

/*
 * Author: Mahesh Kharote
 * Date: 25/10/2016
 * Purpose: Service Interface for Functions
 * 
 * 
 * 
 * */
public interface FunctionService {

	public String listFunctions();
	public String saveFunction(String jsonString);
	public String editFunction(String jsonString);
	public String updateFunction(String jsonString);
	public String deleteFunction(String jsonString);
	public String isFunctionNameExist(String jsonString);
	public String getMappedFunctionsForEntityMapping(String jsonString);
	public String importFunctionList(MultipartFile dept_update_list, String jsonString, HttpSession session);
}
