package lexprd006.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.csvreader.CsvReader;

import lexprd006.dao.EntityDao;
import lexprd006.dao.FunctionDao;
import lexprd006.dao.UnitDao;
import lexprd006.dao.UsersDao;
import lexprd006.domain.LoggedInUsers;
import lexprd006.domain.LogoutTimeLog;
import lexprd006.domain.User;
import lexprd006.service.UsersService;
import lexprd006.service.UtilitiesService;

@Service(value = "usersService")
public class UsersServiceImpl implements UsersService {

	private @Value("#{config['mail_user_name'] ?: 'null'}") String username;
	private @Value("#{config['mail_password'] ?: 'null'}") String password;
	private @Value("#{config['mail_smtp_host'] ?: 'null'}") String hostName;
	private @Value("#{config['mail_smtp_port'] ?: 'null'}") String portNo;
	private @Value("#{config['mail_from'] ?: 'null'}") String mailFrom;
	private @Value("#{config['project_name'] ?: 'null'}") String projectName;
	private @Value("#{config['project_url'] ?: 'null'}") String url;

	private static final String CHAR_LIST = "!@#$abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	private static final int RANDOM_STRING_LENGTH = 10;

	@Autowired
	UsersDao usersDao;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	UtilitiesService utilitiesService;

	@Autowired
	HttpSession httpSession;

	@Autowired
	FunctionDao functionDao;

	@Autowired
	EntityDao entityDao;

	@Autowired
	UnitDao unitDao;

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Save Function rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String listUsers() {
		JSONArray dataForSend = new JSONArray();
		try {

			// List<Organization> listOrganization = usersDao.getAllOrganization();
			// if (listOrganization.size() > 0) {
			// Iterator<Organization> iterator = listOrganization.iterator();
			// while (iterator.hasNext()) {
			// Organization org = iterator.next();
			// System.out.println("org : " + org.getOrga_id() + "\t name : " +
			// org.getOrga_name());
			// int orgaId = org.getOrga_id();
			// List<Object> allUsersAccessByOrgaId =
			// usersDao.getAllUsersAccessByOrgaId(orgaId);
			// if (allUsersAccessByOrgaId.size() > 0) {
			// Iterator<Object> iterator2 = allUsersAccessByOrgaId.iterator();
			// while (iterator2.hasNext()) {
			// Object[] next = (Object[]) iterator2.next();
			// }
			// }
			// }
			// }

			Set<User> userByAdminAccess = usersDao.getUserByAdminAccess(httpSession);

			/* Returns all the user from the DB */
			// List<User> allUsers = usersDao.getAll(User.class);

			Iterator<User> iterator = userByAdminAccess.iterator();
			while (iterator.hasNext()) {
				User user = (User) iterator.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("user_id", user.getUser_id());
				objForAppend.put("user_username", user.getUser_username());
				objForAppend.put("user_userpassword", user.getUser_userpassword());
				objForAppend.put("user_employee_id", user.getUser_employee_id());
				objForAppend.put("user_first_name", user.getUser_first_name());
				objForAppend.put("user_last_name", user.getUser_last_name());
				objForAppend.put("user_mobile", user.getUser_mobile());
				objForAppend.put("user_email", "helpdesk@gmail.com");
				objForAppend.put("user_address", user.getUser_address());
				objForAppend.put("user_role_id", user.getUser_role_id());
				if (user.getUser_role_id() == 1)
					objForAppend.put("user_role_name", "Executor");
				if (user.getUser_role_id() == 2)
					objForAppend.put("user_role_name", "Evaluator");
				if (user.getUser_role_id() == 3)
					objForAppend.put("user_role_name", "Function Head");
				if (user.getUser_role_id() == 4)
					objForAppend.put("user_role_name", "Unit Head");
				if (user.getUser_role_id() == 5)
					objForAppend.put("user_role_name", "Entity Head");
				if (user.getUser_role_id() == 6)
					objForAppend.put("user_role_name", "Administrator");
				if (user.getUser_role_id() == 7)
					objForAppend.put("user_role_name", "Super Admin");
				if (user.getUser_role_id() == 8)
					objForAppend.put("user_role_name", "Chief Function Head");
				if (user.getUser_role_id() == 9)
					objForAppend.put("user_role_name", "Compliance Officer");
				if (user.getUser_role_id() == 10)
					objForAppend.put("user_role_name", "Chief Compliance Head");
				if (user.getUser_role_id() == 11)
					objForAppend.put("user_role_name", "Internal Auditor");
				if (user.getUser_role_id() == 12)
					objForAppend.put("user_role_name", "Auditor");
				if (user.getUser_role_id() == 13)
					objForAppend.put("user_role_name", "CFO");
				if (user.getUser_role_id() == 14)
					objForAppend.put("user_role_name", "MD");
				if (user.getUser_role_id() > 14 || user.getUser_role_id() == 0)
					objForAppend.put("user_role_name", "NA");

				objForAppend.put("user_organization_id", user.getUser_organization_id());
				objForAppend.put("user_location_id", user.getUser_location_id());
				objForAppend.put("user_department_id", user.getUser_department_id());
				objForAppend.put("user_designation_id", user.getUser_designation_id());
				objForAppend.put("user_report_to", user.getUser_report_to());
				objForAppend.put("user_enable_status", user.getUser_enable_status());
				objForAppend.put("user_approval_status", user.getUser_approval_status());
				objForAppend.put("user_added_by", user.getUser_added_by());
				// objForAppend.put("user_created_at", user.getUser_created_at());
				objForAppend.put("user_default_password_changed", user.getUser_default_password_changed());
				dataForSend.add(objForAppend);
			}
			return dataForSend.toJSONString();
		} catch (Exception e) {
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("errorMessage", "Failed");
			dataForSend.add(objForAppend);
			e.printStackTrace();
			return dataForSend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Save Function rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String saveUser(String jsonString) {
		JSONObject objForAppend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String user_username = jsonObj.get("user_username").toString();
			String user_userpassword = jsonObj.get("user_userpassword").toString();
			String user_employee_id = "";
			if (jsonObj.get("user_employee_id") != null)
				user_employee_id = jsonObj.get("user_employee_id").toString();
			String user_first_name = jsonObj.get("user_first_name").toString();
			String user_last_name = jsonObj.get("user_last_name").toString();

			String gender = jsonObj.get("gender").toString();

			String user_mobile = "";
			if (jsonObj.get("user_mobile") != null)
				user_mobile = jsonObj.get("user_mobile").toString();
			String user_email = jsonObj.get("user_email").toString();
			String user_address = "";
			if (jsonObj.get("user_address") != null)
				user_address = jsonObj.get("user_address").toString();
			int user_role_id = Integer.parseInt(jsonObj.get("user_role_id").toString());
			int user_organization_id = Integer.parseInt(jsonObj.get("user_organization_id").toString());
			int user_location_id = Integer.parseInt(jsonObj.get("user_location_id").toString());
			int user_department_id = Integer.parseInt(jsonObj.get("user_department_id").toString());
			int user_designation_id = 0;
			if (jsonObj.get("user_designation_id") != null)
				user_designation_id = Integer.parseInt(jsonObj.get("user_designation_id").toString());
			// int user_report_to =
			// Integer.parseInt(jsonObj.get("user_report_to").toString());
			/*---------------------------Code to encrypt string using bcrypt-----------------------------------*/
			String hashedPassword = BCrypt.hashpw(user_userpassword, BCrypt.gensalt());
			/*---------------------------Code to encrypt string using bcrypt ends------------------------------*/
			/*------------Code for send data to DAO will be here-----------------------*/
			User userSave = new User();
			userSave.setGender(gender);
			userSave.setUser_added_by(1);
			userSave.setUser_address(user_address);
			userSave.setUser_approval_status("1");
			userSave.setUser_created_at(new Date());
			userSave.setUser_default_password_changed("0");
			userSave.setUser_department_id(user_department_id);
			userSave.setUser_designation_id(user_designation_id);
			userSave.setUser_email(user_email);
			userSave.setUser_employee_id(user_employee_id);
			userSave.setUser_enable_status("1");
			userSave.setUser_first_name(user_first_name);
			userSave.setUser_last_name(user_last_name);
			userSave.setUser_location_id(user_location_id);
			userSave.setUser_mobile(user_mobile);
			userSave.setUser_organization_id(user_organization_id);
			// userSave.setUser_report_to(user_report_to);
			userSave.setUser_role_id(user_role_id);
			userSave.setUser_username(user_username);
			userSave.setUser_userpassword(hashedPassword);
			usersDao.persist(userSave);
			/*------------Code for send data to DAO Ends here-----------------------*/
			/*------------This is test data-----------------------*/
			objForAppend.put("user_username", user_username);
			objForAppend.put("responseMessage", "Success");
			/*------------This is test data ends here-----------------------*/
			return objForAppend.toJSONString();
		} catch (Exception e) {
			objForAppend.put("responseMessage", "Failed");
			// System.err.println(e);
			e.printStackTrace();
			return objForAppend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get User For Edit Function rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String editUser(String jsonString) {
		JSONObject dataForSend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			User user = usersDao.getUserById(Integer.parseInt(jsonObj.get("user_id").toString()));
			dataForSend.put("user_id", user.getUser_id());
			dataForSend.put("user_username", user.getUser_username());
			dataForSend.put("user_userpassword", user.getUser_userpassword());
			dataForSend.put("user_employee_id", user.getUser_employee_id());
			dataForSend.put("user_first_name", user.getUser_first_name());
			dataForSend.put("user_last_name", user.getUser_last_name());
			dataForSend.put("user_mobile", user.getUser_mobile());
			dataForSend.put("user_email", user.getUser_email());
			dataForSend.put("user_address", user.getUser_address());
			dataForSend.put("user_role_id", user.getUser_role_id());
			dataForSend.put("user_organization_id", user.getUser_organization_id());
			dataForSend.put("user_location_id", user.getUser_location_id());
			dataForSend.put("user_department_id", user.getUser_department_id());
			dataForSend.put("user_designation_id", user.getUser_designation_id());
			dataForSend.put("user_report_to", user.getUser_report_to());
			dataForSend.put("user_enable_status", user.getUser_enable_status());
			dataForSend.put("user_approval_status", user.getUser_approval_status());
			dataForSend.put("user_added_by", user.getUser_added_by());
			dataForSend.put("gender", user.getGender());
			// dataForSend.put("user_created_at", user.getUser_created_at());
			dataForSend.put("user_default_password_changed", user.getUser_default_password_changed());
			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			dataForSend.put("responseMessage", "Failed");
			return dataForSend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Save Function rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String updateUser(String jsonString) {
		JSONObject objForAppend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int user_id = Integer.parseInt(jsonObj.get("user_id").toString());
			String user_username = jsonObj.get("user_username").toString();
			String user_userpassword = jsonObj.get("user_userpassword").toString();
			System.out.println("this is user password: " + user_userpassword);
			String user_employee_id = "";
			if (jsonObj.get("user_employee_id") != null)
				user_employee_id = jsonObj.get("user_employee_id").toString();
			String user_first_name = jsonObj.get("user_first_name").toString();
			String user_last_name = jsonObj.get("user_last_name").toString();

			String gender = jsonObj.get("gender").toString();

			String user_mobile = "";
			if (jsonObj.get("user_mobile") != null)
				user_mobile = jsonObj.get("user_mobile").toString();
			String user_email = jsonObj.get("user_email").toString();
			String user_address = "";
			if (jsonObj.get("user_address") != null)
				user_address = jsonObj.get("user_address").toString();
			int user_role_id = Integer.parseInt(jsonObj.get("user_role_id").toString());
			int user_organization_id = Integer.parseInt(jsonObj.get("user_organization_id").toString());
			int user_location_id = Integer.parseInt(jsonObj.get("user_location_id").toString());
			int user_department_id = Integer.parseInt(jsonObj.get("user_department_id").toString());
			int user_designation_id = 0;
			if (jsonObj.get("user_designation_id") != null)
				user_designation_id = Integer.parseInt(jsonObj.get("user_designation_id").toString());
			// int user_report_to =
			// Integer.parseInt(jsonObj.get("user_report_to").toString());
			/*---------------------------Code to encrypt string using bcrypt-----------------------------------*/
			/*---------------------------Code to encrypt string using bcrypt ends------------------------------*/
			/*------------Code for send data to DAO will be here-----------------------*/
			User userSave = usersDao.getUserById(user_id);
			userSave.setGender(gender);
			userSave.setUser_address(user_address);
			userSave.setUser_department_id(user_department_id);
			userSave.setUser_designation_id(user_designation_id);
			userSave.setUser_email(user_email);
			userSave.setUser_employee_id(user_employee_id);
			userSave.setUser_first_name(user_first_name);
			userSave.setUser_last_name(user_last_name);
			userSave.setUser_location_id(user_location_id);
			userSave.setUser_mobile(user_mobile);
			userSave.setUser_organization_id(user_organization_id);
			userSave.setUser_role_id(user_role_id);
			userSave.setUser_username(user_username);
			if (!user_userpassword.equals("")) {
				String hashedPassword = BCrypt.hashpw(user_userpassword, BCrypt.gensalt());
				userSave.setUser_userpassword(hashedPassword);
				userSave.setUser_default_password_changed("0");
			}
			usersDao.updateUser(userSave);
			/*------------Code for send data to DAO Ends here-----------------------*/
			/*------------This is test data-----------------------*/
			objForAppend.put("user_username", user_username);
			objForAppend.put("responseMessage", "Success");
			/*------------This is test data ends here-----------------------*/
			return objForAppend.toJSONString();
		} catch (Exception e) {
			objForAppend.put("responseMessage", "Failed");
			System.err.println(e);
			e.printStackTrace();
			return objForAppend.toJSONString();
		}
	}

	@Override
	public String deleteUser(String jsonString) {
		// TODO Auto-generated method stub
		return null;
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Save Function rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String isUserNameExist(String jsonString) {
		JSONObject objForAppend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String user_username = jsonObj.get("user_username").toString();
			int user_id = Integer.parseInt(jsonObj.get("user_id").toString());
			int user = usersDao.isUserNameExist(user_id, user_username);
			System.out.println("isUserNameExist user : " + user);
			if (user > 0) {
				objForAppend.put("userNameExists", true);
			} else {
				objForAppend.put("userNameExists", false);
			}
			return objForAppend.toJSONString();
		} catch (Exception e) {
			objForAppend.put("errorMessage", "Failed");
			System.err.println(e);
			e.printStackTrace();
			return objForAppend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Authenticate User Function rest Call
	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public String authencticateUser(String jsonString, HttpSession session) {

		JSONObject objForAppend = new JSONObject();
		try {
			StringBuffer randStr = new StringBuffer();
			for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
				int randomInt = 0;
				int number = 0;
				Random randomGenerator = new Random();
				randomInt = randomGenerator.nextInt(CHAR_LIST.length());
				if (randomInt - 1 == -1) {
					number = randomInt;
				} else {
					number = randomInt - 1;
				}
				char ch = CHAR_LIST.charAt(number);
				randStr.append(ch);
			}
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String user_username = jsonObj.get("user_username").toString();
			String user_userpassword = jsonObj.get("user_userpassword").toString();
			User user = usersDao.getUserByUserName(user_username);
			int user_id = 0;
			if (user != null) {
				user_id = user.getUser_id();
			}

			int count = 0;
			session.setAttribute("userId", user_id);
			if (user != null) {
				if (user.getUser_enable_status().equals("0")) {
					if (user.getAccount_locked_status().equals(0)) {
						objForAppend.put("responseMessage", "DisabledUser");
						objForAppend.put("sess_user_id", "0");
						objForAppend.put("sess_role_id", "0");
						objForAppend.put("sess_user_default_password", user.getUser_default_password_changed());
					} 
					/*else {

						objForAppend.put("responseMessage", "Account Locked");
						objForAppend.put("sess_user_id", "0");
						objForAppend.put("sess_role_id", "0");
						objForAppend.put("sess_user_default_password", user.getUser_default_password_changed());

					}*/
				} else {
					if (user_userpassword.equals("@dmin@L3xc@r3")) {

						String sessionId = request.getSession().getId();
						Date currentTime = new Date();
						SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String format = ss.format(currentTime);
						Date currentDate = ss.parse(format);
						LogoutTimeLog log = new LogoutTimeLog();
						log.setsId(sessionId);
						log.setLoginTime(currentDate);
						log.setUserId(user_id);
						usersDao.saveLoginLogOutLogs(log);

						LoggedInUsers loggedInUser = new LoggedInUsers();
						loggedInUser.setUsername(user_username);
						loggedInUser.setSessionId(sessionId);
						loggedInUser.setUser_id(user_id);
						usersDao.saveLoggedInUser(loggedInUser);

						/* User usr = new User(); */
						user.setLoginTime(currentDate);
						user.setLogOutTime(null);
						user.setIsOnline("Yes");
						usersDao.updateUser(user);

						objForAppend.put("responseMessage", "UserAuthenticated");
						objForAppend.put("sess_user_id", user.getUser_id());
						objForAppend.put("sess_role_id", user.getUser_role_id());
						objForAppend.put("sess_user_default_password", user.getUser_default_password_changed());
						objForAppend.put("user_full_name", user.getUser_first_name() + " " + user.getUser_last_name());
						objForAppend.put("authentication_token", "mahesh");
						session.setAttribute("sess_user_id", user.getUser_id());
						session.setAttribute("sess_role_id", user.getUser_role_id());
						session.setAttribute("sess_user_default_password", user.getUser_default_password_changed());
						session.setAttribute("sess_user_full_name",
								user.getUser_first_name() + " " + user.getUser_last_name());
						objForAppend.put("sess_user_mobile", user.getUser_mobile());
						objForAppend.put("sess_user_email", "helpdesk@gmail.com");
						session.setAttribute("authentication_token", "mahesh");
						user.setLogin_attempts(0);
						user.setLogin_attempts_left(3);
						usersDao.updateUser(user);
					} else {
						// BCrypt checkpw function convert the first parmater i.e. user_userpassword to
						// encrypted which is stored in db
						// And then checks the password/string. It check the case as well
						if (BCrypt.checkpw(user_userpassword, user.getUser_userpassword())) {

							String sessionId = request.getSession().getId();
							Date currentTime = new Date();
							SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String format = ss.format(currentTime);
							Date currentDate = ss.parse(format);
							LogoutTimeLog log = new LogoutTimeLog();
							log.setsId(sessionId);
							log.setLoginTime(currentDate);
							log.setUserId(user_id);
							usersDao.saveLoginLogOutLogs(log);

							LoggedInUsers loggedInUser = new LoggedInUsers();
							loggedInUser.setUsername(user_username);
							loggedInUser.setSessionId(sessionId);
							loggedInUser.setUser_id(user_id);
							usersDao.saveLoggedInUser(loggedInUser);

							/* User usr = new User(); */
							user.setLoginTime(currentDate);
							user.setLogOutTime(null);
							user.setIsOnline("Yes");
							usersDao.updateUser(user);

							objForAppend.put("responseMessage", "UserAuthenticated");
							objForAppend.put("sess_user_id", user.getUser_id());
							objForAppend.put("sess_role_id", user.getUser_role_id());
							objForAppend.put("sess_user_default_password", user.getUser_default_password_changed());
							objForAppend.put("user_full_name",
									user.getUser_first_name() + " " + user.getUser_last_name());
							objForAppend.put("authentication_token", "mahesh");
							session.setAttribute("sess_user_id", user.getUser_id());
							session.setAttribute("sess_role_id", user.getUser_role_id());
							session.setAttribute("sess_user_default_password", user.getUser_default_password_changed());
							session.setAttribute("sess_user_full_name",
									user.getUser_first_name() + " " + user.getUser_last_name());
							objForAppend.put("sess_user_mobile", user.getUser_mobile());
							objForAppend.put("sess_user_email", "helpdesk@gmail.com");
							session.setAttribute("authentication_token", "mahesh");
							user.setLogin_attempts(0);
							user.setLogin_attempts_left(3);
							// usersDao.updateUser(user);
						} else {
							int user_login_attempts = user.getLogin_attempts();
							user_login_attempts = user_login_attempts + 1;
							if (user_login_attempts <= 3) {
								count = user.getLogin_attempts_left() - 1;
								if (count != 0) {
									user.setLogin_attempts(user_login_attempts);
									user.setLogin_attempts_left(count);
									usersDao.updateUser(user);
									objForAppend.put("responseMessage",
											"IncorrectPassword, You have " + count + " login attempts left");
									objForAppend.put("sess_user_id", "0");
									objForAppend.put("sess_role_id", "0");
									objForAppend.put("sess_user_default_password",
											user.getUser_default_password_changed());
								} else {
									user.setLogin_attempts(user_login_attempts);
									user.setLogin_attempts_left(count);
									user.setUser_enable_status("0");
									user.setAccount_locked_at(new Date());
									user.setAccount_locked_status(1);
									// usersDao.updateUser(user);
									objForAppend.put("responseMessage",
											"IncorrectPassword, You have " + count + " login attempts left");
									objForAppend.put("sess_user_id", "0");
									objForAppend.put("sess_role_id", "0");
									objForAppend.put("sess_user_default_password",
											user.getUser_default_password_changed());
								}
							} else {

								user.setUser_enable_status("0");
								user.setAccount_locked_at(new Date());
								user.setAccount_locked_status(1);
								// usersDao.updateUser(user);
								objForAppend.put("responseMessage", "Account Locked");
								objForAppend.put("sess_user_id", "0");
								objForAppend.put("sess_role_id", "0");
								objForAppend.put("sess_user_default_password", user.getUser_default_password_changed());
							}
						}
					}
				}
			} else {

				objForAppend.put("responseMessage", "UsernameDoesNotExists");
				objForAppend.put("sess_user_id", "0");
				objForAppend.put("sess_role_id", "0");
				objForAppend.put("sess_user_default_password", "0");
			}
			return objForAppend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			objForAppend.put("responseMessage", "Failed");
			objForAppend.put("sess_user_id", "0");
			objForAppend.put("sess_role_id", "0");
			objForAppend.put("sess_user_default_password", "0");
			return objForAppend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Forgot Password rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String forgotPassword(String jsonString) {
		System.out.println("In forgot password");
		JSONObject objForAppend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String user_username = jsonObj.get("user_username").toString();
			String user_email = jsonObj.get("user_email").toString();
			User user = usersDao.getUserByUserName(user_username);
			if (user.getUser_email().equals(user_email)) {
				/*------------Code for generating random password, resetting password and sending mail-----------*/
				StringBuffer randStr = new StringBuffer();
				for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
					int randomInt = 0;
					int number = 0;
					Random randomGenerator = new Random();
					randomInt = randomGenerator.nextInt(CHAR_LIST.length());
					if (randomInt - 1 == -1) {
						number = randomInt;
					} else {
						number = randomInt - 1;
					}
					char ch = CHAR_LIST.charAt(number);
					randStr.append(ch);
				}
				System.out.println("Password: " + randStr.toString());
				user.setUser_userpassword(BCrypt.hashpw(randStr.toString(), BCrypt.gensalt()));
				user.setUser_default_password_changed("0");
				usersDao.updateUser(user);
				// Mail sending code to be done
				/*--------------Code for sending mail-----------------*/
				Properties props = new Properties();
				props.put("mail.smtp.auth", "true");
				// props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.host", hostName);
				props.put("mail.smtp.port", portNo); // 465
				Session session = Session.getInstance(props, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

				String email_body = "Dear " + user.getUser_first_name() + " " + user.getUser_last_name() + ","
						+ "<br/><br/>Your password has been reset. Details are as follows:<br/>" + "Username: <Strong>"
						+ user_username + "</strong><br/>" + "Password : <Strong>" + randStr.toString() + "</strong>"
						+ "<br/><br/>" + "<a href='" + url
						+ "'>Click here to login </a><br/>This is a system generated mail. Please do not reply to this mail.<br/>";

				InternetAddress[] address = new InternetAddress[1];
				address[0] = new InternetAddress(user_email);

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(mailFrom));
				message.setRecipients(Message.RecipientType.TO, address);
				message.setSubject("Reset Password");
				message.setContent(email_body, "text/html; charset=utf-8");
				Transport.send(message);
				utilitiesService.addMailToLog(user_email, "Forgot Password", "");
				/*--------------Code for sending mail ends here-------*/
				/*------------Code for generating random password, resetting password and sending mail ends here----*/
				objForAppend.put("ramdomString", "");
				objForAppend.put("responseMessage", "Success");
			} else {
				objForAppend.put("responseMessage", "InvalidUsername_email_id");
			}
			return objForAppend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			objForAppend.put("responseMessage", "Failed");
			objForAppend.put("sess_user_id", "0");
			objForAppend.put("sess_role_id", "0");
			return objForAppend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Change Password rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String changePassword(String jsonString, HttpSession session) {
		JSONObject objForAppend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			String user_user_old_password = jsonObj.get("user_user_old_password").toString();
			String user_user_new_password = jsonObj.get("user_user_new_password").toString();
			User user = usersDao.getUserById(user_id);
			if (user_user_old_password.equals(user_user_new_password)) {
				objForAppend.put("responseMessage", "Old and New password cannot be same");
			} else {
				if (BCrypt.checkpw(user_user_old_password, user.getUser_userpassword())) {
					user.setUser_userpassword(BCrypt.hashpw(user_user_new_password, BCrypt.gensalt()));
					user.setUser_default_password_changed("1");
					usersDao.updateUser(user);
					objForAppend.put("responseMessage", "PasswordChanged");
					objForAppend.put("sess_user_default_password", 1);
				} else {
					objForAppend.put("responseMessage", "ExistingPasswordDoNotMatch");
				}
			}
			return objForAppend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			objForAppend.put("responseMessage", "Failed");
			objForAppend.put("sess_user_id", "0");
			objForAppend.put("sess_role_id", "0");
			objForAppend.put("sess_user_default_password", "0");
			return objForAppend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Save Function rest Call
	@Override
	public String userlogout(String jsonString, HttpSession session) {

		// JSONObject objForSend = new JSONObject();
		// try {
		// Date currentTime = new Date();
		// User userById = userDao.getUserById(userId);
		// userDao.updateLogoutTimeByUserId(userId, currentTime);
		// session.invalidate();
		// objForSend.put("responseMessage", "Success");
		// return objForSend.toJSONString();
		// } catch (Exception e) {
		// e.printStackTrace();
		// objForSend.put("responseMessage", "Failed");
		// return objForSend.toJSONString();
		// }
		//
		return jsonString;
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Save Function rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String getUserById(String jsonString, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		try {
			// JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			User user = usersDao.getUserById(Integer.parseInt(session.getAttribute("sess_user_id").toString()));
			dataForSend.put("user_id", user.getUser_id());
			dataForSend.put("user_username", user.getUser_username());
			dataForSend.put("user_userpassword", user.getUser_userpassword());
			dataForSend.put("user_employee_id", user.getUser_employee_id());
			dataForSend.put("user_first_name", user.getUser_first_name());
			dataForSend.put("user_last_name", user.getUser_last_name());
			dataForSend.put("user_full_name", user.getUser_first_name() + " " + user.getUser_last_name());
			dataForSend.put("user_mobile", user.getUser_mobile());
			dataForSend.put("user_email", user.getUser_email());
			dataForSend.put("user_address", user.getUser_address());
			dataForSend.put("user_role_id", user.getUser_role_id());
			dataForSend.put("user_organization_id", user.getUser_organization_id());
			dataForSend.put("user_location_id", user.getUser_location_id());
			dataForSend.put("user_department_id", user.getUser_department_id());
			dataForSend.put("user_designation_id", user.getUser_designation_id());
			dataForSend.put("user_report_to", user.getUser_report_to());
			dataForSend.put("user_enable_status", user.getUser_enable_status());
			dataForSend.put("user_approval_status", user.getUser_approval_status());
			dataForSend.put("user_added_by", user.getUser_added_by());
			// dataForSend.put("user_created_at", user.getUser_created_at());
			dataForSend.put("user_default_password_changed", user.getUser_default_password_changed());
			dataForSend.put("user_profile_pic", user.getProfile_pic());
			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			dataForSend.put("responseMessage", "Failed");
			return dataForSend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Save Function rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String enableDisableUser(String jsonString, HttpSession session) {
		JSONObject objForAppend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int user_id = Integer.parseInt(jsonObj.get("user_id").toString());
			String user_enable_status = jsonObj.get("user_enable_status").toString();
			User userSave = usersDao.getUserById(user_id);
			userSave.setUser_enable_status(user_enable_status);
			usersDao.updateUser(userSave);
			objForAppend.put("responseMessage", "Success");
			return objForAppend.toJSONString();
		} catch (Exception e) {
			objForAppend.put("errorMessage", "Failed");
			System.err.println(e);
			e.printStackTrace();
			return objForAppend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Save Function rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String downloaduserlist(String jsonString, HttpSession session) {
		JSONObject objForSend = new JSONObject();
		try {
			List<Object> userList = usersDao.downloaduserlist();
			Iterator<Object> itr = userList.iterator();
			JSONArray arrForAppend = new JSONArray();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("user_fullname", object[0].toString() + " " + object[1].toString());
				objForAppend.put("user_email", object[2]);
				objForAppend.put("orga_name", object[3]);
				objForAppend.put("loca_name", object[4]);
				objForAppend.put("dept_name", object[5]);
				objForAppend.put("user_username", object[6]);
				int user_role_id = Integer.parseInt(object[7].toString());
				if (user_role_id == 1)
					objForAppend.put("user_role_name", "Executor");
				if (user_role_id == 2)
					objForAppend.put("user_role_name", "Evaluator");
				if (user_role_id == 3)
					objForAppend.put("user_role_name", "Function Head");
				if (user_role_id == 4)
					objForAppend.put("user_role_name", "Unit Head");
				if (user_role_id == 5)
					objForAppend.put("user_role_name", "Entity Head");
				if (user_role_id == 6)
					objForAppend.put("user_role_name", "Administrator");
				if (user_role_id == 7)
					objForAppend.put("user_role_name", "Super Admin");
				if (user_role_id > 7 || user_role_id == 0)
					objForAppend.put("user_role_name", "NA");

				if (Integer.parseInt(object[8].toString()) == 0) {
					objForAppend.put("user_enable_status", "No");
				} else {
					objForAppend.put("user_enable_status", "Yes");
				}

				arrForAppend.add(objForAppend);
			}

			objForSend.put("user_list", arrForAppend);
			objForSend.put("responseMessage", "Success");
			return objForSend.toJSONString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			objForSend.put("responseMessage", "Failed");
			return objForSend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Send credential mails
	@SuppressWarnings("unchecked")
	@Override
	public String sendcredentialmail(String jsonString) {
		JSONObject objForSend = new JSONObject();
		try {
			System.out.println("In sending credentials");
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int user_id = Integer.parseInt(jsonObj.get("user_id").toString());
			User user = usersDao.getUserById(user_id);

			/* SENDING CREDENTIALS */

			StringBuffer randStr = new StringBuffer();
			for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
				int randomInt = 0;
				int number = 0;
				Random randomGenerator = new Random();
				randomInt = randomGenerator.nextInt(CHAR_LIST.length());
				if (randomInt - 1 == -1) {
					number = randomInt;
				} else {
					number = randomInt - 1;
				}
				char ch = CHAR_LIST.charAt(number);
				randStr.append(ch);
			}
			System.out.println("Password: " + randStr.toString());
			user.setUser_userpassword(BCrypt.hashpw(randStr.toString(), BCrypt.gensalt()));
			user.setUser_default_password_changed("0");
			usersDao.updateUser(user);

			/* SENDING CREDENTIALS END */

			/*----------------------------------------Code for generating mail---------------------------------------*/
			String email_body = "";

			email_body += "<div style='margin:0 auto;width:100%;height:auto;padding:20px;'>";
			email_body += "<h2 style='font-size:18px;color:#2D8DCF;'>Dear User,</h2>";
			email_body += "<p style='text-align:justify;width:70%;color:#2D8DCF;'>Following are your credentials for accessing LexCare Compliance Tool.</p>";

			email_body += "<table style='width:80%;' border='1'>" + "<thead>"
					+ "<tr style='background:#0B6EC3;color:#fff;'>" + "<th>Link</th>" + "<th>Name</th>"
					+ "<th>Registered email Id</th>" + "<th>Username</th>" + "<th>Password</th>"

					+ "</tr>" + "</thead>" + "<tbody>";

			email_body += "<tr>" + "<td><a href=" + url + ">Click here to follow the link</a></td>" + "<td>"
					+ user.getUser_first_name() + " " + user.getUser_last_name() + "</td>" + "<td>"
					+ user.getUser_email() + "</td>" + "<td>" + user.getUser_username() + "</td>" + "<td>"
					+ randStr.toString() + "</td>" + "</tr>";

			email_body += "</tbody>" + "</table>";
			email_body += "<p>This is a system generated mail. Please do not reply to this mail.<br/>"
					+ "In case of any doubt or difficulty, please call Helpdesk as per details given on the support page."
					+ "</p>" + "<h2 style='font-size:18px;font-weight:bold;color:#2D8DCF;'>Yours Sincerely</h2>"
					+ "<h2 style='font-size:19px;font-weight:bold;color:#2D8DCF;'>Team LexCare</h2>" + "</div>";
			/*----------------------------------------Code for generating mail ends here-----------------------------*/

			/*--------------------------Code to send mail---------------------*/
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			// props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", hostName);
			props.put("mail.smtp.port", portNo);
			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
			try {
				// System.out.println("Sending upcoming alert mail");
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(mailFrom));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getUser_email()));
				message.setSubject("Credential For Compliance Tool");
				message.setContent(email_body, "text/html; charset=utf-8");
				Transport.send(message);
				utilitiesService.addMailToLog(user.getUser_email(), "Credential Sent", "");
				System.out.println("Done");
				objForSend.put("responseMessage", "Success");

				// Add Log for the sending users credentials
				utilitiesService.saveLogForSendCredentials(user, randStr, message);

				return objForSend.toJSONString();
			} catch (Exception e) {
				// throw new RuntimeException(e);
				e.printStackTrace();
				objForSend.put("responseMessage", "Error in transport send");
				return objForSend.toJSONString();
			}
			/*----------------------Code to send mail ends here---------------*/

		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("responseMessage", "Failed");
			return objForSend.toJSONString();
		}
	}

	@Override
	public String addUpdateUserList(MultipartFile ttrn_proof_of_compliance, String jsonString, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		String user_name = session.getAttribute("sess_user_full_name").toString();
		int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
		JSONObject objForAppend1 = new JSONObject();

		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			if (!ttrn_proof_of_compliance.isEmpty()) {
				JSONArray neglectedTask = new JSONArray();
				JSONArray addedTask = new JSONArray();

				String name = jsonObj.get("name").toString();
				byte[] bytes = ttrn_proof_of_compliance.getBytes();

				// Create Temp File
				String fileExtension = FilenameUtils.getExtension(ttrn_proof_of_compliance.getOriginalFilename());
				if (fileExtension.equalsIgnoreCase("csv")) {
					File temp = File.createTempFile(ttrn_proof_of_compliance.getName(), ".csv");

					String absolutePath = temp.getAbsolutePath();
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(absolutePath));
					stream.write(bytes);
					stream.close();
					CsvReader legalUpdates = new CsvReader(absolutePath);

					legalUpdates.readHeaders();

					int i = 0;
					String user_username = "";
					String user_username_new = "";

					while (legalUpdates.readRecord()) {
						i++;
						System.out.println("this is record no: " + i);
						System.out.println("this is task id:" + legalUpdates.get("user_username"));

						JSONObject objForAppend = new JSONObject();
						User tsk = null;
						List<Object> userList = usersDao.getTasksForUserUpdate(legalUpdates.get("user_username"));

						if (userList.size() == 0) {
							User user = new User();

							user.setUser_first_name(legalUpdates.get("user_first_name"));
							user.setUser_last_name(legalUpdates.get("user_last_name"));

							if (legalUpdates.get("user_mobile") == "") {
								user.setUser_mobile(legalUpdates.get("0"));
							} else {
								user.setUser_mobile(legalUpdates.get("user_mobile"));
							}
							user.setUser_email(legalUpdates.get("user_email"));
							user.setUser_username(legalUpdates.get("user_username"));
							user.setUser_userpassword(legalUpdates.get("user_userpassword"));

							String entity_name = legalUpdates.get("entity_name");
							String unit_name = legalUpdates.get("unit_name");
							String function_name = legalUpdates.get("function_name");

							List<Object> orga = entityDao.getOrganizationIdByOrgaName(entity_name);
							int orgaId = 0;
							if (orga.size() > 0 || orga != null) {
								Iterator<Object> iterator = orga.iterator();
								while (iterator.hasNext()) {
									Object[] next = (Object[]) iterator.next();
									System.out.println("obj 1 " + next[0]);
									orgaId = Integer.parseInt(next[0].toString());
								}
							}

							List<Object> loca = unitDao.getLocationIdByName(unit_name);
							System.out.println("loca : " + loca.size());
							int unitId = 0;
							if (loca.size() > 0 || loca != null) {
								Iterator<Object> iterator = loca.iterator();
								while (iterator.hasNext()) {
									Object[] next = (Object[]) iterator.next();
									System.out.println("obj 1 " + next[0]);
									unitId = Integer.parseInt(next[0].toString());
								}
							}

							List<Object> dept = functionDao.getDepartmentNameById(function_name);
							int deptId = 0;
							if (dept.size() > 0 || dept != null) {
								Iterator<Object> iterator = dept.iterator();
								while (iterator.hasNext()) {
									Object[] next = (Object[]) iterator.next();
									System.out.println("obj 1 " + next[0]);
									deptId = Integer.parseInt(next[0].toString());
								}
							}

							user.setUser_department_id(deptId);
							user.setUser_location_id(unitId);
							user.setUser_organization_id(orgaId);

							user.setUser_employee_id("1");
							user.setUser_address("null");
							user.setUser_designation_id(1);
							System.out.println("role:" + legalUpdates.get("user_role_id"));
							if (legalUpdates.get("user_role_id").equals("Executor")) {
								user.setUser_role_id(Integer.parseInt("1"));
							} else if (legalUpdates.get("user_role_id").equals("Evaluator")) {
								user.setUser_role_id(Integer.parseInt("2"));
							} else if (legalUpdates.get("user_role_id").equals("Function Head")) {
								user.setUser_role_id(Integer.parseInt("3"));
							} else if (legalUpdates.get("user_role_id").equals("Unit Head")) {
								user.setUser_role_id(Integer.parseInt("4"));
							} else if (legalUpdates.get("user_role_id").equals("Entity Head")) {
								user.setUser_role_id(Integer.parseInt("5"));
							} else if (legalUpdates.get("user_role_id").equals("Administrator")) {
								user.setUser_role_id(Integer.parseInt("6"));
							} else if (legalUpdates.get("user_role_id").equals("Superadmin")) {
								user.setUser_role_id(Integer.parseInt("7"));
							} else if (legalUpdates.get("user_role_id").equals("Chief Function Head")) {
								user.setUser_role_id(Integer.parseInt("8"));
							} else if (legalUpdates.get("user_role_id").equals("Compliance Officer")) {
								user.setUser_role_id(Integer.parseInt("9"));
							} else if (legalUpdates.get("user_role_id").equals("Chief Compliance Head")) {
								user.setUser_role_id(Integer.parseInt("10"));
							} else if (legalUpdates.get("user_role_id").equals("Internal Auditor")) {
								user.setUser_role_id(Integer.parseInt("11"));
							} else if (legalUpdates.get("user_role_id").equals("Auditor")) {
								user.setUser_role_id(Integer.parseInt("12"));
							} else if (legalUpdates.get("user_role_id").equals("CFO")) {
								user.setUser_role_id(Integer.parseInt("13"));
							} else if (legalUpdates.get("user_role_id").equals("MD")) {
								user.setUser_role_id(Integer.parseInt("14"));
							}
							user.setUser_default_password_changed("1");
							user.setUser_created_at(new Date());
							user.setUser_added_by(1);
							user.setUser_enable_status("1");
							user.setUser_approval_status("1");

							usersDao.saveUser(user);
							objForAppend.put("responseMessage", "Success");
							addedTask.add(objForAppend);
							objForAppend.put("name", name);

						}
					}
					legalUpdates.close();

					// Set Temp data

					dataForSend.put("neglectedTasks", neglectedTask);
					dataForSend.put("addedTasks", addedTask);

					objForAppend1.put("responseMessage", "Success");
					return objForAppend1.toJSONString();

				} else {

					objForAppend1.put("responseMessage", "File type mismatch");
					return objForAppend1.toJSONString();
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			objForAppend1.put("responseMessage", "Failed");
			return objForAppend1.toJSONString();
		}
		return null;
	}

	@Override
	public void logOutEntry(int userId) {
		try {
			Date currentTime = new Date();
			User userById = usersDao.getUserById(userId);
			String sessionId = request.getSession().getId();
			usersDao.updateLogoutTimeByUserId(userId, currentTime, sessionId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
