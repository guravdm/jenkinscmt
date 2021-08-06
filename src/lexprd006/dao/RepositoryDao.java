package lexprd006.dao;

import java.util.List;

public interface RepositoryDao {

	List<Object> getAllTaskForRepository(int user_id, int user_role_id, String jsonString);

}
