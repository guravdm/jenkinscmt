package lexprd006.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
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

import lexprd006.dao.FunctionDao;
import lexprd006.domain.Department;
import lexprd006.domain.Location;
import lexprd006.service.FunctionService;

/*
 * Author: Mahesh Kharote
 * Date: 25/10/2016
 * Purpose: Service Impl for Functions
 * 
 * 
 * 
 * */

@Service(value = "functionService")
public class FunctionServiceImpl implements FunctionService {

	@Autowired
	FunctionDao functionDao;

	@Autowired
	HttpSession session;

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all Functions rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String listFunctions() {
		JSONArray dataForSend = new JSONArray();
		try {

			List<Object> allFunction = functionDao.getAllFunction(session);

			Iterator<Object> iterator = allFunction.iterator();

			while (iterator.hasNext()) {
				Object[] objects = (Object[]) iterator.next();

				JSONObject objForAppend = new JSONObject();

				objForAppend.put("dept_id", objects[1].toString());
				objForAppend.put("dept_name", objects[2].toString());

				dataForSend.add(objForAppend);
			}

			// Listing all the function available in database
			/*
			 * List<Department> departments = functionDao.getAll(Department.class);
			 * Iterator<Department> iterator = departments.iterator(); while
			 * (iterator.hasNext()) { Department department = (Department) iterator.next();
			 * JSONObject objForAppend = new JSONObject();
			 * 
			 * objForAppend.put("dept_id", department.getDept_id());
			 * objForAppend.put("dept_name", department.getDept_name());
			 * 
			 * dataForSend.add(objForAppend);
			 * 
			 * }
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
	public String saveFunction(String jsonString) {
		JSONObject objForAppend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String dept_name = jsonObj.get("dept_name").toString();
			/*------------Code for send data to DAO will be here-----------------------*/

			Department department = new Department();
			department.setDept_name(dept_name);
			department.setDept_added_by(1);
			department.setDept_approval_status("1");
			department.setDept_enable_status("1");
			department.setDept_parent_id(0);
			department.setDept_created_at(new Date());
			functionDao.persist(department);

			/*------------Code for send data to DAO Ends here-----------------------*/

			/*------------This is test data-----------------------*/
			objForAppend.put("dept_name", dept_name);
			objForAppend.put("responseMessage", "Success");
			/*------------This is test data ends here-----------------------*/

			return objForAppend.toJSONString();
		} catch (Exception e) {
			objForAppend.put("responseMessage", "Failed");
			e.printStackTrace();
			return objForAppend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get Function by Id for Edit Function rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String editFunction(String jsonString) {
		JSONArray dataForSend = new JSONArray();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String dept_id = jsonObj.get("dept_id").toString();
			/*------------Code for send data to DAO will be here-----------------------*/

			/*------------Code for send data to DAO Ends here-----------------------*/

			/*------------This is test data-----------------------*/
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("dept_id", dept_id);
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

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Update Function rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String updateFunction(String jsonString) {
		JSONArray dataForSend = new JSONArray();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int dept_id = Integer.parseInt(jsonObj.get("dept_id").toString());
			String dept_name = jsonObj.get("dept_name").toString();
			/*------------Code for send data to DAO will be here-----------------------*/

			Department department = functionDao.getDepartmentById(dept_id);
			department.setDept_name(dept_name);
			functionDao.updateDepartment(department);

			/*------------Code for send data to DAO Ends here-----------------------*/

			/*------------This is test data-----------------------*/
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("dept_id", dept_id);
			objForAppend.put("dept_name", dept_name);
			/*------------This is test data ends here-----------------------*/
			objForAppend.put("responseMessage", "Success");
			//dataForSend.add(objForAppend);
			return objForAppend.toJSONString();
		} catch (Exception e) {
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("responseMessage", "Failed");
			dataForSend.add(objForAppend);
			e.printStackTrace();
			return dataForSend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Delete Function rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String deleteFunction(String jsonString) {
		JSONArray dataForSend = new JSONArray();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String dept_id = jsonObj.get("dept_id").toString();

			/*------------Code for send data to DAO will be here-----------------------*/

			/*------------Code for send data to DAO Ends here-----------------------*/

			/*------------This is test data-----------------------*/
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("dept_id", dept_id);

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

	// Method Created By: Mahesh Kharote(07/11/2016)
	// Method Purpose: Check if Department name already exists
	@SuppressWarnings("unchecked")
	@Override
	public String isFunctionNameExist(String jsonString) {
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int dept_id = Integer.parseInt(jsonObj.get("dept_id").toString());
			String dept_name = jsonObj.get("dept_name").toString();
			int count = functionDao.isDeptNameExist(dept_id, dept_name);
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
	// Method Purpose: Get Mapped Functions for Entity Mapping
	// Not sure of its use check later
	@SuppressWarnings("unchecked")
	@Override
	public String getMappedFunctionsForEntityMapping(String jsonString) {
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int dept_id = Integer.parseInt(jsonObj.get("dept_id").toString());
			String dept_name = jsonObj.get("dept_name").toString();
			int count = functionDao.isDeptNameExist(dept_id, dept_name);
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
	public String importFunctionList(MultipartFile dept_update_list, String jsonString, HttpSession session) {
		String user_name = session.getAttribute("sess_user_full_name").toString();
		int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
		JSONObject objForAppend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			if (!dept_update_list.isEmpty()) {
				JSONArray neglectedTask = new JSONArray();
				JSONArray addedTask = new JSONArray();

				String name = jsonObj.get("name").toString();
				byte[] bytes = dept_update_list.getBytes();

				String fileExtension = FilenameUtils.getExtension(dept_update_list.getOriginalFilename());
				if (fileExtension.equalsIgnoreCase("csv")) {

					File temp = File.createTempFile(dept_update_list.getName(), ".csv");

					int i = 0;
					String absolutePath = temp.getAbsolutePath();
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(absolutePath));
					stream.write(bytes);
					stream.close();
					CsvReader deptUpdates = new CsvReader(absolutePath);
					deptUpdates.readHeaders();

					while (deptUpdates.readRecord()) {
						i++;
						List<Department> deptList = functionDao.checkNameIfExist(deptUpdates.get("dept_name"));

						if (deptList.size() == 0) {
							Department dept = new Department();
							dept.setDept_added_by(user_id);
							dept.setDept_enable_status("1");
							dept.setDept_approval_status("1");
							dept.setDept_name(deptUpdates.get("dept_name"));
							dept.setDept_created_at(new Date());
							dept.setDept_parent_id(deptUpdates.get("dept_parent_id") != null
									|| deptUpdates.get("dept_parent_id") != "0"
											? Integer.parseInt(deptUpdates.get("dept_parent_id"))
											: 0);
							functionDao.persist(dept);
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
