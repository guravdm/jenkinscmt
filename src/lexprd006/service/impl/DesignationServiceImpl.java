package lexprd006.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lexprd006.dao.DesignationDao;
import lexprd006.domain.Designation;
import lexprd006.service.DesignationService;
/*
 * Author: Mahesh Kharote
 * Date: 25/10/2016
 * Purpose: Service Impl for Functions
 * 
 * 
 * 
 * */

@Service(value = "designationService")
public class DesignationServiceImpl implements DesignationService {

	@Autowired
	DesignationDao designationDao;

	@SuppressWarnings("unchecked")
	@Override
	public String listDesignations() {
		JSONArray dataForSend = new JSONArray();
		try {

			List<Designation> designations = designationDao.getAll(Designation.class);

			Iterator<Designation> iterator = designations.iterator();
			while (iterator.hasNext()) {
				Designation designation = (Designation) iterator.next();
				JSONObject objForAppend = new JSONObject();

				objForAppend.put("desi_id", designation.getDesi_id());
				objForAppend.put("desi_name", designation.getDesi_name());

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

	@SuppressWarnings("unchecked")
	@Override
	public String saveDesignation(String jsonString) {
		JSONObject objForAppend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String desi_name = jsonObj.get("desi_name").toString();
			/*------------Code for send data to DAO will be here-----------------------*/

			Designation designation = new Designation();
			designation.setDesi_name(desi_name);
			designation.setDesi_added_by(1);
			designation.setDesi_approval_status("1");
			designation.setDesi_enable_status("1");
			designation.setDesi_parent_id(0);
			designation.setDesi_created_at(new Date());
			designationDao.persist(designation);

			/*------------Code for send data to DAO Ends here-----------------------*/

			/*------------This is test data-----------------------*/
			objForAppend.put("desi_name", desi_name);
			/*------------This is test data ends here-----------------------*/

			return objForAppend.toJSONString();
		} catch (Exception e) {
			objForAppend.put("errorMessage", "Failed");
			e.printStackTrace();
			return objForAppend.toJSONString();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String editDesignation(String jsonString) {
		JSONArray dataForSend = new JSONArray();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String desi_id = jsonObj.get("desi_id").toString();
			/*------------Code for send data to DAO will be here-----------------------*/

			/*------------Code for send data to DAO Ends here-----------------------*/

			/*------------This is test data-----------------------*/
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("dept_id", desi_id);
			/*------------This is test data ends here-----------------------*/

			dataForSend.add(objForAppend);
			return dataForSend.toJSONString();
		} catch (Exception e) {
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("errorMessage", "Failed");
			dataForSend.add(objForAppend);
			e.printStackTrace();
			return dataForSend.toJSONString();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String updateDesignation(String jsonString) {
		JSONArray dataForSend = new JSONArray();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int desi_id = Integer.parseInt(jsonObj.get("desi_id").toString());
			String desi_name = jsonObj.get("desi_name").toString();
			/*------------Code for send data to DAO will be here-----------------------*/

			Designation designation = designationDao.getDesignationById(desi_id);
			designation.setDesi_name(desi_name);
			designationDao.updateDesignation(designation);

			/*------------Code for send data to DAO Ends here-----------------------*/

			/*------------This is test data-----------------------*/
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("desi_id", desi_id);
			objForAppend.put("desi_name", desi_name);
			/*------------This is test data ends here-----------------------*/

			dataForSend.add(objForAppend);
			return dataForSend.toJSONString();
		} catch (Exception e) {
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("errorMessage", "Failed");
			dataForSend.add(objForAppend);
			e.printStackTrace();
			return dataForSend.toJSONString();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String deleteDesignation(String jsonString) {
		JSONArray dataForSend = new JSONArray();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String desi_id = jsonObj.get("desi_id").toString();

			/*------------Code for send data to DAO will be here-----------------------*/

			/*------------Code for send data to DAO Ends here-----------------------*/

			/*------------This is test data-----------------------*/
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("desi_id", desi_id);

			/*------------This is test data ends here-----------------------*/

			dataForSend.add(objForAppend);
			return dataForSend.toJSONString();
		} catch (Exception e) {
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("errorMessage", "Failed");
			dataForSend.add(objForAppend);
			e.printStackTrace();
			return dataForSend.toJSONString();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String isDesignationNameExist(String jsonString) {
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int desi_id = Integer.parseInt(jsonObj.get("desi_id").toString());
			String desi_name = jsonObj.get("desi_name").toString();
			int count = designationDao.isDesiNameExist(desi_id, desi_name);
			if (count > 0) {
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("duplicate", "True");
				return objForAppend.toJSONString();

			} else {
				JSONObject objForAppend = new JSONObject();
				objForAppend.put("duplicate", "False");
				return objForAppend.toJSONString();
			}
		} catch (Exception e) {
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("errorMessage", "Failed");
			e.printStackTrace();
			return objForAppend.toJSONString();

		}
	}

}
