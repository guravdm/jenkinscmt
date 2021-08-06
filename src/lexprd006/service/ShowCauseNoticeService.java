package lexprd006.service;

import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

public interface ShowCauseNoticeService {

	public String saveShowCauseNotice(String json,ArrayList<MultipartFile> show_cause_doc, HttpSession session);
	public String accessWiseDataFilters(String json, HttpSession session);
	public String getAllShowCauseNotice(String jsonString,HttpSession session);
	public String saveActionItem(String json,ArrayList<MultipartFile> action_document,HttpSession session);
	public String getAllActionItem(String json,HttpSession session);
	public String getShowCauseNoticeDetails(String json,HttpSession session);
	public String downloadShowCauseDocument(String json,HttpServletResponse response);
	public String updateShowCauseNotice(String json,ArrayList<MultipartFile> show_cause_doc, HttpSession session);
}
