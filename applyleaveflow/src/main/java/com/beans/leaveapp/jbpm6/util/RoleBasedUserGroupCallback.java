package com.beans.leaveapp.jbpm6.util;

import java.util.List;

import org.kie.api.task.UserGroupCallback;

import com.beans.common.security.role.model.Role;
import com.beans.common.security.role.service.RoleNotFound;
import com.beans.common.security.role.service.RoleService;
import com.beans.common.security.users.model.Users;
import com.beans.common.security.users.service.UsersNotFound;
import com.beans.common.security.users.service.UsersService;
import com.google.inject.Inject;

public class RoleBasedUserGroupCallback implements UserGroupCallback {
	
	RoleService roleService;
	UsersService usersService;
	
	@Override
	public boolean existsGroup(String groupId) {
		try {
			if(groupId.equals("Administrators")) {
				return true;
			}
			Role role = roleService.findByRole(groupId);
			return true;
		} catch(RoleNotFound e) {
			e.printStackTrace();
			return false;
		}
		
		
	}

	@Override
	public boolean existsUser(String userId) {
		try {
			if(userId.equals("Administrator")) {
				return true;
			}
			Users users = usersService.findByUsername(userId);
			return true;
		} catch(UsersNotFound e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<String> getGroupsForUser(String userId, List<String> arg1,
			List<String> arg2) {
		List<String> groups = roleService.findRoleNamesByUsername(userId);
		
		return groups;
	}
	
	public RoleService getRoleService() {
		return roleService;
	}
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
	public UsersService getUsersService() {
		return usersService;
	}
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}
	
}
