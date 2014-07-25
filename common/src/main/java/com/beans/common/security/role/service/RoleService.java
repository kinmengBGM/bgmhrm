package com.beans.common.security.role.service;

import java.util.List;
import java.util.Set;

import com.beans.common.security.role.model.Role;

public interface RoleService {
	
	
	 Role create(Role role);
	 Role delete(int id);
	 List<Role> findAll();
	 Role update(Role role);
	 Role findById(int id) throws RoleNotFound;
	 Role findByRole(String role) throws RoleNotFound;
	 List<Role> findRoleByRoleName(String role);
	 Set<Role> findAllInSet();
	 List<String> findRoleNamesByUsername(String username);
}

