package com.beans.leaveapp.jbpm6.util;

import java.util.List;

import org.kie.api.task.UserGroupCallback;

import com.beans.common.security.role.service.RoleService;
import com.beans.common.security.users.service.UsersService;

public class RoleBasedUserGroupCallback implements UserGroupCallback {
	
	RoleService roleService;
	UsersService usersService;
	
	@Override
	public boolean existsGroup(String groupId) {
		try {
			if(groupId.equals("Administrators")) {
				return true;
			}
			return true;
		} catch(Exception e) {
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
			return true;
		} catch(Exception e) {
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
