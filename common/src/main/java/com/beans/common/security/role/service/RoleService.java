package com.beans.common.security.role.service;

import java.util.List;
import java.util.Set;

import com.beans.common.security.role.model.Role;

public interface RoleService {
	
	
	public Role create(Role role);
	public Role delete(int id);
	public List<Role> findAll();
	public Role update(Role role);
	public Role findById(int id) throws RoleNotFound;
	public Role findByRole(String role) throws RoleNotFound;
	public List<Role> findRoleByRoleName(String role);
	public Set<Role> findAllInSet();
	public List<String> findRoleNamesByUsername(String username);
}

