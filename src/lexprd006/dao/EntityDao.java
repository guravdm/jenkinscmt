package lexprd006.dao;

import java.util.List;

import javax.servlet.http.HttpSession;

import lexprd006.domain.Organization;

public interface EntityDao {

	public void persist(Organization obj);
	public <T> List<T> getAll(Class<T> clazz);
	public <T> List<T> getAllForAddingEntity(Class<T> clazz);
	public <T> List<T> getJoinedAll(HttpSession httpSession, String requestFromMethod);
	public Organization getOrganizationById(int orga_id);
	public void updateOrganization(Organization organization);
	public int approveDisapproveOrg(int org_id , int orga_status);
	public int enableDisableOrg(int org_id , int orga_status);
	public int isOrgaNameExist(int orga_id, String orga_name);
	public int getOrganizationIdByName(String eId);
	public List<Organization> checkDuplicateName(String orga_name);
	public List<Object> getOrganizationIdByOrgaName(String entity_name);

}
