package com.beans.common.security.accessrights.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.beans.common.security.accessrights.model.AccessRights;


public interface AccessRightsRepository extends CrudRepository<AccessRights, Integer>{

	@Query("select ar from AccessRights ar where isDeleted = ?")
	 List<AccessRights> findByIsDeleted( int x);
	
	@Query("select ar from AccessRights ar where isDeleted = 0")
	 Set<AccessRights> findAllInSet();
	
	@Query("select ar from AccessRights ar where accessRights like ? and isDeleted = 0")
	List<AccessRights> findByAccessRightLike(String accessRight);
	
	@Query("select ar from AccessRights ar where accessRights = :accessRight and isDeleted = 0")
	AccessRights findByAccessRight(@Param("accessRight")String accessRight);
	
}

