package lexprd006.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csvreader.CsvReader;

import lexprd006.dao.LegalUpdateDao;
import lexprd006.domain.Task;
import lexprd006.service.LegalUpdateService;

@Service("legalUpdateService")
public class LegalUpdateServiceImpl implements LegalUpdateService {

	@Autowired
	LegalUpdateDao legalUpdateDao;

	@SuppressWarnings("unchecked")
	@Override
	public String uploadlegalUpdates(String jsonString) {
		JSONArray jsonArray = new JSONArray();
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonString);
			JSONObject successObject = new JSONObject();
			//Code For read csv file
			
			CsvReader legalUpdates = new CsvReader(jsonObj.get("file_path").toString());  
			legalUpdates.readHeaders();

			//** Create new object for persist **//
			Task task = null;

			final Set<String> frequencies_values = new HashSet<String>(Arrays.asList("Event_Based","User_Defined","One_Time","Ongoing","Weekly","Fortnightly","Monthly","Two_Monthly","Quarterly","Half_Yearly","Yearly","Two_Yearly","Three_Yearly","Four_Yearly","Five_Yearly","Eight_Yearly","Ten_Yearly"));
			final Set<String> impact_values = new HashSet<String>(Arrays.asList("Severe","Major","Moderate","Low"));
			JSONObject wrongFrequencies = new JSONObject();
			JSONObject wrongImpacts = new JSONObject();
			while (legalUpdates.readRecord())
			{
				task = new Task();				
				if(frequencies_values.contains(legalUpdates.get("Frequency")))
				{
					if(impact_values.contains(legalUpdates.get("Impact"))){
						if(impact_values.contains(legalUpdates.get("Impact on unit"))){
							if(impact_values.contains(legalUpdates.get("Impact on organisation"))){
								task.setTask_lexcare_task_id(legalUpdates.get("Task_id"));
								task.setTask_country_id(Integer.parseInt(legalUpdates.get("Country id").toString()));
								task.setTask_country_name(legalUpdates.get("Country"));
								task.setTask_state_id(Integer.parseInt(legalUpdates.get("State id")));
								task.setTask_state_name(legalUpdates.get("State"));
								task.setTask_cat_law_id(Integer.parseInt(legalUpdates.get("Category_of_law_id")));
								task.setTask_cat_law_name(legalUpdates.get("Category of law"));
								task.setTask_legi_id(Integer.parseInt(legalUpdates.get("Legislation_id")));
								task.setTask_legi_name(legalUpdates.get("Legislation"));
								task.setTask_rule_id(Integer.parseInt(legalUpdates.get("Rule_id")));
								task.setTask_rule_name(legalUpdates.get("Rule"));
								task.setTask_activity_who(legalUpdates.get("Who"));
								task.setTask_activity_when(legalUpdates.get("When"));
								task.setTask_reference(legalUpdates.get("Reference"));
								task.setTask_activity(legalUpdates.get("Compliance Activity"));
								task.setTask_procedure(legalUpdates.get("Procedure"));
								task.setTask_more_info(legalUpdates.get("More Information"));
								task.setTask_prohibitive(legalUpdates.get("Prohibitive/Prescriptive"));
								task.setTask_frequency(legalUpdates.get("Frequency"));
								task.setTask_form_no(legalUpdates.get("Form No"));
								task.setDue_date(legalUpdates.get("Due date"));
								task.setTask_specific_due_date(legalUpdates.get("Specific due date"));
								task.setTask_task_type_of_task(legalUpdates.get("Type of task"));
								task.setTask_level(legalUpdates.get("Corporate Level or Unit Level"));
								task.setTask_excemption_criteria(legalUpdates.get("Exemption criteria"));
								task.setTask_event(legalUpdates.get("Event"));
								task.setTask_sub_event(legalUpdates.get("Sub event"));
								task.setTask_implication(legalUpdates.get("Implications"));
								task.setTask_imprisonment_duration(legalUpdates.get("Imprisonment duration"));
								task.setTask_approval_status(legalUpdates.get("Imprisonment applies to"));
								task.setTask_statutory_authority(legalUpdates.get("Statutory Authority"));
								task.setTask_fine_amount(BigDecimal.valueOf(Integer.parseInt(legalUpdates.get("Fine amount").toString())).movePointLeft(2));
								task.setTask_subsequent_amount_per_day(BigDecimal.valueOf(Integer.parseInt(legalUpdates.get("Subsequent amount per day").toString())).movePointLeft(2));
								task.setTask_impact(legalUpdates.get("Impact"));
								task.setTask_impact_on_unit(legalUpdates.get("Impact on unit"));
								task.setTask_impact_on_organization(legalUpdates.get("Impact on organisation"));
								task.setTask_interlinkage(legalUpdates.get("Interlinkage"));
								task.setTask_linked_task_id(legalUpdates.get("Linked task id"));
								task.setTask_weblinks(legalUpdates.get("Weblinks"));

								//** get current lineitem is exist or not **//

								int existTaskId = legalUpdateDao.getLexcareTaskExist(legalUpdates.get("Task_id"));
								if(existTaskId > 0){
									task.setTask_id(existTaskId);
									legalUpdateDao.update_legalUpdates(task);
								}else{
									legalUpdateDao.uploadlegalUpdates(task);
								}
							}else{
								wrongImpacts.put(legalUpdates.get("Task_id"),"Please check impact on organisation");
							}
						}else{
							wrongImpacts.put(legalUpdates.get("Task_id"),"Please check impact on unit");
						}
					}else{
						wrongImpacts.put(legalUpdates.get("Task_id"),"Please check task impact");
					}
				}else{
					wrongFrequencies.put(legalUpdates.get("Task_id"), "Please check task frequency");
				}
			}  	   
			legalUpdates.close();
			//End Reading csv file

			//** To check all frequencies are correct **//
			if(wrongFrequencies.keySet().size() > 0){
				successObject.put("frequencies_response", "Opps Error..! May be wrong frequiencies please check..!");
				successObject.put("wrongFrequencies",wrongFrequencies);
			}else if(wrongImpacts.keySet().size() > 0){
				successObject.put("frequencies_response", "Opps Error..! May be wrong impacts please check..!");
				successObject.put("wrongImpacts", wrongImpacts);
			}else{
				successObject.put("successMessage", "Well done all task uploaded successfully");
			}

			jsonArray.add(successObject);
			return jsonArray.toJSONString();
		} catch (Exception e) {
			JSONObject errorObject = new JSONObject();
			errorObject.put("errorMessage", "Opps..!");
			jsonArray.add(errorObject);
			e.printStackTrace();
			return jsonArray.toJSONString();
		}
	}
}
