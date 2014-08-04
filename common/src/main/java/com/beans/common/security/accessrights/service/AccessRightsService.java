package com.beans.common.security.accessrights.service;

import java.util.List;
import java.util.Set;

import com.beans.common.security.accessrights.model.AccessRights;

public interface AccessRightsService {

	
	 AccessRights create(AccessRights accessRights);
	 AccessRights delete(int id);
	 List<AccessRights> findAll();
	 AccessRights update(AccessRights accessRights);
	 AccessRights findById(int id) throws AccessRightsNotFound;	
	 Set<AccessRights> findAllInSet();
	 List<AccessRights> findAccessRightsByAccessRight(String accessRight);
	 AccessRights findAccessRights(String accessRight);
	
}

