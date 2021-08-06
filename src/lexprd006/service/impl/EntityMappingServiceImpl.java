package lexprd006.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
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
import lexprd006.dao.EntityMappingDao;
import lexprd006.dao.FunctionDao;
import lexprd006.dao.UnitDao;
import lexprd006.domain.EntityMapping;
import lexprd006.service.EntityMappingService;
/*
 * Author: Mahesh Kharote
 * Date: 25/10/2016
 * Purpose: Service Impl for Functions
 * 
 * 
 * 
 * */

@Service(value = "entityMappingService")
public class EntityMappingServiceImpl implements EntityMappingService {

	@Autowired
	EntityMappingDao entityMappingDao;

	@Autowired
	FunctionDao functionDao;

	@Autowired
	EntityDao entityDao;

	@Autowired
	UnitDao unitDao;

	@Autowired
	HttpSession session;

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all Functions rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String listEntityMappings() {
		JSONArray dataForSend = new JSONArray();
		try {

			List<Object> entityMappingList = entityMappingDao.getJoinedAll(session);

			Iterator<Object> iterator = entityMappingList.iterator();
			while (iterator.hasNext()) {
				Object[] obj = (Object[]) iterator.next();
				JSONObject objForAppend = new JSONObject();

				objForAppend.put("enti_id", obj[0].toString());
				objForAppend.put("enti_orga_name", obj[1].toString());
				objForAppend.put("enti_loca_name", obj[2].toString());
				objForAppend.put("enti_dept_name", obj[3].toString());
				objForAppend.put("enti_approval_status", obj[4].toString());
				objForAppend.put("enti_enable_status", obj[5].toString());

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
	// Method Purpose: Get all Functions rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String saveEntityMapping(String jsonString) {
		JSONObject objForAppend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int enti_orga_id = Integer.parseInt(jsonObj.get("enti_orga_id").toString());
			int enti_loca_id = Integer.parseInt(jsonObj.get("enti_loca_id").toString());
			/*------------Code for send data to DAO will be here-----------------------*/
			/*------------Iterator JSONArray-----------------------*/
			JSONArray department_list = (JSONArray) jsonObj.get("dept_list");

			for (int i = 0; i < department_list.size(); i++) {
				JSONObject dept_list = (JSONObject) department_list.get(i);
				int enti_dept_id = Integer.parseInt(dept_list.get("enti_dept_id").toString());

				EntityMapping entityMapping = new EntityMapping();
				entityMapping.setEnti_added_by(1);
				entityMapping.setEnti_approval_status("1");
				entityMapping.setEnti_created_at(new Date());
				entityMapping.setEnti_dept_id(enti_dept_id);
				entityMapping.setEnti_enable_status("1");
				entityMapping.setEnti_loca_id(enti_loca_id);
				entityMapping.setEnti_orga_id(enti_orga_id);

				entityMappingDao.persist(entityMapping);

			}

			/*------------Iterator JSONArray Ends here-----------------------*/

			/*------------Code for send data to DAO Ends here-----------------------*/

			/*------------This is test data-----------------------*/
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
	public String editEntityMapping(String jsonString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateEntityMapping(String jsonString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deleteEntityMapping(String jsonString) {
		// TODO Auto-generated method stub
		return null;
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Get all Functions rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String getMappedDepartments(String jsonString) {
		JSONObject dataForSend = new JSONObject();

		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int enti_orga_id = Integer.parseInt(jsonObj.get("enti_orga_id").toString());
			int enti_loca_id = Integer.parseInt(jsonObj.get("enti_loca_id").toString());
			/*------------Code for send data to DAO will be here-----------------------*/
			/*------------Iterator JSONArray-----------------------*/
			List<Object> departments_mapped = functionDao.getMappedDepartments(enti_orga_id, enti_loca_id);
			List<Object> departments_unmapped = functionDao.getUnMappedDepartments(enti_orga_id, enti_loca_id);

			/*------------Fetching all mapped Departments------------------------------*/

			JSONArray mappedDepartments = new JSONArray();

			Iterator<Object> iterator = departments_mapped.iterator();
			while (iterator.hasNext()) {
				Object[] object = (Object[]) iterator.next();

				JSONObject objForAppend = new JSONObject();
				objForAppend.put("dept_id", object[0].toString());
				objForAppend.put("dept_name", object[1].toString());

				mappedDepartments.add(objForAppend);
			}

			dataForSend.put("mappedDepartments", mappedDepartments);
			/*------------Fetching all mapped Departments ends here---------------------*/

			/*------------Fetching all unmapped Departments-----------------------------*/
			JSONArray unmappedDepartments = new JSONArray();
			Iterator<Object> itr = departments_unmapped.iterator();
			while (itr.hasNext()) {
				Object[] object_unmapped = (Object[]) itr.next();

				JSONObject objForAppend_unmapped = new JSONObject();
				objForAppend_unmapped.put("dept_id", object_unmapped[0].toString());
				objForAppend_unmapped.put("dept_name", object_unmapped[1].toString());

				unmappedDepartments.add(objForAppend_unmapped);
			}

			dataForSend.put("unmappedDepartments", unmappedDepartments);
			/*------------Fetching all unmapped Departments ends here--------------------*/

			/*------------Iterator JSONArray Ends here-----------------------*/

			/*------------Code for send data to DAO Ends here-----------------------*/

			/*------------This is test data-----------------------*/
			/*------------This is test data ends here-----------------------*/

			return dataForSend.toJSONString();
		} catch (Exception e) {
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("errorMessage", "Failed");
			e.printStackTrace();
			return objForAppend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	// Method Purpose: Save Function rest Call
	@SuppressWarnings("unchecked")
	@Override
	public String getMappedUnitForEntity(String jsonString) {
		JSONObject dataForSend = new JSONObject();

		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			int enti_orga_id = Integer.parseInt(jsonObj.get("enti_orga_id").toString());
			/*------------Code for send data to DAO will be here-----------------------*/
			/*------------Iterator JSONArray-----------------------*/
			List<Object> units_mapped = entityMappingDao.getMappedUnits(enti_orga_id);

			/*------------Fetching all mapped Units------------------------------*/

			JSONArray mappedUnits = new JSONArray();

			Iterator<Object> iterator = units_mapped.iterator();
			while (iterator.hasNext()) {
				Object[] object = (Object[]) iterator.next();

				JSONObject objForAppend = new JSONObject();
				objForAppend.put("unit_id", object[0].toString());
				objForAppend.put("unit_name", object[1].toString());
				objForAppend.put("orga_id", enti_orga_id);

				mappedUnits.add(objForAppend);
			}

			dataForSend.put("mappedUnits", mappedUnits);
			/*------------Fetching all mapped Units ends here---------------------*/

			return mappedUnits.toJSONString();
		} catch (Exception e) {
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("errorMessage", "Failed");
			e.printStackTrace();
			return objForAppend.toJSONString();
		}
	}

	// Method Written By: Mahesh Kharote(25/10/2016)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	// Method Purpose: Save Function rest Call
	@Override
	public String getAllMappings(String jsonString) {
		JSONObject dataForSend = new JSONObject();
		try {
			List<Object> orga = entityMappingDao.getAllMapping();

			Iterator<Object> itr = orga.iterator();
			List<Integer> checkEnti = new ArrayList<>();
			List<List> checkUnit = new ArrayList<>();
			List<List> checkFunc = new ArrayList<>();

			JSONArray entityArray = new JSONArray();
			JSONArray unitArray = new JSONArray();
			JSONArray funcArray = new JSONArray();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject dataForEntity = new JSONObject();

				if (!checkEnti.contains(object[1])) {
					dataForEntity.put("orga_id", object[1]);
					dataForEntity.put("orga_name", object[2]);

					checkEnti.add(Integer.parseInt(object[1].toString()));
					entityArray.add(dataForEntity);
				}

				List<Integer> listForAddingCheckUnit = new ArrayList<>();
				listForAddingCheckUnit.add(Integer.parseInt(object[1].toString()));
				listForAddingCheckUnit.add(Integer.parseInt(object[3].toString()));
				JSONObject dataForUnit = new JSONObject();
				if (!checkUnit.contains(listForAddingCheckUnit)) {
					dataForUnit.put("loca_id", object[3]);
					dataForUnit.put("loca_name", object[4]);
					dataForUnit.put("orga_id", object[1]);

					checkUnit.add(listForAddingCheckUnit);
					unitArray.add(dataForUnit);
				}

				List<Integer> listForAddingCheckFunc = new ArrayList<>();
				listForAddingCheckFunc.add(Integer.parseInt(object[1].toString()));
				listForAddingCheckFunc.add(Integer.parseInt(object[3].toString()));
				listForAddingCheckFunc.add(Integer.parseInt(object[5].toString()));
				JSONObject dataForFunc = new JSONObject();
				if (!checkFunc.contains(listForAddingCheckFunc)) {
					dataForFunc.put("dept_id", object[5]);
					dataForFunc.put("dept_name", object[6]);
					dataForFunc.put("orga_id", object[1]);
					dataForFunc.put("loca_id", object[3]);

					checkFunc.add(listForAddingCheckFunc);
					funcArray.add(dataForFunc);
				}

			}

			dataForSend.put("Entity", entityArray);
			dataForSend.put("Unit", unitArray);
			dataForSend.put("Function", funcArray);

			return dataForSend.toJSONString();
		} catch (Exception e) {
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("errorMessage", "Failed");
			e.printStackTrace();
			return objForAppend.toJSONString();
		}
	}

	@Override
	public String getAllMappingsReport(String jsonString, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		try {
			List<Object> orga = entityMappingDao
					.getAllMappingsReport(Integer.parseInt(session.getAttribute("sess_user_id").toString()));

			Iterator<Object> itr = orga.iterator();
			List<Integer> checkEnti = new ArrayList<>();
			List<List> checkUnit = new ArrayList<>();
			List<List> checkFunc = new ArrayList<>();

			JSONArray entityArray = new JSONArray();
			JSONArray unitArray = new JSONArray();
			JSONArray funcArray = new JSONArray();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				JSONObject dataForEntity = new JSONObject();

				if (!checkEnti.contains(object[1])) {
					dataForEntity.put("orga_id", object[1]);
					dataForEntity.put("orga_name", object[2]);

					checkEnti.add(Integer.parseInt(object[1].toString()));
					entityArray.add(dataForEntity);
				}

				List<Integer> listForAddingCheckUnit = new ArrayList<>();
				listForAddingCheckUnit.add(Integer.parseInt(object[1].toString()));
				listForAddingCheckUnit.add(Integer.parseInt(object[3].toString()));
				JSONObject dataForUnit = new JSONObject();
				if (!checkUnit.contains(listForAddingCheckUnit)) {
					dataForUnit.put("loca_id", object[3]);
					dataForUnit.put("loca_name", object[4]);
					dataForUnit.put("orga_id", object[1]);

					checkUnit.add(listForAddingCheckUnit);
					unitArray.add(dataForUnit);
				}

				List<Integer> listForAddingCheckFunc = new ArrayList<>();
				listForAddingCheckFunc.add(Integer.parseInt(object[1].toString()));
				listForAddingCheckFunc.add(Integer.parseInt(object[3].toString()));
				listForAddingCheckFunc.add(Integer.parseInt(object[5].toString()));
				JSONObject dataForFunc = new JSONObject();
				if (!checkFunc.contains(listForAddingCheckFunc)) {
					dataForFunc.put("dept_id", object[5]);
					dataForFunc.put("dept_name", object[6]);
					dataForFunc.put("orga_id", object[1]);
					dataForFunc.put("loca_id", object[3]);

					checkFunc.add(listForAddingCheckFunc);
					funcArray.add(dataForFunc);
				}

			}

			dataForSend.put("Entity", entityArray);
			dataForSend.put("Unit", unitArray);
			dataForSend.put("Function", funcArray);

			return dataForSend.toJSONString();
		} catch (Exception e) {
			JSONObject objForAppend = new JSONObject();
			objForAppend.put("errorMessage", "Failed");
			e.printStackTrace();
			return objForAppend.toJSONString();
		}
	}

	@Override
	public String importEntityMapping(MultipartFile entity_mapping_list, String jsonString, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		String user_name = session.getAttribute("sess_user_full_name").toString();
		int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
		JSONObject objForAppend1 = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			if (!entity_mapping_list.isEmpty()) {
				JSONArray neglectedTask = new JSONArray();
				JSONArray addedTask = new JSONArray();

				String name = jsonObj.get("name").toString();
				byte[] bytes = entity_mapping_list.getBytes();
				String fileExtension = FilenameUtils.getExtension(entity_mapping_list.getOriginalFilename());
				if (fileExtension.equalsIgnoreCase("csv")) {

					// Create Temp File
					File temp = File.createTempFile(entity_mapping_list.getName(), ".csv");

					String absolutePath = temp.getAbsolutePath();
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(absolutePath));
					stream.write(bytes);
					stream.close();
					CsvReader legalUpdates = new CsvReader(absolutePath);

					legalUpdates.readHeaders();

					int i = 0;

					while (legalUpdates.readRecord()) {
						i++;
						System.out.println("this is record no: " + i);
						System.out.println("this is task id:" + legalUpdates.get("user_username"));

						JSONObject objForAppend = new JSONObject();
						String entity_name = legalUpdates.get("entity_name");
						String unit_name = legalUpdates.get("unit_name");
						String function_name = legalUpdates.get("function_name");

						System.out.println("entity_name:" + entity_name);
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

						// System.out.println("orga_id:" + org.getOrga_id() + "loca_id:" +
						// loca.getLoca_id() + "dept_id:"
						// + dept.getDept_id());
						/*
						 * int entity_id = Integer.parseInt(legalUpdates.get("entity_id")); int unit_id
						 * = Integer.parseInt(legalUpdates.get("unit_id")); int function_id =
						 * Integer.parseInt(legalUpdates.get("function_id"));
						 */

						EntityMapping entityMapping = entityMappingDao.getTasksForUserUpdate(orgaId, unitId, deptId);
						if (entityMapping != null) {

							entityMapping.setEnti_orga_id(orgaId);
							entityMapping.setEnti_loca_id(unitId);
							entityMapping.setEnti_dept_id(deptId);
							entityMapping.setEnti_created_at(new Date());
							entityMapping.setEnti_added_by(1);
							entityMapping.setEnti_enable_status("1");
							entityMapping.setEnti_approval_status("1");

							entityMappingDao.updateEntityMapping(entityMapping);
							objForAppend.put("responseMessage", "Success");
							addedTask.add(objForAppend);
							objForAppend.put("name", name);

						} else {

							EntityMapping enMapping = new EntityMapping();
							enMapping.setEnti_orga_id(orgaId);
							enMapping.setEnti_loca_id(unitId);
							enMapping.setEnti_dept_id(deptId);
							enMapping.setEnti_created_at(new Date());
							enMapping.setEnti_added_by(1);
							enMapping.setEnti_enable_status("1");
							enMapping.setEnti_approval_status("1");

							entityMappingDao.persist(enMapping);
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

}
