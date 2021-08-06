package lexprd006.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lexprd006.dao.ShowCauseNoticeDao;
import lexprd006.dao.UsersDao;
import lexprd006.domain.ShowCauseDocuments;
import lexprd006.domain.ShowCauseNotice;
import lexprd006.domain.ShowCauseNoticeActionItem;
import lexprd006.domain.User;
import lexprd006.service.ShowCauseNoticeService;
import lexprd006.service.UtilitiesService;

/*
 * Author: Harshad Padole
 * Date: 09/05/2017
 * 
 * */

@Service(value = "showCauseNoticeService")
public class ShowCauseNoticeServiceImpl implements ShowCauseNoticeService {

	public final SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd");
	public final SimpleDateFormat sdfOut = new SimpleDateFormat("dd-MM-yyyy");
	private @Value("#{config['mail_user_name'] ?: 'null'}") String username;
	private @Value("#{config['mail_password'] ?: 'null'}") String password;
	private @Value("#{config['mail_smtp_host'] ?: 'null'}") String hostName;
	private @Value("#{config['mail_smtp_port'] ?: 'null'}") String portNo;
	private @Value("#{config['mail_from'] ?: 'null'}") String mailFrom;
	private @Value("#{config['project_name'] ?: 'null'}") String projectName;
	private @Value("#{config['project_url'] ?: 'null'}") String url;

	@Autowired
	ShowCauseNoticeDao showCauseNoticeDao;

	@Autowired
	UtilitiesService utilitiesService;

	@Autowired
	UsersDao usersDao;

	// Method Written By: Harshad Padole(09-05-2017)
	// Method Purpose: Save show cause notice
	@SuppressWarnings("unchecked")
	@Override
	public String saveShowCauseNotice(String json, ArrayList<MultipartFile> show_cause_doc, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);
			int scau_orga_id = Integer.parseInt(jsonObject.get("scau_orga_id").toString());
			int scau_loca_id = Integer.parseInt(jsonObject.get("scau_loca_id").toString());
			int scau_dept_id = Integer.parseInt(jsonObject.get("scau_dept_id").toString());
			String scau_ralated_to = jsonObject.get("scau_ralated_to").toString();
			Date scau_notice_date = null;
			Date scau_received_date = null;
			Date scau_deadline_date = null;
			String scau_comments = jsonObject.get("scau_comments").toString();
			String scau_action_taken = jsonObject.get("scau_action_taken").toString();
			String scau_next_action_item = jsonObject.get("scau_next_action_item").toString();
			int scau_responsible_person = Integer.parseInt(jsonObject.get("scau_responsible_person").toString());
			int scau_reporting_person = Integer.parseInt(jsonObject.get("scau_reporting_person").toString());

			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());

			Date scau_remainder_date = null;

			if (!jsonObject.get("scau_notice_date").equals(""))
				scau_notice_date = sdfOut.parse(jsonObject.get("scau_notice_date").toString());
			if (!jsonObject.get("scau_received_date").equals(""))
				scau_received_date = sdfOut.parse(jsonObject.get("scau_received_date").toString());
			if (!jsonObject.get("scau_deadline_date").equals(""))
				scau_deadline_date = sdfOut.parse(jsonObject.get("scau_deadline_date").toString());
			if (!jsonObject.get("scau_remainder_date").equals(""))
				scau_remainder_date = sdfOut.parse(jsonObject.get("scau_remainder_date").toString());

			ShowCauseNotice causeNotice = new ShowCauseNotice();
			causeNotice.setScau_orga_id(scau_orga_id);
			causeNotice.setScau_loca_id(scau_loca_id);
			causeNotice.setScau_dept_id(scau_dept_id);
			causeNotice.setScau_ralated_to(scau_ralated_to);
			causeNotice.setScau_notice_date(scau_notice_date);
			causeNotice.setScau_received_date(scau_received_date);
			causeNotice.setScau_deadline_date(scau_deadline_date);
			causeNotice.setScau_reminder_date(scau_remainder_date);
			causeNotice.setScau_comments(scau_comments);
			causeNotice.setScau_action_taken(scau_action_taken);
			causeNotice.setScau_next_action_item(scau_next_action_item);
			causeNotice.setScau_responsible_person(scau_responsible_person);
			causeNotice.setScau_reporting_person(scau_reporting_person);
			causeNotice.setScau_status("Open");
			causeNotice.setScau_added_by(user_id);
			causeNotice.setScau_created_at(new Date());
			causeNotice.setScau_updated_at(new Date());

			int res_id = showCauseNoticeDao.saveShowCauseNotice(causeNotice);
			if (res_id != 0) {
				/*---------------Code for uploading files Start here---------------------------------------------------------*/
				for (int i = 0; i < show_cause_doc.size(); i++) {

					String originalFileName = null;
					String generatedFileName = null;
					String file_path = null;
					int lastGeneratedValue = showCauseNoticeDao.getLastGeneratedValueForShowCauseDocument(res_id,
							"ShowCauseNotice");// uploadedDocumentsDao.getLastGeneratedValueByTtrnId(ttrn_id);

					MultipartFile file1 = show_cause_doc.get(i);
					if (file1.getSize() > 0) {
						File dir = new File("C:" + File.separator + "CMS" + File.separator + projectName
								+ File.separator + "documents" + File.separator + "ShowCauseNotice");
						if (!dir.exists())
							dir.mkdirs();

						lastGeneratedValue++;
						originalFileName = file1.getOriginalFilename();
						generatedFileName = "showCauseNotice" + res_id + "_" + lastGeneratedValue + "."
								+ file1.getOriginalFilename().split("\\.")[1];
						File newFile = new File(dir.getPath() + File.separator + generatedFileName);
						if (!newFile.exists()) {
							newFile.createNewFile();
						}

						OutputStream outputStream = new FileOutputStream(newFile);

						outputStream.write(file1.getBytes());

						String algo = "DES/ECB/PKCS5Padding";
						utilitiesService.encrypt(algo, newFile.getPath());

						file_path = "C:" + File.separator + "CMS" + File.separator + projectName + File.separator
								+ "documents" + File.separator + "ShowCauseNotice" + File.separator + generatedFileName;

						ShowCauseDocuments causeDocuments = new ShowCauseDocuments();
						causeDocuments.setScnd_related_id(res_id);
						causeDocuments.setScnd_related_type("ShowCauseNotice");
						causeDocuments.setScnd_original_file_name(originalFileName);
						causeDocuments.setScnd_last_generated_value_for_filename_for_related_id(lastGeneratedValue);
						causeDocuments.setScnd_generated_file_path(file_path);
						causeDocuments.setScnd_added_by(user_id);
						causeDocuments.setScnd_created_at(new Date());
						showCauseNoticeDao.saveDocument(causeDocuments);

						outputStream.flush();
						outputStream.close();

						Path path = Paths.get(file_path);
						try {
							Files.delete(path);
						} catch (IOException e) {
							// deleting file failed
							e.printStackTrace();
							System.out.println(e.getMessage());
						}
					}
					/*---------------Code for uploading files ends here---------------------------------------------------------*/

				}

				/* Send mail to responsible person and reporting manager */
				User responsibe_user = usersDao.getUserById(causeNotice.getScau_responsible_person());
				User reporting_user = usersDao.getUserById(causeNotice.getScau_reporting_person());

				Properties props = new Properties();
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.host", hostName);
				props.put("mail.smtp.port", portNo);
				Session session_mail = Session.getInstance(props, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

				try {
					String email_body = "<div style='margin:0 auto;width:100%;height:auto;padding:16px;'>";
					email_body += "<p style='font-size:18px;'>Dear " + responsibe_user.getUser_first_name() + "" + " "
							+ "" + responsibe_user.getUser_last_name() + ",</p>";
					email_body += "<p style='text-align:justify;width:70%;'>Show Cause Notice has been added related to <b>"
							+ scau_ralated_to + "</b>,where resposible person is  <b>"
							+ responsibe_user.getUser_first_name() + "" + " " + "" + responsibe_user.getUser_last_name()
							+ "</b> and  notice dated is " + jsonObject.get("scau_notice_date").toString()
							+ ", notice was received on " + jsonObject.get("scau_received_date").toString()
							+ ",last date for sending reply to the notice is <b>"
							+ jsonObject.get("scau_deadline_date").toString() + "</b> and action item is "
							+ scau_next_action_item + " </p>";
					email_body += "<p>In case of any doubt or difficulty, please call Helpdesk as per details given on the support page.</p>";
					email_body += "<p ><span style='color:blue'><em>This is System Generated Mail.</span>&nbsp;<span>Please do not reply.</span></em></p>"
							+ "<h2 style='font-size:18px;font-weight:bold;'>Yours Sincerely</h2>"
							+ "<h2 style='font-size:19px;font-weight:bold;'>Team LexCare</h2>" + "</div>";

					Message message = new MimeMessage(session_mail);
					message.setFrom(new InternetAddress(username));
					message.setRecipients(Message.RecipientType.TO,
							InternetAddress.parse(responsibe_user.getUser_email()));
					// System.out.println(" responsible :" + resposibleEmail);
					// System.out.println(" reporting mail :" + reportingEmail);
					message.addRecipient(Message.RecipientType.CC, new InternetAddress(reporting_user.getUser_email()));
					message.setSubject("Show Cause Notice");
					/*
					 * message.setText("Name: "+resposibleFname+"" +""+
					 * ""+resposibleLname+"\n"+"Email: "+resposibleEmail+"\n"
					 * +"Mobile: "+resposibleMobile+"\n");
					 */
					message.setContent(email_body, "text/html; charset=utf-8");
					Transport.send(message);

					String mail_send_to = "Responsible User- " + responsibe_user.getUser_first_name() + " "
							+ responsibe_user.getUser_last_name() + " Reposting User- "
							+ reporting_user.getUser_first_name() + " " + reporting_user.getUser_last_name();

					utilitiesService.addMailToLog(mail_send_to, "Show Cause Notice Mail", "");

					System.out.println("Done");

				}

				catch (Exception e) {
					// throw new RuntimeException(e);
					e.printStackTrace();

				}

				/* End Send mail to responsible person and reporting manager */

				dataForSend.put("responseMessage", "Success");
			} else {
				dataForSend.put("responseMessage", "Fail");
			}
			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			dataForSend.put("responseMessage", "Fail");
			return dataForSend.toJSONString();
		}
	}

	// Method Written By: Harshad Padole(09-05-2017)
	// Method Purpose: Get User access wise organization,location and department
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String accessWiseDataFilters(String json, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		try {
			JSONArray dataForAssign = new JSONArray();

			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			List<Object> assignList = showCauseNoticeDao.getFiltersForShowCauseNotice(session);

			Iterator<Object> itr = assignList.iterator();
			JSONObject filtersObj = new JSONObject();

			JSONArray entityarray = new JSONArray();
			JSONArray unitarray = new JSONArray();
			JSONArray funcarray = new JSONArray();
			JSONArray Userarray = new JSONArray();

			List<Integer> checkEntiList = new ArrayList<>();
			List<List> checUnitList = new ArrayList<>();
			List<List> checFuncList = new ArrayList<>();
			List<List> checUserList = new ArrayList<>();

			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();

				if (!checkEntiList.contains(object[0])) {
					JSONObject dataForAppendEntiArray = new JSONObject();
					dataForAppendEntiArray.put("orga_id", object[0]);
					dataForAppendEntiArray.put("orga_name", object[1]);

					entityarray.add(dataForAppendEntiArray);
					checkEntiList.add(Integer.parseInt(object[0].toString()));
				}

				List<Integer> checkUnitForAdding = new ArrayList<>();

				checkUnitForAdding.add(Integer.parseInt(object[0].toString()));
				checkUnitForAdding.add(Integer.parseInt(object[2].toString()));

				if (!checUnitList.contains(checkUnitForAdding)) {
					JSONObject dataForAppendUnitArray = new JSONObject();
					dataForAppendUnitArray.put("loca_id", object[2]);
					dataForAppendUnitArray.put("loca_name", object[3]);
					dataForAppendUnitArray.put("orga_id", object[0]);

					unitarray.add(dataForAppendUnitArray);
					checUnitList.add(checkUnitForAdding);
				}
				List<Integer> checkFuncForAdding = new ArrayList<>();

				checkFuncForAdding.add(Integer.parseInt(object[0].toString()));
				checkFuncForAdding.add(Integer.parseInt(object[2].toString()));
				checkFuncForAdding.add(Integer.parseInt(object[4].toString()));

				if (!checFuncList.contains(checkFuncForAdding)) {
					JSONObject dataForAppendFuncArray = new JSONObject();
					dataForAppendFuncArray.put("dept_id", object[4]);
					dataForAppendFuncArray.put("dept_name", object[5]);
					dataForAppendFuncArray.put("orga_id", object[0]);
					dataForAppendFuncArray.put("loca_id", object[2]);

					funcarray.add(dataForAppendFuncArray);
					checFuncList.add(checkFuncForAdding);
				}

				List<Integer> checkUserForAdding = new ArrayList<>();

				checkUserForAdding.add(Integer.parseInt(object[0].toString()));
				checkUserForAdding.add(Integer.parseInt(object[2].toString()));
				checkUserForAdding.add(Integer.parseInt(object[4].toString()));
				checkUserForAdding.add(Integer.parseInt(object[6].toString()));

				if (!checUserList.contains(checkUserForAdding)) {
					JSONObject dataForAppendUserArray = new JSONObject();
					dataForAppendUserArray.put("user_id", object[6]);
					dataForAppendUserArray.put("user_name", object[7].toString() + " " + object[8].toString());
					dataForAppendUserArray.put("orga_id", object[0]);
					dataForAppendUserArray.put("loca_id", object[2]);
					dataForAppendUserArray.put("dept_id", object[4]);

					if (Integer.parseInt(object[9].toString()) >= 1) {
						if (!Userarray.contains(dataForAppendUserArray)) {
							Userarray.add(dataForAppendUserArray);
						}
					}
					checUserList.add(checkUserForAdding);
				}
			}
			filtersObj.put("Entity", entityarray);
			filtersObj.put("Unit", unitarray);
			filtersObj.put("Function", funcarray);
			filtersObj.put("UserList", Userarray);

			dataForAssign.add(filtersObj);

			dataForSend.put("assignDropDowns", dataForAssign);

			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			dataForSend.put("errorMessage", "Failed");
			return dataForSend.toJSONString();
		}
	}

	// Method Written By: Harshad Padole(10-05-2017)
	// Method Purpose: Get show cause notice details
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String getAllShowCauseNotice(String jsonString, HttpSession session) {
		JSONObject data = new JSONObject();
		try {
			JSONArray dataToSend = new JSONArray();
			List<Object> result = showCauseNoticeDao.getShowCauseNoticeList(session);
			if (result != null) {
				JSONArray dataForDD = new JSONArray();

				JSONObject filtersObj = new JSONObject();

				JSONArray entityarray = new JSONArray();
				JSONArray unitarray = new JSONArray();
				JSONArray funcarray = new JSONArray();

				List<Integer> checkEntiList = new ArrayList<>();
				List<List> checUnitList = new ArrayList<>();
				List<List> checFuncList = new ArrayList<>();

				Iterator<Object> iterator = result.iterator();
				while (iterator.hasNext()) {
					Object[] objects = (Object[]) iterator.next();
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("scau_id", objects[0]);
					jsonObject.put("orga_id", objects[1]);
					jsonObject.put("orga_name", objects[2]);
					jsonObject.put("loca_id", objects[3]);
					jsonObject.put("loca_name", objects[4]);
					jsonObject.put("dept_id", objects[5]);
					jsonObject.put("dept_name", objects[6]);
					jsonObject.put("related_to", objects[7]);
					jsonObject.put("notice_date", sdfOut.format(sdfIn.parse(objects[8].toString())));
					jsonObject.put("recieved_date", sdfOut.format(sdfIn.parse(objects[9].toString())));
					jsonObject.put("deadline_date", sdfOut.format(sdfIn.parse(objects[10].toString())));
					jsonObject.put("action_taken", objects[11]);
					if (objects[12] != null)
						jsonObject.put("reminder_date", sdfOut.format(sdfIn.parse(objects[12].toString())));
					else
						jsonObject.put("reminder_date", "NA");

					jsonObject.put("status", objects[13]);
					jsonObject.put("responsible_user_id", objects[14]);
					jsonObject.put("responsible_user_name", objects[15].toString() + " " + objects[16].toString());
					jsonObject.put("reporting_user_id", objects[17]);
					jsonObject.put("reporting_user_name", objects[18].toString() + " " + objects[19].toString());
					jsonObject.put("comments", objects[20]);
					dataToSend.add(jsonObject);

					/* DD Filter code start */
					if (!checkEntiList.contains(objects[1])) {
						JSONObject dataForAppendEntiArray = new JSONObject();
						dataForAppendEntiArray.put("orga_id", objects[1]);
						dataForAppendEntiArray.put("orga_name", objects[2]);

						entityarray.add(dataForAppendEntiArray);
						checkEntiList.add(Integer.parseInt(objects[1].toString()));
					}

					List<Integer> checkUnitForAdding = new ArrayList<>();

					checkUnitForAdding.add(Integer.parseInt(objects[1].toString()));
					checkUnitForAdding.add(Integer.parseInt(objects[3].toString()));

					if (!checUnitList.contains(checkUnitForAdding)) {
						JSONObject dataForAppendUnitArray = new JSONObject();
						dataForAppendUnitArray.put("loca_id", objects[3]);
						dataForAppendUnitArray.put("loca_name", objects[4]);
						dataForAppendUnitArray.put("orga_id", objects[1]);

						unitarray.add(dataForAppendUnitArray);
						checUnitList.add(checkUnitForAdding);
					}

					List<Integer> checkFuncForAdding = new ArrayList<>();

					checkFuncForAdding.add(Integer.parseInt(objects[1].toString()));
					checkFuncForAdding.add(Integer.parseInt(objects[3].toString()));
					checkFuncForAdding.add(Integer.parseInt(objects[5].toString()));

					if (!checFuncList.contains(checkFuncForAdding)) {
						JSONObject dataForAppendFuncArray = new JSONObject();
						dataForAppendFuncArray.put("dept_id", objects[5]);
						dataForAppendFuncArray.put("dept_name", objects[6]);
						dataForAppendFuncArray.put("orga_id", objects[1]);
						dataForAppendFuncArray.put("loca_id", objects[3]);

						funcarray.add(dataForAppendFuncArray);
						checFuncList.add(checkFuncForAdding);
					}

				}

				filtersObj.put("Entity", entityarray);
				filtersObj.put("Unit", unitarray);
				filtersObj.put("Function", funcarray);

				dataForDD.add(filtersObj);
				/* DD Filter code end */

				data.put("notice_list", dataToSend);
				data.put("DD_data", dataForDD);

			}

			return data.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			data.put("responseMessage", "Failed");
			return data.toJSONString();

		}
	}

	// Method Written By: Harshad Padole(10-05-2017)
	// Method Purpose: Save show cause Notice - action item
	@SuppressWarnings("unchecked")
	@Override
	public String saveActionItem(String json, ArrayList<MultipartFile> action_document, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);
			Date tscn_next_due_date = null;
			Date tscn_reminder_date = null;
			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			int tscn_scau_id = Integer.parseInt(jsonObject.get("tscn_scau_id").toString());
			String tscn_comments = jsonObject.get("tscn_comment").toString();
			String tscn_action_taken = jsonObject.get("tscn_action_taken").toString();
			String tscn_next_action_item = jsonObject.get("tscn_next_action_item").toString();
			String tcau_status = jsonObject.get("tcau_status").toString();

			if (!jsonObject.get("tscn_next_due_date").equals(""))
				tscn_next_due_date = sdfOut.parse(jsonObject.get("tscn_next_due_date").toString());
			if (!jsonObject.get("tscn_reminder_date").equals(""))
				tscn_reminder_date = sdfOut.parse(jsonObject.get("tscn_reminder_date").toString());

			ShowCauseNoticeActionItem actionItem = new ShowCauseNoticeActionItem();

			actionItem.setTcau_scau_id(tscn_scau_id);
			actionItem.setTscn_action_taken(tscn_action_taken);
			actionItem.setTscn_next_action_item(tscn_next_action_item);
			actionItem.setTscn_comment(tscn_comments);
			actionItem.setTscn_reminder_date(tscn_reminder_date);
			actionItem.setTscn_next_due_date(tscn_next_due_date);
			actionItem.setTcau_status(tcau_status);
			actionItem.setTcau_added_by(user_id);
			actionItem.setTscn_created_at(new Date());
			actionItem.setTcau_updated_at(new Date());

			int action_item_id = showCauseNoticeDao.saveActionItem(actionItem);

			if (action_item_id != 0) {
				/* Update show cause notice status */
				ShowCauseNotice causeNotice = showCauseNoticeDao.getNoticeDetailsById(tscn_scau_id);
				causeNotice.setScau_status(tcau_status);
				showCauseNoticeDao.merge(causeNotice);
				/* Update END show cause notice status */
				/*---------------Code for uploading files Start here---------------------------------------------------------*/
				for (int i = 0; i < action_document.size(); i++) {

					String originalFileName = null;
					String generatedFileName = null;
					String file_path = null;
					int lastGeneratedValue = showCauseNoticeDao
							.getLastGeneratedValueForShowCauseDocument(action_item_id, "ActionItem");// uploadedDocumentsDao.getLastGeneratedValueByTtrnId(ttrn_id);

					MultipartFile file1 = action_document.get(i);
					if (file1.getSize() > 0) {
						File dir = new File("C:" + File.separator + "CMS" + File.separator + projectName
								+ File.separator + "documents" + File.separator + "ShowCauseNotice" + File.separator
								+ "ActionItem");
						if (!dir.exists())
							dir.mkdirs();

						lastGeneratedValue++;
						originalFileName = file1.getOriginalFilename();
						generatedFileName = "actionItem" + action_item_id + "_" + lastGeneratedValue + "."
								+ file1.getOriginalFilename().split("\\.")[1];
						File newFile = new File(dir.getPath() + File.separator + generatedFileName);
						if (!newFile.exists()) {
							newFile.createNewFile();
						}

						OutputStream outputStream = new FileOutputStream(newFile);

						outputStream.write(file1.getBytes());

						String algo = "DES/ECB/PKCS5Padding";
						utilitiesService.encrypt(algo, newFile.getPath());

						file_path = "C:" + File.separator + "CMS" + File.separator + projectName + File.separator
								+ "documents" + File.separator + "ShowCauseNotice" + File.separator + "ActionItem"
								+ File.separator + generatedFileName;

						ShowCauseDocuments causeDocuments = new ShowCauseDocuments();
						causeDocuments.setScnd_related_id(action_item_id);
						causeDocuments.setScnd_related_type("ActionItem");
						causeDocuments.setScnd_original_file_name(originalFileName);
						causeDocuments.setScnd_last_generated_value_for_filename_for_related_id(lastGeneratedValue);
						causeDocuments.setScnd_generated_file_path(file_path);
						causeDocuments.setScnd_added_by(user_id);
						causeDocuments.setScnd_created_at(new Date());
						showCauseNoticeDao.saveDocument(causeDocuments);

						outputStream.flush();
						outputStream.close();

						Path path = Paths.get(file_path);
						try {
							Files.delete(path);
						} catch (IOException e) {
							// deleting file failed
							e.printStackTrace();
							System.out.println(e.getMessage());
						}
					}
					/*---------------Code for uploading files ends here---------------------------------------------------------*/

				}

				dataForSend.put("responseMessage", "Success");
			} else {
				dataForSend.put("responseMessage", "Fail");
			}
			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Method Written By: Harshad Padole(10-05-2017)
	// Method Purpose: get action items details
	@SuppressWarnings("unchecked")
	@Override
	public String getAllActionItem(String json, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);
			int scau_id = Integer.parseInt(jsonObject.get("scau_id").toString());
			JSONArray actionArray = new JSONArray();
			List<ShowCauseNoticeActionItem> res = showCauseNoticeDao.getActionItemDetailsById(scau_id);
			if (res.size() != 0) {
				Iterator<ShowCauseNoticeActionItem> iterator = res.iterator();
				while (iterator.hasNext()) {
					ShowCauseNoticeActionItem actionItem = iterator.next();
					JSONObject object = new JSONObject();

					object.put("tscn_id", actionItem.getTscn_id());
					object.put("tscn_status", actionItem.getTcau_status());
					object.put("tscn_action_taken", actionItem.getTscn_action_taken());
					object.put("tscn_next_action_item", actionItem.getTscn_next_action_item());
					object.put("tscn_comment", actionItem.getTscn_comment());
					object.put("tscn_next_due_date", sdfOut.format(actionItem.getTscn_next_due_date()));
					object.put("tscn_reminder_date", sdfOut.format(actionItem.getTscn_reminder_date()));

					List<ShowCauseDocuments> doc = showCauseNoticeDao.getDocuments(actionItem.getTscn_id(),
							"ActionItem");
					JSONArray doc_array = new JSONArray();
					if (doc != null) {
						Iterator<ShowCauseDocuments> itr = doc.iterator();
						while (itr.hasNext()) {
							ShowCauseDocuments documents = itr.next();
							JSONObject doc_json = new JSONObject();
							doc_json.put("doc_id", documents.getScnd_id());
							doc_json.put("doc_name", documents.getScnd_original_file_name());
							doc_array.add(doc_json);
						}

					}
					object.put("doc_list", doc_array);
					actionArray.add(object);
				}

			}
			dataForSend.put("action_list", actionArray);

			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			dataForSend.put("responseMessage", "Failed");
			return dataForSend.toJSONString();
		}
	}

	// Method Written By: Harshad Padole(10-05-2017)
	// Method Purpose: get show cause notice details
	@SuppressWarnings("unchecked")
	@Override
	public String getShowCauseNoticeDetails(String json, HttpSession session) {
		JSONObject dataToSend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(json);
			int scau_id = Integer.parseInt(jsonObj.get("scau_id").toString());
			List<Object> res = showCauseNoticeDao.getShowCauseNoticeDetailsById(scau_id);
			if (res != null) {
				Iterator<Object> iterator = res.iterator();
				while (iterator.hasNext()) {
					Object[] objects = (Object[]) iterator.next();
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("scau_id", objects[0]);
					jsonObject.put("orga_id", objects[1]);
					jsonObject.put("orga_name", objects[2]);
					jsonObject.put("loca_id", objects[3]);
					jsonObject.put("loca_name", objects[4]);
					jsonObject.put("dept_id", objects[5]);
					jsonObject.put("dept_name", objects[6]);
					jsonObject.put("related_to", objects[7]);
					jsonObject.put("notice_date", sdfOut.format(sdfIn.parse(objects[8].toString())));
					jsonObject.put("recieved_date", sdfOut.format(sdfIn.parse(objects[9].toString())));
					jsonObject.put("deadline_date", sdfOut.format(sdfIn.parse(objects[10].toString())));
					jsonObject.put("action_taken", objects[11]);
					if (objects[12] != null)
						jsonObject.put("reminder_date", sdfOut.format(sdfIn.parse(objects[12].toString())));
					else
						jsonObject.put("reminder_date", "NA");

					jsonObject.put("status", objects[13]);
					jsonObject.put("responsible_user_id", objects[14]);
					jsonObject.put("responsible_user_name", objects[15].toString() + " " + objects[16].toString());
					jsonObject.put("reporting_user_id", objects[17]);
					jsonObject.put("reporting_user_name", objects[18].toString() + " " + objects[19].toString());
					jsonObject.put("comments", objects[20]);
					jsonObject.put("next_action_item", objects[21]);
					List<ShowCauseDocuments> doc = showCauseNoticeDao.getDocuments(scau_id, "ShowCauseNotice");
					JSONArray doc_array = new JSONArray();
					if (doc != null) {
						Iterator<ShowCauseDocuments> itr = doc.iterator();
						while (itr.hasNext()) {
							ShowCauseDocuments documents = itr.next();
							JSONObject doc_json = new JSONObject();
							doc_json.put("doc_id", documents.getScnd_id());
							doc_json.put("doc_name", documents.getScnd_original_file_name());
							doc_array.add(doc_json);
						}

					}
					jsonObject.put("doc_list", doc_array);

					dataToSend.put("ShowCauseNoticeDetails", jsonObject);
				}
			}
			return dataToSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			dataToSend.put("responseMessage", "Failed");
			return dataToSend.toJSONString();
		}
	}

	// Method Written By: Harshad Padole(15-05-2017)
	// Method Purpose: Download Document
	@SuppressWarnings("unchecked")
	@Override
	public String downloadShowCauseDocument(String json, HttpServletResponse response) {
		JSONObject dataToSend = new JSONObject();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(json);
			int doc_id = Integer.parseInt(jsonObj.get("doc_id").toString());
			ShowCauseDocuments causeDocuments = showCauseNoticeDao.getDocDetails(doc_id);
			if (causeDocuments.getScnd_generated_file_path() != null) {
				File file = new File(causeDocuments.getScnd_generated_file_path());
				String algo = "DES/ECB/PKCS5Padding";
				File decFile = new File(utilitiesService.decrypt(algo, file.getPath() + ".enc"));
				InputStream is = new FileInputStream(decFile);

				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", "attachment; filename=\"" + decFile.getName() + "\"");

				OutputStream os = response.getOutputStream();
				byte[] buffer = new byte[1024];
				int len;
				while ((len = is.read(buffer)) != -1) {
					os.write(buffer, 0, len);
				}

				os.flush();
				os.close();
				is.close();
				Path path = Paths.get(decFile.getPath());
				try {
					Files.delete(path);
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
				}
			}

			dataToSend.put("responceMessage", "Success");
			return dataToSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			dataToSend.put("responceMessage", "Failed");
			return dataToSend.toJSONString();
		}
	}

	// Method Written By: Harshad Padole(15-05-2017)
	// Method Purpose: Update show cause notice details
	@SuppressWarnings("unchecked")
	@Override
	public String updateShowCauseNotice(String json, ArrayList<MultipartFile> show_cause_doc, HttpSession session) {
		JSONObject dataForSend = new JSONObject();
		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);
			int scau_id = Integer.parseInt(jsonObject.get("scau_id").toString());
			int scau_orga_id = Integer.parseInt(jsonObject.get("scau_orga_id").toString());
			int scau_loca_id = Integer.parseInt(jsonObject.get("scau_loca_id").toString());
			int scau_dept_id = Integer.parseInt(jsonObject.get("scau_dept_id").toString());
			String scau_ralated_to = jsonObject.get("scau_ralated_to").toString();
			Date scau_notice_date = null;
			Date scau_received_date = null;
			Date scau_deadline_date = null;
			String scau_comments = jsonObject.get("scau_comments").toString();
			String scau_action_taken = jsonObject.get("scau_action_taken").toString();
			String scau_next_action_item = jsonObject.get("scau_next_action_item").toString();
			int scau_responsible_person = Integer.parseInt(jsonObject.get("scau_responsible_person").toString());
			int scau_reporting_person = Integer.parseInt(jsonObject.get("scau_reporting_person").toString());

			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());

			Date scau_remainder_date = null;

			if (!jsonObject.get("scau_notice_date").equals(""))
				scau_notice_date = sdfOut.parse(jsonObject.get("scau_notice_date").toString());
			if (!jsonObject.get("scau_received_date").equals(""))
				scau_received_date = sdfOut.parse(jsonObject.get("scau_received_date").toString());
			if (!jsonObject.get("scau_deadline_date").equals(""))
				scau_deadline_date = sdfOut.parse(jsonObject.get("scau_deadline_date").toString());
			if (!jsonObject.get("scau_remainder_date").equals(""))
				scau_remainder_date = sdfOut.parse(jsonObject.get("scau_remainder_date").toString());

			ShowCauseNotice causeNotice = showCauseNoticeDao.getNoticeDetailsById(scau_id);
			causeNotice.setScau_orga_id(scau_orga_id);
			causeNotice.setScau_loca_id(scau_loca_id);
			causeNotice.setScau_dept_id(scau_dept_id);
			causeNotice.setScau_ralated_to(scau_ralated_to);
			causeNotice.setScau_notice_date(scau_notice_date);
			causeNotice.setScau_received_date(scau_received_date);
			causeNotice.setScau_deadline_date(scau_deadline_date);
			causeNotice.setScau_reminder_date(scau_remainder_date);
			causeNotice.setScau_comments(scau_comments);
			causeNotice.setScau_action_taken(scau_action_taken);
			causeNotice.setScau_next_action_item(scau_next_action_item);
			causeNotice.setScau_responsible_person(scau_responsible_person);
			causeNotice.setScau_reporting_person(scau_reporting_person);
			causeNotice.setScau_updated_at(new Date());

			showCauseNoticeDao.merge(causeNotice);

			int res_id = causeNotice.getScau_id();
			if (res_id != 0) {
				/*---------------Code for uploading files Start here---------------------------------------------------------*/
				for (int i = 0; i < show_cause_doc.size(); i++) {

					String originalFileName = null;
					String generatedFileName = null;
					String file_path = null;
					int lastGeneratedValue = showCauseNoticeDao.getLastGeneratedValueForShowCauseDocument(res_id,
							"ShowCauseNotice");// uploadedDocumentsDao.getLastGeneratedValueByTtrnId(ttrn_id);

					MultipartFile file1 = show_cause_doc.get(i);
					if (file1.getSize() > 0) {
						File dir = new File("C:" + File.separator + "CMS" + File.separator + projectName
								+ File.separator + "documents" + File.separator + "ShowCauseNotice");
						if (!dir.exists())
							dir.mkdirs();

						lastGeneratedValue++;
						originalFileName = file1.getOriginalFilename();
						generatedFileName = "showCauseNotice" + res_id + "_" + lastGeneratedValue + "."
								+ file1.getOriginalFilename().split("\\.")[1];
						File newFile = new File(dir.getPath() + File.separator + generatedFileName);
						if (!newFile.exists()) {
							newFile.createNewFile();
						}

						OutputStream outputStream = new FileOutputStream(newFile);

						outputStream.write(file1.getBytes());

						String algo = "DES/ECB/PKCS5Padding";
						utilitiesService.encrypt(algo, newFile.getPath());

						file_path = "C:" + File.separator + "CMS" + File.separator + projectName + File.separator
								+ "documents" + File.separator + "ShowCauseNotice" + File.separator + generatedFileName;

						ShowCauseDocuments causeDocuments = new ShowCauseDocuments();
						causeDocuments.setScnd_related_id(res_id);
						causeDocuments.setScnd_related_type("ShowCauseNotice");
						causeDocuments.setScnd_original_file_name(originalFileName);
						causeDocuments.setScnd_last_generated_value_for_filename_for_related_id(lastGeneratedValue);
						causeDocuments.setScnd_generated_file_path(file_path);
						causeDocuments.setScnd_added_by(user_id);
						causeDocuments.setScnd_created_at(new Date());
						showCauseNoticeDao.saveDocument(causeDocuments);

						outputStream.flush();
						outputStream.close();

						Path path = Paths.get(file_path);
						try {
							Files.delete(path);
						} catch (IOException e) {
							// deleting file failed
							e.printStackTrace();
							System.out.println(e.getMessage());
						}
					}
					/*---------------Code for uploading files ends here---------------------------------------------------------*/

				}

				dataForSend.put("responseMessage", "Success");
			} else {
				dataForSend.put("responseMessage", "Fail");
			}
			return dataForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			dataForSend.put("responseMessage", "Fail");
			return dataForSend.toJSONString();
		}
	}

}
