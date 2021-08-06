package lexprd006.dao;

import java.util.List;

import javax.servlet.http.HttpSession;

import lexprd006.domain.EntityMapping;

public interface EntityMappingDao {

	public void persist(Object obj);
	public <T> List<T> getAll(Class<T> clazz);
	public <T> List<T> getJoinedAll(HttpSession session);
	public <T> List<T> getMappedUnits(int orga_id);
	public <T> List<T> getAllMapping();
	public <T> List<T> getAllMappingsReport(int user_id);
	public EntityMapping getTasksForUserUpdate(int orgaId, int unitId, int deptId);
	public void updateEntityMapping(EntityMapping entityMapping);
	public <T>List<T> getAllByID(int umap_orga_id, int umap_loca_id, int umap_dept_id);
} 
