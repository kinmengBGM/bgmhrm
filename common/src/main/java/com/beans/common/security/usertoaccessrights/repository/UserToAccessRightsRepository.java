
package com.beans.common.security.usertoaccessrights.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.beans.common.security.usertoaccessrights.model.UserToAccessRights;

public interface UserToAccessRightsRepository extends CrudRepository<UserToAccessRights, Integer>{

	
	@Query("Select uar from UserToAccessRights uar where isDeleted = ?")
	List<UserToAccessRights> findByIsDeleted(int x);
	
	@Query("select uar from UserToAccessRights uar join uar.users u where u.id = :userId and isDeleted = 0")
	List<UserToAccessRights> findByUserId(@Param("userId") int x);	
	
	@Query("select uar from UserToAccessRights uar where userid = ? and accessRightsid = ?")
	UserToAccessRights findByAccessRight(int userId,int accessRightsId);		
}

