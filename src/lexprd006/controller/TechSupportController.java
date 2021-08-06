package lexprd006.controller;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/*")
public class TechSupportController {
	private @Value("#{config['mail_user_name']?: 'null'}") String username;
	private @Value("#{config['mail_password']?: 'null'}") String password;
	private @Value("#{config['mail_smtp_host']?: 'null'}") String hostName;
	private @Value("#{config['mail_smtp_port']?: 'null'}") String portNo;
	private @Value("#{config['mail_from']?: 'null'}") String mailFrom;
	private @Value("#{config['project_name']?: 'null'}") String projectName;
	//Method Written By: Mahesh Kharote(25/10/2016)
	@SuppressWarnings("unchecked")
	//Method Purpose: Get all Functions rest Call
	@RequestMapping(value = "/getSupportQuery", method = RequestMethod.POST)
	public @ResponseBody String getSupportQuery(@RequestBody String jsonString){
		JSONObject objForSend = new JSONObject();
		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);
			String Cname = jsonObject.get("Cname").toString();
			String Cemail = jsonObject.get("Cemail").toString();
			String Cmobile = jsonObject.get("Cmobile").toString();
			String Cmessage = jsonObject.get("Cmessage").toString();
			
			
			/*--------------------------Code to send mail---------------------*/
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			//props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", hostName);
			props.put("mail.smtp.port", portNo);
			Session session = Session.getInstance(props,
					new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
			try {
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(mailFrom));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse("support@lexcareglobal.com"));
				message.setSubject("Query raised from tool");
				message.setText("Name: "+Cname+"\n"+"Email: "+Cemail+"\n"
						+"Mobile: "+Cmobile+"\n"+"Message: "+Cmessage+"\nThis is a system generated mail. Please do not reply to this mail.\n");
				Transport.send(message);
				//utilitiesService.addMailToLog(username, "Query raised from tool","");
				System.out.println("Done");
				objForSend.put("responseMessage", "Success");
				return objForSend.toJSONString();
			} 
			catch (Exception e) 
			{
				// throw new RuntimeException(e);
				e.printStackTrace();
				objForSend.put("responseMessage", "Failed");
				return objForSend.toJSONString();
			}
			/*----------------------Code to send mail ends here---------------*/
		} catch (Exception e) {
			e.printStackTrace();
			objForSend.put("responseMessage", "Failed");
			return objForSend.toJSONString();
		}
	}
	
	
	//Method Written By: Mahesh Kharote(25/10/2016)	
		//Method Purpose: Get all Functions rest Call
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@RequestMapping(value = "/sendcommonemail", method = RequestMethod.POST)
		public @ResponseBody String sendCommonEmail(@RequestParam("ttrn_proof_of_compliance") ArrayList<MultipartFile> attachement , @RequestParam("jsonString") String jsonString){
			JSONObject objForSend = new JSONObject();
			try {
				JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);
				JSONArray email_ids = (JSONArray) jsonObject.get("email_ids");
				String subject = jsonObject.get("subject").toString();
				String body = jsonObject.get("body").toString();			

				int cnt = 1;
				Set<String> s = new HashSet<>();
				int email_ids_size = email_ids.size();
				for(int i = 0; i < email_ids.size(); i++){
					if(!email_ids.get(i).equals("")) {
					s.add(email_ids.get(i).toString());
					
							if((cnt==10 || (email_ids_size-1)==i)){
								InternetAddress[] addresses = new InternetAddress[s.size()];
								int c = 0;
								for (
								Iterator iterator = s.iterator(); iterator.hasNext();) {
									String email_idd = (String) iterator.next();
									addresses[c] = new InternetAddress(email_idd);
									System.out.println(" Email id : "+email_idd);
									c++;
								}
								this.sendEmail(addresses, subject, body,attachement);
								cnt = 0;
								c = 0;
								s.clear();
							}
					cnt++;
					}
				}
				objForSend.put("responseMessage", "Success");
				return objForSend.toJSONString();
			} catch (Exception e) {
				e.printStackTrace();
				objForSend.put("responseMessage", "Failed");
				return objForSend.toJSONString();
			}
		}




		private void sendEmail(InternetAddress[] addresses, String subject, String body, ArrayList<MultipartFile> attachement) {

			
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			//props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", hostName);
			props.put("mail.smtp.port", portNo);
			
			/*props.put("mail.smtp.host", hostName);
			props.put("mail.smtp.port", portNo);*/
			
			Session session = Session.getInstance(props,
					new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
			try {
				String email_body = "";
				email_body +="<div style='margin:0 auto;width:100%;height:auto;padding:20px;color:#000000;'>"
						+ body 
						+"<p>\nThis is a system generated mail. Please do not reply to this mail.<br/>"
						+ "In case of any doubt or difficulty, please call Helpdesk as per details given on the support page."
						+ "</p>"
						+ "<h2 style='font-size:17px;font-weight:bold;'>Yours Sincerely</h2>"
						+ "<h2 style='font-size:17px;font-weight:bold;'>Team LexCare</h2>"
						+ "</div>";
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(mailFrom , "Team LexCare"));
				message.setRecipients(Message.RecipientType.TO,addresses);
				message.setSubject(subject);
				message.setContent(email_body, "text/html; charset=utf-8");
				message.setSentDate(new Date());
				
				
				Multipart multipart = new MimeMultipart();
				
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setContent(email_body, "text/html; charset=utf-8");
				// Create a multipart message
				
				// Set text message part
				multipart.addBodyPart(messageBodyPart);
				// Part two is attachment
				

				
				
				for(int i1 = 0; i1<attachement.size();i1++){
					MultipartFile file1 = attachement.get(i1);
					if(file1.getSize() > 0){
						File dir = new File("C:"+ File.separator +"CMS"+ File.separator +projectName+ File.separator + "commonemail" );
						if (!dir.exists())
							dir.mkdirs();
						
						
						String originalFileName = file1.getOriginalFilename();
						File newFile = new File(dir.getPath()
								+ File.separator + originalFileName);
						if (!newFile.exists()) {  
							newFile.createNewFile();  
						}
						
						OutputStream outputStream = new FileOutputStream(newFile);

						outputStream.write(file1.getBytes());
						messageBodyPart = new MimeBodyPart();
						DataSource source = new FileDataSource(newFile.getAbsolutePath().toString());
						messageBodyPart.setDataHandler(new DataHandler(source));
						messageBodyPart.setFileName(newFile.getName());
						multipart.addBodyPart(messageBodyPart);
						
					}
				}
				
				message.setContent(multipart);
				
				Transport.send(message);
				
				System.out.println("Done");
				
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				
			}
		
			
		}
}
