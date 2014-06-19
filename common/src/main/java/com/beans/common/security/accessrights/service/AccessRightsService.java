package com.beans.common.security.accessrights.service;

import java.util.List;
import java.util.Set;

import com.beans.common.security.accessrights.model.AccessRights;

public interface AccessRightsService {

	
	public AccessRights create(AccessRights accessRights);
	public AccessRights delete(int id);
	public List<AccessRights> findAll();
	public AccessRights update(AccessRights accessRights);
	public AccessRights findById(int id) throws AccessRightsNotFound;	
	public Set<AccessRights> findAllInSet();
	public List<AccessRights> findAccessRightsByAccessRight(String accessRight);
	public AccessRights findAccessRights(String accessRight);
	
}

