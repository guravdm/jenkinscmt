package lexprd006.dao;

import java.util.List;

import javax.servlet.http.HttpSession;

import lexprd006.domain.ShowCauseDocuments;
import lexprd006.domain.ShowCauseNotice;
import lexprd006.domain.ShowCauseNoticeActionItem;

public interface ShowCauseNoticeDao {

	public int saveShowCauseNotice(ShowCauseNotice causeNotice);
	public <T>List<T> getFiltersForShowCauseNotice(HttpSession session);
	public int getLastGeneratedValueForShowCauseDocument(int related_id,String related_to);
	public void saveDocument(Object object);
	public <T>List<T> getShowCauseNoticeList(HttpSession session);
	public int saveActionItem(ShowCauseNoticeActionItem actionItem);
	public ShowCauseNotice getNoticeDetailsById(int scau_id);
	public void merge(Object object);
	public <T>List<T> getShowCauseNoticeDetailsById(int scau_id);
	public List<ShowCauseNoticeActionItem> getActionItemDetailsById(int scau_id );
	public List<ShowCauseDocuments> getDocuments(int id,String type);
	public ShowCauseDocuments getDocDetails(int doc_id);
}
