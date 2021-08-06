package lexprd006.controller;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lexprd006.dao.UsersDao;
import lexprd006.service.UserEntityMappingService;
import lexprd006.service.UsersService;

/*
 * Author: Mahesh Kharote
 * Date: 28/10/2016
 * Purpose: Controller for Users
 * 
 * 
 * 
 * */

@Controller
@RequestMapping("/*")
public class UsersController {

	@Autowired
	UsersService usersService;

	@Autowired
	UserEntityMappingService userEntityMappingService;

	@Autowired
	HttpServletRequest request;

	@Autowired
	UsersDao userDao;
	private @Value("#{config['project_name'] ?: 'null'}") String projectName;

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all Functions rest Call
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/listusers", method = RequestMethod.GET)
	public @ResponseBody String listUsers(HttpSession session, HttpServletResponse res) {
		try {

			Enumeration<String> attributes = request.getSession().getAttributeNames();
			while (attributes.hasMoreElements()) {
				String attribute = (String) attributes.nextElement();
				System.out.println(attribute + " : " + request.getSession().getAttribute(attribute));
			}

			Integer sesRoleId = Integer.parseInt(session.getAttribute("sess_role_id").toString());
			System.out.println("sesRoleId : " + sesRoleId);
			JSONObject objForSend = new JSONObject();
			JSONArray dataForSend = new JSONArray();
			if (sesRoleId < 6) {
				int userId = 0;

				if (session.getAttribute("sess_user_id") != null) {
					userId = Integer.parseInt(session.getAttribute("sess_user_id").toString());
					// usersService.logOutEntry(userId);
					res.setHeader("Cache-Control", "no-cache");
					res.setHeader("Cache-Control", "no-store");
					res.setDateHeader("Expires", 0);
					res.setHeader("Pragma", "no-cache");
					session.invalidate();
					objForSend.put("responseMessage", "Failed");
					dataForSend.add(objForSend);
					return dataForSend.toJSONString();
				}
			}
			return usersService.listUsers();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Authenticate User
	@RequestMapping(value = "/authenticateUser", method = RequestMethod.POST)
	public @ResponseBody String authenticateUser(@RequestBody String jsonString, HttpSession session) {
		try {
			JSONObject objForSend = new JSONObject();
			JSONArray dataForSend = new JSONArray();
			Enumeration<String> attributes = request.getSession().getAttributeNames();
			while (attributes.hasMoreElements()) {
				String attribute = (String) attributes.nextElement();
				System.out.println(attribute + " : " + request.getSession().getAttribute(attribute));
			}
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String user_username = jsonObj.get("user_username").toString();
			String user_userpassword = jsonObj.get("user_userpassword").toString();
			List<Object> loggedInUser = userDao.checkIfExist(user_username);

			Iterator<Object> itr = loggedInUser.iterator();
			while (itr.hasNext()) {
				System.out.println("in while loop");
				Object[] object = (Object[]) itr.next();
				System.out.println("username:" + object[1].toString());
				String username = object[1].toString();
//				if (user_username.equals(username)) {
//					System.out.println("Status:" + object[1].toString());
//					objForSend.put("responseMessage", "loggedIn");
//					// dataForSend.add(objForSend);
//					return objForSend.toJSONString();
//				}
			}

			return usersService.authencticateUser(jsonString, session);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Authenticate User
	@RequestMapping(value = "/forgotpassword", method = RequestMethod.POST)
	public @ResponseBody String forgotPassword(@RequestBody String jsonString) {
		try {
			return usersService.forgotPassword(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Save User rest Call
	@RequestMapping(value = "/saveUser", method = RequestMethod.POST)
	public @ResponseBody String saveUser(@RequestBody String jsonString, HttpSession session, HttpServletResponse res) {
		try {
			System.out.println("USER ID FROM SESSION : " + session.getAttribute("userId"));
			String requestURI = request.getRequestURI();
			System.out.println("Called list entity  : " + requestURI);
			Integer sesRoleId = Integer.parseInt(session.getAttribute("sess_role_id").toString());
			System.out.println("sesRoleId : " + sesRoleId);
			JSONObject objForSend = new JSONObject();
			JSONArray dataForSend = new JSONArray();
			if (sesRoleId < 6) {
				int userId = 0;

				if (session.getAttribute("sess_user_id") != null) {
					userId = Integer.parseInt(session.getAttribute("sess_user_id").toString());
					// usersService.logOutEntry(userId);
					res.setHeader("Cache-Control", "no-cache");
					res.setHeader("Cache-Control", "no-store");
					res.setDateHeader("Expires", 0);
					res.setHeader("Pragma", "no-cache");
					session.invalidate();
					objForSend.put("responseMessage", "Failed");
					// dataForSend.add(objForSend);
					return objForSend.toJSONString();
				}
			}
			return usersService.saveUser(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Save User rest Call
	@RequestMapping(value = "/editUser", method = RequestMethod.POST)
	public @ResponseBody String editUser(@RequestBody String jsonString) {
		try {
			return usersService.editUser(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Save User rest Call
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public @ResponseBody String updateUser(@RequestBody String jsonString, HttpSession session,
			HttpServletResponse res) {
		try {
			System.out.println("USER ID FROM SESSION : " + session.getAttribute("userId"));
			String requestURI = request.getRequestURI();
			System.out.println("Called list entity  : " + requestURI);
			Integer sesRoleId = Integer.parseInt(session.getAttribute("sess_role_id").toString());
			System.out.println("sesRoleId : " + sesRoleId);
			JSONObject objForSend = new JSONObject();
			JSONArray dataForSend = new JSONArray();
			if (sesRoleId < 6) {
				int userId = 0;

				if (session.getAttribute("sess_user_id") != null) {
					userId = Integer.parseInt(session.getAttribute("sess_user_id").toString());
					// usersService.logOutEntry(userId);
					res.setHeader("Cache-Control", "no-cache");
					res.setHeader("Cache-Control", "no-store");
					res.setDateHeader("Expires", 0);
					res.setHeader("Pragma", "no-cache");
					session.invalidate();
					objForSend.put("responseMessage", "Failed");
					// dataForSend.add(objForSend);
					return objForSend.toJSONString();
				}
			}
			return usersService.updateUser(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Save User rest Call
	@RequestMapping(value = "/isusernameexists", method = RequestMethod.POST)
	public @ResponseBody String isUserNameExists(@RequestBody String jsonString) {
		try {
			return usersService.isUserNameExist(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Save User rest Call
	@RequestMapping(value = "/changepassword", method = RequestMethod.POST)
	public @ResponseBody String changepassword(@RequestBody String jsonString, HttpSession session) {
		try {
			return usersService.changePassword(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Save User rest Call
	@RequestMapping(value = "/saveuseraccess", method = RequestMethod.POST)
	public @ResponseBody String saveUserAccess(@RequestBody String jsonString, HttpSession session) {
		try {
			System.out.println("saveUserAccess jsonString : " + jsonString);
			return userEntityMappingService.saveUserEntityMapping(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get User Access both done and remaining rest Call
	@RequestMapping(value = "/getuseraccess", method = RequestMethod.POST)
	public @ResponseBody String getUserAccess(@RequestBody String jsonString, HttpSession session) {
		try {
			return userEntityMappingService.getUserEntityMappingUserWise(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get User Access both done and remaining rest Call
	@RequestMapping(value = "/enabledisableuser", method = RequestMethod.POST)
	public @ResponseBody String enableDisableUser(@RequestBody String jsonString, HttpSession session) {
		try {
			return usersService.enableDisableUser(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get User Access both done and remaining rest Call
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @RequestMapping(value = "/userlogout", method = RequestMethod.POST)
	 * public @ResponseBody String userLogout(@RequestBody String jsonString,
	 * HttpServletRequest req, HttpServletResponse res) {
	 * 
	 * JSONObject objForSend = new JSONObject(); try { HttpSession session =
	 * req.getSession(false); int userId =
	 * Integer.parseInt(session.getAttribute("sess_user_id").toString()); if
	 * (req.isRequestedSessionIdValid() && session != null) {
	 * usersService.logOutEntry(userId); session.invalidate();
	 * objForSend.put("responseMessage", "Success"); return
	 * objForSend.toJSONString(); }
	 * 
	 * int userId = 0;
	 * 
	 * if (session.getAttribute("sess_user_id") != null) { userId =
	 * Integer.parseInt(session.getAttribute("sess_user_id").toString());
	 * session.invalidate(); objForSend.put("responseMessage", "Success"); // return
	 * usersService.userlogout(jsonString, session); return
	 * objForSend.toJSONString(); }
	 * 
	 * handleLogOutResponse(res, req);
	 * 
	 * } catch (Exception e) { e.printStackTrace();
	 * objForSend.put("responseMessage", "Failed"); return
	 * objForSend.toJSONString(); } return jsonString; }
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/userlogout", method = RequestMethod.POST)
	public @ResponseBody String userLogout(@RequestBody String jsonString, HttpSession session, HttpServletRequest req,
			HttpServletResponse res) {
		JSONObject objForSend = new JSONObject();

		try {
			int userId = 0;
			System.out.println("fun userLogout() :" + jsonString);

			if (session.getAttribute("sess_user_id") != null) {
				userId = Integer.parseInt(session.getAttribute("sess_user_id").toString());
				usersService.logOutEntry(userId);
				userDao.deleteLoggedInUser(userId);
				session.invalidate();
				objForSend.put("responseMessage", "Success");
				// return usersService.userlogout(jsonString, session);
				return objForSend.toJSONString();
			}
			handleLogOutResponse(res, req);

		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("responseMessage", "Failed");
			return objForSend.toJSONString();
		}
		return jsonString;
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get User Access both done and remaining rest Call
	@RequestMapping(value = "/myaccount", method = RequestMethod.POST)
	public @ResponseBody String myAccount(@RequestBody String jsonString, HttpSession session) {
		try {
			return usersService.getUserById(jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get User Access both done and remaining rest Call
	@RequestMapping(value = "/downloaduserlist", method = RequestMethod.POST)
	public @ResponseBody String downloadUserList(String jsonString, HttpSession session) {
		try {
			return usersService.downloaduserlist(jsonString, session);
		} catch (Exception e) {
			return null;
		}

	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get User Access both done and remaining rest Call
	@RequestMapping(value = "/sendcredentialsmail", method = RequestMethod.POST)
	public @ResponseBody String sendcredentialsmail(@RequestBody String jsonString, HttpSession session) {
		try {

			// jsonString.(session.getAttribute("sess_user_full_name");
			return usersService.sendcredentialmail(jsonString);
		} catch (Exception e) {
			return null;
		}

	}

	// Method Written By: Harshad Padole
	// Method Purpose: Remove User Access
	@RequestMapping(value = "/removeUserAccess", method = RequestMethod.POST)
	public @ResponseBody String removeUserAccess(@RequestBody String jsonString, HttpSession session) {
		try {
			return userEntityMappingService.removeUserAccess(jsonString, session);
		} catch (Exception e) {
			return null;
		}

	}

	// Method Written By: Harshad Padole
	// Method Purpose: Remove User Access
	@RequestMapping(value = "/usermappinglist", method = RequestMethod.POST)
	public @ResponseBody String usermappinglist(@RequestBody String jsonString, HttpSession session) {
		try {
			return userEntityMappingService.getUserMappingList(jsonString, session);
		} catch (Exception e) {
			return null;
		}

	}

	// Method Written By: Mahesh Kharote
	// Method Purpose: Get user with access for common email
	@RequestMapping(value = "/getuserwithaccessforcommonemail", method = RequestMethod.POST)
	public @ResponseBody String getUserWithAccessForCommonEmail(@RequestBody String jsonString, HttpSession session) {
		try {
			return userEntityMappingService.getUserWithAccessForCommonEmail(jsonString, session);
		} catch (Exception e) {
			return null;
		}

	}

	@RequestMapping(value = "/importusersfromfile", method = RequestMethod.POST)
	public @ResponseBody String importLegalUpdates(@RequestParam("user_list") MultipartFile ttrn_proof_of_compliance,
			@RequestParam("jsonString") String jsonString, HttpSession session) {
		try {
			System.out.println("No of documents attached:" + ttrn_proof_of_compliance.getOriginalFilename());
			return usersService.addUpdateUserList(ttrn_proof_of_compliance, jsonString, session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void handleLogOutResponse(HttpServletResponse response, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		// System.out.println("cookies:" + cookies);
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				cookie.setMaxAge(0);
				cookie.setValue(null);
				cookie.setPath("/" + projectName);
				response.addCookie(cookie);
			}
		}
	}

}
