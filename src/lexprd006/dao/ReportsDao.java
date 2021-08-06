package lexprd006.dao;

import java.util.List;

import javax.servlet.http.HttpSession;

public interface ReportsDao {
	
	public <T> List<T> generateReports(String jsonString , HttpSession session);
	public <T> List<T> generateSubTaskReports(String jsonString , HttpSession session);
	public <T> List<T> automatedMonthlyReports(String jsonString);

}
