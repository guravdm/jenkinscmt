package lexprd006.service;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

public interface UnitService {

	public String listUnits(String requestFromMethod);
	public String saveUnit(String jsonString);
	public String editUnit(String jsonString);
	public String updateUnit(String jsonString);
	public String deleteUnit(String jsonString);
	public String isUnitNameExist(String jsonString);
	public String importUnitList(MultipartFile unit_update_list, String jsonString, HttpSession session);
	
}
