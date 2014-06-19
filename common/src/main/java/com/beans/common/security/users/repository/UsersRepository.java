
package com.beans.common.security.users.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.beans.common.security.users.model.Users;


public interface UsersRepository extends CrudRepository<Users, Integer>{

	@Query("select u from Users u where enabled = ?")
	 List<Users> findByIsEnabled(boolean isEnabled);
	
	@Query("select u from Users u where id = ?")
	List<Users> findById(int id);
	
	@Query("select u from Users u where username=?")
	Users findByUsername(String username);
	
	@Query("select u from Users u where username like ? and enabled = 1")
	List<Users> findByUsernameLike(String username);
}


