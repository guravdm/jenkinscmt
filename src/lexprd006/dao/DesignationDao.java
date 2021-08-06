package lexprd006.dao;

import java.util.List;

import lexprd006.domain.Designation;

public interface DesignationDao {

	public void persist(Object obj);

	public <T> List<T> getAll(Class<T> clazz);

	public Designation getDesignationById(int desi_id);

	public void updateDesignation(Designation designation);

	public int isDesiNameExist(int desi_id, String desi_name);
}
