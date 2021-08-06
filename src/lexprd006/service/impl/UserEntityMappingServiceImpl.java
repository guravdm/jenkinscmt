package lexprd006.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lexprd006.dao.EntityMappingDao;
import lexprd006.dao.UserEntityMappingDao;
import lexprd006.domain.UserEntityMapping;
import lexprd006.service.UserEntityMappingService;
import lexprd006.service.UtilitiesService;

@Service(value = "userEntityMappingService")
public class UserEntityMappingServiceImpl implements UserEntityMappingService {

	@Autowired
	UserEntityMappingDao userEntityMappingDao;

	@Autowired
	EntityMappingDao entityMappingDao;

	@Autowired
	UtilitiesService utilitiesService;

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all Functions rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String saveUserEntityMapping(String jsonString, HttpSession session) {

//		JSONObject objForAppend = new JSONObject();
//		try {
//			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
//
//			int umap_user_id = Integer.parseInt(jsonObj.get("umap_user_id").toString());
//
//
//			/*------------Code for send data to DAO will be here-----------------------*/
//			/*------------Iterator JSONArray-----------------------*/
//			JSONArray mapping_list = (JSONArray) jsonObj.get("mapping_list");
//			for(int i = 0; i < mapping_list.size(); i++){
//				JSONObject entityMappingList = (JSONObject) mapping_list.get(i);
//				int umap_orga_id = Integer.parseInt(entityMappingList.get("umap_orga_id").toString());
//				int umap_loca_id = Integer.parseInt(entityMappingList.get("umap_loca_id").toString());
//				int umap_dept_id = Integer.parseInt(entityMappingList.get("umap_dept_id").toString());
//
//				UserEntityMapping userEntityMapping = new UserEntityMapping();
//				//userEntityMapping.setUmap_added_by(Integer.parseInt(session.getAttribute("sess_user_id").toString()));
//				userEntityMapping.setUmap_added_by(1);
//				userEntityMapping.setUmap_approval_status("1");
//				userEntityMapping.setUmap_created_at(new Date());
//				userEntityMapping.setUmap_dept_id(umap_dept_id);
//				userEntityMapping.setUmap_enable_status("1");
//				userEntityMapping.setUmap_loca_id(umap_loca_id);
//				userEntityMapping.setUmap_orga_id(umap_orga_id);
//				userEntityMapping.setUmap_user_id(umap_user_id);
//
//				userEntityMappingDao.persist(userEntityMapping);
//			}
//
//
//			/*------------Code for send data to DAO Ends here-----------------------*/
//
//			/*------------This is test data-----------------------*/
//			objForAppend.put("responseMessage", "Success");
//			/*------------This is test data ends here-----------------------*/
//
//
//			return objForAppend.toJSONString();
//		} catch (Exception e) {
//			objForAppend.put("errorMessage", "Failed");
//			e.printStackTrace();
//			return objForAppend.toJSONString();
//		}

		JSONObject objForAppend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);

			int umap_user_id = Integer.parseInt(jsonObj.get("umap_user_id").toString());

			/*------------Code for send data to DAO will be here-----------------------*/
			/*------------Iterator JSONArray-----------------------*/
			JSONArray mapping_list = (JSONArray) jsonObj.get("mapping_list");
			for (int i = 0; i < mapping_list.size(); i++) {

				JSONObject entityMappingList = (JSONObject) mapping_list.get(i);
				int umap_orga_id = Integer.parseInt(entityMappingList.get("umap_orga_id").toString());
				int umap_loca_id = Integer.parseInt(entityMappingList.get("umap_loca_id").toString());
				int umap_dept_id = Integer.parseInt(entityMappingList.get("umap_dept_id").toString());

				List<Object> allByID = entityMappingDao.getAllByID(umap_orga_id, umap_loca_id, umap_dept_id);

				if (allByID != null && allByID.size() > 0) {

					System.out.println("Inserted------------------------> ");

					UserEntityMapping userEntityMapping = new UserEntityMapping();
					// userEntityMapping.setUmap_added_by(Integer.parseInt(session.getAttribute("sess_user_id").toString()));
					userEntityMapping.setUmap_added_by(1);
					userEntityMapping.setUmap_approval_status("1");
					userEntityMapping.setUmap_created_at(new Date());
					userEntityMapping.setUmap_dept_id(umap_dept_id);
					userEntityMapping.setUmap_enable_status("1");
					userEntityMapping.setUmap_loca_id(umap_loca_id);
					userEntityMapping.setUmap_orga_id(umap_orga_id);
					userEntityMapping.setUmap_user_id(umap_user_id);

					userEntityMappingDao.persist(userEntityMapping);
				}
			}

			/*------------Code for send data to DAO Ends here-----------------------*/

			/*------------This is test data-----------------------*/
			objForAppend.put("responseMessage", "Success");
			/*------------This is test data ends here-----------------------*/

			return objForAppend.toJSONString();
		} catch (Exception e) {
			objForAppend.put("errorMessage", "Failed");
			e.printStackTrace();
			return objForAppend.toJSONString();
		}

	}

	// Method Written By: Mahesh Kharote(27/02/2017)
	// Method Purpose: Get all Functions rest Call
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String getUserEntityMappingUserWise(String jsonString, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			JSONArray mappedDataForAppend = new JSONArray();
			JSONArray unMappedDataForAppend = new JSONArray();
			if (Integer.parseInt(jsonObj.get("user_id").toString()) > 0) {
				List<Object> allUserEntityMappingList = userEntityMappingDao
						.getUserAccessById(Integer.parseInt(jsonObj.get("user_id").toString()));
				List<Object> allEntityMapping = entityMappingDao.getAllMapping();

				/*-----------------Adding existing mapping to jsonArray whilst adding to checking array--------------------------*/
				List<List> doneMapping = new ArrayList<>();// Add all the mapping done enti, loca, func id remaining
															// will go as remaining mapping

				Iterator<Object> itrUserMappingDone = allUserEntityMappingList.iterator();
				while (itrUserMappingDone.hasNext()) {
					Object[] object = (Object[]) itrUserMappingDone.next();
					JSONObject objForAppend = new JSONObject();
					objForAppend.put("orga_id", object[0]);
					objForAppend.put("orga_name", object[1]);
					objForAppend.put("loca_id", object[2]);
					objForAppend.put("loca_name", object[3]);
					objForAppend.put("dept_id", object[4]);
					objForAppend.put("dept_name", object[5]);
					objForAppend.put("user_id", object[6]);
					objForAppend.put("umap_id", object[10]);

					mappedDataForAppend.add(objForAppend);

					List<Integer> doneMappingChk = new ArrayList<>();
					doneMappingChk.add(Integer.parseInt(object[0].toString()));
					doneMappingChk.add(Integer.parseInt(object[2].toString()));
					doneMappingChk.add(Integer.parseInt(object[4].toString()));

					doneMapping.add(doneMappingChk);

				}

				/*-----------------Adding existing mapping to jsonArray whilst adding to checking array ends here----------------*/

				/*-----------------Adding remaining mapping to jsonArray whilst checking from check array------------------------*/
				Iterator<Object> itrUserMappingNotDone = allEntityMapping.iterator();

				while (itrUserMappingNotDone.hasNext()) {
					Object[] objectPending = (Object[]) itrUserMappingNotDone.next();
					List<Integer> pendingMappingCheck = new ArrayList<>();

					pendingMappingCheck.add(Integer.parseInt(objectPending[1].toString()));
					pendingMappingCheck.add(Integer.parseInt(objectPending[3].toString()));
					pendingMappingCheck.add(Integer.parseInt(objectPending[5].toString()));

					if (!doneMapping.contains(pendingMappingCheck)) {
						JSONObject pendingMappingObj = new JSONObject();
						pendingMappingObj.put("orga_id", objectPending[1]);
						pendingMappingObj.put("orga_name", objectPending[2]);
						pendingMappingObj.put("loca_id", objectPending[3]);
						pendingMappingObj.put("loca_name", objectPending[4]);
						pendingMappingObj.put("dept_id", objectPending[5]);
						pendingMappingObj.put("dept_name", objectPending[6]);

						unMappedDataForAppend.add(pendingMappingObj);
					}
				}

				/*-----------------Adding remaining mapping to jsonArray whilst checking from check array ends here--------------*/

				dataForSend.put("AccessSet", mappedDataForAppend);
				dataForSend.put("AccessRemaining", unMappedDataForAppend);
				return dataForSend.toJSONString();
			} else {
				dataForSend.put("responseMessage", "Failed");
				return dataForSend.toJSONString();
			}

		} catch (Exception e) {
			e.printStackTrace();
			dataForSend.put("responseMessage", "Failed");
			return dataForSend.toJSONString();
		}
	}

	// Method Written By: Harshad Padole
	// Method Purpose: Remove User access if task not assigned to user
	@SuppressWarnings("unchecked")
	@Override
	public String removeUserAccess(String json, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(json);
			int umap_user_id = Integer.parseInt(jsonObj.get("umap_user_id").toString());
			String user_name = session.getAttribute("sess_user_full_name").toString();
			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			JSONArray umap_ids = (JSONArray) jsonObj.get("umap_ids");
			for (int i = 0; i < umap_ids.size(); i++) {
				JSONObject id = (JSONObject) umap_ids.get(i);
				int umap_id = Integer.parseInt(id.get("umap_id").toString());
				int umap_orga = Integer.parseInt(id.get("umap_orga_id").toString());
				int umap_loca = Integer.parseInt(id.get("umap_loca_id").toString());
				int umap_dept = Integer.parseInt(id.get("umap_dept_id").toString());
				List<Object> res = userEntityMappingDao.checkTaskExist(umap_id, umap_user_id);
				System.out.println("Count " + res.size());
				if (res.size() == 0) {
					userEntityMappingDao.removeUserAccess(umap_id);
					String orga_loca_dept = "umap_id-" + umap_id + " Orga_id-" + umap_orga + " Loca id-" + umap_loca
							+ " Dept-" + umap_dept;
					utilitiesService.addRemoveUserAccessLog(user_id, user_name, orga_loca_dept, "Delete");
					;
					dataForSend.put("responseMessage", "Success");

				} else {
					dataForSend.put("responseMessage", "TaskAssigned");
				}
			}
			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			dataForSend.put("responseMessage", "Failed");
			return dataForSend.toJSONString();
		}
	}

	// Method Written By: Harshad Padole
	// Method Purpose: User mapping list
	@SuppressWarnings("unchecked")
	@Override
	public String getUserMappingList(String json, HttpSession session) {
		JSONObject dataTosend = new JSONObject();
		try {
			JSONArray list = new JSONArray();
			List<Object> result = userEntityMappingDao.getOrgonogram();
			Iterator<Object> iterator = result.iterator();
			while (iterator.hasNext()) {
				Object[] objects = (Object[]) iterator.next();

				JSONObject orgonogram = new JSONObject();

				int orga_id = Integer.parseInt(objects[0].toString());
				int loca_id = Integer.parseInt(objects[1].toString());
				int dept_id = Integer.parseInt(objects[2].toString());

				orgonogram.put("orga_name", objects[3]);
				orgonogram.put("loca_name", objects[4]);
				orgonogram.put("dept_name", objects[5]);
				JSONArray executer_list = new JSONArray();
				JSONArray evaluator_list = new JSONArray();
				JSONArray functionHead_list = new JSONArray();
				JSONArray unitHead_list = new JSONArray();
				JSONArray entityHead_list = new JSONArray();
				List<Object> userList = userEntityMappingDao.getUserForMappingList(orga_id, loca_id, dept_id);
				if (userList != null) {
					Iterator<Object> itr = userList.iterator();
					while (itr.hasNext()) {
						Object[] obj = (Object[]) itr.next();
						JSONObject jsonObject = new JSONObject();
						int user_role = Integer.parseInt(obj[3].toString());
						if (user_role == 1) {
							jsonObject.put("user_id", obj[0]);
							jsonObject.put("userFullname", obj[1] + " " + obj[2]);
							executer_list.add(jsonObject);
						}
						if (user_role == 2) {
							jsonObject.put("user_id", obj[0]);
							jsonObject.put("userFullname", obj[1] + " " + obj[2]);
							evaluator_list.add(jsonObject);
						}
						if (user_role == 3) {
							jsonObject.put("user_id", obj[0]);
							jsonObject.put("userFullname", obj[1] + " " + obj[2]);
							functionHead_list.add(jsonObject);
						}
						if (user_role == 4) {
							jsonObject.put("user_id", obj[0]);
							jsonObject.put("userFullname", obj[1] + " " + obj[2]);
							unitHead_list.add(jsonObject);
						}
						if (user_role == 5) {
							jsonObject.put("user_id", obj[0]);
							jsonObject.put("userFullname", obj[1] + " " + obj[2]);
							entityHead_list.add(jsonObject);
						}
					}
				}

				orgonogram.put("Executors", executer_list);
				orgonogram.put("Evaluators", evaluator_list);
				orgonogram.put("FuntionHeads", functionHead_list);
				orgonogram.put("UnitHeads", unitHead_list);
				orgonogram.put("EntityHeads", entityHead_list);
				list.add(orgonogram);
			}

			dataTosend.put("orgonogram", list);
			return dataTosend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			dataTosend.put("responseMessage", "Failed");
			return dataTosend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(05/10/2017)
	// Method Purpose: Get User access wise
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String getUserWithAccessForCommonEmail(String json, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		try {
			List<Object> orga = userEntityMappingDao.getUserWithAccessForCommonEmail();
			Iterator<Object> itr = orga.iterator();

			List<Integer> checkEnti = new ArrayList<>();
			List<List> checkUnit = new ArrayList<>();
			List<List> checkFunc = new ArrayList<>();

			JSONArray entityArray = new JSONArray();
			JSONArray unitArray = new JSONArray();
			JSONArray funcArray = new JSONArray();
			JSONArray userArray = new JSONArray();

			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();

				JSONObject dataForEntity = new JSONObject();

				if (!checkEnti.contains(object[0])) {
					dataForEntity.put("orga_id", object[0]);
					dataForEntity.put("orga_name", object[1]);

					checkEnti.add(Integer.parseInt(object[0].toString()));
					entityArray.add(dataForEntity);
				}

				List<Integer> listForAddingCheckUnit = new ArrayList<>();
				listForAddingCheckUnit.add(Integer.parseInt(object[0].toString()));
				listForAddingCheckUnit.add(Integer.parseInt(object[2].toString()));
				JSONObject dataForUnit = new JSONObject();
				if (!checkUnit.contains(listForAddingCheckUnit)) {
					dataForUnit.put("loca_id", object[2]);
					dataForUnit.put("loca_name", object[3]);
					dataForUnit.put("orga_id", object[0]);

					checkUnit.add(listForAddingCheckUnit);
					unitArray.add(dataForUnit);
				}

				List<Integer> listForAddingCheckFunc = new ArrayList<>();
				listForAddingCheckFunc.add(Integer.parseInt(object[0].toString()));
				listForAddingCheckFunc.add(Integer.parseInt(object[2].toString()));
				listForAddingCheckFunc.add(Integer.parseInt(object[4].toString()));
				JSONObject dataForFunc = new JSONObject();
				if (!checkFunc.contains(listForAddingCheckFunc)) {
					dataForFunc.put("dept_id", object[4]);
					dataForFunc.put("dept_name", object[5]);
					dataForFunc.put("orga_id", object[0]);
					dataForFunc.put("loca_id", object[2]);

					checkFunc.add(listForAddingCheckFunc);
					funcArray.add(dataForFunc);
				}

				JSONObject dataForUser = new JSONObject();
				dataForUser.put("dept_id", object[4]);
				dataForUser.put("orga_id", object[0]);
				dataForUser.put("loca_id", object[2]);
				dataForUser.put("user_id", object[6]);
				dataForUser.put("user_name", object[7].toString() + " " + object[8].toString());
				dataForUser.put("user_email", object[9]);
				userArray.add(dataForUser);

				dataForSend.put("Entity", entityArray);
				dataForSend.put("Unit", unitArray);
				dataForSend.put("Function", funcArray);
				dataForSend.put("Users", userArray);

			}

			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			dataForSend.put("responseMessage", "Failed");
			return dataForSend.toJSONString();
		}
	}

}
