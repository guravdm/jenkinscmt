package lexprd006.dao;

import java.util.List;


public interface UserEntityMappingDao {

	public void persist(Object obj);
	public <T> List<T> getDataForFilterUsingAccessTable();
	public <T> List<T> getUserAccessById(int user_id);
	public <T> List<T> getDistinctOrgaById(int user_id);
	public <T> List<T> getDistinctLocaById(int user_id);
	public <T> List<T> getDistinctDeptById(int user_id);
	public <T>List<T> checkTaskExist(int umap_id,int user_id);
	public void removeUserAccess(int umap_id);
	public <T>List<T> getOrgonogram();
	public <T>List<T> getUserForMappingList(int orga_id,int loca_id,int dept_id);
	public <T> List<T> getUserWithAccessForCommonEmail();
}
