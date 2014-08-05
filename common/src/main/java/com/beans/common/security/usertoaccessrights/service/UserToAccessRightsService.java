package com.beans.common.security.usertoaccessrights.service;

import java.util.List;


import com.beans.common.security.accessrights.model.AccessRights;
import com.beans.common.security.usertoaccessrights.model.UserToAccessRights;

public interface UserToAccessRightsService {
	
	 UserToAccessRights create(UserToAccessRights userToAccessRights);
	 UserToAccessRights delete(int id) throws UserToAccessRightsNotFound;
	 List<UserToAccessRights> findAll();
	 UserToAccessRights update(UserToAccessRights userToAccessRights) throws UserToAccessRightsNotFound;
	 List<UserToAccessRights> findByUserId(int userId);
	 List<AccessRights> findAllAccessRights();
	 UserToAccessRights findByAccessRight(UserToAccessRights userToAccessRights);
}


