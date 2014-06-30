package com.beans.common.security.usertoaccessrights.service;

import java.util.List;


import com.beans.common.security.accessrights.model.AccessRights;
import com.beans.common.security.usertoaccessrights.model.UserToAccessRights;

public interface UserToAccessRightsService {
	
	public UserToAccessRights create(UserToAccessRights userToAccessRights);
	public UserToAccessRights delete(int id) throws UserToAccessRightsNotFound;
	public List<UserToAccessRights> findAll();
	public UserToAccessRights update(UserToAccessRights userToAccessRights) throws UserToAccessRightsNotFound;
	public List<UserToAccessRights> findByUserId(int userId);
	public List<AccessRights> findAllAccessRights();
	UserToAccessRights findByAccessRight(UserToAccessRights userToAccessRights);
}


