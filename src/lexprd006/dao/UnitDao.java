package lexprd006.dao;

import java.util.List;

import javax.servlet.http.HttpSession;

import lexprd006.domain.Location;
import lexprd006.domain.Organization;

public interface UnitDao {

	public void persist(Object obj);
	//Below method not in use
	public <T> List<T> getAll(Class<T> clazz);
	public Location getLocationById(int loca_id);
	public void updateLocation(Location location);
	public int isLocaNameExist(int loca_id, String loca_name);
	public <T> List<Object> getAllUnit(HttpSession session);
	public List<Location> checkNameIfExist(String loca_name);
	public List<Object> getLocationIdByName(String unit_name);
}
