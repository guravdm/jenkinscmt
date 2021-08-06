package lexprd006.service;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

public interface UsersService {
	public String listUsers();
	public String saveUser(String jsonString);
	public String editUser(String jsonString);
	public String updateUser(String jsonString);
	public String deleteUser(String jsonString);
	public String isUserNameExist(String jsonString);
	public String authencticateUser(String jsonString , HttpSession session);
	public String forgotPassword(String jsonString);
	public String changePassword(String jsonString , HttpSession session);
	public String userlogout(String jsonString , HttpSession session);
	public String getUserById(String jsonString , HttpSession session);
	public String enableDisableUser(String jsonString , HttpSession session);
	public String downloaduserlist(String jsonString , HttpSession session);
	public String sendcredentialmail(String jsonString);
	public String addUpdateUserList(MultipartFile ttrn_proof_of_compliance, String jsonString, HttpSession session);
	public void logOutEntry(int userId);
	

}
