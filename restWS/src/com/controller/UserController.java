package com.controller;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.beans.common.security.users.model.Users;
import com.beans.common.security.users.service.ChangePasswordException;
import com.beans.common.security.users.service.UsersNotFound;
import com.beans.common.security.users.service.UsersService;

@RestController
@RequestMapping(value="/protected/users")
public class UserController {

	@Autowired
	UsersService usersService;

	@RequestMapping(value="/create", method = RequestMethod.POST)
	public Users create(@RequestBody Users users) throws UsersNotFound{
		return usersService.create(users);
	}

	@RequestMapping(value="/update", method = RequestMethod.POST)
	public Users update(@RequestBody Users users) throws UsersNotFound{
		return usersService.update(users);
	}
	
	@RequestMapping(value="/delete", method = RequestMethod.GET)
	public Users delete(@RequestBody int id) throws UsersNotFound{
		return usersService.delete(id);
	}
	@RequestMapping(value="/registerUser", method = RequestMethod.POST)
	 public Users registerUser(@RequestBody Users users){
		return usersService.registerUser(users);
		 
	 }

	@RequestMapping(value = "/findAll")
	public List<Users> findAll() {
		return usersService.findAll();
	}
	
	
	@RequestMapping(value="/findById", method = RequestMethod.GET)
	public @ResponseBody Users  findById(int id) throws UsersNotFound{
		return usersService.findById(id);
		
	}

	@RequestMapping(value = "/findByUsername", method = RequestMethod.GET)
	public Users findByUsername(@RequestParam String username) throws UsersNotFound {
		return usersService.findByUsername(username);
	}
	
	@RequestMapping(value = "/findUsersByUsername", method = RequestMethod.GET)
	public List<Users> findUsersByUsername(@RequestParam String username) throws UsersNotFound {
		return usersService.findUsersByUsername(username);
	}

	
	@RequestMapping(value="/getAccessRightsMapForUser", method = RequestMethod.GET)
	public HashSet<String> getAccessRightsMapForUser(@RequestParam int id) throws UsersNotFound{
	
		return usersService.getAccessRightsMapForUser(id);
	}
	
	@RequestMapping(value="/changePassword", method= RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public void changePassword(@RequestBody String username,@RequestBody String oldPassword,@RequestBody String newPassword) throws ChangePasswordException, UsersNotFound{
		Users users = findByUsername(username);
		usersService.changePassword(users, oldPassword, newPassword);
	}
	


}
