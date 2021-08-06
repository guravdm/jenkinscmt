package lexprd006.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lexprd006.dao.RepositoryDao;
import lexprd006.dao.TasksConfigurationDao;
import lexprd006.dao.UploadedDocumentsDao;
import lexprd006.domain.TaskTransactional;
import lexprd006.domain.UploadedDocuments;
import lexprd006.service.RepositoryService;
import lexprd006.service.UtilitiesService;

@Service(value = "repositoryService")
public class RepositoryServiceImpl implements RepositoryService {

	public final SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public final SimpleDateFormat sdfOut = new SimpleDateFormat("dd-MM-yyyy");
	public final SimpleDateFormat sdfOutDisplay = new SimpleDateFormat("dd-MM-yyyy");
	private @Value("#{config['project_name'] ?: 'null'}") String project_name;

	@Autowired
	RepositoryDao repositoryDao;

	@Autowired
	UploadedDocumentsDao uploadedDocumentsDao;

	@Autowired
	UtilitiesService utilitiesService;

	@Autowired
	TasksConfigurationDao taskConfigurationDao;

	@Override
	public String getExportDataById(String jsonString, HttpSession session, HttpServletResponse response) {

		JSONObject dataForSend = new JSONObject();
		JsonNode rootNode = null;
		final ObjectMapper mapper = new ObjectMapper();
		String dumpFile = null;
		int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
		try {
			rootNode = mapper.readTree(jsonString);
			byte data[] = new byte[1024];
			int byteContent;
			int orga_id = rootNode.path("orga_id").asInt();
			int loca_id = rootNode.path("loca_id").asInt();
			int dept_id = rootNode.path("dept_id").asInt();
			System.out.println("orga_id:" + orga_id + "loca_id:" + loca_id + "dept_id:" + dept_id);
			List<Object> allTasks = repositoryDao.getAllTaskForRepository(
					Integer.parseInt(session.getAttribute("sess_user_id").toString()),
					Integer.parseInt(session.getAttribute("sess_role_id").toString()), jsonString);
			Iterator<Object> itr = allTasks.iterator();

			JSONArray dataForAppend = new JSONArray();
			HSSFWorkbook workbook = new HSSFWorkbook();
			System.out.println("size: " + allTasks.size());
			int i = 1;
			HSSFSheet sheet = workbook.createSheet("dump");
			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell((short) 0).setCellValue("Client Task ID");
			rowhead.createCell((short) 1).setCellValue("Legislation");
			rowhead.createCell((short) 2).setCellValue("Rule");
			rowhead.createCell((short) 3).setCellValue("Executor Date");
			rowhead.createCell((short) 4).setCellValue("Evaluator Date");
			rowhead.createCell((short) 5).setCellValue("Function Head Date");
			rowhead.createCell((short) 6).setCellValue("Unit Head Date");
			rowhead.createCell((short) 7).setCellValue("Legal Due Date");
			rowhead.createCell((short) 8).setCellValue("Who");
			rowhead.createCell((short) 9).setCellValue("When");
			rowhead.createCell((short) 10).setCellValue("Activity");
			rowhead.createCell((short) 11).setCellValue("Procedure");
			rowhead.createCell((short) 12).setCellValue("More Info");
			rowhead.createCell((short) 13).setCellValue("Impact");
			rowhead.createCell((short) 14).setCellValue("Frequency");
			rowhead.createCell((short) 15).setCellValue("Reference");
			rowhead.createCell((short) 16).setCellValue("Category Of Law");
			rowhead.createCell((short) 17).setCellValue("Type Of task");
			rowhead.createCell((short) 18).setCellValue("Event");
			rowhead.createCell((short) 19).setCellValue("Sub Event");
			rowhead.createCell((short) 20).setCellValue("Status");
			rowhead.createCell((short) 21).setCellValue("Entity");
			rowhead.createCell((short) 22).setCellValue("Unit");
			rowhead.createCell((short) 23).setCellValue("Function");
			rowhead.createCell((short) 24).setCellValue("Executor");
			rowhead.createCell((short) 25).setCellValue("Evaluator");
			rowhead.createCell((short) 26).setCellValue("Function Head");
			rowhead.createCell((short) 27).setCellValue("Buffer Days");
			rowhead.createCell((short) 28).setCellValue("Alert Days");
			rowhead.createCell((short) 29).setCellValue("Prohibitive");
			rowhead.createCell((short) 30).setCellValue("Impact On Unit");
			rowhead.createCell((short) 31).setCellValue("Impact On Organization");
			rowhead.createCell((short) 32).setCellValue("Implication");
			rowhead.createCell((short) 33).setCellValue("Exemption Criteria");
			rowhead.createCell((short) 34).setCellValue("Form No");
			rowhead.createCell((short) 35).setCellValue("Effective Date");
			rowhead.createCell((short) 36).setCellValue("Country");
			rowhead.createCell((short) 37).setCellValue("State");
			rowhead.createCell((short) 38).setCellValue("Fine Amount");
			rowhead.createCell((short) 39).setCellValue("Imprisonment Duration");
			rowhead.createCell((short) 40).setCellValue("Imprisonment Applies To");
			rowhead.createCell((short) 41).setCellValue("Task Level");
			rowhead.createCell((short) 42).setCellValue("Interlinkage");
			rowhead.createCell((short) 43).setCellValue("Linked Task Id");
			rowhead.createCell((short) 44).setCellValue("Specific Due Date");
			rowhead.createCell((short) 45).setCellValue("Subsequent Amount per day");
			rowhead.createCell((short) 46).setCellValue("Weblinks");
			rowhead.createCell((short) 47).setCellValue("Statutory Authority");

			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				HSSFRow row = sheet.createRow((short) i);
				row.createCell((short) 0).setCellValue(object[0].toString());
				row.createCell((short) 1).setCellValue(object[1].toString());
				row.createCell((short) 2).setCellValue(object[2].toString());

				if (object[3] != null)
					row.createCell((short) 3).setCellValue(sdfOut.format(sdfIn.parse(object[3].toString())));

				// row.createCell((short) 3).setCellValue(object[3].toString());

				if (object[53] != null)
					row.createCell((short) 4).setCellValue(sdfOut.format(sdfIn.parse(object[53].toString())));

				if (object[54] != null)
					row.createCell((short) 5).setCellValue(sdfOut.format(sdfIn.parse(object[54].toString())));

				// row.createCell((short) 4).setCellValue(object[53].toString());
				// row.createCell((short) 5).setCellValue(object[54].toString());

				if (object[57] != null)
					row.createCell((short) 6).setCellValue(sdfOut.format(sdfIn.parse(object[57].toString())));

				// row.createCell((short) 6).setCellValue(object[57].toString());
				if (object[4] != null)
					row.createCell((short) 7).setCellValue(sdfOut.format(sdfIn.parse(object[4].toString())));

				// row.createCell((short) 9).setCellValue(object[4].toString());

				row.createCell((short) 8).setCellValue(object[5].toString());
				row.createCell((short) 9).setCellValue(object[6].toString());
				row.createCell((short) 10).setCellValue(object[7].toString());
				row.createCell((short) 11).setCellValue(object[8].toString());
				row.createCell((short) 12).setCellValue(object[47].toString());
				row.createCell((short) 13).setCellValue(object[9].toString());
				row.createCell((short) 14).setCellValue(object[10].toString());
				row.createCell((short) 15).setCellValue(object[12].toString());
				row.createCell((short) 16).setCellValue(object[13].toString());
				row.createCell((short) 17).setCellValue(object[14].toString());
				row.createCell((short) 18).setCellValue(object[16].toString());
				row.createCell((short) 19).setCellValue(object[17].toString());

				if (object[18] != null)
					row.createCell((short) 20).setCellValue(object[18].toString());

				row.createCell((short) 21).setCellValue(object[20].toString());
				row.createCell((short) 22).setCellValue(object[22].toString());
				row.createCell((short) 23).setCellValue(object[24].toString());
				row.createCell((short) 24).setCellValue(object[26].toString() + " " + object[27].toString());
				row.createCell((short) 25).setCellValue(object[29].toString() + " " + object[30].toString());
				row.createCell((short) 26).setCellValue(object[55].toString() + " " + object[56].toString());

				if (object[58] != null)
					row.createCell((short) 27).setCellValue(object[58].toString());

				if (object[59] != null)
					row.createCell((short) 28).setCellValue(object[59].toString());

				row.createCell((short) 29).setCellValue(object[15].toString());
				row.createCell((short) 30).setCellValue(object[41].toString());
				row.createCell((short) 31).setCellValue(object[40].toString());
				row.createCell((short) 32).setCellValue(object[42].toString());
				row.createCell((short) 33).setCellValue(object[37].toString());
				row.createCell((short) 34).setCellValue(object[39].toString());

				if (object[36] != null)
					row.createCell((short) 35).setCellValue(sdfOut.format(sdfIn.parse(object[36].toString())));

				row.createCell((short) 36).setCellValue(object[34].toString());
				row.createCell((short) 37).setCellValue(object[35].toString());
				row.createCell((short) 38).setCellValue(object[38].toString());
				row.createCell((short) 39).setCellValue(object[43].toString());
				row.createCell((short) 40).setCellValue(object[44].toString());
				row.createCell((short) 41).setCellValue(object[45].toString());
				row.createCell((short) 42).setCellValue(object[48].toString());
				row.createCell((short) 43).setCellValue(object[46].toString());
				row.createCell((short) 44).setCellValue(object[49].toString());
				row.createCell((short) 45).setCellValue(object[52].toString());
				row.createCell((short) 46).setCellValue(object[50].toString());
				row.createCell((short) 47).setCellValue(object[51].toString());
				i++;

				/*
				 * dumpFile = "E:/Drilled_Report.xlsx";
				 * 
				 * FileOutputStream fileOut = new FileOutputStream(dumpFile);
				 * workbook.write(fileOut); fileOut.close();
				 */
			}

			File dir1 = new File(
					"C:" + File.separator + "CMS" + File.separator + project_name + File.separator + "Dump");

			if (!dir1.exists())
				dir1.mkdirs();

			DateFormat df = new SimpleDateFormat("dd-MM-yy_HH-mm-ss");
			Date dateobj = new Date();
			String test = df.format(dateobj);

			Object generatedExcelFile = "Drilled_Report" + test + ".csv";
			File newExcelFile = new File(dir1.getPath() + File.separator + generatedExcelFile);

			if (!newExcelFile.exists()) {
				newExcelFile.createNewFile();
			}

			FileOutputStream fileOut = new FileOutputStream(newExcelFile);
			workbook.write(fileOut);
			fileOut.close();

			/*
			 * ExportReportDocuments list = new ExportReportDocuments();
			 * list.setDoc_filepath(dumpFile); list.setDoc_filename("dump.csv");
			 * list.setUser_id(user_id); repositoryDao.saveExportDocuments(list);
			 */

			dataForSend.put("responseMessage", "Success");

			return dataForSend.toJSONString();
		} catch (Exception e) {
			dataForSend.put("responseMessage", "Failed");
			e.printStackTrace();
			return dataForSend.toJSONString();
		}

	}

	@Override
	public String updateTasksCompletion(ArrayList<MultipartFile> ttrn_proof_of_compliance, String jsonString,
			HttpSession session) {
		JSONObject objForSend = new JSONObject();
		String ttrn_reason_for_non_compliance = "";
		try {

			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			String ttrn_performer_comments = jsonObj.get("ttrn_performer_comments").toString();
			Date ttrn_completed_date = sdfOutDisplay.parse(jsonObj.get("ttrn_completed_date").toString());

			if (jsonObj.get("ttrn_reason_for_non_compliance") == null) {
				ttrn_reason_for_non_compliance = "";
			} else {
				ttrn_reason_for_non_compliance = jsonObj.get("ttrn_reason_for_non_compliance").toString();
			}
			String ttrn_event_not_occure = jsonObj.get("ttrn_event_not_occure").toString();
			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			int user_role = Integer.parseInt(session.getAttribute("sess_role_id").toString());
			int mail_send_flag = 0;

			ArrayList<TaskTransactional> taskTransactionals = new ArrayList<>();

			JSONArray arrayToIterate = (JSONArray) jsonObj.get("ttrn_ids");
			for (int i = 0; i < arrayToIterate.size(); i++) {

				JSONObject configured_tasks_obj = (JSONObject) arrayToIterate.get(i);
				int ttrn_id = Integer.parseInt(configured_tasks_obj.get("ttrn_id").toString());

				String originalFileName = null;
				String generatedFileName = null;
				// int lastGeneratedValue =
				// uploadedDocumentsDao.getLastGeneratedValueByTtrnId(ttrn_id);
				UploadedDocuments document = uploadedDocumentsDao.getDocumentById(ttrn_id);

				if (document != null) {
					for (int i1 = 0; i1 < ttrn_proof_of_compliance.size(); i1++) {
						MultipartFile file1 = ttrn_proof_of_compliance.get(i1);
						if (file1.getSize() > 0) {

							Date cur_date = new Date();

							Calendar cal = Calendar.getInstance();
							cal.setTime(cur_date);
							int year = cal.get(Calendar.YEAR);
							int month = cal.get(Calendar.MONTH);
							int day = cal.get(Calendar.DAY_OF_MONTH);
							String dir_month_name = day + "-" + (month + 1) + "-" + year;

							File dir = new File("C:" + File.separator + "CMS" + File.separator + "documents"
									+ File.separator + "proofOfCompliance" + File.separator + project_name
									+ File.separator + dir_month_name);
							if (!dir.exists())
								dir.mkdirs();

							// lastGeneratedValue++;
							originalFileName = file1.getOriginalFilename();
							generatedFileName = "uploadedProof" + ttrn_id + "_"
									+ file1.getOriginalFilename().split("\\.")[1];
							File newFile = new File(dir.getPath() + File.separator + generatedFileName);
							if (!newFile.exists()) {
								newFile.createNewFile();
							}

							OutputStream outputStream = new FileOutputStream(newFile);

							outputStream.write(file1.getBytes());

							String algo = "DES/ECB/PKCS5Padding";
							utilitiesService.encrypt(algo, newFile.getPath());

							// UploadedDocuments document = null;
							document.setUdoc_ttrn_id(ttrn_id);
							document.setUdoc_original_file_name(originalFileName);
							document.setUdoc_filename(dir + File.separator + generatedFileName);
							document.setUdoc_last_generated_value_for_filename_for_ttrn_id(
									document.getUdoc_last_generated_value_for_filename_for_ttrn_id());
							uploadedDocumentsDao.updateDocuments(document);

							outputStream.flush();
							outputStream.close();

							Path path = Paths.get(dir + File.separator + generatedFileName);
							try {
								Files.delete(path);
							} catch (IOException e) {
								// deleting file failed
								e.printStackTrace();
								System.out.println(e.getMessage());
							}
						}
					}
				}else {
					for (int i1 = 0; i1 < ttrn_proof_of_compliance.size(); i1++) {
						MultipartFile file1 = ttrn_proof_of_compliance.get(i1);
						if (file1.getSize() > 0) {

							Date cur_date = new Date();

							Calendar cal = Calendar.getInstance();
							cal.setTime(cur_date);
							int year = cal.get(Calendar.YEAR);
							int month = cal.get(Calendar.MONTH);
							int day = cal.get(Calendar.DAY_OF_MONTH);
							String dir_month_name = day + "-" + (month + 1) + "-" + year;

							File dir = new File("C:" + File.separator + "CMS" + File.separator + "documents"
									+ File.separator + "proofOfCompliance" + File.separator + project_name
									+ File.separator + dir_month_name);
							if (!dir.exists())
								dir.mkdirs();

							// lastGeneratedValue++;
							originalFileName = file1.getOriginalFilename();
							generatedFileName = "uploadedProof" + ttrn_id + "_"
									+ file1.getOriginalFilename().split("\\.")[1];
							File newFile = new File(dir.getPath() + File.separator + generatedFileName);
							if (!newFile.exists()) {
								newFile.createNewFile();
							}

							OutputStream outputStream = new FileOutputStream(newFile);

							outputStream.write(file1.getBytes());

							String algo = "DES/ECB/PKCS5Padding";
							utilitiesService.encrypt(algo, newFile.getPath());

							UploadedDocuments documents = new UploadedDocuments();
							documents.setUdoc_ttrn_id(ttrn_id);
							documents.setUdoc_original_file_name(originalFileName);
							documents.setUdoc_filename(dir + File.separator + generatedFileName);
							documents.setUdoc_last_generated_value_for_filename_for_ttrn_id(
									document.getUdoc_last_generated_value_for_filename_for_ttrn_id());
							uploadedDocumentsDao.saveDocuments(documents);

							outputStream.flush();
							outputStream.close();

							Path path = Paths.get(dir + File.separator + generatedFileName);
							try {
								Files.delete(path);
							} catch (IOException e) {
								// deleting file failed
								e.printStackTrace();
								System.out.println(e.getMessage());
							}
						}
					}
				}

				TaskTransactional taskTransactional = taskConfigurationDao.getTasksForCompletion(ttrn_id);

				if (taskTransactional.getTtrn_status().equals("Active")
						|| taskTransactional.getTtrn_status().equals("Re_Opened"))
					mail_send_flag = 1;

				taskTransactional.setTtrn_completed_date(ttrn_completed_date);
				taskTransactional.setTtrn_performer_comments(ttrn_performer_comments);
				if (!ttrn_reason_for_non_compliance.equals("")) {
					taskTransactional.setTtrn_reason_for_non_compliance(ttrn_reason_for_non_compliance);
				}
				int noOfBackDaysAllowed = taskTransactional.getTtrn_no_of_back_days_allowed();
				if (noOfBackDaysAllowed > 0) {
					Date currentDate = new Date();
					if (currentDate.after(taskTransactional.getTtrn_legal_due_date())) {
						long diff = currentDate.getTime() - taskTransactional.getTtrn_legal_due_date().getTime();
						int differenceDays = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

						if ((noOfBackDaysAllowed - differenceDays) >= 0) {
							taskTransactional.setTtrn_submitted_date(ttrn_completed_date);
						} else {
							taskTransactional.setTtrn_submitted_date(new Date());
						}

					} else {
						taskTransactional.setTtrn_submitted_date(new Date());
					}

				} else {
					taskTransactional.setTtrn_submitted_date(new Date());
				}

				if (ttrn_event_not_occure.equals("Yes")) {
					taskTransactional.setTtrn_status("Event_Not_Occured");
					// taskTransactional.setTtrn_completed_date(ttrn_completed_date);
					taskTransactional.setTtrn_performer_comments("");
					taskTransactional.setTtrn_reason_for_non_compliance("");

				} else {
					System.out.println(" ROLE " + user_role + " Aprover or not "
							+ taskTransactional.getTtrn_allow_approver_reopening());
					if (taskTransactional.getTtrn_allow_approver_reopening() != null
							&& Integer.parseInt(taskTransactional.getTtrn_allow_approver_reopening()) == 1) {
						taskTransactional.setTtrn_status("Partially_Completed");
					} else {
						taskTransactional.setTtrn_status("Completed");
					}

				}

				// taskTransactional.setTtrn_status("Completed");
				taskTransactional.setTtrn_task_completed_by(user_id);// Change this to session user id

				taskTransactionals.add(taskTransactional);

			}
			taskConfigurationDao.saveTaskCompletion(taskTransactionals);
			if (mail_send_flag == 1) // IF status is active then only mail send while updating task mail mail should
										// not be sent
				utilitiesService.sendTaskCompletionMailToEvaluator(taskTransactionals, "MainTask");// Task completion
																									// mail to evaluator

			objForSend.put("responseMessage", "Success");
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("responseMessage", "Failed");
			return objForSend.toJSONString();
		}
	}

}
