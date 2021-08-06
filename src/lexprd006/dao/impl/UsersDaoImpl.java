package lexprd006.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lexprd006.dao.UsersDao;
import lexprd006.domain.LoggedInUsers;
import lexprd006.domain.LogoutTimeLog;
import lexprd006.domain.User;

/*
 * Author: Mahesh Kharote
 * Date: 28/12/2016
 * Purpose: DAO Impl for Functions
 * 
 * 
 * 
 * */

@Repository(value = "usersDao")
@Transactional
public class UsersDaoImpl implements UsersDao {

	@PersistenceContext
	private EntityManager em;

	// Method Written By: Mahesh Kharote(28/12/2016)
	// Method Purpose: Save Users To DB
	@Override
	public void persist(Object obj) {
		try {
			em.persist(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Method Written By: Mahesh Kharote(28/12/2016)
	// Method Purpose: List Users To DB
	@Override
	public <T> Set<User> getAll(Class<T> clazz) {
		try {

			Set<User> userSet = new HashSet<User>();
			TypedQuery<T> query = em.createQuery(" from " + clazz.getName(), clazz);
			List<User> resultList = (List<User>) query.getResultList();
			userSet.addAll(resultList);
			return userSet;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Mahesh Kharote(28/12/2016)
	// Method Purpose: Get Users By Id
	@SuppressWarnings("rawtypes")
	@Override
	public User getUserById(int user_id) {
		try {
			TypedQuery query = em.createQuery(" from " + User.class.getName() + " where user_id = :user_id",
					User.class);
			query.setParameter("user_id", user_id);
			if (query.getResultList().size() > 0 && query.getResultList() != null) {
				return (User) query.getResultList().get(0);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void updateUser(User user) {
		try {
			em.merge(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int isUserNameExist(int user_id, String user_username) {
		try {
			TypedQuery query = em.createQuery(
					" from " + User.class.getName() + " where user_username = :user_username AND user_id !=:user_id",
					User.class);
			query.setParameter("user_username", user_username);
			query.setParameter("user_id", user_id);
			if (!query.getResultList().isEmpty())
				return query.getResultList().size();
			else
				return 0;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// Method Written By: Mahesh Kharote(28/12/2016)
	// Method Purpose: Get Users By User Name
	@SuppressWarnings("rawtypes")
	@Override
	public User getUserByUserName(String user_username) {
		try {
			TypedQuery query = em.createQuery(" from " + User.class.getName() + " where user_username = :user_username",
					User.class);
			query.setParameter("user_username", user_username);
			if (!query.getResultList().isEmpty())
				return (User) query.getResultList().get(0);
			else
				return null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Mahesh Kharote(28/12/2016)
	// Method Purpose: Download User List
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> downloaduserlist() {
		try {

			String sql = "SELECT usr.user_first_name,usr.user_last_name , usr.user_email, orga.orga_name, loca.loca_name , dept.dept_name, usr.user_username,usr.user_role_id, usr.user_enable_status "
					+ "FROM mst_user usr " + "JOIN mst_organization orga on orga.orga_id = usr.user_organization_id "
					+ "JOIN mst_location loca on loca.loca_id = usr.user_location_id "
					+ "JOIN mst_department dept on dept.dept_id = usr.user_department_id ";
			Query query = em.createQuery(sql);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Sharad Rindhe(06/02/2018)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	// Method Purpose: get User by role
	@Override
	public List<User> getUsersByRole(int role_id) {
		try {

			TypedQuery query = em.createQuery(" from " + User.class.getName() + " where user_role_id = :user_role_id",
					User.class);
			query.setParameter("user_role_id", role_id);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * Returning the list of users as per the admin has access to the Entity
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <Organization> Set<User> getUserByAdminAccess(HttpSession httpSession) {

		try {

			int userId = (int) httpSession.getAttribute("userId");
			System.out.println("User id in user dao impl is : " + userId);

			int userRole = (int) httpSession.getAttribute("sess_role_id");

			System.out.println("User Role Id : " + userRole);

			if (userRole <= 6) {
				// Query to get all the list of entity id where admin has access
				String query = "SELECT org.orga_id from mst_organization org, mst_organization org1, mst_user usr, cfg_user_entity_mapping umap WHERE org.orga_parent_id = org1.orga_id AND umap.umap_user_id = usr.user_id AND org.orga_id = umap.umap_orga_id AND usr.user_id = "
						+ userId + " group by org.orga_name";

				Query queryOrganizationId = em.createQuery(query);

				// List og organization id's
				List<Organization> resultList = queryOrganizationId.getResultList();

				Iterator<Organization> iterator = resultList.iterator();

				Set<User> users = new HashSet<User>();
				while (iterator.hasNext()) {

					Organization organization = iterator.next();

					int orgaId = (Integer.parseInt(organization.toString()));
					Class class1 = User.class;
					// System.out.println("ORGA ID : " +
					// (Integer.parseInt(organization.toString())));

					// Retriving maching record the data from the User_Entity_Mapping Table
					String sql = "select distinct(usr) from  mst_user usr JOIN cfg_user_entity_mapping umap ON usr.user_id = umap.umap_user_id "
							+ "JOIN mst_organization org ON org.orga_id = umap.umap_orga_id where umap_orga_id = '"
							+ orgaId + "' group by user_id ";

					// Maching record from Mst_user table
					String sqlFromUserTable = "select distinct(usr) from  mst_user usr where usr.user_organization_id = "
							+ orgaId + "";
					Query queryForUserList = em.createQuery(sql);
					Query queryForUserListFromUserTable = em.createQuery(sqlFromUserTable);
					//
					// TypedQuery<User> queryForUserList = em.createQuery(
					// " from " + class1.getName() + " where user_organization_id =" + orgaId,
					// User.class);

					List<User> userResultList = queryForUserList.getResultList();
					List<User> resultListFromMSTUser = queryForUserListFromUserTable.getResultList();
					users.addAll(userResultList);
					users.addAll(resultListFromMSTUser);
				}
				return users;
			} else {
				return getAll(User.class);
			}

			/*
			 * System.out.println("Size : " + users.size()); for (User user : users) {
			 * System.out.println("TEST : " + user.getUser_first_name() + "   " +
			 * user.getUser_organization_id()); }
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public List<Object> getTasksForUserUpdate(String user_username) {
		try {
			String sql = null;
			if (user_username.contains("'")) {
				user_username = user_username.replace("'", "''");
				sql = "from " + User.class.getName() + " where user_username = " + "'" + user_username + "'";
			} else {
				sql = "from " + User.class.getName() + " where user_username = " + "'" + user_username + "'";
			}
			System.out.println("sql:" + sql);
			Query query = em.createQuery(sql);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void saveUser(User user) {
		try {
			em.persist(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public User getUserIdByName(String user_name) {
		try {
			String arr[] = user_name.split(" ");
			System.out.println("arr[0]:" + arr[0] + " arr[1]" + arr[1]);
			String sql = null;
			sql = " from " + User.class.getName() + " where user_first_name = " + "'" + arr[0] + "'"
					+ " and user_last_name = " + "'" + arr[1] + "'";
			Query query = em.createQuery(sql);
			return (User) query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void saveLoginLogOutLogs(LogoutTimeLog log) {
		// em.merge(log);
		em.persist(log);
	}

	@Override
	public void persistUserLoginLog(int user_id, User usr) {
		try {
			Date currentDate = new Date();
			SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String format = ss.format(currentDate);
			// Date loginOutTime = ss.parse(format);

			String sql = "UPDATE mst_user SET isOnline = 'Yes', loginTime = '" + format + "' WHERE USER_ID = '"
					+ user_id + "' ";
			Query q = em.createNativeQuery(sql);
			// System.out.println("update query : " + sql);
			q.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateLogoutTimeByUserId(int userId, Date loginOutTimes, String sessionId) {

		try {
			SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String format = ss.format(loginOutTimes);
			Date loginOutTime = ss.parse(format);

			String sql = "update login_logout_log set LOGOUT_TIME = '" + format + "' where user_id = '" + userId
					+ "' AND SESSION_ID = '" + sessionId + "' ";
			Query q = em.createNativeQuery(sql);
			// System.out.println("updateLogoutTimeByUserId sql query : " + sql);
			q.executeUpdate();

			Date currentDates = new Date();
			SimpleDateFormat sss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String formats = sss.format(currentDates);
			// Date loginOutTime = ss.parse(format);

			String sqls = "UPDATE mst_user SET isOnline = 'No', logOutTime = '" + formats + "' WHERE USER_ID = '"
					+ userId + "' ";
			Query qs = em.createNativeQuery(sqls);
			System.out.println("update query : " + sqls);
			qs.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Object> getAllUsers() {
		String sql = "select usr.user_id, usr.user_first_name, usr.user_last_name, usr.user_enable_status, usr.account_locked_at, usr.account_locked_status from mst_user usr where usr.account_locked_status = 1 ";
		Query query = em.createQuery(sql);
		return query.getResultList();
	}

	@Override
	public List<Object> checkIfExist(String user_username) {
		try {
			String sql = null;
			sql = "select log.id, log.username, log.sessionId from logged_in_users log where log.username = " + "'"
					+ user_username + "'";
			Query query = em.createQuery(sql);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void saveLoggedInUser(LoggedInUsers loggedInUser) {
		try {
			em.persist(loggedInUser);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// TODO Auto-generated method stub

	}

	@Override
	public int deleteLoggedInUser(int userId) {
		try {
			String sql = "delete from logged_in_users where user_id = " + userId;
			Query query = em.createQuery(sql);
			return query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int deleteAllLoggedInUsers() {
		try {
			String sql = " delete from logged_in_users ";
			Query query = em.createQuery(sql);
			return query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
