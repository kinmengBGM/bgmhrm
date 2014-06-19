package com.beans.leaveapp.masterdata.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.beans.common.security.role.model.Role;
import com.beans.common.security.role.service.RoleService;


public class RoleListBean {
	private static final long serialVersionUID = 1L;
	private Set<Role> roleSet =  null;
	private RoleService roleService;
	
	public Set<Role> getDepartmentList() {
		if(roleSet == null) {
			roleSet = roleService.findAllInSet();
		}
		return roleSet;
	}
	public void setDepartmentList(Set<Role> roleSet) {
		this.roleSet = roleSet;
	}
	public RoleService getRoleService() {
		return roleService;
	}
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
	
}
