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

import lexprd006.dao.UnitDao;
import lexprd006.domain.Location;
import lexprd006.domain.Organization;
import lexprd006.service.UnitService;

@Service(value = "unitService")
public class UnitServiceImpl implements UnitService {

	@Autowired
	UnitDao unitDao;

	@Autowired
	HttpSession session;

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all Functions rest Call
	// Returns the list of unit where user has access
	@SuppressWarnings("unchecked")
	@Override
	public String listUnits(String requestFromMethod) {
		JSONArray dataForSend = new JSONArray();
		try {

			session.setAttribute("requestFromMethod", requestFromMethod);
			List<Object> allUnit = unitDao.getAllUnit(session);

			Iterator<Object> iterator = allUnit.iterator();
			while (iterator.hasNext()) {
				Object[] object = (Object[]) iterator.next();
				// System.out.println(object[1].toString()+" "+object[2].toString());

				JSONObject objForAppend = new JSONObject();
				objForAppend.put("loca_id", object[1].toString());
				objForAppend.put("loca_name", object[2].toString());

				dataForSend.add(objForAppend);
				// System.out.println(object[2].toString());
			}
			// Return all the Units from the database
			/*
			 * List<Location> locations = unitDao.getAll(Location.class);
			 * 
			 * 
			 * Iterator<Location> itr = locations.iterator(); while (itr.hasNext()) {
			 * Location location = (Location) itr.next(); JSONObject objForAppend = new
			 * JSONObject(); objForAppend.put("loca_id", location.getLoca_id());
			 * objForAppend.put("loca_name", location.getLoca_name());
			 * 
			 * dataForSend.add(objForAppend); }
			 */
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
	public String saveUnit(String jsonString) {
		JSONObject objForAppend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String loca_name = jsonObj.get("loca_name").toString();
			/*------------Code for send data to DAO will be here-----------------------*/

			Location location = new Location();
			location.setLoca_name(loca_name);
			location.setLoca_added_by(1);
			location.setLoca_approval_status("1");
			location.setLoca_created_at(new Date());
			location.setLoca_enable_status("1");
			location.setLoca_parent_id(0);
			unitDao.persist(location);

			/*------------Code for send data to DAO Ends here-----------------------*/

			/*------------This is test data-----------------------*/

			objForAppend.put("loca_name", loca_name);
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
	public String editUnit(String jsonString) {
		// TODO Auto-generated method stub
		return null;
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Save Function rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String updateUnit(String jsonString) {
		JSONObject objForAppend = new JSONObject();
		try {

			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			System.out.println("location is : " + jsonObj.get("loca_id"));
			int loca_id = Integer.parseInt(jsonObj.get("loca_id").toString());
			String loca_name = jsonObj.get("loca_name").toString();

			Location location = unitDao.getLocationById(loca_id);
			location.setLoca_name(loca_name);
			unitDao.updateLocation(location);

			objForAppend.put("loca_id", loca_id);
			objForAppend.put("loca_name", loca_name);
			objForAppend.put("responseMessage", "Success");

			return objForAppend.toJSONString();

		} catch (Exception e) {
			objForAppend.put("responseMessage", "Failed");
			e.printStackTrace();
			return objForAppend.toJSONString();

		}
	}

	@Override
	public String deleteUnit(String jsonString) {
		// TODO Auto-generated method stub
		return null;
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Save Function rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String isUnitNameExist(String jsonString) {
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int loca_id = Integer.parseInt(jsonObj.get("loca_id").toString());
			String loca_name = jsonObj.get("loca_name").toString();
			int count = unitDao.isLocaNameExist(loca_id, loca_name);
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

	@Override
	public String importUnitList(MultipartFile unit_update_list, String jsonString, HttpSession session) {
		String user_name = session.getAttribute("sess_user_full_name").toString();
		int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
		JSONObject objForAppend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			if (!unit_update_list.isEmpty()) {
				JSONArray neglectedTask = new JSONArray();
				JSONArray addedTask = new JSONArray();

				String name = jsonObj.get("name").toString();
				byte[] bytes = unit_update_list.getBytes();

				String fileExtension = FilenameUtils.getExtension(unit_update_list.getOriginalFilename());
				if (fileExtension.equalsIgnoreCase("csv")) {

					File temp = File.createTempFile(unit_update_list.getName(), ".csv");

					int i = 0;
					String absolutePath = temp.getAbsolutePath();
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(absolutePath));
					stream.write(bytes);
					stream.close();
					CsvReader unitUpdates = new CsvReader(absolutePath);
					unitUpdates.readHeaders();

					while (unitUpdates.readRecord()) {
						i++;
						List<Location> loca = unitDao.checkNameIfExist(unitUpdates.get("loca_name"));

						if (loca.size() == 0) {

							Location locs = new Location();
							locs.setLoca_added_by(user_id);
							locs.setLoca_enable_status("1");
							locs.setLoca_approval_status("1");
							locs.setLoca_name(unitUpdates.get("loca_name"));
							locs.setLoca_created_at(new Date());
							locs.setLoca_parent_id(unitUpdates.get("loca_parent_id") != null
									|| unitUpdates.get("loca_parent_id") != "0"
											? Integer.parseInt(unitUpdates.get("loca_parent_id"))
											: 0);
							unitDao.persist(locs);
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
