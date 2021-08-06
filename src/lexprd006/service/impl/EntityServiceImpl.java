package lexprd006.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.csvreader.CsvReader;

import lexprd006.dao.EntityDao;
import lexprd006.domain.Organization;
import lexprd006.service.EntityService;

/*
 * Author: Mahesh Kharote
 * Date: 25/10/2016
 * Purpose: Service Interface for Entities
 * 
 * 
 * 
 * */
@Service(value = "entityService")
public class EntityServiceImpl implements EntityService {

	@Autowired
	EntityDao entityDao;

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all Organizations rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String listEntities(HttpSession httpSession, String requestFromMethod) {
		JSONArray dataForSend = new JSONArray();

		try {

			// System.out.println("URL FROM : " +request.getAttribute("url").toString());

			// List<Organization> organizations = entityDao.getAll(Organization.class);
			List<Object> orga = entityDao.getJoinedAll(httpSession, requestFromMethod);

			Iterator<Object> itr = orga.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject objForAppend = new JSONObject();

				objForAppend.put("orga_id", object[0]);
				objForAppend.put("orga_name", object[1]);

				objForAppend.put("orga_parent_id", object[2]);
				objForAppend.put("orga_parent_name", object[3]);
				dataForSend.add(objForAppend);
			}

			// Iterator<Organization> itr = organizations.iterator();
			// while (itr.hasNext()) {
			// JSONObject objForAppend = new JSONObject();
			// Organization organization = (Organization) itr.next();
			// objForAppend.put("orga_id", organization.getOrga_id());
			// objForAppend.put("orga_name", organization.getOrga_name());
			// objForAppend.put("orga_parent_name", organization.getOrga_parent_id());
			// objForAppend.put("orga_parent_id", organization.getOrga_parent_id());
			// dataForSend.add(objForAppend);
			// }
			/* System.out.println("Entity List : "+dataForSend.toJSONString()); */
			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("errorMessage", "Failed");
			dataForSend.add(objForAppend);
			return dataForSend.toJSONString();
		}

	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Save Organization rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String saveEntity(String jsonString) {
		JSONObject objForAppend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int orga_parent_id = Integer.parseInt(jsonObj.get("orga_parent_id").toString());
			String orga_name = jsonObj.get("orga_name").toString();
			/*------------Code for send data to DAO will be here-----------------------*/

			Organization organization = new Organization();
			organization.setOrga_parent_id(orga_parent_id);
			organization.setOrga_name(orga_name);
			organization.setOrga_added_by(1);
			organization.setOrga_approval_status("1");
			organization.setOrga_created_at(new Date());
			organization.setOrga_enable_status("1");
			entityDao.persist(organization);

			/*------------Code for send data to DAO Ends here-----------------------*/

			/*------------This is test data-----------------------*/

			objForAppend.put("orga_name", orga_name);
			objForAppend.put("responseMessage", "Success");
			/*------------This is test data ends here-----------------------*/

			return objForAppend.toJSONString();
		} catch (Exception e) {

			objForAppend.put("responseMessage", "Failed");

			e.printStackTrace();
			return objForAppend.toJSONString();
		}
	}

	@Override
	public String editEntity(String jsonString) {
		// TODO Auto-generated method stub
		return null;
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Save Organization rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String updateEntity(String jsonString) {
		JSONObject objForAppend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int orga_id = Integer.parseInt(jsonObj.get("orga_id").toString());
			int orga_parent_id = Integer.parseInt(jsonObj.get("orga_parent_id").toString());
			String orga_name = jsonObj.get("orga_name").toString();

			Organization organization = entityDao.getOrganizationById(orga_id);
			organization.setOrga_parent_id(orga_parent_id);
			organization.setOrga_name(orga_name);
			entityDao.updateOrganization(organization);

			objForAppend.put("orga_id", orga_id);
			objForAppend.put("orga_name", orga_name);

			objForAppend.put("responseMessage", "Success");
			return objForAppend.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
			objForAppend.put("responseMessage", "Failed");
			return objForAppend.toJSONString();
		}
	}

	@Override
	public String deleteEntity(String jsonString) {
		// TODO Auto-generated method stub
		return null;
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Check if Organization name already exists
	@SuppressWarnings("unchecked")
	@Override
	public String isEntityNameExist(String jsonString) {
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int orga_id = Integer.parseInt(jsonObj.get("orga_id").toString());
			String orga_name = jsonObj.get("orga_name").toString();
			int count = entityDao.isOrgaNameExist(orga_id, orga_name);
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

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all Organizations rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String listAllForAddingEntity() {
		JSONArray dataForSend = new JSONArray();

		try {

			List<Organization> organizations = entityDao.getAllForAddingEntity(Organization.class);

			Iterator<Organization> itr = organizations.iterator();
			while (itr.hasNext()) {
				JSONObject objForAppend = new JSONObject();
				Organization organization = (Organization) itr.next();
				objForAppend.put("orga_parent_id", organization.getOrga_id());
				objForAppend.put("orga_parent_name", organization.getOrga_name());
				dataForSend.add(objForAppend);
			}
			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("errorMessage", "Failed");
			dataForSend.add(objForAppend);
			return dataForSend.toJSONString();
		}
	}

	@SuppressWarnings("unused")
	@Override
	public String importEntityList(MultipartFile entity_update_list, String jsonString, HttpSession session) {
		String user_name = session.getAttribute("sess_user_full_name").toString();
		int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
		JSONObject objForAppend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			if (!entity_update_list.isEmpty()) {
				JSONArray neglectedTask = new JSONArray();
				JSONArray addedTask = new JSONArray();

				String name = jsonObj.get("name").toString();
				byte[] bytes = entity_update_list.getBytes();

				String fileExtension = FilenameUtils.getExtension(entity_update_list.getOriginalFilename());
				if (fileExtension.equalsIgnoreCase("csv")) {

					File temp = File.createTempFile(entity_update_list.getName(), ".csv");

					int i = 0;
					String absolutePath = temp.getAbsolutePath();
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(absolutePath));
					stream.write(bytes);
					stream.close();
					CsvReader entityUpdates = new CsvReader(absolutePath);
					entityUpdates.readHeaders();

					while (entityUpdates.readRecord()) {
						i++;
						String orga_name = null;
						List<Organization> orga = entityDao.checkDuplicateName(entityUpdates.get("orga_name"));
						System.out.println("size : " + orga.size());
						if (orga.size() == 0) {
							System.out.println("inside updateOrganization {} ");
							// Iterator<Organization> itrOrga = orga.iterator();

							System.out.println("inside persist {} ");
							System.out.println("inside persist while {} ");
							Organization oObj = new Organization();
							oObj.setOrga_added_by(user_id);
							oObj.setOrga_approval_status("1");
							oObj.setOrga_enable_status("1");
							oObj.setOrga_created_at(new Date());
							oObj.setOrga_name(entityUpdates.get("orga_name"));
							oObj.setOrga_parent_id(Integer.parseInt(entityUpdates.get("orga_parent_id")));
							entityDao.persist(oObj);
							System.out.println(oObj.getOrga_name());
							System.out.println(oObj.getOrga_parent_id());
						}
					}

					objForAppend.put("responseMessage", "Success");
					return objForAppend.toJSONString();
				} else {

					objForAppend.put("responseMessage", "File type mismatch");
					return objForAppend.toJSONString();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			objForAppend.put("responseMessage", "Failed");
			return objForAppend.toJSONString();

		}
		return null;
	}

}
