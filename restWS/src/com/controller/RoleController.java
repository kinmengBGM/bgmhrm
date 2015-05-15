package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beans.common.security.role.service.RoleService;

@RestController
@RequestMapping("/protected/role")
public class RoleController {

	@Autowired
	RoleService roleService;

	@RequestMapping(value="/findRoleNamesByUsername", method=RequestMethod.GET)
	List<String> findRoleNamesByUsername(@RequestParam String username) {
		return roleService.findRoleNamesByUsername(username);

	}

}
