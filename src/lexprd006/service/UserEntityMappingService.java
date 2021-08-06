package lexprd006.service;

import javax.servlet.http.HttpSession;

public interface UserEntityMappingService {

	public String saveUserEntityMapping(String jsonString , HttpSession session);
	public String getUserEntityMappingUserWise(String jsonString , HttpSession session);
	public String removeUserAccess(String json,HttpSession session);
	public String getUserMappingList(String json,HttpSession session);
	public String getUserWithAccessForCommonEmail(String json,HttpSession session);
}
