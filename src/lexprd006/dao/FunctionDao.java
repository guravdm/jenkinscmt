package lexprd006.dao;

import java.util.List;

import javax.servlet.http.HttpSession;

import lexprd006.domain.Department;

public interface FunctionDao {
	public void persist(Object obj);

	public <T> List<T> getAll(Class<T> clazz);

	public <T> List<Object> getAllFunction(HttpSession session);

	public Department getDepartmentById(int dept_id);

	public void updateDepartment(Department department);

	public <T> List<T> getMappedDepartments(int enti_orga_id, int enti_loca_id);

	public <T> List<T> getUnMappedDepartments(int enti_orga_id, int enti_loca_id);

	public int isDeptNameExist(int dept_id, String dept_name);

	public List<Department> checkNameIfExist(String dept_name);

	public List<Object> getDepartmentNameById(String function_name);

}
