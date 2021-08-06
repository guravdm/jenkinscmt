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
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lexprd006.dao.CertificateDao;
import lexprd006.dao.UsersDao;
import lexprd006.domain.QuerterlyGeneratedCertificate;
import lexprd006.domain.User;
import lexprd006.service.CertificateService;

/*
 * Author: Harshad Padole
 * Date: 22/05/2017
 * Purpose: Controller for certificate
 * 
 * 
 * 
 * */

@Service(value = "certificateService")
public class CertificateServiceImpl implements CertificateService {

	@Autowired
	CertificateDao certificateDao;

	@Autowired
	UsersDao userDao;

	private @Value("#{config['project_name'] ?: 'null'}") String projectName;

	public final SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd");
	public final SimpleDateFormat sdfOut = new SimpleDateFormat("dd-MM-yyyy");

	// Method Created By: Harshad Padole
	// Method Purpose: get certificate details
	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public String getQuerterlyCertificateDetails(String json, HttpSession session) {

		JSONObject dataToSend = new JSONObject();
		System.out.println("getQuerterlyCertificateDetails method () ");
		try {
			JSONArray nonCompliedTask = new JSONArray();
			JSONArray showCauseNotice = new JSONArray();
			JSONArray legisArray = new JSONArray();

			JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);
			Date querterly_certificate_from = sdfOut.parse(jsonObject.get("date_from").toString());
			Date querterly_certificate_to = sdfOut.parse(jsonObject.get("date_to").toString());

			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");

			String from_date = jsonObject.get("date_from").toString();
			Date date = formatter.parse(from_date);

			String to_date = jsonObject.get("date_to").toString();
			Date date2 = formatter.parse(to_date);

			String fromDate = formatter.format(date);
			String toDate = formatter.format(date2);

			int user_id = Integer.parseInt(session.getAttribute("sess_user_id").toString());
			String username = (String) session.getAttribute("sess_user_full_name");
			System.out.println("Username : " + username);
			// User user = usersDao.getUserById(user_id);
			User user = userDao.getUserById(user_id);

			// PDF Generation code starts

			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			String curDate = sdf.format(dt);
			SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
			String curTime = sdf2.format(dt);
			JSONObject ob = (JSONObject) new JSONParser().parse(json);

			File dir = new File(
					"C:" + File.separator + "CMS" + File.separator + projectName + File.separator + "Certificate");

			if (!dir.exists())
				dir.mkdirs();

			String generatedFileName = username + "_" + curDate + "_" + new Date().getTime() + ".pdf";
			File newFile = new File(dir.getPath() + File.separator + generatedFileName);
			if (!newFile.exists()) {
				try {
					// newFile.mkdirs();
					newFile.createNewFile();
					Document document = new Document();
					document.setPageSize(PageSize.A4);
					Paragraph paragraph = new Paragraph();

					// step 2
					PdfWriter.getInstance(document, new FileOutputStream(newFile));
					// step 3
					document.open();
					document.add(Chunk.NEWLINE);
					document.add(Chunk.NEWLINE);
					document.add(Chunk.NEWLINE);
					System.out.println("File created at location : " + newFile);

					int indentation = 0;
					// Image img =
					// Image.getInstance("WebContent/img/certificate.jpg");
					Image img = Image.getInstance("C:/CMS/MLL-LOGO.jpg");
					img.scaleToFit(200, 100);
					img.setAbsolutePosition(50, 300);

					Image img1 = Image.getInstance("C:/CMS/MLL-Address.jpg");
					img1.scaleToFit(200, 100);
					img1.setAbsolutePosition(50, 300);

					float scaler = ((document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin()
							- indentation) / img.getWidth()) * 25;

					float scaler1 = ((document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin()
							- indentation) / img1.getWidth()) * 25;
					Font f = new Font(FontFamily.TIMES_ROMAN, 18.0f, Font.BOLD, BaseColor.BLACK);
					Font fnts = new Font(FontFamily.TIMES_ROMAN, 20.0f, Font.NORMAL, BaseColor.BLACK);
					Font f3 = new Font(FontFamily.TIMES_ROMAN, 14.0f, Font.BOLD, BaseColor.BLACK);
					Font f4 = new Font(FontFamily.TIMES_ROMAN, 10.5f, Font.BOLD, BaseColor.BLACK);

					PdfPTable table = new PdfPTable(2);
					// table.setWidthPercentage(100);
					PdfPCell cell = new PdfPCell();
					cell.setColspan(2);
					cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
					cell.setPadding(5.0f);
					cell.setPaddingLeft(-50);
					cell.setBorder(0);
					table.getDefaultCell().setBorder(0);
					table.setWidths(new int[] { 300, 600 });
					table.addCell(img);
					table.addCell(img1);

					document.add(table);

					// img.setIndentationLeft(10);
					// paragraph.add(new Chunk(img, 10, -80));
					// img.setAlignment(Element.ALIGN_LEFT);
					// document.add(paragraph);

					// document.add(para);

					document.add(Chunk.NEWLINE);
					document.add(Chunk.NEWLINE);
					document.add(Chunk.NEWLINE);
					document.add(Chunk.NEWLINE);
					document.add(Chunk.NEWLINE);
					document.add(Chunk.NEWLINE);

					Paragraph paragraph1 = new Paragraph();
					paragraph1.add(new Chunk("Date : " + curTime, f3));
					document.add(paragraph1);
					document.add(Chunk.NEWLINE);

					/*
					 * Paragraph paragraph2 = new Paragraph(); paragraph2.add(new Chunk("To"));
					 * img.setIndentationLeft(10); paragraph2.add(new Chunk(img, 4, 10));
					 * img.setAlignment(Element.ALIGN_RIGHT); document.add(paragraph2); //
					 * document.add(Chunk.NEWLINE);
					 */
					Paragraph paragraph3 = new Paragraph();
					paragraph3.add(new Chunk("To"));
					paragraph3.add(Chunk.NEWLINE);
					paragraph3.add(new Chunk("Board of Directors,"));

					paragraph3.add(Chunk.NEWLINE);
					paragraph3.add(new Chunk("Mahindra Logistics Limited,"));
					paragraph3.add(Chunk.NEWLINE);
					paragraph3.add(new Chunk("Mahindra Towers, P K Kurne Chowk,"));
					paragraph3.add(Chunk.NEWLINE);
					paragraph3.add(new Chunk("Worli, Mumbai 400 018"));

					// img.setIndentationLeft(10);
					// paragraph3.add(new Chunk(img, 4, 10));
					// img.setAlignment(Element.ALIGN_RIGHT);
					document.add(paragraph3);

					document.add(Chunk.NEWLINE);
					/*
					 * Paragraph p2 = new Paragraph("Dear Sir/Madam,", f3); document.add(p2);
					 * document.add(Chunk.NEWLINE);
					 */

					Paragraph p3 = new Paragraph();
					p3.add(new Chunk("Sub : Compliance Certificate"));
					document.add(p3);
					document.add(Chunk.NEWLINE);

					Paragraph p4;

					p4 = new Paragraph(" I, " + user.getUser_first_name() + " " + user.getUser_last_name()
							+ ", certify that during the period " + fromDate + " to " + toDate
							+ "(except as mentioned in Annexure) there has been due compliance with all the Laws, Orders "
							+ "Regulations and other requirements as amended from time to time (including Rules framed "
							+ "under the respective legislations) of the Central, State, and other Government and Local"
							+ "Authorities, concerning the business of the Company to the extent applicable to the Company including, but not limited to:");
					p4.setAlignment(p4.ALIGN_JUSTIFIED);
					document.add(p4);
					document.add(Chunk.NEWLINE);

					// New Code by Navdeep

					PdfPTable table1 = new PdfPTable(1);
					table1.setWidthPercentage(100);
					PdfPCell cell1 = new PdfPCell(new Phrase("Annexure 1"));
					cell1.setColspan(1);
					cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell1.setPadding(5.0f);
					// cell.setBackgroundColor(new BaseColor(0, 0, 0));
					// table1.addCell(cell1);

					// table1.addCell("Category Law Name");
					table1.addCell("Name Of Legislation");
					// table1.addCell("Rule");

					// unique records getting ( Legislation )
					List<Object> unqLegRec = certificateDao.getQrtrLegRecords(json, session);
					System.out.println("unqLegRec size : " + unqLegRec.size());

					Paragraph paragraph5 = new Paragraph();
					int i = 1;
					/* JSONObject legTask = null; */
					if (unqLegRec != null && unqLegRec.size() > 0) {
						Iterator<Object> itrs = unqLegRec.iterator();
						while (itrs.hasNext()) {
							Object[] obj = (Object[]) itrs.next();
							/* legTask = new JSONObject(); */
							String legiName = (String) obj[1];
							// String ruleName = (String) obj[3];
							// String lawName = (String) obj[17];
							// table1.addCell(lawName);
							// table1.addCell(legiName);
							// table1.addCell(ruleName);
							/* legisArray.add(legTask); */

							paragraph5.add(new Chunk(i + ". " + legiName));
							paragraph5.add(Chunk.NEWLINE);

							i++;
						}
					} else {
						PdfPCell cell2 = new PdfPCell(new Phrase("No Records Available"));
						cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell2.setColspan(3);
						table1.addCell(cell2);
					}
					// document.add(table1);

					// Navdeep

					document.add(paragraph5);

					document.add(Chunk.NEWLINE);
					document.add(Chunk.NEWLINE);

					Paragraph paragraph6 = new Paragraph();
					paragraph6.add(new Chunk(
							"I. All the dues to the Financial Institutions and Banks in respect of the financial assistance rendered by them have been paid and no amounts are overdue."));
					document.add(paragraph6);
					document.add(Chunk.NEWLINE);

					Paragraph paragraph10 = new Paragraph();
					paragraph10.add(new Chunk(
							"II. The Company has no deposits or interest payable thereon on due date, and the provisions of Section 164 are not attracted to the Company."));
					document.add(paragraph10);
					document.add(Chunk.NEWLINE);

					Paragraph paragraph11 = new Paragraph();
					paragraph11.add(new Chunk(
							"This certificate is given by the undersigned knowing fully well that on the faith and strength of what is stated above; full reliance is placed by the Board of Directors of the Company."));
					document.add(paragraph11);
					document.add(Chunk.NEWLINE);

					Paragraph paragraph12 = new Paragraph();
					paragraph12.add(new Chunk(
							"Annexure 1 contains the reason of non-compliance and the action taken for all non-complied activities."));
					document.add(paragraph12);
					document.add(Chunk.NEWLINE);

					Paragraph p7 = new Paragraph("Thanking You,");
					p7.setAlignment(Element.ALIGN_LEFT);
					document.add(p7);
					document.add(Chunk.NEWLINE);

					Paragraph p8 = new Paragraph("Your's Sincerely");
					p8.setAlignment(Element.ALIGN_RIGHT);
					document.add(p8);
					// Paragraph p9 = new Paragraph("Lexcare User");
					Paragraph p9 = new Paragraph(user.getUser_first_name() + " " + user.getUser_last_name());
					p9.setAlignment(Element.ALIGN_RIGHT);
					p9.setIndentationRight(8);
					document.add(p9);
					document.add(Chunk.NEWLINE);
					document.add(Chunk.NEWLINE);
					document.add(Chunk.NEWLINE);
					document.add(Chunk.NEWLINE);

					// table 2 or annexure 2
					PdfPTable tbl = new PdfPTable(9);
					tbl.setWidthPercentage(100);
					PdfPCell cell2 = new PdfPCell(new Phrase("Annexure 1"));
					cell2.setColspan(9);
					cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell2.setPadding(5.0f);
					tbl.addCell(cell2);

					tbl.addCell("Task Id");
					tbl.addCell("Who");
					tbl.addCell("When");
					tbl.addCell("Activity");
					tbl.addCell("Executor");
					tbl.addCell("Evaluator");
					tbl.addCell("Function Head");
					tbl.addCell("Impact");
					tbl.addCell("Legal Due Date");
//					/*
//					 * tbl.addCell("Comments"); tbl.addCell("Reason for Non-Compliance");
//					 */

					List<Object> result = certificateDao.getQuerterlyCertificateDetails(json, session);
					System.out.println("result size : " + result.size());
					if (result != null && result.size() > 0) {
						Iterator<Object> itr = result.iterator();
						while (itr.hasNext()) {
							Object[] obj = (Object[]) itr.next();
							JSONObject task = new JSONObject();
							Date submitted_date = null;
							Date legal_due_date = null;
							String client_task_id = (String) obj[1];
							String task_legi_name = (String) obj[2];
							String task_rule_name = (String) obj[3];
							String task_activity = (String) obj[4];
							String task_impact = (String) obj[5];
							String task_frequency = (String) obj[6];
							String task_who = (String) obj[7];
							String task_when = (String) obj[8];

							String task_legal_due_date = (String) sdfOut.format(sdfIn.parse(obj[9].toString()));
							/*
							 * String task_comment = (String) obj[11]; String task_reasone_of_non_compliance
							 * = (String) obj[12];
							 */
							String task_submitted_date = "";
							if (obj[13] != null)
								task_submitted_date = (String) sdfOut.format(sdfIn.parse(obj[13].toString()));
							else
								task_submitted_date = "";
							String task_executor = (String) obj[14] + " " + obj[15];
							String task_evaluator = (String) obj[16] + " " + obj[17];
							String task_functionhead = (String) obj[18] + " " + obj[19];

							tbl.addCell(client_task_id);
							tbl.addCell(task_who);
							tbl.addCell(task_when);
							tbl.addCell(task_activity);
							tbl.addCell(task_executor);
							tbl.addCell(task_evaluator);
							tbl.addCell(task_functionhead);
							tbl.addCell(task_impact);
							tbl.addCell(task_legal_due_date);
							/*
							 * tbl.addCell(task_comment); tbl.addCell(task_reasone_of_non_compliance);
							 */
						}
					} else {

						PdfPCell pCell = new PdfPCell(new Phrase("No Records Available"));
						pCell.setHorizontalAlignment(Element.ALIGN_CENTER);
						pCell.setColspan(9);
						tbl.addCell(pCell);
					}
					document.add(tbl);
					document.add(Chunk.NEWLINE);

					// step 5
					document.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// saving path and user_id

			QuerterlyGeneratedCertificate sc = new QuerterlyGeneratedCertificate();
			sc.setQuerterly_certificate_added_by(Integer.parseInt(session.getAttribute("sess_user_id").toString()));
			sc.setQuerterly_certificate_created_at(new Date());

			sc.setQuerterly_certificate_querter("3");
			sc.setQuerterly_certificate_year(2019);
			sc.setQuerterly_certificate_from(querterly_certificate_from);
			sc.setQuerterly_certificate_to(querterly_certificate_to);
			sc.setQuerterly_certificate_orignalName(generatedFileName);
			// sc.setQuerterly_certificate_id(Querterly_certificate_id);
			sc.setQuerterly_certificate_certificatePath(dir + File.separator + generatedFileName);
			// System.out.println("records saved! ");
			certificateDao.saveQuerterlyCertificate(sc);
			// PDF Generation code End...

			List<Object> notice_list = certificateDao.getQuerterlyShowCauseDetails(json, session);
			if (notice_list != null) {
				Iterator<Object> itr = notice_list.iterator();
				while (itr.hasNext()) {
					Object[] obj = (Object[]) itr.next();
					JSONObject notice = new JSONObject();
					notice.put("orga_name", obj[1]);
					notice.put("loca_name", obj[2]);
					notice.put("dept_name", obj[3]);
					notice.put("notice_related_to", obj[4]);
					notice.put("notice_date", sdfOut.format(sdfIn.parse(obj[5].toString())));
					notice.put("notice_recived_date", sdfOut.format(sdfIn.parse(obj[6].toString())));
					notice.put("notice_deadline_date", sdfOut.format(sdfIn.parse(obj[7].toString())));
					notice.put("notice_next_action_item", obj[8]);
					notice.put("notice_next_action_item", obj[9]);
					notice.put("notice_responsible_name", obj[10] + " " + obj[11]);
					notice.put("notice_reporting_name", obj[12] + " " + obj[13]);
					notice.put("notice_status", obj[14]);
					showCauseNotice.add(notice);
				}

			}

			dataToSend.put("responseMessage", "Success");
			return dataToSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			dataToSend.put("responseMessage", "Failed");
			return dataToSend.toJSONString();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public String downloadCertificateDetails(HttpSession session) {
		String data = "";

		JSONArray dataForSend = new JSONArray();
		List<Object> list = certificateDao.downloadCertificate(session);
		if (list.size() > 0) {
			Iterator<Object> iterator = list.iterator();
			while (iterator.hasNext()) {
				Object[] next = (Object[]) iterator.next();
				JSONObject fileJSON = new JSONObject();
				fileJSON.put("certificatePath", next[0]);
				fileJSON.put("created_at", next[1]);
				fileJSON.put("from", next[2]);
				fileJSON.put("to", next[3]);
				fileJSON.put("fname", next[4]);
				fileJSON.put("orig_file_name", next[5]);
				dataForSend.add(fileJSON);
			}
			return dataForSend.toJSONString();
		}
		return dataForSend.toJSONString();

//		ObjectMapper Obj = new ObjectMapper();
//		try {
//			String jsonStr = Obj.writeValueAsString(list);
//			System.out.println("jsonStr : " + jsonStr);
//			data = new ObjectMapper().writeValueAsString(list);
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
//		return data;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String downloadCertificate(String certificate, HttpSession session, HttpServletResponse response) {
		JSONObject objForSend = new JSONObject();
		try {
			JSONObject ob = (JSONObject) new JSONParser().parse(certificate);
			String certificateName = ob.get("certificateName").toString();
			String docPath = certificateDao.getCertificatePath(ob.get("certificateName").toString());
			System.out.println(" path : " + docPath);
			if (certificateName != null) {
				File file = new File(docPath);
				File decFile = new File(certificateName);
				// System.out.println("return :"+utilitiesService.decrypt(algo,
				// file.getPath()+".enc"));

				InputStream is = new FileInputStream(file);
				System.out.println("File : " + file.getName());

//				response.setContentType("application/octet-stream");
//				response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

				OutputStream os = response.getOutputStream();
//				byte[] buffer = new byte[1024];
//				int len;
//				while ((len = is.read(buffer)) != -1) {
//					System.out.println("download pdf");
//					os.write(buffer, 0, len);
//				}

				response.setContentType("application/pdf");
				response.addHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
				String filePath = "C://CMS//documents//Certificate//";
				Path path = Paths.get(decFile.getPath());
				Path file1 = Paths.get(filePath, decFile.getPath());
				System.out.println("File path is : " + file1);
				if (Files.exists(file1)) {
//					response.setContentType("application/pdf");
//					response.addHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
					try {
						Files.copy(file1, response.getOutputStream());
						response.getOutputStream().flush();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}

				os.flush();
				os.close();
				is.close();

			} else {

			}
			objForSend.put("responeMessage", "Success");
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("responeMessage", "Failed");
			return objForSend.toJSONString();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public String downloadDocuments(String jsonString, HttpServletResponse response) throws Throwable {
		JSONObject objForSend = new JSONObject();
		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);
			System.out.println("udoc_id :" + jsonObject.get("certificate").toString());
			String certificateName = jsonObject.get("certificate").toString();
			if (certificateDao.getCertificatePath(certificateName) != null) {
				File file = new File(certificateDao.getCertificatePath(certificateName));

				File decFile = new File(file.getPath());
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
			} else {

			}
			objForSend.put("responeMessage", "Success");
			return objForSend.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("responeMessage", "Failed");
			return objForSend.toJSONString();
		}

	}

}
